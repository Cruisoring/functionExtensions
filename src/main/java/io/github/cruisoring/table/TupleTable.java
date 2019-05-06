package io.github.cruisoring.table;

import io.github.cruisoring.TypeHelper;
import io.github.cruisoring.function.FunctionThrowable;
import io.github.cruisoring.function.PredicateThrowable;
import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues;
import io.github.cruisoring.utility.ArrayHelper;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.github.cruisoring.Asserts.checkWithoutNull;

public class TupleTable<R extends WithValues> implements ITable<R> {
    final IColumns columns;
    final List<WithValues> rows;
    final Class[] elementTypes;

    protected TupleTable(Supplier<List<WithValues>> rowsSupplier, IColumns columns, Class... elementTypes){
        this.columns = checkWithoutNull(columns, elementTypes);
        this.rows = rowsSupplier == null ? new ArrayList<>() : rowsSupplier.get();
        this.elementTypes = elementTypes;
    }

    protected TupleTable(IColumns columns, Class... elementTypes){
        this(null, columns, elementTypes);
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

        return columns.asRow(rows.get(rowIndex));
    }

    @Override
    public WithValuesByName getRow(Map<String, Object> valuesByName){
        int index = indexOf(checkWithoutNull(valuesByName));

        return getRow(index);
    }

    @Override
    public WithValuesByName getRow(int rowIndex, IColumns viewColumns) {
        if(rowIndex < 0 || rowIndex >= rows.size()){
            return null;
        }

        //More efficient since the mapping is cached for later use
        WithValues<Integer> mappedIndex = checkWithoutNull(viewColumns).mapIndexes(getColumns());
        if(mappedIndex.anyMatch(v -> v == null)){
            return null;        //Cannot find all columns
        }

        WithValues row = rows.get(rowIndex);
        Object[] viewElements = (Object[]) ArrayHelper.create(Object.class, mappedIndex.getLength(),
                i -> row.getValue(mappedIndex.getValue(i)));
        Tuple tuple = Tuple.of(viewElements);
        return new TupleRow(viewColumns, tuple);
    }

    @Override
    public WithValuesByName[] getAllRows() {
        WithValuesByName[] namedRows = (WithValuesByName[]) ArrayHelper.create(WithValuesByName.class, size(), i -> new TupleRow(columns, rows.get(i)));
        return namedRows;
    }

    @Override
    public WithValuesByName[] getAllRows(Map<String, PredicateThrowable> expectedConditions){
        WithValuesByName[] matchedRows = streamOfRows(expectedConditions)
                .toArray(size -> new WithValuesByName[size]);

        return matchedRows;
    }

