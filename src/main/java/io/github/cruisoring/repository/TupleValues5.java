package io.github.cruisoring.repository;

import io.github.cruisoring.tuple.Tuple;

/**
 * Interface for map.values that are of <tt>Tuple</tt> containing at least 5 strong-typed values and could be retrieved with the key
 * @param <TKey>    Type of the Key of the map
 * @param <T>       Type of the first element of the Tuple value
 * @param <U>       Type of the second element of the Tuple value
 * @param <V>       Type of the third element of the Tuple value
 * @param <W>       Type of the fourth element of the Tuple value
 * @param <X>       Type of the fifth element of the Tuple value
 */
public interface TupleValues5<TKey, T,U,V,W,X> extends TupleValues4<TKey, T,U,V,W> {
    /**
     * Retrieve the fifth value of the Tuple as type of <tt>X</tt>
     * @param key   key to retrieve corresponding Tuple value as a whole
     * @return      fifth value of type <tt>X</tt>
     */
    default X getFifthValue(TKey key) {
        Tuple tuple = retrieve(key);
        if(tuple == null)
            return null;
        return (X) tuple.getValue(4);
    }
}
