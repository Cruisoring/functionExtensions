package io.github.cruisoring.function;

import java.util.function.Consumer;

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
public interface QuadConsumerThrowable<T, U, V, W> {
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
     * Execute <code>accept()</code> and ignore any Exceptions thrown.
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
     * Convert the QuadConsumerThrowable&lt;T,U,V,W&gt; to QuadConsumer&lt;T,U,V,W&gt; with injected Exception Handler
     *
     * @param exceptionHandler Exception Handler of the caught Exceptions
     * @return Converted QuadConsumer&lt;T,U,V,W&gt; that get Exceptions handled with the exceptionHandler
     */
    default QuadConsumer<T, U, V, W> withHandler(Consumer<Exception> exceptionHandler) {
        QuadConsumer<T, U, V, W> consumer = (t, u, v, w) -> {
            try {
                accept(t, u, v, w);
            } catch (Exception e) {
                if (exceptionHandler != null)
                    exceptionHandler.accept(e);
            }
        };
        return consumer;
    }

    /**
     * Convert the QuadConsumerThrowable&lt;T,U,V,W&gt; to QuadConsumer&lt;T,U,V,W&gt; with optional alternative QuadConsumer&lt;T,U,V,W&gt;
     *
     * @param alternatives varargs of QuadConsumer&lt;T,U,V,W&gt; to consume the input.
     * @return the tryAccept() if no alternatives provided, otherwise a converted QuadConsumer&lt;T,U,V,W&gt; that 
     *  would use the first QuadConsumer&lt;T,U,V,W&gt; to finish the job
     */
    default QuadConsumer<T, U, V, W> orElse(QuadConsumer<T, U, V, W>... alternatives){
        if(alternatives == null || alternatives.length == 0){
            return this::tryAccept;
        }

        QuadConsumer<T, U, V, W> consumer = (t, u, v, w) -> {
            try {
                accept(t, u, v, w);
            } catch (Exception e) {
                alternatives[0].accept(t, u, v, w);
            }
        };
        return consumer;
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
