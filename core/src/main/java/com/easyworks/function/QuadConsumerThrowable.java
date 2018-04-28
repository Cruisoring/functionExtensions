package com.easyworks.function;

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
public interface QuadConsumerThrowable<T,U,V,W> {
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
     * Convert the QuadConsumerThrowable&lt;T,U,V,W&gt; to QuadConsumer&lt;T,U,V,W&gt; with injected Exception Handler
     * @param exceptionHandler  Exception Handler of the caught Exceptions
     * @return  Converted QuadConsumer&lt;T,U,V,W&gt; that get Exceptions handled with the exceptionHandler
     */
    default QuadConsumer<T,U,V,W> withHandler(Consumer<Exception> exceptionHandler){
        QuadConsumer<T,U,V,W> consumer = (t, u, v, w) -> {
            try {
                accept(t, u, v, w);
            } catch (Exception e) {
                if(exceptionHandler != null)
                    exceptionHandler.accept(e);
            }
        };
        return consumer;
    }

    /**
     * Represents an operation that accepts four input arguments and returns no result.
     * @param <T>   Type of the first argument
     * @param <U>   Type of the second argument
     * @param <V>   Type of the third argument
     * @param <W>   Type of the fourth argument
     */
    @FunctionalInterface
    interface QuadConsumer<T,U,V,W> {
        void accept(T t, U u, V v, W w);
    }
}
