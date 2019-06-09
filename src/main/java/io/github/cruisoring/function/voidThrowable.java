package io.github.cruisoring.function;

import io.github.cruisoring.Functions;

public interface voidThrowable {
    /**
     * The default handler of an Exception, just throw a {@code RuntimeException} with message and the original Exception as the cause.
     * @param message detail message to detail the handling process
     * @param cause the Exception thrown by the business logic
     * @return  actually nothing would be returned
     */
    default void handle(String message, Exception cause) {
        Functions.defaultExceptionHandler.apply(message, cause);
    }

    /**
     * The default handler of an Exception, just throw a {@code RuntimeException} with the original Exception as the cause.
     * @param cause the Exception thrown by the business logic
     * @return  actually nothing would be returned
     */
    default void handle(Exception cause) {
        handle(String.format("%s thrown as IllegalStateException", cause==null ? "null" : cause.getClass().getSimpleName()), cause);
    }

}
