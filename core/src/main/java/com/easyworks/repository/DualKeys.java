package com.easyworks.repository;

import com.easyworks.tuple.*;

/**
 * Interface for map.keys that are of <tt>Tuple</tt> of 2 elements and keep the methods to use them as strong-typed
 * values to fetch mapped values of type <tt>T</tt>
 * @param <K1>  type of the first element of the Keys of the map, that are type of Dual
 * @param <K2>  type of the second element of the Keys of the map
 * @param <T>   generic type of the stored value mapped from Tuple.Dual composed by 2 elements
 */
public interface DualKeys<K1,K2, T> {

    /**
     * Retrieve mapped value with both keys
     * @param k1    first element of actual key of the map, with type of <tt>K1</tt>
     * @param k2    second element of actual key of the map, with type of <tt>K2</tt>
     * @return      mapped value of type <tt>T</tt>
     */
    T retrieve(K1 k1, K2 k2);

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the specified
     * key.  More formally, returns <tt>true</tt> if and only if
     * this map contains a mapping for a key <tt>k</tt> such that
     * <tt>(key==null ? k==null : key.equals(k))</tt>.  (There can be
     * at most one such mapping.)
     * @param k1    first element of actual key of the map, with type of <tt>K1</tt>
     * @param k2    second element of actual key of the map, with type of <tt>K2</tt>
     * @return <tt>true</tt> if this map contains a mapping for the specified
     *         key
     */
    boolean containsKeyOf(K1 k1, K2 k2);

    /**
     * Get the  Tuple.Dual as the actual Key of the concerned map
     * @param k1    first element of actual key of the map, with type of <tt>K1</tt>
     * @param k2    second element of actual key of the map, with type of <tt>K2</tt>
     * @return  Tuple.Dual composed by <tt>k1</tt> and <tt>t2</tt> to be used as the actual key of the map
     */
    default Dual<K1, K2> getKey(K1 k1, K2 k2){
        return Tuple.create(k1, k2);
    }

    /**
     * Interface for map.keys that are of <tt>Tuple</tt> of 2 elements and keep the methods to use them as strong-typed
     * values to fetch mapped values of Tuple.Dual composed by 2 elements of types <tt>T</tt> and <tt>U</tt>
     * @param <K1>  type of the first element of the Keys of the map, that are type of Dual
     * @param <K2>  type of the second element of the Keys of the map
     * @param <T>   type of the first element of stored value (Tuple.Dual composed by 2 elements of types <tt>T</tt> and <tt>U</tt>)
     * @param <U>   type of the second element of stored value (Tuple.Dual composed by 2 elements of types <tt>T</tt> and <tt>U</tt>)
     */
    interface DualKeys2<K1,K2, T,U> extends DualKeys<K1,K2, Dual<T,U>>, DualValues<Dual<K1,K2>, T,U>{
        default T getFirst(K1 k1, K2 k2) {
            return getFirstValue(getKey(k1, k2));
        }

        default U getSecond(K1 k1, K2 k2) {
            return getSecondValue(getKey(k1, k2));
        }
    }

    /**
     * Interface for map.keys that are of <tt>Tuple</tt> of 2 elements and keep the methods to use them as strong-typed
     * values to fetch mapped values of Tuple.Single composed by 1 elements of types <tt>T</tt>
     * @param <K1>  type of the first element of the Keys of the map, that are type of Dual
     * @param <K2>  type of the second element of the Keys of the map
     * @param <T>   type of the first element of stored value (Tuple.Single composed by 1 element of types <tt>T</tt>
     */
    interface DualKeys1<K1,K2, T> extends DualKeys<K1,K2, Single<T>>, SingleValues<Dual<K1,K2>, T>{
        default T getFirst(K1 k1, K2 k2) {
            return getFirstValue(getKey(k1, k2));
        }
    }

