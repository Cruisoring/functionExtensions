package io.github.cruisoring.logger;

import static io.github.cruisoring.Asserts.checkNoneNulls;

/**
 * Logger using {@code StringBuilder} as InMemory logger.
 */
public class InMemoryLogger extends Logger {
    private final StringBuilder stringBuilder;

    public InMemoryLogger() {
        this(new StringBuilder(), LogLevel.verbose);
    }

    public InMemoryLogger(LogLevel minLevel) {
        this(new StringBuilder(), minLevel);
    }

    public InMemoryLogger(final StringBuilder sb, LogLevel minLevel) {
        super(log -> checkNoneNulls(sb).append(log + "\n"), minLevel);
        stringBuilder = sb;
    }

    public InMemoryLogger(final StringBuilder sb) {
        this(sb, LogLevel.verbose);
    }

    /**
     * Returns the {@code StringBuilder} instance backing this {@code InMemoryLogger}
     * @return the {@code StringBuilder} instance keeping logged messages that shall not be used to perform write operations.
     */
    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }

    /**
     * Retrieve the messages that have been logged.
     *
     * @return the String of all the messages.
     */
    public String getHistory() {
        return stringBuilder.toString();
    }
}
