package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues7;

public class TupleRow7<T, U, V, W, X, Y, Z> extends TupleRow6<T, U, V, W, X, Y>
        implements WithValuesByName7<T, U, V, W, X, Y, Z> {

    public TupleRow7(ITableColumns indexes, WithValues7<T, U, V, W, X, Y, Z> values) {
        super(indexes, values);
    }

    public TupleRow7(String[] columns, WithValues7<T, U, V, W, X, Y, Z> values) {
        super(columns, values);
    }

    public TupleRow7(ITableColumns indexes, final T t, final U u, final V v, final W w, final X x, final Y y,
                     final Z z) {
        super(indexes, Tuple.create(t, u, v, w, x, y, z));
    }

    public TupleRow7(String[] columns, final T t, final U u, final V v, final W w, final X x, final Y y,
                     final Z z) {
        super(columns, Tuple.create(t, u, v, w, x, y, z));
    }
}
