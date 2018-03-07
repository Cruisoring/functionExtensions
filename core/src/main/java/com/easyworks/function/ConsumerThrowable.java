package com.easyworks.function;

/**
 * Functional Interface identifying methods, accepting one argument and returning nothing,
 * while their service logic could throw Exceptions.
 * @param <T>   Type of the first argument.
 */
@FunctionalInterface
public interface ConsumerThrowable<T> extends AbstractThrowable {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 1 argument and returning nothing.
     * @param t     Needed argument of type T.
     * @throws Exception    Any Exception could be thrown by the concerned service logic.
     */
    void accept(T t) throws Exception;

    /**
     * Convert the above <code>accept</code> method to <code>RunnableThrowable</code> with argument <code>t</code> captured
     * @param t     The first argument of type <code>T</code>.
     * @return  The converted <code>RunnableThrowable</code> with with argument <code>t</code> captured
     */
    default RunnableThrowable asRunnable(T t){
        return () -> accept(t);
    }
}
