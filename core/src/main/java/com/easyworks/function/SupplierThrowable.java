package com.easyworks.function;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Functional Interface identifying methods, accepting no arguments and returning result of type <code>T</code>,
 * while their service logic could throw Exceptions.
 * @param <R>   Type of the returned result.
 */
@FunctionalInterface
public interface SupplierThrowable<R> extends Supplierable<R> {

    /**
     * Get a result
     * @return the result of type <tt>T</tt>
     * @throws Exception
     */
    R get() throws Exception;

    default Supplier<R> orElse(Function<Exception, R> exceptionHanlder){
        return () -> {
            try {
                return get();
            } catch (Exception e) {
                if(exceptionHanlder == null)
                    return null;
                return exceptionHanlder.apply(e);
            }
        };
    }

    default Supplier<R> orElse(R defaultValue){
        return () -> {
            try {
                return get();
            } catch (Exception e) {
                return defaultValue;
            }
        };
    }
}