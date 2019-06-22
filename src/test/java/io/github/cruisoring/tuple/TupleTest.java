package io.github.cruisoring.tuple;

import io.github.cruisoring.TypeHelper;
import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.throwables.PredicateThrowable;
import io.github.cruisoring.utility.ArrayHelper;
import io.github.cruisoring.utility.PlainList;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import static io.github.cruisoring.Asserts.*;

public class TupleTest {
    public static List<String> closeMessages = new PlainList<>();
    Tuple tuple0 = Tuple.create();
    Tuple tuple1 = Tuple.create("First");
    Tuple set1 = Tuple.setOf(true);
    Tuple tuple2 = Tuple.create("Second", "2");
    Tuple<Boolean> boolSet2 = Tuple.setOf(true, false);
    Tuple tuple3 = Tuple.create(3, "Third", 3.33d);
    Tuple4 tuple4 = Tuple.create(true, null, new A("Aaa"), new A("B").getClass());
    Tuple5 tuple5 = Tuple.create(Progress.Working, "5", null, 5f, false);
    Tuple tuple6 = Tuple.create(6, Integer.valueOf(6), 6f, 6.0d, "six", Progress.ofIndex(3));
    Tuple2<String, Integer> dual = Tuple.create("First", 2);
    Tuple2<Boolean, Boolean> nullDual = Tuple.create(null, null);
    Tuple7<Boolean, Integer, String, LocalDate, Progress, String, Double> hepta =
            Tuple.create(false, 77, "Tuple7", LocalDate.MAX, Progress.Done, "Result", 99.9d);
    Tuple NULL = Tuple.create(null);
    Tuple<String> stringSet = Tuple.setOf("", null, "third", "3");
    Tuple<Integer> intSet = Tuple.setOf(111, 222, 3333, 4444);
    Tuple<Tuple> tupleSet = Tuple.setOf(tuple0, tuple1);
    Tuple tuple7 = Tuple.create("Seven", new String[]{"arrayValue1", "arrayValue2"}, Character.valueOf('7'), '7', Integer.valueOf(7),
            new Character[]{'C'}, new char[]{'a', 'b'});
    Object[] raw = new Object[]{
            DayOfWeek.FRIDAY, new A(3), new Object[0][], new AutoA(), new AutoB(),
            new double[][]{new double[]{1.1}, new double[]{2.2}, new double[]{3.3}}, new Byte[][]{new Byte[]{1, 2, 3}, null},
            new Short[]{1, 2, 3}, new Comparable[]{1, 'a'}, new int[][]{new int[0]},
            "The 21th", 22
    };

    @Test
    public void testArraysDeepFunctions() {
        Logger.D("%d", TypeHelper.deepHashCode(new int[]{1, 2, 3}));
        Logger.D("%d", Arrays.deepHashCode(new Integer[]{1, 2, 3}));
        Logger.D("%d", Arrays.deepHashCode(new int[][]{new int[]{1, 2, 3}, new int[0], null, new int[]{4, 5}}));
        Logger.D("%d", Arrays.deepHashCode(new Object[]{new Integer[]{1, 2, 3}, new int[0], null, new int[]{4, 5}}));
        Logger.D("%d", Arrays.deepHashCode(new Integer[][]{new Integer[]{1, 2, 3}, new Integer[0], null, new Integer[]{4, 5}}));

        assertAllTrue(Integer.valueOf(1).equals(1));
        assertEquals(1, Integer.valueOf(1));
        //assertEquals(new int[]{1,2,3}, new Integer[]{1,2,3});

    }

