package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple5;
import io.github.cruisoring.tuple.WithValues;
import io.github.cruisoring.tuple.WithValues5;
import io.github.cruisoring.utility.ArrayHelper;

import java.util.List;
import java.util.function.Supplier;

public class TupleTable5<T, U, V, W, X> extends TupleTable<WithValues5<T, U, V, W, X>> {

    protected TupleTable5(Supplier<List<WithValues>> rowsSupplier, IColumns columns,
                          Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV,
                          Class<? extends W> typeW, Class<? extends X> typeX) {
        super(rowsSupplier, columns, typeT, typeU, typeV, typeW, typeX);
        if(columns.width() < 5){
            throw new UnsupportedOperationException("Not enough columns defined!");
        }
    }

    public boolean addValues(T t, U u, V v, W w, X x, Object... more) {
        return addValues(Tuple.of(ArrayHelper.append(more, t, u, v, w, x)));
    }
}
