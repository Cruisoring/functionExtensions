package com.easyworks.tuple;

/**
 * Tuple type with six elements persisted.
 * @param <T> Type of the first persisted element
 * @param <U> Type of the second persisted element
 * @param <V> Type of the third persisted element
 * @param <W> Type of the fourth persisted element
 * @param <X> Type of the fifth persisted element
 * @param <Y> Type of the sixth persisted element
 */
public class Hexa<T,U,V,W,X,Y> extends Tuple implements
        WithSixth<T,U,V,W,X,Y> {

    protected Hexa(T t, U u, V v, W w, X x, Y y){
        super(t, u, v, w, x, y);
    }

}
