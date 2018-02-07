package com.easyworks.function;

import com.easyworks.NoThrows;
import com.easyworks.RuntimeThrows;

@FunctionalInterface
public interface FunctionThrows<T, R> {
    R apply(T t) throws Exception;

    default R applyNoThrows(T t, R defaultR){
        return NoThrows.apply(t, this::apply, defaultR);
    }

    default R applyRuntimeThrows(T t){
        return RuntimeThrows.apply(t, this::apply);
    }
}