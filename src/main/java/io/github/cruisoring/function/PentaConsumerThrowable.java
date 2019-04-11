package io.github.cruisoring.function;

import java.util.function.Consumer;

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
public interface PentaConsumerThrowable<T, U, V, W, X> {
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
     * Execute <code>accept()</code> and ignore any Exceptions thrown.
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
        }
    }

    /**
     * Convert the PentaConsumerThrowable&lt;T,U,V,W,X&gt; to PentaConsumer&lt;T,U,V,W,X&gt; with injected Exception Handler
     *
     * @param exceptionHandler Exception Handler of the caught Exceptions
     * @return Converted PentaConsumer&lt;T,U,V,W,X&gt; that get Exceptions handled with the exceptionHandler
     */
    default PentaConsumer<T, U, V, W, X> withHandler(Consumer<Exception> exceptionHandler) {
        PentaConsumer<T, U, V, W, X> consumer = (t, u, v, w, x) -> {
            try {
                accept(t, u, v, w, x);
            } catch (Exception e) {
                if (exceptionHandler != null)
                    exceptionHandler.accept(e);
            }
        };
        return consumer;
    }

    /**
     * Convert the PentaConsumerThrowable&lt;T,U,V,W,X&gt; to PentaConsumer&lt;T,U,V,W,X&gt; with optional alternative PentaConsumer&lt;T,U,V,W,X&gt;
     *
     * @param alternatives varargs of PentaConsumer&lt;T,U,V,W,X&gt; to consume the input.
     * @return the tryAccept() if no alternatives provided, otherwise a converted PentaConsumer&lt;T,U,V,W,X&gt; that 
     *  would use the first PentaConsumer&lt;T,U,V,W,X&gt; to finish the job
     */
    default PentaConsumer<T, U, V, W, X> orElse(PentaConsumer<T, U, V, W, X>... alternatives){
        if(alternatives == null || alternatives.length == 0){
            return this::tryAccept;
        }

        PentaConsumer<T, U, V, W, X> consumer = (t, u, v, w, x) -> {
            try {
                accept(t, u, v, w, x);
            } catch (Exception e) {
                alternatives[0].accept(t, u, v, w, x);
            }
        };
        return consumer;
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
