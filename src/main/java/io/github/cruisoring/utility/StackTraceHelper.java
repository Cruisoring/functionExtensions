package io.github.cruisoring.utility;

import io.github.cruisoring.logger.ILogger;
import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.tuple.Tuple;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper class to get meaningful Stack Traces.
 */
public class StackTraceHelper {

    /**
     * Java JDK or Logger class names that shall be neglected and used as indicators to locate the customer class methods
     */
    static final String[] defaultFilters = new String[]{
            Logger.class.getName(), ILogger.class.getName(),
            StackTraceHelper.class.getName(), "sun.reflect.NativeMethodAccessorImpl", "java.lang.Thread"};

    /**
     * Retrieve relevant stack trace elements.
     *
     * @param maxCount Its abs() specify up to how many stack frames to be displayed, prefer high level if it is less than 0.
     * @param ex       Exception if available to get the captured stack trace.
     * @param filters   keywords that shall be neglected.
     * @return List of stack trace elements
     */
    public static List<StackTraceElement> getFilteredStacks(int maxCount, Exception ex, String... filters) {
        if (maxCount == 0)
            return null;

        Tuple<String> tuple = Tuple.setOf(filters.length == 0 ? defaultFilters : filters);
        StackTraceElement[] stacks = ex == null ? Thread.currentThread().getStackTrace() : ex.getStackTrace();
        int first = -1, last = -1;
        for (int i = 0; i < stacks.length; i++) {
            String className = stacks[i].getClassName();
            if (first == -1) {
                if (!tuple.anyMatch(s -> className.equals(s))) {
                    first = i;
                } else {
                    continue;
                }
            }
            last = i;
            if (tuple.anyMatch(s -> className.equals(s))) {
                break;
            }
        }

        if (maxCount > 0 && last - first > maxCount) {
            last = first + maxCount;
        } else if (maxCount < 0 && first - last < maxCount) {
            first = last + maxCount < 0 ? 0 : last + maxCount;
        }
        List<StackTraceElement> concerned = Arrays.stream(stacks)
                .skip(first).limit(last - first).collect(Collectors.toList());
        return concerned;
    }

    /**
     * Get the caller class who calls any methods of the baseTestRunner
     *
     * @param filters keywords that shall be neglected.
     * @return Class of the Caller.
     */
    public static List<String> getFilteredCallers(String... filters) {
        List<StackTraceElement> stacks = getFilteredStacks(500, null, filters);
        List<String> classNames = stacks.stream().map(stack -> stack.getClassName()).collect(Collectors.toList());

        return classNames;
    }

    /**
     * Get the caller class who calls any methods of the baseTestRunner
     *
     * @param filters keywords that shall be neglected.
     * @return Class of the Caller.
     */
    public static Class getCallerClass(String... filters) {
        List<String> callers = getFilteredCallers(filters);
        try {
            String className = callers.get(callers.size() - 1);
            Class callerClass = Class.forName(className);
            return callerClass;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Retrieve the StackTraceElement of the caller of the class/method as specified by the <code>keywords</code>
     *
     * @param ex       Used to get the captured stackTrace when Exception is thrown
     * @param calledKeywords the keywords of the called class/method to be used to
     * @return the last caller of the called class/method as specified by <code>keywords</code>
     */
    public static StackTraceElement getCallerStackByEntry(Exception ex, String... calledKeywords) {
        if (calledKeywords == null || calledKeywords.length == 0) {
            calledKeywords = new String[]{StackTraceHelper.class.getSimpleName() + ".java"};
        }
        boolean notMatched = true;
        StackTraceElement[] stacks = ex == null ? Thread.currentThread().getStackTrace() : ex.getStackTrace();
        int length = stacks.length;
        for (int i = 0; i < length; i++) {
            StackTraceElement stack = stacks[i];
            String stackString = stack.toString();
            if (notMatched) {
                if (StringHelper.containsAll(stackString, calledKeywords)) {
                    notMatched = false;
                }
            } else {
                if (!StringHelper.containsAll(stackString, calledKeywords)) {
                    return stack;
                }
            }
        }
        return null;
    }

    /**
     * Get the label to describe the caller.
     *
     * @param ex       Used to get the captured stackTrace when Exception is thrown
     * @param calledKeywords the keywords of the called class/method to be used to
     * @return the label to describe the caller that is identified by the given keywords.
     */
    public static String getCallerLabel(Exception ex, String... calledKeywords) {
        StackTraceElement stack = getCallerStackByEntry(ex, calledKeywords);
        String label = stack == null ? null : StringHelper.tryFormatString("%s(%s:%d)",
                stack.getMethodName(), stack.getFileName(), stack.getLineNumber());
        return label;
    }


}
