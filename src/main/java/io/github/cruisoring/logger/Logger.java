package io.github.cruisoring.logger;

import io.github.cruisoring.AutoCloseableObject;
import io.github.cruisoring.Lazy;
import io.github.cruisoring.TypeHelper;
import io.github.cruisoring.function.RunnableThrowable;
import io.github.cruisoring.function.SupplierThrowable;
import io.github.cruisoring.utility.StringHelper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Logger implements ILogger {

    /**
     * Global LogLevel to be used as both default LogLevel when new a Logger instance, and also a global switch to turn off any logging when set to <code>LogLevel.none</code>
     */
    static LogLevel GlobalLogLevel = LogLevel.debug;

    /**
     * Default Logger to be used to log messages with static methods.
     */
    public static ILogger Default;

    // Use static constructor to ensure Default is created after GlobalLogLevel is initialised.
    static {
        //TODO: load GlobalLogLevel and Default from config
        Default = new ConsoleLogger(System.out::println, GlobalLogLevel);
    }

    /**
     * Get the LogLevel of GlobalLogLevel.
     * @return  The LogLevel held by GlobalLogLevel.
     */
    public static LogLevel getGlobalLogLevel(){
        return GlobalLogLevel;
    }

    /**
     * Get the Logger.Default instance.
     * @return  The instance of Logger held by <code>Default</code>
     */
    public static ILogger getDefault(){
        return Default;
    }

    /**
     * Set the <code>Logger.Default</code> to a new <code>Logger</code>.
     * @param newLogger New <code>Logger</code> to be used globally when calling static methods of <code>Logger</code> class. If it is null, then no logging would happen.
     * @return      The existing <code>Logger</code> instance.
     */
    public static ILogger setDefault(Logger newLogger){
        final ILogger oldLogger = Default;

        if(oldLogger != newLogger){
            Default = newLogger;
        }
        return oldLogger;
    }

    /**
     * Set the <code>Logger.Default</code> to a new <code>Logger</code> and return an <code>AutoCloseableObject</code> which
     * would restore <code>Logger.Default</code> to the existing Logger, so calling static methods of Logger would use the new instance
     * until the returned <code>AutoCloseableObject</code> is closed to restore the old one.
     * @param newLogger  New <code>Logger</code> instance to be set to <code>Logger.Default</code>. If it is <code>null</code>,
     *                      then no logging would happen before the returned <code>AutoCloseableObject</code> is closed.
     * @return the AutoCloseableObject<Logger> containing the existing Logger held by <code>Logger.Default</code>.
     *      Once the AutoCloseableObject<Logger> is closed, the <code>Logger.Default</code> would be replaced back to the existing one.
     */
    public static AutoCloseableObject<ILogger> useInScope(ILogger newLogger) {
        final ILogger oldLogger = Default;
        if(newLogger == Default){
            //Nothing would change, return simply Lazy<> returning newLogLevel directly
            return new AutoCloseableObject<ILogger>(newLogger, AutoCloseableObject.DoNothing);
        }
        Default = newLogger;
        return new AutoCloseableObject<>(oldLogger, t -> Default = oldLogger);
    }


    /**
     * Set the GlobalLogLevel to a new <code>LogLevel</code>.
     * @param newLogLevel   New LogLevel to be set to <code>GlobalLogLevel</code>. If it is <code>LogLevel.none</code>, then no logging methods would be performed.
     * @return      The existing LogLevel held by <code>GlobalLogLevel</code>.
     */
    public static LogLevel setGlobalLevel(LogLevel newLogLevel){
        if(newLogLevel == null){
            //GlobalLevel cannot be set to null
            return GlobalLogLevel;
        }

        final LogLevel oldLevel = GlobalLogLevel;
        if(oldLevel != newLogLevel) {
            GlobalLogLevel = newLogLevel;
        }
        return oldLevel;
    }

    /**
     * Set the GlobalLogLevel to a new value and wrap the restore action as an <code>AutoCloseableObject</code> which
     * would restore <code>GlobalLogLevel</code> to its state before calling this method when closing.
     * @param newLogLevel   New LogLevel to be set to <code>GlobalLogLevel</code>. If it is <code>LogLevel.none</code>,
     *                      then no logging methods would be performed before the returned <code>AutoCloseableObject</code> is closed.
     * @return the AutoCloseableObject<LogLevel> containing the existing LogLevel held by <code>GlobalLogLevel</code>.
     *      Once the AutoCloseableObject<LogLevel> is closed, the <code>GlobalLogLevel</code> would be restored to its old value.
     */
    public static AutoCloseableObject<LogLevel> setLevelInScope(LogLevel newLogLevel) {
        if(newLogLevel == GlobalLogLevel || newLogLevel == null){
            //Nothing would change, return simply Lazy<> returning newLogLevel directly
            return new AutoCloseableObject<LogLevel>(GlobalLogLevel, AutoCloseableObject.DoNothing);
        }
        final LogLevel oldLevel = GlobalLogLevel;
        GlobalLogLevel = newLogLevel;
        return new AutoCloseableObject<>(oldLevel, t -> GlobalLogLevel = oldLevel);
    }

    public static LogLevel DefaultMeasureLogLevel = LogLevel.info;

//    public static EnumSet<LogLevel> DefaultConcernedLevel = EnumSet.allOf(LogLevel.class);

    public static boolean NeglectExceptionWhenMeasure = true;

    public static DateTimeFormatter DefaultTimeStampFormatter = DateTimeFormatter.ISO_TIME;

    public static String[] DefaultSuccessKeywords = new String[]{"success", "passed", "pass"};
    public static String[] DefaultFailedKeywords = new String[]{"fail", "error", "exception", "wrong", "mistake", "problem"};

    //region Measure performance of any method calls as either SupplierThrowable or RunnableThrowable with the Default Logger.
    /**
     * Use Logger.Default to measure time used to get value from the given SupplierThrowable.
     * @return The value returned by the supplier or default value of type <tt>R</tt>.
     */
    public static <R> R M(SupplierThrowable<R> supplier, Object... formatAndArgs){
        if(Default == null)
            return null;

        return Default.measure(DefaultMeasureLogLevel, supplier, formatAndArgs);
    }

    /**
     * Use Logger.Default to measure time used to get value from the given RunnableThrowable.
     * @return The Logger.Default to be used fluently.
     */
    public static ILogger M(RunnableThrowable runable, Object... formatAndArgs){
        if(Default == null)
            return null;

        return Default.measure(DefaultMeasureLogLevel, runable, formatAndArgs);
    }
    //endregion

    //region Logger exception with the Default Logger
    /**
     * Logger exception using the Logger.Default, with default color and including stack frames for Verbose level.
     * @param ex    Exception to be logged.
     * @return      The Logger used to enable fluent logging.
     */
    public static ILogger V(Exception ex) {
        if(Default == null)
            return null;

        return Default.log(LogLevel.verbose, ex);
    }

    /**
     * Logger exception using the Logger.Default, with default color and including stack frames for Debug level.
     * @param ex    Exception to be logged.
     * @return      The Logger used to enable fluent logging.
     */
    public static ILogger D(Exception ex) {
        if(Default == null)
            return null;

        return Default.log(LogLevel.debug, ex);
    }

    /**
     * Logger exception using the Logger.Default, with default color and including stack frames for Info level.
     * @param ex    Exception to be logged.
     * @return      The Logger used to enable fluent logging.
     */
    public static ILogger I(Exception ex) {
        if(Default == null)
            return null;

        return Default.log(LogLevel.info, ex);
    }

    /**
     * Logger exception using the Logger.Default, with default color and including stack frames for Warning level.
     * @param ex    Exception to be logged.
     * @return      The Logger used to enable fluent logging.
     */
    public static ILogger W(Exception ex) {
        if(Default == null)
            return null;

        return Default.log(LogLevel.warning, ex);
    }

    /**
     * Logger exception using the Logger.Default, with default color and including stack frames for Error level.
     * @param ex    Exception to be logged.
     * @return      The Logger used to enable fluent logging.
     */
    public static ILogger E(Exception ex) {
        if(Default == null)
            return null;

        return Default.log(LogLevel.error, ex);
    }
    //endregion

    //region Logger message identified by the specified format string and arguments.
    /**
     * Logger message with the Logger.Default as Verbose level.
     * @param format    Format to compose the message body.
     * @param args      Optional argument to compose the message.
     * @return          The Logger used to enable fluent logging.
     */
    public static ILogger V(String format, Object... args){
        if(Default == null)
            return null;

        return Default.log(LogLevel.verbose, format, args);
    }

    /**
     * Logger message with the Logger.Default as Debug level.
     * @param format    Format to compose the message body.
     * @param args      Optional argument to compose the message.
     * @return          The Logger used to enable fluent logging.
     */
    public static ILogger D(String format, Object... args){
        if(Default == null)
            return null;

        return Default.log(LogLevel.debug, format, args);
    }

    /**
     * Logger message with the Logger.Default as Info level.
     * @param format    Format to compose the message body.
     * @param args      Optional argument to compose the message.
     * @return          The Logger used to enable fluent logging.
     */
    public static ILogger I(String format, Object... args){
        if(Default == null)
            return null;

        return Default.log(LogLevel.info, format, args);
    }

    /**
     * Logger message with the Logger.Default as Warning level.
     * @param format    Format to compose the message body.
     * @param args      Optional argument to compose the message.
     * @return          The Logger used to enable fluent logging.
     */
    public static ILogger W(String format, Object... args){
        if(Default == null)
            return null;

        return Default.log(LogLevel.warning, format, args);
    }

    /**
     * Logger message with the Logger.Default as Error level.
     * @param format    Format to compose the message body.
     * @param args      Optional argument to compose the message.
     * @return          The Logger used to enable fluent logging.
     */
    public static ILogger E(String format, Object... args){
        if(Default == null)
            return null;

        return Default.log(LogLevel.error, format, args);
    }
    //endregion


    final Consumer<String> recorder;
    final LogLevel minLevel;
    Duration measuredElapsed=null;

    /**
     * Compose a Logger with the default value defined by GlobalLogLevel.
     * @param recorder  the means of keeping a String as a log.
     */
    public Logger(Consumer<String> recorder){
        this(recorder, GlobalLogLevel);
    }

    /**
     * Compose a Logger with its means of keeping logs and minimum <code>LogLevel</code> acceptable for logging.
     * @param recorder  the means of keeping a String as a log.
     * @param minLevel  the minimum <code>LogLevel</code> could be logged by this Logger.
     */
    public Logger(Consumer<String> recorder, LogLevel minLevel) {
        this.recorder = recorder;
        Objects.requireNonNull(minLevel);

        this.minLevel = minLevel;
    }

    @Override
    public String toString() {
        return String.format("Logger of %s or obove.", minLevel);
    }

    public Duration getMeasuredElapsed(){
        return measuredElapsed;
    }

    public boolean isSuccess(String format){
        return StringHelper.containsAnyIgnoreCase(format, DefaultSuccessKeywords);
    }

    public boolean isFailed(String format){
        return StringHelper.containsAnyIgnoreCase(format, DefaultFailedKeywords);
    }

    @Override
    public void save(String message) {
        if(recorder != null){
            recorder.accept(message);
        }
    }

    @Override
    public LogLevel getMinLevel() {
        return minLevel;
    }

    /**
     * Update the default format by wrapping args placeholders with highlight notes supported by this Logger.
     * @param format    Original format used to get the message, with placeholders like <tt>%s</tt> or <tt>%d</tt>, complex syntax is not supported.
     * @return          Decorated format String to highlight args when displayed.
     */
    protected String highlightArgs(String format){
        return format;
    }

    @Override
    public boolean canLog(LogLevel level){
        return GlobalLogLevel != LogLevel.none && level.compareTo(minLevel) >= 0;
    }


    @Override
    public String getMessage(LogLevel level, String format, Object... args) {
        Objects.requireNonNull(format);
        final String label = String.format("[%s%s]: ", level.label, DefaultTimeStampFormatter==null? "":"@"+LocalDateTime.now().format(DefaultTimeStampFormatter));
        String message;
        try {
            message = String.format(format, args);
        }catch (IllegalFormatException ex){
            String argsString = Arrays.stream(args).map(a -> a==null?"null":a.toString()).collect(Collectors.joining(", "));
            message = String.format("IllegalFormatException: format='%s', args='%s'", format, argsString);
        }

        return label + message;
    }

    @Override
    public <R> R measure(LogLevel level, SupplierThrowable<R> supplier, Object... formatAndArgs){
        final long startMills = System.currentTimeMillis();
        Exception e=null;
        try {
            return supplier.get();
        }catch (Exception ex){
            e = ex;
            return null;
        }finally {
            _measure(level, e, startMills, formatAndArgs);
        }
    }



    @Override
    public ILogger measure(LogLevel level, RunnableThrowable runable, Object... formatAndArgs){
        final long startMills = System.currentTimeMillis();
        Exception e=null;
        try {
            runable.run();
        }catch (Exception ex){
            e = ex;
        }finally {
            _measure(level, e, startMills, formatAndArgs);
            return this;
        }
    }

    void _measure(LogLevel level, Exception e, long startMills, Object... formatAndArgs){
        if(!canLog(level)) {
            return;
        }

        measuredElapsed = Duration.ofMillis(System.currentTimeMillis() - startMills);
        if (NeglectExceptionWhenMeasure && e != null) {
            log(level, e);
        }
        final String label;
        if(formatAndArgs==null || formatAndArgs.length==0) {
            StackTraceElement stack = ILogger.getStackTrace(1, e).get(0);
            label = StringHelper.tryFormatString("%s.%s(%s:%d)",
                    stack.getClassName(), stack.getMethodName(), stack.getFileName(), stack.getLineNumber());
        } else {
            if(formatAndArgs[0] instanceof String){
                String format = (String) formatAndArgs[0];
                int len = formatAndArgs.length;
                if(len==1){
                    label = format;
                } else {
                    label = StringHelper.tryFormatString(format, TypeHelper.copyOfRange(formatAndArgs, 1, formatAndArgs.length));
                }
            } else {
                label = Arrays.stream(formatAndArgs)
                        .map(o -> o==null? "null":o.toString())
                        .collect(Collectors.joining(", "));
            }
        }
        log(level, "%s to " + (e==null?"pass":"fail") + " %s",
                measuredElapsed, label);
    }
}
