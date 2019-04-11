package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple1;

public class TupleRow1<T> extends TupleRow implements WithValuesByName1<T> {

    public TupleRow1(ITableColumns indexes, Tuple1<T> values) {
        super(indexes, values);
    }

    public TupleRow1(String[] columns, Tuple1<T> values) {
        super(columns, values);
    }

    public TupleRow1(ITableColumns indexes, final T t) {
        super(indexes, Tuple.create(t));
    }

    public TupleRow1(String[] columns, final T t) {
        super(columns, Tuple.create(t));
    }

}
