package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues8;
import io.github.cruisoring.utility.ArrayHelper;

public class TupleTable8<T, U, V, W, X, Y, Z, A> extends TupleTable<WithValues8<T, U, V, W, X, Y, Z, A>> {

    protected TupleTable8(IColumns columns, Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV,
                          Class<? extends W> typeW, Class<? extends X> typeX, Class<? extends Y> typeY, Class<? extends Z> typeZ,
                          Class<? extends A> typeA) {
        super(columns, typeT, typeU, typeV, typeW, typeX, typeY, typeZ, typeA);
        if(columns.width() < 8){
            throw new UnsupportedOperationException("Not enough columns defined!");
        }
    }

    public boolean addValues(T t, U u, V v, W w, X x, Y y, Z z, A a, Object... more) {
        return addValues(Tuple.of(ArrayHelper.append(more, t, u, v, w, x, y, z, a)));
    }
}
