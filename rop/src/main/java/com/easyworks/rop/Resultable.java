package com.easyworks.rop;

import java.util.Arrays;

@FunctionalInterface
public interface Resultable {
    /**
     * Execute the embedded logic to apply the Result.
     * @return
     */
    Result perform();

    default Resultable next(Resultable other){
        return () -> {
            Result thisResult = perform();
            if(!thisResult.isSuccess)
                return thisResult;
            return other.perform();
        };
    }

    default Resultable nextAny(Resultable... others){
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

    default Resultable nextAll(Resultable... others){
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
