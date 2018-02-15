package com.easyworks.function;

import com.easyworks.NoThrows;
import com.easyworks.RuntimeThrows;

@FunctionalInterface
public interface SupplierThrowable<T> {
    T get() throws Exception;

    default T getNoThrowable(T defaultT){
        return NoThrows.get(this::get, defaultT);
    }

    default T getRuntimeThrows(){
        return RuntimeThrows.get(this::get);
    }

    @FunctionalInterface
    interface PredicateThrowable<T> {
        boolean test(T t) throws Exception;

        default SupplierThrowable<Boolean> asSupplier(T t){
            return () -> test(t);
        }

        default boolean testNoThrows(T t){
            return NoThrows.get(asSupplier(t), false);
        }

        default boolean testRuntimeThrows(T t){
            return RuntimeThrows.get(asSupplier(t));
        }
    }

    @FunctionalInterface
    interface FunctionThrowable<T, R> {
        R apply(T t) throws Exception;

        default SupplierThrowable<R> asSupplier(T t){
            return () -> apply(t);
        }

        default R acceptNoThrowable(T t, R defaultR){
            return NoThrows.get(asSupplier(t), defaultR);
        }

        default R acceptRuntimeThrowable(T t){
            return RuntimeThrows.get(asSupplier(t));
        }
    }

    @FunctionalInterface
    interface BiFunctionThrowable<T, U, R> {
        R apply(T t, U u) throws Exception;

        default SupplierThrowable<R> asSupplier(T t, U u){
            return () -> apply(t, u);
        }

        default R acceptNoThrowable(T t, U u, R defaultR) {
            return NoThrows.get(asSupplier(t, u), defaultR);
        }

        default R acceptRuntimeThrowable(T t, U u) {
            return RuntimeThrows.get(asSupplier(t, u));
        }
    }

        @FunctionalInterface
    interface TriFunctionThrowable<T,U,V,R> {
        R apply(T t, U u, V v) throws Exception;

        default SupplierThrowable<R> asSupplier(T t, U u, V v){
            return () -> apply(t, u, v);
        }

        default R acceptNoThrowable(T t, U u, V v, R defaultR) throws Exception{
            return NoThrows.get(asSupplier(t, u, v), defaultR);
        }

        default R acceptRuntimeThrowable(T t, U u, V v){
            return RuntimeThrows.get(asSupplier(t, u, v));
        }
    }

    @FunctionalInterface
    interface QuadFunctionThrowable<T,U,V,W,R> {
        R apply(T t, U u, V v, W w) throws Exception;

        default SupplierThrowable<R> asSupplier(T t, U u, V v, W w){
            return () -> apply(t, u, v, w);
        }

        default R acceptNoThrowable(T t, U u, V v, W w, R defaultR) throws Exception{
            return NoThrows.get(asSupplier(t, u, v, w), defaultR);
        }

        default R acceptRuntimeThrowable(T t, U u, V v, W w){
            return RuntimeThrows.get(asSupplier(t, u, v, w));
        }
    }

    @FunctionalInterface
    interface PentaFunctionThrowable<T,U,V,W,X,R> {
        R apply(T t, U u, V v, W w, X x) throws Exception;

        default SupplierThrowable<R> asSupplier(T t, U u, V v, W w, X x){
            return () -> apply(t, u, v, w, x);
        }

        default R acceptNoThrowable(T t, U u, V v, W w, X x, R defaultR) throws Exception{
            return NoThrows.get(asSupplier(t, u, v, w, x), defaultR);
        }

        default R acceptRuntimeThrowable(T t, U u, V v, W w, X x){
            return RuntimeThrows.get(asSupplier(t, u, v, w, x));
        }
    }

    @FunctionalInterface
    interface HexaFunctionThrowable<T,U,V,W,X,Y,R> {
        R apply(T t, U u, V v, W w, X x, Y y) throws Exception;

        default SupplierThrowable<R> asSupplier(T t, U u, V v, W w, X x, Y y){
            return () -> apply(t, u, v, w, x, y);
        }

        default R acceptNoThrowable(T t, U u, V v, W w, X x, Y y, R defaultR) throws Exception{
            return NoThrows.get(asSupplier(t, u, v, w, x, y), defaultR);
        }

        default R acceptRuntimeThrowable(T t, U u, V v, W w, X x, Y y){
            return RuntimeThrows.get(asSupplier(t, u, v, w, x, y));
        }
    }

    @FunctionalInterface
    interface HeptaFunctionThrowable<T,U,V,W,X,Y,Z,R> {
        R apply(T t, U u, V v, W w, X x, Y y, Z z) throws Exception;

        default SupplierThrowable<R> asSupplier(T t, U u, V v, W w, X x, Y y, Z z){
            return () -> apply(t, u, v, w, x, y, z);
        }

        default R acceptNoThrowable(T t, U u, V v, W w, X x, Y y, Z z, R defaultR) throws Exception{
            return NoThrows.get(asSupplier(t, u, v, w, x, y, z), defaultR);
        }

        default R acceptRuntimeThrowable(T t, U u, V v, W w, X x, Y y, Z z){
            return RuntimeThrows.get(asSupplier(t, u, v, w, x, y, z));
        }
    }

}