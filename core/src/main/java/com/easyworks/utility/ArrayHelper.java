package com.easyworks.utility;

import com.easyworks.Functions;
import com.easyworks.function.*;
import com.easyworks.repository.*;
import com.easyworks.tuple.Hexa;
import com.easyworks.tuple.Quad;
import com.easyworks.tuple.Single;
import com.easyworks.tuple.Tuple;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;

public class ArrayHelper<T, R> {
    public static final Class ObjectClass = Object.class;
    public static final Class ArrayClass = Array.class;
    public static int ParalellEvaluationThreashold = 100;
    public static long ParalellEvaluationTimeoutMills = 30 * 60 * 1000;

    private static <T> T[] defaultArrayFactory(Class<T> clazz, int length) {
        return (T[]) Array.newInstance(clazz, length);
    }

    protected static final QuadValuesRepository<
            Class,

            Class                                  //Wrap class of the value
            , FunctionThrowable<Integer, Object>     //Factory to create T[]
            , BiFunctionThrowable<Object, Integer, Object>   //Get an element of T[] with Index
            , TriConsumerThrowable<Object, Integer, Object> //Set an element of T[] at Index with value
//            , TriFunctionThrowable<Object, Integer, Integer, Object>    //Copies the specified range of the specified array into a new array. with fromIndex and toIndex
//            , QuadFunctionThrowable<Objects, Integer, Integer, Object, Integer>   //Search a range of specific array with a specific key
            >
            classArrayFactories = MultiValuesRepository.toQuadValuesRepository(
            () -> new HashMap<Class, Quad<
                    Class,
                    FunctionThrowable<Integer, Object>,
                    BiFunctionThrowable<Object, Integer, Object>,
                    TriConsumerThrowable<Object, Integer, Object>>
//                    TriFunctionThrowable<Object, Integer, Integer, Object>,
//                    QuadFunctionThrowable.QuadFunction<Objects, Integer, Integer, Object, Integer>
                    >() {{
                put(int.class, Tuple.create(
                        Integer.class,
                        i -> new int[i],
                        Array::getInt,
                        (array, index, value) -> Array.setInt(array, index, (int) value))
//                        (original, fromIndex, toIndex) -> Arrays.copyOfRange(original, fromIndex, toIndex),
//                        (array, fromIndex, toIndex, key) -> Arrays.binarySearch(array, fromIndex, toIndex, (int)key)
                );
                put(char.class, Tuple.create(
                        Character.class,
                        i -> new char[i],
                        Array::getChar,
                        (array, index, value) -> Array.setChar(array, index, (char) value)));
                put(byte.class, Tuple.create(
                        Byte.class,
                        i -> new byte[i],
                        Array::getByte,
                        (array, index, value) -> Array.setByte(array, index, (byte) value)));
                put(boolean.class, Tuple.create(
                        Boolean.class,
                        i -> new boolean[i],
                        Array::getBoolean,
                        (array, index, value) -> Array.setBoolean(array, index, (boolean) value)));
                put(short.class, Tuple.create(
                        Short.class,
                        i -> new short[i],
                        Array::getShort,
                        (array, index, value) -> Array.setShort(array, index, (short) value)));
                put(long.class, Tuple.create(
                        Long.class,
                        i -> new long[i],
                        Array::getLong,
                        (array, index, value) -> Array.setLong(array, index, (long) value)));
                put(float.class, Tuple.create(
                        Float.class,
                        i -> new float[i],
                        Array::getFloat,
                        (array, index, value) -> Array.setFloat(array, index, (float) value)));
                put(double.class, Tuple.create(
                        Double.class,
                        i -> new double[i],
                        Array::getDouble,
                        (array, index, value) -> Array.setDouble(array, index, (double) value)));
                put(Object[].class, Tuple.create(
                        Object.class,
                        i -> new Object[i],
                        Array::get,
                        Array::set));
            }},
            null,
            clazz -> Tuple.create(
                    clazz
                    , (Integer length) -> Array.newInstance(clazz, length)
                    , Array::get
                    , Array::set
//                    , (Object array, Integer fromIndex, Integer toIndex) -> Arrays.copyOfRange((T[])array, fromIndex, toIndex)
            )
    );

