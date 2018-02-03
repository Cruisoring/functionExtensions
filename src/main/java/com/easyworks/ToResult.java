package com.easyworks;

import com.easyworks.function.*;
import com.easyworks.utilities.Result;

import java.util.Arrays;

/**
 * Helper methods to always return a Result object to indicate success operation or not,
 *      and optionally, with delivery either returned Value or message of the caught Exception.
*  Higher-Order functions to convert all service logic and inputs to {@code ResultFunction}
 * Notice: Wrapping the functions to enable railway oriented programming.
 */
public class ToResult {

    //region Execute service logic presented as various inputs, return as {@code Result}
    public static Result run(RunnableThrows runnable){
        try {
            runnable.run();
            return Result.Success;
        } catch (Exception ex){
            return new Result(ex);
        }
    }

    public static Result run(RunnableThrows... runnables){
        try {
            //Make it throw Exception when runnables is empty
            for (int i=0; i<runnables.length; i++) {
                runnables[i].run();
            }
            return Result.Success;
        } catch (Exception ex){
            return new Result(ex);
        }
    }

    public static <T> Result accept(T t, ConsumerThrows<T> consumer){
        try {
            consumer.accept(t);
            return Result.Success;
        } catch (Exception ex){
            return new Result(ex);
        }
    }

    public static <T> Result accept(T t, ConsumerThrows<T>... consumers){
        try {
            //Make it throw Exception when consumers is empty
            for (int i=0; i<consumers.length; i++) {
                consumers[i].accept(t);
            }
            return Result.Success;
        } catch (Exception ex){
            return new Result(ex);
        }
    }

    public static <T> Result test(T t, PredicateThrows<T> predicate){
        try {
            return predicate.test(t) ? Result.Success : Result.Failure;
        }catch (Exception ex){
            return new Result(ex);
        }
    }

    public static <T> Result test(T t, PredicateThrows<T>... predicates){
        try {
            //Make it throw Exception when predicates is empty
            for (int i=0; i<predicates.length; i++) {
                if(!predicates[i].test(t))
                    return Result.Failure;
            }
            return Result.Success;
        }catch (Exception ex){
            return new Result(ex);
        }
    }

    public static <T> Result get(SupplierThrows<T> supplier){
        try {
            return new Result(supplier.get());
        }catch (Exception ex){
            return new Result(ex);
        }
    }

    public static <T, U> Result test(T t, U u, BiPredicateThrows<T, U> predicate){
        try {
            return predicate.test(t, u) ? Result.Success : Result.Failure;
        }catch (Exception ex){
            return new Result(ex);
        }
    }

    public static <T, U> Result test(T t, U u, BiPredicateThrows<T, U>... predicates){
        try {
            //Make it throw Exception when predicates is empty
            for (int i=0; i<predicates.length; i++) {
                if(!predicates[i].test(t, u))
                    return Result.Failure;
            }
            return Result.Success;
        }catch (Exception ex){
            return new Result(ex);
        }
    }

    public static <T, U> Result accept(T t, U u, BiConsumerThrows<T, U> consumer){
        try {
            consumer.accept(t, u);
            return Result.Success;
        } catch (Exception ex){
            return new Result(ex);
        }
    }

    public static <T, U> Result accept(T t, U u, BiConsumerThrows<T, U>... consumers){
        try {
            //Make it throw Exception when predicates is empty
            for (int i=0; i<consumers.length; i++) {
                consumers[i].accept(t, u);
            }
            return Result.Success;
        } catch (Exception ex){
            return new Result(ex);
        }
    }

    public static <T, R> Result apply(T t, FunctionThrows<T,R> function){
        try {
            return new Result(function.apply(t));
        }catch (Exception ex){
            return new Result(ex);
        }
    }

    public static <T, U, R> Result apply(T t, U u, BiFunctionThrows<T,U,R> function){
        try {
            return new Result(function.apply(t, u));
        }catch (Exception ex){
            return new Result(ex);
        }
    }
    //endregion

    //region Higher order function to convert all service logic and inputs to {@code ResultFunction}
    public static ResultFunction bind(RunnableThrows runnable){
        return () -> run(runnable);
    }

    public static ResultFunction toResultFunction(RunnableThrows... runnables){
        return () -> run(runnables);
    }

    public static <T> ResultFunction toResultFunction(SupplierThrows<T> supplier){
        return () -> get(supplier);
    }

    public static <T> ResultFunction toResultFunction(T t, ConsumerThrows<T> consumer){
        return () -> ToResult.accept(consumer, t);
    }

    public static <T> ResultFunction toResultFunction(T t, ConsumerThrows<T>... consumers){
        return () -> ToResult.accept(consumers, t);
    }

    public static <T> ResultFunction toResultFunction(T t, PredicateThrows<T> predicate){
        return () -> test(predicate, t);
    }

    public static <T> ResultFunction toResultFunction(T t, PredicateThrows<T>... predicates){
        return () -> test(predicates, t);
    }

    public static <T, U> ResultFunction toResultFunction(T t, U u, BiPredicateThrows<T, U> predicate){
        return () -> test(t, u, predicate);
    }

    public static <T, U> ResultFunction toResultFunction(T t, U u, BiConsumerThrows<T, U> consumer){
        return () -> accept(t, u, consumer);
    }

    public static <T, R> ResultFunction toResultFunction(T t, FunctionThrows<T,R> function){
        return () -> apply(t, function);
    }

    public static <T, U, R> ResultFunction toResultFunction(BiFunctionThrows<T,U,R> function, T t, U u){
        return () -> apply(t, u, function);
    }
    //endregion

}
