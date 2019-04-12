package io.github.cruisoring.table;

import io.github.cruisoring.tuple.WithValues;
import io.github.cruisoring.utility.StringHelper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TupleRow<R extends WithValues> implements WithValuesByName {
    final ITableColumns columns;
    final WithValues values;

    public TupleRow(ITableColumns indexes, WithValues values) {
        this.columns = indexes;
        this.values = values;
    }

    public TupleRow(String[] columns, R values) {
        this.columns = new TableColumns(columns);
        this.values = values;
    }

    @Override
    public WithValues getValues() {
        return values;
    }

    public int compareTo(WithValues o) {
        return values.compareTo(o);
    }

    public Object getValue(int columnIndex) {
        return values.getValue(columnIndex);
    }

    public int getLength() {
        return values.getLength();
    }

    @Override
    public ITableColumns getColumnIndexes() {
        return columns;
    }

    @Override
    public NameValuePair[] asNameValuePairs() {
        NameValuePair[] pairs = new NameValuePair[columns.width()];
        for (String name : columns.getColumnNames()) {
            int index = columns.get(name);
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
        if (columns != other.columns) {
            Set<String> thisColumns = columns.keySet();
            if (thisColumns.size() != other.columns.size() || !thisColumns.containsAll(other.columns.keySet())) {
                return false;
            } else if (thisColumns.stream().anyMatch(k -> !columns.get(k).equals(other.columns.get(k)))) {
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
        int width = columns.width()>values.getLength() ? values.getLength() : columns.width();

        List<String> columnNames = columns.getColumnNames();
        String _string = IntStream.range(0, width).boxed()
                .map(i -> StringHelper.tryFormatString("\"%s\"=%s", columnNames.get(i), getValue(i)))
                .collect(Collectors.joining(", "));

        return "[" + _string + "]";
    }
}
