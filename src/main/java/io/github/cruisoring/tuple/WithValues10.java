package io.github.cruisoring.tuple;

public interface WithValues10<T,U,V,W,X,Y,Z,A,B,C> extends WithValues {

    /**
     * Get the first element of <code>T</code>
     * @return  value of the persisted element of type <code>T</code>
     */
    default T getFirst() {
        return (T) getValue(0);
    }

    /**
     * Get the second element of <code>U</code>
     * @return  value of the second element of type <code>U</code>
     */
    default U getSecond() {
        return (U) getValue(1);
    }

    /**
     * Get the third element of <code>V</code>
     * @return  value of the third element of type <code>V</code>
     */
    default V getThird() {
        return (V) getValue(2);
    }

    /**
     * Get the fourth element of <code>W</code>
     * @return  value of the fourth element of type <code>W</code>
     */
    default W getFourth() {
        return (W) getValue(3);
    }

    /**
     * Get the fifth element of <code>X</code>
     * @return  value of the fifth element of type <code>X</code>
     */
    default X getFifth() {
        return (X) getValue(4);
    }

    /**
     * Get the sixth element of <code>Y</code>
     * @return  value of the sixth element of type <code>Y</code>
     */
    default Y getSixth() {
        return (Y) getValue(5);
    }

    /**
     * Get the seventh element of <code>Z</code>
     * @return  value of the seventh element of type <code>Z</code>
     */
    default Z getSeventh() {
        return (Z) getValue(6);
    }

    /**
     * Get the eighth element of <code>A</code>
     * @return  value of the eighth element of type <code>A</code>
     */
    default A getEighth() {
        return (A) getValue(7);
    }

    /**
     * Get the nineth element of <code>B</code>
     * @return  value of the nineth element of type <code>B</code>
     */
    default B getNineth() {
        return (B) getValue(8);
    }

    /**
     * Get the 10th element of <code>C</code>
     * @return  value of the 10th element of type <code>C</code>
     */
    default C getTenth() {
        return (C) getValue(9);
    }
}