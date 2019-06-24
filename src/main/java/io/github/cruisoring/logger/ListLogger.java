package io.github.cruisoring.logger;

import io.github.cruisoring.TypedList;
import io.github.cruisoring.utility.SimpleTypedList;

import static io.github.cruisoring.Asserts.checkNoneNulls;

/**
 * Logger using {@code SimpleTypedList<String>} as InMemory logger.
 */
public class ListLogger extends Logger {
    private final TypedList<String> logList;

    public ListLogger() {
        this(LogLevel.verbose);
    }

    public ListLogger(LogLevel minLevel) {
        this(new SimpleTypedList<String>(), minLevel);
    }

    public ListLogger(final TypedList<String> list, LogLevel minLevel) {
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
     * Returns the {@code SimpleTypedList<String>} instance backing this {@code ListLogger}
     * @return the {@code SimpleTypedList<String>} instance keeping logged messages that shall not be used to perform write operations.
     */
    public TypedList<String> getLogList() {
        return logList;
    }
}
