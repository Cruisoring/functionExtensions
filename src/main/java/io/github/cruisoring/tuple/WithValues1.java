package io.github.cruisoring.tuple;

public interface WithValues1<T> extends WithValues {
    /**
     * Get the first element of <code>T</code>
     *
     * @return value of the persisted element of type <code>T</code>
     */
    default T getFirst() {
        return (T) getValue(0);
    }

}
