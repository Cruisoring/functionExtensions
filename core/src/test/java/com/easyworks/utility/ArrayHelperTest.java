package com.easyworks.utility;

import com.easyworks.TypeHelper;
import com.easyworks.function.BiConsumerThrowable;
import com.easyworks.tuple.Tuple;
import org.junit.Test;

import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.*;
import java.util.function.Function;

import static org.junit.Assert.*;

public class ArrayHelperTest {

    private static int arraySize = 100;
    private static final int[] expectedInts = new int[arraySize];
    private static final char[] expectedChars = new char[arraySize];
    private static final String[] expectedStrings = new String[arraySize];

    static {
        for (int i = 0; i < arraySize; i++) {
            expectedInts[i] = i+100;
            expectedChars[i] = (char)('a'+i);
            expectedStrings[i] = String.valueOf(i);
        }
    }

    private final int[] paraInts = new int[arraySize];
    private final int[] serialInts = new int[arraySize];
    private final int[] ints = new int[arraySize];

    private static void assertValueEquals(Object a, Object b){
        Logger.L("%s == %s %s", TypeHelper.deepToString(a), TypeHelper.deepToString(b), "?");
        assertTrue(TypeHelper.valueEquals(a, b));
        assertTrue(TypeHelper.valueEqualsSerial(a, b));
        assertTrue(TypeHelper.valueEqualsParallel(a, b));
    }

    private static void assertDeepEquals(Object a, Object b){
        Logger.L("%s ???? %s", TypeHelper.deepToString(a), TypeHelper.deepToString(b));
        assertTrue(Objects.deepEquals(a, b));
    }

    @Test
    public void getComponentType() {
        assertEquals(Object.class, ArrayHelper.getComponentType(new Object[0]));
        assertEquals(int.class, ArrayHelper.getComponentType(new int[1]));
        assertEquals(Integer.class, ArrayHelper.getComponentType(new Integer[3]));
        assertEquals(Character.class, ArrayHelper.getComponentType(new Character[0]));
        assertEquals(char.class, ArrayHelper.getComponentType("".toCharArray()));
        assertEquals(List.class, ArrayHelper.getComponentType(new List[0]));
        assertEquals(Month.class, ArrayHelper.getComponentType(new Month[3]));
    }

    @Test
    public void getNewArray() {
        char[] chars = (char[]) ArrayHelper.getNewArray(char.class, 2);
        chars[1] = 'x';
        assertTrue(TypeHelper.valueEquals(new Object[]{Character.valueOf((char)0), Character.valueOf('x')}, ArrayHelper.asObjects(chars)));

        int[] ints = (int[])ArrayHelper.getNewArray(int.class, 10);
        assertEquals(10, ints.length);

        Integer[] integers = (Integer[]) ArrayHelper.getNewArray(Integer.class, 3);
        assertEquals(3, integers.length);

        DayOfWeek[] days = (DayOfWeek[]) ArrayHelper.getNewArray(DayOfWeek.class, 3);
        assertEquals(3, days.length);

        Character[] characters = new Character[]{'a', 'b'};
        int length = Array.getLength(characters);

        List[] lists = (List[])ArrayHelper.getNewArray(List.class, 3);
        lists[0] = new ArrayList();
        lists[2] = new ArrayList();
    }

    @Test
    public void mergeTypedArray(){
        assertValueEquals(new int[]{1,2,3,4}, ArrayHelper.mergeTypedArray(new Integer[]{1,2}, 3, 4));
        assertValueEquals(new int[]{1,2}, ArrayHelper.mergeTypedArray(new Integer[]{1,2}));
        assertValueEquals(new int[]{1,2,3,4}, ArrayHelper.mergeTypedArray(new Integer[]{1,2}, new Integer[]{3, 4}));
        assertValueEquals(new int[]{1,2}, ArrayHelper.mergeTypedArray(new Integer[]{1,2}, new Integer[0]));
        assertValueEquals(new Number[]{1,2,3,4.4}, ArrayHelper.mergeTypedArray(new Number[]{1,2}, 3, 4.4));

        assertValueEquals(new Object[]{1, null}, ArrayHelper.mergeTypedArray(new Object[]{1}, new Object[]{null}));
        assertValueEquals(new Object[]{1, null, null}, ArrayHelper.mergeTypedArray(new Object[]{1}, null, null));
        assertValueEquals(new Object[]{null, null}, ArrayHelper.mergeTypedArray(new Object[]{}, null, null));
    }

