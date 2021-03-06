package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues2;

public class TupleRow2<T, U> extends TupleRow1<T> implements WithValuesByName2<T, U> {

    public TupleRow2(IColumns indexes, WithValues2<T, U> values) {
        super(indexes, values);
    }

    public TupleRow2(IColumns indexes, final T t, final U u) {
        super(indexes, Tuple.create(t, u));
    }
}
