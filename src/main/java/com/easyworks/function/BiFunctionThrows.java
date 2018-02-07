package com.easyworks.function;

import com.easyworks.NoThrows;
import com.easyworks.RuntimeThrows;

@FunctionalInterface
public interface BiFunctionThrows<T, U, R> {
    R apply(T t, U u) throws Exception;

    default R applyNoThrows(T t, U u, R defaultR){
        return NoThrows.apply(t, u, this::apply, defaultR);
    }

    default R applyRuntimeThrows(T t, U u){
        return RuntimeThrows.apply(t, u, this::apply);
    }
}