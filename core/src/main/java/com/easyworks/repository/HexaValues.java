package com.easyworks.repository;

import com.easyworks.tuple.Hexa;

public interface HexaValues<TKey, T,U,V,W,X,Y> {

    Hexa<T,U,V,W,X,Y> retrieve(TKey key);

    default T getFirstValue(TKey key) {
        Hexa<T,U,V,W,X,Y> value = retrieve(key);
        return value == null ? null : value.getFirst();
    }

    default U getSecondValue(TKey key) {
        Hexa<T,U,V,W,X,Y> value = retrieve(key);
        return value == null ? null : value.getSecond();
    }

    default V getThirdValue(TKey key) {
        Hexa<T,U,V,W,X,Y> value = retrieve(key);
        return value == null ? null : value.getThird();
    }

    default W getFourthValue(TKey key) {
        Hexa<T,U,V,W,X,Y> value = retrieve(key);
        return value == null ? null : value.getFourth();
    }

    default X getFifthValue(TKey key) {
        Hexa<T,U,V,W,X,Y> value = retrieve(key);
        return value == null ? null : value.getFifth();
    }

    default Y getSixthValue(TKey key) {
        Hexa<T,U,V,W,X,Y> value = retrieve(key);
        return value == null ? null : value.getSixth();
    }
}
