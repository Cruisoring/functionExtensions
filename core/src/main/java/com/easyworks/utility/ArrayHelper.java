package com.easyworks.utility;

import com.easyworks.Functions;
import com.easyworks.TypeHelper;
import com.easyworks.function.*;
import com.easyworks.repository.SingleValuesRepository;
import com.easyworks.repository.TripleValuesRepository;
import com.easyworks.tuple.Tuple;
import com.easyworks.tuple.Tuple1;
import com.easyworks.tuple.Tuple3;
import com.easyworks.tuple.Tuple6;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

public class ArrayHelper<T, R> {
    public static final Class ObjectClass = Object.class;
    public static int ParalellEvaluationThreashold = 100;

    @FunctionalInterface
    public interface ArraySetAll{
        Object setAll(Object array, FunctionThrowable<Integer, Object> generator) throws Exception;
    }

    /**
     * Create an array of specific element type and length
     * @param clazz     Type of the elements of the array
     * @param length    Length of the array
     * @return          Array of the specific length of the specific elements if <code>clazz</code> and <code>length</code>
     *                  are set correctly; otherwise returns null
     */
    public static Object getNewArray(Class clazz, int length) {
        FunctionThrowable<Integer, Object> factory = TypeHelper.getArrayFactory(clazz);
        return factory.orElse(null).apply(length);
    }

    /**
     * Get the element type if the concerned Object is an array
     * @param array     Concerned array
     * @return          type of the elements of the array, or null if it is null or not an array
     */
    public static Class getComponentType(Object array) {
        if (array == null) return null;
        Class arrayClass = array.getClass();
        if (!arrayClass.isArray())
            return null;

        return arrayClass.getComponentType();
    }

    /**
     * Merge two array of the same type together, and returned the merged array of the same type.
     * Notice: the element type would be deducted from the first array
     * @param array     First array to be merged
     * @param others    Second array in form of varargs
     * @param <T>       Type of the elements within the above two arrays
     * @return          Merged array containing all elements of above two arrays
     */
    public static <T> T[] mergeTypedArray(T[] array, T... others){
        Objects.requireNonNull(array);
        Objects.requireNonNull(others);

        int length1 = array.length;
        int length2 = others.length;
        T[] newArray = (T[]) TypeHelper.copyOfRange(array, 0, length1+length2);
        for (int i = length1; i < length1+length2; i++) {
            newArray[i] = (T)Array.get(others, i-length1);
        }
        return newArray;
    }

    /**
     * More generic way to merge two arrays that could contain primitive elements, and the result array would be the most
     * fitted type (object type preferred) to containing all elements of the two arrays.
     * @param first     first array or element to be merged
     * @param others    remaining elements to be merged
     * @return      an array containing all elements of the given arguments
     *      <ui>
     *          <li>when <code>others</code> is emtpy, returns either copy of the first array when it is array,
     *          or a new array containing only the first argument when it is not array</li>
     *          <li>when component type of first and others are identical, retuns a array of the same type containing all their elements</li>
     *          <li>when component type of first and others are equivalent, retuns a array of the object type containing all their elements</li>
     *          <li>when component type of first is assignable from the second, retuns a array of same type as first containing all their elements</li>
     *          <li>when component type of second is assignable from the first, retuns a array of same type as second containing all their elements</li>
     *          <li>otherwise, returns either a new Object[] containing all their elements or null if something goes wrong</li>
     *      </ui>
     */
    public static Object arrayOf(Object first, Object... others){
        Objects.requireNonNull(first);
        Objects.requireNonNull(others);

        Class class1 = first.getClass();
        boolean isArray1 = class1.isArray();
        Class componentClass1 = isArray1 ? class1.getComponentType() : class1;
        boolean isPremitive1 = TypeHelper.isPrimitive(class1);
        int length1 = class1.isArray() ? Array.getLength(first) : 1;
        int length2 = Array.getLength(others);

        Object firstArrayElement = null;
        if(length2 == 0){
            if(isArray1){
                Class componentClass = class1.getComponentType();
                TriFunctionThrowable<Object, Integer, Integer, Object> copier = TypeHelper.getArrayRangeCopier(componentClass);
                return copier.orElse(null).apply(first, 0, length1);
            } else {
                try {
                    Object resultArray = TypeHelper.getArrayFactory(class1).apply(1);
                    TriConsumerThrowable<Object, Integer, Object> elementSetter = TypeHelper.getArrayElementSetter(class1);
                    elementSetter.accept(resultArray, 0, first);
                    return resultArray;
                }catch (Exception ex){
                    return null;
                }
            }
        } else if (length2 == 1){
            Object firstElement = Array.get(others, 0);
            if(firstElement != null){
                Class firstElementClass = firstElement.getClass();
                if(firstElementClass.isArray()){
                    firstArrayElement = firstElement;
                } else {
                    firstArrayElement = arrayOf(firstElement);
                }
            }
        }

        Object array2 = firstArrayElement == null ? others : firstArrayElement;
        Class class2 = array2.getClass();
        length2 = Array.getLength(array2);
        boolean isPremitive2 = TypeHelper.isPrimitive(class2);
        Class componentClass2 = class2.getComponentType();
        FunctionThrowable<Integer, Object> elementGetter = i -> (i<length1) ? ( isArray1 ? Array.get(first, i) : first)
                : Array.get(array2, i -length1);
        Object resultArray;
        Class resultComponentClass;
        if(Objects.equals(componentClass1, componentClass2) || TypeHelper.areEquivalent(componentClass1, componentClass2)){
            resultComponentClass = (isPremitive1 && !isPremitive2) ? componentClass2 : componentClass1;
        } else if(!isPremitive1 && (componentClass1.isAssignableFrom(componentClass2))
                || (isPremitive2 && componentClass1.isAssignableFrom(TypeHelper.getEquivalentClass(componentClass2)))){
            resultComponentClass = componentClass1;
        } else if(!isPremitive2 && (componentClass2.isAssignableFrom(componentClass1))
                || (isPremitive1 && componentClass2.isAssignableFrom(TypeHelper.getEquivalentClass(componentClass1)))){
            resultComponentClass = componentClass2;
        } else {
            resultComponentClass = Object.class;
        }
        resultArray = TypeHelper.getArrayFactory(resultComponentClass)
                .orElse(null).apply(length1+length2);
        try {
            setAll(resultArray, elementGetter);
            return resultArray;
        }catch(Exception ex){
            return null;
        }
    }

