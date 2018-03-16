package com.easyworks.repository;

import com.easyworks.tuple.Tuple;

/**
 * Interface for map.values that are of <tt>Tuple</tt> of 2 elements and keep the methods to retrieve them as strong-typed values
 * @param <TKey>    Type of the Key of the map
 * @param <T>       Type of the first element of the Tuple value
 * @param <U>       Type of the second element of the Tuple value
 * @param <V>       Type of the third element of the Tuple value
 * @param <W>       Type of the fourth element of the Tuple value
 * @param <X>       Type of the fifth element of the Tuple value
 * @param <Y>       Type of the sixth element of the Tuple value
 * @param <Z>       Type of the seventh element of the Tuple value
 */
public interface HeptaValues<TKey, T,U,V,W,X,Y,Z> extends HexaValues<TKey, T,U,V,W,X,Y> {
    /**
     * Retrieve the seventh value of the Tuple as type of <tt>Z</tt>
     * @param key   key to retrieve corresponding Tuple value as a whole
     * @return      seventh value of type <tt>Z</tt>
     */
    default Z getSeventhValue(TKey key) {
        Tuple tuple = retrieve(key);
        if(tuple == null)
            return null;
        return (Z) tuple.getValueAt(6);
    }
}
