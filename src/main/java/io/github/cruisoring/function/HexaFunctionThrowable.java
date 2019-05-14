package io.github.cruisoring.function;

import java.util.function.BiFunction;

/**
 * Functional Interface identifying methods, accepting 6 arguments and returning result of type <code>R</code>,
 * while their service logic could throw Exceptions.
 *
 * @param <T> Type of the first argument.
 * @param <U> Type of the second argument.
 * @param <V> Type of the third argument.
 * @param <W> Type of the fourth argument.
 * @param <X> Type of the fifth argument.
 * @param <Y> Type of the sixth argument.
 * @param <R> Type of the returned result.
 */
@FunctionalInterface
public interface HexaFunctionThrowable<T, U, V, W, X, Y, R> extends WithValueReturned<R> {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 6 arguments and returning result of type <code>R</code>
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @param x The fifth argument of type <code>X</code>.
     * @param y The sixth argument of type <code>Y</code>.
     * @return The result of applying the given arguments.
     * @throws Exception Any Exception could be thrown by the concerned service logic.
     */
    R apply(T t, U u, V v, W w, X x, Y y) throws Exception;

    /**
     * Execute the given business logic to return the generated value or null if Exception is thrown.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @param x The fifth argument of type <code>X</code>.
     * @param y The sixth argument of type <code>Y</code>.
     * @return the result of type <tt>R</tt> if applying the given argments successfully, or <tt>null</tt> if Exception is thrown.
     */
    default R tryApply(T t, U u, V v, W w, X x, Y y) {
        try {
            return apply(t, u, v, w, x, y);
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Convert the {@code HexaFunctionThrowable<T, U, V, W, X, Y, R>} to {@code SupplierThrowable<R>} with given argument.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @param x The fifth argument of type <code>X</code>.
     * @param y The sixth argument of type <code>Y</code>.
     * @return the {@code SupplierThrowable<R>} instance invoking the original
     * {@code HexaFunctionThrowable<T, U, V, W, X, Y, R>} with required arguments
     */
    default SupplierThrowable<R> asSupplierThrowable(T t, U u, V v, W w, X x, Y y) {
        return () -> apply(t, u, v, w, x, y);
    }

    /**
     * Convert the HexaFunctionThrowable&lt;T,U,V,W,X,Y, R&gt; to HexaFunction&lt;T,U,V,W,X,Y, R&gt; with injected Exception Handler
     *
     * @param exceptionHandler Exception Handler of the caught Exceptions
     * @return Converted HexaFunction&lt;T,U,V,W,X,Y, R&gt; that get Exceptions handled with the exceptionHandler
     */
    default HexaFunction<T, U, V, W, X, Y, R> withHandler(BiFunction<Exception, WithValueReturned, Object> exceptionHandler) {
        HexaFunction<T, U, V, W, X, Y, R> function = (t, u, v, w, x, y) -> {
            try {
                return apply(t, u, v, w, x, y);
            } catch (Exception e) {
                return exceptionHandler == null ? null : (R) exceptionHandler.apply(e, this);
            }
        };
        return function;
    }

    /**
     * Simplified version of converting the HexaFunctionThrowable&lt;T,U,V,W,X,Y, R&gt; to HexaFunction&lt;T,U,V,W,X,Y, R&gt;
     * by ignoring the caught Exception and simply returns a pre-defined default value.
     *
     * @param defaultValue Predefined default value.
     * @return Converted HexaFunction&lt;T,U,V,W,X,Y, R&gt; that returns the given defaultValue when exception caught
     */
    default HexaFunction<T, U, V, W, X, Y, R> orElse(R defaultValue) {
        HexaFunction<T, U, V, W, X, Y, R> function = (t, u, v, w, x, y) -> {
            try {
                return apply(t, u, v, w, x, y);
            } catch (Exception e) {
                return defaultValue;
            }
        };
        return function;
    }

    /**
     * Represents a function that accepts six arguments and produces a result.
     *
     * @param <T> Type of the first argument.
     * @param <U> Type of the second argument.
     * @param <V> Type of the third argument.
     * @param <W> Type of the fourth argument.
     * @param <X> Type of the fifth argument.
     * @param <Y> Type of the sixth argument.
     * @param <R> Type of the result of the function
     */
    @FunctionalInterface
    interface HexaFunction<T, U, V, W, X, Y, R> {
        R apply(T t, U u, V v, W w, X x, Y y);
    }
}
