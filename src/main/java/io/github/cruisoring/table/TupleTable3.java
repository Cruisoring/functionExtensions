package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple3;

public class TupleTable3<T, U, V> extends TupleTable<Tuple3<T, U, V>> implements ITable3<T, U, V> {
    protected TupleTable3(String c1, String c2, String c3) {
        super(c1, c2, c3);
    }
}