    @Test
    public void deepHashCode() {
        Logger.D("%d", TypeHelper.deepHashCode(new int[]{1, 2, 3}));
        assertEquals(TypeHelper.deepHashCode(new int[]{1, 2, 3}), TypeHelper.deepHashCode(new Integer[]{1, 2, 3}));
        Logger.D("%d", TypeHelper.deepHashCode(new Object[]{new int[]{1, 2, 3}, new int[0], null, new int[]{4, 5}}));
        assertEquals(TypeHelper.deepHashCode(new Object[]{new int[]{1, 2, 3}, new int[0], null, new int[]{4, 5}}),
                TypeHelper.deepHashCode(new Object[]{new Integer[]{1, 2, 3}, new Integer[0], null, new int[]{4, 5}}));
        Logger.D("%d", TypeHelper.deepHashCode(new int[][]{new int[]{1, 2, 3}, new int[0], null, new int[]{4, 5}}));
        assertEquals(TypeHelper.deepHashCode(new int[][]{new int[]{1, 2, 3}, new int[0], null, new int[]{4, 5}}),
                TypeHelper.deepHashCode(new Integer[][]{new Integer[]{1, 2, 3}, new Integer[0], null, new Integer[]{4, 5}}));
    }

    @Test
    public void testValueEquals() {
        assertEquals(new int[]{1, 2, 3}, new Integer[]{1, 2, 3});
        assertEquals(new Object[]{new int[]{1, 2, 3}, new int[0], null, new int[]{4, 5}},
                new Object[]{new Integer[]{1, 2, 3}, new Integer[0], null, new int[]{4, 5}});
        assertEquals(new int[][]{new int[]{1, 2, 3}, new int[0], null, new int[]{4, 5}},
                new Integer[][]{new Integer[]{1, 2, 3}, new Integer[0], null, new Integer[]{4, 5}});

        assertEquals(new Object[]{new int[]{1, 2, 3}, new int[0], null, new int[]{4, 5}},
                new Comparable[][]{new Integer[]{1, 2, 3}, new Integer[0], null, new Integer[]{4, 5}});

        assertNotEquals(new Object[]{new int[]{1, 2, 3}, new int[0], null, new int[]{4, 5}},
                new Number[][]{new Number[]{1, 2, 3}, new Number[0], null, new Number[]{4, 5}}, true);

        assertEquals(new Object[]{new int[]{1, 2, 3}, new int[0], null, new int[]{4, 5}},
                new Number[][]{new Number[]{1, 2, 3}, new Number[0], null, new Number[]{4, 5}});
    }

    @Test
    public void testAccessors() {
        assertEquals(false, hepta.getFirst());
        assertEquals(Integer.valueOf(77), hepta.getSecond());
        assertEquals("Tuple7", hepta.getThird());
        assertEquals(LocalDate.MAX, hepta.getFourth());
        assertEquals(Progress.Done, hepta.getFifth());
        assertEquals("Result", hepta.getSixth());
        assertEquals(Double.valueOf(99.9), hepta.getSeventh());
    }

    @Test
    public void testToString() {
        Logger.D(tuple0.toString());
        Logger.D(tuple1.toString());
        Logger.D(tuple2.toString());
        Logger.D(tuple3.toString());
        Logger.D(tuple4.toString());
        Logger.D(tuple5.toString());
        Logger.D(tuple6.toString());
        Logger.D(tuple7.toString());
        Logger.D(set1.toString());
        Logger.D(boolSet2.toString());
        Logger.D(stringSet.toString());
        Logger.D(intSet.toString());
        Logger.D(dual.toString());
        Logger.D(nullDual.toString());
        Logger.D(hepta.toString());
        Logger.D(NULL.toString());
        Logger.D(Tuple.UNIT.toString());
        Logger.D(Tuple.TRUE.toString());
        Logger.D(Tuple.FALSE.toString());
        Logger.D(tupleSet.toString());
    }

