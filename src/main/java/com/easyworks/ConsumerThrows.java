package com.easyworks;

@FunctionalInterface
public interface ConsumerThrows<T> {
    void accept(T t) throws Exception;
}