package com.easyworks;

@FunctionalInterface
public interface FunctionThrows<T, R> {
    R apply(T t) throws Exception;
}