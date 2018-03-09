package com.easyworks.utility;

import com.easyworks.Functions;
import com.easyworks.function.BiFunctionThrowable;
import com.easyworks.function.FunctionThrowable;
import com.easyworks.function.PredicateThrowable;
import com.easyworks.tuple.Quad;
import com.easyworks.tuple.Tuple;
import sun.reflect.ConstantPool;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

public class TypeHelper {
    public static final Class ObjectClass = Object.class;
    public static final String LAMBDA_NAME_KEY = "$Lambda";

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

    public static final Map<Class, Quad<Class, Class, FunctionThrowable<Object, Integer>, BiFunctionThrowable<Object, Integer, Object>>> valueArrayConverters
            = new HashMap<Class, Quad<Class, Class, FunctionThrowable<Object, Integer>, BiFunctionThrowable<Object, Integer, Object>>>(){
        {
            put(byte[].class, Tuple.create(byte.class, Byte.class, array -> ((byte[]) array).length,
                    (array, i) -> Byte.valueOf(((byte[]) array)[i])));
            put(boolean[].class, Tuple.create(boolean.class, Boolean.class, array -> ((boolean[]) array).length,
                    (array, i) -> Boolean.valueOf(((boolean[]) array)[i])));
            put(char[].class, Tuple.create(char.class, Character.class, array -> ((char[]) array).length,
                    (array, i) -> Character.valueOf(((char[]) array)[i])));
            put(float[].class, Tuple.create(float.class, Float.class, array -> ((float[]) array).length,
                    (array, i) -> Float.valueOf(((float[]) array)[i])));
            put(int[].class, Tuple.create(int.class, Integer.class, array -> ((int[]) array).length,
                    (array, i) -> Integer.valueOf(((int[]) array)[i])));
            put(double[].class, Tuple.create(double.class, Double.class, array -> ((double[]) array).length,
                    (array, i) -> Double.valueOf(((double[]) array)[i])));
            put(short[].class, Tuple.create(short.class, Short.class, array -> ((short[]) array).length,
                    (array, i) -> Short.valueOf(((short[]) array)[i])));
            put(long[].class, Tuple.create(long.class, Long.class, array -> ((long[]) array).length,
                    (array, i) -> Long.valueOf(((long[]) array)[i])));
        }
    };

    private static <V,O extends Comparator<O>> O[] asArray(V array){
        if(array == null)
            return null;

        Class arrayClass = array.getClass();
        Quad<Class, Class, FunctionThrowable<Object, Integer>, BiFunctionThrowable<Object, Integer, Object>> quad =
                valueArrayConverters.get(arrayClass);
        return (O[]) Functions.Default.apply(() -> toObjectArray(array, quad.getSecond(), quad.getThird().apply(array), quad.getFourth()));
    }

    private static <V,O> O[] toObjectArray(V array, Class<O> objectClass, int size, BiFunctionThrowable<V, Integer, O> getValueAt)
            throws Exception {
        if(array == null)
            return null;
        O[] result = (O[]) Array.newInstance(objectClass, size);
        for (int i = 0; i < size; i++) {
            result[i] = getValueAt.apply(array, i);
        }
        return result;
    }

    //*/

    public static Boolean[] toObjectArray(boolean[] array){
        return asArray(array);
    }

    public static Byte[] toObjectArray(byte[] array){
        return asArray(array);
    }

    public static Character[] toObjectArray(char[] array){
        return asArray(array);
    }

    public static Float[] toObjectArray(float[] array){
        return asArray(array);
    }

    public static Double[] toObjectArray(double[] array){
        return asArray(array);
    }

    public static Integer[] toObjectArray(int[] array){
        return asArray(array);
    }

