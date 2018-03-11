package com.easyworks.utility;

import com.easyworks.Functions;
import com.easyworks.function.BiFunctionThrowable;
import com.easyworks.function.FunctionThrowable;
import com.easyworks.repository.*;
import com.easyworks.tuple.Dual;
import com.easyworks.tuple.Quad;
import com.easyworks.tuple.Tuple;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;

public class ArrayHelper {

    private static <T> T[] defaultArrayFactory(Class<T> clazz, int length){
        return (T[]) Array.newInstance(clazz, length);
    }

    public static final DualValuesRepository<Class, Class, FunctionThrowable<Integer, Object>> classFactories = MultiValuesRepository.toDualValuesRepository(
            () -> new HashMap<Class, Dual<Class, FunctionThrowable<Integer, Object>>>(){{
                put(int.class, Tuple.create(int[].class, i -> new int[i]));
                put(char.class, Tuple.create(char[].class, i -> new char[i]));
                put(byte.class, Tuple.create(byte[].class, i -> new byte[i]));
                put(boolean.class, Tuple.create(boolean[].class, i -> new boolean[i]));
                put(short.class, Tuple.create(short[].class, i -> new short[i]));
                put(long.class, Tuple.create(long[].class, i -> new long[i]));
                put(float.class, Tuple.create(float[].class, i -> new float[i]));
                put(double.class, Tuple.create(double[].class, i -> new double[i]));
                put(Object[].class, Tuple.create(Object.class, i -> new Object[i]));
            }},
            null,
            clazz -> Tuple.create( null, length -> Array.newInstance(clazz, length)
    ));

    public static Object getArray(Class clazz, int length) {
        FunctionThrowable<Integer, Object> factory = classFactories.getSecondValue(clazz);
        return Functions.Default.apply(factory, length);
    }

    public static final TripleValuesRepository.TripleValuesRepository2<Class, Class, FunctionThrowable<Object, Integer>, BiFunctionThrowable<Object, Integer, Object>, FunctionThrowable<Object, Object>> arrayConverters = TupleRepository.toTripleValuesRepository(
            (fromClass, toClass) -> {

            }
    );

    private static <T, R> Function<Object,R[]> getArrayConverter(
            Class<T> fromClass, Class<R> toClass,
            FunctionThrowable<Object, Integer> getLength,
            BiFunctionThrowable<Object, Integer, T> getElementByIndex,
            FunctionThrowable<T, R> elementConverter){
        Objects.requireNonNull(fromClass);
        Objects.requireNonNull(toClass);
        if(fromClass.equals(toClass))
            return array -> (array==null || !array.getClass().isArray()) ? null : (R[])array;

        //Try best to prepare default getLength, getElementByIndex and elementConverter
        final FunctionThrowable<Object, Integer> getLengthFinal = getLength == null ?
                getLength: array -> Array.getLength(array);
        final BiFunctionThrowable<Object, Integer, T> getElement = getElementByIndex == null ?
                getElementByIndex : (array, i) -> ((T[])array)[i];
        final FunctionThrowable<T, R> toResultElement = elementConverter == null ?
                elementConverter : t -> (R)t;

        return array -> {
            if (array == null) return null;
            try {
                int length = getLengthFinal.apply(array);
                R[] toArray = (R[]) getArray(toClass, length);
                for (int i = 0; i < length; i++) {
                    toArray[i] = toResultElement.apply(getElement.apply(array, i));
                }
                return toArray;
            }catch (Exception ex){
                return null;
            }
        };
    }

