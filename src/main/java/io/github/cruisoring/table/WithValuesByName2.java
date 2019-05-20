package io.github.cruisoring.table;

import io.github.cruisoring.tuple.WithValues2;

/**
 * Generic interface definining a structure containing 2 strong-typed data values that can be accessed by names.
 * @param <T> Type of the first value held by this {@code WithValuesByName}.
 * @param <U> Type of the second value held by this {@code WithValuesByName}.
 */
public interface WithValuesByName2<T,U>
        extends WithValuesByName1<T>, WithValues2<T,U> {

}
