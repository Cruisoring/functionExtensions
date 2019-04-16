package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues7;
import io.github.cruisoring.utility.ArrayHelper;

public class TupleTable7<T, U, V, W, X, Y, Z> extends TupleTable<WithValues7<T, U, V, W, X, Y, Z>> {

    protected TupleTable7(IColumns columns, Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV,
                          Class<? extends W> typeW, Class<? extends X> typeX, Class<? extends Y> typeY, Class<? extends Z> typeZ) {
        super(columns, typeT, typeU, typeV, typeW, typeX, typeY, typeZ);
        if(columns.width() < 7){
            throw new UnsupportedOperationException("Not enough columns defined!");
        }
    }

    public boolean addValues(T t, U u, V v, W w, X x, Y y, Z z, Object... more) {
        return addValues(Tuple.create(ArrayHelper.append(more, t, u, v, w, x, y, z)));
    }
}
