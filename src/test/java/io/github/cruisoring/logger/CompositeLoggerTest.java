package io.github.cruisoring.logger;

import io.github.cruisoring.Revokable;
import org.junit.Test;

import static io.github.cruisoring.Asserts.assertTrue;

public class CompositeLoggerTest {

    @Test
    public void canLog() {
        InMemoryLogger logger1 = new InMemoryLogger(LogLevel.verbose);
        InMemoryLogger logger2 = new InMemoryLogger(LogLevel.info);

        final CompositeLogger compositeLogger = new CompositeLogger(logger1, logger2, Logger.getDefault());

        ILogger old = null;
        try (Revokable<ILogger> oldLogger = Logger.useInScope(compositeLogger)) {
            Logger.V("verbose shall be logged by logger1");
            Logger.D("debug shall be loggered by both logger1 and oldLogger");
            Logger.I("info shall be logged by all 3 ILogger instances");

            String logs1 = logger1.getHistory();
            String logs2 = logger2.getHistory();
            assertTrue(logs1.contains("verbose shall be logged by logger1") && !logs2.contains("verbose shall be logged by logger1"));
            assertTrue(logs1.contains("debug shall be loggered by both logger1 and oldLogger") && !logs2.contains("debug shall be loggered by both logger1 and oldLogger"));
            assertTrue(logs1.contains("info shall be logged by all 3 ILogger instances") && logs2.contains("info shall be logged by all 3 ILogger instances"));

            old = oldLogger.getValue();
            assertTrue(Logger.Default != old);
        } catch (Exception ignored) {
        }

        assertTrue(old == Logger.getDefault());

        try (Revokable<ILogger> oldLogger = Logger.useInScope(new CompositeLogger(LogLevel.error, logger1, logger2, Logger.getDefault()))) {
            Logger.V("verbose2 shall be logged by logger1");
            Logger.D("debug2 shall be loggered by both logger1 and oldLogger");
            Logger.I("info2 shall be logged by all 3 ILogger instances");
            Logger.E("error shall be logged by all 3 ILogger instances");

            String logs1 = logger1.getHistory();
            String logs2 = logger2.getHistory();
            assertTrue(!logs1.contains("verbose2 shall be logged by logger1") && !logs2.contains("verbose2 shall be logged by logger1"));
            assertTrue(!logs1.contains("debug2 shall be loggered by both logger1 and oldLogger") && !logs2.contains("debug2 shall be loggered by both logger1 and oldLogger"));
            assertTrue(!logs1.contains("info2 shall be logged by all 3 ILogger instances") && !logs2.contains("info2 shall be logged by all 3 ILogger instances"));
            assertTrue(logs1.contains("error shall be logged by all 3 ILogger instances") && logs2.contains("error shall be logged by all 3 ILogger instances"));

            old = oldLogger.getValue();
            assertTrue(Logger.Default != old);
        } catch (Exception ignored) {
        }

        assertTrue(old == Logger.getDefault());
    }

}