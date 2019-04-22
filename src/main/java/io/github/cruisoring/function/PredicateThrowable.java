package io.github.cruisoring.function;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Functional Interface identifying methods, accepting 1 argument and returning value type of boolean
 * while their service logic could throw Exceptions.
 *
 * @param <T> Type of the first argument.
 */
@FunctionalInterface
public interface PredicateThrowable<T> extends FunctionThrowable<T, Boolean> {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 7 arguments and returning result of boolean type
     *
     * @param t The first argument of type <code>T</code>.
     * @return The result of applying the given arguments.
     * @throws Exception Any Exception could be thrown by the concerned service logic.
     */
    default Boolean test(T t) throws Exception {
        return apply(t);
    }

    /**
     * Convert this {@code PredicateThrowable<T>} to {@code Predicate<T>} by returning <code>false</code> with Exception.
     *
     * @return {@code Predicate<T>} with same predicate logic except always return false with Exception.
     */
    default Predicate<T> orElse() {
        Predicate<T> predicate = (t) -> {
            try {
                return apply(t);
            } catch (Exception ignored) {
                return false;
            }
        };
        return predicate;
    }
}
