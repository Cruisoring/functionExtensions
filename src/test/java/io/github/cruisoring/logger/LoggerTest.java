package io.github.cruisoring.logger;

import org.junit.Test;

import java.time.LocalDateTime;

public class LoggerTest {

    @Test
    public void testMeasureSupplier() {
        Integer integer = Logger.M(()-> {
            Thread.sleep(100);
            return -1;
        });

        integer = Logger.M(()-> {
            Thread.sleep(100);
            throw new IllegalStateException();
        });
    }

    @Test
    public void testMeasureRunnable() {
        Logger.M(()-> {
            Thread.sleep(100);
        });

        Logger.M(()-> {
            Thread.sleep(100);
            throw new IllegalStateException();
        });
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

    int a(){ throw new IllegalStateException();}
    boolean b(int i){ return i > a();}
    void c(){ b(3);}
    int testD(){ c(); return 0; }
    void testE(){ testD();}

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