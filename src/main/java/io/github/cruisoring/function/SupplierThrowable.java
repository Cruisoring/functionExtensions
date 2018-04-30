package io.github.cruisoring.function;

import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Functional Interface identifying methods, accepting no arguments and returning result of type <code>T</code>,
 * while their service logic could throw Exceptions.
 * @param <R>   Type of the returned result.
 */
@FunctionalInterface
public interface SupplierThrowable<R> extends WithValueReturned<R> {

    /**
     * Get a result
     * @return the result of type <tt>T</tt>
     * @throws Exception any exception to be thrown
     */
    R get() throws Exception;

    /**
     * Convert the SupplierThrowable&lt;R&gt; to Supplier&lt;R&gt;
     * @param exceptionHandler  Exception Handler of the caught Exceptions that retuns default value of type R.
     * @return  The Supplier&lt;R&gt; version of the original SupplierThrowable&lt;R&gt;
     */
    default Supplier<R> withHandler(BiFunction<Exception, WithValueReturned, Object> exceptionHandler){
        Supplier<R> supplier = () -> {
            try {
                return get();
            } catch (Exception e) {
                return exceptionHandler == null ? null : (R) exceptionHandler.apply(e, this);
            }
        };
        return supplier;
    }

    /**
     * Simplified version of converting the SupplierThrowable&lt;R&gt; to Supplier&lt;R&gt; by ignoring the caught Exception
     * and simply returns a pre-defined default value.
     * @param defaultValue  Predefined default value.
     * @return the Converted Supplier&lt;R&gt; instance containing the same service logic
     */
    default Supplier<R> orElse(R defaultValue){
        Supplier<R> supplier = () -> {
            try {
                return get();
            } catch (Exception e) {
                return defaultValue;
            }
        };
        return supplier;
    }
}