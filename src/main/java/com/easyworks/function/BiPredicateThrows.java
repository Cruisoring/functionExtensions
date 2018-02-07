package com.easyworks.function;

import com.easyworks.NoThrows;
import com.easyworks.RuntimeThrows;

@FunctionalInterface
public interface BiPredicateThrows<T, U> {
    boolean	test(T t, U u) throws Exception;

    default boolean testNoThrows(T t, U u){
        return NoThrows.test(t, u, this::test);
    }

    default boolean testRuntimeThrows(T t, U u){
        return RuntimeThrows.test(t, u, this::test);
    }

}