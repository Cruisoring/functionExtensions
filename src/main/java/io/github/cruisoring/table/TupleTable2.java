package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues2;
import io.github.cruisoring.utility.ArrayHelper;

public class TupleTable2<T, U> extends TupleTable<WithValues2<T, U>> {

    protected TupleTable2(IColumns columns, Class<? extends T> typeT, Class<? extends U> typeU){
        super(columns, typeT, typeU);
        if(columns.width() < 2){
            throw new UnsupportedOperationException("Not enough columns defined!");
        }
    }

    public boolean addValues(T t, U u) {
        return addValues(Tuple.create(t, u));
    }

    public boolean addValues(T t, U u, Object... more) {
        return addValues(Tuple.of(ArrayHelper.append(more, t, u)));
    }

    public boolean add(WithValuesByName2<T,U> row) {
        if (row == null) {// || row.getColumnIndexes() != this.columns) {
            return false;
        }

        return rows.add(row.getValues());
    }

}
