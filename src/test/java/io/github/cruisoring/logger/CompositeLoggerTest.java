package io.github.cruisoring.logger;

import io.github.cruisoring.Asserts;
import io.github.cruisoring.Revokable;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CompositeLoggerTest {

    static Revokable<LogLevel> logLevelRevokable;

    @BeforeClass
    public static void setup() {
        logLevelRevokable = Logger.setLevelInScope(LogLevel.verbose);
    }

    @AfterClass
    public static void teardown() {
        logLevelRevokable.close();
    }

    @Test
    public void canLog() {
        InMemoryLogger logger1 = new InMemoryLogger(LogLevel.verbose);
        InMemoryLogger logger2 = new InMemoryLogger(LogLevel.info);

        final CompositeLogger compositeLogger = new CompositeLogger(logger1, logger2, Logger.getDefault());

        ILogger old = null;
        try (
                Revokable<ILogger> oldLogger = Logger.useInScope(compositeLogger)
        ) {
            old = oldLogger.getOriginalSetting();

            Logger.V("verbose shall be logged by logger1");
            Logger.D("debug shall be loggered by both logger1 and oldLogger");
            Logger.I("info shall be logged by all 3 ILogger instances");

            Asserts.assertAllTrue(Logger.Default != old);
        }

        String logs1 = logger1.getHistory();
        String logs2 = logger2.getHistory();
        Asserts.assertAllTrue(logs1.contains("verbose shall be logged by logger1"),
                !logs2.contains("verbose shall be logged by logger1"));
        Asserts.assertAllTrue(logs1.contains("debug shall be loggered by both logger1 and oldLogger"),
                !logs2.contains("debug shall be loggered by both logger1 and oldLogger"));
        Asserts.assertAllTrue(logs1.contains("info shall be logged by all 3 ILogger instances"),
                logs2.contains("info shall be logged by all 3 ILogger instances"));

        Asserts.assertAllTrue(old == Logger.getDefault());

        try (Revokable<ILogger> oldLogger = Logger.useInScope(new CompositeLogger(LogLevel.error, logger1, logger2, Logger.getDefault()))) {
            Logger.V("verbose2 shall be logged by logger1");
            Logger.D("debug2 shall be loggered by both logger1 and oldLogger");
            Logger.I("info2 shall be logged by all 3 ILogger instances");
            Logger.E("error shall be logged by all 3 ILogger instances");
            old = oldLogger.getOriginalSetting();
            Asserts.assertAllTrue(Logger.Default != old);
        } catch (Exception ignored) {
        }

        Asserts.assertAllTrue(old == Logger.getDefault());

        logs1 = logger1.getHistory();
        logs2 = logger2.getHistory();
        Asserts.assertAllFalse(logs1.contains("verbose2 shall be logged by logger1"),
                logs2.contains("verbose2 shall be logged by logger1"));
        Asserts.assertAllFalse(logs1.contains("debug2 shall be loggered by both logger1 and oldLogger"),
                logs2.contains("debug2 shall be loggered by both logger1 and oldLogger"));
        Asserts.assertAllFalse(logs1.contains("info2 shall be logged by all 3 ILogger instances"),
                logs2.contains("info2 shall be logged by all 3 ILogger instances"));
        Asserts.assertAllTrue(logs1.contains("error shall be logged by all 3 ILogger instances"),
                logs2.contains("error shall be logged by all 3 ILogger instances"));

    }

}