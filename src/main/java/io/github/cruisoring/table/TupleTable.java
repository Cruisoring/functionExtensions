package io.github.cruisoring.table;

import io.github.cruisoring.TypeHelper;
import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.utility.ArrayHelper;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TupleTable<R extends Tuple> implements ITable<R> {
    final List<String> columns;
    final int width;
    final List<R> rows = new ArrayList<>();
    final Map<String, Integer> columnIndexes;

    protected TupleTable(String... columns) {
        Objects.requireNonNull(columns);
        this.columns = Collections.unmodifiableList(Arrays.asList(columns));
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
        width = columns.length;
    }

    @Override
    public int getColumnIndex(String columnName) {
        return columns.indexOf(columnName);
    }

    @Override
    public Collection<String> getColumns() {
        return columns;
    }

    @Override
    public int width() {
        return rows.size();
    }

    @Override
    public TupleRow<R> getRow(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= rows.size())
            return null;
        return new TupleRow<>(columnIndexes, rows.get(rowIndex));
    }

    @Override
    public Map<String, Integer> getColumnIndexes() {
        return columnIndexes;
    }

    @Override
    public int size() {
        return rows.size();
    }

    @Override
    public boolean isEmpty() {
        return rows.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof Tuple) {
            return rows.contains(o);
        } else if (o instanceof TupleRow) {
            return rows.contains(((TupleRow) o).getValues());
        } else {
            return false;
        }
    }

    @Override
    public Iterator<TupleRow<R>> iterator() {
        Stream<TupleRow<R>> stream = rows.stream().map(v -> new TupleRow<R>(columnIndexes, v));
        return stream.iterator();
    }

    @Override
    public Object[] toArray() {
        return rows.stream().map(v -> new TupleRow<R>(columnIndexes, v))
                .toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        Class componentClass = ArrayHelper.getComponentType(a);
        if (componentClass.isAssignableFrom(TupleRow.class) || TupleRow.class.isAssignableFrom(componentClass)) {
            return (T[]) TypeHelper.convert(toArray(), a.getClass());
        } else if (componentClass.isAssignableFrom(Tuple.class) || Tuple.class.isAssignableFrom(componentClass)) {
            return (T[]) TypeHelper.convert(rows.toArray(), a.getClass());
        } else {
            return null;
        }
    }

    @Override
    public boolean add(TupleRow<R> row) {
        if (row == null || row.nameIndexes != this.columnIndexes) {
            return false;
        }

        return rows.add(row.getValues());
    }

    @Override
    public boolean addValues(R rowValues) {
        if (rowValues == null) {
            return false;
        }
        return rows.add(rowValues);
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        } else if (o instanceof TupleRow) {
            return rows.remove(((TupleRow) o).getValues());
        } else if (o instanceof Tuple) {
            return rows.remove(o);
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null || c.isEmpty()) {
            return false;
        }
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends TupleRow<R>> c) {
        if (c == null || c.isEmpty()) {
            return false;
        }
        Collection<R> values = c.stream().map(row -> row.getValues()).collect(Collectors.toList());
        return rows.addAll(values);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null || c.isEmpty()) {
            return false;
        }
        boolean changed = false;
        for (Object o : c) {
            changed |= remove(o);
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        rows.clear();
    }
}
