package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple4;

public class TupleTable4<T, U, V, W> extends TupleTable<Tuple4<T, U, V, W>> implements ITable4<T, U, V, W> {
    protected TupleTable4(String c1, String c2, String c3, String c4) {
        super(c1, c2, c3, c4);
    }
}
