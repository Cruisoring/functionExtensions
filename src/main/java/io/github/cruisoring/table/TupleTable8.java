package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple8;

public class TupleTable8<T, U, V, W, X, Y, Z, A, B> extends TupleTable<Tuple8<T, U, V, W, X, Y, Z, A>> implements ITable8<T, U, V, W, X, Y, Z, A> {
    protected TupleTable8(String tableName, String c1, String c2, String c3, String c4, String c5, String c6, String c7, String c8) {
        super(tableName, new String[]{c1, c2, c3, c4, c5, c6, c7, c8});
    }
}
