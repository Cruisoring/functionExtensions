package com.easyworks;

import com.easyworks.function.*;

/**
 * Helper functions to hide Exceptions and return the default values instead.
 *
 * Note: Calling FunctionalInterface returning values, like SupplierThrows/FunctionThrows/BiFuntionThrows
 * shall append the defaultValue argument. Generally, it is awkward way to perform and evaluate.
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
        return get(() -> predicate.test(t), false);
    }

    public static <T> boolean test(T t, PredicateThrows<T>... predicates){
        return get(() -> {
            for (int i=0; i<predicates.length; i++) {
                if(!predicates[i].test(t))
                    return false;
            }
            return true;
        }, false);
    }

    public static <T, U> boolean test(T t, U u, BiPredicateThrows<T, U> predicate){
        return get(()-> predicate.test(t, u), false);
    }

    public static <T, U> boolean test(T t, U u, BiPredicateThrows<T, U>... predicates){
        return get(()-> {
            for (int i=0; i<predicates.length; i++) {
                if(!predicates[i].test(t, u))
                    return false;
            }
            return true;
        }, false);
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

    public static <T, R> R apply(T t, FunctionThrows<T,R> function, R defaultValue){
        return get(()->function.apply(t), defaultValue);
    }

    public static <T, U, R> R apply(T t, U u, BiFunctionThrows<T,U,R> function, R defaultValue){
        return get(()->function.apply(t, u), defaultValue);
    }
}
