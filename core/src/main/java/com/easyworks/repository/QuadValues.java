package com.easyworks.repository;

import com.easyworks.tuple.Tuple;

/**
 * Interface for map.values that are of <tt>Tuple</tt> of 2 elements and keep the methods to retrieve them as strong-typed values
 * @param <TKey>    Type of the Key of the map
 * @param <T>       Type of the first element of the Tuple value
 * @param <U>       Type of the second element of the Tuple value
 * @param <V>       Type of the third element of the Tuple value
 * @param <W>       Type of the fourth element of the Tuple value
 */
public interface QuadValues<TKey, T,U,V,W> extends TripleValues<TKey, T,U,V> {

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
