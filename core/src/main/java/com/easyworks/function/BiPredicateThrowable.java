package com.easyworks.function;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Functional Interface identifying methods, accepting 2 arguments and returning value type of boolean
 * while their service logic could throw Exceptions.
 * @param <T>   Type of the first argument.
 * @param <U>   Type of the second argument.
 */
@FunctionalInterface
public interface BiPredicateThrowable<T,U> extends BiFunctionThrowable<T, U, Boolean> {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 2 arguments and returning result of boolean type
     * @param t     The first argument of type <code>T</code>.
     * @param u     The second argument of type <code>U</code>.
     * @return      The result of applying the given arguments.
     * @throws Exception    Any Exception could be thrown by the concerned service logic.
     */
    default boolean test(T t, U u) throws Exception {
        return apply(t, u);
    }

    /**
     * Convert the above <code>apply</code> method to <code>SupplierThrowable</code> with 2 arguments
     * @param t     The first argument of type <code>T</code>.
     * @param u     The second argument of type <code>U</code>.
     * @return      The converted <code>SupplierThrowable</code> by applying the 2 given arguments.
     */
    default SupplierThrowable<Boolean> asSupplier(T t, U u){
        return () -> test(t, u);
    }

}
