package com.easyworks.tuple;

/**
 * Tuple type with four elements persisted.
 * @param <T> Type of the first persisted element
 * @param <U> Type of the second persisted element
 * @param <V> Type of the third persisted element
 * @param <W> Type of the fourth persisted element
 */
public class Quad<T,U,V,W> extends Tuple {

    protected Quad(T t, U u, V v, W w){
        super(t, u, v, w);
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

    /**
     * Get the fourth element of <code>W</code>
     * @return  value of the fourth element of type <code>W</code>
     */
    public W getFourth() {
        return (W)values[3];
    }
}