    @Test
    public void arrayOf(){
        //when <code>others</code> is emtpy, returns either copy of the first array when it is array,
        // or a new array containing only the first argument when it is not array
        assertDeepEquals(new Integer[]{1}, ArrayHelper.arrayOf(1));
        assertDeepEquals(new Integer[]{1}, ArrayHelper.arrayOf(new Integer[]{1}));
        int[] ints = new int[]{1,2};
        assertNotEquals(ints, ArrayHelper.arrayOf(ints));       //verify arrayOf() returns a new array
        assertDeepEquals(new int[]{1,2,3}, ArrayHelper.arrayOf(new int[]{1,2,3}));

        //when component type of first and others are identical, retuns a array of the same type containing all their elements
        assertDeepEquals(new Integer[]{1,2}, ArrayHelper.arrayOf(1,2));
        assertDeepEquals(new Integer[]{1,2}, ArrayHelper.arrayOf(new Integer[]{1},2));
        assertDeepEquals(new Integer[]{1,2}, ArrayHelper.arrayOf(new Integer[]{1}, new Integer[]{2}));
        assertDeepEquals(new int[]{1,2}, ArrayHelper.arrayOf(new int[]{1}, new int[]{2}));
        assertDeepEquals(new int[]{2}, ArrayHelper.arrayOf(new int[]{}, new int[]{2}));
        assertDeepEquals(new Integer[]{1,2,3}, ArrayHelper.arrayOf(1,2,3));

        //when component type of first and others are equivalent, retuns a array of the object type containing all their elements
        assertDeepEquals(new Integer[]{1,2,3}, ArrayHelper.arrayOf(new int[]{1,2}, 3));
        assertDeepEquals(new Integer[]{1,2,3}, ArrayHelper.arrayOf(new int[]{1},2, 3));
        assertDeepEquals(new Integer[]{1,2,3}, ArrayHelper.arrayOf(new int[]{},1, 2, 3));

        //when component type of first is assignable from the second, retuns a array of same type as first containing all their elements
        assertDeepEquals(new Number[]{1,2,3}, ArrayHelper.arrayOf(new Number[]{},1, 2, 3));
        assertDeepEquals(new Number[]{1,2,3.2f}, ArrayHelper.arrayOf(new Number[]{},1, 2, 3.2f));
        assertDeepEquals(new Number[]{1,2,3.2f}, ArrayHelper.arrayOf(new Number[]{1},2, 3.2f));
        assertDeepEquals(new Number[]{1,2,3.2f}, ArrayHelper.arrayOf(new Number[]{1,2, 3.2f},new Integer[0]));
        assertDeepEquals(new Number[]{1,2,3.2f, 4.4, 5.5d}, ArrayHelper.arrayOf(new Number[]{1,2, 3.2f},new Double[]{4.4, 5.5}));
        Object array = ArrayHelper.arrayOf(new Comparable[]{'a', "OK"},new int[]{1, 2, 3});
        assertDeepEquals(new Comparable[]{'a', "OK",1,2,3}, array);
        assertEquals(Comparable[].class, array.getClass());

        array = ArrayHelper.arrayOf(new Number[]{1, 2.2f, 3.3}, new float[]{4.4f});
        assertEquals(Number[].class, array.getClass());
        assertDeepEquals(new Number[]{1, 2.2f, 3.3, 4.4f}, array);

        array = ArrayHelper.arrayOf(new Object[]{null, 'a'}, new Number[]{1, 2.2f, 3.3, 4.4f});
        assertEquals(Object[].class, array.getClass());
        assertDeepEquals(new Object[]{null, 'a', 1, 2.2f, 3.3, 4.4f}, array);

        //when component type of second is assignable from the first, retuns a array of same type as second containing all their elements
        array = ArrayHelper.arrayOf(new int[]{1, 2, 3}, new Comparable[]{'a', "OK"});
        assertDeepEquals(new Comparable[]{1,2,3, 'a', "OK"}, array);
        assertEquals(Comparable[].class, array.getClass());

        array = ArrayHelper.arrayOf(-1.0f, new Number[]{1, 2.2f, 3.3});
        assertEquals(Number[].class, array.getClass());
        assertDeepEquals(new Number[]{-1.0f, 1, 2.2f, 3.3}, array);

        array = ArrayHelper.arrayOf(new Number[]{1, 2.2f, 3.3, 4.4f}, new Object[]{null, 'a'});
        assertEquals(Object[].class, array.getClass());
        assertDeepEquals(new Object[]{1, 2.2f, 3.3, 4.4f, null, 'a'}, array);

        //otherwise, returns either a new Object[] containing all their elements
        array = ArrayHelper.arrayOf(new int[]{1, 2, 3}, new String[]{null, "OK"});
        assertDeepEquals(new Object[]{1,2,3, null, "OK"}, array);
        assertEquals(Object[].class, array.getClass());

        Object list = new ArrayList();
        array = ArrayHelper.arrayOf(list, new Number[]{1, 2.2f, 3.3});
        assertEquals(Object[].class, array.getClass());
        assertDeepEquals(new Object[]{list, 1, 2.2f, 3.3}, array);

        array = ArrayHelper.arrayOf(new Object[]{1, 2.2f, 3.3, 4.4f, list}, new Object[]{null, 'a'});
        assertEquals(Object[].class, array.getClass());
        assertDeepEquals(new Object[]{1, 2.2f, 3.3, 4.4f, list, null, 'a'}, array);
    }


