package io.github.cruisoring;

import io.github.cruisoring.logger.*;
import io.github.cruisoring.throwables.RunnableThrowable;
import io.github.cruisoring.throwables.SupplierThrowable;
import io.github.cruisoring.utility.ArrayHelper;
import io.github.cruisoring.utility.StringHelper;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;

import static io.github.cruisoring.TypeHelper.valueEquals;
import static io.github.cruisoring.utility.StringHelper.tryFormatString;

/**
 * Helper to evaluate test outcomes with handy static classes to throw {@code Exception} when a test failed to meet expectation.
 */
public class Asserts {

    public static LogLevel defaultLogLevel = LogLevel.debug;

    private Asserts(){}

    /**
     * Use the {@code Logger.Default} to log the concerned Exception with level specified by <tt>defaultLogLevel</tt>.
     *
     * @param exception   the Exception to be logged.
     * @return the composed message with given format and args.
     */
    protected static RuntimeException logRuntimeException(Exception exception) {
        if (Logger.Default != null && exception != null) {
            Logger.Default.log(defaultLogLevel, exception);
        }
        return (exception instanceof RuntimeException) ? 
                (RuntimeException) exception : new IllegalStateException(exception);
    }

    /**
     * Fail the current test with an exception containing composed message.
     *
     * @param format Format to compose the message body.
     * @param args   Optional argument to compose the message.
     * @return Not used, <tt>true</tt> to indicate all good.
     */
    public static boolean fail(String format, Object... args) {
        throw logRuntimeException(new IllegalStateException(tryFormatString(format, args)));
    }

    //region Evaluate multiple conditions only by throw IllegalStateException with the index of problematic statement
    /**
     * Ensure the states represented by <tt>expressions</tt> are all <tt>TRUE</tt>, otherwise throw IllegalStateException to fail the test.
     *
     * @param first         the first statement to be evaluated.
     * @param expressions any number of boolean expressions
     * @throws IllegalStateException if any {@code expression} is false
     */
    public static void assertAllTrue(boolean first, boolean... expressions) {
        if(!first) {
            throw logRuntimeException(new IllegalStateException("The first expresion is false when true is expected."));
        }

        int length = expressions.length;
        for (int i = 0; i < length; i++) {
            if (!expressions[i]) {
                throw logRuntimeException(new IllegalStateException(
                        tryFormatString("The %dth expresion is false when true is expected.", i+1)));
            }
        }
    }

    /**
     * Ensure the states represented by <tt>expressions</tt> are all <tt>false</tt>, otherwise throw IllegalStateException to fail the test.
     *
     * @param first     the first boolean expression to be evaluated
     * @param expressions any other boolean expressions to be evaluated
     * @throws IllegalStateException if any {@code expression} is true
     */
    public static void assertAllFalse(boolean first, boolean... expressions) {
        if(first) {
            throw logRuntimeException(new IllegalStateException("The first expression is true when false is expected"));
        }

        int length = expressions.length;
        for (int i = 0; i < length; i++) {
            if (expressions[i]) {
                throw logRuntimeException(new IllegalStateException(
                        tryFormatString("The %dth expression is true when false is expected", 1+i)));
            }
        }
    }

    /**
     * Ensure all of the references are <tt>NULL</tt>s, otherwise throw Exception to fail the test.
     *
     * @param first     the first reference under evaluation that shall be null.
     * @param others    other references that shall contains only null references.
     * @param <T>       type of the reference under evaluation.
     */
    public static <T> void assertAllNull(T first, T... others) {
        if (first != null) {
            throw logRuntimeException(new IllegalStateException(tryFormatString("The first object should be null: %s", TypeHelper.deepToString(first))));
        } else if (others == null) {
            return;
        }

        int length = others.length;
        for (int i = 0; i < length; i++) {
            if (others[i] != null) {
                throw logRuntimeException(new NullPointerException(
                        tryFormatString("The %dth object should be null: %s", i+1, TypeHelper.deepToString(others[i]))));
            }
        }
    }

