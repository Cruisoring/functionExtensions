package com.easyworks.repository;

import com.easyworks.tuple.*;

public interface HeptaKeys<K1,K2,K3,K4,K5,K6,K7, T> {

    T retrieve(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7);

    boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7);

    default Hepta<K1,K2,K3,K4,K5,K6,K7> getKey(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7){
        return Tuple.create(k1, k2, k3, k4, k5, k6, k7);
    }

    interface HeptaKeys2<K1,K2,K3,K4,K5,K6,K7, T,U> extends HeptaKeys<K1,K2,K3,K4,K5,K6,K7, Dual<T,U>>, 
            DualValues<Hepta<K1,K2,K3,K4,K5,K6,K7>, T,U>{
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getFirstValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getSecondValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }
    }

    interface HeptaKeys3<K1,K2,K3,K4,K5,K6,K7, T,U,V> extends HeptaKeys<K1,K2,K3,K4,K5,K6,K7, Triple<T,U,V>>,
            TripleValues<Hepta<K1,K2,K3,K4,K5,K6,K7>, T,U,V> {
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getFirstValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getSecondValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }

        default V getThird(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getThirdValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }
    }

    interface HeptaKeys4<K1,K2,K3,K4,K5,K6,K7, T,U,V,W> extends HeptaKeys<K1,K2,K3,K4,K5,K6,K7, Quad<T,U,V,W>>, 
            QuadValues<Hepta<K1,K2,K3,K4,K5,K6,K7>, T,U,V,W>{
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getFirstValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getSecondValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }

        default V getThird(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getThirdValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }
        default W getFourth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getFourthValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }
    }

    interface HeptaKeys5<K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X> extends HeptaKeys<K1,K2,K3,K4,K5,K6,K7, Penta<T,U,V,W,X>>, 
            PentaValues<Hepta<K1,K2,K3,K4,K5,K6,K7>, T,U,V,W,X>{
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getFirstValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getSecondValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }

        default V getThird(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getThirdValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }
        default W getFourth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getFourthValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }

        default X getFifth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getFifthValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }
    }

    interface HeptaKeys6<K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X,Y> extends HeptaKeys<K1,K2,K3,K4,K5,K6,K7, Hexa<T,U,V,W,X,Y>>, 
            HexaValues<Hepta<K1,K2,K3,K4,K5,K6,K7>, T,U,V,W,X,Y>{
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getFirstValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getSecondValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }

        default V getThird(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getThirdValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }
        default W getFourth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getFourthValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }

        default X getFifth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getFifthValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }

        default Y getSixth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getSixthValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }
    }

    interface HeptaKeys7<K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X,Y,Z> extends HeptaKeys<K1,K2,K3,K4,K5,K6,K7, Hepta<T,U,V,W,X,Y,Z>>, 
            HeptaValues<Hepta<K1,K2,K3,K4,K5,K6,K7>, T,U,V,W,X,Y,Z>{
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getFirstValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getSecondValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }

        default V getThird(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getThirdValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }
        default W getFourth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getFourthValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }

        default X getFifth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getFifthValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }

        default Y getSixth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getSixthValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }

        default Z getSeventh(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return getSeventhValue(getKey(k1, k2, k3, k4, k5, k6, k7));
        }
    }
}
