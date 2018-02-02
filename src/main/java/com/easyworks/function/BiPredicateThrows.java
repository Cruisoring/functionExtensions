package com.easyworks.function;

@FunctionalInterface
public interface BiPredicateThrows<T, U> {
    boolean	test(T t, U u) throws Exception;
}