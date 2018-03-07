package com.easyworks.tuple;

/**
 * Tuple type with three elements persisted.
 * @param <T> Type of the first persisted element
 * @param <U> Type of the second persisted element
 * @param <V> Type of the third persisted element
 */
public class Triple<T,U,V> extends Tuple {

    protected Triple(T t, U u, V v){
        super(t, u, v);
    }

    /**
     * Get the first element of <code>T</code>
     * @return  value of the persisted element of type <code>T</code>
     */
    public T getFirst() {
        return (T)values[0];
    }

    /**
     * Get the second element of <code>U</code>
     * @return  value of the second element of type <code>U</code>
     */
    public U getSecond() {
        return (U)values[1];
    }

    /**
     * Get the third element of <code>V</code>
     * @return  value of the third element of type <code>V</code>
     */
    public V getThird() {
        return (V)values[2];
    }
}
