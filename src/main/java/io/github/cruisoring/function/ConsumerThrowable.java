package io.github.cruisoring.function;

import java.util.function.Consumer;

/**
 * Functional Interface identifying methods, accepting one argument and returning nothing,
 * while their service logic could throw Exceptions.
 *
 * @param <T> Type of the first argument.
 */
@FunctionalInterface
public interface ConsumerThrowable<T> {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 1 argument and returning nothing.
     *
     * @param t Needed argument of type T.
     * @throws Exception Any Exception could be thrown by the concerned service logic.
     */
    void accept(T t) throws Exception;

    /**
     * Execute <code>accept()</code> and ignore any Exceptions thrown.
     *
     * @param t Argument needed by <code>accept(t)</code>
     */
    default void tryAccept(T t) {
        try {
            accept(t);
        } catch (Exception e) {
        }
    }

    /**
     * Convert the ConsumerThrowable&lt;T&gt; to Consumer&lt;T&gt; with injected Exception Handler
     *
     * @param exceptionHandler Exception Handler of the caught Exceptions
     * @return Converted Consumer&lt;T&gt; that get Exceptions handled with the exceptionHandler
     */
    default Consumer<T> withHandler(Consumer<Exception> exceptionHandler) {
        Consumer<T> consumer = t -> {
            try {
                accept(t);
            } catch (Exception e) {
                if (exceptionHandler != null)
                    exceptionHandler.accept(e);
            }
        };
        return consumer;
    }
}
