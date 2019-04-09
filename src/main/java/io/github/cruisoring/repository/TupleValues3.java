package io.github.cruisoring.repository;

import io.github.cruisoring.tuple.Tuple;

/**
 * Interface for map.values that are of <tt>Tuple</tt> containing at least 3 strong-typed values and could be retrieved with the key
 *
 * @param <TKey> Type of the Key of the map
 * @param <T>    Type of the first element of the Tuple value
 * @param <U>    Type of the second element of the Tuple value
 * @param <V>    Type of the third element of the Tuple value
 */
public interface TupleValues3<TKey, T, U, V> extends TupleValues2<TKey, T, U> {

    /**
     * Retrieve the first value of the Tuple as type of <tt>V</tt>
     *
     * @param key key to retrieve corresponding Tuple value as a whole
     * @return third value of type <tt>V</tt>
     */
    default V getThirdValue(TKey key) {
        Tuple tuple = retrieve(key);
        if (tuple == null)
            return null;
        return (V) tuple.getValue(2);
    }
}
