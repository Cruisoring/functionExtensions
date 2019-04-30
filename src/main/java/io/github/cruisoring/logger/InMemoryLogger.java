package io.github.cruisoring.logger;

import static io.github.cruisoring.Asserts.checkWithoutNull;

public class InMemoryLogger extends Logger {
    private final StringBuilder stringBuilder;

    public InMemoryLogger() {
        this(new StringBuilder(), LogLevel.verbose);
    }

    public InMemoryLogger(LogLevel minLevel) {
        this(new StringBuilder(), minLevel);
    }

    public InMemoryLogger(final StringBuilder sb, LogLevel minLevel) {
        super(log -> checkWithoutNull(sb).append(log + "\n"), minLevel);
        stringBuilder = sb;
    }

    public InMemoryLogger(final StringBuilder sb) {
        this(sb, LogLevel.verbose);
    }

    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }

    public String getHistory() {
        return stringBuilder.toString();
    }
}
