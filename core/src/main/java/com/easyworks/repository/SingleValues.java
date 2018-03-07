package com.easyworks.repository;

import com.easyworks.tuple.Single;

public interface SingleValues<TKey, T> {

    Single<T> retrieve(TKey key);

    default T getFirstValue(TKey key) {
        Single<T> value = retrieve(key);
        return value == null ? null : value.getFirst();
    }
}
