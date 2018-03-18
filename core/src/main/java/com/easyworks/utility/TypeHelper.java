package com.easyworks.utility;

import com.easyworks.Functions;
import com.easyworks.function.BiFunctionThrowable;
import com.easyworks.function.PredicateThrowable;
import com.easyworks.function.TriConsumerThrowable;
import com.easyworks.function.TriFunctionThrowable;
import com.easyworks.repository.HeptaValuesRepository;
import com.easyworks.repository.HexaValuesRepository;
import com.easyworks.tuple.Hepta;
import com.easyworks.tuple.Hexa;
import com.easyworks.tuple.Tuple;
import sun.reflect.ConstantPool;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class TypeHelper {
    public static final Class ObjectClass = Object.class;
    public static final String LAMBDA_NAME_KEY = "$Lambda";

    private static final <T> T[] asCopyOfRange(Object array, int from, int to){
        return Arrays.copyOfRange((T[])array, from , to);
    }

    private static final String objectsToString(Object array){
        Object[] objects = ArrayHelper.asObjects(array);
        return Arrays.toString(objects);
    }

    public static final HeptaValuesRepository<
            Class,              //concerned Class

            Class             //Class of the corresponding wrapper
            , Predicate<Class> //Equivalent predicate
            , Class              //Class of its array
            , BiFunctionThrowable<Object, Integer, Object>//Function to get the Element as index of its array
            , TriConsumerThrowable<Object, Integer, Object>//Function to set the Element at Index of its array
            , TriFunctionThrowable<Object, Integer, Integer, Object>//copyOfRange(original, from, to) -> new array
            , Function<Object, String>             // Convert array to String
    > classOperators = HeptaValuesRepository.fromKey(
            () -> new HashMap<Class,
                    Hepta<
                        Class,
                        Predicate<Class>,
                        Class,
                        BiFunctionThrowable<Object, Integer, Object>,
                        TriConsumerThrowable<Object, Integer, Object>,
                        TriFunctionThrowable<Object, Integer, Integer, Object>,
                        Function<Object, String>
                    >
                >(){{
                    put(int.class, Tuple.create(
                            Integer.class
                            , clazz -> int.class.equals(clazz) || Integer.class.equals(clazz)
                            , int[].class
                            , (array, index) -> Array.getInt(array, index)
                            , (array, index, value) -> Array.setInt(array, index, (int)value)
                            , (array, from, to) -> Arrays.copyOfRange((int[])array, from, to)
                            , array -> Arrays.toString((int [])array)));
                    put(byte.class, Tuple.create(
                            Byte.class
                            , clazz -> byte.class.equals(clazz) || Byte.class.equals(clazz)
                            , byte[].class
                            , (array, index) -> Array.getByte(array, index)
                            , (array, index, value) -> Array.setByte(array, index, (byte)value)
                            , (array, from, to) -> Arrays.copyOfRange((byte[])array, from, to)
                            , array -> Arrays.toString((byte[])array)));
                    put(boolean.class, Tuple.create(
                            Boolean.class
                            , clazz -> boolean.class.equals(clazz) || Boolean.class.equals(clazz)
                            , boolean[].class
                            , (array, index) -> Array.getBoolean(array, index)
                            , (array, index, value) -> Array.setBoolean(array, index, (boolean)value)
                            , (array, from, to) -> Arrays.copyOfRange((boolean[])array, from, to)
                            , array -> Arrays.toString((boolean[])array)));
                    put(char.class, Tuple.create(
                            Character.class
                            , clazz -> char.class.equals(clazz) || Character.class.equals(clazz)
                            , char[].class
                            , (array, index) -> Array.getChar(array, index)
                            , (array, index, value) -> Array.setChar(array, index, (char)value)
                            , (array, from, to) -> Arrays.copyOfRange((char[])array, from, to)
                            , array -> Arrays.toString((char[])array)));
                    put(short.class, Tuple.create(
                            Short.class
                            , clazz -> short.class.equals(clazz) || Short.class.equals(clazz)
                            , short[].class
                            , (array, index) -> Array.getShort(array, index)
                            , (array, index, value) -> Array.setShort(array, index, (short)value)
                            , (array, from, to) -> Arrays.copyOfRange((short[])array, from, to)
                            , array -> Arrays.toString((short[])array)));
                    put(long.class, Tuple.create(
                            Long.class
                            , clazz -> long.class.equals(clazz) || Long.class.equals(clazz)
                            , long[].class
                            , (array, index) -> Array.getByte(array, index)
                            , (array, index, value) -> Array.setLong(array, index, (long)value)
                            , (array, from, to) -> Arrays.copyOfRange((long[])array, from, to)
                            , array -> Arrays.toString((long[])array)));
                    put(double.class, Tuple.create(
                            Double.class
                            , clazz -> double.class.equals(clazz) || Double.class.equals(clazz)
                            , double[].class
                            , (array, index) -> Array.getDouble(array, index)
                            , (array, index, value) -> Array.setDouble(array, index, (double)value)
                            , (array, from, to) -> Arrays.copyOfRange((double[])array, from, to)
                            , array -> Arrays.toString((double[])array)));
                    put(float.class, Tuple.create(
                            Float.class
                            , clazz -> float.class.equals(clazz) || Float.class.equals(clazz)
                            , float[].class
                            , (array, index) -> Array.getFloat(array, index)
                            , (array, index, value) -> Array.setFloat(array, index, (float)value)
                            , (array, from, to) -> Arrays.copyOfRange((float[])array, from, to)
                            , array -> Arrays.toString((float[])array)));

            }},
            null,
            clazz -> {
                Class wrapper = null;
                Predicate<Class> cPredicate;

                if(!clazz.isArray()){
                    cPredicate = otherClass -> otherClass != null && otherClass.isAssignableFrom(clazz);
                } else {
                    Class componentClass = clazz.getComponentType();
                    Predicate<Class> componentPredicate = getClassPredicate(componentClass);
                    cPredicate = otherClass -> otherClass != null && otherClass.isArray()
                            && componentPredicate.test(otherClass.getComponentType());
                }

                Class arrayClass = ArrayHelper.getNewArray(clazz, 0).getClass();
                BiFunctionThrowable<Object, Integer, Object> getElement = Array::get;
                TriConsumerThrowable<Object, Integer, Object> setElement = Array::set;
                TriFunctionThrowable<Object, Integer, Integer, Object> copyOfRange = TypeHelper::asCopyOfRange;
                Function<Object, String> toString = TypeHelper::objectsToString;
                return Tuple.create(wrapper, cPredicate, arrayClass, getElement, setElement, copyOfRange, toString);
            }
    );

    public static final Predicate<Class> getClassPredicate(Class clazz){
        if(clazz == null) return null;

        return classOperators.getSecondValue(clazz);
    }

    public static final Map<Class, PredicateThrowable<Class>> classPredicates = new HashMap<Class, PredicateThrowable<Class>>(){{
        put(Boolean.class, clazz -> Boolean.class.equals(clazz) || boolean.class.equals(clazz));
        put(boolean.class, clazz -> Boolean.class.equals(clazz) || boolean.class.equals(clazz));
        put(Byte.class, clazz -> Byte.class.equals(clazz) || byte.class.equals(clazz));
        put(byte.class, clazz -> Byte.class.equals(clazz) || byte.class.equals(clazz));
        put(Short.class, clazz -> Short.class.equals(clazz) || short.class.equals(clazz));
        put(short.class, clazz -> short.class.equals(clazz) || Short.class.equals(clazz));
        put(Integer.class, clazz -> Integer.class.equals(clazz) || int.class.equals(clazz));
        put(int.class, clazz -> int.class.equals(clazz) || Integer.class.equals(clazz));
        put(Long.class, clazz -> Long.class.equals(clazz) || long.class.equals(clazz));
        put(long.class, clazz -> long.class.equals(clazz) || Long.class.equals(clazz));
        put(Float.class, clazz -> Float.class.equals(clazz) || float.class.equals(clazz));
        put(float.class, clazz -> float.class.equals(clazz) || Float.class.equals(clazz));
        put(Double.class, clazz -> Double.class.equals(clazz) || double.class.equals(clazz));
        put(double.class, clazz -> double.class.equals(clazz) || Double.class.equals(clazz));
        put(char.class, clazz -> char.class.equals(clazz) || Character.class.equals(clazz));
        put(Character.class, clazz -> Character.class.equals(clazz) || char.class.equals(clazz));

        put(Object[].class, clazz -> Object[].class.equals(clazz));
        put(Boolean[].class, clazz -> Boolean[].class.equals(clazz) || boolean[].class.equals(clazz));
        put(boolean[].class, clazz -> Boolean[].class.equals(clazz) || boolean[].class.equals(clazz));
        put(Byte[].class, clazz -> Byte[].class.equals(clazz) || byte[].class.equals(clazz));
        put(byte[].class, clazz -> Byte[].class.equals(clazz) || byte[].class.equals(clazz));
        put(Short[].class, clazz -> Short[].class.equals(clazz) || short[].class.equals(clazz));
        put(short[].class, clazz -> short[].class.equals(clazz) || Short[].class.equals(clazz));
        put(Integer[].class, clazz -> Integer[].class.equals(clazz) || int[].class.equals(clazz));
        put(int[].class, clazz -> int[].class.equals(clazz) || Integer[].class.equals(clazz));
        put(Long[].class, clazz -> Long[].class.equals(clazz) || long[].class.equals(clazz));
        put(long[].class, clazz -> long[].class.equals(clazz) || Long[].class.equals(clazz));
        put(Float[].class, clazz -> Float[].class.equals(clazz) || float[].class.equals(clazz));
        put(float[].class, clazz -> float[].class.equals(clazz) || Float[].class.equals(clazz));
        put(Double[].class, clazz -> Double[].class.equals(clazz) || double[].class.equals(clazz));
        put(double[].class, clazz -> double[].class.equals(clazz) || Double[].class.equals(clazz));
        put(char[].class, clazz -> char[].class.equals(clazz) || Character[].class.equals(clazz));
        put(Character[].class, clazz -> Character[].class.equals(clazz) || char[].class.equals(clazz));
    }};

    protected static final PredicateThrowable<Class> alwaysFalse = c -> false;

    private static final Method _getConstantPool = (Method) Functions.ReturnsDefaultValue.apply(() -> {
        Method method = Class.class.getDeclaredMethod("getConstantPool");
        method.setAccessible(true);
        return method;
    });

    public static ConstantPool getConstantPoolOfClass(Class objectClass){
        return (ConstantPool) Functions.ReturnsDefaultValue.apply(()->_getConstantPool.invoke(objectClass));
    }


    /**
     * @param instanceClass Type to be evaludated
     * @return
     */
//    public static Predicate<Class> getClassPredicate(Class instanceClass){
//        if(instanceClass == null) return c -> false;
//        if (!classPredicates.containsKey(instanceClass)){
//            classPredicates.put(instanceClass,clazz -> instanceClass.isAssignableFrom(clazz));
//        }
//        return classPredicates.get(instanceClass).orElse(false);
//    }

    public static <T> Predicate getClassPredicate(T... instances){
        Class<T> instanceClass = getDeclaredType(instances);
        return getClassPredicate(instanceClass);
    }

    public static <T> Class<T> getDeclaredType(T... instances){
        Objects.requireNonNull(instances);
        Class arrayClass = instances.getClass();
        Class componentType = (Class<T>) arrayClass.getComponentType();
        if(ObjectClass == componentType && 1 == instances.length && instances[0] != null)
            return (Class<T>) instances[0].getClass();
        return componentType;
    }

//    public static <T> Type getGenericType(T... instances){
//        Class<T> instanceClass = getDeclaredType(instances);
//        return getGenericTypeFromType(instanceClass);
//    }
//
//    public static Type getGenericTypeFromType(Type instanceType){
//        if(instanceType instanceof ParameterizedType)
//            return ((ParameterizedType) instanceType).getRawType();
//
//        Class<?> instanceClass = (Class<?>) instanceType;
//        if(ObjectClass.equals(instanceClass) || instanceClass.isPrimitive())
//            return null;
//        Type[] typeParameters = instanceClass.getTypeParameters();
//        if(typeParameters != null && typeParameters.length>0)
//            return instanceClass;
//
//        Type superType = instanceClass.getGenericSuperclass();
//        return superType == null ? null : getGenericTypeFromType(superType);
//    }
//
//    public static <T> Tuple getGenericInfo(T... instances){
//        Class<T> instanceClass = getDeclaredType(instances);
//        if(instances.length == 1 || instances[0] != null)
//            return getGenericInfoFromTypeAndInstance(instances[0], instanceClass);
//
//        return getGenericInfoFromType(instanceClass);
//    }
//
//    public static <T> Tuple getGenericInfoFromTypeAndInstance(T instance, Class<T> instanceClass){
//
//        return getGenericInfoFromInstance(instance, instanceClass, instanceClass);
//    }
//
//    public static Tuple getGenericInfoFromType(Type instanceType) {
//        return getGenericInfoFromType(instanceType, instanceType);
//    }
//
//    public static Tuple getGenericInfoFromType(Type instanceType, Type originalType){
//        //Define Triple with types explicitly to keep Type information
//        Quad<Class, Type[], String, String> typeInfo = null;
//        Type[] typeParameters;
//        Type[] typedArguments;
//        if(instanceType instanceof ParameterizedType){
//            ParameterizedType parameterizedType = ((ParameterizedType) instanceType);
//            Type rawType = parameterizedType.getRawType();
//            Class rawClass = (Class) rawType;
//            typeParameters = rawClass.getTypeParameters();
//            typedArguments = parameterizedType.getActualTypeArguments();
//
//            String token = String.format("%s<%s>", rawClass.getName(),
//                    Arrays.stream(typedArguments).map(t -> t.getTypeName()).collect(Collectors.joining(",")));
//            String tokenSimple = String.format("%s<%s>", rawClass.getSimpleName(),
//                    Arrays.stream(typedArguments).map(t -> ((Class)t).getSimpleName()).collect(Collectors.joining(",")));
//            typeInfo = Tuple.create(rawClass, typedArguments, token, tokenSimple);
//            return typeInfo;
//        }
//
//        Class<?> instanceClass = (Class<?>) instanceType;
//        if(ObjectClass.equals(instanceClass) || instanceClass.isPrimitive()) {
//            Class originalClass = ((Class)originalType);
//            typeInfo = Tuple.create(null, null, originalClass.getName(), originalClass.getSimpleName());
//            return typeInfo;
//        }
//        typeParameters = instanceClass.getTypeParameters();
//        if(typeParameters != null && typeParameters.length>0) {
//            String token = String.format("%s<%s>", instanceClass.getName(),
//                    Arrays.stream(typeParameters).map(t -> t.getTypeName()).collect(Collectors.joining(",")));
//            String tokenSimple = String.format("%s<%s>", instanceClass.getSimpleName(),
//                    Arrays.stream(typeParameters).map(t -> t.toString()).collect(Collectors.joining(",")));
//            typeInfo = Tuple.create(instanceClass, typeParameters, token, tokenSimple);
//            return typeInfo;
//        }
//
//        Type superType = instanceClass.getGenericSuperclass();
//        return getGenericInfoFromType(superType, instanceType);
//    }
//
//    public static <T> Tuple getGenericInfoFromInstance(T instance, Class<T> declaredClass, Type instanceType){
//        //Define Triple with types explicitly to keep Type information
//        Quad<Class, Type[], String, String> typeInfo = null;
//        Type[] typeParameters;
//        Type[] typedArguments;
//        if(instanceType instanceof ParameterizedType){
//            ParameterizedType parameterizedType = ((ParameterizedType) instanceType);
//            Type rawType = parameterizedType.getRawType();
//            Class rawClass = (Class) rawType;
//            typeParameters = rawClass.getTypeParameters();
//            typedArguments = parameterizedType.getActualTypeArguments();
//
//            String token = String.format("%s<%s>", rawClass.getName(),
//                    Arrays.stream(typedArguments).map(t -> t.getTypeName()).collect(Collectors.joining(",")));
//            String tokenSimple = String.format("%s<%s>", rawClass.getSimpleName(),
//                    Arrays.stream(typedArguments).map(t -> ((Class)t).getSimpleName()).collect(Collectors.joining(",")));
//            typeInfo = Tuple.create(rawClass, typedArguments, token, tokenSimple);
//            return typeInfo;
//        }
//
//        Class<?> instanceClass = (Class<?>) instanceType;
//        if(ObjectClass.equals(instanceClass) || instanceClass.isPrimitive()) {
//            typeInfo = Tuple.create(null, null, declaredClass.getName(), declaredClass.getSimpleName());
//            return typeInfo;
//        }
//
//        typeParameters = instanceClass.getTypeParameters();
//        int paraLength = typeParameters.length;
//        if(typeParameters != null && paraLength>0) {
//            Method[] methods = declaredClass.getMethods();
//            Method functionalInterfaceMethod = Arrays.stream(methods)
//                    .filter(m -> !m.isDefault() && !m.isBridge() && !Modifier.isStatic(m.getModifiers()))
//                    .findFirst().orElse(null);
//            Type returnType = functionalInterfaceMethod.getGenericReturnType();
//            Type[] paramTypes = functionalInterfaceMethod.getGenericParameterTypes();
//
//            try {
//                ConstantPool constantPool = (ConstantPool) _getConstantPool.invoke(instance.getClass());
//                String lambdaString = getLambdaTypeString(constantPool);
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
//
//            typedArguments = new Type[paraLength];
//            for (int i = 0; i < typeParameters.length; i++) {
//            }
//
//            String token = String.format("%s<%s>", instanceClass.getName(),
//                    Arrays.stream(typeParameters).map(t -> t.getTypeName()).collect(Collectors.joining(",")));
//            String tokenSimple = String.format("%s<%s>", instanceClass.getSimpleName(),
//                    Arrays.stream(typeParameters).map(t -> t.toString()).collect(Collectors.joining(",")));
//            typeInfo = Tuple.create(instanceClass, typeParameters, token, tokenSimple);
//            return typeInfo;
//        }
//
//        Type superType = instanceClass.getGenericSuperclass();
//        return getGenericInfoFromType(superType, instanceType);
//    }
//
//    private static String getLambdaTypeString(ConstantPool constantPool) {
//        int size = constantPool.getSize();
//        String[] memberRef = null;
//
//        // find last element in constantPool with valid memberRef
//        // - previously always at size-2 index but changed with JDK 1.8.0_60
//        for (int i = size - 1; i > -1; i--) {
//            try {
//                memberRef = constantPool.getMemberRefInfoAt(i);
//
//                return memberRef[2];
//            } catch (IllegalArgumentException e) {
//                // eat error; null entry at ConstantPool index?
//            }
//        }
//        throw new RuntimeException("Couldn't find memberRef.");
//    }
//
//    private static void extractTypeArguments(Map<Type, Type> typeMap, Class<?> clazz) {
//        Type genericSuperclass = clazz.getGenericSuperclass();
//        if (!(genericSuperclass instanceof ParameterizedType)) {
//            return;
//        }
//
//        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
//        Type[] typeParameter = ((Class<?>) parameterizedType.getRawType()).getTypeParameters();
//        Type[] actualTypeArgument = parameterizedType.getActualTypeArguments();
//        for (int i = 0; i < typeParameter.length; i++) {
//            if(typeMap.containsKey(actualTypeArgument[i])) {
//                actualTypeArgument[i] = typeMap.get(actualTypeArgument[i]);
//            }
//            typeMap.put(typeParameter[i], actualTypeArgument[i]);
//        }
//    }
//
//    public static Class<?> findSubClassParameterType(Object instance, Class<?> classOfInterest, int parameterIndex) {
//        Map<Type, Type> typeMap = new HashMap<Type, Type>();
//        Class<?> instanceClass = instance.getClass();
//        ConstantPool constantPool = getConstantPoolOfClass(instanceClass);
//        Map<Integer, String[]> members = new HashMap();
//        String[] member = null;
//        int index = constantPool.getSize();
//        while(--index >=0) {
//            try {
//                member = constantPool.getMemberRefInfoAt(index);
//                members.put(index, member);
////                break;
//            } catch (Exception ex){
//                continue;
//            }
//        }
//
//
//        while (classOfInterest != instanceClass.getSuperclass()) {
//            extractTypeArguments(typeMap, instanceClass);
//            instanceClass = instanceClass.getSuperclass();
//            if (instanceClass == null) throw new IllegalArgumentException();
//        }
//
//        ParameterizedType parameterizedType = (ParameterizedType) instanceClass.getGenericSuperclass();
//        Type actualType = parameterizedType.getActualTypeArguments()[parameterIndex];
//        if (typeMap.containsKey(actualType)) {
//            actualType = typeMap.get(actualType);
//        }
//
//        if (actualType instanceof Class) {
//            return (Class<?>) actualType;
//        } else if (actualType instanceof TypeVariable) {
//            return browseNestedTypes(instance, (TypeVariable<?>) actualType);
//        } else {
//            throw new IllegalArgumentException();
//        }
//    }
//
//    private static Class<?> browseNestedTypes(Object instance, TypeVariable<?> actualType) {
//        Class<?> instanceClass = instance.getClass();
//        List<Class<?>> nestedOuterTypes = new LinkedList<Class<?>>();
//        for (
//                Class<?> enclosingClass = instanceClass.getEnclosingClass();
//                enclosingClass != null;
//                enclosingClass = enclosingClass.getEnclosingClass()) {
//            try {
//                Field this$0 = instanceClass.getDeclaredField("this$0");
//                Object outerInstance = this$0.get(instance);
//                Class<?> outerClass = outerInstance.getClass();
//                nestedOuterTypes.add(outerClass);
//                Map<Type, Type> outerTypeMap = new HashMap<Type, Type>();
//                extractTypeArguments(outerTypeMap, outerClass);
//                for (Map.Entry<Type, Type> entry : outerTypeMap.entrySet()) {
//                    if (!(entry.getKey() instanceof TypeVariable)) {
//                        continue;
//                    }
//                    TypeVariable<?> foundType = (TypeVariable<?>) entry.getKey();
//                    if (foundType.getName().equals(actualType.getName())
//                            && isInnerClass(foundType.getGenericDeclaration(), actualType.getGenericDeclaration())) {
//                        if (entry.getValue() instanceof Class) {
//                            return (Class<?>) entry.getValue();
//                        }
//                        actualType = (TypeVariable<?>) entry.getValue();
//                    }
//                }
//            } catch (NoSuchFieldException e) { /* this should never happen */ } catch (IllegalAccessException e) { /* this might happen */}
//
//        }
//        throw new IllegalArgumentException();
//    }
//
//    private static boolean isInnerClass(GenericDeclaration outerDeclaration, GenericDeclaration innerDeclaration) {
//        if (!(outerDeclaration instanceof Class) || !(innerDeclaration instanceof Class)) {
//            throw new IllegalArgumentException();
//        }
//        Class<?> outerClass = (Class<?>) outerDeclaration;
//        Class<?> innerClass = (Class<?>) innerDeclaration;
//        while ((innerClass = innerClass.getEnclosingClass()) != null) {
//            if (innerClass == outerClass) {
//                return true;
//            }
//        }
//        return false;
//    }
//
////    private static final QuadValuesRepository<Class, Class[], Class[], String[] Class[]> typeInfoRepository = MultiValuesRepository.toQuadValuesRepository(() ->{
////
////    })
//
//    public static Quad<Class[], Class[], String[], Class[]> parse(Object instance){
//        Objects.requireNonNull(instance);
//        Class instanceClass = instance.getClass();
//        TypeVariable[] typeParameters = instanceClass.getTypeParameters();
//
//        return null;
//    }
}
