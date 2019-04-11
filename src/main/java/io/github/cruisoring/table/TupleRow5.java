package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple5;

public class TupleRow5<T, U, V, W, X> extends TupleRow implements WithValuesByName5<T, U, V, W, X> {

    public TupleRow5(ITableColumns indexes, Tuple5<T, U, V, W, X> values) {
        super(indexes, values);
    }

    public TupleRow5(String[] columns, Tuple5<T, U, V, W, X> values) {
        super(columns, values);
    }

    public TupleRow5(ITableColumns indexes, final T t, final U u, final V v, final W w, final X x) {
        super(indexes, Tuple.create(t, u, v, w, x));
    }

    public TupleRow5(String[] columns, final T t, final U u, final V v, final W w, final X x) {
        super(columns, Tuple.create(t, u, v, w, x));
    }
}
