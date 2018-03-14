package com.easyworks.function;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Functional Interface identifying methods, accepting 7 arguments and returning result of type <code>R</code>,
 * while their service logic could throw Exceptions.
 * @param <T>   Type of the first argument.
 * @param <U>   Type of the second argument.
 * @param <V>   Type of the third argument.
 * @param <W>   Type of the fourth argument.
 * @param <X>   Type of the fifth argument.
 * @param <Y>   Type of the sixth argument.
 * @param <Z>   Type of the seventh argument.
 * @param <R>   Type of the returned result.
 */
@FunctionalInterface
public interface HeptaFunctionThrowable<T,U,V,W,X,Y,Z, R> extends AbstractThrowable {
    /**
     * The abstract method to be mapped to Lambda Expresion accepting 7 arguments and returning result of type <code>R</code>
     * @param t     The first argument of type <code>T</code>.
     * @param u     The second argument of type <code>U</code>.
     * @param v     The third argument of type <code>V</code>.
     * @param w     The fourth argument of type <code>W</code>.
     * @param x     The fifth argument of type <code>X</code>.
     * @param y     The sixth argument of type <code>Y</code>.
     * @param z     The seventh argument of type <code>Z</code>.
     * @return      The result of applying the given arguments.
     * @throws Exception    Any Exception could be thrown by the concerned service logic.
     */
    R apply(T t, U u, V v, W w, X x, Y y, Z z) throws Exception;

    /**
     * Convert the above <code>apply</code> method to <code>SupplierThrowable</code> with all 7 arguments
     * @param t     The first argument of type <code>T</code>.
     * @param u     The second argument of type <code>U</code>.
     * @param v     The third argument of type <code>V</code>.
     * @param w     The fourth argument of type <code>W</code>.
     * @param x     The fifth argument of type <code>X</code>.
     * @param y     The sixth argument of type <code>Y</code>.
     * @param z     The seventh argument of type <code>Z</code>.
     * @return      The converted <code>SupplierThrowable</code> by applying the 7 given arguments.
     */

    default SupplierThrowable<R> asSupplier(T t, U u, V v, W w, X x, Y y, Z z){
        return () -> apply(t, u, v, w, x, y, z);
    }


    default HeptaFunction<T,U,V,W,X,Y,Z, R> orElse(Function<Exception, R> exceptionHanlder){
        Objects.requireNonNull(exceptionHanlder);
        return (t, u, v, w, x, y, z) -> {
            try {
                return apply(t, u, v, w, x, y, z);
            } catch (Exception e) {
                return exceptionHanlder.apply(e);
            }
        };
    }

    @FunctionalInterface
    interface HeptaFunction<T,U,V,W,X,Y,Z, R> extends AbstractThrowable{
        R apply(T t, U u, V v, W w, X x, Y y, Z z);
    }
}
