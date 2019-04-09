package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple5;

public class TupleTable5<T, U, V, W, X> extends TupleTable<Tuple5<T, U, V, W, X>> implements ITable5<T, U, V, W, X> {
    protected TupleTable5(String tableName, String c1, String c2, String c3, String c4, String c5) {
        super(tableName, new String[]{c1, c2, c3, c4, c5});
    }
}
