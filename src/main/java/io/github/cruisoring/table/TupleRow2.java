package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple2;

public class TupleRow2<T, U> extends TupleRow implements WithValuesByName2<T, U> {

    public TupleRow2(ITableColumns indexes, Tuple2<T, U> values) {
        super(indexes, values);
    }

    public TupleRow2(String[] columns, Tuple2<T, U> values) {
        super(columns, values);
    }

    public TupleRow2(ITableColumns indexes, final T t, final U u) {
        super(indexes, Tuple.create(t, u));
    }

    public TupleRow2(String[] columns, final T t, final U u) {
        super(columns, Tuple.create(t, u));
    }
}
