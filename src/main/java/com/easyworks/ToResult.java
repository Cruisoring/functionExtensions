package com.easyworks;

import com.easyworks.function.*;
import com.easyworks.utilities.Result;

/**
 * Helper methods to always return a Result object to indicate success operation or not,
 *      and optionally, with delivery either returned value or message of the caught Exception.
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

    public static <T> Result get(SupplierThrows<T> supplier){
        try {
            return new Result(supplier.get());
        }catch (Exception ex){
            return new Result(ex);
        }
    }

    public static <T> Result accept(ConsumerThrows<T> consumer, T t){
        try {
            consumer.accept(t);
            return Result.Success;
        } catch (Exception ex){
            return new Result(ex);
        }
    }

    public static <T> Result test(PredicateThrows<T> predicate, T t){
        try {
            return predicate.test(t) ? Result.Success : Result.Failure;
        }catch (Exception ex){
            return new Result(ex);
        }
    }

    public static <T, U> Result test(BiPredicateThrows<T, U> predicate, T t, U u){
        try {
            return predicate.test(t, u) ? Result.Success : Result.Failure;
        }catch (Exception ex){
            return new Result(ex);
        }
    }

    public static <T, U> Result accept(BiConsumerThrows<T, U> consumer, T t, U u){
        try {
            consumer.accept(t, u);
            return Result.Success;
        } catch (Exception ex){
            return new Result(ex);
        }
    }

    public static <T, R> Result apply(FunctionThrows<T,R> function, T t){
        try {
            return new Result(function.apply(t));
        }catch (Exception ex){
            return new Result(ex);
        }
    }

    public static <T, U, R> Result apply(BiFunctionThrows<T,U,R> function, T t, U u){
        try {
            return new Result(function.apply(t, u));
        }catch (Exception ex){
            return new Result(ex);
        }
    }
    //endregion

    //region Higher order function to convert all service logic and inputs to {@code ResultFunction}
    public static ResultFunction toResultFunction(RunnableThrows runnable){
        return () -> run(runnable);
    }

    public static <T> ResultFunction toResultFunction(SupplierThrows<T> supplier){
        return () -> get(supplier);
    }

    public static <T> ResultFunction toResultFunction(ConsumerThrows<T> consumer, T t){
        return () -> ToResult.accept(consumer, t);
    }

    public static <T> ResultFunction toResultFunction(PredicateThrows<T> predicate, T t){
        return () -> test(predicate, t);
    }

    public static <T, U> ResultFunction toResultFunction(BiPredicateThrows<T, U> predicate, T t, U u){
        return () -> test(predicate, t, u);
    }

    public static <T, U> ResultFunction toResultFunction(BiConsumerThrows<T, U> consumer, T t, U u){
        return () -> accept(consumer, t, u);
    }

    public static <T, R> ResultFunction toResultFunction(FunctionThrows<T,R> function, T t){
        return () -> apply(function, t);
    }

    public static <T, U, R> ResultFunction toResultFunction(BiFunctionThrows<T,U,R> function, T t, U u){
        return () -> apply(function, t, u);
    }
    //endregion

}
