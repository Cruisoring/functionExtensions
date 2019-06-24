package io.github.cruisoring.logger;

import io.github.cruisoring.Revokable;
import io.github.cruisoring.throwables.RunnableThrowable;
import io.github.cruisoring.throwables.SupplierThrowable;
import io.github.cruisoring.utility.StringHelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static io.github.cruisoring.Asserts.assertAllNotNull;
import static io.github.cruisoring.Asserts.checkNoneNulls;

public class Logger implements ILogger {

    //region Static variables
    /**
     * Default Logger to be used to log messages with static methods.
     */
    public static ILogger Default = new ConsoleLogger(System.out::println, LogLevel.verbose);
    public static LogLevel DefaultMeasureLogLevel = LogLevel.debug;

    //DoNothing will be used as Default if invalid instance is provided
    public static final ILogger DoNothing = new Logger(s -> {});

    public static DateTimeFormatter DefaultTimeStampFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    public static String[] DefaultSuccessKeywords = new String[]{"success", "passed", "pass"};
    public static String[] DefaultFailedKeywords = new String[]{"fail", "error", "exception", "wrong", "mistake", "problem"};
    /**
     * Global LogLevel to be used as both default LogLevel when new a Logger instance, and also a global switch to turn off any logging when set to <code>LogLevel.none</code>
     */
    static LogLevel GlobalLogLevel = LogLevel.debug;

    static Object lock = new Object();
    //endregion

    //region Static methods
    /**
     * Get the LogLevel of GlobalLogLevel.
     *
     * @return The LogLevel held by GlobalLogLevel.
     */
    public static LogLevel getGlobalLogLevel() {
        synchronized (lock) {
            return GlobalLogLevel;
        }
    }

    //region Measure performance of any method

    /**
     * Get the Logger.Default instance.
     *
     * @return The instance of Logger held by <code>Default</code>
     */
    public static ILogger getDefault() {
        synchronized (lock) {
            return Default;
        }
    }

    /**
     * Set1 the <code>Logger.Default</code> to a new <code>Logger</code>.
     *
     * @param newLogger New <code>Logger</code> to be used globally when calling static methods of <code>Logger</code> class. If it is null, then no logging would happen.
     * @return The existing <code>Logger</code> instance.
     */
    public static ILogger setDefault(Logger newLogger) {
        synchronized (lock) {
            final ILogger oldLogger = getDefault();

            if(newLogger == null){
                Default = DoNothing;
            } else if (oldLogger != newLogger) {
                Default = newLogger;
            }
            return oldLogger;
        }
    }

    /**
     * Set1 the <code>Logger.Default</code> to a new <code>Logger</code> and return an {@code Revocable<ILogger>} which
     * would restore <code>Logger.Default</code> to the existing Logger, so calling static methods of Logger would use the new instance
     * until the returned {@code Revocable<ILogger>} is closed to restore the old one.
     *
     * @param newLogger New <code>Logger</code> instance to be set to <code>Logger.Default</code>. If it is <code>`null</code>,
     *                  then no logging would happen before the returned {@code Revocable<ILogger>} is closed.
     * @return the {@code Revocable<ILogger>} containing the existing Logger held by <code>Logger.Default</code>.
     * Once the {@code Revocable<ILogger>} is closed, the <code>Logger.Default</code> would be replaced back to the existing one.
     */
    public static Revokable<ILogger> useInScope(ILogger newLogger) {
        synchronized (lock) {
            Revokable<ILogger> revokable = new Revokable<>(() -> Default, level -> Default = level, newLogger);
            return revokable;
        }
    }
    //endregion

    //region Logger exception with the Default Logger

    /**
     * Set1 the GlobalLogLevel to a new <code>LogLevel</code>.
     *
     * @param newLogLevel New LogLevel to be set to <code>GlobalLogLevel</code>. If it is <code>LogLevel.none</code>, then no logging methods would be performed.
     * @return The existing LogLevel held by <code>GlobalLogLevel</code>.
     */
    public static LogLevel setGlobalLevel(LogLevel newLogLevel) {
        synchronized (lock) {
            if (newLogLevel == null) {
                //GlobalLevel cannot be set to null
                return GlobalLogLevel;
            }

            final LogLevel oldLevel = GlobalLogLevel;
            if (oldLevel != newLogLevel) {
                GlobalLogLevel = newLogLevel;
            }
            return oldLevel;
        }
    }

