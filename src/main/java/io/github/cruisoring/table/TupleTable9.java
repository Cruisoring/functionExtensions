package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues;
import io.github.cruisoring.tuple.WithValues9;
import io.github.cruisoring.utility.ArrayHelper;

import java.util.List;
import java.util.function.Supplier;

public class TupleTable9<T, U, V, W, X, Y, Z, A, B> extends TupleTable<WithValues9<T, U, V, W, X, Y, Z, A, B>> {

    protected TupleTable9(Supplier<List<WithValues>> rowsSupplier, IColumns columns,
                          Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV,
                          Class<? extends W> typeW, Class<? extends X> typeX, Class<? extends Y> typeY,
                          Class<? extends Z> typeZ, Class<? extends A> typeA, Class<? extends B> typeB) {
        super(rowsSupplier, columns, typeT, typeU, typeV, typeW, typeX, typeY, typeZ, typeA, typeB);
        if(columns.width() < 9){
            throw new UnsupportedOperationException("Not enough columns defined!");
        }
    }

    public boolean addValues(T t, U u, V v, W w, X x, Y y, Z z, A a, B b, Object... more) {
        return addValues(Tuple.of(ArrayHelper.mergeVarargsFirst(more, t, u, v, w, x, y, z, a, b)));
    }
}
