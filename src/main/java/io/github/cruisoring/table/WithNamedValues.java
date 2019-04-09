package io.github.cruisoring.table;

public interface WithNamedValues<T> {
    /**
     * Retrieve the element at specific index as an Object.
     *
     * @param name name of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    T getValue(String name) throws IndexOutOfBoundsException;

    /**
     * Retrieve the element at specific index as an Object.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    T getValue(int index) throws IndexOutOfBoundsException;
}