    @Test
    public void setAll(){
        ArrayHelper.setAll(ints, i -> i+100);
        assertDeepEquals(expectedInts, ints);

        ArrayHelper.setAllParallel(paraInts, i -> i+100);
        assertDeepEquals(expectedInts, paraInts);

        ArrayHelper.setAllParallel(serialInts, i -> i+100);
        assertDeepEquals(expectedInts, serialInts);

        char[] newChars = new char[arraySize];
        ArrayHelper.setAllParallel(newChars, i -> (char)(i+'a'));
        assertDeepEquals(expectedChars, newChars);

        String[] newStrings = new String[arraySize];
        ArrayHelper.setAll(newStrings, i -> String.valueOf(i));
        assertDeepEquals(expectedStrings, newStrings);
    }

    @Test
    public void asObjects() {
        int[] ints = new int[]{1,2,3};
        Object[] objects = ArrayHelper.asObjects(ints);
        assertTrue(ArrayHelper.matchArrays(new Object[]{1,2,3}, objects));

        assertTrue(ArrayHelper.matchArrays(new Object[]{1,2,3}, ArrayHelper.asObjects(new Integer[]{1,2,3})));

        DayOfWeek[] days = new DayOfWeek[]{DayOfWeek.FRIDAY, DayOfWeek.MONDAY};
        assertTrue(ArrayHelper.matchArrays(new Object[]{DayOfWeek.FRIDAY, DayOfWeek.MONDAY}, ArrayHelper.asObjects(days)));

        Collection[] collections = new Collection[]{new ArrayList(), new HashSet()};
        objects = ArrayHelper.asObjects(collections);
        assertEquals(ArrayList.class, objects[0].getClass());
        assertEquals(HashSet.class, objects[1].getClass());
    }

    @Test
    public void asArray() {
        assertTrue(TypeHelper.valueEquals(new Integer[]{1,2,3}, (Integer[])ArrayHelper.asArray(new int[]{1,2,3})));
        assertTrue(TypeHelper.valueEquals(new Integer[]{1,2,3}, (Integer[])ArrayHelper.asArray(new Integer[]{1,2,3})));
        assertTrue(TypeHelper.valueEquals(new DayOfWeek[]{DayOfWeek.MONDAY}, (DayOfWeek[])ArrayHelper.asArray(new DayOfWeek[]{DayOfWeek.MONDAY})));
        assertTrue(TypeHelper.valueEquals(new Tuple[]{Tuple.TRUE, Tuple.FALSE}, (Tuple[])ArrayHelper.asArray(new Tuple[]{Tuple.TRUE, Tuple.FALSE})));
    }

    @Test
    public void asPureObject() {
        Object[] complexObjects = new Object[] {null, 23, new int[]{1, 2, 3}, DayOfWeek.FRIDAY,
                new DayOfWeek[]{DayOfWeek.MONDAY, null}, new Collection[]{null, null, null},
                new char[][]{new char[0], new char[]{'a', 'b', 'c'}}};

        Object pureObject = ArrayHelper.asPureObject(complexObjects);
        Object[] objects = (Object[])pureObject;
        assertEquals(7, objects.length);
        assertEquals(null, objects[0]);
        assertEquals(23, objects[1]);
        assertTrue(ArrayHelper.matchArrays(new Object[]{1,2,3}, (Object[])objects[2]));
        assertEquals(DayOfWeek.FRIDAY, objects[3]);
        assertTrue(ArrayHelper.matchArrays(new Object[]{DayOfWeek.MONDAY, null}, (Object[])objects[4]));
        assertTrue(ArrayHelper.matchArrays(new Object[]{null, null, null}, (Object[])objects[5]));
        assertTrue(ArrayHelper.matchArrays(new Object[]{new Object[0], new Object[]{'a', 'b', 'c'}}, (Object[])objects[6]));
    }