    /**
     * Set1 the GlobalLogLevel to a new value and wrap the restore action as an {@code Revocable<LogLevel>} which
     * would restore <code>GlobalLogLevel</code> to its state before calling this method when closing.
     *
     * @param newLogLevel New LogLevel to be set to <code>GlobalLogLevel</code>. If it is <code>LogLevel.none</code>,
     *                    then no logging methods would be performed before the returned {@code Revocable<LogLevel>} is closed.
     * @return the {@code Revocable<LogLevel>} containing the existing LogLevel held by <code>GlobalLogLevel</code>.
     * Once the {@code Revocable<LogLevel>} is closed, the <code>GlobalLogLevel</code> would be restored to its old value.
     */
    public static Revokable<LogLevel> setLevelInScope(LogLevel newLogLevel) {
        synchronized (lock) {
            Revokable<LogLevel> revokable = new Revokable<>(() -> GlobalLogLevel, level -> GlobalLogLevel = level, newLogLevel);
            return revokable;
        }
    }

    /**
     * Use <code>Logger.Default</code> to measure the performance of getting the value with a time-consuming process,
     * save into Measurement and log the elapse with proper <code>LogLevel</code>
     *
     * @param startMoment the token object to keep the moment to start calculating the value of type <code>R</code>
     * @param value       the value of type <code>R</code> returned by a time-consuming calculation which is the target to measure
     * @param levels      the optional <code>LogLevel</code> used to display outcome immediately the first LogLevel
     *                    is equal or above the <code>minLevel</code> of this Logger.
     * @param <R>         the type of the value, usually returned by a time-consuming calculation
     * @return the value returned by the concerned time-consuming calculation
     */
    public static <R> R M(Measurement.Moment startMoment, R value, LogLevel... levels) {
        LogLevel level = (levels == null || levels.length == 0) ? DefaultMeasureLogLevel : levels[0];
        return getDefault().measure(startMoment, value, level);
    }

    /**
     * Use Logger.Default to measure time used to get value from the given SupplierThrowable, save into Measurement and log the
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
    public static <R> R M(Measurement.Moment startMoment, SupplierThrowable<R> supplier, LogLevel... levels) {
        LogLevel level = (levels == null || levels.length == 0) ? DefaultMeasureLogLevel : levels[0];
        return getDefault().measure(startMoment, supplier, level);
    }

    /**
     * Use Logger.Default to measure time used to get value from the given RunnableThrowable, save into Measurement and log the
     * elapse with proper <code>LogLevel</code>
     *
     * @param startMoment the token object to keep the moment to start triggering the concerned time-consuming process
     * @param runnable    RunnableThrowable representing how to trigger that time-consuming process
     * @param levels      the optional <code>LogLevel</code> used to display outcome immediately the first LogLevel
     *                    is equal or above the <code>minLevel</code> of this Logger.
     * @return the <code>Logger.Default</code> to be used fluently.
     */

    public static ILogger M(Measurement.Moment startMoment, RunnableThrowable runnable, LogLevel... levels) {
        LogLevel level = (levels == null || levels.length == 0) ? DefaultMeasureLogLevel : levels[0];
        return getDefault().measure(startMoment, runnable, level);
    }
    //endregion

    //region Logger message identified by the specified format string and arguments.

    /**
     * Logger exception using the Logger.Default, with default color and including stack frames for Verbose level.
     *
     * @param ex Exception to be logged.
     * @return The Logger used to enable fluent logging.
     */
    public static ILogger V(Exception ex) {
        return getDefault().log(LogLevel.verbose, ex);
    }

    /**
     * Logger exception using the Logger.Default, with default color and including stack frames for Debug level.
     *
     * @param ex Exception to be logged.
     * @return The Logger used to enable fluent logging.
     */
    public static ILogger D(Exception ex) {
        return getDefault().log(LogLevel.debug, ex);
    }

    /**
     * Logger exception using the Logger.Default, with default color and including stack frames for Info level.
     *
     * @param ex Exception to be logged.
     * @return The Logger used to enable fluent logging.
     */
    public static ILogger I(Exception ex) {
        return getDefault().log(LogLevel.info, ex);
    }

    /**
     * Logger exception using the Logger.Default, with default color and including stack frames for Warning level.
     *
     * @param ex Exception to be logged.
     * @return The Logger used to enable fluent logging.
     */
    public static ILogger W(Exception ex) {
        return getDefault().log(LogLevel.warning, ex);
    }

    /**
     * Logger exception using the Logger.Default, with default color and including stack frames for Error level.
     *
     * @param ex Exception to be logged.
     * @return The Logger used to enable fluent logging.
     */
    public static ILogger E(Exception ex) {
        return getDefault().log(LogLevel.error, ex);
    }
    //endregion

    /**
     * Logger message with the Logger.Default as Verbose level.
     *
     * @param format Format to compose the message body.
     * @param args   Optional argument to compose the message.
     * @return The Logger used to enable fluent logging.
     */
    public static ILogger V(String format, Object... args) {
        return getDefault().log(LogLevel.verbose, format, args);
    }

    /**
     * Logger message with the Logger.Default as Debug level.
     *
     * @param format Format to compose the message body.
     * @param args   Optional argument to compose the message.
     * @return The Logger used to enable fluent logging.
     */
    public static ILogger D(String format, Object... args) {
        return getDefault().log(LogLevel.debug, format, args);
    }

