package io.github.cruisoring.utility;

import io.github.cruisoring.logger.ILogger;
import io.github.cruisoring.logger.Logger;

import java.util.*;
import java.util.stream.Collectors;

import static io.github.cruisoring.Functions.checkNotNull;

public class StackTraceHelper {

    /**
     * Java JDK or Logger class names that shall be neglected and used as indicators to locate the customer class methods
     */
    static final Set<String> defaultNegligibleClassNames = new HashSet<String>
            (Arrays.asList(Logger.class.getName(), ILogger.class.getName(), "sun.reflect", "java.lang"));

    /**
     * Retrieve relevant stack trace elements.
     *
     * @param maxCount Its abs() specify up to how many stack frames to be displayed, prefer high level if it is less than 0.
     * @param ex       Exception if available to get the captured stack trace.
     * @return List of stack trace elements
     */
    public static List<StackTraceElement> getStackTrace(int maxCount, Exception ex) {
        if (maxCount == 0)
            return null;

        StackTraceElement[] stacks = ex == null ? Thread.currentThread().getStackTrace() : ex.getStackTrace();
        int first = -1, last = -1;
        for (int i = 0; i < stacks.length; i++) {
            String className = stacks[i].getClassName();
            if (first == -1) {
                if (!defaultNegligibleClassNames.contains(className)) {
                    first = i;
                } else {
                    continue;
                }
            }
            last = i;
            if (defaultNegligibleClassNames.contains(className)) {
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
     * Retrieve the StackTraceElement of the caller of the class/method as specified by the <code>keywords</code>
     *
     * @param ex       Used to get the captured stackTrace when Exception is thrown
     * @param keywords the keywords of the called class/method to be used to
     * @return the last caller of the called class/method as specified by <code>keywords</code>
     */
    public static StackTraceElement getCallerStackTrace(Exception ex, String... keywords) {
        checkNotNull(keywords);

        if(keywords.length==0){
            keywords = new String[]{StackTraceHelper.class.getSimpleName() + ".java"};
        }
        boolean notMatched = true;
        StackTraceElement[] stacks = ex == null ? Thread.currentThread().getStackTrace() : ex.getStackTrace();
        int length = stacks.length;
        for (int i = 0; i < length; i++) {
            StackTraceElement stack = stacks[i];
            String fileName = stack.getFileName();
            if (notMatched) {
                if (StringHelper.containsAll(fileName, keywords)) {
                    notMatched = false;
                }
            } else {
                if (!StringHelper.containsAll(fileName, keywords)) {
                    return stack;
                }
            }
        }
        return null;
    }

    public static String getCallerLabel(Exception ex, String... keywords){
        StackTraceElement stack = StackTraceHelper.getCallerStackTrace(null, keywords);
        String label = StringHelper.tryFormatString("%s(%s:%d)",
                stack.getMethodName(), stack.getFileName(), stack.getLineNumber());
        return label;
    }
}
