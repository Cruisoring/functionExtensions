package com.easyworks;

import com.easyworks.function.*;

/**
 * Helper functions to hide Exceptions and return the default values instead.
 *
 * Note: Calling FunctionalInterface returning values, like SupplierThrows/FunctionThrows/BiFuntionThrows
 * shall append the defaultValue argument. Generally, it is awkward way to execute and evaluate.
 * {@code ToResult} is preferred.
 */
public class HideThrows {

    public static <T> boolean test(PredicateThrows<T> predicate, T t){
        try {
            return predicate.test(t);
        }catch (Exception ex){
            return false;
        }
    }

    public static <T, U> boolean test(BiPredicateThrows<T, U> predicate, T t, U u){
        try {
            return predicate.test(t, u);
        }catch (Exception ex){
            return false;
        }
    }

    public static void run(RunnableThrows runnable){
        try {
            runnable.run();
        } catch (Exception ex){
        }
    }

    public static <T> void accept(ConsumerThrows<T> consumer, T t){
        try {
            consumer.accept(t);
        } catch (Exception ex){
        }
    }

    public static <T, U> void accept(BiConsumerThrows<T, U> consumer, T t, U u){
        try {
            consumer.accept(t, u);
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

    public static <T,R> R apply(FunctionThrows<T,R> function, T t, R defaultValue){
        try {
            return function.apply(t);
        }catch (Exception ex){
            return defaultValue;
        }
    }

    public static <T, U,R> R apply(BiFunctionThrows<T,U,R> function, T t, U u, R defaultValue){
        try {
            return function.apply(t, u);
        }catch (Exception ex){
            return defaultValue;
        }
    }

}
