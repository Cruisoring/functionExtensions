package io.github.cruisoring.tuple;

public interface WithValues4<T,U,V,W> extends WithValues {

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

}
