package com.easyworks.function;

@FunctionalInterface
public interface ConsumerThrows<T> {
    void accept(T t) throws Exception;
}