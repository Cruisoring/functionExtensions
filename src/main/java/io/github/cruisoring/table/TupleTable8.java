package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple8;

public class TupleTable8<T, U, V, W, X, Y, Z, A> extends TupleTable<Tuple8<T, U, V, W, X, Y, Z, A>> {
    protected TupleTable8(String c1, String c2, String c3, String c4, String c5, String c6, String c7, String c8) {
        super(c1, c2, c3, c4, c5, c6, c7, c8);
    }

    protected TupleTable8(TableColumns columns){
        super(columns);
    }

    public boolean addValues(T t, U u, V v, W w, X x, Y y, Z z, A a) {
        return addValues(Tuple.create(t, u, v, w, x, y, z, a));
    }
}