    public static Class getComponentType(Object array) {
        if (array == null) return null;
        Class arrayClass = array.getClass();
        if (!arrayClass.isArray())
            return null;

        return arrayClass.getComponentType();
    }

    public static Class objectify(Class clazz) {
        if (clazz == null)
            return null;
        return classArrayFactories.containsKey(clazz) ? classArrayFactories.getFirstValue(clazz) : clazz;
    }

    public static Class[] objectify(Class[] classes) {
        if (classes == null)
            return null;

        int size = classes.length;
        Class[] result = new Class[size];
        for (int i = 0; i < size; i++) {
            result[i] = objectify(classes[i]);
        }
        return result;
    }


    public static Object getNewArray(Class clazz, int length) {
        FunctionThrowable<Integer, Object> factory = classArrayFactories.getSecondValue(clazz);
        return Functions.Default.apply(factory, length);
    }

    public static Object getNewArray(Class clazz, int length, FunctionThrowable<Integer, Object> elementAtIndexFactory) {
        FunctionThrowable<Integer, Object> factory = classArrayFactories.getSecondValue(clazz);
        return Functions.Default.apply(factory, length);
    }

    public static TriConsumerThrowable<Object, Integer, Object> getArraySetter(Class clazz){
        return classArrayFactories.getFourthValue(clazz);
    }


    public static final SingleValuesRepository.SingleValuesRepository6<
            Class,              //fromClass as the first key
            Class,              //toClass as the second key
            BiFunctionThrowable<Object, Integer, Object>, //Function to get the Element At Index of the FromArray
            TriConsumerThrowable<Object, Integer, Object>, //Function to set the Element At Index of the FromArray
            Function<Object, Object>,             // Optional elementConverter of the fromElement to toElement
            Boolean,            //parallelEvaluationRquired

            Function<Object, Object>       //Final converter based on the given keys

            >
            arrayConverters = TupleRepository.toSingleValuesRepository(
            () -> new HashMap<
                    Hexa<Class, Class, BiFunctionThrowable<Object, Integer, Object>, TriConsumerThrowable<Object, Integer, Object>, Function<Object, Object>, Boolean>,
                    Single<Function<Object, Object>>>() {{
                //Convert array of primitive values to corresponding array of wrapper of the primitive values
                put(Tuple.create(boolean.class, Boolean.class, null, null, null, null),
                        Tuple.create(getArrayConverter(boolean.class, Boolean.class, null, null, null, null)));
                put(Tuple.create(byte.class, Byte.class, null, null, null, null),
                        Tuple.create(getArrayConverter(byte.class, Byte.class, null, null, null, null)));
                put(Tuple.create(char.class, Character.class, null, null, null, null),
                        Tuple.create(getArrayConverter(char.class, Character.class, null, null, null, null)));
                put(Tuple.create(int.class, Integer.class, null, null, null, null),
                        Tuple.create(getArrayConverter(int.class, Integer.class, null, null, null, null)));
                put(Tuple.create(short.class, Short.class, null, null, null, null),
                        Tuple.create(getArrayConverter(short.class, Short.class, null, null, null, null)));
                put(Tuple.create(long.class, Long.class, null, null, null, null),
                        Tuple.create(getArrayConverter(long.class, Long.class, null, null, null, null)));
                put(Tuple.create(float.class, Float.class, null, null, null, null),
                        Tuple.create(getArrayConverter(float.class, Float.class, null, null, null, null)));
                put(Tuple.create(double.class, Double.class, null, null, null, null),
                        Tuple.create(getArrayConverter(double.class, Double.class, null, null, null, null)));

                //Convert array of wrapper of the primitive values back to array of primitive values
                put(Tuple.create(Boolean.class, boolean.class, null, null, null, null),
                        Tuple.create(getArrayConverter(Boolean.class, boolean.class, null, null, null, null)));
                put(Tuple.create(Byte.class, byte.class, null, null, null, null),
                        Tuple.create(getArrayConverter(Byte.class, byte.class, null, null, null, null)));
                put(Tuple.create(Character.class, char.class, null, null, null, null),
                        Tuple.create(getArrayConverter(Character.class, char.class, null, null, null, null)));
                put(Tuple.create(Integer.class, int.class, null, null, null, null),
                        Tuple.create(getArrayConverter(Integer.class, int.class, null, null, null, null)));
                put(Tuple.create(Short.class, short.class, null, null, null, null),
                        Tuple.create(getArrayConverter(Short.class, short.class, null, null, null, null)));
                put(Tuple.create(Long.class, long.class, null, null, null, null),
                        Tuple.create(getArrayConverter(Long.class, long.class, null, null, null, null)));
                put(Tuple.create(Float.class, float.class, null, null, null, null),
                        Tuple.create(getArrayConverter(Float.class, float.class, null, null, null, null)));
                put(Tuple.create(Double.class, double.class, null, null, null, null),
                        Tuple.create(getArrayConverter(Double.class, double.class, null, null, null, null)));
            }},
            null,
            (fromClass, toClass, getFromElementAtIndex, setToElementByIndex, elementConverter, parallelRequired) -> Tuple.create(
                    getArrayConverter(fromClass, toClass, getFromElementAtIndex, setToElementByIndex, elementConverter, parallelRequired))
    );

