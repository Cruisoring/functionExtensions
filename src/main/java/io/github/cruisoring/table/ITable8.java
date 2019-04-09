package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple8;

public interface ITable8<T, U, V, W, X, Y, Z, A> extends ITable<Tuple8<T, U, V, W, X, Y, Z, A>> {
    default boolean addValues(T t, U u, V v, W w, X x, Y y, Z z, A a) {
        return addTuple(Tuple.create(t, u, v, w, x, y, z, a));
    }

}
