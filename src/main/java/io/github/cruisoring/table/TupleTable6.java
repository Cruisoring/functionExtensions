package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues;
import io.github.cruisoring.tuple.WithValues6;
import io.github.cruisoring.utility.ArrayHelper;

import java.util.List;
import java.util.function.Supplier;

public class TupleTable6<T, U, V, W, X, Y> extends TupleTable<WithValues6<T, U, V, W, X, Y>> {

    protected TupleTable6(Supplier<List<WithValues>> rowsSupplier, IColumns columns,
                          Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV,
                          Class<? extends W> typeW, Class<? extends X> typeX, Class<? extends Y> typeY) {
        super(rowsSupplier, columns, typeT, typeU, typeV, typeW, typeX, typeY);
        if(columns.width() < 6){
            throw new UnsupportedOperationException("Not enough columns defined!");
        }
    }

    public boolean addValues(T t, U u, V v, W w, X x, Y y, Object... more) {
        return addValues(Tuple.of(ArrayHelper.append(more, t, u, v, w, x, y)));
    }
}
