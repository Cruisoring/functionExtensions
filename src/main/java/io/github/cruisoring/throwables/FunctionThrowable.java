package io.github.cruisoring.throwables;

import io.github.cruisoring.ofThrowable;

import java.util.function.Function;

/**
 * Functional Interface identifying methods, accepting 1 argument and returning value type of <code>R</code>
 * while their service logic could throw Exceptions.
 *
 * @param <T> Type of the first argument.
 * @param <R> Type of the returned result.
 */
@FunctionalInterface
public interface FunctionThrowable<T, R> extends ofThrowable<R> {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 1 argument and returning result of type <code>R</code>
     *
     * @param t The first argument of type <code>T</code>.
     * @return The result of applying the given arguments.
     * @throws Exception Any Exception could be thrown by the concerned service logic.
     */
    R apply(T t) throws Exception;

    /**
     * Execute the given business logic to return the generated value or
     * handle thrown Exception with the default handler of {@code ofThrowable}.
     *
     * @param t The first argument of type <code>T</code>.
     * @return the result of type <tt>R</tt> if evaluating the given argments successfully,
     * or let the default handler of {@code ofThrowable} to process
     */
    default R tryApply(T t) {
        try {
            return apply(t);
        } catch (Exception e) {
            return handle(e);
        }
    }

    /**
     * Convert the {@code FunctionThrowable<T, R>} to {@code SupplierThrowable<R>} with given argument.
     * @param t     the argument of type <tt>T</tt>
     * @return the {@code SupplierThrowable<R>} instance invoking the original {@code FunctionThrowable<T, R>} with required arguments
     */
    default SupplierThrowable<R> asSupplierThrowable(T t) {
        return () -> apply(t);
    }

    /**
     * Convert the FunctionThrowable&lt;T,R&gt; to Function&lt;T,R&gt; with given Exception Handler
     *
     * @param exceptionHandlers Handler of the caught Exceptions and returns default value
     * @return Converted Function&lt;T,R&gt; that get Exceptions handled with the first of exceptionHandlers if given,
     *          otherwise {@code this::tryAccept} if no exceptionHandler specified
     */

    default Function<T, R> withHandler(Function<Exception, Object>... exceptionHandlers) {
        if(exceptionHandlers == null || exceptionHandlers.length == 0) {
            return this::tryApply;
        } else {
            Function<T, R> function = (t) -> {
                try {
                    return apply(t);
                } catch (Exception e) {
                    return (R)exceptionHandlers[0].apply(e);
                }
            };
            return function;
        }
    }

    /**
     * Simplified version of converting the FunctionThrowable&lt;T,R&gt; to Function&lt;T,R&gt; by ignoring the caught Exception
     * and simply returns a pre-defined default value.
     *
     * @param defaultValue Predefined default value.
     * @return Converted Function&lt;T,R&gt; that get Exceptions handled with the first of exceptionHandlers if given, otherwise {@code this::tryAccept} if no exceptionHandler specified
     */
    default Function<T, R> orElse(R defaultValue) {
        Function<T, R> function = (t) -> {
            try {
                return apply(t);
            } catch (Exception e) {
                return defaultValue;
            }
        };
        return function;
    }
}
