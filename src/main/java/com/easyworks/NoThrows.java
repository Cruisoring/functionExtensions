package com.easyworks;

import com.easyworks.function.*;

/**
 * Helper functions to hide Exceptions and return the default values instead.
 *
 * Note: Calling FunctionalInterface returning values, like SupplierThrows/FunctionThrows/BiFuntionThrows
 * shall append the defaultOfType argument. Generally, it is awkward way to perform and evaluate.
 * {@code ToResult} is preferred.
 */
public class NoThrows {

    public static void run(RunnableThrows runnable){
        try {
            runnable.run();
        } catch (Exception ex){
        }
    }

    public static <T> T get(SupplierThrows<T> supplier, T defaultValue){
        try {
            return supplier.get();
        }catch (Exception ex){
            return defaultValue;
        }
    }

    public static void run(RunnableThrows... runnables){
        run(() -> {
            for (int i=0; i<runnables.length; i++) {
                runnables[i].run();
            }
        });
    }
}
