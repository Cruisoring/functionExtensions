package com.easyworks.utility;

import com.easyworks.tuple.Tuple;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class ArrayHelper {

    public static <T> T[] toArray(Collection<T> collection, Class<T> clazz){
        Objects.requireNonNull(collection);
        Objects.requireNonNull(clazz);
        T[] array = (T[])collection.toArray((T[]) Array.newInstance(clazz, 0));
        return array;
    }

    public static <T extends Comparable<T>> boolean matchInOrder(T[] expected, T[] actual) {
        if(expected.length != actual.length)
            return false;

        if(!Arrays.deepEquals(expected, actual)){
            Logger.L("Expected: %s != %s", Tuple.asTuple(expected), Tuple.asTuple(actual));
            return false;
        }
        return true;
    }

    public static <T extends Comparable<T>> boolean matchInOrder(Collection<T> expected, Collection<T> actual, Class<T> clazz) {
        int size = expected.size();
        if(size != actual.size())
            return false;
        T[] expectedArray = toArray(expected, clazz);
        T[] actualArray = toArray(actual, clazz);

        return matchInOrder(expectedArray, actualArray);
    }

    public static <T extends Comparable<T>> boolean matchWithoutOrder(T[] expected, T[] actual){
        if(expected.length != actual.length)
            return false;

        Arrays.sort(expected);
        Arrays.sort(actual);
        if(!Arrays.deepEquals(expected, actual)){
            Logger.L("Expected: %s != %s", Tuple.asTuple(expected), Tuple.asTuple(actual));
            return false;
        }
        return true;
    }

    public static <T extends Comparable<T>> boolean matchWithoutOrder(Collection<T> expected, Collection<T> actual, Class<T> clazz) {
        int size = expected.size();
        if(size != actual.size())
            return false;
        T[] expectedArray = toArray(expected, clazz);
        T[] actualArray = toArray(actual, clazz);

        return matchWithoutOrder(expectedArray, actualArray);
    }

}