    @Test
    public void testSetOf_withElementTypeSpecified() {
        Date date = null;
        Tuple<Date> nullSet = Tuple.setOf(date);
        assertEquals(Date.class, nullSet.getElementType());
        assertEquals(1, nullSet.getLength());
        assertEquals(null, nullSet.getValue(0));

        Integer aInteger = null;
        Tuple<Integer> iSet = Tuple.setOfType(Integer.class, aInteger);
        assertEquals(Integer.class, iSet.getElementType());
        assertEquals(1, iSet.getLength());
        assertEquals(null, iSet.getValue(0));
        Tuple<Number> numSet = Tuple.setOfType(Number.class, aInteger);
        assertEquals(Number.class, numSet.getElementType());

        Tuple<Integer> integerSet = Tuple.setOf(1, 2, 3);
        assertAllTrue(Arrays.deepEquals(new Integer[]{1, 2, 3}, integerSet.asArray()));

        Tuple<Comparable> comparableSet = Tuple.setOfType(Comparable.class, 1.0, 'a', "abc");
        assertAllTrue(Arrays.deepEquals(new Comparable[]{1.0, 'a', "abc"}, comparableSet.asArray()));

        Tuple intTuple = Tuple.setOf(1, 23);
        assertEquals(Integer.class, intTuple.getElementType());
    }

    @Test
    public void create0() {
        assertAllTrue(tuple0 instanceof Tuple0, tuple0 instanceof Tuple);
        assertAllFalse(tuple0 instanceof Tuple1);
        assertEquals(tuple0, Tuple.UNIT);
        assertEquals(0, tuple0.getLength());
    }

    @Test
    public void create1() {
        assertEquals(1, tuple1.getLength());
        assertAllTrue(tuple1 instanceof Tuple1);

        Tuple _1 = Tuple.create("First");
        assertEquals(_1, tuple1);
        Tuple1<String> singleString = (Tuple1) _1;
        assertEquals(_1, singleString);
        String v = singleString.getFirst();
        assertEquals("First", v);
        assertEquals(tuple0, tupleSet.getValue(0));

        assertEquals(1, set1.getLength());
    }

    @Test
    public void create2() {
        assertEquals(2, tuple2.getLength());
        assertEquals(2, boolSet2.getLength());
        assertEquals(2, boolSet2.getLength());
        assertAllTrue(tuple2 instanceof Tuple2,
                tuple2 instanceof Tuple);

        assertAllTrue(boolSet2 instanceof Tuple);
        assertEquals(boolSet2, Tuple.setOf(Boolean.valueOf(true), Boolean.valueOf("false")));
        Tuple _2 = Tuple.create(null, null);
        assertEquals(2, _2.getLength());
        assertAllTrue(_2 instanceof Tuple2);
        Tuple2 _dual = (Tuple2) _2;
        assertAllNull(_dual.getFirst(),
                _dual.getSecond(),
                nullDual.getFirst());
        assertEquals(nullDual, _dual);
    }

    @Test
    public void create3() {
        assertEquals(3, tuple3.getLength());
        assertEquals(1, tuple3.getSetOf(Integer.class).getLength());
        assertEquals(1, tuple3.getSetOf(double.class).getLength());
        assertEquals(1, tuple3.getSetOf(String.class).getLength());
        assertEquals(Integer.valueOf(3), tuple3.getSetOf(Integer.class).getValue(0));
        assertEquals("Third", tuple3.getSetOf(String.class).getValue(0));
    }

    @Test
    public void create4() {
        assertEquals(4, tuple4.getLength());
        assertEquals(true, tuple4.getFirst());
        assertEquals(A.class, tuple4.getFourth());
        assertEquals("Aaa", ((A) tuple4.getThird()).getValue());
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
                Tuple.create(7, 7 % 2 == 0, "Seven", "Seven".toCharArray(), 7.0d, new A("Seven"),
                        a -> ((String) (a.value)).length() > 5);
        Integer item1 = hepta.getFirst();
        Boolean item2 = hepta.getSecond();
        String item3 = hepta.getThird();
        char[] item4 = hepta.getFourth();
        Double item5 = hepta.getFifth();
        A<String> item6 = hepta.getSixth();
        PredicateThrowable<A> item7 = hepta.getSeventh();
        assertEquals(false, item2);
        assertEquals(false, item7.tryTest(item6));
    }

    @Test
    public void isMatched() {
        Tuple6<Comparable, Object, Number, String, DayOfWeek, Boolean> tuple = Tuple.create(1, 'a', 3.0, "abc", DayOfWeek.MONDAY, true);

        assertAllTrue(tuple.isMatched(new HashMap<Integer, Object>(){{
            put(0, 1);
            put(3, "abc");
        }}));

        assertAllFalse(tuple.isMatched(new HashMap<Integer, Object>(){{
            put(0, 2);
            put(3, "abc");
        }}));
    }