    private static <T, R> Function<Object, Object> getArrayConverter(
            Class<T> fromClass,
            Class<R> toClass,
            BiFunctionThrowable<Object, Integer, Object> getFromArrayAtIndex,
            TriConsumerThrowable<Object, Integer, Object> setToArrayAtIndex,
            Function<Object, R> elementConverter,
            Boolean parallelRequired) {
        Objects.requireNonNull(fromClass);
        Objects.requireNonNull(toClass);

        if (fromClass.equals(toClass)) {
            return array -> {
                if (array == null || !array.getClass().isArray())
                    return null;
                return Functions.Default.apply(() -> {
                    int length = Array.getLength(array);
                    Object newArray = getNewArray(toClass, length);
                    System.arraycopy(array, 0, newArray, 0, length);
                    return newArray;
                });
            };
        }

        //Use default getElementOfFromArrayByIndex, setElementOfToArrayByIndex if they are not provided
        final BiFunctionThrowable<Object, Integer, Object> getElement = getFromArrayAtIndex != null ?
                getFromArrayAtIndex : classArrayFactories.getThirdValue(fromClass);
        final TriConsumerThrowable<Object, Integer, Object> setElement = setToArrayAtIndex != null ?
                setToArrayAtIndex : classArrayFactories.getFourthValue(toClass);

        if (null == parallelRequired) {
            return array -> {
                if (array == null) return null;
                try {
                    int length = Array.getLength(array);
                    final Object toArray = getNewArray(toClass, length);
                    if (length < ParalellEvaluationThreashold) {
                        for (int i = 0; i < length; i++) {
                            Object fromElement = getElement.apply(array, i);
                            Object toElement = elementConverter == null ? fromElement : elementConverter.apply(fromElement);
                            setElement.accept(toArray, i, toElement);
                        }
                    } else {
                        Functions.runParallel(
                                (Integer i) -> {
                                    Object fromElement = getElement.apply(array, i);
                                    Object toElement = elementConverter == null ? fromElement : elementConverter.apply(fromElement);
                                    setElement.accept(toArray, i, toElement);
                                },
                                IntStream.range(0, length).boxed(),
                                Long.MAX_VALUE);
                    }
                    return toArray;
                } catch (Exception ex) {
                    return null;
                }
            };

        } else if (parallelRequired) {
            return array -> {
                if (array == null) return null;
                try {
                    int length = Array.getLength(array);
                    final Object toArray = getNewArray(toClass, length);
                    Functions.runParallel(
                            (Integer i) -> {
                                Object fromElement = getElement.apply(array, i);
                                Object toElement = elementConverter == null ? fromElement : elementConverter.apply(fromElement);
                                setElement.accept(toArray, i, toElement);
                            },
                            IntStream.range(0, length).boxed(),
                            Long.MAX_VALUE);
                    return toArray;
                } catch (Exception ex) {
                    return null;
                }
            };
        } else {
            return array -> {
                if (array == null) return null;
                try {
                    int length = Array.getLength(array);
                    final Object toArray = getNewArray(toClass, length);
                    for (int i = 0; i < length; i++) {
                        Object fromElement = getElement.apply(array, i);
                        Object toElement = elementConverter == null ? fromElement : elementConverter.apply(fromElement);
                        setElement.accept(toArray, i, toElement);
                    }
                    return toArray;
                } catch (Exception ex) {
                    return null;
                }
            };
        }
    }

