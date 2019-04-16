package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues3;
import io.github.cruisoring.utility.ArrayHelper;

public class TupleTable3<T, U, V> extends TupleTable<WithValues3<T, U, V>> {

    protected TupleTable3(IColumns columns, Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV){
        super(columns, typeT, typeU, typeV);
        if(columns.width() < 3){
            throw new UnsupportedOperationException("Not enough columns defined!");
        }
    }

    public boolean addValues(T t, U u, V v, Object... more) {
        return addValues(Tuple.of(ArrayHelper.append(more, t, u, v)));
    }
}
