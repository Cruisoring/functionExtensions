package com.easyworks.utility;

import com.easyworks.function.SupplierThrows;

import java.util.HashMap;
import java.util.Map;

public class ClassHelper {

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


}
