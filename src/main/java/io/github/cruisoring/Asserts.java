package io.github.cruisoring;

import io.github.cruisoring.function.RunnableThrowable;
import io.github.cruisoring.function.SupplierThrowable;
import io.github.cruisoring.logger.LogLevel;
import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.utility.StringHelper;

import java.lang.reflect.Array;
import java.util.Arrays;

import static io.github.cruisoring.TypeHelper.valueEquals;

/**
 * Helper to evaluate test outcomes with handy static classes to throw {code Exception} when a test failed to meet expectation.
 */
public class Asserts {

    public static LogLevel logLevel = LogLevel.info;


    /**
     * Use the {@code Logger.Default} to compose and log message with level specified by <tt>logLevel</tt>.
     *
     * @param format Format to compose the message body.
     * @param args   Optional argument to compose the message.
     */
    protected static void log(String format, Object... args) {
        if (Logger.Default != null) {
            Logger.Default.log(logLevel, format, args);
        }
    }


    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @param format    template to compose the error message when {@code reference is null}
     * @param args      arguments to compose the error message when {@code reference is null}
     * @param <R>       type of the argument to be returned.
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <R> R checkNotNull(R reference, String format, Object... args) {
        if (reference == null) {
            throw new NullPointerException(StringHelper.tryFormatString(format, args));
        }
        return reference;
    }

    /**
     * Ensure the state represented by <tt>expression</tt> is true.
     *
     * @param expressions any number of boolean expressions
     * @throws IllegalStateException if {@code expression} is false
     */
    public static void checkStates(boolean... expressions) {
        int length = expressions.length;
        for (int i = 0; i < length; i++) {
            if (!expressions[i]) {
                throw new IllegalStateException("Failed with " + i + "th expresion");
            }
        }
    }

    /**
     * Ensures that all object references passed as a parameter to the calling method is not null.
     *
     * @param reference a object reference
     * @param others    other references
     * @param <T>       type of first reference that would also be returned after checking.
     * @return the first non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkWithoutNull(T reference, Object... others) {
        if (reference == null) {
            throw new NullPointerException();
        }

        int length = others.length;
        if (length == 0 && reference.getClass().isArray()) {
            length = Array.getLength(reference);
            if (length == 0) {
                throw new UnsupportedOperationException();
            }

            for (int i = 0; i < length; i++) {
                if (Array.get(reference, i) == null) {
                    throw new NullPointerException("The " + (1 + i) + "th reference is null!");
                }
            }
            return (T) Array.get(reference, 0);
        } else {
            for (int i = 0; i < length; i++) {
                if (others[i] == null) {
                    throw new NullPointerException("The " + (1 + i) + "th reference is null!");
                }
            }
            return reference;
        }
    }

    /**
     * Fail the current test with an exception containing composed message.
     *
     * @param format Format to compose the message body.
     * @param args   Optional argument to compose the message.
     * @return Not used, <tt>true</tt> to indicate all good.
     */
    public static boolean fail(String format, Object... args) {
        String message = StringHelper.tryFormatString(format, args);
        log(message);
        throw new IllegalStateException(message);
    }

    /**
     * Ensure the states represented by <tt>expressions</tt> are all <tt>true</tt>, otherwise throw IllegalStateException to fail the test.
     *
     * @param expressions any number of boolean expressions
     * @return Not used, <tt>true</tt> to indicate all good.
     * @throws IllegalStateException if any {@code expression} is false
     */
    public static boolean assertTrue(boolean... expressions) {
        int length = expressions.length;
        for (int i = 0; i < length; i++) {
            if (!expressions[i]) {
                throw new IllegalStateException("Failed with " + i + "th expresion");
            }
        }
        return true;
    }

    /**
     * Ensure none of the references is null, otherwise throw Exception to fail the test.
     *
     * @param reference the first reference under evaluation that shall not be null.
     * @param others    other references that shall contains no null reference.
     * @param <T>       type of the reference under evaluation.
     * @return Not used, <tt>true</tt> to indicate all good.
     */
    public static <T> boolean assertNotNull(T reference, T... others) {
        if (reference == null) {
            log("Actual value is %s, instead of <null>.", TypeHelper.deepToString(reference));
            throw new IllegalStateException("Unexpected <null> of the first argument");
        }

        int length = others.length;
        if (length == 0 && reference.getClass().isArray()) {
            length = Array.getLength(reference);
            if (length == 0) {
                throw new UnsupportedOperationException("Try to call assertNotNull() multiple times for each reference");
            }

            for (int i = 0; i < length; i++) {
                if (Array.get(reference, i) == null) {
                    throw new NullPointerException("The " + (1 + i) + "th reference is null!");
                }
            }
            return true;
        } else {
            for (int i = 0; i < length; i++) {
                if (others[i] == null) {
                    throw new NullPointerException("The " + (1 + i) + "th reference is null!");
                }
            }
            return true;
        }
    }

    /**
     * Ensure all of the references are null, otherwise throw Exception to fail the test.
     *
     * @param reference the first reference under evaluation that shall be null.
     * @param others    other references that shall contains only null references.
     * @param <T>       type of the reference under evaluation.
     * @return Not used, <tt>true</tt> to indicate all good.
     */
    public static <T> boolean assertNull(T reference, T... others) {
        if (reference != null) {
            log("Expected: <null> but was: %s", TypeHelper.deepToString(reference));
            throw new IllegalStateException("Unexpected not <null>");
        }

        int length = others.length;
        for (int i = 0; i < length; i++) {
            if (others[i] != null) {
                throw new NullPointerException("The " + (1 + i) + "th reference is not null!");
            }
        }
        return true;
    }

