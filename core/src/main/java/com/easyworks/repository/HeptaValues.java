package com.easyworks.repository;

import com.easyworks.tuple.Hepta;

public interface HeptaValues<TKey, T,U,V,W,X,Y,Z> {

    Hepta<T,U,V,W,X,Y,Z> retrieve(TKey key);

    default T getFirstValue(TKey key) {
        Hepta<T,U,V,W,X,Y,Z> value = retrieve(key);
        return value == null ? null : value.getFirst();
    }

    default U getSecondValue(TKey key) {
        Hepta<T,U,V,W,X,Y,Z> value = retrieve(key);
        return value == null ? null : value.getSecond();
    }

    default V getThirdValue(TKey key) {
        Hepta<T,U,V,W,X,Y,Z> value = retrieve(key);
        return value == null ? null : value.getThird();
    }

    default W getFourthValue(TKey key) {
        Hepta<T,U,V,W,X,Y,Z> value = retrieve(key);
        return value == null ? null : value.getFourth();
    }

    default X getFifthValue(TKey key) {
        Hepta<T,U,V,W,X,Y,Z> value = retrieve(key);
        return value == null ? null : value.getFifth();
    }

    default Y getSixthValue(TKey key) {
        Hepta<T,U,V,W,X,Y,Z> value = retrieve(key);
        return value == null ? null : value.getSixth();
    }

    default Z getSeventhValue(TKey key) {
        Hepta<T,U,V,W,X,Y,Z> value = retrieve(key);
        return value == null ? null : value.getSeventh();
    }
}
