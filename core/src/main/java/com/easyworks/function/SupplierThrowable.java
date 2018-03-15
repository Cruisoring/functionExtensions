package com.easyworks.function;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Functional Interface identifying methods, accepting no arguments and returning result of type <code>T</code>,
 * while their service logic could throw Exceptions.
 * @param <T>   Type of the returned result.
 */
@FunctionalInterface
public interface SupplierThrowable<T> extends AbstractThrowable {

    /**
     * Get a result
     * @return the result of type <tt>T</tt>
     * @throws Exception
     */
    T get() throws Exception;

    default Supplier<T> orElse(Function<Exception, T> exceptionHanlder){
        Objects.requireNonNull(exceptionHanlder);
        return () -> {
            try {
                return get();
            } catch (Exception e) {
                return exceptionHanlder.apply(e);
            }
        };
    }

    default Supplier<T> orElse(T defaultValue){
        return () -> {
            try {
                return get();
            } catch (Exception e) {
                return defaultValue;
            }
        };
    }
}