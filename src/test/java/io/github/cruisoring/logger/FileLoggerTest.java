package io.github.cruisoring.logger;

import io.github.cruisoring.Revokable;
import org.junit.Test;

import static io.github.cruisoring.Asserts.*;

public class FileLoggerTest {

    @Test
    public void getPath() {
        Logger.D(FileLogger.getFile(".").toString());
        Logger.D(FileLogger.getFile("..").toString());
        Logger.D(FileLogger.getFile("..\\..\\test.log").toString());
    }

    @Test
    public void canLog() {
        FileLogger fileLogger = new FileLogger("..\\test.log", LogLevel.info);
        LogLevel currentLevel = Logger.getGlobalLogLevel();
        try (Revokable<LogLevel> level = Logger.setLevelInScope(LogLevel.none)) {
            Logger.setGlobalLevel(LogLevel.info);
            assertTrue(fileLogger.canLog(LogLevel.info) && fileLogger.canLog(LogLevel.warning) && fileLogger.canLog(LogLevel.warning));
            assertFalse(fileLogger.canLog(LogLevel.verbose) || fileLogger.canLog(LogLevel.debug));
        } catch (Exception ignored) {
        }
        assertEquals(currentLevel, Logger.getGlobalLogLevel());

        try (Revokable<LogLevel> level = Logger.setLevelInScope(LogLevel.none)) {
            assertFalse(fileLogger.canLog(LogLevel.verbose) || fileLogger.canLog(LogLevel.info) || fileLogger.canLog(LogLevel.error));
        } catch (Exception ignored) {
        }
    }

    @Test
    public void record() {
        FileLogger fileLogger = new FileLogger("..\\test.log", LogLevel.info);
        assertTrue(fileLogger.canLog(LogLevel.warning));
        fileLogger.info("Test info");
        fileLogger.warning("Test warning");
        fileLogger.error("Error message");
        fileLogger.debug("Debug you shall not see");
        fileLogger.debug("verbose you shall not see");
    }
}