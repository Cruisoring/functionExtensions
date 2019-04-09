package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple9;

public interface ITable9<T, U, V, W, X, Y, Z, A, B> extends ITable<Tuple9<T, U, V, W, X, Y, Z, A, B>> {
    default boolean addValues(T t, U u, V v, W w, X x, Y y, Z z, A a, B b) {
        return addTuple(Tuple.create(t, u, v, w, x, y, z, a, b));
    }

}
