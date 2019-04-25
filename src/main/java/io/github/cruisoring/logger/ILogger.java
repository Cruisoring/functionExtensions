package io.github.cruisoring.logger;

import io.github.cruisoring.Functions;
import io.github.cruisoring.function.RunnableThrowable;
import io.github.cruisoring.function.SupplierThrowable;
import io.github.cruisoring.utility.StackTraceHelper;
import io.github.cruisoring.utility.StringHelper;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static io.github.cruisoring.Functions.checkNotNull;

/**
 * Interface to abstract Logger instances along with default methods could be used or overriden.
 */
public interface ILogger {

    /**
     * Specify how message would be persistent.
     *
     * @param message message to be saved as a log item.
     */
    void save(String message);

    /**
     * Get the minimum LogLevel to be logged by this ILogger.
     *
     * @return the mininum LogLevel that would enable the ILogger to save messages.
     */
    LogLevel getMinLevel();

    /**
     * Define if the message of the specific LogLevel shall be recorded.
     *
     * @param level LogLevel of the concerned message.
     * @return <tt>true</tt> means the message can be recorded, otherwise <tt>false</tt>
     */
    boolean canLog(LogLevel level);

    /**
     * Get the message to be recorded as given LogLevel, with specific format and corresponding arguments.
     *
     * @param level  LogLevel of the message to be recorded as.
     * @param format A format String.
     * @param args   Arguments referenced by the format specifiers in the format string.
     * @return A formatted string
     */
    String getMessage(LogLevel level, String format, Object... args);

//    <R> R measure(Measurement.Moment startMoment, R value, LogLevel logLevel);

    /**
     * Measure the performance of getting the value with a time-consuming process, save into Measurement and log the
     * elapse with proper <code>LogLevel</code>
     *
     * @param startMoment the token object to keep the moment to start calculating the value of type <code>R</code>
     * @param value       the value of type <code>R</code> returned by a time-consuming calculation which is the target to measure
     * @param levels      the optional <code>LogLevel</code> used to display outcome immediately the first LogLevel
     *                    is equal or above the <code>minLevel</code> of this Logger.
     * @param <R>         the type of the value, usually returned by a time-consuming calculation
     * @return the value returned by the concerned time-consuming calculation
     */
    default <R> R measure(Measurement.Moment startMoment, R value, LogLevel... levels) {
        checkNotNull(startMoment);

        LogLevel level = (levels == null || levels.length==0)? Logger.DefaultMeasureLogLevel : levels[0];
        final long elapsedMills = System.currentTimeMillis() - startMoment.createdAt;
        Measurement.save(startMoment.label, Measurement.DefaultColumns.createRow(startMoment.createdAt, elapsedMills));
        log(level, "%s costs %s.", startMoment.label, Duration.ofMillis(elapsedMills));
        return value;
    }

    /**
     * Mesure the performance of given SupplierThrowable and log its elapse save into Measurement and log the
     * elapse with proper <code>LogLevel</code>. Since it triggers the <code>supplier</code>, it gets the chance
     * to capture and display Exception thrown by the <code>supplier</code>
     *
     * @param startMoment the token object to keep the moment to start calculating the value of type <code>R</code>
     * @param supplier    SupplierThrowable that shall return value of type <tt>R</tt>, can be lambda of any method returning a value.
     * @param levels      the optional <code>LogLevel</code> used to display outcome immediately the first LogLevel
     *                    is equal or above the <code>minLevel</code> of this Logger.
     * @param <R>         Type of the returned value by the given lambda.
     * @return Value returned by the SupplierThrowable or default value of type <tt>R</tt> when it failed.
     */
    default <R> R measure(Measurement.Moment startMoment, SupplierThrowable<R> supplier, LogLevel... levels) {
        checkNotNull(startMoment, supplier);

        LogLevel level = (levels == null || levels.length==0)? Logger.DefaultMeasureLogLevel : levels[0];
        Exception e = null;
        long elapsedMills = 0;
        try {
            R result = checkNotNull(supplier).get();
            elapsedMills = System.currentTimeMillis() - startMoment.createdAt;
            Measurement.save(startMoment.label, Measurement.DefaultColumns.createRow(startMoment.createdAt, elapsedMills));
            return result;
        } catch (Exception ex) {
            elapsedMills = System.currentTimeMillis() - startMoment.createdAt;
            e = ex;
            return null;
        } finally {
            log(level, "%s costs %s%s.", startMoment.label, Duration.ofMillis(elapsedMills),
                    e==null ? "" : " with " + e.getClass().getSimpleName());
        }
    }

