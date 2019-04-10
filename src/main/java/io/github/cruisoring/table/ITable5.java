package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple5;

public interface ITable5<T, U, V, W, X> extends ITable<Tuple5<T, U, V, W, X>> {
    default boolean addValues(T t, U u, V v, W w, X x) {
        return addValues(Tuple.create(t, u, v, w, x));
    }

}
