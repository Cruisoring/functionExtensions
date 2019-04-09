package io.github.cruisoring.repository;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple7;

/**
 * Interface for map.keys that are of <tt>Tuple</tt> of 7 elements and keep the methods to use them as strong-typed
 * values to fetch strong typed elements
 *
 * @param <K1> type of the first element of the Keys of the map, that are type of Tuple7
 * @param <K2> type of the second element of the Keys of the map, that are type of Tuple7
 * @param <K3> type of the third element of the Keys of the map, that are type of Tuple7
 * @param <K4> type of the fourth element of the Keys of the map, that are type of Tuple7
 * @param <K5> type of the fifth element of the Keys of the map, that are type of Tuple7
 * @param <K6> type of the sixth element of the Keys of the map, that are type of Tuple7
 * @param <K7> type of the seventh element of the Keys of the map, that are type of Tuple7
 */
public interface TupleKeys7<K1, K2, K3, K4, K5, K6, K7> extends TupleKeys<Tuple7<K1, K2, K3, K4, K5, K6, K7>> {

    /**
     * Get the strong-typed Tuple that matched with the actual Key of the concerned map
     *
     * @param k1 first element of actual key of the Tuple, with type of <tt>K1</tt>
     * @param k2 second element of actual key of the Tuple, with type of <tt>K2</tt>
     * @param k3 third element of actual key of the Tuple, with type of <tt>K3</tt>
     * @param k4 fourth element of actual key of the Tuple, with type of <tt>K4</tt>
     * @param k5 fifth element of actual key of the Tuple, with type of <tt>K5</tt>
     * @param k6 sixth element of actual key of the Tuple, with type of <tt>K6</tt>
     * @param k7 seventh element of actual key of the Tuple, with type of <tt>K7</tt>
     * @return Tuple composed by the above elements to be used as the actual key of the map
     */
    default Tuple7<K1, K2, K3, K4, K5, K6, K7> getKey(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
        return Tuple.create(k1, k2, k3, k4, k5, k6, k7);
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the specified Tuple key.
     *
     * @param k1 first element of actual key of the Tuple, with type of <tt>K1</tt>
     * @param k2 second element of actual key of the Tuple, with type of <tt>K2</tt>
     * @param k3 third element of actual key of the Tuple, with type of <tt>K3</tt>
     * @param k4 fourth element of actual key of the Tuple, with type of <tt>K4</tt>
     * @param k5 fifth element of actual key of the Tuple, with type of <tt>K5</tt>
     * @param k6 sixth element of actual key of the Tuple, with type of <tt>K6</tt>
     * @param k7 seventh element of actual key of the Tuple, with type of <tt>K7</tt>
     * @return <tt>true</tt> if this map contains a mapping for the specified Tuple key
     */
    default boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
        return containsKey(getKey(k1, k2, k3, k4, k5, k6, k7));
    }

