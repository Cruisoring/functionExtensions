package com.easyworks.repository;

import com.easyworks.tuple.Tuple;

/**
 * Interface for map.values that are of <tt>Tuple</tt> containing at least 4 strong-typed values and could be retrieved with the key
 * @param <TKey>    Type of the Key of the map
 * @param <T>       Type of the first element of the Tuple value
 * @param <U>       Type of the second element of the Tuple value
 * @param <V>       Type of the third element of the Tuple value
 * @param <W>       Type of the fourth element of the Tuple value
 */
public interface TupleValues4<TKey, T,U,V,W> extends TupleValues3<TKey, T,U,V> {

    /**
     * Retrieve the fourth value of the Tuple as type of <tt>W</tt>
     * @param key   key to retrieve corresponding Tuple value as a whole
     * @return      fourth value of type <tt>W</tt>
     */
    default W getFourthValue(TKey key) {
        Tuple tuple = retrieve(key);
        if(tuple == null)
            return null;
        return (W) tuple.getValueAt(3);
    }
}
