package io.github.cruisoring.table;

import io.github.cruisoring.tuple.WithValues;

public interface WithValuesByName<T> extends WithValues<T> {

    IColumns getColumnIndexes();

    /**
     * Retrieve the element at specific index as an Object.
     *
     * @param name name of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    default T getValueByName(String name) {
        Integer index = getColumnIndexes().get(name);
        return index < 0 ? null : getValue(index);
    }

    /**
     * Convert the values as NamedValuePair array with their natural order.
     *
     * @return NameValuePair array
     */
    NameValuePair[] asNameValuePairs();
}
