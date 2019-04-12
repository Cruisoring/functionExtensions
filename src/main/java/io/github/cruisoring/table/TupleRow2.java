package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues2;

public class TupleRow2<T, U> extends TupleRow1<T> implements WithValuesByName2<T, U> {

    public TupleRow2(ITableColumns indexes, WithValues2<T, U> values) {
        super(indexes, values);
    }

    public TupleRow2(String[] columns, WithValues2<T, U> values) {
        super(columns, values);
    }

    public TupleRow2(ITableColumns indexes, final T t, final U u) {
        super(indexes, Tuple.create(t, u));
    }

    public TupleRow2(String[] columns, final T t, final U u) {
        this(columns, Tuple.create(t, u));
    }
}
