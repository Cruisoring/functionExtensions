package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface ITableColumns extends Map<String, Integer> {
    List<String> getColumnNames();

    int width();

    Map<String, Integer> getColumnIndexes();

    /**
     * Factory mathod to create strong-typed <code>TableRow</code> instance with given values.
     * @param elements  Values to be used to create the strong-typed <code>TableRow</code> instance
     * @return      <code>TupleRow</code> created with given values and this <code>TableColumns</code>
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
        return new TupleRow1(this, t);
    }

    default <T, U> TupleRow2<T, U> createRow(T t, U u){
        return new TupleRow2(this, t, u);
    }

    default <T, U, V> TupleRow3<T, U, V> createRow(T t, U u, V v){
        return new TupleRow3(this, t, u, v);
    }

    default <T, U, V, W> TupleRow4<T, U, V, W> createRow(T t, U u, V v, W w){
        return new TupleRow4(this, t, u, v, w);
    }

    default <T, U, V, W, X> TupleRow5<T, U, V, W, X> createRow(T t, U u, V v, W w, X x){
        return new TupleRow5(this, t, u, v, w, x);
    }

    default <T, U, V, W, X, Y> TupleRow6<T, U, V, W, X, Y> createRow(T t, U u, V v, W w, X x,
                                                                     Y y){
        return new TupleRow6(this, t, u, v, w, x, y);
    }

    default <T, U, V, W, X, Y, Z> TupleRow7<T, U, V, W, X, Y, Z> createRow(T t, U u, V v, W w, X x,
                                                                           Y y, Z z){
        return new TupleRow7(this, t, u, v, w, x, y, z);
    }

    default <T, U, V, W, X, Y, Z, A> TupleRow8<T, U, V, W, X, Y, Z, A> createRow(T t, U u, V v, W w,
                                                                                 X x, Y y, Z z, A a){
        return new TupleRow8(this, t, u, v, w, x, y, z, a);
    }

    default <T, U, V, W, X, Y, Z, A, B> TupleRow9<T, U, V, W, X, Y, Z, A, B> createRow(T t, U u, V v, W w,
                                                                                       X x, Y y, Z z, A a, B b){
        return new TupleRow9(this, t, u, v, w, x, y, z, a, b);
    }

    default <T, U, V, W, X, Y, Z, A, B, C> TupleRow10<T, U, V, W, X, Y, Z, A, B, C> createRow(T t, U u, V v, W w, X x, Y y,
                                                                                              Z z, A a, B b, C c){
        return new TupleRow10(this, t, u, v, w, x, y, z, a, b, c);
    }

    default <T, U, V, W, X, Y, Z, A, B, C> TupleRowPlus<T, U, V, W, X, Y, Z, A, B, C> createRow(T t, U u, V v, W w, X x, Y y,
                                                                                                Z z, A a, B b, C c, Object... more){
        return new TupleRowPlus(this, t, u, v, w, x, y, z, a, b, c, more);
    }

    default <T> TupleTable1<T> createTable1(){
        return new TupleTable1(this);
    }

    default <T, U> TupleTable2<T, U> createTable2(){
        return new TupleTable2(this);
    }

    default <T, U, V> TupleTable3<T, U, V> createTable3(){
        return new TupleTable3(this);
    }

    default <T, U, V, W> TupleTable4<T, U, V, W> createTable4(){
        return new TupleTable4(this);
    }

    default <T, U, V, W, X> TupleTable5<T, U, V, W, X> createTable5(){
        return new TupleTable5(this);
    }

    default <T, U, V, W, X, Y> TupleTable6<T, U, V, W, X, Y> createTable6(){
        return new TupleTable6(this);
    }

    default <T, U, V, W, X, Y, Z> TupleTable7<T, U, V, W, X, Y, Z> createTable7(){
        return new TupleTable7(this);
    }

    default <T, U, V, W, X, Y, Z, A> TupleTable8<T, U, V, W, X, Y, Z, A> createTable8(){
        return new TupleTable8(this);
    }

    default <T, U, V, W, X, Y, Z, A, B> TupleTable9<T, U, V, W, X, Y, Z, A, B> createTable9(){
        return new TupleTable9(this);
    }

    default <T, U, V, W, X, Y, Z, A, B, C> TupleTable10<T, U, V, W, X, Y, Z, A, B, C> createTable10(){
        return new TupleTable10(this);
    }

    default <T, U, V, W, X, Y, Z, A, B, C> TupleTablePlus<T, U, V, W, X, Y, Z, A, B, C> createTablePlus(){
        return new TupleTablePlus(this);
    }
}
