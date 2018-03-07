package com.easyworks.repository;

import com.easyworks.tuple.Quad;

public interface QuadValues<TKey, T,U,V,W> {

    Quad<T,U,V,W> retrieve(TKey key);

    default T getFirstValue(TKey key) {
        Quad<T, U, V, W> value = retrieve(key);
        return value == null ? null : value.getFirst();
    }

    default U getSecondValue(TKey key) {
        Quad<T, U, V, W> value = retrieve(key);
        return value == null ? null : value.getSecond();
    }

    default V getThirdValue(TKey key) {
        Quad<T, U, V, W> value = retrieve(key);
        return value == null ? null : value.getThird();
    }

    default W getFourthValue(TKey key) {
        Quad<T, U, V, W> value = retrieve(key);
        return value == null ? null : value.getFourth();
    }
}
