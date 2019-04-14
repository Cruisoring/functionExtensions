package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues8;
import io.github.cruisoring.utility.ArrayHelper;

public class TupleTable8<T, U, V, W, X, Y, Z, A> extends TupleTable<WithValues8<T, U, V, W, X, Y, Z, A>> {
    protected TupleTable8(String c1, String c2, String c3, String c4, String c5, String c6, String c7, String c8) {
        super(c1, c2, c3, c4, c5, c6, c7, c8);
    }

    protected TupleTable8(IMetaData columns){
        super(columns);
    }

    public boolean addValues(T t, U u, V v, W w, X x, Y y, Z z, A a) {
        return addValues(Tuple.create(t, u, v, w, x, y, z, a));
    }

    public boolean addValues(T t, U u, V v, W w, X x, Y y, Z z, A a, Object... more) {
        return addValues(Tuple.of(ArrayHelper.append(more, t, u, v, w, x, y, z, a)));
    }

    public boolean add(WithValuesByName8<T, U, V, W, X, Y, Z, A> row) {
        if (row == null) {// || row.getColumnIndexes() != this.columns) {
            return false;
        }

        return rows.add(row.getValues());
    }
}
