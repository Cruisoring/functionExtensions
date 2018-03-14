package com.easyworks.function;

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

}