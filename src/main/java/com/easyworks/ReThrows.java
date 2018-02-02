package com.easyworks;

/**
 * Helper functions to throw RuntimeException when any Exceptions are encountered.
 */
public class ReThrows {

    public static <T> boolean test(PredicateThrows<T> predicate, T t){
        try {
            return predicate.test(t);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static <T, U> boolean test(BiPredicateThrows<T, U> predicate, T t, U u){
        try {
            return predicate.test(t, u);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

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

    public static <T> void accept(ConsumerThrows<T> consumer, T t){
        try {
            consumer.accept(t);
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static <T, U> void accept(BiConsumerThrows<T, U> consumer, T t, U u){
        try {
            consumer.accept(t, u);
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static <T,R> R apply(FunctionThrows<T,R> function, T t){
        try {
            return function.apply(t);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static <T, U,R> R apply(BiFunctionThrows<T,U,R> function, T t, U u){
        try {
            return function.apply(t, u);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

}
