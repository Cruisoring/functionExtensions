package com.easyworks.repository;

import com.easyworks.tuple.*;

/**
 * Interface for map.keys that are of <tt>Tuple</tt> of 4 elements and keep the methods to use them as strong-typed
 * values to fetch strong typed elements
 * @param <K1>  type of the first element of the Keys of the map, that are type of Quad
 * @param <K2>  type of the second element of the Keys of the map, that are type of Quad
 * @param <K3>  type of the third element of the Keys of the map, that are type of Quad
 * @param <K4>  type of the fourth element of the Keys of the map, that are type of Quad
 */
public interface QuadKeys<K1,K2,K3,K4> extends TupleKeys<Quad<K1,K2,K3,K4>> {

    /**
     * Get the strong-typed Tuple that matched with the actual Key of the concerned map
     *
     * @param k1    first element of actual key of the Tuple, with type of <tt>K1</tt>
     * @param k2    second element of actual key of the Tuple, with type of <tt>K2</tt>
     * @return  Tuple composed by the above elements to be used as the actual key of the map
     */
    default Quad<K1,K2,K3,K4> getKey(K1 k1, K2 k2, K3 k3, K4 k4){
        return Tuple.create(k1, k2, k3, k4);
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the specified Tuple key.
     *
     * @param k1    first element of actual key of the Tuple, with type of <tt>K1</tt>
     * @param k2    second element of actual key of the Tuple, with type of <tt>K2</tt>
     * @return <tt>true</tt> if this map contains a mapping for the specified Tuple key
     */
    default boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4){
        return containsKey(getKey(k1, k2, k3, k4));
    }

