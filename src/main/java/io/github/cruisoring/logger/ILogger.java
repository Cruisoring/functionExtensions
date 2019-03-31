package io.github.cruisoring.logger;

import io.github.cruisoring.function.RunnableThrowable;
import io.github.cruisoring.function.SupplierThrowable;
import io.github.cruisoring.utility.StringHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public interface ILogger {
    /**
     * Get the message to be recorded as given LogLevel, with specific format and corresponding arguments.
     * @param level         LogLevel of the message to be recorded as.
     * @param format        A format String.
     * @param args          Arguments referenced by the format specifiers in the format string.
     * @return              A formatted string
     */
    String getMessage(LogLevel level, String format, Object... args);

    /**
     * Defines how message shall be logged.
     */
    void record(String message);

    /**
     * Mesure the performance of given SupplierThrowable and log its elapse at given LogLevel.
     * @param level     LogLevel to log the measure message.
     * @param supplier  SupplierThrowable that shall return value of type <tt>R</tt>, can be lambda of any method returning a value.
     * @param <R>       Type of the returned value by the given lambda.
     * @return          Value returned by the SupplierThrowable or default value of type <tt>R</tt> when it failed.
     */
    <R> R measure(LogLevel level, SupplierThrowable<R> supplier);

    /**
     * Mesure the performance of given RunnableThrowable and log its elapse at given LogLevel.
     * @param level     LogLevel to log the measure message.
     * @param runable   RunnableThrowable that return nothing.
     * @return          The ILogger instance that can be used fluently.
     */
    ILogger measure(LogLevel level, RunnableThrowable runable);

    /**
     * Define if the message of the specific LogLevel shall be recorded.
     * @param level     LogLevel of the concerned message.
     * @return          <tt>true</tt> means the message shall be recorded, otherwise <tt>false</tt>
     */
    default boolean canLog(LogLevel level){
        return false;
    }

    /**
     * For each LogLevel, retrieve meaningful stack trace of specific number of stack frames.
     * @return Stack trace of the call stack with specific number of stack frames.
     */
    default String getCallStack(LogLevel level){
        switch (level) {
            case verbose:
                return getStackTrace(5);
            case debug:
                return getStackTrace(3);
            case info:
                return getStackTrace(3);
//            case warning:
//            case error:
//                return getStackTrace(-5);
            default:
                return "";
        }
    };

    /**
     * Try to call String.format() and refrain potential IllegalFormatException
     * @param format    template to compose a string with given arguments
     * @param args      arguments to be applied to the above template
     * @return          string formatted with the given or exceptional template.
     */
    default String tryFormatString(String format, Object... args) {
        Objects.requireNonNull(format);
        try {
            String formatted = String.format(format, args);
            formatted = formatted.replaceAll(PercentageAscii, "%");
            return formatted;
        } catch (Exception e) {
            String[] argStrings = Arrays.stream(args).map(arg -> arg.toString()).toArray(size -> new String[size]);
            return String.format("MalFormated format: '%s'\nargs: '%s'", format, String.join(", ", argStrings));
        }
    }

    default ILogger log(LogLevel level, String format, Object... arguments) {
        if(canLog(level)) {
            final String message = getMessage(level, format, arguments);
            record(message);
        }
        return this;
    }

    default ILogger verbose(String format, Object... args){
        return log(LogLevel.verbose, tryFormatString(format, args));
    }

    default ILogger debug(String format, Object... args){
        return log(LogLevel.debug, tryFormatString(format, args));
    }

    default ILogger info(String format, Object... args){
        return log(LogLevel.info, tryFormatString(format, args));
    }

    default ILogger warning(String format, Object... args){
        return log(LogLevel.warning, tryFormatString(format, args));
    }

    default ILogger error(String format, Object... args){
        return log(LogLevel.error, tryFormatString(format, args));
    }

    default ILogger log(LogLevel level, Exception ex) {
        if(canLog(level)) {
            String stackTrace = getCallStack(level);
            log(level, "%s: %s%s", ex.getClass().getSimpleName(), ex.getMessage(),
                    StringUtils.isBlank(stackTrace)?"":"\n"+stackTrace);
        }
        return this;
    }

    default ILogger verbose(Exception ex){
        return log(LogLevel.verbose, ex);
    }

    default ILogger debug(Exception ex){
        return log(LogLevel.debug, ex);
    }

    default ILogger info(Exception ex){
        return log(LogLevel.info, ex);
    }

    default ILogger warning(Exception ex){
        return log(LogLevel.warning, ex);
    }

    default ILogger error(Exception ex){
        return log(LogLevel.error, ex);
    }

    //region Static variables
    final static String PercentageAscii = "&#37";

    static final Set<String> loggerClasses = new HashSet<String>(Arrays.asList(Logger.class.getName(), ILogger.class.getName()));

    static final String SunReflect = "sun.reflect";
    static final String[] platformNeglibles = new String[] { SunReflect };

    //endregion

    //region Retrieve only concerned stack for logging purposes
    static String getStackTrace(int stackCount){
        if (stackCount == 0)
            return  "";

        List<StackTraceElement> stacks = getStackTraces(stackCount);
        AtomicInteger counter = new AtomicInteger();

        String stackTrace = stacks.stream()
                .map(s -> String.format("%s%s", StringUtils.repeat(" ", 2* counter.getAndIncrement()), s))
                .collect(Collectors.joining("\n..."));

        return stackTrace;
    }

    static List<StackTraceElement> getStackTraces(int maxCount){
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        int first=-1, last=-1;
        for(int i=1; i<stacks.length; i++){
            String className = stacks[i].getClassName();
            if (first==-1){
                if (!loggerClasses.contains(className)){
                    first = i;
                }
            } else if(last == -1){
                first = i-1;
                if(!loggerClasses.contains(className)){
                    last = i+1;
                }
            } else if(StringHelper.containsAny(className, platformNeglibles)) {
                last = i+1;
                break;
            } else if(last-first > maxCount){
                break;
            }
        }

        int total = last-first;
        List<StackTraceElement> concerned = Arrays.stream(stacks)
                .skip(first-1).limit(total).collect(Collectors.toList());
        return concerned;
    }
    //endregion


}
