package com.easyworks.rop;

import com.easyworks.Tuple;

import java.util.function.Function;

/**
 * Generic wrapper of the returned Value, success or not and optional message from the Exception caught.
 * @param <T>   Type of the embedded result.
 */
public class Result<T>{

    public static Function<Exception, Tuple> exceptionAsValue =
            ex -> Tuple.create(ex.getClass().getSimpleName(), ex.getMessage());

    //Predefined Value indicating success operations returning void
    public static final Result Success = new Result(Tuple.UNIT);

    //Predefined Value indicating failed operations returning void
    public static final Result Failure = new Result(false);

    public final boolean isSuccess;
    public final Tuple Value;

    protected Result(Boolean result){
        isSuccess = result;
        Value = null;
    }

    /**
     * Construct a Result when expected Value is returned.
     * Notice: Due to the potential confliction when T is boolean, keep using the static variable {@code Success}
     * and {@code Failure} to indicate normal boolean results.
     * @param value the returned Value of success operation.
     */
    public Result(Tuple value){
        isSuccess = value == null ? false : true;
        Value = value;
    }

    /**
     * Constructs a Result indicating an operation failed with embedded message.
     * @param exception the Exception caused the failure
     */
    public Result(Exception exception){

        isSuccess = false;
        Value = exceptionAsValue.apply(exception);
    }

    public Result and(Result other){
        return this.isSuccess ? other : this;
    }

    public Result or(Result other){
        return this.isSuccess ? this : other;
    }

//    public Result and(ResultFunction other){
//        return !this.isSuccess ? this : other.perform();
//    }
//
//    public Result or(ResultFunction other){
//        return this.isSuccess ? this : other.perform();
//    }


    public Result and(Result... others){
        if(!this.isSuccess)
            return this;

        //others shall not be empty
        int len = others.length;
        if(len == 0)
            return new Result(new Exception("others shall not be empty"));
        for(int i=0; i<len-1; i++) {
            if (!others[i].isSuccess)
                return others[i];
        }
        return others[len-1];
    }

    public Result or(Result... others){
        if(this.isSuccess)
            return this;

        //others shall not be empty
        int len = others.length;
        if(len == 0)
            return new Result(new Exception("others shall not be empty"));
        for(int i=0; i<len-1; i++) {
            if (others[i].isSuccess)
                return others[i];
        }
        return others[len-1];
    }

}