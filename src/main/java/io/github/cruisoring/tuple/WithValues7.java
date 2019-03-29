package io.github.cruisoring.tuple;

public interface WithValues7<T,U,V,W,X,Y,Z> extends WithValues {

    /**
     * Get the first element of <code>T</code>
     * @return  value of the persisted element of type <code>T</code>
     */
    default T getFirst() {
        return (T)getValueAt(0);
    }

    /**
     * Get the second element of <code>U</code>
     * @return  value of the second element of type <code>U</code>
     */
    default U getSecond() {
        return (U)getValueAt(1);
    }

    /**
     * Get the third element of <code>V</code>
     * @return  value of the third element of type <code>V</code>
     */
    default V getThird() {
        return (V)getValueAt(2);
    }

    /**
     * Get the fourth element of <code>W</code>
     * @return  value of the fourth element of type <code>W</code>
     */
    default W getFourth() {
        return (W)getValueAt(3);
    }

    /**
     * Get the fifth element of <code>X</code>
     * @return  value of the fifth element of type <code>X</code>
     */
    default X getFifth() {
        return (X)getValueAt(4);
    }

    /**
     * Get the sixth element of <code>Y</code>
     * @return  value of the sixth element of type <code>Y</code>
     */
    default Y getSixth() {
        return (Y)getValueAt(5);
    }

    /**
     * Get the seventh element of <code>Z</code>
     * @return  value of the seventh element of type <code>Z</code>
     */
    default Z getSeventh() {
        return (Z)getValueAt(6);
    }
}
