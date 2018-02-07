package com.easyworks;

import com.easyworks.utility.Result;

import java.util.Arrays;


@FunctionalInterface
public interface ResultFunction<T> {
    /**
     * Execute the embedded logic to get the Result.
     * @return
     */
    Result perform();

    default ResultFunction next(ResultFunction other){
        return () -> {
            Result thisResult = perform();
            if(!thisResult.isSuccess)
                return thisResult;
            return other.perform();
        };
    }

    default ResultFunction nextAny(ResultFunction... others){
        return () -> {
            Result thisResult = perform();
            if(!thisResult.isSuccess || others.length == 0)
                return thisResult;
            return Arrays.stream(others).parallel()
                    .map(fun->fun.perform())
                    .filter(result -> result.isSuccess)
                    .findAny()
                    .orElse(Result.Failure);
        };
    }

    default ResultFunction nextAll(ResultFunction... others){
        return () -> {
            Result thisResult = perform();
            if(!thisResult.isSuccess || others.length == 0)
                return thisResult;
            return Arrays.stream(others).parallel()
                    .map(fun->fun.perform())
                    .filter(result -> !result.isSuccess)
                    .findAny()
                    .orElse(thisResult);
        };
    }
}
