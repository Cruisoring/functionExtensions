package io.github.cruisoring.table;

import io.github.cruisoring.tuple.WithValues2;
import io.github.cruisoring.utility.ArrayHelper;

import java.lang.reflect.Type;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Keep column names and their indexes as a map, aliases could be defined in the given Map to create
 * <code>MetaData</code> with multiple String keys pointing to the same index.
 * Combined with <code>TupleRow</code> or underlying <code>Tuple</code> lists, it might be possible to define views on the same row values.
 */
public class MetaData implements IMetaData {

    final Map<Integer, List<String>> indexedColumns;
    final Map<String, Integer> columnIndexes;
    final List<String> columnNames;
    final Type[] elementTypes;

    /**
     * Construct the MetaData with column names directly.
     * @param columnNames   Names of the columns that cannot be null or duplicated.
     */
    public MetaData(String... columnNames) {
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
        this.elementTypes = ArrayHelper.getNewArray(Type.class, columnNames.length, Object.class);
    }

//    /**
//     * Construct the MetaData with pre-build indexes with no missing ones, but could have aliases to make one column have multiple names.
//     * @param cIndexes   Pre-defined column names to position map.
//     */
    public MetaData(Map<Integer, WithValues2<Type, String[]>> columnDefintions){
        checkNotNull(columnDefintions);

        int width = columnDefintions.size();
        this.elementTypes = new Type[width];
        Map<Integer, List<String>> indexes = new HashMap<>();
        Map<String, Integer> map = new LinkedHashMap<>();
        List<String> names = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            checkState(columnDefintions.containsKey(i));
            WithValues2<Type, String[]> defintion = columnDefintions.get(i);
            elementTypes[i] = defintion.getFirst() == null ? Object.class : defintion.getFirst();
            String[] alias = defintion.getSecond();
            int aliasLength = alias.length;
            checkState(aliasLength > 0 && Arrays.stream(alias).allMatch(name -> name != null));
            indexes.put(i, Arrays.asList(alias));
            for (int j = 0; j < aliasLength; j++) {
                checkState(!map.containsKey(alias[j]));
                map.put(alias[j], i);
            }
            names.add(alias[0]);
        }
        indexedColumns = Collections.unmodifiableMap(indexes);
        columnIndexes = Collections.unmodifiableMap(map);
        this.columnNames = Collections.unmodifiableList(names);
    }

    @Override
    public Type[] getElementTypes() {
        return new Type[0];
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
     * The width describe how many columns have been defiend by this <code>MetaData</code>
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
