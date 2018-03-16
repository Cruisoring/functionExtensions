package com.easyworks.tuple;

/**
 * Tuple type with a single element persisted.
 * @param <T> Type of the persisted element
 */
public class Single<T> extends Tuple
        implements WithFirst<T> {

    protected Single(T t){
        super(t);
    }

}
