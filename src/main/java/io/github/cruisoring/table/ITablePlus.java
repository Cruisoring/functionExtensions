package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.TuplePlus;

public interface ITablePlus<T, U, V, W, X, Y, Z, A, B, C> extends ITable<TuplePlus<T, U, V, W, X, Y, Z, A, B, C>> {
    default boolean addValues(T t, U u, V v, W w, X x, Y y, Z z, A a, B b, C c, Object... more) {
        return addTuple(Tuple.create(t, u, v, w, x, y, z, a, b, c, more));
    }
}
