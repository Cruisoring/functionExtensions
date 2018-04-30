package io.github.cruisoring.repository;

import io.github.cruisoring.tuple.Tuple;

/**
 * Interface for map.values that are of <tt>Tuple</tt> and keep the methods to retrieve them
 * @param <TKey>    Type of the Key of the map
 */
public interface TupleValues<TKey> {
    /**
     * Retreive the value of key as a Tuple instance
     *
     * @param key   key to retrieve corresponding Tuple value as a whole
     * @return      corresponding Tuple value
     */
    Tuple retrieve(TKey key);

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the
     * specified key.
     *
     * @param   key   The key whose presence in this map is to be tested
     * @return <tt>true</tt> if this map contains a mapping for the specified
     */
    boolean containsKey(Object key);
}
