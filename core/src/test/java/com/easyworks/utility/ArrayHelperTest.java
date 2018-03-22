package com.easyworks.utility;

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
        assertTrue(Arrays.deepEquals(new Object[]{Character.valueOf((char)0), Character.valueOf('x')}, ArrayHelper.asObjects(chars)));

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
        assertTrue(Arrays.deepEquals(new Integer[]{1,2,3}, (Integer[])ArrayHelper.asArray(new int[]{1,2,3})));
        assertTrue(Arrays.deepEquals(new Integer[]{1,2,3}, (Integer[])ArrayHelper.asArray(new Integer[]{1,2,3})));
        assertTrue(Arrays.deepEquals(new DayOfWeek[]{DayOfWeek.MONDAY}, (DayOfWeek[])ArrayHelper.asArray(new DayOfWeek[]{DayOfWeek.MONDAY})));
        assertTrue(Arrays.deepEquals(new Tuple[]{Tuple.TRUE, Tuple.FALSE}, (Tuple[])ArrayHelper.asArray(new Tuple[]{Tuple.TRUE, Tuple.FALSE})));
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
        assertTrue(Arrays.deepEquals(new Object[0], ArrayHelper.asObjects(new int[0])));
        assertTrue(Arrays.deepEquals(new Object[]{1,2,3}, ArrayHelper.asObjects(new int[]{1,2,3})));
        int[] values = null;
        assertTrue(Arrays.deepEquals(null, ArrayHelper.asObjects(values)));

        Integer[] integers = new Integer[]{1, 2, 3, 4, 5};
        Object[] objects = ArrayHelper.asObjects(integers);
        assertTrue(Arrays.deepEquals(new Object[]{1,2,3,4,5}, objects));
    }

    @Test
    public void bytesToObjects() {
        assertTrue(Arrays.deepEquals(new Object[0], ArrayHelper.asObjects(new byte[0])));
        assertTrue(Arrays.deepEquals(new Object[]{(byte)1,(byte)2,(byte)3}, ArrayHelper.asObjects(new byte[]{1,2,3})));
        byte[] values = null;
        assertTrue(Arrays.deepEquals(null, ArrayHelper.asObjects(values)));
    }

    @Test
    public void booleansToObjects() {
        assertTrue(Arrays.deepEquals(new Object[0], ArrayHelper.asObjects(new boolean[0])));
        assertTrue(Arrays.deepEquals(new Object[]{true, false}, ArrayHelper.asObjects(new boolean[]{true, false})));
        boolean[] values = null;
        assertTrue(Arrays.deepEquals(null, ArrayHelper.asObjects(values)));
    }

    @Test
    public void charsToObjects() {
        assertTrue(Arrays.deepEquals(new Object[0], ArrayHelper.asObjects(new char[0])));
        assertTrue(Arrays.deepEquals(new Object[]{'x', 'y'}, ArrayHelper.asObjects(new char[]{'x', 'y'})));
        char[] values = null;
        assertTrue(Arrays.deepEquals(null, ArrayHelper.asObjects(values)));
    }

    @Test
    public void floatsToObjects() {
        assertTrue(Arrays.deepEquals(new Object[0], ArrayHelper.asObjects(new float[0])));
        assertTrue(Arrays.deepEquals(new Object[]{1.2f,2f,-3f}, ArrayHelper.asObjects(new float[]{1.2f, 2f, -3f})));
        assertFalse(Arrays.deepEquals(new Object[]{1.2f,2,-3f}, ArrayHelper.asObjects(new float[]{1.2f, 2f, -3f})));
        float[] values = null;
        assertTrue(Arrays.deepEquals(null, ArrayHelper.asObjects(values)));
    }

    @Test
    public void doublesToObjects() {
        assertTrue(Arrays.deepEquals(new Object[0], ArrayHelper.asObjects(new double[0])));
        assertTrue(Arrays.deepEquals(new Object[]{1.3,2.0,3.0}, ArrayHelper.asObjects(new double[]{1.3,2.0,3.0})));
        double[] values = null;
        assertTrue(Arrays.deepEquals(null, ArrayHelper.asObjects(values)));
    }

    @Test
    public void shortsToObjects() {
        assertTrue(Arrays.deepEquals(new Object[0], ArrayHelper.asObjects(new short[0])));
        assertTrue(Arrays.deepEquals(new Object[]{(short)1, (short)2, (short)3}, ArrayHelper.asObjects(new short[]{1,2,3})));
        short[] values = null;
        assertTrue(Arrays.deepEquals(null, ArrayHelper.asObjects(values)));
    }

    @Test
    public void longsToObjects() {
        assertTrue(Arrays.deepEquals(new Object[0], ArrayHelper.asObjects(new long[0])));
        assertTrue(Arrays.deepEquals(new Object[]{1L,2L,3L}, ArrayHelper.asObjects(new long[]{1,2,3})));
        long[] values = null;
        assertTrue(Arrays.deepEquals(null, ArrayHelper.asObjects(values)));
    }

    @Test
    public void getParallelSetAll_withPrimitiveTypes_getArraySet() {
        BiConsumerThrowable<Object, Function<Integer, Object>> parallelSetAll;

        parallelSetAll = ArrayHelper.getParallelSetAll(int.class);
        int[] ints = new int[10];
        parallelSetAll.orElse(null).accept(ints, i->i);
        assertTrue(ArrayHelper.deepEquals(new int[]{0,1,2,3,4,5,6,7,8,9}, ints));
    }
}