package io.github.cruisoring.table;

import io.github.cruisoring.tuple.WithValues;

import java.util.LinkedHashMap;
import java.util.Map;

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
     * Convert the values as Map that keep their original order.
     *
     * @return a Map with the names as keys and keep .
     */
    default Map<String, T> asMap(){
        IColumns columns = getColumnIndexes();
        String[][] indexedColumns = columns.getIndexedColumns();
        Map<String, T> map = new LinkedHashMap<>();
        int len = indexedColumns.length;
        for (int i = 0; i < len; i++) {
            map.put(indexedColumns[i][0], getValue(i));
        }
        return map;
    }
}
