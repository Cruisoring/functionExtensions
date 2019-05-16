package io.github.cruisoring.table;

import io.github.cruisoring.tuple.WithValues6;

/**
 * Generic interface definining a structure containing 6 strong-typed data values that can be accessed by names.
 *
 * @param <T> Type of the first value held by this {@code WithValuesByName}.
 * @param <U> Type of the second value held by this {@code WithValuesByName}.
 * @param <V> Type of the third value held by this {@code WithValuesByName}.
 * @param <W> Type of the fourth value held by this {@code WithValuesByName}.
 * @param <X> Type of the fifth value held by this {@code WithValuesByName}.
 * @param <Y> Type of the sixth value held by this {@code WithValuesByName}.
 */
public interface WithValuesByName6<T,U,V,W,X,Y>
        extends WithValuesByName5<T,U,V,W,X>, WithValues6<T,U,V,W,X,Y> {

}