    /**
     * Ensure none of the references is null, otherwise throw Exception to fail the test.
     *
     * @param reference the first reference under evaluation that shall not be null.
     * @param others    other references that shall contains no null reference.
     * @param <T>       type of the reference under evaluation.
     */
    public static <T> void assertAllNotNull(T reference, T... others) {
        if (reference == null) {
            throw logRuntimeException(new NullPointerException("The first object should not be null"));
        } else if (others == null) {
            throw logRuntimeException(new NullPointerException("The second object should not be null"));
        }

        int length = others.length;
        if (length == 0 && reference.getClass().isArray()) {
            length = Array.getLength(reference);
            for (int i = 0; i < length; i++) {
                if (Array.get(reference, i) == null) {
                    throw logRuntimeException(new NullPointerException(tryFormatString("The %dth object should not be null!", i+1)));
                }
            }
        } else {
            for (int i = 0; i < length; i++) {
                if (others[i] == null) {
                    throw logRuntimeException(new NullPointerException(tryFormatString("The %dth object should not be null!", i+1)));
                }
            }
        }
    }
    //endregion

    //region Perform single evaluation without returning with customised messages
    /**
     * Ensure the single <tt>expression</tt> is <tt>true</tt>, otherwise throw IllegalStateException to fail the test.
     *
     * @param expression the boolean expression that is expected to be <tt>true</tt>
     * @param message   message of the Exception
     * @throws IllegalStateException if the {@code expression} is false
     */
    public static void assertTrue(boolean expression, String message) {
        if (!expression) {
            throw logRuntimeException(new IllegalStateException(message));
        }
    }

    /**
     * Ensure the single <tt>expression</tt> is <tt>false</tt>, otherwise throw IllegalStateException to fail the test.
     *
     * @param expression the boolean expression that is expected to be <tt>false</tt>
     * @param message   message of the Exception
     * @throws IllegalStateException if the {@code expression} is true
     */
    public static void assertFalse(boolean expression, String message) {
        if (expression) {
            throw logRuntimeException(new IllegalStateException(message));
        }
    }

    /**
     * Ensure the single object is <tt>null</tt>, otherwise throw IllegalStateException to fail the test.
     *
     * @param object    the object that is expected to be <tt>null</tt>
     * @param message   message of the Exception
     * @throws IllegalStateException if the {@code expression} is not null
     */
    public static void assertNull(Object object, String message) {
        if (object != null) {
            throw logRuntimeException(new IllegalStateException(message));
        }
    }

    /**
     * Ensure the single object is not <tt>null</tt>, otherwise throw IllegalStateException to fail the test.
     *
     * @param object    the object that is expected to be not <tt>null</tt>
     * @param message   message of the Exception
     * @throws NullPointerException if the {@code expression} is null
     */
    public static void assertNotNull(Object object, String message) {
        if (object == null) {
            throw logRuntimeException(new NullPointerException(message));
        }
    }

    /**
     * Asserts that two objects are equal by themselves or containing same set of values if both are arrays,
     * with/without considering their types.
     * If they are not, an {@link IllegalStateException} is thrown with brief info.
     *
     * @param expected         the expected value to be compared, could be of array or not.
     * @param actual           the actual value to be compared, could be of array or not.
     * @param matchTypeExactly indicates if the types of <code>expected</code> and <code>actual</code> shall be identical.
     * @throws IllegalStateException if {@code expected} is not equal with {@code actual}
     */
    public static void assertEquals(Object expected, Object actual, boolean matchTypeExactly) {
        final String template = "%s !== %s";
        if (expected == null && actual == null) {
            return;
        } else if (expected == null) {
            throw logRuntimeException(new IllegalStateException(
                    tryFormatString(template, "null",  TypeHelper.deepToString(actual))));
        } else if (actual == null) {
            throw logRuntimeException(new IllegalStateException(
                    tryFormatString(template,  TypeHelper.deepToString(expected), "null")));
        }

        if (matchTypeExactly && expected.getClass() != actual.getClass()) {
            throw logRuntimeException(new IllegalStateException(
                    tryFormatString("Expect value of type %s, but actual value is of type %s",
                expected.getClass().getSimpleName(), actual.getClass().getSimpleName())));
        }

        if (!valueEquals(expected, actual)) {
            throw logRuntimeException(new IllegalStateException(
                    tryFormatString(template, TypeHelper.deepToString(expected), TypeHelper.deepToString(actual))));
        }
    }

