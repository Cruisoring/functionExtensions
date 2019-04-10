package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;

import java.util.*;

public class TableColumns implements Map<String, Integer> {

    final Map<String, Integer> columnIndexes;

    public TableColumns(String... columns) {
        Map<String, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < columns.length; i++) {
            String column = columns[i];
            if (column == null) {
                throw new UnsupportedOperationException("Column name at index of " + i + " cannot be null");
            } else if (map.containsKey(column)) {
                throw new UnsupportedOperationException("Column name at index of " + i +
                        " is duplicated with the one at index of " + map.get(column));
            }
            map.put(column, i);
        }
        columnIndexes = Collections.unmodifiableMap(map);
    }

    public TableColumns(Map<String, Integer> indexes) {
        Objects.requireNonNull(indexes);
        Map<String, Integer> map = new LinkedHashMap<>(indexes);
        columnIndexes = Collections.unmodifiableMap(map);
    }


    @Override
    public int size() {
        return columnIndexes.size();
    }

    @Override
    public boolean isEmpty() {
        return columnIndexes.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return columnIndexes.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return columnIndexes.containsValue(value);
    }

    @Override
    public Integer get(Object key) {
        return columnIndexes.get(key);
    }

    @Override
    public Integer put(String key, Integer value) {
        return null;
    }

    @Override
    public Integer remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Integer> m) {
    }

    @Override
    public void clear() {
    }

    @Override
    public Set<String> keySet() {
        return columnIndexes.keySet();
    }

    @Override
    public Collection<Integer> values() {
        return columnIndexes.values();
    }

    @Override
    public Set<Entry<String, Integer>> entrySet() {
        return columnIndexes.entrySet();
    }

    public TupleRow of(final Object... elements) {
        if (elements == null) {
            return new TupleRow(columnIndexes, null);
        }
        int length = elements.length;
        switch (length) {
            case 0:
                return new TupleRow(columnIndexes, Tuple.UNIT);
            case 1:
                return new TupleRow1(columnIndexes, elements[0]);
            case 2:
                return new TupleRow2(columnIndexes, elements[0], elements[1]);
            case 3:
                return new TupleRow3(columnIndexes, elements[0], elements[1], elements[2]);
            case 4:
                return new TupleRow4(columnIndexes, elements[0], elements[1], elements[2], elements[3]);
            case 5:
                return new TupleRow5(columnIndexes, elements[0], elements[1], elements[2], elements[3], elements[4]);
            case 6:
                return new TupleRow6(columnIndexes, elements[0], elements[1], elements[2], elements[3], elements[4],
                        elements[5]);
            case 7:
                return new TupleRow7(columnIndexes, elements[0], elements[1], elements[2], elements[3], elements[4],
                        elements[5], elements[6]);
            case 8:
                return new TupleRow8(columnIndexes, elements[0], elements[1], elements[2], elements[3], elements[4],
                        elements[5], elements[6], elements[7]);
            case 9:
                return new TupleRow9(columnIndexes, elements[0], elements[1], elements[2], elements[3], elements[4],
                        elements[5], elements[6], elements[7], elements[8]);
            case 10:
                return new TupleRow10(columnIndexes, elements[0], elements[1], elements[2], elements[3], elements[4],
                        elements[5], elements[6], elements[7], elements[8], elements[9]);

            default:
                return new TupleRowPlus(columnIndexes, elements[0], elements[1], elements[2], elements[3], elements[4],
                        elements[5], elements[6], elements[7], elements[8], elements[9], elements[10],
                        Arrays.copyOfRange(elements, 11, length));
        }
    }
}
