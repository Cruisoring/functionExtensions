package com.easyworks.utility;

import com.easyworks.ResultFunction;

/**
 * Generic wrapper of the returned Value, success or not and optional message from the Exception caught.
 * @param <T>   Type of the embedded result.
 */
public class Result<T>{

    //Predefined Value indicating success operations returning void
    public static final Result Success = new Result(true, null, null);

    //Predefined Value indicating failed operations returning void
    public static final Result Failure = new Result(false, null, null);

    public final boolean isSuccess;
    public final String Message;
    public final T Value;

    /**
     * Protected constructor to limit the kinds of Outcomes.
     * @param isSuccess {@code true} if the operation is regarded as success, otherwise {@code false}
     * @param message   String message as explanation of the failed Outcocme
     * @param result    Returned result to be wrapped by the Value
     */
    protected Result(boolean isSuccess, String message, T result){
        this.isSuccess = isSuccess;
        Message = message;
        Value = result;
    }

    /**
     * Construct a Result when expected Value is returned.
     * Notice: Due to the potential confliction when T is boolean, keep using the static variable {@code Success}
     * and {@code Failure} to indicate normal boolean results.
     * @param value the returned Value.
     */
    public Result(T value){
        this(true, null, value);
    }

    /**
     * Constructs a Result indicating an operation failed with embedded message.
     * @param exception the Exception caused the failure
     */
    public Result(Exception exception){
        this(false, exception.getMessage(), null);
    }

    public Result and(Result other){
        return this.isSuccess ? other : this;
    }

    public Result or(Result other){
        return this.isSuccess ? this : other;
    }

    public Result and(ResultFunction other){
        return !this.isSuccess ? this : other.perform();
    }

    public Result or(ResultFunction other){
        return this.isSuccess ? this : other.perform();
    }


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
