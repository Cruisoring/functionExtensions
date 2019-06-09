package io.github.cruisoring.function;

import java.util.function.Function;

/**
 * Functional Interface identifying methods, accepting 7 arguments and returning result of type <code>R</code>,
 * while their service logic could throw Exceptions.
 *
 * @param <T> Type of the first argument.
 * @param <U> Type of the second argument.
 * @param <V> Type of the third argument.
 * @param <W> Type of the fourth argument.
 * @param <X> Type of the fifth argument.
 * @param <Y> Type of the sixth argument.
 * @param <Z> Type of the seventh argument.
 * @param <R> Type of the returned result.
 */
@FunctionalInterface
public interface HeptaFunctionThrowable<T, U, V, W, X, Y, Z, R> extends getThrowable<R> {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 7 arguments and returning result of type <code>R</code>
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @param x The fifth argument of type <code>X</code>.
     * @param y The sixth argument of type <code>Y</code>.
     * @param z The seventh argument of type <code>Z</code>.
     * @return The result of applying the given arguments.
     * @throws Exception Any Exception could be thrown by the concerned service logic.
     */
    R apply(T t, U u, V v, W w, X x, Y y, Z z) throws Exception;

    /**
     * Execute the given business logic to return the generated value or
     * handle thrown Exception with the default handler of {@code getThrowable}.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @param x The fifth argument of type <code>X</code>.
     * @param y The sixth argument of type <code>Y</code>.
     * @param z The seventh argument of type <code>Z</code>.
     * @return the result of type <tt>R</tt> if evaluating the given argments successfully, or let the default handler of {@code getThrowable} to process
     */
    default R tryApply(T t, U u, V v, W w, X x, Y y, Z z) {
        try {
            return apply(t, u, v, w, x, y, z);
        } catch (Exception e) {
            return handle(e);
        }
    }

    /**
     * Convert the {@code HeptaFunctionThrowable<T, U, V, W, X, Y, Z, R>} to {@code SupplierThrowable<R>} with given argument.
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @param x The fifth argument of type <code>X</code>.
     * @param y The sixth argument of type <code>Y</code>.
     * @param z The seventh argument of type <code>Z</code>.
     * @return the {@code SupplierThrowable<R>} instance invoking the original
     *          {@code HeptaFunctionThrowable<T, U, V, W, X, Y, Z, R>} with required arguments
     */
    default SupplierThrowable<R> asSupplierThrowable(T t, U u, V v, W w, X x, Y y, Z z) {
        return () -> apply(t, u, v, w, x, y, z);
    }

    /**
     * Convert this {@code HeptaFunctionThrowable<T, U, V, W, X, Y, Z, R>} to {@code HeptaFunction<T, U, V, W, X, Y, Z, R>} with optional Exception Handler
     *
     * @param exceptionHandlers optional Handler of the caught Exception with same returning type
     * @return Converted {@code HeptaFunction<T, U, V, W, X, Y, Z, R>} that get Exceptions handled with the first of exceptionHandlers if given,
     *      otherwise {@code this::tryApply} if no exceptionHandler specified
     */
    default HeptaFunction<T, U, V, W, X, Y, Z, R> withHandler(Function<Exception, R>... exceptionHandlers) {
        if(exceptionHandlers == null || exceptionHandlers.length == 0) {
            return this::tryApply;
        } else {
            HeptaFunction<T, U, V, W, X, Y, Z, R> function = (t, u, v, w, x, y, z) -> {
                try {
                    return apply(t, u, v, w, x, y, z);
                } catch (Exception e) {
                    return exceptionHandlers[0].apply(e);
                }
            };
            return function;
        }
    }

    /**
     * Simplified version of converting the HeptaFunctionThrowable&lt;T,U,V,W,X,Y,Z, R&gt; to {@code HeptaFunction<T, U, V, W, X, Y, Z, R>}
     * by ignoring the caught Exception and simply returns a pre-defined default value.
     *
     * @param defaultValue Predefined default value.
     * @return Converted {@code HeptaFunction<T, U, V, W, X, Y, Z, R>} that returns the given defaultValue when exception caught
     */
    default HeptaFunction<T, U, V, W, X, Y, Z, R> orElse(R defaultValue) {
        HeptaFunction<T, U, V, W, X, Y, Z, R> function = (t, u, v, w, x, y, z) -> {
            try {
                return apply(t, u, v, w, x, y, z);
            } catch (Exception e) {
                return defaultValue;
            }
        };
        return function;
    }

    /**
     * Represents a function that accepts seven arguments and produces a result.
     *
     * @param <T> Type of the first argument.
     * @param <U> Type of the second argument.
     * @param <V> Type of the third argument.
     * @param <W> Type of the fourth argument.
     * @param <X> Type of the fifth argument.
     * @param <Y> Type of the sixth argument.
     * @param <Z> Type of the seventh argument.
     * @param <R> Type of the result of the function
     */
    @FunctionalInterface
    interface HeptaFunction<T, U, V, W, X, Y, Z, R> {
        R apply(T t, U u, V v, W w, X x, Y y, Z z);
    }
}
