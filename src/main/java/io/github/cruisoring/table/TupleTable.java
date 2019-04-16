package io.github.cruisoring.table;

import io.github.cruisoring.TypeHelper;
import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues;
import io.github.cruisoring.utility.ArrayHelper;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TupleTable<R extends WithValues> implements ITable<R> {
    final IColumns columns;
    final List<WithValues> rows = new ArrayList<>();
    final Class[] elementTypes;

    protected TupleTable(IColumns columns, Class... elementTypes){
        Objects.requireNonNull(columns);
        this.columns = columns;
        this.elementTypes = elementTypes;
    }

    @Override
    public int getColumnIndex(String columnName) {
        return columns.get(columnName);
    }

    @Override
    public Collection<String> getColumns() {
        return columns.getColumnNames();
    }

    @Override
    public int width() {
        return elementTypes.length;
    }

    @Override
    public Class[] getElementTypes() {
        return elementTypes;
    }

    @Override
    public WithValuesByName getRow(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= rows.size())
            return null;
        return new TupleRow<>(columns, rows.get(rowIndex));
    }

    @Override
    public Map<String, Integer> getColumnIndexes() {
        return columns;
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
        if(o == null || !(o instanceof WithValues)){
            return false;
        } else {
            return rows.contains(((WithValues) o).getValues());
        }
    }

    @Override
    public Iterator<WithValuesByName> iterator() {
        Stream<WithValuesByName> stream = rows.stream().map(v -> new TupleRow<R>(columns, v));
        return stream.iterator();
    }

    @Override
    public Object[] toArray() {
        return rows.stream().map(v -> new TupleRow<R>(columns, v))
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
    public boolean add(WithValuesByName row) {


        return false;
    }

    @Override
    public boolean addValues(WithValues rowValues) {
        if (rowValues == null) {
            return false;
        }

        
        return rows.add(rowValues);
    }

    @Override
    public boolean remove(Object o) {
        if (o == null || !(o instanceof WithValues)) {
            return false;
        } else {
            return rows.remove(((WithValues) o).getValues());
        }
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
    public boolean addAll(Collection<? extends WithValuesByName> c) {
        if (c == null || c.isEmpty()) {
            return false;
        }

        boolean added = false;
        for(WithValuesByName r : c){
            if(add(r)){
                added = true;
            }
        }
        return added;
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
        if(c == null){
            return false;
        }
        Set<WithValues> valueSet = c.stream()
                .filter(item -> item instanceof WithValues)
                .map(item -> ((WithValues)item).getValues())
                .collect(Collectors.toSet());
        int size = size();
        int removed = 0;
        for (int i = size-1; i >= 0; i--) {
            if(!valueSet.contains(rows.get(i))){
                rows.remove(i);
                removed++;
            }
        }

        return removed != 0;
    }

    @Override
    public void clear() {
        rows.clear();
    }
}
