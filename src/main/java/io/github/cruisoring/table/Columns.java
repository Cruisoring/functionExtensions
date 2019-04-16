package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues;
import io.github.cruisoring.tuple.WithValues1;
import io.github.cruisoring.tuple.WithValues2;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Keep column names and their indexes as a map, aliases could be defined in the given Map to create
 * <code>Columns</code> with multiple String keys pointing to the same index.
 * Combined with <code>TupleRow</code> or underlying <code>Tuple</code> lists, it might be possible to define views on the same row values.
 */
public class Columns implements IColumns {
    public static final Map<WithValues2<IColumns, IColumns>, WithValues<Integer>> cachedMappings = new HashMap<>();

    public final static Comparator<String> NATURAL = String::compareTo;

    public static Comparator<String> DefaultNameComparator = String.CASE_INSENSITIVE_ORDER;

    private final static String _defaultEscapedPattern = "\\s|-|_";
    public final static Comparator<String> ESCAPED = (s1, s2) -> {
        String escaped1 = s1.replaceAll(_defaultEscapedPattern, "");
        String escaped2 = s2.replaceAll(_defaultEscapedPattern, "");
        return escaped1.compareTo(escaped2);
    };

    public final static Comparator<String> ESCAPED_CASE_INSENSITIVE = (s1, s2) -> {
        String escaped1 = s1.replaceAll(_defaultEscapedPattern, "");
        String escaped2 = s2.replaceAll(_defaultEscapedPattern, "");
        return escaped1.compareToIgnoreCase(escaped2);
    };

    public static Comparator<String> getEscapedComparator(String escapePattern) {
        Objects.requireNonNull(escapePattern);
        return (s1, s2) -> {
            String escaped1 = s1.replaceAll(escapePattern, "");
            String escaped2 = s2.replaceAll(escapePattern, "");
            return escaped1.compareTo(escaped2);
        };
    }

    public static Comparator<String> getEscapedInsensitiveComparator(String escapePattern) {
        Objects.requireNonNull(escapePattern);
        return (s1, s2) -> {
            String escaped1 = s1.replaceAll(escapePattern, "");
            String escaped2 = s2.replaceAll(escapePattern, "");
            return escaped1.compareToIgnoreCase(escaped2);
        };
    }

    final Comparator<String> nameComparator;
    final Map<Integer, List<String>> indexedColumns;
    final Map<String, Integer> columnIndexes;
    final List<String> columnNames;

    /**
     * Construct the Columns with column names directly.
     * @param columnNames   Names of the columns that cannot be null or duplicated.
     */
    public Columns(String... columnNames) {
        //Use String::compareTo() by default
        nameComparator = NATURAL;
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
     * Construct the Columns with ordered column names and Comparator&lt;String&gt; for come comparing.
     * @param columnDefintions  Column names that must be defined consecutively, the first of the String[] is the main
     *                         key, and others are aliases if defined.
     * @param nameComparator    Comparator&lt;String&gt; used to compare column names.
     */
    public Columns(Map<Integer, WithValues1<String[]>> columnDefintions, Comparator<String> nameComparator){
        Objects.requireNonNull(columnDefintions);

        this.nameComparator = nameComparator==null ? DefaultNameComparator : nameComparator;
        int width = columnDefintions.size();
        if(width < 1){
            throw new UnsupportedOperationException("No columns defined");
        }

        Map<Integer, List<String>> indexes = new HashMap<>();
        Map<String, Integer> map = new LinkedHashMap<>();
        List<String> names = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            if(!columnDefintions.containsKey(i)){
                throw new UnsupportedOperationException("Missing definition of column "+i);
            }
            WithValues1<String[]> defintion = columnDefintions.get(i);
            String[] alias = defintion.getFirst();
            int aliasLength = alias.length;
            if(aliasLength==0 || Arrays.stream(alias).anyMatch(name -> name == null)){
                throw new UnsupportedOperationException("Unsupported definitons of column " + i);
            }
            indexes.put(i, Arrays.asList(alias));
            for (int j = 0; j < aliasLength; j++) {
                if(map.containsKey(alias[j])){
                    throw new UnsupportedOperationException("Duplicated key: "+alias[j]);
                }
                map.put(alias[j], i);
            }
            names.add(alias[0]);
        }
        indexedColumns = Collections.unmodifiableMap(indexes);
        columnIndexes = Collections.unmodifiableMap(map);
        this.columnNames = Collections.unmodifiableList(names);
    }

    /**
     * Construct the Columns with ordered column names only.
     * @param columnDefintions  Column names that must be defined consecutively, the first of the String[] is the main
     *                         key, and others are aliases if defined.
     */
    public Columns(Map<Integer, WithValues1<String[]>> columnDefintions){
        this(columnDefintions, null);
    }

    @Override
    public Comparator<String> getNameComparator() {
        return nameComparator;
    }

    @Override
    public List<String> getColumnNames(){
        return columnNames;
    }

    @Override
    public int width(){
        return columnNames.size();
    }

    @Override
    public Map<String, Integer> getColumnIndexes(){
        return columnIndexes;
    }

    @Override
    public Map<Integer, List<String>> getIndexedColumns() {
        return indexedColumns;
    }

    @Override
    public WithValues<Integer> mapIndexes(IColumns other){
        Objects.requireNonNull(other);

        if(this == other){
            Integer[] indexes = IntStream.range(0, width()).boxed().toArray(size -> new Integer[size]);
            return Tuple.of(indexes);
        }

        WithValues2<IColumns, IColumns> key = Tuple.create(this, other);
        WithValues<Integer> mappings=null;
        if(!cachedMappings.containsKey(key)){
            List<Integer> indexes = new ArrayList<>();
            Map<Integer, List<String>> thisIndexedColumns = getIndexedColumns();
            int width = thisIndexedColumns.size();
            WithValues2<Integer, Integer> indexPair;
            for (int i = 0; i < width; i++) {
                List<String> alias = thisIndexedColumns.get(i);
                for (int j = 0; j < alias.size(); j++) {
                    indexPair = mapIndexes(alias.get(j), other);
                    if(indexPair != null) {
                        indexes.add(indexPair.getSecond());
                        break;
                    }
                }
                if(indexes.size() < i){
                    indexes.add(null);
                }
            }
            mappings =  Tuple.setOf(indexes.toArray(new Integer[0]));
            cachedMappings.put(key, mappings);
        } else {
            mappings = cachedMappings.get(key);
        }
        return mappings;
    }

    //region Implementation of Map<String, Integer>
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
    //endregion

}
