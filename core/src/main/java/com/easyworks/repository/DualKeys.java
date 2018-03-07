package com.easyworks.repository;

import com.easyworks.tuple.*;

public interface DualKeys<K1,K2, T> {

    T retrieve(K1 k1, K2 k2);

    boolean containsKeyOf(K1 k1, K2 k2);

    default Dual<K1, K2> getKey(K1 k1, K2 k2){
        return Tuple.create(k1, k2);
    }

    interface DualKeys2<K1,K2, T,U> extends DualKeys<K1,K2, Dual<T,U>>, DualValues<Dual<K1,K2>, T,U>{
        default T getFirst(K1 k1, K2 k2) {
            return getFirstValue(getKey(k1, k2));
        }

        default U getSecond(K1 k1, K2 k2) {
            return getSecondValue(getKey(k1, k2));
        }
    }

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
