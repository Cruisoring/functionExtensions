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
            T t,
            RunnableThrows.ConsumerThrows<T> consumer) throws Exception{
        return () -> consumer.accept(t);
    }

    public static<T,U>  RunnableThrows toRunnable(
            T t, U u,
            RunnableThrows.BiConsumerThrows<T,U> consumer) throws Exception{
        return () -> consumer.accept(t, u);
    }

    public static<T,U,V>  RunnableThrows toRunnable(
            T t, U u, V v,
            RunnableThrows.TriConsumerThrows<T,U,V> consumer) throws Exception{
        return () -> consumer.accept(t, u, v);
    }

    public static<T,U,V,W>  RunnableThrows toRunnable(
            T t, U u, V v, W w,
            RunnableThrows.QuadConsumerThrows<T,U,V,W> consumer) throws Exception{
        return () -> consumer.accept(t, u, v, w);
    }

    public static<T,U,V,W,X>  RunnableThrows toRunnable(
            T t, U u, V v, W w, X x,
            RunnableThrows.PentaConsumerThrows<T,U,V,W,X> consumer) throws Exception{
        return () -> consumer.accept(t, u, v, w, x);
    }

    public static<T,U,V,W,X,Y>  RunnableThrows toRunnable(
            T t, U u, V v, W w, X x, Y y,
            RunnableThrows.HexaConsumerThrows<T,U,V,W,X,Y> consumer) throws Exception{
        return () -> consumer.accept(t, u, v, w, x, y);
    }

    public static<T,U,V,W,X,Y,Z>  RunnableThrows toRunnable(
            T t, U u, V v, W w, X x, Y y, Z z,
            RunnableThrows.HeptaConsumerThrows<T,U,V,W,X,Y,Z> consumer) throws Exception{
        return () -> consumer.accept(t, u, v, w, x, y, z);
    }
    //endregion

    //region Factories to convert various functions, plus expected 1-7 arguments, to RunnableThrows
    // that accept no input and returns nothing.
    public static <T> SupplierThrows<Boolean> asSupplier(
            T t,
            SupplierThrows.PredicateThrows<T> predicate)
            throws Exception {
        return () -> predicate.test(t);
    }

    public static <T,R> SupplierThrows<R> asSupplier(
            T t,
            SupplierThrows.FunctionThrows<T,R> function)
            throws Exception {
         return () -> function.apply(t);
    }

    public static <T,U,R> SupplierThrows<R> asSupplier(
            T t, U u,
            SupplierThrows.BiFunctionThrows<T,U,R> function)
            throws Exception {
        return () -> function.apply(t, u);
    }

    public static <T,U,V,R> SupplierThrows<R> asSupplier(
            T t, U u, V v,
            SupplierThrows.TriFunctionThrows<T,U,V,R> function)
            throws Exception {
        return () -> function.apply(t, u, v);
    }

    public static <T,U,V,W,R> SupplierThrows<R> asSupplier(
            T t, U u, V v, W w,
            SupplierThrows.QuadFunctionThrows<T,U,V,W,R> function)
            throws Exception {
        return () -> function.apply(t, u, v, w);
    }

    public static <T,U,V,W,X,R> SupplierThrows<R> asSupplier(
            T t, U u, V v, W w, X x,
            SupplierThrows.PentaFunctionThrows<T,U,V,W,X,R> function)
            throws Exception {
        return () -> function.apply(t, u, v, w, x);
    }

    public static <T,U,V,W,X,Y,R> SupplierThrows<R> asSupplier(
            T t, U u, V v, W w, X x, Y y,
            SupplierThrows.HexaFunctionThrows<T,U,V,W,X,Y,R> function)
            throws Exception {
        return () -> function.apply(t, u, v, w, x, y);
    }

    public static <T,U,V,W,X,Y,Z,R> SupplierThrows<R> asSupplier(
            T t, U u, V v, W w, X x, Y y, Z z,
            SupplierThrows.HeptaFunctionThrows<T,U,V,W,X,Y,Z,R> function)
            throws Exception {
        return () -> function.apply(t, u, v, w, x, y, z);
    }
    //endregion
}
