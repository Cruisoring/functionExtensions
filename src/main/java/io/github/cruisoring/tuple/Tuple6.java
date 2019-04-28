package io.github.cruisoring.tuple;

/**
 * Tuple type with six elements persisted.
 *
 * @param <T> Type of the first persisted element
 * @param <U> Type of the second persisted element
 * @param <V> Type of the third persisted element
 * @param <W> Type of the fourth persisted element
 * @param <X> Type of the fifth persisted element
 * @param <Y> Type of the sixth persisted element
 */
public class Tuple6<T, U, V, W, X, Y> extends Tuple implements
        WithValues6<T, U, V, W, X, Y> {

    protected Tuple6(final T t, final U u, final V v, final W w, final X x, final Y y) {
        super(t, u, v, w, x, y);
    }

    protected Tuple6(final Class elementType, final T t, final U u, final V v, final W w, final X x, final Y y) {
        super(elementType, t, u, v, w, x, y);
    }

    /**
     * Tuple with 6 elements of identical type.
     *
     * @param <T> type of the elements.
     */
    public static class Set6<T> extends Tuple6<T, T, T, T, T, T> {

        protected Set6(final Class<? extends T> elementType, final T t1, final T t2, final T t3, final T t4, final T t5, final T t6) {
            super(elementType, t1, t2, t3, t4, t5, t6);
        }
    }

}
