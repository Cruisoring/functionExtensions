package com.easyworks.utility;

import com.easyworks.Functions;
import com.easyworks.function.BiFunctionThrowable;
import com.easyworks.function.FunctionThrowable;
import com.easyworks.repository.*;
import com.easyworks.tuple.*;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

public class ArrayHelper<T,R> {
    public static final Class ObjectClass = Object.class;
    public static final Class ArrayClass = Array.class;
    public static int ParalellEvaluationThreashold = 100;

    private static <T> T[] defaultArrayFactory(Class<T> clazz, int length){
        return (T[]) Array.newInstance(clazz, length);
    }

    protected static final DualValuesRepository<Class, Class, FunctionThrowable<Integer, Object>> classFactories = MultiValuesRepository.toDualValuesRepository(
            () -> new HashMap<Class, Dual<Class, FunctionThrowable<Integer, Object>>>(){{
                put(int.class, Tuple.create(Integer.class, i -> new int[i]));
                put(char.class, Tuple.create(Character.class, i -> new char[i]));
                put(byte.class, Tuple.create(Byte.class, i -> new byte[i]));
                put(boolean.class, Tuple.create(Boolean.class, i -> new boolean[i]));
                put(short.class, Tuple.create(Short.class, i -> new short[i]));
                put(long.class, Tuple.create(Long.class, i -> new long[i]));
                put(float.class, Tuple.create(Float.class, i -> new float[i]));
                put(double.class, Tuple.create(Double.class, i -> new double[i]));
                put(Object[].class, Tuple.create(Object.class, i -> new Object[i]));
            }},
            null,
            clazz -> Tuple.create( clazz, length -> Array.newInstance(clazz, length)
    ));

    public static Class getComponentType(Object array){
        if(array == null) return null;
        Class arrayClass = array.getClass();
        if(!arrayClass.isArray())
            return null;

        return arrayClass.getComponentType();
    }

    public static Class objectify(Class clazz){
        if(clazz == null)
            return null;
        return classFactories.containsKey(clazz) ? classFactories.getFirstValue(clazz) : clazz;
    }

    public static Class[] objectify(Class[] classes){
        if(classes == null)
            return null;

        int size = classes.length;
        Class[] result = new Class[size];
        for (int i = 0; i < size; i++) {
            result[i] = objectify(classes[i]);
        }
        return result;
    }


    public static Object getNewArray(Class clazz, int length) {
        FunctionThrowable<Integer, Object> factory = classFactories.getSecondValue(clazz);
        return Functions.Default.apply(factory, length);
    }

    public static final SingleValuesRepository.SingleValuesRepository6<
                Class,              //fromClass as the first key
                Class,              //toClass as the second key
                FunctionThrowable<Object, Integer>, //getLength
                BiFunctionThrowable<Object, Integer, Object>,   //getFromElementAtIndex
                Function<Object, Object>,             // elementConverter as the third key
                Boolean,            //parallelEvaluationRquired

                Function<Object, Object>        //Final converter based on the given keys
                >
            arrayConverters = TupleRepository.toSingleValuesRepository(
            () -> new HashMap<
                    Hexa<Class, Class, FunctionThrowable<Object, Integer>, BiFunctionThrowable<Object, Integer, Object>, Function<Object, Object>, Boolean>,
                    Single<Function<Object, Object>>>(){{
                put(Tuple.create(boolean.class, Boolean.class, null, Array::getBoolean, null,null),
                        Tuple.create(getArrayConverter(boolean.class, Boolean.class, null, null, null, null)));
                put(Tuple.create(byte.class, Byte.class, null, Array::getByte,null, null),
                        Tuple.create(getArrayConverter(byte.class, Byte.class, null, null, null, null)));
                put(Tuple.create(char.class, Character.class, null, Array::getChar,null, null),
                        Tuple.create(getArrayConverter(char.class, Character.class, null, null, null, null)));
                put(Tuple.create(int.class, Integer.class, null, Array::getInt,null, null),
                        Tuple.create(getArrayConverter(int.class, Integer.class, null, null, null, null)));
                put(Tuple.create(short.class, Short.class, null, Array::getShort,null, null),
                        Tuple.create(getArrayConverter(short.class, Short.class, null, null, null, null)));
                put(Tuple.create(long.class, Long.class, null, Array::getLong,null, null),
                        Tuple.create(getArrayConverter(long.class, Long.class, null, null, null, null)));
                put(Tuple.create(float.class, Float.class, null, Array::getFloat,null, null),
                        Tuple.create(getArrayConverter(float.class, Float.class, null, null, null, null)));
                put(Tuple.create(double.class, Double.class, null, Array::getDouble, null, null),
                        Tuple.create(getArrayConverter(double.class, Double.class, null, null, null, null)));
            }},
            null,
            (fromClass, toClass, getLength, getFromElementAtIndex, elementConverter, parallelRequired) -> Tuple.create(
                    getArrayConverter( fromClass, toClass, getLength, getFromElementAtIndex, elementConverter, parallelRequired))
    );

