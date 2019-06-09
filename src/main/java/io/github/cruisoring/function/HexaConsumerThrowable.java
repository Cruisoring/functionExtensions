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
public interface HexaConsumerThrowable<T, U, V, W, X, Y> extends voidThrowable {
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
     * Execute <code>accept()</code> and handle thrown Exception with the default handler of {@code throwsException}.
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
            handle(e);
        }
    }

    /**
     * Convert the {@code HexaConsumerThrowable<T, U, V, W, X, Y>} to {@code RunnableThrowable} with given argument.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @param x The fifth argument of type <code>X</code>.
     * @param y The sixth argument of type <code>Y</code>.
     * @return the {@code RunnableThrowable} instance invoking the original {@code HexaConsumerThrowable<T, U, V, W, X, Y>} with required arguments
     */
    default RunnableThrowable asRunnableThrowable(T t, U u, V v, W w, X x, Y y) {
        return () -> accept(t, u, v, w, x, y);
    }

    /**
     * Convert the HexaConsumerThrowable&lt;T,U,V,W,X,Y&gt; to HexaConsumer&lt;T,U,V,W,X,Y&gt; with given Exception Handler
     *
     * @param exceptionHandlers Optional Exception Handlers to process the caught Exception with its first memeber if exists
     * @return Converted HexaConsumer&lt;T,U,V,W,X,Y&gt; that get Exceptions handled with the first of exceptionHandlers if given,
     *      otherwise {@code this::tryAccept} if no exceptionHandler specified
     */
    default HexaConsumer<T, U, V, W, X, Y> withHandler(Consumer<Exception>... exceptionHandlers) {
        if(exceptionHandlers == null || exceptionHandlers.length == 0) {
            return this::tryAccept;
        } else {
            HexaConsumer<T, U, V, W, X, Y> consumer = (t, u, v, w, x, y) -> {
                try {
                    accept(t, u, v, w, x, y);
                } catch (Exception e) {
                    if (exceptionHandlers != null)
                        exceptionHandlers[0].accept(e);
                }
            };
            return consumer;
        }
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
