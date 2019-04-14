package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues8;

public class TupleRow8<T, U, V, W, X, Y, Z, A> extends TupleRow7<T, U, V, W, X, Y, Z>
        implements WithValuesByName8<T, U, V, W, X, Y, Z, A> {

    public TupleRow8(IMetaData indexes, WithValues8<T, U, V, W, X, Y, Z, A> values) {
        super(indexes, values);
    }

    public TupleRow8(String[] columns, WithValues8<T, U, V, W, X, Y, Z, A> values) {
        super(columns, values);
    }

    public TupleRow8(IMetaData indexes, final T t, final U u, final V v, final W w, final X x, final Y y,
                     final Z z, final A a) {
        super(indexes, Tuple.create(t, u, v, w, x, y, z, a));
    }

    public TupleRow8(String[] columns, final T t, final U u, final V v, final W w, final X x, final Y y,
                     final Z z, final A a) {
        super(columns, Tuple.create(t, u, v, w, x, y, z, a));
    }
}
