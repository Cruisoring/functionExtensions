package io.github.cruisoring.utility;

import io.github.cruisoring.TypeHelper;
import io.github.cruisoring.function.FunctionThrowable;
import io.github.cruisoring.function.TriConsumerThrowable;
import io.github.cruisoring.repository.Repository;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.MalformedParametersException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Stream;

public class StringHelper {

    /**
     * Try to convert the String to specific Enum type, returns the converted Enum value if success or the first Enum value if conversion failed
     * @param enumClass     Type of the concerned Enum
     * @param enumString    String to be converted
     * @param <E>           Type of the concerned Enum
     * @return              the converted Enum value if success or the first Enum value if conversion failed
     * @throws Exception    any exception that could be thrown when parsing the string to get Enum values
     */
    public static <E extends Enum<E>> E parseEnum(Class<E> enumClass, String enumString) throws Exception{
        E[] enumValues = (E[]) enumClass.getEnumConstants();
        E matchedOrFirst = Arrays.stream(enumValues)
                .filter(e -> e.toString().equalsIgnoreCase(enumString))
                .findFirst()
                .orElse(enumValues[0]);
        return matchedOrFirst;
    }

    private static Object throwsException(String s) throws Exception {
        throw new Exception();
    }

    private static final Repository<Class, FunctionThrowable<String, Object>> stringParsers = new Repository<Class, FunctionThrowable<String, Object>>(
            new HashMap<Class, FunctionThrowable<String, Object>>(){{
                put(Integer.class, s -> Integer.decode(s));
                put(int.class, s -> Integer.decode(s).intValue());
                put(Double.class, s -> Double.valueOf(s));
                put(double.class, s -> Double.valueOf(s).doubleValue());
                put(Short.class, s -> Short.valueOf(s));
                put(short.class, s -> Short.valueOf(s).shortValue());
                put(Long.class, s -> Long.valueOf(s));
                put(long.class, s -> Long.valueOf(s).longValue());
                put(Boolean.class, s -> Boolean.valueOf(s));
                put(boolean.class, s -> Boolean.valueOf(s).booleanValue());
                put(Float.class, s -> Float.valueOf(s));
                put(float.class, s -> Float.valueOf(s).floatValue());
                put(Byte.class, s -> Byte.valueOf(s));
                put(byte.class, s -> Byte.valueOf(s).byteValue());
                put(Character.class, s -> Character.valueOf(s.charAt(0)));
                put(char.class, s -> s.charAt(0));
            }},
            null,
            StringHelper::getParser
    );

    private static FunctionThrowable<String, Object> getParser(Class clazz) throws Exception{
        Objects.requireNonNull(clazz);

        if(stringParsers.containsKey(clazz))
            return stringParsers.get(clazz, null);

        if(TypeHelper.isPrimitive(clazz)){
            return getParser(TypeHelper.getEquivalentClass(clazz));
        }
        if(Enum.class.isAssignableFrom(clazz)){
            return s -> parseEnum(clazz, s);
        }

        if(clazz.isArray()){
            Class componentClass = clazz.getComponentType();
            FunctionThrowable<String, Object> componentParser = getParser(componentClass);
            TriConsumerThrowable<Object, Integer, Object> equivalentSetter = TypeHelper.getArrayElementSetter(componentClass);
            if(componentParser == null || equivalentSetter == null) {
                return StringHelper::throwsException;
            }
            return s -> {
                s = s.trim();
                if(!s.startsWith("[") || !s.endsWith("]"))
                    throw new MalformedParametersException("Array types shall be wrapped within '[]' and seperated by ','s.");
                String[] substrings = s.substring(1, s.length()-1).split(",");
                int length = substrings.length;
                Object parsedValues = ArrayHelper.getNewArray(componentClass, length);
                for (int i = 0; i < length; i++) {
                    equivalentSetter.accept(parsedValues, i, componentParser.apply(substrings[i]));
                }
                return parsedValues;
            };
        }

        throw new Exception("Not support!");
    }

    public static <T> T parse(String objString, Class<T> objectType, T defaultValue){
        if(objString == null || objectType == null)
            return defaultValue;

        try {
            FunctionThrowable<String, Object> parser = stringParsers.apply(objectType);
            return (T)parser.apply(objString);
        }catch (Exception ex){
            return defaultValue;
        }
    }

    public static final Function<Object, String[]> defaultToStringForms = o -> new String[]{o.toString()};
    public static final BiPredicate<String, String> contains = (s, k) -> StringUtils.contains(s, k);
    public static final BiPredicate<String, String> containsIgnoreCase = (s, k) -> StringUtils.containsIgnoreCase(s, k);

    private static final Boolean matchAny(BiPredicate<String, String> matcher, String context, String[] keys) {
        if (context == null) return false;
        return Arrays.stream(keys).anyMatch(k -> matcher.test(context, k));
    }

    public static Boolean containsAll(String context, Function<Object, String[]> toStringForms, Object... keys) {
        if (context == null) return false;

        return Arrays.stream(keys)
                .filter(o -> o != null)
                .allMatch(o -> matchAny(contains, context, toStringForms.apply(o)));
    }

    public static Boolean containsAll(String context, Object... keys) {
        return containsAll(context, defaultToStringForms, keys);
    }

    public static Boolean containsAllIgnoreCase(String context, Function<Object, String[]> toStringForms, Object... keys) {
        if (context == null) return false;

        return Stream.of(keys)
                .filter(o -> o != null)
                .allMatch(o ->
                        matchAny(containsIgnoreCase, context,
                                toStringForms.apply(o)));
    }

    public static Boolean containsAllIgnoreCase(String context, Object... keys) {
        return containsAllIgnoreCase(context, defaultToStringForms, keys);
    }

    public static Boolean containsAny(String context, Function<Object, String[]> toStringForms, Object... keys) {
        if (context == null) {
            return false;
        }
        return Arrays.stream(keys).filter(o -> o != null)
                .anyMatch(o -> matchAny(contains, context, toStringForms.apply(o)));
    }

    public static Boolean containsAny(String context, Object... keys) {
        return containsAny(context, defaultToStringForms, keys);
    }

    public static Boolean containsAny(String context, String... keys) {
        return containsAny(context, defaultToStringForms, (Object[]) keys);
    }

    public static Boolean containsAnyIgnoreCase(String context, Function<Object, String[]> toStringForms, Object... keys) {
        if (context == null) {
            return false;
        }
        return Arrays.stream(keys).filter(o -> o != null).parallel()
                .anyMatch(o -> matchAny(containsIgnoreCase, context, toStringForms.apply(o)));
    }

    public static Boolean containsAnyIgnoreCase(String context, Object... keys) {
        return containsAnyIgnoreCase(context, defaultToStringForms, keys);
    }


}
