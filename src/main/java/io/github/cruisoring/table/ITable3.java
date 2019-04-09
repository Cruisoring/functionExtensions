package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple3;

public interface ITable3<T, U, V> extends ITable<Tuple3<T, U, V>> {
    default boolean addValues(T t, U u, V v) {
        return addTuple(Tuple.create(t, u, v));
    }
}
