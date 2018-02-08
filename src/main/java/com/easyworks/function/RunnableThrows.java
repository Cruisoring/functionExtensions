package com.easyworks.function;

import com.easyworks.NoThrows;
import com.easyworks.RuntimeThrows;

@FunctionalInterface
public interface RunnableThrows {
    void run() throws Exception;

    default void acceptNoThrows(){
        NoThrows.run(this::run);
    }

    default void acceptRuntimeThrows(){
        RuntimeThrows.run(this::run);
    }

    default RunnableThrows tryStartWith(RunnableThrows other){
        return () -> {
            NoThrows.run(other);
            this.run();
        };
    }

    default RunnableThrows tryFollowWith(RunnableThrows other) {
        return () -> {
            try {
                this.run();
            } finally {
                NoThrows.run(other);
            }
        };
    }

    @FunctionalInterface
    interface ConsumerThrows<T> {
        void accept(T t) throws Exception;

        default RunnableThrows asRunnable(T t){
            return () -> accept(t);
        }

        default void acceptNoThrows(T t){
            NoThrows.run(asRunnable(t));
        }

        default void acceptRuntimeThrows(T t){
            RuntimeThrows.run(asRunnable(t));
        }
    }

    @FunctionalInterface
    interface BiConsumerThrows<T, U> {
        void accept(T t, U u) throws Exception;

        default RunnableThrows asRunnable(T t, U u){
            return () -> accept(t, u);
        }

        default void acceptNoThrows(T t, U u){
            NoThrows.run(asRunnable(t, u));
        }

        default void acceptRuntimeThrows(T t, U u){
            RuntimeThrows.run(asRunnable(t, u));
        }
    }

    @FunctionalInterface
    interface TriConsumerThrows<T,U,V> {
        void accept(T t, U u, V v) throws Exception;

        default RunnableThrows asRunnable(T t, U u, V v){
            return () -> accept(t, u, v);
        }

        default void acceptNoThrows(T t, U u, V v){
            NoThrows.run(asRunnable(t, u, v));
        }

        default void acceptRuntimeThrows(T t, U u, V v){
            RuntimeThrows.run(asRunnable(t, u, v));
        }
    }

    @FunctionalInterface
    interface QuadConsumerThrows<T,U,V,W> {
        void accept(T t, U u, V v, W w) throws Exception;

        default RunnableThrows asRunnable(T t, U u, V v, W w){
            return () -> accept(t, u, v, w);
        }

        default void acceptNoThrows(T t, U u, V v, W w){
            NoThrows.run(asRunnable(t, u, v, w));
        }

        default void acceptRuntimeThrows(T t, U u, V v, W w){
            RuntimeThrows.run(asRunnable(t, u, v, w));
        }
    }

    @FunctionalInterface
    interface PentaConsumerThrows<T,U,V,W,X> {
        void accept(T t, U u, V v, W w, X x) throws Exception;

        default RunnableThrows asRunnable(T t, U u, V v, W w, X x){
            return () -> accept(t, u, v, w, x);
        }

        default void acceptNoThrows(T t, U u, V v, W w, X x){
            NoThrows.run(asRunnable(t, u, v, w, x));
        }

        default void acceptRuntimeThrows(T t, U u, V v, W w, X x){
            RuntimeThrows.run(asRunnable(t, u, v, w, x));
        }
    }

    @FunctionalInterface
    interface HexaConsumerThrows<T,U,V,W,X,Y> {
        void accept(T t, U u, V v, W w, X x, Y y) throws Exception;

        default RunnableThrows asRunnable(T t, U u, V v, W w, X x, Y y){
            return () -> accept(t, u, v, w, x, y);
        }

        default void acceptNoThrows(T t, U u, V v, W w, X x, Y y){
            NoThrows.run(asRunnable(t, u, v, w, x, y));
        }

        default void acceptRuntimeThrows(T t, U u, V v, W w, X x, Y y){
            RuntimeThrows.run(asRunnable(t, u, v, w, x, y));
        }
    }

    @FunctionalInterface
    interface HeptaConsumerThrows<T,U,V,W,X,Y,Z> {
        void accept(T t, U u, V v, W w, X x, Y y, Z z) throws Exception;

        default RunnableThrows asRunnable(T t, U u, V v, W w, X x, Y y, Z z){
            return () -> accept(t, u, v, w, x, y, z);
        }

        default void acceptNoThrows(T t, U u, V v, W w, X x, Y y, Z z){
            NoThrows.run(asRunnable(t, u, v, w, x, y, z));
        }

        default void acceptRuntimeThrows(T t, U u, V v, W w, X x, Y y, Z z){
            RuntimeThrows.run(asRunnable(t, u, v, w, x, y, z));
        }
    }
}