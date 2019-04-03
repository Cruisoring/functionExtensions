package io.github.cruisoring.logger;

import io.github.cruisoring.TypeHelper;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LoggerTest {

    int a(){ throw new IllegalStateException("for test");}
    boolean b(int i){ return i > a();}
    void c(){ b(3);}
    int testD(){ c(); return 0; }
    void testE(){ testD();}

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