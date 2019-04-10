package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import org.omg.CORBA.INTERNAL;

import java.util.*;

/**
 * Keep column names and their indexes as a map, aliases could be defined in the given Map to create
 * <code>TableColumns</code> with multiple String keys pointing to the same index.
 * Combined with <code>TupleRow</code> or underlying <code>Tuple</code> lists, it might be possible to define views on the same row values.
 */
public class TableColumns implements Map<String, Integer> {

    final Map<String, Integer> columnIndexes;
    final List<String> columnNames;

    /**
     * Construct the TableColumns with column names directly.
     * @param columnNames   Names of the columns that cannot be null or duplicated.
     */
    public TableColumns(String... columnNames) {
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
        }
        columnIndexes = Collections.unmodifiableMap(map);
        this.columnNames = Collections.unmodifiableList(names);
    }

    /**
     * Construct the TableColumns with pre-build indexes with no missing ones, but could have aliases to make one column have multiple names.
     * @param indexes   Pre-defined column names to position map.
     */
    public TableColumns(Map<String, Integer> indexes) {
        Objects.requireNonNull(indexes);

        List<Map.Entry<String, Integer>> entries = new ArrayList<>(indexes.entrySet());
        Collections.sort(entries, Comparator.comparing(entry -> entry.getValue()));
        int next = 0;
        List<String> names = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<String, Integer> entry = entries.get(i);
            int index = entry.getValue();
            if(index == next){
                next++;
                names.add(entry.getKey());
            } else if(index > next) {
                throw new UnsupportedOperationException("Missing column name definition for index of " + next);
            }
        }

        Map<String, Integer> map = new LinkedHashMap<>();
        //Keep the order by index
        entries.forEach(entry -> map.put(entry.getKey(), entry.getValue()));

        columnIndexes = Collections.unmodifiableMap(map);
        this.columnNames = Collections.unmodifiableList(names);
    }

    /**
     * Get the names for each column in order.
     * @return  List of the names each represent the column at the same position as these names.
     */
    public List<String> getColumnNames(){
        return columnNames;
    }

    /**
     * The width describe how many columns have been defiend by this <code>TableColumns</code>
     * @return
     */
    public int width(){
        return columnNames.size();
    }

    /**
     * Retrieve the immutable Column names to indexes map.
     * @return  Immutable map can be shared safely by multiple TupleRows or TupleTables.
     */
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

    /**
     * Factory mathod to create strong-typed <code>TableRow</code> instance with given values.
     * @param elements  Values to be used to create the strong-typed <code>TableRow</code> instance
     * @return      <code>TupleRow</code> created with given values and this <code>TableColumns</code>
     */
    public TupleRow rowOf(final Object... elements) {
        if (elements == null) {
            return new TupleRow(this, null);
        }
        int length = elements.length;
        switch (length) {
            case 0:
                return new TupleRow(this, Tuple.UNIT);
            case 1:
                return new TupleRow1(this, elements[0]);
            case 2:
                return new TupleRow2(this, elements[0], elements[1]);
            case 3:
                return new TupleRow3(this, elements[0], elements[1], elements[2]);
            case 4:
                return new TupleRow4(this, elements[0], elements[1], elements[2], elements[3]);
            case 5:
                return new TupleRow5(this, elements[0], elements[1], elements[2], elements[3], elements[4]);
            case 6:
                return new TupleRow6(this, elements[0], elements[1], elements[2], elements[3], elements[4],
                        elements[5]);
            case 7:
                return new TupleRow7(this, elements[0], elements[1], elements[2], elements[3], elements[4],
                        elements[5], elements[6]);
            case 8:
                return new TupleRow8(this, elements[0], elements[1], elements[2], elements[3], elements[4],
                        elements[5], elements[6], elements[7]);
            case 9:
                return new TupleRow9(this, elements[0], elements[1], elements[2], elements[3], elements[4],
                        elements[5], elements[6], elements[7], elements[8]);
            case 10:
                return new TupleRow10(this, elements[0], elements[1], elements[2], elements[3], elements[4],
                        elements[5], elements[6], elements[7], elements[8], elements[9]);

            default:
                return new TupleRowPlus(this, elements[0], elements[1], elements[2], elements[3], elements[4],
                        elements[5], elements[6], elements[7], elements[8], elements[9],
                        Arrays.copyOfRange(elements, 10, length));
        }
    }

    public <T> TupleRow1<T> createRow(final T t){
        return new TupleRow1(this, t);
    }

    public <T, U> TupleRow2<T, U> createRow(final T t, final U u){
        return new TupleRow2(this, t, u);
    }

    public <T, U, V> TupleRow3<T, U, V> createRow(final T t, final U u, final V v){
        return new TupleRow3(this, t, u, v);
    }

    public <T, U, V, W> TupleRow4<T, U, V, W> createRow(final T t, final U u, final V v, final W w){
        return new TupleRow4(this, t, u, v, w);
    }

    public <T, U, V, W, X> TupleRow5<T, U, V, W, X> createRow(final T t, final U u, final V v, final W w, final X x){
        return new TupleRow5(this, t, u, v, w, x);
    }

    public <T, U, V, W, X, Y> TupleRow6<T, U, V, W, X, Y> createRow(final T t, final U u, final V v, final W w, final X x,
                                                                    final Y y){
        return new TupleRow6(this, t, u, v, w, x, y);
    }

    public <T, U, V, W, X, Y, Z> TupleRow7<T, U, V, W, X, Y, Z> createRow(final T t, final U u, final V v, final W w, final X x,
                                                                          final Y y, final Z z){
        return new TupleRow7(this, t, u, v, w, x, y, z);
    }

    public <T, U, V, W, X, Y, Z, A> TupleRow8<T, U, V, W, X, Y, Z, A> createRow(final T t, final U u, final V v, final W w,
                                                                                final X x, final Y y, final Z z, final A a){
        return new TupleRow8(this, t, u, v, w, x, y, z, a);
    }

    public <T, U, V, W, X, Y, Z, A, B> TupleRow9<T, U, V, W, X, Y, Z, A, B> createRow(final T t, final U u, final V v, final W w,
                                                                                      final X x, final Y y, final Z z, final A a, final B b){
        return new TupleRow9(this, t, u, v, w, x, y, z, a, b);
    }

    public <T, U, V, W, X, Y, Z, A, B, C> TupleRow10<T, U, V, W, X, Y, Z, A, B, C> createRow(final T t, final U u, final V v, final W w, final X x, final Y y,
                                                                                             final Z z, final A a, final B b, final C c){
        return new TupleRow10(this, t, u, v, w, x, y, z, a, b, c);
    }

    public <T, U, V, W, X, Y, Z, A, B, C> TupleRowPlus<T, U, V, W, X, Y, Z, A, B, C> createRow(final T t, final U u, final V v, final W w, final X x, final Y y,
                                                                                               final Z z, final A a, final B b, final C c, Object... more){
        return new TupleRowPlus(this, t, u, v, w, x, y, z, a, b, c, more);
    }

    public <T> TupleTable1<T> createTable1(){
        return new TupleTable1(this);
    }

    public <T, U> TupleTable2<T, U> createTable2(){
        return new TupleTable2(this);
    }

    public <T, U, V> TupleTable3<T, U, V> createTable3(){
        return new TupleTable3(this);
    }

    public <T, U, V, W> TupleTable4<T, U, V, W> createTable4(){
        return new TupleTable4(this);
    }

    public <T, U, V, W, X> TupleTable5<T, U, V, W, X> createTable5(){
        return new TupleTable5(this);
    }

    public <T, U, V, W, X, Y> TupleTable6<T, U, V, W, X, Y> createTable6(){
        return new TupleTable6(this);
    }

    public <T, U, V, W, X, Y, Z> TupleTable7<T, U, V, W, X, Y, Z> createTable7(){
        return new TupleTable7(this);
    }

    public <T, U, V, W, X, Y, Z, A> TupleTable8<T, U, V, W, X, Y, Z, A> createTable8(){
        return new TupleTable8(this);
    }

    public <T, U, V, W, X, Y, Z, A, B> TupleTable9<T, U, V, W, X, Y, Z, A, B> createTable9(){
        return new TupleTable9(this);
    }

    public <T, U, V, W, X, Y, Z, A, B, C> TupleTable10<T, U, V, W, X, Y, Z, A, B, C> createTable10(){
        return new TupleTable10(this);
    }

    public <T, U, V, W, X, Y, Z, A, B, C> TupleTablePlus<T, U, V, W, X, Y, Z, A, B, C> createTablePlus(){
        return new TupleTablePlus(this);
    }
}
