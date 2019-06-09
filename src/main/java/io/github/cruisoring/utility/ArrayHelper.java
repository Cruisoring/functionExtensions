package io.github.cruisoring.utility;

import io.github.cruisoring.Range;
import io.github.cruisoring.TypeHelper;
import io.github.cruisoring.function.BiConsumerThrowable;
import io.github.cruisoring.function.FunctionThrowable;
import io.github.cruisoring.function.TriConsumerThrowable;
import io.github.cruisoring.function.TriFunctionThrowable;
import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.repository.TupleRepository3;
import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple3;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static io.github.cruisoring.Asserts.*;

/**
 * Class to hold helper methods related with array operations.
 */
public class ArrayHelper {

    public static final Class ObjectClass = Object.class;
    //region Repository of setAll functions of any array whose element type is used as the key
    private static final TupleRepository3<
            Class,  //Type of the elements of the array is used as the key

            BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>>,       //Parallel setAll method
            BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>>,        //Serial setAll method
            BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>>>        //Default setAll method
            arraySetters = TupleRepository3.fromKey(ArrayHelper::getAssetSetter);
    public static int ParalellEvaluationThreashold = 100;

    /**
     * Convert a given array to an ArrayList with same order.
     *
     * @param elements the Array of elements to be converted, the null would be treated as an Array containing only one single element of null.
     * @param <T>      the type of the elements.
     * @return the List containing same set of elements with same orders.
     */
    public static <T> List<T> asList(T... elements) {
        List<T> list = new ArrayList<T>();
        if (elements == null) {
            list.add(null);
        } else {
            for (int i = 0; i < elements.length; i++) {
                list.add(elements[i]);
            }
        }
        return list;
    }

    /**
     * Create an array of specific element type and length
     *
     * @param clazz  Type of the elements of the array
     * @param length Length of the array
     * @return Array of the specific length of the specific elements if <code>clazz</code> and <code>length</code>
     * are set correctly; otherwise returns null
     */
    public static Object getNewArray(Class clazz, int length) {
        if (clazz == Object.class)
            return new Object[length];

        FunctionThrowable<Integer, Object> factory = TypeHelper.getArrayFactory(clazz);
        return factory.orElse(null).apply(length);
    }

    /**
     * Create an array of specific element type and length and filled with given default value.
     *
     * @param clazz  Type of the elements of the array
     * @param length Length of the array
     * @param defaultValue defaultValue to fill the newly generated array.
     * @param <T> element type of the new array.
     * @return Array of the specific length of the specific elements if <code>clazz</code> and <code>length</code>
     * are set correctly; otherwise returns null
     */
    public static <T> T[] getNewArray(Class<? extends T> clazz, int length, T defaultValue) {
        T[] array =(T[])  (clazz == Object.class ? new Object[length] : TypeHelper.getArrayFactory(clazz).orElse(null).apply(length));

        for (int i = 0; i < length; i++) {
            array[i] = defaultValue;
        }
        return array;
    }

    /**
     * Create an array of specific element type and length and filled with values from the {@code elementSupplier}
     *
     * @param clazz  Type of the elements of the array
     * @param length Length of the array
     * @param elementSupplier supplier to get element based on index
     * @param <T> element type of the new array.
     * @return Array of the specific length of the specific elements if <code>clazz</code> and <code>length</code>
     * are set correctly; otherwise returns null
     */

    public static <T> Object create(Class<? extends T> clazz, int length, Function<Integer, T> elementSupplier) {
        checkWithoutNull(elementSupplier);

        Object array = clazz == Object.class ? new Object[length] : TypeHelper.getArrayFactory(clazz).orElse(null).apply(length);

        for (int i = 0; i < length; i++) {
            Array.set(array, i, elementSupplier.apply(i));
        }
        return array;
    }

    /**
     * Get the element type if the concerned Object is an array
     *
     * @param array Concerned array
     * @return type of the elements of the array, or null if it is null or not an array
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
     *
     * @param array  First array to be merged
     * @param others Second array in form of varargs
     * @param <T>    Type of the elements within the above two arrays
     * @return Merged array containing all elements of above two arrays
     */
    public static <T> T[] mergeTypedArray(T[] array, T... others) {
        checkStates(array != null, others != null);

        int length1 = array.length;
        int length2 = others.length;
        T[] newArray = (T[]) TypeHelper.copyOfRange(array, 0, length1 + length2);
        for (int i = length1; i < length1 + length2; i++) {
            newArray[i] = (T) Array.get(others, i - length1);
        }
        return newArray;
    }

