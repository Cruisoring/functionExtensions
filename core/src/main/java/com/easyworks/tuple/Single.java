package com.easyworks.tuple;

/**
 * Tuple type with a single element persisted.
 * @param <T> Type of the persisted element
 */
public class Single<T> extends Tuple {

    protected Single(T t){
        super(t);
    }

    /**
     * Get the first element of <code>T</code>
     * @return  value of the persisted element
     */
    public T getFirst() {
        return (T)values[0];
    }
}
