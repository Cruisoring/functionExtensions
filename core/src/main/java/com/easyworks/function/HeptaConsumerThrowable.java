package com.easyworks.function;

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
public interface HeptaConsumerThrowable<T,U,V,W,X,Y,Z> extends AbstractThrowable {
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
     * Convert the above <code>accept</code> method to <code>RunnableThrowable</code> with all 7 arguments
     * @param t     The first argument of type <code>T</code>.
     * @param u     The second argument of type <code>U</code>.
     * @param v     The third argument of type <code>V</code>.
     * @param w     The fourth argument of type <code>W</code>.
     * @param x     The fifth argument of type <code>X</code>.
     * @param y     The sixth argument of type <code>Y</code>.
     * @param z     The seventh argument of type <code>Z</code>.
     * @return  The converted <code>RunnableThrowable</code> with all 7 argments captured when called.
     */
    default RunnableThrowable asRunnable(T t, U u, V v, W w, X x, Y y, Z z){
        return () -> accept(t, u, v, w, x, y, z);
    }
}
