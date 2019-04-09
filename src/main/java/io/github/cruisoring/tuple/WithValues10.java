package io.github.cruisoring.tuple;

public interface WithValues10<T, U, V, W, X, Y, Z, A, B, C> extends WithValues9<T, U, V, W, X, Y, Z, A, B> {

    /**
     * Get the 10th element of <code>C</code>
     *
     * @return value of the 10th element of type <code>C</code>
     */
    default C getTenth() {
        return (C) getValue(9);
    }
}