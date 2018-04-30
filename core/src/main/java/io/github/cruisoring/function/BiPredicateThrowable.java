package io.github.cruisoring.function;

/**
 * Functional Interface identifying methods, accepting 2 arguments and returning value type of boolean
 * while their service logic could throw Exceptions.
 * @param <T>   Type of the first argument.
 * @param <U>   Type of the second argument.
 */
@FunctionalInterface
public interface BiPredicateThrowable<T,U> extends BiFunctionThrowable<T,U, Boolean> {
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
}
