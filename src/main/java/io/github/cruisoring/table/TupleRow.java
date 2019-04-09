package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.utility.StringHelper;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TupleRow<R extends Tuple> implements WithNamedValues, Comparable<TupleRow> {
    final Map<String, Integer> nameIndexes;
    final R values;

    public TupleRow(Map<String, Integer> names, R values) {
        this.nameIndexes = names;
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
        Set<String> thisColumns = nameIndexes.keySet();
        if (thisColumns.size() != other.nameIndexes.size() || thisColumns.containsAll(other.nameIndexes.keySet())) {
            return false;
        } else if (thisColumns.stream().anyMatch(k -> !nameIndexes.get(k).equals(other.nameIndexes.get(k)))) {
            return false;
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