    @Test
    public void meetConditions() {
        Tuple6<Comparable, Object, Number, String, DayOfWeek, Boolean> tuple = Tuple.create(1, 'a', 3.0, "abc", DayOfWeek.MONDAY, true);

        assertAllTrue(tuple.meetConditions(new HashMap<Integer, PredicateThrowable>(){{
            put(0, o -> (Integer)o > 0);
            put(3, o -> o.toString().startsWith("a"));
        }}));

        assertAllFalse(tuple.meetConditions(new HashMap<Integer, PredicateThrowable>(){{
            put(1, o -> o instanceof Integer);
        }}));
    }

    @Test
    public void anyMatch() {
        Tuple6<Comparable, Object, Number, String, DayOfWeek, Boolean> tuple = Tuple.create(1, 'a', 3.0, "abc", DayOfWeek.MONDAY, true);

        assertAllTrue(tuple.anyMatch(o -> o == DayOfWeek.MONDAY));

        assertAllFalse(tuple.anyMatch(o -> o.toString().length() > 10));
    }

    @Test
    public void allMatch() {
        Tuple6<Comparable, Object, Number, String, DayOfWeek, Boolean> tuple = Tuple.create(1, 'a', 3.0, "abc", DayOfWeek.MONDAY, true);

        assertAllTrue(tuple.allMatch(o -> o != null));

        assertAllFalse(tuple.allMatch(o -> o instanceof Number));
    }

    @Test
    public void noneMatch() {
        Tuple6<Comparable, Object, Number, String, DayOfWeek, Boolean> tuple = Tuple.create(1, 'a', 3.0, "abc", DayOfWeek.MONDAY, true);

        assertAllTrue(tuple.noneMatch(o -> o instanceof LocalDate));

        assertAllFalse(tuple.noneMatch(o -> o instanceof DayOfWeek));
    }

    @Test
    public void setOf() {

        assertEquals(4, stringSet.getLength());
        assertEquals(Tuple.class, stringSet.getClass());
        assertEquals(stringSet.getValue(0), "");
        assertEquals(stringSet.getValue(1), null);
        assertEquals(new Integer[]{111, 222, 3333, 4444}, intSet.asArray());
        Tuple tupleSet2 = Tuple.setOf(Tuple.create(), Tuple.create("First"));
        assertEquals(tupleSet, tupleSet2);
    }

    @Test
    public void testArrayToSet() {
        Integer[] ints = new Integer[]{1, 2, 3};
        Tuple integerSet = Tuple.setOf(ints);
        assertEquals(Integer.class, integerSet.getElementType());
        assertEquals(3, integerSet.getLength());
    }

    @Test
    public void setOfArray() {
        List<A> aList = new PlainList<>();
        aList.add(new A(true));
        aList.add(new A(3.33d));
        aList.add(new B(77));
        Tuple aSet = Tuple.setOfType(A.class, aList);
        assertEquals(3, aSet.getLength());
        assertEquals(A.class, aSet.getElementType());
    }

    @Test
    public void getSetOf() {
        assertEquals(0, stringSet.getSetOf(int.class).getLength());
        assertEquals(4, stringSet.getSetOf(String.class).getLength());

        Tuple7<Integer, Boolean, String, char[], Double, A<String>, PredicateThrowable<A>> hepta =
                Tuple.create(7, 7 % 2 == 0, "Seven", "Seven".toCharArray(), 7.0d, new A("Seven"),
                        a -> ((String) (a.value)).length() > 5);

        Tuple<String> stringSet1 = hepta.getSetOf(String.class);
        assertEquals(new String[]{"Seven"}, stringSet1.asArray());

        Tuple t = Tuple.create(1, 2, 3, null, 4, 5);
        assertEquals(new Integer[]{1, 2, 3, 4, 5}, t.getSetOf(int.class).asArray());

        Tuple<A> aSet = Tuple.setOf(new A('a'), new A("A"), new A(100));
        Tuple aSet1 = aSet.getSetOf(A.class);
        assertEquals(aSet, aSet1);
        Tuple aSet2 = aSet.getSetOf(B.class);
        assertEquals(0, aSet2.getLength());

        Tuple<B> bSet = Tuple.setOf(new B(1), new B(2), new B(Integer.valueOf(3)));
        Tuple bSet1 = bSet.getSetOf(A.class).getSetOf(B.class);
        assertEquals(bSet, bSet1);

        Tuple bSet2 = bSet.getSetOf(B.class);
        assertEquals(bSet, bSet2);
    }

