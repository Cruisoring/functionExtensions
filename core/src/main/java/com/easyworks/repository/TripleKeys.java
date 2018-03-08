package com.easyworks.repository;

import com.easyworks.tuple.*;

public interface TripleKeys<K1,K2,K3, T> {

    T retrieve(K1 k1, K2 k2, K3 k3);

    boolean containsKeyOf(K1 k1, K2 k2, K3 k3);

    default Triple<K1, K2, K3> getKey(K1 k1, K2 k2, K3 k3){
        return Tuple.create(k1, k2, k3);
    }

    /**
     * Interface for map.keys that are of <tt>Tuple.Triple</tt> of 3 elements and keep the methods to use them as strong-typed
     * values to fetch mapped values of Tuple.Single composed by 1 elements of types <tt>T</tt>
     * @param <K1>  type of the first element of the Keys of the map
     * @param <K2>  type of the second element of the Keys of the map
     * @param <K3>  type of the third element of the Keys of the map
     * @param <T>   type of the first element of stored value (Tuple.Single composed by 1 element of types <tt>T</tt>
     */
    interface TripleKeys1<K1,K2,K3, T> extends TripleKeys<K1,K2,K3, Single<T>>, SingleValues<Triple<K1,K2,K3>, T>{
        default T getFirst(K1 k1, K2 k2, K3 k3) {
            return getFirstValue(getKey(k1, k2, k3));
        }
    }

    interface TripleKeys2<K1,K2,K3, T,U> extends TripleKeys<K1,K2,K3, Dual<T,U>>,
            DualValues<Triple<K1,K2,K3>, T,U>{
        default T getFirst(K1 k1, K2 k2, K3 k3) {
            return getFirstValue(getKey(k1, k2, k3));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3) {
            return getSecondValue(getKey(k1, k2, k3));
        }
    }

    interface TripleKeys3<K1,K2,K3, T,U,V> extends TripleKeys<K1,K2,K3, Triple<T,U,V>>,
            TripleValues<Triple<K1,K2,K3>, T,U,V> {
        default T getFirst(K1 k1, K2 k2, K3 k3) {
            return getFirstValue(getKey(k1, k2, k3));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3) {
            return getSecondValue(getKey(k1, k2, k3));
        }

        default V getThird(K1 k1, K2 k2, K3 k3) {
            return getThirdValue(getKey(k1, k2, k3));
        }
    }

    interface TripleKeys4<K1,K2,K3, T,U,V,W> extends TripleKeys<K1,K2,K3, Quad<T,U,V,W>>,
            QuadValues<Triple<K1,K2,K3>, T,U,V,W>{
        default T getFirst(K1 k1, K2 k2, K3 k3) {
            return getFirstValue(getKey(k1, k2, k3));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3) {
            return getSecondValue(getKey(k1, k2, k3));
        }

        default V getThird(K1 k1, K2 k2, K3 k3) {
            return getThirdValue(getKey(k1, k2, k3));
        }
        default W getFourth(K1 k1, K2 k2, K3 k3) {
            return getFourthValue(getKey(k1, k2, k3));
        }
    }

    interface TripleKeys5<K1,K2,K3, T,U,V,W,X> extends TripleKeys<K1,K2,K3, Penta<T,U,V,W,X>>,
            PentaValues<Triple<K1,K2,K3>, T,U,V,W,X>{
        default T getFirst(K1 k1, K2 k2, K3 k3) {
            return getFirstValue(getKey(k1, k2, k3));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3) {
            return getSecondValue(getKey(k1, k2, k3));
        }

        default V getThird(K1 k1, K2 k2, K3 k3) {
            return getThirdValue(getKey(k1, k2, k3));
        }
        default W getFourth(K1 k1, K2 k2, K3 k3) {
            return getFourthValue(getKey(k1, k2, k3));
        }

        default X getFifth(K1 k1, K2 k2, K3 k3) {
            return getFifthValue(getKey(k1, k2, k3));
        }
    }

    interface TripleKeys6<K1,K2,K3, T,U,V,W,X,Y> extends TripleKeys<K1,K2,K3, Hexa<T,U,V,W,X,Y>>,
            HexaValues<Triple<K1,K2,K3>, T,U,V,W,X,Y>{
        default T getFirst(K1 k1, K2 k2, K3 k3) {
            return getFirstValue(getKey(k1, k2, k3));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3) {
            return getSecondValue(getKey(k1, k2, k3));
        }

        default V getThird(K1 k1, K2 k2, K3 k3) {
            return getThirdValue(getKey(k1, k2, k3));
        }
        default W getFourth(K1 k1, K2 k2, K3 k3) {
            return getFourthValue(getKey(k1, k2, k3));
        }

        default X getFifth(K1 k1, K2 k2, K3 k3) {
            return getFifthValue(getKey(k1, k2, k3));
        }

        default Y getSixth(K1 k1, K2 k2, K3 k3) {
            return getSixthValue(getKey(k1, k2, k3));
        }
    }

    interface TripleKeys7<K1,K2,K3, T,U,V,W,X,Y,Z> extends TripleKeys<K1,K2,K3, Hepta<T,U,V,W,X,Y,Z>>,
            HeptaValues<Triple<K1,K2,K3>, T,U,V,W,X,Y,Z>{
        default T getFirst(K1 k1, K2 k2, K3 k3) {
            return getFirstValue(getKey(k1, k2, k3));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3) {
            return getSecondValue(getKey(k1, k2, k3));
        }

        default V getThird(K1 k1, K2 k2, K3 k3) {
            return getThirdValue(getKey(k1, k2, k3));
        }
        default W getFourth(K1 k1, K2 k2, K3 k3) {
            return getFourthValue(getKey(k1, k2, k3));
        }

        default X getFifth(K1 k1, K2 k2, K3 k3) {
            return getFifthValue(getKey(k1, k2, k3));
        }

        default Y getSixth(K1 k1, K2 k2, K3 k3) {
            return getSixthValue(getKey(k1, k2, k3));
        }

        default Z getSeventh(K1 k1, K2 k2, K3 k3) {
            return getSeventhValue(getKey(k1, k2, k3));
        }
    }
}
