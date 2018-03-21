package com.easyworks.function;


import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Functional Interface identifying methods, accepting 2 arguments and returning nothing,
 * while their service logic could throw Exceptions.
 * @param <T>   Type of the first argument.
 * @param <U>   Type of the second argument.
 */
@FunctionalInterface
public interface BiConsumerThrowable<T, U> extends AbstractThrowable {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 2 arguments and returning nothing.
     * @param t     The first argument of type <code>T</code>.
     * @param u     The second argument of type <code>U</code>.
     * @throws Exception    Any Exception could be thrown by the concerned service logic.
     */
    void accept(T t, U u) throws Exception;

    /**
     * Convert the above <code>accept</code> method to <code>RunnableThrowable</code> with both 2 arguments
     * @param t     The first argument of type <code>T</code>.
     * @param u     The second argument of type <code>U</code>.
     * @return  The converted <code>RunnableThrowable</code> with both 2 argments captured when called.
     */
    default RunnableThrowable asRunnable(T t, U u){
        return () -> accept(t, u);
    }


    default BiConsumer<T,U> orElse(Consumer<Exception> exceptionHandler){
        return (t, u) -> {
            try {
                accept(t, u);
            } catch (Exception e) {
                if(exceptionHandler != null)
                    exceptionHandler.accept(e);
            }
        };
    }
}
