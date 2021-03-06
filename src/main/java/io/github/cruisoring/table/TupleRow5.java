package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues5;

public class TupleRow5<T, U, V, W, X> extends TupleRow4<T, U, V, W> implements WithValuesByName5<T, U, V, W, X> {

    public TupleRow5(IColumns indexes, WithValues5<T, U, V, W, X> values) {
        super(indexes, values);
    }

    public TupleRow5(IColumns indexes, final T t, final U u, final V v, final W w, final X x) {
        super(indexes, Tuple.create(t, u, v, w, x));
    }
}
