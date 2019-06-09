package io.github.cruisoring.function;

import java.util.function.Function;

/**
 * Functional Interface identifying methods, accepting 5 arguments and returning result of type <code>R</code>,
 * while their service logic could throw Exceptions.
 *
 * @param <T> Type of the first argument.
 * @param <U> Type of the second argument.
 * @param <V> Type of the third argument.
 * @param <W> Type of the fourth argument.
 * @param <X> Type of the fifth argument.
 * @param <R> Type of the returned result.
 */
@FunctionalInterface
public interface PentaFunctionThrowable<T, U, V, W, X, R> extends getThrowable<R> {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 5 arguments and returning result of type <code>R</code>
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @param x The fifth argument of type <code>X</code>.
     * @return The result of applying the given arguments.
     * @throws Exception Any Exception could be thrown by the concerned service logic.
     */
    R apply(T t, U u, V v, W w, X x) throws Exception;

    /**
     * Execute the given business logic to return the generated value or handle thrown Exception with the default handler of {@code getThrowable}.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @param x The fifth argument of type <code>X</code>.
     * @return the result of type <tt>R</tt> if evaluating the given argments successfully, or let the default handler of {@code getThrowable} to process
     */
    default R tryApply(T t, U u, V v, W w, X x) {
        try {
            return apply(t, u, v, w, x);
        } catch (Exception e) {
            return handle(e);
        }
    }

    /**
     * Convert the {@code PentaFunctionThrowable<T, U, V, W, X, R>} to {@code SupplierThrowable<R>} with given argument.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @param x The fifth argument of type <code>X</code>.
     * @return the {@code SupplierThrowable<R>} instance invoking the original
     *          {@code PentaFunctionThrowable<T, U, V, W, X, R>} with required arguments
     */
    default SupplierThrowable<R> asSupplierThrowable(T t, U u, V v, W w, X x) {
        return () -> apply(t, u, v, w, x);
    }

    /**
     * Convert the {@code PentaFunctionThrowable<T, U, V, W, X, R>} to {@code PentaFunction<T, U, V, W, X, R>} with optional Exception Handler
     *
     * @param exceptionHandlers optional Handler of the caught Exception with same returning type
     * @return Converted {@code PentaFunction<T, U, V, W, X, R>} that get Exceptions handled with the first of exceptionHandlers if given,
     * otherwise {@code this::tryApply} if no exceptionHandler specified
     */
    default PentaFunction<T, U, V, W, X, R> withHandler(Function<Exception, R>... exceptionHandlers) {
        if(exceptionHandlers == null || exceptionHandlers.length == 0) {
            return this::tryApply;
        } else {
            PentaFunction<T, U, V, W, X, R> function = (t, u, v, w, x) -> {
                try {
                    return apply(t, u, v, w, x);
                } catch (Exception e) {
                    return exceptionHandlers[0].apply(e);
                }
            };
            return function;
        }
    }

    /**
     * Simplified version of converting the {@code PentaFunctionThrowable<T, U, V, W, X, R>} to PentaFunction<T, U, V, W, X, R>
     * by ignoring the caught Exception and simply returns a pre-defined default value.
     *
     * @param defaultValue Predefined default value.
     * @return Converted PentaFunction<T, U, V, W, X, R> that returns the given defaultValue when exception caught
     */
    default PentaFunction<T, U, V, W, X, R> orElse(R defaultValue) {
        PentaFunction<T, U, V, W, X, R> function = (t, u, v, w, x) -> {
            try {
                return apply(t, u, v, w, x);
            } catch (Exception e) {
                return defaultValue;
            }
        };
        return function;
    }

    /**
     * Represents a function that accepts five arguments and produces a result.
     *
     * @param <T> Type of the first argument.
     * @param <U> Type of the second argument.
     * @param <V> Type of the third argument.
     * @param <W> Type of the fourth argument.
     * @param <X> Type of the fifth argument.
     * @param <R> Type of the result of the function
     */
    @FunctionalInterface
    interface PentaFunction<T, U, V, W, X, R> {
        R apply(T t, U u, V v, W w, X x);
    }
}
