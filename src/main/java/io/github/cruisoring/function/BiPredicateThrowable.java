package io.github.cruisoring.function;

import io.github.cruisoring.logger.Logger;

import java.util.function.BiPredicate;

/**
 * Functional Interface identifying methods, accepting 2 arguments and returning value type of boolean
 * while their service logic could throw Exceptions.
 *
 * @param <T> Type of the first argument.
 * @param <U> Type of the second argument.
 */
@FunctionalInterface
public interface BiPredicateThrowable<T, U> extends BiFunctionThrowable<T, U, Boolean> {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 2 arguments and returning result of boolean type
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @return The result of applying the given arguments.
     * @throws Exception Any Exception could be thrown by the concerned service logic.
     */
    default boolean test(T t, U u) throws Exception {
        return apply(t, u);
    }

    /**
     * Convert this {@code BiPredicateThrowable<T, U>} to {@code BiPredicate<T, U>} by returning <code>false</code> with Exception.
     *
     * @return {@code BiPredicate<T, U>} with same predicate logic except always return false with Exception.
     */
    default BiPredicate<T, U> orFalse() {
        BiPredicate<T, U> predicate = (t, u) -> {
            try {
                return apply(t, u);
            } catch (Exception e) {
                return false;
            }
        };
        return predicate;
    }

    /**
     * Convert this {@code BiPredicateThrowable<T, U>} to {@code BiPredicate<T, U>} by returning <code>false</code> with Exception.
     *
     * @return {@code BiPredicate<T, U>} with same predicate throws IllegalStateException when something wrong.
     */
    default BiPredicate<T, U> orException() {
        BiPredicate<T, U> predicate = (t, u) -> {
            try {
                return apply(t, u);
            } catch (Exception e) {
                Logger.D(e);
                throw new IllegalStateException(e.getMessage());
            }
        };
        return predicate;
    }
}
