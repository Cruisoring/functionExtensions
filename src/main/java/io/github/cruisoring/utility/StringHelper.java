package io.github.cruisoring.utility;

import io.github.cruisoring.TypeHelper;
import io.github.cruisoring.function.FunctionThrowable;
import io.github.cruisoring.function.TriConsumerThrowable;
import io.github.cruisoring.repository.Repository;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.MalformedParametersException;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.cruisoring.Functions.checkNotNull;

public class StringHelper {
    public final static String PercentageAscii = "&#37";
    public static final Function<Object, String[]> defaultToStringForms = o -> new String[]{o.toString()};
    public static final BiPredicate<String, String> contains = (s, k) -> StringUtils.contains(s, k);
    public static final BiPredicate<String, String> containsIgnoreCase = (s, k) -> StringUtils.containsIgnoreCase(s, k);
    private static final Repository<Class, FunctionThrowable<String, Object>> stringParsers = new Repository<Class, FunctionThrowable<String, Object>>(
            new HashMap<Class, FunctionThrowable<String, Object>>() {{
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

    /**
     * Try to convert the String to specific Enum type, returns the converted Enum value if success or the first Enum value if conversion failed
     *
     * @param enumClass  Type of the concerned Enum
     * @param enumString String to be converted
     * @param <E>        Type of the concerned Enum
     * @return the converted Enum value if success or the first Enum value if conversion failed
     * @throws Exception any exception that could be thrown when parsing the string to get Enum values
     */
    public static <E extends Enum<E>> E parseEnum(Class<E> enumClass, String enumString) throws Exception {
        E[] enumValues = enumClass.getEnumConstants();
        E matchedOrFirst = Arrays.stream(enumValues)
                .filter(e -> e.toString().equalsIgnoreCase(enumString))
                .findFirst()
                .orElse(enumValues[0]);
        return matchedOrFirst;
    }

    private static Object throwsException(String s) throws Exception {
        throw new Exception();
    }

    private static FunctionThrowable<String, Object> getParser(Class clazz) throws Exception {
        checkNotNull(clazz);

        if (stringParsers.containsKey(clazz))
            return stringParsers.get(clazz, null);

        if (TypeHelper.isPrimitive(clazz)) {
            return getParser(TypeHelper.getEquivalentClass(clazz));
        }
        if (Enum.class.isAssignableFrom(clazz)) {
            return s -> parseEnum(clazz, s);
        }

        if (clazz.isArray()) {
            Class componentClass = clazz.getComponentType();
            FunctionThrowable<String, Object> componentParser = getParser(componentClass);
            TriConsumerThrowable<Object, Integer, Object> equivalentSetter = TypeHelper.getArrayElementSetter(componentClass);
            if (componentParser == null || equivalentSetter == null) {
                return StringHelper::throwsException;
            }
            return s -> {
                s = s.trim();
                if (!s.startsWith("[") || !s.endsWith("]"))
                    throw new MalformedParametersException("Array types shall be wrapped within '[]' and seperated by ','s.");
                String[] substrings = s.substring(1, s.length() - 1).split(",");
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

    public static <T> T parse(String objString, Class<T> objectType, T defaultValue) {
        if (objString == null || objectType == null)
            return defaultValue;

        try {
            FunctionThrowable<String, Object> parser = stringParsers.apply(objectType);
            return (T) parser.apply(objString);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    private static final boolean matchAny(BiPredicate<String, String> matcher, String context, String[] keys) {
        if (context == null) return false;
        return Arrays.stream(keys).anyMatch(k -> matcher.test(context, k));
    }

    public static boolean containsAll(Collection<String> collection, Collection<String> targets) {
        checkNotNull(collection);
        checkNotNull(targets);

        for (String target : targets) {
            if (!collection.contains(target)) {
                return false;
            }
        }

        return true;
    }

    public static boolean containsAllIgnoreCase(Collection<String> collection, Collection<String> targets) {
        checkNotNull(collection);
        checkNotNull(targets);

        List<String> lowerCases = collection.stream().map(s -> s == null ? null : s.toLowerCase()).collect(Collectors.toList());
        for (String target : targets) {
            if (!lowerCases.contains(target == null ? null : target.toLowerCase())) {
                return false;
            }
        }

        return true;
    }

    public static boolean containsAll(String context, Function<Object, String[]> toStringForms, Object... keys) {
        if (context == null) return false;

        return Arrays.stream(keys)
                .filter(o -> o != null)
                .allMatch(o -> matchAny(contains, context, toStringForms.apply(o)));
    }

    public static boolean containsAll(String context, Object... keys) {
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

    public static boolean containsAllIgnoreCase(String context, Object... keys) {
        return containsAllIgnoreCase(context, defaultToStringForms, keys);
    }

    public static boolean containsAny(String context, Function<Object, String[]> toStringForms, Object... keys) {
        if (context == null) {
            return false;
        }
        return Arrays.stream(keys).filter(o -> o != null)
                .anyMatch(o -> matchAny(contains, context, toStringForms.apply(o)));
    }

    public static boolean containsAny(String context, Object... keys) {
        return containsAny(context, defaultToStringForms, keys);
    }

    public static boolean containsAny(String context, String... keys) {
        return containsAny(context, defaultToStringForms, (Object[]) keys);
    }

    public static boolean containsAnyIgnoreCase(String context, Function<Object, String[]> toStringForms, Object... keys) {
        if (context == null) {
            return false;
        }
        return Arrays.stream(keys).filter(o -> o != null).parallel()
                .anyMatch(o -> matchAny(containsIgnoreCase, context, toStringForms.apply(o)));
    }

    public static boolean containsAnyIgnoreCase(String context, Object... keys) {
        return containsAnyIgnoreCase(context, defaultToStringForms, keys);
    }

    /**
     * Try to call String.format() and refrain potential IllegalFormatException
     *
     * @param format template to compose a string with given arguments
     * @param args   arguments to be applied to the above template
     * @return string formatted with the given or exceptional template.
     */
    public static String tryFormatString(String format, Object... args) {
        checkNotNull(format);
        if (args.length == 1 && args[0] instanceof Object[]) {
            args = (Object[]) args[0];
        }
        try {
            String formatted = String.format(format, args);
            formatted = formatted.replaceAll(PercentageAscii, "%");
            return formatted;
        } catch (Exception e) {
            String[] argStrings = Arrays.stream(args).map(arg -> arg.toString()).toArray(size -> new String[size]);
            return String.format("MalFormated format: '%s'\nargs: '%s'", format, String.join(", ", argStrings));
        }
    }


}