    @Test
    public void getSetOf2() {
        Tuple tuple = Tuple.create(-2, null, -1, new int[]{1, 2}, new Integer[]{3, 4, 5}, -5, new Object[]{6, 7});
        Tuple<Integer> integers = tuple.getSetOf(int.class);

        Tuple<Integer[]> integerArrays = tuple.getSetOf(Integer[].class);
        assertEquals(Tuple.setOfType(Integer[].class, null, new Integer[]{1, 2}, new Integer[]{3, 4, 5}), integerArrays);

        Tuple<int[]> intArrays = tuple.getSetOf(int[].class);
        assertEquals(Tuple.setOf(new int[]{1, 2}, new int[]{3, 4, 5}), intArrays);

        Tuple<Integer> ints = tuple.getSetOf(Integer.class);
        assertEquals(Tuple.setOf(Integer.valueOf(-2), null, Integer.valueOf(-1), Integer.valueOf(-5)), ints);
    }

    @Test
    public void getSetOfWithPredicate() {
        Tuple<String> manyValues = Tuple.create("abc", null, 33, true, "a", "", 'a', Tuple.TRUE, 47);
        assertEquals(Tuple.setOf("abc", null, "a", ""), manyValues.getSetOf(String.class));
        assertEquals(Tuple.setOf("abc"), manyValues.getSetOf(String.class, s -> s.length() > 2));
    }

    @Test
    public void asTuple() {
        Tuple tuple = Tuple.create(null);
        assertEquals(1, tuple.getLength());

        int[] ints = new int[]{1, 2};
        tuple = Tuple.create(ints);
        assertEquals(1, tuple.getLength());
        assertEquals(Tuple.create(new int[]{1, 2}), tuple);

        Object[] elements = new Object[]{1, "ok", true};
        tuple = Tuple.setOf(elements);
        assertEquals(3, tuple.getLength());
        assertEquals(tuple.values, elements);

        elements = new Object[]{1, 2, 3, "abc", 'a', true, Tuple.TRUE, Tuple.FALSE, Tuple.setOf(1, 2, 3), 'b'};
        tuple = Tuple.setOf(elements);
        assertEquals(10, tuple.getLength());
        Tuple<Tuple> tupleSet = tuple.getSetOf(Tuple.class);
        assertEquals(3, tupleSet.getLength());
        assertEquals(tupleSet, Tuple.create(Tuple.TRUE, Tuple.FALSE, Tuple.setOf(1, 2, 3)));
        assertEquals(Tuple.setOf(Tuple.TRUE, Tuple.FALSE, Tuple.setOf(1, 2, 3)), tupleSet);
    }

    @Test
    public void testOf() {
        Tuple6<Comparable, Object, Number, String, DayOfWeek, Boolean> tuple = Tuple.create(1, 'a', 3.0, "abc", DayOfWeek.MONDAY, true);
        Comparable first = tuple.getFirst();
        assertEquals(Integer.valueOf(1), first);
        assertEquals(Character.valueOf('a'), tuple.getSecond());
        assertEquals(Double.valueOf(3.0), tuple.getThird());
        assertEquals("abc", tuple.getFourth());
        assertEquals(DayOfWeek.MONDAY, tuple.getFifth());
        assertEquals(true, tuple.getSixth());

        Tuple6<Integer, Character, Double, String, DayOfWeek, Boolean> tuple2 = Tuple.create(1, 'a', 3.0, "abc", DayOfWeek.MONDAY, true);
        assertEquals(Integer.valueOf(1), tuple2.getFirst());
        assertEquals(Character.valueOf('a'), tuple2.getSecond());
        assertEquals(Double.valueOf(3.0), tuple2.getThird());
        assertEquals("abc", tuple2.getFourth());
        assertEquals(DayOfWeek.MONDAY, tuple2.getFifth());
        assertEquals(true, tuple2.getSixth());
    }

