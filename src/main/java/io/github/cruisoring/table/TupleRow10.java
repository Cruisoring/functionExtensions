package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues10;

public class TupleRow10<T, U, V, W, X, Y, Z, A, B, C> extends TupleRow9<T, U, V, W, X, Y, Z, A, B>
        implements WithValuesByName10<T, U, V, W, X, Y, Z, A, B, C> {

    public TupleRow10(IColumns indexes, WithValues10<T, U, V, W, X, Y, Z, A, B, C> values) {
        super(indexes, values);
    }

    public TupleRow10(IColumns indexes, final T t, final U u, final V v, final W w, final X x, final Y y,
                      final Z z, final A a, final B b, final C c) {
        super(indexes, Tuple.create(t, u, v, w, x, y, z, a, b, c));
    }
}
