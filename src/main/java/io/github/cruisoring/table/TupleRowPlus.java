package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.TuplePlus;

import java.util.Map;

public class TupleRowPlus<T, U, V, W, X, Y, Z, A, B, C> extends TupleRow<TuplePlus<T, U, V, W, X, Y, Z, A, B, C>> {

    public TupleRowPlus(Map<String, Integer> indexes, TuplePlus<T, U, V, W, X, Y, Z, A, B, C> values) {
        super(indexes, values);
    }

    public TupleRowPlus(String[] columns, TuplePlus<T, U, V, W, X, Y, Z, A, B, C> values) {
        super(columns, values);
    }

    public TupleRowPlus(Map<String, Integer> indexes, final T t, final U u, final V v, final W w, final X x, final Y y,
                        final Z z, final A a, final B b, final C c, final Object d, Object... more) {
        super(indexes, Tuple.create(t, u, v, w, x, y, z, a, b, c, d, more));
    }

    public TupleRowPlus(String[] columns, final T t, final U u, final V v, final W w, final X x, final Y y,
                        final Z z, final A a, final B b, final C c, final Object d, Object... more) {
        super(columns, Tuple.create(t, u, v, w, x, y, z, a, b, c, d, more));
    }
}
