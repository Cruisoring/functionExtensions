package com.easyworks.function;

@FunctionalInterface
public interface PredicateThrows<T> {
    boolean	test(T t) throws Exception;
}