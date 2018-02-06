package com.easyworks;

import com.easyworks.function.*;

/**
 * Helper functions to throw RuntimeException when any Exceptions are encountered, that would terminate the application.
 * Notice: this is a quite controversy implementation as discussed in
 * {@see https://docs.oracle.com/javase/tutorial/essential/exceptions/runtime.html}
 */
public class RuntimeThrows {

    public static void run(RunnableThrows runnable){
        try {
            runnable.run();
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static <T> T get(SupplierThrows<T> supplier){
        try {
            return supplier.get();
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }


    public static void run(RunnableThrows... runnables){
        run(() -> {
            for (int i=0; i<runnables.length; i++) {
                runnables[i].run();
            }
        });
    }

    public static <T> void accept(T t, ConsumerThrows<T> consumer){
        run(() -> consumer.accept(t));
    }

    public static <T> void accept(T t, ConsumerThrows<T>... consumers){
        run(() -> {
            for (int i=0; i<consumers.length; i++) {
                consumers[i].accept(t);
            }
        });
    }

    public static <T> boolean test(T t, PredicateThrows<T> predicate){
        return get(() -> predicate.test(t));
    }

    public static <T> boolean test(T t, PredicateThrows<T>... predicates){
        return get(() -> {
            for (int i=0; i<predicates.length; i++) {
                if(!predicates[i].test(t))
                    return false;
            }
            return true;
        });
    }

    public static <T, U> boolean test(T t, U u, BiPredicateThrows<T, U> predicate){
        return get(()-> predicate.test(t, u));
    }

    public static <T, U> boolean test(T t, U u, BiPredicateThrows<T, U>... predicates){
        return get(()-> {
            for (int i=0; i<predicates.length; i++) {
                if(!predicates[i].test(t, u))
                    return false;
            }
            return true;
        });
    }

    public static <T, U> void accept(T t, U u, BiConsumerThrows<T, U> consumer){
        run(() -> consumer.accept(t, u));
    }

    public static <T, U> void accept(T t, U u, BiConsumerThrows<T, U>... consumers){
        run(() -> {
            for (int i=0; i<consumers.length; i++) {
                consumers[i].accept(t, u);
            }
        });
    }

    public static <T, R> R apply(T t, FunctionThrows<T,R> function){
        return get(()->function.apply(t));
    }

    public static <T, U, R> R apply(T t, U u, BiFunctionThrows<T,U,R> function){
        return get(()->function.apply(t, u));
    }
}
