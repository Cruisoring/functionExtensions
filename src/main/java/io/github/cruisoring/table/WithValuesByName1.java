package io.github.cruisoring.table;

import io.github.cruisoring.tuple.WithValues1;

/**
 * Generic interface definining a structure containing a single strong-typed data value that can be accessed by name.
 *
 * @param <T> Type of the single value held by this {@code WithValuesByName}.
 */
public interface WithValuesByName1<T>
        extends WithValuesByName, WithValues1<T> {

}
