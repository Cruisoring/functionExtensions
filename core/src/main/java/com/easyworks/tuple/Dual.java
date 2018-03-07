package com.easyworks.tuple;

/**
 * Tuple type with two elements persisted.
 * @param <T> Type of the first persisted element
 * @param <U> Type of the second persisted element
 */
public class Dual<T,U> extends Tuple {

    protected Dual(T t, U u){
        super(t, u);
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
}
