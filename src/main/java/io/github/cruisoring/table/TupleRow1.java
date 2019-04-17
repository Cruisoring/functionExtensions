package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues1;

public class TupleRow1<T> extends TupleRow implements WithValuesByName1<T> {

    public TupleRow1(IColumns indexes, WithValues1<T> values) {
        super(indexes, values);
    }

    public TupleRow1(IColumns indexes, final T t) {
        super(indexes, Tuple.create(t));
    }
}
