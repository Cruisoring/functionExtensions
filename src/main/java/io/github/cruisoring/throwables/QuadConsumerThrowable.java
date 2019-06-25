package io.github.cruisoring.throwables;

import io.github.cruisoring.OfThrowable;

import java.util.function.Function;

/**
 * Functional Interface identifying methods, accepting 4 arguments and returning nothing,
 * while their service logic could throw Exceptions.
 *
 * @param <T> Type of the first argument.
 * @param <U> Type of the second argument.
 * @param <V> Type of the third argument.
 * @param <W> Type of the fourth argument.
 */
@FunctionalInterface
public interface QuadConsumerThrowable<T, U, V, W> extends OfThrowable {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 4 arguments and returning nothing.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @throws Exception Any Exception could be thrown by the concerned service logic.
     */
    void accept(T t, U u, V v, W w) throws Exception;

    /**
     * Execute <code>accept()</code> and handle thrown Exception with the default handler of {@code throwsException}.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     */
    default void tryAccept(T t, U u, V v, W w) {
        try {
            accept(t, u, v, w);
        } catch (Exception e) {
            handle(e);
        }
    }

    /**
     * Convert the {@code QuadConsumerThrowable<T, U, V, W>} to {@code RunnableThrowable} with given argument.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @return the {@code RunnableThrowable} instance invoking the original {@code QuadConsumerThrowable<T, U, V, W>} with required arguments
     */
    default RunnableThrowable asRunnableThrowable(T t, U u, V v, W w) {
        return () -> accept(t, u, v, w);
    }

    /**
     * Convert the QuadConsumerThrowable&lt;T,U,V,W&gt; to QuadConsumer&lt;T,U,V,W&gt; with given Exception Handler
     *
     * @param exceptionHandlers Optional Exception Handlers to process the caught Exception with its first memeber if exists
     * @return Converted QuadConsumer&lt;T,U,V,W&gt; that get Exceptions handled with the first of exceptionHandlers if given,
     *          otherwise {@code this::tryAccept} if no exceptionHandler specified
     */
    default QuadConsumer<T, U, V, W> withHandler(Function<Exception, Object>... exceptionHandlers) {
        if(exceptionHandlers == null || exceptionHandlers.length == 0){
            return this::tryAccept;
        } else {
            QuadConsumer<T, U, V, W> consumer = (t, u, v, w) -> {
                try {
                    accept(t, u, v, w);
                } catch (Exception e) {
                    if (exceptionHandlers != null)
                        exceptionHandlers[0].apply(e);
                }
            };
            return consumer;
        }
    }

    /**
     * Represents an operation that accepts four input arguments and returns no result.
     *
     * @param <T> Type of the first argument
     * @param <U> Type of the second argument
     * @param <V> Type of the third argument
     * @param <W> Type of the fourth argument
     */
    @FunctionalInterface
    interface QuadConsumer<T, U, V, W> {
        void accept(T t, U u, V v, W w);
    }
}