    /**
     * Interface for map.keys that are of <tt>Tuple</tt> of 2 elements and keep the methods to use them as strong-typed
     * values to fetch mapped values of Tuple.Hepta composed by 3 elements of types different types
     * @param <K1>  type of the first element of the Keys of the map, that are type of Dual
     * @param <K2>  type of the second element of the Keys of the map
     * @param <T>   type of the first element of stored value (Tuple composed by 7 elements)
     * @param <U>   type of the second element of stored value (Tuple composed by 7 elements)
     * @param <V>   type of the third element of stored value (Tuple composed by 7 elements)
     */
    interface DualKeys3<K1,K2, T,U,V> extends DualKeys<K1,K2, Triple<T,U,V>>, TripleValues<Dual<K1,K2>, T,U,V> {
        default T getFirst(K1 k1, K2 k2) {
            return getFirstValue(getKey(k1, k2));
        }

        default U getSecond(K1 k1, K2 k2) {
            return getSecondValue(getKey(k1, k2));
        }

        default V getThird(K1 k1, K2 k2) {
            return getThirdValue(getKey(k1, k2));
        }
    }

    /**
     * Interface for map.keys that are of <tt>Tuple</tt> of 2 elements and keep the methods to use them as strong-typed
     * values to fetch mapped values of Tuple.Hepta composed by 4 elements of types different types
     * @param <K1>  type of the first element of the Keys of the map, that are type of Dual
     * @param <K2>  type of the second element of the Keys of the map
     * @param <T>   type of the first element of stored value (Tuple composed by 7 elements)
     * @param <U>   type of the second element of stored value (Tuple composed by 7 elements)
     * @param <V>   type of the third element of stored value (Tuple composed by 7 elements)
     * @param <W>   type of the fourth element of stored value (Tuple composed by 7 elements)
     */
    interface DualKeys4<K1,K2, T,U,V,W> extends DualKeys<K1,K2, Quad<T,U,V,W>>, QuadValues<Dual<K1,K2>, T,U,V,W>{
        default T getFirst(K1 k1, K2 k2) {
            return getFirstValue(getKey(k1, k2));
        }

        default U getSecond(K1 k1, K2 k2) {
            return getSecondValue(getKey(k1, k2));
        }

        default V getThird(K1 k1, K2 k2) {
            return getThirdValue(getKey(k1, k2));
        }
        default W getFourth(K1 k1, K2 k2) {
            return getFourthValue(getKey(k1, k2));
        }
    }

    /**
     * Interface for map.keys that are of <tt>Tuple</tt> of 2 elements and keep the methods to use them as strong-typed
     * values to fetch mapped values of Tuple.Hepta composed by 5 elements of types different types
     * @param <K1>  type of the first element of the Keys of the map, that are type of Dual
     * @param <K2>  type of the second element of the Keys of the map
     * @param <T>   type of the first element of stored value (Tuple composed by 7 elements)
     * @param <U>   type of the second element of stored value (Tuple composed by 7 elements)
     * @param <V>   type of the third element of stored value (Tuple composed by 7 elements)
     * @param <W>   type of the fourth element of stored value (Tuple composed by 7 elements)
     * @param <X>   type of the fifth element of stored value (Tuple composed by 7 elements)
     */
    interface DualKeys5<K1,K2, T,U,V,W,X> extends DualKeys<K1,K2, Penta<T,U,V,W,X>>, PentaValues<Dual<K1,K2>, T,U,V,W,X>{
        default T getFirst(K1 k1, K2 k2) {
            return getFirstValue(getKey(k1, k2));
        }

        default U getSecond(K1 k1, K2 k2) {
            return getSecondValue(getKey(k1, k2));
        }

        default V getThird(K1 k1, K2 k2) {
            return getThirdValue(getKey(k1, k2));
        }
        default W getFourth(K1 k1, K2 k2) {
            return getFourthValue(getKey(k1, k2));
        }

