package io.github.cruisoring.throwables;


import io.github.cruisoring.ofThrowable;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Functional Interface identifying methods, accepting 2 arguments and returning nothing,
 * while their service logic could throw Exceptions.
 *
 * @param <T> Type of the first argument.
 * @param <U> Type of the second argument.
 */
@FunctionalInterface
public interface BiConsumerThrowable<T, U> extends ofThrowable {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 2 arguments and returning nothing.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @throws Exception Any Exception could be thrown by the concerned service logic.
     */
    void accept(T t, U u) throws Exception;

    /**
     * Execute <code>accept()</code> and handle thrown Exception with the default handler of {@code throwsException}.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     */
    default void tryAccept(T t, U u) {
        try {
            accept(t, u);
        } catch (Exception cause) {
            handle(cause);
        }
    }

    /**
     * Convert the {@code BiConsumerThrowable<T, U>} to {@code RunnableThrowable} with given argument.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @return the {@code RunnableThrowable} instance invoking the original {@code BiConsumerThrowable<T, U>} with required arguments
     */
    default RunnableThrowable asRunnableThrowable(T t, U u) {
        return () -> accept(t, u);
    }

    /**
     * Convert the BiConsumerThrowable&lt;T,U&gt; to BiConsumer&lt;T,U&gt; with given Exception Handler
     *
     * @param exceptionHandlers Optional Exception Handlers to process the caught Exception with its first memeber if exists
     * @return Converted BiConsumer&lt;T,U&gt; that get Exception handled with the first of exceptionHandlers if given,
     *          otherwise {@code this::tryAccept} if no exceptionHandler specified
     */
    default BiConsumer<T, U> withHandler(Function<Exception, Object>... exceptionHandlers) {
        if(exceptionHandlers == null || exceptionHandlers.length == 0) {
            return this::tryAccept;
        } else {
            BiConsumer<T, U> function = (t, u) -> {
                try {
                    accept(t, u);
                } catch (Exception e) {
                    exceptionHandlers[0].apply(e);
                }
            };
            return function;
        }
    }
}
