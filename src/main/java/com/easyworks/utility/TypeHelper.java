package com.easyworks.utility;

import com.easyworks.function.SupplierThrowable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TypeHelper {
    public static final Class ObjectClass = Object.class;
    
    public static final Map<Class, SupplierThrowable.PredicateThrowable<Class>> classPredicates = new HashMap<Class, SupplierThrowable.PredicateThrowable<Class>>(){{
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

    protected static final SupplierThrowable.PredicateThrowable<Class> alwaysFalse = c -> false;
    /**
     * @param instanceClass Type to be evaludated
     * @return
     */
    public static SupplierThrowable.PredicateThrowable<Class> getClassPredicate(Class instanceClass){
        if(instanceClass == null) return alwaysFalse;
        if (!classPredicates.containsKey(instanceClass)){
            classPredicates.put(instanceClass,clazz -> clazz.isAssignableFrom(instanceClass));
        }
        return classPredicates.get(instanceClass);
    }

    public static <T> SupplierThrowable.PredicateThrowable getClassPredicate(T... instances){
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
        return getGenericInfoFromType(instanceClass);
    }

    public static Tuple getGenericInfoFromType(Type instanceType) {
        return getGenericInfoFromType(instanceType, instanceType);
    }

    public static Tuple getGenericInfoFromType(Type instanceType, Type originalType){
        //Define Triple with types explicitly to keep Type information
        Tuple.Quad<Class, Type[], String, String> typeInfo = null;
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

}
