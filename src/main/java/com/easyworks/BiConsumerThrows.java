package com.easyworks;

@FunctionalInterface
public interface BiConsumerThrows<T, U> {
    void accept(T t, U u) throws Exception;
}