package com.easyworks;

import com.easyworks.function.RunnableThrows;
import com.easyworks.function.SupplierThrows;

/**
 * Higher-order function factor to convert methods receiving 1 to 7 arguments, as well as expected 1-7 parameters, to
 * either RunnableThrows or SupplierThrows depending on if it returns void or some result.
 */
public class Functions {
    //region Factories to convert various Consumer methods, plus expected arguments, to RunnableThrows
    // that accept no input and returns nothing.
    public static<T>  RunnableThrows toRunnable(
            RunnableThrows.ConsumerThrows<T> consumer,
            T t) throws Exception{
        return () -> consumer.accept(t);
    }

    public static<T,U>  RunnableThrows toRunnable(
            RunnableThrows.BiConsumerThrows<T,U> consumer,
            T t, U u) throws Exception{
        return () -> consumer.accept(t, u);
    }

    public static<T,U,V>  RunnableThrows toRunnable(
            RunnableThrows.TriConsumerThrows<T,U,V> consumer,
            T t, U u, V v) throws Exception{
        return () -> consumer.accept(t, u, v);
    }

    public static<T,U,V,W>  RunnableThrows toRunnable(
            RunnableThrows.QuadConsumerThrows<T,U,V,W> consumer,
            T t, U u, V v, W w) throws Exception{
        return () -> consumer.accept(t, u, v, w);
    }

    public static<T,U,V,W,X>  RunnableThrows toRunnable(
            RunnableThrows.PentaConsumerThrows<T,U,V,W,X> consumer,
            T t, U u, V v, W w, X x) throws Exception{
        return () -> consumer.accept(t, u, v, w, x);
    }

    public static<T,U,V,W,X,Y>  RunnableThrows toRunnable(
            RunnableThrows.HexaConsumerThrows<T,U,V,W,X,Y> consumer,
            T t, U u, V v, W w, X x, Y y) throws Exception{
        return () -> consumer.accept(t, u, v, w, x, y);
    }

    public static<T,U,V,W,X,Y,Z>  RunnableThrows toRunnable(
            RunnableThrows.HeptaConsumerThrows<T,U,V,W,X,Y,Z> consumer,
            T t, U u, V v, W w, X x, Y y, Z z) throws Exception{
        return () -> consumer.accept(t, u, v, w, x, y, z);
    }
    //endregion

    //region Factories to convert various functions, plus expected 1-7 arguments, to RunnableThrows
    // that accept no input and returns nothing.
    public static <T,R> SupplierThrows<R> asSupplier(
            SupplierThrows.FunctionThrows<T,R> function,
            T t)
            throws Exception {
         return () -> function.apply(t);
    }

    public static <T,U,R> SupplierThrows<R> asSupplier(
            SupplierThrows.BiFunctionThrows<T,U,R> function,
            T t, U u)
            throws Exception {
        return () -> function.apply(t, u);
    }

    public static <T,U,V,R> SupplierThrows<R> asSupplier(
            SupplierThrows.TriFunctionThrows<T,U,V,R> function,
            T t, U u, V v)
            throws Exception {
        return () -> function.apply(t, u, v);
    }

    public static <T,U,V,W,R> SupplierThrows<R> asSupplier(
            SupplierThrows.QuadFunctionThrows<T,U,V,W,R> function,
            T t, U u, V v, W w)
            throws Exception {
        return () -> function.apply(t, u, v, w);
    }

    public static <T,U,V,W,X,R> SupplierThrows<R> asSupplier(
            SupplierThrows.PentaFunctionThrows<T,U,V,W,X,R> function,
            T t, U u, V v, W w, X x)
            throws Exception {
        return () -> function.apply(t, u, v, w, x);
    }

    public static <T,U,V,W,X,Y,R> SupplierThrows<R> asSupplier(
            SupplierThrows.HexaFunctionThrows<T,U,V,W,X,Y,R> function,
            T t, U u, V v, W w, X x, Y y)
            throws Exception {
        return () -> function.apply(t, u, v, w, x, y);
    }

    public static <T,U,V,W,X,Y,Z,R> SupplierThrows<R> asSupplier(
            SupplierThrows.HeptaFunctionThrows<T,U,V,W,X,Y,Z,R> function,
            T t, U u, V v, W w, X x, Y y, Z z)
            throws Exception {
        return () -> function.apply(t, u, v, w, x, y, z);
    }
    //endregion
}
