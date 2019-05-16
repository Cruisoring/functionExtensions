package io.github.cruisoring.table;

import io.github.cruisoring.tuple.WithValues3;

/**
 * Generic interface definining a structure containing 3 strong-typed data values that can be accessed by names.
 *
 * @param <T> Type of the first value held by this {@code WithValuesByName}.
 * @param <U> Type of the second value held by this {@code WithValuesByName}.
 * @param <V> Type of the third value held by this {@code WithValuesByName}.
 */
public interface WithValuesByName3<T,U,V>
        extends WithValuesByName2<T,U>, WithValues3<T,U,V> {

}
