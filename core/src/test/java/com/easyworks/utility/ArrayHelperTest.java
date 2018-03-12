package com.easyworks.utility;

import org.junit.Test;

import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ArrayHelperTest {

    @Test
    public void getArrayTest(){
        char[] chars = (char[]) ArrayHelper.getArray(char.class, 2);
        chars[1] = 'x';
        int l = Array.getLength(chars);
        assertEquals(2, chars.length);

        int[] ints = (int[])ArrayHelper.getArray(int.class, 10);
        assertEquals(10, ints.length);

        Integer[] integers = (Integer[]) ArrayHelper.getArray(Integer.class, 3);
        assertEquals(3, integers.length);

        DayOfWeek[] days = (DayOfWeek[]) ArrayHelper.getArray(DayOfWeek.class, 3);
        assertEquals(3, days.length);

        Character[] characters = new Character[]{'a', 'b'};
        int length = Array.getLength(characters);

//        List<Character> list = Arrays.asList('a', 'b', 'c', 'd');
//        length = Array.getLength(list);
        Character ch = Array.getChar(chars, 0);

        boolean isAssignable = Character.class.isAssignableFrom(char.class);
        isAssignable = char.class.isAssignableFrom(Character.class);
        isAssignable = char.class.isAssignableFrom(Object.class);
        isAssignable = Object.class.isAssignableFrom(char.class);

    }

    @Test
    public void toIntegerArray() {
        int[] ints = new int[]{1, 2, 3};
        Integer[] integers = ArrayHelper.toArray(ints);
        assertNotNull(integers);
        assertEquals(3, integers.length);

        integers = ArrayHelper.toArray(new int[0]);
        assertEquals(0, integers.length);
        ints = null;
        integers = ArrayHelper.toArray(ints);
        assertNull(integers);
    }

    @Test
    public void toCharacterArray() {
        char[] chars = "abc".toCharArray();
        Character[] characters = ArrayHelper.toArray(chars);
        assertEquals(3, characters.length);

        characters = ArrayHelper.toArray(new char[0]);
        assertEquals(0, characters.length);
        chars = null;
        assertEquals(null, ArrayHelper.toArray(chars));
    }

    @Test
    public void toByteArray() {
        byte[] bytes = new byte[]{3, 2};
        Byte[] Bytes = ArrayHelper.toArray(bytes);
        assertEquals(2, Bytes.length);

        assertEquals(0, ArrayHelper.toArray(new byte[0]).length);
        assertNull(ArrayHelper.toArray(bytes=null));
    }

    @Test
    public void toBooleanArray() {
        boolean[] bools = new boolean[]{true, false, true};
        Boolean[] booleans = ArrayHelper.toArray(bools);
        assertEquals(3, booleans.length);

        booleans = ArrayHelper.toArray(new boolean[0]);
        assertEquals(0, booleans.length);
        bools = null;
        booleans = ArrayHelper.toArray(bools);
        assertNull(booleans);
    }

    @Test
    public void toShortArray() {
        assertEquals(0, ArrayHelper.toArray(new short[0]).length);
        assertEquals(1, ArrayHelper.toArray(new short[]{3}).length);
        short[] shorts = null;
        assertNull(ArrayHelper.toArray(shorts));
    }

    @Test
    public void toLongArray() {
        assertEquals(0, ArrayHelper.toArray(new long[0]).length);
        assertEquals(1, ArrayHelper.toArray(new long[]{3}).length);
        long[] values = null;
        assertNull(ArrayHelper.toArray(values));
    }

    @Test
    public void toFloatArray() {
        assertEquals(0, ArrayHelper.toArray(new float[0]).length);
        assertEquals(2, ArrayHelper.toArray(new float[]{3f, 0.2f}).length);
        float[] values = null;
        assertNull(ArrayHelper.toArray(values));
    }

    @Test
    public void toDoubleArray() {
        assertEquals(0, ArrayHelper.toArray(new double[0]).length);
        assertEquals(2, ArrayHelper.toArray(new double[]{3f, 0.2f}).length);
        double[] values = null;
        assertNull(ArrayHelper.toArray(values));
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
    public void matchInOrder() {
    }

    @Test
    public void matchInOrder1() {
    }

    @Test
    public void matchWithoutOrder() {
    }

    @Test
    public void matchWithoutOrder1() {
    }
}