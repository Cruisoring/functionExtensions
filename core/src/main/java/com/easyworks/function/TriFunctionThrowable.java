package com.easyworks.function;

import java.util.function.Function;

/**
 * Functional Interface identifying methods, accepting 3 arguments and returning result of type <code>R</code>,
 * while their service logic could throw Exceptions.
 * @param <T>   Type of the first argument.
 * @param <U>   Type of the second argument.
 * @param <V>   Type of the third argument.
 * @param <R>   Type of the returned result.
 */
@FunctionalInterface
public interface TriFunctionThrowable<T,U,V,R> extends Supplierable<R> {
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
     * Convert the above <code>apply</code> method to <code>SupplierThrowable</code> with all 3 arguments
     * @param t     The first argument of type <code>T</code>.
     * @param u     The second argument of type <code>U</code>.
     * @param v     The third argument of type <code>V</code>.
     * @return      The converted <code>SupplierThrowable</code> by applying the 3 given arguments.
     */
    default SupplierThrowable<R> asSupplier(T t, U u, V v){
        return () -> apply(t, u, v);
    }

    default TriFunction<T,U,V, R> orElse(Function<Exception, R> exceptionHanlder){
        return (t, u, v) -> {
            try {
                return apply(t, u, v);
            } catch (Exception e) {
                if(exceptionHanlder == null)
                    return null;
                return exceptionHanlder.apply(e);
            }
        };
    }

    @FunctionalInterface
    interface TriFunction<T,U,V, R> extends AbstractThrowable{
        R apply(T t, U u, V v);
    }
}
