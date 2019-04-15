package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues9;

public class TupleRow9<T, U, V, W, X, Y, Z, A, B> extends TupleRow8<T, U, V, W, X, Y, Z, A>
        implements WithValuesByName9<T, U, V, W, X, Y, Z, A, B> {

    public TupleRow9(IColumns indexes, WithValues9<T, U, V, W, X, Y, Z, A, B> values) {
        super(indexes, values);
    }

    public TupleRow9(String[] columns, WithValues9<T, U, V, W, X, Y, Z, A, B> values) {
        super(columns, values);
    }

    public TupleRow9(IColumns indexes, final T t, final U u, final V v, final W w, final X x, final Y y,
                     final Z z, final A a, final B b) {
        super(indexes, Tuple.create(t, u, v, w, x, y, z, a, b));
    }

    public TupleRow9(String[] columns, final T t, final U u, final V v, final W w, final X x, final Y y,
                     final Z z, final A a, final B b) {
        super(columns, Tuple.create(t, u, v, w, x, y, z, a, b));
    }
}
