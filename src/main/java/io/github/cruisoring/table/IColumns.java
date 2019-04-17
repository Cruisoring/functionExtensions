package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues;
import io.github.cruisoring.tuple.WithValues2;

import java.lang.reflect.Type;
import java.util.*;

public interface IColumns extends Map<String, Integer> {

    /**
     * Get the Comparator&lt;String&gt; that is used to compare names of this <code>IColumns</code>
     * @return the Comparator&lt;String&gt; of this <code>IColumns</code>
     */
    Comparator<String> getNameComparator();

    /**
     * Get the names for each column in order.
     * @return  List of the names each represent the column at the same position as these names.
     */
    List<String> getColumnNames();

    /**
     * The width describe how many columns have been defined by this {@code Columns}
     * @return
     */
    int width();

    /**
     * Retrieve the immutable Column names to indexes map.
     * @return  Immutable map can be shared safely by multiple TupleRows or TupleTables.
     */
    Map<String, Integer> getColumnIndexes();

    /**
     * Retrieve the indexed column names as a {@code Map} with indexes as keys, and corresponding
     * immutable List of column name+aliases as values.
     * @return  Immutable Map of indexed column names.
     */
    Map<Integer, List<String>> getIndexedColumns();

    @Override
    /**
     * Returns the value to which the specified key is mapped directly or through the nameComparator,
     * or {@code -1} if this map contains no mapping for the key.
     */
    default Integer get(Object key) {
        if(key == null || !(key instanceof String)){
            return -1;
        }

        String keyString = (String)key;
        Set<String> keySet = keySet();
        if(keySet.contains(keyString)){
            return getColumnIndexes().get(keyString);
        }

        Comparator<String> nameComparator = getNameComparator();
        for (String k : keySet) {
            if(nameComparator.compare(k, keyString)==0){
                return getColumnIndexes().get(k);
            }
        }

        return -1;
    }

    /**
     * With concerned columName, get the pair of matched index of this Columns and other as a
     * {@code WithValues2<Integer, Integer>}
     * @param columnName    the columnName recognised by both this and other <tt>IColumns</tt>
     * @param other         the other <tt>IColumns</tt> instance to be mapped.
     * @return      {@code null} if the given <tt>columnName</tt> is not defined in this or other,
     * otherwise a pair of indexes of both parites.
     */
    default WithValues2<Integer, Integer> mapIndexes(String columnName, IColumns other){
        Integer thisIndex = get(columnName);
        if(thisIndex == -1){
            return null;
        } else if (other == this) {
            return Tuple.create(thisIndex, thisIndex);
        }

        Integer otherIndex = other.get(columnName);
        if(otherIndex == -1) {
            return null;
        }
        return Tuple.create(thisIndex, otherIndex);
    }

    /**
     * For each columns of this  {@code IColumns}, get the corresponding indexes of column of the other {@code IColumns}
     * @param other     the other <tt>IColumns</tt> instance to be mapped.
     * @return          {@code WithValues<Integer>} instance holding the mapped indexes in order with concerned {@code IColumns}
     */
    WithValues<Integer> mapIndexes(IColumns other);

