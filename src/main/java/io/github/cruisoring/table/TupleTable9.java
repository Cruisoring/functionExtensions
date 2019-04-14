package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues9;
import io.github.cruisoring.utility.ArrayHelper;

public class TupleTable9<T, U, V, W, X, Y, Z, A, B> extends TupleTable<WithValues9<T, U, V, W, X, Y, Z, A, B>> {
    protected TupleTable9(String c1, String c2, String c3, String c4, String c5, String c6, String c7, String c8, String c9) {
        super(c1, c2, c3, c4, c5, c6, c7, c8, c9);
    }

    protected TupleTable9(IMetaData columns){
        super(columns);
    }

    public boolean addValues(T t, U u, V v, W w, X x, Y y, Z z, A a, B b) {
        return addValues(Tuple.create(t, u, v, w, x, y, z, a, b));
    }

    public boolean addValues(T t, U u, V v, W w, X x, Y y, Z z, A a, B b, Object... more) {
        return addValues(Tuple.of(ArrayHelper.append(more, t, u, v, w, x, y, z, a, b)));
    }

    public boolean add(WithValuesByName9<T, U, V, W, X, Y, Z, A, B> row) {
        if (row == null) {// || row.getColumnIndexes() != this.columns) {
            return false;
        }

        return rows.add(row.getValues());
    }
}