    @Test
    public void testHashCode() {
        Logger.D("%s: %d", tuple0, tuple0.hashCode());
        Logger.D("%s: %d", tuple1, tuple1.hashCode());
        Logger.D("%s: %d", tuple2, tuple2.hashCode());
        Logger.D("%s: %d", tuple3, tuple3.hashCode());
        Logger.D("%s: %d", tuple4, tuple4.hashCode());
        Logger.D("%s: %d", tuple5, tuple5.hashCode());
        Logger.D("%s: %d", tuple6, tuple6.hashCode());
        Logger.D("%s: %d", tuple7, tuple7.hashCode());
        Logger.D("%s: %d", set1, set1.hashCode());
        Logger.D("%s: %d", boolSet2, boolSet2.hashCode());
        Logger.D("%s: %d", stringSet, stringSet.hashCode());
        Logger.D("%s: %d", intSet, intSet.hashCode());
        Logger.D("%s: %d", dual, dual.hashCode());
        Logger.D("%s: %d", nullDual, nullDual.hashCode());
        Logger.D("%s: %d", hepta, hepta.hashCode());
        Logger.D("%s: %d", NULL, NULL.hashCode());
        Logger.D("%s: %d", Tuple.UNIT, Tuple.UNIT.hashCode());
        Logger.D("%s: %d", Tuple.TRUE, Tuple.TRUE.hashCode());
        Logger.D("%s: %d", Tuple.FALSE, Tuple.FALSE.hashCode());
        Logger.D("%s: %d", tupleSet, tupleSet.hashCode());

        Tuple<A> aSet = Tuple.setOf(new A('a'), new A("A"), new A(100));
        Tuple<B> bSet = Tuple.setOf(new B(1), new B(2), new B(Integer.valueOf(3)));
        Tuple bSet1 = bSet.getSetOf(A.class);
        Logger.D("%s: %d", aSet, aSet.hashCode());
        Logger.D("%s: %d", bSet, bSet.hashCode());
        Logger.D("%s: %d", bSet1, bSet1.hashCode());
    }

    @Test
    public void equals() {
        Tuple2<Boolean, Boolean> nullDual2 = Tuple.create(null, null);
        assertAllTrue(nullDual.equals(nullDual2));
        assertAllTrue(nullDual2.equals(nullDual));
        assertEquals(nullDual2, nullDual);
        assertEquals(nullDual, nullDual2);

        Tuple<Boolean> nullDual3 = Tuple.setOfType(Boolean.class, null, null);
        assertAllTrue(nullDual2.equals(nullDual3));
        assertAllTrue(nullDual3.equals(nullDual2));
        assertEquals(nullDual3, Tuple.setOf(null, null));

        Tuple<String> null4 = Tuple.setOf(null, null);
        assertEquals(String.class, null4.getElementType());
        assertAllFalse(nullDual3.equals(null4), null4.equals(nullDual3));

        Tuple<String> null5 = Tuple.setOf(null, null);
        assertEquals(null5, null4);

        Tuple tuple1 = Tuple.create(1, "abc".toCharArray(), new boolean[]{true, false}, new int[]{3, 2}, new int[][]{null, new int[]{0}});
        Tuple tuple2 = Tuple.create(Integer.valueOf(1),
                new Character[]{'a', 'b', 'c'}, new Boolean[]{true, false}, new Integer[]{3, 2}, new Integer[][]{null, new Integer[]{0}});
        assertEquals(tuple1, tuple2);
    }

