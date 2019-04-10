package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple10;

public class TupleTable10<T, U, V, W, X, Y, Z, A, B, C>
        extends TupleTable<Tuple10<T, U, V, W, X, Y, Z, A, B, C>> {
    protected TupleTable10(String c1, String c2, String c3, String c4, String c5, String c6,
                           String c7, String c8, String c9, String c10) {
        super(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10);
    }

    protected TupleTable10(TableColumns columns){
        super(columns);
    }

    public boolean addValues(T t, U u, V v, W w, X x, Y y, Z z, A a, B b, C c) {
        return addValues(Tuple.create(t, u, v, w, x, y, z, a, b, c));
    }
}