    /**
     * Ensure the states represented by <tt>expressions</tt> are all <tt>false</tt>, otherwise throw IllegalStateException to fail the test.
     *
     * @param expressions any number of boolean expressions
     * @return Not used, <tt>true</tt> to indicate all good.
     * @throws IllegalStateException if any {@code expression} is true
     */
    public static boolean assertFalse(boolean... expressions) {
        int length = expressions.length;
        for (int i = 0; i < length; i++) {
            if (expressions[i]) {
                throw new IllegalStateException("Failed with " + i + "th expresion");
            }
        }
        return true;
    }

    /**
     * Asserts that two objects are equal by themselves or containing same set of values if both are arrays,
     * with/without considering their types.
     * If they are not, an {@link IllegalStateException} is thrown with brief info.
     *
     * @param expected         the expected value to be compared, could be of array or not.
     * @param actual           the actual value to be compared, could be of array or not.
     * @param matchTypeExactly indicates if the types of <code>expected</code> and <code>actual</code> shall be identical.
     * @return Not used, <tt>true</tt> to indicate all good.
     * @throws IllegalStateException if {@code expected} is not equal with {@code actual}
     */
    public static boolean assertEquals(Object expected, Object actual, boolean matchTypeExactly) {
        if (expected == null && actual == null) {
            return true;
        } else if (expected == null) {
            log("%s is not expected null.", TypeHelper.deepToString(actual));
            throw new IllegalStateException("Actual value is not expected null.");
        } else if (actual == null) {
            log("%s is expected, but actual is null.", TypeHelper.deepToString(expected));
            throw new IllegalStateException("NULL is not expected.");
        }

        if (matchTypeExactly && expected.getClass() != actual.getClass()) {
            log("Expect value of type %s, but actual value is of type %s",
                    expected.getClass().getSimpleName(), actual.getClass().getSimpleName());
            throw new IllegalStateException("Types of expectation and actual are not matched.");
        }

        if (!valueEquals(expected, actual)) {
            log("%s !== %s", TypeHelper.deepToString(expected), TypeHelper.deepToString(actual));
            throw new IllegalStateException("Values not matched");
        }
        return true;
    }

    /**
     * Asserts that two objects are equal by themselves or containing same set of values if both are arrays WITHOUT considering their types.
     * If they are not, an {@link IllegalStateException} is thrown with brief info.
     *
     * @param expected the expected value to be compared, could be of array or not.
     * @param actual   the actual value to be compared, could be of array or not.
     * @return Not used, <tt>true</tt> to indicate all good.
     * @throws IllegalStateException if {@code expected} is not equal with {@code actual}
     */
    public static boolean assertEquals(Object expected, Object actual) {
        return assertEquals(expected, actual, false);
    }

    /**
     * Asserts executing the given function would throw an Exception of specific type, otherwise it would throw an IllegalStateException to fail the test.
     *
     * @param supplier       function returning a result of type <tt>R</tt>.
     * @param exceptionClass type of the Exception to be thrown by the concerned function.
     * @param keywords       optional keywords contained in the message of the Exception.
     * @param <R>            type of the result to be returned by the function.
     * @return type of the returning value of the concerned function.
     */
    public static <R> R assertException(SupplierThrowable<R> supplier, Class<? extends Exception> exceptionClass, Object... keywords) {
        checkWithoutNull(supplier, exceptionClass);

        try {
            supplier.get();

            throw new IllegalStateException("No exception of " + exceptionClass.getSimpleName() + "thrown");
        } catch (Exception e) {
            if (e.getClass() != exceptionClass) {
                log("Unexpected type of Exception: expected is %s, actual is %s.", exceptionClass.getSimpleName(), e.getClass().getSimpleName());
                throw new IllegalStateException("Expect exception of " + exceptionClass.getSimpleName() + ", but get " + e.getClass().getSimpleName());
            } else if (keywords != null && keywords.length > 0) {
                if (StringHelper.containsAll(e.getMessage(), keywords)) {
                    return null;
                } else {
                    String[] keyStrings = Arrays.stream(keywords).map(o -> o == null ? "null" : o.toString()).toArray(String[]::new);
                    log("Some keys (%s) are missing: %s", String.join(", ", keyStrings), e.getMessage());

                    throw new IllegalStateException("Missing one or multiple keywords");
                }
            } else {
                return null;
            }
        }
    }

    /**
     * Assserts executing the given function would throw an Exception of specific type, otherwise it would throw an IllegalStateException to fail the test.
     *
     * @param runnableThrowable function returning nothing.
     * @param exceptionClass    type of the Exception to be thrown by the concerned function.
     * @param keywords          optional keywords contained in the message of the Exception.
     */
    public static void assertException(RunnableThrowable runnableThrowable, Class<? extends Exception> exceptionClass, Object... keywords) {
        try {
            runnableThrowable.run();

            throw new IllegalStateException("No exception of " + exceptionClass.getSimpleName() + "thrown");
        } catch (Exception e) {
            if (e.getClass() != exceptionClass) {
                log("Unexpected type of Exception: expected is %s, actual is %s.", exceptionClass.getSimpleName(), e.getClass().getSimpleName());
                throw new IllegalStateException("Expect exception of " + exceptionClass.getSimpleName() + ", but get " + e.getClass().getSimpleName());
            } else if (keywords != null && keywords.length > 0) {
                if (!StringHelper.containsAll(e.getMessage(), keywords)) {
                    String[] keyStrings = Arrays.stream(keywords).map(o -> o == null ? "null" : o.toString()).toArray(String[]::new);
                    log("Some keys (%s) are missing: %s", String.join(", ", keyStrings), e.getMessage());

                    throw new IllegalStateException("Missing one or multiple keywords");
                }
            }
        }
    }
}
