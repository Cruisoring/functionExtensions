package com.easyworks;

import com.easyworks.function.*;
import com.easyworks.repository.TupleRepository3;
import com.easyworks.repository.TupleRepository6;
import com.easyworks.tuple.Tuple;
import com.easyworks.tuple.Tuple2;
import com.easyworks.tuple.Tuple3;
import com.easyworks.tuple.Tuple6;
import com.easyworks.utility.ArrayHelper;
import com.easyworks.utility.StringHelper;
import sun.reflect.ConstantPool;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class TypeHelper {
    public final static Class OBJECT_CLASS = Object.class;

    /**
     * Strategy to compare two variables when both of them are nulls.
     */
    public enum NullEquality{
        TypeIgnored,            //Two null variables are regarded as equal even if they are declared as different types,
                                // for example: Integer of null and Double of null
        BetweenAssignableTypes, //Two null variables are regarded as equal if one can be assigned from another
                                // (like Object and Integer variables, both are null)
        SameTypeOnly            //Two null variables are regarded as equal only when they are of the same type
    }

    /**
     * Strategy to compare two variables when both of them are empty array.
     */
    public enum EmptyArrayEquality {
        TypeIgnored,            //Two empty arrays are regarded as equal even if they are declared as different types,
                                // for example: Integer[0] and Double[0]
        BetweenAssignableTypes, //Two null variables are regarded as equal if one can be assigned from another,
                                // for example: Comparable[0] and Integer[0], or int[0] and Integer[0]
        SameTypeOnly            //Two null variables are regarded as equal only when they are of the same type
    }

    public final static int NORMAL_VALUE_NODE = 0;
    public final static int NULL_NODE = -1;
    public final static int EMPTY_ARRAY_NODE = -2;

    private final static int _defaultParallelEvaluationThread = 100000;

    public final static boolean EMPTY_ARRAY_AS_DEFAULT;
    public final static int PARALLEL_EVALUATION_THRESHOLD;
    public final static EmptyArrayEquality EMPTY_ARRAY_EQUALITY;
    public final static NullEquality NULL_EQUALITY;

    private static <T> T tryParse(T defaultValue){
        Class<T> valueClass = (Class<T>) defaultValue.getClass();
        String valueKey = valueClass.getSimpleName();
        String valueString = System.getProperty(valueKey);
        if(valueString == null)
            return defaultValue;
        return StringHelper.parse(valueString, valueClass, defaultValue);
    }

    public static <T> T tryParse(String valueKey, T defaultValue){
        String valueString = System.getProperty(valueKey);
        if(valueString == null)
            return defaultValue;
        Class<T> valueClass = (Class<T>) defaultValue.getClass();
        return StringHelper.parse(valueString, valueClass, defaultValue);
    }

    static{
        EMPTY_ARRAY_AS_DEFAULT = tryParse("EMPTY_ARRAY_AS_DEFAULT", false);
        PARALLEL_EVALUATION_THRESHOLD = tryParse("PARALLEL_EVALUATION_THRESHOLD", _defaultParallelEvaluationThread);
        EMPTY_ARRAY_EQUALITY = tryParse(EmptyArrayEquality.TypeIgnored);
        NULL_EQUALITY = tryParse(NullEquality.TypeIgnored);
    }

    //region Common functions saved as static variables
    private static final BiFunctionThrowable<Object, Integer, Object> arrayGet = Array::get;
    private static final TriConsumerThrowable<Object, Integer, Object> arraySet = Array::set;
    private static final Function<Object, Object> returnsSelf = obj -> obj;
    private static final Function<Object, Object> mapsToNull = obj -> null;
    private static final BiPredicate<Object, Object> alwaysFalse = (a, b) -> false;
    private static final BiPredicate<Object, Object> objectsEquals = (a, b) -> Objects.equals(a, b);
    private static final BiPredicate<Object, Object> arraysDeepEquals = (a, b) ->
            Arrays.deepEquals((Object[]) a, (Object[])b);
    //endregion

    //region Higher-order functions to create lambda Functions
    private static <T> TriFunctionThrowable<Object, Integer, Integer, Object> asGenericCopyOfRange(Class<T> componentType){
        return (array, from, to) -> Arrays.copyOfRange((T[])array, from, to);
    }

    private static <T> Function<Object, String> getDeepToString(Class<T> componentClass){
        Objects.requireNonNull(componentClass);
        Function<Object, String> toString = (obj) -> {
            T[] objects = (T[])obj;
            int length = objects.length;
            String[] strings = new String[length];
            for (int i = 0; i < length; i++) {
                T element = objects[i];
                if(element == null) {
                    strings[i] = "null";
                } else {
                    Class elementClass = element.getClass();
                    if(elementClass.isArray()){
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
            , Function<Object, Object> elementConverter){
        if(elementConverter == returnsSelf || elementConverter == null)
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
    //endregion

    //region deepLength based objects comparing utilities
    /**
     * Merge any numbers of int values with an int array and return the merged copy, used by getDeepLength0()
     * @param fromRoot      An existing int array
     * @param selfIndexes   new int values to be included
     * @return              A new int array containing all int values in order
     */
    private static int[] mergeOfInts(int[] fromRoot, int... selfIndexes){
        int fromLength = fromRoot.length;
        int selfLength = selfIndexes.length;
        int[] fullPath = Arrays.copyOfRange(fromRoot, 0, fromLength + selfLength);

        for (int i = 0, j=fromLength; i < selfLength; i++) {
            fullPath[j+i] = selfIndexes[i];
        }
        return fullPath;
    }

    /**
     * Return an array of int[] to get the node type and indexes to access EVERY node elements of the concerned object.
     * @param object    Object under concerned
     * @return      An array of int[]. Each node element is mapped to one int[] where the last int shows the type of the
     *              value of the node:
     *                  NULL_NODE when it is null, EMPTY_ARRAY_NODE when it is an array of 0 length and otherwise NORMAL_VALUE_NODE.
     *              The other int values of the int[] are indexes of the node element or its parent array in their container arrays.
     */
    public static int[][] getDeepLength(Object object){
        return getDeepLength0(object, new int[0]);
    }

    private static int[][] getDeepLength0(Object object, int[] indexes){
        if(object == null) {
            return new int[][]{mergeOfInts(indexes, NULL_NODE)};
        }else if(!object.getClass().isArray()) {
            return new int[][]{mergeOfInts(indexes, NORMAL_VALUE_NODE)};
        }
        Class objectClass = object.getClass();
        int length = Array.getLength(object);
        if(length == 0)
            return new int[][]{mergeOfInts(indexes, EMPTY_ARRAY_NODE)};

        List<int[]> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Object element = Array.get(object, i);
            int[][] positions = getDeepLength0(element, mergeOfInts(indexes, i));
            list.addAll(Arrays.asList(positions));
        }
        return list.toArray(new int[0][]);
    }

    /**
     * Get the node as an Object with all indexes of its parent arrays
     * @param obj       root Object to get the specific node
     * @param indexes   array indexes if the specific node is kept as element of an array, or empty if the root Object
     *                  is the specific node
     * @return          the root Object itself if the indexes is empty; or the element in an array
     *      Notice: the returned value is converted to Object automatically. For an element of an int[], the returned
     *      value would be the corresponding Integer
     */
    static Object getNode(Object obj, int[] indexes){
        int last = indexes.length-1;
        if(indexes[last] == NULL_NODE)
            last--;

        Object result = obj;
        for (int i = 0; i < last; i++) {
            result = Array.get(result, indexes[i]);
        }
        return result;
    }

    /**
     * Compare the two nodes of two Objects that share the same indexes to retrieve
     * @param obj1      First Object to be compared
     * @param obj2      Second Object to be compared
     * @param indexes   array indexes if the specific node is kept as element of an array, or empty if the root Object
     *                  is the specific node
     * @param nullEquality  Strategy to compare nodes when both are nulls: TypeIgnored, BetweenAssignableTypes, SameTypeOnly
     * @param emptyArrayEquality  Strategy to compare nodes when both are empty arrays: TypeIgnored, BetweenAssignableTypes, SameTypeOnly
     * @return          <code>true</code> if the two nodes are identical, otherwise <code>false</code>
     */
    static boolean nodeEquals(Object obj1, Object obj2, int[] indexes,
                                      NullEquality nullEquality, EmptyArrayEquality emptyArrayEquality){
        int depth = indexes.length;
        int last = indexes[depth-1];

        if((last == NULL_NODE && (nullEquality == NullEquality.TypeIgnored || depth==1))
                || (last == EMPTY_ARRAY_NODE && emptyArrayEquality == EmptyArrayEquality.TypeIgnored))
            return true;

        //node1 and node2 shall not be null
        //getNode would always cast primitive type values to their wrapper objects
        Object node1 = getNode(obj1, indexes);
        Object node2 = getNode(obj2, indexes);
        Class class1 = node1.getClass();
        Class class2 = node2.getClass();
        if(last == NULL_NODE){
            return (nullEquality == NullEquality.SameTypeOnly)
                    ? class1.equals(class2)
                    : areEquivalent(class1, class2)
                        || class1.isAssignableFrom(class2)
                        || class2.isAssignableFrom(class1);
        } else if(last == EMPTY_ARRAY_NODE) {
             return emptyArrayEquality == EmptyArrayEquality.SameTypeOnly ?
                     class1.equals(class2) :
                    areEquivalent(class1, class2)
                        || class1.isAssignableFrom(class2)
                        || class2.isAssignableFrom(class1);
        } else {
            return node1.equals(node2);
        }
    }


    /**
     * Helper method to validate if the 2 Objects are equal by comparing their deepLength first, then using their deepLength
     * to compare their node values either serially or parallelly based on how many nodes they have
     * @param obj1      First Object to be compared
     * @param obj2      Second Object to be compared
     * @param deepLength    identical deepLength of the above two objects
     * @param nullEquality  Strategy to compare nodes when both are nulls: TypeIgnored, BetweenAssignableTypes, SameTypeOnly
     * @param emptyArrayEquality  Strategy to compare nodes when both are empty arrays: TypeIgnored, BetweenAssignableTypes, SameTypeOnly
     * @return          <code>true</code> if they have same set of values, otherwise <code>false</code>
     */
    static boolean deepLengthEquals(Object obj1, Object obj2, int[][] deepLength,
                                    NullEquality nullEquality, EmptyArrayEquality emptyArrayEquality){
        int length = deepLength.length;

        if(length < PARALLEL_EVALUATION_THRESHOLD){
            for (int i = 0; i < length; i++) {
                int[] indexes = deepLength[i];
                if(!nodeEquals(obj1, obj2, indexes, nullEquality, emptyArrayEquality))
                    return false;
            }
            return true;
        } else {
            boolean allEquals = IntStream.range(0, length).boxed().parallel()
                    .allMatch(i -> nodeEquals(obj1, obj2, deepLength[i], nullEquality, emptyArrayEquality));
            return allEquals;
        }
    }

    /**
     * Helper method to validate if the 2 Objects are equal by comparing their deepLength first, then using their deepLength
     * to compare their node values parallelly
     * @param obj1      First Object to be compared
     * @param obj2      Second Object to be compared
     * @param deepLength    identical deepLength of the above two objects
     * @param nullEquality  Strategy to compare nodes when both are nulls: TypeIgnored, BetweenAssignableTypes, SameTypeOnly
     * @param emptyArrayEquality  Strategy to compare nodes when both are empty arrays: TypeIgnored, BetweenAssignableTypes, SameTypeOnly
     * @return          <code>true</code> if they have same set of values, otherwise <code>false</code>
     */
    static boolean deepLengthEqualsParallel(Object obj1, Object obj2, int[][] deepLength,
                                                    NullEquality nullEquality, EmptyArrayEquality emptyArrayEquality){
        int length = deepLength.length;
        boolean allEquals =IntStream.range(0, length).boxed().parallel()
                .allMatch(i -> nodeEquals(obj1, obj2, deepLength[i], nullEquality, emptyArrayEquality));
        return allEquals;
    }

    /**
     * Helper method to validate if the 2 Objects are equal by comparing their deepLength first, then using their deepLength
     * to compare their node values either serially
     * @param obj1      First Object to be compared
     * @param obj2      Second Object to be compared
     * @param deepLength    identical deepLength of the above two objects
     * @param nullEquality  Strategy to compare nodes when both are nulls: TypeIgnored, BetweenAssignableTypes, SameTypeOnly
     * @param emptyArrayEquality  Strategy to compare nodes when both are empty arrays: TypeIgnored, BetweenAssignableTypes, SameTypeOnly
     * @return          <code>true</code> if they have same set of values, otherwise <code>false</code>
     */
    private static boolean deepLengthEqualsSerial(Object obj1, Object obj2, int[][] deepLength,
                                                  NullEquality nullEquality, EmptyArrayEquality emptyArrayEquality){
        int length = deepLength.length;
        for (int i = 0; i < length; i++) {
            int[] indexes = deepLength[i];
            if(!nodeEquals(obj1, obj2, indexes, nullEquality, emptyArrayEquality))
                return false;
        }
        return true;
    }

    /**
     * This method compare two Objects that are not arrays or are both empty arrays
     * @param obj1  first Object to be compared
     * @param obj2  second Object to be compared
     * @return  <code>true</code> if they are equal,
     *          <code>false</code> if they are not equal or cannot be assignedFrom,
     *          otherwise <code>null</code>
     */
    private static Boolean simpleValueEquals(Object obj1, Object obj2) {
        if(obj1 == obj2 || (obj1 != null && obj1.equals(obj2)))
            return true;
        else if(obj1 == null || obj2 == null)
            return false;

        Class class1 = obj1.getClass();
        Class class2 = obj2.getClass();
        boolean isArray1 = class1.isArray();
        boolean isArray2 = class2.isArray();

        if( isArray1 != isArray2)
            return false;
        else if(!isArray1)
            return  obj1.equals(obj2);

        Class componentType1 = class1.getComponentType();
        componentType1 = componentType1.isPrimitive() ? TypeHelper.getEquivalentClass(componentType1) : componentType1;
        Class componentType2 = class2.getComponentType();
        componentType2 = componentType2.isPrimitive() ? TypeHelper.getEquivalentClass(componentType2) : componentType2;
        if(areEquivalent(componentType1, componentType2)|| componentType1.isAssignableFrom(componentType2) || componentType2.isAssignableFrom(componentType1))
            return null;

        return false;
    }

    /**
     * Comparing two objects by comparing their actual values.
     * First by treating them as Non-Array Objects or Empty arrays, then compare them by deepDepth either serial or parallel
     *  Note: primitive types are converted to their corresponding wrappers automatically
     * @param obj1  first object to be compared
     * @param obj2  second object to be compared
     * @return  <code>true</code> if both objects have same values, otherwise <code>false</code>
     */
    public static boolean valueEquals(Object obj1, Object obj2){
        final Boolean simpleEquals = simpleValueEquals(obj1, obj2);
        if (simpleEquals != null)
            return simpleEquals;

        int[][] deepLength1 = getDeepLength(obj1);
        int[][] deepLength2 = getDeepLength(obj2);
        if(!Arrays.deepEquals(deepLength1, deepLength2))
            return false;

        return deepLengthEquals(obj1, obj2, deepLength1, NULL_EQUALITY, EMPTY_ARRAY_EQUALITY);
    }

    /**
     * Comparing two objects with deepLengthEquals directly when their deepDepth provided
     * @param obj1  first object to be compared
     * @param obj2  second object to be compared
     * @param deepLength1    deepLength of the first object
     * @param deepLength2    deepLength of the second object
     * @return <code>true</code> if both objects have same values, otherwise <code>false</code>
     */
    public static boolean valueEquals(Object obj1, Object obj2, int[][] deepLength1, int[][] deepLength2){
        Objects.requireNonNull(deepLength1);
        Objects.requireNonNull(deepLength2);

        if(!Arrays.deepEquals(deepLength1, deepLength2))
            return false;

        return deepLengthEquals(obj1, obj2, deepLength1, NULL_EQUALITY, EMPTY_ARRAY_EQUALITY);
    }

    /**
     * Comparing two objects by comparing their actual values.
     * First by treating them as Non-Array Objects or Empty arrays, then compare them by deepDepth parallelly
     *  Note: primitive types are converted to their corresponding wrappers automatically
     * @param obj1  first object to be compared
     * @param obj2  second object to be compared
     * @return  <code>true</code> if both objects have same values, otherwise <code>false</code>
     */
    public static boolean valueEqualsParallel(Object obj1, Object obj2){
        final Boolean singleObjectConverter = simpleValueEquals(obj1, obj2);
        if (singleObjectConverter != null)
            return singleObjectConverter;

        int[][] deepLength1 = getDeepLength(obj1);
        int[][] deepLength2 = getDeepLength(obj2);
        if(!Arrays.deepEquals(deepLength1, deepLength2))
            return false;
        return deepLengthEqualsParallel(obj1, obj2, deepLength1, NULL_EQUALITY, EMPTY_ARRAY_EQUALITY);
    }

    /**
     * Comparing two objects with deepLengthEquals parallelly when their deepDepth provided
     * @param obj1  first object to be compared
     * @param obj2  second object to be compared
     * @param deepLength1    deepLength of the first object
     * @param deepLength2    deepLength of the second object
     * @return <code>true</code> if both objects have same values, otherwise <code>false</code>
     */
    public static boolean valueEqualsParallel(Object obj1, Object obj2, int[][] deepLength1, int[][] deepLength2){
        Objects.requireNonNull(deepLength1);
        Objects.requireNonNull(deepLength2);

        if(!Arrays.deepEquals(deepLength1, deepLength2))
            return false;

        return deepLengthEqualsParallel(obj1, obj2, deepLength1, NULL_EQUALITY, EMPTY_ARRAY_EQUALITY);
    }

    /**
     * Comparing two objects by comparing their actual values.
     * First by treating them as Non-Array Objects or Empty arrays, then compare them by deepDepth either serially
     *  Note: primitive types are converted to their corresponding wrappers automatically
     * @param obj1  first object to be compared
     * @param obj2  second object to be compared
     * @return  <code>true</code> if both objects have same values, otherwise <code>false</code>
     */
    public static boolean valueEqualsSerial(Object obj1, Object obj2){
        final Boolean singleObjectConverter = simpleValueEquals(obj1, obj2);
        if (singleObjectConverter != null)
            return singleObjectConverter;

        int[][] deepLength1 = getDeepLength(obj1);
        int[][] deepLength2 = getDeepLength(obj2);
        if(!Arrays.deepEquals(deepLength1, deepLength2))
            return false;
        return deepLengthEqualsSerial(obj1, obj2, deepLength1, NULL_EQUALITY, EMPTY_ARRAY_EQUALITY);
    }

    /**
     * Comparing two objects with deepLengthEquals parallelly when their deepDepth provided
     * @param obj1  first object to be compared
     * @param obj2  second object to be compared
     * @param deepLength1    deepLength of the first object
     * @param deepLength2    deepLength of the second object
     * @return <code>true</code> if both objects have same values, otherwise <code>false</code>
     */
    public static boolean valueEqualsSerially(Object obj1, Object obj2, int[][] deepLength1, int[][] deepLength2){
        Objects.requireNonNull(deepLength1);
        Objects.requireNonNull(deepLength2);

        if(!Arrays.deepEquals(deepLength1, deepLength2))
            return false;

        return deepLengthEqualsSerial(obj1, obj2, deepLength1, NULL_EQUALITY, EMPTY_ARRAY_EQUALITY);
    }
    //endregion


    //region Method and repository to get return type of Lambda Expression
    private static final Method _getConstantPool = (Method) Functions.ReturnsDefaultValue.apply(() -> {
        Method method = Class.class.getDeclaredMethod("getConstantPool");
        method.setAccessible(true);
        return method;
    });

    protected static ConstantPool getConstantPoolOfClass(Class objectClass){
        return (ConstantPool) Functions.ReturnsDefaultValue.apply(()->_getConstantPool.invoke(objectClass));
    }

    /**
     * Repository to evaluate a Lambda expression to get its Parameter Types, and return Type.
     * Notice: the parameter types are not always accurate when some extra values are used to compose the lambda
     *
     * <tt>FunctionThrowable&lt;TKey, Tuple3&lt;T,U,V&gt;&gt; valueFunction</tt>
     */
    public static final TupleRepository3<WithValueReturned,
                Boolean, Class[], Class> lambdaGenericInfoRepository = TupleRepository3.fromKey(
            TypeHelper::getLambdaGenericInfo
    );

    private static Tuple3<Boolean, Class[], Class> getLambdaGenericInfo(WithValueReturned lambda){
        Objects.requireNonNull(lambda);

        Class lambdaClass = lambda.getClass();
        ConstantPool constantPool = TypeHelper.getConstantPoolOfClass(lambdaClass);
        Method functionInterfaceMethod = null;
        int index = constantPool.getSize();
        while(--index >=0) {
            try {
                functionInterfaceMethod = (Method)  constantPool.getMethodAt(index);
                break;
            } catch (Exception ex){
                continue;
            }
        }
        Class[] parameterClasses = functionInterfaceMethod.getParameterTypes();
        int parameterCount = functionInterfaceMethod.getParameterCount();
        Class returnClass = functionInterfaceMethod.getReturnType();

        return Tuple.create(parameterClasses.length == parameterCount, parameterClasses, returnClass);
    }

    /**
     * Helper method to get the return type of a RunnableThrowable (as void.class) or SupplierThrowable.
     * Notice: only applicable on first-hand lambda expressions. Lambda Expressions created by Lambda would erase the return type in Java 1.8.161.
     * @param aThrowable solid Lambda expression
     * @return  The type of the return value defined by the Lambda Expression.
     */
    public static Class getReturnType(WithValueReturned aThrowable){
        return lambdaGenericInfoRepository.getThirdValue(aThrowable);
    }
    //endregion

    //region Repository with Class as the key, to keep 7 common used attributes or operators
    /**
     * Repository to keep operators related with specific class that is kept as the keys of the map.
     * Relative operators are saved as a strong-typed Tuple7 with following elements:
     *      Equivalent predicate: to evaluate if another class is regarded as equivalent to the specific class.
     *              Notice: int.class and Integer.class are regarded as equivalent in this library, their array types
     *                  int[].class and Integer[].class are also regarded as equivalent.
     *      ArrayFactory: given the length of the new array, the factory would create a new array with elements of the specific class.
     *      ArrayClass:     class of the array containing elements of the specific class
     *      ArrayElementSetter: setter of the array with target index and element to be set
     *      CopyOfRange:    method to copy part of the original array to a new array of the same type, while its length
     *              is determined by the from and to indexes
     *      ConvertToString:    convert the array to a string by recursively applying the deep toString() on every elements
     *              of the array
     */
    public static final TupleRepository6<
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
                    >>(){{
                Predicate<Class> classPredicate = clazz -> int.class.equals(clazz) || Integer.class.equals(clazz);
                put(int.class, Tuple.create(
                        classPredicate
                        , i -> new int[i]
                        , int[].class
                        , (array, index, value) -> ((int[])array)[index] = ((Integer)value).intValue()
                        , (array, from, to) -> Arrays.copyOfRange((int[])array, from, to)
                        , array -> Arrays.toString((int [])array)));
                put(Integer.class, Tuple.create(
                        classPredicate
                        , i -> new Integer[i]
                        , Integer[].class
                        , arraySet
                        , (array, from, to) -> Arrays.copyOfRange((Integer[])array, from, to)
                        , array -> Arrays.toString((Integer[])array)));
                classPredicate = clazz -> byte.class.equals(clazz) || Byte.class.equals(clazz);
                put(byte.class, Tuple.create(
                        classPredicate
                        , i -> new byte[i]
                        , byte[].class
                        , (array, index, value) -> ((byte[])array)[index] = ((Byte)value).byteValue()
                        , (array, from, to) -> Arrays.copyOfRange((byte[])array, from, to)
                        , array -> Arrays.toString((byte[])array)));
                put(Byte.class, Tuple.create(
                        classPredicate
                        , i -> new Byte[i]
                        , Byte[].class
                        , arraySet
                        , (array, from, to) -> Arrays.copyOfRange((Byte[])array, from, to)
                        , array -> Arrays.toString((Byte[])array)));
                classPredicate = clazz -> boolean.class.equals(clazz) || Boolean.class.equals(clazz);
                put(boolean.class, Tuple.create(
                        classPredicate
                        , i -> new boolean[i]
                        , boolean[].class
                        , (array, index, value) -> ((boolean[])array)[index] = ((Boolean)value).booleanValue()
                        , (array, from, to) -> Arrays.copyOfRange((boolean[])array, from, to)
                        , array -> Arrays.toString((boolean[])array)));
                put(Boolean.class, Tuple.create(
                        classPredicate
                        , i -> new Boolean[i]
                        , Boolean[].class
                        , arraySet
                        , (array, from, to) -> Arrays.copyOfRange((Boolean[])array, from, to)
                        , array -> Arrays.toString((Boolean[])array)));
                classPredicate = clazz -> char.class.equals(clazz) || Character.class.equals(clazz);
                put(char.class, Tuple.create(
                        classPredicate
                        , i -> new char[i]
                        , char[].class
                        , (array, index, value) -> ((char[])array)[index] = ((Character)value).charValue()
                        , (array, from, to) -> Arrays.copyOfRange((char[])array, from, to)
                        , array -> Arrays.toString((char[])array)));
                put(Character.class, Tuple.create(
                        classPredicate
                        , i -> new Character[i]
                        , Character[].class
                        , arraySet
                        , (array, from, to) -> Arrays.copyOfRange((Character[])array, from, to)
                        , array -> Arrays.toString((Character[])array)));
                classPredicate = clazz -> short.class.equals(clazz) || Short.class.equals(clazz);
                put(short.class, Tuple.create(
                        classPredicate
                        , i -> new short[i]
                        , short[].class
                        , (array, index, value) -> ((short[])array)[index] = ((Short)value).shortValue()
                        , (array, from, to) -> Arrays.copyOfRange((short[])array, from, to)
                        , array -> Arrays.toString((short[])array)));
                put(Short.class, Tuple.create(
                        classPredicate
                        , i -> new Short[i]
                        , Short[].class
                        , arraySet
                        , (array, from, to) -> Arrays.copyOfRange((Short[])array, from, to)
                        , array -> Arrays.toString((Short[])array)));
                classPredicate = clazz -> long.class.equals(clazz) || Long.class.equals(clazz);
                put(long.class, Tuple.create(
                        classPredicate
                        , i -> new long[i]
                        , long[].class
                        , (array, index, value) -> ((long[])array)[index] = ((Long)value).longValue()
                        , (array, from, to) -> Arrays.copyOfRange((long[])array, from, to)
                        , array -> Arrays.toString((long[])array)));
                put(Long.class, Tuple.create(
                        classPredicate
                        , i -> new Long[i]
                        , Long[].class
                        , arraySet
                        , (array, from, to) -> Arrays.copyOfRange((Long[])array, from, to)
                        , array -> Arrays.toString((Long[])array)));
                classPredicate = clazz -> double.class.equals(clazz) || Double.class.equals(clazz);
                put(double.class, Tuple.create(
                        classPredicate
                        , i -> new double[i]
                        , double[].class
                        , (array, index, value) -> ((double[])array)[index] = ((Double)value).doubleValue()
                        , (array, from, to) -> Arrays.copyOfRange((double[])array, from, to)
                        , array -> Arrays.toString((double[])array)));
                put(Double.class, Tuple.create(
                        classPredicate
                        , i -> new Double[i]
                        , Double[].class
                        , arraySet
                        , (array, from, to) -> Arrays.copyOfRange((Double[])array, from, to)
                        , array -> Arrays.toString((Double[])array)));
                classPredicate = clazz -> float.class.equals(clazz) || Float.class.equals(clazz);
                put(float.class, Tuple.create(
                        classPredicate
                        , i -> new float[i]
                        , float[].class
                        , (array, index, value) -> ((float[])array)[index] = ((Float)value).floatValue()
                        , (array, from, to) -> Arrays.copyOfRange((float[])array, from, to)
                        , array -> Arrays.toString((float[])array)));
                put(Float.class, Tuple.create(
                        classPredicate
                        , i -> new Float[i]
                        , Float[].class
                        , arraySet
                        , (array, from, to) -> Arrays.copyOfRange((Float[])array, from, to)
                        , array -> Arrays.toString((Float[])array)));
            }},
            null,
            TypeHelper::makeClassOperators
    );

    private static Tuple6<
            Predicate<Class>,
            FunctionThrowable<Integer, Object>,
            Class,
            TriConsumerThrowable<Object, Integer, Object>,
            TriFunctionThrowable<Object, Integer, Integer, Object>,
            Function<Object, String>
            > makeClassOperators(Class clazz) throws Exception{
        FunctionThrowable<Integer, Object> arrayFactory = (Integer length) -> Array.newInstance(clazz, length);
        Predicate<Class> cPredicate;

        if(!clazz.isArray()){
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
    //endregion

    /**
     * Retrive the class equalitor of the target class to evaluate if another class is equivalent to this class
     * @param clazz     The class to be served by the equalitor
     * @return          Predicate&lt;Class&gt; to evaluate if another class is equivalent to <tt>clazz</tt>
     */
    public static Predicate<Class> getClassEqualitor(Class clazz){
        if(clazz == null) return null;

        return classOperators.getFirstValue(clazz);
    }

    /**
     * Evalueate if two classes are identical or one is wrapper of another
     * @param class1    first class to be evaluated
     * @param class2    second class to be evaluated
     * @return      <code>true</code> if they are equivalent, otherwise <code>false</code>
     */
    public static boolean areEquivalent(Class class1, Class class2){
        Objects.requireNonNull(class1);
        Objects.requireNonNull(class2);

        return classOperators.getFirstValue(class1).test(class2);
    }

    /**
     * Retrive the array factory of the target class
     * @param clazz     The class of the elements of the array
     * @return          Factory to create new Array of specific length and elements of type <tt>clazz</tt>
     */
    public static FunctionThrowable<Integer, Object> getArrayFactory(Class clazz){
        if(clazz == null) return null;

        return classOperators.getSecondValue(clazz);
    }

    /**
         * Get the class of the Array composed by elements of the concerned class
     * @param clazz     type of the elements to compose the array
     * @return      class of the array composed by elements of the concerned class
     */
    public static Class getArrayClass(Class clazz){
        if(clazz == null) return null;

        return classOperators.getThirdValue(clazz);
    }

    /**
     * get the setter operator to set element at specific position of concerned array with given value
     * @param clazz type of the elements to compose the array
     * @return      operator receiving 3 arguments (array, index, value) and set the element at index of the array with the given value
     */
    public static TriConsumerThrowable<Object, Integer, Object> getArrayElementSetter(Class clazz){
        if(clazz == null) return null;

        return classOperators.getFourthValue(clazz);
    }

    /**
     * get the copyOfRange operator to build new array of the same type, but with different ranges
     * @param clazz type of the elements to compose the array
     * @return      copyOfRange operator receiving 3 arguments(originalArray, fromIndex, toIndex) and return the new array
     *              of the same type as originalArray, but with part or all elements as specified by the fromIndex and toIndex
     */
    public static TriFunctionThrowable<Object, Integer, Integer, Object> getArrayRangeCopier(Class clazz){
        if(clazz == null) return null;

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
     * @param from the initial index of the range to be copied, inclusive
     * @param to the final index of the range to be copied, exclusive.
     *     (This index may lie outside the array.)
     * @return a new array containing the specified range from the original array,
     *     truncated or padded with zeros to obtain the required length; null if anything wrong
     */
    public static Object copyOfRange(Object array, int from, int to){
        try{
            if(array == null) return null;
            TriFunctionThrowable<Object, Integer, Integer, Object> copier = getArrayRangeCopier(array.getClass().getComponentType());
            Object newArray = copier.apply(array, from, to);
            return newArray;
        }catch (Exception ex){
            return null;
        }
    }

    /**
     * get the deep toString operator to compose a String reflect all values of the elements, calling the corresponding
     * deep toString operator of the elements if they are arrays
     * @param clazz type of the elements composing the concerned array
     * @return  deep toString operator to capture all elements of the array and any arrays represented by the elements
     */
    public static Function<Object, String> getArrayToString(Class clazz){
        if(clazz == null) return null;

        return classOperators.getSixthValue(clazz);
    }


    //region Repository to keep equivalent class and operators of given classes
    /**
     * This repository use a single class as the key, and keeps tuple of:
     * #    its default value
     * #    if its instance is composed by primitive elements
     * #    equivalent class: could be the wrapper if original class is primitive (for int[]: it is Integer[])
     *          , or primitive type if original class is wrapper (for Character[][], it is char[][])
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
            new HashMap(){{
                //For primitive values, return itself as object would convert it to the wrapper type automatically
                Function<Object,Object> convertWithCasting = returnsSelf;
                put(boolean.class, Tuple.create(
                        true
                        , false
                        , Boolean.class
                        , convertWithCasting
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> ((Boolean)fromElement).booleanValue();
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
                        , (byte)0
                        , Byte.class
                        , convertWithCasting
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> Byte.class.cast(fromElement).byteValue();
                put(Byte.class, Tuple.create(
                        false
                        , (byte)0
                        , byte.class
                        , convertWithCasting
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = returnsSelf;
                put(char.class, Tuple.create(
                        true
                        , (char)0
                        , Character.class
                        , convertWithCasting
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> Character.class.cast(fromElement).charValue();
                put(Character.class, Tuple.create(
                        false
                        , (char)0
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
                convertWithCasting = fromElement -> Double.class.cast(fromElement).doubleValue();
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
                convertWithCasting = fromElement -> Float.class.cast(fromElement).floatValue();
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
                convertWithCasting = fromElement -> Integer.class.cast(fromElement).intValue();
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
                convertWithCasting = fromElement -> Long.class.cast(fromElement).longValue();
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
                        , (short)0
                        , Short.class
                        , convertWithCasting
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> Short.class.cast(fromElement).shortValue();
                put(Short.class, Tuple.create(
                        false
                        , (short)0
                        , short.class
                        , convertWithCasting
                        , convertWithCasting
                        , convertWithCasting));
            }},
            null,
            TypeHelper::makeBaseTypeConverters
    );

    /**
     * This higher-order method parse a class to get
     * 1) its default value
     * 2) if it's finally composed of primitive values
     * 3) its equivalent class. For example: for int[][], its equivalent class is Integer[][] and vice versa
     * 4) 3 converters to convert varibles of this class to its equivalent type: parallelly, serially or choosing either way based on the array length
     * @param clazz
     * @return Tuple of 6 elements containing
     * #    its default value
     * #    if its instance is composed by primitive elements
     * #    equivalent class: could be the wrapper if original class is primitive (for int[]: it is Integer[])
     *          , or primitive type if original class is wrapper (for Character[][], it is char[][])
     * #    parallel converter: convert it instance to its equivalent type parallelly
     * #    serial converter: convert it instance to its equivalent type in serial
     * #    default converter: convert it instance to its equivalent type either parallelly or serially depending on the length of the array
     */
    private static Tuple6<Boolean, Object, Class, Function<Object,Object>, Function<Object,Object>, Function<Object,Object>> makeBaseTypeConverters(Class clazz){
        Boolean isArray = clazz.isArray();
        if(!isArray && !clazz.isPrimitive())
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
                    if(fromArray == null) return null;

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
            if(fromArray == null) return null;

            int length = Array.getLength(fromArray);
            Object toArray = ArrayHelper.getNewArray(equivalentComponentClass, length);
            try {
                for (int i = 0; i < length; i++) {
                    //Three-steps to do: get original element first, convert to equivalent value, and then set to
                    // the corresponding element of the converted array
                    //Get original element
                    Object fromElement = Array.get(fromArray, i);
                    //Get the converted element
                    //Set the converted value to the target array
                    equivalentSetter.accept(toArray, i, componentConverter.apply(fromElement));
                }
                return toArray;
            }catch (Exception ex){
                return null;
            }
        };

        Function<Object, Object> defaultConverterr = equivalentClass == null ? returnsSelf
                : fromArray -> {
            if(fromArray == null) return null;

            int length = Array.getLength(fromArray);
            Object toArray = ArrayHelper.getNewArray(equivalentComponentClass, length);
            if(length < PARALLEL_EVALUATION_THRESHOLD){
                try {
                    for (int i = 0; i < length; i++) {
                        //Three-steps to do: get original element first, convert to equivalent value, and then set to
                        // the corresponding element of the converted array
                        //Get original element
                        Object fromElement = Array.get(fromArray, i);
                        //Get the converted element
                        //Set the converted value to the target array
                        equivalentSetter.accept(toArray, i, componentConverter.apply(fromElement));
                    }
                    return toArray;
                }catch (Exception ex){
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
     * @param clazz Class to be evaluated
     * @return  true if and only if this class represents a primitive type or an array of primitive type elements
     */
    public static Boolean isPrimitive(Class clazz){
        Objects.requireNonNull(clazz);
        Boolean result =  baseTypeConverters.getFirstValue(clazz);
        return result == null ? false : result;
    }

    /**
     * Returns the default value of the concerned class, null by default
     * @param clazz Class to be evaluated
     * @return  Object represents the default value of the class.
     *      Notice: for primitive types, the returned value is their wrappers.
     *                  For instance int or char, would get Integer or Character respectively
     *              for arrays, depending on the value of <code>EMPTY_ARRAY_AS_DEFAULT</code> that can be override as System.Property
     *              of the same name 'EMPTY_ARRAY_AS_DEFAULT', the default value could be:
     *                  <code>null</code> when <code>EMPTY_ARRAY_AS_DEFAULT</code> is false
     *                  a zero-length array of the type of the class, when <code>EMPTY_ARRAY_AS_DEFAULT</code> is true by default
     */
    public static Object getDefaultValue(Class clazz){
        Objects.requireNonNull(clazz);
        return baseTypeConverters.getSecondValue(clazz);
    }

    /**
     * Return the equivalent class of the concerned class, null by default
     * <ul>
     *      <li>for Primitive types or array composed of promitive values at the end, it would return corresponding wrapper classes
     *      or class of arrays composed by wrapper values</li>
     *      <li>for wrapper types or array composed of wrapper values at the end, it woul return correspondingl primitive classes
     *      or class of arrays composed by primitive values</li>
     *      <li>otherwise returns null</li>
     * </ul>
     * @param clazz Class to be evaluated
     * @return  equivalent class of the concerned class
     */
    public static Class getEquivalentClass(Class clazz){
        Objects.requireNonNull(clazz);
        return baseTypeConverters.getThirdValue(clazz);
    }

    /**
     * Get the parallel converter of one class to its corresponding equivalent class
     * <ul>
     *     <li>when there is an equivalent class of the concerned class, the returned Function would convert value of the
     *     concerned class to value of its equivalent class (Notice: primitive types would be wrapped)</li>
     *     <li>otherwise, the returned Function would </li>
     * </ul>
     * @param clazz
     * @return
     */
    public static Function<Object, Object> getToEquivalentParallelConverter(Class clazz){
        Objects.requireNonNull(clazz);
        return baseTypeConverters.getFourthValue(clazz);
    }

    public static Function<Object, Object> getToEquivalentSerialConverter(Class clazz){
        Objects.requireNonNull(clazz);
        return baseTypeConverters.getFifthValue(clazz);
    }

    public static Function<Object, Object> getToEquivalentConverter(Class clazz){
        Objects.requireNonNull(clazz);
        return baseTypeConverters.getSixthValue(clazz);
    }

    /**
     * Convert an Object to its equivalent form.
     * <ul>
     *     <li>For primitive types, like int, byte[], char[][], the <tt>obj</tt> is converted to Integer, Byte[] and Character[][] respectively</li>
     *     <li>For wrapper types, like Boolean, Double[], Float[][], the <tt>obj</tt> is converted to boolean, double[] and float[][] respectively</li>
     *     <li>For other types, they would be converted to Object, Object[] based on if they are Array and their dimension</li>
     * </ul>
     * @param obj   Object to be converted to its equivalent form
     * @return      Converted Object containing same values in possible different forms
     */
    public static Object toEquivalent(Object obj){
        Objects.requireNonNull(obj);
        return getToEquivalentConverter(obj.getClass()).apply(obj);
    }

    //region Repository to keep deep converters of two classes
    /**
     * This repository use two classes as its key:
     * #    fromClass:  the type of source value to be converted by the converters
     * #    toClass:    the target type to be converted to by the converters
     * to generate and cache tuple containing:
     * #    parallelConverter:  Convert the object of type fromClass to value of type toClass in parallel
     * #    serialConverter:  Convert the object of type fromClass to value of type toClass in serial
     * #    defaultConverter:  Convert the object of type fromClass to value of type toClass either in parallel or in serial based on its length
     */
    public static final TupleRepository3.TupleKeys2<
                Class, Class   //fromClass & toClass as the keys

                , Function<Object, Object>          //Convert the first object to another in parallel
                , Function<Object, Object>          //Convert the first object to another serially
                , Function<Object, Object>          //Convert the first object by default, in parallel or serial based on the length of the array
                > deepConverters = TupleRepository3.fromKeys2(TypeHelper::getDeepEvaluators);

    private static Map<Tuple2<Class, Class>, Tuple3<Function<Object, Object>, Function<Object, Object>, Function<Object, Object>>> getConverterMap(){
        return new HashMap<Tuple2<Class, Class>, Tuple3<Function<Object, Object>, Function<Object, Object>, Function<Object, Object>>>();
    }

    private static Tuple3<Function<Object, Object>, Function<Object, Object>, Function<Object, Object>>
        getDeepEvaluators(Class fromClass, Class toClass) throws Exception {
        Objects.requireNonNull(fromClass);
        Objects.requireNonNull(toClass);

        boolean isFromArray = fromClass.isArray();
        boolean isToArray = toClass.isArray();

        if(!isFromArray && !isToArray){
            if(Objects.equals(fromClass, toClass)){
                //They are identical classes, no converter needed, user the Objects.equals as comparator
                return Tuple.create(returnsSelf, returnsSelf, returnsSelf);
            } else if (Objects.equals(getEquivalentClass(fromClass), toClass)){
                //One class is primitive, another is its wrapper, so use corresponding converter
                final Function<Object, Object> singleObjectConverter = getToEquivalentConverter(fromClass);
                return Tuple.create(singleObjectConverter, singleObjectConverter, singleObjectConverter);
            } else if (toClass.isAssignableFrom(fromClass)){
                //Value of type class1 can be assigned to variable of type class2 directly, like ArrayList can be assigned to List
                final Function<Object, Object> singleObjectConverter = returnsSelf;
                return Tuple.create(singleObjectConverter, singleObjectConverter, singleObjectConverter);
            } else if (fromClass.isAssignableFrom(toClass)){
                //Value of type class1 cannot be assigned to variable of type class2, use cast
                final Function<Object, Object> singleObjectConverter = toClass::cast;
                return Tuple.create(singleObjectConverter, singleObjectConverter, singleObjectConverter);
            } else {
                //No chance to convert or equals
                return Tuple.create(mapsToNull, mapsToNull, mapsToNull);
            }
        } else if(!isToArray){
            return toClass.isAssignableFrom(fromClass) ?
                    Tuple.create(returnsSelf, returnsSelf, returnsSelf)
                    : Tuple.create(mapsToNull, mapsToNull, mapsToNull);
        } else if(!isFromArray){
            return Tuple.create(mapsToNull, mapsToNull, mapsToNull);
        }

        Class fromComponentClass = fromClass.getComponentType();
        Class toComponentClass = toClass.getComponentType();

        //Now both type are of array
        //First, check fromClass and toClass is identical
        if(fromComponentClass.equals(toComponentClass)){
            return Tuple.create(returnsSelf, returnsSelf, returnsSelf);
        }else if(!getClassEqualitor(fromComponentClass).test(toComponentClass) && !toComponentClass.isAssignableFrom(fromComponentClass)) {
            //Second, check to see if fromClass is equivalent to toClass
            // Use the equivalent converter if they are
            return Tuple.create(mapsToNull, mapsToNull, mapsToNull);
        }

        FunctionThrowable<Integer, Object> factory = TypeHelper.getArrayFactory(toComponentClass);
        TriConsumerThrowable<Object, Integer, Object> toElementSetter = getArrayElementSetter(toComponentClass);

        Function<Object, Object> elementConverter = (getEquivalentClass(fromClass).equals(toClass)) ?
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
                if(length < PARALLEL_EVALUATION_THRESHOLD) {
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
    //endregion

    /**
     * Convert Object to another type either parallelly
     * @param obj       Object to be converted that can be of Array
     * @param toClass   Target type to be converted
     * @param <T>       Target type to be converted
     * @return          Converted instance
     */
    public static <T> T convertParallel(Object obj, Class<T> toClass){
        if(obj == null)
            return null;

        Class fromClass = obj.getClass();
        Function<Object, Object> converter = deepConverters.getFirst(fromClass, toClass);
        return (T)converter.apply(obj);
    }

    /**
     * Convert Object to another type serially
     * @param obj       Object to be converted that can be of Array
     * @param toClass   Target type to be converted
     * @param <T>       Target type to be converted
     * @return          Converted instance
     */
    public static <T> T convertSerial(Object obj, Class<T> toClass){
        if(obj == null)
            return null;

        Class fromClass = obj.getClass();
        Function<Object, Object> converter = deepConverters.getSecond(fromClass, toClass);
        return (T)converter.apply(obj);
    }

    /**
     * Convert Object to another type either serially or parallelly
     * @param obj       Object to be converted that can be of Array
     * @param toClass   Target type to be converted
     * @param <T>       Target type to be converted
     * @return          Converted instance
     */
    public static <T> T convert(Object obj, Class<T> toClass){
        if(obj == null)
            return null;

        Class fromClass = obj.getClass();
        Function<Object, Object> converter = deepConverters.getThird(fromClass, toClass);
        return (T)converter.apply(obj);
    }

    /**
     * Returns a string representation of the "deep contents" of the specified
     * array.  If the array contains other arrays as elements, the string
     * representation contains their contents and so on.  This method is
     * designed for converting multidimensional arrays to strings.
     *
     * <p>The string representation consists of a list of the array's
     * elements, enclosed in square brackets (<tt>"[]"</tt>).  Adjacent
     * elements are separated by the characters <tt>", "</tt> (a comma
     * followed by a space).  Elements are converted to strings as by
     * <tt>String.valueOf(Object)</tt>, unless they are themselves
     * arrays.
     *
     * <p>This method returns <tt>"null"</tt> if the specified array
     * is <tt>null</tt>.
     *
     * @param obj the object whose string representation to be returned
     * @return a string represeting of <tt>obj</tt>
     */
    public static String deepToString(Object obj){
        if(obj==null)
            return "null";
        Class objClass = obj.getClass();
        if(!objClass.isArray())
            return obj.toString();

        Function<Object, String> arrayToString = getArrayToString(objClass.getComponentType());
        return arrayToString.apply(obj);
    }

}
