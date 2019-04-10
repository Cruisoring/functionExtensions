package io.github.cruisoring.tuple;

/**
 * Tuple type with four elements persisted.
 *
 * @param <T> Type of the first persisted element
 * @param <U> Type of the second persisted element
 * @param <V> Type of the third persisted element
 * @param <W> Type of the fourth persisted element
 */
public class Tuple4<T, U, V, W> extends Tuple
        implements WithValues4<T, U, V, W> {

    protected Tuple4(final T t, final U u, final V v, final W w) {
        super(t, u, v, w);
    }

    protected Tuple4(final Class elementType, final T t, final U u, final V v, final W w) {
        super(elementType, t, u, v, w);
    }

    /**
     * Tuple with 4 elements of identical type.
     *
     * @param <T> type of the elements.
     */
    public static class Set<T> extends Tuple4<T, T, T, T> {

        protected Set(final Class<? extends T> elementType, final T t1, final T t2, final T t3, final T t4) {
            super(elementType, t1, t2, t3, t4);
        }
    }

}
