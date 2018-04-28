package com.easyworks.function;

import java.util.function.BiFunction;

/**
 * Functional Interface identifying methods, accepting 5 arguments and returning result of type <code>R</code>,
 * while their service logic could throw Exceptions.
 * @param <T>   Type of the first argument.
 * @param <U>   Type of the second argument.
 * @param <V>   Type of the third argument.
 * @param <W>   Type of the fourth argument.
 * @param <X>   Type of the fifth argument.
 * @param <R>   Type of the returned result.
 */
@FunctionalInterface
public interface PentaFunctionThrowable<T,U,V,W,X, R> extends WithValueReturned<R> {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 5 arguments and returning result of type <code>R</code>
     * @param t     The first argument of type <code>T</code>.
     * @param u     The second argument of type <code>U</code>.
     * @param v     The third argument of type <code>V</code>.
     * @param w     The fourth argument of type <code>W</code>.
     * @param x     The fifth argument of type <code>X</code>.
     * @return      The result of applying the given arguments.
     * @throws Exception    Any Exception could be thrown by the concerned service logic.
     */
    R apply(T t, U u, V v, W w, X x) throws Exception;

    /**
     * Convert the PentaFunctionThrowable&lt;T,U,V,W,X, R&gt; to PentaFunction&lt;T,U,V,W,X, R&gt; with injected Exception Handler
     * @param exceptionHandler  Exception Handler of the caught Exceptions
     * @return  Converted PentaFunction&lt;T,U,V,W,X, R&gt; that get Exceptions handled with the exceptionHandler
     */
    default PentaFunction<T,U,V,W,X, R> withHandler(BiFunction<Exception, WithValueReturned, Object> exceptionHandler){
        PentaFunction<T,U,V,W,X, R> function = (t, u, v, w, x) -> {
            try {
                return apply(t, u, v, w, x);
            } catch (Exception e) {
                return exceptionHandler == null ? null : (R) exceptionHandler.apply(e, this);
            }
        };
        return function;
    }

    /**
     * Simplified version of converting the HexaFunctionThrowable&lt;T,U,V,W,X,Y, R&gt; to HexaFunction&lt;T,U,V,W,X,Y, R&gt;
     * by ignoring the caught Exception and simply returns a pre-defined default value.
     * @param defaultValue  Predefined default value.
     * @return Converted HexaFunction&lt;T,U,V,W,X,Y, R&gt; that returns the given defaultValue when exception caught
     */
    default PentaFunction<T,U,V,W,X, R> orElse(R defaultValue){
        PentaFunction<T,U,V,W,X, R> function = (t, u, v, w, x) -> {
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
     * @param <T>   Type of the first argument.
     * @param <U>   Type of the second argument.
     * @param <V>   Type of the third argument.
     * @param <W>   Type of the fourth argument.
     * @param <X>   Type of the fifth argument.
     * @param <R>   Type of the result of the function
     */
    @FunctionalInterface
    interface PentaFunction<T,U,V,W,X, R> {
        R apply(T t, U u, V v, W w, X x);
    }
}
