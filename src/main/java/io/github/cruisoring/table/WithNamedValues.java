package io.github.cruisoring.table;

import io.github.cruisoring.tuple.WithValues;

public interface WithNamedValues<T> extends WithValues<T> {
    /**
     * Retrieve the element at specific index as an Object.
     *
     * @param name name of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    T getValue(String name) throws IndexOutOfBoundsException;

    /**
     * Convert the values as NamedValuePair array with their natural order.
     *
     * @return NameValuePair array
     */
    NameValuePair[] asNameValuePairs();
}