    /**
     * Interface for map.keys that are of <tt>Tuple</tt> of 4 elements and keep the methods to use them as strong-typed
     * values to fetch mapped values of Tuple.Quad composed by 1 element
     * @param <K1>  type of the first element of the Keys of the map, that are type of Quad
     * @param <K2>  type of the second element of the Keys of the map, that are type of Quad
     * @param <K3>  type of the third element of the Keys of the map, that are type of Quad
     * @param <K4>  type of the fourth element of the Keys of the map, that are type of Quad
     * @param <T>   type of the first element of the Tuple value
     */
    interface SingleValues<K1,K2,K3,K4, T> extends QuadKeys<K1,K2,K3,K4>,
            com.easyworks.repository.SingleValues<Quad<K1,K2,K3,K4>, T> {

        default Tuple retrieve(K1 k1, K2 k2, K3 k3, K4 k4){
            return retrieve(getKey(k1, k2, k3, k4));
        }

        /**
         * Retrieve the first value of the Tuple as type of <tt>T</tt>
         *
         * @param k1    first element of actual key of the Tuple, with type of <tt>K1</tt>
         * @param k2    second element of actual key of the Tuple, with type of <tt>K2</tt>
         * @return      the first element of the Tuple value
         */
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getFirstValue(getKey(k1, k2, k3, k4));
        }
    }

    /**
     * Interface for map.keys that are of <tt>Tuple</tt> of 4 elements and keep the methods to use them as strong-typed
     * values to fetch mapped values of Tuple composed by 4 elements
     * @param <K1>  type of the first element of the Keys of the map, that are type of Quad
     * @param <K2>  type of the second element of the Keys of the map, that are type of Quad
     * @param <K3>  type of the third element of the Keys of the map, that are type of Quad
     * @param <K4>  type of the fourth element of the Keys of the map, that are type of Quad
     * @param <T>   type of the first element of the Tuple value
     * @param <U>   type of the second element of the Tuple value
     */
    interface DualValues<K1,K2,K3,K4, T,U> extends SingleValues<K1,K2,K3,K4, T>,
            com.easyworks.repository.DualValues<Quad<K1,K2,K3,K4>, T,U> {

        /**
         * Retrieve the second element of the Tuple as type of <tt>U</tt>
         *
         * @param k1    first element of actual key of the Tuple, with type of <tt>K1</tt>
         * @param k2    second element of actual key of the Tuple, with type of <tt>K2</tt>
         * @return      the second element of the Tuple
         */
        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getSecondValue(Tuple.create(k1, k2, k3, k4));
        }
    }

    /**
     * Interface for map.keys that are of <tt>Tuple</tt> of 4 elements and keep the methods to use them as strong-typed
     * values to fetch mapped values of Tuple composed by 3 elements
     * @param <K1>  type of the first element of the Keys of the map, that are type of Quad
     * @param <K2>  type of the second element of the Keys of the map, that are type of Quad
     * @param <K3>  type of the third element of the Keys of the map, that are type of Quad
     * @param <K4>  type of the fourth element of the Keys of the map, that are type of Quad
     * @param <T>   type of the first element of the Tuple value
     * @param <U>   type of the second element of the Tuple value
     * @param <V>   type of the third element of the Tuple value
     */
    interface TripleValues<K1,K2,K3,K4, T,U,V> extends DualValues<K1,K2,K3,K4, T,U>,
            com.easyworks.repository.TripleValues<Quad<K1,K2,K3,K4>, T,U,V> {

        /**
         * Retrieve the third element of the Tuple as type of <tt>V</tt>
         *
         * @param k1    first element of actual key of the Tuple, with type of <tt>K1</tt>
         * @param k2    second element of actual key of the Tuple, with type of <tt>K2</tt>
         * @return      the third element of the Tuple value
         */
        default V getThird(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getThirdValue(getKey(k1, k2, k3, k4));
        }
    }

    /**
     * Interface for map.keys that are of <tt>Tuple</tt> of 4 elements and keep the methods to use them as strong-typed
     * values to fetch mapped values of Tuple composed by 4 elements
     * @param <K1>  type of the first element of the Keys of the map, that are type of Quad
     * @param <K2>  type of the second element of the Keys of the map, that are type of Quad
     * @param <K3>  type of the third element of the Keys of the map, that are type of Quad
     * @param <K4>  type of the fourth element of the Keys of the map, that are type of Quad
     * @param <T>   type of the first element of the Tuple value
     * @param <U>   type of the second element of the Tuple value
     * @param <V>   type of the third element of the Tuple value
     * @param <W>   type of the fourth element of the Tuple value
     */
    interface QuadValues<K1,K2,K3,K4, T,U,V,W> extends TripleValues<K1,K2,K3,K4, T,U,V>,
            com.easyworks.repository.QuadValues<Quad<K1,K2,K3,K4>, T,U,V,W> {

        /**
         * Retrieve the fourth element of the Tuple as type of <tt>W</tt>
         *
         * @param k1    first element of actual key of the Tuple, with type of <tt>K1</tt>
         * @param k2    second element of actual key of the Tuple, with type of <tt>K2</tt>
         * @return      the fourth element of the Tuple value
         */
        default W getFourth(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getFourthValue(getKey(k1, k2, k3, k4));
        }
    }

    /**
     * Interface for map.keys that are of <tt>Tuple</tt> of 4 elements and keep the methods to use them as strong-typed
     * values to fetch mapped values of Tuple composed by 5 elements
     * @param <K1>  type of the first element of the Keys of the map, that are type of Quad
     * @param <K2>  type of the second element of the Keys of the map, that are type of Quad
     * @param <K3>  type of the third element of the Keys of the map, that are type of Quad
     * @param <K4>  type of the fourth element of the Keys of the map, that are type of Quad
     * @param <T>   type of the first element of the Tuple value
     * @param <U>   type of the second element of the Tuple value
     * @param <V>   type of the third element of the Tuple value
     * @param <W>   type of the fourth element of the Tuple value
     * @param <X>   type of the fifth element of the Tuple value
     */
    interface PentaValues<K1,K2,K3,K4, T,U,V,W,X> extends QuadValues<K1,K2,K3,K4, T,U,V,W>,
            com.easyworks.repository.PentaValues<Quad<K1,K2,K3,K4>, T,U,V,W,X> {

        /**
         * Retrieve the fifth element of the Tuple as type of <tt>X</tt>
         *
         * @param k1    first element of actual key of the Tuple, with type of <tt>K1</tt>
         * @param k2    second element of actual key of the Tuple, with type of <tt>K2</tt>
         * @return      the fifth element of the Tuple value
         */
        default X getFifth(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getFifthValue(getKey(k1, k2, k3, k4));
        }
    }

    /**
     * Interface for map.keys that are of <tt>Tuple</tt> of 4 elements and keep the methods to use them as strong-typed
     * values to fetch mapped values of Tuple composed by 6 elements
     * @param <K1>  type of the first element of the Keys of the map, that are type of Quad
     * @param <K2>  type of the second element of the Keys of the map, that are type of Quad
     * @param <K3>  type of the third element of the Keys of the map, that are type of Quad
     * @param <K4>  type of the fourth element of the Keys of the map, that are type of Quad
     * @param <T>   type of the first element of the Tuple value
     * @param <U>   type of the second element of the Tuple value
     * @param <V>   type of the third element of the Tuple value
     * @param <W>   type of the fourth element of the Tuple value
     * @param <X>   type of the fifth element of the Tuple value
     * @param <Y>   type of the sixth element of the Tuple value
     */
    interface HexaValues<K1,K2,K3,K4, T,U,V,W,X,Y> extends PentaValues<K1,K2,K3,K4, T,U,V,W,X>,
            com.easyworks.repository.HexaValues<Quad<K1,K2,K3,K4>, T,U,V,W,X,Y> {

        /**
         * Retrieve the sixth element of the Tuple as type of <tt>Y</tt>
         *
         * @param k1    first element of actual key of the Tuple, with type of <tt>K1</tt>
         * @param k2    second element of actual key of the Tuple, with type of <tt>K2</tt>
         * @return      the sixth element of the Tuple value
         */
        default Y getSixth(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getSixthValue(getKey(k1, k2, k3, k4));
        }
    }

    /**
     * Interface for map.keys that are of <tt>Tuple</tt> of 4 elements and keep the methods to use them as strong-typed
     * values to fetch mapped values of Tuple composed by 7 elements
     * @param <K1>  type of the first element of the Keys of the map, that are type of Quad
     * @param <K2>  type of the second element of the Keys of the map, that are type of Quad
     * @param <K3>  type of the third element of the Keys of the map, that are type of Quad
     * @param <K4>  type of the fourth element of the Keys of the map, that are type of Quad
     * @param <T>   type of the first element of the Tuple value
     * @param <U>   type of the second element of the Tuple value
     * @param <V>   type of the third element of the Tuple value
     * @param <W>   type of the fourth element of the Tuple value
     * @param <X>   type of the fifth element of the Tuple value
     * @param <Y>   type of the sixth element of the Tuple value
     * @param <Y>   type of the seventh element of the Tuple value
     */
    interface HeptaValues<K1,K2,K3,K4, T,U,V,W,X,Y,Z> extends HexaValues<K1,K2,K3,K4, T,U,V,W,X,Y>,
            com.easyworks.repository.HeptaValues<Quad<K1,K2,K3,K4>, T,U,V,W,X,Y,Z> {

        /**
         * Retrieve the seventh element of the Tuple as type of <tt>Z</tt>
         *
         * @param k1    first element of actual key of the Tuple, with type of <tt>K1</tt>
         * @param k2    second element of actual key of the Tuple, with type of <tt>K2</tt>
         * @return      the seventh element of the Tuple value
         */
        default Z getSeventh(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getSeventhValue(getKey(k1, k2, k3, k4));
        }
    }
}