    /**
     * Asserts that two objects are not equal by themselves nor containing same set of values if both are arrays,
     * with/without considering their types.
     * If they are equal, an {@link IllegalStateException} is thrown with brief info.
     *
     * @param expected         the expected value to be compared, could be of array or not.
     * @param actual           the actual value to be compared, could be of array or not.
     * @param matchTypeExactly indicates if the types of <code>expected</code> and <code>actual</code> shall be identical.
     * @throws IllegalStateException if {@code expected} is equal with {@code actual}
     */
    public static void assertNotEquals(Object expected, Object actual, boolean matchTypeExactly) {
        if (expected == null && actual == null) {
            throw logRuntimeException(new IllegalStateException("Both values are nulls."));
        } else if (expected == null || actual == null) {
            return;
        }

        if (matchTypeExactly && expected.getClass() != actual.getClass()) {
            return;
        }

        if (valueEquals(expected, actual)) {
            throw logRuntimeException(new IllegalStateException(
                    tryFormatString("%s !=== %s", TypeHelper.deepToString(expected), TypeHelper.deepToString(actual))));
        }
    }

    /**
     * Asserts that two objects are equal by themselves or containing same set of values if both are arrays WITHOUT considering their types.
     * If they are not, an {@link IllegalStateException} is thrown with brief info.
     *
     * @param expected the expected value to be compared, could be of array or not.
     * @param actual   the actual value to be compared, could be of array or not.
     * @throws IllegalStateException if {@code expected} is not equal with {@code actual}
     */
    public static void assertEquals(Object expected, Object actual) {
        assertEquals(expected, actual, false);
    }

    /**
     * Asserts that two objects are not equal by themselves nor containing same set of values if both are arrays WITHOUT considering their types.
     * If they are equal, an {@link IllegalStateException} is thrown with brief info.
     *
     * @param expected the expected value to be compared, could be of array or not.
     * @param actual   the actual value to be compared, could be of array or not.
     * @throws IllegalStateException if {@code expected} is equal with {@code actual}
     */
    public static void assertNotEquals(Object expected, Object actual) {
        assertNotEquals(expected, actual, false);
    }
    //endregion


    /**
     * Iterate through the given Collection to check if testing of all its elements passed or not.
     *
     * @param predicate     the Predicate used to test the elements one by one.
     * @param collection    the Collection containing elements of type <tt>T</tt> to be tested.
     * @param <T>           type of the elements of the Collection.
     * @return              <tt>true</tt> if all elements of the given Collection passed the test, otherwise <tt>false</tt>
     */
    public static <T> boolean isAllMatched(Predicate<T> predicate, Collection<T> collection){
        assertAllNotNull(collection, predicate);

        Iterator<T> iterator = collection.iterator();
        int index = 0;
        while (iterator.hasNext()){
            T next = iterator.next();
            index++;
            if(!predicate.test(next)) {
                Logger.V("Failed to test the %dth element '%s'", index, next);
                return false;
            }
        }
        return true;
    }

    /**
     * Iterate through the given target to check if testing of all its elements passed or not.
     *
     * @param predicate     the Predicate used to test the elements one by one.
     * @param target        could be a Collection, an instance or an Array containing elements of type <tt>T</tt> to be tested.
     * @param <T>           type of the elements of the Collection.
     * @return              <tt>true</tt> if all elements of the given Array passed the test, otherwise <tt>false</tt>
     */
    public static <T> boolean isAllMatched(Predicate<T> predicate, Object target){
        assertAllNotNull(target, predicate);
        if(target instanceof Collection){
            return isAllMatched(predicate, (Collection)target);
        } else if (!target.getClass().isArray()){
            return predicate.test((T)target);
        }

        int length = Array.getLength(target);
        for (int i = 0; i < length; i++) {
            T next = (T)Array.get(target, i);
            if(!predicate.test(next)) {
                Logger.V("Failed to test the %dth element '%s'", i, next);
                return false;
            }
        }
        return true;
    }

