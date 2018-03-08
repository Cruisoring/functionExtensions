package com.easyworks.repository;

import com.easyworks.tuple.Dual;

/**
 * Interface for map.values that are of <tt>Tuple</tt> of 2 elements and keep the methods to retrieve them as strong-typed values
 * @param <TKey>    Type of the Key of the map
 * @param <T>       Type of the first element of the Tuple value
 * @param <U>       Type of the second element of the Tuple value
 */
public interface DualValues<TKey, T,U> {

    /**
     * Retreive the value of key as strong-typed Tuple of 2 elements
     * @param key   key to retrieve corresponding Tuple.Dual value
     * @return      corresponding Tuple.Dual value
     */
    Dual<T,U> retrieve(TKey key);

    /**
     * Get the first element of type <tt>T</tt>
     * @param key   key to retrieve corresponding Tuple.Dual value
     * @return      the first element of type <tt>T</tt>
     */
    default T getFirstValue(TKey key) {
        Dual<T,U> value = retrieve(key);
        return value == null ? null : value.getFirst();
    }

    /**
     * Get the second element of type <tt>U</tt>
     * @param key   key to retrieve corresponding Tuple.Dual value
     * @return      the second element of type <tt>U</tt>
     */
    default U getSecondValue(TKey key) {
        Dual<T,U> value = retrieve(key);
        return value == null ? null : value.getSecond();
    }
}
