package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple4;

import java.util.Map;

public class TupleRow4<T, U, V, W> extends TupleRow<Tuple4<T, U, V, W>> {

    public TupleRow4(Map<String, Integer> indexes, Tuple4<T, U, V, W> values) {
        super(indexes, values);
    }

    public TupleRow4(String[] columns, Tuple4<T, U, V, W> values) {
        super(columns, values);
    }

    public TupleRow4(Map<String, Integer> indexes, final T t, final U u, final V v, final W w) {
        super(indexes, Tuple.create(t, u, v, w));
    }

    public TupleRow4(String[] columns, final T t, final U u, final V v, final W w) {
        super(columns, Tuple.create(t, u, v, w));
    }
}
