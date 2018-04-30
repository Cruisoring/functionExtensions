package io.github.cruisoring.repository;

import io.github.cruisoring.tuple.Tuple;

/**
 * Interface for map.values that are of <tt>Tuple</tt> containing at least 2 strong-typed values and could be retrieved with the key
 * @param <TKey>    Type of the Key of the map
 * @param <T>       Type of the first element of the Tuple value
 * @param <U>       Type of the second element of the Tuple value
 */
public interface TupleValues2<TKey, T,U> extends TupleValues1<TKey, T> {

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
