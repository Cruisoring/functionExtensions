package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple2;
import io.github.cruisoring.tuple.WithValues2;

import java.util.Map;

public class TupleRow2<T, U> extends TupleRow<WithValues2<T, U>> {

    public TupleRow2(TableColumns indexes, Tuple2<T, U> values) {
        super(indexes, values);
    }

    public TupleRow2(String[] columns, Tuple2<T, U> values) {
        super(columns, values);
    }

    public TupleRow2(TableColumns indexes, final T t, final U u) {
        super(indexes, Tuple.create(t, u));
    }

    public TupleRow2(String[] columns, final T t, final U u) {
        super(columns, Tuple.create(t, u));
    }
}
