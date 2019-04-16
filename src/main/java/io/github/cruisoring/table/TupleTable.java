package io.github.cruisoring.table;

import io.github.cruisoring.TypeHelper;
import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues;
import io.github.cruisoring.tuple.WithValues2;
import io.github.cruisoring.utility.ArrayHelper;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
    public IColumns getColumns() {
        return columns;
    }

    @Override
    public int getColumnIndex(String columnName) {
        return columns.get(columnName);
    }

    @Override
    public Collection<String> getDisplayedNames() {
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
    public WithValuesByName getRow(int rowIndex, IColumns viewColumns) {
        Objects.requireNonNull(viewColumns);

        if(rowIndex < 0 || rowIndex >= rows.size()){
            return null;
        }

        //More efficient since the mapping is cached for later use
        WithValues<Integer> mappedIndex = viewColumns.mapIndexes(getColumns());
        if(mappedIndex.anyMatch(v -> v == null)){
            return null;        //Cannot find all columns
        }

        WithValues row = rows.get(rowIndex);
        Object[] viewElements = ArrayHelper.create(Object.class, mappedIndex.getLength(),
                i -> row.getValue(mappedIndex.getValue(i)));
        Tuple tuple = Tuple.of(viewElements);
        return new TupleRow(viewColumns, tuple);
    }

    @Override
    public WithValuesByName[] getAllRows() {
        WithValuesByName[] namedRows = ArrayHelper.create(WithValuesByName.class, size(), i -> new TupleRow(columns, rows.get(i)));
        return namedRows;
    }


    @Override
    public WithValuesByName[] getAllRows(IColumns viewColumns) {
        Objects.requireNonNull(viewColumns);

        //More efficient since the mapping is cached for later use
        WithValues<Integer> mappedIndex = viewColumns.mapIndexes(getColumns());
        if(mappedIndex.anyMatch(v -> v == null)){
            return null;        //Cannot find all columns
        }

        int size = rows.size();
        int width = viewColumns.width();
        WithValuesByName[] namedRows = new WithValuesByName[size];
        for (int i = 0; i < size; i++) {
            Object[] viewElements = new Object[width];
            WithValues row = rows.get(i);
            for (int j = 0; j < width; j++) {
                viewElements[j] = row.getValue(mappedIndex.getValue(j));
            }
            Tuple tuple = Tuple.of(viewElements);
            namedRows[i] = new TupleRow(viewColumns, tuple);
        }
        return namedRows;
    }


    @Override
    public boolean add(WithValuesByName row) {
        if(row == null){
            return false;
        }

        if(row.getColumnIndexes() == columns){
            return addValues(row.getValues());
        }

        WithValues<Integer> mappedIndexes = columns.mapIndexes(row.getColumnIndexes());
        if(mappedIndexes.anyMatch(i -> i==null)){
            return false;
        }

        int _width = width();
        for (int i = 0; i < _width; i++) {
            int position = mappedIndexes.getValue(i);
            Object value = row.getValue(position);
            Class expectedType = elementTypes[i];
            if(value != null && !(value.getClass().isAssignableFrom(expectedType))){
                Logger.V("The value '%s' at position %d is not assignable from %s", value.toString(), position, expectedType.getSimpleName());
                return false;
            }
        }

        return false;
    }

    @Override
    public boolean addValues(WithValues rowValues) {
        if (rowValues == null) {
            return false;
        }

        int _width = width();
        for (int i = 0; i < _width; i++) {
            Object value = rowValues.getValue(i);
            Class expectedType = elementTypes[i];
            if(value != null && !(value.getClass().isAssignableFrom(expectedType))){
                Logger.V("The value '%s' at position %d is not assignable from %s", value.toString(), i, expectedType.getSimpleName());
                return false;
            }
        }

        return rows.add(rowValues);
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
        } else if(o instanceof WithValuesByName) {
            WithValuesByName other = (WithValuesByName)o;
            if(other.getColumnIndexes() == columns){
                //Simplify the evaluation by assuming there is no extra columns to be evaluated
                return rows.contains(other.getValues());
            }

            WithValues<Integer> mappedIndexes = columns.mapIndexes(other.getColumnIndexes());
            if(mappedIndexes.anyMatch(i -> i==null)){
                return false;
            }

            Object[] elements = IntStream.range(0, width()).boxed().map(i -> mappedIndexes.getValue(i))
                    .map(i -> other.getValue(i)).toArray();

            return rows.contains(Tuple.of(elements));
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
        if (componentClass.isAssignableFrom(WithValuesByName.class) || WithValuesByName.class.isAssignableFrom(componentClass)) {
            return (T[]) TypeHelper.convert(toArray(), a.getClass());
        } else if (componentClass.isAssignableFrom(WithValues.class) || WithValues.class.isAssignableFrom(componentClass)) {
            return (T[]) TypeHelper.convert(rows.toArray(), a.getClass());
        } else {
            return null;
        }
    }

    @Override
    public boolean remove(Object o) {
        if(o == null || !(o instanceof WithValues)){
            return false;
        } else if(o instanceof WithValuesByName) {
            WithValuesByName other = (WithValuesByName)o;
            if(other.getColumnIndexes() == columns){
                //Simplify the evaluation by assuming there is no extra columns to be evaluated
                return rows.remove(other.getValues());
            }

            WithValues<Integer> mappedIndexes = columns.mapIndexes(other.getColumnIndexes());
            if(mappedIndexes.anyMatch(i -> i==null)){
                return false;
            }

            Object[] elements = IntStream.range(0, width()).boxed().map(i -> mappedIndexes.getValue(i))
                    .map(i -> other.getValue(i)).toArray();
            Tuple row = Tuple.of(elements);
            return rows.contains(row) ? rows.remove(row) : false;
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
