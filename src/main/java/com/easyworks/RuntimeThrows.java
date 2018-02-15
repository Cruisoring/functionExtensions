package com.easyworks;

import com.easyworks.function.*;

/**
 * Helper functions to throw RuntimeException when any Exceptions are encountered, that would terminate the application.
 * Notice: this is a quite controversy implementation as discussed in
 * {@see https://docs.oracle.com/javase/tutorial/essential/exceptions/runtime.html}
 */
public class RuntimeThrows {

    public static void run(RunnableThrowable runnable){
        try {
            runnable.run();
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static <T> T get(SupplierThrowable<T> supplier){
        try {
            return supplier.get();
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static void run(RunnableThrowable... runnables){
        run(() -> {
            for (int i=0; i<runnables.length; i++) {
                runnables[i].run();
            }
        });
    }
}
