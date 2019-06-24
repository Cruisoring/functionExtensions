package io.github.cruisoring.utility;

import io.github.cruisoring.TypeHelper;
import io.github.cruisoring.logger.Logger;
import org.junit.Test;

import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.*;

import static io.github.cruisoring.Asserts.*;
import static io.github.cruisoring.TypeHelper.valueEquals;
import static io.github.cruisoring.utility.ArrayHelper.*;

public class ArrayHelperTest {

    private static int arraySize = 100;
    private static final int[] expectedInts = new int[arraySize];
    private static final char[] expectedChars = new char[arraySize];
    private static final String[] expectedStrings = new String[arraySize];

    static {
        for (int i = 0; i < arraySize; i++) {
            expectedInts[i] = i + 100;
            expectedChars[i] = (char) ('a' + i);
            expectedStrings[i] = String.valueOf(i);
        }
    }

    private final int[] paraInts = new int[arraySize];
    private final int[] serialInts = new int[arraySize];
    private final int[] ints = new int[arraySize];

    @Test
    public void testGetComponentType() {
        assertEquals(Object.class, getComponentType(new Object[0]));
        assertEquals(int.class, getComponentType(new int[1]));
        assertEquals(Integer.class, getComponentType(new Integer[3]));
        assertEquals(Character.class, getComponentType(new Character[0]));
        assertEquals(char.class, getComponentType("".toCharArray()));
        assertEquals(List.class, getComponentType(new List[0]));
        assertEquals(Month.class, getComponentType(new Month[3]));
    }

    @Test
    public void testGetNewArray() {
        char[] chars = (char[]) getNewArray(char.class, 2);
        chars[1] = 'x';
        assertEquals(new Object[]{Character.valueOf((char) 0), Character.valueOf('x')}, chars);

        int[] ints = (int[]) getNewArray(int.class, 10);
        assertEquals(10, ints.length);

        Integer[] integers = (Integer[]) getNewArray(Integer.class, 3);
        assertEquals(3, integers.length);

        DayOfWeek[] days = (DayOfWeek[]) getNewArray(DayOfWeek.class, 3);
        assertEquals(3, days.length);

        Character[] characters = new Character[]{'a', 'b'};
        int length = Array.getLength(characters);

        List[] lists = (List[]) getNewArray(List.class, 3);
        lists[0] = new SimpleTypedList();
        lists[2] = new SimpleTypedList();

        Comparable[][] comparablesArray = (Comparable[][]) getNewArray(Comparable[].class, 2);
        comparablesArray[0] = new Integer[]{1, 2};
        comparablesArray[1] = new String[0];
    }

    @Test
    public void testMergeTypedArray() {
        assertEquals(new int[]{1, 2, 3, 4}, merge(new Integer[]{1, 2}, 3, 4));
        assertEquals(new int[]{1, 2}, merge(new Integer[]{1, 2}));
        assertEquals(new int[]{1, 2, 3, 4}, merge(new Integer[]{1, 2}, 3, 4));
        assertEquals(new int[]{1, 2}, merge(new Integer[]{1, 2}));
        assertEquals(new Number[]{1, 2, 3, 4.4}, merge(new Number[]{1, 2}, 3, 4.4));

        assertEquals(new Object[]{1, null}, merge(new Object[]{1}, new Object[]{null}));
        assertEquals(new Object[]{1, null, null}, merge(new Object[]{1}, null, null));
        assertEquals(new Object[]{null, null}, merge(new Object[]{}, null, null));
    }

