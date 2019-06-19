package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues;
import io.github.cruisoring.tuple.WithValues2;
import io.github.cruisoring.utility.ArrayHelper;

import java.util.*;
import java.util.stream.IntStream;

import static io.github.cruisoring.Asserts.assertAllNotNull;

/**
 * Keep column names and their indexes as a map, aliases could be defined in the given Map to create
 * <code>Columns</code> with multiple String keys pointing to the same index.
 * Combined with <code>TupleRow</code> or underlying <code>Tuple</code> lists, it might be possible to define views on the same row values.
 */
public class Columns implements IColumns {
    //region Static members and methods
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

    /**
     * Construct the Columns with column names directly.
     * @param columnNames   Names of the columns that cannot be null or duplicated.
     */
    public Columns(String... columnNames) {
        assertAllNotNull(columnNames);

        //Use String::compareTo() by default
        nameComparator = String::compareTo;
        Map<Integer, List<String>> indexes = new HashMap<>();
        Map<String, Integer> map = new LinkedHashMap<>();
        List<String> names = new ArrayList<>();
        int len = columnNames.length;
        indexedColumns = new String[len][];
        for (int i = 0; i < columnNames.length; i++) {
            String columnName = columnNames[i];
            if (map.containsKey(columnName)) {
                throw new UnsupportedOperationException("Column name at index of " + i +
                        " is duplicated with the one at index of " + map.get(columnName));
            }
            map.put(columnName, i);
            names.add(columnName);
            indexes.put(i, Arrays.asList(columnName));
            indexedColumns[i] = new String[]{columnName};
        }
        columnIndexes = Collections.unmodifiableMap(map);
        this.columnNames = Collections.unmodifiableList(names);
    }

    public static Comparator<String> getEscapedComparator(String escapePattern) {
        assertAllNotNull(escapePattern);
        return (s1, s2) -> {
            String escaped1 = s1.replaceAll(escapePattern, "");
            String escaped2 = s2.replaceAll(escapePattern, "");
            return escaped1.compareTo(escaped2);
        };
    }
    //endregion

    //region Instance variables
    final Comparator<String> nameComparator;
    final String[][] indexedColumns;
    final Map<String, Integer> columnIndexes;
    final List<String> columnNames;
    //endregion

    //region Constructors

    public static Comparator<String> getEscapedInsensitiveComparator(String escapePattern) {
        assertAllNotNull(escapePattern);
        return (s1, s2) -> {
            String escaped1 = s1.replaceAll(escapePattern, "");
            String escaped2 = s2.replaceAll(escapePattern, "");
            return escaped1.compareToIgnoreCase(escaped2);
        };
    }

    /**
     * Construct the Columns with ordered column names and Comparator&lt;String&gt; for come comparing.
     * @param columnDefintions  Column names that must be defined consecutively, the first of the String[] is the main
     *                         key, and others are aliases if defined.
     * @param nameComparator    Comparator&lt;String&gt; used to compare column names.
     */
    public Columns(String[][] columnDefintions, Comparator<String> nameComparator){
        assertAllNotNull(columnDefintions);

        this.nameComparator = nameComparator==null ? DefaultNameComparator : nameComparator;
        int width = columnDefintions.length;
        if(width < 1){
            throw new UnsupportedOperationException("No columns defined");
        }

        indexedColumns = columnDefintions;
        Map<String, Integer> map = new LinkedHashMap<>();
        List<String> names = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            String[] columnDefinition = columnDefintions[i];
            int aliasLength = columnDefinition.length;
            if(aliasLength==0 || Arrays.stream(columnDefinition).anyMatch(name -> name == null)){
                throw new UnsupportedOperationException("Unsupported definitons of column " + i);
            }
            for (int j = 0; j < aliasLength; j++) {
                if(map.containsKey(columnDefinition[j])){
                    throw new UnsupportedOperationException("Duplicated key: "+columnDefinition[j]);
                }
                map.put(columnDefinition[j], i);
            }
            names.add(columnDefinition[0]);
        }
        columnIndexes = Collections.unmodifiableMap(map);
        this.columnNames = Collections.unmodifiableList(names);
    }

    /**
     * Construct the Columns with ordered column names only.
     * @param columnDefintions  Column names that must be defined consecutively, the first of the String[] is the main
     *                         key, and others are aliases if defined.
     */
    public Columns(String[][] columnDefintions){
        this(columnDefintions, null);
    }
    //endregion

    //region Instance methods
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
    public String[][] getIndexedColumns() {
        return (String[][]) ArrayHelper.create(String[].class, indexedColumns.length, i -> indexedColumns[i].clone());
    }

    @Override
    public WithValues<Integer> mapIndexes(IColumns other){
        assertAllNotNull(other);

        if(this == other){
            Integer[] indexes = IntStream.range(0, width()).boxed().toArray(size -> new Integer[size]);
            return Tuple.setOfType(Integer.class, indexes);
        }

        WithValues2<IColumns, IColumns> key = Tuple.create(this, other);
        WithValues<Integer> mappings=null;
        if(!cachedMappings.containsKey(key)){
            List<Integer> indexes = new ArrayList<>();
            String[][] thisIndexedColumns = getIndexedColumns();
            int width = thisIndexedColumns.length;
            WithValues2<Integer, Integer> indexPair;
            for (int i = 0; i < width; i++) {
                String[] alias = thisIndexedColumns[i];
                for (int j = 0; j <= alias.length; j++) {
                    if(j == alias.length){
                        indexes.add(null);
                        break;
                    }
                    indexPair = mapIndexes(alias[j], other);
                    if(indexPair != null) {
                        indexes.add(indexPair.getSecond());
                        break;
                    }
                }
            }
            mappings =  Tuple.setOf(indexes.toArray(new Integer[0]));
            cachedMappings.put(key, mappings);
        } else {
            mappings = cachedMappings.get(key);
        }
        return mappings;
    }
    //endregion

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
