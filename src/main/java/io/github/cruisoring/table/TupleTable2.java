package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues2;
import io.github.cruisoring.utility.ArrayHelper;

public class TupleTable2<T, U> extends TupleTable<WithValues2<T, U>> {
    protected TupleTable2(String column1, String column2) {
        super(column1, column2);
    }

    protected TupleTable2(ITableColumns columns){
        super(columns);
    }

    public boolean addValues(T t, U u) {
        return addValues(Tuple.create(t, u));
    }

    public boolean addValues(T t, U u, Object... more) {
        return addValues(Tuple.of(ArrayHelper.append(more, t, u)));
    }}