    //region Repository of setAll functions of any array whose element type is used as the key
    private static final TripleValuesRepository<
            Class,  //Type of the elements of the array is used as the key

            BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>>,       //Parallel setAll method
            BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>>,        //Serial setAll method
            BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>>>        //Default setAll method
            arraySetters = TripleValuesRepository.fromKey( ArrayHelper::getAssetSetter);

    private static Map<Class, Tuple3<
            BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>>
                , BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>>
                , BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>>>> getArraysSetAll(){
        Map<Class, Tuple3<
                BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>>
                , BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>>
                , BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>>>> map = new HashMap();
        BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>> parallelSetAll = (Object array, FunctionThrowable<Integer, Object> intFunction) -> {
            Arrays.parallelSetAll((int[])array, i -> ((Integer)intFunction.orElse(0).apply(i)).intValue());
        };
        BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>> serialSetAll = (Object array, FunctionThrowable<Integer, Object> intFunction) -> {
            Arrays.setAll((int[])array, i -> ((Integer)intFunction.orElse(0).apply(i)).intValue());
        };
        BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>> setAll = serialSetAll;
        map.put(int.class, Tuple.create(parallelSetAll, serialSetAll, setAll));

        parallelSetAll = (Object array, FunctionThrowable<Integer, Object> intFunction) -> {
            Arrays.parallelSetAll((double[])array, i -> ((Double)intFunction.orElse(0d).apply(i)).doubleValue());
        };
        serialSetAll = (Object array, FunctionThrowable<Integer, Object> intFunction) -> {
            Arrays.setAll((double[])array, i -> ((Double)intFunction.orElse(0).apply(i)).doubleValue());
        };
        setAll = serialSetAll;
        map.put(double.class, Tuple.create(parallelSetAll, serialSetAll, setAll));

        parallelSetAll = (Object array, FunctionThrowable<Integer, Object> intFunction) -> {
            Arrays.parallelSetAll((long[])array, i -> ((Long)intFunction.orElse(0L).apply(i)).longValue());
        };
        serialSetAll = (Object array, FunctionThrowable<Integer, Object> intFunction) -> {
            Arrays.setAll((long[])array, i -> ((Long)intFunction.orElse(0L).apply(i)).longValue());
        };
        setAll = serialSetAll;
        map.put(long.class, Tuple.create(parallelSetAll, serialSetAll, setAll));
        return map;
    }

