package io.github.cruisoring.logger;

import io.github.cruisoring.throwables.RunnableThrowable;
import io.github.cruisoring.throwables.SupplierThrowable;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Arrays;

import static io.github.cruisoring.Asserts.checkNoneNulls;

/**
 * The composite logger composed by 0 to many children ILogger instances so as to allow multiple loggers to log same message with different means.
 */
public class CompositeLogger implements ILogger {

    static final String NOT_SUPPORTED = "The CompositeLogger doesn't support this operation";

    final ILogger[] loggers;
    final LogLevel minLevel;

    /**
     * Constructor to create a virtual <code>ILogger</code> that would distribute logging requests to all loggers.
     *
     * @param loggers the <code>ILogger</code> instances to be compose this <code>CompositeLogger</code>,
     *                null or CompositeLogger would not be accepted.
     */
    public CompositeLogger(ILogger... loggers) {
        this(LogLevel.verbose, loggers);
    }

    /**
     * Constructor to create a virtual <code>ILogger</code> that would distribute logs equal or above <code>minLevel</code> to all loggers.
     *
     * @param minLevel the minimum <code>LogLevel</code> could be processed by this Logger, notice it would skip
     *                 logging even if some or all of its component Logger would accept logs with lower level than it.
     * @param loggers  the <code>ILogger</code> instances to be compose this <code>CompositeLogger</code>,
     *                 null or CompositeLogger would not be accepted.
     */
    public CompositeLogger(LogLevel minLevel, ILogger... loggers) {
        this.minLevel = checkNoneNulls(minLevel, loggers);
        this.loggers = Arrays.stream(loggers)
                .filter(l -> l != null && !(l instanceof CompositeLogger))
                .toArray(size -> new ILogger[size]);
    }

    /**
     * Get the minimum LogLevel of messages can be logged, that means messages with lower levels would not be saved by any children ILoggers.
     *
     * @return The minimum LogLevel of messages can be logged.
     */
    @Override
    public LogLevel getMinLevel() {
        return minLevel;
    }

    /**
     * Evaluate if the message can be logged as concerned {@code LogLevel}
     *
     * @param level {@code LogLevel} of the concerned message.
     * @return <tt>true</tt> if logging is not disabled and the concerned {@code LogLevel} is supported by
     * this {@code Logger}, otherwise <tt>false</tt>
     */
    @Override
    public boolean canLog(LogLevel level) {
        return Logger.getGlobalLogLevel() != LogLevel.none && level.compareTo(minLevel) >= 0;
    }

    @Override
    public void save(String message) {
        throw new NotImplementedException(NOT_SUPPORTED);
    }

    @Override
    public String getMessage(LogLevel level, String format, Object... args) {
        throw new NotImplementedException(NOT_SUPPORTED);
    }

    @Override
    public <R> R measure(Measurement.Moment startMoment, R value, LogLevel... level) {
        throw new NotImplementedException(NOT_SUPPORTED);
    }

    @Override
    public <R> R measure(Measurement.Moment startMoment, SupplierThrowable<R> supplier, LogLevel... levels) {
        throw new NotImplementedException(NOT_SUPPORTED);
    }

    @Override
    public ILogger measure(Measurement.Moment startMoment, RunnableThrowable runnable, LogLevel... levels) {
        throw new NotImplementedException(NOT_SUPPORTED);
    }

    @Override
    public ILogger log(LogLevel level, String format, Object... arguments) {
        if (!canLog(level)) {
            return this;
        }

        for (ILogger logger : loggers) {
            logger.log(level, format, arguments);
        }

        return this;
    }

    @Override
    public ILogger log(LogLevel level, Exception ex) {
        if (!canLog(level) || ex == null) {
            return this;
        }

        for (ILogger logger : loggers) {
            logger.log(level, ex);
        }

        return this;
    }
}
