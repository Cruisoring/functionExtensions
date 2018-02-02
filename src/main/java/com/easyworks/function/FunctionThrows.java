package com.easyworks.function;

@FunctionalInterface
public interface FunctionThrows<T, R> {
    R apply(T t) throws Exception;
}