    private static <T, R> Function<Object, Object> getArrayConverter(
            Class<T> fromClass,
            Class<R> toClass,
            FunctionThrowable<Object, Integer> getLength,
            BiFunctionThrowable<Object, Integer, Object> getElementByIndex,
            Function<Object, R> elementConverter,
            Boolean parallelRequired){
        Objects.requireNonNull(fromClass);
        Objects.requireNonNull(toClass);
        //Try best to prepare default getLength, getElementByIndex and elementConverter
        final FunctionThrowable<Object, Integer> getLengthFinal = getLength != null ?
                getLength: Array::getLength;
        final BiFunctionThrowable<Object, Integer, Object> getElement = getElementByIndex != null ?
                getElementByIndex : Array::get;
        final FunctionThrowable<Object, R> toResultElement = elementConverter != null ?
                t -> elementConverter.apply(t) : (t -> toClass.cast(t));

        if(fromClass.equals(toClass)) {
            return array -> {
                if (array == null || !array.getClass().isArray())
                    return null;
                return Functions.Default.apply(() -> {
                    int length = getLengthFinal.apply(array);
                    Object newArray = getNewArray(toClass, length);
                    System.arraycopy(array, 0, newArray, 0, length);
                    return newArray;
                });
            };
        }

        if(null == parallelRequired){
            return group -> {
                if (group == null) return null;
                try{
                    int length = getLengthFinal.apply(group);
                    final R[] toArray = (R[]) getNewArray(toClass, length);
                    if(length < ParalellEvaluationThreashold){
                        for (int i = 0; i < length; i++) {
                            toArray[i] = toResultElement.apply(getElement.apply(group, i));
                        }
                    } else {
                        Functions.runParallel(
                                (Integer i) -> toArray[i] = toResultElement.apply(getElement.apply(group, i)),
                                IntStream.range(0, length).boxed(),
                                Long.MAX_VALUE);
                    }
                    return toArray;
                }catch (Exception ex){
                    return null;
                }
            };

        } else if (parallelRequired){
            return array -> {
                if (array == null) return null;
                try {
                    int length = getLengthFinal.apply(array);
                    final R[] toArray = (R[]) getNewArray(toClass, length);
                    Functions.runParallel(
                            (Integer i) -> toArray[i] = toResultElement.apply(getElement.apply(array, i)),
                            IntStream.range(0, length).boxed(),
                            Long.MAX_VALUE);
                    return toArray;
                }catch (Exception ex){
                    return null;
                }
            };
        } else {
            return array -> {
                if (array == null) return null;
                try {
                    int length = getLengthFinal.apply(array);
                    final R[] toArray = (R[]) getNewArray(toClass, length);
                    for (int i = 0; i < length; i++) {
                        toArray[i] = toResultElement.apply(getElement.apply(array, i));
                    }
                    return toArray;
                }catch (Exception ex){
                    return null;
                }
            };
        }
   }

    public static Object[] asObjects(Object array){
        if(array == null)
            return null;

        Class arrayClass = array.getClass();
        if(!arrayClass.isArray())
            return null;

        Class componentClass = arrayClass.getComponentType();
        Function<Object, Object> converter = arrayConverters.getFirst(componentClass, ObjectClass, null, null, null, null);
        if(converter == null)
            return null;
        return (Object[]) converter.apply(array);
    }

    public static Object asArray(Object array){
        if(array == null) return null;

        //Assuming array is array of primitive values
        Class valueType = getComponentType(array);
        if(!valueType.isPrimitive())
            return array;

        Class objectType = classFactories.getFirstValue(valueType);
        Function<Object, Object> toArrayConverter  =
                arrayConverters.getFirst(valueType, objectType, null, null, null,null);
        return toArrayConverter.apply(array);
    }