    //region Factory methods to create strong-typed TupleRows
    /**
     * Factory mathod to create strong-typed <code>TableRow</code> instance with given values.
     * @param elements  Values to be used to create the strong-typed <code>TableRow</code> instance
     * @return      <code>TupleRow</code> created with given values and this <code>Columns</code>
     */
    default TupleRow rowOf(Object... elements) {
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

    default <T> TupleRow1<T> createRow(T t){
        return new TupleRow1<T>(this, t);
    }

    default <T, U> TupleRow2<T, U> createRow(T t, U u){
        return new TupleRow2<T, U>(this, t, u);
    }

    default <T, U, V> TupleRow3<T, U, V> createRow(T t, U u, V v){
        return new TupleRow3<T, U, V>(this, t, u, v);
    }

    default <T, U, V, W> TupleRow4<T, U, V, W> createRow(T t, U u, V v, W w){
        return new TupleRow4<T, U, V, W>(this, t, u, v, w);
    }

    default <T, U, V, W, X> TupleRow5<T, U, V, W, X> createRow(T t, U u, V v, W w, X x){
        return new TupleRow5<T, U, V, W, X>(this, t, u, v, w, x);
    }

    default <T, U, V, W, X, Y> TupleRow6<T, U, V, W, X, Y> createRow(T t, U u, V v, W w, X x,
                                                                     Y y){
        return new TupleRow6<T, U, V, W, X, Y>(this, t, u, v, w, x, y);
    }

    default <T, U, V, W, X, Y, Z> TupleRow7<T, U, V, W, X, Y, Z> createRow(T t, U u, V v, W w, X x,
                                                                           Y y, Z z){
        return new TupleRow7<T, U, V, W, X, Y, Z>(this, t, u, v, w, x, y, z);
    }

    default <T, U, V, W, X, Y, Z, A> TupleRow8<T, U, V, W, X, Y, Z, A> createRow(T t, U u, V v, W w,
                                                                                 X x, Y y, Z z, A a){
        return new TupleRow8<T, U, V, W, X, Y, Z, A>(this, t, u, v, w, x, y, z, a);
    }

    default <T, U, V, W, X, Y, Z, A, B> TupleRow9<T, U, V, W, X, Y, Z, A, B> createRow(T t, U u, V v, W w,
                                                                                       X x, Y y, Z z, A a, B b){
        return new TupleRow9<T, U, V, W, X, Y, Z, A, B>(this, t, u, v, w, x, y, z, a, b);
    }

    default <T, U, V, W, X, Y, Z, A, B, C> TupleRow10<T, U, V, W, X, Y, Z, A, B, C> createRow(T t, U u, V v, W w, X x, Y y,
                                                                                              Z z, A a, B b, C c){
        return new TupleRow10<T, U, V, W, X, Y, Z, A, B, C>(this, t, u, v, w, x, y, z, a, b, c);
    }

    default <T, U, V, W, X, Y, Z, A, B, C> TupleRowPlus<T, U, V, W, X, Y, Z, A, B, C> createRow(T t, U u, V v, W w, X x, Y y,
                                                                                                Z z, A a, B b, C c, Object... more){
        return new TupleRowPlus<T, U, V, W, X, Y, Z, A, B, C>(this, t, u, v, w, x, y, z, a, b, c, more);
    }
    //endregion

    //region Factory methods to create strong-typed TupleTables
    default <T> TupleTable1<T> createTable1(Class<? extends T> typeT){
        return new TupleTable1<T>(this, typeT);
    }

    default <T, U> TupleTable2<T, U> createTable2(Class<? extends T> typeT, Class<? extends U> typeU){
        return new TupleTable2<T, U>(this, typeT, typeU);
    }

    default <T, U, V> TupleTable3<T, U, V> createTable3(Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV){
        return new TupleTable3<T, U, V>(this, typeT, typeU, typeV);
    }

    default <T, U, V, W> TupleTable4<T, U, V, W> createTable4(Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV,
                                                              Class<? extends W> typeW){
        return new TupleTable4<T, U, V, W>(this, typeT, typeU, typeV, typeW);
    }

    default <T, U, V, W, X> TupleTable5<T, U, V, W, X> createTable5(Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV,
                                                                    Class<? extends W> typeW, Class<? extends X> typeX){
        return new TupleTable5<T, U, V, W, X>(this, typeT, typeU, typeV, typeW, typeX);
    }

    default <T, U, V, W, X, Y> TupleTable6<T, U, V, W, X, Y> createTable6(Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV,
                                                                          Class<? extends W> typeW, Class<? extends X> typeX, Class<? extends Y> typeY){
        return new TupleTable6<T, U, V, W, X, Y>(this, typeT, typeU, typeV, typeW, typeX, typeY);
    }

    default <T, U, V, W, X, Y, Z> TupleTable7<T, U, V, W, X, Y, Z> createTable7(Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV,
                                                                                Class<? extends W> typeW, Class<? extends X> typeX, Class<? extends Y> typeY, Class<? extends Z> typeZ){
        return new TupleTable7<T, U, V, W, X, Y, Z>(this, typeT, typeU, typeV, typeW, typeX, typeY, typeZ);
    }

    default <T, U, V, W, X, Y, Z, A> TupleTable8<T, U, V, W, X, Y, Z, A> createTable8(Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV,
                                                                                      Class<? extends W> typeW, Class<? extends X> typeX, Class<? extends Y> typeY, Class<? extends Z> typeZ,
                                                                                      Class<? extends A> typeA){
        return new TupleTable8<T, U, V, W, X, Y, Z, A>(this, typeT, typeU, typeV, typeW, typeX, typeY, typeZ, typeA);
    }

    default <T, U, V, W, X, Y, Z, A, B> TupleTable9<T, U, V, W, X, Y, Z, A, B> createTable9(Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV,
                                                                                            Class<? extends W> typeW, Class<? extends X> typeX, Class<? extends Y> typeY, Class<? extends Z> typeZ,
                                                                                            Class<? extends A> typeA, Class<? extends B> typeB){
        return new TupleTable9<T, U, V, W, X, Y, Z, A, B>(this, typeT, typeU, typeV, typeW, typeX, typeY, typeZ, typeA, typeB);
    }

    default <T, U, V, W, X, Y, Z, A, B, C> TupleTable10<T, U, V, W, X, Y, Z, A, B, C> createTable10(Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV,
                                                                                                    Class<? extends W> typeW, Class<? extends X> typeX, Class<? extends Y> typeY, Class<? extends Z> typeZ,
                                                                                                    Class<? extends A> typeA, Class<? extends B> typeB, Class<? extends C> typeC){
        return new TupleTable10<T, U, V, W, X, Y, Z, A, B, C>(this, typeT, typeU, typeV, typeW, typeX, typeY, typeZ, typeA, typeB, typeC);
    }

    default <T, U, V, W, X, Y, Z, A, B, C> TupleTablePlus<T, U, V, W, X, Y, Z, A, B, C> createTablePlus(Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV,
                                                                                                        Class<? extends W> typeW, Class<? extends X> typeX, Class<? extends Y> typeY, Class<? extends Z> typeZ,
                                                                                                        Class<? extends A> typeA, Class<? extends B> typeB, Class<? extends C> typeC, Type... moreTypes){
        return new TupleTablePlus<T, U, V, W, X, Y, Z, A, B, C>(this, typeT, typeU, typeV, typeW, typeX, typeY, typeZ, typeA, typeB, typeC, moreTypes);
    }
    //endregion
}
