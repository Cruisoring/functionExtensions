package com.easyworks.tuple;

/**
 * Tuple type with two elements persisted.
 * @param <T> Type of the first persisted element
 * @param <U> Type of the second persisted element
 */
public class Dual<T,U> extends Tuple
        implements WithSecond<T,U> {

    protected Dual(T t, U u){
        super(t, u);
    }

}
