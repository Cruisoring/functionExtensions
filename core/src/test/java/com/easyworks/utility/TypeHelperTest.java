package com.easyworks.utility;

import com.easyworks.function.BiFunctionThrowable;
import com.easyworks.function.FunctionThrowable;
import com.easyworks.function.TriConsumerThrowable;
import com.easyworks.function.TriFunctionThrowable;
import org.junit.Test;

import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class TypeHelperTest {

    @Test
    public void getClassPredicate_withPrimitiveTypes_shallMatchCorrectly() {
        Predicate<Class> classPredicate = TypeHelper.getClassPredicate(int.class);
        assertTrue(classPredicate.test(int.class) && classPredicate.test(Integer.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(int[].class) || classPredicate.test(Integer[].class));

        classPredicate = TypeHelper.getClassPredicate(char.class);
        assertTrue(classPredicate.test(char.class) && classPredicate.test(Character.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(int.class) ||
                classPredicate.test(char[].class) || classPredicate.test(Character[].class));

        classPredicate = TypeHelper.getClassPredicate(boolean.class);
        assertTrue(classPredicate.test(boolean.class) && classPredicate.test(Boolean.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(boolean[].class) || classPredicate.test(Boolean[].class));

        classPredicate = TypeHelper.getClassPredicate(byte.class);
        assertTrue(classPredicate.test(byte.class) && classPredicate.test(Byte.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(byte[].class) || classPredicate.test(Byte[].class));

        classPredicate = TypeHelper.getClassPredicate(double.class);
        assertTrue(classPredicate.test(double.class) && classPredicate.test(Double.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(double[].class) || classPredicate.test(Double[].class));

        classPredicate = TypeHelper.getClassPredicate(float.class);
        assertTrue(classPredicate.test(float.class) && classPredicate.test(Float.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(float[].class) || classPredicate.test(Float[].class));

        classPredicate = TypeHelper.getClassPredicate(short.class);
        assertTrue(classPredicate.test(short.class) && classPredicate.test(Short.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(short[].class) || classPredicate.test(Short[].class));

        classPredicate = TypeHelper.getClassPredicate(long.class);
        assertTrue(classPredicate.test(long.class) && classPredicate.test(Long.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(long[].class) || classPredicate.test(Long[].class));
    }

    @Test
    public void getClassPredicate_withWrapperTypes_shallMatchCorrectly() {
        Predicate<Class> classPredicate = TypeHelper.getClassPredicate(Integer.class);
        assertTrue(classPredicate.test(int.class) && classPredicate.test(Integer.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(int[].class) || classPredicate.test(Integer[].class));

        classPredicate = TypeHelper.getClassPredicate(Character.class);
        assertTrue(classPredicate.test(char.class) && classPredicate.test(Character.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(int.class) ||
                classPredicate.test(char[].class) || classPredicate.test(Character[].class));

        classPredicate = TypeHelper.getClassPredicate(Boolean.class);
        assertTrue(classPredicate.test(boolean.class) && classPredicate.test(Boolean.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(boolean[].class) || classPredicate.test(Boolean[].class));

        classPredicate = TypeHelper.getClassPredicate(Byte.class);
        assertTrue(classPredicate.test(byte.class) && classPredicate.test(Byte.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(byte[].class) || classPredicate.test(Byte[].class));

        classPredicate = TypeHelper.getClassPredicate(Double.class);
        assertTrue(classPredicate.test(double.class) && classPredicate.test(Double.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(double[].class) || classPredicate.test(Double[].class));

        classPredicate = TypeHelper.getClassPredicate(Float.class);
        assertTrue(classPredicate.test(float.class) && classPredicate.test(Float.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(float[].class) || classPredicate.test(Float[].class));

        classPredicate = TypeHelper.getClassPredicate(Short.class);
        assertTrue(classPredicate.test(short.class) && classPredicate.test(Short.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(short[].class) || classPredicate.test(Short[].class));

        classPredicate = TypeHelper.getClassPredicate(Long.class);
        assertTrue(classPredicate.test(long.class) && classPredicate.test(Long.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(long[].class) || classPredicate.test(Long[].class));
    }

    @Test
    public void getClassPredicate_withPrimitiveArraysAndTheirWrappersType_shallMatchCorrectly() {
        Predicate<Class> classPredicate = TypeHelper.getClassPredicate(int[].class);
        assertTrue(classPredicate.test(int[].class) && classPredicate.test(Integer[].class));
        assertFalse(classPredicate.test(null) || classPredicate.test(Object[].class)
                || classPredicate.test(int.class) || classPredicate.test(Integer.class)
                || classPredicate.test(int[][].class) || classPredicate.test(Integer[][].class));

        classPredicate = TypeHelper.getClassPredicate(Integer[].class);
        assertTrue(classPredicate.test(int[].class) && classPredicate.test(Integer[].class));
        assertFalse(classPredicate.test(null) || classPredicate.test(Object[].class)
                || classPredicate.test(int.class) || classPredicate.test(Integer.class)
                || classPredicate.test(int[][].class) || classPredicate.test(Integer[][].class));

        classPredicate = TypeHelper.getClassPredicate(Integer[][].class);
        assertTrue(classPredicate.test(int[][].class) && classPredicate.test(Integer[][].class));
        assertFalse(classPredicate.test(null) || classPredicate.test(Object[][].class)
                || classPredicate.test(int[].class) || classPredicate.test(Integer.class)
                || classPredicate.test(int[][][].class) || classPredicate.test(Integer[][][].class));

    }

    interface ITest1 {}
    interface ITest2 {}
    class A implements ITest1 {}
    class B extends A {}
    class C extends B implements ITest2{}
    class D implements ITest1, ITest2 {}

    @Test
    public void getClassPredicate_withInterfaceOrClassTypes_shallMatchCorrectly() {
        Predicate<Class> classPredicate = TypeHelper.getClassPredicate(ITest1.class);
        assertTrue(classPredicate.test(A.class) && classPredicate.test(B.class)
                && classPredicate.test(C.class) && classPredicate.test(D.class)
                && classPredicate.test(ITest1.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(Object.class));

        classPredicate = TypeHelper.getClassPredicate(ITest2.class);
        assertTrue(classPredicate.test(C.class) && classPredicate.test(D.class)
                && classPredicate.test(ITest2.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(Object.class)
                || classPredicate.test(A.class) || classPredicate.test(B.class));

        classPredicate = TypeHelper.getClassPredicate(A.class);
        assertTrue(classPredicate.test(A.class) && classPredicate.test(B.class)
                && classPredicate.test(C.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(Object.class)
                || classPredicate.test(D.class) || classPredicate.test(ITest1.class) || classPredicate.test(ITest2.class));

        classPredicate = TypeHelper.getClassPredicate(ITest1[].class);
        assertTrue(classPredicate.test(A[].class) && classPredicate.test(B[].class)
                && classPredicate.test(C[].class) && classPredicate.test(D[].class)
                && classPredicate.test(ITest1[].class));
        assertFalse(classPredicate.test(null) || classPredicate.test(Object.class)
                || classPredicate.test(A.class) || classPredicate.test(ITest1.class) || classPredicate.test(ITest2.class));

        classPredicate = TypeHelper.getClassPredicate(ITest2[].class);
        assertTrue(classPredicate.test(C[].class) && classPredicate.test(D[].class)
                && classPredicate.test(ITest2[].class));
        assertFalse(classPredicate.test(null) || classPredicate.test(Object.class)
                || classPredicate.test(Object[].class)
                || classPredicate.test(A[].class) || classPredicate.test(B[].class)
                || classPredicate.test(C.class) || classPredicate.test(D.class));

        classPredicate = TypeHelper.getClassPredicate(A[].class);
        assertTrue(classPredicate.test(A[].class) && classPredicate.test(B[].class)
                && classPredicate.test(C[].class));
        assertFalse(classPredicate.test(null) || classPredicate.test(Object.class)
                || classPredicate.test(Object[].class)
                || classPredicate.test(A.class) || classPredicate.test(B.class) || classPredicate.test(C.class)
                || classPredicate.test(D[].class) || classPredicate.test(ITest1[].class) || classPredicate.test(ITest2[].class));
    }

    private void validateFactory(FunctionThrowable<Integer, Object> arrayFactory, int length, Class clazz) {
        Object array = arrayFactory.orElse(null).apply(length);
        assertEquals(clazz, array.getClass());
        assertEquals(length, Array.getLength(array));
    }

    @Test
    public void getArrayFactory_withPrimitiveTypes_getPrimitiveArrays() {
        FunctionThrowable<Integer, Object> arrayFactory = TypeHelper.getArrayFactory(int.class);
        validateFactory(arrayFactory, 3, int[].class);

        arrayFactory = TypeHelper.getArrayFactory(int[].class);
        validateFactory(arrayFactory, 2, int[][].class);

        arrayFactory = TypeHelper.getArrayFactory(char.class);
        validateFactory(arrayFactory, 0, char[].class);

        arrayFactory = TypeHelper.getArrayFactory(char[].class);
        validateFactory(arrayFactory, 1, char[][].class);

        arrayFactory = TypeHelper.getArrayFactory(byte.class);
        validateFactory(arrayFactory, 2, byte[].class);

        arrayFactory = TypeHelper.getArrayFactory(byte[].class);
        validateFactory(arrayFactory, 0, byte[][].class);

        arrayFactory = TypeHelper.getArrayFactory(boolean.class);
        validateFactory(arrayFactory, 5, boolean[].class);

        arrayFactory = TypeHelper.getArrayFactory(boolean[].class);
        validateFactory(arrayFactory, 1, boolean[][].class);

        arrayFactory = TypeHelper.getArrayFactory(double.class);
        validateFactory(arrayFactory, 3, double[].class);

        arrayFactory = TypeHelper.getArrayFactory(double[].class);
        validateFactory(arrayFactory, 0, double[][].class);

        arrayFactory = TypeHelper.getArrayFactory(float.class);
        validateFactory(arrayFactory, 1, float[].class);

        arrayFactory = TypeHelper.getArrayFactory(float[].class);
        validateFactory(arrayFactory, 0, float[][].class);

        arrayFactory = TypeHelper.getArrayFactory(long.class);
        validateFactory(arrayFactory, 2, long[].class);

        arrayFactory = TypeHelper.getArrayFactory(long[].class);
        validateFactory(arrayFactory, 3, long[][].class);

        arrayFactory = TypeHelper.getArrayFactory(short.class);
        validateFactory(arrayFactory, 1, short[].class);

        arrayFactory = TypeHelper.getArrayFactory(short[].class);
        validateFactory(arrayFactory, 0, short[][].class);
    }

    @Test
    public void getArrayFactory_withWrapperTypes_getWrapperArrays() {
        FunctionThrowable<Integer, Object> arrayFactory = TypeHelper.getArrayFactory(Integer.class);
        validateFactory(arrayFactory, 3, Integer[].class);

        arrayFactory = TypeHelper.getArrayFactory(Integer[].class);
        validateFactory(arrayFactory, 2, Integer[][].class);

        arrayFactory = TypeHelper.getArrayFactory(Character.class);
        validateFactory(arrayFactory, 0, Character[].class);

        arrayFactory = TypeHelper.getArrayFactory(Character[].class);
        validateFactory(arrayFactory, 1, Character[][].class);

        arrayFactory = TypeHelper.getArrayFactory(Byte.class);
        validateFactory(arrayFactory, 2, Byte[].class);

        arrayFactory = TypeHelper.getArrayFactory(Byte[].class);
        validateFactory(arrayFactory, 0, Byte[][].class);

        arrayFactory = TypeHelper.getArrayFactory(Boolean.class);
        validateFactory(arrayFactory, 5, Boolean[].class);

        arrayFactory = TypeHelper.getArrayFactory(Boolean[].class);
        validateFactory(arrayFactory, 1, Boolean[][].class);

        arrayFactory = TypeHelper.getArrayFactory(Double.class);
        validateFactory(arrayFactory, 3, Double[].class);

        arrayFactory = TypeHelper.getArrayFactory(Double[].class);
        validateFactory(arrayFactory, 0, Double[][].class);

        arrayFactory = TypeHelper.getArrayFactory(Float.class);
        validateFactory(arrayFactory, 1, Float[].class);

        arrayFactory = TypeHelper.getArrayFactory(Float[].class);
        validateFactory(arrayFactory, 0, Float[][].class);

        arrayFactory = TypeHelper.getArrayFactory(Long.class);
        validateFactory(arrayFactory, 2, Long[].class);

        arrayFactory = TypeHelper.getArrayFactory(Long[].class);
        validateFactory(arrayFactory, 3, Long[][].class);

        arrayFactory = TypeHelper.getArrayFactory(Short.class);
        validateFactory(arrayFactory, 1, Short[].class);

        arrayFactory = TypeHelper.getArrayFactory(Short[].class);
        validateFactory(arrayFactory, 0, Short[][].class);
    }
    @Test
    public void getArrayFactory_withClassOrInterfaceTypes_getRightArrays() {
        FunctionThrowable<Integer, Object> arrayFactory = TypeHelper.getArrayFactory(ITest1.class);
        validateFactory(arrayFactory, 3, ITest1[].class);

        arrayFactory = TypeHelper.getArrayFactory(ITest1[].class);
        validateFactory(arrayFactory, 2, ITest1[][].class);

        arrayFactory = TypeHelper.getArrayFactory(A.class);
        validateFactory(arrayFactory, 0, A[].class);

        arrayFactory = TypeHelper.getArrayFactory(C[].class);
        validateFactory(arrayFactory, 1, C[][].class);
    }

    @Test
    public void getSetArrayElement_withPrimitiveArray_bothGetterSetterWorks() {
        int[] ints = new int[]{1,2,3};
        BiFunctionThrowable<Object, Integer, Object> getter = TypeHelper.getArrayElementGetter(ints.getClass());
        TriConsumerThrowable<Object, Integer, Object> setter = TypeHelper.getArrayElementSetter(ints.getClass());
        assertEquals(1, getter.orElse(null).apply(ints, 0));
        setter.orElse(null).accept(ints, 2, 33);
        assertEquals(33, ints[2]);
        
        long[] longs = new long[]{1,2L,3};
        getter = TypeHelper.getArrayElementGetter(longs.getClass());
        setter = TypeHelper.getArrayElementSetter(longs.getClass());
        assertEquals(1L, getter.orElse(null).apply(longs, 0));
        setter.orElse(null).accept(longs, 2, 33L);
        assertEquals(33L, longs[2]);
        
        short[] shorts = new short[]{1,2,3};
        getter = TypeHelper.getArrayElementGetter(shorts.getClass());
        setter = TypeHelper.getArrayElementSetter(shorts.getClass());
        assertEquals((short)1, getter.orElse(null).apply(shorts, 0));
        setter.orElse(null).accept(shorts, 2, (short)33);
        assertEquals((short)33, shorts[2]);
        
        char[] chars = new char[]{'a', 'b', 'c'};
        getter = TypeHelper.getArrayElementGetter(chars.getClass());
        setter = TypeHelper.getArrayElementSetter(chars.getClass());
        assertEquals('a', getter.orElse(null).apply(chars, 0));
        setter.orElse(null).accept(chars, 2, 'x');
        assertEquals('x', chars[2]);
        
        byte[] bytes = new byte[]{44, 45, 46};
        getter = TypeHelper.getArrayElementGetter(bytes.getClass());
        setter = TypeHelper.getArrayElementSetter(bytes.getClass());
        assertEquals((byte)44, getter.orElse(null).apply(bytes, 0));
        setter.orElse(null).accept(bytes, 2, (byte)55);
        assertEquals((byte)55, bytes[2]);
        
        boolean[] booleans = new boolean[]{true, false};
        getter = TypeHelper.getArrayElementGetter(booleans.getClass());
        setter = TypeHelper.getArrayElementSetter(booleans.getClass());
        assertEquals(true, getter.orElse(null).apply(booleans, 0));
        setter.orElse(null).accept(booleans, 1, true);
        assertEquals(true, booleans[1]);
        
        double[] doubles = new double[]{1.0, 2.0, 3.0};
        getter = TypeHelper.getArrayElementGetter(doubles.getClass());
        setter = TypeHelper.getArrayElementSetter(doubles.getClass());
        assertEquals(1.0, getter.orElse(null).apply(doubles, 0));
        setter.orElse(null).accept(doubles, 1, 100);
        assertEquals(100.0, doubles[1], 0);
        
        float[] floats = new float[]{1.1f, 2.2f, 3.3f};
        getter = TypeHelper.getArrayElementGetter(floats.getClass());
        setter = TypeHelper.getArrayElementSetter(floats.getClass());
        assertEquals(1.1f, getter.orElse(null).apply(floats, 0));
        setter.orElse(null).accept(floats, 1, 101.3f);
        assertEquals(101.3f, floats[1], 0);
    }

    @Test
    public void getSetArrayElement_withWrapperArray_bothGetterSetterWorks() {
        Integer[] ints = new Integer[]{1,2,3};
        BiFunctionThrowable<Object, Integer, Object> getter = TypeHelper.getArrayElementGetter(ints.getClass());
        TriConsumerThrowable<Object, Integer, Object> setter = TypeHelper.getArrayElementSetter(ints.getClass());
        assertEquals(1, getter.orElse(null).apply(ints, 0));
        setter.orElse(null).accept(ints, 2, 33);
        assertEquals(Integer.valueOf(33), ints[2]);
        
        Long[] longs = new Long[]{1L,2L,3L};
        getter = TypeHelper.getArrayElementGetter(longs.getClass());
        setter = TypeHelper.getArrayElementSetter(longs.getClass());
        assertEquals(1L, getter.orElse(null).apply(longs, 0));
        setter.orElse(null).accept(longs, 2, 33L);
        assertEquals(Long.valueOf(33), longs[2]);
        
        Short[] shorts = new Short[]{1,2,3};
        getter = TypeHelper.getArrayElementGetter(shorts.getClass());
        setter = TypeHelper.getArrayElementSetter(shorts.getClass());
        assertEquals(Short.valueOf((short)1), getter.orElse(null).apply(shorts, 0));
        setter.orElse(null).accept(shorts, 2, Short.valueOf((short)45));
        assertEquals(Short.valueOf((short)45), shorts[2]);
        
        Character[] chars = new Character[]{'a', 'b', 'c'};
        getter = TypeHelper.getArrayElementGetter(chars.getClass());
        setter = TypeHelper.getArrayElementSetter(chars.getClass());
        assertEquals(Character.valueOf('a'), getter.orElse(null).apply(chars, 0));
        setter.orElse(null).accept(chars, 2, 'x');
        assertEquals(Character.valueOf('x'), chars[2]);
        
        Byte[] bytes = new Byte[]{44, 45, 46};
        getter = TypeHelper.getArrayElementGetter(bytes.getClass());
        setter = TypeHelper.getArrayElementSetter(bytes.getClass());
        assertEquals(Byte.valueOf("44"), getter.orElse(null).apply(bytes, 0));
        setter.orElse(null).accept(bytes, 2, (byte)55);
        assertEquals(Byte.valueOf("55"), bytes[2]);
        
        Boolean[] booleans = new Boolean[]{true, false};
        getter = TypeHelper.getArrayElementGetter(booleans.getClass());
        setter = TypeHelper.getArrayElementSetter(booleans.getClass());
        assertEquals(true, getter.orElse(null).apply(booleans, 0));
        setter.orElse(null).accept(booleans, 1, true);
        assertEquals(true, booleans[1]);
        
        Double[] doubles = new Double[]{1.0, 2.0, 3.0};
        getter = TypeHelper.getArrayElementGetter(doubles.getClass());
        setter = TypeHelper.getArrayElementSetter(doubles.getClass());
        assertEquals(1.0, getter.orElse(null).apply(doubles, 0));
        setter.orElse(null).accept(doubles, 1, 100.0);
        assertEquals(100.0, doubles[1], 0);
        
        Float[] floats = new Float[]{1.1f, 2.2f, 3.3f};
        getter = TypeHelper.getArrayElementGetter(floats.getClass());
        setter = TypeHelper.getArrayElementSetter(floats.getClass());
        assertEquals(1.1f, getter.orElse(null).apply(floats, 0));
        setter.orElse(null).accept(floats, 1, 101.3f);
        assertEquals(101.3f, floats[1], 0);
    }

    @Test
    public void getSetArrayElement_withClassOrInterfaceType_bothGetterSetterWorks() {
        ITest1[] interfaces1 = new ITest1[]{null, new A(), new B(), new C(), new D()};
        BiFunctionThrowable<Object, Integer, Object> getter = TypeHelper.getArrayElementGetter(interfaces1.getClass());
        TriConsumerThrowable<Object, Integer, Object> setter = TypeHelper.getArrayElementSetter(interfaces1.getClass());
        assertEquals(null, getter.orElse(null).apply(interfaces1, 0));
        assertEquals(A.class, getter.orElse(null).apply(interfaces1, 1).getClass());
        assertEquals(D.class, getter.orElse(null).apply(interfaces1, 4).getClass());
        setter.orElse(null).accept(interfaces1, 0, new D());
        assertEquals(D.class, getter.orElse(null).apply(interfaces1, 0).getClass());

        A[] aArray = new A[] { new A(), new B(), new C()};
        getter = TypeHelper.getArrayElementGetter(aArray.getClass());
        setter = TypeHelper.getArrayElementSetter(aArray.getClass());
        assertEquals(A.class, getter.orElse(null).apply(aArray, 0).getClass());
        assertEquals(B.class, getter.orElse(null).apply(aArray, 1).getClass());
        //Invalid operations returns null
        assertEquals(null, getter.orElse(null).apply(aArray, -1));
        assertEquals(null, getter.orElse(null).apply(aArray, 5));
        //Invalid setting operation would not update the element
        setter.orElse(null).accept(aArray, 1, new D());
        assertEquals(B.class, getter.orElse(null).apply(aArray, 1).getClass());

        //set element@1 to new C()
        setter.orElse(null).accept(aArray, 1, new C());
        assertEquals(C.class, getter.orElse(null).apply(aArray, 1).getClass());
    }

    private void assertCopying(Object expected, TriFunctionThrowable<Object, Integer, Integer, Object> copier, Object original, int from, int to){
        Object copy = copier.orElse(null).apply(original, from, to);
        if(expected == null){
            assertNull(copy);
            return;
        }

        assertEquals(expected.getClass(), copy.getClass());
        assertTrue(ArrayHelper.deepEquals(expected, copy));
    }

    @Test
    public void getArrayRangeCopier_withPrimitiveTypes_getCorrectCopies() {
        TriFunctionThrowable<Object, Integer, Integer, Object> copier;

        int[] ints = new int[]{1,2,3,4};
        copier = TypeHelper.getArrayRangeCopier(ints.getClass().getComponentType());
        assertCopying(new int[]{2,3}, copier, ints, 1, 3);

        long[] longs = new long[]{1,2,3,4,5};
        copier = TypeHelper.getArrayRangeCopier(longs.getClass().getComponentType());
        assertCopying(new long[]{4,5}, copier, longs, 3, 5);

        short[] shorts = new short[]{1,2,3,4};
        copier = TypeHelper.getArrayRangeCopier(shorts.getClass().getComponentType());
        assertCopying(null, copier, shorts, 5, 6);

        char[] chars = new char[]{1,2,3,4};
        copier = TypeHelper.getArrayRangeCopier(chars.getClass().getComponentType());
        assertCopying(new char[]{2,3}, copier, chars, 1, 3);

        boolean[] booleans = new boolean[]{true, false, false, true, true};
        copier = TypeHelper.getArrayRangeCopier(booleans.getClass().getComponentType());
        assertCopying(new boolean[]{true, false, false, true, true}, copier, booleans, 0, 5);

        byte[] bytes = new byte[]{1,2,3,4};
        copier = TypeHelper.getArrayRangeCopier(bytes.getClass().getComponentType());
        assertCopying(new byte[]{2}, copier, bytes, 1, 2);

        float[] floats = new float[]{1f,2f,3f,4f};
        copier = TypeHelper.getArrayRangeCopier(floats.getClass().getComponentType());
        assertCopying(new float[]{4f}, copier, floats, 3, 4);

        double[] doubles = new double[]{1d,2d,3d,4d};
        copier = TypeHelper.getArrayRangeCopier(doubles.getClass().getComponentType());
        //Be cautious when the <tt>to</tt> is more then the length of the original array, default values would be kep
        assertCopying(new double[]{0d}, copier, doubles, 4, 5);
    }

    @Test
    public void getArrayRangeCopier_withWrapperTypes_getCorrectCopies() {
        TriFunctionThrowable<Object, Integer, Integer, Object> copier;

        Integer[] ints = new Integer[]{1,2,3,4};
        copier = TypeHelper.getArrayRangeCopier(ints.getClass().getComponentType());
        assertCopying(new Integer[]{2,3}, copier, ints, 1, 3);

        Long[] longs = new Long[]{1L,2L,3L,4L,5L};
        copier = TypeHelper.getArrayRangeCopier(longs.getClass().getComponentType());
        //Be cautious when the <tt>to</tt> is more then the length of the original array, default values would be kep
        assertCopying(new Long[]{4L,5L, null, null}, copier, longs, 3, 7);

        Short[] shorts = new Short[]{1,2,3,4};
        copier = TypeHelper.getArrayRangeCopier(shorts.getClass().getComponentType());
        assertCopying(null, copier, shorts, 3, 2);

        Character[] chars = new Character[]{'a', 'b', 'c', 'd'};
        copier = TypeHelper.getArrayRangeCopier(chars.getClass().getComponentType());
        assertCopying(new Character[]{'b', 'c'}, copier, chars, 1, 3);

        Boolean[] booleans = new Boolean[]{true, false, false, true, true};
        copier = TypeHelper.getArrayRangeCopier(booleans.getClass().getComponentType());
        assertCopying(new Boolean[]{true}, copier, booleans, 4, 5);

        Byte[] bytes = new Byte[]{1,2,3,4};
        copier = TypeHelper.getArrayRangeCopier(bytes.getClass().getComponentType());
        assertCopying(new Byte[]{2}, copier, bytes, 1, 2);

        Float[] floats = new Float[]{1f,2f,3f,4f};
        copier = TypeHelper.getArrayRangeCopier(floats.getClass().getComponentType());
        assertCopying(new Float[]{4f}, copier, floats, 3, 4);

        double[] doubles = new double[]{1d,2d,3d,4d};
        copier = TypeHelper.getArrayRangeCopier(doubles.getClass().getComponentType());
        assertCopying(new double[]{}, copier, doubles, 1, 1);
    }

    @Test
    public void getArrayRangeCopier_withClassOrInterfaceTypes_getCorrectCopies() {
        TriFunctionThrowable<Object, Integer, Integer, Object> copier;

        Comparable[] comparables = new Comparable[]{1,'b',3L,4f,5d,"6s"};
        copier = TypeHelper.getArrayRangeCopier(comparables.getClass().getComponentType());
        assertCopying(new Comparable[]{1,'b',3L,4f,5d,"6s"}, copier, comparables, 0, 6);
        assertCopying(new Comparable[]{'b',3L,4f,5d,"6s"}, copier, comparables, 1, 6);
        assertCopying(new Comparable[]{}, copier, comparables, 1, 1);
        assertCopying(new Comparable[]{1,'b',3L,4f,5d}, copier, comparables, 0, 5);
        assertCopying(null, copier, comparables, 3, 2);

        Object[] objects = new Object[]{1,'b',3L,4f,5d,"6s"};
        copier = TypeHelper.getArrayRangeCopier(objects.getClass().getComponentType());
        assertCopying(new Object[]{1,'b',3L,4f,5d,"6s"}, copier, objects, 0, 6);
        assertCopying(new Object[]{'b',3L,4f,5d,"6s"}, copier, objects, 1, 6);
        assertCopying(new Object[]{}, copier, objects, 5, 5);
        assertCopying(new Object[]{1,'b',3L,4f,5d}, copier, objects, 0, 5);

        ITest1[] tests = new ITest1[]{new A(), new B(), new C(), new D()};
        copier = TypeHelper.getArrayRangeCopier(objects.getClass().getComponentType());
        assertCopying(new ITest1[]{tests[0], tests[1], tests[2], tests[3]}, copier, tests, 0, 4);
        assertCopying(new ITest1[]{tests[3]}, copier, tests, 3, 4);
        assertCopying(new ITest1[]{tests[1], tests[2], tests[3], null, null}, copier, tests, 1, 6);
        assertCopying(new ITest1[]{tests[2], tests[3], null}, copier, tests, 2, 5);
        assertCopying(null, copier, tests, 3, 2);
    }

    private void assertToString(String expected, Function<Object, String> toString, Object array){
        String arrayString = toString.apply(array);

        assertEquals(expected, arrayString);
    }

    @Test
    public void getArrayToString_withPrimitiveTypes_getExpectedString() {
        Function<Object, String> toString;

        int[] ints = new int[]{1, 2, 3};
        toString = TypeHelper.getArrayToString(ints.getClass().getComponentType());
        assertToString("[1, 2, 3]", toString, ints);

        short[] shorts = new short[]{1, 2, 3};
        toString = TypeHelper.getArrayToString(shorts.getClass().getComponentType());
        assertToString("[1, 2, 3]", toString, shorts);

        long[] longs = new long[]{1, 2, 3};
        toString = TypeHelper.getArrayToString(longs.getClass().getComponentType());
        assertToString("[1, 2, 3]", toString, longs);

        double[] doubles = new double[]{1, 2, 3};
        toString = TypeHelper.getArrayToString(doubles.getClass().getComponentType());
        assertToString("[1.0, 2.0, 3.0]", toString, doubles);

        float[] floats = new float[]{1, 2, 3};
        toString = TypeHelper.getArrayToString(floats.getClass().getComponentType());
        assertToString("[1.0, 2.0, 3.0]", toString, floats);

        char[] chars = new char[]{'a', 'b', 'c'};
        toString = TypeHelper.getArrayToString(chars.getClass().getComponentType());
        assertToString("[a, b, c]", toString, chars);

        byte[] bytes = new byte[]{'a', 'b', 'c'};
        toString = TypeHelper.getArrayToString(bytes.getClass().getComponentType());
        assertToString("[97, 98, 99]", toString, bytes);

        boolean[] booleans = new boolean[]{true, false, true};
        toString = TypeHelper.getArrayToString(booleans.getClass().getComponentType());
        assertToString("[true, false, true]", toString, booleans);
    }

    @Test
    public void getArrayToString_withWrapperTypes_getExpectedString() {
        Function<Object, String> toString;

        Integer[] ints = new Integer[]{1, 2, 3};
        toString = TypeHelper.getArrayToString(ints.getClass().getComponentType());
        assertToString("[1, 2, 3]", toString, ints);

        Short[] shorts = new Short[]{1, 2, 3};
        toString = TypeHelper.getArrayToString(shorts.getClass().getComponentType());
        assertToString("[1, 2, 3]", toString, shorts);

        Long[] longs = new Long[]{1L, 2L, 3L};
        toString = TypeHelper.getArrayToString(longs.getClass().getComponentType());
        assertToString("[1, 2, 3]", toString, longs);

        Double[] doubles = new Double[]{1d, 2d, 3d, null};
        toString = TypeHelper.getArrayToString(doubles.getClass().getComponentType());
        assertToString("[1.0, 2.0, 3.0, null]", toString, doubles);

        Float[] floats = new Float[]{null, 1f, 2f, 3f};
        toString = TypeHelper.getArrayToString(floats.getClass().getComponentType());
        assertToString("[null, 1.0, 2.0, 3.0]", toString, floats);

        Character[] chars = new Character[]{'a', 'b', 'c'};
        toString = TypeHelper.getArrayToString(chars.getClass().getComponentType());
        assertToString("[a, b, c]", toString, chars);

        Byte[] bytes = new Byte[]{'a', 'b', 'c'};
        toString = TypeHelper.getArrayToString(bytes.getClass().getComponentType());
        assertToString("[97, 98, 99]", toString, bytes);

        Boolean[] booleans = new Boolean[]{true, false, true};
        toString = TypeHelper.getArrayToString(booleans.getClass().getComponentType());
        assertToString("[true, false, true]", toString, booleans);
    }

    @Test
    public void getArrayToString_withClassOrInterfaceTypes_getExpectedString() {
        Function<Object, String> toString;

        Comparable[] comparables = new Comparable[]{1, 2L, null, "A string", 3, true};
        toString = TypeHelper.getArrayToString(comparables.getClass().getComponentType());
        assertToString("[1, 2, null, A string, 3, true]", toString, comparables);

        Object[] objects = new Object[]{DayOfWeek.FRIDAY, 2.0001f, 3E2, new char[]{'a', 'b'}, new Integer[]{null, -1}};
        toString = TypeHelper.getArrayToString(objects.getClass().getComponentType());
        assertToString("[FRIDAY, 2.0001, 300.0, [a, b], [null, -1]]", toString, objects);

        String[] strings = new String[]{"", " \t", null, "a"};
        toString = TypeHelper.getArrayToString(strings.getClass().getComponentType());
        assertToString("[,  \t, null, a]", toString, strings);
    }

    @Test
    public void getArrayToString_withArrayOfArrayTypes_getExpectedString() {
        Function<Object, String> toString;

        Comparable[][] comparables = new Comparable[][]{new Integer[]{1,2}, new String[]{null, "str"}, null, new Comparable[]{"A string", 3, true}};
        toString = TypeHelper.getArrayToString(comparables.getClass().getComponentType());
        assertToString("[[1, 2], [null, str], null, [A string, 3, true]]", toString, comparables);

        Object[][] objects = new Object[][]{new Object[]{DayOfWeek.FRIDAY, 2.0001f, 3E2}, new Character[]{'a', 'b'},
                new Integer[]{null, -1}, new Boolean[]{true, false, null}};
        toString = TypeHelper.getArrayToString(objects.getClass().getComponentType());
        assertToString("[[FRIDAY, 2.0001, 300.0], [a, b], [null, -1], [true, false, null]]", toString, objects);

        String[][] strings = new String[][]{null, new String[]{"", " \t", null, "a"}, new String[]{}};
        toString = TypeHelper.getArrayToString(strings.getClass().getComponentType());
        assertToString("[null, [,  \t, null, a], []]", toString, strings);
    }
}