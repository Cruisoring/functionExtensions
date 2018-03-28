package com.easyworks.utility;

import com.easyworks.function.*;
import com.easyworks.tuple.Tuple3;
import com.easyworks.tuple.Tuple;
import org.junit.Test;

import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.easyworks.utility.TypeHelper.*;
import static org.junit.Assert.*;

public class TypeHelperTest {


    @Test
    public void getGenericInfo(){
        FunctionThrowable<Integer, List<Integer>> listFactory = i -> new ArrayList<Integer>();
        Tuple3<Boolean, Class[], Class> genericInfo = TypeHelper.lambdaGenericInfoRepository.retrieve(listFactory);
        assertEquals(Tuple.create(true, new Class[]{Integer.class}, List.class), genericInfo);

        BiFunctionThrowable<Integer, Integer, Boolean> func = (n1, n2) -> n1 + 12 > n2;
        genericInfo = TypeHelper.lambdaGenericInfoRepository.retrieve(func);
        assertEquals(Tuple.create(true, new Class[]{Integer.class, Integer.class}, Boolean.class), genericInfo);
    }

    @Test
    public void getClassPredicate_withPrimitiveTypes_shallMatchCorrectly() {
        Predicate<Class> classPredicate = getClassPredicate(int.class);
        assertTrue(classPredicate.test(int.class) && classPredicate.test(Integer.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(int[].class) || classPredicate.test(Integer[].class));

        classPredicate = getClassPredicate(char.class);
        assertTrue(classPredicate.test(char.class) && classPredicate.test(Character.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(int.class) ||
                classPredicate.test(char[].class) || classPredicate.test(Character[].class));

        classPredicate = getClassPredicate(boolean.class);
        assertTrue(classPredicate.test(boolean.class) && classPredicate.test(Boolean.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(boolean[].class) || classPredicate.test(Boolean[].class));

        classPredicate = getClassPredicate(byte.class);
        assertTrue(classPredicate.test(byte.class) && classPredicate.test(Byte.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(byte[].class) || classPredicate.test(Byte[].class));

        classPredicate = getClassPredicate(double.class);
        assertTrue(classPredicate.test(double.class) && classPredicate.test(Double.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(double[].class) || classPredicate.test(Double[].class));

        classPredicate = getClassPredicate(float.class);
        assertTrue(classPredicate.test(float.class) && classPredicate.test(Float.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(float[].class) || classPredicate.test(Float[].class));

        classPredicate = getClassPredicate(short.class);
        assertTrue(classPredicate.test(short.class) && classPredicate.test(Short.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(short[].class) || classPredicate.test(Short[].class));

        classPredicate = getClassPredicate(long.class);
        assertTrue(classPredicate.test(long.class) && classPredicate.test(Long.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(long[].class) || classPredicate.test(Long[].class));
    }

    @Test
    public void getClassPredicate_withWrapperTypes_shallMatchCorrectly() {
        Predicate<Class> classPredicate = getClassPredicate(Integer.class);
        assertTrue(classPredicate.test(int.class) && classPredicate.test(Integer.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(int[].class) || classPredicate.test(Integer[].class));

        classPredicate = getClassPredicate(Character.class);
        assertTrue(classPredicate.test(char.class) && classPredicate.test(Character.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(int.class) ||
                classPredicate.test(char[].class) || classPredicate.test(Character[].class));

        classPredicate = getClassPredicate(Boolean.class);
        assertTrue(classPredicate.test(boolean.class) && classPredicate.test(Boolean.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(boolean[].class) || classPredicate.test(Boolean[].class));

        classPredicate = getClassPredicate(Byte.class);
        assertTrue(classPredicate.test(byte.class) && classPredicate.test(Byte.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(byte[].class) || classPredicate.test(Byte[].class));

        classPredicate = getClassPredicate(Double.class);
        assertTrue(classPredicate.test(double.class) && classPredicate.test(Double.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(double[].class) || classPredicate.test(Double[].class));

        classPredicate = getClassPredicate(Float.class);
        assertTrue(classPredicate.test(float.class) && classPredicate.test(Float.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(float[].class) || classPredicate.test(Float[].class));

        classPredicate = getClassPredicate(Short.class);
        assertTrue(classPredicate.test(short.class) && classPredicate.test(Short.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(short[].class) || classPredicate.test(Short[].class));

        classPredicate = getClassPredicate(Long.class);
        assertTrue(classPredicate.test(long.class) && classPredicate.test(Long.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(long[].class) || classPredicate.test(Long[].class));
    }

    @Test
    public void getClassPredicate_withPrimitiveArraysAndTheirWrappersType_shallMatchCorrectly() {
        Predicate<Class> classPredicate = getClassPredicate(int[].class);
        assertTrue(classPredicate.test(int[].class) && classPredicate.test(Integer[].class));
        assertFalse(classPredicate.test(null) || classPredicate.test(Object[].class)
                || classPredicate.test(int.class) || classPredicate.test(Integer.class)
                || classPredicate.test(int[][].class) || classPredicate.test(Integer[][].class));

        classPredicate = getClassPredicate(Integer[].class);
        assertTrue(classPredicate.test(int[].class) && classPredicate.test(Integer[].class));
        assertFalse(classPredicate.test(null) || classPredicate.test(Object[].class)
                || classPredicate.test(int.class) || classPredicate.test(Integer.class)
                || classPredicate.test(int[][].class) || classPredicate.test(Integer[][].class));

        classPredicate = getClassPredicate(Integer[][].class);
        assertTrue(classPredicate.test(int[][].class) && classPredicate.test(Integer[][].class));
        assertFalse(classPredicate.test(null) || classPredicate.test(Object[][].class)
                || classPredicate.test(int[].class) || classPredicate.test(Integer.class)
                || classPredicate.test(int[][][].class) || classPredicate.test(Integer[][][].class));

    }

    @Test
    public void isPrimitive_withSimpleClass_getExpectedResults() {
        assertTrue(isPrimitive(int.class) && isPrimitive(char.class) && isPrimitive(byte.class) && isPrimitive(float.class) 
                && isPrimitive(boolean.class) && isPrimitive(double.class) && isPrimitive(short.class) && isPrimitive(long.class));
        
        assertFalse(isPrimitive(Integer.class) || isPrimitive(Character.class) || isPrimitive(Byte.class) || isPrimitive(Float.class)
                || isPrimitive(Boolean.class) || isPrimitive(Double.class) || isPrimitive(Short.class) || isPrimitive(Long.class));

        assertFalse(isPrimitive(DayOfWeek.class) || isPrimitive(Object.class) || isPrimitive(Comparable.class) || isPrimitive(A.class)
                || isPrimitive(ITest1.class) || isPrimitive(new Integer(3).getClass()));
    }

    @Test
    public void isPrimitive_withArrayClass_getExpectedResults() {
        assertTrue(isPrimitive(int[].class) && isPrimitive(char[].class) && isPrimitive(byte[].class) && isPrimitive(float[].class)
                && isPrimitive(boolean[][].class) && isPrimitive(double[][].class) && isPrimitive(short[][].class) && isPrimitive(long[][][].class));

        assertFalse(isPrimitive(Integer[][][].class) || isPrimitive(Character[][].class) || isPrimitive(Byte[].class)
                || isPrimitive(Float[][][].class)
                || isPrimitive(Boolean[].class) || isPrimitive(Double[].class) || isPrimitive(Short[].class) || isPrimitive(Long[].class));

        assertFalse(isPrimitive(DayOfWeek[].class) || isPrimitive(Object[][][].class) || isPrimitive(Comparable[].class)
                || isPrimitive(A[].class) || isPrimitive(ITest1[][].class));

    }

    @Test
    public void getDefaultValue_withSimpleClass_getExpectedResults() {
        assertEquals(0, getDefaultValue(int.class));
        assertEquals((short)0, getDefaultValue(short.class));
        assertEquals((byte)0, getDefaultValue(byte.class));
        assertEquals((char)0, getDefaultValue(char.class));
        assertEquals(0L, getDefaultValue(long.class));
        assertEquals(0d, getDefaultValue(double.class));
        assertEquals(0f, getDefaultValue(float.class));
        assertEquals(false, getDefaultValue(boolean.class));

        assertEquals(0, getDefaultValue(Integer.class));
        assertEquals((short)0, getDefaultValue(Short.class));
        assertEquals((byte)0, getDefaultValue(Byte.class));
        assertEquals((char)0, getDefaultValue(Character.class));
        assertEquals(0L, getDefaultValue(Long.class));
        assertEquals(0d, getDefaultValue(Double.class));
        assertEquals(0f, getDefaultValue(Float.class));
        assertEquals(false, getDefaultValue(Boolean.class));

        assertEquals(null, getDefaultValue(Object.class));
        assertEquals(null, getDefaultValue(String.class));
        assertEquals(null, getDefaultValue(Comparable.class));
        assertEquals(null, getDefaultValue(DayOfWeek.class));
        assertEquals(null, getDefaultValue(A.class));
        assertEquals(null, getDefaultValue(ITest1.class));
    }

    @Test
    public void getDefaultValue_withArrayClass_getExpectedResults() {
        if(System.getProperties().containsKey("EMPTY_ARRAY_AS_DEFAULT")
                && "false".equalsIgnoreCase(System.getProperty("EMPTY_ARRAY_AS_DEFAULT"))){
            assertNull(getDefaultValue(int[].class));
            assertNull(getDefaultValue(short[][].class));

            assertNull(getDefaultValue(Byte[].class));
            assertNull(getDefaultValue(Character[][].class));
            assertNull(getDefaultValue(Boolean[].class));

            assertNull(getDefaultValue(Object[].class));
            assertNull(getDefaultValue(String[].class));
            assertNull(getDefaultValue(Comparable[].class));
            assertNull(getDefaultValue(DayOfWeek[].class));
            assertNull(getDefaultValue(A[].class));
            assertNull(getDefaultValue(ITest1[].class));

            assertNull(getDefaultValue(SupplierThrowable[].class));
            assertNull(getDefaultValue(Consumer[][].class));
            assertNull(getDefaultValue(List[].class));
        }else {
            assertTrue(TypeHelper.deepEquals(new int[0], (int[])getDefaultValue(int[].class)));
            assertTrue(TypeHelper.deepEquals(new short[0][], getDefaultValue(short[][].class)));
            assertTrue(TypeHelper.deepEquals(new long[0], getDefaultValue(long[].class)));

            assertTrue(TypeHelper.deepEquals(new Byte[0], getDefaultValue(Byte[].class)));
            assertTrue(TypeHelper.deepEquals(new Character[0][], getDefaultValue(Character[][].class)));
            assertTrue(TypeHelper.deepEquals(new Boolean[0], getDefaultValue(Boolean[].class)));

            assertTrue(TypeHelper.deepEquals(new Object[0], getDefaultValue(Object[].class)));
            assertTrue(TypeHelper.deepEquals(new String[0][][], getDefaultValue(String[][][].class)));
            assertTrue(TypeHelper.deepEquals(new Comparable[0], getDefaultValue(Comparable[].class)));
            assertTrue(TypeHelper.deepEquals(new DayOfWeek[0], getDefaultValue(DayOfWeek[].class)));
            assertTrue(TypeHelper.deepEquals(new A[0], getDefaultValue(A[].class)));
            assertTrue(TypeHelper.deepEquals(new ITest1[0], getDefaultValue(ITest1[].class)));

            assertTrue(TypeHelper.deepEquals(new Function[0], getDefaultValue(Function[].class)));
            assertFalse(TypeHelper.deepEquals(new Function[0], getDefaultValue(FunctionThrowable[].class)));
            assertTrue(TypeHelper.deepEquals(new AbstractThrowable[0], getDefaultValue(AbstractThrowable[].class)));
            assertTrue(TypeHelper.deepEquals(new Predicate[0][], getDefaultValue(Predicate[][].class)));
        }
    }

    @Test
    public void getEquivalentClass_withSimpleClass_getExpectedResults() {
        assertEquals(Integer.class, getEquivalentClass(int.class));
        assertEquals(Character.class, getEquivalentClass(char.class));
        assertEquals(Short.class, getEquivalentClass(short.class));
        assertEquals(Long.class, getEquivalentClass(long.class));
        assertEquals(Byte.class, getEquivalentClass(byte.class));
        assertEquals(Boolean.class, getEquivalentClass(boolean.class));
        assertEquals(Double.class, getEquivalentClass(double.class));
        assertEquals(Float.class, getEquivalentClass(float.class));

        assertEquals(int.class, getEquivalentClass(Integer.class));
        assertEquals(char.class, getEquivalentClass(Character.class));
        assertEquals(short.class, getEquivalentClass(Short.class));
        assertEquals(long.class, getEquivalentClass(Long.class));
        assertEquals(byte.class, getEquivalentClass(Byte.class));
        assertEquals(boolean.class, getEquivalentClass(Boolean.class));
        assertEquals(double.class, getEquivalentClass(Double.class));
        assertEquals(float.class, getEquivalentClass(Float.class));

        assertEquals(null, getEquivalentClass(Object.class));
        assertEquals(null, getEquivalentClass(Comparable.class));
        assertEquals(null, getEquivalentClass(String.class));
        assertEquals(null, getEquivalentClass(Function.class));
        assertEquals(null, getEquivalentClass(A.class));
        assertEquals(null, getEquivalentClass(ITest1.class));
    }

    @Test
    public void getEquivalentClass_withArrayClass_getExpectedResults() {
        assertEquals(Integer[].class, getEquivalentClass(int[].class));
        assertEquals(Character[].class, getEquivalentClass(char[].class));
        assertEquals(Short[].class, getEquivalentClass(short[].class));
        assertEquals(Long[].class, getEquivalentClass(long[].class));
        assertEquals(Byte[][].class, getEquivalentClass(byte[][].class));
        assertEquals(Boolean[][].class, getEquivalentClass(boolean[][].class));
        assertEquals(Double[][].class, getEquivalentClass(double[][].class));
        assertEquals(Float[][].class, getEquivalentClass(float[][].class));

        assertEquals(int[][].class, getEquivalentClass(Integer[][].class));
        assertEquals(char[][].class, getEquivalentClass(Character[][].class));
        assertEquals(short[][].class, getEquivalentClass(Short[][].class));
        assertEquals(long[][].class, getEquivalentClass(Long[][].class));
        assertEquals(byte[].class, getEquivalentClass(Byte[].class));
        assertEquals(boolean[].class, getEquivalentClass(Boolean[].class));
        assertEquals(double[].class, getEquivalentClass(Double[].class));
        assertEquals(float[].class, getEquivalentClass(Float[].class));

        assertEquals(null, getEquivalentClass(Object[].class));
        assertEquals(null, getEquivalentClass(Comparable[].class));
        assertEquals(null, getEquivalentClass(String[][].class));
        assertEquals(null, getEquivalentClass(Function[].class));
        assertEquals(null, getEquivalentClass(A[].class));
        assertEquals(null, getEquivalentClass(ITest1[].class));
    }

    @Test
    public void getToEquivalentConverter_withSimpleClass_getExpectedResults() {
        assertTrue(Integer.valueOf(100) ==  TypeHelper.getToEquivalentParallelConverter(int.class).apply(100));
        assertTrue(Long.valueOf(100L) == TypeHelper.getToEquivalentParallelConverter(long.class).apply(100L));
        assertTrue(Character.valueOf('a') == TypeHelper.getToEquivalentParallelConverter(char.class).apply('a'));
        assertTrue(Byte.valueOf((byte)33) == TypeHelper.getToEquivalentParallelConverter(byte.class).apply((byte)33));
        assertTrue(Boolean.valueOf(true) == TypeHelper.getToEquivalentParallelConverter(boolean.class).apply(true));
        assertTrue(Short.valueOf((short)45) == TypeHelper.getToEquivalentParallelConverter(short.class).apply((short)45));
        //Surprise, following two assertion of Float and Double would fail in Java 8
//        assertTrue(Float.valueOf(0f) == Float.valueOf(0f));
//        assertTrue(Double.valueOf(99.6d) == Double.valueOf(99.6d));
        assertTrue(1f == ((Float)TypeHelper.getToEquivalentParallelConverter(float.class).apply(1f)));
        assertTrue(99.6d == ((Double)TypeHelper.getToEquivalentParallelConverter(double.class).apply(99.6d)));

        assertTrue(-1 ==  (int)TypeHelper.getToEquivalentParallelConverter(Integer.class).apply(Integer.valueOf(-1)));
        assertTrue((100L) == (long)TypeHelper.getToEquivalentParallelConverter(Long.class).apply(Long.valueOf(100L)));
        assertTrue(('a') == (char)TypeHelper.getToEquivalentParallelConverter(Character.class).apply(Character.valueOf('a')));
        assertTrue(((byte)33) == (byte)TypeHelper.getToEquivalentParallelConverter(Byte.class).apply(Byte.valueOf((byte)33)));
        assertTrue((true) == (boolean)TypeHelper.getToEquivalentParallelConverter(Boolean.class).apply(Boolean.valueOf(true)));
        assertTrue(((short)45) == (short)TypeHelper.getToEquivalentParallelConverter(Short.class).apply(Short.valueOf((short)45)));
        assertTrue((-72.3d) == (double)TypeHelper.getToEquivalentParallelConverter(Double.class).apply(Double.valueOf(-72.3d)));
        assertTrue((-0.03f) == (float)TypeHelper.getToEquivalentParallelConverter(Float.class).apply(Float.valueOf(-0.03f)));

        Object obj = new ArrayList();
        assertTrue(obj == TypeHelper.getToEquivalentParallelConverter(Object.class).apply(obj));
        assertTrue("abc" == TypeHelper.getToEquivalentParallelConverter(Object.class).apply("abc") );
        Comparable comparable = 33;
        assertTrue( comparable == TypeHelper.getToEquivalentParallelConverter(Object.class).apply(comparable));
        A newA = new A();
        assertTrue( newA == TypeHelper.getToEquivalentParallelConverter(A.class).apply(newA));
        assertFalse( newA == TypeHelper.getToEquivalentParallelConverter(A.class).apply(new A()));
    }

    @Test
    public void getToEquivalentConverter_withArrayClass_getExpectedResults() {
        assertTrue(TypeHelper.deepEquals(new Long[]{1L,2L,3L},  TypeHelper.getToEquivalentParallelConverter(long[].class).apply(new long[]{1,2,3})));
        assertTrue(TypeHelper.deepEquals(new Short[]{1,2,3},  TypeHelper.getToEquivalentParallelConverter(short[].class).apply(new short[]{1,2,3})));
        assertTrue(TypeHelper.deepEquals(new Character[]{'a','b','c'},  TypeHelper.getToEquivalentParallelConverter(char[].class).apply(new char[]{'a','b','c'})));
        assertTrue(TypeHelper.deepEquals(new char[]{'a','b','c'},  TypeHelper.getToEquivalentParallelConverter(char[].class).apply(new char[]{'a','b','c'})));
        assertTrue(TypeHelper.deepEquals(new Byte[]{1,2,3},  TypeHelper.getToEquivalentParallelConverter(byte[].class).apply(new byte[]{1,2,3})));
        assertTrue(TypeHelper.deepEquals(new Boolean[]{true, false},  TypeHelper.getToEquivalentParallelConverter(boolean[].class).apply(new boolean[]{true, false})));
        assertTrue(TypeHelper.deepEquals(new Double[]{1.1,2.2,3.3},  TypeHelper.getToEquivalentParallelConverter(double[].class).apply(new double[]{1.1,2.2,3.3})));
        assertTrue(TypeHelper.deepEquals(new Float[]{-11f,-3.0f,0f},  TypeHelper.getToEquivalentParallelConverter(float[].class).apply(new float[]{-11f,-3.0f,0f})));

        assertTrue(TypeHelper.deepEquals(new int[]{1,2,3},  TypeHelper.getToEquivalentParallelConverter(Integer[].class).apply(new Integer[]{1,2,3})));
        assertTrue(TypeHelper.deepEquals(new long[]{1,2,3},  TypeHelper.getToEquivalentParallelConverter(Long[].class).apply(new Long[]{1L,2L,3L})));
        assertTrue(TypeHelper.deepEquals(new short[]{1,2,3},  TypeHelper.getToEquivalentParallelConverter(Short[].class).apply(new Short[]{1,2,3})));
        assertTrue(TypeHelper.deepEquals(new char[]{'a','b','c'},  TypeHelper.getToEquivalentParallelConverter(Character[].class).apply(new Character[]{'a','b','c'})));
        assertTrue(TypeHelper.deepEquals(new byte[]{1,2,3},  TypeHelper.getToEquivalentParallelConverter(Byte[].class).apply(new Byte[]{1,2,3})));
        assertTrue(TypeHelper.deepEquals(new boolean[]{true, false},  TypeHelper.getToEquivalentParallelConverter(Boolean[].class).apply(new Boolean[]{true, false})));
        assertTrue(TypeHelper.deepEquals(new double[]{1.1,2.2,3.3},  TypeHelper.getToEquivalentParallelConverter(Double[].class).apply(new Double[]{1.1,2.2,3.3})));
        assertTrue(TypeHelper.deepEquals(new float[]{-11f,-3.0f,0f},  TypeHelper.getToEquivalentParallelConverter(Float[].class).apply(new Float[]{-11f,-3.0f,0f})));
        assertTrue(TypeHelper.deepEquals(new Float[]{-11f,-3.0f,0f},  TypeHelper.getToEquivalentParallelConverter(Float[].class).apply(new Float[]{-11f,-3.0f,0f})));

        assertFalse(TypeHelper.deepEquals(new boolean[]{true, false, false},  TypeHelper.getToEquivalentParallelConverter(Boolean[].class).apply(new Boolean[]{true, false, null})));

        assertTrue(TypeHelper.deepEquals(
                new Boolean[][]{new Boolean[]{true, false}, new Boolean[0], null},
                TypeHelper.
                        getToEquivalentParallelConverter(boolean[][].class)
                        .apply(new boolean[][]{new boolean[]{true, false}, new boolean[0], null})));
        assertTrue(TypeHelper.deepEquals(new Byte[][]{new Byte[]{1,2,3}, null},  TypeHelper.getToEquivalentParallelConverter(byte[][].class).apply(new byte[][]{new byte[]{1,2,3}, null})));
        assertTrue(TypeHelper.deepEquals(new Double[][]{new Double[]{1.1}, new Double[]{2.2}, new Double[]{3.3}},
                TypeHelper.getToEquivalentParallelConverter(double[][].class).apply((new double[][]{new double[]{1.1}, new double[]{2.2}, new double[]{3.3}}))));
        assertTrue(TypeHelper.deepEquals(new Float[][]{null},  TypeHelper.getToEquivalentParallelConverter(float[][].class).apply(new float[][]{null})));

        assertTrue(TypeHelper.deepEquals(new int[][]{new int[]{1,2,3}},
                TypeHelper.getToEquivalentParallelConverter(Integer[][].class).apply(new Integer[][]{new Integer[]{1,2,3}})));
        assertTrue(TypeHelper.deepEquals(new long[][]{},  TypeHelper.getToEquivalentParallelConverter(Long[][].class).apply(new Long[][]{})));
        assertTrue(TypeHelper.deepEquals(new short[][]{new short[]{1,2,3}},  TypeHelper.getToEquivalentParallelConverter(Short[][].class).apply(new Short[][]{new Short[]{1,2,3}})));
        assertTrue(TypeHelper.deepEquals(new char[][]{null, null},  TypeHelper.getToEquivalentParallelConverter(Character[][].class).apply(new Character[][]{null, null})));


        Object[] objects = new Object[]{null, 0, void.class, 'a', "", null};
        assertTrue(objects == TypeHelper.getToEquivalentParallelConverter(Object[].class).apply(objects));
        Comparable[] comparables = new Comparable[]{1, 33f, -2d, 'a', "abc"};
        assertTrue( comparables == TypeHelper.getToEquivalentParallelConverter(Comparable[].class).apply(comparables));
        A[] newA = new A[]{};
        assertTrue( newA == TypeHelper.getToEquivalentParallelConverter(A.class).apply(newA));
        String[] strings = new String[]{null, "", "   "};
        assertTrue( strings == TypeHelper.getToEquivalentParallelConverter(A.class).apply(strings));
    }

    interface ITest1 {}
    interface ITest2 {}
    class A implements ITest1 {}
    class B extends A {}
    class C extends B implements ITest2{}
    class D implements ITest1, ITest2 {}

    @Test
    public void getClassPredicate_withInterfaceOrClassTypes_shallMatchCorrectly() {
        Predicate<Class> classPredicate = getClassPredicate(ITest1.class);
        assertTrue(classPredicate.test(A.class) && classPredicate.test(B.class)
                && classPredicate.test(C.class) && classPredicate.test(D.class)
                && classPredicate.test(ITest1.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(Object.class));

        classPredicate = getClassPredicate(ITest2.class);
        assertTrue(classPredicate.test(C.class) && classPredicate.test(D.class)
                && classPredicate.test(ITest2.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(Object.class)
                || classPredicate.test(A.class) || classPredicate.test(B.class));

        classPredicate = getClassPredicate(A.class);
        assertTrue(classPredicate.test(A.class) && classPredicate.test(B.class)
                && classPredicate.test(C.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(Object.class)
                || classPredicate.test(D.class) || classPredicate.test(ITest1.class) || classPredicate.test(ITest2.class));

        classPredicate = getClassPredicate(ITest1[].class);
        assertTrue(classPredicate.test(A[].class) && classPredicate.test(B[].class)
                && classPredicate.test(C[].class) && classPredicate.test(D[].class)
                && classPredicate.test(ITest1[].class));
        assertFalse(classPredicate.test(null) || classPredicate.test(Object.class)
                || classPredicate.test(A.class) || classPredicate.test(ITest1.class) || classPredicate.test(ITest2.class));

        classPredicate = getClassPredicate(ITest2[].class);
        assertTrue(classPredicate.test(C[].class) && classPredicate.test(D[].class)
                && classPredicate.test(ITest2[].class));
        assertFalse(classPredicate.test(null) || classPredicate.test(Object.class)
                || classPredicate.test(Object[].class)
                || classPredicate.test(A[].class) || classPredicate.test(B[].class)
                || classPredicate.test(C.class) || classPredicate.test(D.class));

        classPredicate = getClassPredicate(A[].class);
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
        FunctionThrowable<Integer, Object> arrayFactory = getArrayFactory(int.class);
        validateFactory(arrayFactory, 3, int[].class);

        arrayFactory = getArrayFactory(int[].class);
        validateFactory(arrayFactory, 2, int[][].class);

        arrayFactory = getArrayFactory(char.class);
        validateFactory(arrayFactory, 0, char[].class);

        arrayFactory = getArrayFactory(char[].class);
        validateFactory(arrayFactory, 1, char[][].class);

        arrayFactory = getArrayFactory(byte.class);
        validateFactory(arrayFactory, 2, byte[].class);

        arrayFactory = getArrayFactory(byte[].class);
        validateFactory(arrayFactory, 0, byte[][].class);

        arrayFactory = getArrayFactory(boolean.class);
        validateFactory(arrayFactory, 5, boolean[].class);

        arrayFactory = getArrayFactory(boolean[].class);
        validateFactory(arrayFactory, 1, boolean[][].class);

        arrayFactory = getArrayFactory(double.class);
        validateFactory(arrayFactory, 3, double[].class);

        arrayFactory = getArrayFactory(double[].class);
        validateFactory(arrayFactory, 0, double[][].class);

        arrayFactory = getArrayFactory(float.class);
        validateFactory(arrayFactory, 1, float[].class);

        arrayFactory = getArrayFactory(float[].class);
        validateFactory(arrayFactory, 0, float[][].class);

        arrayFactory = getArrayFactory(long.class);
        validateFactory(arrayFactory, 2, long[].class);

        arrayFactory = getArrayFactory(long[].class);
        validateFactory(arrayFactory, 3, long[][].class);

        arrayFactory = getArrayFactory(short.class);
        validateFactory(arrayFactory, 1, short[].class);

        arrayFactory = getArrayFactory(short[].class);
        validateFactory(arrayFactory, 0, short[][].class);
    }

    @Test
    public void getArrayFactory_withWrapperTypes_getWrapperArrays() {
        FunctionThrowable<Integer, Object> arrayFactory = getArrayFactory(Integer.class);
        validateFactory(arrayFactory, 3, Integer[].class);

        arrayFactory = getArrayFactory(Integer[].class);
        validateFactory(arrayFactory, 2, Integer[][].class);

        arrayFactory = getArrayFactory(Character.class);
        validateFactory(arrayFactory, 0, Character[].class);

        arrayFactory = getArrayFactory(Character[].class);
        validateFactory(arrayFactory, 1, Character[][].class);

        arrayFactory = getArrayFactory(Byte.class);
        validateFactory(arrayFactory, 2, Byte[].class);

        arrayFactory = getArrayFactory(Byte[].class);
        validateFactory(arrayFactory, 0, Byte[][].class);

        arrayFactory = getArrayFactory(Boolean.class);
        validateFactory(arrayFactory, 5, Boolean[].class);

        arrayFactory = getArrayFactory(Boolean[].class);
        validateFactory(arrayFactory, 1, Boolean[][].class);

        arrayFactory = getArrayFactory(Double.class);
        validateFactory(arrayFactory, 3, Double[].class);

        arrayFactory = getArrayFactory(Double[].class);
        validateFactory(arrayFactory, 0, Double[][].class);

        arrayFactory = getArrayFactory(Float.class);
        validateFactory(arrayFactory, 1, Float[].class);

        arrayFactory = getArrayFactory(Float[].class);
        validateFactory(arrayFactory, 0, Float[][].class);

        arrayFactory = getArrayFactory(Long.class);
        validateFactory(arrayFactory, 2, Long[].class);

        arrayFactory = getArrayFactory(Long[].class);
        validateFactory(arrayFactory, 3, Long[][].class);

        arrayFactory = getArrayFactory(Short.class);
        validateFactory(arrayFactory, 1, Short[].class);

        arrayFactory = getArrayFactory(Short[].class);
        validateFactory(arrayFactory, 0, Short[][].class);
    }
    @Test
    public void getArrayFactory_withClassOrInterfaceTypes_getRightArrays() {
        FunctionThrowable<Integer, Object> arrayFactory = getArrayFactory(ITest1.class);
        validateFactory(arrayFactory, 3, ITest1[].class);

        arrayFactory = getArrayFactory(ITest1[].class);
        validateFactory(arrayFactory, 2, ITest1[][].class);

        arrayFactory = getArrayFactory(A.class);
        validateFactory(arrayFactory, 0, A[].class);

        arrayFactory = getArrayFactory(C[].class);
        validateFactory(arrayFactory, 1, C[][].class);
    }

    public void getSetArrayElement_withPrimitiveArray_bothGetterSetterWorks(){
        int[] ints = new int[]{1,2,3};
        BiFunctionThrowable<Object, Integer, Object> getter = getArrayElementGetter(ints.getClass());
        TriConsumerThrowable<Object, Integer, Object> setter = getArrayElementSetter(ints.getClass());
        assertEquals(1, getter.orElse(null).apply(ints, 0));
        setter.orElse(null).accept(ints, 2, 33);
        assertEquals(33, ints[2]);
        
        long[] longs = new long[]{1,2L,3};
        getter = getArrayElementGetter(longs.getClass());
        setter = getArrayElementSetter(longs.getClass());
        assertEquals(1L, getter.orElse(null).apply(longs, 0));
        setter.orElse(null).accept(longs, 2, 33L);
        assertEquals(33L, longs[2]);
        
        short[] shorts = new short[]{1,2,3};
        getter = getArrayElementGetter(shorts.getClass());
        setter = getArrayElementSetter(shorts.getClass());
        assertEquals((short)1, getter.orElse(null).apply(shorts, 0));
        setter.orElse(null).accept(shorts, 2, (short)33);
        assertEquals((short)33, shorts[2]);
        
        char[] chars = new char[]{'a', 'b', 'c'};
        getter = getArrayElementGetter(chars.getClass());
        setter = getArrayElementSetter(chars.getClass());
        assertEquals('a', getter.orElse(null).apply(chars, 0));
        setter.orElse(null).accept(chars, 2, 'x');
        assertEquals('x', chars[2]);
        
        byte[] bytes = new byte[]{44, 45, 46};
        getter = getArrayElementGetter(bytes.getClass());
        setter = getArrayElementSetter(bytes.getClass());
        assertEquals((byte)44, getter.orElse(null).apply(bytes, 0));
        setter.orElse(null).accept(bytes, 2, (byte)55);
        assertEquals((byte)55, bytes[2]);
        
        boolean[] booleans = new boolean[]{true, false};
        getter = getArrayElementGetter(booleans.getClass());
        setter = getArrayElementSetter(booleans.getClass());
        assertEquals(true, getter.orElse(null).apply(booleans, 0));
        setter.orElse(null).accept(booleans, 1, true);
        assertEquals(true, booleans[1]);
        
        double[] doubles = new double[]{1.0, 2.0, 3.0};
        getter = getArrayElementGetter(doubles.getClass());
        setter = getArrayElementSetter(doubles.getClass());
        assertEquals(1.0, getter.orElse(null).apply(doubles, 0));
        setter.orElse(null).accept(doubles, 1, 100);
        assertEquals(100.0, doubles[1], 0);
        
        float[] floats = new float[]{1.1f, 2.2f, 3.3f};
        getter = getArrayElementGetter(floats.getClass());
        setter = getArrayElementSetter(floats.getClass());
        assertEquals(1.1f, getter.orElse(null).apply(floats, 0));
        setter.orElse(null).accept(floats, 1, 101.3f);
        assertEquals(101.3f, floats[1], 0);
    }

    @Test
    public void getSetArrayElement_withWrapperArray_bothGetterSetterWorks() {
        Integer[] ints2 = new Integer[]{1,2,3};
        BiFunctionThrowable<Object, Integer, Object> getter = getArrayElementGetter(ints2.getClass());
        TriConsumerThrowable<Object, Integer, Object> setter = getArrayElementSetter(ints2.getClass());
        assertEquals(1, getter.orElse(null).apply(ints2, 0));
        setter.orElse(null).accept(ints2, 2, 33);
        assertEquals(Integer.valueOf(33), ints2[2]);
        
        Long[] longs2 = new Long[]{1L,2L,3L};
        getter = getArrayElementGetter(longs2.getClass());
        setter = getArrayElementSetter(longs2.getClass());
        assertEquals(1L, getter.orElse(null).apply(longs2, 0));
        setter.orElse(null).accept(longs2, 2, 33L);
        assertEquals(Long.valueOf(33), longs2[2]);
        
        Short[] shorts2 = new Short[]{1,2,3};
        getter = getArrayElementGetter(shorts2.getClass());
        setter = getArrayElementSetter(shorts2.getClass());
        assertEquals(Short.valueOf((short)1), getter.orElse(null).apply(shorts2, 0));
        setter.orElse(null).accept(shorts2, 2, Short.valueOf((short)45));
        assertEquals(Short.valueOf((short)45), shorts2[2]);
        
        Character[] chars2 = new Character[]{'a', 'b', 'c'};
        getter = getArrayElementGetter(chars2.getClass());
        setter = getArrayElementSetter(chars2.getClass());
        assertEquals(Character.valueOf('a'), getter.orElse(null).apply(chars2, 0));
        setter.orElse(null).accept(chars2, 2, 'x');
        assertEquals(Character.valueOf('x'), chars2[2]);
        
        Byte[] bytes2 = new Byte[]{44, 45, 46};
        getter = getArrayElementGetter(bytes2.getClass());
        setter = getArrayElementSetter(bytes2.getClass());
        assertEquals(Byte.valueOf("44"), getter.orElse(null).apply(bytes2, 0));
        setter.orElse(null).accept(bytes2, 2, (byte)55);
        assertEquals(Byte.valueOf("55"), bytes2[2]);
        
        Boolean[] booleans2 = new Boolean[]{true, false};
        getter = getArrayElementGetter(booleans2.getClass());
        setter = getArrayElementSetter(booleans2.getClass());
        assertEquals(true, getter.orElse(null).apply(booleans2, 0));
        setter.orElse(null).accept(booleans2, 1, true);
        assertEquals(true, booleans2[1]);
        
        Double[] doubles2 = new Double[]{1.0, 2.0, 3.0};
        getter = getArrayElementGetter(doubles2.getClass());
        setter = getArrayElementSetter(doubles2.getClass());
        assertEquals(1.0, getter.orElse(null).apply(doubles2, 0));
        setter.orElse(null).accept(doubles2, 1, 100.0);
        assertEquals(100.0, doubles2[1], 0);
        
        Float[] floats2 = new Float[]{1.1f, 2.2f, 3.3f};
        getter = getArrayElementGetter(floats2.getClass());
        setter = getArrayElementSetter(floats2.getClass());
        assertEquals(1.1f, getter.orElse(null).apply(floats2, 0));
        setter.orElse(null).accept(floats2, 1, 101.3f);
        assertEquals(101.3f, floats2[1], 0);
    }

    @Test
    public void getSetArrayElement_withClassOrInterfaceType_bothGetterSetterWorks() {
        ITest1[] interfaces1 = new ITest1[]{null, new A(), new B(), new C(), new D()};
        BiFunctionThrowable<Object, Integer, Object> getter = getArrayElementGetter(interfaces1.getClass());
        TriConsumerThrowable<Object, Integer, Object> setter = getArrayElementSetter(interfaces1.getClass());
        assertEquals(null, getter.orElse(null).apply(interfaces1, 0));
        assertEquals(A.class, getter.orElse(null).apply(interfaces1, 1).getClass());
        assertEquals(D.class, getter.orElse(null).apply(interfaces1, 4).getClass());
        setter.orElse(null).accept(interfaces1, 0, new D());
        assertEquals(D.class, getter.orElse(null).apply(interfaces1, 0).getClass());

        A[] aArray = new A[] { new A(), new B(), new C()};
        getter = getArrayElementGetter(aArray.getClass());
        setter = getArrayElementSetter(aArray.getClass());
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

    @Test
    public void getSetArrayElement_withMultiDimensionArrayTypes_bothGetterSetterWorks() {
        int[][] ints = new int[][]{ new int[]{1,2,3}, null};
        BiFunctionThrowable<Object, Integer, Object> getter = getArrayElementGetter(ints.getClass());
        TriConsumerThrowable<Object, Integer, Object> setter = getArrayElementSetter(ints.getClass());
        assertEquals(ints[0], getter.orElse(null).apply(ints, 0));
        setter.orElse(null).accept(ints, 1, new int[0]);
        assertEquals(0, ints[1].length);

        long[][] longs = new long[][]{new long[]{1,2L,3}, null};
        getter = getArrayElementGetter(longs.getClass());
        setter = getArrayElementSetter(longs.getClass());
        assertEquals(longs[0], getter.orElse(null).apply(longs, 0));
        setter.orElse(null).accept(longs, 0, null);
        assertNull(longs[0]);

        short[][] shorts = new short[][]{new short[]{1,2,3}, null};
        getter = getArrayElementGetter(shorts.getClass());
        setter = getArrayElementSetter(shorts.getClass());
        assertEquals(shorts[0], getter.orElse(null).apply(shorts, 0));
        setter.orElse(null).accept(shorts, 0, null);
        assertNull(shorts[0]);

        char[][] chars = new char[][]{new char[]{'a', 'b', 'c'}, null};
        getter = getArrayElementGetter(chars.getClass());
        setter = getArrayElementSetter(chars.getClass());
        assertEquals(chars[0], getter.orElse(null).apply(chars, 0));
        setter.orElse(null).accept(chars, 0, null);
        assertNull(chars[0]);

        byte[][] bytes = new byte[][]{new byte[]{44, 45, 46}, null};
        getter = getArrayElementGetter(bytes.getClass());
        setter = getArrayElementSetter(bytes.getClass());
        assertEquals(bytes[0], getter.orElse(null).apply(bytes, 0));
        setter.orElse(null).accept(bytes, 0, null);
        assertNull(bytes[0]);

        boolean[][] booleans = new boolean[][]{new boolean[]{true, false}, null};
        getter = getArrayElementGetter(booleans.getClass());
        setter = getArrayElementSetter(booleans.getClass());
        assertEquals(booleans[0], getter.orElse(null).apply(booleans, 0));
        setter.orElse(null).accept(booleans, 0, null);
        assertNull(booleans[0]);

        double[][] doubles = new double[][]{new double[]{1.0, 2.0, 3.0}, null};
        getter = getArrayElementGetter(doubles.getClass());
        setter = getArrayElementSetter(doubles.getClass());
        assertEquals(doubles[0], getter.orElse(null).apply(doubles, 0));
        setter.orElse(null).accept(doubles, 0, null);
        assertNull(doubles[0]);

        float[][] floats = new float[][]{new float[]{1.1f, 2.2f, 3.3f}, null};
        getter = getArrayElementGetter(floats.getClass());
        setter = getArrayElementSetter(floats.getClass());
        assertEquals(floats[0], getter.orElse(null).apply(floats, 0));
        setter.orElse(null).accept(floats, 0, null);
        assertNull(floats[0]);

        Integer[][] ints2 = new Integer[][]{new Integer[]{1,2,3}, null};
        getter = getArrayElementGetter(ints2.getClass());
        setter = getArrayElementSetter(ints2.getClass());
        assertEquals(ints2[0], getter.orElse(null).apply(ints2, 0));
        setter.orElse(null).accept(ints2, 0, null);
        assertNull(ints2[0]);

        Long[][] longs2 = new Long[][]{new Long[]{1L,2L,3L}, null};
        getter = getArrayElementGetter(longs2.getClass());
        setter = getArrayElementSetter(longs2.getClass());
        assertEquals(longs2[0], getter.orElse(null).apply(longs2, 0));
        setter.orElse(null).accept(longs2, 0, null);
        assertNull(longs2[0]);

        Short[][] shorts2 = new Short[][]{new Short[]{1,2,3}, null};
        getter = getArrayElementGetter(shorts2.getClass());
        setter = getArrayElementSetter(shorts2.getClass());
        assertEquals(shorts2[0], getter.orElse(null).apply(shorts2, 0));
        setter.orElse(null).accept(shorts2, 0, null);
        assertNull(shorts2[0]);

        Character[][] chars2 = new Character[][]{new Character[]{'a', 'b', 'c'}, null};
        getter = getArrayElementGetter(chars2.getClass());
        setter = getArrayElementSetter(chars2.getClass());
        assertEquals(chars2[0], getter.orElse(null).apply(chars2, 0));
        setter.orElse(null).accept(chars2, 0, null);
        assertNull(chars2[0]);

        Byte[][] bytes2 = new Byte[][]{new Byte[]{44, 45, 46}, null};
        getter = getArrayElementGetter(bytes2.getClass());
        setter = getArrayElementSetter(bytes2.getClass());
        assertEquals(bytes2[0], getter.orElse(null).apply(bytes2, 0));
        setter.orElse(null).accept(bytes2, 0, null);
        assertNull(bytes2[0]);

        Boolean[][] booleans2 = new Boolean[][]{new Boolean[]{true, false}, null};
        getter = getArrayElementGetter(booleans2.getClass());
        setter = getArrayElementSetter(booleans2.getClass());
        assertEquals(booleans2[0], getter.orElse(null).apply(booleans2, 0));
        setter.orElse(null).accept(booleans2, 0, null);
        assertNull(booleans2[0]);

        Double[][] doubles2 = new Double[][]{new Double[]{1.0, 2.0, 3.0}, null};
        getter = getArrayElementGetter(doubles2.getClass());
        setter = getArrayElementSetter(doubles2.getClass());
        assertEquals(doubles2[0], getter.orElse(null).apply(doubles2, 0));
        setter.orElse(null).accept(doubles2, 0, null);
        assertNull(doubles2[0]);

        Float[][] floats2 = new Float[][]{new Float[]{1.1f, 2.2f, 3.3f}, null};
        getter = getArrayElementGetter(floats2.getClass());
        setter = getArrayElementSetter(floats2.getClass());
        assertEquals(floats2[0], getter.orElse(null).apply(floats2, 0));
        setter.orElse(null).accept(floats2, 0, null);
        assertNull(floats2[0]);

        ITest1[][] interfaces1 = new ITest1[][]{new ITest1[]{null, new A(), new B(), new C(), new D()}, null};
        getter = getArrayElementGetter(interfaces1.getClass());
        setter = getArrayElementSetter(interfaces1.getClass());
        assertEquals(interfaces1[0], getter.orElse(null).apply(interfaces1, 0));
        assertEquals(null, getter.orElse(null).apply(interfaces1, 1));
        setter.orElse(null).accept(interfaces1, 0, null);
        assertNull(interfaces1[0]);

        A[][] aArray = new A[][]{ new A[] { new A(), new B(), new C()}, null };
        getter = getArrayElementGetter(aArray.getClass());
        setter = getArrayElementSetter(aArray.getClass());
        assertEquals(A[].class, getter.orElse(null).apply(aArray, 0).getClass());
        //Invalid operations returns null
        assertEquals(null, getter.orElse(null).apply(aArray, -1));
        assertEquals(null, getter.orElse(null).apply(aArray, 5));
        //Invalid setting operation would not update the element
        setter.orElse(null).accept(aArray, 0, null);
        assertNull(aArray[0]);
    }

    private void assertCopying(Object expected, TriFunctionThrowable<Object, Integer, Integer, Object> copier, Object original, int from, int to){
        Object copy = copier.orElse(null).apply(original, from, to);
        if(expected == null){
            assertNull(copy);
            return;
        }

        assertEquals(expected.getClass(), copy.getClass());
        assertTrue(TypeHelper.deepEquals(expected, copy));
    }

    @Test
    public void getArrayRangeCopier_withPrimitiveTypes_getCorrectCopies() {
        TriFunctionThrowable<Object, Integer, Integer, Object> copier;

        int[] ints = new int[]{1,2,3,4};
        copier = getArrayRangeCopier(ints.getClass().getComponentType());
        assertCopying(new int[]{2,3}, copier, ints, 1, 3);

        long[] longs = new long[]{1,2,3,4,5};
        copier = getArrayRangeCopier(longs.getClass().getComponentType());
        assertCopying(new long[]{4,5}, copier, longs, 3, 5);

        short[] shorts = new short[]{1,2,3,4};
        copier = getArrayRangeCopier(shorts.getClass().getComponentType());
        assertCopying(null, copier, shorts, 5, 6);

        char[] chars = new char[]{1,2,3,4};
        copier = getArrayRangeCopier(chars.getClass().getComponentType());
        assertCopying(new char[]{2,3}, copier, chars, 1, 3);

        boolean[] booleans = new boolean[]{true, false, false, true, true};
        copier = getArrayRangeCopier(booleans.getClass().getComponentType());
        assertCopying(new boolean[]{true, false, false, true, true}, copier, booleans, 0, 5);

        byte[] bytes = new byte[]{1,2,3,4};
        copier = getArrayRangeCopier(bytes.getClass().getComponentType());
        assertCopying(new byte[]{2}, copier, bytes, 1, 2);

        float[] floats = new float[]{1f,2f,3f,4f};
        copier = getArrayRangeCopier(floats.getClass().getComponentType());
        assertCopying(new float[]{4f}, copier, floats, 3, 4);

        double[] doubles = new double[]{1d,2d,3d,4d};
        copier = getArrayRangeCopier(doubles.getClass().getComponentType());
        //Be cautious when the <tt>to</tt> is more then the length of the original array, default values would be kep
        assertCopying(new double[]{0d}, copier, doubles, 4, 5);
    }

    @Test
    public void getArrayRangeCopier_withWrapperTypes_getCorrectCopies() {
        TriFunctionThrowable<Object, Integer, Integer, Object> copier;

        Integer[] ints = new Integer[]{1,2,3,4};
        copier = getArrayRangeCopier(ints.getClass().getComponentType());
        assertCopying(new Integer[]{2,3}, copier, ints, 1, 3);

        Long[] longs = new Long[]{1L,2L,3L,4L,5L};
        copier = getArrayRangeCopier(longs.getClass().getComponentType());
        //Be cautious when the <tt>to</tt> is more then the length of the original array, default values would be kep
        assertCopying(new Long[]{4L,5L, null, null}, copier, longs, 3, 7);

        Short[] shorts = new Short[]{1,2,3,4};
        copier = getArrayRangeCopier(shorts.getClass().getComponentType());
        assertCopying(null, copier, shorts, 3, 2);

        Character[] chars = new Character[]{'a', 'b', 'c', 'd'};
        copier = getArrayRangeCopier(chars.getClass().getComponentType());
        assertCopying(new Character[]{'b', 'c'}, copier, chars, 1, 3);

        Boolean[] booleans = new Boolean[]{true, false, false, true, true};
        copier = getArrayRangeCopier(booleans.getClass().getComponentType());
        assertCopying(new Boolean[]{true}, copier, booleans, 4, 5);

        Byte[] bytes = new Byte[]{1,2,3,4};
        copier = getArrayRangeCopier(bytes.getClass().getComponentType());
        assertCopying(new Byte[]{2}, copier, bytes, 1, 2);

        Float[] floats = new Float[]{1f,2f,3f,4f};
        copier = getArrayRangeCopier(floats.getClass().getComponentType());
        assertCopying(new Float[]{4f}, copier, floats, 3, 4);

        double[] doubles = new double[]{1d,2d,3d,4d};
        copier = getArrayRangeCopier(doubles.getClass().getComponentType());
        assertCopying(new double[]{}, copier, doubles, 1, 1);
    }

    @Test
    public void getArrayRangeCopier_withClassOrInterfaceTypes_getCorrectCopies() {
        TriFunctionThrowable<Object, Integer, Integer, Object> copier;

        Comparable[] comparables = new Comparable[]{1,'b',3L,4f,5d,"6s"};
        copier = getArrayRangeCopier(comparables.getClass().getComponentType());
        assertCopying(new Comparable[]{1,'b',3L,4f,5d,"6s"}, copier, comparables, 0, 6);
        assertCopying(new Comparable[]{'b',3L,4f,5d,"6s"}, copier, comparables, 1, 6);
        assertCopying(new Comparable[]{}, copier, comparables, 1, 1);
        assertCopying(new Comparable[]{1,'b',3L,4f,5d}, copier, comparables, 0, 5);
        assertCopying(null, copier, comparables, 3, 2);

        Object[] objects = new Object[]{1,'b',3L,4f,5d,"6s"};
        copier = getArrayRangeCopier(objects.getClass().getComponentType());
        assertCopying(new Object[]{1,'b',3L,4f,5d,"6s"}, copier, objects, 0, 6);
        assertCopying(new Object[]{'b',3L,4f,5d,"6s"}, copier, objects, 1, 6);
        assertCopying(new Object[]{}, copier, objects, 5, 5);
        assertCopying(new Object[]{1,'b',3L,4f,5d}, copier, objects, 0, 5);

        ITest1[] tests = new ITest1[]{new A(), new B(), new C(), new D()};
        copier = getArrayRangeCopier(objects.getClass().getComponentType());
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
        toString = getArrayToString(ints.getClass().getComponentType());
        assertToString("[1, 2, 3]", toString, ints);

        short[] shorts = new short[]{1, 2, 3};
        toString = getArrayToString(shorts.getClass().getComponentType());
        assertToString("[1, 2, 3]", toString, shorts);

        long[] longs = new long[]{1, 2, 3};
        toString = getArrayToString(longs.getClass().getComponentType());
        assertToString("[1, 2, 3]", toString, longs);

        double[] doubles = new double[]{1, 2, 3};
        toString = getArrayToString(doubles.getClass().getComponentType());
        assertToString("[1.0, 2.0, 3.0]", toString, doubles);

        float[] floats = new float[]{1, 2, 3};
        toString = getArrayToString(floats.getClass().getComponentType());
        assertToString("[1.0, 2.0, 3.0]", toString, floats);

        char[] chars = new char[]{'a', 'b', 'c'};
        toString = getArrayToString(chars.getClass().getComponentType());
        assertToString("[a, b, c]", toString, chars);

        byte[] bytes = new byte[]{'a', 'b', 'c'};
        toString = getArrayToString(bytes.getClass().getComponentType());
        assertToString("[97, 98, 99]", toString, bytes);

        boolean[] booleans = new boolean[]{true, false, true};
        toString = getArrayToString(booleans.getClass().getComponentType());
        assertToString("[true, false, true]", toString, booleans);
    }

    @Test
    public void getArrayToString_withWrapperTypes_getExpectedString() {
        Function<Object, String> toString;

        Integer[] ints = new Integer[]{1, 2, 3};
        toString = getArrayToString(ints.getClass().getComponentType());
        assertToString("[1, 2, 3]", toString, ints);

        Short[] shorts = new Short[]{1, 2, 3};
        toString = getArrayToString(shorts.getClass().getComponentType());
        assertToString("[1, 2, 3]", toString, shorts);

        Long[] longs = new Long[]{1L, 2L, 3L};
        toString = getArrayToString(longs.getClass().getComponentType());
        assertToString("[1, 2, 3]", toString, longs);

        Double[] doubles = new Double[]{1d, 2d, 3d, null};
        toString = getArrayToString(doubles.getClass().getComponentType());
        assertToString("[1.0, 2.0, 3.0, null]", toString, doubles);

        Float[] floats = new Float[]{null, 1f, 2f, 3f};
        toString = getArrayToString(floats.getClass().getComponentType());
        assertToString("[null, 1.0, 2.0, 3.0]", toString, floats);

        Character[] chars = new Character[]{'a', 'b', 'c'};
        toString = getArrayToString(chars.getClass().getComponentType());
        assertToString("[a, b, c]", toString, chars);

        Byte[] bytes = new Byte[]{'a', 'b', 'c'};
        toString = getArrayToString(bytes.getClass().getComponentType());
        assertToString("[97, 98, 99]", toString, bytes);

        Boolean[] booleans = new Boolean[]{true, false, true};
        toString = getArrayToString(booleans.getClass().getComponentType());
        assertToString("[true, false, true]", toString, booleans);
    }

    @Test
    public void getArrayToString_withClassOrInterfaceTypes_getExpectedString() {
        Function<Object, String> toString;

        Comparable[] comparables = new Comparable[]{1, 2L, null, "A string", 3, true};
        toString = getArrayToString(comparables.getClass().getComponentType());
        assertToString("[1, 2, null, A string, 3, true]", toString, comparables);

        Object[] objects = new Object[]{DayOfWeek.FRIDAY, 2.0001f, 3E2, new char[]{'a', 'b'}, new Integer[]{null, -1}};
        toString = getArrayToString(objects.getClass().getComponentType());
        assertToString("[FRIDAY, 2.0001, 300.0, [a, b], [null, -1]]", toString, objects);

        String[] strings = new String[]{"", " \t", null, "a"};
        toString = getArrayToString(strings.getClass().getComponentType());
        assertToString("[,  \t, null, a]", toString, strings);
    }

    @Test
    public void getArrayToString_withArrayOfArrayTypes_getExpectedString() {
        Function<Object, String> toString;

        Comparable[][] comparables = new Comparable[][]{new Integer[]{1,2}, new String[]{null, "str"}, null, new Comparable[]{"A string", 3, true}};
        toString = getArrayToString(comparables.getClass().getComponentType());
        assertToString("[[1, 2], [null, str], null, [A string, 3, true]]", toString, comparables);

        Object[][] objects = new Object[][]{new Object[]{DayOfWeek.FRIDAY, 2.0001f, 3E2}, new Character[]{'a', 'b'},
                new Integer[]{null, -1}, new Boolean[]{true, false, null}};
        toString = getArrayToString(objects.getClass().getComponentType());
        assertToString("[[FRIDAY, 2.0001, 300.0], [a, b], [null, -1], [true, false, null]]", toString, objects);

        String[][] strings = new String[][]{null, new String[]{"", " \t", null, "a"}, new String[]{}};
        toString = getArrayToString(strings.getClass().getComponentType());
        assertToString("[null, [,  \t, null, a], []]", toString, strings);
    }

    @Test
    public void getArrayToString_withArrayOfArrayTypes_dependentKeysGenerated() {
        Function<Object, String> toString;

        assertFalse(classOperators.containsKey(Month.class) || classOperators.containsKey(Month[].class)
                || classOperators.containsKey(SupplierThrowable.class) || classOperators.containsKey(SupplierThrowable[].class));
        Object[] objects = new Object[][]{new Month[][]{null, new Month[]{Month.APRIL}},
                new SupplierThrowable[]{null, ()-> 1}, new SupplierThrowable[][]{null} };
        toString = getArrayToString(objects.getClass().getComponentType());
        String result = toString.apply(objects);
        assertTrue(classOperators.containsKey(Month.class) && classOperators.containsKey(Month[].class)
                && classOperators.containsKey(SupplierThrowable.class) && classOperators.containsKey(SupplierThrowable[].class));
    }
}