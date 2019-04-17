package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValuesPlus;

public class TupleRowPlus<T, U, V, W, X, Y, Z, A, B, C> extends TupleRow10<T, U, V, W, X, Y, Z, A, B, C>
        implements WithValuesByName10<T, U, V, W, X, Y, Z, A, B, C>{

    public TupleRowPlus(IColumns indexes, WithValuesPlus<T, U, V, W, X, Y, Z, A, B, C> values) {
        super(indexes, values);
    }

    public TupleRowPlus(IColumns indexes, final T t, final U u, final V v, final W w, final X x, final Y y,
                        final Z z, final A a, final B b, final C c, final Object... more) {
        super(indexes, Tuple.create(t, u, v, w, x, y, z, a, b, c, more));
    }
}
