package com.easyworks.utility;

import com.easyworks.Functions;
import com.easyworks.function.BiFunctionThrowable;
import com.easyworks.function.FunctionThrowable;
import com.easyworks.function.TriConsumerThrowable;
import com.easyworks.function.TriFunctionThrowable;
import com.easyworks.repository.DualValuesRepository;
import com.easyworks.repository.HeptaValuesRepository;
import com.easyworks.repository.PentaValuesRepository;
import com.easyworks.repository.QuadValuesRepository;
import com.easyworks.tuple.Hepta;
import com.easyworks.tuple.Penta;
import com.easyworks.tuple.Tuple;
import sun.reflect.ConstantPool;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class TypeHelper {
    private static boolean EMPTY_ARRAY_AS_DEFAULT = true;
    private static int PARALLEL_EVALUATION_THRESHOLD = 100;

    static{
        if("false".equalsIgnoreCase(System.getProperty("EMPTY_ARRAY_AS_DEFAULT"))){
            EMPTY_ARRAY_AS_DEFAULT = true;
        }
    }

    private static final Function<Object, Object> returnsSelf = obj -> obj;
    private static final Function<Object, Object> mapsToNull = obj -> null;
    private static final FunctionThrowable<Object, Object> returnsSelfThrowable = obj -> obj;
    private static final BiPredicate<Object, Object> alwaysFalse = (a, b) -> false;
    private static final BiPredicate<Object, Object> objectsEquals = (a, b) -> Objects.equals(a, b);

    private static final Method _getConstantPool = (Method) Functions.ReturnsDefaultValue.apply(() -> {
        Method method = Class.class.getDeclaredMethod("getConstantPool");
        method.setAccessible(true);
        return method;
    });

    public static ConstantPool getConstantPoolOfClass(Class objectClass){
        return (ConstantPool) Functions.ReturnsDefaultValue.apply(()->_getConstantPool.invoke(objectClass));
    }


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
                        Function<Object, String> elementToString = classOperators.getSeventhValue(elementClass.getComponentType());
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

    /**
     * Repository to keep operators related with specific class that is kept as the keys of the map.
     * Relative operators are saved as a strong-typed Hepta with following elements:
     *      Equivalent predicate: to evaluate if another class is regarded as equivalent to the specific class.
     *              Notice: int.class and Integer.class are regarded as equivalent in this library, their array types
     *                  int[].class and Integer[].class are also regarded as equivalent.
     *      ArrayFactory: given the length of the new array, the factory would create a new array with elements of the specific class.
     *      ArrayClass:     class of the array containing elements of the specific class
     *      ArrayElementGetter: getter of the element of the array with its index
     *      ArrayElementSetter: setter of the array with target index and element to be set
     *      CopyOfRange:    method to copy part of the original array to a new array of the same type, while its length
     *              is determined by the from and to indexes
     *      ConvertToString:    convert the array to a string by recursively applying the deep toString() on every elements
     *              of the array
     */
    public static final HeptaValuesRepository<
            Class,              //concerned Class

            Predicate<Class> //Equivalent predicate
            , FunctionThrowable<Integer, Object>             //Array factory
            , Class              //Class of its array
            , BiFunctionThrowable<Object, Integer, Object>//Function to get the Element as index of its array
            , TriConsumerThrowable<Object, Integer, Object>//Function to set the Element at Index of its array
            , TriFunctionThrowable<Object, Integer, Integer, Object>//copyOfRange(original, from, to) -> new array
            , Function<Object, String>             // Convert array to String
    > classOperators = HeptaValuesRepository.fromKey(
            () -> new HashMap<Class,
                    Hepta<
                        Predicate<Class>,
                        FunctionThrowable<Integer, Object>,
                        Class,
                        BiFunctionThrowable<Object, Integer, Object>,
                        TriConsumerThrowable<Object, Integer, Object>,
                        TriFunctionThrowable<Object, Integer, Integer, Object>,
                        Function<Object, String>
                    >
                >(){{
                //region Special cases of Primitive types and their wrappers
                Predicate<Class> classPredicate = clazz -> int.class.equals(clazz) || Integer.class.equals(clazz);
                put(int.class, Tuple.create(
                            classPredicate
                            , i -> new int[i]
                            , int[].class
                            , (array, index) -> Array.getInt(array, index)
                            , (array, index, value) -> Array.setInt(array, index.intValue(), Integer.class.cast(value).intValue())
                            , (array, from, to) -> Arrays.copyOfRange((int[])array, from, to)
                            , array -> Arrays.toString((int [])array)));
                    put(Integer.class, Tuple.create(
                            classPredicate
                            , i -> new Integer[i]
                            , Integer[].class
                            , Array::get
                            , Array::set
                            , (array, from, to) -> Arrays.copyOfRange((Integer[])array, from, to)
                            , array -> Arrays.toString((Integer[])array)));
                classPredicate = clazz -> byte.class.equals(clazz) || Byte.class.equals(clazz);
                    put(byte.class, Tuple.create(
                            classPredicate
                            , i -> new byte[i]
                            , byte[].class
                            , (array, index) -> Array.getByte(array, index)
                            , (array, index, value) -> Array.setByte(array, index.intValue(), Byte.class.cast(value).byteValue())
                            , (array, from, to) -> Arrays.copyOfRange((byte[])array, from, to)
                            , array -> Arrays.toString((byte[])array)));
                    put(Byte.class, Tuple.create(
                            classPredicate
                            , i -> new Byte[i]
                            , Byte[].class
                            , Array::get
                            , Array::set
                            , (array, from, to) -> Arrays.copyOfRange((Byte[])array, from, to)
                            , array -> Arrays.toString((Byte[])array)));
                classPredicate = clazz -> boolean.class.equals(clazz) || Boolean.class.equals(clazz);
                    put(boolean.class, Tuple.create(
                            classPredicate
                            , i -> new boolean[i]
                            , boolean[].class
                            , (array, index) -> Array.getBoolean(array, index)
                            , (array, index, value) -> Array.setBoolean(array, index.intValue(), Boolean.class.cast(value).booleanValue())
                            , (array, from, to) -> Arrays.copyOfRange((boolean[])array, from, to)
                            , array -> Arrays.toString((boolean[])array)));
                    put(Boolean.class, Tuple.create(
                            classPredicate
                            , i -> new Boolean[i]
                            , Boolean[].class
                            , Array::get
                            , Array::set
                            , (array, from, to) -> Arrays.copyOfRange((Boolean[])array, from, to)
                            , array -> Arrays.toString((Boolean[])array)));
                classPredicate = clazz -> char.class.equals(clazz) || Character.class.equals(clazz);
                    put(char.class, Tuple.create(
                            classPredicate
                            , i -> new char[i]
                            , char[].class
                            , (array, index) -> Array.getChar(array, index)
                            , (array, index, value) -> Array.setChar(array, index, Character.class.cast(value).charValue())
                            , (array, from, to) -> Arrays.copyOfRange((char[])array, from, to)
                            , array -> Arrays.toString((char[])array)));
                    put(Character.class, Tuple.create(
                            classPredicate
                            , i -> new Character[i]
                            , Character[].class
                            , Array::get
                            , Array::set
                            , (array, from, to) -> Arrays.copyOfRange((Character[])array, from, to)
                            , array -> Arrays.toString((Character[])array)));
                classPredicate = clazz -> short.class.equals(clazz) || Short.class.equals(clazz);
                    put(short.class, Tuple.create(
                            classPredicate
                            , i -> new short[i]
                            , short[].class
                            , (array, index) -> Array.getShort(array, index)
                            , (array, index, value) -> Array.setShort(array, index, Short.class.cast(value).shortValue())
                            , (array, from, to) -> Arrays.copyOfRange((short[])array, from, to)
                            , array -> Arrays.toString((short[])array)));
                    put(Short.class, Tuple.create(
                            classPredicate
                            , i -> new Short[i]
                            , Short[].class
                            , Array::get
                            , Array::set
                            , (array, from, to) -> Arrays.copyOfRange((Short[])array, from, to)
                            , array -> Arrays.toString((Short[])array)));
                classPredicate = clazz -> long.class.equals(clazz) || Long.class.equals(clazz);
                    put(long.class, Tuple.create(
                            classPredicate
                            , i -> new long[i]
                            , long[].class
                            , (array, index) -> Array.getLong(array, index)
                            , (array, index, value) -> Array.setLong(array, index, Long.class.cast(value).longValue())
                            , (array, from, to) -> Arrays.copyOfRange((long[])array, from, to)
                            , array -> Arrays.toString((long[])array)));
                    put(Long.class, Tuple.create(
                            classPredicate
                            , i -> new Long[i]
                            , Long[].class
                            , Array::get
                            , Array::set
                            , (array, from, to) -> Arrays.copyOfRange((Long[])array, from, to)
                            , array -> Arrays.toString((Long[])array)));
                classPredicate = clazz -> double.class.equals(clazz) || Double.class.equals(clazz);
                    put(double.class, Tuple.create(
                            classPredicate
                            , i -> new double[i]
                            , double[].class
                            , (array, index) -> Array.getDouble(array, index)
                            , (array, index, value) -> Array.setDouble(array, index, Double.class.cast(value).doubleValue())
                            , (array, from, to) -> Arrays.copyOfRange((double[])array, from, to)
                            , array -> Arrays.toString((double[])array)));
                    put(Double.class, Tuple.create(
                            classPredicate
                            , i -> new Double[i]
                            , Double[].class
                            , Array::get
                            , Array::set
                            , (array, from, to) -> Arrays.copyOfRange((Double[])array, from, to)
                            , array -> Arrays.toString((Double[])array)));
                classPredicate = clazz -> float.class.equals(clazz) || Float.class.equals(clazz);
                    put(float.class, Tuple.create(
                            classPredicate
                            , i -> new float[i]
                            , float[].class
                            , (array, index) -> Array.getFloat(array, index)
                            , (array, index, value) -> Array.setFloat(array, index, Float.class.cast(value).floatValue())
                            , (array, from, to) -> Arrays.copyOfRange((float[])array, from, to)
                            , array -> Arrays.toString((float[])array)));
                    put(Float.class, Tuple.create(
                            classPredicate
                            , i -> new Float[i]
                            , Float[].class
                            , Array::get
                            , Array::set
                            , (array, from, to) -> Arrays.copyOfRange((Float[])array, from, to)
                            , array -> Arrays.toString((Float[])array)));
                //endregion

            }},
            null,
            clazz -> {
                FunctionThrowable<Integer, Object> arrayFactory = (Integer length) -> Array.newInstance(clazz, length);
                Predicate<Class> cPredicate;

                if(!clazz.isArray()){
                    cPredicate = otherClass -> otherClass != null && clazz.isAssignableFrom(otherClass);
                } else {
                    Class componentClass = clazz.getComponentType();
                    Predicate<Class> componentPredicate = getClassPredicate(componentClass);
                    cPredicate = otherClass -> otherClass != null && otherClass.isArray()
                            && componentPredicate.test(otherClass.getComponentType());
                }

                Class arrayClass = arrayFactory.apply(0).getClass();
                BiFunctionThrowable<Object, Integer, Object> getElement = Array::get;
                TriConsumerThrowable<Object, Integer, Object> setElement = Array::set;
                TriFunctionThrowable<Object, Integer, Integer, Object> copyOfRange = asGenericCopyOfRange(clazz);
                Function<Object, String> toString = getDeepToString(clazz);
                return Tuple.create(cPredicate, arrayFactory, arrayClass, getElement, setElement, copyOfRange, toString);
            }
    );

    /**
     * Retrive the class predicate of the target class to evaluate if another class is equivalent to this class
     * @param clazz     The class to be served by the predicate
     * @return          Predicate&lt;Class&gt; to evaluate if another class is equivalent to <tt>clazz</tt>
     */
    public static Predicate<Class> getClassPredicate(Class clazz){
        if(clazz == null) return null;

        return classOperators.getFirstValue(clazz);
    }

    /**
     * Retrive the array factory of the target class
     * @param clazz     The class to be served by the factory
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
     * get the getter operator to get element at specific position of concerned array
     * @param clazz type of the elements to compose the array
     * @return      operator with 2 input arguments of the arra and the index, and returns the element at that position
     */
    public static BiFunctionThrowable<Object, Integer, Object> getArrayElementGetter(Class clazz){
        if(clazz == null) return null;

        return classOperators.getFourthValue(clazz);
    }

    /**
     * get the setter operator to set element at specific position of concerned array with given value
     * @param clazz type of the elements to compose the array
     * @return      operator receiving 3 arguments (array, index, value) and set the element at index of the array with the given value
     */
    public static TriConsumerThrowable<Object, Integer, Object> getArrayElementSetter(Class clazz){
        if(clazz == null) return null;

        return classOperators.getFifthValue(clazz);
    }

    /**
     * get the copyOfRange operator to build new array of the same type, but with different ranges
     * @param clazz type of the elements to compose the array
     * @return      copyOfRange operator receiving 3 arguments(originalArray, fromIndex, toIndex) and return the new array
     *              of the same type as originalArray, but with part or all elements as specified by the fromIndex and toIndex
     */
    public static TriFunctionThrowable<Object, Integer, Integer, Object> getArrayRangeCopier(Class clazz){
        if(clazz == null) return null;

        return classOperators.getSixthValue(clazz);
    }

    /**
     * get the deep toString operator to compose a String reflect all values of the elements, calling the corresponding
     * deep toString operator of the elements if they are arays
     * @param clazz type of the elements composing the concerned array
     * @return  deep toString operator to capture all elements of the array and any arrays represented by the elements
     */
    public static Function<Object, String> getArrayToString(Class clazz){
        if(clazz == null) return null;

        return classOperators.getSeventhValue(clazz);
    }

    private static final PentaValuesRepository<
                    Class,      // original Class of the concerned object

                    Boolean     // isPrmitiveType, when the original class is primitive type, or it is array of primitive type
                    , Object    // default value of the concerned class
                    , Class     // equivalent class: could be the wrapper if original class is primitive,
                                // or primitive type if original class is wrapper
                    , Function<Object, Object>  // convert the value of original class to equivalent class parallelly
                    , Function<Object, Object>  // convert the value of original class to equivalent class in serial
            > baseTypeConverters = PentaValuesRepository.fromKey(
            () -> new HashMap<Class, Penta<Boolean, Object, Class, Function<Object,Object>, Function<Object,Object>>>(){{
                Function<Object,Object> convertWithCasting = fromElement -> Boolean.valueOf((boolean)fromElement);
                put(boolean.class, Tuple.create(
                        true
                        , false
                        , Boolean.class
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> ((Boolean)fromElement).booleanValue();
                put(Boolean.class, Tuple.create(
                        false
                        , false
                        , boolean.class
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> Byte.valueOf((byte)fromElement);
                put(byte.class, Tuple.create(
                        true
                        , (byte)0
                        , Byte.class
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> Byte.class.cast(fromElement).byteValue();
                put(Byte.class, Tuple.create(
                        false
                        , (byte)0
                        , byte.class
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> Character.valueOf((char)fromElement);
                put(char.class, Tuple.create(
                        true
                        , (char)0
                        , Character.class
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> Character.class.cast(fromElement).charValue();
                put(Character.class, Tuple.create(
                        false
                        , (char)0
                        , char.class
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> Double.valueOf((double)fromElement);
                put(double.class, Tuple.create(
                        true
                        , 0d
                        , Double.class
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> Double.class.cast(fromElement).doubleValue();
                put(Double.class, Tuple.create(
                        false
                        , 0d
                        , double.class
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> Float.valueOf((float)fromElement);
                put(float.class, Tuple.create(
                        true
                        , 0f
                        , Float.class
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> Float.class.cast(fromElement).floatValue();
                put(Float.class, Tuple.create(
                        false
                        , 0f
                        , float.class
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> Integer.valueOf((int)fromElement);
                put(int.class, Tuple.create(
                        true
                        , 0
                        , Integer.class
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> Integer.class.cast(fromElement).intValue();
                put(Integer.class, Tuple.create(
                        false
                        , 0
                        , int.class
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> Long.valueOf((long)fromElement);
                put(long.class, Tuple.create(
                        true
                        , 0L
                        , Long.class
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> Long.class.cast(fromElement).longValue();
                put(Long.class, Tuple.create(
                        false
                        , 0L
                        , long.class
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> Short.valueOf((short)fromElement);
                put(short.class, Tuple.create(
                        true
                        , (short)0
                        , Short.class
                        , convertWithCasting
                        , convertWithCasting));
                convertWithCasting = fromElement -> Short.class.cast(fromElement).shortValue();
                put(Short.class, Tuple.create(
                        false
                        , (short)0
                        , short.class
                        , convertWithCasting
                        , convertWithCasting));

            }},
            null,
            clazz -> {
                Boolean isArray = clazz.isArray();
                if(!isArray && !clazz.isPrimitive())
                    return Tuple.create(false, null, null, returnsSelf, returnsSelf);

                Class componentClass = clazz.getComponentType();
                Boolean isPrimitive = isPrimitive(componentClass);

                Object defaultValue = EMPTY_ARRAY_AS_DEFAULT ? ArrayHelper.getNewArray(componentClass, 0) : null;
                Class equivalentComponentClass = getEquivalentClass(componentClass);
                Class equivalentClass = classOperators.getThirdValue(equivalentComponentClass);
                Function<Object, Object> componentParallelConverter = getToEquivalentParallelConverter(componentClass);
                Function<Object, Object> componentSerialConverter = getToEquivalentSerialConverter(componentClass);
                BiFunctionThrowable<Object, Integer, Object> componentGetter = getArrayElementGetter(componentClass);
                TriConsumerThrowable<Object, Integer, Object> equivalentSetter = getArrayElementSetter(equivalentComponentClass);

                TriFunctionThrowable.TriFunction<Object, Object, Integer, Boolean> getExceptionWhileMapping =
                        getExceptionWithMapping(componentGetter, equivalentSetter, componentParallelConverter);

                Function<Object, Object> parallelConverter = equivalentClass == null ? returnsSelf
                        : fromArray -> {
                    if(fromArray == null) return null;

                    int length = Array.getLength(fromArray);
                    Object toArray = ArrayHelper.getNewArray(equivalentComponentClass, length);
                    Optional<Integer> firstExceptionIndex = IntStream.range(0, length).boxed().parallel()
                            .filter(i -> getExceptionWhileMapping.apply(fromArray, toArray, i)).findFirst();
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
                            Object fromElement = componentGetter.apply(fromArray, i);
                            //Get the converted element
                            Object toElement = componentSerialConverter.apply(fromElement);
                            //Set the converted value
                            equivalentSetter.accept(toArray, i, toElement);
                        }
                        return toArray;
                    }catch (Exception ex){
                        return null;
                    }
                };

                return Tuple.create(isPrimitive, defaultValue, equivalentClass, parallelConverter, serialConverter);
            }
    );

    /**
     * Evaluate if the concerned class represents a primitive type or an array of primitive type elements
     * @param clazz Class to be evaluated
     * @return  true if and only if this class represents a primitive type or an array of primitive type elements
     */
    public static Boolean isPrimitive(Class clazz){
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
        return baseTypeConverters.getFourthValue(clazz);
    }

    public static Function<Object, Object> getToEquivalentSerialConverter(Class clazz){
        return baseTypeConverters.getFifthValue(clazz);
    }

    public static int getDimension(Class clazz){
        Objects.requireNonNull(clazz);
        char[] chars = clazz.toString().toCharArray();
        int count = 0;
        for (int i = 0; i < chars.length; i++) {
            if(chars[i] == '[')
                count ++;
        }
        return count;
    }

    private static TriFunctionThrowable.TriFunction<Object, Object, Integer, Boolean> getExceptionWithMapping(
            BiFunctionThrowable<Object, Integer, Object> fromElementGetter
            , TriConsumerThrowable<Object, Integer, Object> toElementSetter
            , Function<Object, Object> elementConverter){
        if(elementConverter == returnsSelf || elementConverter == null)
            return (fromArray, toArray, index) -> {
                    try {
                        Object fromElement = fromElementGetter.apply(fromArray, index);
                        toElementSetter.accept(toArray, index, fromElement);
                        return false;
                    } catch (Exception ex) {
                        return true;
                    }
                };
        else
            return (fromArray, toArray, index) -> {
                try {
                    Object fromElement = fromElementGetter.apply(fromArray, index);
                    Object convertedElement = elementConverter.apply(fromElement);
                    toElementSetter.accept(toArray, index, convertedElement);
                    return false;
                } catch (Exception ex) {
                    return true;
                }
            };
    }

    protected static Function<Object, Object> getMapper(Class fromClass, Class toClass, Boolean parallel){
        Objects.requireNonNull(fromClass);
        Objects.requireNonNull(toClass);

        //First, check fromClass and toClass is identical
        if(fromClass == toClass)
            // No mapping needed, thus returnSelf is enough
            return returnsSelf;
        //Second, check to see if fromClass is equivalent to toClass
        else if(getClassPredicate(fromClass).test(toClass))
            // Use the equivalent converter if they are
            return getToEquivalentParallelConverter(fromClass);

        boolean isFromArray = fromClass.isArray();
        boolean isToArray = toClass.isArray();
        //Then, since fromClass and toClass are not equivalent, check if both of them are not array class
        if(!isFromArray && !isToArray){
            //Third, cast the fromObj to toClass object by force, that would throw Exceptions when their types are not match
            return toClass::cast;
        } else if( Boolean.logicalXor(isFromArray, isToArray))
            //Fourth, when one type is Array, another is not, mapping is not possible, return mapsToNull directly
            return mapsToNull;

        //Now both fromClass and toClass are of Array
        Class fromComponentClass = fromClass.getComponentType();
        Class toComponentClass = toClass.getComponentType();
        Function<Object, Object> elementConverter = getMapper(fromComponentClass, toComponentClass, parallel);
        //If element of the fromArray cannot be mapped to toArray, their Arrays cannot be mapped either, return mapsToNull
        if(elementConverter == mapsToNull)
            return mapsToNull;

        boolean isFromComponentArray = fromComponentClass.isArray();
        boolean isToComponentArray = toComponentClass.isArray();

        Function<Integer, Object> factory = TypeHelper.getArrayFactory(toComponentClass).orElse(null);
        BiFunctionThrowable<Object, Integer, Object> fromElementGetter = getArrayElementGetter(fromComponentClass);
        TriConsumerThrowable<Object, Integer, Object> toElementSetter = getArrayElementSetter(toComponentClass);
        FunctionThrowable<Object, Object> mapper;

        if(parallel == null) {

        } else if (parallel){
            TriFunctionThrowable.TriFunction<Object, Object, Integer, Boolean> elementMappingWithException =
                    getExceptionWithMapping(fromElementGetter, toElementSetter, elementConverter);
            mapper = (fromArray) -> {
                int length = Array.getLength(fromArray);
                Object toArray = factory.apply(length);
                Integer firstIndexWithException = IntStream.range(0, length).boxed().parallel()
                        .filter(i -> elementMappingWithException.apply(fromArray, toArray, i))
                        .findFirst().orElse(-1);

                return firstIndexWithException == -1 ? toArray : null;
            };
        } else {
            if(elementConverter == returnsSelf){
                mapper = (fromArray) -> {
                    int length = Array.getLength(fromArray);
                    Object toArray = factory.apply(length);
                    for (int i = 0; i < length; i++) {
                        toElementSetter.accept(toArray, i, fromElementGetter.apply(fromArray, i));
                    }
                    return toArray;
                };
            } else if(!isFromComponentArray && !isToComponentArray){
                //The component converter must be Class.cast()
                mapper = (fromArray) -> {
                    int length = Array.getLength(fromArray);
                    Object toArray = factory.apply(length);
                    for (int i = 0; i < length; i++) {
                        toElementSetter.accept(toArray, i, toComponentClass.cast(fromElementGetter.apply(fromArray, i)));
                    }
                    return toArray;
                };
            } else {
                mapper = (fromArray) -> {
                    int length = Array.getLength(fromArray);
                    Object toArray = factory.apply(length);
                    for (int i = 0; i < length; i++) {
                        Object fromElement = fromElementGetter.apply(fromArray, i);
                        Object convertedElement = elementConverter.apply(fromElement);
                        toElementSetter.accept(toArray, i, convertedElement);
                    }
                    return toArray;
                };
            }
        }

        mapper = (fromArray) -> {
            int length = Array.getLength(fromArray);
            Object toArray = factory.apply(length);
            for (int i = 0; i < length; i++) {
                toElementSetter.accept(toArray, i, toComponentClass.cast(fromElementGetter.apply(fromArray, i)));
            }
            return toArray;
        };
        return mapper.orElse(null);
    }

    public static final QuadValuesRepository.DualKeys<
            Class       //fromClass
            ,Class      //toClass

            , Function<Object, Object>          //Convert the first object to another parallelly
            , Function<Object, Object>          //Convert the first object to another serially
            , BiPredicate<Object,Object>        //Predicate of deepEquals in parallel
            , BiPredicate<Object,Object>        //Predicate of deepEquals in serial
        > deepEvaluators = QuadValuesRepository.fromTwoKeys(
            (Class fromClass, Class toClass) ->{
                Objects.requireNonNull(fromClass);
                Objects.requireNonNull(toClass);

                BiPredicate<Object,Object> parallelPredicate, serialPredicate;

                if(!fromClass.isArray() && !toClass.isArray()){
                    if(fromClass.equals(toClass)){
                        parallelPredicate = serialPredicate = Objects::equals;
                        return Tuple.create(returnsSelf, returnsSelf, parallelPredicate, serialPredicate);
                    } else if (getEquivalentClass(fromClass).equals(toClass)){
                        final Function<Object, Object> singleObjectConverter = getToEquivalentParallelConverter(fromClass);
                        parallelPredicate = serialPredicate = (a, b) -> Objects.equals(singleObjectConverter.apply(a), b);
                        return Tuple.create(singleObjectConverter, singleObjectConverter, parallelPredicate, serialPredicate);
                    } else if (toClass.isAssignableFrom(fromClass)){
                        parallelPredicate = serialPredicate = Object::equals;
                        return Tuple.create(returnsSelf, returnsSelf, parallelPredicate, serialPredicate);
                    } else {
                        return Tuple.create(mapsToNull, mapsToNull, alwaysFalse, alwaysFalse);
                    }
                } else if(toClass.equals(Object.class)) {
                    return Tuple.create(returnsSelf, returnsSelf, alwaysFalse, alwaysFalse);
                } else if(!fromClass.isArray() || !toClass.isArray()){
                    //One type is array, another is not
                    return Tuple.create(mapsToNull, mapsToNull, alwaysFalse, alwaysFalse);
                }

                //Now both type are of array
                //First, check fromClass and toClass is identical
                if(fromClass.equals(toClass)){
                    BiFunctionThrowable<Object, Integer, Object> getter1 = TypeHelper.getArrayElementGetter(fromClass.getComponentType());
                    parallelPredicate = (array1, array2) -> deepEqualsParallel(getter1, getter1, array1, array2);
                    serialPredicate = (array1, array2) -> deepEqualsSerial(getter1, getter1, array1, array2);
                    return Tuple.create(returnsSelf, returnsSelf, parallelPredicate, serialPredicate);
                }else if(!getClassPredicate(fromClass).test(toClass)) {
                    //Second, check to see if fromClass is equivalent to toClass
                    // Use the equivalent converter if they are
                    return Tuple.create(mapsToNull, mapsToNull, alwaysFalse, alwaysFalse);
                }

                Class fromComponentClass = fromClass.getComponentType();
                Class toComponentClass = toClass.getComponentType();
                boolean isFromComponentArray = fromComponentClass.isArray();
                boolean isToComponentArray = toComponentClass.isArray();

                FunctionThrowable<Integer, Object> factory = TypeHelper.getArrayFactory(toComponentClass);
                BiFunctionThrowable<Object, Integer, Object> fromElementGetter = getArrayElementGetter(fromComponentClass);
                TriConsumerThrowable<Object, Integer, Object> toElementSetter = getArrayElementSetter(toComponentClass);
                Function<Object, Object> parallelElementConverter = getToEquivalentParallelConverter(fromComponentClass);
                Function<Object, Object> serialElementConverter = getToEquivalentSerialConverter(fromComponentClass);

                TriFunctionThrowable.TriFunction<Object, Object, Integer, Boolean> elementMappingWithException =
                        getExceptionWithMapping(fromElementGetter, toElementSetter, parallelElementConverter);
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
                            Object fromElement = fromElementGetter.apply(fromArray, i);
                            Object convertedElement = serialElementConverter.apply(fromElement);
                            toElementSetter.accept(toArray, i, convertedElement);
                        }
                        return toArray;
                    } catch (Exception ex) {
                        return null;
                    }
                };

                BiFunctionThrowable<Object, Integer, Object> toElementGetter = getArrayElementGetter(toComponentClass);
                parallelPredicate = (array1, array2) -> deepEqualsParallel(fromElementGetter, toElementGetter, array1, array2);
                serialPredicate = (array1, array2) -> deepEqualsSerial(fromElementGetter, toElementGetter, array1, array2);
                return Tuple.create(parallelConverter, serialConverter, parallelPredicate, serialPredicate);
            }
        );

    private static boolean deepEqualsSerial(
            BiFunctionThrowable<Object, Integer, Object> getter1,
            BiFunctionThrowable<Object, Integer, Object> getter2,
            Object array1,
            Object array2){
        if(Objects.equals(array1, array2))
            return true;
        else if(array1 == null || array2 == null)
            return false;

        int length = Array.getLength(array1);
        if(length != Array.getLength(array2))
            return false;

        try {
            for (int i = 0; i < length; i++) {
                Object element1 = getter1.apply(array1, i);
                Object element2 = getter2.apply(array2, i);
                if(!deepEquals(element1, element2))
                    return false;
            }
            return true;
        }catch(Exception ex){
            return false;
        }
    }

    private static boolean deepEqualsParallel(
            BiFunctionThrowable<Object, Integer, Object> getter1,
            BiFunctionThrowable<Object, Integer, Object> getter2,
            Object array1,
            Object array2){
        if(Objects.equals(array1, array2))
            return true;
        else if(array1 == null || array2 == null)
            return false;

        int length = Array.getLength(array1);
        if(length != Array.getLength(array2))
            return false;

        Predicate<Integer> elementUnmatchedPredicate = i -> {
            try {
                Object element1 = getter1.apply(array1, i);
                Object element2 = getter2.apply(array2, i);
                return !deepEquals(element1, element2);
            }catch(Exception ex){
                return true;
            }
        };
        boolean result = !IntStream.range(0, length).boxed().parallel()
                .filter(elementUnmatchedPredicate).findFirst().isPresent();
        return result;
    }

    public static boolean deepEquals(Object obj1, Object obj2){
        if(Objects.equals(obj1, obj2))
            return true;
        else if(obj1 == null || obj2 == null)
            return false;

        Class class1 = obj1.getClass();
        Class class2 = obj2.getClass();
        if( !TypeHelper.getClassPredicate(class1).test(class2))
            return false;

        if (!class1.isArray()) {
            if(class1.isPrimitive())
                return Objects.equals(obj1, getToEquivalentSerialConverter(class2).apply(obj2));
            else if(class2.isPrimitive())
                return Objects.equals(obj2, getToEquivalentSerialConverter(class1).apply(obj1));
            else
                return false;
        }

        int length = Array.getLength(obj1);
        if(length != Array.getLength(obj2))
            return false;

        BiFunctionThrowable<Object, Integer, Object> getter1 = TypeHelper.getArrayElementGetter(class1.getComponentType());
        BiFunctionThrowable<Object, Integer, Object> getter2 = TypeHelper.getArrayElementGetter(class2.getComponentType());

        if(length < PARALLEL_EVALUATION_THRESHOLD){
            return deepEqualsSerial(getter1, getter2, obj1, obj2);
        } else {
            return deepEqualsParallel(getter1, getter2, obj1, obj2);
        }
    }

}