    /**
     * Iterate through the given Array to check if testing of all its elements passed or not.
     *
     * @param predicate     the Predicate used to test the elements one by one.
     * @param first         the first element to be tested
     * @param others        other elements of the same type to be tested
     * @param <T>           type of the elements of the Collection.
     * @return              <tt>true</tt> if all given elements passed the test, otherwise <tt>false</tt>
     */
    public static <T> boolean isAllMatched(Predicate<T> predicate, T first, T... others){
        assertAllNotNull(predicate);

        T[] aggregation = (T[]) ArrayHelper.mergeVarargsFirst(others, first);
        return isAllMatched(predicate, aggregation);
    }

    /**
     * Iterate through the given Collection to check if any of its elements passed.
     *
     * @param predicate     the Predicate used to test the elements one by one.
     * @param collection    the Collection containing elements of type <tt>T</tt> to be tested.
     * @param <T>           type of the elements of the Collection.
     * @return              <tt>true</tt> if one element of the given Array or Collection passed the test, otherwise <tt>false</tt>
     */
    public static <T> boolean isAnyMatched(Predicate<T> predicate, Collection<T> collection){
        assertAllNotNull(collection, predicate);

        Iterator<T> iterator = collection.iterator();
        int index = 0;
        while (iterator.hasNext()){
            T next = iterator.next();
            index++;
            if(predicate.test(next)) {
                Logger.V("The %dth element '%s' passed the test", index, next);
                return true;
            }
        }
        return false;
    }

    /**
     * Iterate through the given target to check if any of its elements passed.
     *
     * @param predicate     the Predicate used to test the elements one by one.
     * @param target        could be a Collection, an instance or an Array containing elements of type <tt>T</tt> to be tested.
     * @param <T>           type of the elements of the Collection.
     * @return              <tt>true</tt> if one element of the given Array passed the test, otherwise <tt>false</tt>
     */
    public static <T> boolean isAnyMatched(Predicate<T> predicate, Object target){
        assertAllNotNull(target, predicate);
        if(target instanceof Collection){
            return isAllMatched(predicate, (Collection)target);
        } else if (!target.getClass().isArray()){
            return predicate.test((T)target);
        }

        int length = Array.getLength(target);
        for (int i = 0; i < length; i++) {
            T next = (T)Array.get(target, i);
            if(predicate.test(next)) {
                Logger.V("The %dth element '%s' passed the test", i, next);
                return true;
            }
        }
        return false;
    }

