package io.github.cruisoring.tuple;

/**
 * Tuple type with two elements persisted.
 *
 * @param <T> Type of the first persisted element
 * @param <U> Type of the second persisted element
 */
public class Tuple2<T, U> extends Tuple
        implements WithValues2<T, U> {

    protected Tuple2(final T t, final U u) {
        super(t, u);
    }

    protected Tuple2(final Class elementType, final T t, final U u) {
        super(elementType, t, u);
    }

    /**
     * Tuple with 2 elements of identical type.
     *
     * @param <T> type of the elements.
     */
    public static class Set<T> extends Tuple2<T, T> {

        protected Set(final Class<? extends T> elementType, final T t1, final T t2) {
            super(elementType, t1, t2);
        }
    }
}