    public static final Map<Class, Quad<Class, Class, FunctionThrowable<Object, Integer>, BiFunctionThrowable<Object, Integer, Object>>> valuesMapper
            = new HashMap<Class, Quad<Class, Class, FunctionThrowable<Object, Integer>, BiFunctionThrowable<Object, Integer, Object>>>(){
        {
            put(byte[].class, Tuple.create(byte.class, Byte.class, array -> ((byte[]) array).length,
                    (array, i) -> Byte.valueOf(((byte[]) array)[i])));
            put(boolean[].class, Tuple.create(boolean.class, Boolean.class, array -> ((boolean[]) array).length,
                    (array, i) -> Boolean.valueOf(((boolean[]) array)[i])));
            put(char[].class, Tuple.create(char.class, Character.class, array -> ((char[]) array).length,
                    (array, i) -> Character.valueOf(((char[]) array)[i])));
            put(float[].class, Tuple.create(float.class, Float.class, array -> ((float[]) array).length,
                    (array, i) -> Float.valueOf(((float[]) array)[i])));
            put(int[].class, Tuple.create(int.class, Integer.class, array -> ((int[]) array).length,
                    (array, i) -> Integer.valueOf(((int[]) array)[i])));
            put(double[].class, Tuple.create(double.class, Double.class, array -> ((double[]) array).length,
                    (array, i) -> Double.valueOf(((double[]) array)[i])));
            put(short[].class, Tuple.create(short.class, Short.class, array -> ((short[]) array).length,
                    (array, i) -> Short.valueOf(((short[]) array)[i])));
            put(long[].class, Tuple.create(long.class, Long.class, array -> ((long[]) array).length,
                    (array, i) -> Long.valueOf(((long[]) array)[i])));
        }
    };

    private static Object[] asObjects(Object array){
        if(array == null)
            return null;

        Class arrayClass = array.getClass();
        Quad<Class, Class, FunctionThrowable<Object, Integer>, BiFunctionThrowable<Object, Integer, Object>> quad =
                valuesMapper.get(arrayClass);
        return (Object[]) Functions.Default.apply(() -> toObjects(array, quad.getSecond(), quad.getThird().apply(array), quad.getFourth()));
    }

    private static <O> Object[] toObjects(Object values, Class<O> objectClass, int size, BiFunctionThrowable<Object, Integer, O> getValueAt)
            throws Exception {
        if(values == null)
            return null;
        Object[] objects = new Object[size];
        for (int i = 0; i < size; i++) {
            objects[i] = getValueAt.apply(values, i);
        }
        return objects;
    }

    private static <T> T[] toArray(Object values, Class<T> objectClass, int size, BiFunctionThrowable<Object, Integer, T> getValueAt)
            throws Exception {
        if(values == null)
            return null;
        T[] result = (T[]) Array.newInstance(objectClass, size);
        for (int i = 0; i < size; i++) {
            result[i] = getValueAt.apply(values, i);
        }
        return result;
    }

    private static <T> T[] asArray(Object values){
        if(values == null)
            return null;

        Class arrayClass = values.getClass();
        Quad<Class, Class, FunctionThrowable<Object, Integer>, BiFunctionThrowable<Object, Integer, Object>> quad =
                valuesMapper.get(arrayClass);
        return (T[]) Functions.Default.apply(() -> toArray(values, quad.getSecond(), quad.getThird().apply(values), quad.getFourth()));
    }

    public static Object[] toObjects(boolean[] values){
        return asObjects(values);
    }

    public static Object[] toObjects(byte[] values){
        return asObjects(values);
    }

    public static Object[] toObjects(int[] values){
        return asObjects(values);
    }

    public static Object[] toObjects(char[] values){
        return asObjects(values);
    }

    public static Object[] toObjects(short[] values){
        return asObjects(values);
    }

    public static Object[] toObjects(long[] values){
        return asObjects(values);
    }

    public static Object[] toObjects(float[] values){
        return asObjects(values);
    }

    public static Object[] toObjects(double[] values){
        return asObjects(values);
    }

    //*/

    public static Boolean[] toArray(boolean[] values){
        return asArray(values);
    }

    public static Byte[] toArray(byte[] values){
        return asArray(values);
    }
    
    public static Character[] toArray(char[] values){
        return asArray(values);
    }

    public static Float[] toArray(float[] values){
        return asArray(values);
    }

