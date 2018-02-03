package com.easyworks;

import com.easyworks.function.*;

/**
 * Helper functions to hide Exceptions and return the default values instead.
 *
 * Note: Calling FunctionalInterface returning values, like SupplierThrows/FunctionThrows/BiFuntionThrows
 * shall append the defaultValue argument. Generally, it is awkward way to perform and evaluate.
 * {@code ToResult} is preferred.
 */
public class HideThrows {

    public static void run(RunnableThrows runnable){
        try {
            runnable.run();
        } catch (Exception ex){

        }
    }

    public static void run(RunnableThrows... runnables){
        try {
            //Make it throw Exception when runnables is empty
            for (int i=0; i<runnables.length; i++) {
                runnables[i].run();
            }
        } catch (Exception ex){

        }
    }

    public static <T> void accept(T t, ConsumerThrows<T> consumer){
        try {
            consumer.accept(t);
        } catch (Exception ex){

        }
    }

    public static <T> void accept(T t, ConsumerThrows<T>... consumers){
        try {
            //Make it throw Exception when consumers is empty
            for (int i=0; i<consumers.length; i++) {
                consumers[i].accept(t);
            }
        } catch (Exception ex){

        }
    }

    public static <T> boolean test(T t, PredicateThrows<T> predicate){
        try {
            return predicate.test(t);
        }catch (Exception ex){
            return false;
        }
    }

    public static <T> boolean test(T t, PredicateThrows<T>... predicates){
        try {
            //Make it throw Exception when predicates is empty
            for (int i=0; i<predicates.length; i++) {
                if(!predicates[i].test(t))
                    return false;
            }
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    public static <T> T get(SupplierThrows<T> supplier, T defaultValue){
        try {
            return supplier.get();
        }catch (Exception ex){
            return defaultValue;
        }
    }

    public static <T, U> boolean test(T t, U u, BiPredicateThrows<T, U> predicate){
        try {
            return predicate.test(t, u);
        }catch (Exception ex){
            return false;
        }
    }

    public static <T, U> boolean test(T t, U u, BiPredicateThrows<T, U>... predicates){
        try {
            //Make it throw Exception when predicates is empty
            for (int i=0; i<predicates.length; i++) {
                if(!predicates[i].test(t, u))
                    return false;
            }
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    public static <T, U> void accept(T t, U u, BiConsumerThrows<T, U> consumer){
        try {
            consumer.accept(t, u);
        } catch (Exception ex){
        }
    }

    public static <T, U> void accept(T t, U u, BiConsumerThrows<T, U>... consumers){
        try {
            //Make it throw Exception when predicates is empty
            for (int i=0; i<consumers.length; i++) {
                consumers[i].accept(t, u);
            }
        } catch (Exception ex){
        }
    }

    public static <T, R> R apply(T t, FunctionThrows<T,R> function, R defaultValue){
        try {
            return function.apply(t);
        }catch (Exception ex){
            return defaultValue;
        }
    }

    public static <T, U, R> R apply(T t, U u, BiFunctionThrows<T,U,R> function, R defaultValue){
        try {
            return function.apply(t, u);
        }catch (Exception ex){
            return defaultValue;
        }
    }
}
