package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple7;

public interface ITable7<T, U, V, W, X, Y, Z> extends ITable<Tuple7<T, U, V, W, X, Y, Z>> {
    default boolean addValues(T t, U u, V v, W w, X x, Y y, Z z) {
        return addTuple(Tuple.create(t, u, v, w, x, y, z));
    }

}
