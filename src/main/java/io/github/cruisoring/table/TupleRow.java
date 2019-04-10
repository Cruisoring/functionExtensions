package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.utility.StringHelper;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TupleRow<R extends Tuple> implements WithNamedValues, Comparable<TupleRow> {
    final Map<String, Integer> nameIndexes;
    final R values;

    public TupleRow(Map<String, Integer> indexes, R values) {
//        int length = values.getLength();
//        if(names.values().stream().anyMatch(i -> i<0 || i>= length)){
//            throw new UnsupportedOperationException();
//        }

        this.nameIndexes = indexes;
        this.values = values;
    }

    public TupleRow(String[] columns, R values) {
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
        nameIndexes = Collections.unmodifiableMap(map);
        this.values = values;
    }

    public R getValues() {
        return values;
    }

    public Object getValue(int columnIndex) throws IndexOutOfBoundsException {
        return values.getValue(columnIndex);
    }

    public int getLength() {
        return values.getLength();
    }

    @Override
    public Object getValue(String columnName) throws IndexOutOfBoundsException {
        if (!nameIndexes.containsKey(columnName)) {
            throw new IndexOutOfBoundsException("No column of " + columnName);
        }
        return values.getValue(nameIndexes.get(columnName));
    }

    @Override
    public NameValuePair[] asNameValuePairs() {
        NameValuePair[] pairs = new NameValuePair[getLength()];
        for (String name : nameIndexes.keySet()) {
            int index = nameIndexes.get(name);
            Object value = getValue(index);
            pairs[index] = new NameValuePair(name, value);
        }
        return pairs;
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof TupleRow)) {
            return false;
        } else if (obj == this) {
            return true;
        }

        TupleRow other = (TupleRow) obj;
        if (!other.canEqual(this) || other.getLength() != values.getLength()) {
            return false;
        }

        //If only the columns and their indexes of this TupleRow are compared, other.equals(this) might be different!!!
        if (nameIndexes != other.nameIndexes) {
            Set<String> thisColumns = nameIndexes.keySet();
            if (thisColumns.size() != other.nameIndexes.size() || !thisColumns.containsAll(other.nameIndexes.keySet())) {
                return false;
            } else if (thisColumns.stream().anyMatch(k -> !nameIndexes.get(k).equals(other.nameIndexes.get(k)))) {
                return false;
            }
        }

        return values.equals(other.values);
    }

    public boolean canEqual(Object obj) {
        if (!(obj instanceof TupleRow))
            return false;
        TupleRow other = (TupleRow) obj;
        return this.values.canEqual(other.values);
    }

    @Override
    public String toString() {
        String _string = nameIndexes.entrySet().stream()
                .map(entry -> StringHelper.tryFormatString("\"%s\"=%s", entry.getKey(), getValue(entry.getValue())))
                .collect(Collectors.joining(", "));
        return "[" + _string + "]";
    }

    @Override
    public int compareTo(TupleRow o) {
        return values.compareTo(o.values);
    }
}
