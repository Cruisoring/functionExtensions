package com.easyworks.tuple;

/**
 * Tuple type with three elements persisted.
 * @param <T> Type of the first persisted element
 * @param <U> Type of the second persisted element
 * @param <V> Type of the third persisted element
 */
public class Triple<T,U,V> extends Tuple
    implements WithThird<T,U,V> {

    protected Triple(T t, U u, V v){
        super(t, u, v);
    }

}