    @Test
    public void testArrayOf() {
        //when <code>others</code> is emtpy, returns either copy of the first array when it is array,
        // or a new array containing only the first argument when it is not array
        assertEquals(new Integer[]{1}, arrayOf(1));
        assertEquals(new Integer[]{1}, arrayOf(new Integer[]{1}));
        int[] ints = new int[]{1, 2};
        assertAllFalse(ints.equals(arrayOf(ints)));       //verify arrayOf() returns a new array
        assertEquals(new int[]{1, 2, 3}, arrayOf(new int[]{1, 2, 3}));

        //when component type of first and others are identical, retuns a array of the same type containing all their elements
        assertEquals(new Integer[]{1, 2}, arrayOf(1, 2));
        assertEquals(new Integer[]{1, 2}, arrayOf(new Integer[]{1}, 2));
        assertEquals(new Integer[]{1, 2}, arrayOf(new Integer[]{1}, new Integer[]{2}));
        assertEquals(new int[]{1, 2}, arrayOf(new int[]{1}, new int[]{2}));
        assertEquals(new int[]{2}, arrayOf(new int[]{}, new int[]{2}));
        assertEquals(new Integer[]{1, 2, 3}, arrayOf(1, 2, 3));

        //when component type of first and others are equivalent, retuns a array of the object type containing all their elements
        assertEquals(new Integer[]{1, 2, 3}, arrayOf(new int[]{1, 2}, 3));
        assertEquals(new Integer[]{1, 2, 3}, arrayOf(new int[]{1}, 2, 3));
        assertEquals(new Integer[]{1, 2, 3}, arrayOf(new int[]{}, 1, 2, 3));

        //when component type of first is assignable from the second, retuns a array of same type as first containing all their elements
        assertEquals(new Number[]{1, 2, 3}, arrayOf(new Number[]{}, 1, 2, 3));
        assertEquals(new Number[]{1, 2, 3.2f}, arrayOf(new Number[]{}, 1, 2, 3.2f));
        assertEquals(new Number[]{1, 2, 3.2f}, arrayOf(new Number[]{1}, 2, 3.2f));
        assertEquals(new Number[]{1, 2, 3.2f}, arrayOf(new Number[]{1, 2, 3.2f}, new Integer[0]));
        assertEquals(new Number[]{1, 2, 3.2f, 4.4, 5.5d}, arrayOf(new Number[]{1, 2, 3.2f}, new Double[]{4.4, 5.5}));
        Object array = arrayOf(new Comparable[]{'a', "OK"}, new int[]{1, 2, 3});
        assertEquals(new Comparable[]{'a', "OK", 1, 2, 3}, array);
        assertEquals(Comparable[].class, array.getClass());

        array = arrayOf(new Number[]{1, 2.2f, 3.3}, new float[]{4.4f});
        assertEquals(Number[].class, array.getClass());
        assertEquals(new Number[]{1, 2.2f, 3.3, 4.4f}, array);

        array = arrayOf(new Object[]{null, 'a'}, new Number[]{1, 2.2f, 3.3, 4.4f});
        assertEquals(Object[].class, array.getClass());
        assertEquals(new Object[]{null, 'a', 1, 2.2f, 3.3, 4.4f}, array);

        //when component type of second is assignable from the first, retuns a array of same type as second containing all their elements
        array = arrayOf(new int[]{1, 2, 3}, new Comparable[]{'a', "OK"});
        assertEquals(new Comparable[]{1, 2, 3, 'a', "OK"}, array);
        assertEquals(Comparable[].class, array.getClass());

        array = arrayOf(-1.0f, new Number[]{1, 2.2f, 3.3});
        assertEquals(Number[].class, array.getClass());
        assertEquals(new Number[]{-1.0f, 1, 2.2f, 3.3}, array);

        array = arrayOf(new Number[]{1, 2.2f, 3.3, 4.4f}, null, 'a');
        assertEquals(Object[].class, array.getClass());
        assertEquals(new Object[]{1, 2.2f, 3.3, 4.4f, null, 'a'}, array);

        //otherwise, returns either a new Object[] containing all their elements
        array = arrayOf(new int[]{1, 2, 3}, new String[]{null, "OK"});
        assertEquals(new Object[]{1, 2, 3, null, "OK"}, array);
        assertEquals(Object[].class, array.getClass());

        Object list = new SimpleTypedList();
        array = arrayOf(list, new Number[]{1, 2.2f, 3.3});
        assertEquals(Object[].class, array.getClass());
        assertEquals(new Object[]{list, 1, 2.2f, 3.3}, array);

        array = arrayOf(new Object[]{1, 2.2f, 3.3, 4.4f, list}, null, 'a');
        assertEquals(Object[].class, array.getClass());
        assertEquals(new Object[]{1, 2.2f, 3.3, 4.4f, list, null, 'a'}, array);
    }


    @Test
    public void testSetAll() {
        setAll(ints, i -> i + 100);
        assertEquals(expectedInts, ints);

        setAllParallel(paraInts, i -> i + 100);
        assertEquals(expectedInts, paraInts);

        setAllParallel(serialInts, i -> i + 100);
        assertEquals(expectedInts, serialInts);

        char[] newChars = new char[arraySize];
        setAllParallel(newChars, i -> (char) (i + 'a'));
        assertEquals(expectedChars, newChars);

        String[] newStrings = new String[arraySize];
        setAll(newStrings, i -> String.valueOf(i));
        assertEquals(expectedStrings, newStrings);
    }

    @Test
    public void setConvertArray() {
        A[] array = new A[]{new B(), new C()};
        B[] bArray = TypeHelper.convert(array, B[].class);
        assertEquals(2, bArray.length);

        assertAllNull(TypeHelper.convert(array, C[].class));

        Object[] objects = new Object[]{new A(), new B(), new C(), new D()};
        A[] aArray = TypeHelper.convert(objects, A[].class);
        assertEquals(4, aArray.length);
    }

