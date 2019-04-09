package io.github.cruisoring.tuple;

/**
 * Tuple type with 9 elements persisted.
 *
 * @param <T> Type of the first persisted element
 * @param <U> Type of the second persisted element
 * @param <V> Type of the third persisted element
 * @param <W> Type of the fourth persisted element
 * @param <X> Type of the fifth persisted element
 * @param <Y> Type of the sixth persisted element
 * @param <Z> Type of the seventh persisted element
 * @param <A> Type of the 8th persisted element
 * @param <B> Type of the 9th persisted element
 */
public class Tuple9<T, U, V, W, X, Y, Z, A, B> extends Tuple
        implements WithValues9<T, U, V, W, X, Y, Z, A, B> {

    protected Tuple9(final T t, final U u, final V v, final W w, final X x, final Y y, final Z z, final A a, final B b) {
        super(t, u, v, w, x, y, z, a, b);
    }

    /**
     * Tuple with 9 elements of identical type.
     *
     * @param <T> type of the elements.
     */
    public static class Set<T> extends Tuple9<T, T, T, T, T, T, T, T, T> {

        protected Set(final T t1, final T t2, final T t3, final T t4, final T t5, final T t6, final T t7, final T t8, final T t9) {
            super(t1, t2, t3, t4, t5, t6, t7, t8, t9);
        }
    }
}
