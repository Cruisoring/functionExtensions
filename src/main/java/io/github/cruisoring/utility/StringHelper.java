package io.github.cruisoring.utility;

import io.github.cruisoring.Range;
import io.github.cruisoring.TypeHelper;
import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.repository.Repository;
import io.github.cruisoring.throwables.FunctionThrowable;
import io.github.cruisoring.throwables.TriConsumerThrowable;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.MalformedParametersException;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.github.cruisoring.Asserts.assertAllNotNull;

/**
 * Helper class to keep String related utilities.
 */
public class StringHelper {
    public final static String PercentageAscii = "&#37";
    public static final Function<Object, String[]> defaultToStringForms = StringHelper::commonToStrings;

    private static final String[] commonToStrings(Object obj) {
        if (obj == null) {
            return new String[]{"null", "NULL"};
        }

        Class objClass = obj.getClass();
        if (objClass.isArray()) {
            return new String[]{TypeHelper.deepToString(obj)};
        }

        return new String[]{obj.toString()};
    }

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
        assertAllNotNull(clazz);

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

    /**
     * Parse a given String as value of given type, return it if parse successfully or the default value if failed.
     * @param objString     the String to be intepreted as given type.
     * @param objectType    the class of the value represented by the given String.
     * @param defaultValue  the default value to be returned if given values are illegal or parsing failed.
     * @param <T>     type of the value represented by the given String.
     * @return the parsed value of type <tt>T</tt> if success, or the given {@code defaultValue} if something wrong.
     */
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

