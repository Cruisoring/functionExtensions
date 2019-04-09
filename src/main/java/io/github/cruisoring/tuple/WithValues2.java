package io.github.cruisoring.tuple;

public interface WithValues2<T, U> extends WithValues1<T> {
    /**
     * Get the second element of <code>U</code>
     *
     * @return value of the second element of type <code>U</code>
     */
    default U getSecond() {
        return (U) getValue(1);
    }

}
