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
public class Hepta<T,U,V,W,X,Y,Z> extends Tuple {

    protected Hepta(T t, U u, V v, W w, X x, Y y, Z z){
        super(t, u, v, w, x, y, z);
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

    /**
     * Get the fifth element of <code>X</code>
     * @return  value of the fifth element of type <code>X</code>
     */
    public X getFifth() {
        return (X)values[4];
    }

    /**
     * Get the sixth element of <code>Y</code>
     * @return  value of the sixth element of type <code>Y</code>
     */
    public Y getSixth() {
        return (Y)values[5];
    }

    /**
     * Get the seventh element of <code>Z</code>
     * @return  value of the seventh element of type <code>Z</code>
     */
    public Z getSeventh() {
        return (Z)values[6];
    }
}