    public static <T> Object[] append(T[] extras, T... array){
        return mergeTypedArray(array, extras);
    }

    /**
     * More generic way to merge two arrays that could contain primitive elements, and the result array would be the most
     * fitted type (object type preferred) to containing all elements of the two arrays.
     *
     * @param first  first array or element to be merged
     * @param others remaining elements to be merged
     * @return an array containing all elements of the given arguments
     * <ul>
     * <li>when <code>others</code> is emtpy, returns either copy of the first array when it is array,
     * or a new array containing only the first argument when it is not array</li>
     * <li>when component type of first and others are identical, retuns a array of the same type containing all their elements</li>
     * <li>when component type of first and others are equivalent, retuns a array of the object type containing all their elements</li>
     * <li>when component type of first is assignable from the second, retuns a array of same type as first containing all their elements</li>
     * <li>when component type of second is assignable from the first, retuns a array of same type as second containing all their elements</li>
     * <li>otherwise, returns either a new Object[] containing all their elements or null if something goes wrong</li>
     * </ul>
     */
    public static Object arrayOf(Object first, Object... others) {
        checkStates(others != null);

        Class class1 = first.getClass();
        boolean isArray1 = class1.isArray();
        Class componentClass1 = isArray1 ? class1.getComponentType() : class1;
        boolean isPremitive1 = TypeHelper.isPrimitive(class1);
        int length1 = class1.isArray() ? Array.getLength(first) : 1;
        int length2 = Array.getLength(others);

        Object firstArrayElement = null;
        if (length2 == 0) {
            if (isArray1) {
                Class componentClass = class1.getComponentType();
                TriFunctionThrowable<Object, Integer, Integer, Object> copier = TypeHelper.getArrayRangeCopier(componentClass);
                return copier.orElse(null).apply(first, 0, length1);
            } else {
                try {
                    Object resultArray = TypeHelper.getArrayFactory(class1).apply(1);
                    TriConsumerThrowable<Object, Integer, Object> elementSetter = TypeHelper.getArrayElementSetter(class1);
                    elementSetter.accept(resultArray, 0, first);
                    return resultArray;
                } catch (Exception ex) {
                    return null;
                }
            }
        } else if (length2 == 1) {
            Object firstElement = Array.get(others, 0);
            if (firstElement != null) {
                Class firstElementClass = firstElement.getClass();
                if (firstElementClass.isArray()) {
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
        FunctionThrowable<Integer, Object> elementGetter = i -> (i < length1) ? (isArray1 ? Array.get(first, i) : first)
                : Array.get(array2, i - length1);
        Object resultArray;
        Class resultComponentClass;
        if (componentClass1.equals(componentClass2) || TypeHelper.areEquivalent(componentClass1, componentClass2)) {
            resultComponentClass = (isPremitive1 && !isPremitive2) ? componentClass2 : componentClass1;
        } else if (!isPremitive1 && (componentClass1.isAssignableFrom(componentClass2))
                || (isPremitive2 && componentClass1.isAssignableFrom(TypeHelper.getEquivalentClass(componentClass2)))) {
            resultComponentClass = componentClass1;
        } else if (!isPremitive2 && (componentClass2.isAssignableFrom(componentClass1))
                || (isPremitive1 && componentClass2.isAssignableFrom(TypeHelper.getEquivalentClass(componentClass1)))) {
            resultComponentClass = componentClass2;
        } else {
            resultComponentClass = Object.class;
        }
        resultArray = TypeHelper.getArrayFactory(resultComponentClass)
                .orElse(null).apply(length1 + length2);
        try {
            setAll(resultArray, elementGetter);
            return resultArray;
        } catch (Exception ex) {
            return null;
        }
    }

    private static Map<Class, Tuple3<
            BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>>
            , BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>>
            , BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>>>> getArraysSetAll() {
        Map<Class, Tuple3<
                BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>>
                , BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>>
                , BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>>>> map = new HashMap();
        BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>> parallelSetAll = (Object array, FunctionThrowable<Integer, Object> intFunction) -> {
            Arrays.parallelSetAll((int[]) array, i -> (Integer) intFunction.orElse(0).apply(i));
        };
        BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>> serialSetAll = (Object array, FunctionThrowable<Integer, Object> intFunction) -> {
            Arrays.setAll((int[]) array, i -> (Integer) intFunction.orElse(0).apply(i));
        };
        BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>> setAll = serialSetAll;
        map.put(int.class, Tuple.create(parallelSetAll, serialSetAll, setAll));

        parallelSetAll = (Object array, FunctionThrowable<Integer, Object> intFunction) -> {
            Arrays.parallelSetAll((double[]) array, i -> (Double) intFunction.orElse(0d).apply(i));
        };
        serialSetAll = (Object array, FunctionThrowable<Integer, Object> intFunction) -> {
            Arrays.setAll((double[]) array, i -> (Double) intFunction.orElse(0).apply(i));
        };
        setAll = serialSetAll;
        map.put(double.class, Tuple.create(parallelSetAll, serialSetAll, setAll));

        parallelSetAll = (Object array, FunctionThrowable<Integer, Object> intFunction) -> {
            Arrays.parallelSetAll((long[]) array, i -> (Long) intFunction.orElse(0L).apply(i));
        };
        serialSetAll = (Object array, FunctionThrowable<Integer, Object> intFunction) -> {
            Arrays.setAll((long[]) array, i -> (Long) intFunction.orElse(0L).apply(i));
        };
        setAll = serialSetAll;
        map.put(long.class, Tuple.create(parallelSetAll, serialSetAll, setAll));
        return map;
    }

    private static Tuple3<
            BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>>
            , BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>>
            , BiConsumerThrowable<Object, FunctionThrowable<Integer, Object>>> getAssetSetter(Class componentClass) {
        checkWithoutNull(componentClass);
        TriConsumerThrowable<Object, Integer, Object> elementSetter;
        elementSetter = TypeHelper.getArrayElementSetter(componentClass);

        TriFunctionThrowable.TriFunction<Object, Integer, Object, Boolean> setElementWithException =
                (array, index, element) -> {
                    try {
                        elementSetter.accept(array, index, element);
                        return false;
                    } catch (Exception ex) {
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
            if (length < TypeHelper.PARALLEL_EVALUATION_THRESHOLD) {
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
     * Set1 all elements of the concerned array with values generated by <tt>generator</tt> parallelly.
     *
     * @param array     array of any types
     * @param generator function to get object values of the array elements with a single int as input
     */
    public static void setAllParallel(Object array, FunctionThrowable<Integer, Object> generator) {
        try {
            arraySetters.getFirstValue(getComponentType(array)).accept(array, generator);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set1 all elements of the concerned array with values generated by <tt>generator</tt> serailly.
     *
     * @param array     array of any types
     * @param generator function to get object values of the array elements with a single int as input
     */
    public static void setAllSerial(Object array, FunctionThrowable<Integer, Object> generator) {
        try {
            arraySetters.getSecondValue(getComponentType(array)).accept(array, generator);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set1 all elements of the concerned array with values generated by <tt>generator</tt>.
     *
     * @param array     array of any types
     * @param generator function to get object values of the array elements with a single int as input
     */
    public static void setAll(Object array, FunctionThrowable<Integer, Object> generator) {
        try {
            arraySetters.getThirdValue(getComponentType(array)).accept(array, generator);
        } catch (Exception ex) {
        }
    }

    //endregion

    /**
     * Convert any array of elements of type <tt>T</tt> to {@code Object[]} version.
     *
     * @param array the array to be converted.
     * @param <T>   the element type of the array.
     * @return the converted {@code Object[]} sharing the same elements as original array.
     */
    public static <T> Object[] toObjectArray(T[] array) {
        if (array == null)
            return null;

        return (Object[]) TypeHelper.convert(array, Object.class);
    }

    //region Methods to convert array of primitive values to corresponding Object array
    /**
     * Convert the {@code boolean[]} to corresponding {@code Boolean[]}
     *
     * @param values the array of primtive boolean values
     * @return the array of {@code Boolean[]}
     */
    public static Boolean[] toObject(boolean[] values) {

        return (Boolean[]) TypeHelper.toEquivalent(values);
    }

    /**
     * Convert the {@code byte[]} to corresponding {@code Byte[]}
     * @param values the array of primtive byte values
     * @return the array of {@code Byte[]}
     */
    public static Byte[] toObject(byte[] values) {
        if (values == null)
            return null;

        return (Byte[]) TypeHelper.toEquivalent(values);
    }

    /**
     * Convert the {@code char[]} to corresponding {@code Character[]}
     * @param values the array of primtive char values
     * @return the array of {@code Character[]}
     */
    public static Character[] toObject(char[] values) {
        if (values == null)
            return null;

        return (Character[]) TypeHelper.toEquivalent(values);
    }

    /**
     * Convert the {@code float[]} to corresponding {@code Float[]}
     * @param values the array of primtive float values
     * @return the array of {@code Float[]}
     */
    public static Float[] toObject(float[] values) {
        if (values == null)
            return null;

        return (Float[]) TypeHelper.toEquivalent(values);
    }

    /**
     * Convert the {@code double[]} to corresponding {@code Double[]}
     * @param values the array of primtive double values
     * @return the array of {@code Double[]}
     */
    public static Double[] toObject(double[] values) {
        if (values == null)
            return null;

        return (Double[]) TypeHelper.toEquivalent(values);
    }

    /**
     * Convert the {@code int[]} to corresponding {@code Integer[]}
     * @param values the array of primtive int values
     * @return the array of {@code Integer[]}
     */
    public static Integer[] toObject(int[] values) {
        if (values == null)
            return null;

        return (Integer[]) TypeHelper.toEquivalent(values);
    }

    /**
     * Convert the {@code shart[]} to corresponding {@code Short[]}
     * @param values the array of primtive short values
     * @return the array of {@code Short[]}
     */
    public static Short[] toObject(short[] values) {
        if (values == null)
            return null;

        return (Short[]) TypeHelper.toEquivalent(values);
    }

    /**
     * Convert the {@code long[]} to corresponding {@code Long[]}
     * @param values the array of primtive long values
     * @return the array of {@code Long[]}
     */
    public static Long[] toObject(long[] values) {
        if (values == null)
            return null;

        return (Long[]) TypeHelper.toEquivalent(values);
    }
    //endregion

    //region Methods to convert object arrays to corresponding arrays of primitive values
    /**
     * Convert the {@code boolean[]} to corresponding {@code Boolean[]}
     * @param values the array of primtive boolean values
     * @return the array of {@code Boolean[]}
     */
    public static boolean[] toPrimitive(Boolean[] values) {
        if (values == null) {
            return null;
        }

        if (values.length > 0) {
            checkWithoutNull(values[0], (Boolean[]) TypeHelper.copyOfRange(values, 1, values.length));
        }

        return (boolean[]) TypeHelper.toEquivalent(values);
    }

    /**
     * Convert the {@code boolean[]} to corresponding {@code Boolean[]}
     * @param values the array of primtive boolean values
     * @return the array of {@code Boolean[]}
     */
    public static byte[] toPrimitive(Byte[] values) {
        if (values == null) {
            return null;
        }

        if (values.length > 0) {
            checkWithoutNull(values[0], (Byte[]) TypeHelper.copyOfRange(values, 1, values.length));
        }

        return (byte[]) TypeHelper.toEquivalent(values);
    }

    /**
     * Convert the {@code boolean[]} to corresponding {@code Boolean[]}
     * @param values the array of primtive boolean values
     * @return the array of {@code Boolean[]}
     */
    public static char[] toPrimitive(Character[] values) {
        if (values == null) {
            return null;
        }

        if (values.length > 0) {
            checkWithoutNull(values[0], (Character[]) TypeHelper.copyOfRange(values, 1, values.length));
        }

        return (char[]) TypeHelper.toEquivalent(values);
    }

    /**
     * Convert the {@code boolean[]} to corresponding {@code Boolean[]}
     * @param values the array of primtive boolean values
     * @return the array of {@code Boolean[]}
     */
    public static float[] toPrimitive(Float[] values) {
        if (values == null) {
            return null;
        }

        if (values.length > 0) {
            checkWithoutNull(values[0], (Float[]) TypeHelper.copyOfRange(values, 1, values.length));
        }

        return (float[]) TypeHelper.toEquivalent(values);
    }

    /**
     * Convert the {@code boolean[]} to corresponding {@code Boolean[]}
     * @param values the array of primtive boolean values
     * @return the array of {@code Boolean[]}
     */
    public static double[] toPrimitive(Double[] values) {
        if (values == null) {
            return null;
        }

        if (values.length > 0) {
            checkWithoutNull(values[0], (Double[]) TypeHelper.copyOfRange(values, 1, values.length));
        }

        return (double[]) TypeHelper.toEquivalent(values);
    }

    /**
     * Convert the {@code boolean[]} to corresponding {@code Boolean[]}
     * @param values the array of primtive boolean values
     * @return the array of {@code Boolean[]}
     */
    public static int[] toPrimitive(Integer[] values) {
        if (values == null) {
            return null;
        }

        if (values.length > 0) {
            checkWithoutNull(values[0], (Integer[]) TypeHelper.copyOfRange(values, 1, values.length));
        }

        return (int[]) TypeHelper.toEquivalent(values);
    }

    /**
     * Convert the {@code boolean[]} to corresponding {@code Boolean[]}
     * @param values the array of primtive boolean values
     * @return the array of {@code Boolean[]}
     */
    public static short[] toPrimitive(Short[] values) {
        if (values == null) {
            return null;
        }

        if (values.length > 0) {
            checkWithoutNull(values[0], (Short[]) TypeHelper.copyOfRange(values, 1, values.length));
        }

        return (short[]) TypeHelper.toEquivalent(values);
    }

    /**
     * Convert the {@code boolean[]} to corresponding {@code Boolean[]}
     * @param values the array of primtive boolean values
     * @return the array of {@code Boolean[]}
     */
    public static long[] toPrimitive(Long[] values) {
        if (values == null) {
            return null;
        }

        if (values.length > 0) {
            checkWithoutNull(values[0], (Long[]) TypeHelper.copyOfRange(values, 1, values.length));
        }

        return (long[]) TypeHelper.toEquivalent(values);
    }
    //endregion

    /**
     * Rearrange the elements of the given array randomely to get a new array with same elements but of different sequence.
     *
     * @param original the array to be shuffled.
     * @return a new array with same elements but of different sequence.
     */
    public static Object shuffle(Object original) {
        Class arrayClass = checkNotNull(original, "The given array is null").getClass();
        checkState(arrayClass.isArray(), "The given value must be an array!");

        int len = Array.getLength(original);
        if (len < 2) {
            return TypeHelper.copyOfRange(original, 0, len);
        }

        Integer[] indexes = Range.closedOpen(0, len).getRandomIndexes();
        Object shuffled = create(original.getClass().getComponentType(), len, i -> Array.get(original, indexes[i]));
        return shuffled;
    }

    /**
     * Iterate through the given Array or Collection to check if testing of all its elements passed or not.
     *
     * @param arrayOrCollection the Array or Collection to be checked.
     * @param predicate         the Predicate used to test the elements one by one.
     * @return              <tt>true</tt> if all elements of the given Array or Collection passed the test, otherwise <tt>false</tt>
     */
    public static  boolean matchAll(Object arrayOrCollection, Predicate predicate){
        checkWithoutNull(arrayOrCollection, predicate);

        if(arrayOrCollection instanceof Collection){
            Iterator iterator = ((Collection) arrayOrCollection).iterator();
            int index = 0;
            while (iterator.hasNext()){
                Object next = iterator.next();
                index++;
                if(!predicate.test(next)) {
                    Logger.V("Failed to test %s at position %d", next, index);
                    return false;
                }
            }
            return true;
        } else if (arrayOrCollection.getClass().isArray()) {
            int length = Array.getLength(arrayOrCollection);
            for (int i = 0; i < length; i++) {
                Object next = Array.get(arrayOrCollection, i);
                if(!predicate.test(next)) {
                    Logger.V("Failed to test %s at position %d", next, i);
                    return false;
                }
            }
            return true;
        }

        throw new IllegalStateException(
                StringHelper.tryFormatString("Type %s of %s is not supported.",
                        arrayOrCollection.getClass().getSimpleName(), arrayOrCollection));
    }

    /**
     * Iterate through the given Array or Collection to check if there is any element meet the criteria set by the given Predicate.
     *
     * @param arrayOrCollection the Array or Collection to be checked.
     * @param predicate         the Predicate used to test the elements one by one.
     * @return              <tt>true</tt> if any one of the elements of the given Array or Collection passed the test, otherwise <tt>false</tt>
     */
    public static  boolean matchAny(Object arrayOrCollection, Predicate predicate){
        checkWithoutNull(arrayOrCollection, predicate);

        if(arrayOrCollection instanceof Collection){
            Iterator iterator = ((Collection) arrayOrCollection).iterator();
            while (iterator.hasNext()){
                Object next = iterator.next();
                if(predicate.test(next)) {
                    return true;
                }
            }
            return false;
        } else if (arrayOrCollection.getClass().isArray()) {
            int length = Array.getLength(arrayOrCollection);
            for (int i = 0; i < length; i++) {
                Object next = Array.get(arrayOrCollection, i);
                if(predicate.test(next)) {
                    return true;
                }
            }
            return false;
        }

        throw new IllegalStateException(
                StringHelper.tryFormatString("Type %s of %s is not supported.",
                        arrayOrCollection.getClass().getSimpleName(), arrayOrCollection));
    }
}
