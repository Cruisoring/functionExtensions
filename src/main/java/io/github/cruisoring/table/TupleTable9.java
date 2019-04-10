package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple9;

public class TupleTable9<T, U, V, W, X, Y, Z, A, B>
        extends TupleTable<Tuple9<T, U, V, W, X, Y, Z, A, B>> implements ITable9<T, U, V, W, X, Y, Z, A, B> {
    protected TupleTable9(String c1, String c2, String c3, String c4, String c5, String c6, String c7, String c8, String c9) {
        super(c1, c2, c3, c4, c5, c6, c7, c8, c9);
    }

    protected TupleTable9(TableColumns columns){
        super(columns);
    }
}
