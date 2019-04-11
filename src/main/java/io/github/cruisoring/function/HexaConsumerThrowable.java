package io.github.cruisoring.function;

import java.util.function.Consumer;

/**
 * Functional Interface identifying methods, accepting 6 arguments and returning nothing,
 * while their service logic could throw Exceptions.
 *
 * @param <T> Type of the first argument.
 * @param <U> Type of the second argument.
 * @param <V> Type of the third argument.
 * @param <W> Type of the fourth argument.
 * @param <X> Type of the fifth argument.
 * @param <Y> Type of the sixth argument.
 */
@FunctionalInterface
public interface HexaConsumerThrowable<T, U, V, W, X, Y> {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 6 arguments and returning nothing.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @param x The fifth argument of type <code>X</code>.
     * @param y The sixth argument of type <code>Y</code>.
     * @throws Exception Any Exception could be thrown by the concerned service logic.
     */
    void accept(T t, U u, V v, W w, X x, Y y) throws Exception;

    /**
     * Execute <code>accept()</code> and ignore any Exceptions thrown.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @param x The fifth argument of type <code>X</code>.
     * @param y The sixth argument of type <code>Y</code>.
     */
    default void tryAccept(T t, U u, V v, W w, X x, Y y) {
        try {
            accept(t, u, v, w, x, y);
        } catch (Exception e) {
        }
    }

    /**
     * Convert the HexaConsumerThrowable&lt;T,U,V,W,X,Y&gt; to HexaConsumer&lt;T,U,V,W,X,Y&gt; with injected Exception Handler
     *
     * @param exceptionHandler Exception Handler of the caught Exceptions
     * @return Converted HexaConsumer&lt;T,U,V,W,X,Y&gt; that get Exceptions handled with the exceptionHandler
     */
    default HexaConsumer<T, U, V, W, X, Y> withHandler(Consumer<Exception> exceptionHandler) {
        HexaConsumer<T, U, V, W, X, Y> consumer = (t, u, v, w, x, y) -> {
            try {
                accept(t, u, v, w, x, y);
            } catch (Exception e) {
                if (exceptionHandler != null)
                    exceptionHandler.accept(e);
            }
        };
        return consumer;
    }

    /**
     * Convert the HexaConsumerThrowable&lt;T,U,V,W,X,Y&gt; to HexaConsumer&lt;T,U,V,W,X,Y&gt; with optional alternative HexaConsumer&lt;T,U,V,W,X,Y&gt;
     *
     * @param alternatives varargs of HexaConsumer&lt;T,U,V,W,X,Y&gt; to consume the input.
     * @return the tryAccept() if no alternatives provided, otherwise a converted HexaConsumer&lt;T,U,V,W,X,Y&gt; that 
     *  would use the first HexaConsumer&lt;T,U,V,W,X,Y&gt; to finish the job
     */
    default HexaConsumer<T, U, V, W, X, Y> orElse(HexaConsumer<T, U, V, W, X, Y>... alternatives){
        if(alternatives == null || alternatives.length == 0){
            return this::tryAccept;
        }

        HexaConsumer<T, U, V, W, X, Y> consumer = (t, u, v, w, x, y) -> {
            try {
                accept(t, u, v, w, x, y);
            } catch (Exception e) {
                alternatives[0].accept(t, u, v, w, x, y);
            }
        };
        return consumer;
    }
    
    /**
     * Represents an operation that accepts six input arguments and returns no result.
     *
     * @param <T> Type of the first argument
     * @param <U> Type of the second argument
     * @param <V> Type of the third argument
     * @param <W> Type of the fourth argument
     * @param <X> Type of the fifth argument
     * @param <Y> Type of the sixth argument
     */
    @FunctionalInterface
    interface HexaConsumer<T, U, V, W, X, Y> {
        void accept(T t, U u, V v, W w, X x, Y y);
    }
}
