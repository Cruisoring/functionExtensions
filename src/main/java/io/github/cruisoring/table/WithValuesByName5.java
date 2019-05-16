package io.github.cruisoring.table;

import io.github.cruisoring.tuple.WithValues5;

/**
 * Generic interface definining a structure containing 5 strong-typed data values that can be accessed by names.
 *
 * @param <T> Type of the first value held by this {@code WithValuesByName}.
 * @param <U> Type of the second value held by this {@code WithValuesByName}.
 * @param <V> Type of the third value held by this {@code WithValuesByName}.
 * @param <W> Type of the fourth value held by this {@code WithValuesByName}.
 * @param <X> Type of the fifth value held by this {@code WithValuesByName}.
 */
public interface WithValuesByName5<T,U,V,W,X>
        extends WithValuesByName4<T,U,V,W>, WithValues5<T,U,V,W,X> {

}
