package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues6;
import io.github.cruisoring.utility.ArrayHelper;

public class TupleTable6<T, U, V, W, X, Y> extends TupleTable<WithValues6<T, U, V, W, X, Y>> {
    protected TupleTable6(String c1, String c2, String c3, String c4, String c5, String c6) {
        super(c1, c2, c3, c4, c5, c6);
    }

    protected TupleTable6(ITableColumns columns){
        super(columns);
    }

    public boolean addValues(T t, U u, V v, W w, X x, Y y) {
        return addValues(Tuple.create(t, u, v, w, x, y));
    }

    public boolean addValues(T t, U u, V v, W w, X x, Y y, Object... more) {
        return addValues(Tuple.of(ArrayHelper.append(more, t, u, v, w, x, y)));
    }}
