package com.easyworks;

import com.easyworks.function.RunnableThrows;
import com.easyworks.function.SupplierThrows;

/**
 * Higher-order function factor to convert methods receiving 1 to 7 arguments, as well as expected 1-7 parameters, to
 * either RunnableThrows or SupplierThrows depending on if it returns void or some result.
 */
public class Functions<R> {

    private ExceptionHandler<R> handler;
    public Functions(ExceptionHandler<R> handler){
        this.handler = handler;
    }

    protected void run(RunnableThrows runnableThrows){
        try {
            runnableThrows.run();
        } catch (Exception ex){
            handler.apply(ex);
        }
    }

    protected <T> T get(SupplierThrows<T> supplierThrows){
        try {
            return supplierThrows.get();
        } catch (Exception ex){
            return (T) handler.apply(ex);
        }
    }

    public <T> void run(
            T t,
            RunnableThrows.ConsumerThrows<T> consumer){
        run(() -> consumer.accept(t));
    }

    public <T,U> void run(
            T t, U u,
            RunnableThrows.BiConsumerThrows<T,U> consumer){
        run(() -> consumer.accept(t,u));
    }

    public <T,U,V> void run(
            T t, U u, V v,
            RunnableThrows.TriConsumerThrows<T,U,V> consumer){
        run(() -> consumer.accept(t,u,v));
    }

    public <T,U,V,W> void run(
            T t, U u, V v, W w,
            RunnableThrows.QuadConsumerThrows<T,U,V,W> consumer){
        run(() -> consumer.accept(t,u,v,w));
    }

    public <T,U,V,W,X> void run(
            T t, U u, V v, W w, X x,
            RunnableThrows.PentaConsumerThrows<T,U,V,W,X> consumer){
        run(() -> consumer.accept(t,u,v,w,x));
    }

    public <T,U,V,W,X,Y> void run(
            T t, U u, V v, W w, X x, Y y,
            RunnableThrows.HexaConsumerThrows<T,U,V,W,X,Y> consumer){
        run(() -> consumer.accept(t,u,v,w,x,y));
    }

    public <T,U,V,W,X,Y,Z> void run(
            T t, U u, V v, W w, X x, Y y, Z z,
            RunnableThrows.HeptaConsumerThrows<T,U,V,W,X,Y,Z> consumer){
        run(() -> consumer.accept(t,u,v,w,x,y,z));
    }

    public <T,R> R get(
            T t,
            SupplierThrows.FunctionThrows<T,R> function){
        return get(() -> function.apply(t));
    }

    public <T,U,R> R get(
            T t, U u,
            SupplierThrows.BiFunctionThrows<T,U,R> function){
        return get(() -> function.apply(t,u));
    }

    public <T,U,V,R> R get(
            T t, U u, V v,
            SupplierThrows.TriFunctionThrows<T,U,V,R> function){
        return get(() -> function.apply(t,u,v));
    }

    public <T,U,V,W,R> R get(
            T t, U u, V v, W w,
            SupplierThrows.QuadFunctionThrows<T,U,V,W,R> function){
        return get(() -> function.apply(t,u,v,w));
    }

    public <T,U,V,W,X,R> R get(
            T t, U u, V v, W w, X x,
            SupplierThrows.PentaFunctionThrows<T,U,V,W,X,R> function){
        return get(() -> function.apply(t,u,v,w,x));
    }

    public <T,U,V,W,X,Y,R> R get(
            T t, U u, V v, W w, X x, Y y,
            SupplierThrows.HexaFunctionThrows<T,U,V,W,X,Y,R> function){
        return get(() -> function.apply(t,u,v,w,x,y));
    }

    public <T,U,V,W,X,Y,Z,R> R get(
            T t, U u, V v, W w, X x, Y y, Z z,
            SupplierThrows.HeptaFunctionThrows<T,U,V,W,X,Y,Z,R> function){
        return get(() -> function.apply(t,u,v,w,x,y,z));
    }

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

    //region other static helper functions that might be removed
    //    public static Functions Default = null;
//
//    public static void execute(RunnableThrows runnableThrows){
//        Default.run(runnableThrows);
//    }
//
//    public static <T> void execute(T t,
//                               RunnableThrows.ConsumerThrows<T> consumer){
//        Default.run(t, consumer);
//    }
//
//    public static <T,U> void execute(
//            T t, U u,
//            RunnableThrows.BiConsumerThrows<T,U> consumer){
//        Default.run(t,u,consumer);
//    }
//
//    public static <T,U,V> void execute(
//            T t, U u, V v,
//            RunnableThrows.TriConsumerThrows<T,U,V> consumer){
//        Default.run(t,u,v,consumer);
//    }
//
//    public static <T,U,V,W> void execute(
//            T t, U u, V v, W w,
//            RunnableThrows.QuadConsumerThrows<T,U,V,W> consumer){
//        Default.run(t,u,v,w,consumer);
//    }
//
//    public static <T,U,V,W,X> void execute(
//            T t, U u, V v, W w, X x,
//            RunnableThrows.PentaConsumerThrows<T,U,V,W,X> consumer){
//        Default.run(t,u,v,w,x,consumer);
//    }
//
//    public static <T,U,V,W,X,Y> void execute(
//            T t, U u, V v, W w, X x, Y y,
//            RunnableThrows.HexaConsumerThrows<T,U,V,W,X,Y> consumer){
//        Default.run(t,u,v,w,x,y,consumer);
//    }
//
//    public static <T,U,V,W,X,Y,Z> void execute(
//            T t, U u, V v, W w, X x, Y y, Z z,
//            RunnableThrows.HeptaConsumerThrows<T,U,V,W,X,Y,Z> consumer){
//        Default.run(t,u,v,w,x,y,z,consumer);
//    }
//
//    public static <R> R returns(SupplierThrows supplierThrows){
//        return (R) Default.get(supplierThrows);
//    }
//
//    public static <T,R> R returns(
//            T t,
//            SupplierThrows.FunctionThrows<T,R> function){
//        return (R) Default.get(() -> function.apply(t));
//    }
//
//    public static  <T,U,R> R returns(
//            T t, U u,
//            SupplierThrows.BiFunctionThrows<T,U,R> function){
//        return (R) Default.get(() -> function.apply(t,u));
//    }
//endregion

}
