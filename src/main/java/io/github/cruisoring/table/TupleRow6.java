package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues6;

public class TupleRow6<T, U, V, W, X, Y> extends TupleRow5<T, U, V, W, X> implements WithValuesByName6<T, U, V, W, X, Y> {

    public TupleRow6(IMetaData indexes, WithValues6<T, U, V, W, X, Y> values) {
        super(indexes, values);
    }

    public TupleRow6(String[] columns, WithValues6<T, U, V, W, X, Y> values) {
        super(columns, values);
    }

    public TupleRow6(IMetaData indexes, final T t, final U u, final V v, final W w, final X x, final Y y) {
        super(indexes, Tuple.create(t, u, v, w, x, y));
    }

    public TupleRow6(String[] columns, final T t, final U u, final V v, final W w, final X x, final Y y) {
        super(columns, Tuple.create(t, u, v, w, x, y));
    }
}