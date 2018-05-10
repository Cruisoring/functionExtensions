package io.github.cruisoring;

import io.github.cruisoring.function.*;
import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple3;
import io.github.cruisoring.utility.Logger;
import org.junit.Test;

import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.github.cruisoring.TypeHelper.*;
import static org.junit.Assert.*;

public class TypeHelperTest {

    private static void assertValueEquals(Object a, Object b){
        Logger.L("%s == %s %s", TypeHelper.deepToString(a), TypeHelper.deepToString(b), "?");
        assertTrue(TypeHelper.valueEquals(a, b));
        assertTrue(TypeHelper.valueEqualsSerial(a, b));
        assertTrue(TypeHelper.valueEqualsParallel(a, b));
    }


    static Object object1 = new Object[] { new Object[]{(int)1, 1.2d, 3},
            new int[0],
            new int[0],
            new Number[0],
            new Comparable[]{null, 2, 3},
            new Object[]{null, 4},
            new int[][]{null, new int[]{5,6}}
    };
    static Object object2 = new Object[] { new Number[]{1, 1.2f, 4},
            new double[0],
            new Integer[0],
            new Comparable[0],
            new Number[]{null, 2, 3},
            new Integer[]{null, 4},
            new Object[]{null, new int[]{5,6}}
    };
    static Number nullNumber = null;
    static Comparable nullComparable = null;

    @Test
    public void testNodeEquals(){
        int[][] deepLength = getDeepLength(object1);
        //Normal nodes are always compared as Objects, 1 vs 1, 1.2d vs 1.2f, 3 vs 4
        assertTrue(nodeEquals(object1, object2, deepLength[0], NullEquality.SameTypeOnly, EmptyArrayEquality.SameTypeOnly));
        assertFalse(nodeEquals(object1, object2, deepLength[1], NullEquality.SameTypeOnly, EmptyArrayEquality.SameTypeOnly));
        assertFalse(nodeEquals(object1, object2, deepLength[2], NullEquality.SameTypeOnly, EmptyArrayEquality.SameTypeOnly));
        assertTrue(nodeEquals(object1, object2, deepLength[7], NullEquality.SameTypeOnly, EmptyArrayEquality.SameTypeOnly));
        assertTrue(nodeEquals(object1, object2, deepLength[10], NullEquality.SameTypeOnly, EmptyArrayEquality.SameTypeOnly));

        //Empty Arrays
        //int[0] vs double[0]
        assertTrue(nodeEquals(object1, object2, new int[]{1, EMPTY_ARRAY_NODE}, NullEquality.TypeIgnored, EmptyArrayEquality.TypeIgnored));
        assertFalse(nodeEquals(object1, object2, new int[]{1, EMPTY_ARRAY_NODE}, NullEquality.TypeIgnored, EmptyArrayEquality.BetweenAssignableTypes));
        assertFalse(nodeEquals(object1, object2, new int[]{1, EMPTY_ARRAY_NODE}, NullEquality.TypeIgnored, EmptyArrayEquality.SameTypeOnly));
        //int[0] vs Integer[0]
        assertTrue(nodeEquals(object1, object2, new int[]{2, EMPTY_ARRAY_NODE}, NullEquality.TypeIgnored, EmptyArrayEquality.TypeIgnored));
        assertTrue(nodeEquals(object1, object2, new int[]{2, EMPTY_ARRAY_NODE}, NullEquality.TypeIgnored, EmptyArrayEquality.BetweenAssignableTypes));
        assertFalse(nodeEquals(object1, object2, new int[]{2, EMPTY_ARRAY_NODE}, NullEquality.TypeIgnored, EmptyArrayEquality.SameTypeOnly));
        //Number[0] vs Comparable[0]
        assertTrue(nodeEquals(object1, object2, new int[]{3, EMPTY_ARRAY_NODE}, NullEquality.TypeIgnored, EmptyArrayEquality.TypeIgnored));
        assertFalse(nodeEquals(object1, object2, new int[]{3, EMPTY_ARRAY_NODE}, NullEquality.TypeIgnored, EmptyArrayEquality.BetweenAssignableTypes));
        assertFalse(nodeEquals(object1, object2, new int[]{3, EMPTY_ARRAY_NODE}, NullEquality.TypeIgnored, EmptyArrayEquality.SameTypeOnly));

        //Null values
        //between null of Comparable and null of Number
        assertTrue(nodeEquals(object1, object2, new int[]{4, 0, NULL_NODE}, NullEquality.TypeIgnored, EmptyArrayEquality.TypeIgnored));
        assertFalse(nodeEquals(object1, object2, new int[]{4, 0, NULL_NODE}, NullEquality.BetweenAssignableTypes, EmptyArrayEquality.BetweenAssignableTypes));
        assertFalse(nodeEquals(object1, object2, new int[]{4, 0, NULL_NODE}, NullEquality.SameTypeOnly, EmptyArrayEquality.SameTypeOnly));
        //between null of Object and null of Integer
        assertTrue(nodeEquals(object1, object2, new int[]{5, 0, NULL_NODE}, NullEquality.TypeIgnored, EmptyArrayEquality.TypeIgnored));
        assertTrue(nodeEquals(object1, object2, new int[]{5, 0, NULL_NODE}, NullEquality.BetweenAssignableTypes, EmptyArrayEquality.BetweenAssignableTypes));
        assertFalse(nodeEquals(object1, object2, new int[]{5, 0, NULL_NODE}, NullEquality.SameTypeOnly, EmptyArrayEquality.SameTypeOnly));
        //between null of int[] and null of Object
        assertTrue(nodeEquals(object1, object2, new int[]{6, 0, NULL_NODE}, NullEquality.TypeIgnored, EmptyArrayEquality.TypeIgnored));
        assertTrue(nodeEquals(object1, object2, new int[]{6, 0, NULL_NODE}, NullEquality.BetweenAssignableTypes, EmptyArrayEquality.BetweenAssignableTypes));
        assertFalse(nodeEquals(object1, object2, new int[]{6, 0, NULL_NODE}, NullEquality.SameTypeOnly, EmptyArrayEquality.SameTypeOnly));

        //Comparing null of Number and null of Comparable
        int[] indexes = getDeepLength(nullNumber)[0];
        assertTrue(nodeEquals(nullNumber, nullComparable, indexes, NullEquality.TypeIgnored, EmptyArrayEquality.TypeIgnored));
        assertTrue(nodeEquals(nullNumber, nullComparable, indexes, NullEquality.BetweenAssignableTypes, EmptyArrayEquality.TypeIgnored));
        assertTrue(nodeEquals(nullNumber, nullComparable, indexes, NullEquality.SameTypeOnly, EmptyArrayEquality.TypeIgnored));
    }

