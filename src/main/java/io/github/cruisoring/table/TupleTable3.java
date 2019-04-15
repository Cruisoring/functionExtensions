package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues3;
import io.github.cruisoring.utility.ArrayHelper;

public class TupleTable3<T, U, V> extends TupleTable<WithValues3<T, U, V>> {
//    protected TupleTable3(String c1, String c2, String c3) {
//        super(c1, c2, c3);
//    }

    protected TupleTable3(IColumns columns, Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV){
        super(columns, typeT, typeU, typeV);
        if(columns.width() < 3){
            throw new UnsupportedOperationException("Not enough columns defined!");
        }
    }

    public boolean addValues(T t, U u, V v) {
        return addValues(Tuple.create(t, u, v));
    }

    public boolean addValues(T t, U u, V v, Object... more) {
        return addValues(Tuple.of(ArrayHelper.append(more, t, u, v)));
    }

    public boolean add(WithValuesByName3<T, U, V> row) {
        if (row == null) {// || row.getColumnIndexes() != this.columns) {
            return false;
        }

        return rows.add(row.getValues());
    }
}
