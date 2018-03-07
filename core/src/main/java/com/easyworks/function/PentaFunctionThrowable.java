package com.easyworks.function;

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
public interface PentaFunctionThrowable<T,U,V,W,X,R> extends AbstractThrowable {
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
     * Convert the above <code>apply</code> method to <code>SupplierThrowable</code> with all 5 arguments
     * @param t     The first argument of type <code>T</code>.
     * @param u     The second argument of type <code>U</code>.
     * @param v     The third argument of type <code>V</code>.
     * @param w     The fourth argument of type <code>W</code>.
     * @param x     The fifth argument of type <code>X</code>.
     * @return      The converted <code>SupplierThrowable</code> by applying the 5 given arguments.
     */
    default SupplierThrowable<R> asSupplier(T t, U u, V v, W w, X x){
        return () -> apply(t, u, v, w, x);
    }
}
