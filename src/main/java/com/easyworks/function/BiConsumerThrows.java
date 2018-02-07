package com.easyworks.function;

import com.easyworks.NoThrows;
import com.easyworks.RuntimeThrows;

@FunctionalInterface
public interface BiConsumerThrows<T, U> {
    void accept(T t, U u) throws Exception;


    default void acceptNoThrows(T t, U u){
        NoThrows.accept(t, u, this::accept);
    }

    default void acceptRuntimeThrows(T t, U u){
        RuntimeThrows.accept(t, u, this::accept);
    }

}