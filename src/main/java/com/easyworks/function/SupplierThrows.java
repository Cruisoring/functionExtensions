package com.easyworks.function;

import com.easyworks.NoThrows;
import com.easyworks.RuntimeThrows;

@FunctionalInterface
public interface SupplierThrows<T> {
    T get() throws Exception;

    default T getNoThrows(T defaultT){
        return NoThrows.get(this::get, defaultT);
    }

    default T getRuntimeThrows(){
        return RuntimeThrows.get(this::get);
    }
}