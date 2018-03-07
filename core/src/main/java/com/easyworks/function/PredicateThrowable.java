package com.easyworks.function;

/**
 * Functional Interface identifying methods, accepting 1 argument and returning value type of boolean
 * while their service logic could throw Exceptions.
 * @param <T>   Type of the first argument.
 */
@FunctionalInterface
public interface PredicateThrowable<T> extends FunctionThrowable<T, Boolean> {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 7 arguments and returning result of boolean type
     * @param t     The first argument of type <code>T</code>.
     * @return      The result of applying the given arguments.
     * @throws Exception    Any Exception could be thrown by the concerned service logic.
     */
    default boolean test(T t) throws Exception {
        return apply(t);
    }

    /**
     * Convert the above <code>apply</code> method to <code>SupplierThrowable</code> with 1 argument
     * @param t     The first argument of type <code>T</code>.
     * @return      The converted <code>SupplierThrowable</code> by applying the given argument.
     */
    default SupplierThrowable<Boolean> asSupplier(T t){
        return () -> test(t);
    }
}
