package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple6;

public class TupleTable6<T, U, V, W, X, Y> extends TupleTable<Tuple6<T, U, V, W, X, Y>> implements ITable6<T, U, V, W, X, Y> {
    protected TupleTable6(String c1, String c2, String c3, String c4, String c5, String c6) {
        super(c1, c2, c3, c4, c5, c6);
    }

    protected TupleTable6(TableColumns columns){
        super(columns);
    }
}
