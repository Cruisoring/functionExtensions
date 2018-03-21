package com.easyworks.utility;

import com.easyworks.Functions;
import com.easyworks.function.BiFunctionThrowable;
import com.easyworks.function.FunctionThrowable;
import com.easyworks.function.TriConsumerThrowable;
import com.easyworks.function.TriFunctionThrowable;
import com.easyworks.repository.HeptaValuesRepository;
import com.easyworks.repository.QuadValuesRepository;
import com.easyworks.tuple.Hepta;
import com.easyworks.tuple.Quad;
import com.easyworks.tuple.Tuple;
import sun.reflect.ConstantPool;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class TypeHelper {
    private static boolean EMPTY_ARRAY_AS_DEFAULT = true;

    static{
        if("false".equalsIgnoreCase(System.getProperty("EMPTY_ARRAY_AS_DEFAULT"))){
            EMPTY_ARRAY_AS_DEFAULT = true;
        }
    }

    private static final Method _getConstantPool = (Method) Functions.ReturnsDefaultValue.apply(() -> {
        Method method = Class.class.getDeclaredMethod("getConstantPool");
        method.setAccessible(true);
        return method;
    });

    public static ConstantPool getConstantPoolOfClass(Class objectClass){
        return (ConstantPool) Functions.ReturnsDefaultValue.apply(()->_getConstantPool.invoke(objectClass));
    }


    private static <T> TriFunctionThrowable<Object, Integer, Integer, Object> getCopier(Class<T> componentType){
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
                put(int.class, Tuple.create(
                            clazz -> int.class.equals(clazz) || Integer.class.equals(clazz)
                            , i -> new int[i]
                            , int[].class
                            , (array, index) -> Array.getInt(array, index)
                            , (array, index, value) -> Array.setInt(array, index, (int)value)
                            , (array, from, to) -> Arrays.copyOfRange((int[])array, from, to)
                            , array -> Arrays.toString((int [])array)));
                    put(Integer.class, Tuple.create(
                            clazz -> int.class.equals(clazz) || Integer.class.equals(clazz)
                            , i -> new Integer[i]
                            , Integer[].class
                            , Array::get
                            , Array::set
                            , (array, from, to) -> Arrays.copyOfRange((Integer[])array, from, to)
                            , array -> Arrays.toString((Integer[])array)));
                    put(byte.class, Tuple.create(
                            clazz -> byte.class.equals(clazz) || Byte.class.equals(clazz)
                            , i -> new byte[i]
                            , byte[].class
                            , (array, index) -> Array.getByte(array, index)
                            , (array, index, value) -> Array.setByte(array, index, (byte)value)
                            , (array, from, to) -> Arrays.copyOfRange((byte[])array, from, to)
                            , array -> Arrays.toString((byte[])array)));
                    put(Byte.class, Tuple.create(
                            clazz -> byte.class.equals(clazz) || Byte.class.equals(clazz)
                            , i -> new Byte[i]
                            , Byte[].class
                            , Array::get
                            , Array::set
                            , (array, from, to) -> Arrays.copyOfRange((Byte[])array, from, to)
                            , array -> Arrays.toString((Byte[])array)));
                    put(boolean.class, Tuple.create(
                            clazz -> boolean.class.equals(clazz) || Boolean.class.equals(clazz)
                            , i -> new boolean[i]
                            , boolean[].class
                            , (array, index) -> Array.getBoolean(array, index)
                            , (array, index, value) -> Array.setBoolean(array, index, (boolean)value)
                            , (array, from, to) -> Arrays.copyOfRange((boolean[])array, from, to)
                            , array -> Arrays.toString((boolean[])array)));
                    put(Boolean.class, Tuple.create(
                            clazz -> boolean.class.equals(clazz) || Boolean.class.equals(clazz)
                            , i -> new Boolean[i]
                            , Boolean[].class
                            , Array::get
                            , Array::set
                            , (array, from, to) -> Arrays.copyOfRange((Boolean[])array, from, to)
                            , array -> Arrays.toString((Boolean[])array)));
                    put(char.class, Tuple.create(
                            clazz -> char.class.equals(clazz) || Character.class.equals(clazz)
                            , i -> new char[i]
                            , char[].class
                            , (array, index) -> Array.getChar(array, index)
                            , (array, index, value) -> Array.setChar(array, index, (char)value)
                            , (array, from, to) -> Arrays.copyOfRange((char[])array, from, to)
                            , array -> Arrays.toString((char[])array)));
                    put(Character.class, Tuple.create(
                            clazz -> char.class.equals(clazz) || Character.class.equals(clazz)
                            , i -> new Character[i]
                            , Character[].class
                            , Array::get
                            , Array::set
                            , (array, from, to) -> Arrays.copyOfRange((Character[])array, from, to)
                            , array -> Arrays.toString((Character[])array)));
                    put(short.class, Tuple.create(
                            clazz -> short.class.equals(clazz) || Short.class.equals(clazz)
                            , i -> new short[i]
                            , short[].class
                            , (array, index) -> Array.getShort(array, index)
                            , (array, index, value) -> Array.setShort(array, index, (short)value)
                            , (array, from, to) -> Arrays.copyOfRange((short[])array, from, to)
                            , array -> Arrays.toString((short[])array)));
                    put(Short.class, Tuple.create(
                            clazz -> short.class.equals(clazz) || Short.class.equals(clazz)
                            , i -> new Short[i]
                            , Short[].class
                            , Array::get
                            , Array::set
                            , (array, from, to) -> Arrays.copyOfRange((Short[])array, from, to)
                            , array -> Arrays.toString((Short[])array)));
                    put(long.class, Tuple.create(
                            clazz -> long.class.equals(clazz) || Long.class.equals(clazz)
                            , i -> new long[i]
                            , long[].class
                            , (array, index) -> Array.getLong(array, index)
                            , (array, index, value) -> Array.setLong(array, index, (long)value)
                            , (array, from, to) -> Arrays.copyOfRange((long[])array, from, to)
                            , array -> Arrays.toString((long[])array)));
                    put(Long.class, Tuple.create(
                            clazz -> long.class.equals(clazz) || Long.class.equals(clazz)
                            , i -> new Long[i]
                            , Long[].class
                            , Array::get
                            , Array::set
                            , (array, from, to) -> Arrays.copyOfRange((Long[])array, from, to)
                            , array -> Arrays.toString((Long[])array)));
                    put(double.class, Tuple.create(
                            clazz -> double.class.equals(clazz) || Double.class.equals(clazz)
                            , i -> new double[i]
                            , double[].class
                            , (array, index) -> Array.getDouble(array, index)
                            , (array, index, value) -> Array.setDouble(array, index, (double)value)
                            , (array, from, to) -> Arrays.copyOfRange((double[])array, from, to)
                            , array -> Arrays.toString((double[])array)));
                    put(Double.class, Tuple.create(
                            clazz -> double.class.equals(clazz) || Double.class.equals(clazz)
                            , i -> new Double[i]
                            , double[].class
                            , Array::get
                            , Array::set
                            , (array, from, to) -> Arrays.copyOfRange((Double[])array, from, to)
                            , array -> Arrays.toString((Double[])array)));
                    put(float.class, Tuple.create(
                            clazz -> float.class.equals(clazz) || Float.class.equals(clazz)
                            , i -> new float[i]
                            , float[].class
                            , (array, index) -> Array.getFloat(array, index)
                            , (array, index, value) -> Array.setFloat(array, index, (float)value)
                            , (array, from, to) -> Arrays.copyOfRange((float[])array, from, to)
                            , array -> Arrays.toString((float[])array)));
                    put(Float.class, Tuple.create(
                            clazz -> float.class.equals(clazz) || Float.class.equals(clazz)
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
                TriFunctionThrowable<Object, Integer, Integer, Object> copyOfRange = getCopier(clazz);
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

    private static final QuadValuesRepository<
            Class,      // original Class of the concerned object

            Boolean     // isPrmitiveType, when the original class is primitive type, or it is array of primitive type
            , Object    // default value of the concerned class
            , Class     // equivalent class: could be the wrapper if original class is primitive,
            // or primitive type if original class is wrapper
            , Function<Object, Object>  // convert the value of original class to equivalent class
    > primitiveTypeConverters = QuadValuesRepository.fromKey(
            () -> new HashMap<Class, Quad<Boolean, Object, Class, Function<Object,Object>>>(){{
                put(boolean.class, Tuple.create(
                        true
                        , false
                        , Boolean.class
                        , b -> Boolean.valueOf((boolean)b)));
                put(Boolean.class, Tuple.create(
                        false
                        , false
                        , boolean.class
                        , b -> boolean.class.cast(b)));
                put(byte.class, Tuple.create(
                        true
                        , (byte)0
                        , Byte.class
                        , b -> Byte.valueOf((byte)b)));
                put(Byte.class, Tuple.create(
                        false
                        , (byte)0
                        , byte.class
                        , b -> byte.class.cast(b)));
                put(char.class, Tuple.create(
                        true
                        , (char)0
                        , Character.class
                        , c -> Character.valueOf((char)c)));
                put(Character.class, Tuple.create(
                        false
                        , (char)0
                        , char.class
                        , c -> char.class.cast(c)));
                put(double.class, Tuple.create(
                        true
                        , 0d
                        , Double.class
                        , d -> Double.valueOf((double)d)));
                put(Double.class, Tuple.create(
                        false
                        , 0d
                        , double.class
                        , d -> double.class.cast(d)));
                put(float.class, Tuple.create(
                        true
                        , 0f
                        , Float.class
                        , f -> Float.valueOf((float)f)));
                put(Float.class, Tuple.create(
                        false
                        , 0f
                        , float.class
                        , f -> float.class.cast(f)));
                put(int.class, Tuple.create(
                        true
                        , 0
                        , Integer.class
                        , i -> Integer.valueOf((int)i)));
                put(Integer.class, Tuple.create(
                        false
                        , 0
                        , int.class
                        , i -> int.class.cast(i)));
                put(long.class, Tuple.create(
                        true
                        , 0L
                        , Long.class
                        , l -> Long.valueOf((long)l)));
                put(Long.class, Tuple.create(
                        false
                        , 0L
                        , long.class
                        , l -> long.class.cast(l)));
                put(short.class, Tuple.create(
                        true
                        , (short)0
                        , Short.class
                        , s -> Short.valueOf((short)s)));
                put(Short.class, Tuple.create(
                        false
                        , (short)0
                        , Short.class
                        , s -> short.class.cast(s)));

            }},
            null,
            clazz -> {
                Boolean isArray = clazz.isArray();
                if(!isArray && !clazz.isPrimitive())
                    return Tuple.create(false, null, null, obj -> obj);

                Class componentClass = clazz.getComponentType();
                Boolean isPrimitive = isPrimitive(componentClass);

                Object defaultValue = EMPTY_ARRAY_AS_DEFAULT ? ArrayHelper.getNewArray(componentClass, 0) : null;
                Class equivalentComponentClass = getEquivalentClass(componentClass);
                Class equivalentClass = classOperators.getThirdValue(equivalentComponentClass);

                Function<Object, Object> converter = ArrayHelper.arrayConverters.getFirst(componentClass, equivalentComponentClass, null, null, null, null);
                Quad<Boolean, Object, Class, FunctionThrowable<Object, Object>> componentTuple = null;

                return Tuple.create(isPrimitive, defaultValue, equivalentClass, converter);
            }
    );

    public static Boolean isPrimitive(Class clazz){
        Boolean result =  primitiveTypeConverters.getFirstValue(clazz);
        return result == null ? false : result;
    }

    public static Object getDefaultValue(Class clazz){
        return primitiveTypeConverters.getSecondValue(clazz);
    }

    public static Class getEquivalentClass(Class clazz){
        return primitiveTypeConverters.getThirdValue(clazz);
    }

    public static Function<Object, Object> getToEquivalentConverter(Class clazz){
        return primitiveTypeConverters.getFourthValue(clazz);
    }
}