    @Test
    public void close() {
        String[] expected = new String[]{"AutoH.close()", "AutoG.close()", "AutoF.close()", "AutoE.close()",
                "AutoD.close()", "AutoC.close()", "AutoB.close()", "AutoA.close()"};
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

    private void testCloseInOrder(String[] expection, Object... values) {
        closeMessages.clear();
        try (Tuple tuple = Tuple.setOf(values)) {
        } catch (Exception ex) {
            Logger.D(ex.getMessage());
        }

        assertEquals(expection, closeMessages.toArray());
    }

    @Test
    public void testTuple10_ofAllAccessors_retrieveElementAsExpected() {
        Tuple10<DayOfWeek, A, Object[][], AutoA, AutoB, double[][], Byte[][], Short[], Comparable[], int[][]> tuple10 = Tuple.create(
                DayOfWeek.FRIDAY, new A(3), new Object[0][], new AutoA(), new AutoB(),
                new double[][]{new double[]{1.1}, new double[]{2.2}, new double[]{3.3}}, new Byte[][]{new Byte[]{1, 2, 3}, null},
                new Short[]{1, 2, 3}, new Comparable[]{1, 'a'}, new int[][]{new int[0]}
        );

        assertEquals(DayOfWeek.FRIDAY, tuple10.getFirst());
        assertEquals(A.class, tuple10.getSecond().getClass());
        assertEquals(0, tuple10.getThird().length);
        assertEquals(AutoA.class, tuple10.getFourth().getClass());
        assertEquals(AutoB.class, tuple10.getFifth().getClass());
        assertEquals(new double[]{2.2}, tuple10.getSixth()[1]);
        assertAllNull(tuple10.getSeventh()[1]);
        assertEquals(Short.valueOf("3"), tuple10.getEighth()[2]);
        assertEquals(new Object[]{1, 'a'}, tuple10.getNineth());
        assertEquals(0, tuple10.getTenth()[0].length);
    }

    @Test
    public void testTuplePlus_castAfterOf_retrieveElementAsExpected() {
        TuplePlus<DayOfWeek, A, Object[][], AutoA, AutoB, double[][], Byte[][], Short[], Comparable[], int[][]> tupleOf =
                (TuplePlus<DayOfWeek, A, Object[][], AutoA, AutoB, double[][], Byte[][], Short[], Comparable[], int[][]>)
                        Tuple.of(raw);

        assertEquals(12, tupleOf.getLength());
        assertEquals(DayOfWeek.FRIDAY, tupleOf.getFirst());
        assertEquals(A.class, tupleOf.getSecond().getClass());
        assertEquals(0, tupleOf.getThird().length);
        assertEquals(AutoA.class, tupleOf.getFourth().getClass());
        assertEquals(AutoB.class, tupleOf.getFifth().getClass());
        assertEquals(new double[]{2.2}, tupleOf.getSixth()[1]);
        assertEquals(null, tupleOf.getSeventh()[1]);
        assertEquals(Short.valueOf("3"), tupleOf.getEighth()[2]);
        assertEquals(new Object[]{1, 'a'}, tupleOf.getNineth());
        assertEquals(0, tupleOf.getTenth()[0].length);

        assertEquals("The 21th", tupleOf.getValue(10));
        assertEquals(22, tupleOf.getValue(11));

        TuplePlus<DayOfWeek, A, Object[][], AutoA, AutoB, double[][], Byte[][], Short[], Comparable[], int[][]> tuplePlus =
                Tuple.create(
                        DayOfWeek.FRIDAY, (A) raw[1], (Object[][]) raw[2], (AutoA) raw[3], (AutoB) raw[4],
                        (double[][]) raw[5], (Byte[][]) raw[6],
                        (Short[]) raw[7], (Comparable[]) raw[8], (int[][]) raw[9],
                        "The 21th", 22
                );

        assertEquals(tupleOf, tuplePlus);

        Tuple tuple2 = Tuple.of(tuplePlus.getFirst(), tuplePlus.getSecond(), tuplePlus.getThird(), tuplePlus.getFourth(), tuplePlus.getFifth()
                , tuplePlus.getSixth(), tuplePlus.getSeventh(), tuplePlus.getEighth(), tuplePlus.getNineth(), tuplePlus.getTenth()
                , tuplePlus.getValue(10), tuplePlus.getValue(11)
        );
        assertEquals(tuple2, tuplePlus);
    }

    @Test
    public void asArray() {
        Tuple<Number> numbers = Tuple.setOf(1, 2.3f, 3.07, 44, null);
        Number[] array = numbers.asArray();
        assertEquals(Number[].class, array.getClass());
    }

    @Test
    public void testCompabilities(){
        Tuple1<Integer> t1 = Tuple.create(1);
        Tuple2<Integer, String> t2 = Tuple.create(1, "OK");
        Tuple3<Integer, String, Boolean> t3 = Tuple.create(1, "OK", true);

//        Tuple1<Integer> t1_3 = t3;      //Tuple2 is not Tuple1

        WithValues1<Integer> values1 = Tuple.create(1);
        WithValues2<Integer, String> values2 = Tuple.create(1, "OK");
        WithValues3<Integer, String, Boolean> values3 = Tuple.create(1, "OK", true);

        WithValues1<Integer> values1_3 = values3;
        WithValues2<Integer, String> values2_3 = values3;
        WithValues1<Integer> values1_2 = values2;
    }

    @Test
    public void getSignatures() {
        TuplePlus<DayOfWeek, A, Object[][], AutoA, AutoB, double[][], Byte[][], Short[], Comparable[], int[][]> tuplePlus =
                Tuple.create(
                        DayOfWeek.FRIDAY, (A) raw[1], (Object[][]) raw[2], (AutoA) raw[3], (AutoB) raw[4],
                        (double[][]) raw[5], (Byte[][]) raw[6],
                        (Short[]) raw[7], (Comparable[]) raw[8], (int[][]) raw[9],
                        "The 21th", 22
                );

        assertEquals(TypeHelper.deepHashCode(tuplePlus.values), tuplePlus.hashCode());

        List<Integer> expectedSignatures = ArrayHelper.asList(
                DayOfWeek.FRIDAY.hashCode(),
                raw[1].hashCode(),
                raw[2].hashCode(),
                raw[3].hashCode(),
                raw[4].hashCode(),
                raw[5].hashCode(),
                raw[6].hashCode(),
                raw[7].hashCode(),
                raw[8].hashCode(),
                raw[9].hashCode(),
                "The 21th".hashCode(),
                22,
                TypeHelper.deepHashCode(tuplePlus.values)
        );

        Set<Integer> actualSignatures = tuplePlus.getSignatures();
        assertAllTrue(expectedSignatures.size() == actualSignatures.size() && expectedSignatures.containsAll(actualSignatures));
    }

    enum Progress {
        Backlog, Study, Working, ReadyForQA, Done;

        private static Progress[] _values = values();

        public static Progress ofIndex(int index) {
            if (index < 0 || index >= _values.length) throw new IndexOutOfBoundsException();
            return _values[index];
        }

        public int toInt() {
            return ordinal();
        }

        public Progress getNext() {
            return _values[(ordinal() + 1) % _values.length];
        }
    }

    class A<T> {
        T value;

        public A(T t) {
            value = t;
        }

        public T getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.format("A(%s)", value == null ? "null" : value.toString());
        }
    }

    class B extends A<Integer> {
        public B(Integer i) {
            super(i);
        }
    }

    public class AutoA implements AutoCloseable {
        @Override
        public void close() throws Exception {
            closeMessages.add(this.getClass().getSimpleName() + ".close()");
        }
    }

    public class AutoB extends AutoA {
    }

    public class AutoC extends AutoA {
    }

    public class AutoD extends AutoA {
    }

    public class AutoE extends AutoA {
    }

    public class AutoF extends AutoA {
    }

    public class AutoG extends AutoA {
    }

    public class AutoH extends AutoA {
    }

    public class E {
    }

}