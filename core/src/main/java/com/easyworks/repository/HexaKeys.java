package com.easyworks.repository;

import com.easyworks.tuple.*;

public interface HexaKeys<K1,K2,K3,K4,K5,K6, T> {

    T retrieve(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6);

    boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6);

    default Hexa<K1,K2,K3,K4,K5,K6> getKey(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6){
        return Tuple.create(k1, k2, k3, k4, k5, k6);
    }

    /**
     * Interface for map.keys that are of <tt>Tuple.Hexa</tt> of 6 elements and keep the methods to use them as strong-typed
     * values to fetch mapped values of Tuple.Single composed by 1 elements of types <tt>T</tt>
     * @param <K1>  type of the first element of the Keys of the map
     * @param <K2>  type of the second element of the Keys of the map
     * @param <K3>  type of the third element of the Keys of the map
     * @param <K4>  type of the fourth element of the Keys of the map
     * @param <K5>  type of the fifth element of the Keys of the map
     * @param <K6>  type of the sixth element of the Keys of the map
     * @param <T>   type of the first element of stored value (Tuple.Single composed by 1 element of types <tt>T</tt>
     */
    interface HexaKeys1<K1,K2,K3,K4,K5,K6, T> extends HexaKeys<K1,K2,K3,K4,K5,K6, Single<T>>, SingleValues<Hexa<K1,K2,K3,K4,K5,K6>, T>{
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getFirstValue(getKey(k1, k2, k3, k4, k5, k6));
        }
    }

    interface HexaKeys2<K1,K2,K3,K4,K5,K6, T,U> extends HexaKeys<K1,K2,K3,K4,K5,K6, Dual<T,U>>, 
            DualValues<Hexa<K1,K2,K3,K4,K5,K6>, T,U>{
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getFirstValue(getKey(k1, k2, k3, k4, k5, k6));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getSecondValue(getKey(k1, k2, k3, k4, k5, k6));
        }
    }

    interface HexaKeys3<K1,K2,K3,K4,K5,K6, T,U,V> extends HexaKeys<K1,K2,K3,K4,K5,K6, Triple<T,U,V>>,
            TripleValues<Hexa<K1,K2,K3,K4,K5,K6>, T,U,V> {
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getFirstValue(getKey(k1, k2, k3, k4, k5, k6));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getSecondValue(getKey(k1, k2, k3, k4, k5, k6));
        }

        default V getThird(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getThirdValue(getKey(k1, k2, k3, k4, k5, k6));
        }
    }

    interface HexaKeys4<K1,K2,K3,K4,K5,K6, T,U,V,W> extends HexaKeys<K1,K2,K3,K4,K5,K6, Quad<T,U,V,W>>, 
            QuadValues<Hexa<K1,K2,K3,K4,K5,K6>, T,U,V,W>{
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getFirstValue(getKey(k1, k2, k3, k4, k5, k6));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getSecondValue(getKey(k1, k2, k3, k4, k5, k6));
        }

        default V getThird(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getThirdValue(getKey(k1, k2, k3, k4, k5, k6));
        }
        default W getFourth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getFourthValue(getKey(k1, k2, k3, k4, k5, k6));
        }
    }

    interface HexaKeys5<K1,K2,K3,K4,K5,K6, T,U,V,W,X> extends HexaKeys<K1,K2,K3,K4,K5,K6, Penta<T,U,V,W,X>>, 
            PentaValues<Hexa<K1,K2,K3,K4,K5,K6>, T,U,V,W,X>{
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getFirstValue(getKey(k1, k2, k3, k4, k5, k6));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getSecondValue(getKey(k1, k2, k3, k4, k5, k6));
        }

        default V getThird(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getThirdValue(getKey(k1, k2, k3, k4, k5, k6));
        }
        default W getFourth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getFourthValue(getKey(k1, k2, k3, k4, k5, k6));
        }

        default X getFifth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getFifthValue(getKey(k1, k2, k3, k4, k5, k6));
        }
    }

    interface HexaKeys6<K1,K2,K3,K4,K5,K6, T,U,V,W,X,Y> extends HexaKeys<K1,K2,K3,K4,K5,K6, Hexa<T,U,V,W,X,Y>>, 
            HexaValues<Hexa<K1,K2,K3,K4,K5,K6>, T,U,V,W,X,Y>{
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getFirstValue(getKey(k1, k2, k3, k4, k5, k6));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getSecondValue(getKey(k1, k2, k3, k4, k5, k6));
        }

        default V getThird(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getThirdValue(getKey(k1, k2, k3, k4, k5, k6));
        }
        default W getFourth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getFourthValue(getKey(k1, k2, k3, k4, k5, k6));
        }

        default X getFifth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getFifthValue(getKey(k1, k2, k3, k4, k5, k6));
        }

        default Y getSixth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getSixthValue(getKey(k1, k2, k3, k4, k5, k6));
        }
    }

    interface HexaKeys7<K1,K2,K3,K4,K5,K6, T,U,V,W,X,Y,Z> extends HexaKeys<K1,K2,K3,K4,K5,K6, Hepta<T,U,V,W,X,Y,Z>>, 
            HeptaValues<Hexa<K1,K2,K3,K4,K5,K6>, T,U,V,W,X,Y,Z>{
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getFirstValue(getKey(k1, k2, k3, k4, k5, k6));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getSecondValue(getKey(k1, k2, k3, k4, k5, k6));
        }

        default V getThird(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getThirdValue(getKey(k1, k2, k3, k4, k5, k6));
        }
        default W getFourth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getFourthValue(getKey(k1, k2, k3, k4, k5, k6));
        }

        default X getFifth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getFifthValue(getKey(k1, k2, k3, k4, k5, k6));
        }

        default Y getSixth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getSixthValue(getKey(k1, k2, k3, k4, k5, k6));
        }

        default Z getSeventh(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return getSeventhValue(getKey(k1, k2, k3, k4, k5, k6));
        }
    }
}
