package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple10;

public interface ITable10<T, U, V, W, X, Y, Z, A, B, C> extends ITable<Tuple10<T, U, V, W, X, Y, Z, A, B, C>> {
    default boolean addValues(T t, U u, V v, W w, X x, Y y, Z z, A a, B b, C c) {
        return addValues(Tuple.create(t, u, v, w, x, y, z, a, b, c));
    }
}
