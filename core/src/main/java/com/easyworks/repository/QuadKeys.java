package com.easyworks.repository;

import com.easyworks.tuple.*;

public interface QuadKeys<K1,K2,K3,K4, T> {

    T retrieve(K1 k1, K2 k2, K3 k3, K4 k4);

    boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4);

    default Quad<K1,K2,K3,K4> getKey(K1 k1, K2 k2, K3 k3, K4 k4){
        return Tuple.create(k1, k2, k3, k4);
    }

    /**
     * Interface for map.keys that are of <tt>Tuple.Quad</tt> of 4 elements and keep the methods to use them as strong-typed
     * values to fetch mapped values of Tuple.Single composed by 1 elements of types <tt>T</tt>
     * @param <K1>  type of the first element of the Keys of the map
     * @param <K2>  type of the second element of the Keys of the map
     * @param <K3>  type of the third element of the Keys of the map
     * @param <K4>  type of the fourth element of the Keys of the map
     * @param <T>   type of the first element of stored value (Tuple.Single composed by 1 element of types <tt>T</tt>
     */
    interface QuadKeys1<K1,K2,K3,K4, T> extends QuadKeys<K1,K2,K3,K4, Single<T>>, SingleValues<Quad<K1,K2,K3,K4>, T>{
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getFirstValue(getKey(k1, k2, k3, k4));
        }
    }

    interface QuadKeys2<K1,K2,K3,K4, T,U> extends QuadKeys<K1,K2,K3,K4, Dual<T,U>>, 
            DualValues<Quad<K1,K2,K3,K4>, T,U>{
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getFirstValue(getKey(k1, k2, k3, k4));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getSecondValue(getKey(k1, k2, k3, k4));
        }
    }

    interface QuadKeys3<K1,K2,K3,K4, T,U,V> extends QuadKeys<K1,K2,K3,K4, Triple<T,U,V>>,
            TripleValues<Quad<K1,K2,K3,K4>, T,U,V> {
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getFirstValue(getKey(k1, k2, k3, k4));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getSecondValue(getKey(k1, k2, k3, k4));
        }

        default V getThird(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getThirdValue(getKey(k1, k2, k3, k4));
        }
    }

    interface QuadKeys4<K1,K2,K3,K4, T,U,V,W> extends QuadKeys<K1,K2,K3,K4, Quad<T,U,V,W>>, 
            QuadValues<Quad<K1,K2,K3,K4>, T,U,V,W>{
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getFirstValue(getKey(k1, k2, k3, k4));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getSecondValue(getKey(k1, k2, k3, k4));
        }

        default V getThird(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getThirdValue(getKey(k1, k2, k3, k4));
        }
        default W getFourth(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getFourthValue(getKey(k1, k2, k3, k4));
        }
    }

    interface QuadKeys5<K1,K2,K3,K4, T,U,V,W,X> extends QuadKeys<K1,K2,K3,K4, Penta<T,U,V,W,X>>, 
            PentaValues<Quad<K1,K2,K3,K4>, T,U,V,W,X>{
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getFirstValue(getKey(k1, k2, k3, k4));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getSecondValue(getKey(k1, k2, k3, k4));
        }

        default V getThird(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getThirdValue(getKey(k1, k2, k3, k4));
        }
        default W getFourth(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getFourthValue(getKey(k1, k2, k3, k4));
        }

        default X getFifth(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getFifthValue(getKey(k1, k2, k3, k4));
        }
    }

    interface QuadKeys6<K1,K2,K3,K4, T,U,V,W,X,Y> extends QuadKeys<K1,K2,K3,K4, Hexa<T,U,V,W,X,Y>>, 
            HexaValues<Quad<K1,K2,K3,K4>, T,U,V,W,X,Y>{
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getFirstValue(getKey(k1, k2, k3, k4));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getSecondValue(getKey(k1, k2, k3, k4));
        }

        default V getThird(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getThirdValue(getKey(k1, k2, k3, k4));
        }
        default W getFourth(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getFourthValue(getKey(k1, k2, k3, k4));
        }

        default X getFifth(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getFifthValue(getKey(k1, k2, k3, k4));
        }

        default Y getSixth(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getSixthValue(getKey(k1, k2, k3, k4));
        }
    }

    interface QuadKeys7<K1,K2,K3,K4, T,U,V,W,X,Y,Z> extends QuadKeys<K1,K2,K3,K4, Hepta<T,U,V,W,X,Y,Z>>, 
            HeptaValues<Quad<K1,K2,K3,K4>, T,U,V,W,X,Y,Z>{
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getFirstValue(getKey(k1, k2, k3, k4));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getSecondValue(getKey(k1, k2, k3, k4));
        }

        default V getThird(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getThirdValue(getKey(k1, k2, k3, k4));
        }
        default W getFourth(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getFourthValue(getKey(k1, k2, k3, k4));
        }

        default X getFifth(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getFifthValue(getKey(k1, k2, k3, k4));
        }

        default Y getSixth(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getSixthValue(getKey(k1, k2, k3, k4));
        }

        default Z getSeventh(K1 k1, K2 k2, K3 k3, K4 k4) {
            return getSeventhValue(getKey(k1, k2, k3, k4));
        }
    }
}
