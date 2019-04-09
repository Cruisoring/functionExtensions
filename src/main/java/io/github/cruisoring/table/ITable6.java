package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple6;

public interface ITable6<T, U, V, W, X, Y> extends ITable<Tuple6<T, U, V, W, X, Y>> {
    default boolean addValues(T t, U u, V v, W w, X x, Y y) {
        return addTuple(Tuple.create(t, u, v, w, x, y));
    }

}
