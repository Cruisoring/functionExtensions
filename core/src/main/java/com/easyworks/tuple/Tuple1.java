package com.easyworks.tuple;

/**
 * Tuple type with a single element persisted.
 * @param <T> Type of the persisted element
 */
public class Tuple1<T> extends Tuple
        implements WithFirst<T> {

    protected Tuple1(T t){
        super(t);
    }

}
