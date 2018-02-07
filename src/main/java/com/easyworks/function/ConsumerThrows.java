package com.easyworks.function;

import com.easyworks.NoThrows;
import com.easyworks.RuntimeThrows;

@FunctionalInterface
public interface ConsumerThrows<T> {
    void accept(T t) throws Exception;


    default void acceptNoThrows(T t){
        NoThrows.accept(t, this::accept);
    }

    default void acceptRuntimeThrows(T t){
        RuntimeThrows.accept(t, this::accept);
    }

}