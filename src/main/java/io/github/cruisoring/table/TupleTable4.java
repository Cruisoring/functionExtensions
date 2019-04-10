package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple4;

public class TupleTable4<T, U, V, W> extends TupleTable<Tuple4<T, U, V, W>> {
    protected TupleTable4(String c1, String c2, String c3, String c4) {
        super(c1, c2, c3, c4);
    }

    protected TupleTable4(TableColumns columns){
        super(columns);
    }

    public boolean addValues(T t, U u, V v, W w) {
        return addValues(Tuple.create(t, u, v, w));
    }
}
