package com.easyworks.repository;


import com.easyworks.tuple.Tuple;

/**
 * Interface for map.values that are of <tt>Tuple</tt> containing at least one strong-typed value and could be retrieved with the key
 * @param <TKey>    Type of the Key of the map
 * @param <T>       Type of the first element of the Tuple value
 */
public interface SingleValues<TKey, T> extends TupleValues<TKey> {

    /**
     * Retrieve the first value of the Tuple as type of <tt>T</tt>
     * @param key   key to retrieve corresponding Tuple value as a whole
     * @return      first value of type <tt>T</tt>
     */
    default T getFirstValue(TKey key) {
        Tuple tuple = retrieve(key);
        if(tuple == null)
            return null;
        return (T) tuple.getValueAt(0);
    }
}