        default X getFifth(K1 k1, K2 k2) {
            return getFifthValue(getKey(k1, k2));
        }
    }

    /**
     * Interface for map.keys that are of <tt>Tuple</tt> of 2 elements and keep the methods to use them as strong-typed
     * values to fetch mapped values of Tuple.Hepta composed by 6 elements of types different types
     * @param <K1>  type of the first element of the Keys of the map, that are type of Dual
     * @param <K2>  type of the second element of the Keys of the map
     * @param <T>   type of the first element of stored value (Tuple composed by 7 elements)
     * @param <U>   type of the second element of stored value (Tuple composed by 7 elements)
     * @param <V>   type of the third element of stored value (Tuple composed by 7 elements)
     * @param <W>   type of the fourth element of stored value (Tuple composed by 7 elements)
     * @param <X>   type of the fifth element of stored value (Tuple composed by 7 elements)
     * @param <Y>   type of the sixth element of stored value (Tuple composed by 7 elements)
     */
    interface DualKeys6<K1,K2, T,U,V,W,X,Y> extends DualKeys<K1,K2, Hexa<T,U,V,W,X,Y>>, HexaValues<Dual<K1,K2>, T,U,V,W,X,Y>{
        default T getFirst(K1 k1, K2 k2) {
            return getFirstValue(getKey(k1, k2));
        }

        default U getSecond(K1 k1, K2 k2) {
            return getSecondValue(getKey(k1, k2));
        }

        default V getThird(K1 k1, K2 k2) {
            return getThirdValue(getKey(k1, k2));
        }
        default W getFourth(K1 k1, K2 k2) {
            return getFourthValue(getKey(k1, k2));
        }

        default X getFifth(K1 k1, K2 k2) {
            return getFifthValue(getKey(k1, k2));
        }

        default Y getSixth(K1 k1, K2 k2) {
            return getSixthValue(getKey(k1, k2));
        }
    }

    /**
     * Interface for map.keys that are of <tt>Tuple</tt> of 2 elements and keep the methods to use them as strong-typed
     * values to fetch mapped values of Tuple.Hepta composed by 7 elements of types different types
     * @param <K1>  type of the first element of the Keys of the map, that are type of Dual
     * @param <K2>  type of the second element of the Keys of the map
     * @param <T>   type of the first element of stored value (Tuple composed by 7 elements)
     * @param <U>   type of the second element of stored value (Tuple composed by 7 elements)
     * @param <V>   type of the third element of stored value (Tuple composed by 7 elements)
     * @param <W>   type of the fourth element of stored value (Tuple composed by 7 elements)
     * @param <X>   type of the fifth element of stored value (Tuple composed by 7 elements)
     * @param <Y>   type of the sixth element of stored value (Tuple composed by 7 elements)
     * @param <Z>   type of the seventh element of stored value (Tuple composed by 7 elements)
     */
    interface DualKeys7<K1,K2, T,U,V,W,X,Y,Z> extends DualKeys<K1,K2, Hepta<T,U,V,W,X,Y,Z>>, HeptaValues<Dual<K1,K2>, T,U,V,W,X,Y,Z>{
        default T getFirst(K1 k1, K2 k2) {
            return getFirstValue(getKey(k1, k2));
        }

        default U getSecond(K1 k1, K2 k2) {
            return getSecondValue(getKey(k1, k2));
        }

        default V getThird(K1 k1, K2 k2) {
            return getThirdValue(getKey(k1, k2));
        }
        default W getFourth(K1 k1, K2 k2) {
            return getFourthValue(getKey(k1, k2));
        }

        default X getFifth(K1 k1, K2 k2) {
            return getFifthValue(getKey(k1, k2));
        }

        default Y getSixth(K1 k1, K2 k2) {
            return getSixthValue(getKey(k1, k2));
        }

        default Z getSeventh(K1 k1, K2 k2) {
            return getSeventhValue(getKey(k1, k2));
        }
    }
}
