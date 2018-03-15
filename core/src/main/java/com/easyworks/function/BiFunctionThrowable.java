package com.easyworks.function;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Functional Interface identifying methods, accepting 2 arguments and returning result of type <code>R</code>,
 * while their service logic could throw Exceptions.
 * @param <T>   Type of the first argument.
 * @param <U>   Type of the second argument.
 * @param <R>   Type of the returned result.
 */
@FunctionalInterface
public interface BiFunctionThrowable<T, U, R> extends AbstractThrowable {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 2 arguments and returning result of type <code>R</code>
     * @param t     The first argument of type <code>T</code>.
     * @param u     The second argument of type <code>U</code>.
     * @return      The result of applying the given arguments.
     * @throws Exception    Any Exception could be thrown by the concerned service logic.
     */
    R apply(T t, U u) throws Exception;

    /**
     * Convert the above <code>apply</code> method to <code>SupplierThrowable</code> with all 2 arguments
     * @param t     The first argument of type <code>T</code>.
     * @param u     The second argument of type <code>U</code>.
     * @return      The converted <code>SupplierThrowable</code> by applying the 2 given arguments.
     */
    default SupplierThrowable<R> asSupplier(T t, U u){
        return () -> apply(t, u);
    }

    default BiFunction<T, U, R> orElse(Function<Exception, R> exceptionHandler){
        Objects.requireNonNull(exceptionHandler);
        return (t, u) -> {
            try {
                return apply(t, u);
            } catch (Exception e) {
                return exceptionHandler.apply(e);
            }
        };
    }

}
