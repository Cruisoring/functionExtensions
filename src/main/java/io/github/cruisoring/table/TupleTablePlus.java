package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues;
import io.github.cruisoring.tuple.WithValuesPlus;
import io.github.cruisoring.utility.ArrayHelper;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Supplier;

public class TupleTablePlus<T, U, V, W, X, Y, Z, A, B, C> extends TupleTable<WithValuesPlus<T, U, V, W, X, Y, Z, A, B, C>> {

    protected TupleTablePlus(Supplier<List<WithValues>> rowsSupplier, IColumns columns,
                             Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV,
                             Class<? extends W> typeW, Class<? extends X> typeX, Class<? extends Y> typeY,
                             Class<? extends Z> typeZ, Class<? extends A> typeA, Class<? extends B> typeB,
                             Class<? extends C> typeC, Type... moreTypes) {
        super(rowsSupplier, columns, (Class[])ArrayHelper.append(moreTypes,
                typeT, typeU, typeV, typeW, typeX, typeY, typeZ, typeA, typeB, typeC));
    }

    public boolean addValues(T t, U u, V v, W w, X x, Y y, Z z, A a, B b, C c, Object... more) {
        return addValues(Tuple.of(t, u, v, w, x, y, z, a, b, c, more));
    }
}
