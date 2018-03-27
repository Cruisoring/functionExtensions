package com.easyworks.tuple;

/**
 * Tuple type with seven elements persisted.
 * @param <T> Type of the first persisted element
 * @param <U> Type of the second persisted element
 * @param <V> Type of the third persisted element
 * @param <W> Type of the fourth persisted element
 * @param <X> Type of the fifth persisted element
 * @param <Y> Type of the sixth persisted element
 * @param <Z> Type of the seventh persisted element
 */
public class Tuple7<T,U,V,W,X,Y,Z> extends Tuple
        implements WithSeventh<T,U,V,W,X,Y,Z> {

    protected Tuple7(T t, U u, V v, W w, X x, Y y, Z z){
        super(t, u, v, w, x, y, z);
    }

}