    /**
     * Logger message with the Logger.Default as Info level.
     *
     * @param format Format to compose the message body.
     * @param args   Optional argument to compose the message.
     * @return The Logger used to enable fluent logging.
     */
    public static ILogger I(String format, Object... args) {
        return getDefault().log(LogLevel.info, format, args);

    }

    /**
     * Logger message with the Logger.Default as Warning level.
     *
     * @param format Format to compose the message body.
     * @param args   Optional argument to compose the message.
     * @return The Logger used to enable fluent logging.
     */
    public static ILogger W(String format, Object... args) {
        return getDefault().log(LogLevel.warning, format, args);
    }

    /**
     * Logger message with the Logger.Default as Error level.
     *
     * @param format Format to compose the message body.
     * @param args   Optional argument to compose the message.
     * @return The Logger used to enable fluent logging.
     */
    public static ILogger E(String format, Object... args) {
        return getDefault().log(LogLevel.error, format, args);
    }
    //endregion

    //region Instance variables
    final Consumer<String> recorder;
    final LogLevel minLevel;
    //endregion

    //region Constructors
    /**
     * Compose a Logger with the default {@code LogLevel.verbose}.
     *
     * @param recorder the means of keeping a String as a log.
     */
    public Logger(Consumer<String> recorder) {
        this(recorder, LogLevel.verbose);
    }

    /**
     * Compose a Logger with its means of keeping logs and minimum <code>LogLevel</code> acceptable for logging.
     *
     * @param recorder the means of keeping a String as a log.
     * @param minLevel the minimum <code>LogLevel</code> could be logged by this Logger.
     */
    public Logger(Consumer<String> recorder, LogLevel minLevel) {
        this.recorder = checkNoneNulls(recorder, minLevel);

        this.minLevel = minLevel;
    }
    //endregion

    @Override
    public String toString() {
        return String.format("Logger of %s or obove.", minLevel);
    }

    /**
     * Helper method to guess if the message to be logged means something positive.
     *
     * @param format the format to compose the message that would be logged.
     * @return <tt>true</tt> if this message means something positive, otherwise <tt>false</tt>
     */
    public boolean isSuccess(String format) {
        return StringHelper.containsAnyIgnoreCase(format, DefaultSuccessKeywords);
    }

    /**
     * Helper method to guess if the message to be logged means something negative.
     *
     * @param format the format to compose the message that would be logged.
     * @return <tt>true</tt> if this message means something negative, otherwise <tt>false</tt>
     */
    public boolean isFailed(String format) {
        return StringHelper.containsAnyIgnoreCase(format, DefaultFailedKeywords);
    }

    @Override
    public void save(String message) {
        if (recorder != null) {
            recorder.accept(message);
        }
    }

    @Override
    public LogLevel getMinLevel() {
        return minLevel;
    }

    /**
     * Update the default format by wrapping args placeholders with highlight notes supported by this Logger.
     *
     * @param format Original format used to get the message, with placeholders like <tt>%s</tt> or <tt>%d</tt>, complex syntax is not supported.
     * @return Decorated format String to highlight args when displayed.
     */
    protected String highlightArgs(String format) {
        return format;
    }

    /**
     * Evaluate if the message can be logged as concerned {@code LogLevel}
     *
     * @param level {@code LogLevel} of the concerned message.
     * @return <tt>true</tt> if the concerned {@code LogLevel} is supported by
     * both globally and by this {@code Logger}, otherwise <tt>false</tt>
     */
    @Override
    public boolean canLog(LogLevel level) {
        return level.compareTo(GlobalLogLevel) >= 0 && level.compareTo(minLevel) >= 0;
    }

    /**
     * Get the max lines of stacks to be logged for concerned {@code LogLevel} so as to keep relevant StackTrace only.
     *
     * @param level <code>LogLevel</code> to be evaluated.
     * @return the number of stackTraces to be logged. Positive values wold
     */
    @Override
    public int getStackTraceCount(LogLevel level) {
        switch (level) {
            case verbose:
                return 30;
            case debug:
                return 15;
            case info:
                return 10;
            case warning:
                return -5;
            case error:
                return -8;
            default:
                return 0;
        }
    }

    @Override
    public String getMessage(LogLevel level, String format, Object... args) {
        assertAllNotNull(format);

        final String label = String.format("[%s%s]: ", level.label, DefaultTimeStampFormatter == null ? "" : "@" + LocalDateTime.now().format(DefaultTimeStampFormatter));
        String message;
        try {
            message = String.format(format, args);
        } catch (IllegalFormatException ex) {
            String argsString = Arrays.stream(args).map(a -> a == null ? "null" : a.toString()).collect(Collectors.joining(", "));
            message = String.format("IllegalFormatException: format='%s', args='%s'", format, argsString);
        }

        return label + message;
    }
}
