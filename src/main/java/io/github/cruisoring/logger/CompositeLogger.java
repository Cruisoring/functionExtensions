package io.github.cruisoring.logger;

import io.github.cruisoring.function.RunnableThrowable;
import io.github.cruisoring.function.SupplierThrowable;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Arrays;

import static io.github.cruisoring.Asserts.checkWithoutNull;

public class CompositeLogger implements ILogger {

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
        checkWithoutNull(minLevel, loggers);

        this.minLevel = minLevel;
        this.loggers = Arrays.stream(loggers)
                .filter(l -> l != null && !(l instanceof CompositeLogger))
                .toArray(size -> new ILogger[size]);
    }


    @Override
    public void save(String message) {
        throw new NotImplementedException("Not supported");
    }

    @Override
    public LogLevel getMinLevel() {
        return minLevel;
    }

    @Override
    public boolean canLog(LogLevel level) {
        return Logger.getGlobalLogLevel() != LogLevel.none && level.compareTo(minLevel) >= 0;
    }

    @Override
    public String getMessage(LogLevel level, String format, Object... args) {
        throw new NotImplementedException("Not supported");
    }

    @Override
    public <R> R measure(Measurement.Moment startMoment, R value, LogLevel... level) {
        throw new NotImplementedException("Not supported");
    }

    @Override
    public <R> R measure(Measurement.Moment startMoment, SupplierThrowable<R> supplier, LogLevel... levels) {
        throw new NotImplementedException("Not supported");
    }

    @Override
    public ILogger measure(Measurement.Moment startMoment, RunnableThrowable runnable, LogLevel... levels) {
        throw new NotImplementedException("Not supported");
    }

    @Override
    public ILogger log(LogLevel level, String format, Object... arguments) {
        if (!canLog(level) || format == null) {
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