    @Test
    public void testToIntegerArray() {
        int[] ints = new int[]{1, 2, 3};
        Integer[] integers = toObject(ints);
        assertAllNotNull(integers);
        assertEquals(3, integers.length);
        int[] intsBack = toPrimitive(integers);

        integers = toObject(new int[0]);
        assertEquals(0, integers.length);
        ints = null;
        integers = toObject(ints);
        assertAllNull(integers);
    }

    @Test
    public void testToCharacterArray() {
        char[] chars = "abc".toCharArray();
        Character[] characters = toObject(chars);
        assertEquals(3, characters.length);

        characters = toObject(new char[0]);
        assertEquals(0, characters.length);
        chars = null;
        assertEquals(null, toObject(chars));
    }

    @Test
    public void testToByteArray() {
        byte[] bytes = new byte[]{3, 2};
        Byte[] Bytes = toObject(bytes);
        assertEquals(2, Bytes.length);

        assertEquals(0, toObject(new byte[0]).length);
        assertAllNull(toObject(bytes = null));
    }

    @Test
    public void testToBooleanArray() {
        boolean[] bools = new boolean[]{true, false, true};
        Boolean[] booleans = toObject(bools);
        assertEquals(3, booleans.length);

        booleans = toObject(new boolean[0]);
        assertEquals(0, booleans.length);
        bools = null;
        booleans = toObject(bools);
        assertAllNull(booleans);
    }

    @Test
    public void testToShortArray() {
        assertEquals(0, toObject(new short[0]).length);
        assertEquals(1, toObject(new short[]{3}).length);
        short[] shorts = null;
        assertAllNull(toObject(shorts));
    }

    @Test
    public void testToLongArray() {
        assertEquals(0, toObject(new long[0]).length);
        assertEquals(1, toObject(new long[]{3}).length);
        long[] values = null;
        assertAllNull(toObject(values));
    }

    @Test
    public void testToFloatArray() {
        assertEquals(0, toObject(new float[0]).length);
        assertEquals(2, toObject(new float[]{3f, 0.2f}).length);
        float[] values = null;
        assertAllNull(toObject(values));
    }

    @Test
    public void testToDoubleArray() {
        assertEquals(0, toObject(new double[0]).length);
        assertEquals(2, toObject(new double[]{3f, 0.2f}).length);
        double[] values = null;
        assertAllNull(toObject(values));
    }

    @Test
    public void testCreate() {
        assertAllTrue(
                Objects.deepEquals(new int[0], create(int.class, 0, i -> 100)),
                Objects.deepEquals(new int[]{0}, create(int.class, 1, i -> i)),
                Objects.deepEquals(new int[]{100, 99}, create(int.class, 2, i -> 100 - i)),
                Objects.deepEquals(new Integer[]{0, 2, 4}, create(Integer.class, 3, i -> 2 * i)),
                Objects.deepEquals(new String[]{"0", "1", "2", "3", "4"}, create(String.class, 5, i -> i.toString()))
        );
    }

    @Test
    public void testShuffle() {
        Object int0 = shuffle(new int[0]);
        assertAllTrue(int0 instanceof int[]);
        assertEquals(new int[]{1}, shuffle(new int[]{1}));
        Object character2 = getChangedArray(new Character[]{'a', 'b'});
        Object double3 = getChangedArray(new double[]{0.1, 2.2, 3.333});
        Object char5 = getChangedArray(new char[]{'a', 'b', 'c', 'd', 'e'});
        Object integer10 = getChangedArray(new Integer[]{101, 202, 303, 404, 505, 606, 707, 808, 909, 1001});
    }

    Object getChangedArray(Object array) {
        for (int i = 0; i < 100; i++) {
            Object shuffled = shuffle(array);
            if (!valueEquals(shuffled, array)) {
                Logger.D(TypeHelper.deepToString(shuffled));
                return shuffled;
            }
        }
        fail("Failed to get a changed array of %s", TypeHelper.deepToString(array));
        return null;
    }

    @Test
    public void testAsList() {
        assertEquals(new SimpleTypedList(), asList());
        SimpleTypedList expected = new SimpleTypedList();
        expected.add(null);
        assertEquals(expected, asList(null));
        expected.add(null);
        assertEquals(expected, asList(null, null));
        expected.add(1, true);
        assertEquals(expected, asList(null, true, null));
    }

    @Test
    public void testAsHashSet() {
        Set expected = new HashSet();
        assertEquals(expected, SetHelper.asHashSet());
        expected.add(null);
        assertEquals(expected, SetHelper.asHashSet(null));
        assertEquals(expected, SetHelper.asHashSet(null, null));
        expected.add(true);
        assertEquals(expected, SetHelper.asHashSet(null, true, null));
        assertEquals(expected, SetHelper.asHashSet(null, null, true));

        //validate the order might not be kept
        expected.clear();
        expected.add(123);
        expected.add(true);
        expected.add(0);
        expected.add(null);
        assertEquals(expected, SetHelper.asHashSet(null, 0, true, 123));
    }

