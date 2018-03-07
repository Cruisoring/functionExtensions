package com.easyworks.repository;

import com.easyworks.tuple.Triple;

public interface TripleValues<TKey, T,U,V> {

    Triple<T,U,V> retrieve(TKey key);

    default T getFirstValue(TKey key) {
        Triple<T,U,V> value = retrieve(key);
        return value == null ? null : value.getFirst();
    }

    default U getSecondValue(TKey key) {
        Triple<T,U,V> value = retrieve(key);
        return value == null ? null : value.getSecond();
    }

    default V getThirdValue(TKey key) {
        Triple<T,U,V> value = retrieve(key);
        return value == null ? null : value.getThird();
    }
}
