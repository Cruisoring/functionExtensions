package io.github.cruisoring.throwables;

import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.ofThrowable;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Functional Interface identifying methods, accepting no arguments and returning result of type <code>T</code>,
 * while their service logic could throw Exceptions.
 *
 * @param <R> Type of the returned result.
 */
@FunctionalInterface
public interface SupplierThrowable<R> extends ofThrowable<R> {

    /**
     * Get a result
     *
     * @return the result of type <tt>T</tt>
     * @throws Exception any exception to be thrown
     */
    R get() throws Exception;

    /**
     * Execute the given business logic to return the generated value or handle thrown Exception with the default handler of {@code ofThrowable}.
     *
     * @return the result of type <tt>T</tt> or let the default handler of {@code ofThrowable} to process
     */
    default R tryGet() {
        try {
            return get();
        } catch (Exception e) {
            return handle(e);
        }
    }

    /**
     * Convert the {@code SupplierThrowable<R>} to {@code Supplier<R>} with optional Exception handler
     *
     * @param exceptionHandlers Optional Exception Handlers to process the caught Exception with its first memeber
     * @return The {@code Supplier<R>} version of the original that get Exceptions handled with the first of exceptionHandlers if given,
     * otherwise {@code this::tryGet} if no exceptionHandler specified
     */
    default Supplier<R> withHandler(Function<Exception, Object>... exceptionHandlers) {
        if(exceptionHandlers == null || exceptionHandlers.length == 0) {
            return this::tryGet;
        } else {
            Supplier<R> supplier = () -> {
                try {
                    return get();
                } catch (Exception e) {
                    return (R)exceptionHandlers[0].apply(e);
                }
            };
            return supplier;
        }
    }

    /**
     * Simplified version of converting the {@code SupplierThrowable<R>} to {@code Supplier<R>} by ignoring the caught Exception
     * and simply returns a pre-defined default value.
     *
     * @param defaultValue Predefined default value.
     * @return the Converted {@code Supplier<R>} instance containing the same service logic
     */
    default Supplier<R> orElse(R defaultValue) {
        Supplier<R> supplier = () -> {
            try {
                return get();
            } catch (Exception e) {
                Logger.D(e);
                return defaultValue;
            }
        };
        return supplier;
    }
}