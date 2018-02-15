package com.easyworks;

import com.easyworks.function.RunnableThrowable;
import com.easyworks.function.SupplierThrowable;

/**
 * Higher-order function factor to convert methods receiving 1 to 7 arguments, as well as expected 1-7 parameters, to
 * either RunnableThrowable or SupplierThrowable depending on if it returns void or some result.
 */
public class Functions<R> {

    private ExceptionHandler<R> handler;
    public Functions(ExceptionHandler<R> handler){
        this.handler = handler;
    }

    protected void run(RunnableThrowable runnableThrowable){
        try {
            runnableThrowable.run();
        } catch (Exception ex){
            handler.apply(ex);
        }
    }

    protected <T> T get(SupplierThrowable<T> supplierThrowable){
        try {
            return supplierThrowable.get();
        } catch (Exception ex){
            return (T) handler.apply(ex);
        }
    }

    public <T> void run(
            T t,
            RunnableThrowable.ConsumerThrowable<T> consumer){
        run(() -> consumer.accept(t));
    }

    public <T,U> void run(
            T t, U u,
            RunnableThrowable.BiConsumerThrowable<T,U> consumer){
        run(() -> consumer.accept(t,u));
    }

    public <T,U,V> void run(
            T t, U u, V v,
            RunnableThrowable.TriConsumerThrowable<T,U,V> consumer){
        run(() -> consumer.accept(t,u,v));
    }

    public <T,U,V,W> void run(
            T t, U u, V v, W w,
            RunnableThrowable.QuadConsumerThrowable<T,U,V,W> consumer){
        run(() -> consumer.accept(t,u,v,w));
    }

    public <T,U,V,W,X> void run(
            T t, U u, V v, W w, X x,
            RunnableThrowable.PentaConsumerThrowable<T,U,V,W,X> consumer){
        run(() -> consumer.accept(t,u,v,w,x));
    }

    public <T,U,V,W,X,Y> void run(
            T t, U u, V v, W w, X x, Y y,
            RunnableThrowable.HexaConsumerThrowable<T,U,V,W,X,Y> consumer){
        run(() -> consumer.accept(t,u,v,w,x,y));
    }

    public <T,U,V,W,X,Y,Z> void run(
            T t, U u, V v, W w, X x, Y y, Z z,
            RunnableThrowable.HeptaConsumerThrowable<T,U,V,W,X,Y,Z> consumer){
        run(() -> consumer.accept(t,u,v,w,x,y,z));
    }

    public <T,R> R get(
            T t,
            SupplierThrowable.FunctionThrowable<T,R> function){
        return get(() -> function.apply(t));
    }

    public <T,U,R> R get(
            T t, U u,
            SupplierThrowable.BiFunctionThrowable<T,U,R> function){
        return get(() -> function.apply(t,u));
    }

    public <T,U,V,R> R get(
            T t, U u, V v,
            SupplierThrowable.TriFunctionThrowable<T,U,V,R> function){
        return get(() -> function.apply(t,u,v));
    }

    public <T,U,V,W,R> R get(
            T t, U u, V v, W w,
            SupplierThrowable.QuadFunctionThrowable<T,U,V,W,R> function){
        return get(() -> function.apply(t,u,v,w));
    }

    public <T,U,V,W,X,R> R get(
            T t, U u, V v, W w, X x,
            SupplierThrowable.PentaFunctionThrowable<T,U,V,W,X,R> function){
        return get(() -> function.apply(t,u,v,w,x));
    }

    public <T,U,V,W,X,Y,R> R get(
            T t, U u, V v, W w, X x, Y y,
            SupplierThrowable.HexaFunctionThrowable<T,U,V,W,X,Y,R> function){
        return get(() -> function.apply(t,u,v,w,x,y));
    }

    public <T,U,V,W,X,Y,Z,R> R get(
            T t, U u, V v, W w, X x, Y y, Z z,
            SupplierThrowable.HeptaFunctionThrowable<T,U,V,W,X,Y,Z,R> function){
        return get(() -> function.apply(t,u,v,w,x,y,z));
    }

    //region Factories to convert various Consumer methods, plus expected arguments, to RunnableThrowable
    // that accept no input and returns nothing.
    public static<T> RunnableThrowable toRunnable(
            T t,
            RunnableThrowable.ConsumerThrowable<T> consumer) throws Exception{
        return () -> consumer.accept(t);
    }

    public static<T,U> RunnableThrowable toRunnable(
            T t, U u,
            RunnableThrowable.BiConsumerThrowable<T,U> consumer) throws Exception{
        return () -> consumer.accept(t, u);
    }

    public static<T,U,V> RunnableThrowable toRunnable(
            T t, U u, V v,
            RunnableThrowable.TriConsumerThrowable<T,U,V> consumer) throws Exception{
        return () -> consumer.accept(t, u, v);
    }

    public static<T,U,V,W> RunnableThrowable toRunnable(
            T t, U u, V v, W w,
            RunnableThrowable.QuadConsumerThrowable<T,U,V,W> consumer) throws Exception{
        return () -> consumer.accept(t, u, v, w);
    }

    public static<T,U,V,W,X> RunnableThrowable toRunnable(
            T t, U u, V v, W w, X x,
            RunnableThrowable.PentaConsumerThrowable<T,U,V,W,X> consumer) throws Exception{
        return () -> consumer.accept(t, u, v, w, x);
    }

