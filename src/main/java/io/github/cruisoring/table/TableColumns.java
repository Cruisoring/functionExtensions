package io.github.cruisoring.table;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Keep column names and their indexes as a map, aliases could be defined in the given Map to create
 * <code>TableColumns</code> with multiple String keys pointing to the same index.
 * Combined with <code>TupleRow</code> or underlying <code>Tuple</code> lists, it might be possible to define views on the same row values.
 */
public class TableColumns implements ITableColumns {

    final Map<Integer, List<String>> indexedColumns;
    final Map<String, Integer> columnIndexes;
    final List<String> columnNames;

    /**
     * Construct the TableColumns with column names directly.
     * @param columnNames   Names of the columns that cannot be null or duplicated.
     */
    public TableColumns(String... columnNames) {
        Map<Integer, List<String>> indexes = new HashMap<>();
        Map<String, Integer> map = new LinkedHashMap<>();
        List<String> names = new ArrayList<>();
        for (int i = 0; i < columnNames.length; i++) {
            String columnName = columnNames[i];
            if (columnName == null) {
                throw new UnsupportedOperationException("Column name at index of " + i + " cannot be null");
            } else if (map.containsKey(columnName)) {
                throw new UnsupportedOperationException("Column name at index of " + i +
                        " is duplicated with the one at index of " + map.get(columnName));
            }
            map.put(columnName, i);
            names.add(columnName);
            indexes.put(i, Arrays.asList(columnName));
        }
        indexedColumns = Collections.unmodifiableMap(indexes);
        columnIndexes = Collections.unmodifiableMap(map);
        this.columnNames = Collections.unmodifiableList(names);
    }

    /**
     * Construct the TableColumns with pre-build indexes with no missing ones, but could have aliases to make one column have multiple names.
     * @param cIndexes   Pre-defined column names to position map.
     */
    public TableColumns(Map<String, Integer> cIndexes) {
        Objects.requireNonNull(cIndexes);

        Map<Integer, List<String>> indexes = new HashMap<>();
        Map<String, Integer> map = new LinkedHashMap<>();
        Integer max = Integer.MIN_VALUE;
        for (Entry<String, Integer> entry : cIndexes.entrySet()) {
            Integer index = entry.getValue();
            max = index > max ? index : max;
            String name = entry.getKey();
            if(indexes.containsKey(index)){
                indexes.get(index).add(name);
            } else {
                indexes.put(index, new ArrayList<>(Arrays.asList(name)));
            }
        }
        if(max < 0){
            throw new UnsupportedOperationException();
        }

        IntStream.rangeClosed(0, max).forEach(i -> {
            if(!indexes.containsKey(i)){
                throw new UnsupportedOperationException("Missing column name definition for index of " + i);
            }
            indexes.get(i).forEach(name -> map.put(name, i));
        });

        indexedColumns = Collections.unmodifiableMap(indexes);
        columnIndexes = Collections.unmodifiableMap(map);
        this.columnNames = IntStream.rangeClosed(0, max).boxed()
            .map(i -> indexedColumns.get(i).get(0)).collect(Collectors.toList());
    }

    /**
     * Get the names for each column in order.
     * @return  List of the names each represent the column at the same position as these names.
     */
    @Override
    public List<String> getColumnNames(){
        return columnNames;
    }

    /**
     * The width describe how many columns have been defiend by this <code>TableColumns</code>
     * @return
     */
    @Override
    public int width(){
        return columnNames.size();
    }

    /**
     * Retrieve the immutable Column names to indexes map.
     * @return  Immutable map can be shared safely by multiple TupleRows or TupleTables.
     */
    @Override
    public Map<String, Integer> getColumnIndexes(){
        return columnIndexes;
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
    /**
     * If there is no key matched, it would return -1 instead of throwing NullPointException.
     */
    public Integer get(Object key) {
        return columnIndexes.containsKey(key) ? columnIndexes.get(key) : -1;
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

}
