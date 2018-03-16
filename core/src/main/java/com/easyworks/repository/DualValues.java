package com.easyworks.repository;

import com.easyworks.tuple.Tuple;

/**
 * Interface for map.values that are of <tt>Tuple</tt> of 2 elements and keep the methods to retrieve them as strong-typed values
 * @param <TKey>    Type of the Key of the map
 * @param <T>       Type of the first element of the Tuple value
 * @param <U>       Type of the second element of the Tuple value
 */
public interface DualValues<TKey, T,U> extends SingleValues<TKey, T> {

    /**
     * Retrieve the first value of the Tuple as type of <tt>U</tt>
     * @param key   key to retrieve corresponding Tuple value as a whole
     * @return      second value of type <tt>U</tt>
     */
    default U getSecondValue(TKey key) {
        Tuple tuple = retrieve(key);
        if(tuple == null)
            return null;
        return (U) tuple.getValueAt(1);
    }
}