    public static Double[] toArray(double[] values){
        return asArray(values);
    }

    public static Integer[] toArray(int[] values){
        return asArray(values);
    }

    public static Short[] toArray(short[] values){
        return asArray(values);
    }

    public static Long[] toArray(long[] values){
        return asArray(values);
    }

    /*/
    public static Character[] toArray(char[] values){
        if(values == null)
            return null;

        return (Character[]) Functions.Default.apply(() ->
                toArray(values, Character.class, values.length, (a, i)->Character.valueOf(values[i])));
    }

    public static Float[] toArray(float[] values){
        if(values == null)
            return null;

        return (Float[]) Functions.Default.apply(() ->
                toArray(values, Float.class, values.length, (a, i)->Float.valueOf(values[i])));
    }

    public static Double[] toArray(double[] values){
        if(values == null)
            return null;

        return (Double[]) Functions.Default.apply(() ->
                toArray(values, Double.class, values.length, (a, i)->Double.valueOf(values[i])));
    }

    public static Integer[] toArray(int[] values){
        if(values == null)
            return null;

        return (Integer[]) Functions.Default.apply(() ->
                toArray(values, Integer.class, values.length, (a, i)->Integer.valueOf(values[i])));
    }

    public static Short[] toArray(short[] values){
        if(values == null)
            return null;

        return (Short[]) Functions.Default.apply(() ->
                toArray(values, Short.class, values.length, (a, i)->Short.valueOf(values[i])));
    }

    public static Long[] toArray(long[] values){
        if(values == null)
            return null;

        return (Long[]) Functions.Default.apply(() ->
                toArray(values, Long.class, values.length, (a, i)->Long.valueOf(values[i])));
    }

    public static Byte[] toArray(byte[] values){
        if(values == null)
            return null;

        return (Byte[]) Functions.Default.apply(() ->
                toArray(values, Byte.class, values.length, (a, i)->Byte.valueOf(values[i])));
    }

    public static Boolean[] toArray(boolean[] values){
        if(values == null)
            return null;

        return (Boolean[]) Functions.Default.apply(() ->
                toArray(values, Boolean.class, values.length, (a, i)->Boolean.valueOf(values[i])));
    }
    //*/

    public static <T> T[] toArray(Collection<T> collection, Class<T> clazz){
        Objects.requireNonNull(collection);
        Objects.requireNonNull(clazz);
        T[] array = (T[])collection.toArray((T[]) Array.newInstance(clazz, 0));
        return array;
    }


    public static <T extends Comparable<T>> boolean matchInOrder(T[] expected, T[] actual) {
        if(expected.length != actual.length)
            return false;

        if(!Arrays.deepEquals(expected, actual)){
            Logger.L("Expected: %s != %s", Tuple.asTuple(expected), Tuple.asTuple(actual));
            return false;
        }
        return true;
    }

    public static <T extends Comparable<T>> boolean matchInOrder(Collection<T> expected, Collection<T> actual, Class<T> clazz) {
        int size = expected.size();
        if(size != actual.size())
            return false;
        T[] expectedArray = toArray(expected, clazz);
        T[] actualArray = toArray(actual, clazz);

        return matchInOrder(expectedArray, actualArray);
    }

    public static <T extends Comparable<T>> boolean matchWithoutOrder(T[] expected, T[] actual){
        if(expected.length != actual.length)
            return false;

        Arrays.sort(expected);
        Arrays.sort(actual);
        if(!Arrays.deepEquals(expected, actual)){
            return false;
        }
        return true;
    }

    public static <T extends Comparable<T>> boolean matchWithoutOrder(Collection<T> expected, Collection<T> actual, Class<T> clazz) {
        int size = expected.size();
        if(size != actual.size())
            return false;
        T[] expectedArray = toArray(expected, clazz);
        T[] actualArray = toArray(actual, clazz);

        return matchWithoutOrder(expectedArray, actualArray);
    }

}
