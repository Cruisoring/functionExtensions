package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues1;
import io.github.cruisoring.utility.ArrayHelper;

public class TupleTable1<T> extends TupleTable<WithValues1<T>> {
    protected TupleTable1(String column1) {
        super(column1);
    }

    protected TupleTable1(IMetaData columns){
        super(columns);
    }

    public boolean addValues(T t) {
        return addValues(Tuple.create(t));
    }

    public boolean addValues(T t, Object... more) {
        return addValues(Tuple.of(ArrayHelper.append(more, t)));
    }

    public boolean add(WithValuesByName1<T> row) {
        if (row == null) {// || row.getColumnIndexes() != this.columns) {
            return false;
        }

        return rows.add(row.getValues());
    }
}