    /**
     * Test if the given String collection contains all keywords.
     * @param collection    Collection of Strings under test.
     * @param targets       Keywords shall be all contained by the given String Collection.
     * @return  <tt>true</tt> if all keywords are contained by the String collection, otherwise <tt>false</tt>
     */
    public static boolean containsAll(Collection<String> collection, Collection<String> targets) {
        assertAllNotNull(collection, targets);

        for (String target : targets) {
            if (!collection.contains(target)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Test if the given String collection contains all keywords with case ignored.
     * @param collection    Collection of Strings under test.
     * @param targets       Keywords shall be all contained by the given String Collection.
     * @return  <tt>true</tt> if all keywords are contained by the String collection with case ignored, otherwise <tt>false</tt>
     */
    public static boolean containsAllIgnoreCase(Collection<String> collection, Collection<String> targets) {
        assertAllNotNull(collection, targets);

        List<String> lowerCases = collection.stream().map(s -> s == null ? null : s.toLowerCase()).collect(Collectors.toList());
        for (String target : targets) {
            if (!lowerCases.contains(target == null ? null : target.toLowerCase())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Test if the given String context contains all string representations of the given objects.
     * @param toStringForms converter of any {@code Object} to an array of Strings.
     * @param context   the Text context under test.     *
     * @param keys      the Objects whose String representations shall all be contained by the given text context.
     * @return  <tt>true</tt> if all keys are contained by the text context in any form as specified
     * by the {@code toStringForms}, otherwise <tt>false</tt>
     */
    public static boolean containsAll(Function<Object, String[]> toStringForms, String context, Object... keys) {
        assertAllNotNull(context, toStringForms);
        if(keys == null) {
            return context.contains("null");
        }

        for (int i = 0; i < keys.length; i++) {
            Object key = keys[i];
            String[] options = toStringForms.apply(key);
            boolean containsKey = false;
            for (String option : options) {
                if(context.contains(option)){
                    containsKey = true;
                    break;
                }
            }

            if(!containsKey) {
                Logger.V("%s doesn't contains %s.", Range.ofLength(100).intersectionWith(Range.ofLength(context.length())).subString(context), key);
                return false;
            }
        }
        return true;
    }

    /**
     * Test if the given String context contains all default string representations of the given objects.
     * @param context   the Text context under test.     *
     * @param keys      the Objects whose String representations shall all be contained by the given text context.
     * @return  <tt>true</tt> if all keys are contained by the text context with their default toString(), otherwise <tt>false</tt>
     */
    public static boolean containsAll(String context, Object... keys) {
        return containsAll(defaultToStringForms, context, keys);
    }

    /**
     * Test if the given String context contains all string representations of the given objects with case ignored.
     * @param toStringForms converter of any {@code Object} to an array of Strings.
     * @param context   the Text context under test.     *
     * @param keys      the Objects whose String representations shall all be contained by the given text context.
     * @return  <tt>true</tt> if all keys are contained by the text context in any form as specified
     * by the {@code toStringForms} and with case ignored, otherwise <tt>false</tt>
     */
    public static Boolean containsAllIgnoreCase(Function<Object, String[]> toStringForms, String context, Object... keys) {
        assertAllNotNull(context, toStringForms);
        if(keys == null) {
            return StringUtils.containsIgnoreCase(context, "null");
        }

        for (int i = 0; i < keys.length; i++) {
            Object key = keys[i];
            String[] options = toStringForms.apply(key);
            int optionlength = options.length;
            for (int j = 0; j < optionlength; j++) {
                if(StringUtils.containsIgnoreCase(context, options[j])){
                    break;
                } else if (j == optionlength-1){
                    Logger.V("%s doesn't contain %s",
                        context.length() < 100 ? context : context.substring(0, 100)+"...",
                        key);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Test if the given String context contains all default string representations of the given objects with case ignored.
     * @param context   the Text context under test.     *
     * @param keys      the Objects whose String representations shall all be contained by the given text context.
     * @return  <tt>true</tt> if all keys are contained by the text context with their default toString()
     * with case ignored, otherwise <tt>false</tt>
     */
    public static boolean containsAllIgnoreCase(String context, Object... keys) {
        return containsAllIgnoreCase(defaultToStringForms, context, keys);
    }

    /**
     * Test if the given String context contains ANY string representations of the given objects.
     * @param toStringForms converter of any {@code Object} to an array of Strings.
     * @param context   the Text context under test.     *
     * @param keys      the Objects under test.
     * @return  <tt>true</tt> if ANY keys are contained by the text context in any form as specified
     * by the {@code toStringForms}, otherwise <tt>false</tt>
     */
    public static boolean containsAny(Function<Object, String[]> toStringForms, String context, Object... keys) {
        assertAllNotNull(context, toStringForms);
        if(keys == null) {
            return StringUtils.contains(context, "null");
        }

        for (int i = 0; i < keys.length; i++) {
            Object key = keys[i];
            String[] options = toStringForms.apply(key);
            int optionlength = options.length;
            for (int j = 0; j < optionlength; j++) {
                if(StringUtils.contains(context, options[j])){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Test if the given String context contains ANY default string representations of the given objects.
     * @param context   the Text context under test.     *
     * @param keys      the Objects whose String representations shall all be contained by the given text context.
     * @return  <tt>true</tt> if ANY keys are contained by the text context with their default toString(), otherwise <tt>false</tt>
     */
    public static boolean containsAny(String context, Object... keys) {
        return containsAny(defaultToStringForms, context, (Object[]) keys);
    }

    /**
     * Test if the given String context contains ANY string representations of ANY of the given objects with case ignored.
     * @param toStringForms converter of any {@code Object} to an array of Strings.
     * @param context   the Text context under test.     *
     * @param keys      the Objects under test.
     * @return  <tt>true</tt> if any keys are contained by the text context in any form as specified
     * by the {@code toStringForms} and with case ignored, otherwise <tt>false</tt>
     */
    public static boolean containsAnyIgnoreCase(Function<Object, String[]> toStringForms, String context, Object... keys) {
        assertAllNotNull(context, toStringForms);
        if(keys == null) {
            return StringUtils.containsIgnoreCase(context, "null");
        }

        for (int i = 0; i < keys.length; i++) {
            Object key = keys[i];
            String[] options = toStringForms.apply(key);
            int optionlength = options.length;
            for (int j = 0; j < optionlength; j++) {
                if(StringUtils.containsIgnoreCase(context, options[j])){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Test if the given String context contains ANY default string representations of the given objects with case ignored.
     * @param context   the Text context under test.     *
     * @param keys      the Objects under test.
     * @return  <tt>true</tt> if ANY keys are contained by the text context with their default toString()
     * with case ignored, otherwise <tt>false</tt>
     */
    public static boolean containsAnyIgnoreCase(String context, Object... keys) {
        return containsAnyIgnoreCase(defaultToStringForms, context, keys);
    }

    /**
     * Try to call String.format() and refrain potential IllegalFormatException
     *
     * @param format template to compose a string with given arguments
     * @param args   arguments to be applied to the above template
     * @return string formatted with the given or exceptional template.
     */
    public static String tryFormatString(String format, Object... args) {
        if (args.length == 1 && args[0] instanceof Object[]) {
            args = (Object[]) args[0];
        }
        try {
            String formatted = String.format(format, args);
            formatted = formatted.replaceAll(PercentageAscii, "%");
            return formatted;
        } catch (Exception e) {
            String[] argStrings = Arrays.stream(args).map(arg -> arg == null ? "null" : arg.toString()).toArray(size -> new String[size]);
            return String.format("MalFormated format: '%s' where args[%d]: '%s'", format, args.length, String.join(", ", argStrings));
        }
    }

    /**
     * Try to retrieve the defined value from the given properties with the specified keys,
     * or returns the given defaultValue if no definition found or failed to convert the String to concerned type.
     *
     * @param properties    the Properties instance which might contains the property specified by any of the keys
     * @param defaultValue  the default value if failed to extract or convert the system property specified by the keys.
     * @param valueKeys     the keys used to locate the System Property to get the value String.
     * @param <T>           the type of the value to be extracted and converted to.
     * @return              the converted value if extracting System Property of given valueKeys successfully,
     *                      otherwise the given defaultValue woudl be returned.
     */
    public static <T> T tryGetProperty(Properties properties, T defaultValue, String... valueKeys) {
        if(properties == null || defaultValue == null || valueKeys == null) {
            return defaultValue;
        }

        for (String key : valueKeys) {
            String valueString = properties.getProperty(key);
            if(valueString != null) {
                parse(valueString, (Class<T>) defaultValue.getClass(), defaultValue);
            }
        }
        return defaultValue;
    }


    /**
     * Try to retrieve the defined value of a System property specified by the given keys,
     * or returns the given defaultValue if no definition found or failed to convert the String to concerned type.
     *
     * @param defaultValue  the default value if failed to extract or convert the system property specified by the keys.
     * @param valueKeys     the keys used to locate the System Property to get the value String.
     * @param <T>           the type of the value to be extracted and converted to.
     * @return              the converted value if extracting System Property of given valueKeys successfully,
     *                      otherwise the given defaultValue woudl be returned.
     */
    public static <T> T tryParseSystemProperties(T defaultValue, String... valueKeys) {
        if(defaultValue == null || valueKeys == null) {
            return defaultValue;
        }

        for (String key : valueKeys) {
            String valueString = System.getProperty(key);
            if(valueString != null) {
                parse(valueString, (Class<T>) defaultValue.getClass(), defaultValue);
            }
        }
        return defaultValue;
    }

    /**
     * Try to retrieve the defined value of a System property specified by the class name or class simpleName of the concerned type,
     * or returns the given defaultValue if no definition found or failed to convert the String to concerned type.
     *
     * @param defaultValue  the default value if failed to extract or convert the system property specified by the keys.
     * @param <T>           the type of the value to be extracted and converted to.
     * @return              the converted value if extracting System Property of given valueKeys successfully,
     *                      otherwise the given defaultValue woudl be returned.
     */
    public static <T> T tryParseSystemProperties(T defaultValue) {
        if(defaultValue == null) {
            return null;
        }

        Class<T> valueClass = (Class<T>) defaultValue.getClass();
        return tryParseSystemProperties(defaultValue, valueClass.getName(), valueClass.getSimpleName());
    }
}
