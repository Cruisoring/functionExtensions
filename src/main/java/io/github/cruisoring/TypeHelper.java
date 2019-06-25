package io.github.cruisoring;

import io.github.cruisoring.repository.TupleRepository3;
import io.github.cruisoring.repository.TupleRepository6;
import io.github.cruisoring.throwables.BiFunctionThrowable;
import io.github.cruisoring.throwables.FunctionThrowable;
import io.github.cruisoring.throwables.TriConsumerThrowable;
import io.github.cruisoring.throwables.TriFunctionThrowable;
import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple2;
import io.github.cruisoring.tuple.Tuple3;
import io.github.cruisoring.tuple.Tuple6;
import io.github.cruisoring.utility.ArrayHelper;
import io.github.cruisoring.utility.SimpleTypedList;
import sun.reflect.ConstantPool;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.github.cruisoring.Asserts.*;

/**
 * Container class of type related utilities.
 */
public class TypeHelper {
    static final int NULL_NODE = -1;
    private static final Class OBJECT_CLASS = Object.class;
    private static final int NORMAL_VALUE_NODE = 0;
    public static int EMPTY_ARRAY_NODE = -2;
    public static int EMPTY_COLLECTION_NODE = -3;
    static int _defaultParallelEvaluationThread = 100000;

    public static boolean EMPTY_ARRAY_AS_DEFAULT = false;
    public static int PARALLEL_EVALUATION_THRESHOLD = _defaultParallelEvaluationThread;

    public static EqualityStategy DEFAULT_EMPTY_EQUALITY = EqualityStategy.TypeIgnored;

    //region Common functions saved as static variables
    private static final BiFunctionThrowable<Object, Integer, Object> arrayGet = Array::get;
    private static final TriConsumerThrowable<Object, Integer, Object> arraySet = Array::set;
    /**
     * Repository to keep operators related with specific class that is kept as the keys of the map.
     * Relative operators are saved as a strong-typed Tuple7 with following elements:
     * Equivalent predicate: to evaluate if another class is regarded as equivalent to the specific class.
     * Notice: int.class and Integer.class are regarded as equivalent in this library, their array types
     * int[].class and Integer[].class are also regarded as equivalent.
     * ArrayFactory: given the length of the new array, the factory would create a new array with elements of the specific class.
     * ArrayClass:     class of the array containing elements of the specific class
     * ArrayElementSetter: setter of the array with target index and element to be set
     * CopyOfRange:    method to copy part of the original array to a new array of the same type, while its length
     * is determined by the from and to indexes
     * ConvertToString:    convert the array to a string by recursively applying the deep toString() on every elements
     * of the array
     */
    static final TupleRepository6<
            Class,              //concerned Class

            Predicate<Class> //Equivalent predicate to check if another class is regarded as equal with the concerned class
            , FunctionThrowable<Integer, Object>             //Array factory
            , Class              //Class of its array
            , TriConsumerThrowable<Object, Integer, Object>//Function to set the Element at Index of its array
            , TriFunctionThrowable<Object, Integer, Integer, Object>//copyOfRange(original, from, to) -> new array
            , Function<Object, String>             // Convert array to String
            > classOperators = TupleRepository6.fromKey(
            new HashMap<Class, Tuple6<
                    Predicate<Class>,
                    FunctionThrowable<Integer, Object>,
                    Class,
                    TriConsumerThrowable<Object, Integer, Object>,
                    TriFunctionThrowable<Object, Integer, Integer, Object>,
                    Function<Object, String>
                    >>() {
            {
                Predicate<Class> classPredicate = clazz -> int.class.equals(clazz) || Integer.class.equals(clazz);
                put(int.class, Tuple.create(
                        classPredicate
                        , int[]::new
                        , int[].class
                        , (array, index, value) -> ((int[]) array)[index] = (Integer) value
                        , (array, from, to) -> Arrays.copyOfRange((int[]) array, from, to)
                        , array -> Arrays.toString((int[]) array)));
                put(Integer.class, Tuple.create(
                        classPredicate
                        , Integer[]::new
                        , Integer[].class
                        , arraySet
                        , (array, from, to) -> Arrays.copyOfRange((Integer[]) array, from, to)
                        , array -> Arrays.toString((Integer[]) array)));
                classPredicate = clazz -> byte.class.equals(clazz) || Byte.class.equals(clazz);
                put(byte.class, Tuple.create(
                        classPredicate
                        , byte[]::new
                        , byte[].class
                        , (array, index, value) -> ((byte[]) array)[index] = (Byte) value
                        , (array, from, to) -> Arrays.copyOfRange((byte[]) array, from, to)
                        , array -> Arrays.toString((byte[]) array)));
                put(Byte.class, Tuple.create(
                        classPredicate
                        , Byte[]::new
                        , Byte[].class
                        , arraySet
                        , (array, from, to) -> Arrays.copyOfRange((Byte[]) array, from, to)
                        , array -> Arrays.toString((Byte[]) array)));
                classPredicate = clazz -> boolean.class.equals(clazz) || Boolean.class.equals(clazz);
                put(boolean.class, Tuple.create(
                        classPredicate
                        , boolean[]::new
                        , boolean[].class
                        , (array, index, value) -> ((boolean[]) array)[index] = (Boolean) value
                        , (array, from, to) -> Arrays.copyOfRange((boolean[]) array, from, to)
                        , array -> Arrays.toString((boolean[]) array)));
                put(Boolean.class, Tuple.create(
                        classPredicate
                        , Boolean[]::new
                        , Boolean[].class
                        , arraySet
                        , (array, from, to) -> Arrays.copyOfRange((Boolean[]) array, from, to)
                        , array -> Arrays.toString((Boolean[]) array)));
                classPredicate = clazz -> char.class.equals(clazz) || Character.class.equals(clazz);
                put(char.class, Tuple.create(
                        classPredicate
                        , char[]::new
                        , char[].class
                        , (array, index, value) -> ((char[]) array)[index] = (Character) value
                        , (array, from, to) -> Arrays.copyOfRange((char[]) array, from, to)
                        , array -> Arrays.toString((char[]) array)));
                put(Character.class, Tuple.create(
                        classPredicate
                        , Character[]::new
                        , Character[].class
                        , arraySet
                        , (array, from, to) -> Arrays.copyOfRange((Character[]) array, from, to)
                        , array -> Arrays.toString((Character[]) array)));
                classPredicate = clazz -> short.class.equals(clazz) || Short.class.equals(clazz);
                put(short.class, Tuple.create(
                        classPredicate
                        , short[]::new
                        , short[].class
                        , (array, index, value) -> ((short[]) array)[index] = (Short) value
                        , (array, from, to) -> Arrays.copyOfRange((short[]) array, from, to)
                        , array -> Arrays.toString((short[]) array)));
                put(Short.class, Tuple.create(
                        classPredicate
                        , Short[]::new
                        , Short[].class
                        , arraySet
                        , (array, from, to) -> Arrays.copyOfRange((Short[]) array, from, to)
                        , array -> Arrays.toString((Short[]) array)));
                classPredicate = clazz -> long.class.equals(clazz) || Long.class.equals(clazz);
                put(long.class, Tuple.create(
                        classPredicate
                        , long[]::new
                        , long[].class
                        , (array, index, value) -> ((long[]) array)[index] = (Long) value
                        , (array, from, to) -> Arrays.copyOfRange((long[]) array, from, to)
                        , array -> Arrays.toString((long[]) array)));
                put(Long.class, Tuple.create(
                        classPredicate
                        , Long[]::new
                        , Long[].class
                        , arraySet
                        , (array, from, to) -> Arrays.copyOfRange((Long[]) array, from, to)
                        , array -> Arrays.toString((Long[]) array)));
                classPredicate = clazz -> double.class.equals(clazz) || Double.class.equals(clazz);
                put(double.class, Tuple.create(
                        classPredicate
                        , double[]::new
                        , double[].class
                        , (array, index, value) -> ((double[]) array)[index] = (Double) value
                        , (array, from, to) -> Arrays.copyOfRange((double[]) array, from, to)
                        , array -> Arrays.toString((double[]) array)));
                put(Double.class, Tuple.create(
                        classPredicate
                        , Double[]::new
                        , Double[].class
                        , arraySet
                        , (array, from, to) -> Arrays.copyOfRange((Double[]) array, from, to)
                        , array -> Arrays.toString((Double[]) array)));
                classPredicate = clazz -> float.class.equals(clazz) || Float.class.equals(clazz);
                put(float.class, Tuple.create(
                        classPredicate
                        , float[]::new
                        , float[].class
                        , (array, index, value) -> ((float[]) array)[index] = (Float) value
                        , (array, from, to) -> Arrays.copyOfRange((float[]) array, from, to)
                        , array -> Arrays.toString((float[]) array)));
                put(Float.class, Tuple.create(
                        classPredicate
                        , Float[]::new
                        , Float[].class
                        , arraySet
                        , (array, from, to) -> Arrays.copyOfRange((Float[]) array, from, to)
                        , array -> Arrays.toString((Float[]) array)));
            }
            },
        null,
        TypeHelper::makeClassOperators
    );
    private static final Function<Object, Object> returnsSelf = obj -> obj;
    private static final Function<Object, Object> mapsToNull = obj -> null;

