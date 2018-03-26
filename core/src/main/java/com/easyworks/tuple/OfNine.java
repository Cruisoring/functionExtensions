package com.easyworks.tuple;

/**
 * Tuple type with 9 elements persisted.
 * @param <T> Type of the first persisted element
 * @param <U> Type of the second persisted element
 * @param <V> Type of the third persisted element
 * @param <W> Type of the fourth persisted element
 * @param <X> Type of the fifth persisted element
 * @param <Y> Type of the sixth persisted element
 * @param <Z> Type of the seventh persisted element
 * @param <A> Type of the 8th persisted element
 * @param <B> Type of the 9th persisted element
 */
public class OfNine<T,U,V,W,X,Y,Z,A,B> extends Tuple
        implements WithNinth<T,U,V,W,X,Y,Z,A,B> {

    protected OfNine(T t, U u, V v, W w, X x, Y y, Z z, A a, B b){
        super(t, u, v, w, x, y, z, a, b);
    }

}