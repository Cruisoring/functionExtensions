package io.github.cruisoring.tuple;

/**
 * Tuple type with a single element persisted.
 *
 * @param <T> Type of the persisted element
 */
public class Tuple1<T> extends Tuple
        implements WithValues1<T> {

    protected Tuple1(final T t) {
        super(t);
    }

    protected Tuple1(final Class elementType, final T t) {
        super(elementType, t);
    }

    /**
     * Tuple with 1 elements of identical type.
     *
     * @param <T> type of the elements.
     */
    public static class Set<T> extends Tuple1<T> {

        protected Set(final Class<? extends T> elementType, final T t1) {
            super(elementType, t1);
        }
    }
}
