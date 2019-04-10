package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple2;

import java.util.Map;

public class TupleRow2<T, U> extends TupleRow<Tuple2<T, U>> {

    public TupleRow2(Map<String, Integer> indexes, Tuple2<T, U> values) {
        super(indexes, values);
    }

    public TupleRow2(String[] columns, Tuple2<T, U> values) {
        super(columns, values);
    }

    public TupleRow2(Map<String, Integer> indexes, final T t, final U u) {
        super(indexes, Tuple.create(t, u));
    }

    public TupleRow2(String[] columns, final T t, final U u) {
        super(columns, Tuple.create(t, u));
    }
}
