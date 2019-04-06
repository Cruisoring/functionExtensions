package io.github.cruisoring.logger;

import io.github.cruisoring.AutoCloseableObject;
import io.github.cruisoring.Lazy;
import io.github.cruisoring.TypeHelper;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoggerTest {

    int a(){ throw new IllegalStateException("for test");}
    boolean b(int i){ return i > a();}
    void c(){ b(3);}
    int testD(){ c(); return 0; }
    void testE(){ testD();}

    @Test
    public void testCompareLogLevel(){
        assertTrue(LogLevel.none.compareTo(LogLevel.error) > 0 && LogLevel.none.compareTo(LogLevel.verbose)>0);
        assertTrue(LogLevel.verbose.compareTo(LogLevel.none)<0 && LogLevel.error.compareTo(LogLevel.none)<0);

        assertTrue(LogLevel.verbose.compareTo(LogLevel.verbose)==0 && LogLevel.verbose.compareTo(LogLevel.debug)<0);
        assertTrue(LogLevel.error.compareTo(LogLevel.error)>= 0 && LogLevel.error.compareTo(LogLevel.verbose)>=0);
    }

    @Test
    public void testCanLog(){
        LogLevel defaultLevel = Logger.getGlobalLogLevel();
        try {
            ILogger defaultLogger = Logger.Default;

            Logger.setGlobalLevel(LogLevel.none);
            assertFalse(Logger.Default.canLog(defaultLogger.getMinLevel()));
            assertFalse(Logger.Default.canLog(LogLevel.error));

            Logger.setGlobalLevel(LogLevel.verbose);
            Logger.V("The minLevel of Logger.Default is %s", defaultLogger.getMinLevel());
            assertTrue(Logger.Default.canLog(defaultLogger.getMinLevel()));
            assertTrue(Logger.Default.canLog(LogLevel.error));

            Logger newLogger = new Logger(System.err::println, LogLevel.warning);
            assertTrue(newLogger.canLog(LogLevel.warning) && newLogger.canLog(LogLevel.error));
            assertFalse(newLogger.canLog(LogLevel.verbose) || newLogger.canLog(LogLevel.debug) || newLogger.canLog(LogLevel.info));

            Logger.setGlobalLevel(LogLevel.none);
            assertFalse(newLogger.canLog(LogLevel.verbose) || newLogger.canLog(LogLevel.debug) || newLogger.canLog(LogLevel.info)
                    || newLogger.canLog(LogLevel.warning) || newLogger.canLog(LogLevel.error));

        }finally {
            Logger.setGlobalLevel(defaultLevel);
        }
    }


    @Test
    public void testSetGlobalLogLeveInScope(){
        Logger.W("This is a warning you shall see.");

        try(AutoCloseableObject closeable = Logger.setLevelInScope(LogLevel.none)){
            Logger.V("But you shall not be able to see this verbose log");
            Logger.E("You shall not be able to see this error log");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.W("Now you shall see this message logged.");
    }


    @Test
    public void testMeasureSupplier() {
        Integer integer = Logger.M(()-> {
            Thread.sleep(100);
            return -1;
        });

        integer = Logger.M(()-> {
            Thread.sleep(100);
            return testD();
        });
    }

    @Test
    public void testMeasureRunnable() {
        Logger.M(()-> {
            Thread.sleep(100);
        });

        Logger.M(()-> {
            Thread.sleep(100);
            testE();
        });
    }

    @Test
    public void testMeasureSupplier_WithLabel() {
        Integer integer = Logger.M(()-> {
            Thread.sleep(100);
            return testD();
        }, "testD()");
    }

    @Test
    public void testMeasureRunnable_WithLabel() {
        Logger.M(()-> {
            Thread.sleep(100);
            testE();
        }, "testE() on %s", TypeHelper.asString(LocalDate.now()));

        Logger.M(() -> Thread.sleep(1),
                "testNothing on %s for %d", TypeHelper.asString(LocalDate.now()), 3);
    }

    @Test
    public void testVerboseLogging() {
        Logger.V("Today is %s, and result is %s", LocalDateTime.now(), true);

        Logger.V("Sth wrong with %s, and result is %s", LocalDateTime.now(), null);

        Logger.V("Sth wrong with the format %s, and result is %s", LocalDateTime.now());
    }

    @Test
    public void testDebugLogging() {
        Logger.D("Today is %s, and result is %s", LocalDateTime.now(), true);

        Logger.D("Sth wrong with %s, and result is %s", LocalDateTime.now(), null);

        Logger.D("Sth wrong with the format %s, and result is %s", LocalDateTime.now());
    }

    @Test
    public void testInfoLogging() {
        Logger.I("Today is %s, and result is %s", LocalDateTime.now(), true);

        Logger.I("Sth wrong with %s, and result is %s", LocalDateTime.now(), null);

        Logger.I("Sth wrong with the format %s, and result is %s", LocalDateTime.now());
    }

    @Test
    public void testWarningLogging() {
        Logger.W("Today is %s, and result is %s", LocalDateTime.now(), true);

        Logger.W("Sth wrong with %s, and result is %s", LocalDateTime.now(), null);

        Logger.W("Sth wrong with the format %s, and result is %s", LocalDateTime.now());
    }

    @Test
    public void testErrorLogging() {
        Logger.E("Today is %s, and result is %s", LocalDateTime.now(), true);

        Logger.E("Sth wrong with %s, and result is %s", LocalDateTime.now(), null);

        Logger.E("Sth wrong with the format %s, and result is %s", LocalDateTime.now());
    }

    @Test
    public void testLogVerbose() {
        try{
            testE();
        }catch (Exception e){
            Logger.V(e);
        }
    }

    @Test
    public void testLogDebug() {
        try{
            testE();
        }catch (Exception e){
            Logger.D(e);
        }
    }

    @Test
    public void testLogInfo() {
        try{
            testE();
        }catch (Exception e){
            Logger.I(e);
        }
    }

    @Test
    public void testLogWarning() {
        try{
            testE();
        }catch (Exception e){
            Logger.W(e);
        }
    }

    @Test
    public void testLogError() {
        try{
            testE();
        }catch (Exception e){
            Logger.E(e);
        }
    }
}