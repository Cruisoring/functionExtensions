package io.github.cruisoring.table;

import io.github.cruisoring.tuple.*;

import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Supplier;

import static io.github.cruisoring.Asserts.checkStates;
import static io.github.cruisoring.Asserts.checkWithoutNull;

/**
 * Interface to specify how Columns of a table shall function which can map Column names to indexes.
 */
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
     * @return number of the columns defined by this IColumns instance.
     */
    int width();

    /**
     * Retrieve the immutable Column names to indexes map.
     * @return  Immutable map can be shared safely by multiple TupleRows or TupleTables.
     */
    Map<String, Integer> getColumnIndexes();

    /**
     * Retrieve the indexed column names as a String[][] with column name+aliases as values.
     * @return  Copy of the column names.
     */
    String[][] getIndexedColumns();

    @Override
    /*
      Returns the value to which the specified key is mapped directly or through the nameComparator,
      or {@code -1} if this map contains no mapping for the key.
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
        Integer thisIndex = get(checkWithoutNull(columnName));
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
    default TupleRow createRow(Object... elements) {
        checkStates(elements != null);

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

    /**
     * Wrap a strong-typed {@code WithValues} instance such as Tuple to a row whose cells can be referred by names
     * @param tuple the container to hold the actual data of a single row with orders specified by this {@code IColumns}
     * @return a light-weight {@code TupleRow} object that hold all its cell values as {@code WithValues}
     * such as an immutable {@code Tuple} and with shared {@code IColumns} to access them by names.
     */
    default TupleRow asRow(WithValues tuple){
        checkWithoutNull(tuple);

        int length = tuple.getLength();
        if(length == 0){
            throw new UnsupportedOperationException("No columns defined");
        }
        switch (length){
            case 1: return asRow1((WithValues1)tuple);
            case 2: return asRow2((WithValues2)tuple);
            case 3: return asRow3((WithValues3)tuple);
            case 4: return asRow4((WithValues4)tuple);
            case 5: return asRow5((WithValues5)tuple);
            case 6: return asRow6((WithValues6)tuple);
            case 7: return asRow7((WithValues7)tuple);
            case 8: return asRow8((WithValues8)tuple);
            case 9: return asRow9((WithValues9)tuple);
            case 10: return asRow10((WithValues10)tuple);
            default: return asRowPlus((WithValuesPlus)tuple);
        }
    }

    //region Factory methods to create strong-typed TupleRow instance with both width and value types specified
    default <T> TupleRow1<T> asRow1(WithValues1<T> tuple){
        return new TupleRow1<>(this, tuple);
    }

    default <T, U> TupleRow2<T, U> asRow2(WithValues2<T,U> tuple){
        return new TupleRow2<>(this, tuple);
    }

    default <T, U, V> TupleRow3<T, U, V> asRow3(WithValues3<T,U,V> tuple){
        return new TupleRow3<>(this, tuple);
    }

    default <T, U, V, W> TupleRow4<T, U, V, W> asRow4(WithValues4<T,U,V,W> tuple){
        return new TupleRow4<>(this, tuple);
    }

    default <T, U, V, W, X> TupleRow5<T, U, V, W, X> asRow5(WithValues5<T,U,V,W,X> tuple){
        return new TupleRow5<>(this, tuple);
    }

    default <T, U, V, W, X, Y> TupleRow6<T, U, V, W, X, Y> asRow6(
            WithValues6<T, U, V, W, X, Y> tuple){
        return new TupleRow6<>(this, tuple);
    }

    default <T, U, V, W, X, Y, Z> TupleRow7<T, U, V, W, X, Y, Z> asRow7(
            WithValues7<T, U, V, W, X, Y, Z> tuple){
        return new TupleRow7<>(this, tuple);
    }

    default <T, U, V, W, X, Y, Z, A> TupleRow8<T, U, V, W, X, Y, Z, A> asRow8(
            WithValues8<T, U, V, W, X, Y, Z, A> tuple){
        return new TupleRow8<>(this, tuple);
    }

    default <T, U, V, W, X, Y, Z, A, B> TupleRow9<T, U, V, W, X, Y, Z, A, B> asRow9(
            WithValues9<T, U, V, W, X, Y, Z, A, B> tuple){
        return new TupleRow9<>(this, tuple);
    }

    default <T, U, V, W, X, Y, Z, A, B, C> TupleRow10<T, U, V, W, X, Y, Z, A, B, C> asRow10(
            WithValues10<T, U, V, W, X, Y, Z, A, B, C> tuple){
        return new TupleRow10<>(this, tuple);
    }

    default <T, U, V, W, X, Y, Z, A, B, C> TupleRowPlus<T, U, V, W, X, Y, Z, A, B, C> asRowPlus(
            WithValuesPlus<T, U, V, W, X, Y, Z, A, B, C> tuple){
        return new TupleRowPlus<>(this, tuple);
    }
    //endregion
    //endregion

    //region Factory methods to create strong-typed TupleTables

    /**
     * Construct a {@code TupleTable} instance with both row values supplier and types of the columns in order
     * @param rowsSupplier  the supplier of the row data as a list of {@code WithValues}
     * @param types     types of the columns in order, for example: {@code types[0]} specifies the value types of the first column
     * @return a {@code TupleTable} instance with both values and value types fixed
     */
    default TupleTable createTable(Supplier<List<WithValues>> rowsSupplier, Class... types){
        checkWithoutNull(types);

        int length = types.length;
        switch (length) {
            case 0:
                return new TupleTable(rowsSupplier, this);
            case 1:
                return new TupleTable1(rowsSupplier, this, types[0]);
            case 2:
                return new TupleTable2(rowsSupplier, this, types[0], types[1]);
            case 3:
                return new TupleTable3(rowsSupplier, this, types[0], types[1], types[2]);
            case 4:
                return new TupleTable4(rowsSupplier, this, types[0], types[1], types[2],
                        types[3]);
            case 5:
                return new TupleTable5(rowsSupplier, this, types[0], types[1], types[2],
                        types[3], types[4]);
            case 6:
                return new TupleTable6(rowsSupplier, this, types[0], types[1], types[2],
                        types[3], types[4], types[5]);
            case 7:
                return new TupleTable7(rowsSupplier, this, types[0], types[1], types[2],
                        types[3], types[4], types[5], types[6]);
            case 8:
                return new TupleTable8(rowsSupplier, this, types[0], types[1], types[2],
                        types[3], types[4], types[5], types[6], types[7]);
            case 9:
                return new TupleTable9(rowsSupplier, this, types[0], types[1], types[2],
                        types[3], types[4], types[5], types[6], types[7], types[8]);
            case 10:
                return new TupleTable10(rowsSupplier, this, types[0], types[1], types[2],
                        types[3], types[4], types[5], types[6], types[7], types[8], types[9]);

            default:
                return new TupleTablePlus(rowsSupplier, this, types[0], types[1], types[2],
                        types[3], types[4], types[5], types[6], types[7], types[8], types[9],
                        Arrays.copyOfRange(types, 10, length));
        }
    }

    //region Factory methods to create strong-typed TupleTabale instance with both width and value types of columns fixed
    default <T> TupleTable1<T> createTable1(Class<? extends T> typeT){
        return new TupleTable1<>(null, this, typeT);
    }

    default <T, U> TupleTable2<T, U> createTable2(Class<? extends T> typeT, Class<? extends U> typeU){
        return new TupleTable2<>(null, this, typeT, typeU);
    }

    default <T, U, V> TupleTable3<T, U, V> createTable3(Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV){
        return new TupleTable3<>(null, this, typeT, typeU, typeV);
    }

    default <T, U, V, W> TupleTable4<T, U, V, W> createTable4(Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV,
                                                              Class<? extends W> typeW){
        return new TupleTable4<>(null, this, typeT, typeU, typeV, typeW);
    }

    default <T, U, V, W, X> TupleTable5<T, U, V, W, X> createTable5(Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV,
                                                                    Class<? extends W> typeW, Class<? extends X> typeX){
        return new TupleTable5<>(null, this, typeT, typeU, typeV, typeW, typeX);
    }

    default <T, U, V, W, X, Y> TupleTable6<T, U, V, W, X, Y> createTable6(Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV,
                                                                          Class<? extends W> typeW, Class<? extends X> typeX, Class<? extends Y> typeY){
        return new TupleTable6<>(null, this, typeT, typeU, typeV, typeW, typeX, typeY);
    }

    default <T, U, V, W, X, Y, Z> TupleTable7<T, U, V, W, X, Y, Z> createTable7(Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV,
                                                                                Class<? extends W> typeW, Class<? extends X> typeX, Class<? extends Y> typeY, Class<? extends Z> typeZ){
        return new TupleTable7<>(null, this, typeT, typeU, typeV, typeW, typeX, typeY, typeZ);
    }

    default <T, U, V, W, X, Y, Z, A> TupleTable8<T, U, V, W, X, Y, Z, A> createTable8(Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV,
                                                                                      Class<? extends W> typeW, Class<? extends X> typeX, Class<? extends Y> typeY, Class<? extends Z> typeZ,
                                                                                      Class<? extends A> typeA){
        return new TupleTable8<>(null, this, typeT, typeU, typeV, typeW, typeX, typeY, typeZ, typeA);
    }

    default <T, U, V, W, X, Y, Z, A, B> TupleTable9<T, U, V, W, X, Y, Z, A, B> createTable9(Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV,
                                                                                            Class<? extends W> typeW, Class<? extends X> typeX, Class<? extends Y> typeY, Class<? extends Z> typeZ,
                                                                                            Class<? extends A> typeA, Class<? extends B> typeB){
        return new TupleTable9<>(null, this, typeT, typeU, typeV, typeW, typeX, typeY, typeZ, typeA, typeB);
    }

    default <T, U, V, W, X, Y, Z, A, B, C> TupleTable10<T, U, V, W, X, Y, Z, A, B, C> createTable10(Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV,
                                                                                                    Class<? extends W> typeW, Class<? extends X> typeX, Class<? extends Y> typeY, Class<? extends Z> typeZ,
                                                                                                    Class<? extends A> typeA, Class<? extends B> typeB, Class<? extends C> typeC){
        return new TupleTable10<>(null, this, typeT, typeU, typeV, typeW, typeX, typeY, typeZ, typeA, typeB, typeC);
    }

    default <T, U, V, W, X, Y, Z, A, B, C> TupleTablePlus<T, U, V, W, X, Y, Z, A, B, C> createTablePlus(Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV,
                                                                                                        Class<? extends W> typeW, Class<? extends X> typeX, Class<? extends Y> typeY, Class<? extends Z> typeZ,
                                                                                                        Class<? extends A> typeA, Class<? extends B> typeB, Class<? extends C> typeC, Type... moreTypes){
        return new TupleTablePlus<>(null, this, typeT, typeU, typeV, typeW, typeX, typeY, typeZ, typeA, typeB, typeC, moreTypes);
    }
    //endregion
    //endregion
}
