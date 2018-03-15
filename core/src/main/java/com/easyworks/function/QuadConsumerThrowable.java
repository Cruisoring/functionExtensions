package com.easyworks.function;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Functional Interface identifying methods, accepting 4 arguments and returning nothing,
 * while their service logic could throw Exceptions.
 * @param <T>   Type of the first argument.
 * @param <U>   Type of the second argument.
 * @param <V>   Type of the third argument.
 * @param <W>   Type of the fourth argument.
 */
@FunctionalInterface
public interface QuadConsumerThrowable<T,U,V,W> extends AbstractThrowable {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 4 arguments and returning nothing.
     * @param t     The first argument of type <code>T</code>.
     * @param u     The second argument of type <code>U</code>.
     * @param v     The third argument of type <code>V</code>.
     * @param w     The fourth argument of type <code>W</code>.
     * @throws Exception    Any Exception could be thrown by the concerned service logic.
     */
    void accept(T t, U u, V v, W w) throws Exception;

    /**
     * Convert the above <code>accept</code> method to <code>RunnableThrowable</code> with all 4 arguments
     * @param t     The first argument of type <code>T</code>.
     * @param u     The second argument of type <code>U</code>.
     * @param v     The third argument of type <code>V</code>.
     * @param w     The fourth argument of type <code>W</code>.
     * @return  The converted <code>RunnableThrowable</code> with all 4 argments captured when called.
     */
    default RunnableThrowable asRunnable(T t, U u, V v, W w){
        return () -> accept(t, u, v, w);
    }

    default QuadConsumer<T,U,V,W> orElse(Consumer<Exception> exceptionHandler){
        Objects.requireNonNull(exceptionHandler);
        return (t, u, v, w) -> {
            try {
                accept(t, u, v, w);
            } catch (Exception e) {
                exceptionHandler.accept(e);
            }
        };
    }

    @FunctionalInterface
    interface QuadConsumer<T,U,V,W> extends AbstractThrowable{
        void accept(T t, U u, V v, W w);
    }
}
