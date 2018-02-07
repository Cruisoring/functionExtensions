package com.easyworks.function;

import com.easyworks.NoThrows;
import com.easyworks.RuntimeThrows;

@FunctionalInterface
public interface PredicateThrows<T> {
    boolean	test(T t) throws Exception;

    default boolean testNoThrows(T t){
        return NoThrows.test(t, this::test);
    }

    default boolean testRuntimeThrows(T t){
        return RuntimeThrows.test(t, this::test);
    }
}