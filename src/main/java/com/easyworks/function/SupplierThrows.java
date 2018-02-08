package com.easyworks.function;

import com.easyworks.NoThrows;
import com.easyworks.RuntimeThrows;

@FunctionalInterface
public interface SupplierThrows<T> {
    T get() throws Exception;

    default T getNoThrows(T defaultT){
        return NoThrows.get(this::get, defaultT);
    }

    default T getRuntimeThrows(){
        return RuntimeThrows.get(this::get);
    }

    @FunctionalInterface
    interface FunctionThrows<T, R> {
        R apply(T t) throws Exception;

        default SupplierThrows<R> asSupplier(T t){
            return () -> apply(t);
        }

        default R applyNoThrows(T t, R defaultR){
            return NoThrows.get(asSupplier(t), defaultR);
        }

        default R applyRuntimeThrows(T t, R defaultR){
            return RuntimeThrows.get(asSupplier(t));
        }
    }

    @FunctionalInterface
    interface BiFunctionThrows<T, U, R> {
        R apply(T t, U u) throws Exception;

        default SupplierThrows<R> asSupplier(T t, U u){
            return () -> apply(t, u);
        }

        default R applyNoThrows(T t, U u, R defaultR) {
            return NoThrows.get(asSupplier(t, u), defaultR);
        }

        default R applyRuntimeThrows(T t, U u) {
            return RuntimeThrows.get(asSupplier(t, u));
        }
    }

        @FunctionalInterface
    interface TriFunctionThrows<T,U,V,R> {
        R apply(T t, U u, V v) throws Exception;

        default SupplierThrows<R> asSupplier(T t, U u, V v){
            return () -> apply(t, u, v);
        }

        default R applyNoThrows(T t, U u, V v, R defaultR) throws Exception{
            return NoThrows.get(asSupplier(t, u, v), defaultR);
        }

        default R applyRuntimeThrows(T t, U u, V v){
            return RuntimeThrows.get(asSupplier(t, u, v));
        }
    }

    @FunctionalInterface
    interface QuadFunctionThrows<T,U,V,W,R> {
        R apply(T t, U u, V v, W w) throws Exception;

        default SupplierThrows<R> asSupplier(T t, U u, V v, W w){
            return () -> apply(t, u, v, w);
        }

        default R applyNoThrows(T t, U u, V v, W w, R defaultR) throws Exception{
            return NoThrows.get(asSupplier(t, u, v, w), defaultR);
        }

        default R applyRuntimeThrows(T t, U u, V v, W w){
            return RuntimeThrows.get(asSupplier(t, u, v, w));
        }
    }

    @FunctionalInterface
    interface PentaFunctionThrows<T,U,V,W,X,R> {
        R apply(T t, U u, V v, W w, X x) throws Exception;

        default SupplierThrows<R> asSupplier(T t, U u, V v, W w, X x){
            return () -> apply(t, u, v, w, x);
        }

        default R applyNoThrows(T t, U u, V v, W w, X x, R defaultR) throws Exception{
            return NoThrows.get(asSupplier(t, u, v, w, x), defaultR);
        }

        default R applyRuntimeThrows(T t, U u, V v, W w, X x){
            return RuntimeThrows.get(asSupplier(t, u, v, w, x));
        }
    }

    @FunctionalInterface
    interface HexaFunctionThrows<T,U,V,W,X,Y,R> {
        R apply(T t, U u, V v, W w, X x, Y y) throws Exception;

        default SupplierThrows<R> asSupplier(T t, U u, V v, W w, X x, Y y){
            return () -> apply(t, u, v, w, x, y);
        }

        default R applyNoThrows(T t, U u, V v, W w, X x, Y y, R defaultR) throws Exception{
            return NoThrows.get(asSupplier(t, u, v, w, x, y), defaultR);
        }

        default R applyRuntimeThrows(T t, U u, V v, W w, X x, Y y){
            return RuntimeThrows.get(asSupplier(t, u, v, w, x, y));
        }
    }

    @FunctionalInterface
    interface HeptaFunctionThrows<T,U,V,W,X,Y,Z,R> {
        R apply(T t, U u, V v, W w, X x, Y y, Z z) throws Exception;

        default SupplierThrows<R> asSupplier(T t, U u, V v, W w, X x, Y y, Z z){
            return () -> apply(t, u, v, w, x, y, z);
        }

        default R applyNoThrows(T t, U u, V v, W w, X x, Y y, Z z, R defaultR) throws Exception{
            return NoThrows.get(asSupplier(t, u, v, w, x, y, z), defaultR);
        }

        default R applyRuntimeThrows(T t, U u, V v, W w, X x, Y y, Z z){
            return RuntimeThrows.get(asSupplier(t, u, v, w, x, y, z));
        }
    }

}