package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple2;

public class TupleTable2<T, U> extends TupleTable<Tuple2<T, U>> implements ITable2<T, U> {
    protected TupleTable2(String column1, String column2) {
        super(column1, column2);
    }
}
