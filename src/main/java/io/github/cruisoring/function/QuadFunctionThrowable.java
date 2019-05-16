package io.github.cruisoring.function;

import java.util.function.BiFunction;

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
public interface QuadFunctionThrowable<T, U, V, W, R> extends WithValueReturned<R> {
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
     * Execute the given business logic to return the generated value or null if Exception is thrown.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @return the result of type <tt>R</tt> if applying the given argments successfully, or <tt>null</tt> if Exception is thrown.
     */
    default R tryApply(T t, U u, V v, W w) {
        try {
            return apply(t, u, v, w);
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Convert the {@code QuadFunctionThrowable<T, U, V, W, R>} to {@code SupplierThrowable<R>} with given argument.
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
     * Convert the QuadFunctionThrowable&lt;T,U,V,W, R&gt; to QuadFunction&lt;T,U,V,W, R&gt; with injected Exception Handler
     *
     * @param exceptionHandler Exception Handler of the caught Exceptions
     * @return Converted QuadFunction&lt;T,U,V,W, R&gt; that get Exceptions handled with the exceptionHandler
     */
    default QuadFunction<T, U, V, W, R> withHandler(BiFunction<Exception, WithValueReturned, Object> exceptionHandler) {
        QuadFunction<T, U, V, W, R> function = (t, u, v, w) -> {
            try {
                return apply(t, u, v, w);
            } catch (Exception e) {
                return exceptionHandler == null ? null : (R) exceptionHandler.apply(e, this);
            }
        };
        return function;
    }

    /**
     * Simplified version of converting the QuadFunctionThrowable&lt;T,U,V,W, R&gt; to QuadFunction&lt;T,U,V,W, R&gt;
     * by ignoring the caught Exception and simply returns a pre-defined default value.
     *
     * @param defaultValue Predefined default value.
     * @return Converted QuadFunction&lt;T,U,V,W, R&gt; that returns the given defaultValue when exception caught
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
