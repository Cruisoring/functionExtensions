package com.easyworks.utility;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class TupleTest {
    class A<T> {
        T value;
        public A(T t){
            value = t;
        }

        public T getValue(){
            return value;
        }

        @Override
        public String toString(){
            return String.format("A(%s)", value==null?"null":value.toString());
        }
    }

    enum Progress {
        Backlog, Study, Working, ReadyForQA, Done;

        private static Progress[] _values = values();
        public int toInt(){
            return ordinal();
        }
        public static Progress ofIndex(int index){
            if(index<0 || index>= _values.length) throw new IndexOutOfBoundsException();
            return _values[index];
        }
        public Progress getNext(){
            return _values[(ordinal()+1)% _values.length];
        }
    }

    Tuple tuple0 = Tuple.create();
    Tuple tuple1 = Tuple.create("First");
    Tuple set1 = Tuple.setOf(true);
    Tuple tuple2 = Tuple.create("Second", "2");
    Tuple.Set<Boolean> boolSet2 = Tuple.setOf(true, false);
    Tuple tuple3 = Tuple.create(3, "Third", 3.33d);
    Tuple tuple4 = Tuple.create(true, null, new A("Aaa"), new A("B").getClass());
    Tuple tuple5 = Tuple.create(Progress.Working, "5", null, 5f, false);
    Tuple tuple6 = Tuple.create(6, Integer.valueOf(6), 6f, 6.0d, "six",  Progress.ofIndex(3));
    Tuple tuple7 = Tuple.create("Seven", new String[]{"arrayValue1", "arrayValue2"}, Character.valueOf('7'), '7', Integer.valueOf(7),
            new Character[]{'C'}, new char[]{'a','b'});

    Tuple.Set<String> stringSet = Tuple.setOf("", null, "third", "3");
    Tuple.Set<Integer> intSet = Tuple.setOf(111, 222, 3333, 4444);
    Tuple.Dual<String, Integer> dual = Tuple.create("First", 2);
    Tuple.Dual<Boolean, Boolean> nullDual = Tuple.create(null, null);
    Tuple.Hepta<Boolean, Integer, String, LocalDate, Progress, String, Double> hepta=
            Tuple.create(false, 77, "Hepta", LocalDate.MAX, Progress.Done, "Result", 99.9d);
    Tuple NULL = Tuple.create(null);
    Tuple.Set<Tuple> tupleSet = Tuple.setOf(tuple0, tuple1);

    @Test
    public void testToString(){
        System.out.println(tuple0.toString());
        System.out.println(tuple1.toString());
        System.out.println(tuple2.toString());
        System.out.println(tuple3.toString());
        System.out.println(tuple4.toString());
        System.out.println(tuple5.toString());
        System.out.println(tuple6.toString());
        System.out.println(tuple7.toString());
        System.out.println(set1.toString());
        System.out.println(boolSet2.toString());
        System.out.println(stringSet.toString());
        System.out.println(intSet.toString());
        System.out.println(dual.toString());
        System.out.println(nullDual.toString());
        System.out.println(hepta.toString());
        System.out.println(NULL.toString());
        System.out.println(Tuple.UNIT.toString());
        System.out.println(Tuple.TRUE.toString());
        System.out.println(Tuple.FALSE.toString());
        System.out.println(tupleSet.toString());
    }

    @Test
    public void create0() {
        assertTrue(tuple0 instanceof Tuple.Unit);
        assertTrue(tuple0 instanceof Tuple);
        assertFalse(tuple0 instanceof Tuple.Single);
        assertFalse(tuple0 instanceof Tuple.Set);
        assertEquals(tuple0, Tuple.UNIT);
        assertEquals(0, tuple0.getLength());
    }

    @Test
    public void create1() {
        assertEquals(1, tuple1.getLength());
        assertTrue(tuple1 instanceof Tuple.Single);
        assertFalse(tuple1 instanceof Tuple.Set);

        Tuple _1 = Tuple.create("First");
        assertEquals(_1, tuple1);
        Tuple.Single<String> singleString = (Tuple.Single)_1;
        assertEquals(_1, singleString);
        String v = singleString.getFirst();
        assertEquals("First", v);
        assertEquals(tuple0, tupleSet.getGroup()[0]);

        assertEquals(1, set1.getLength());
    }

    @Test
    public void create2() {
        assertEquals(2, tuple2.getLength());
        assertEquals(2, boolSet2.getLength());
        assertEquals(2, boolSet2.getGroup().length);
        assertTrue(tuple2 instanceof Tuple.Dual);
        assertFalse(tuple2 instanceof Tuple.Set);

        assertTrue(boolSet2 instanceof Tuple.Set);
        assertEquals(boolSet2, Tuple.setOf(Boolean.valueOf(true), Boolean.valueOf("false")));
        Tuple _2 = Tuple.create(null, null);
        assertEquals(2, _2.getLength());
        assertTrue(_2 instanceof Tuple.Dual);
        Tuple.Dual _dual = (Tuple.Dual) _2;
        assertTrue(_dual.getFirst() == null);
        assertTrue(_dual.getSecond() == null);
        assertTrue(nullDual.getFirst() == null);
        assertEquals(nullDual, _dual);
    }

    @Test
    public void create3() {
    }

    @Test
    public void create4() {
    }

    @Test
    public void create5() {
    }

    @Test
    public void create6() {
    }

    @Test
    public void create7() {
    }

    @Test
    public void ofArray() {
    }

    @Test
    public void getValuesOf() {
    }

    @Test
    public void getLength() {
    }

    @Test
    public void testHashCode() {
    }

    @Test
    public void equals() {
        Tuple.Dual<Boolean, Boolean> nullDual2 = Tuple.create(null, null);
        assertTrue(nullDual.equals(nullDual2));
        assertTrue(nullDual2.equals(nullDual));
        assertEquals(nullDual2, nullDual);
        assertEquals(nullDual, nullDual2);

        Tuple.Set<Boolean> nullDual3 = Tuple.setOf(null, null);
        assertFalse(nullDual2.equals(nullDual3));
        assertFalse(nullDual3.equals(nullDual2));

        Tuple.Set<String> null4 = Tuple.setOf(null, null);
        assertTrue(nullDual3.equals(null4));
        assertEquals(null4, nullDual3);
        assertEquals(nullDual3, null4);

    }
}