package io.github.cruisoring.tuple;

import io.github.cruisoring.utility.ArrayHelper;

/**
 * Tuple type with 20 elements persisted.
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
 * @param <C> Type of the 10th persisted element
 */
public class TuplePlus<T, U, V, W, X, Y, Z, A, B, C> extends Tuple
        implements WithValuesPlus<T, U, V, W, X, Y, Z, A, B, C> {

    protected TuplePlus(final T t, final U u, final V v, final W w, final X x, final Y y,
                        final Z z, final A a, final B b, final C c, final Object... more) {
        super(ArrayHelper.mergeVarargsFirst(more, t, u, v, w, x, y, z, a, b, c));
    }

    protected TuplePlus(final Class elementType, final T t, final U u, final V v, final W w, final X x, final Y y,
                        final Z z, final A a, final B b, final C c, final Object... more) {
        super(elementType, ArrayHelper.mergeVarargsFirst(more, t, u, v, w, x, y, z, a, b, c));
    }
}