    @Test
    public void toArray() {
        Collection<Object> collection = Arrays.asList(3, 'a', null, "String", DayOfWeek.FRIDAY, new char[]{'a'});
        Object[] objects = ArrayHelper.toArray(collection, Object[].class);
        assertEquals(6, objects.length);
        assertNull(ArrayHelper.toArray(collection, Integer[].class));

        collection = Arrays.asList(new int[]{1,2}, new boolean[0], new Character[]{'x'},
                new Tuple[]{Tuple.TRUE, Tuple.UNIT}, new Enum[]{DayOfWeek.FRIDAY, Month.APRIL},
                new Collection[]{new ArrayList(), null});
        objects = ArrayHelper.toArray(collection, Object[].class);
        assertEquals(6, objects.length);


        Collection collection2 = Arrays.asList(new ArrayList(), null, new LinkedList());
        List[] lists = (List[])ArrayHelper.toArray(collection2, List[].class);
        assertEquals(3, lists.length);
    }

    class A{}
    class B extends A{}
    class C extends B{}
    class D extends A{}
    @Test
    public void convertArray() {
        A[] array = new A[]{ new B(), new C()};
        B[] bArray = (B[]) ArrayHelper.mapArray(array, B.class);
        assertEquals(2, bArray.length);

        assertNull(ArrayHelper.mapArray(array, C.class));

        Object[] objects = new Object[]{new A(), new B(), new C(), new D()};
        A[] aArray = (A[])ArrayHelper.mapArray(objects, A.class);
        assertEquals(4, aArray.length);
    }

    @Test
    public void toIntegerArray() {
        int[] ints = new int[]{1, 2, 3};
        Integer[] integers = ArrayHelper.convertArray(ints);
        assertNotNull(integers);
        assertEquals(3, integers.length);
        int[] intsBack = (int[])ArrayHelper.mapArray(integers, int.class);

        integers = ArrayHelper.convertArray(new int[0]);
        assertEquals(0, integers.length);
        ints = null;
        integers = ArrayHelper.convertArray(ints);
        assertNull(integers);
    }


    @Test
    public void toCharacterArray() {
        char[] chars = "abc".toCharArray();
        Character[] characters = ArrayHelper.convertArray(chars);
        assertEquals(3, characters.length);

        characters = ArrayHelper.convertArray(new char[0]);
        assertEquals(0, characters.length);
        chars = null;
        assertEquals(null, ArrayHelper.convertArray(chars));
    }

    @Test
    public void toByteArray() {
        byte[] bytes = new byte[]{3, 2};
        Byte[] Bytes = ArrayHelper.convertArray(bytes);
        assertEquals(2, Bytes.length);

        assertEquals(0, ArrayHelper.convertArray(new byte[0]).length);
        assertNull(ArrayHelper.convertArray(bytes=null));
    }

    @Test
    public void toBooleanArray() {
        boolean[] bools = new boolean[]{true, false, true};
        Boolean[] booleans = ArrayHelper.convertArray(bools);
        assertEquals(3, booleans.length);

        booleans = ArrayHelper.convertArray(new boolean[0]);
        assertEquals(0, booleans.length);
        bools = null;
        booleans = ArrayHelper.convertArray(bools);
        assertNull(booleans);
    }

    @Test
    public void toShortArray() {
        assertEquals(0, ArrayHelper.convertArray(new short[0]).length);
        assertEquals(1, ArrayHelper.convertArray(new short[]{3}).length);
        short[] shorts = null;
        assertNull(ArrayHelper.convertArray(shorts));
    }

    @Test
    public void toLongArray() {
        assertEquals(0, ArrayHelper.convertArray(new long[0]).length);
        assertEquals(1, ArrayHelper.convertArray(new long[]{3}).length);
        long[] values = null;
        assertNull(ArrayHelper.convertArray(values));
    }

    @Test
    public void toFloatArray() {
        assertEquals(0, ArrayHelper.convertArray(new float[0]).length);
        assertEquals(2, ArrayHelper.convertArray(new float[]{3f, 0.2f}).length);
        float[] values = null;
        assertNull(ArrayHelper.convertArray(values));
    }

    @Test
    public void toDoubleArray() {
        assertEquals(0, ArrayHelper.convertArray(new double[0]).length);
        assertEquals(2, ArrayHelper.convertArray(new double[]{3f, 0.2f}).length);
        double[] values = null;
        assertNull(ArrayHelper.convertArray(values));
    }

