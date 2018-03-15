package com.easyworks.function;

public interface Supplierable<R> extends AbstractThrowable {
    static <R> R getDefaultValue(Class<R> rClass){
        return null;
    }
}
