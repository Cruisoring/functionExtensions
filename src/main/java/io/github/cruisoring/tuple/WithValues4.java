package io.github.cruisoring.tuple;

public interface WithValues4<T, U, V, W> extends WithValues3<T, U, V> {

    /**
     * Get the fourth element of <code>W</code>
     *
     * @return value of the fourth element of type <code>W</code>
     */
    default W getFourth() {
        return (W) getValue(3);
    }

}
