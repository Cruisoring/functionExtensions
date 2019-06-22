package io.github.cruisoring.logger;

import io.github.cruisoring.utility.PlainList;

import static io.github.cruisoring.Asserts.checkNoneNulls;

/**
 * Logger using {@code PlainList<String>} as InMemory logger.
 */
public class ListLogger extends Logger {
    private final PlainList<String> logList;

    public ListLogger() {
        this(LogLevel.verbose);
    }

    public ListLogger(LogLevel minLevel) {
        this(new PlainList<String>(), minLevel);
    }

    public ListLogger(final PlainList<String> list, LogLevel minLevel) {
        super(log -> checkNoneNulls(list).add(log), minLevel);
        logList = list;
    }

    /**
     * Get all saved messages as an Array of Strings.
     * @return  the messages array in their saved order.
     */
    public String[] getMessages(){
        return logList.toArray(null);
    }

    /**
     * Returns the {@code PlainList<String>} instance backing this {@code ListLogger}
     * @return the {@code PlainList<String>} instance keeping logged messages that shall not be used to perform write operations.
     */
    public PlainList<String> getLogList() {
        return logList;
    }
}