    @Test
    public void intsToObjects() {
        assertTrue(TypeHelper.valueEquals(new Object[0], ArrayHelper.asObjects(new int[0])));
        assertTrue(TypeHelper.valueEquals(new Object[]{1,2,3}, ArrayHelper.asObjects(new int[]{1,2,3})));
        int[] values = null;
        assertTrue(TypeHelper.valueEquals(null, ArrayHelper.asObjects(values)));

        Integer[] integers = new Integer[]{1, 2, 3, 4, 5};
        Object[] objects = ArrayHelper.asObjects(integers);
        assertTrue(TypeHelper.valueEquals(new Object[]{1,2,3,4,5}, objects));
    }

    @Test
    public void bytesToObjects() {
        assertTrue(TypeHelper.valueEquals(new Object[0], ArrayHelper.asObjects(new byte[0])));
        assertTrue(TypeHelper.valueEquals(new Object[]{(byte)1,(byte)2,(byte)3}, ArrayHelper.asObjects(new byte[]{1,2,3})));
        byte[] values = null;
        assertTrue(TypeHelper.valueEquals(null, ArrayHelper.asObjects(values)));
    }

    @Test
    public void booleansToObjects() {
        assertTrue(TypeHelper.valueEquals(new Object[0], ArrayHelper.asObjects(new boolean[0])));
        assertTrue(TypeHelper.valueEquals(new Object[]{true, false}, ArrayHelper.asObjects(new boolean[]{true, false})));
        boolean[] values = null;
        assertTrue(TypeHelper.valueEquals(null, ArrayHelper.asObjects(values)));
    }

    @Test
    public void charsToObjects() {
        assertTrue(TypeHelper.valueEquals(new Object[0], ArrayHelper.asObjects(new char[0])));
        assertTrue(TypeHelper.valueEquals(new Object[]{'x', 'y'}, ArrayHelper.asObjects(new char[]{'x', 'y'})));
        char[] values = null;
        assertTrue(TypeHelper.valueEquals(null, ArrayHelper.asObjects(values)));
    }

    @Test
    public void floatsToObjects() {
        assertTrue(TypeHelper.valueEquals(new Object[0], ArrayHelper.asObjects(new float[0])));
        assertTrue(TypeHelper.valueEquals(new Object[]{1.2f,2f,-3f}, ArrayHelper.asObjects(new float[]{1.2f, 2f, -3f})));
        assertFalse(TypeHelper.valueEquals(new Object[]{1.2f,2,-3f}, ArrayHelper.asObjects(new float[]{1.2f, 2f, -3f})));
        float[] values = null;
        assertTrue(TypeHelper.valueEquals(null, ArrayHelper.asObjects(values)));
    }

    @Test
    public void doublesToObjects() {
        assertTrue(TypeHelper.valueEquals(new Object[0], ArrayHelper.asObjects(new double[0])));
        assertTrue(TypeHelper.valueEquals(new Object[]{1.3,2.0,3.0}, ArrayHelper.asObjects(new double[]{1.3,2.0,3.0})));
        double[] values = null;
        assertTrue(TypeHelper.valueEquals(null, ArrayHelper.asObjects(values)));
    }

    @Test
    public void shortsToObjects() {
        assertTrue(TypeHelper.valueEquals(new Object[0], ArrayHelper.asObjects(new short[0])));
        assertTrue(TypeHelper.valueEquals(new Object[]{(short)1, (short)2, (short)3}, ArrayHelper.asObjects(new short[]{1,2,3})));
        short[] values = null;
        assertTrue(TypeHelper.valueEquals(null, ArrayHelper.asObjects(values)));
    }

    @Test
    public void longsToObjects() {
        assertTrue(TypeHelper.valueEquals(new Object[0], ArrayHelper.asObjects(new long[0])));
        assertTrue(TypeHelper.valueEquals(new Object[]{1L,2L,3L}, ArrayHelper.asObjects(new long[]{1,2,3})));
        long[] values = null;
        assertTrue(TypeHelper.valueEquals(null, ArrayHelper.asObjects(values)));
    }

    @Test
    public void getParallelSetAll_withPrimitiveTypes_getArraySet() {
        BiConsumerThrowable<Object, Function<Integer, Object>> parallelSetAll;

        parallelSetAll = ArrayHelper.getParallelSetAll(int.class);
        int[] ints = new int[10];
        parallelSetAll.orElse(null).accept(ints, i->i);
        assertTrue(TypeHelper.valueEquals(new int[]{0,1,2,3,4,5,6,7,8,9}, ints));
    }
}