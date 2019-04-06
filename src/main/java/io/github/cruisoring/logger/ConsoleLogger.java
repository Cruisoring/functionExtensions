package io.github.cruisoring.logger;


import io.github.cruisoring.utility.StringHelper;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ConsoleLogger extends Logger implements ILogWithColor {
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

    public static final String DefaultFailPlaceholder = RED_BOLD + "$0" + RESET;
    public static final String DefaultSuccessPlaceholder = GREEN_BOLD + "$0" + RESET;
    public static final String DefaultNormalPlaceholder = BLUE_BOLD + "$0" + RESET;

    public static final Map<LogLevel, String> levelColors = new HashMap<LogLevel, String>(){{
        put(LogLevel.verbose, WHITE_BACKGROUND+BLACK);
        put(LogLevel.debug, PURPLE_UNDERLINED);
        put(LogLevel.info, CYAN_BACKGROUND+BLACK);
        put(LogLevel.warning, YELLOW_BACKGROUND+PURPLE_UNDERLINED);
        put(LogLevel.error, RED_BACKGROUND+BLACK_BOLD);
        put(LogLevel.none, "");
    }};
    //endregion

    public ConsoleLogger(Consumer<String> recorder, LogLevel concernedLevel) {
        super(recorder, concernedLevel);
    }

    public ConsoleLogger(Consumer<String> recorder) {
        this(recorder, null);
    }

    @Override
    public String failPlaceholder() {
        return DefaultFailPlaceholder;
    }

    @Override
    public String successPlaceholder() {
        return DefaultSuccessPlaceholder;
    }

    @Override
    public String normalPlaceholder() {
        return DefaultNormalPlaceholder;
    }

    @Override
    public String highlightArgs(String format) {
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
    public String withColor(LogLevel level, String text) {
        return null;
    }

    @Override
    public String getMessage(LogLevel level, String format, Object... args) {
        Objects.requireNonNull(format);
        final String label = String.format("[%s%s]: ", level.label, DefaultTimeStampFormatter==null? "":"@"+ LocalDateTime.now().format(DefaultTimeStampFormatter));
        String message=levelColors.get(level) + label + RESET;

        if(args!=null && args.length==0){
            message = message + format;
            return message;
        }

        String highlighted = highlightArgs(format);
        message = message + StringHelper.tryFormatString(highlighted, args);
        return message;
    }
}

