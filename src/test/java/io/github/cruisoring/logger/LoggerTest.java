package io.github.cruisoring.logger;

import io.github.cruisoring.Asserts;
import io.github.cruisoring.Revokable;
import io.github.cruisoring.TypeHelper;
import io.github.cruisoring.TypedList;
import io.github.cruisoring.utility.DateTimeHelper;
import io.github.cruisoring.utility.StringHelper;
import org.junit.Test;
import sun.rmi.runtime.Log;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

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
    public void testLoggingOnOff_withAllLevels(){
        LogLevel globalLogLevel = Logger.getGlobalLogLevel();
        Logger.V("This is a message of %s that shall not be displayed when Logger.getGlobalLogLevel()=%s", LogLevel.verbose, globalLogLevel);
        Logger.D("This is a message of %s that shall be displayed when Logger.getGlobalLogLevel()=%s, and the arguments shown in %s", LogLevel.debug, globalLogLevel, "Blue");
        Logger.I("This is a success message of %s would be displayed when globalLogLevel=%s, you shall see the arguments shown in %s color with 'success' included.", LogLevel.info, globalLogLevel, "Green");
        Logger.W("Message of %s shown up when globalLogLevel=%s, with other parameter of %s with keyword 'fail' presented", LogLevel.warning, globalLogLevel, "Red");
        Logger.E("This is a malformatted message with placeholder '%d' for a string, it would still print out somthing with all parameters displayed for trouble-shooting.\n", LogLevel.error, "wrong", globalLogLevel, "\r\n");

        //Set Logger.GlobalLogLevel to LogLevel.verbose to turn on most detail logging
        Logger.I("To turn ON logging of all levels, calling %s", "Logger.setGlobalLogLevel(LogLevel.verbose)");
        Logger.setGlobalLevel(LogLevel.verbose);
        Logger.V("Now you can pass the message of %s when Logger.getGlobalLogLevel()=%s\n", LogLevel.verbose, Logger.getGlobalLogLevel());

        //Set Logger.GlobalLogLevel to LogLevel.waring to show only warning and error messages
        Logger.setGlobalLevel(LogLevel.warning);
        Logger.I("No LogLevel.info message to be displayed");
        Logger.W("Set %s to %s to show only %s messages by calling %s", "Logger.GlobalLogLevel", LogLevel.warning, "warning and error", "Logger.setGlobalLevel(LogLevel.warning)");
        Logger.E("Of course you can see error messages with LogLevel.%s\n", LogLevel.error);

        //Set Logger.GlobalLogLevel to LogLevel.none to turn off logging
        Logger.I("To turn OFF logging of all levels, calling %s\n", "Logger.setGlobalLogLevel(LogLevel.none)");
        Logger.setGlobalLevel(LogLevel.none);
        Logger.E("Now you shall not see any message, even if it is highest LogLevel %s", LogLevel.error);

        //Restore Logger.GlobalLogLevel to default LogLevel.debug
        Logger.setGlobalLevel(globalLogLevel);
        Logger.D("Now with Logger.GlobalLogLevel restored to %s successfully, %s messages and above shall be logged.", globalLogLevel, LogLevel.debug);
    }

    void recursiveToThrow(int count){
        if(--count == 0){
            try {
                count = count / count;
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        } else {
            recursiveToThrow(count);
        }
    }

    @Test
    public void testLoggingException(){
        LogLevel globalLogLevel = Logger.setGlobalLevel(LogLevel.verbose);
        try {
            recursiveToThrow(100);
        } catch (Exception e) {
            Logger.V(e);
            Logger.D(e);
            Logger.I(e);
            Logger.W(e);
            Logger.E(e);
        }
        Logger.setGlobalLevel(globalLogLevel);
    }

    @Test
    public void sampleToChangeGlobalLogLevel(){
        String logLevelString = System.getProperty("LogLevel");

        if(logLevelString != null) {
            LogLevel runtimeLogLevel = StringHelper.parse(logLevelString, LogLevel.class, LogLevel.debug);
            Logger.W("Now it is about to change %s from %s to %s.", "Logger.GlobalLogLevel", Logger.getGlobalLogLevel(), runtimeLogLevel);
            Logger.setGlobalLevel(runtimeLogLevel);
        }

        Logger.V("%s message logged only when '%s' == '%s'", LogLevel.verbose, logLevelString, LogLevel.verbose);
        Logger.I("%s message logged only when '%s' < '%s'", LogLevel.info, logLevelString, LogLevel.info);
        Logger.E("%s message logged only when '%s' != '%s'", LogLevel.error, logLevelString, LogLevel.none);
    }

    @Test
    public void testScopedLogLevelAndLogger(){
        ListLogger listLogger = new ListLogger();
        Logger.V("%s message shall not be dispalyed when Logger.GlobalLogLevel=%s", LogLevel.verbose, Logger.getGlobalLogLevel());
        try (
                Revokable<ILogger> loggerRevokable = Logger.useInScope(listLogger);
                Revokable<LogLevel> levelRevokable = Logger.setLevelInScope(LogLevel.verbose);
                ){
            Logger.V("%s message shall be saved to listLogger when Logger.GlobalLogLevel=%s", LogLevel.verbose, Logger.getGlobalLogLevel());
        }

        Logger.V("%s message shall not be dispalyed when Logger.GlobalLogLevel=%s", LogLevel.verbose, Logger.getGlobalLogLevel());
        Logger.D("Now Logger.GlobalLogLevel is restored to %s", Logger.getGlobalLogLevel());
        Logger.I("The content of listLogger: %s", TypeHelper.deepToString(listLogger.getMessages()));
    }

    @Test
    public void testCompareLogLevel() {
        Asserts.assertAllTrue(
            LogLevel.none.compareTo(LogLevel.error) > 0,
            LogLevel.none.compareTo(LogLevel.verbose) > 0,
            LogLevel.verbose.compareTo(LogLevel.none) < 0,
            LogLevel.error.compareTo(LogLevel.none) < 0,
            LogLevel.verbose.compareTo(LogLevel.verbose) == 0,
            LogLevel.verbose.compareTo(LogLevel.debug) < 0,
            LogLevel.error.compareTo(LogLevel.error) >= 0,
            LogLevel.error.compareTo(LogLevel.verbose) >= 0
        );
    }

    @Test
    public void testCanLog() {
        LogLevel defaultLevel = Logger.getGlobalLogLevel();
        try {
            ILogger defaultLogger = Logger.Default;

            Logger.setGlobalLevel(LogLevel.none);
            Asserts.assertAllFalse(
                Logger.Default.canLog(defaultLogger.getMinLevel()), Logger.Default.canLog(LogLevel.error));

            Logger.setGlobalLevel(LogLevel.verbose);
            Logger.V("The minLevel of Logger.Default is %s", defaultLogger.getMinLevel());
            Asserts.assertAllTrue(Logger.Default.canLog(defaultLogger.getMinLevel()), Logger.Default.canLog(LogLevel.error));

            Logger newLogger = new Logger(System.err::println, LogLevel.warning);
            Asserts.assertAllTrue(newLogger.canLog(LogLevel.warning), newLogger.canLog(LogLevel.error));
            Asserts.assertAllFalse(newLogger.canLog(LogLevel.verbose), newLogger.canLog(LogLevel.debug), newLogger.canLog(LogLevel.info));

            Logger.setGlobalLevel(LogLevel.none);
            Asserts.assertAllFalse(newLogger.canLog(LogLevel.verbose), newLogger.canLog(LogLevel.debug), newLogger.canLog(LogLevel.info),
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

        try (Revokable closeable = Logger.setLevelInScope(LogLevel.warning)) {
            Logger.I("But you shall not be able to see this %s log", LogLevel.info);
            Logger.E("You shall not be able to see this %s log", LogLevel.error);
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
    public void testMeasurement_demoAllTypes(){
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int next = random.nextInt(10);
            int v = Logger.M(Measurement.start(), withDelay(next, next));
            String nextString = Logger.M(Measurement.start("Get string"), withDelay(String.valueOf(next), next + 5), LogLevel.info);
            Logger.M(Measurement.start("justSleep"), ()->justSleep(random.nextInt(15)+10), LogLevel.warning);
            next = random.nextInt(20);

            nextString = Logger.M(Measurement.start("Get string"), withDelay(String.valueOf(next), next + 15), LogLevel.debug);
        }

        Logger.D(Measurement.getMeasurements("Get string").toString());

        Measurement.purge(LogLevel.info);
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

        Logger.M(Measurement.start("testNothing on %s for %d", DateTimeHelper.asString(LocalDate.now(), "dd-MM"), 3),
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