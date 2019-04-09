package io.github.cruisoring.tuple;

/**
 * Tuple type with three elements persisted.
 *
 * @param <T> Type of the first persisted element
 * @param <U> Type of the second persisted element
 * @param <V> Type of the third persisted element
 */
public class Tuple3<T, U, V> extends Tuple
        implements WithValues3<T, U, V> {

    protected Tuple3(final T t, final U u, final V v) {
        super(t, u, v);
    }

    /**
     * Tuple with 3 elements of identical type.
     *
     * @param <T> type of the elements.
     */
    public static class Set<T> extends Tuple3<T, T, T> {

        protected Set(final T t1, final T t2, final T t3) {
            super(t1, t2, t3);
        }
    }
}
