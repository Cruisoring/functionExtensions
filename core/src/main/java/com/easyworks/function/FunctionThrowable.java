package com.easyworks.function;

import java.util.Objects;
import java.util.function.Function;

/**
 * Functional Interface identifying methods, accepting 1 argument and returning value type of <code>R</code>
 * while their service logic could throw Exceptions.
 * @param <T>   Type of the first argument.
 * @param <R>   Type of the returned result.
 */
@FunctionalInterface
public interface FunctionThrowable<T, R> extends AbstractThrowable {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 1 argument and returning result of type <code>R</code>
     * @param t     The first argument of type <code>T</code>.
     * @return      The result of applying the given arguments.
     * @throws Exception    Any Exception could be thrown by the concerned service logic.
     */
    R apply(T t) throws Exception;

    /**
     * Convert the above <code>apply</code> method to <code>SupplierThrowable</code> with 1 argument
     * @param t     The first argument of type <code>T</code>.
     * @return      The converted <code>SupplierThrowable</code> by applying the given argument.
     */
    default SupplierThrowable<R> asSupplier(T t){
        return () -> apply(t);
    }


    default Function<T, R> withHandler(Function<Exception, R> exceptionHanlder){
        Objects.requireNonNull(exceptionHanlder);
        return (t) -> {
            try {
                return apply(t);
            } catch (Exception e) {
                return exceptionHanlder.apply(e);
            }
        };
    }

    default Function<T, R> orElse(R defaultResult){
        return (t) -> {
            try {
                return apply(t);
            } catch (Exception e) {
                return defaultResult;
            }
        };
    }

}
