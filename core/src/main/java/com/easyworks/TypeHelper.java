package com.easyworks;

import com.easyworks.function.*;
import com.easyworks.repository.HexaValuesRepository;
import com.easyworks.repository.TripleValuesRepository;
import com.easyworks.tuple.Tuple;
import com.easyworks.tuple.Tuple2;
import com.easyworks.tuple.Tuple3;
import com.easyworks.tuple.Tuple6;
import com.easyworks.utility.ArrayHelper;
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

    public final static int NORMAL_VALUE_NODE = 0;
    public final static int NULL_NODE = -1;
    public final static int EMPTY_ARRAY_NODE = -2;

    private final static int _defaultParallelEvaluationThread = 10000;

    public final static boolean EMPTY_ARRAY_AS_DEFAULT;
    public final static int PARALLEL_EVALUATION_THRESHOLD;
    public final static ValueEquality VALUE_EQUALITY;
    public final static EmptyArrayEquality EMPTY_ARRAY_EQUALITY;
    public final static NullEquality NULL_EQUALITY;

    private static <E extends Enum<E>> E tryParse(E defaultEnum){
        Class enumClass = defaultEnum.getClass();
        String enumKey = enumClass.getSimpleName();
        return parseEnumWithDefault(System.getProperty(enumKey), defaultEnum);
    }
    static{
        EMPTY_ARRAY_AS_DEFAULT = !("false".equalsIgnoreCase(System.getProperty("EMPTY_ARRAY_AS_DEFAULT")));
        PARALLEL_EVALUATION_THRESHOLD = _defaultParallelEvaluationThread;
        VALUE_EQUALITY = tryParse(ValueEquality.BetweenAssignableTypes);
        EMPTY_ARRAY_EQUALITY =tryParse(EmptyArrayEquality.TypeIgnored);
        NULL_EQUALITY = tryParse(NullEquality.TypeIgnored);
    }

    public enum NullEquality{ TypeIgnored, BetweenAssignableTypes, SameTypeOnly };
    public enum EmptyArrayEquality {TypeIgnored, BetweenAssignableTypes, SameTypeOnly};
    public enum ValueEquality {BetweenAssignableTypes, SameTypeOnly}

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

    /**
     * Try to convert the String to specific Enum type, returns the converted Enum value if success or the first Enum value if conversion failed
     * @param enumString    String to be converted
     * @param defaultEnum   default Enum value to be returned if parsing failed
     * @param <E>           Type of the concerned Enum
     * @return              the converted Enum value if success or the first Enum value if conversion failed
     */
    public static <E extends Enum<E>> E parseEnumWithDefault(String enumString, E defaultEnum){
        E[] enumValues = (E[]) defaultEnum.getClass().getEnumConstants();
        E matchedOrFirst = Arrays.stream(enumValues)
                .filter(e -> e.toString().equalsIgnoreCase(enumString))
                .findFirst()
                .orElse(enumValues[0]);
        return matchedOrFirst;
    }

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
    private static Object getNode(Object obj, int[] indexes){
        int depth = indexes.length;
        Object result = obj;
        for (int i = 0; i < depth-1; i++) {
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
     * @return          <code>true</code> if the two nodes are identical, otherwise <code>false</code>
     */
    private static boolean nodeEquals(Object obj1, Object obj2, int[] indexes){
        int depth = indexes.length;
        int last = indexes[depth-1];

        if((last == NULL_NODE && NULL_EQUALITY == NullEquality.TypeIgnored)
                || (last == EMPTY_ARRAY_NODE && EMPTY_ARRAY_EQUALITY == EmptyArrayEquality.TypeIgnored))
            return true;

        //node1 and node2 shall not be null
        Object node1 = getNode(obj1, indexes);
        Object node2 = getNode(obj2, indexes);
        Class class1 = node1.getClass();
        Class class2 = node2.getClass();
        if(last == NULL_NODE){
            if(NULL_EQUALITY == NullEquality.SameTypeOnly)
                return class1.equals(class2);
            return areEquivalent(class1, class2)
                    || class1.isAssignableFrom(class2)
                    || class2.isAssignableFrom(class1);
        } else if(last == EMPTY_ARRAY_NODE) {
            if(NULL_EQUALITY == NullEquality.SameTypeOnly)
                return class1.equals(class2);
            return areEquivalent(class1, class2)
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
     * @return          <code>true</code> if they have same set of values, otherwise <code>false</code>
     */
    private static boolean deepLengthEquals(Object obj1, Object obj2){
        int[][] deepLength1 = getDeepLength(obj1);
        int[][] deepLength2 = getDeepLength(obj2);
        int length = deepLength1.length;
        if(length != deepLength2.length)
            return false;
        if(length < PARALLEL_EVALUATION_THRESHOLD){
            if(!Arrays.deepEquals(deepLength1, deepLength2))
                return false;
            for (int i = 0; i < length; i++) {
                int[] indexes = deepLength1[i];
                if(!nodeEquals(obj1, obj2, indexes))
                    return false;
            }
            return true;
        } else {
            boolean allEquals = IntStream.range(0, length).boxed().parallel()
                    .allMatch(i -> Arrays.equals(deepLength1[i], deepLength2[i]));
            if(!allEquals)
                return false;
            allEquals = IntStream.range(0, length).boxed().parallel()
                    .allMatch(i -> nodeEquals(obj1, obj2, deepLength1[i]));
            return allEquals;
        }
    }

    /**
     * Helper method to validate if the 2 Objects are equal by comparing their deepLength first, then using their deepLength
     * to compare their node values parallelly
     * @param obj1      First Object to be compared
     * @param obj2      Second Object to be compared
     * @return          <code>true</code> if they have same set of values, otherwise <code>false</code>
     */
    private static boolean deepLengthEqualsParallel(Object obj1, Object obj2){
        int[][] deepLength1 = getDeepLength(obj1);
        int[][] deepLength2 = getDeepLength(obj2);
        int length = deepLength1.length;
        if(length != deepLength2.length)
            return false;
        boolean allEquals = IntStream.range(0, length).boxed().parallel()
                .allMatch(i -> Arrays.equals(deepLength1[i], deepLength2[i]));
        if(!allEquals)
            return false;
        allEquals = IntStream.range(0, length).boxed().parallel()
                .allMatch(i -> nodeEquals(obj1, obj2, deepLength1[i]));
        return allEquals;
    }

    /**
     * Helper method to validate if the 2 Objects are equal by comparing their deepLength first, then using their deepLength
     * to compare their node values either serially
     * @param obj1      First Object to be compared
     * @param obj2      Second Object to be compared
     * @return          <code>true</code> if they have same set of values, otherwise <code>false</code>
     */
    private static boolean deepLengthEqualsSerial(Object obj1, Object obj2){
        int[][] deepLength1 = getDeepLength(obj1);
        int[][] deepLength2 = getDeepLength(obj2);
        int length = deepLength1.length;
        if(length != deepLength2.length)
            return false;
        if(!Arrays.deepEquals(deepLength1, deepLength2))
            return false;
        for (int i = 0; i < length; i++) {
            int[] indexes = deepLength1[i];
            if(!nodeEquals(obj1, obj2, indexes))
                return false;
        }
        return true;
    }

    /**
     * This method compare two Objects that are not arrays or are both empty arrays
     * @param obj1  first Object to be compared
     * @param obj2  second Object to be compared
     * @return  <code>true</code> if they are equal, <code>false</code> if they are not equal, otherwise <code>null</code>
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
        if(!isArray1 && !isArray2){
            return  obj1.equals(obj2);
        }

        Class equivalentClass1 = getEquivalentClass(class1);
        if(equivalentClass1 != class2 && !class1.isAssignableFrom(class2) && !class2.isAssignableFrom(class1))
            return false;
        return null;
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
        final Boolean singleObjectConverter = simpleValueEquals(obj1, obj2);
        if (singleObjectConverter != null)
            return singleObjectConverter;

        return deepLengthEquals(obj1, obj2);
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

        return deepLengthEqualsParallel(obj1, obj2);
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

        return deepLengthEqualsSerial(obj1, obj2);
    }


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
    public static final TripleValuesRepository<AbstractThrowable,
            Boolean, Class[], Class> lambdaGenericInfoRepository = TripleValuesRepository.fromKey(
            TypeHelper::getLambdaGenericInfo
    );

    private static Tuple3<Boolean, Class[], Class> getLambdaGenericInfo(AbstractThrowable lambda){
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

        Class[] paraClasses = ArrayHelper.objectify(parameterClasses);
        return Tuple.create(paraClasses.length == parameterCount, paraClasses, returnClass);
    }

    /**
     * Helper method to get the return type of a RunnableThrowable (as void.class) or SupplierThrowable.
     * Notice: only applicable on first-hand lambda expressions. Lambda Expressions created by Lambda would erase the return type in Java 1.8.161.
     * @param aThrowable solid Lambda expression
     * @return  The type of the return value defined by the Lambda Expression.
     */
    public static Class getReturnType(AbstractThrowable aThrowable){
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
    public static final HexaValuesRepository<
                Class,              //concerned Class

                Predicate<Class> //Equivalent predicate to check if another class is regarded as equal with the concerned class
                , FunctionThrowable<Integer, Object>             //Array factory
                , Class              //Class of its array
                , TriConsumerThrowable<Object, Integer, Object>//Function to set the Element at Index of its array
                , TriFunctionThrowable<Object, Integer, Integer, Object>//copyOfRange(original, from, to) -> new array
                , Function<Object, String>             // Convert array to String
        > classOperators = HexaValuesRepository.fromKey(
            TypeHelper::getReservedClassOperators,
            null,
            TypeHelper::makeClassOperators
    );

    private static Map<Class, Tuple6<
                Predicate<Class>,
                FunctionThrowable<Integer, Object>,
                Class,
                TriConsumerThrowable<Object, Integer, Object>,
                TriFunctionThrowable<Object, Integer, Integer, Object>,
                Function<Object, String>
                >> getReservedClassOperators() {
        Map<Class, Tuple6<
                Predicate<Class>,
                FunctionThrowable<Integer, Object>,
                Class,
                TriConsumerThrowable<Object, Integer, Object>,
                TriFunctionThrowable<Object, Integer, Integer, Object>,
                Function<Object, String>
                >> map = new HashMap();

        Predicate<Class> classPredicate = clazz -> int.class.equals(clazz) || Integer.class.equals(clazz);
        map.put(int.class, Tuple.create(
                classPredicate
                , i -> new int[i]
                , int[].class
                , (array, index, value) -> ((int[])array)[index] = ((Integer)value).intValue()
//                , (array, index, value) -> Array.setInt(array, index.intValue(), Integer.class.cast(value).intValue())
                , (array, from, to) -> Arrays.copyOfRange((int[])array, from, to)
                , array -> Arrays.toString((int [])array)));
        map.put(Integer.class, Tuple.create(
                classPredicate
                , i -> new Integer[i]
                , Integer[].class
                , arraySet
                , (array, from, to) -> Arrays.copyOfRange((Integer[])array, from, to)
                , array -> Arrays.toString((Integer[])array)));
        classPredicate = clazz -> byte.class.equals(clazz) || Byte.class.equals(clazz);
        map.put(byte.class, Tuple.create(
                classPredicate
                , i -> new byte[i]
                , byte[].class
                , (array, index, value) -> ((byte[])array)[index] = ((Byte)value).byteValue()
//                , (array, index, value) -> Array.setByte(array, index.intValue(), Byte.class.cast(value).byteValue())
                , (array, from, to) -> Arrays.copyOfRange((byte[])array, from, to)
                , array -> Arrays.toString((byte[])array)));
        map.put(Byte.class, Tuple.create(
                classPredicate
                , i -> new Byte[i]
                , Byte[].class
                , arraySet
                , (array, from, to) -> Arrays.copyOfRange((Byte[])array, from, to)
                , array -> Arrays.toString((Byte[])array)));
        classPredicate = clazz -> boolean.class.equals(clazz) || Boolean.class.equals(clazz);
        map.put(boolean.class, Tuple.create(
                classPredicate
                , i -> new boolean[i]
                , boolean[].class
                , (array, index, value) -> ((boolean[])array)[index] = ((Boolean)value).booleanValue()
//                , (array, index, value) -> Array.setBoolean(array, index.intValue(), Boolean.class.cast(value).booleanValue())
                , (array, from, to) -> Arrays.copyOfRange((boolean[])array, from, to)
                , array -> Arrays.toString((boolean[])array)));
        map.put(Boolean.class, Tuple.create(
                classPredicate
                , i -> new Boolean[i]
                , Boolean[].class
                , arraySet
                , (array, from, to) -> Arrays.copyOfRange((Boolean[])array, from, to)
                , array -> Arrays.toString((Boolean[])array)));
        classPredicate = clazz -> char.class.equals(clazz) || Character.class.equals(clazz);
        map.put(char.class, Tuple.create(
                classPredicate
                , i -> new char[i]
                , char[].class
                , (array, index, value) -> ((char[])array)[index] = ((Character)value).charValue()
//                , (array, index, value) -> Array.setChar(array, index, Character.class.cast(value).charValue())
                , (array, from, to) -> Arrays.copyOfRange((char[])array, from, to)
                , array -> Arrays.toString((char[])array)));
        map.put(Character.class, Tuple.create(
                classPredicate
                , i -> new Character[i]
                , Character[].class
                , arraySet
                , (array, from, to) -> Arrays.copyOfRange((Character[])array, from, to)
                , array -> Arrays.toString((Character[])array)));
        classPredicate = clazz -> short.class.equals(clazz) || Short.class.equals(clazz);
        map.put(short.class, Tuple.create(
                classPredicate
                , i -> new short[i]
                , short[].class
                , (array, index, value) -> ((short[])array)[index] = ((Short)value).shortValue()
//                , (array, index, value) -> Array.setShort(array, index, Short.class.cast(value).shortValue())
                , (array, from, to) -> Arrays.copyOfRange((short[])array, from, to)
                , array -> Arrays.toString((short[])array)));
        map.put(Short.class, Tuple.create(
                classPredicate
                , i -> new Short[i]
                , Short[].class
                , arraySet
                , (array, from, to) -> Arrays.copyOfRange((Short[])array, from, to)
                , array -> Arrays.toString((Short[])array)));
        classPredicate = clazz -> long.class.equals(clazz) || Long.class.equals(clazz);
        map.put(long.class, Tuple.create(
                classPredicate
                , i -> new long[i]
                , long[].class
                , (array, index, value) -> ((long[])array)[index] = ((Long)value).longValue()
//                , (array, index, value) -> Array.setLong(array, index, Long.class.cast(value).longValue())
                , (array, from, to) -> Arrays.copyOfRange((long[])array, from, to)
                , array -> Arrays.toString((long[])array)));
        map.put(Long.class, Tuple.create(
                classPredicate
                , i -> new Long[i]
                , Long[].class
                , arraySet
                , (array, from, to) -> Arrays.copyOfRange((Long[])array, from, to)
                , array -> Arrays.toString((Long[])array)));
        classPredicate = clazz -> double.class.equals(clazz) || Double.class.equals(clazz);
        map.put(double.class, Tuple.create(
                classPredicate
                , i -> new double[i]
                , double[].class
                , (array, index, value) -> ((double[])array)[index] = ((Double)value).doubleValue()
//                , (array, index, value) -> Array.setDouble(array, index, Double.class.cast(value).doubleValue())
                , (array, from, to) -> Arrays.copyOfRange((double[])array, from, to)
                , array -> Arrays.toString((double[])array)));
        map.put(Double.class, Tuple.create(
                classPredicate
                , i -> new Double[i]
                , Double[].class
                , arraySet
                , (array, from, to) -> Arrays.copyOfRange((Double[])array, from, to)
                , array -> Arrays.toString((Double[])array)));
        classPredicate = clazz -> float.class.equals(clazz) || Float.class.equals(clazz);
        map.put(float.class, Tuple.create(
                classPredicate
                , i -> new float[i]
                , float[].class
                , (array, index, value) -> ((float[])array)[index] = ((Float)value).floatValue()
//                , (array, index, value) -> Array.setFloat(array, index, Float.class.cast(value).floatValue())
                , (array, from, to) -> Arrays.copyOfRange((float[])array, from, to)
                , array -> Arrays.toString((float[])array)));
        map.put(Float.class, Tuple.create(
                classPredicate
                , i -> new Float[i]
                , Float[].class
                , arraySet
                , (array, from, to) -> Arrays.copyOfRange((Float[])array, from, to)
                , array -> Arrays.toString((Float[])array)));
        return map;
    }

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
     * deep toString operator of the elements if they are arays
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
    private static final HexaValuesRepository<
                        Class,      // original Class of the concerned object

                        Boolean     // isPrmitiveType, when the original class is primitive type, or it is array of primitive type
                        , Object    // default value of the concerned class
                        , Class     // equivalent class: could be the wrapper if original class is primitive,
                                    // or primitive type if original class is wrapper
                        , Function<Object, Object>  // convert the value of original class to equivalent class parallelly
                        , Function<Object, Object>  // convert the value of original class to equivalent class in serial
                        , Function<Object, Object>  // convert the value of original class to equivalent class either parallely or srially
                > baseTypeConverters = HexaValuesRepository.fromKey(
            TypeHelper::getReservedBaseTypeConverters,
            null,
            TypeHelper::makeBaseTypeConverters
    );

    private static Map<Class, Tuple6<Boolean, Object, Class, Function<Object,Object>, Function<Object,Object>, Function<Object,Object>>>
        getReservedBaseTypeConverters(){
        Map<Class, Tuple6<Boolean, Object, Class, Function<Object,Object>, Function<Object,Object>, Function<Object,Object>>> map = new
                HashMap<Class, Tuple6<Boolean, Object, Class, Function<Object,Object>, Function<Object,Object>, Function<Object,Object>>>();

        //For primitive values, return itself as object would convert it to the wrapper type automatically
        Function<Object,Object> convertWithCasting = returnsSelf;
        map.put(boolean.class, Tuple.create(
                true
                , false
                , Boolean.class
                , convertWithCasting
                , convertWithCasting
                , convertWithCasting));
        convertWithCasting = fromElement -> ((Boolean)fromElement).booleanValue();
        map.put(Boolean.class, Tuple.create(
                false
                , false
                , boolean.class
                , convertWithCasting
                , convertWithCasting
                , convertWithCasting));
        convertWithCasting = returnsSelf;
        map.put(byte.class, Tuple.create(
                true
                , (byte)0
                , Byte.class
                , convertWithCasting
                , convertWithCasting
                , convertWithCasting));
        convertWithCasting = fromElement -> Byte.class.cast(fromElement).byteValue();
        map.put(Byte.class, Tuple.create(
                false
                , (byte)0
                , byte.class
                , convertWithCasting
                , convertWithCasting
                , convertWithCasting));
        convertWithCasting = returnsSelf;
        map.put(char.class, Tuple.create(
                true
                , (char)0
                , Character.class
                , convertWithCasting
                , convertWithCasting
                , convertWithCasting));
        convertWithCasting = fromElement -> Character.class.cast(fromElement).charValue();
        map.put(Character.class, Tuple.create(
                false
                , (char)0
                , char.class
                , convertWithCasting
                , convertWithCasting
                , convertWithCasting));
        convertWithCasting = returnsSelf;
        map.put(double.class, Tuple.create(
                true
                , 0d
                , Double.class
                , convertWithCasting
                , convertWithCasting
                , convertWithCasting));
        convertWithCasting = fromElement -> Double.class.cast(fromElement).doubleValue();
        map.put(Double.class, Tuple.create(
                false
                , 0d
                , double.class
                , convertWithCasting
                , convertWithCasting
                , convertWithCasting));
        convertWithCasting = returnsSelf;
        map.put(float.class, Tuple.create(
                true
                , 0f
                , Float.class
                , convertWithCasting
                , convertWithCasting
                , convertWithCasting));
        convertWithCasting = fromElement -> Float.class.cast(fromElement).floatValue();
        map.put(Float.class, Tuple.create(
                false
                , 0f
                , float.class
                , convertWithCasting
                , convertWithCasting
                , convertWithCasting));
        convertWithCasting = returnsSelf;
        map.put(int.class, Tuple.create(
                true
                , 0
                , Integer.class
                , convertWithCasting
                , convertWithCasting
                , convertWithCasting));
        convertWithCasting = fromElement -> Integer.class.cast(fromElement).intValue();
        map.put(Integer.class, Tuple.create(
                false
                , 0
                , int.class
                , convertWithCasting
                , convertWithCasting
                , convertWithCasting));
        convertWithCasting = returnsSelf;
        map.put(long.class, Tuple.create(
                true
                , 0L
                , Long.class
                , convertWithCasting
                , convertWithCasting
                , convertWithCasting));
        convertWithCasting = fromElement -> Long.class.cast(fromElement).longValue();
        map.put(Long.class, Tuple.create(
                false
                , 0L
                , long.class
                , convertWithCasting
                , convertWithCasting
                , convertWithCasting));
        convertWithCasting = returnsSelf;
        map.put(short.class, Tuple.create(
                true
                , (short)0
                , Short.class
                , convertWithCasting
                , convertWithCasting
                , convertWithCasting));
        convertWithCasting = fromElement -> Short.class.cast(fromElement).shortValue();
        map.put(Short.class, Tuple.create(
                false
                , (short)0
                , short.class
                , convertWithCasting
                , convertWithCasting
                , convertWithCasting));
        return map;
    }

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
            return Tuple.create(false, null, null, returnsSelf, returnsSelf, returnsSelf);

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
     * Get the converter of one class to its corresponding equivalent class
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

    //region Repository to keep deep converters and deep predicate of two classes
    /**
     * This repository use two classes as its key:
     * #    fromClass:  the class of the first value to be predicated, or the type of source value to be converted by the converters
     * #    toClass:    the class of the second value to be predicated, or the target type to be converted to by the converters
     * to generate and cache tuple containing:
     * #    parallelConverter:  Convert the object of type fromClass to value of type toClass in parallel
     * #    serialConverter:  Convert the object of type fromClass to value of type toClass in serial
     * #    defaultConverter:  Convert the object of type fromClass to value of type toClass either in parallel or in serial based on its length
     * #    parallelPredicate:  Predicate if value of type fromClass is equal with value of type toClass in parallel
     * #    serialPredicate:  Predicate if value of type fromClass is equal with value of type toClass in serial
     * #    defaultPredicate:  Predicate if value of type fromClass is equal with value of type toClass, in parallel or in serial based on its length
     */
    public static final HexaValuesRepository.DualKeys<
            Class       //fromClass
            ,Class      //toClass

            , Function<Object, Object>          //Convert the first object to another in parallel
            , Function<Object, Object>          //Convert the first object to another serially
            , Function<Object, Object>          //Convert the first object by default, in parallel or serial based on the length of the array
            , BiPredicate<Object,Object>        //Predicate of deepEquals in parallel
            , BiPredicate<Object,Object>        //Predicate of deepEquals in serial
            , BiPredicate<Object,Object>        //Predicate of deepEquals by default, in parallel or serial based on the length of the array
        > deepEvaluators = HexaValuesRepository.fromTwoKeys(
            () -> new HashMap<Tuple2<Class,Class>,
                    Tuple6<Function<Object, Object>, Function<Object, Object>, Function<Object, Object>, BiPredicate<Object, Object>, BiPredicate<Object, Object>, BiPredicate<Object, Object>>>(){{
                        put(Tuple.create(OBJECT_CLASS, OBJECT_CLASS),
                                Tuple.create(returnsSelf, returnsSelf, returnsSelf, objectsEquals, objectsEquals, objectsEquals));

                        put(Tuple.create(Object[].class, Object[].class),
                                Tuple.create(returnsSelf, returnsSelf, returnsSelf,
                                        (a,b)->arraysParallelDeepEquals((Object[])a, (Object[])b),
                                        (a,b)->arraysSerialDeepEquals((Object[])a, (Object[])b),
                                        (a,b)->arraysDeepEquals((Object[])a, (Object[])b)
                                        ));
            }},
            null,
            TypeHelper::getDeepEvaluators
        );

    private static Tuple6<Function<Object, Object>, Function<Object, Object>, Function<Object, Object>, BiPredicate<Object, Object>, BiPredicate<Object, Object>, BiPredicate<Object, Object>>
        getDeepEvaluators(Class class1, Class class2) throws Exception {
        Objects.requireNonNull(class1);
        Objects.requireNonNull(class2);

        BiPredicate<Object,Object> parallelPredicate, serialPredicate, defaultPredicate;

        if(!class1.isArray() && !class2.isArray()){
            if(Objects.equals(class1, class2)){
                //They are identical classes, no converter needed, user the Objects.equals as comparator
                parallelPredicate = serialPredicate = defaultPredicate = objectsEquals;
                return Tuple.create(returnsSelf, returnsSelf, returnsSelf,
                        parallelPredicate, serialPredicate, defaultPredicate);
            } else if (Objects.equals(getEquivalentClass(class1), class2)){
                //One class is primitive, another is its wrapper, so use corresponding converter
                final Function<Object, Object> singleObjectConverter = getToEquivalentConverter(class1);
                parallelPredicate = serialPredicate = defaultPredicate
                        = (a, b) -> Objects.equals(singleObjectConverter.apply(a), b);
                return Tuple.create(singleObjectConverter, singleObjectConverter, singleObjectConverter,
                        parallelPredicate, serialPredicate, defaultPredicate);
            } else if (class2.isAssignableFrom(class1)){
                //Value of type class1 can be assigned to variable of type class2 directly, like ArrayList can be assigned to List
                final Function<Object, Object> singleObjectConverter = returnsSelf;
                //Use the Objects.equals in case equals() is override
                parallelPredicate = serialPredicate = defaultPredicate = objectsEquals;
                return Tuple.create(singleObjectConverter, singleObjectConverter, singleObjectConverter,
                        parallelPredicate, serialPredicate, defaultPredicate);
            } else if (class1.isAssignableFrom(class2)){
                //Value of type class1 cannot be assigned to variable of type class2, use cast
                final Function<Object, Object> singleObjectConverter = class2::cast;
                //Use the Objects.equals in case equals() is override
                parallelPredicate = serialPredicate = defaultPredicate = objectsEquals;
                return Tuple.create(singleObjectConverter, singleObjectConverter, singleObjectConverter,
                        parallelPredicate, serialPredicate, defaultPredicate);
            } else {
                //No chance to convert or equals
                return Tuple.create(mapsToNull, mapsToNull, mapsToNull,
                        alwaysFalse, alwaysFalse, alwaysFalse);
            }
        }

        Class componentClasses1 = class1.getComponentType();
        Class componentClasses2 = class2.getComponentType();
        BiPredicate<Object, Object> componentParallelPredicate = getParallelPredicate(componentClasses1, componentClasses2);
        BiPredicate<Object, Object> componentSerialPredicate = getSerialPredicate(componentClasses1, componentClasses2);
        BiPredicate<Object, Object> componentDefaultPredicate = getDefaultPredicate(componentClasses1, componentClasses2);

        parallelPredicate = (obj1, obj2) -> {
            if(Objects.equals(obj1, obj2))
                return true;
            else if(obj1 == null || obj2 == null)
                return false;

            int length = Array.getLength(obj1);
            if(length != Array.getLength(obj2))
                return false;

            Predicate<Integer> elementEquals = i -> {
                try {
                    Object element1 = Array.get(obj1, i);
                    Object element2 = Array.get(obj2, i);
                    return componentParallelPredicate.test(element1, element2);
                }catch (Exception ex){
                    return false;
                }
            };

            boolean result = IntStream.range(0, length).boxed().parallel()
                    .allMatch(elementEquals);
            return result;
        };

        serialPredicate = (obj1, obj2) -> {
            if(Objects.equals(obj1, obj2))
                return true;
            else if(obj1 == null || obj2 == null)
                return false;

            int length = Array.getLength(obj1);
            if(length != Array.getLength(obj2))
                return false;

            try {
                for (int i = 0; i < length; i++) {
                    Object element1 = Array.get(obj1, i);
                    Object element2 = Array.get(obj2, i);
                    if(!componentSerialPredicate.test(element1, element2))
                        return false;
                }
                return true;
            }catch(Exception ex){
                return false;
            }
        };

        defaultPredicate = (obj1, obj2) -> {
            if(Objects.equals(obj1, obj2))
                return true;
            else if(obj1 == null || obj2 == null)
                return false;

            int length = Array.getLength(obj1);
            if(length != Array.getLength(obj2))
                return false;

            if(length < PARALLEL_EVALUATION_THRESHOLD){
                try {
                    for (int i = 0; i < length; i++) {
                        Object element1 = Array.get(obj1, i);
                        Object element2 = Array.get(obj2, i);
                        if(!componentDefaultPredicate.test(element1, element2))
                            return false;
                    }
                    return true;
                }catch(Exception ex){
                    return false;
                }
            }else{
                Predicate<Integer> elementEquals = i -> {
                    try {
                        Object element1 = Array.get(obj1, i);
                        Object element2 = Array.get(obj2, i);
                        return componentDefaultPredicate.test(element1, element2);
                    }catch (Exception ex){
                        return false;
                    }
                };

                boolean result = IntStream.range(0, length).boxed().parallel()
                        .allMatch(elementEquals);
                return result;
            }
        };

        //Now both type are of array
        //First, check fromClass and toClass is identical
        if(class1.equals(class2)){
            return Tuple.create(returnsSelf, returnsSelf, returnsSelf, parallelPredicate, serialPredicate, defaultPredicate);
        }else if(!getClassEqualitor(class1).test(class2)) {
            //Second, check to see if fromClass is equivalent to toClass
            // Use the equivalent converter if they are
            return Tuple.create(mapsToNull, mapsToNull, mapsToNull, alwaysFalse, alwaysFalse, alwaysFalse);
        }

        FunctionThrowable<Integer, Object> factory = TypeHelper.getArrayFactory(componentClasses2);
        TriConsumerThrowable<Object, Integer, Object> toElementSetter = getArrayElementSetter(componentClasses2);

        Function<Object, Object> serialElementConverter = getToEquivalentSerialConverter(componentClasses1);

        TriFunctionThrowable.TriFunction<Object, Object, Integer, Boolean> elementMappingWithException =
                getExceptionWithMapping(toElementSetter, serialElementConverter);

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
                    Object convertedElement = serialElementConverter.apply(fromElement);
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
                        Object convertedElement = serialElementConverter.apply(fromElement);
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

        return Tuple.create(parallelConverter, serialConverter, defaultConverter,
                parallelPredicate, serialPredicate, defaultPredicate);
    }
    //endregion

    public static <T,U> boolean arraysSerialDeepEquals(T[] array1, U[] array2){
        if(Objects.equals(array1, array2))
            return true;
        else if(array1 == null || array2 == null)
            return false;

        int length = Array.getLength(array1);
        if(length != Array.getLength(array2))
            return false;

        try {
            for (int i = 0; i < length; i++) {
                Object element1 = Array.get(array1, i);
                Object element2 = Array.get(array2, i);
                if(!Objects.equals(element1, element2))
                    return false;
            }
            return true;
        }catch(Exception ex){
            return false;
        }
    }

    public static <T,U> boolean arraysParallelDeepEquals(T[] array1, U[] array2){
        if(Objects.equals(array1, array2))
            return true;
        else if(array1 == null || array2 == null)
            return false;

        int length = Array.getLength(array1);
        if(length != Array.getLength(array2))
            return false;

        Predicate<Integer> elementEquals = i -> {
            try {
                Object element1 = Array.get(array1, i);
                Object element2 = Array.get(array2, i);
                return Objects.equals(element1, element2);
            }catch (Exception ex){
                return false;
            }
        };

        boolean result = IntStream.range(0, length).boxed().parallel()
                .allMatch(elementEquals);
        return result;
    }

    public static <T,U> boolean arraysDeepEquals(T[] array1, U[] array2){
        if(Objects.equals(array1, array2))
            return true;
        else if(array1 == null || array2 == null)
            return false;

        int length = Array.getLength(array1);
        if(length != Array.getLength(array2))
            return false;

        if(length < PARALLEL_EVALUATION_THRESHOLD){
            try {
                for (int i = 0; i < length; i++) {
                    Object element1 = Array.get(array1, i);
                    Object element2 = Array.get(array2, i);
                    if(!valueEquals(element1, element2))
                        return false;
                }
                return true;
            }catch(Exception ex){
                return false;
            }
        }else{
            Predicate<Integer> elementEquals = i -> {
                try {
                    Object element1 = Array.get(array1, i);
                    Object element2 = Array.get(array2, i);
                    return valueEquals(element1, element2);
                }catch (Exception ex){
                    return false;
                }
            };

            boolean result = IntStream.range(0, length).boxed().parallel()
                    .allMatch(elementEquals);
            return result;
        }
    }

    public static BiPredicate<Object, Object> getParallelPredicate(Class class1, Class class2){
        Objects.requireNonNull(class1);
        Objects.requireNonNull(class2);

        return deepEvaluators.getFourth(class1, class2);
    }

    public static BiPredicate<Object, Object> getSerialPredicate(Class class1, Class class2){
        Objects.requireNonNull(class1);
        Objects.requireNonNull(class2);

        return deepEvaluators.getFifth(class1, class2);
    }

    public static BiPredicate<Object, Object> getDefaultPredicate(Class class1, Class class2){
        Objects.requireNonNull(class1);
        Objects.requireNonNull(class2);

        return deepEvaluators.getSixth(class1, class2);
    }

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