    public static <T> T[] copyOfRange(Object original, Integer from, Integer to) throws Exception {
        Objects.requireNonNull(original);
        Integer newLength = to - from;
        if(newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);

        T[] copy = (T[])getNewArray(original.getClass().getComponentType(), newLength);
        System.arraycopy(original, from, copy, 0,
                Math.min(Array.getLength(original) - from, newLength));
        return copy;
    }

    public static Object[] asObjects(Object array) {
        if (array == null)
            return null;

        Class arrayClass = array.getClass();
        if (!arrayClass.isArray())
            return null;

        Class componentClass = arrayClass.getComponentType();
        Function<Object, Object> converter = arrayConverters.getFirst(componentClass, ObjectClass, null, null, null, null);
        if (converter == null)
            return null;
        return (Object[]) converter.apply(array);
    }

    public static Object asArray(Object array) {
        if (array == null) return null;

        //Assuming array is array of primitive values
        Class valueType = getComponentType(array);
        if (!valueType.isPrimitive())
            return array;

        Class objectType = classArrayFactories.getFirstValue(valueType);
        Function<Object, Object> toArrayConverter =
                arrayConverters.getFirst(valueType, objectType, null, null, null, null);
        return toArrayConverter.apply(array);
    }

    public static Object asPureObject(Object object) {
        if (object == null)
            return null;

        Class objectClass = object.getClass();
        if (!objectClass.isArray())
            return object;

        Object[] objects = asObjects(object);
        for (int i = 0; i < objects.length; i++) {
            objects[i] = asPureObject(objects[i]);
        }
        return objects;
    }

//    private static <O> Object[] toObjects(Object values, Class<O> objectClass, int size, BiFunctionThrowable<Object, Integer, O> getValueAt)
//            throws Exception {
//        if(values == null)
//            return null;
//        Object[] objects = new Object[size];
//        for (int i = 0; i < size; i++) {
//            objects[i] = getValueAt.apply(values, i);
//        }
//        return objects;
//    }
//
//    private static <T> T[] toArray(Object values, Class<T> objectClass, int size, BiFunctionThrowable<Object, Integer, T> getValueAt)
//            throws Exception {
//        if(values == null)
//            return null;
//        T[] result = (T[]) Array.newInstance(objectClass, size);
//        for (int i = 0; i < size; i++) {
//            result[i] = getValueAt.apply(values, i);
//        }
//        return result;
//    }

//    private static <T> T[] asArray(Object values){
//        if(values == null)
//            return null;
//
//        Class arrayClass = values.getClass();
//        Quad<Class, Class, FunctionThrowable<Object, Integer>, BiFunctionThrowable<Object, Integer, Object>> quad =
//                valuesMapper.get(arrayClass);
//        return (T[]) Functions.Default.apply(() -> toArray(values, quad.getSecond(), quad.getThird().apply(values), quad.getFourth()));
//    }
//
//    public static Object[] toObjects(boolean[] values){
//        return (Object[]) arrayConverters.getThird(boolean.class, Object.class, null, null).apply(values);
//    }
//
//    public static Object[] toObjects(byte[] values){
//        return asObjects(values);
//    }
//
//    public static Object[] toObjects(int[] values){
//        return asObjects(values);
//    }
//
//    public static Object[] toObjects(char[] values){
//        return asObjects(values);
//    }
//
//    public static Object[] toObjects(short[] values){
//        return asObjects(values);
//    }
//
//    public static Object[] toObjects(long[] values){
//        return asObjects(values);
//    }
//
//    public static Object[] toObjects(float[] values){
//        return asObjects(values);
//    }
//
//    public static Object[] toObjects(double[] values){
//        return asObjects(values);
//    }
//
//    public static Character[] toArray(char[] values){
//        if(values == null)
//            return null;
//
//        return (Character[]) Functions.Default.apply(() ->
//                toArray(values, Character.class, values.length, (a, i)->Character.valueOf(values[i])));
//    }
//
//    public static Float[] toArray(float[] values){
//        if(values == null)
//            return null;
//
//        return (Float[]) Functions.Default.apply(() ->
//                toArray(values, Float.class, values.length, (a, i)->Float.valueOf(values[i])));
//    }
//
//    public static Double[] toArray(double[] values){
//        if(values == null)
//            return null;
//
//        return (Double[]) Functions.Default.apply(() ->
//                toArray(values, Double.class, values.length, (a, i)->Double.valueOf(values[i])));
//    }
//
//    public static Integer[] toArray(int[] values){
//        if(values == null)
//            return null;
//
//        return (Integer[]) Functions.Default.apply(() ->
//                toArray(values, Integer.class, values.length, (a, i)->Integer.valueOf(values[i])));
//    }
//
//    public static Short[] toArray(short[] values){
//        if(values == null)
//            return null;
//
//        return (Short[]) Functions.Default.apply(() ->
//                toArray(values, Short.class, values.length, (a, i)->Short.valueOf(values[i])));
//    }
//
//    public static Long[] toArray(long[] values){
//        if(values == null)
//            return null;
//
//        return (Long[]) Functions.Default.apply(() ->
//                toArray(values, Long.class, values.length, (a, i)->Long.valueOf(values[i])));
//    }
//
//    public static Byte[] toArray(byte[] values){
//        if(values == null)
//            return null;
//
//        return (Byte[]) Functions.Default.apply(() ->
//                toArray(values, Byte.class, values.length, (a, i)->Byte.valueOf(values[i])));
//    }
//
//    public static Boolean[] toArray(boolean[] values){
//        if(values == null)
//            return null;
//
//        return (Boolean[]) Functions.Default.apply(() ->
//                toArray(values, Boolean.class, values.length, (a, i)->Boolean.valueOf(values[i])));
//    }

