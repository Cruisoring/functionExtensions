package com.easyworks.utility;

import com.easyworks.function.SupplierThrows;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper to get the default value of a given type or given strongly typed instance.
 * @param <T> Type of the default value.
 */
public class Defaults<T> {
    /**
     * The default value factories of known value types.
     * New types and corresponding factories could be put here.
     */
    public static final Map<Class<?>, SupplierThrows<Object>> typeDefaults = new HashMap<Class<?>, SupplierThrows<Object>>(){{
        put(boolean.class, ()->Boolean.FALSE);
        put(Boolean.class, ()->Boolean.FALSE);
        put(char.class, () -> Character.valueOf('\0'));
        put(Character.class, () -> Character.valueOf('\0'));
        put(byte.class, () -> Byte.valueOf((byte) 0));
        put(Byte.class, () -> Byte.valueOf((byte) 0));
        put(short.class, () -> Short.valueOf((short) 0));
        put(Short.class, () -> Short.valueOf((short) 0));
        put(int.class, () -> Integer.valueOf(0));
        put(Integer.class, () -> Integer.valueOf(0));
        put(long.class, () -> Long.valueOf(0L));
        put(Long.class, () -> Long.valueOf(0L));
        put(float.class, () -> Float.valueOf(0f));
        put(Float.class, () -> Float.valueOf(0f));
        put(double.class, () -> Double.valueOf(0d));
        put(Double.class, () -> Double.valueOf(0d));
    }};

    /**
     * Get the default value of a given type.
     * @param type  Class of the expected default value.
     * @param <T>   Type of the expected default value.
     * @return      Default value of the given class
     */
    @SuppressWarnings("unchecked")
    public static <T> T defaultValue(Class<T> type) {
        if(typeDefaults.containsKey(type)){
            return (T) typeDefaults.get(type);
        } else {
            return null;
        }
    }

    /**
     * Get the default value based on the given instance.
     * @param sample    Given instance of the type
     * @param <T>       Type to be evaluated.
     * @return          Default instance of the given value instance.
     */
    public static <T> T defaultValue(T sample){
        if(sample == null)
            return null;
        try {
            Class<T> clazz = (Class<T>) sample.getClass();
            if (clazz == null) {
                Type mySuperclass = sample.getClass().getGenericSuperclass();
                Type tType = ((ParameterizedType)mySuperclass).getActualTypeArguments()[0];
                clazz = (Class<T>) tType.getClass();
            }
            return defaultValue(clazz);
        } catch (Exception ex) {
            return null;
        }
    }
}
