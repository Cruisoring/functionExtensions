package com.easyworks.function;

@FunctionalInterface
public interface SupplierThrows<T> {
    T get() throws Exception;
}