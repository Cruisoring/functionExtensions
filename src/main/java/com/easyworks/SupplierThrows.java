package com.easyworks;

@FunctionalInterface
public interface SupplierThrows<T> {
    T get() throws Exception;
}