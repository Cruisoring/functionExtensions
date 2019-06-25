package io.github.cruisoring.throwables;

import io.github.cruisoring.OfThrowable;

import java.util.function.Function;

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
public interface HexaFunctionThrowable<T, U, V, W, X, Y, R> extends OfThrowable<R> {
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
     * Execute the given business logic to return the generated value or handle thrown Exception
     * with the default handler of {@code OfThrowable}.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @param x The fifth argument of type <code>X</code>.
     * @param y The sixth argument of type <code>Y</code>.
     * @return the result of type <tt>R</tt> if evaluating the given argments successfully,
     * or let the default handler of {@code OfThrowable} to process
     */
    default R tryApply(T t, U u, V v, W w, X x, Y y) {
        try {
            return apply(t, u, v, w, x, y);
        } catch (Exception e) {
            return handle(e);
        }
    }

    /**
     * Convert the {@code HexaFunctionThrowable<T, U, V, W, X, Y, R>} to {@code SupplierThrowable<R>} with given arguments.
     * 
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @param x The fifth argument of type <code>X</code>.
     * @param y The sixth argument of type <code>Y</code>.
     * @return the {@code SupplierThrowable<R>} instance invoking the original
     *          {@code HexaFunctionThrowable<T, U, V, W, X, Y, R>} with required arguments
     */
    default SupplierThrowable<R> asSupplierThrowable(T t, U u, V v, W w, X x, Y y) {
        return () -> apply(t, u, v, w, x, y);
    }

    /**
     * Convert the {@code HexaFunctionThrowable<T, U, V, W, X, Y, R>} to {@code HexaFunction<T, U, V, W, X, Y, R>} with optional Exception Handler
     *
     * @param exceptionHandlers optional Handler of the caught Exception with same returning type
     * @return Converted {@code HexaFunction<T, U, V, W, X, Y, R>} that get Exceptions handled with the first of exceptionHandlers if given,
     *          otherwise {@code this::tryApply} if no exceptionHandler specified
     */
    default HexaFunction<T, U, V, W, X, Y, R> withHandler(Function<Exception, Object>... exceptionHandlers) {
        if(exceptionHandlers == null || exceptionHandlers.length == 0) {
            return this::tryApply;
        } else {
            HexaFunction<T, U, V, W, X, Y, R> function = (t, u, v, w, x, y) -> {
                try {
                    return apply(t, u, v, w, x, y);
                } catch (Exception e) {
                    return (R)exceptionHandlers[0].apply(e);
                }
            };
            return function;
        }
    }

    /**
     * Simplified version of converting the {@code HexaFunctionThrowable<T, U, V, W, X, Y, R>} to {@code HexaFunction<T, U, V, W, X, Y, R>}
     * by ignoring the caught Exception and simply returns a pre-defined default value.
     *
     * @param defaultValue Predefined default value.
     * @return Converted {@code HexaFunction<T, U, V, W, X, Y, R>} that returns the given defaultValue when exception caught
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
