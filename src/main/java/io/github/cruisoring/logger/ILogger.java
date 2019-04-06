package io.github.cruisoring.logger;

import io.github.cruisoring.function.RunnableThrowable;
import io.github.cruisoring.function.SupplierThrowable;
import io.github.cruisoring.utility.StringHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Interface to abstract Logger instances along with default methods could be used or overriden.
 */
public interface ILogger {

    /**
     * Specify how message would be persistent.
     * @param message   message to be saved as a log item.
     */
    void save(String message);

    /**
     * Get the minimum LogLevel to be logged by this ILogger.
     * @return  the mininum LogLevel that would enable the ILogger to save messages.
     */
    LogLevel getMinLevel();

    /**
     * Define if the message of the specific LogLevel shall be recorded.
     * @param level     LogLevel of the concerned message.
     * @return          <tt>true</tt> means the message can be recorded, otherwise <tt>false</tt>
     */
    boolean canLog(LogLevel level);

    /**
     * Get the message to be recorded as given LogLevel, with specific format and corresponding arguments.
     * @param level         LogLevel of the message to be recorded as.
     * @param format        A format String.
     * @param args          Arguments referenced by the format specifiers in the format string.
     * @return              A formatted string
     */
    String getMessage(LogLevel level, String format, Object... args);


    /**
     * Mesure the performance of given SupplierThrowable and log its elapse at given LogLevel.
     * @param level     LogLevel to log the measure message.
     * @param supplier  SupplierThrowable that shall return value of type <tt>R</tt>, can be lambda of any method returning a value.
     * @param formatAndArgs Optional format and args to compose the label for this measurement.
     * @param <R>       Type of the returned value by the given lambda.
     * @return          Value returned by the SupplierThrowable or default value of type <tt>R</tt> when it failed.
     */
    <R> R measure(LogLevel level, SupplierThrowable<R> supplier, Object... formatAndArgs);

    /**
     * Mesure the performance of given RunnableThrowable and log its elapse at given LogLevel.
     * @param level     LogLevel to log the measure message.
     * @param runable   RunnableThrowable that return nothing.
     * @param formatAndArgs Optional format and args to compose the label for this measurement.
     * @return          The ILogger instance that can be used fluently.
     */
    ILogger measure(LogLevel level, RunnableThrowable runable, Object... formatAndArgs);

    /**
     * For each LogLevel, retrieve meaningful stack trace of specific number of stack frames.
     * @return Stack trace of the call stack with specific number of stack frames.
     */
    default String getCallStack(LogLevel level, Exception ex){
        List<StackTraceElement> stacks = null;
        switch (level) {
            case verbose:
                stacks = getStackTrace(30, ex);
                break;
            case debug:
                stacks = getStackTrace(15, ex);
                break;
            case info:
                stacks = getStackTrace(10, ex);
                break;
            case warning:
                stacks = getStackTrace(-3, ex);
                break;
            case error:
                stacks = getStackTrace(-8, ex);
                break;
            default:
                break;
        }

        if(stacks == null){
            return "";
        }

        AtomicInteger counter = new AtomicInteger();
        String stackTrace = stacks.stream()
                .map(s -> String.format("%s%s", StringUtils.repeat(" ", 2* counter.getAndIncrement()), s))
                .collect(Collectors.joining("\n..."));

        return stackTrace;
    };

    /**
     * Main entrance method to check if it is allowed by <code>canLog(level)</code> first, if <tt>yes</tt> then compose the message with given <code>format</code> and <code>arguments</code> to save as <code>level</code>.
     * @param level     the <code>LogLevel</code> of the message to be recorded.
     * @param format    the <code>format</code> part of String.format() to be used to compose the final message
     * @param arguments the optional <code>arguments</code> of String.format() to be used to compose the final message
     * @return  this ILogger instance to be used fluently.
     */
    default ILogger log(LogLevel level, String format, Object... arguments) {
        if(canLog(level) && format != null) {
            final String message = getMessage(level, format, arguments);
            save(message);
        }
        return this;
    }

    /**
     * Main entrance method to check if it is allowed by <code>canLog(level)</code> first, if <tt>yes</tt> then compose the message stackTrace of <code>ex</code> to save as <code>level</code>.
     * @param level     the <code>LogLevel</code> of the message to be recorded.
     * @param ex        the <code>Exception</code> to be recorded.
     * @return  this ILogger instance to be used fluently.
     */
    default ILogger log(LogLevel level, Exception ex) {
        if(canLog(level) && ex != null) {
            String stackTrace = getCallStack(level, ex);
            log(level, "%s: %s%s", ex.getClass().getSimpleName(), ex.getMessage(),
                    StringUtils.isBlank(stackTrace)?"":"\n"+stackTrace);
        }
        return this;
    }

    /**
     * To compose the message with given <code>format</code> and <code>arguments</code> to save as <code>LogLevel.verbose</code>
     * @param format    the <code>format</code> part of String.format() to be used to compose the final message
     * @param args      the optional <code>arguments</code> of String.format() to be used to compose the final message
     * @return  this ILogger instance to be used fluently.
     */
    default ILogger verbose(String format, Object... args){
        return log(LogLevel.verbose, StringHelper.tryFormatString(format, args));
    }

