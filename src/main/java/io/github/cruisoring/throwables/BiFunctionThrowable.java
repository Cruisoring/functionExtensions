package io.github.cruisoring.throwables;

import io.github.cruisoring.OfThrowable;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Functional Interface identifying methods, accepting 2 arguments and returning result of type <code>R</code>,
 * while their service logic could throw Exceptions.
 *
 * @param <T> Type of the first argument.
 * @param <U> Type of the second argument.
 * @param <R> Type of the returned result.
 */
@FunctionalInterface
public interface BiFunctionThrowable<T, U, R> extends OfThrowable<R> {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 2 arguments and returning result of type <code>R</code>
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @return The result of applying the given arguments.
     * @throws Exception Any Exception could be thrown by the concerned service logic.
     */
    R apply(T t, U u) throws Exception;

    /**
     * Execute the given business logic to return the generated value or
     * handle thrown Exception with the default handler of {@code OfThrowable}.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @return the result of type <tt>R</tt> if evaluating the given argments successfully, or let the default handler of {@code OfThrowable} to process
     */
    default R tryApply(T t, U u) {
        try {
            return apply(t, u);
        } catch (Exception cause) {
            return handle(cause);
        }
    }

    /**
     * Convert the {@code BiFunctionThrowable<T, U, R>} to {@code SupplierThrowable<R>} with given argument.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @return the {@code SupplierThrowable<R>} instance invoking the original {@code BiFunctionThrowable<T, U, R>} with required arguments
     */
    default SupplierThrowable<R> asSupplierThrowable(T t, U u) {
        return () -> apply(t, u);
    }

    /**
     * Convert the BiFunctionThrowable&lt;T,U,R&gt; to BiFunction&lt;T,U,R&gt; with optional Exception Handler
     *
     * @param exceptionHandlers optional Handler of the caught Exception with same returning type
     * @return Converted BiFunction&lt;T,U,R&gt; that get Exceptions handled with the first of exceptionHandlers if given,
     *      otherwise {@code this::tryApply} if no exceptionHandler specified
     */
    default BiFunction<T, U, R> withHandler(Function<Exception, Object>... exceptionHandlers) {
        if(exceptionHandlers == null || exceptionHandlers.length == 0) {
            return this::tryApply;
        } else {
            BiFunction<T, U, R> function = (t, u) -> {
                try {
                    return apply(t, u);
                } catch (Exception e) {
                    return (R)exceptionHandlers[0].apply(e);
                }
            };
            return function;
        }
    }

    /**
     * Simplified version of converting the BiFunctionThrowable&lt;T,U,R&gt; to BiFunction&lt;T,U,R&gt; by ignoring the caught Exception
     * and simply returns a given default value.
     *
     * @param defaultValue Predefined default value.
     * @return Converted BiFunction&lt;T,U,R&gt; that simply return the given defaultValue
     */
    default BiFunction<T, U, R> orElse(R defaultValue) {
        BiFunction<T, U, R> function = (t, u) -> {
            try {
                return apply(t, u);
            } catch (Exception e) {
                return defaultValue;
            }
        };
        return function;
    }
}
