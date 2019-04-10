package io.github.cruisoring.tuple;

/**
 * Tuple type with five elements persisted.
 *
 * @param <T> Type of the first persisted element
 * @param <U> Type of the second persisted element
 * @param <V> Type of the third persisted element
 * @param <W> Type of the fourth persisted element
 * @param <X> Type of the fifth persisted element
 */
public class Tuple5<T, U, V, W, X> extends Tuple
        implements WithValues5<T, U, V, W, X> {

    protected Tuple5(final T t, final U u, final V v, final W w, final X x) {
        super(t, u, v, w, x);
    }

    protected Tuple5(final Class elementType, final T t, final U u, final V v, final W w, final X x) {
        super(elementType, t, u, v, w, x);
    }

    /**
     * Tuple with 5 elements of identical type.
     *
     * @param <T> type of the elements.
     */
    public static class Set<T> extends Tuple5<T, T, T, T, T> {

        protected Set(final Class<? extends T> elementType, final T t1, final T t2, final T t3, final T t4, final T t5) {
            super(elementType, t1, t2, t3, t4, t5);
        }
    }

}
