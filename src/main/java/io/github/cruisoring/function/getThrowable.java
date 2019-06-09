package io.github.cruisoring.function;

import io.github.cruisoring.Functions;

/**
 * Base of Functional Interfaces that might throw Exception,
 * the default handle() methods can be used to define the default behaviour when Exception is caught.
 *
 * @param <R> optional type of the returned value
 */
public interface getThrowable<R> {

    /**
     * The default handler of an Exception, just throw a {@code RuntimeException} with message and the original Exception as the cause.
     * @param message detail message to detail the handling process
     * @param cause the Exception thrown by the business logic
     * @return  actually nothing would be returned
     */
    default R handle(String message, Exception cause) {
        return (R) Functions.defaultExceptionHandler.apply(message, cause);
    }

    /**
     * The default handler of an Exception, just throw a {@code RuntimeException} with the original Exception as the cause.
     * @param cause the Exception thrown by the business logic
     * @return  actually nothing would be returned
     */
    default R handle(Exception cause) {
        return handle(String.format("%s thrown as IllegalStateException", cause==null ? "null" : cause.getClass().getSimpleName()), cause);
    }
}
