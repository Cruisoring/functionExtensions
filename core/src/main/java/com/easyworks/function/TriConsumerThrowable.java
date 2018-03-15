package com.easyworks.function;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Functional Interface identifying methods, accepting 3 arguments and returning nothing,
 * while their service logic could throw Exceptions.
 * @param <T>   Type of the first argument.
 * @param <U>   Type of the second argument.
 * @param <V>   Type of the third argument.
 */
@FunctionalInterface
public interface TriConsumerThrowable<T,U,V> extends AbstractThrowable {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 3 arguments and returning nothing.
     * @param t     The first argument of type <code>T</code>.
     * @param u     The second argument of type <code>U</code>.
     * @param v     The third argument of type <code>V</code>.
     * @throws Exception    Any Exception could be thrown by the concerned service logic.
     */
    void accept(T t, U u, V v) throws Exception;

    /**
     * Convert the above <code>accept</code> method to <code>RunnableThrowable</code> with all 3 arguments
     * @param t     The first argument of type <code>T</code>.
     * @param u     The second argument of type <code>U</code>.
     * @param v     The third argument of type <code>V</code>.
     * @return  The converted <code>RunnableThrowable</code> with all 3 argments captured when called.
     */
    default RunnableThrowable asRunnable(T t, U u, V v){
        return () -> accept(t, u, v);
    }

    default TriConsumer<T,U,V> orElse(Consumer<Exception> exceptionHandler){
        Objects.requireNonNull(exceptionHandler);
        return (t, u, v) -> {
            try {
                accept(t, u, v);
            } catch (Exception e) {
                exceptionHandler.accept(e);
            }
        };
    }

    @FunctionalInterface
    interface TriConsumer<T,U,V> extends AbstractThrowable{
        void accept(T t, U u, V v);
    }
}
