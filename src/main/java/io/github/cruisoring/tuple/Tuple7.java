package io.github.cruisoring.tuple;

/**
 * Tuple type with seven elements persisted.
 *
 * @param <T> Type of the first persisted element
 * @param <U> Type of the second persisted element
 * @param <V> Type of the third persisted element
 * @param <W> Type of the fourth persisted element
 * @param <X> Type of the fifth persisted element
 * @param <Y> Type of the sixth persisted element
 * @param <Z> Type of the seventh persisted element
 */
public class Tuple7<T, U, V, W, X, Y, Z> extends Tuple
        implements WithValues7<T, U, V, W, X, Y, Z> {

    protected Tuple7(final T t, final U u, final V v, final W w, final X x, final Y y, final Z z) {
        super(t, u, v, w, x, y, z);
    }

    protected Tuple7(final Class elementType, final T t, final U u, final V v, final W w, final X x, final Y y,
                     final Z z) {
        super(elementType, t, u, v, w, x, y, z);
    }
}