    //region Method and repository to get return type of Lambda Expression
    private static final Method _getConstantPool() throws NoSuchMethodException {
        Method method = Class.class.getDeclaredMethod("getConstantPool");
        method.setAccessible(true);
        return method;
    }

    /**
     * Repository to evaluate a Lambda expression to get its Parameter Types, and return Type.
     * Notice: the parameter types are not always accurate when some extra values are used to compose the lambda
     * <p>
     * <tt>FunctionThrowable&lt;TKey, Tuple3&lt;T,U,V&gt;&gt; valueFunction</tt>
     */
    static final TupleRepository3<OfThrowable,
            Boolean, Class[], Class> lambdaGenericInfoRepository = TupleRepository3.fromKey(
            TypeHelper::getLambdaGenericInfo
    );
    /**
     * This repository use a single class as the key, and keeps tuple of:
     * #    its default value
     * #    if its instance is composed by primitive elements
     * #    equivalent class: could be the wrapper if original class is primitive (for int[]: it is Integer[])
     * , or primitive type if original class is wrapper (for Character[][], it is char[][])
     * #    parallel converter: convert it instance to its equivalent type parallelly
     * #    serial converter: convert it instance to its equivalent type in serial
     * #    default converter: convert it instance to its equivalent type either parallelly or serially depending on the length of the array
     */
    private static final TupleRepository6<
            Class,      // original Class of the concerned object

