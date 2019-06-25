package io.github.cruisoring.throwables;

import io.github.cruisoring.OfThrowable;

import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Functional Interface identifying methods, accepting 2 arguments and returning value type of boolean
 * while their service logic could throw Exceptions.
 *
 * @param <T> Type of the first argument.
 * @param <U> Type of the second argument.
 */
@FunctionalInterface
public interface BiPredicateThrowable<T, U> extends OfThrowable<Boolean> {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 2 arguments and returning result of boolean type
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @return The result of applying the given arguments.
     * @throws Exception Any Exception could be thrown by the concerned service logic.
     */
    boolean test(T t, U u) throws Exception;


    /**
     * Get the {@code BiPredicateThrowable<T, U>} instance that would return exactly opposite result than this
     * {@code BiPredicateThrowable<T, U>} when evaluating two arguments.
     * @return a new {@code BiPredicateThrowable<T, U>} instance accepting 2 arguments to return opposite boolean result.
     */
    default BiPredicateThrowable<T, U> reversed() {
        BiPredicateThrowable<T, U> opposite = (t, u) -> !test(t, u);
        return opposite;
    }

    /**
     * Execute the given business logic to test the given 2 arguments, return the result or
     * handle thrown Exception with the default handler of {@code OfThrowable}.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @return The result by testing the given arguments,
     *      or result returned by the default Exception handler of {@code OfThrowable}.
     */
    default boolean tryTest(T t, U u) {
        try {
            return test(t, u);
        } catch (Exception e) {
            return handle(e);
        }
    }

    /**
     * Convert the {@code BiPredicateThrowable<T, U>} to {@code SupplierThrowable<Boolean>} with given arguments.
     *
     * @param t     the argument of type <tt>T</tt>
     * @param u The second argument of type <code>U</code>.
     * @return the {@code SupplierThrowable<R>} instance invoking the original {@code PredicateThrowable<T>} with required arguments
     */
    default SupplierThrowable<Boolean> asSupplierThrowable(T t, U u) {
        return () -> test(t, u);
    }

    /**
     * Convert this {@code BiPredicateThrowable<T, U>} to {@code BiPredicate<T, U>} with optional Exception Handler
     *
     * @param exceptionHandlers Optional Exception handlers returning Boolean when Exception is caught
     * @return Converted Function&lt;T,R&gt; that get Exceptions handled with the first of exceptionHandlers if given,
     *          otherwise {@code this::tryTest} if no exceptionHandler specified
     */

    default BiPredicate<T,U> withHandler(Function<Exception, Object>... exceptionHandlers) {
        if(exceptionHandlers == null || exceptionHandlers.length == 0) {
            return this::tryTest;
        } else {
            BiPredicate<T,U> predicate = (t, u) -> {
                try {
                    return test(t, u);
                } catch (Exception e) {
                    return (Boolean) exceptionHandlers[0].apply(e);
                }
            };
            return predicate;
        }
    }

    /**
     * Convert this {@code BiPredicateThrowable<T, U>} to {@code BiPredicate<T, U>} by ignoring the caught Exception
     * and simply returns a pre-defined default value.
     *
     * @param defaultValue Predefined default value.
     * @return Converted {@code BiPredicate<T, U>} that get Exceptions handled with the first of exceptionHandlers if given, otherwise {@code this::tryAccept} if no exceptionHandler specified
     */
    default BiPredicate<T,U> orElse(boolean defaultValue) {
        BiPredicate<T,U> predicate = (t, u) -> {
            try {
                return test(t, u);
            } catch (Exception e) {
                return defaultValue;
            }
        };
        return predicate;
    }
}
