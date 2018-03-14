package com.easyworks.function;

import java.util.function.BiFunction;

/**
 * Functional Interface to define the behaviours of handling Exception by considering return type of the Lambda Expression.
 * Notice: The following method shall be implmented: <code>T apply(Exception t, AbstractThrowable u);</code>
 * @param <T> Return type of the original Lambda Expression.
 *           Notice: Type of <code>T</code> shall be ignored to cope with more generic scenario, thanks to Java type erasure.
 */
@FunctionalInterface
public interface ExceptionHandler<T>{
    void handle(Exception e);

    default T apply(DefaultReturner<T> defaultReturner, Exception e, AbstractThrowable function){
        handle(e);
        return defaultReturner.returns(function);
    }

    default T apply(Exception e, AbstractThrowable function, T defaultValue){
        handle(e);
        return defaultValue;
    }

    default T apply(Exception e, AbstractThrowable function){
        handle(e);
        return null;
    }
}
