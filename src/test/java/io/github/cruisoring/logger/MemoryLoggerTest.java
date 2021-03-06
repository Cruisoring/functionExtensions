package io.github.cruisoring.logger;

import io.github.cruisoring.Asserts;
import org.junit.Test;

public class MemoryLoggerTest {

    @Test
    public void testInheritedMethods() {
        InMemoryLogger memoryLogger = new InMemoryLogger(LogLevel.info);

        memoryLogger.verbose("verbose log shall not be recorded");
        memoryLogger.debug("debug log shall not be recorded either");
        memoryLogger.info("info log shall be recorded either");
        memoryLogger.warning("You shall also see warning logs");
        memoryLogger.error("You shall also see error logs");

        String history = memoryLogger.getHistory();
        Logger.D("History of memoryLogger:\n%s", history);
        Asserts.assertAllFalse(history.contains("verbose log shall not be recorded"),
                history.contains("debug log shall not be recorded either"));
        Asserts.assertAllTrue(history.contains("info log shall be recorded either")
                && history.contains("You shall also see warning logs")
                && history.contains("You shall also see error logs")
        );
    }


}