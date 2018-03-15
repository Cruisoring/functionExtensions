package com.easyworks.function;

import java.util.Objects;
import java.util.function.Function;

/**
 * Functional Interface identifying methods, accepting 4 arguments and returning result of type <code>R</code>,
 * while their service logic could throw Exceptions.
 * @param <T>   Type of the first argument.
 * @param <U>   Type of the second argument.
 * @param <V>   Type of the third argument.
 * @param <W>   Type of the fourth argument.
 * @param <R>   Type of the returned result.
 */
@FunctionalInterface
public interface QuadFunctionThrowable<T,U,V,W,R> extends Supplierable {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 4 arguments and returning result of type <code>R</code>
     * @param t     The first argument of type <code>T</code>.
     * @param u     The second argument of type <code>U</code>.
     * @param v     The third argument of type <code>V</code>.
     * @param w     The fourth argument of type <code>W</code>.
     * @return      The result of applying the given arguments.
     * @throws Exception    Any Exception could be thrown by the concerned service logic.
     */
    R apply(T t, U u, V v, W w) throws Exception;

    /**
     * Convert the above <code>apply</code> method to <code>SupplierThrowable</code> with all 4 arguments
     * @param t     The first argument of type <code>T</code>.
     * @param u     The second argument of type <code>U</code>.
     * @param v     The third argument of type <code>V</code>.
     * @param w     The fourth argument of type <code>W</code>.
     * @return      The converted <code>SupplierThrowable</code> by applying the 4 given arguments.
     */
    default SupplierThrowable<R> asSupplier(T t, U u, V v, W w){
        return () -> apply(t, u, v, w);
    }

    default QuadFunction<T,U,V,W, R> orElse(Function<Exception, R> exceptionHanlder){
        Objects.requireNonNull(exceptionHanlder);
        return (t, u, v, w) -> {
            try {
                return apply(t, u, v, w);
            } catch (Exception e) {
                return exceptionHanlder.apply(e);
            }
        };
    }

    @FunctionalInterface
    interface QuadFunction<T,U,V,W, R> extends Supplierable {
        R apply(T t, U u, V v, W w);
    }
}
