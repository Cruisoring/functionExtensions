package io.github.cruisoring.logger;

import io.github.cruisoring.TypeHelper;
import io.github.cruisoring.function.RunnableThrowable;
import io.github.cruisoring.function.SupplierThrowable;
import io.github.cruisoring.utility.StringHelper;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.IllegalFormatException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Logger implements ILogger {

    /**
     * Default Logger to be used to log messages with static methods.
     */
    public static Logger Default = new ConsoleLogger(System.out::println);
    
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
    final EnumSet<LogLevel> concernedLevels;
    Duration measuredElapsed=null;

    public Logger(Consumer<String> recorder, EnumSet<LogLevel> concernedLevels) {
        this.recorder = recorder;
        this.concernedLevels = concernedLevels == null ? EnumSet.allOf(LogLevel.class) : concernedLevels;
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
    public void record(String message) {
        if(recorder != null){
            recorder.accept(message);
        }
    }

    @Override
    public boolean canLog(LogLevel level) {
        return concernedLevels.contains(level);
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
            label = tryFormatString("%s.%s(%s:%d)",
                    stack.getClassName(), stack.getMethodName(), stack.getFileName(), stack.getLineNumber());
        } else {
            if(formatAndArgs[0] instanceof String){
                String format = (String) formatAndArgs[0];
                int len = formatAndArgs.length;
                if(len==1){
                    label = format;
                } else {
                    label = tryFormatString(format, TypeHelper.copyOfRange(formatAndArgs, 1, formatAndArgs.length));
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

    public static class ConsoleLogger extends Logger {
        //region Console color controls
        // Reset
        public static final String RESET = "\033[0m";  // Text Reset

        // Regular Colors
        public static final String BLACK = "\033[0;30m";   // BLACK
        public static final String RED = "\033[0;31m";     // RED
        public static final String GREEN = "\033[0;32m";   // GREEN
        public static final String YELLOW = "\033[0;33m";  // YELLOW
        public static final String BLUE = "\033[0;34m";    // BLUE
        public static final String PURPLE = "\033[0;35m";  // PURPLE
        public static final String CYAN = "\033[0;36m";    // CYAN
        public static final String WHITE = "\033[0;37m";   // WHITE

        // Bold
        public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
        public static final String RED_BOLD = "\033[1;31m";    // RED
        public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
        public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
        public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
        public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
        public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
        public static final String WHITE_BOLD = "\033[1;37m";  // WHITE

        // Underline
        public static final String BLACK_UNDERLINED = "\033[4;30m";  // BLACK
        public static final String RED_UNDERLINED = "\033[4;31m";    // RED
        public static final String GREEN_UNDERLINED = "\033[4;32m";  // GREEN
        public static final String YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
        public static final String BLUE_UNDERLINED = "\033[4;34m";   // BLUE
        public static final String PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
        public static final String CYAN_UNDERLINED = "\033[4;36m";   // CYAN
        public static final String WHITE_UNDERLINED = "\033[4;37m";  // WHITE

        // Background
        public static final String BLACK_BACKGROUND = "\033[40m";  // BLACK
        public static final String DARK_GRAY_BACKGROUND = "\033[100m";  // BLACK
        public static final String RED_BACKGROUND = "\033[101m";    // RED
        public static final String GREEN_BACKGROUND = "\033[102m";  // GREEN
        public static final String YELLOW_BACKGROUND = "\033[103m"; // YELLOW
        public static final String BLUE_BACKGROUND = "\033[104m";   // BLUE
        public static final String PURPLE_BACKGROUND = "\033[105m"; // PURPLE
        public static final String CYAN_BACKGROUND = "\033[106m";   // CYAN
        public static final String WHITE_BACKGROUND = "\033[107m";  // WHITE
        //endregion

        public ConsoleLogger(Consumer<String> recorder, EnumSet<LogLevel> concernedLevels) {
            super(recorder, concernedLevels);
        }

        public ConsoleLogger(Consumer<String> recorder) {
            this(recorder, null);
        }

        @Override
        protected String highlightArgs(String format) {
            String highlighted;
            if(isSuccess(format)){
                highlighted = format.replaceAll("%(\\d*$)?(\\d*)?\\S", GREEN_BOLD + "$0" + RESET);
            } else if(isFailed(format)){
                highlighted = format.replaceAll("%(\\d*$)?(\\d*)?\\S", RED_BOLD + "$0" + RESET);
            } else {
                highlighted = format.replaceAll("%(\\d*$)?(\\d*)?\\S", BLUE_BOLD + "$0" + RESET);
            }
            return highlighted;
        }

        @Override
        public String getMessage(LogLevel level, String format, Object... args) {
            Objects.requireNonNull(format);
            final String label = String.format("[%s%s]: ", level.label, DefaultTimeStampFormatter==null? "":"@"+LocalDateTime.now().format(DefaultTimeStampFormatter));
            String message=null;
            switch (level){
                case verbose:
                    message = WHITE_BACKGROUND+BLACK+label+RESET;
                    break;
                case debug:
                    message = PURPLE_UNDERLINED+label+RESET;
                    break;
                case info:
                    message = CYAN_BACKGROUND+BLACK+label+RESET;
                    break;
                case warning:
                    message = YELLOW_BACKGROUND+PURPLE_UNDERLINED+label+RESET;
                    break;
                case error:
                    message = RED_BACKGROUND+BLACK_BOLD+label+RESET;
                    break;
            }

            if(args!=null && args.length==0){
                message = message + format;
                return message;
            }

            String highlighted = highlightArgs(format);
            try {
                message = message + String.format(highlighted, args);
            }catch (Exception ex){
                String argsString = Arrays.stream(args).map(a -> a==null?"null":a.toString()).collect(Collectors.joining(", "));
                highlighted = highlightArgs(ex.getClass().getName() + ": wrong format='%s', args='%s'");
                message = message + String.format(highlighted, format, argsString);
            }
            return message;
        }

        @Override
        public ILogger log(LogLevel level, Exception ex) {
            if(canLog(level)) {
                String stackTrace = getCallStack(level, ex);
                log(level, String.format("%s: %s%s", ex.getClass().getSimpleName(), ex.getMessage(),
                        StringUtils.isBlank(stackTrace)?"":"\n"+stackTrace));
            }
            return this;
        }
    }
}
