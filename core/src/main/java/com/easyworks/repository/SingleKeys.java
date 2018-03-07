package com.easyworks.repository;

import com.easyworks.tuple.*;

public interface SingleKeys<TKey> {

    boolean containsKeyOf(TKey key);

    default Single<TKey> getKey(TKey key){
        return Tuple.create(key);
    }

    interface SingleKeys2<TKey, T,U> extends SingleKeys<TKey>, DualValues<Single<TKey>, T,U>{
        
        default T getFirst(TKey key) {
            return getFirstValue(getKey(key));
        }

        default U getSecond(TKey key) {
            return getSecondValue(getKey(key));
        }
    }

    interface SingleKeys3<TKey, T,U,V> extends SingleKeys<TKey>, TripleValues<Single<TKey>, T,U,V> {
        default T getFirst(TKey key) {
            return getFirstValue(getKey(key));
        }

        default U getSecond(TKey key) {
            return getSecondValue(getKey(key));
        }

        default V getThird(TKey key) {
            return getThirdValue(getKey(key));
        }
    }

    interface SingleKeys4<TKey, T,U,V,W> extends SingleKeys<TKey>, QuadValues<Single<TKey>, T,U,V,W>{
        default T getFirst(TKey key) {
            return getFirstValue(getKey(key));
        }

        default U getSecond(TKey key) {
            return getSecondValue(getKey(key));
        }

        default V getThird(TKey key) {
            return getThirdValue(getKey(key));
        }
        default W getFourth(TKey key) {
            return getFourthValue(getKey(key));
        }
    }

    interface SingleKeys5<TKey, T,U,V,W,X> extends SingleKeys<TKey>, PentaValues<Single<TKey>, T,U,V,W,X>{
        default T getFirst(TKey key) {
            return getFirstValue(getKey(key));
        }

        default U getSecond(TKey key) {
            return getSecondValue(getKey(key));
        }

        default V getThird(TKey key) {
            return getThirdValue(getKey(key));
        }
        default W getFourth(TKey key) {
            return getFourthValue(getKey(key));
        }

        default X getFifth(TKey key) {
            return getFifthValue(getKey(key));
        }
    }

    interface SingleKeys6<TKey, T,U,V,W,X,Y> extends SingleKeys<TKey>, HexaValues<Single<TKey>, T,U,V,W,X,Y>{
        default T getFirst(TKey key) {
            return getFirstValue(getKey(key));
        }

        default U getSecond(TKey key) {
            return getSecondValue(getKey(key));
        }

        default V getThird(TKey key) {
            return getThirdValue(getKey(key));
        }
        default W getFourth(TKey key) {
            return getFourthValue(getKey(key));
        }

        default X getFifth(TKey key) {
            return getFifthValue(getKey(key));
        }

        default Y getSixth(TKey key) {
            return getSixthValue(getKey(key));
        }
    }

    interface SingleKeys7<TKey, T,U,V,W,X,Y,Z> extends SingleKeys<TKey>, HeptaValues<Single<TKey>, T,U,V,W,X,Y,Z>{
        default T getFirst(TKey key) {
            return getFirstValue(getKey(key));
        }

        default U getSecond(TKey key) {
            return getSecondValue(getKey(key));
        }

        default V getThird(TKey key) {
            return getThirdValue(getKey(key));
        }
        default W getFourth(TKey key) {
            return getFourthValue(getKey(key));
        }

        default X getFifth(TKey key) {
            return getFifthValue(getKey(key));
        }

        default Y getSixth(TKey key) {
            return getSixthValue(getKey(key));
        }

        default Z getSeventh(TKey key) {
            return getSeventhValue(getKey(key));
        }
    }
}
