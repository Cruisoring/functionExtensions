package io.github.cruisoring.function;

import io.github.cruisoring.logger.Logger;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Functional Interface identifying methods, accepting 1 argument and returning value type of <code>R</code>
 * while their service logic could throw Exceptions.
 *
 * @param <T> Type of the first argument.
 * @param <R> Type of the returned result.
 */
@FunctionalInterface
public interface FunctionThrowable<T, R> extends WithValueReturned<R> {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 1 argument and returning result of type <code>R</code>
     *
     * @param t The first argument of type <code>T</code>.
     * @return The result of applying the given arguments.
     * @throws Exception Any Exception could be thrown by the concerned service logic.
     */
    R apply(T t) throws Exception;

    /**
     * Execute the given business logic to return the generated value or null if Exception is thrown.
     *
     * @param t The first argument of type <code>T</code>.
     * @return the result of type <tt>R</tt> if applying the given argments successfully, or <tt>null</tt> if Exception is thrown.
     */
    default R tryApply(T t) {
        try {
            return apply(t);
        } catch (Exception ignored) {
            return null;
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
     * Convert the FunctionThrowable&lt;T,R&gt; to Function&lt;T,R&gt; with injected Exception Handler
     *
     * @param exceptionHandler Handler of the caught Exceptions and returns default value
     * @return Converted Function&lt;T,R&gt; that get Exceptions handled with the exceptionHandler
     */

    default Function<T, R> withHandler(BiFunction<Exception, WithValueReturned, Object> exceptionHandler) {
        Function<T, R> function = (t) -> {
            try {
                return apply(t);
            } catch (Exception e) {
                return exceptionHandler == null ? null : (R) exceptionHandler.apply(e, this);
            }
        };
        return function;
    }

    /**
     * Simplified version of converting the FunctionThrowable&lt;T,R&gt; to Function&lt;T,R&gt; by ignoring the caught Exception
     * and simply returns a pre-defined default value.
     *
     * @param defaultValue Predefined default value.
     * @return Converted Function&lt;T,R&gt; that get Exceptions handled with the exceptionHandler
     */
    default Function<T, R> orElse(R defaultValue) {
        Function<T, R> function = (t) -> {
            try {
                return apply(t);
            } catch (Exception e) {
                Logger.D(e);
                return defaultValue;
            }
        };
        return function;
    }

}
