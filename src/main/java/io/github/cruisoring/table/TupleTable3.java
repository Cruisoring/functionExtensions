package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues;
import io.github.cruisoring.tuple.WithValues3;
import io.github.cruisoring.utility.ArrayHelper;

import java.util.List;
import java.util.function.Supplier;

public class TupleTable3<T, U, V> extends TupleTable<WithValues3<T, U, V>> {

    protected TupleTable3(Supplier<List<WithValues>> rowsSupplier, IColumns columns,
                          Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV){
        super(rowsSupplier, columns, typeT, typeU, typeV);
        if(columns.width() < 3){
            throw new UnsupportedOperationException("Not enough columns defined!");
        }
    }

    public boolean addValues(T t, U u, V v, Object... more) {
        return addValues(Tuple.of(ArrayHelper.mergeVarargsFirst(more, t, u, v)));
    }
}
