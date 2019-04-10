package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple7;

import java.util.Map;

public class TupleRow7<T, U, V, W, X, Y, Z> extends TupleRow<Tuple7<T, U, V, W, X, Y, Z>> {

    public TupleRow7(Map<String, Integer> indexes, Tuple7<T, U, V, W, X, Y, Z> values) {
        super(indexes, values);
    }

    public TupleRow7(String[] columns, Tuple7<T, U, V, W, X, Y, Z> values) {
        super(columns, values);
    }

    public TupleRow7(Map<String, Integer> indexes, final T t, final U u, final V v, final W w, final X x, final Y y,
                     final Z z) {
        super(indexes, Tuple.create(t, u, v, w, x, y, z));
    }

    public TupleRow7(String[] columns, final T t, final U u, final V v, final W w, final X x, final Y y,
                     final Z z) {
        super(columns, Tuple.create(t, u, v, w, x, y, z));
    }
}