    private static Function<Object, Object> toBooleanArray =
            arrayConverters.getFirst(boolean.class, Boolean.class, null, null, null, null);

    public static Boolean[] convertArray(boolean[] values) {
        return (Boolean[]) toBooleanArray.apply(values);
    }

    private static Function<Object, Object> toByteArray =
            arrayConverters.getFirst(byte.class, Byte.class, null, null, null, null);

    public static Byte[] convertArray(byte[] values) {
        return (Byte[]) toByteArray.apply(values);
    }

    private static Function<Object, Object> toCharacterArray =
            arrayConverters.getFirst(char.class, Character.class, null, null, null, null);

    public static Character[] convertArray(char[] values) {
        return (Character[]) toCharacterArray.apply(values);
    }

    private static Function<Object, Object> toFloatArray =
            arrayConverters.getFirst(float.class, Float.class, null, null, null, null);

    public static Float[] convertArray(float[] values) {
        return (Float[]) toFloatArray.apply(values);
    }

    private static Function<Object, Object> toDoubleArray =
            arrayConverters.getFirst(double.class, Double.class, null, null, null, null);

    public static Double[] convertArray(double[] values) {
        return (Double[]) toDoubleArray.apply(values);
    }

    private static Function<Object, Object> toIntegerArray =
            arrayConverters.getFirst(int.class, Integer.class, null, null, null, null);

    public static Integer[] convertArray(int[] values) {
        return (Integer[]) toIntegerArray.apply(values);
    }

