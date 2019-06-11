package io.github.cruisoring.throwables;

import io.github.cruisoring.ofThrowable;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Functional Interface identifying methods, accepting 1 argument and returning value type of boolean
 * while their service logic could throw Exceptions.
 *
 * @param <T> Type of the first argument.
 */
@FunctionalInterface
public interface PredicateThrowable<T> extends ofThrowable<Boolean> {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 7 arguments and returning result of boolean type
     *
     * @param t The first argument of type <code>T</code>.
     * @return The result of applying the given arguments.
     * @throws Exception Any Exception could be thrown by the concerned service logic.
     */
    boolean test(T t) throws Exception;

    /**
     * Execute the given business logic to evaluate the given statement, return the result or
     * handle thrown Exception with the default handler of {@code ofThrowable}..
     *
     * @param t The first argument of type <code>T</code>.
     * @return The result of applying the given arguments.
     */
    default boolean tryTest(T t) {
        try {
            return test(t);
        } catch (Exception e) {
            return handle(e);
        }
    }

    /**
     * Convert the {@code PredicateThrowable<T>} to {@code SupplierThrowable<Boolean>} with given argument.
     *
     * @param t     the argument of type <tt>T</tt>
     * @return the {@code SupplierThrowable<R>} instance invoking the original {@code PredicateThrowable<T>} with required arguments
     */
    default SupplierThrowable<Boolean> asSupplierThrowable(T t) {
        return () -> test(t);
    }

    /**
     * Convert this PredicateThrowable&lt;T,R&gt; to Predicate&lt;T,R&gt; with optional Exception Handler
     *
     * @param exceptionHandlers Optional Exception handlers returning Boolean when Exception is caught
     * @return Converted Function&lt;T,R&gt; that get Exceptions handled with the first of exceptionHandlers if given,
     *          otherwise {@code this::tryTest} if no exceptionHandler specified
     */

    default Predicate<T> withHandler(Function<Exception, Object>... exceptionHandlers) {
        if(exceptionHandlers == null || exceptionHandlers.length == 0) {
            return this::tryTest;
        } else {
            Predicate<T> predicate = (t) -> {
                try {
                    return test(t);
                } catch (Exception e) {
                    return (Boolean)exceptionHandlers[0].apply(e);
                }
            };
            return predicate;
        }
    }

    /**
     * Simplified version of converting the FunctionThrowable&lt;T,R&gt; to Function&lt;T,R&gt; by ignoring the caught Exception
     * and simply returns a pre-defined default value.
     *
     * @param defaultValue Predefined default value.
     * @return Converted {@code Predicate<T>} that would return evaluated result
     */
    default Predicate<T> orElse(boolean defaultValue) {
        Predicate<T> predicate = (t) -> {
            try {
                return test(t);
            } catch (Exception e) {
                return defaultValue;
            }
        };
        return predicate;
    }
}
