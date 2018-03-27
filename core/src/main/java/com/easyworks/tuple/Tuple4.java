package com.easyworks.tuple;

/**
 * Tuple type with four elements persisted.
 * @param <T> Type of the first persisted element
 * @param <U> Type of the second persisted element
 * @param <V> Type of the third persisted element
 * @param <W> Type of the fourth persisted element
 */
public class Tuple4<T,U,V,W> extends Tuple
        implements WithFourth<T,U,V,W> {

    protected Tuple4(T t, U u, V v, W w){
        super(t, u, v, w);
    }

}
