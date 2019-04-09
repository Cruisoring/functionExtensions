package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple1;

public class TupleTable1<T> extends TupleTable<Tuple1<T>> implements ITable1<T> {
    protected TupleTable1(String tableName, String column1) {
        super(tableName, new String[]{column1});
    }
}
