package io.github.cruisoring.throwables;

import io.github.cruisoring.ofThrowable;

import java.util.function.Function;

/**
 * Functional Interface identifying methods, accepting 3 arguments and returning nothing,
 * while their service logic could throw Exceptions.
 *
 * @param <T> Type of the first argument.
 * @param <U> Type of the second argument.
 * @param <V> Type of the third argument.
 */
@FunctionalInterface
public interface TriConsumerThrowable<T, U, V> extends ofThrowable {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 3 arguments and returning nothing.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @throws Exception Any Exception could be thrown by the concerned service logic.
     */
    void accept(T t, U u, V v) throws Exception;

    /**
     * Execute <code>accept(t)</code> and handle thrown Exception with the default handler of {@code throwsException}.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     */
    default void tryAccept(T t, U u, V v) {
        try {
            accept(t, u, v);
        } catch (Exception e) {
            handle(e);
        }
    }

    /**
     * Convert the {@code TriConsumerThrowable<T, U, V>} to {@code RunnableThrowable} with given argument.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @return the {@code RunnableThrowable} instance invoking the original {@code TriConsumerThrowable<T, U, V>} with required arguments
     */
    default RunnableThrowable asRunnableThrowable(T t, U u, V v) {
        return () -> accept(t, u, v);
    }

    /**
     * Convert the TriConsumerThrowable&lt;T,U,V&gt; to TriConsumer&lt;T,U,V&gt; with given Exception Handler
     *
     * @param exceptionHandlers Optional Exception Handlers to process the caught Exception with its first memeber if exists
     * @return Converted TriConsumer&lt;T,U,V&gt; that get Exceptions handled with the first of exceptionHandlers if given, otherwise {@code this::tryAccept} if no exceptionHandler specified
     */
    default TriConsumer<T, U, V> withHandler(Function<Exception, Object>... exceptionHandlers) {
        if(exceptionHandlers == null || exceptionHandlers.length == 0) {
            return this::tryAccept;
        } else {
            TriConsumer<T, U, V> consumer = (t, u, v) -> {
                try {
                    accept(t, u, v);
                } catch (Exception e) {
                    if (exceptionHandlers != null)
                        exceptionHandlers[0].apply(e);
                }
            };
            return consumer;
        }
    }

    /**
     * Represents an operation that accepts three input arguments and returns no result.
     *
     * @param <T> Type of the first argument
     * @param <U> Type of the second argument
     * @param <V> Type of the third argument
     */
    @FunctionalInterface
    interface TriConsumer<T, U, V> {
        void accept(T t, U u, V v);
    }
}