    private static Tuple3<
            BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>>
            , BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>>
            , BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>>> getAssetSetter(Class componentClass){
        Objects.requireNonNull(componentClass);
        TriConsumerThrowable<Object, Integer, Object> elementSetter;
        elementSetter = TypeHelper.getArrayElementSetter(componentClass);

        TriFunctionThrowable.TriFunction<Object, Integer, Object, Boolean> setElementWithException =
                (array, index, element) ->  {
            try {
                elementSetter.accept(array, index, element);
                return false;
            }catch (Exception ex){
                return true;
            }
        };
        BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>> parallelSetAll = (array, generator) -> {
            int length = Array.getLength(array);
            Integer indexWithException = IntStream.range(0, length).boxed()
                    .parallel()
                    .filter(i -> setElementWithException.apply(array, i, generator.orElse(null).apply(i)))
                    .findFirst().orElse(-1);
        };

        BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>> serialSetAll = (array, generator) -> {
            int length = Array.getLength(array);
            for (int i = 0; i < length; i++) {
                Object element = generator.apply(i);
                elementSetter.accept(array, i, element);
            }
        };

        BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>> defaultSetAll = (array, generator) -> {
            int length = Array.getLength(array);
            if(length < TypeHelper.PARALLEL_EVALUATION_THRESHOLD) {
                for (int i = 0; i < length; i++) {
                    Object element = generator.apply(i);
                    elementSetter.accept(array, i, element);
                }
            } else {
                Integer indexWithException = IntStream.range(0, length).boxed()
                        .parallel()
                        .filter(i -> setElementWithException.apply(array, i, generator.orElse(null).apply(i)))
                        .findFirst().orElse(-1);
            }
        };

        return Tuple.create(parallelSetAll, serialSetAll, defaultSetAll);
    }

    /**
     * Set all elements of the concerned array with values generated by <tt>generator</tt> parallelly.
     * @param array     array of any types
     * @param generator function to get object values of the array elements with a single int as input
     */
    public static void setAllParallel(Object array, FunctionThrowable<Integer, Object> generator){
        try {
            arraySetters.getFirstValue(getComponentType(array)).accept(array, generator);
        }catch (Exception ex){}
    }

    /**
     * Set all elements of the concerned array with values generated by <tt>generator</tt> serailly.
     * @param array     array of any types
     * @param generator function to get object values of the array elements with a single int as input
     */
    public static void setAllSerial(Object array, FunctionThrowable<Integer, Object> generator){
        try {
            arraySetters.getSecondValue(getComponentType(array)).accept(array, generator);
        }catch (Exception ex){}
    }

    /**
     * Set all elements of the concerned array with values generated by <tt>generator</tt>.
     * @param array     array of any types
     * @param generator function to get object values of the array elements with a single int as input
     */
    public static void setAll(Object array, FunctionThrowable<Integer, Object> generator){
        try {
            arraySetters.getThirdValue(getComponentType(array)).accept(array, generator);
        }catch (Exception ex){}
    }
    //endregion




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
                    Tuple6<Class, Class, BiFunctionThrowable<Object, Integer, Object>, TriConsumerThrowable<Object, Integer, Object>, Function<Object, Object>, Boolean>,
                    Tuple1<Function<Object, Object>>>() {{
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
        final TriConsumerThrowable<Object, Integer, Object> setElement = setToArrayAtIndex != null ?
                setToArrayAtIndex : TypeHelper.classOperators.getFourthValue(toClass);

        if (null == parallelRequired) {
            return array -> {
                if (array == null) return null;
                try {
                    int length = Array.getLength(array);
                    final Object toArray = getNewArray(toClass, length);
                    if (length < ParalellEvaluationThreashold) {
                        for (int i = 0; i < length; i++) {
                            Object fromElement = Array.get(array, i);
                            Object toElement = elementConverter == null ? fromElement : elementConverter.apply(fromElement);
                            setElement.accept(toArray, i, toElement);
                        }
                    } else {
                        Functions.runParallel(
                                (Integer i) -> {
                                    Object fromElement = Array.get(array, i);
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
                                Object fromElement = Array.get(array, i);
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
                        Object fromElement = Array.get(array, i);
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

    public static Boolean[] convertArray(boolean[] values) {
        if(values == null)
            return null;

        int length = values.length;
        Function<Object, Object> converter = TypeHelper.getToEquivalentParallelConverter(values.getClass());
        return (Boolean[])converter.apply(values);
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
        return TypeHelper.valueEquals(expectedCopy, actualCopy);
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
        return TypeHelper.valueEquals(expectedCopy, actualCopy);
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
