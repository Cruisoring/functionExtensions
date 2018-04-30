package io.github.cruisoring.function;

import java.util.function.Consumer;

/**
 * Functional Interface identifying methods, accepting 3 arguments and returning nothing,
 * while their service logic could throw Exceptions.
 * @param <T>   Type of the first argument.
 * @param <U>   Type of the second argument.
 * @param <V>   Type of the third argument.
 */
@FunctionalInterface
public interface TriConsumerThrowable<T,U,V> {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 3 arguments and returning nothing.
     * @param t     The first argument of type <code>T</code>.
     * @param u     The second argument of type <code>U</code>.
     * @param v     The third argument of type <code>V</code>.
     * @throws Exception    Any Exception could be thrown by the concerned service logic.
     */
    void accept(T t, U u, V v) throws Exception;

    /**
     * Convert the TriConsumerThrowable&lt;T,U,V&gt; to TriConsumer&lt;T,U,V&gt; with injected Exception Handler
     * @param exceptionHandler  Exception Handler of the caught Exceptions
     * @return  Converted TriConsumer&lt;T,U,V&gt; that get Exceptions handled with the exceptionHandler
     */
    default TriConsumer<T,U,V> withHandler(Consumer<Exception> exceptionHandler){
        TriConsumer<T,U,V> consumer = (t, u, v) -> {
            try {
                accept(t, u, v);
            } catch (Exception e) {
                if(exceptionHandler != null)
                    exceptionHandler.accept(e);
            }
        };
        return consumer;
    }

    /**
     * Represents an operation that accepts three input arguments and returns no result.
     * @param <T>   Type of the first argument
     * @param <U>   Type of the second argument
     * @param <V>   Type of the third argument
     */
    @FunctionalInterface
    interface TriConsumer<T,U,V> {
        void accept(T t, U u, V v);
    }
}
