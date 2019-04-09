package io.github.cruisoring.function;

import java.util.function.BiFunction;

/**
 * Functional Interface identifying methods, accepting 2 arguments and returning result of type <code>R</code>,
 * while their service logic could throw Exceptions.
 *
 * @param <T> Type of the first argument.
 * @param <U> Type of the second argument.
 * @param <R> Type of the returned result.
 */
@FunctionalInterface
public interface BiFunctionThrowable<T, U, R> extends WithValueReturned<R> {
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
     * Convert the BiFunctionThrowable&lt;T,U,R&gt; to BiFunction&lt;T,U,R&gt; with injected Exception Handler
     *
     * @param exceptionHandler Handler of the caught Exceptions and returns default value
     * @return Converted BiFunction&lt;T,U,R&gt; that get Exceptions handled with the exceptionHandler
     */
    default BiFunction<T, U, R> withHandler(BiFunction<Exception, WithValueReturned, Object> exceptionHandler) {
        BiFunction<T, U, R> function = (t, u) -> {
            try {
                return apply(t, u);
            } catch (Exception e) {
                return exceptionHandler == null ? null : (R) exceptionHandler.apply(e, this);
            }
        };
        return function;
    }

    /**
     * Simplified version of converting the BiFunctionThrowable&lt;T,U,R&gt; to BiFunction&lt;T,U,R&gt; by ignoring the caught Exception
     * and simply returns a pre-defined default value.
     *
     * @param defaultValue Predefined default value.
     * @return Converted BiFunction&lt;T,U,R&gt; that get Exceptions handled with the exceptionHandler
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
