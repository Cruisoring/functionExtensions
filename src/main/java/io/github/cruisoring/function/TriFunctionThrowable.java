package io.github.cruisoring.function;

import java.util.function.Function;

/**
 * Functional Interface identifying methods, accepting 3 arguments and returning result of type <code>R</code>,
 * while their service logic could throw Exceptions.
 *
 * @param <T> Type of the first argument.
 * @param <U> Type of the second argument.
 * @param <V> Type of the third argument.
 * @param <R> Type of the returned result.
 */
@FunctionalInterface
public interface TriFunctionThrowable<T, U, V, R> extends getThrowable<R> {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 3 arguments and returning result of type <code>R</code>
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @return The result of applying the given arguments.
     * @throws Exception Any Exception could be thrown by the concerned service logic.
     */
    R apply(T t, U u, V v) throws Exception;

    /**
     * Execute the given business logic to return the generated value or handle thrown Exception with the default handler of {@code getThrowable}.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @return the result of type <tt>R</tt> if evaluating the given argments successfully, 
     * or let the default handler of {@code getThrowable} to process
     */
    default R tryApply(T t, U u, V v) {
        try {
            return apply(t, u, v);
        } catch (Exception e) {
            return handle(e);
        }
    }

    /**
     * Convert the {@code TriFunctionThrowable<T, U, V, R>} to {@code SupplierThrowable<R>} with given argument.
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @return the {@code SupplierThrowable<R>} instance invoking the original
     *          {@code TriFunctionThrowable<T, U, V, R>} with required arguments
     */
    default SupplierThrowable<R> asSupplierThrowable(T t, U u, V v) {
        return () -> apply(t, u, v);
    }

    /**
     * Convert the {@code TriFunctionThrowable<T, U, V, R>} to {@code TriFunction<T, U, V, R>} with given Exception Handler
     *
     * @param exceptionHandlers Optional Exception Handlers to process the caught Exception with its first memeber if exists
     * @return Converted {@code TriFunction<T, U, V, R>} that get Exceptions handled with the first of exceptionHandlers if given,
     * otherwise {@code this::tryApply} if no exceptionHandler specified
     */
    default TriFunction<T, U, V, R> withHandler(Function<Exception, R>... exceptionHandlers) {
        if(exceptionHandlers == null || exceptionHandlers.length == 0) {
            return this::tryApply;
        } else {
            TriFunction<T, U, V, R> function = (t, u, v) -> {
                try {
                    return apply(t, u, v);
                } catch (Exception e) {
                    return exceptionHandlers[0].apply(e);
                }
            };
            return function;
        }
    }

    /**
     * Simplified version of converting the {@code TriFunctionThrowable<T, U, V, R>} to {@code TriFunction<T, U, V, R>}
     * by ignoring the caught Exception and simply returns a pre-defined default value.
     *
     * @param defaultValue Predefined default value.
     * @return Converted {@code TriFunction<T, U, V, R>} that returns the given defaultValue when exception caught
     */
    default TriFunction<T, U, V, R> orElse(R defaultValue) {
        TriFunction<T, U, V, R> function = (t, u, v) -> {
            try {
                return apply(t, u, v);
            } catch (Exception e) {
                return defaultValue;
            }
        };
        return function;
    }

    /**
     * Represents a function that accepts three arguments and produces a result.
     *
     * @param <T> Type of the first argument.
     * @param <U> Type of the second argument.
     * @param <V> Type of the third argument.
     * @param <R> Type of the result of the function
     */
    @FunctionalInterface
    interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v);
    }
}