    public static Short[] toObjectArray(short[] array){
        return asArray(array);
    }
    /*/
    public static Boolean[] toObjectArray(boolean[] array){
        if(array == null)
            return null;

        return (Boolean[]) Functions.Default.apply(() ->
                toObjectArray(array, Boolean.class, array.length, (a, i)->Boolean.valueOf(a[i])));
    }

    public static Byte[] toObjectArray(byte[] array){
        if(array == null)
            return null;

        return (Byte[]) Functions.Default.apply(() ->
                toObjectArray(array, Byte.class, array.length, (a, i)->Byte.valueOf(a[i])));
    }

    public static Character[] toObjectArray(char[] array){
        if(array == null)
            return null;

        return (Character[]) Functions.Default.apply(() ->
                toObjectArray(array, Character.class, array.length, (a, i)->Character.valueOf(a[i])));
    }

    public static Float[] toObjectArray(float[] array){
        if(array == null)
            return null;

        return (Float[]) Functions.Default.apply(() ->
                toObjectArray(array, Float.class, array.length, (a, i)->Float.valueOf(a[i])));
    }

    public static Double[] toObjectArray(double[] array){
        if(array == null)
            return null;

        return (Double[]) Functions.Default.apply(() ->
                toObjectArray(array, Double.class, array.length, (a, i)->Double.valueOf(a[i])));
    }

    public static Integer[] toObjectArray(int[] array){
        if(array == null)
            return null;

        return (Integer[]) Functions.Default.apply(() ->
                toObjectArray(array, Integer.class, array.length, (a, i)->Integer.valueOf(a[i])));
    }

    public static Short[] toObjectArray(short[] array){
        if(array == null)
            return null;

        return (Short[]) Functions.Default.apply(() ->
                toObjectArray(array, Short.class, array.length, (a, i)->Short.valueOf(a[i])));
    }
    //*/

    protected static final PredicateThrowable<Class> alwaysFalse = c -> false;

    private static final Method _getConstantPool = (Method) Functions.ReturnsDefaultValue.apply(() -> {
        Method method = Class.class.getDeclaredMethod("getConstantPool");
        method.setAccessible(true);
        return method;
    });

    public static ConstantPool getConstantPoolOfClass(Class objectClass){
        return (ConstantPool) Functions.ReturnsDefaultValue.apply(()->_getConstantPool.invoke(objectClass));
    }

//    public static TupleValueRepository.PentaValues<Class, Class, Type[], Type[], Class, String> lambdaTypeInfos =
//            TupleValueRepository.create5(TypeHelper::resolveLambdaType);
//
//    public static Tuple.Penta<Class, Type[], Type[], Class, String> resolveLambdaType(Class instanceClass){
//        Objects.requireNonNull(instanceClass);
//        String className = instanceClass.getName();
//        if(!className.contains(LAMBDA_NAME_KEY))
//            return null;
//
//        Class genericType = (Class) instanceClass.getGenericInterfaces()[0];
//        ConstantPool constantPool = getConstantPoolOfClass(instanceClass);
//        Method functionInterfaceMethod = null;
//        int index = constantPool.getSize();
//        while(--index >=0) {
//            final int i = index;
//            functionInterfaceMethod = (Method) Functions.ReturnsDefaultValue.apply(() -> constantPool.getMethodAt(i));
//            if(functionInterfaceMethod != null && functionInterfaceMethod.isSynthetic()){
//                break;
//            }
//        }
//        Type[] typeParameters = genericType.getTypeParameters();
//        int len = typeParameters.length;
//
//        Class[] argumentTypes = functionInterfaceMethod.getParameterTypes();
//        Class[] genericArgumentTypes = Arrays.copyOf(argumentTypes, len);
//        Class returnType = functionInterfaceMethod.getReturnType();
//        genericArgumentTypes[len-1] = returnType;
//
//        //Notice: this typeSegment may not be accurate when the lambda expression has extra argment embedded
//        String typeSegments = IntStream.range(0, len).boxed()
//                .map(i -> String.format("%s %s", genericArgumentTypes[i].getSimpleName(),
//                        typeParameters[i].getTypeName()))
//                .collect(Collectors.joining(", "));
//
//        String token = String.format("%s<%s>", genericType.getName(), typeSegments);
//        return Tuple.create(genericType, typeParameters, argumentTypes, returnType, token);
//    }
//
//    public static Tuple resolveLambdaInstance(AbstractThrowable lambda){
//        return resolveLambdaType(lambda.getClass());
//    }

//    public static Class getReturnType(AbstractThrowable lambda){
//        return lambdaTypeInfos.getFourthValue(lambda.getClass());
//    }

