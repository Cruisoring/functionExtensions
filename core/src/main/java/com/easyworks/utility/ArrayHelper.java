package com.easyworks.utility;

import com.easyworks.Functions;
import com.easyworks.function.*;
import com.easyworks.repository.SingleValuesRepository;
import com.easyworks.tuple.Hexa;
import com.easyworks.tuple.Single;
import com.easyworks.tuple.Tuple;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class ArrayHelper<T, R> {
    public static final Class ObjectClass = Object.class;
    public static final Class ArrayClass = Array.class;
    public static int ParalellEvaluationThreashold = 100;
    public static long ParalellEvaluationTimeoutMills = 30 * 60 * 1000;

    public static Class getComponentType(Object array) {
        if (array == null) return null;
        Class arrayClass = array.getClass();
        if (!arrayClass.isArray())
            return null;

        return arrayClass.getComponentType();
    }

    public static Class objectify(Class clazz) {
        if (clazz == null || !clazz.isPrimitive())
            return clazz;

        return TypeHelper.getEquivalentClass(clazz);
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
        FunctionThrowable<Integer, Object> factory = TypeHelper.getArrayFactory(clazz);
        return factory.orElse(null).apply(length);
    }



    public static final SingleValuesRepository.HexaKeys<
            Class,              //fromClass as the first key
            Class,              //toClass as the second key
            BiFunctionThrowable<Object, Integer, Object>, //Function to get the Element At Index of the FromArray
            TriConsumerThrowable<Object, Integer, Object>, //Function to set the Element At Index of the FromArray
            Function<Object, Object>,             // Optional elementConverter of the fromElement to toElement
            Boolean,            //parallelEvaluationRquired

            Function<Object, Object>       //Final converter based on the given keys

            >
            arrayConverters = SingleValuesRepository.fromSixKeys(
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
                getFromArrayAtIndex : TypeHelper.classOperators.getFourthValue(fromClass);
        final TriConsumerThrowable<Object, Integer, Object> setElement = setToArrayAtIndex != null ?
                setToArrayAtIndex : TypeHelper.classOperators.getFifthValue(toClass);

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

        Class objectType = TypeHelper.getEquivalentClass(valueType);
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
        return TypeHelper.deepEquals(expectedCopy, actualCopy);
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
        return TypeHelper.deepEquals(expectedCopy, actualCopy);
    }

    public static boolean deepEquals(Object obj1, Object obj2){
        if(Objects.equals(obj1, obj2))
            return true;

        Class class1 = obj1.getClass();
        Class class2 = obj2.getClass();
        if(!TypeHelper.getClassPredicate(class1).test(class2) || !class1.isArray())
            return false;

        int expectedSize = Array.getLength(obj1);
        if(expectedSize != Array.getLength(obj2))
            return false;

        BiFunctionThrowable<Object, Integer, Object> getter = TypeHelper.getArrayElementGetter(class1);
        Predicate<Integer> elementUnmatchedPredicate = i -> {
            try {
                Object expectedElement = getter.apply(obj1, i);
                Object actualElement = getter.apply(obj2, i);
                return !deepEquals(expectedElement, actualElement);
            }catch(Exception ex){
                return true;
            }
        };
        boolean result = !IntStream.range(0, expectedSize).boxed().parallel()
                .filter(elementUnmatchedPredicate).findFirst().isPresent();
        return result;
    }

    public static <T> BiConsumerThrowable<Object, Function<Integer, Object>> getParallelSetAll(Class<T> clazz){
        Objects.requireNonNull(clazz);
        return (Object array, Function<Integer, Object> generator) -> {
            Objects.requireNonNull(array);
            Objects.requireNonNull(generator);
            int length = Array.getLength(array);
            Class<T> componentType = (Class<T>) array.getClass().getComponentType();
            TriConsumerThrowable<Object, Integer, Object> elementSetter = TypeHelper.getArrayElementSetter(componentType);
            //Split the "i -> elementSetter.accept(array, i, generator.apply(i))" to two steps to avoid
            // "Unhandled Exception: java.lang.Exception" warning
            BiConsumerThrowable<Object, Integer> setElementAtIndex = (theArray, index) -> {
                Object value = generator.apply(index);
                elementSetter.accept(theArray, index, value);
            };

            IntStream.range(0, length).parallel().forEach(i -> setElementAtIndex.orElse(null).accept(array, i));
        };
    }
}
