package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple8;
import io.github.cruisoring.tuple.WithValues8;
import io.github.cruisoring.tuple.WithValues9;

import java.util.Map;

public class TupleRow8<T, U, V, W, X, Y, Z, A> extends TupleRow<WithValues8<T, U, V, W, X, Y, Z, A>> {

    public TupleRow8(TableColumns indexes, Tuple8<T, U, V, W, X, Y, Z, A> values) {
        super(indexes, values);
    }

    public TupleRow8(String[] columns, Tuple8<T, U, V, W, X, Y, Z, A> values) {
        super(columns, values);
    }

    public TupleRow8(TableColumns indexes, final T t, final U u, final V v, final W w, final X x, final Y y,
                     final Z z, final A a) {
        super(indexes, Tuple.create(t, u, v, w, x, y, z, a));
    }

    public TupleRow8(String[] columns, final T t, final U u, final V v, final W w, final X x, final Y y,
                     final Z z, final A a) {
        super(columns, Tuple.create(t, u, v, w, x, y, z, a));
    }
}