    /**
     * To compose the message with given <code>format</code> and <code>arguments</code> to save as <code>LogLevel.debug</code>
     * @param format    the <code>format</code> part of String.format() to be used to compose the final message
     * @param args      the optional <code>arguments</code> of String.format() to be used to compose the final message
     * @return  this ILogger instance to be used fluently.
     */
    default ILogger debug(String format, Object... args){
        return log(LogLevel.debug, StringHelper.tryFormatString(format, args));
    }

    /**
     * To compose the message with given <code>format</code> and <code>arguments</code> to save as <code>LogLevel.info</code>
     * @param format    the <code>format</code> part of String.format() to be used to compose the final message
     * @param args      the optional <code>arguments</code> of String.format() to be used to compose the final message
     * @return  this ILogger instance to be used fluently.
     */
    default ILogger info(String format, Object... args){
        return log(LogLevel.info, StringHelper.tryFormatString(format, args));
    }

    /**
     * To compose the message with given <code>format</code> and <code>arguments</code> to save as <code>LogLevel.warning</code>
     * @param format    the <code>format</code> part of String.format() to be used to compose the final message
     * @param args      the optional <code>arguments</code> of String.format() to be used to compose the final message
     * @return  this ILogger instance to be used fluently.
     */
    default ILogger warning(String format, Object... args){
        return log(LogLevel.warning, StringHelper.tryFormatString(format, args));
    }

    /**
     * To compose the message with given <code>format</code> and <code>arguments</code> to save as <code>LogLevel.error</code>
     * @param format    the <code>format</code> part of String.format() to be used to compose the final message
     * @param args      the optional <code>arguments</code> of String.format() to be used to compose the final message
     * @return  this ILogger instance to be used fluently.
     */
    default ILogger error(String format, Object... args){
        return log(LogLevel.error, StringHelper.tryFormatString(format, args));
    }

    /**
     * if it is allowed by <code>canLog(level)</code>, compose the message including stackTrace of <code>ex</code> to save as <code>LogLevel.verbose</code>.
     * @param ex        the <code>Exception</code> to be recorded.
     * @return  this ILogger instance to be used fluently.
     */
    default ILogger verbose(Exception ex){
        return log(LogLevel.verbose, ex);
    }

    /**
     * if it is allowed by <code>canLog(level)</code>, compose the message including stackTrace of <code>ex</code> to save as <code>LogLevel.debug</code>.
     * @param ex        the <code>Exception</code> to be recorded.
     * @return  this ILogger instance to be used fluently.
     */
    default ILogger debug(Exception ex){
        return log(LogLevel.debug, ex);
    }

    /**
     * if it is allowed by <code>canLog(level)</code>, compose the message including stackTrace of <code>ex</code> to save as <code>LogLevel.info</code>.
     * @param ex        the <code>Exception</code> to be recorded.
     * @return  this ILogger instance to be used fluently.
     */
    default ILogger info(Exception ex){
        return log(LogLevel.info, ex);
    }

    /**
     * if it is allowed by <code>canLog(level)</code>, compose the message including stackTrace of <code>ex</code> to save as <code>LogLevel.warning</code>.
     * @param ex        the <code>Exception</code> to be recorded.
     * @return  this ILogger instance to be used fluently.
     */
    default ILogger warning(Exception ex){
        return log(LogLevel.warning, ex);
    }

    /**
     * if it is allowed by <code>canLog(level)</code>, compose the message including stackTrace of <code>ex</code> to save as <code>LogLevel.error</code>.
     * @param ex        the <code>Exception</code> to be recorded.
     * @return  this ILogger instance to be used fluently.
     */
    default ILogger error(Exception ex){
        return log(LogLevel.error, ex);
    }

    //region Static variables and methods

    //Logger related classes that are usually not displayed in the final logs
    static final Set<String> loggerClasses = new HashSet<String>(Arrays.asList(Logger.class.getName(), ILogger.class.getName()));

    //Java SDK related classes that are not displayed in the final logs
    static final String[] systemClassNames = new String[] { "sun.reflect", "java.lang" };

    /**
     * Retrieve relevant stack trace elements.
     * @param maxCount    Its abs() specify up to how many stack frames to be displayed, prefer high level if it is less than 0.
     * @param ex    Exception if available to get the captured stack trace.
     * @return      List of stack trace elements
     */
    static List<StackTraceElement> getStackTrace(int maxCount, Exception ex){
        if (maxCount == 0)
            return  null;

        StackTraceElement[] stacks = ex==null ? Thread.currentThread().getStackTrace() : ex.getStackTrace();
        int first=-1, last=-1;
        for(int i=0; i<stacks.length; i++){
            String className = stacks[i].getClassName();
            if (first==-1){
                if (!loggerClasses.contains(className) && !StringHelper.containsAny(className, systemClassNames)){
                    first = i;
                }else{
                    continue;
                }
            }
            last = i;
            if(StringHelper.containsAny(className, systemClassNames)){
                break;
            }
        }

        if(maxCount > 0 && last-first > maxCount){
            last = first+maxCount;
        } else if(maxCount<0 && first-last < maxCount){
            first = last+maxCount < 0 ? 0 : last+maxCount;
        }
        List<StackTraceElement> concerned = Arrays.stream(stacks)
                .skip(first).limit(last-first).collect(Collectors.toList());
        return concerned;
    }
    //endregion


}