    public static Object asPureObject(Object object){
        if(object == null)
            return null;

        Class objectClass = object.getClass();
        if(!objectClass.isArray())
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

    //*/
    public static <T,R> R[] convertArray(T[] fromArray, Class<R> toComponentType){
        if(fromArray == null)
            return null;

        Objects.requireNonNull(toComponentType);
        Class<T> fromComponentType = getComponentType(fromArray);
        Function<Object, Object> converter = arrayConverters.getFirst(fromComponentType, toComponentType, null, null, null, null);
        return (R[]) converter.apply(fromArray);
    }

    private static Function<Object, Object> toBooleanArray  =
            arrayConverters.getFirst(boolean.class, Boolean.class, null, null, null,null);
    public static Boolean[] convertArray(boolean[] values){
        return (Boolean[]) toBooleanArray.apply(values);
    }

    private static Function<Object, Object> toByteArray =
            arrayConverters.getFirst(byte.class, Byte.class, null, null, null,null);
    public static Byte[] convertArray(byte[] values){
        return (Byte[]) toByteArray.apply(values);
    }

    private static Function<Object, Object> toCharacterArray =
            arrayConverters.getFirst(char.class, Character.class, null, Array::getChar, null,null);
    public static Character[] convertArray(char[] values){
        return (Character[]) toCharacterArray.apply(values);
    }

    private static Function<Object, Object> toFloatArray =
            arrayConverters.getFirst(float.class, Float.class, null, Array::getFloat, null,null);
    public static Float[] convertArray(float[] values){
        return (Float[]) toFloatArray.apply(values);
    }

    private static Function<Object, Object> toDoubleArray =
            arrayConverters.getFirst(double.class, Double.class, null, Array::getDouble, null,null);
    public static Double[] convertArray(double[] values){
        return (Double[]) toDoubleArray.apply(values);
    }

    private static Function<Object, Object> toIntegerArray =
            arrayConverters.getFirst(int.class, Integer.class, null, Array::getInt, null,null);
    public static Integer[] convertArray(int[] values){
        return (Integer[]) toIntegerArray.apply(values);
    }

    private static Function<Object, Object> toShortArray =
            arrayConverters.getFirst(short.class, Short.class, null, Array::getShort, null,null);
    public static Short[] convertArray(short[] values){
        return (Short[]) toShortArray.apply(values);
    }

    private static Function<Object, Object> toLongArray =
            arrayConverters.getFirst(long.class, Long.class, null, Array::getLong, null, null);
    public static Long[] convertArray(long[] values){
        return (Long[]) toLongArray.apply(values);
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

    public static <T> T[] toArray(Collection collection, Class<T> clazz){
        Objects.requireNonNull(collection);
        Objects.requireNonNull(clazz);
        try {
            Object[] objects = collection.toArray();
            T[] array = convertArray(objects, clazz);
            return array;
        } catch (Exception ex){
            return null;
        }
    }

    public static <T> boolean matchWithOrder(T[] expected, T[] actual) {
        if(expected.length != actual.length)
            return false;

        if(!Arrays.deepEquals(expected, actual)){
            Logger.L("Expected: %s != %s", Tuple.asTuple(expected), Tuple.asTuple(actual));
            return false;
        }
        return true;
    }

    public static <T extends Comparable<T>> boolean matchWithOrder(Collection<T> expected, Collection<T> actual, Class<T> clazz) {
        int size = expected.size();
        if(size != actual.size())
            return false;
        T[] expectedArray = toArray(expected, clazz);
        T[] actualArray = toArray(actual, clazz);

        return matchWithOrder(expectedArray, actualArray);
    }

    public static <T> boolean matchWithoutOrder(T[] expected, T[] actual){
        if(expected == null || actual ==null || expected.length != actual.length)
            return false;

        Class componentType = getComponentType(expected);
        T[] expectedCopy = (T[]) convertArray(expected, componentType);
        T[] actualCopy = (T[]) convertArray(actual, componentType);

        Arrays.sort(expectedCopy);
        Arrays.sort(actualCopy);
        if(!Arrays.deepEquals(expectedCopy, actualCopy)){
            return false;
        }
        return true;
    }

    public static <T extends Comparable<T>> boolean matchWithoutOrder(Collection<T> expected, Collection<T> actual, Class<T> clazz) {
        if(expected == null || actual == null)
            return false;

        int size = expected.size();
        if(size != actual.size())
            return false;
        T[] expectedCopy = toArray(expected, clazz);
        T[] actualCopy = toArray(actual, clazz);

        Arrays.sort(expectedCopy);
        Arrays.sort(actualCopy);
        if(!Arrays.deepEquals(expectedCopy, actualCopy)){
            return false;
        }
        return true;
    }

}