    public static<T,U,V,W,X,Y> RunnableThrowable toRunnable(
            T t, U u, V v, W w, X x, Y y,
            RunnableThrowable.HexaConsumerThrowable<T,U,V,W,X,Y> consumer) throws Exception{
        return () -> consumer.accept(t, u, v, w, x, y);
    }

    public static<T,U,V,W,X,Y,Z> RunnableThrowable toRunnable(
            T t, U u, V v, W w, X x, Y y, Z z,
            RunnableThrowable.HeptaConsumerThrowable<T,U,V,W,X,Y,Z> consumer) throws Exception{
        return () -> consumer.accept(t, u, v, w, x, y, z);
    }
    //endregion

    //region Factories to convert various functions, plus expected 1-7 arguments, to RunnableThrowable
    // that accept no input and returns nothing.
    public static <T> SupplierThrowable<Boolean> asSupplier(
            T t,
            SupplierThrowable.PredicateThrowable<T> predicate)
            throws Exception {
        return () -> predicate.test(t);
    }

    public static <T,R> SupplierThrowable<R> asSupplier(
            T t,
            SupplierThrowable.FunctionThrowable<T,R> function)
            throws Exception {
         return () -> function.apply(t);
    }

    public static <T,U,R> SupplierThrowable<R> asSupplier(
            T t, U u,
            SupplierThrowable.BiFunctionThrowable<T,U,R> function)
            throws Exception {
        return () -> function.apply(t, u);
    }

    public static <T,U,V,R> SupplierThrowable<R> asSupplier(
            T t, U u, V v,
            SupplierThrowable.TriFunctionThrowable<T,U,V,R> function)
            throws Exception {
        return () -> function.apply(t, u, v);
    }

    public static <T,U,V,W,R> SupplierThrowable<R> asSupplier(
            T t, U u, V v, W w,
            SupplierThrowable.QuadFunctionThrowable<T,U,V,W,R> function)
            throws Exception {
        return () -> function.apply(t, u, v, w);
    }

    public static <T,U,V,W,X,R> SupplierThrowable<R> asSupplier(
            T t, U u, V v, W w, X x,
            SupplierThrowable.PentaFunctionThrowable<T,U,V,W,X,R> function)
            throws Exception {
        return () -> function.apply(t, u, v, w, x);
    }

    public static <T,U,V,W,X,Y,R> SupplierThrowable<R> asSupplier(
            T t, U u, V v, W w, X x, Y y,
            SupplierThrowable.HexaFunctionThrowable<T,U,V,W,X,Y,R> function)
            throws Exception {
        return () -> function.apply(t, u, v, w, x, y);
    }

    public static <T,U,V,W,X,Y,Z,R> SupplierThrowable<R> asSupplier(
            T t, U u, V v, W w, X x, Y y, Z z,
            SupplierThrowable.HeptaFunctionThrowable<T,U,V,W,X,Y,Z,R> function)
            throws Exception {
        return () -> function.apply(t, u, v, w, x, y, z);
    }
    //endregion

    //region other static helper functions that might be removed
    //    public static Functions Default = null;
//
//    public static void execute(RunnableThrowable runnableThrows){
//        Default.run(runnableThrows);
//    }
//
//    public static <T> void execute(T t,
//                               RunnableThrowable.ConsumerThrowable<T> consumer){
//        Default.run(t, consumer);
//    }
//
//    public static <T,U> void execute(
//            T t, U u,
//            RunnableThrowable.BiConsumerThrowable<T,U> consumer){
//        Default.run(t,u,consumer);
//    }
//
//    public static <T,U,V> void execute(
//            T t, U u, V v,
//            RunnableThrowable.TriConsumerThrowable<T,U,V> consumer){
//        Default.run(t,u,v,consumer);
//    }
//
//    public static <T,U,V,W> void execute(
//            T t, U u, V v, W w,
//            RunnableThrowable.QuadConsumerThrowable<T,U,V,W> consumer){
//        Default.run(t,u,v,w,consumer);
//    }
//
//    public static <T,U,V,W,X> void execute(
//            T t, U u, V v, W w, X x,
//            RunnableThrowable.PentaConsumerThrowable<T,U,V,W,X> consumer){
//        Default.run(t,u,v,w,x,consumer);
//    }
//
//    public static <T,U,V,W,X,Y> void execute(
//            T t, U u, V v, W w, X x, Y y,
//            RunnableThrowable.HexaConsumerThrowable<T,U,V,W,X,Y> consumer){
//        Default.run(t,u,v,w,x,y,consumer);
//    }
//
//    public static <T,U,V,W,X,Y,Z> void execute(
//            T t, U u, V v, W w, X x, Y y, Z z,
//            RunnableThrowable.HeptaConsumerThrowable<T,U,V,W,X,Y,Z> consumer){
//        Default.run(t,u,v,w,x,y,z,consumer);
//    }
//
//    public static <R> R returns(SupplierThrowable supplierThrows){
//        return (R) Default.get(supplierThrows);
//    }
//
//    public static <T,R> R returns(
//            T t,
//            SupplierThrowable.FunctionThrowable<T,R> function){
//        return (R) Default.get(() -> function.apply(t));
//    }
//
//    public static  <T,U,R> R returns(
//            T t, U u,
//            SupplierThrowable.BiFunctionThrowable<T,U,R> function){
//        return (R) Default.get(() -> function.apply(t,u));
//    }
//endregion

}
