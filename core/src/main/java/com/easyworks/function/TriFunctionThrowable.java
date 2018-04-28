package com.easyworks.function;

import java.util.function.BiFunction;

/**
 * Functional Interface identifying methods, accepting 3 arguments and returning result of type <code>R</code>,
 * while their service logic could throw Exceptions.
 * @param <T>   Type of the first argument.
 * @param <U>   Type of the second argument.
 * @param <V>   Type of the third argument.
 * @param <R>   Type of the returned result.
 */
@FunctionalInterface
public interface TriFunctionThrowable<T,U,V, R> extends WithValueReturned<R> {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 3 arguments and returning result of type <code>R</code>
     * @param t     The first argument of type <code>T</code>.
     * @param u     The second argument of type <code>U</code>.
     * @param v     The third argument of type <code>V</code>.
     * @return      The result of applying the given arguments.
     * @throws Exception    Any Exception could be thrown by the concerned service logic.
     */
    R apply(T t, U u, V v) throws Exception;

    /**
     * Convert the TriFunctionThrowable&lt;T,U,V, R&gt; to TriFunction&lt;T,U,V, R&gt; with injected Exception Handler
     * @param exceptionHandler  Exception Handler of the caught Exceptions
     * @return  Converted TriFunction&lt;T,U,V, R&gt; that get Exceptions handled with the exceptionHandler
     */
    default TriFunction<T,U,V, R> withHandler(BiFunction<Exception, WithValueReturned, Object> exceptionHandler){
        TriFunction<T,U,V, R> function = (t, u, v) -> {
            try {
                return apply(t, u, v);
            } catch (Exception e) {
                return exceptionHandler == null ? null : (R) exceptionHandler.apply(e, this);
            }
        };
        return function;
    }

    /**
     * Simplified version of converting the TriFunctionThrowable&lt;T,U,V, R&gt; to TriFunction&lt;T,U,V, R&gt;
     * by ignoring the caught Exception and simply returns a pre-defined default value.
     * @param defaultValue  Predefined default value.
     * @return Converted TriFunction&lt;T,U,V, R&gt; that returns the given defaultValue when exception caught
     */
    default TriFunction<T,U,V, R> orElse(R defaultValue){
        TriFunction<T,U,V, R> function = (t, u, v) -> {
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
     * @param <T>   Type of the first argument.
     * @param <U>   Type of the second argument.
     * @param <V>   Type of the third argument.
     * @param <R>   Type of the result of the function
     */
    @FunctionalInterface
    interface TriFunction<T,U,V, R> {
        R apply(T t, U u, V v);
    }
}
