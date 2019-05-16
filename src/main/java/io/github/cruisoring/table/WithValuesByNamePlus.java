package io.github.cruisoring.table;

import io.github.cruisoring.tuple.WithValuesPlus;

/**
 * Generic interface definining a structure containing more than 10 strong-typed data values that can be accessed by names.
 *
 * @param <T> Type of the first value held by this {@code WithValuesByName}.
 * @param <U> Type of the second value held by this {@code WithValuesByName}.
 * @param <V> Type of the third value held by this {@code WithValuesByName}.
 * @param <W> Type of the fourth value held by this {@code WithValuesByName}.
 * @param <X> Type of the fifth value held by this {@code WithValuesByName}.
 * @param <Y> Type of the sixth value held by this {@code WithValuesByName}.
 * @param <Z> Type of the seventh value held by this {@code WithValuesByName}.
 * @param <A> Type of the eighth value held by this {@code WithValuesByName}.
 * @param <B> Type of the nineth value held by this {@code WithValuesByName}.
 * @param <C> Type of the tenth value held by this {@code WithValuesByName}.
 */
public interface WithValuesByNamePlus<T,U,V,W,X,Y,Z,A,B,C>
        extends WithValuesByName10<T,U,V,W,X,Y,Z,A,B,C>, WithValuesPlus<T,U,V,W,X,Y,Z,A,B,C> {

}