            Boolean     // isPrmitiveType, when the original class is primitive type, or it is array of primitive type
            , Object    // default value of the concerned class
            , Class     // equivalent class: could be the wrapper if original class is primitive,
            // or primitive type if original class is wrapper
            , Function<Object, Object>  // convert the value of original class to equivalent class parallelly
            , Function<Object, Object>  // convert the value of original class to equivalent class in serial
            , Function<Object, Object>  // convert the value of original class to equivalent class either parallely or srially
            > baseTypeConverters = TupleRepository6.fromKey(
            new HashMap() {{
                //For primitive values, return itself as object would convert it to the wrapper type automatically
                Function<Object, Object> convertWithCasting = returnsSelf;
                put(boolean.class, Tuple.create(
                        true
                        , false
                        , Boolean.class
                        , convertWithCasting
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> (Boolean) fromElement;
                put(Boolean.class, Tuple.create(
                        false
                        , false
                        , boolean.class
                        , convertWithCasting
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = returnsSelf;
                put(byte.class, Tuple.create(
                        true
                        , (byte) 0
                        , Byte.class
                        , convertWithCasting
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> (Byte) fromElement;
                put(Byte.class, Tuple.create(
                        false
                        , (byte) 0
                        , byte.class
                        , convertWithCasting
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = returnsSelf;
                put(char.class, Tuple.create(
                        true
                        , (char) 0
                        , Character.class
                        , convertWithCasting
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> (Character) fromElement;
                put(Character.class, Tuple.create(
                        false
                        , (char) 0
                        , char.class
                        , convertWithCasting
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = returnsSelf;
                put(double.class, Tuple.create(
                        true
                        , 0d
                        , Double.class
                        , convertWithCasting
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> (Double) fromElement;
                put(Double.class, Tuple.create(
                        false
                        , 0d
                        , double.class
                        , convertWithCasting
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = returnsSelf;
                put(float.class, Tuple.create(
                        true
                        , 0f
                        , Float.class
                        , convertWithCasting
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> (Float) fromElement;
                put(Float.class, Tuple.create(
                        false
                        , 0f
                        , float.class
                        , convertWithCasting
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = returnsSelf;
                put(int.class, Tuple.create(
                        true
                        , 0
                        , Integer.class
                        , convertWithCasting
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> (Integer) fromElement;
                put(Integer.class, Tuple.create(
                        false
                        , 0
                        , int.class
                        , convertWithCasting
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = returnsSelf;
                put(long.class, Tuple.create(
                        true
                        , 0L
                        , Long.class
                        , convertWithCasting
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> (Long) fromElement;
                put(Long.class, Tuple.create(
                        false
                        , 0L
                        , long.class
                        , convertWithCasting
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = returnsSelf;
                put(short.class, Tuple.create(
                        true
                        , (short) 0
                        , Short.class
                        , convertWithCasting
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> (Short) fromElement;
                put(Short.class, Tuple.create(
                        false
                        , (short) 0
                        , short.class
                        , convertWithCasting
                        , convertWithCasting
                        , convertWithCasting));
            }},
            null,
            TypeHelper::makeBaseTypeConverters
    );
    /**
     * This repository use two classes as its key:
     * #    fromClass:  the type of source value to be converted by the converters
     * #    toClass:    the target type to be converted to by the converters
     * to generate and cache tuple containing:
     * #    parallelConverter:  Convert the object of type fromClass to value of type toClass in parallel
     * #    serialConverter:  Convert the object of type fromClass to value of type toClass in serial
     * #    defaultConverter:  Convert the object of type fromClass to value of type toClass either in parallel or in serial based on its length
     */
    private static final TupleRepository3.TupleKeys2<
            Class, Class   //fromClass & toClass as the keys

            , Function<Object, Object>          //Convert the first object to another in parallel
            , Function<Object, Object>          //Convert the first object to another serially
            , Function<Object, Object>          //Convert the first object by default, in parallel or serial based on the length of the array
            > deepConverters = TupleRepository3.fromKeys2(TypeHelper::getDeepEvaluators);
    //endregion
    //endregion

    //region deepIndexes based objects comparing utilities

    //region Higher-order functions to create lambda Functions
    private static <T> TriFunctionThrowable<Object, Integer, Integer, Object> asGenericCopyOfRange(Class<T> componentType) {
        return (array, from, to) -> Arrays.copyOfRange((T[]) array, from, to);
    }

    private static <T> Function<Object, String> getDeepToString(Class<T> componentClass) {
        assertAllNotNull(componentClass);

        Function<Object, String> toString = obj -> {
            T[] objects = (T[]) obj;
            int length = objects.length;
            String[] strings = new String[length];
            for (int i = 0; i < length; i++) {
                T element = objects[i];
                if (element == null) {
                    strings[i] = "null";
                } else {
                    Class elementClass = element.getClass();
                    if (elementClass.isArray()) {
                        Function<Object, String> elementToString = getArrayToString(elementClass.getComponentType());
                        strings[i] = elementToString.apply(element);
                    } else {
                        strings[i] = element.toString();
                    }
                }
            }
            return String.format("[%s]", String.join(", ", strings));
        };
        return toString;
    }

    private static TriFunctionThrowable.TriFunction<Object, Object, Integer, Boolean> getExceptionWithMapping(
            TriConsumerThrowable<Object, Integer, Object> toElementSetter
            , Function<Object, Object> elementConverter) {
        if (elementConverter == returnsSelf || elementConverter == null)
            return (fromArray, toArray, index) -> {
                try {
                    Object fromElement = Array.get(fromArray, index);
                    toElementSetter.accept(toArray, index, fromElement);
                    return false;
                } catch (Exception ex) {
                    return true;
                }
            };
        else
            return (fromArray, toArray, index) -> {
                try {
                    Object fromElement = Array.get(fromArray, index);
                    toElementSetter.accept(toArray, index, elementConverter.apply(fromElement));
                    return false;
                } catch (Exception ex) {
                    return true;
                }
            };
    }

    /**
     * Merge any numbers of int values with an int array and return the merged copy, used by getDeepIndexes0()
     *
     * @param fromRoot    An existing int array
     * @param selfIndexes new int values to be included
     * @return A new int array containing all int values in order
     */
    private static int[] mergeOfInts(int[] fromRoot, int... selfIndexes) {
        int fromLength = fromRoot.length;
        int selfLength = selfIndexes.length;
        int[] fullPath = Arrays.copyOfRange(fromRoot, 0, fromLength + selfLength);

        for (int i = 0; i < selfLength; i++) {
            fullPath[fromLength + i] = selfIndexes[i];
        }
        return fullPath;
    }

    /**
     * Get the element of  at the specific position.
     * @param object    the {@code Array} or {@code Collection} to be searched.
     * @param index     the position of the element of the {@code Array} or {@code Collection}
     * @return the element at the specific position of the given {@code Array} or {@code Collection}
     * @throws IllegalArgumentException if the index is out o f range
     *          IllegalStateException if the given object is not an {@code Array} or {@code Collection}
     */
    public static Object getElement(Object object, int index) {
        Asserts.assertAllTrue(object != null, index >= 0);

        if (object.getClass().isArray()) {
            int length = Array.getLength(object);
            if (index >= length) {
                throw new IllegalArgumentException("Invalid index " + index + " of array with length " + length);
            }
            return Array.get(object, index);
        } else if (object instanceof Collection) {
            Collection collection = (Collection) object;
            int size = collection.size();
            if (index >= size) {
                throw new IllegalArgumentException("Invalid index " + index + " of Collection with size " + size);
            }
            Iterator iterator = collection.iterator();
            int i = 0;
            Object next;
            while (iterator.hasNext()) {
                next = iterator.next();
                if (i++ == index) {
                    return next;
                }
            }
        }

        throw new IllegalStateException("Fail to get element of " + deepToString(object) + " at index " + index);
    }

    /**
     * Get the element of a given Object by all indexes of its parent arrays or collections.
     *
     * @param object    the object to be searched, it can be a {@code Array}, a {@code Collection} or a normal Java Object.
     * @param deepIndex     indexes as a route to the concerned element. {@code int[0]} stands for the concerned Object by itself.
     * @return the root Object itself if the indexes is empty; or the element of the concerned Object referred by the given {@code deepIndex}
     */
    public static Object getDeepElement(Object object, int[] deepIndex) {
        Asserts.assertAllTrue(deepIndex != null);

        int deepIndexLength = deepIndex.length;
        if (deepIndexLength == 0 || deepIndex[0] < 0) {
            return object;
        }

        Object child = getElement(object, deepIndex[0]);
        int[] childIndex = (int[]) copyOfRange(deepIndex, 1, deepIndexLength);
        return getDeepElement(child, childIndex);
    }

    /**
     * Return an array of int[] to get the node type and indexes to access EVERY node elements of the concerned object.
     *
     * @param object Object under concerned
     * @return An array of int[]. Each node element is mapped to one int[] where the last int shows the type of the
     * value of the node:
     * NULL_NODE when it is null, EMPTY_ARRAY_NODE when it is an array of 0 length and otherwise NORMAL_VALUE_NODE.
     * The other int values of the int[] are indexes of the node element or its parent array in their container arrays.
     */
    public static int[][] getDeepIndexes(Object object) {
        return getDeepIndexes0(object, new int[0]);
    }

    private static int[][] getDeepIndexes0(Object object, int[] indexes) {
        if (object == null) {
            return new int[][]{mergeOfInts(indexes, NULL_NODE)};
        }

        if (object.getClass().isArray()) {
            int length = Array.getLength(object);
            if (length == 0)
                return new int[][]{mergeOfInts(indexes, EMPTY_ARRAY_NODE)};

            TypedList<int[]> list = new SimpleTypedList<>();
            for (int i = 0; i < length; i++) {
                Object element = Array.get(object, i);
                int[][] positions = getDeepIndexes0(element, mergeOfInts(indexes, i));
                list.appendAll(positions);
            }
            return list.toArray(new int[0][]);
        } else if (object instanceof Collection) {
            Collection collection = (Collection) object;
            int size = collection.size();
            if (size == 0) {
                return new int[][]{mergeOfInts(indexes, EMPTY_COLLECTION_NODE)};
            }

            TypedList<int[]> list = new SimpleTypedList<>();
            Iterator iterator = collection.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                Object element = iterator.next();
                int[][] positions = getDeepIndexes0(element, mergeOfInts(indexes, i++));
                list.appendAll(positions);
            }
            return list.toArray(new int[0][]);

        } else {
            return new int[][]{mergeOfInts(indexes, NORMAL_VALUE_NODE)};
        }
    }

    /**
     * Compare the two nodes of two Objects that share the same indexes to retrieve
     *
     * @param obj1               First Object to be compared
     * @param obj2               Second Object to be compared
     * @param indexes            the deep indexes if the specific node is kept as element of an array or collection,
     *                          or empty if the root Object is the specific node
     * @param equalityStategy Strategy to compare nodes when both are empty arrays: TypeIgnored, BetweenAssignableTypes, SameTypeOnly
     * @return <code>true</code> if the two nodes are identical, otherwise <code>false</code>
     */
    public static boolean nodeEquals(Object obj1, Object obj2, int[] indexes, EqualityStategy equalityStategy) {
        assertAllFalse(obj1 == null, obj2 == null, indexes == null);

        int depth = indexes.length;
        int nodeType = indexes[depth - 1];

        if (nodeType == NULL_NODE) {
            //Now both nodes are null, return true if TypeIgnored or no access to their parents
            if (equalityStategy == EqualityStategy.TypeIgnored || equalityStategy == EqualityStategy.EmptyAsNull || depth == 1) {
                return true;
            }

            //Otherwise their parents must be assignable in any manner
            indexes = (int[]) copyOfRange(indexes, 0, depth - 2);
            Class class1 = getDeepElement(obj1, indexes).getClass();
            Class class2 = getDeepElement(obj2, indexes).getClass();
            return (equalityStategy == EqualityStategy.SameTypeOnly)
                    ? class1.equals(class2)
                    : canBeAssigned(class1, class2) || canBeAssigned(class2, class1);
        } else if (nodeType == EMPTY_ARRAY_NODE) {
            //Now both nodes are empty Array, return true if TypeIgnored
            if (equalityStategy == EqualityStategy.TypeIgnored) {
                return true;
            }

            //Otherwise their classes must be assignable in any manner
            Class class1 = getDeepElement(obj1, indexes).getClass();
            Class class2 = getDeepElement(obj2, indexes).getClass();
            return equalityStategy == EqualityStategy.SameTypeOnly ?
                    class1.equals(class2) : canBeAssigned(class1, class2) || canBeAssigned(class2, class1);
        } else if (nodeType == EMPTY_COLLECTION_NODE) {
            //Now both nodes are empty Collection, simply return true
            return true;
        } else {
            indexes = (int[]) copyOfRange(indexes, 0, depth - 1);
            //getDeepElement would always cast primitive type values to their wrapper objects
            Object node1 = getDeepElement(obj1, indexes);
            Object node2 = getDeepElement(obj2, indexes);
            return node1.equals(node2);
        }
    }

    /**
     * Helper method to validate if the 2 Objects are equal by comparing their deepIndexes first, then using their deepIndexes
     * to compare their node values either serially or parallelly based on how many nodes they have
     *
     * @param obj1               First Object to be compared
     * @param obj2               Second Object to be compared
     * @param deepIndexes         identical deepIndexes of the above two objects
     * @param equalityStategy   Strategy to compare nodes when both are empty arrays: TypeIgnored, BetweenAssignableTypes, SameTypeOnly
     * @return <code>true</code> if they have same set of values, otherwise <code>false</code>
     */
    static boolean deepValueEquals(Object obj1, Object obj2, int[][] deepIndexes, EqualityStategy equalityStategy) {
        int length = deepIndexes.length;

        if (length < PARALLEL_EVALUATION_THRESHOLD) {
            for (int i = 0; i < length; i++) {
                int[] indexes = deepIndexes[i];
                if (!nodeEquals(obj1, obj2, indexes, equalityStategy))
                    return false;
            }
            return true;
        } else {
            boolean allEquals = IntStream.range(0, length).boxed().parallel()
                    .allMatch(i -> nodeEquals(obj1, obj2, deepIndexes[i], equalityStategy));
            return allEquals;
        }
    }

    /**
     * Helper method to validate if the 2 Objects are equal by comparing their deepIndexes first, then using their deepIndexes
     * to compare their node values parallelly
     *
     * @param obj1               First Object to be compared
     * @param obj2               Second Object to be compared
     * @param deepIndexes         identical deepIndexes of the above two objects
     * @param equalityStategy   Strategy to compare nodes when both are empty arrays: TypeIgnored, BetweenAssignableTypes, SameTypeOnly
     * @return <code>true</code> if they have same set of values, otherwise <code>false</code>
     */
    static boolean deepEqualsParallel(Object obj1, Object obj2, int[][] deepIndexes, EqualityStategy equalityStategy) {
        int length = deepIndexes.length;
        boolean allEquals = IntStream.range(0, length).boxed().parallel()
                .allMatch(i -> nodeEquals(obj1, obj2, deepIndexes[i], equalityStategy));
        return allEquals;
    }

    /**
     * Helper method to validate if the 2 Objects are equal by comparing their deepIndexes first, then using their deepIndexes
     * to compare their node values either serially
     *
     * @param obj1               First Object to be compared
     * @param obj2               Second Object to be compared
     * @param deepIndexes         identical deepIndexes of the above two objects
     * @param equalityStategy Strategy to compare nodes when both are empty arrays: TypeIgnored, BetweenAssignableTypes, SameTypeOnly
     * @return <code>true</code> if they have same set of values, otherwise <code>false</code>
     */
    private static boolean deepEqualsSerial(Object obj1, Object obj2, int[][] deepIndexes, EqualityStategy equalityStategy) {
        int length = deepIndexes.length;
        for (int i = 0; i < length; i++) {
            int[] indexes = deepIndexes[i];
            if (!nodeEquals(obj1, obj2, indexes, equalityStategy))
                return false;
        }
        return true;
    }

    /**
     * Check if the given Object is null or empty array/Collection.
     *
     * @param object Object under test.
     * @return <tt>true</tt> if the given Object is null or empty Array or empty Collection, otherwise <tt>false</tt>
     */
    public static boolean isNullOrEmpty(Object object) {
        if (object == null) {
            return true;
        } else if (object instanceof Collection) {
            return ((Collection) object).isEmpty();
        } else if (object.getClass().isArray()) {
            return Array.getLength(object) == 0;
        }

        return false;
    }

    /**
     * Check to see if actual value of specific type can be assigned to a variable declared as specific type.
     * @param declaringType     the declared type of the variable.
     * @param actualValueType   the type of the actual value to be assigned to the declared variable.
     * @return      <tt>true</tt> if the actual value can be assigned to the declared variable.
     */
    public static boolean canBeAssigned(Class declaringType, Class actualValueType) {
        assertAllNotNull(declaringType, actualValueType);

        declaringType = isPrimitive(declaringType) ? getEquivalentClass(declaringType) : declaringType;
        actualValueType = isPrimitive(actualValueType) ? getEquivalentClass(actualValueType) : actualValueType;

        if (declaringType.isAssignableFrom(actualValueType)) {
            return true;
        } else if (declaringType.isArray() && actualValueType.isArray()) {
            return canBeAssigned(declaringType.getComponentType(), actualValueType.getComponentType());
        } else if (declaringType.isArray() || actualValueType.isArray()) {
            return false;
        } else {
            return false;
        }
    }

    /**
     * This method check if two Objects are equal if both are simple values, otherwise determine their possibilities only by their types without considering element values.
     *
     * @param obj1 first Object to be compared
     * @param obj2 second Object to be compared
     * @param equalityStategy     Strategy to compare nodes when both are empty arrays: EmptyAsNull, TypeIgnored, BetweenAssignableTypes, SameTypeOnly
     * @return <code>true</code> if they are equal,
     * <code>false</code> if they are not equal or cannot be assignedFrom,
     * otherwise <code>null</code>
     */
    public static Boolean canValueEquals(Object obj1, Object obj2, EqualityStategy equalityStategy) {
        if (obj1 == obj2 || (obj1 != null && obj1.equals(obj2))) {
            return true;
        } else if (equalityStategy == EqualityStategy.EmptyAsNull) {
            if (isNullOrEmpty(obj1)) {
                return isNullOrEmpty(obj2);
            } else if (isNullOrEmpty(obj2)) {
                return false;
            }
        } else if (obj1 == null || obj2 == null) {
            return false;
        } else if (equalityStategy == EqualityStategy.SameTypeOnly) {
            if (obj1.getClass() != obj2.getClass()) {
                return false;
            } else {
                return null;
            }
        }

        if (equalityStategy == EqualityStategy.BetweenAssignableTypes) {
            Class class1 = obj1.getClass();
            Class class2 = obj2.getClass();

            if (canBeAssigned(class1, class2) || canBeAssigned(class2, class1)) {
                return null;
            } else {
                return false;
            }
        } else if (equalityStategy == EqualityStategy.TypeIgnored) {
            boolean isNullOrEmpty1 = isNullOrEmpty(obj1);
            boolean isNullOrEmpty2 = isNullOrEmpty(obj2);
            if (isNullOrEmpty1 && isNullOrEmpty2) {
                return true;
            } else if (isNullOrEmpty1 || isNullOrEmpty2) {
                return false;
            } else {
                return null;
            }
        }

        return false;
    }

    /**
     * Comparing two objects by comparing their actual values with desired strategies.
     * First by treating them as Non-Array Objects or Empty arrays, then compare them by deepDepth either serial or parallel
     * Note: primitive types are converted to their corresponding wrappers automatically
     *
     * @param obj1               first object to be compared
     * @param obj2               second object to be compared
     * @param equalityStategy     Strategy to compare nodes when both are empty arrays: TypeIgnored, BetweenAssignableTypes, SameTypeOnly
     * @return <code>true</code> if both objects have same values, otherwise <code>false</code>
     */
    public static boolean valueEquals(Object obj1, Object obj2, EqualityStategy equalityStategy) {
        final Boolean simpleEquals = canValueEquals(obj1, obj2, equalityStategy);
        if (simpleEquals != null)
            return simpleEquals;

        int[][] deepIndexes1 = getDeepIndexes(obj1);
        int[][] deepIndexes2 = getDeepIndexes(obj2);
        if (!Arrays.deepEquals(deepIndexes1, deepIndexes2))
            return false;

        return deepValueEquals(obj1, obj2, deepIndexes1, equalityStategy);
    }

    /**
     * Comparing two objects by comparing their actual values with default strategies.
     * First by treating them as Non-Array Objects or Empty arrays, then compare them by deepDepth either serial or parallel
     * Note: primitive types are converted to their corresponding wrappers automatically
     *
     * @param obj1 first object to be compared
     * @param obj2 second object to be compared
     * @return <code>true</code> if both objects have same values, otherwise <code>false</code>
     */
    public static boolean valueEquals(Object obj1, Object obj2) {
        return valueEquals(obj1, obj2, DEFAULT_EMPTY_EQUALITY);
    }

    /**
     * Comparing two objects with deepValueEquals directly when their deepDepth provided
     *
     * @param obj1        first object to be compared
     * @param obj2        second object to be compared
     * @param deepIndexes1 deepIndexes of the first object
     * @param deepIndexes2 deepIndexes of the second object
     * @return <code>true</code> if both objects have same values, otherwise <code>false</code>
     */
    public static boolean valueEquals(Object obj1, Object obj2, int[][] deepIndexes1, int[][] deepIndexes2) {
        assertAllNotNull(deepIndexes1, deepIndexes2);

        if (!Arrays.deepEquals(deepIndexes1, deepIndexes2))
            return false;

        return deepValueEquals(obj1, obj2, deepIndexes1, DEFAULT_EMPTY_EQUALITY);
    }

    /**
     * Comparing two objects by comparing their actual values in parallel with desired strategies.
     * First by treating them as Non-Array Objects or Empty arrays, then compare them by deepDepth parallelly
     * Note: primitive types are converted to their corresponding wrappers automatically
     *
     * @param obj1               first object to be compared
     * @param obj2               second object to be compared
     * @param equalityStategy Strategy to compare nodes when both are empty arrays: TypeIgnored, BetweenAssignableTypes, SameTypeOnly
     * @return <code>true</code> if both objects have same values, otherwise <code>false</code>
     */
    public static boolean valueEqualsParallel(Object obj1, Object obj2, EqualityStategy equalityStategy) {
        final Boolean singleObjectConverter = canValueEquals(obj1, obj2, equalityStategy);
        if (singleObjectConverter != null)
            return singleObjectConverter;

        int[][] deepIndexes1 = getDeepIndexes(obj1);
        int[][] deepIndexes2 = getDeepIndexes(obj2);
        if (!Arrays.deepEquals(deepIndexes1, deepIndexes2))
            return false;
        return deepEqualsParallel(obj1, obj2, deepIndexes1, equalityStategy);
    }

    /**
     * Comparing two objects by comparing their actual values in parallel with default strategies.
     * First by treating them as Non-Array Objects or Empty arrays, then compare them by deepDepth parallelly
     * Note: primitive types are converted to their corresponding wrappers automatically
     *
     * @param obj1 first object to be compared
     * @param obj2 second object to be compared
     * @return <code>true</code> if both objects have same values, otherwise <code>false</code>
     */
    public static boolean valueEqualsParallel(Object obj1, Object obj2) {
        return valueEqualsParallel(obj1, obj2, DEFAULT_EMPTY_EQUALITY);
    }
    //endregion

    /**
     * Comparing two objects with deepValueEquals in parallel when their deepDepth provided
     *
     * @param obj1        first object to be compared
     * @param obj2        second object to be compared
     * @param deepIndexes1 deepIndexes of the first object
     * @param deepIndexes2 deepIndexes of the second object
     * @return <code>true</code> if both objects have same values, otherwise <code>false</code>
     */
    public static boolean valueEqualsParallel(Object obj1, Object obj2, int[][] deepIndexes1, int[][] deepIndexes2) {
        assertAllNotNull(deepIndexes1, deepIndexes2);

        if (!Arrays.deepEquals(deepIndexes1, deepIndexes2))
            return false;

        return deepEqualsParallel(obj1, obj2, deepIndexes1, DEFAULT_EMPTY_EQUALITY);
    }

    /**
     * Comparing two objects by comparing their actual values in serial with desired strategies.
     * First by treating them as Non-Array Objects or Empty arrays, then compare them by deepDepth either serially
     * Note: primitive types are converted to their corresponding wrappers automatically
     *
     * @param obj1               first object to be compared
     * @param obj2               second object to be compared
     * @param equalityStategy Strategy to compare nodes when both are empty arrays: TypeIgnored, BetweenAssignableTypes, SameTypeOnly
     * @return <code>true</code> if both objects have same values, otherwise <code>false</code>
     */
    public static boolean valueEqualsSerial(Object obj1, Object obj2, EqualityStategy equalityStategy) {
        final Boolean singleObjectConverter = canValueEquals(obj1, obj2, equalityStategy);
        if (singleObjectConverter != null)
            return singleObjectConverter;

        int[][] deepIndexes1 = getDeepIndexes(obj1);
        int[][] deepIndexes2 = getDeepIndexes(obj2);
        if (!Arrays.deepEquals(deepIndexes1, deepIndexes2))
            return false;
        return deepEqualsSerial(obj1, obj2, deepIndexes1, equalityStategy);
    }

    /**
     * Comparing two objects by comparing their actual values in serial with default strategies.
     * First by treating them as Non-Array Objects or Empty arrays, then compare them by deepDepth either serially
     * Note: primitive types are converted to their corresponding wrappers automatically
     *
     * @param obj1 first object to be compared
     * @param obj2 second object to be compared
     * @return <code>true</code> if both objects have same values, otherwise <code>false</code>
     */
    public static boolean valueEqualsSerial(Object obj1, Object obj2) {
        return valueEqualsSerial(obj1, obj2, DEFAULT_EMPTY_EQUALITY);
    }

    /**
     * Comparing two objects with deepValueEquals parallelly when their deepDepth provided
     *
     * @param obj1        first object to be compared
     * @param obj2        second object to be compared
     * @param deepIndexes1 deepIndexes of the first object
     * @param deepIndexes2 deepIndexes of the second object
     * @return <code>true</code> if both objects have same values, otherwise <code>false</code>
     */
    public static boolean valueEqualsSerially(Object obj1, Object obj2, int[][] deepIndexes1, int[][] deepIndexes2) {
        assertAllNotNull(deepIndexes1, deepIndexes2);

        if (!Arrays.deepEquals(deepIndexes1, deepIndexes2))
            return false;

        return deepEqualsSerial(obj1, obj2, deepIndexes1, DEFAULT_EMPTY_EQUALITY);
    }

    @SuppressWarnings("unchecked")
    protected static ConstantPool getConstantPoolOfClass(Class objectClass) {
        try {
            Method method = _getConstantPool();
            return (ConstantPool) method.invoke(objectClass);
        } catch (Exception e) {
            return null;
        }
    }
    //endregion

    //region Repository with Class as the key, to keep 7 common used attributes or operators

    private static Tuple3<Boolean, Class[], Class> getLambdaGenericInfo(OfThrowable lambda) {
        Class lambdaClass = checkNoneNulls(lambda).getClass();
        ConstantPool constantPool = TypeHelper.getConstantPoolOfClass(lambdaClass);
        if(constantPool == null) {
            return null;
        }

        Method functionInterfaceMethod = null;
        int index = constantPool.getSize();
        while (--index >= 0) {
            try {
                functionInterfaceMethod = (Method) constantPool.getMethodAt(index);
                break;
            } catch (Exception ex) {
            }
        }
        Class[] parameterClasses = checkNotNull(functionInterfaceMethod, "Failed to get the Method instance.").getParameterTypes();
        int parameterCount = functionInterfaceMethod.getParameterCount();
        Class returnClass = functionInterfaceMethod.getReturnType();

        return Tuple.create(parameterClasses.length == parameterCount, parameterClasses, returnClass);
    }

    /**
     * Helper method to get the return type of a RunnableThrowable (as void.class) or SupplierThrowable.
     * Notice: only applicable on first-hand lambda expressions. Lambda Expressions created by Lambda would erase the return type in Java 1.8.161.
     *
     * @param aThrowable solid Lambda expression
     * @return The type of the return value defined by the Lambda Expression.
     */
    public static Class getReturnType(OfThrowable aThrowable) {
        return lambdaGenericInfoRepository.getThirdValue(aThrowable);
    }
    //endregion

    private static Tuple6<
            Predicate<Class>,
            FunctionThrowable<Integer, Object>,
            Class,
            TriConsumerThrowable<Object, Integer, Object>,
            TriFunctionThrowable<Object, Integer, Integer, Object>,
            Function<Object, String>
            > makeClassOperators(Class clazz) throws Exception {
        FunctionThrowable<Integer, Object> arrayFactory = (Integer length) -> Array.newInstance(clazz, length);
        Predicate<Class> cPredicate;

        if (!clazz.isArray()) {
            cPredicate = otherClass -> otherClass != null && clazz.isAssignableFrom(otherClass);
        } else {
            Class componentClass = clazz.getComponentType();
            Predicate<Class> componentPredicate = getClassEqualitor(componentClass);
            cPredicate = otherClass -> otherClass != null && otherClass.isArray()
                    && componentPredicate.test(otherClass.getComponentType());
        }

        Class arrayClass = arrayFactory.apply(0).getClass();
        TriConsumerThrowable<Object, Integer, Object> setElement = arraySet;
        TriFunctionThrowable<Object, Integer, Integer, Object> copyOfRange = asGenericCopyOfRange(clazz);
        Function<Object, String> toString = getDeepToString(clazz);
        return Tuple.create(cPredicate, arrayFactory, arrayClass, setElement, copyOfRange, toString);

    }

    /**
     * Retrive the class equalitor of the target class to evaluate if another class is equivalent to this class
     *
     * @param clazz The class to be served by the equalitor
     * @return Predicate&lt;Class&gt; to evaluate if another class is equivalent to <tt>clazz</tt>
     */
    public static Predicate<Class> getClassEqualitor(Class clazz) {
        if (clazz == null) return null;

        return classOperators.getFirstValue(clazz);
    }

    /**
     * Evalueate if two classes are identical or one is wrapper of another
     *
     * @param class1 first class to be evaluated
     * @param class2 second class to be evaluated
     * @return <code>true</code> if they are equivalent, otherwise <code>false</code>
     */
    public static boolean areEquivalent(Class class1, Class class2) {
        assertAllNotNull(class1, class2);

        return classOperators.getFirstValue(class1).test(class2);
    }

    /**
     * Retrive the array factory of the target class
     *
     * @param clazz The class of the elements of the array
     * @return Factory to create new Array of specific length and elements of type <tt>clazz</tt>
     */
    public static FunctionThrowable<Integer, Object> getArrayFactory(Class clazz) {
        if (clazz == null) return null;

        return classOperators.getSecondValue(clazz);
    }

    /**
     * Get the class of the Array composed by elements of the concerned class
     *
     * @param clazz type of the elements to compose the array
     * @return class of the array composed by elements of the concerned class
     */
    public static Class getArrayClass(Class clazz) {
        if (clazz == null) return null;

        return classOperators.getThirdValue(clazz);
    }

    /**
     * get the setter operator to set element at specific position of concerned array with given value
     *
     * @param clazz type of the elements to compose the array
     * @return operator receiving 3 arguments (array, index, value) and set the element at index of the array with the given value
     */
    public static TriConsumerThrowable<Object, Integer, Object> getArrayElementSetter(Class clazz) {
        if (clazz == null) return null;

        return classOperators.getFourthValue(clazz);
    }

    /**
     * get the copyOfRange operator to build new array of the same type, but with different ranges
     *
     * @param clazz type of the elements to compose the array
     * @return copyOfRange operator receiving 3 arguments(originalArray, fromIndex, toIndex) and return the new array
     * of the same type as originalArray, but with part or all elements as specified by the fromIndex and toIndex
     */
    public static TriFunctionThrowable<Object, Integer, Integer, Object> getArrayRangeCopier(Class clazz) {
        if (clazz == null) return null;

        return classOperators.getFifthValue(clazz);
    }

    /**
     * Copies the specified range of the specified array into a new array.
     * The initial index of the range (<tt>from</tt>) must lie between zero
     * and <tt>original.length</tt>, inclusive.  The value at
     * <tt>original[from]</tt> is placed into the initial element of the copy
     * (unless <tt>from == original.length</tt> or <tt>from == to</tt>).
     * Values from subsequent elements in the original array are placed into
     * subsequent elements in the copy.  The final index of the range
     * (<tt>to</tt>), which must be greater than or equal to <tt>from</tt>,
     * may be greater than <tt>original.length</tt>, in which case
     * <tt>0f</tt> is placed in all elements of the copy whose index is
     * greater than or equal to <tt>original.length - from</tt>.  The length
     * of the returned array will be <tt>to - from</tt>.
     *
     * @param array the array from which a range is to be copied
     * @param from  the initial index of the range to be copied, inclusive
     * @param to    the final index of the range to be copied, exclusive.
     *              (This index may lie outside the array.)
     * @return a new array containing the specified range from the original array,
     * truncated or padded with zeros to obtain the required length; null if anything wrong
     */
    public static Object copyOfRange(Object array, int from, int to) {
        try {
            if (array == null) return null;
            Class compoentType = checkNotNull(array.getClass().getComponentType(), "failed to get the componentType of %s", array);
            TriFunctionThrowable<Object, Integer, Integer, Object> copier = checkNotNull(
                    getArrayRangeCopier(compoentType), "Failed to get ArrayRangeCopier of %s", compoentType);
            return copier.apply(array, from, to);
        } catch (Exception ex) {
            return null;
        }
    }

    //region Repository to keep equivalent class and operators of given classes

    /**
     * get the deep toString operator to compose a String reflect all values of the elements, calling the corresponding
     * deep toString operator of the elements if they are arrays
     *
     * @param clazz type of the elements composing the concerned array
     * @return deep toString operator to capture all elements of the array and any arrays represented by the elements
     */
    public static Function<Object, String> getArrayToString(Class clazz) {
        if (clazz == null) return null;

        return classOperators.getSixthValue(clazz);
    }

    /**
     * This higher-order method parse a class to get
     * 1) its default value
     * 2) if it's finally composed of primitive values
     * 3) its equivalent class. For example: for int[][], its equivalent class is Integer[][] and vice versa
     * 4) 3 converters to convert varibles of this class to its equivalent type: parallelly, serially or choosing either way based on the array length
     *
     * @param clazz Class of the concerned object
     * @return Tuple of 6 elements containing
     * #    its default value
     * #    if its instance is composed by primitive elements
     * #    equivalent class: could be the wrapper if original class is primitive (for int[]: it is Integer[])
     * , or primitive type if original class is wrapper (for Character[][], it is char[][])
     * #    parallel converter: convert it instance to its equivalent type parallelly
     * #    serial converter: convert it instance to its equivalent type in serial
     * #    default converter: convert it instance to its equivalent type either parallelly or serially depending on the length of the array
     */
    private static Tuple6<Boolean, Object, Class, Function<Object, Object>, Function<Object, Object>, Function<Object, Object>> makeBaseTypeConverters(Class clazz) {
        boolean isArray = clazz.isArray();
        if (!isArray && !clazz.isPrimitive())
            return Tuple.create(false, null, OBJECT_CLASS, returnsSelf, returnsSelf, returnsSelf);

        Class componentClass = clazz.getComponentType();
        Boolean isPrimitive = isPrimitive(componentClass);

        //Use either empty array or null as the default value
        Object defaultValue = EMPTY_ARRAY_AS_DEFAULT ? ArrayHelper.getNewArray(componentClass, 0) : null;
        Class equivalentComponentClass = getEquivalentClass(componentClass);

        Class equivalentClass = classOperators.getThirdValue(equivalentComponentClass);
        Function<Object, Object> componentConverter = getToEquivalentSerialConverter(componentClass);
        TriConsumerThrowable<Object, Integer, Object> equivalentSetter = getArrayElementSetter(equivalentComponentClass);

        TriFunctionThrowable.TriFunction<Object, Object, Integer, Boolean> getExceptionWhileMapping =
                getExceptionWithMapping(equivalentSetter, componentConverter);

        Function<Object, Object> parallelConverter =
                equivalentClass == null ? returnsSelf
                        : fromArray -> {
                    if (fromArray == null) return null;

                    int length = Array.getLength(fromArray);
                    Object toArray = ArrayHelper.getNewArray(equivalentComponentClass, length);
                    Optional<Integer> firstExceptionIndex = IntStream.range(0, length)
                            .boxed()
                            .parallel()
                            .filter(i -> getExceptionWhileMapping.apply(fromArray, toArray, i))
                            .findFirst();       //To terminate immediately if any Exception caught
                    return firstExceptionIndex.isPresent() ? null : toArray;
                };

        Function<Object, Object> serialConverter = equivalentClass == null ? returnsSelf
                : fromArray -> {
            if (fromArray == null) return null;

            int length = Array.getLength(fromArray);
            Object toArray = ArrayHelper.getNewArray(equivalentComponentClass, length);
            try {
                for (int i = 0; i < length; i++) {
                    //Three-steps to do: get original element first, convert to equivalent value, and then set to
                    // the corresponding element of the converted array
                    //Get original element
                    Object fromElement = Array.get(fromArray, i);
                    //Get the converted element
                    //Set1 the converted value to the target array
                    equivalentSetter.accept(toArray, i, componentConverter.apply(fromElement));
                }
                return toArray;
            } catch (Exception ex) {
                return null;
            }
        };

        Function<Object, Object> defaultConverterr = equivalentClass == null ? returnsSelf
                : fromArray -> {
            if (fromArray == null) return null;

            int length = Array.getLength(fromArray);
            Object toArray = ArrayHelper.getNewArray(equivalentComponentClass, length);
            if (length < PARALLEL_EVALUATION_THRESHOLD) {
                try {
                    for (int i = 0; i < length; i++) {
                        //Three-steps to do: get original element first, convert to equivalent value, and then set to
                        // the corresponding element of the converted array
                        //Get original element
                        Object fromElement = Array.get(fromArray, i);
                        //Get the converted element
                        //Set1 the converted value to the target array
                        equivalentSetter.accept(toArray, i, componentConverter.apply(fromElement));
                    }
                    return toArray;
                } catch (Exception ex) {
                    return null;
                }
            } else {
                Optional<Integer> firstExceptionIndex = IntStream.range(0, length)
                        .boxed()
                        .parallel()
                        .filter(i -> getExceptionWhileMapping.apply(fromArray, toArray, i))
                        .findFirst();       //To terminate immediately if any Exception caught
                return firstExceptionIndex.isPresent() ? null : toArray;
            }
        };

        return Tuple.create(isPrimitive, defaultValue, equivalentClass, parallelConverter, serialConverter, defaultConverterr);
    }
    //endregion

    /**
     * Evaluate if the concerned class represents a primitive type or an array of primitive type elements
     *
     * @param clazz Class to be evaluated
     * @return true if and only if this class represents a primitive type or an array of primitive type elements
     */
    public static Boolean isPrimitive(Class clazz) {
        Boolean result = baseTypeConverters.getFirstValue(checkNoneNulls(clazz));
        return result == null ? false : result;
    }

    /**
     * Returns the default value of the concerned class, null by default
     *
     * @param clazz Class to be evaluated
     * @return Object represents the default value of the class.
     * Notice: for primitive types, the returned value is their wrappers.
     * For instance int or char, would get Integer or Character respectively
     * for arrays, depending on the value of <code>EMPTY_ARRAY_AS_DEFAULT</code> that can be override as System.Property
     * of the same name 'EMPTY_ARRAY_AS_DEFAULT', the default value could be:
     * <code>null</code> when <code>EMPTY_ARRAY_AS_DEFAULT</code> is false
     * a zero-length array of the type of the class, when <code>EMPTY_ARRAY_AS_DEFAULT</code> is true by default
     */
    public static Object getDefaultValue(Class clazz) {
        return baseTypeConverters.getSecondValue(checkNoneNulls(clazz));
    }

    /**
     * Return the equivalent class of the concerned class, null by default
     * <ul>
     * <li>for Primitive types or array composed of promitive values at the end, it would return corresponding wrapper classes
     * or class of arrays composed by wrapper values</li>
     * <li>for wrapper types or array composed of wrapper values at the end, it woul return correspondingl primitive classes
     * or class of arrays composed by primitive values</li>
     * <li>otherwise returns null</li>
     * </ul>
     *
     * @param clazz Class to be evaluated
     * @return equivalent class of the concerned class
     */
    public static Class getEquivalentClass(Class clazz) {
        return baseTypeConverters.getThirdValue(checkNoneNulls(clazz));
    }

    /**
     * Get the parallel converter of one class to its corresponding equivalent class
     * <ul>
     * <li>when there is an equivalent class of the concerned class, the returned Function would convert value of the
     * concerned class to value of its equivalent class (Notice: primitive types would be wrapped)</li>
     * <li>otherwise, the returned Function would </li>
     * </ul>
     *
     * @param clazz Class of the concerned object
     * @return The converter function to convert the concerned object to its equivalent one.
     */
    public static Function<Object, Object> getToEquivalentParallelConverter(Class clazz) {
        return baseTypeConverters.getFourthValue(checkNoneNulls(clazz));
    }

    /**
     * Get the serial converter of one class to its corresponding equivalent class
     * <ul>
     * <li>when there is an equivalent class of the concerned class, the returned Function would convert value of the
     * concerned class to value of its equivalent class (Notice: primitive types would be wrapped)</li>
     * <li>otherwise, the returned Function would </li>
     * </ul>
     *
     * @param clazz Class of the concerned object
     * @return The converter function to convert the concerned object to its equivalent one.
     */
    public static Function<Object, Object> getToEquivalentSerialConverter(Class clazz) {
        return baseTypeConverters.getFifthValue(checkNoneNulls(clazz));
    }

    /**
     * Get the default converter of one class to its corresponding equivalent class
     * <ul>
     * <li>when there is an equivalent class of the concerned class, the returned Function would convert value of the
     * concerned class to value of its equivalent class (Notice: primitive types would be wrapped)</li>
     * <li>otherwise, the returned Function would </li>
     * </ul>
     *
     * @param clazz Class of the concerned object
     * @return The converter function to convert the concerned object to its equivalent one.
     */
    public static Function<Object, Object> getToEquivalentConverter(Class clazz) {
        return baseTypeConverters.getSixthValue(checkNoneNulls(clazz));
    }

    /**
     * Convert an Object to its equivalent form.
     * <ul>
     * <li>For primitive types, like int, byte[], char[][], the <tt>obj</tt> is converted to Integer, Byte[] and Character[][] respectively</li>
     * <li>For wrapper types, like Boolean, Double[], Float[][], the <tt>obj</tt> is converted to boolean, double[] and float[][] respectively</li>
     * <li>For other types, they would be converted to Object, Object[] based on if they are Array and their dimension</li>
     * </ul>
     *
     * @param obj Object to be converted to its equivalent form
     * @return Converted Object containing same values in possible different forms
     */
    public static Object toEquivalent(Object obj) {
        if(obj==null){
            return null;
        }
        return getToEquivalentConverter(obj.getClass()).apply(obj);
    }

    //region Repository to keep deep converters of two classes

    private static Map<Tuple2<Class, Class>, Tuple3<Function<Object, Object>, Function<Object, Object>, Function<Object, Object>>> getConverterMap() {
        return new HashMap<>();
    }

    private static Tuple3<Function<Object, Object>, Function<Object, Object>, Function<Object, Object>>
    getDeepEvaluators(Class fromClass, Class toClass) {
        assertAllNotNull(fromClass, toClass);

        boolean isFromArray = fromClass.isArray();
        boolean isToArray = toClass.isArray();

        if (!isFromArray && !isToArray) {
            if (valueEquals(fromClass, toClass)) {
                //They are identical classes, no converter needed, user the Objects.equals as comparator
                return Tuple.create(returnsSelf, returnsSelf, returnsSelf);
            } else if (valueEquals(getEquivalentClass(fromClass), toClass)) {
                //One class is primitive, another is its wrapper, so use corresponding converter
                final Function<Object, Object> singleObjectConverter = getToEquivalentConverter(fromClass);
                return Tuple.create(singleObjectConverter, singleObjectConverter, singleObjectConverter);
            } else if (toClass.isAssignableFrom(fromClass)) {
                //Value of type class1 can be assigned to variable of type class2 directly, like ArrayList can be assigned to List
                final Function<Object, Object> singleObjectConverter = returnsSelf;
                return Tuple.create(singleObjectConverter, singleObjectConverter, singleObjectConverter);
            } else if (fromClass.isAssignableFrom(toClass)) {
                //Value of type class1 cannot be assigned to variable of type class2, use cast
                final Function<Object, Object> singleObjectConverter = toClass::cast;
                return Tuple.create(singleObjectConverter, singleObjectConverter, singleObjectConverter);
            } else {
                //No chance to convert or equals
                return Tuple.create(mapsToNull, mapsToNull, mapsToNull);
            }
        } else if (!isToArray) {
            return toClass.isAssignableFrom(fromClass) ?
                    Tuple.create(returnsSelf, returnsSelf, returnsSelf)
                    : Tuple.create(mapsToNull, mapsToNull, mapsToNull);
        } else if (!isFromArray) {
            return Tuple.create(mapsToNull, mapsToNull, mapsToNull);
        }

        Class fromComponentClass = fromClass.getComponentType();
        Class toComponentClass = toClass.getComponentType();

        //Now both type are of array
        //First, check fromClass and toClass is identical, return itself if they are identical
        if (fromComponentClass.equals(toComponentClass)) {
            return Tuple.create(returnsSelf, returnsSelf, returnsSelf);
        }

        //Second, checking if it is converting between primitive array and Object array
        boolean needConversion = fromComponentClass.isPrimitive() != toComponentClass.isPrimitive();
        if (needConversion) {
            Class equivalentFromComponentType = getEquivalentClass(fromComponentClass);
            Class equivalentToComponentType = getEquivalentClass(toComponentClass);
            //Returns null factory methods only when there is no chance to convert one type to another
            if (!toComponentClass.isAssignableFrom(equivalentFromComponentType) && !fromComponentClass.isAssignableFrom(equivalentToComponentType)) {
                // Use the equivalent converter if they are
                return Tuple.create(mapsToNull, mapsToNull, mapsToNull);
            }
        } else if (!fromComponentClass.isAssignableFrom(toComponentClass) && !toComponentClass.isAssignableFrom(fromComponentClass)) {
            //Third, return null factory methods when there is no chance to convert between two primitive types or two object types
            return Tuple.create(mapsToNull, mapsToNull, mapsToNull);
        }

        FunctionThrowable<Integer, Object> factory = TypeHelper.getArrayFactory(toComponentClass);
        TriConsumerThrowable<Object, Integer, Object> toElementSetter = getArrayElementSetter(toComponentClass);

        Function<Object, Object> elementConverter = needConversion ?
                getToEquivalentSerialConverter(fromComponentClass) : returnsSelf;

        TriFunctionThrowable.TriFunction<Object, Object, Integer, Boolean> elementMappingWithException =
                getExceptionWithMapping(toElementSetter, elementConverter);

        Function<Object, Object> parallelConverter = (fromArray) -> {
            if (fromArray == null) return null;
            try {
                int length = Array.getLength(fromArray);
                Object toArray = factory.apply(length);
                Integer firstIndexWithException = IntStream.range(0, length).boxed().parallel()
                        .filter(i -> elementMappingWithException.apply(fromArray, toArray, i))
                        .findFirst().orElse(-1);

                return firstIndexWithException == -1 ? toArray : null;
            } catch (Exception ex) {
                return null;
            }
        };

        Function<Object, Object> serialConverter = (fromArray) -> {
            if (fromArray == null) return null;
            try {
                int length = Array.getLength(fromArray);
                Object toArray = factory.apply(length);
                for (int i = 0; i < length; i++) {
                    Object fromElement = Array.get(fromArray, i);
                    Object convertedElement = elementConverter.apply(fromElement);
                    toElementSetter.accept(toArray, i, convertedElement);
                }
                return toArray;
            } catch (Exception ex) {
                return null;
            }
        };

        Function<Object, Object> defaultConverter = (fromArray) -> {
            if (fromArray == null) return null;
            int length = Array.getLength(fromArray);
            try {
                Object toArray = factory.apply(length);
                if (length < PARALLEL_EVALUATION_THRESHOLD) {
                    for (int i = 0; i < length; i++) {
                        Object fromElement = Array.get(fromArray, i);
                        Object convertedElement = elementConverter.apply(fromElement);
                        toElementSetter.accept(toArray, i, convertedElement);
                    }
                    return toArray;
                } else {
                    Integer firstIndexWithException = IntStream.range(0, length).boxed().parallel()
                            .filter(i -> elementMappingWithException.apply(fromArray, toArray, i))
                            .findFirst().orElse(-1);

                    return firstIndexWithException == -1 ? toArray : null;
                }
            } catch (Exception ex) {
                return null;
            }
        };

        return Tuple.create(parallelConverter, serialConverter, defaultConverter);
    }

    /**
     * Convert Object to another type either parallelly
     *
     * @param obj     Object to be converted that can be of Array
     * @param toClass Target type to be converted
     * @param <T>     Target type to be converted
     * @return Converted instance
     */
    public static <T> T convertParallel(Object obj, Class<T> toClass) {
        if (obj == null)
            return null;

        Class fromClass = obj.getClass();
        Function<Object, Object> converter = deepConverters.getFirst(fromClass, toClass);
        return (T) converter.apply(obj);
    }
    //endregion

    /**
     * Convert Object to another type serially
     *
     * @param obj     Object to be converted that can be of Array
     * @param toClass Target type to be converted
     * @param <T>     Target type to be converted
     * @return Converted instance
     */
    public static <T> T convertSerial(Object obj, Class<T> toClass) {
        if (obj == null)
            return null;

        Class fromClass = obj.getClass();
        Function<Object, Object> converter = deepConverters.getSecond(fromClass, toClass);
        return (T) converter.apply(obj);
    }

    /**
     * Convert Object to another type either serially or parallelly
     *
     * @param obj     Object to be converted that can be of Array
     * @param toClass Target type to be converted
     * @param <T>     Target type to be converted
     * @return Converted instance
     */
    public static <T> T convert(Object obj, Class<T> toClass) {
        if (obj == null)
            return null;

        Class fromClass = obj.getClass();
        Function<Object, Object> converter = deepConverters.getThird(fromClass, toClass);
        return (T) converter.apply(obj);
    }

    /**
     * Returns a hash code based on the object itself when it is not an array, or the elements contained if it is an array.
     * The evaluation could continue recursively if its elements are of arrays.
     *
     * @param obj the object whose hash code is to be computed
     * @return a deep values based hash code of the <tt>obj</tt>
     */
    public static int deepHashCode(Object obj) {
        if (obj == null) {
            return 0;
        } else if (obj instanceof Collection) {
            if (((Collection) obj).size() == 0) {
                return 37;
            }
        } else if (obj.getClass().isArray()) {
            if (Array.getLength(obj) == 0) {
                return 37;
            }
        } else {
            return obj.hashCode();
        }

        int result = 1;
        int[][] deepIndexes = getDeepIndexes(obj);
        for (int i = 0; i < deepIndexes.length; i++) {
            int[] deepIndex = deepIndexes[i];
            int[] childDeepIndex = checkNotNull( (int[]) copyOfRange(deepIndex, 0, deepIndex.length - 1), "Failed to copy range");
            Object element = getDeepElement(obj, childDeepIndex);
            int elementHash = deepHashCode(element);

            result = 31 * result + elementHash;
        }

        return result;
    }

    /**
     * Returns a string representation of the "deep contents" of the specified
     * array.  If the array contains other arrays as elements, the string
     * representation contains their contents and so on.  This method is
     * designed for converting multidimensional arrays to strings.
     * The string representation consists of a list of the array's
     * elements, enclosed in square brackets (<tt>"[]"</tt>).  Adjacent
     * elements are separated by the characters <tt>", "</tt> (a comma
     * followed by a space).  Elements are converted to strings as by
     * <tt>String.valueOf(Object)</tt>, unless they are themselves
     * arrays.
     * This method returns <tt>"null"</tt> if the specified array
     * is <tt>null</tt>.
     *
     * @param obj the object whose string representation to be returned
     * @return a string represeting of <tt>obj</tt>
     */
    public static String deepToString(Object obj) {
        if (obj == null) {
            return "null";
        } else if (obj instanceof Collection) {
            Collection collection = (Collection) obj;
            List<String> elementStrings = IntStream.range(0, collection.size())
                    .mapToObj(i -> deepToString(getElement(obj, i)))
                    .collect(Collectors.toList());
            return "[" + String.join(", ", elementStrings) + "]";
        } else if (obj.getClass().isArray()) {
            Function<Object, String> arrayToString = checkNotNull(getArrayToString(obj.getClass().getComponentType()), "failed to get arrayToString");
            return arrayToString.apply(obj);
        } else {
            return obj.toString();
        }
    }

    /**
     * Strategy to compare two variables when both are empty arrays or collections.
     */
    public enum EqualityStategy {
        //Treat empty object as null
        EmptyAsNull,

        //Two empty objects are regarded as equal even if they are declared as different types,
        // for example: Integer[0] and Double[0], or Integer[0] and new ArrayList(), but null != Integer[0]
        TypeIgnored,

        //Two null variables are regarded as equal if one can be assigned from another,
        // for example: Comparable[0] and Integer[0], or int[0] and Integer[0]
        BetweenAssignableTypes,

        //Two null variables are regarded as equal only when they are of the same type
        SameTypeOnly
    }
}