    @Test
    public void testGetDeepLength(){
        Object target = null;
        int[][] deepLength = TypeHelper.getDeepLength(target);
        assertValueEquals(new int[][]{new int[]{-1}}, deepLength);

        target = 33;
        deepLength = TypeHelper.getDeepLength(target);
        assertValueEquals(new int[][]{new int[]{0}}, deepLength);

        target = new Integer[]{null};
        deepLength = TypeHelper.getDeepLength(target);
        assertValueEquals(new int[][]{new int[]{0, -1}}, deepLength);

        target = new Integer[]{null, null};
        deepLength = TypeHelper.getDeepLength(target);
        assertValueEquals(new int[][]{new int[]{0, -1}, new int[]{1, -1}}, deepLength);

        target = new Object[]{1,
                new int[]{2, 3},
                new Object[]{null, '5', '6', new Character[]{'7', null}, null},
                null,
                11.0,
                new char[0],
                new ArrayList(),
                new char[0][],
                new int[][]{new int[0], null}};
        deepLength = TypeHelper.getDeepLength(target);
        assertValueEquals(new int[][]{new int[]{0,0}, new int[]{1,0,0}, new int[]{1,1,0}, new int[]{2,0,-1}, new int[]{2,1,0},
            new int[]{2,2,0}, new int[]{2,3,0,0}, new int[]{2,3,1,-1}, new int[]{2,4,-1}, new int[]{3,-1}, new int[]{4,0}
            , new int[]{5,-2}, new int[]{6,0}, new int[]{7,-2}, new int[]{8,0,-2}, new int[]{8,1,-1}}, deepLength);
        assertEquals(16, deepLength.length);

        Object array = new Object[]{1,
                new int[]{2, 3},
                new Object[]{null, '5', '6', null},
                new char[0],
                11.0};
        deepLength = TypeHelper.getDeepLength(array);
        assertValueEquals(new int[][]{new int[]{0,0}, new int[]{1,0,0}, new int[]{1,1,0}, new int[]{2,0,-1}, new int[]{2,1,0},
                new int[]{2,2,0}, new int[]{2,3,-1}, new int[]{3,-2}, new int[]{4,0}}, deepLength);
    }

    @Test
    public void testValueEquals_withSimpleValues(){
        assertTrue(TypeHelper.valueEquals(1, Integer.valueOf(1)));
        assertTrue(TypeHelper.valueEquals('a', Character.valueOf('a')));
        assertTrue(TypeHelper.valueEquals(Boolean.FALSE, false));
        assertTrue(TypeHelper.valueEquals(Double.valueOf(3.3), 3.3d));
        assertTrue(TypeHelper.valueEquals(Float.MIN_VALUE, Float.valueOf(Float.MIN_VALUE)));
        assertTrue(TypeHelper.valueEquals(Long.MAX_VALUE, Long.valueOf(Long.MAX_VALUE)));
        assertTrue(TypeHelper.valueEquals(null, null));

        assertFalse(TypeHelper.valueEquals((byte)33, (short)33));
        assertFalse(TypeHelper.valueEquals(null, true));
        assertFalse(TypeHelper.valueEquals(new int[0], 0));
        assertFalse(TypeHelper.valueEquals(null, Boolean.TRUE));
    }

    @Test
    public void testValueEquals_withTwoArrays(){
        int[] array1 = new int[]{1,2,3};
        Integer[] array2 = new Integer[]{1,2,3};

        assertValueEquals(array1, array2);

        assertValueEquals(new Object[]{new int[]{3,2,1}, (short)3, true, null, new String[]{"S1", "S2"}, new char[0], 1.1d},
                new Object[]{new Integer[]{3,2,1}, Short.valueOf("3"), Boolean.TRUE, null, new String[]{"S1", "S2"}, new Character[0], Double.valueOf(1.1)});

        assertValueEquals(new Object[]{3, (short) 3, true, null, "S1", "S2", 1.1d, DayOfWeek.WEDNESDAY},
                new Comparable[]{3, Short.valueOf("3"), true, null, "S1", "S2", Double.valueOf(1.1d), DayOfWeek.WEDNESDAY});
    }

