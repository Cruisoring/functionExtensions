package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues5;
import io.github.cruisoring.utility.ArrayHelper;

public class TupleTable5<T, U, V, W, X> extends TupleTable<WithValues5<T, U, V, W, X>> {

    protected TupleTable5(String c1, String c2, String c3, String c4, String c5) {
        super(c1, c2, c3, c4, c5);
    }

    protected TupleTable5(ITableColumns columns){
        super(columns);
    }


    public boolean addValues(T t, U u, V v, W w, X x) {
        return addValues(Tuple.create(t, u, v, w, x));
    }

    public boolean addValues(T t, U u, V v, W w, X x, Object... more) {
        return addValues(Tuple.of(ArrayHelper.append(more, t, u, v, w, x)));
    }

    public boolean add(WithValuesByName5<T, U, V, W, X> row) {
        if (row == null) {// || row.getColumnIndexes() != this.columns) {
            return false;
        }

        return rows.add(row.getValues());
    }
}
