package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple4;

public interface ITable4<T, U, V, W> extends ITable<Tuple4<T, U, V, W>> {
    default boolean addValues(T t, U u, V v, W w) {
        return addTuple(Tuple.create(t, u, v, w));
    }
}
