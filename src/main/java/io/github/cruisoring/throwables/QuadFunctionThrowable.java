package io.github.cruisoring.throwables;

import io.github.cruisoring.ofThrowable;

import java.util.function.Function;

/**
 * Functional Interface identifying methods, accepting 4 arguments and returning result of type <code>R</code>,
 * while their service logic could throw Exceptions.
 *
 * @param <T> Type of the first argument.
 * @param <U> Type of the second argument.
 * @param <V> Type of the third argument.
 * @param <W> Type of the fourth argument.
 * @param <R> Type of the returned result.
 */
@FunctionalInterface
public interface QuadFunctionThrowable<T, U, V, W, R> extends ofThrowable<R> {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 4 arguments and returning result of type <code>R</code>
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @return The result of applying the given arguments.
     * @throws Exception Any Exception could be thrown by the concerned service logic.
     */
    R apply(T t, U u, V v, W w) throws Exception;

    /**
     * Execute the given business logic to return the generated value or handle thrown Exception with the default handler of {@code ofThrowable}.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @return the result of type <tt>R</tt> if evaluating the given argments successfully, or let the default handler of {@code ofThrowable} to process
     */
    default R tryApply(T t, U u, V v, W w) {
        try {
            return apply(t, u, v, w);
        } catch (Exception e) {
            return handle(e);
        }
    }

    /**
     * Convert the {@code QuadFunctionThrowable<T, U, V, W, R>} to {@code SupplierThrowable<R>} with given argument.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @return the {@code SupplierThrowable<R>} instance invoking the original
     *          {@code QuadFunctionThrowable<T, U, V, W, R>} with required arguments
     */
    default SupplierThrowable<R> asSupplierThrowable(T t, U u, V v, W w) {
        return () -> apply(t, u, v, w);
    }

    /**
     * Convert the {@code QuadFunctionThrowable<T, U, V, W, R>} to {@code QuadFunction<T, U, V, W, R>} with given Exception Handler
     *
     * @param exceptionHandlers Optional Exception Handlers to process the caught Exception with its first memeber if exists
     * @return Converted {@code QuadFunction<T, U, V, W, R>} that get Exceptions handled with the first of exceptionHandlers if given,
     * otherwise {@code this::tryApply} if no exceptionHandler specified
     */
    default QuadFunction<T, U, V, W, R> withHandler(Function<Exception, Object>... exceptionHandlers) {
        if(exceptionHandlers == null || exceptionHandlers.length == 0) {
            return this::tryApply;
        } else {
            QuadFunction<T, U, V, W, R> function = (t, u, v, w) -> {
                try {
                    return apply(t, u, v, w);
                } catch (Exception e) {
                    return (R)exceptionHandlers[0].apply(e);
                }
            };
            return function;
        }
    }

    /**
     * Simplified version of converting the {@code QuadFunctionThrowable<T, U, V, W, R>} to {@code QuadFunction<T, U, V, W, R>}
     * by ignoring the caught Exception and simply returns a pre-defined default value.
     *
     * @param defaultValue Predefined default value.
     * @return Converted {@code QuadFunction<T, U, V, W, R>} that returns the given defaultValue when exception caught
     */
    default QuadFunction<T, U, V, W, R> orElse(R defaultValue) {
        QuadFunction<T, U, V, W, R> function = (t, u, v, w) -> {
            try {
                return apply(t, u, v, w);
            } catch (Exception e) {
                return defaultValue;
            }
        };
        return function;
    }

    /**
     * Represents a function that accepts four arguments and produces a result.
     *
     * @param <T> Type of the first argument.
     * @param <U> Type of the second argument.
     * @param <V> Type of the third argument.
     * @param <W> Type of the fourth argument.
     * @param <R> Type of the result of the function
     */
    @FunctionalInterface
    interface QuadFunction<T, U, V, W, R> {
        R apply(T t, U u, V v, W w);
    }
}