    @Override
    public Stream<WithValuesByName> streamOfRows(Map<String, PredicateThrowable> expectedConditions){
        checkWithoutNull(expectedConditions);
        if(expectedConditions.isEmpty()){
            throw new IllegalArgumentException("No expections specified.");
        }

        Map<Integer, String> map = getIndexedNames(expectedConditions.keySet());
        Map<Integer, PredicateThrowable> indexedPredicates = map.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> expectedConditions.get(entry.getValue())
                ));

        Stream<WithValuesByName> matchedRows = rows.stream()
                .filter(row -> row.meetConditions(indexedPredicates))
                .map(row -> new TupleRow(columns, row));

        return matchedRows;
    }

    @Override
    public ITable getView(IColumns viewColumns) {
        checkWithoutNull(viewColumns);

        //More efficient since the mapping is cached for later use
        WithValues<Integer> mappedIndex = viewColumns.mapIndexes(getColumns());
        if(mappedIndex.anyMatch(v -> v == null)){
            return null;        //Cannot find all columns
        }

        Integer[] indexes = IntStream.range(0, mappedIndex.getLength()).boxed()
                .map(i -> mappedIndex.getValue(i)).toArray(size -> new Integer[size]);
        Class[] eTypes = Arrays.stream(indexes).map(i -> elementTypes[i])
                .toArray(size -> new Class[size]);
        TupleTable table = new TupleTable(viewColumns, eTypes);

        int size = rows.size();
        int width = indexes.length;
        for (int i = 0; i < size; i++) {
            WithValues row = rows.get(i);
            Object[] elements = (Object[]) ArrayHelper.create(Object.class, width, vIndex -> row.getValue(indexes[vIndex]));
            table.rows.add(Tuple.of(elements));
        }
        return table;
    }

    @Override
    public boolean replace(WithValuesByName row, Map<String, Object> newValues) {
        checkWithoutNull(row, newValues);

        int index = indexOf(row);
        if(index < 0 || newValues.isEmpty()) {
            return false;
        }

        try {
            WithValues tuple = rows.get(index);
            Map<Integer, String> indexedNames = getIndexedNames(newValues.keySet());
            Object[] elements = (Object[]) ArrayHelper.create(Object.class, tuple.getLength(), i -> indexedNames.containsKey(i) ? newValues.get(indexedNames.get(i)) : tuple.getValue(i));
            WithValues replacement = Tuple.of(elements);
            rows.remove(index);
            rows.add(index, replacement);
            return true;
        }catch (Exception ignored){
            return false;
        }
    }

    @Override
    public boolean update(WithValuesByName row, Map<String, FunctionThrowable<WithValuesByName, Object>> valueSuppliers){
        checkWithoutNull(row, valueSuppliers);

        int index = indexOf(row);
        if(index < 0) {
            return false;
        }

        try {
            WithValuesByName oldRow = getRow(index);
            Map<Integer, String> indexedNames = getIndexedNames(valueSuppliers.keySet());
            Object[] elements = (Object[]) ArrayHelper.create(Object.class, row.getLength(), i ->
                    indexedNames.containsKey(i) ? valueSuppliers.get(indexedNames.get(i)).orElse(null).apply(oldRow) : oldRow.getValue(i));
            WithValues replacement = Tuple.of(elements);
            rows.remove(index);
            rows.add(index, replacement);
            return true;
        }catch (Exception ignored){
            return false;
        }
    }

    @Override
    public int updateAll(Stream<WithValuesByName> rowsToBeUpdate, Map<String, FunctionThrowable<WithValuesByName, Object>> valueSuppliers) {
        checkWithoutNull(rowsToBeUpdate, valueSuppliers);

        Map<Integer, String> indexedNames = getIndexedNames(valueSuppliers.keySet());
        Map<Integer, WithValues> replacements = new HashMap<>();
        for (WithValuesByName row : (Iterable<WithValuesByName>)rowsToBeUpdate::iterator) {
            int index = indexOf(row);
            if(index == -1){
                continue;
            }
            WithValues oldRow = rows.get(index);
            Object[] elements = (Object[]) ArrayHelper.create(Object.class, oldRow.getLength(), i ->
                    indexedNames.containsKey(i) ? valueSuppliers.get(indexedNames.get(i)).orElse(null).apply(row) : oldRow.getValue(i));
            replacements.put(index, Tuple.of(elements));
        }

        for (Map.Entry<Integer, WithValues> entry : replacements.entrySet()) {
            int index = entry.getKey();
            rows.remove(index);
            rows.add(index, entry.getValue());
        }
        return replacements.size();
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
    public boolean addValues(Map<String, Object> valuesByName){
        if(valuesByName == null){
            return false;
        }

        Map<Integer, String> indexedNames = getIndexedNames(valuesByName.keySet());
        int length = width() > columns.width() ? width() : columns.width();
        Object[] values = IntStream.range(0, length).boxed()
                .map(i -> indexedNames.containsKey(i) ? valuesByName.get(indexedNames.get(i)) : null)
                .toArray();
        Tuple row = Tuple.of(values);
        return addValues(row);
    }

    @Override
    public boolean addValues(WithValues rowValues) {
        int _width = width();
        if (rowValues == null || rowValues.getLength() < width()) {
            return false;
        }

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
    public int indexOf(WithValuesByName row){
        if(row == null) {
            return -1;
        }

        IColumns rowColumns = row.getColumnIndexes();
        if(rowColumns == columns){
            return rows.indexOf(row.getValues());
        }

        WithValues<Integer> mappedIndexes = columns.mapIndexes(rowColumns);
        if(mappedIndexes.anyMatch(i -> i==null)){
            return -1;
        }

        Object[] elements = IntStream.range(0, width()).boxed().map(i -> mappedIndexes.getValue(i))
                .map(i -> row.getValue(i)).toArray();

        Tuple values = Tuple.of(elements);
        return rows.indexOf(values);
    }

    @Override
    public int indexOf(Map<String, Object> valuesByName){
        Map<Integer, String> map = getIndexedNames(valuesByName.keySet());

        final Map<Integer, Object> expectedValues = map.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> valuesByName.get(entry.getValue())
                ));

        for (int i = 0; i < size(); i++) {
            if(rows.get(i).isMatched(expectedValues)){
                return i;
            }
        }
        return -1;
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
            return rows.contains(row) && rows.remove(row);
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
