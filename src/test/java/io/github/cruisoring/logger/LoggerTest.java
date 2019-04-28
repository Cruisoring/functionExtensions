package io.github.cruisoring.logger;

import io.github.cruisoring.Revokable;
import io.github.cruisoring.TypeHelper;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

import static io.github.cruisoring.Asserts.assertFalse;
import static io.github.cruisoring.Asserts.assertTrue;
public class LoggerTest {

    int a() {
        throw new IllegalStateException("for test");
    }

    boolean b(int i) {
        return i > a();
    }

    void c() {
        b(3);
    }

    int testD() {
        c();
        return 0;
    }

    void testE() {
        testD();
    }

    @Test
    public void testCompareLogLevel() {
        assertTrue(LogLevel.none.compareTo(LogLevel.error) > 0, LogLevel.none.compareTo(LogLevel.verbose) > 0);
        assertTrue(LogLevel.verbose.compareTo(LogLevel.none) < 0, LogLevel.error.compareTo(LogLevel.none) < 0);

        assertTrue(LogLevel.verbose.compareTo(LogLevel.verbose) == 0, LogLevel.verbose.compareTo(LogLevel.debug) < 0);
        assertTrue(LogLevel.error.compareTo(LogLevel.error) >= 0, LogLevel.error.compareTo(LogLevel.verbose) >= 0);
    }

    @Test
    public void testCanLog() {
        LogLevel defaultLevel = Logger.getGlobalLogLevel();
        try {
            ILogger defaultLogger = Logger.Default;

            Logger.setGlobalLevel(LogLevel.none);
            assertFalse(Logger.Default.canLog(defaultLogger.getMinLevel()), Logger.Default.canLog(LogLevel.error));

            Logger.setGlobalLevel(LogLevel.verbose);
            Logger.V("The minLevel of Logger.Default is %s", defaultLogger.getMinLevel());
            assertTrue(Logger.Default.canLog(defaultLogger.getMinLevel()), Logger.Default.canLog(LogLevel.error));

            Logger newLogger = new Logger(System.err::println, LogLevel.warning);
            assertTrue(newLogger.canLog(LogLevel.warning), newLogger.canLog(LogLevel.error));
            assertFalse(newLogger.canLog(LogLevel.verbose), newLogger.canLog(LogLevel.debug), newLogger.canLog(LogLevel.info));

            Logger.setGlobalLevel(LogLevel.none);
            assertFalse(newLogger.canLog(LogLevel.verbose), newLogger.canLog(LogLevel.debug), newLogger.canLog(LogLevel.info),
                    newLogger.canLog(LogLevel.warning), newLogger.canLog(LogLevel.error));

        } finally {
            Logger.setGlobalLevel(defaultLevel);
        }
    }

    @Test
    public void testSetGlobalLogLeveInScope() {
        Logger.W("This is a warning you shall see.");

        try (Revokable closeable = Logger.setLevelInScope(LogLevel.none)) {
            Logger.V("But you shall not be able to see this verbose log");
            Logger.E("You shall not be able to see this error log");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.W("Now you shall see this message logged.");
    }

    <T> T withDelay(T value, long mills) {
        justSleep(mills);
        return value;
    }

    void justSleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (Exception e) {
        }
    }

    @Test
    public void testMeasurement_withValueReturned() {
        Logger logger = new ConsoleLogger(System.out::println, LogLevel.debug);

        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int v = logger.measure(Measurement.start(), withDelay(99, random.nextInt(20) + 90), LogLevel.info);
            String s = logger.measure(Measurement.start("Get String"), withDelay("s", random.nextInt(20) + 190), LogLevel.debug);
        }

        Map<String, String> all = Measurement.getAllSummary();
        for (String label : all.keySet()) {
            Logger.I("%s: %s", label, all.get(label));
        }
    }

    @Test
    public void testMeasurement_withoutValueReturned() {
        Logger logger = new ConsoleLogger(System.out::println, LogLevel.debug);

        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            logger.measure(Measurement.start(), () -> justSleep(random.nextInt(20) + 90), LogLevel.error);
            logger.measure(Measurement.start("Sleep 200ms"), () -> justSleep(random.nextInt(20) + 190), LogLevel.debug);
        }

        Map<String, String> all = Measurement.getAllSummary();
        for (String label : all.keySet()) {
            Logger.I("%s: %s", label, all.get(label));
        }
    }

    @Test
    public void testMeasureSupplier() {
        Integer integer = Logger.M(Measurement.start(), () -> {
            Thread.sleep(100);
            return -1;
        }, LogLevel.info);

        integer = Logger.M(Measurement.start(), () -> {
            Thread.sleep(100);
            return testD();
        }, LogLevel.warning);
    }

    @Test
    public void testMeasureRunnable() {
        Logger.M(Measurement.start(), () -> {
            Thread.sleep(100);
        }, LogLevel.info);

        Logger.M(Measurement.start(), () -> {
            Thread.sleep(100);
            testE();
        }, LogLevel.debug);
    }

    @Test
    public void testMeasureSupplier_WithLabel() {
        Integer integer = Logger.M(Measurement.start("testD()"), () -> {
            Thread.sleep(100);
            return testD();
        }, LogLevel.info);
    }

    @Test
    public void testMeasureRunnable_WithLabel() {
        Logger.M(Measurement.start("testE() on %s"), () -> {
            Thread.sleep(100);
            testE();
        }, LogLevel.debug);

        Logger.M(Measurement.start("testNothing on %s for %d", TypeHelper.asString(LocalDate.now(), "dd-MM"), 3),
                () -> Thread.sleep(1), LogLevel.info);
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
        try {
            testE();
        } catch (Exception e) {
            Logger.V(e);
        }
    }

    @Test
    public void testLogDebug() {
        try {
            testE();
        } catch (Exception e) {
            Logger.D(e);
        }
    }

    @Test
    public void testLogInfo() {
        try {
            testE();
        } catch (Exception e) {
            Logger.I(e);
        }
    }

    @Test
    public void testLogWarning() {
        try {
            testE();
        } catch (Exception e) {
            Logger.W(e);
        }
    }

    @Test
    public void testLogError() {
        try {
            testE();
        } catch (Exception e) {
            Logger.E(e);
        }
    }
}