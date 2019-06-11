package io.github.cruisoring.throwables;

import io.github.cruisoring.ofThrowable;

import java.util.function.Function;

/**
 * Functional Interface identifying methods, accepting 5 arguments and returning nothing,
 * while their service logic could throw Exceptions.
 *
 * @param <T> Type of the first argument.
 * @param <U> Type of the second argument.
 * @param <V> Type of the third argument.
 * @param <W> Type of the fourth argument.
 * @param <X> Type of the fifth argument.
 */
@FunctionalInterface
public interface PentaConsumerThrowable<T, U, V, W, X> extends ofThrowable {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 5 arguments and returning nothing.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @param x The fifth argument of type <code>X</code>.
     * @throws Exception Any Exception could be thrown by the concerned service logic.
     */
    void accept(T t, U u, V v, W w, X x) throws Exception;

    /**
     * Execute <code>accept()</code> and handle thrown Exception with the default handler of {@code throwsException}.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @param x The fifth argument of type <code>X</code>.
     */
    default void tryAccept(T t, U u, V v, W w, X x) {
        try {
            accept(t, u, v, w, x);
        } catch (Exception e) {
            handle(e);
        }
    }

    /**
     * Convert the {@code PentaConsumerThrowable<T, U, V, W, X>} to {@code RunnableThrowable} with given argument.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @param x The fifth argument of type <code>X</code>.
     * @return the {@code RunnableThrowable} instance invoking the original {@code PentaConsumerThrowable<T, U, V, W, X>} with required arguments
     */
    default RunnableThrowable asRunnableThrowable(T t, U u, V v, W w, X x) {
        return () -> accept(t, u, v, w, x);
    }

    /**
     * Convert the PentaConsumerThrowable&lt;T,U,V,W,X&gt; to PentaConsumer&lt;T,U,V,W,X&gt; with given Exception Handler
     *
     * @param exceptionHandlers Optional Exception Handlers to process the caught Exception with its first memeber if exists
     * @return Converted PentaConsumer&lt;T,U,V,W,X&gt; that get Exceptions handled with the first of exceptionHandlers if given,
     *          otherwise {@code this::tryAccept} if no exceptionHandler specified
     */
    default PentaConsumer<T, U, V, W, X> withHandler(Function<Exception, Object>... exceptionHandlers) {
        if(exceptionHandlers == null || exceptionHandlers.length == 0) {
            return this::tryAccept;
        } else {
            PentaConsumer<T, U, V, W, X> consumer = (t, u, v, w, x) -> {
                try {
                    accept(t, u, v, w, x);
                } catch (Exception e) {
                    if (exceptionHandlers != null)
                        exceptionHandlers[0].apply(e);
                }
            };
            return consumer;
        }
    }

    /**
     * Represents an operation that accepts five input arguments and returns no result.
     *
     * @param <T> Type of the first argument
     * @param <U> Type of the second argument
     * @param <V> Type of the third argument
     * @param <W> Type of the fourth argument
     * @param <X> Type of the fifth argument
     */
    @FunctionalInterface
    interface PentaConsumer<T, U, V, W, X> {
        void accept(T t, U u, V v, W w, X x);
    }

}
