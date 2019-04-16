package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues10;
import io.github.cruisoring.utility.ArrayHelper;

public class TupleTable10<T, U, V, W, X, Y, Z, A, B, C> extends TupleTable<WithValues10<T, U, V, W, X, Y, Z, A, B, C>> {

    protected TupleTable10(IColumns columns, Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV,
                           Class<? extends W> typeW, Class<? extends X> typeX, Class<? extends Y> typeY, Class<? extends Z> typeZ,
                           Class<? extends A> typeA, Class<? extends B> typeB, Class<? extends C> typeC) {
        super(columns, typeT, typeU, typeV, typeW, typeX, typeY, typeZ, typeA, typeB, typeC);
        if(columns.width() < 10){
            throw new UnsupportedOperationException("Not enough columns defined!");
        }
    }

    public boolean addValues(T t, U u, V v, W w, X x, Y y, Z z, A a, B b, C c, Object... more) {
        return addValues(Tuple.of(ArrayHelper.append(more, t, u, v, w, x, y, z, a, b, c)));
    }
}
