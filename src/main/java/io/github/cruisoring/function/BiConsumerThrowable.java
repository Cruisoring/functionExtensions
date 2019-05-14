package io.github.cruisoring.function;


import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Functional Interface identifying methods, accepting 2 arguments and returning nothing,
 * while their service logic could throw Exceptions.
 *
 * @param <T> Type of the first argument.
 * @param <U> Type of the second argument.
 */
@FunctionalInterface
public interface BiConsumerThrowable<T, U> {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 2 arguments and returning nothing.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     * @throws Exception Any Exception could be thrown by the concerned service logic.
     */
    void accept(T t, U u) throws Exception;

    /**
     * Execute <code>accept()</code> and ignore any Exceptions thrown.
     *
     * @param t The first argument of type <code>T</code>.
     * @param u The second argument of type <code>U</code>.
     */
    default void tryAccept(T t, U u) {
        try {
            accept(t, u);
        } catch (Exception e) {
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
     * Convert the BiConsumerThrowable&lt;T,U&gt; to BiConsumer&lt;T,U&gt; with injected Exception Handler
     *
     * @param exceptionHandler Exception Handler of the caught Exceptions
     * @return Converted BiConsumer&lt;T,U&gt; that get Exceptions handled with the exceptionHandler
     */
    default BiConsumer<T, U> withHandler(Consumer<Exception> exceptionHandler) {
        BiConsumer<T, U> biConsumer = (t, u) -> {
            try {
                accept(t, u);
            } catch (Exception e) {
                if (exceptionHandler != null)
                    exceptionHandler.accept(e);
            }
        };
        return biConsumer;
    }

    /**
     * Convert the BiConsumerThrowable&lt;T,U&gt; to BiConsumer&lt;T,U&gt; with optional alternative BiConsumer&lt;T,U&gt;
     *
     * @param alternatives varargs of BiConsumer&lt;T,U&gt; to consume the input.
     * @return the tryAccept() if no alternatives provided, otherwise a converted BiConsumer&lt;T,U&gt; that
     *  would use the first BiConsumer&lt;T,U&gt; to finish the job
     */
    default BiConsumer<T, U> orElse(BiConsumer<T, U>... alternatives){
        if(alternatives == null || alternatives.length == 0){
            return this::tryAccept;
        }

        BiConsumer<T, U> consumer = (t,u) -> {
            try {
                accept(t, u);
            } catch (Exception e) {
                alternatives[0].accept(t, u);
            }
        };
        return consumer;
    }

}
