package com.easyworks.tuple;

import com.easyworks.Functions;
import com.easyworks.function.PredicateThrowable;
import com.easyworks.utility.Logger;
import com.easyworks.utility.TypeHelper;
import org.junit.Assert;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

    class B extends A<Integer>{
        public B(Integer i){
            super(i);
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
    Set<Boolean> boolSet2 = Tuple.setOf(true, false);
    Tuple tuple3 = Tuple.create(3, "Third", 3.33d);
    Tuple4 tuple4 = Tuple.create(true, null, new A("Aaa"), new A("B").getClass());
    Tuple5 tuple5 = Tuple.create(Progress.Working, "5", null, 5f, false);
    Tuple tuple6 = Tuple.create(6, Integer.valueOf(6), 6f, 6.0d, "six",  Progress.ofIndex(3));


    Tuple2<String, Integer> dual = Tuple.create("First", 2);
    Tuple2<Boolean, Boolean> nullDual = Tuple.create(null, null);
    Tuple7<Boolean, Integer, String, LocalDate, Progress, String, Double> hepta=
            Tuple.create(false, 77, "Tuple7", LocalDate.MAX, Progress.Done, "Result", 99.9d);
    Tuple NULL = Tuple.create(null);
    Set<String> stringSet = Tuple.setOf("", null, "third", "3");
    Set<Integer> intSet = Tuple.setOf(111, 222, 3333, 4444);
    Set<Tuple> tupleSet = Tuple.setOf(tuple0, tuple1);

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
    public void testSetOf(){
        Date date = null;
        Set nullSet = Tuple.setOf(date);
        assertEquals(Date.class, nullSet.elementType);
        assertEquals(1, nullSet.getLength());
        assertEquals(null, nullSet.get(0));

        Integer aInteger = null;
        Set<Integer> iSet = (Set<Integer>) Tuple.setOf(aInteger);
        assertEquals(1, iSet.getLength());
        assertEquals(null, iSet.get(0));
    }

    @Test
    public void create0() {
        assertTrue(tuple0 instanceof Tuple0);
        assertTrue(tuple0 instanceof Tuple);
        assertFalse(tuple0 instanceof Tuple1);
        assertFalse(tuple0 instanceof Set);
        assertEquals(tuple0, Tuple.UNIT);
        assertEquals(0, tuple0.getLength());
    }

    @Test
    public void create1() {
        assertEquals(1, tuple1.getLength());
        assertTrue(tuple1 instanceof Tuple1);
        assertFalse(tuple1 instanceof Set);

        Tuple _1 = Tuple.create("First");
        assertEquals(_1, tuple1);
        Tuple1<String> singleString = (Tuple1)_1;
        assertEquals(_1, singleString);
        String v = singleString.getFirst();
        assertEquals("First", v);
        assertEquals(tuple0, tupleSet.get(0));

        assertEquals(1, set1.getLength());
    }

    @Test
    public void create2() {
        assertEquals(2, tuple2.getLength());
        assertEquals(2, boolSet2.getLength());
        assertEquals(2, boolSet2.getLength());
        assertTrue(tuple2 instanceof Tuple2);
        assertFalse(tuple2 instanceof Set);

        assertTrue(boolSet2 instanceof Set);
        assertEquals(boolSet2, Tuple.setOf(Boolean.valueOf(true), Boolean.valueOf("false")));
        Tuple _2 = Tuple.create(null, null);
        assertEquals(2, _2.getLength());
        assertTrue(_2 instanceof Tuple2);
        Tuple2 _dual = (Tuple2) _2;
        assertTrue(_dual.getFirst() == null);
        assertTrue(_dual.getSecond() == null);
        assertTrue(nullDual.getFirst() == null);
        assertEquals(nullDual, _dual);
    }

    @Test
    public void create3() {
        assertEquals(3, tuple3.getLength());
        assertEquals(1, tuple3.getSetOf(Integer.class).getLength());
        assertEquals(1, tuple3.getSetOf(double.class).getLength());
        assertEquals(1, tuple3.getSetOf(String.class).getLength());
        assertEquals(Integer.valueOf(3), tuple3.getSetOf(Integer.class).get(0));
        assertEquals("Third", tuple3.getSetOf(String.class).get(0));
    }

    @Test
    public void create4() {
        assertEquals(4, tuple4.getLength());
        assertEquals(true, tuple4.getFirst());
        assertEquals(A.class, tuple4.getFourth());
        assertEquals("Aaa", ((A)tuple4.getThird()).getValue());
    }

    @Test
    public void create5() {
        assertEquals(5, tuple5.getLength());
        assertEquals(Progress.Working, tuple5.getFirst());
        assertEquals(false, tuple5.getFifth());
    }

    @Test
    public void create6() {

        assertEquals(6, tuple6.getLength());
        assertEquals(2, tuple6.getSetOf(Integer.class).getLength());
        assertEquals(2, tuple6.getSetOf(int.class).getLength());
        assertEquals(1, tuple6.getSetOf(float.class).getLength());
        assertEquals(1, tuple6.getSetOf(Double.class).getLength());
        assertEquals(1, tuple6.getSetOf(Progress.class).getLength());
    }

    Tuple tuple7 = Tuple.create("Seven", new String[]{"arrayValue1", "arrayValue2"}, Character.valueOf('7'), '7', Integer.valueOf(7),
            new Character[]{'C'}, new char[]{'a','b'});

    @Test
    public void create7() {

        assertEquals(7, tuple7.getLength());
        assertEquals(2, tuple7.getSetOf(char.class).getLength());
        assertEquals(2, tuple7.getSetOf(Character.class).getLength());
        assertEquals(2, tuple7.getSetOf(char[].class).getLength());
        assertEquals(2, tuple7.getSetOf(Character[].class).getLength());
        assertEquals(1, tuple7.getSetOf(String.class).getLength());
        assertEquals(1, tuple7.getSetOf(int.class).getLength());

        Tuple7<Integer, Boolean, String, char[], Double, A<String>, PredicateThrowable<A>> hepta =
                Tuple.create(7, 7%2==0, "Seven", "Seven".toCharArray(), 7.0d, new A("Seven"),
                        (PredicateThrowable<A>) a -> ((String)(a.value)).length() > 5 );
        Integer item1 = hepta.getFirst();
        Boolean item2 = hepta.getSecond();
        String item3 = hepta.getThird();
        char[] item4 = hepta.getFourth();
        Double item5 = hepta.getFifth();
        A<String> item6 = hepta.getSixth();
        PredicateThrowable<A> item7 = hepta.getSeventh();
        assertEquals(false, item2);
        Assert.assertEquals(false, Functions.ReturnsDefaultValue.apply(() -> (boolean)(item7.test(item6))));
    }

    @Test
    public void setOf() {

        assertEquals(4, stringSet.getLength());
        assertEquals(Set.class, stringSet.getClass());
        assertEquals(stringSet.get(0), "");
        assertEquals(stringSet.get(1), null);
        assertTrue(TypeHelper.deepEquals(new Integer[]{111, 222, 3333, 4444}, intSet.asArray()));
        Set tupleSet2 = Tuple.setOf(Tuple.create(), Tuple.create("First"));
        assertEquals(tupleSet, tupleSet2);
    }

    @Test
    public void testArrayToSet(){
        Integer[] ints = new Integer[]{1, 2, 3};
        Set integerSet = Tuple.setOf(ints);
        assertEquals(Integer.class, integerSet.elementType);
        assertEquals(3, integerSet.getLength());

        ints = null;
        integerSet = Tuple.setOf(ints);
        assertEquals(1, integerSet.getLength());
        assertEquals(null, integerSet.get(0));
    }

    @Test
    public void setOf2() {
        List<A> aList = new ArrayList<>();
        aList.add(new A(true));
        aList.add(new A(3.33d));
        aList.add(new B(77));
        Set aSet = Tuple.setOf(aList, A.class);
        assertEquals(3, aSet.getLength());
        assertEquals(A.class, aSet.elementType);
    }

    @Test
    public void getSetOf() {
        assertEquals(0, stringSet.getSetOf(int.class).getLength());
        assertEquals(4, stringSet.getSetOf(String.class).getLength());

        Tuple7<Integer, Boolean, String, char[], Double, A<String>, PredicateThrowable<A>> hepta =
                Tuple.create(7, 7%2==0, "Seven", "Seven".toCharArray(), 7.0d, new A("Seven"),
                        (PredicateThrowable<A>) a -> ((String)(a.value)).length() > 5 );

        Set<String> stringSet1 = hepta.getSetOf(String.class);
        assertTrue(TypeHelper.deepEquals(new String[]{"Seven"}, stringSet1.asArray()));

        Tuple t = Tuple.create(1, 2, 3, null, 4, 5);
        assertTrue(TypeHelper.deepEquals(new Integer[]{1,2,3,4,5}, t.getSetOf(int.class).asArray()));

        Set<A> aSet = Tuple.setOf(new A('a'), new A("A"), new A(100));
        Set aSet1 = aSet.getSetOf(A.class);
        assertEquals(aSet, aSet1);
        Set aSet2 = aSet.getSetOf(B.class);
        assertEquals(Set.EMPTY, aSet2);

        Set<B> bSet = Tuple.setOf(new B(1), new B(2), new B(Integer.valueOf(3)));
        Set bSet1 = bSet.getSetOf(A.class);
        assertEquals(bSet, bSet1);

        Set bSet2 = bSet.getSetOf(B.class);
        assertEquals(bSet, bSet2);
    }

    @Test
    public void getSetOf2() {
        Tuple tuple = Tuple.of((Integer)null, -1, -2, new int[]{1,2}, new Integer[]{3,4,5}, -5, new Object[]{6,7});
        Set<Integer> integers = tuple.getSetOf(int.class);

        Set<Integer[]> integerArrays = tuple.getSetOf(Integer[].class);
        assertEquals(Set.setOf(new Integer[]{1,2}, new Integer[]{3,4,5}), integerArrays);

        Set<int[]> intArrays = tuple.getSetOf(int[].class);
        assertEquals(Set.setOf(new int[]{1,2}, new int[]{3,4,5}), intArrays);

        Set<Integer> ints = tuple.getSetOf(Integer.class);
        assertEquals(Set.setOf(Integer.valueOf(-1), Integer.valueOf(-2), Integer.valueOf(-5)), ints);

        assertEquals(ints, integers);
   }

    @Test
    public void getSetOfWithPredicate() {
        Tuple manyValues = Tuple.of("abc", null, 33, true, "a", "", 'a', Tuple.TRUE, 47);
        assertEquals(Tuple.setOf("abc", "a", ""), manyValues.getSetOf(String.class));
        assertEquals(Tuple.setOf("abc"), manyValues.getSetOf(String.class, s->s.length()>2));
    }

    @Test
    public void asTuple(){
        Tuple tuple = Tuple.of(null);
        assertEquals(1, tuple.getLength());

        int[] ints = new int[] {1, 2};
        tuple = Tuple.of(ints);
        assertEquals(1, tuple.getLength());
        assertEquals(Tuple.create(new int[] {1, 2}), tuple);

        Object[] elements = new Object[] {1, "ok", true};
        tuple = Tuple.of(elements);
        assertEquals(3, tuple.getLength());
        assertTrue(TypeHelper.deepEquals(tuple.values, elements));

        elements = new Object[]{1,2,3,"abc", 'a', true, Tuple.TRUE, Tuple.FALSE, Tuple.setOf(1, 2, 3), 'b'};
        tuple = Tuple.of(elements);
        assertEquals(10, tuple.getLength());
        Set<Tuple> tupleSet = tuple.getSetOf(Tuple.class);
        assertEquals(3, tupleSet.getLength());
        assertNotEquals(tupleSet, Tuple.of(Tuple.TRUE, Tuple.FALSE, Tuple.setOf(1, 2, 3)));
        assertEquals(Tuple.setOf(Tuple.TRUE, Tuple.FALSE, Tuple.setOf(1, 2, 3)), tupleSet);
    }

    @Test
    public void testHashCode() {
        System.out.println(tuple0.hashCode());
        System.out.println(tuple1.hashCode());
        System.out.println(tuple2.hashCode());
        System.out.println(tuple3.hashCode());
        System.out.println(tuple4.hashCode());
        System.out.println(tuple5.hashCode());
        System.out.println(tuple6.hashCode());
        System.out.println(tuple7.hashCode());
        System.out.println(set1.hashCode());
        System.out.println(boolSet2.hashCode());
        System.out.println(stringSet.hashCode());
        System.out.println(intSet.hashCode());
        System.out.println(dual.hashCode());
        System.out.println(nullDual.hashCode());
        System.out.println(hepta.hashCode());
        System.out.println(NULL.hashCode());
        System.out.println(Tuple.UNIT.hashCode());
        System.out.println(Tuple.TRUE.hashCode());
        System.out.println(Tuple.FALSE.hashCode());
        System.out.println(tupleSet.hashCode());

        Set<A> aSet = Tuple.setOf(new A('a'), new A("A"), new A(100));
        Set<B> bSet = Tuple.setOf(new B(1), new B(2), new B(Integer.valueOf(3)));
        Set bSet1 = bSet.getSetOf(A.class);
        System.out.println(aSet.hashCode());
        System.out.println(bSet.hashCode());
        System.out.println(bSet1.hashCode());
    }

    @Test
    public void equals() {
        Tuple2<Boolean, Boolean> nullDual2 = Tuple.create(null, null);
        assertTrue(nullDual.equals(nullDual2));
        assertTrue(nullDual2.equals(nullDual));
        assertEquals(nullDual2, nullDual);
        assertEquals(nullDual, nullDual2);

        Set<Boolean> nullDual3 = Tuple.setOf(Boolean.class, new Boolean[]{null, null});
        assertFalse(nullDual2.equals(nullDual3));
        assertFalse(nullDual3.equals(nullDual2));

        Set<String> null4 = Tuple.setOf(String.class, new String[]{null, null} );
        assertFalse(nullDual3.equals(null4));
        assertNotEquals(null4, nullDual3);
        assertNotEquals(nullDual3, null4);

        Set<String> null5 = Tuple.setOf((String)null, (String)null);
        assertEquals(null5, null4);

        Tuple tuple1 = Tuple.create(1, "abc".toCharArray(), new boolean[]{true, false}, new int[]{3, 2}, new int[][]{null, new int[]{0}});
        Tuple tuple2 = Tuple.create(Integer.valueOf(1),
                new Character[]{'a','b','c'}, new Boolean[]{true, false}, new Integer[]{3, 2}, new Integer[][]{null, new Integer[]{0}});
        assertEquals(tuple1, tuple2);
    }

    public static List<String> closeMessages = new ArrayList<>();
    public class AutoA implements AutoCloseable{
        @Override
        public void close() throws Exception {
            closeMessages.add(this.getClass().getSimpleName() + ".close()");
        }
    }

    public class AutoB extends AutoA { }
    public class AutoC extends AutoA { }
    public class AutoD extends AutoA { }
    public class AutoE extends AutoA { }
    public class AutoF extends AutoA { }
    public class AutoG extends AutoA { }
    public class AutoH extends AutoA { }

    public class E{}

    @Test
    public void close(){
        String[] expected = new String[]{"AutoH.close()","AutoG.close()","AutoF.close()","AutoE.close()",
                "AutoD.close()","AutoC.close()","AutoB.close()","AutoA.close()"};
        testCloseInOrder(new String[0], 123, "String");
        testCloseInOrder(Arrays.copyOfRange(expected, 7, 8), 123, new AutoA());
        testCloseInOrder(Arrays.copyOfRange(expected, 6, 8), new AutoA(), new AutoB());
        testCloseInOrder(Arrays.copyOfRange(expected, 5, 8), 123, new AutoA(), new AutoB(), new AutoC());
        testCloseInOrder(Arrays.copyOfRange(expected, 4, 8), 123, new AutoA(), new AutoB(), new AutoC(), new AutoD());
        testCloseInOrder(Arrays.copyOfRange(expected, 3, 8), 123, new AutoA(), new AutoB(), new AutoC(), new AutoD(), new AutoE());
        testCloseInOrder(Arrays.copyOfRange(expected, 2, 8), 123, new AutoA(), new AutoB(), new AutoC(), new AutoD(), new AutoE(), new AutoF());
        testCloseInOrder(Arrays.copyOfRange(expected, 1, 8), 123, new AutoA(), new AutoB(), new AutoC(), new AutoD(), new AutoE(), new AutoF(), new AutoG());
        testCloseInOrder(expected, 123, new AutoA(), new AutoB(), new AutoC(), new AutoD(), new AutoE(), new AutoF(), new AutoG(), new AutoH());
    }

    private void testCloseInOrder(String[] expection, Object... values){
        closeMessages.clear();
        try (Tuple tuple = Tuple.getTupleSupplier(values).get()){
        }catch (Exception ex){
            Logger.L(ex.getMessage());
        }

        assertTrue(TypeHelper.deepEquals(expection, closeMessages.toArray()));
    }


    @Test
    public void testTuple20_ofAllAccessors_retrieveElementAsExpected(){
        Tuple20<Integer, Character, String, Double, Float, Byte, Integer, Boolean, char[], Character[]
                , DayOfWeek, A, Object[][], AutoA, AutoB, double[][], Byte[][], Short[], Comparable[], int[][]> tuple20 = Tuple.create(1, 'a', "String", 3.0d, 2.1f,
                (byte)33, Integer.valueOf(7), null, new char[]{'x'}, new Character[]{'y'},
                DayOfWeek.FRIDAY, new A(3), new Object[0][], new AutoA(), new AutoB(),
                new double[][]{new double[]{1.1}, new double[]{2.2}, new double[]{3.3}}, new Byte[][]{new Byte[]{1,2,3}, null},
                new Short[]{1,2,3}, new Comparable[]{1, 'a'}, new int[][]{new int[0]}
        );

//        assertEquals(Integer.valueOf(1), tuple20.getFirst());
//        assertEquals(Character.valueOf('a'), tuple20.getSecond());
//        assertEquals("String", tuple20.getThird());
//        assertEquals(Double.valueOf(3.0), tuple20.getFourth());
//        assertEquals(Float.valueOf(2.1f), tuple20.getFifth());
//        assertEquals(Byte.valueOf((byte)33), tuple20.getSixth());
//        assertEquals(Integer.valueOf(7), tuple20.getSeventh());
//        assertEquals(null, tuple20.getEighth());
//        assertTrue(TypeHelper.deepEquals(new char[]{'x'}, tuple20.getNineth()));
//        assertEquals(DayOfWeek.FRIDAY, tuple20.getEleventh());
//        assertEquals(A.class, tuple20.getTwelfth().getClass());
//        assertEquals(0, tuple20.getThirteenth().length);
//        assertEquals(AutoA.class, tuple20.getFourteenth().getClass());
//        assertEquals(AutoB.class, tuple20.getFifteenth().getClass());
//        assertTrue(TypeHelper.deepEquals(new double[]{2.2}, tuple20.getSixteenth()[1]));
//        assertEquals(null, tuple20.getSeventeenth()[1]);
//        assertEquals(Short.valueOf("3"), tuple20.getEighteenth()[2]);
        assertTrue(TypeHelper.deepEquals(new Object[]{1, 'a'}, tuple20.getNineteenth()));
        assertEquals(0, tuple20.getTwentieth()[0].length);
    }

    @Test
    public void testTuple20_castAfterOf_retrieveElementAsExpected(){
        Tuple20<Integer, Character, String, Double, Float, Byte, Integer, Boolean, char[], Character[]
                , DayOfWeek, A, Object[][], AutoA, AutoB, double[][], Byte[][], Short[], Comparable[], int[][]> tuple20 = (Tuple20<Integer, Character, String, Double, Float, Byte, Integer, Boolean, char[], Character[]
                , DayOfWeek, A, Object[][], AutoA, AutoB, double[][], Byte[][], Short[], Comparable[], int[][]>)Tuple.of(1, 'a', "String", 3.0d, 2.1f,
                (byte)33, Integer.valueOf(7), null, new char[]{'x'}, new Character[]{'y'},
                DayOfWeek.FRIDAY, new A(3), new Object[0][], new AutoA(), new AutoB(),
                new double[][]{new double[]{1.1}, new double[]{2.2}, new double[]{3.3}}, new Byte[][]{new Byte[]{1,2,3}, null},
                new Short[]{1,2,3}, new Comparable[]{1, 'a'}, new int[][]{new int[0]}
        );

        assertEquals(Integer.valueOf(1), tuple20.getFirst());
        assertEquals(Character.valueOf('a'), tuple20.getSecond());
        assertEquals("String", tuple20.getThird());
        assertEquals(Double.valueOf(3.0), tuple20.getFourth());
        assertEquals(Float.valueOf(2.1f), tuple20.getFifth());
        assertEquals(Byte.valueOf((byte)33), tuple20.getSixth());
        assertEquals(Integer.valueOf(7), tuple20.getSeventh());
        assertEquals(null, tuple20.getEighth());
        assertTrue(TypeHelper.deepEquals(new char[]{'x'}, tuple20.getNineth()));
        assertEquals(DayOfWeek.FRIDAY, tuple20.getEleventh());
        assertEquals(A.class, tuple20.getTwelfth().getClass());
        assertEquals(0, tuple20.getThirteenth().length);
        assertEquals(AutoA.class, tuple20.getFourteenth().getClass());
        assertEquals(AutoB.class, tuple20.getFifteenth().getClass());
        assertTrue(TypeHelper.deepEquals(new double[]{2.2}, tuple20.getSixteenth()[1]));
        assertEquals(null, tuple20.getSeventeenth()[1]);
        assertEquals(Short.valueOf("3"), tuple20.getEighteenth()[2]);
        assertTrue(TypeHelper.deepEquals(new Object[]{1, 'a'}, tuple20.getNineteenth()));
        assertEquals(0, tuple20.getTwentieth()[0].length);
    }
}