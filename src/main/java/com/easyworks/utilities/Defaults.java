package com.easyworks.utilities;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Defaults<T> {
    @SuppressWarnings("unchecked")
    public static <T> T defaultValue(Class<T> type) {
        if (type == boolean.class || type == Boolean.class) {
            return (T) Boolean.FALSE;
        } else if (type == char.class || type == Character.class) {
            return (T) Character.valueOf('\0');
        } else if (type == byte.class || type == Byte.class) {
            return (T) Byte.valueOf((byte) 0);
        } else if (type == short.class || type == Short.class) {
            return (T) Short.valueOf((short) 0);
        } else if (type == int.class || type == Integer.class) {
            return (T) Integer.valueOf(0);
        } else if (type == long.class || type == Long.class) {
            return (T) Long.valueOf(0L);
        } else if (type == float.class || type == Float.class) {
            return (T) Float.valueOf(0f);
        } else if (type == double.class || type == Double.class) {
            return (T) Double.valueOf(0d);
        } else {
            return null;
        }
    }

    public static <T> T defaultValue(T sample){
        Class<T> clazz;
        try {
            clazz = (Class<T>) sample.getClass();
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
