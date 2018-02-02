package com.easyworks;

@FunctionalInterface
public interface PredicateThrows<T> {
    boolean	test(T t) throws Exception;
}