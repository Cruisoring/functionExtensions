package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple1;

public interface ITable1<T> extends ITable<Tuple1<T>> {

    default boolean addValues(T t) {
        return addTuple(Tuple.create(t));
    }
}