    /**
     * Iterate through the given Array to check if any of its elements passed.
     *
     * @param predicate     the Predicate used to test the elements one by one.
     * @param first         the first element to be tested
     * @param others        other elements of the same type to be tested
     * @param <T>           type of the elements of the Collection.
     * @return              <tt>true</tt> if one element of the given Array passed the test, otherwise <tt>false</tt>
     */
    public static <T> boolean isAnyMatched(Predicate<T> predicate, T first, T... others){
        assertAllNotNull(predicate);

        T[] aggregation = (T[]) ArrayHelper.mergeVarargsFirst(others, first);
        return isAnyMatched(predicate, aggregation);
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @param message   message of the Exception
     * @param <R>       type of the argument to be returned.
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <R> R checkNotNull(R reference, String message) {
        if (reference == null) {
            throw logRuntimeException(new NullPointerException(message));
        }
        return reference;
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
    public static <T> T checkNoneNulls(T reference, Object... others) {
        if (reference == null) {
            throw logRuntimeException(new NullPointerException(tryFormatString("the first argument is null")));
        }

        int length = others.length;
        if (length == 0 && reference.getClass().isArray()) {
            length = Array.getLength(reference);
            for (int i = 0; i < length; i++) {
                if (Array.get(reference, i) == null) {
                    throw logRuntimeException(new NullPointerException(tryFormatString("The %dth reference is null!", i+1)));
                }
            }
            return (T) Array.get(reference, 0);
        } else {
            for (int i = 0; i < length; i++) {
                if (others[i] == null) {
                    throw logRuntimeException(new NullPointerException(tryFormatString("The %dth reference is null!", i+1)));
                }
            }
            return reference;
        }
    }

    /**
     * Asserts executing the given function would throw an Exception of specific type, otherwise it would throw an IllegalStateException to fail the test.
     *
     * @param supplier       function returning a result of type <tt>R</tt>.
     * @param exceptionClass type of the Exception to be thrown by the concerned function.
     * @param keywords       optional keywords contained in the message of the Exception.
     * @param <R>            type of the result to be returned by the concerned function.
     * @return type of the returning value of the concerned function.
     */
    public static <R> R assertException(SupplierThrowable<R> supplier, Class<? extends Exception> exceptionClass, Object... keywords) {
        assertAllNotNull(supplier, exceptionClass);

        try {
            supplier.get();
        } catch (Exception e) {
            if (e.getClass() != exceptionClass) {
                throw logRuntimeException(new IllegalStateException(tryFormatString(
                    "Unexpected type of Exception: expected is %s, actual is %s.", exceptionClass.getSimpleName(), e.getClass().getSimpleName())));
            } else if (keywords != null && keywords.length > 0) {
                if (StringHelper.containsAll(e.getMessage(), keywords)) {
                    return null;
                } else {
                    String[] keyStrings = Arrays.stream(keywords).map(o -> o == null ? "null" : o.toString()).toArray(String[]::new);
                    throw logRuntimeException(new IllegalStateException(tryFormatString(
                            "Some keys (%s) are missing: %s", String.join(", ", keyStrings), e.getMessage())));
                }
            } else {
                return null;
            }
        }

        throw logRuntimeException(new IllegalStateException(tryFormatString("No exception of %s thrown", exceptionClass.getSimpleName())));
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
        } catch (Exception e) {
            if (e.getClass() != exceptionClass) {
                throw logRuntimeException(new IllegalStateException(tryFormatString(
                    "Unexpected type of Exception: expected is %s, actual is %s.", exceptionClass.getSimpleName(), e.getClass().getSimpleName())));
            } else if (keywords != null && keywords.length > 0 && !StringHelper.containsAll(e.getMessage(), keywords)) {
                String[] keyStrings = Arrays.stream(keywords).map(o -> o == null ? "null" : o.toString()).toArray(String[]::new);
                throw logRuntimeException(new IllegalStateException(tryFormatString("Some keys (%s) are missing: %s", String.join(", ", keyStrings), e.getMessage())));
            }
            return;
        }

        throw logRuntimeException(new IllegalStateException(tryFormatString("No exception of %s thrown", exceptionClass.getSimpleName())));
    }

    /**
     * Hijack the Logger.Default to test the logged message generated by the concerned function contains all expected outputs
     * @param supplier       function returning a result of type <tt>R</tt>.
     * @param expectations   Objects to be logged by executing the concerned function
     * @param <R>            type of the result to be returned by the concerned function.
     * @return type of the returning value of the concerned function.
     */
    public static <R> R assertLogging(SupplierThrowable<R> supplier, Object... expectations) {
        final InMemoryLogger memoryLogger = new InMemoryLogger();
        CompositeLogger logger = new CompositeLogger(LogLevel.verbose,
            new ConsoleLogger(System.out::println), memoryLogger);
        try (
            Revokable revokable = Logger.useInScope(logger);
            Revokable revokable2 = Logger.setLevelInScope(LogLevel.verbose)
        ) {
            R result = supplier.withHandler(Functions::logAndReturnsNull).get();
            return result;
        } finally {
            String history = memoryLogger.getHistory();
            assertAllTrue(StringHelper.containsAll(history, expectations));
        }
    }

    /**
     * Hijack the Logger.Default to test the logged message generated by the concerned function contains all expected outputs
     * @param runnableThrowable function returning nothing.
     * @param expectations   Objects to be logged by executing the concerned function
     */
    public static void assertLogging(RunnableThrowable runnableThrowable, Object... expectations) {
        final InMemoryLogger memoryLogger = new InMemoryLogger();
        CompositeLogger logger = new CompositeLogger(LogLevel.verbose,
            new ConsoleLogger(System.out::println), memoryLogger);
        try (
            Revokable revokable = Logger.useInScope(logger);
            Revokable revokable2 = Logger.setLevelInScope(LogLevel.verbose)
        ) {
            runnableThrowable.withHandler(Functions::logAndReturnsNull).run();
        } finally {
            String history = memoryLogger.getHistory();
            if(!StringHelper.containsAllIgnoreCase(history, expectations)) {
                Logger.getDefault().log(defaultLogLevel, "'%s' doesn't contain all: %s", history, TypeHelper.deepToString(expectations));
                fail("Failed with containsAllIgnoreCase");
            }
        }
    }
}