    @Test
    public void testValueEquals_withDifferentStrategies(){
        assertValueEquals(new Object[]{new int[]{3,2,1}, new short[0], 1.1d, null, new String[]{"S1", "S2", null}, new Integer[]{null, 23}},
                new Object[]{new Integer[]{3,2,1}, new Short[0], Double.valueOf(1.1), null, new Comparable[]{"S1", "S2", null}, new Number[]{null, 23}});
        assertTrue(TypeHelper.valueEquals(new Object[]{new int[]{3,2,1}, new short[0], 1.1d, null, new String[]{"S1", "S2", null}, new Integer[]{null, 23}},
                new Object[]{new Integer[]{3,2,1}, new Short[0], Double.valueOf(1.1), null, new Comparable[]{"S1", "S2", null}, new Number[]{null, 23}},
                NullEquality.BetweenAssignableTypes, EmptyArrayEquality.TypeIgnored));
        assertFalse(TypeHelper.valueEquals(new Object[]{new int[]{3,2,1}, new short[0], 1.1d, null, new String[]{"S1", "S2", null}, new Integer[]{null, 23}},
                new Object[]{new Integer[]{3,2,1}, new Short[0], Double.valueOf(1.1), null, new Comparable[]{"S1", "S2", null}, new Number[]{null, 23}},
                NullEquality.SameTypeOnly, EmptyArrayEquality.TypeIgnored));
        assertFalse(TypeHelper.valueEquals(new Object[]{new int[]{3,2,1}, new short[0], 1.1d, null, new String[]{"S1", "S2", null}, new Integer[]{null, 23}},
                new Object[]{new Integer[]{3,2,1}, new Short[0], Double.valueOf(1.1), null, new Comparable[]{"S1", "S2", null}, new Number[]{null, 23}},
                NullEquality.TypeIgnored, EmptyArrayEquality.SameTypeOnly));
    }

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
        Predicate<Class> classPredicate = getClassEqualitor(int.class);
        assertTrue(classPredicate.test(int.class) && classPredicate.test(Integer.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(int[].class) || classPredicate.test(Integer[].class));

        classPredicate = getClassEqualitor(char.class);
        assertTrue(classPredicate.test(char.class) && classPredicate.test(Character.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(int.class) ||
                classPredicate.test(char[].class) || classPredicate.test(Character[].class));

        classPredicate = getClassEqualitor(boolean.class);
        assertTrue(classPredicate.test(boolean.class) && classPredicate.test(Boolean.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(boolean[].class) || classPredicate.test(Boolean[].class));

        classPredicate = getClassEqualitor(byte.class);
        assertTrue(classPredicate.test(byte.class) && classPredicate.test(Byte.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(byte[].class) || classPredicate.test(Byte[].class));

        classPredicate = getClassEqualitor(double.class);
        assertTrue(classPredicate.test(double.class) && classPredicate.test(Double.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(double[].class) || classPredicate.test(Double[].class));

        classPredicate = getClassEqualitor(float.class);
        assertTrue(classPredicate.test(float.class) && classPredicate.test(Float.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(float[].class) || classPredicate.test(Float[].class));

        classPredicate = getClassEqualitor(short.class);
        assertTrue(classPredicate.test(short.class) && classPredicate.test(Short.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(short[].class) || classPredicate.test(Short[].class));

        classPredicate = getClassEqualitor(long.class);
        assertTrue(classPredicate.test(long.class) && classPredicate.test(Long.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(long[].class) || classPredicate.test(Long[].class));
    }

    @Test
    public void getClassPredicate_withWrapperTypes_shallMatchCorrectly() {
        Predicate<Class> classPredicate = getClassEqualitor(Integer.class);
        assertTrue(classPredicate.test(int.class) && classPredicate.test(Integer.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(int[].class) || classPredicate.test(Integer[].class));

        classPredicate = getClassEqualitor(Character.class);
        assertTrue(classPredicate.test(char.class) && classPredicate.test(Character.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(int.class) ||
                classPredicate.test(char[].class) || classPredicate.test(Character[].class));

        classPredicate = getClassEqualitor(Boolean.class);
        assertTrue(classPredicate.test(boolean.class) && classPredicate.test(Boolean.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(boolean[].class) || classPredicate.test(Boolean[].class));

        classPredicate = getClassEqualitor(Byte.class);
        assertTrue(classPredicate.test(byte.class) && classPredicate.test(Byte.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(byte[].class) || classPredicate.test(Byte[].class));

        classPredicate = getClassEqualitor(Double.class);
        assertTrue(classPredicate.test(double.class) && classPredicate.test(Double.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(double[].class) || classPredicate.test(Double[].class));

        classPredicate = getClassEqualitor(Float.class);
        assertTrue(classPredicate.test(float.class) && classPredicate.test(Float.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(float[].class) || classPredicate.test(Float[].class));

        classPredicate = getClassEqualitor(Short.class);
        assertTrue(classPredicate.test(short.class) && classPredicate.test(Short.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(short[].class) || classPredicate.test(Short[].class));

        classPredicate = getClassEqualitor(Long.class);
        assertTrue(classPredicate.test(long.class) && classPredicate.test(Long.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(long[].class) || classPredicate.test(Long[].class));
    }

    @Test
    public void getClassPredicate_withPrimitiveArraysAndTheirWrappersType_shallMatchCorrectly() {
        Predicate<Class> classPredicate = getClassEqualitor(int[].class);
        assertTrue(classPredicate.test(int[].class) && classPredicate.test(Integer[].class));
        assertFalse(classPredicate.test(null) || classPredicate.test(Object[].class)
                || classPredicate.test(int.class) || classPredicate.test(Integer.class)
                || classPredicate.test(int[][].class) || classPredicate.test(Integer[][].class));

        classPredicate = getClassEqualitor(Integer[].class);
        assertTrue(classPredicate.test(int[].class) && classPredicate.test(Integer[].class));
        assertFalse(classPredicate.test(null) || classPredicate.test(Object[].class)
                || classPredicate.test(int.class) || classPredicate.test(Integer.class)
                || classPredicate.test(int[][].class) || classPredicate.test(Integer[][].class));

        classPredicate = getClassEqualitor(Integer[][].class);
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
        if(TypeHelper.tryParse("EMPTY_ARRAY_AS_DEFAULT", false)){
            assertTrue(TypeHelper.valueEquals(new int[0], (int[])getDefaultValue(int[].class)));
            assertTrue(TypeHelper.valueEquals(new short[0][], getDefaultValue(short[][].class)));
            assertTrue(TypeHelper.valueEquals(new long[0], getDefaultValue(long[].class)));

            assertTrue(TypeHelper.valueEquals(new Byte[0], getDefaultValue(Byte[].class)));
            assertTrue(TypeHelper.valueEquals(new Character[0][], getDefaultValue(Character[][].class)));
            assertTrue(TypeHelper.valueEquals(new Boolean[0], getDefaultValue(Boolean[].class)));

            assertTrue(TypeHelper.valueEquals(new Object[0], getDefaultValue(Object[].class)));
            assertTrue(TypeHelper.valueEquals(new String[0][][], getDefaultValue(String[][][].class)));
            assertTrue(TypeHelper.valueEquals(new Comparable[0], getDefaultValue(Comparable[].class)));
            assertTrue(TypeHelper.valueEquals(new DayOfWeek[0], getDefaultValue(DayOfWeek[].class)));
            assertTrue(TypeHelper.valueEquals(new A[0], getDefaultValue(A[].class)));
            assertTrue(TypeHelper.valueEquals(new ITest1[0], getDefaultValue(ITest1[].class)));

            assertTrue(TypeHelper.valueEquals(new Function[0], getDefaultValue(Function[].class)));
            assertFalse(TypeHelper.valueEquals(new Function[0], getDefaultValue(FunctionThrowable[].class)));
            assertTrue(TypeHelper.valueEquals(new WithValueReturned[0], getDefaultValue(WithValueReturned[].class)));
            assertTrue(TypeHelper.valueEquals(new Predicate[0][], getDefaultValue(Predicate[][].class)));
        }else {
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

        assertEquals(Object.class, getEquivalentClass(Object.class));
        assertEquals(Object.class, getEquivalentClass(Comparable.class));
        assertEquals(Object.class, getEquivalentClass(String.class));
        assertEquals(Object.class, getEquivalentClass(Function.class));
        assertEquals(Object.class, getEquivalentClass(A.class));
        assertEquals(Object.class, getEquivalentClass(ITest1.class));
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

        assertEquals(Object[].class, getEquivalentClass(Object[].class));
        assertEquals(Object[].class, getEquivalentClass(Comparable[].class));
        assertEquals(Object[][].class, getEquivalentClass(String[][].class));
        assertEquals(Object[].class, getEquivalentClass(Function[].class));
        assertEquals(Object[].class, getEquivalentClass(A[].class));
        assertEquals(Object[].class, getEquivalentClass(ITest1[].class));
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
        assertTrue(TypeHelper.valueEquals(new Long[]{1L,2L,3L},  TypeHelper.getToEquivalentParallelConverter(long[].class).apply(new long[]{1,2,3})));
        assertTrue(TypeHelper.valueEquals(new Short[]{1,2,3},  TypeHelper.getToEquivalentParallelConverter(short[].class).apply(new short[]{1,2,3})));
        assertTrue(TypeHelper.valueEquals(new Character[]{'a','b','c'},  TypeHelper.getToEquivalentParallelConverter(char[].class).apply(new char[]{'a','b','c'})));
        assertTrue(TypeHelper.valueEquals(new char[]{'a','b','c'},  TypeHelper.getToEquivalentParallelConverter(char[].class).apply(new char[]{'a','b','c'})));
        assertTrue(TypeHelper.valueEquals(new Byte[]{1,2,3},  TypeHelper.getToEquivalentParallelConverter(byte[].class).apply(new byte[]{1,2,3})));
        assertTrue(TypeHelper.valueEquals(new Boolean[]{true, false},  TypeHelper.getToEquivalentParallelConverter(boolean[].class).apply(new boolean[]{true, false})));
        assertTrue(TypeHelper.valueEquals(new Double[]{1.1,2.2,3.3},  TypeHelper.getToEquivalentParallelConverter(double[].class).apply(new double[]{1.1,2.2,3.3})));
        assertTrue(TypeHelper.valueEquals(new Float[]{-11f,-3.0f,0f},  TypeHelper.getToEquivalentParallelConverter(float[].class).apply(new float[]{-11f,-3.0f,0f})));

        assertTrue(TypeHelper.valueEquals(new int[]{1,2,3},  TypeHelper.getToEquivalentParallelConverter(Integer[].class).apply(new Integer[]{1,2,3})));
        assertTrue(TypeHelper.valueEquals(new long[]{1,2,3},  TypeHelper.getToEquivalentParallelConverter(Long[].class).apply(new Long[]{1L,2L,3L})));
        assertTrue(TypeHelper.valueEquals(new short[]{1,2,3},  TypeHelper.getToEquivalentParallelConverter(Short[].class).apply(new Short[]{1,2,3})));
        assertTrue(TypeHelper.valueEquals(new char[]{'a','b','c'},  TypeHelper.getToEquivalentParallelConverter(Character[].class).apply(new Character[]{'a','b','c'})));
        assertTrue(TypeHelper.valueEquals(new byte[]{1,2,3},  TypeHelper.getToEquivalentParallelConverter(Byte[].class).apply(new Byte[]{1,2,3})));
        assertTrue(TypeHelper.valueEquals(new boolean[]{true, false},  TypeHelper.getToEquivalentParallelConverter(Boolean[].class).apply(new Boolean[]{true, false})));
        assertTrue(TypeHelper.valueEquals(new double[]{1.1,2.2,3.3},  TypeHelper.getToEquivalentParallelConverter(Double[].class).apply(new Double[]{1.1,2.2,3.3})));
        assertTrue(TypeHelper.valueEquals(new float[]{-11f,-3.0f,0f},  TypeHelper.getToEquivalentParallelConverter(Float[].class).apply(new Float[]{-11f,-3.0f,0f})));
        assertTrue(TypeHelper.valueEquals(new Float[]{-11f,-3.0f,0f},  TypeHelper.getToEquivalentParallelConverter(Float[].class).apply(new Float[]{-11f,-3.0f,0f})));

        assertFalse(TypeHelper.valueEquals(new boolean[]{true, false, false},  TypeHelper.getToEquivalentParallelConverter(Boolean[].class).apply(new Boolean[]{true, false, null})));

        assertTrue(TypeHelper.valueEquals(
                new Boolean[][]{new Boolean[]{true, false}, new Boolean[0], null},
                TypeHelper.
                        getToEquivalentParallelConverter(boolean[][].class)
                        .apply(new boolean[][]{new boolean[]{true, false}, new boolean[0], null})));
        assertTrue(TypeHelper.valueEquals(new Byte[][]{new Byte[]{1,2,3}, null},  TypeHelper.getToEquivalentParallelConverter(byte[][].class).apply(new byte[][]{new byte[]{1,2,3}, null})));
        assertTrue(TypeHelper.valueEquals(new Double[][]{new Double[]{1.1}, new Double[]{2.2}, new Double[]{3.3}},
                TypeHelper.getToEquivalentParallelConverter(double[][].class).apply((new double[][]{new double[]{1.1}, new double[]{2.2}, new double[]{3.3}}))));
        assertTrue(TypeHelper.valueEquals(new Float[][]{null},  TypeHelper.getToEquivalentParallelConverter(float[][].class).apply(new float[][]{null})));

        assertTrue(TypeHelper.valueEquals(new int[][]{new int[]{1,2,3}},
                TypeHelper.getToEquivalentParallelConverter(Integer[][].class).apply(new Integer[][]{new Integer[]{1,2,3}})));
        assertTrue(TypeHelper.valueEquals(new long[][]{},  TypeHelper.getToEquivalentParallelConverter(Long[][].class).apply(new Long[][]{})));
        assertTrue(TypeHelper.valueEquals(new short[][]{new short[]{1,2,3}},  TypeHelper.getToEquivalentParallelConverter(Short[][].class).apply(new Short[][]{new Short[]{1,2,3}})));
        assertTrue(TypeHelper.valueEquals(new char[][]{null, null},  TypeHelper.getToEquivalentParallelConverter(Character[][].class).apply(new Character[][]{null, null})));


        Object[] objects = new Object[]{null, 0, void.class, 'a', "", null};
        assertTrue(TypeHelper.valueEquals(objects, TypeHelper.getToEquivalentParallelConverter(Object[].class).apply(objects)));
        Comparable[] comparables = new Comparable[]{1, 33f, -2d, 'a', "abc"};
        assertTrue(TypeHelper.valueEquals(comparables, TypeHelper.getToEquivalentParallelConverter(Comparable[].class).apply(comparables)));
        A[] newA = new A[]{};
        assertTrue(TypeHelper.valueEquals(newA, TypeHelper.getToEquivalentParallelConverter(A.class).apply(newA)));
        String[] strings = new String[]{null, "", "   "};
        assertTrue(TypeHelper.valueEquals(strings, TypeHelper.getToEquivalentParallelConverter(A.class).apply(strings)));
    }

    interface ITest1 {}
    interface ITest2 {}
    class A implements ITest1 {}
    class B extends A {}
    class C extends B implements ITest2{}
    class D implements ITest1, ITest2 {}

    @Test
    public void getClassPredicate_withInterfaceOrClassTypes_shallMatchCorrectly() {
        Predicate<Class> classPredicate = getClassEqualitor(ITest1.class);
        assertTrue(classPredicate.test(A.class) && classPredicate.test(B.class)
                && classPredicate.test(C.class) && classPredicate.test(D.class)
                && classPredicate.test(ITest1.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(Object.class));

        classPredicate = getClassEqualitor(ITest2.class);
        assertTrue(classPredicate.test(C.class) && classPredicate.test(D.class)
                && classPredicate.test(ITest2.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(Object.class)
                || classPredicate.test(A.class) || classPredicate.test(B.class));

        classPredicate = getClassEqualitor(A.class);
        assertTrue(classPredicate.test(A.class) && classPredicate.test(B.class)
                && classPredicate.test(C.class));
        assertFalse(classPredicate.test(null) || classPredicate.test(Object.class)
                || classPredicate.test(D.class) || classPredicate.test(ITest1.class) || classPredicate.test(ITest2.class));

        classPredicate = getClassEqualitor(ITest1[].class);
        assertTrue(classPredicate.test(A[].class) && classPredicate.test(B[].class)
                && classPredicate.test(C[].class) && classPredicate.test(D[].class)
                && classPredicate.test(ITest1[].class));
        assertFalse(classPredicate.test(null) || classPredicate.test(Object.class)
                || classPredicate.test(A.class) || classPredicate.test(ITest1.class) || classPredicate.test(ITest2.class));

        classPredicate = getClassEqualitor(ITest2[].class);
        assertTrue(classPredicate.test(C[].class) && classPredicate.test(D[].class)
                && classPredicate.test(ITest2[].class));
        assertFalse(classPredicate.test(null) || classPredicate.test(Object.class)
                || classPredicate.test(Object[].class)
                || classPredicate.test(A[].class) || classPredicate.test(B[].class)
                || classPredicate.test(C.class) || classPredicate.test(D.class));

        classPredicate = getClassEqualitor(A[].class);
        assertTrue(classPredicate.test(A[].class) && classPredicate.test(B[].class)
                && classPredicate.test(C[].class));
        assertFalse(classPredicate.test(null) || classPredicate.test(Object.class)
                || classPredicate.test(Object[].class)
                || classPredicate.test(A.class) || classPredicate.test(B.class) || classPredicate.test(C.class)
                || classPredicate.test(D[].class) || classPredicate.test(ITest1[].class) || classPredicate.test(ITest2[].class));

        assertTrue(getClassEqualitor(Map.class).test(HashMap.class));
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
        TriConsumerThrowable<Object, Integer, Object> setter = getArrayElementSetter(ints.getClass());
        assertEquals(1, Array.get(ints, 0));
        setter.withHandler(null).accept(ints, 2, 33);
        assertEquals(33, ints[2]);
        
        long[] longs = new long[]{1,2L,3};
        setter = getArrayElementSetter(longs.getClass());
        assertEquals(1L, Array.get(longs, 0));
        setter.withHandler(null).accept(longs, 2, 33L);
        assertEquals(33L, longs[2]);
        
        short[] shorts = new short[]{1,2,3};
        setter = getArrayElementSetter(shorts.getClass());
        assertEquals((short)1, Array.get(shorts, 0));
        setter.withHandler(null).accept(shorts, 2, (short)33);
        assertEquals((short)33, shorts[2]);
        
        char[] chars = new char[]{'a', 'b', 'c'};
        setter = getArrayElementSetter(chars.getClass());
        assertEquals('a', Array.get(chars, 0));
        setter.withHandler(null).accept(chars, 2, 'x');
        assertEquals('x', chars[2]);
        
        byte[] bytes = new byte[]{44, 45, 46};
        setter = getArrayElementSetter(bytes.getClass());
        assertEquals((byte)44, Array.get(bytes, 0));
        setter.withHandler(null).accept(bytes, 2, (byte)55);
        assertEquals((byte)55, bytes[2]);
        
        boolean[] booleans = new boolean[]{true, false};
        setter = getArrayElementSetter(booleans.getClass());
        assertEquals(true, Array.get(booleans, 0));
        setter.withHandler(null).accept(booleans, 1, true);
        assertEquals(true, booleans[1]);
        
        double[] doubles = new double[]{1.0, 2.0, 3.0};
        setter = getArrayElementSetter(doubles.getClass());
        assertEquals(1.0, Array.get(doubles, 0));
        setter.withHandler(null).accept(doubles, 1, 100);
        assertEquals(100.0, doubles[1], 0);
        
        float[] floats = new float[]{1.1f, 2.2f, 3.3f};
        setter = getArrayElementSetter(floats.getClass());
        assertEquals(1.1f, Array.get(floats, 0));
        setter.withHandler(null).accept(floats, 1, 101.3f);
        assertEquals(101.3f, floats[1], 0);
    }

    @Test
    public void getSetArrayElement_withWrapperArray_bothGetterSetterWorks() {
        Integer[] ints2 = new Integer[]{1,2,3};
        TriConsumerThrowable<Object, Integer, Object> setter = getArrayElementSetter(ints2.getClass());
        assertEquals(1, Array.get(ints2, 0));
        setter.withHandler(null).accept(ints2, 2, 33);
        assertEquals(Integer.valueOf(33), ints2[2]);
        
        Long[] longs2 = new Long[]{1L,2L,3L};
        setter = getArrayElementSetter(longs2.getClass());
        assertEquals(1L, Array.get(longs2, 0));
        setter.withHandler(null).accept(longs2, 2, 33L);
        assertEquals(Long.valueOf(33), longs2[2]);
        
        Short[] shorts2 = new Short[]{1,2,3};
        setter = getArrayElementSetter(shorts2.getClass());
        assertEquals(Short.valueOf((short)1), Array.get(shorts2, 0));
        setter.withHandler(null).accept(shorts2, 2, Short.valueOf((short)45));
        assertEquals(Short.valueOf((short)45), shorts2[2]);
        
        Character[] chars2 = new Character[]{'a', 'b', 'c'};
        setter = getArrayElementSetter(chars2.getClass());
        assertEquals(Character.valueOf('a'), Array.get(chars2, 0));
        setter.withHandler(null).accept(chars2, 2, 'x');
        assertEquals(Character.valueOf('x'), chars2[2]);
        
        Byte[] bytes2 = new Byte[]{44, 45, 46};
        setter = getArrayElementSetter(bytes2.getClass());
        assertEquals(Byte.valueOf("44"), Array.get(bytes2, 0));
        setter.withHandler(null).accept(bytes2, 2, (byte)55);
        assertEquals(Byte.valueOf("55"), bytes2[2]);
        
        Boolean[] booleans2 = new Boolean[]{true, false};
        setter = getArrayElementSetter(booleans2.getClass());
        assertEquals(true, Array.get(booleans2, 0));
        setter.withHandler(null).accept(booleans2, 1, true);
        assertEquals(true, booleans2[1]);
        
        Double[] doubles2 = new Double[]{1.0, 2.0, 3.0};
        setter = getArrayElementSetter(doubles2.getClass());
        assertEquals(1.0, Array.get(doubles2, 0));
        setter.withHandler(null).accept(doubles2, 1, 100.0);
        assertEquals(100.0, doubles2[1], 0);
        
        Float[] floats2 = new Float[]{1.1f, 2.2f, 3.3f};
        setter = getArrayElementSetter(floats2.getClass());
        assertEquals(1.1f, Array.get(floats2, 0));
        setter.withHandler(null).accept(floats2, 1, 101.3f);
        assertEquals(101.3f, floats2[1], 0);
    }

    @Test
    public void getSetArrayElement_withClassOrInterfaceType_bothGetterSetterWorks() {
        ITest1[] interfaces1 = new ITest1[]{null, new A(), new B(), new C(), new D()};
        TriConsumerThrowable<Object, Integer, Object> setter = getArrayElementSetter(interfaces1.getClass());
        assertEquals(null, Array.get(interfaces1, 0));
        assertEquals(A.class, Array.get(interfaces1, 1).getClass());
        assertEquals(D.class, Array.get(interfaces1, 4).getClass());
        setter.withHandler(null).accept(interfaces1, 0, new D());
        assertEquals(D.class, Array.get(interfaces1, 0).getClass());

        A[] aArray = new A[] { new A(), new B(), new C()};
        setter = getArrayElementSetter(aArray.getClass());
        assertEquals(A.class, Array.get(aArray, 0).getClass());
        assertEquals(B.class, Array.get(aArray, 1).getClass());

        //Invalid setting operation would not update the element
        setter.withHandler(null).accept(aArray, 1, new D());
        assertEquals(B.class, Array.get(aArray, 1).getClass());

        //set element@1 to new C()
        setter.withHandler(null).accept(aArray, 1, new C());
        assertEquals(C.class, Array.get(aArray, 1).getClass());
    }

    @Test
    public void getSetArrayElement_withMultiDimensionArrayTypes_bothGetterSetterWorks() {
        int[][] ints = new int[][]{ new int[]{1,2,3}, null};
        TriConsumerThrowable<Object, Integer, Object> setter = getArrayElementSetter(ints.getClass());
        assertEquals(ints[0], Array.get(ints, 0));
        setter.withHandler(null).accept(ints, 1, new int[0]);
        assertEquals(0, ints[1].length);

        long[][] longs = new long[][]{new long[]{1,2L,3}, null};
        setter = getArrayElementSetter(longs.getClass());
        assertEquals(longs[0], Array.get(longs, 0));
        setter.withHandler(null).accept(longs, 0, null);
        assertNull(longs[0]);

        short[][] shorts = new short[][]{new short[]{1,2,3}, null};
        setter = getArrayElementSetter(shorts.getClass());
        assertEquals(shorts[0], Array.get(shorts, 0));
        setter.withHandler(null).accept(shorts, 0, null);
        assertNull(shorts[0]);

        char[][] chars = new char[][]{new char[]{'a', 'b', 'c'}, null};
        setter = getArrayElementSetter(chars.getClass());
        assertEquals(chars[0], Array.get(chars, 0));
        setter.withHandler(null).accept(chars, 0, null);
        assertNull(chars[0]);

        byte[][] bytes = new byte[][]{new byte[]{44, 45, 46}, null};
        setter = getArrayElementSetter(bytes.getClass());
        assertEquals(bytes[0], Array.get(bytes, 0));
        setter.withHandler(null).accept(bytes, 0, null);
        assertNull(bytes[0]);

        boolean[][] booleans = new boolean[][]{new boolean[]{true, false}, null};
        setter = getArrayElementSetter(booleans.getClass());
        assertEquals(booleans[0], Array.get(booleans, 0));
        setter.withHandler(null).accept(booleans, 0, null);
        assertNull(booleans[0]);

        double[][] doubles = new double[][]{new double[]{1.0, 2.0, 3.0}, null};
        setter = getArrayElementSetter(doubles.getClass());
        assertEquals(doubles[0], Array.get(doubles, 0));
        setter.withHandler(null).accept(doubles, 0, null);
        assertNull(doubles[0]);

        float[][] floats = new float[][]{new float[]{1.1f, 2.2f, 3.3f}, null};
        setter = getArrayElementSetter(floats.getClass());
        assertEquals(floats[0], Array.get(floats, 0));
        setter.withHandler(null).accept(floats, 0, null);
        assertNull(floats[0]);

        Integer[][] ints2 = new Integer[][]{new Integer[]{1,2,3}, null};
        setter = getArrayElementSetter(ints2.getClass());
        assertEquals(ints2[0], Array.get(ints2, 0));
        setter.withHandler(null).accept(ints2, 0, null);
        assertNull(ints2[0]);

        Long[][] longs2 = new Long[][]{new Long[]{1L,2L,3L}, null};
        setter = getArrayElementSetter(longs2.getClass());
        assertEquals(longs2[0], Array.get(longs2, 0));
        setter.withHandler(null).accept(longs2, 0, null);
        assertNull(longs2[0]);

        Short[][] shorts2 = new Short[][]{new Short[]{1,2,3}, null};
        setter = getArrayElementSetter(shorts2.getClass());
        assertEquals(shorts2[0], Array.get(shorts2, 0));
        setter.withHandler(null).accept(shorts2, 0, null);
        assertNull(shorts2[0]);

        Character[][] chars2 = new Character[][]{new Character[]{'a', 'b', 'c'}, null};
        setter = getArrayElementSetter(chars2.getClass());
        assertEquals(chars2[0], Array.get(chars2, 0));
        setter.withHandler(null).accept(chars2, 0, null);
        assertNull(chars2[0]);

        Byte[][] bytes2 = new Byte[][]{new Byte[]{44, 45, 46}, null};
        setter = getArrayElementSetter(bytes2.getClass());
        assertEquals(bytes2[0], Array.get(bytes2, 0));
        setter.withHandler(null).accept(bytes2, 0, null);
        assertNull(bytes2[0]);

        Boolean[][] booleans2 = new Boolean[][]{new Boolean[]{true, false}, null};
        setter = getArrayElementSetter(booleans2.getClass());
        assertEquals(booleans2[0], Array.get(booleans2, 0));
        setter.withHandler(null).accept(booleans2, 0, null);
        assertNull(booleans2[0]);

        Double[][] doubles2 = new Double[][]{new Double[]{1.0, 2.0, 3.0}, null};
        setter = getArrayElementSetter(doubles2.getClass());
        assertEquals(doubles2[0], Array.get(doubles2, 0));
        setter.withHandler(null).accept(doubles2, 0, null);
        assertNull(doubles2[0]);

        Float[][] floats2 = new Float[][]{new Float[]{1.1f, 2.2f, 3.3f}, null};
        setter = getArrayElementSetter(floats2.getClass());
        assertEquals(floats2[0], Array.get(floats2, 0));
        setter.withHandler(null).accept(floats2, 0, null);
        assertNull(floats2[0]);

        ITest1[][] interfaces1 = new ITest1[][]{new ITest1[]{null, new A(), new B(), new C(), new D()}, null};
        setter = getArrayElementSetter(interfaces1.getClass());
        assertEquals(interfaces1[0], Array.get(interfaces1, 0));
        assertEquals(null, Array.get(interfaces1, 1));
        setter.withHandler(null).accept(interfaces1, 0, null);
        assertNull(interfaces1[0]);

        A[][] aArray = new A[][]{ new A[] { new A(), new B(), new C()}, null };
        setter = getArrayElementSetter(aArray.getClass());
        assertEquals(A[].class, Array.get(aArray, 0).getClass());

        //Invalid setting operation would not update the element
        setter.withHandler(null).accept(aArray, 0, null);
        assertNull(aArray[0]);
    }

    private void assertCopying(Object expected, TriFunctionThrowable<Object, Integer, Integer, Object> copier, Object original, int from, int to){
        Object copy = copier.orElse(null).apply(original, from, to);
        if(expected == null){
            assertNull(copy);
            return;
        }

        assertEquals(expected.getClass(), copy.getClass());
        assertTrue(TypeHelper.valueEquals(expected, copy));
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

    @Test
    public void rangeCopyOf_primitiveTypes_getPrimitiveArray(){
        assertValueEquals(new int[]{1,2,0,0}, (int[])TypeHelper.copyOfRange(new int[]{1,2}, 0, 4));
        assertValueEquals(new char[]{'a', 'b'}, TypeHelper.copyOfRange(new char[]{'a', 'b', 'c'}, 0, 2));
        assertValueEquals(new byte[]{}, TypeHelper.copyOfRange(new byte[]{1,2}, 0, 0));
        assertValueEquals(new float[]{1f,2f}, TypeHelper.copyOfRange(new float[]{0f, 1f,2f, 3f}, 1, 3));
        assertValueEquals(new boolean[]{false, true, false, false}, TypeHelper.copyOfRange(
                new boolean[]{true, true, false, false, true, false}, 3, 7));
        assertValueEquals(new byte[]{}, TypeHelper.copyOfRange(new byte[]{1,2}, 0, 0));
        assertValueEquals(new long[]{1L, 2L}, TypeHelper.copyOfRange(new long[]{0L,1L,2L}, 1, 3));
        assertValueEquals(new short[]{3,4,0}, TypeHelper.copyOfRange(new short[]{1,2,3,4}, 2, 5));
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

    @Test
    public void testToEquivalent(){
        //For primitive types
        assertEquals(Integer.valueOf(45), TypeHelper.toEquivalent(45));
        Object converted = TypeHelper.toEquivalent(new int[]{1,2,3});
        assertTrue(converted.getClass().equals(Integer[].class) && TypeHelper.valueEquals(new int[]{1,2,3}, converted));
        converted = TypeHelper.toEquivalent(new int[][]{new int[]{1,2}, null, new int[]{3}});
        assertTrue(converted.getClass().equals(Integer[][].class) &&
                TypeHelper.valueEquals(new int[][]{new int[]{1,2}, null, new int[]{3}}, converted));

        //For wrapper types
        assertEquals(45, TypeHelper.toEquivalent(Integer.valueOf(45)));
        assertNull(TypeHelper.toEquivalent(new Integer[]{1,2,null}));
        converted = TypeHelper.toEquivalent(new Integer[]{1,2,3});
        assertTrue(converted.getClass().equals(int[].class) && TypeHelper.valueEquals(new int[]{1,2,3}, converted));
        converted = TypeHelper.toEquivalent(new Integer[][]{new Integer[]{1,2}, new Integer[]{3}, null, new Integer[]{4,5}});
        assertTrue(converted.getClass().equals(int[][].class) &&
                TypeHelper.valueEquals(new int[][]{new int[]{1,2}, new int[]{3}, null, new int[]{4,5}}, converted));

        //Other types
        String test = "test";
        converted = TypeHelper.toEquivalent(test);
        assertEquals(test, converted);
        converted = TypeHelper.toEquivalent(new String[]{"a", "b"});
        assertTrue(converted.getClass().equals(Object[].class) && TypeHelper.valueEquals(new String[]{"a", "b"}, converted));

        Number[] numbers = new Number[]{1, 2d, 3f, 4L};
        converted = TypeHelper.toEquivalent(numbers);
        assertTrue(converted.getClass().equals(Object[].class) && Array.getLength(converted)==4);

        Comparable[][][] comparables = new Comparable[][][]{ new Integer[][]{new Integer[0], new Integer[]{1}, new Integer[]{2,3}},
                new String[][]{null, new String[]{"string1"}}, new Character[][]{new Character[]{'1'}}, new Boolean[][]{new Boolean[0], new Boolean[]{true, false}}};
        converted = TypeHelper.toEquivalent(comparables);
        assertTrue(converted.getClass().equals(Object[][][].class) && TypeHelper.valueEquals(comparables, converted));
    }

    @Test
    public void testConvert() {
        //For single object
        assertEquals(Integer.valueOf(3), TypeHelper.convert(3, Integer.class));
        assertEquals(true, TypeHelper.convert(Boolean.TRUE, boolean.class));

        Object anyObject = new Object[]{1.0, 2f, 3L, 4, 5};
        Object converted = TypeHelper.convert(anyObject, Number[].class);
        assertTrue(converted.getClass().equals(Number[].class) && TypeHelper.valueEquals(converted, anyObject));
        converted = TypeHelper.convert(anyObject, Comparable[].class);
        assertTrue(converted.getClass().equals(Comparable[].class) && TypeHelper.valueEquals(converted, anyObject));

        Integer[][][] integers = new Integer[][][]{new Integer[][]{new Integer[]{1}, null}, new Integer[0][]};
        converted = TypeHelper.convert(integers, Object[].class);
        //The Integer[][][] is converted to Object[]{Integer[][], Integer[][]}
        assertTrue(converted.getClass().equals(Object[].class) && TypeHelper.valueEquals(converted, integers));

        Number[] numbers = new Number[]{1, 2, 3, 4f, (byte)5, 6f};
        Comparable[] comparables = TypeHelper.convertParallel(numbers, Comparable[].class);
        assertNull(comparables);

        Object[] objects = TypeHelper.convertSerial(numbers, Object[].class);
        assertTrue(TypeHelper.valueEquals(numbers, objects));
    }
}