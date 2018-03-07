package com.easyworks.repository;

import com.easyworks.tuple.*;

public interface PentaKeys<K1,K2,K3,K4,K5, T> {

    T retrieve(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5);

    boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5);

    default Penta<K1,K2,K3,K4,K5> getKey(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5){
        return Tuple.create(k1, k2, k3, k4, k5);
    }

    interface PentaKeys2<K1,K2,K3,K4,K5, T,U> extends PentaKeys<K1,K2,K3,K4,K5, Dual<T,U>>, 
            DualValues<Penta<K1,K2,K3,K4,K5>, T,U>{
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getFirstValue(getKey(k1, k2, k3, k4, k5));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getSecondValue(getKey(k1, k2, k3, k4, k5));
        }
    }

    interface PentaKeys3<K1,K2,K3,K4,K5, T,U,V> extends PentaKeys<K1,K2,K3,K4,K5, Triple<T,U,V>>,
            TripleValues<Penta<K1,K2,K3,K4,K5>, T,U,V> {
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getFirstValue(getKey(k1, k2, k3, k4, k5));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getSecondValue(getKey(k1, k2, k3, k4, k5));
        }

        default V getThird(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getThirdValue(getKey(k1, k2, k3, k4, k5));
        }
    }

    interface PentaKeys4<K1,K2,K3,K4,K5, T,U,V,W> extends PentaKeys<K1,K2,K3,K4,K5, Quad<T,U,V,W>>, 
            QuadValues<Penta<K1,K2,K3,K4,K5>, T,U,V,W>{
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getFirstValue(getKey(k1, k2, k3, k4, k5));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getSecondValue(getKey(k1, k2, k3, k4, k5));
        }

        default V getThird(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getThirdValue(getKey(k1, k2, k3, k4, k5));
        }
        default W getFourth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getFourthValue(getKey(k1, k2, k3, k4, k5));
        }
    }

    interface PentaKeys5<K1,K2,K3,K4,K5, T,U,V,W,X> extends PentaKeys<K1,K2,K3,K4,K5, Penta<T,U,V,W,X>>, 
            PentaValues<Penta<K1,K2,K3,K4,K5>, T,U,V,W,X>{
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getFirstValue(getKey(k1, k2, k3, k4, k5));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getSecondValue(getKey(k1, k2, k3, k4, k5));
        }

        default V getThird(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getThirdValue(getKey(k1, k2, k3, k4, k5));
        }
        default W getFourth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getFourthValue(getKey(k1, k2, k3, k4, k5));
        }

        default X getFifth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getFifthValue(getKey(k1, k2, k3, k4, k5));
        }
    }

    interface PentaKeys6<K1,K2,K3,K4,K5, T,U,V,W,X,Y> extends PentaKeys<K1,K2,K3,K4,K5, Hexa<T,U,V,W,X,Y>>, 
            HexaValues<Penta<K1,K2,K3,K4,K5>, T,U,V,W,X,Y>{
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getFirstValue(getKey(k1, k2, k3, k4, k5));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getSecondValue(getKey(k1, k2, k3, k4, k5));
        }

        default V getThird(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getThirdValue(getKey(k1, k2, k3, k4, k5));
        }
        default W getFourth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getFourthValue(getKey(k1, k2, k3, k4, k5));
        }

        default X getFifth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getFifthValue(getKey(k1, k2, k3, k4, k5));
        }

        default Y getSixth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getSixthValue(getKey(k1, k2, k3, k4, k5));
        }
    }

    interface PentaKeys7<K1,K2,K3,K4,K5, T,U,V,W,X,Y,Z> extends PentaKeys<K1,K2,K3,K4,K5, Hepta<T,U,V,W,X,Y,Z>>, 
            HeptaValues<Penta<K1,K2,K3,K4,K5>, T,U,V,W,X,Y,Z>{
        default T getFirst(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getFirstValue(getKey(k1, k2, k3, k4, k5));
        }

        default U getSecond(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getSecondValue(getKey(k1, k2, k3, k4, k5));
        }

        default V getThird(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getThirdValue(getKey(k1, k2, k3, k4, k5));
        }
        default W getFourth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getFourthValue(getKey(k1, k2, k3, k4, k5));
        }

        default X getFifth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getFifthValue(getKey(k1, k2, k3, k4, k5));
        }

        default Y getSixth(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getSixthValue(getKey(k1, k2, k3, k4, k5));
        }

        default Z getSeventh(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return getSeventhValue(getKey(k1, k2, k3, k4, k5));
        }
    }
}
