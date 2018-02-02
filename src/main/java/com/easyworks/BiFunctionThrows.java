package com.easyworks;

@FunctionalInterface
public interface BiFunctionThrows<T, U, R> {
    R apply(T t, U u) throws Exception;
}