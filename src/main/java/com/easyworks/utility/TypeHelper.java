package com.easyworks.utility;

import com.easyworks.function.SupplierThrows;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TypeHelper {
    public static final Class ObjectClass = Object.class;
    
    public static final Map<Class, SupplierThrows.PredicateThrows<Class>> classPredicates = new HashMap<Class, SupplierThrows.PredicateThrows<Class>>(){{
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

    protected static final SupplierThrows.PredicateThrows<Class> alwaysFalse = c -> false;
    /**
     * TODO: get the Class predicate based on the given object.
     * @param klass Type to be evaludated
     * @return
     */
    public static SupplierThrows.PredicateThrows<Class> getClassPredicate(Class klass){
        if(klass == null) return alwaysFalse;
        if (!classPredicates.containsKey(klass)){
            classPredicates.put(klass,clazz -> clazz.isAssignableFrom(klass));
        }
        return classPredicates.get(klass);
    }

    public static <T> Class<T> getDeclaredType(T... instances){
        Objects.requireNonNull(instances);
        Class arrayClass = instances.getClass();
        Class componentType = (Class<T>) arrayClass.getComponentType();
        if(ObjectClass == componentType && 1 == instances.length && instances[0] != null)
            return (Class<T>) instances[0].getClass();
        return componentType;
    }

    public static <T> Type getGenericType(T... instance){
        Class<T> instanceClass = getDeclaredType(instance);
        return getGenericType(instanceClass);
    }

    public static Type getGenericType(Class<?> instanceClass){
        if(ObjectClass.equals(instanceClass) || instanceClass.isPrimitive())
            return null;
        return getGenericTypeImpl(instanceClass);
    }

    private static Type getGenericTypeImpl(Type instanceType){
        if(instanceType instanceof ParameterizedType)
            return ((ParameterizedType) instanceType).getRawType();

        Class<?> instanceClass = (Class<?>) instanceType;
        if(ObjectClass.equals(instanceClass))
            return null;
        Type[] typeParameters = instanceClass.getTypeParameters();
        if(typeParameters != null && typeParameters.length>0)
            return instanceClass;

        Type[] interfaces = instanceClass.getGenericInterfaces();
        ParameterizedType[] parameterizedTypes = Arrays.stream(interfaces)
                .filter(t -> t instanceof ParameterizedType)
                .map(t -> (ParameterizedType)t)
                .toArray(ParameterizedType[]::new);

        if(parameterizedTypes.length > 0) {
            for (int i = 0; i < parameterizedTypes.length; i++) {
                ParameterizedType pType = parameterizedTypes[i];
                Type[] argumentTypes = pType.getActualTypeArguments();
                for (int j = 0; j < argumentTypes.length; j++) {
                    Class argumentClass = (Class) argumentTypes[i];
                    if(!argumentClass.isAssignableFrom(instanceClass))
                        return instanceClass;
                }
            }
        }

        Type superType = instanceClass.getGenericSuperclass();
        return superType == null ? null : getGenericTypeImpl(superType);
    }

}
