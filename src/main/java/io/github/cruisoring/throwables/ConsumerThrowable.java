package io.github.cruisoring.throwables;

import io.github.cruisoring.ofThrowable;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Functional Interface identifying methods, accepting one argument and returning nothing,
 * while their service logic could throw Exceptions.
 *
 * @param <T> Type of the first argument.
 */
@FunctionalInterface
public interface ConsumerThrowable<T> extends ofThrowable {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 1 argument and returning nothing.
     *
     * @param t The first argument of type <code>T</code>.
     * @throws Exception Any Exception could be thrown by the concerned service logic.
     */
    void accept(T t) throws Exception;

    /**
     * Execute <code>accept()</code> and handle thrown Exception with the default handler of {@code throwsException}.
     *
     * @param t The first argument of type <code>T</code>.
     */
    default void tryAccept(T t) {
        try {
            accept(t);
        } catch (Exception e) {
            handle(e);
        }
    }

    /**
     * Convert the {@code ConsumerThrowable<T>} to {@code RunnableThrowable} with given argument.
     * @param t The first argument of type <code>T</code>.
     * @return the {@code RunnableThrowable} instance invoking the original {@code ConsumerThrowable<T>} with required arguments
     */
    default RunnableThrowable asRunnableThrowable(T t) {
        return () -> accept(t);
    }

    /**
     * Convert the ConsumerThrowable&lt;T&gt; to Consumer&lt;T&gt; with given Exception Handler
     *
     * @param exceptionHandlers Optional Exception Handlers to process the caught Exception with its first memeber if exists
     * @return Converted Consumer&lt;T&gt; that get Exceptions handled with the first of exceptionHandlers if given,
     *          otherwise {@code this::tryAccept}
     */
    default Consumer<T> withHandler(Function<Exception, Object>... exceptionHandlers) {
        if(exceptionHandlers == null || exceptionHandlers.length == 0) {
            return this::tryAccept;
        } else {
            Consumer<T> consumer = t -> {
                try {
                    accept(t);
                } catch (Exception e) {
                    exceptionHandlers[0].apply(e);
                }
            };
            return consumer;
        }
    }
}
