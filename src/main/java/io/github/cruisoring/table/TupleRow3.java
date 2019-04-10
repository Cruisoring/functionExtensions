package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple3;
import io.github.cruisoring.tuple.WithValues3;

import java.util.Map;

public class TupleRow3<T, U, V> extends TupleRow<WithValues3<T, U, V>> {

    public TupleRow3(TableColumns indexes, Tuple3<T, U, V> values) {
        super(indexes, values);
    }

    public TupleRow3(String[] columns, Tuple3<T, U, V> values) {
        super(columns, values);
    }

    public TupleRow3(TableColumns indexes, final T t, final U u, final V v) {
        super(indexes, Tuple.create(t, u, v));
    }

    public TupleRow3(String[] columns, final T t, final U u, final V v) {
        super(columns, Tuple.create(t, u, v));
    }
}