    private static Function<Object, Object> toShortArray =
            arrayConverters.getFirst(short.class, Short.class, null, null, null, null);

    public static Short[] convertArray(short[] values) {
        return (Short[]) toShortArray.apply(values);
    }

    private static Function<Object, Object> toLongArray =
            arrayConverters.getFirst(long.class, Long.class, null, null, null, null);

    public static Long[] convertArray(long[] values) {
        return (Long[]) toLongArray.apply(values);
    }

    public static <T> Object mapArray(Object fromArray, Class<T> toElementType) {
        Objects.requireNonNull(toElementType);
        if (fromArray == null)
            return null;

        Class<T> fromComponentType = getComponentType(fromArray);
        if (fromComponentType == null)
            return null;

        Function<Object, Object> converter = arrayConverters.getFirst(fromComponentType, toElementType, null, null, null, null);
        return converter.apply(fromArray);
    }

    public static <T> T toArray(Collection collection, Class<T> arrayClass) {
        Objects.requireNonNull(collection);
        Objects.requireNonNull(arrayClass);
        try {
            Object[] objects = collection.toArray();
            return (T) mapArray(objects, arrayClass.getComponentType());
        } catch (Exception ex) {
            return null;
        }
    }

    public static <T> boolean matchArrays(T[] expected, T[] actual) {
        if (expected.length != actual.length)
            return false;

        if (!Arrays.deepEquals(expected, actual)) {
            Logger.L("Expected: %s != %s", Tuple.of(expected), Tuple.of(actual));
            return false;
        }
        return true;
    }
//    public static <T> boolean matchArrays(T[] expected, Object actual) {
//        Objects.requireNonNull(expected);
//        Objects.requireNonNull(actual);
//
//        Class class1 = expected.getClass();
//        Class class2 = actual.getClass();
//        if(!class1.isArray() || !class2.isArray()){
//            return false;
//        }
//        if(Array.getLength(expected) != Array.getLength(actual))
//            return false;
//
//        Class<T> componentClass1 = class1.getComponentType();
//        Class<T> componentClass2 = class2.getComponentType();
//        if(!componentClass1.equals(componentClass2) && !TypeHelper.getClassPredicate(componentClass1).test(componentClass2))
//            return false;
//
//        Object[] objects1 = asObjects(expected);
//        Object[] objects2 = asObjects(actual);
//
//        if(!Arrays.deepEquals(objects1, objects2)){
//            Logger.L("Expected: %s != %s", Tuple.of(expected), Tuple.of(actual));
//            return false;
//        }
//        return true;
//    }

    public static <T extends Comparable<T>> boolean matchArrays(Collection expected, Collection actual, Class<T[]> arrayClass) {
        int size = expected.size();
        if (size != actual.size())
            return false;
        T[] expectedArray = toArray(expected, arrayClass);
        T[] actualArray = toArray(actual, arrayClass);

        return matchArrays(expectedArray, actualArray);
    }

    public static <T> boolean matchWithoutOrder(T[] expected, T[] actual) {
        if (expected == null || actual == null || expected.length != actual.length)
            return false;

        Class componentType = getComponentType(expected);
        T[] expectedCopy = (T[]) mapArray(expected, componentType);
        T[] actualCopy = (T[]) mapArray(actual, componentType);

        Arrays.sort(expectedCopy);
        Arrays.sort(actualCopy);
        if (!Arrays.deepEquals(expectedCopy, actualCopy)) {
            return false;
        }
        return true;
    }

    public static <T extends Comparable<T>> boolean matchWithoutOrder(Collection<T> expected, Collection<T> actual, Class<T[]> clazz) {
        if (expected == null || actual == null)
            return false;

        int size = expected.size();
        if (size != actual.size())
            return false;
        T[] expectedCopy = toArray(expected, clazz);
        T[] actualCopy = toArray(actual, clazz);

        Arrays.sort(expectedCopy);
        Arrays.sort(actualCopy);
        if (!Arrays.deepEquals(expectedCopy, actualCopy)) {
            return false;
        }
        return true;
    }

}
