package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple1;
import io.github.cruisoring.tuple.WithValues1;

import java.util.Map;

public class TupleRow1<T> extends TupleRow<WithValues1<T>> {

    public TupleRow1(TableColumns indexes, Tuple1<T> values) {
        super(indexes, values);
    }

    public TupleRow1(String[] columns, Tuple1<T> values) {
        super(columns, values);
    }

    public TupleRow1(TableColumns indexes, final T t) {
        super(indexes, Tuple.create(t));
    }

    public TupleRow1(String[] columns, final T t) {
        super(columns, Tuple.create(t));
    }

}
