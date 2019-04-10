package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple6;

import java.util.Map;

public class TupleRow6<T, U, V, W, X, Y> extends TupleRow<Tuple6<T, U, V, W, X, Y>> {

    public TupleRow6(Map<String, Integer> indexes, Tuple6<T, U, V, W, X, Y> values) {
        super(indexes, values);
    }

    public TupleRow6(String[] columns, Tuple6<T, U, V, W, X, Y> values) {
        super(columns, values);
    }

    public TupleRow6(Map<String, Integer> indexes, final T t, final U u, final V v, final W w, final X x, final Y y) {
        super(indexes, Tuple.create(t, u, v, w, x, y));
    }

    public TupleRow6(String[] columns, final T t, final U u, final V v, final W w, final X x, final Y y) {
        super(columns, Tuple.create(t, u, v, w, x, y));
    }
}
