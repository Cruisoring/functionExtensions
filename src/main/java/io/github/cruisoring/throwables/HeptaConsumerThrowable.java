package io.github.cruisoring.throwables;

import io.github.cruisoring.OfThrowable;

import java.util.function.Function;

/**
 * Functional Interface identifying methods, accepting 7 arguments and returning nothing,
 * while their service logic could throw Exceptions.
 *
 * @param <T> Type of the first argument.
 * @param <U> Type of the second argument.
 * @param <V> Type of the third argument.
 * @param <W> Type of the fourth argument.
 * @param <X> Type of the fifth argument.
 * @param <Y> Type of the sixth argument.
 * @param <Z> Type of the seventh argument.
 */
@FunctionalInterface
public interface HeptaConsumerThrowable<T, U, V, W, X, Y, Z> extends OfThrowable {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 7 arguments and returning nothing.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @param x The fifth argument of type <code>X</code>.
     * @param y The sixth argument of type <code>Y</code>.
     * @param z The seventh argument of type <code>Z</code>.
     * @throws Exception Any Exception could be thrown by the concerned service logic.
     */
    void accept(T t, U u, V v, W w, X x, Y y, Z z) throws Exception;

    /**
     * Execute <code>accept()</code> and handle thrown Exception with the default handler of {@code throwsException}.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @param x The fifth argument of type <code>X</code>.
     * @param y The sixth argument of type <code>Y</code>.
     * @param z The seventh argument of type <code>Z</code>.
     */
    default void tryAccept(T t, U u, V v, W w, X x, Y y, Z z) {
        try {
            accept(t, u, v, w, x, y, z);
        } catch (Exception e) {
            handle(e);
        }
    }

    /**
     * Convert the {@code HeptaConsumerThrowable<T, U, V, W, X, Y, Z>} to {@code RunnableThrowable} with given argument.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @param v The third argument of type <code>V</code>.
     * @param w The fourth argument of type <code>W</code>.
     * @param x The fifth argument of type <code>X</code>.
     * @param y The sixth argument of type <code>Y</code>.
     * @param z The seventh argument of type <code>Z</code>.
     * @return the {@code RunnableThrowable} instance invoking the original {@code HeptaConsumerThrowable<T, U, V, W, X, Y, Z>} with required arguments
     */
    default RunnableThrowable asRunnableThrowable(T t, U u, V v, W w, X x, Y y, Z z) {
        return () -> accept(t, u, v, w, x, y, z);
    }

    /**
     * Convert the ConsumerThrowable&lt;T&gt; to Consumer&lt;T&gt; with given Exception Handler
     *
     * @param exceptionHandlers Optional Exception Handlers to process the caught Exception with its first memeber if exists
     * @return Converted Consumer&lt;T&gt; that get Exceptions handled with the first of exceptionHandlers if given,
     *      otherwise {@code this::tryAccept} if no exceptionHandler specified
     */
    default HeptaConsumer<T, U, V, W, X, Y, Z> withHandler(Function<Exception, Object>... exceptionHandlers) {
        if(exceptionHandlers == null || exceptionHandlers.length == 0) {
            return this::tryAccept;
        } else {
            HeptaConsumer<T, U, V, W, X, Y, Z> consumer = (t, u, v, w, x, y, z) -> {
                try {
                    accept(t, u, v, w, x, y, z);
                } catch (Exception e) {
                    exceptionHandlers[0].apply(e);
                }
            };
            return consumer;
        }
    }

    /**
     * Represents an operation that accepts seven input arguments and returns no result.
     *
     * @param <T> Type of the first argument
     * @param <U> Type of the second argument
     * @param <V> Type of the third argument
     * @param <W> Type of the fourth argument
     * @param <X> Type of the fifth argument
     * @param <Y> Type of the sixth argument
     * @param <Z> Type of the seventh argument
     */
    @FunctionalInterface
    interface HeptaConsumer<T, U, V, W, X, Y, Z> {
        void accept(T t, U u, V v, W w, X x, Y y, Z z);
    }
}