    /**
     * Measure the performance of running a time-consuming process that returns nothing, save into Measurement and log the
     * elapse with proper <code>LogLevel</code>
     *
     * @param startMoment the token object to keep the moment to start triggering the concerned time-consuming process
     * @param runnable    RunnableThrowable representing how to trigger that time-consuming process
     * @param levels      the optional <code>LogLevel</code> used to display outcome immediately the first LogLevel
     *                    is equal or above the <code>minLevel</code> of this Logger.
     * @return this ILogger instance to be used fluently.
     */
    default ILogger measure(Measurement.Moment startMoment, RunnableThrowable runnable, LogLevel... levels) {
        checkNotNull(startMoment, runnable);

        LogLevel level = (levels == null || levels.length==0)? Logger.DefaultMeasureLogLevel : levels[0];
        Exception e = null;
        long elapsedMills = 0;
        try {
            checkNotNull(runnable).run();
            elapsedMills = System.currentTimeMillis() - startMoment.createdAt;
            Measurement.save(startMoment.label, Measurement.DefaultColumns.createRow(startMoment.createdAt, elapsedMills));
        } catch (Exception ex) {
            elapsedMills = System.currentTimeMillis() - startMoment.createdAt;
            e = ex;
        } finally {
            log(level, "%s costs %s%s.", startMoment.label, Duration.ofMillis(elapsedMills),
                    e==null ? "" : " with " + e.getClass().getSimpleName());
            return this;
        }
    }

    /**
     * Get max number of meaningful StackTraceElements for the given <code>LogLevel</code> that are not Logger or JDK related.
     *
     * @param level <code>LogLevel</code> to be evaluated.
     * @return Max number of LogLevel to be captured by the Logger, 0 means no StackTraceElement info would be saved.
     */
    default int getStackTraceCount(LogLevel level) {
        return 0;
    }

    /**
     * For each LogLevel, retrieve meaningful stack trace of specific number of stack frames.
     *
     * @param level <code>LogLevel</code> to be evaluated
     * @param ex    Exception if thrown that contains stackTrace.
     * @return Stack trace of the call stack with specific number of stack frames.
     */
    default String getCallStack(LogLevel level, Exception ex) {
        int maxCount = getStackTraceCount(level);
        if (maxCount == 0) {
            return "";
        }
        List<StackTraceElement> stacks = StackTraceHelper.getStackTrace(maxCount, ex);
        if (stacks == null) {
            return "";
        }

        AtomicInteger counter = new AtomicInteger();
        String stackTrace = stacks.stream()
                .map(s -> String.format("%s%s", StringUtils.repeat(" ", 2 * counter.getAndIncrement()), s))
                .collect(Collectors.joining("\n..."));

        return stackTrace;
    }

    /**
     * Main entrance method to check if it is allowed by <code>canLog(level)</code> first, if <tt>yes</tt> then compose the message with given <code>format</code> and <code>arguments</code> to save as <code>level</code>.
     *
     * @param level     the <code>LogLevel</code> of the message to be recorded.
     * @param format    the <code>format</code> part of String.format() to be used to compose the final message
     * @param arguments the optional <code>arguments</code> of String.format() to be used to compose the final message
     * @return this ILogger instance to be used fluently.
     */
    default ILogger log(LogLevel level, String format, Object... arguments) {
        if (canLog(level) && format != null) {
            final String message = getMessage(level, format, arguments);
            save(message);
        }
        return this;
    }

    /**
     * Main entrance method to check if it is allowed by <code>canLog(level)</code> first, if <tt>yes</tt> then compose the message stackTrace of <code>ex</code> to save as <code>level</code>.
     *
     * @param level the <code>LogLevel</code> of the message to be recorded.
     * @param ex    the <code>Exception</code> to be recorded.
     * @return this ILogger instance to be used fluently.
     */
    default ILogger log(LogLevel level, Exception ex) {
        if (canLog(level) && ex != null) {
            String stackTrace = getCallStack(level, ex);
            log(level, "%s: %s%s", ex.getClass().getSimpleName(), ex.getMessage(),
                    StringUtils.isBlank(stackTrace) ? "" : "\n" + stackTrace);
        }
        return this;
    }

