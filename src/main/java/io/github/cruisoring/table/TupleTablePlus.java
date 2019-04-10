package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple10;
import io.github.cruisoring.utility.ArrayHelper;

import java.util.Objects;

public class TupleTablePlus<T, U, V, W, X, Y, Z, A, B, C>
        extends TupleTable<Tuple10<T, U, V, W, X, Y, Z, A, B, C>> {
    protected TupleTablePlus(String c1, String c2, String c3, String c4, String c5,
                             String c6, String c7, String c8, String c9, String c10, String... more) {
        super(ArrayHelper.mergeTypedArray(new String[]{c1, c2, c3, c4, c5, c6, c7, c8, c9, c10}, more));
    }

    protected TupleTablePlus(TableColumns columns){
        super(columns);
    }

    public boolean addValues(T t, U u, V v, W w, X x, Y y, Z z, A a, B b, C c, Object... more) {
        return addValues(Tuple.create(t, u, v, w, x, y, z, a, b, c, more));
    }
}