    @Test
    public void testAsLinkedHashSet() {
        Set expected = new LinkedHashSet();
        assertEquals(expected, SetHelper.asLinkedHashSet());
        expected.add(null);
        assertEquals(expected, SetHelper.asLinkedHashSet(null));
        assertEquals(expected, SetHelper.asLinkedHashSet(null, null));
        expected.add(true);
        assertEquals(expected, SetHelper.asLinkedHashSet(null, true, null));
        assertEquals(expected, SetHelper.asLinkedHashSet(null, null, true));

        //validate the order might not be kept
        expected.clear();
        expected.add(123);
        expected.add(true);
        expected.add(0);
        expected.add(null);
        expected.add(0);
        assertEquals(expected, SetHelper.asLinkedHashSet(123, true, 0, null));
    }


    class A {
    }

    class B extends A {
    }

    class C extends B {
    }

    class D extends A {
    }

    @Test
    public void intsToObjects() {
        assertEquals(new Object[0], ArrayHelper.toObject(new int[0]));
        assertEquals(new Object[]{1, 2, 3}, ArrayHelper.toObject(new int[]{1, 2, 3}));
        int[] values = null;
        assertEquals(null, ArrayHelper.toObject(values));

        Integer[] integers = new Integer[]{1, 2, 3, 4, 5};
        Object[] objects = ArrayHelper.toObjectArray(integers);
        assertEquals(new Object[]{1, 2, 3, 4, 5}, objects);
    }

    @Test
    public void bytesToObjects() {
        assertEquals(new Object[0], ArrayHelper.toObject(new byte[0]));
        assertEquals(new Object[]{(byte) 1, (byte) 2, (byte) 3}, ArrayHelper.toObject(new byte[]{1, 2, 3}));
        byte[] values = null;
        assertEquals(null, ArrayHelper.toObject(values));
    }

    @Test
    public void booleansToObjects() {
        assertEquals(new Object[0], ArrayHelper.toObject(new boolean[0]));
        assertEquals(new Object[]{true, false}, ArrayHelper.toObject(new boolean[]{true, false}));
        boolean[] values = null;
        assertEquals(null, ArrayHelper.toObject(values));
    }

    @Test
    public void charsToObjects() {
        assertEquals(new Object[0], ArrayHelper.toObject(new char[0]));
        assertEquals(new Object[]{'x', 'y'}, ArrayHelper.toObject(new char[]{'x', 'y'}));
        char[] values = null;
        assertEquals(null, ArrayHelper.toObject(values));
    }

    @Test
    public void floatsToObjects() {
        assertEquals(new Object[0], ArrayHelper.toObject(new float[0]));
        assertEquals(new Object[]{1.2f, 2f, -3f}, ArrayHelper.toObject(new float[]{1.2f, 2f, -3f}));
        assertAllFalse(valueEquals(new Object[]{1.2f, 2, -3f}, ArrayHelper.toObject(new float[]{1.2f, 2f, -3f})));
        float[] values = null;
        assertEquals(null, ArrayHelper.toObject(values));
    }

    @Test
    public void doublesToObjects() {
        assertEquals(new Object[0], ArrayHelper.toObject(new double[0]));
        assertEquals(new Object[]{1.3, 2.0, 3.0}, ArrayHelper.toObject(new double[]{1.3, 2.0, 3.0}));
        double[] values = null;
        assertEquals(null, ArrayHelper.toObject(values));
    }

    @Test
    public void shortsToObjects() {
        assertEquals(new Object[0], ArrayHelper.toObject(new short[0]));
        assertEquals(new Object[]{(short) 1, (short) 2, (short) 3}, ArrayHelper.toObject(new short[]{1, 2, 3}));
        short[] values = null;
        assertEquals(null, ArrayHelper.toObject(values));
    }

    @Test
    public void longsToObjects() {
        assertEquals(new Object[0], ArrayHelper.toObject(new long[0]));
        assertEquals(new Object[]{1L, 2L, 3L}, ArrayHelper.toObject(new long[]{1, 2, 3}));
        long[] values = null;
        assertEquals(null, ArrayHelper.toObject(values));
    }

    @Test
    public void BooleansToPrimitive() {
        assertEquals(new boolean[0], ArrayHelper.toPrimitive(new Boolean[0]));
        assertEquals(new boolean[]{true, false}, ArrayHelper.toPrimitive(new Boolean[]{true, false}));
        Boolean[] values = null;
        assertEquals(null, ArrayHelper.toPrimitive(values));

        boolean[] primitives = assertException(() -> ArrayHelper.toPrimitive(new Boolean[]{true, null, false}), NullPointerException.class);
    }
}