package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues4;
import io.github.cruisoring.utility.ArrayHelper;

public class TupleTable4<T, U, V, W> extends TupleTable<WithValues4<T, U, V, W>> {

    protected TupleTable4(IColumns columns, Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV,
                          Class<? extends W> typeW){
        super(columns, typeT, typeU, typeV, typeW);
        if(columns.width() < 4){
            throw new UnsupportedOperationException("Not enough columns defined!");
        }
    }

    public boolean addValues(T t, U u, V v, W w, Object... more) {
        return addValues(Tuple.of(ArrayHelper.append(more, t, u, v, w)));
    }
}
