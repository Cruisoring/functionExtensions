package com.easyworks.repository;

import com.easyworks.tuple.Penta;

public interface PentaValues<TKey, T,U,V,W,X> {

    Penta<T,U,V,W,X> retrieve(TKey key);

    default T getFirstValue(TKey key) {
        Penta<T,U,V,W,X> value = retrieve(key);
        return value == null ? null : value.getFirst();
    }

    default U getSecondValue(TKey key) {
        Penta<T,U,V,W,X> value = retrieve(key);
        return value == null ? null : value.getSecond();
    }

    default V getThirdValue(TKey key) {
        Penta<T,U,V,W,X> value = retrieve(key);
        return value == null ? null : value.getThird();
    }

    default W getFourthValue(TKey key) {
        Penta<T,U,V,W,X> value = retrieve(key);
        return value == null ? null : value.getFourth();
    }

    default X getFifthValue(TKey key) {
        Penta<T,U,V,W,X> value = retrieve(key);
        return value == null ? null : value.getFifth();
    }
}
