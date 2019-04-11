package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues4;
import io.github.cruisoring.utility.ArrayHelper;

public class TupleTable4<T, U, V, W> extends TupleTable<WithValues4<T, U, V, W>> {
    protected TupleTable4(String c1, String c2, String c3, String c4) {
        super(c1, c2, c3, c4);
    }

    protected TupleTable4(ITableColumns columns){
        super(columns);
    }

    public boolean addValues(T t, U u, V v, W w) {
        return addValues(Tuple.create(t, u, v, w));
    }

    public boolean addValues(T t, U u, V v, W w, Object... more) {
        return addValues(Tuple.of(ArrayHelper.append(more, t, u, v, w)));
    }

    public boolean add(WithValuesByName4<T, U, V, W> row) {
        if (row == null) {// || row.getColumnIndexes() != this.columns) {
            return false;
        }

        return rows.add(row.getValues());
    }
}
