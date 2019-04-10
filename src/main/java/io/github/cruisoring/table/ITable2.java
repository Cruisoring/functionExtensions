package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple2;

public interface ITable2<T, U> extends ITable<Tuple2<T, U>> {
    default boolean addValues(T t, U u) {
        return addValues(Tuple.create(t, u));
    }
}