    /**
     * To compose the message with given <code>format</code> and <code>arguments</code> to save as <code>LogLevel.verbose</code>
     *
     * @param format the <code>format</code> part of String.format() to be used to compose the final message
     * @param args   the optional <code>arguments</code> of String.format() to be used to compose the final message
     * @return this ILogger instance to be used fluently.
     */
    default ILogger verbose(String format, Object... args) {
        return log(LogLevel.verbose, StringHelper.tryFormatString(format, args));
    }

    /**
     * To compose the message with given <code>format</code> and <code>arguments</code> to save as <code>LogLevel.debug</code>
     *
     * @param format the <code>format</code> part of String.format() to be used to compose the final message
     * @param args   the optional <code>arguments</code> of String.format() to be used to compose the final message
     * @return this ILogger instance to be used fluently.
     */
    default ILogger debug(String format, Object... args) {
        return log(LogLevel.debug, StringHelper.tryFormatString(format, args));
    }

    /**
     * To compose the message with given <code>format</code> and <code>arguments</code> to save as <code>LogLevel.info</code>
     *
     * @param format the <code>format</code> part of String.format() to be used to compose the final message
     * @param args   the optional <code>arguments</code> of String.format() to be used to compose the final message
     * @return this ILogger instance to be used fluently.
     */
    default ILogger info(String format, Object... args) {
        return log(LogLevel.info, StringHelper.tryFormatString(format, args));
    }

    /**
     * To compose the message with given <code>format</code> and <code>arguments</code> to save as <code>LogLevel.warning</code>
     *
     * @param format the <code>format</code> part of String.format() to be used to compose the final message
     * @param args   the optional <code>arguments</code> of String.format() to be used to compose the final message
     * @return this ILogger instance to be used fluently.
     */
    default ILogger warning(String format, Object... args) {
        return log(LogLevel.warning, StringHelper.tryFormatString(format, args));
    }

    /**
     * To compose the message with given <code>format</code> and <code>arguments</code> to save as <code>LogLevel.error</code>
     *
     * @param format the <code>format</code> part of String.format() to be used to compose the final message
     * @param args   the optional <code>arguments</code> of String.format() to be used to compose the final message
     * @return this ILogger instance to be used fluently.
     */
    default ILogger error(String format, Object... args) {
        return log(LogLevel.error, StringHelper.tryFormatString(format, args));
    }

    /**
     * if it is allowed by <code>canLog(level)</code>, compose the message including stackTrace of <code>ex</code> to save as <code>LogLevel.verbose</code>.
     *
     * @param ex the <code>Exception</code> to be recorded.
     * @return this ILogger instance to be used fluently.
     */
    default ILogger verbose(Exception ex) {
        return log(LogLevel.verbose, ex);
    }

    /**
     * if it is allowed by <code>canLog(level)</code>, compose the message including stackTrace of <code>ex</code> to save as <code>LogLevel.debug</code>.
     *
     * @param ex the <code>Exception</code> to be recorded.
     * @return this ILogger instance to be used fluently.
     */
    default ILogger debug(Exception ex) {
        return log(LogLevel.debug, ex);
    }

    /**
     * if it is allowed by <code>canLog(level)</code>, compose the message including stackTrace of <code>ex</code> to save as <code>LogLevel.info</code>.
     *
     * @param ex the <code>Exception</code> to be recorded.
     * @return this ILogger instance to be used fluently.
     */
    default ILogger info(Exception ex) {
        return log(LogLevel.info, ex);
    }

    /**
     * if it is allowed by <code>canLog(level)</code>, compose the message including stackTrace of <code>ex</code> to save as <code>LogLevel.warning</code>.
     *
     * @param ex the <code>Exception</code> to be recorded.
     * @return this ILogger instance to be used fluently.
     */
    default ILogger warning(Exception ex) {
        return log(LogLevel.warning, ex);
    }

    /**
     * if it is allowed by <code>canLog(level)</code>, compose the message including stackTrace of <code>ex</code> to save as <code>LogLevel.error</code>.
     *
     * @param ex the <code>Exception</code> to be recorded.
     * @return this ILogger instance to be used fluently.
     */
    default ILogger error(Exception ex) {
        return log(LogLevel.error, ex);
    }


}
