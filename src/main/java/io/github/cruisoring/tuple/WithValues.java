package io.github.cruisoring.tuple;

import java.util.Set;

public interface WithValues<T> {
    /**
     * Retrieve the element at specific index as an Object.
     * @param  index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    T getValue(int index) throws IndexOutOfBoundsException;

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    int getLength();

    /**
     * Get the set of this.hashCode() and all its elements' hashCodes as signatures.
     *
     * @return the hashCodes of this and its elements as a Set.
     */
    Set<Integer> getSignatures();
}