    /**
     * Interface for map.keys that are of <tt>Tuple</tt> of 7 elements and keep the methods to use them as strong-typed
     * values to fetch mapped values of Tuple.Tuple7 composed by 1 element
     *
     * @param <K1> type of the first element of the Keys of the map, that are type of Tuple7
     * @param <K2> type of the second element of the Keys of the map, that are type of Tuple7
     * @param <K3> type of the third element of the Keys of the map, that are type of Tuple7
     * @param <K4> type of the fourth element of the Keys of the map, that are type of Tuple7
     * @param <K5> type of the fifth element of the Keys of the map, that are type of Tuple7
     * @param <K6> type of the sixth element of the Keys of the map, that are type of Tuple7
     * @param <K7> type of the seventh element of the Keys of the map, that are type of Tuple7
     * @param <T>  type of the first element of the Tuple value
     */
    interface TupleValues1<K1, K2, K3, K4, K5, K6, K7, T> extends TupleKeys7<K1, K2, K3, K4, K5, K6, K7>,
            io.github.cruisoring.repository.TupleValues1<Tuple7<K1, K2, K3, K4, K5, K6, K7>, T> {

        default Tuple retrieve(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return retrieve(getKey(k1, k2, k3, k4, k5, k6, k7));
        }

        /**
         * Retrieve the first value of the Tuple as type of <tt>T</tt>
         *
         * @param k1 first element of actual key of the Tuple, with type of <tt>K1</tt>
         * @param k2 second element of actual key of the Tuple, with type of <tt>K2</tt>
         * @param k3 third element of actual key of the Tuple, with type of <tt>K3</tt>
         * @param k4 fourth element of actual key of the Tuple, with type of <tt>K4</tt>
         * @param k5 fifth element of actual key of the Tuple, with type of <tt>K5</tt>
         * @param k6 sixth element of actual key of the Tuple, with type of <tt>K6</tt>
         * @param k7 seventh element of actual key of the Tuple, with type of <tt>K7</tt>
         * @return the first element of the Tuple value
         */
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getFirstValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }
    }

    /**
     * Interface for map.keys that are of <tt>Tuple</tt> of 7 elements and keep the methods to use them as strong-typed
     * values to fetch mapped values of Tuple composed by 7 elements
     *
     * @param <K1> type of the first element of the Keys of the map, that are type of Tuple7
     * @param <K2> type of the second element of the Keys of the map, that are type of Tuple7
     * @param <K3> type of the third element of the Keys of the map, that are type of Tuple7
     * @param <K4> type of the fourth element of the Keys of the map, that are type of Tuple7
     * @param <K5> type of the fifth element of the Keys of the map, that are type of Tuple7
     * @param <K6> type of the sixth element of the Keys of the map, that are type of Tuple7
     * @param <K7> type of the seventh element of the Keys of the map, that are type of Tuple7
     * @param <T>  type of the first element of the Tuple value
     * @param <U>  type of the second element of the Tuple value
     */
    interface TupleValues2<K1, K2, K3, K4, K5, K6, K7, T, U> extends TupleValues1<K1, K2, K3, K4, K5, K6, K7, T>,
            io.github.cruisoring.repository.TupleValues2<Tuple7<K1, K2, K3, K4, K5, K6, K7>, T, U> {

        /**
         * Retrieve the second element of the Tuple as type of <tt>U</tt>
         *
         * @param k1 first element of actual key of the Tuple, with type of <tt>K1</tt>
         * @param k2 second element of actual key of the Tuple, with type of <tt>K2</tt>
         * @param k3 third element of actual key of the Tuple, with type of <tt>K3</tt>
         * @param k4 fourth element of actual key of the Tuple, with type of <tt>K4</tt>
         * @param k5 fifth element of actual key of the Tuple, with type of <tt>K5</tt>
         * @param k6 sixth element of actual key of the Tuple, with type of <tt>K6</tt>
         * @param k7 seventh element of actual key of the Tuple, with type of <tt>K7</tt>
         * @return the second element of the Tuple
         */
        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getSecondValue(Tuple.create(k1, k2, k3, k4, k5, k6, k7));
        }
    }

    /**
     * Interface for map.keys that are of <tt>Tuple</tt> of 7 elements and keep the methods to use them as strong-typed
     * values to fetch mapped values of Tuple composed by 3 elements
     *
     * @param <K1> type of the first element of the Keys of the map, that are type of Tuple7
     * @param <K2> type of the second element of the Keys of the map, that are type of Tuple7
     * @param <K3> type of the third element of the Keys of the map, that are type of Tuple7
     * @param <K4> type of the fourth element of the Keys of the map, that are type of Tuple7
     * @param <K5> type of the fifth element of the Keys of the map, that are type of Tuple7
     * @param <K6> type of the sixth element of the Keys of the map, that are type of Tuple7
     * @param <K7> type of the seventh element of the Keys of the map, that are type of Tuple7
     * @param <T>  type of the first element of the Tuple value
     * @param <U>  type of the second element of the Tuple value
     * @param <V>  type of the third element of the Tuple value
     */
    interface TupleValues3<K1, K2, K3, K4, K5, K6, K7, T, U, V> extends TupleValues2<K1, K2, K3, K4, K5, K6, K7, T, U>,
            io.github.cruisoring.repository.TupleValues3<Tuple7<K1, K2, K3, K4, K5, K6, K7>, T, U, V> {

        /**
         * Retrieve the third element of the Tuple as type of <tt>V</tt>
         *
         * @param k1 first element of actual key of the Tuple, with type of <tt>K1</tt>
         * @param k2 second element of actual key of the Tuple, with type of <tt>K2</tt>
         * @param k3 third element of actual key of the Tuple, with type of <tt>K3</tt>
         * @param k4 fourth element of actual key of the Tuple, with type of <tt>K4</tt>
         * @param k5 fifth element of actual key of the Tuple, with type of <tt>K5</tt>
         * @param k6 sixth element of actual key of the Tuple, with type of <tt>K6</tt>
         * @param k7 seventh element of actual key of the Tuple, with type of <tt>K7</tt>
         * @return the third element of the Tuple value
         */
        default V getThird(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getThirdValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }
    }

    /**
     * Interface for map.keys that are of <tt>Tuple</tt> of 7 elements and keep the methods to use them as strong-typed
     * values to fetch mapped values of Tuple composed by 4 elements
     *
     * @param <K1> type of the first element of the Keys of the map, that are type of Tuple7
     * @param <K2> type of the second element of the Keys of the map, that are type of Tuple7
     * @param <K3> type of the third element of the Keys of the map, that are type of Tuple7
     * @param <K4> type of the fourth element of the Keys of the map, that are type of Tuple7
     * @param <K5> type of the fifth element of the Keys of the map, that are type of Tuple7
     * @param <K6> type of the sixth element of the Keys of the map, that are type of Tuple7
     * @param <K7> type of the seventh element of the Keys of the map, that are type of Tuple7
     * @param <T>  type of the first element of the Tuple value
     * @param <U>  type of the second element of the Tuple value
     * @param <V>  type of the third element of the Tuple value
     * @param <W>  type of the fourth element of the Tuple value
     */
    interface TupleValues4<K1, K2, K3, K4, K5, K6, K7, T, U, V, W> extends TupleValues3<K1, K2, K3, K4, K5, K6, K7, T, U, V>,
            io.github.cruisoring.repository.TupleValues4<Tuple7<K1, K2, K3, K4, K5, K6, K7>, T, U, V, W> {

        /**
         * Retrieve the fourth element of the Tuple as type of <tt>W</tt>
         *
         * @param k1 first element of actual key of the Tuple, with type of <tt>K1</tt>
         * @param k2 second element of actual key of the Tuple, with type of <tt>K2</tt>
         * @param k3 third element of actual key of the Tuple, with type of <tt>K3</tt>
         * @param k4 fourth element of actual key of the Tuple, with type of <tt>K4</tt>
         * @param k5 fifth element of actual key of the Tuple, with type of <tt>K5</tt>
         * @param k6 sixth element of actual key of the Tuple, with type of <tt>K6</tt>
         * @param k7 seventh element of actual key of the Tuple, with type of <tt>K7</tt>
         * @return the fourth element of the Tuple value
         */
        default W getFourth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getFourthValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }
    }

    /**
     * Interface for map.keys that are of <tt>Tuple</tt> of 7 elements and keep the methods to use them as strong-typed
     * values to fetch mapped values of Tuple composed by 5 elements
     *
     * @param <K1> type of the first element of the Keys of the map, that are type of Tuple7
     * @param <K2> type of the second element of the Keys of the map, that are type of Tuple7
     * @param <K3> type of the third element of the Keys of the map, that are type of Tuple7
     * @param <K4> type of the fourth element of the Keys of the map, that are type of Tuple7
     * @param <K5> type of the fifth element of the Keys of the map, that are type of Tuple7
     * @param <K6> type of the sixth element of the Keys of the map, that are type of Tuple7
     * @param <K7> type of the seventh element of the Keys of the map, that are type of Tuple7
     * @param <T>  type of the first element of the Tuple value
     * @param <U>  type of the second element of the Tuple value
     * @param <V>  type of the third element of the Tuple value
     * @param <W>  type of the fourth element of the Tuple value
     * @param <X>  type of the fifth element of the Tuple value
     */
    interface TupleValues5<K1, K2, K3, K4, K5, K6, K7, T, U, V, W, X> extends TupleValues4<K1, K2, K3, K4, K5, K6, K7, T, U, V, W>,
            io.github.cruisoring.repository.TupleValues5<Tuple7<K1, K2, K3, K4, K5, K6, K7>, T, U, V, W, X> {

        /**
         * Retrieve the fifth element of the Tuple as type of <tt>X</tt>
         *
         * @param k1 first element of actual key of the Tuple, with type of <tt>K1</tt>
         * @param k2 second element of actual key of the Tuple, with type of <tt>K2</tt>
         * @param k3 third element of actual key of the Tuple, with type of <tt>K3</tt>
         * @param k4 fourth element of actual key of the Tuple, with type of <tt>K4</tt>
         * @param k5 fifth element of actual key of the Tuple, with type of <tt>K5</tt>
         * @param k6 sixth element of actual key of the Tuple, with type of <tt>K6</tt>
         * @param k7 seventh element of actual key of the Tuple, with type of <tt>K7</tt>
         * @return the fifth element of the Tuple value
         */
        default X getFifth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getFifthValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }
    }

    /**
     * Interface for map.keys that are of <tt>Tuple</tt> of 7 elements and keep the methods to use them as strong-typed
     * values to fetch mapped values of Tuple composed by 6 elements
     *
     * @param <K1> type of the first element of the Keys of the map, that are type of Tuple7
     * @param <K2> type of the second element of the Keys of the map, that are type of Tuple7
     * @param <K3> type of the third element of the Keys of the map, that are type of Tuple7
     * @param <K4> type of the fourth element of the Keys of the map, that are type of Tuple7
     * @param <K5> type of the fifth element of the Keys of the map, that are type of Tuple7
     * @param <K6> type of the sixth element of the Keys of the map, that are type of Tuple7
     * @param <K7> type of the seventh element of the Keys of the map, that are type of Tuple7
     * @param <T>  type of the first element of the Tuple value
     * @param <U>  type of the second element of the Tuple value
     * @param <V>  type of the third element of the Tuple value
     * @param <W>  type of the fourth element of the Tuple value
     * @param <X>  type of the fifth element of the Tuple value
     * @param <Y>  type of the sixth element of the Tuple value
     */
    interface TupleValues6<K1, K2, K3, K4, K5, K6, K7, T, U, V, W, X, Y> extends TupleValues5<K1, K2, K3, K4, K5, K6, K7, T, U, V, W, X>,
            io.github.cruisoring.repository.TupleValues6<Tuple7<K1, K2, K3, K4, K5, K6, K7>, T, U, V, W, X, Y> {

        /**
         * Retrieve the sixth element of the Tuple as type of <tt>Y</tt>
         *
         * @param k1 first element of actual key of the Tuple, with type of <tt>K1</tt>
         * @param k2 second element of actual key of the Tuple, with type of <tt>K2</tt>
         * @param k3 third element of actual key of the Tuple, with type of <tt>K3</tt>
         * @param k4 fourth element of actual key of the Tuple, with type of <tt>K4</tt>
         * @param k5 fifth element of actual key of the Tuple, with type of <tt>K5</tt>
         * @param k6 sixth element of actual key of the Tuple, with type of <tt>K6</tt>
         * @param k7 seventh element of actual key of the Tuple, with type of <tt>K7</tt>
         * @return the sixth element of the Tuple value
         */
        default Y getSixth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getSixthValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }
    }

    /**
     * Interface for map.keys that are of <tt>Tuple</tt> of 7 elements and keep the methods to use them as strong-typed
     * values to fetch mapped values of Tuple composed by 7 elements
     *
     * @param <K1> type of the first element of the Keys of the map, that are type of Tuple7
     * @param <K2> type of the second element of the Keys of the map, that are type of Tuple7
     * @param <K3> type of the third element of the Keys of the map, that are type of Tuple7
     * @param <K4> type of the fourth element of the Keys of the map, that are type of Tuple7
     * @param <K5> type of the fifth element of the Keys of the map, that are type of Tuple7
     * @param <K6> type of the sixth element of the Keys of the map, that are type of Tuple7
     * @param <K7> type of the seventh element of the Keys of the map, that are type of Tuple7
     * @param <T>  type of the first element of the Tuple value
     * @param <U>  type of the second element of the Tuple value
     * @param <V>  type of the third element of the Tuple value
     * @param <W>  type of the fourth element of the Tuple value
     * @param <X>  type of the fifth element of the Tuple value
     * @param <Y>  type of the sixth element of the Tuple value
     */
    interface TupleValues7<K1, K2, K3, K4, K5, K6, K7, T, U, V, W, X, Y, Z> extends TupleValues6<K1, K2, K3, K4, K5, K6, K7, T, U, V, W, X, Y>,
            io.github.cruisoring.repository.TupleValues7<Tuple7<K1, K2, K3, K4, K5, K6, K7>, T, U, V, W, X, Y, Z> {

        /**
         * Retrieve the seventh element of the Tuple as type of <tt>Z</tt>
         *
         * @param k1 first element of actual key of the Tuple, with type of <tt>K1</tt>
         * @param k2 second element of actual key of the Tuple, with type of <tt>K2</tt>
         * @param k3 third element of actual key of the Tuple, with type of <tt>K3</tt>
         * @param k4 fourth element of actual key of the Tuple, with type of <tt>K4</tt>
         * @param k5 fifth element of actual key of the Tuple, with type of <tt>K5</tt>
         * @param k6 sixth element of actual key of the Tuple, with type of <tt>K6</tt>
         * @param k7 seventh element of actual key of the Tuple, with type of <tt>K7</tt>
         * @return the seventh element of the Tuple value
         */
        default Z getSeventh(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getSeventhValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }
    }
}
