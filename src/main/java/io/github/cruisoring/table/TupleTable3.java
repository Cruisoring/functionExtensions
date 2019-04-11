package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues3;
import io.github.cruisoring.utility.ArrayHelper;

public class TupleTable3<T, U, V> extends TupleTable<WithValues3<T, U, V>> {
    protected TupleTable3(String c1, String c2, String c3) {
        super(c1, c2, c3);
    }

    protected TupleTable3(ITableColumns columns){
        super(columns);
    }

    public boolean addValues(T t, U u, V v) {
        return addValues(Tuple.create(t, u, v));
    }

    public boolean addValues(T t, U u, V v, Object... more) {
        return addValues(Tuple.of(ArrayHelper.append(more, t, u, v)));
    }
}