    /**
     * @param instanceClass Type to be evaludated
     * @return
     */
    public static PredicateThrowable<Class> getClassPredicate(Class instanceClass){
        if(instanceClass == null) return alwaysFalse;
        if (!classPredicates.containsKey(instanceClass)){
            classPredicates.put(instanceClass,clazz -> instanceClass.isAssignableFrom(clazz));
        }
        return classPredicates.get(instanceClass);
    }

    public static <T> PredicateThrowable getClassPredicate(T... instances){
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

    public static <T> Type getGenericType(T... instances){
        Class<T> instanceClass = getDeclaredType(instances);
        return getGenericTypeFromType(instanceClass);
    }

    public static Type getGenericTypeFromType(Type instanceType){
        if(instanceType instanceof ParameterizedType)
            return ((ParameterizedType) instanceType).getRawType();

        Class<?> instanceClass = (Class<?>) instanceType;
        if(ObjectClass.equals(instanceClass) || instanceClass.isPrimitive())
            return null;
        Type[] typeParameters = instanceClass.getTypeParameters();
        if(typeParameters != null && typeParameters.length>0)
            return instanceClass;

        Type superType = instanceClass.getGenericSuperclass();
        return superType == null ? null : getGenericTypeFromType(superType);
    }

    public static <T> Tuple getGenericInfo(T... instances){
        Class<T> instanceClass = getDeclaredType(instances);
        if(instances.length == 1 || instances[0] != null)
            return getGenericInfoFromTypeAndInstance(instances[0], instanceClass);

        return getGenericInfoFromType(instanceClass);
    }

    public static <T> Tuple getGenericInfoFromTypeAndInstance(T instance, Class<T> instanceClass){

        return getGenericInfoFromInstance(instance, instanceClass, instanceClass);
    }

    public static Tuple getGenericInfoFromType(Type instanceType) {
        return getGenericInfoFromType(instanceType, instanceType);
    }

    public static Tuple getGenericInfoFromType(Type instanceType, Type originalType){
        //Define Triple with types explicitly to keep Type information
        Quad<Class, Type[], String, String> typeInfo = null;
        Type[] typeParameters;
        Type[] typedArguments;
        if(instanceType instanceof ParameterizedType){
            ParameterizedType parameterizedType = ((ParameterizedType) instanceType);
            Type rawType = parameterizedType.getRawType();
            Class rawClass = (Class) rawType;
            typeParameters = rawClass.getTypeParameters();
            typedArguments = parameterizedType.getActualTypeArguments();

            String token = String.format("%s<%s>", rawClass.getName(),
                    Arrays.stream(typedArguments).map(t -> t.getTypeName()).collect(Collectors.joining(",")));
            String tokenSimple = String.format("%s<%s>", rawClass.getSimpleName(),
                    Arrays.stream(typedArguments).map(t -> ((Class)t).getSimpleName()).collect(Collectors.joining(",")));
            typeInfo = Tuple.create(rawClass, typedArguments, token, tokenSimple);
            return typeInfo;
        }

        Class<?> instanceClass = (Class<?>) instanceType;
        if(ObjectClass.equals(instanceClass) || instanceClass.isPrimitive()) {
            Class originalClass = ((Class)originalType);
            typeInfo = Tuple.create(null, null, originalClass.getName(), originalClass.getSimpleName());
            return typeInfo;
        }
        typeParameters = instanceClass.getTypeParameters();
        if(typeParameters != null && typeParameters.length>0) {
            String token = String.format("%s<%s>", instanceClass.getName(),
                    Arrays.stream(typeParameters).map(t -> t.getTypeName()).collect(Collectors.joining(",")));
            String tokenSimple = String.format("%s<%s>", instanceClass.getSimpleName(),
                    Arrays.stream(typeParameters).map(t -> t.toString()).collect(Collectors.joining(",")));
            typeInfo = Tuple.create(instanceClass, typeParameters, token, tokenSimple);
            return typeInfo;
        }

        Type superType = instanceClass.getGenericSuperclass();
        return getGenericInfoFromType(superType, instanceType);
    }

    public static <T> Tuple getGenericInfoFromInstance(T instance, Class<T> declaredClass, Type instanceType){
        //Define Triple with types explicitly to keep Type information
        Quad<Class, Type[], String, String> typeInfo = null;
        Type[] typeParameters;
        Type[] typedArguments;
        if(instanceType instanceof ParameterizedType){
            ParameterizedType parameterizedType = ((ParameterizedType) instanceType);
            Type rawType = parameterizedType.getRawType();
            Class rawClass = (Class) rawType;
            typeParameters = rawClass.getTypeParameters();
            typedArguments = parameterizedType.getActualTypeArguments();

            String token = String.format("%s<%s>", rawClass.getName(),
                    Arrays.stream(typedArguments).map(t -> t.getTypeName()).collect(Collectors.joining(",")));
            String tokenSimple = String.format("%s<%s>", rawClass.getSimpleName(),
                    Arrays.stream(typedArguments).map(t -> ((Class)t).getSimpleName()).collect(Collectors.joining(",")));
            typeInfo = Tuple.create(rawClass, typedArguments, token, tokenSimple);
            return typeInfo;
        }

        Class<?> instanceClass = (Class<?>) instanceType;
        if(ObjectClass.equals(instanceClass) || instanceClass.isPrimitive()) {
            typeInfo = Tuple.create(null, null, declaredClass.getName(), declaredClass.getSimpleName());
            return typeInfo;
        }

        typeParameters = instanceClass.getTypeParameters();
        int paraLength = typeParameters.length;
        if(typeParameters != null && paraLength>0) {
            Method[] methods = declaredClass.getMethods();
            Method functionalInterfaceMethod = Arrays.stream(methods)
                    .filter(m -> !m.isDefault() && !m.isBridge() && !Modifier.isStatic(m.getModifiers()))
                    .findFirst().orElse(null);
            Type returnType = functionalInterfaceMethod.getGenericReturnType();
            Type[] paramTypes = functionalInterfaceMethod.getGenericParameterTypes();

            try {
                ConstantPool constantPool = (ConstantPool) _getConstantPool.invoke(instance.getClass());
                String lambdaString = getLambdaTypeString(constantPool);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            typedArguments = new Type[paraLength];
            for (int i = 0; i < typeParameters.length; i++) {
            }

            String token = String.format("%s<%s>", instanceClass.getName(),
                    Arrays.stream(typeParameters).map(t -> t.getTypeName()).collect(Collectors.joining(",")));
            String tokenSimple = String.format("%s<%s>", instanceClass.getSimpleName(),
                    Arrays.stream(typeParameters).map(t -> t.toString()).collect(Collectors.joining(",")));
            typeInfo = Tuple.create(instanceClass, typeParameters, token, tokenSimple);
            return typeInfo;
        }

        Type superType = instanceClass.getGenericSuperclass();
        return getGenericInfoFromType(superType, instanceType);
    }

    private static String getLambdaTypeString(ConstantPool constantPool) {
        int size = constantPool.getSize();
        String[] memberRef = null;

        // find last element in constantPool with valid memberRef
        // - previously always at size-2 index but changed with JDK 1.8.0_60
        for (int i = size - 1; i > -1; i--) {
            try {
                memberRef = constantPool.getMemberRefInfoAt(i);

                return memberRef[2];
            } catch (IllegalArgumentException e) {
                // eat error; null entry at ConstantPool index?
            }
        }
        throw new RuntimeException("Couldn't find memberRef.");
    }

    private static void extractTypeArguments(Map<Type, Type> typeMap, Class<?> clazz) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (!(genericSuperclass instanceof ParameterizedType)) {
            return;
        }

        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type[] typeParameter = ((Class<?>) parameterizedType.getRawType()).getTypeParameters();
        Type[] actualTypeArgument = parameterizedType.getActualTypeArguments();
        for (int i = 0; i < typeParameter.length; i++) {
            if(typeMap.containsKey(actualTypeArgument[i])) {
                actualTypeArgument[i] = typeMap.get(actualTypeArgument[i]);
            }
            typeMap.put(typeParameter[i], actualTypeArgument[i]);
        }
    }

    public static Class<?> findSubClassParameterType(Object instance, Class<?> classOfInterest, int parameterIndex) {
        Map<Type, Type> typeMap = new HashMap<Type, Type>();
        Class<?> instanceClass = instance.getClass();
        while (classOfInterest != instanceClass.getSuperclass()) {
            extractTypeArguments(typeMap, instanceClass);
            instanceClass = instanceClass.getSuperclass();
            if (instanceClass == null) throw new IllegalArgumentException();
        }

        ParameterizedType parameterizedType = (ParameterizedType) instanceClass.getGenericSuperclass();
        Type actualType = parameterizedType.getActualTypeArguments()[parameterIndex];
        if (typeMap.containsKey(actualType)) {
            actualType = typeMap.get(actualType);
        }

        if (actualType instanceof Class) {
            return (Class<?>) actualType;
        } else if (actualType instanceof TypeVariable) {
            return browseNestedTypes(instance, (TypeVariable<?>) actualType);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static Class<?> browseNestedTypes(Object instance, TypeVariable<?> actualType) {
        Class<?> instanceClass = instance.getClass();
        List<Class<?>> nestedOuterTypes = new LinkedList<Class<?>>();
        for (
                Class<?> enclosingClass = instanceClass.getEnclosingClass();
                enclosingClass != null;
                enclosingClass = enclosingClass.getEnclosingClass()) {
            try {
                Field this$0 = instanceClass.getDeclaredField("this$0");
                Object outerInstance = this$0.get(instance);
                Class<?> outerClass = outerInstance.getClass();
                nestedOuterTypes.add(outerClass);
                Map<Type, Type> outerTypeMap = new HashMap<Type, Type>();
                extractTypeArguments(outerTypeMap, outerClass);
                for (Map.Entry<Type, Type> entry : outerTypeMap.entrySet()) {
                    if (!(entry.getKey() instanceof TypeVariable)) {
                        continue;
                    }
                    TypeVariable<?> foundType = (TypeVariable<?>) entry.getKey();
                    if (foundType.getName().equals(actualType.getName())
                            && isInnerClass(foundType.getGenericDeclaration(), actualType.getGenericDeclaration())) {
                        if (entry.getValue() instanceof Class) {
                            return (Class<?>) entry.getValue();
                        }
                        actualType = (TypeVariable<?>) entry.getValue();
                    }
                }
            } catch (NoSuchFieldException e) { /* this should never happen */ } catch (IllegalAccessException e) { /* this might happen */}

        }
        throw new IllegalArgumentException();
    }

    private static boolean isInnerClass(GenericDeclaration outerDeclaration, GenericDeclaration innerDeclaration) {
        if (!(outerDeclaration instanceof Class) || !(innerDeclaration instanceof Class)) {
            throw new IllegalArgumentException();
        }
        Class<?> outerClass = (Class<?>) outerDeclaration;
        Class<?> innerClass = (Class<?>) innerDeclaration;
        while ((innerClass = innerClass.getEnclosingClass()) != null) {
            if (innerClass == outerClass) {
                return true;
            }
        }
        return false;
    }
}
