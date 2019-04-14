package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues7;
import io.github.cruisoring.utility.ArrayHelper;

public class TupleTable7<T, U, V, W, X, Y, Z> extends TupleTable<WithValues7<T, U, V, W, X, Y, Z>> {
    protected TupleTable7(String c1, String c2, String c3, String c4, String c5, String c6, String c7) {
        super(c1, c2, c3, c4, c5, c6, c7);
    }

    protected TupleTable7(IMetaData columns){
        super(columns);
    }

    public boolean addValues(T t, U u, V v, W w, X x, Y y, Z z) {
        return addValues(Tuple.create(t, u, v, w, x, y, z));
    }

    public boolean addValues(T t, U u, V v, W w, X x, Y y, Z z, Object... more) {
        return addValues(Tuple.create(ArrayHelper.append(more, t, u, v, w, x, y, z)));
    }

    public boolean add(WithValuesByName7<T, U, V, W, X, Y, Z> row) {
        if (row == null) {// || row.getColumnIndexes() != this.columns) {
            return false;
        }

        return rows.add(row.getValues());
    }
}
