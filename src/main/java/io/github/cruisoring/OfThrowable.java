package io.github.cruisoring;

/**
 * Base of Functional Interfaces that might throw Exception,
 * the default handle() methods can be used to define the default behaviour when Exception is caught.
 *
 * @param <R> optional type of the returned value
 */
public interface OfThrowable<R> {

    /**
     * The default handler of an Exception, just throw a {@code RuntimeException} with the original Exception as the cause.
     * @param cause the Exception thrown by the business logic
     * @return  actually nothing would be returned
     */
    default R handle(Exception cause) {
        return (R)Functions.defaultExceptionHandler.apply(cause);
    }
}
