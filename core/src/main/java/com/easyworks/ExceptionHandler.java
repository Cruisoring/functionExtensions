package com.easyworks;

import com.easyworks.function.AbstractThrowable;

import java.util.function.BiFunction;

/**
 * Functional Interface to define the behaviours of handling Exception by considering return type of the Lambda Expression.
 * Notice: The following method shall be implmented: <code>T apply(Exception t, AbstractThrowable u);</code>
 * @param <T> Return type of the original Lambda Expression.
 *           Notice: Type of <code>T</code> shall be ignored to cope with more generic scenario, thanks to Java type erasure.
 */
@FunctionalInterface
public interface ExceptionHandler<T>
        extends BiFunction<Exception, AbstractThrowable, T> {
}
