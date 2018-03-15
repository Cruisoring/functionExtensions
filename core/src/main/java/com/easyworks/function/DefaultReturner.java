package com.easyworks.function;

@FunctionalInterface
public interface DefaultReturner<T> {
    T returns(AbstractThrowable function);
}
