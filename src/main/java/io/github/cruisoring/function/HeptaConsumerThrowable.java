package io.github.cruisoring.function;

import java.util.function.Consumer;

/**
 * Functional Interface identifying methods, accepting 7 arguments and returning nothing,
 * while their service logic could throw Exceptions.
 * @param <T>   Type of the first argument.
 * @param <U>   Type of the second argument.
 * @param <V>   Type of the third argument.
 * @param <W>   Type of the fourth argument.
 * @param <X>   Type of the fifth argument.
 * @param <Y>   Type of the sixth argument.
 * @param <Z>   Type of the seventh argument.
 */
@FunctionalInterface
public interface HeptaConsumerThrowable<T,U,V,W,X,Y,Z> {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 7 arguments and returning nothing.
     * @param t     The first argument of type <code>T</code>.
     * @param u     The second argument of type <code>U</code>.
     * @param v     The third argument of type <code>V</code>.
     * @param w     The fourth argument of type <code>W</code>.
     * @param x     The fifth argument of type <code>X</code>.
     * @param y     The sixth argument of type <code>Y</code>.
     * @param z     The seventh argument of type <code>Z</code>.
     * @throws Exception    Any Exception could be thrown by the concerned service logic.
     */
    void accept(T t, U u, V v, W w, X x, Y y, Z z) throws Exception;

    /**
     * Execute <code>accept()</code> and ignore any Exceptions thrown.
     * @param t     The first argument of type <code>T</code>.
     * @param u     The second argument of type <code>U</code>.
     * @param v     The third argument of type <code>V</code>.
     * @param w     The fourth argument of type <code>W</code>.
     * @param x     The fifth argument of type <code>X</code>.
     * @param y     The sixth argument of type <code>Y</code>.
     * @param z     The seventh argument of type <code>Z</code>.
     */
    default void tryAccept(T t, U u, V v, W w, X x, Y y, Z z){
        try {
            accept(t, u, v, w, x, y, z);
        }catch (Exception e){ }
    }


    /**
     * Convert the HeptaConsumerThrowable&lt;T,U,V,W,X,Y,Z&gt; to HeptaConsumer&lt;T,U,V,W,X,Y,Z&gt; with injected Exception Handler
     * @param exceptionHandler  Exception Handler of the caught Exceptions
     * @return  Converted HeptaConsumer&lt;T,U,V,W,X,Y,Z&gt; that get Exceptions handled with the exceptionHandler
     */
    default HeptaConsumer<T,U,V,W,X,Y,Z> withHandler(Consumer<Exception> exceptionHandler){
        HeptaConsumer<T,U,V,W,X,Y,Z> consumer = (t, u, v, w, x, y, z) -> {
            try {
                accept(t, u, v, w, x, y, z);
            } catch (Exception e) {
                if(exceptionHandler != null)
                    exceptionHandler.accept(e);
            }
        };
        return consumer;
    }

    /**
     * Represents an operation that accepts seven input arguments and returns no result.
     * @param <T>   Type of the first argument
     * @param <U>   Type of the second argument
     * @param <V>   Type of the third argument
     * @param <W>   Type of the fourth argument
     * @param <X>   Type of the fifth argument
     * @param <Y>   Type of the sixth argument
     * @param <Z>   Type of the seventh argument
     */
    @FunctionalInterface
    interface HeptaConsumer<T,U,V,W,X,Y,Z> {
        void accept(T t, U u, V v, W w, X x, Y y, Z z);
    }
}
