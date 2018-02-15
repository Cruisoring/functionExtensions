package com.easyworks.function;

import com.easyworks.NoThrows;
import com.easyworks.RuntimeThrows;

@FunctionalInterface
public interface RunnableThrowable {
    void run() throws Exception;

    default void acceptNoThrowable(){
        NoThrows.run(this::run);
    }

    default void acceptRuntimeThrowable(){
        RuntimeThrows.run(this::run);
    }

    default RunnableThrowable tryStartWith(RunnableThrowable other){
        return () -> {
            NoThrows.run(other);
            this.run();
        };
    }

    default RunnableThrowable tryFollowWith(RunnableThrowable other) {
        return () -> {
            try {
                this.run();
            } finally {
                NoThrows.run(other);
            }
        };
    }

    @FunctionalInterface
    interface ConsumerThrowable<T> {
        void accept(T t) throws Exception;

        default RunnableThrowable asRunnable(T t){
            return () -> accept(t);
        }

        default void acceptNoThrowable(T t){
            NoThrows.run(asRunnable(t));
        }

        default void acceptRuntimeThrowable(T t){
            RuntimeThrows.run(asRunnable(t));
        }
    }

    @FunctionalInterface
    interface BiConsumerThrowable<T, U> {
        void accept(T t, U u) throws Exception;

        default RunnableThrowable asRunnable(T t, U u){
            return () -> accept(t, u);
        }

        default void acceptNoThrowable(T t, U u){
            NoThrows.run(asRunnable(t, u));
        }

        default void acceptRuntimeThrowable(T t, U u){
            RuntimeThrows.run(asRunnable(t, u));
        }
    }

    @FunctionalInterface
    interface TriConsumerThrowable<T,U,V> {
        void accept(T t, U u, V v) throws Exception;

        default RunnableThrowable asRunnable(T t, U u, V v){
            return () -> accept(t, u, v);
        }

        default void acceptNoThrowable(T t, U u, V v){
            NoThrows.run(asRunnable(t, u, v));
        }

        default void acceptRuntimeThrowable(T t, U u, V v){
            RuntimeThrows.run(asRunnable(t, u, v));
        }
    }

    @FunctionalInterface
    interface QuadConsumerThrowable<T,U,V,W> {
        void accept(T t, U u, V v, W w) throws Exception;

        default RunnableThrowable asRunnable(T t, U u, V v, W w){
            return () -> accept(t, u, v, w);
        }

        default void acceptNoThrowable(T t, U u, V v, W w){
            NoThrows.run(asRunnable(t, u, v, w));
        }

        default void acceptRuntimeThrowable(T t, U u, V v, W w){
            RuntimeThrows.run(asRunnable(t, u, v, w));
        }
    }

    @FunctionalInterface
    interface PentaConsumerThrowable<T,U,V,W,X> {
        void accept(T t, U u, V v, W w, X x) throws Exception;

        default RunnableThrowable asRunnable(T t, U u, V v, W w, X x){
            return () -> accept(t, u, v, w, x);
        }

        default void acceptNoThrowable(T t, U u, V v, W w, X x){
            NoThrows.run(asRunnable(t, u, v, w, x));
        }

        default void acceptRuntimeThrowable(T t, U u, V v, W w, X x){
            RuntimeThrows.run(asRunnable(t, u, v, w, x));
        }
    }

    @FunctionalInterface
    interface HexaConsumerThrowable<T,U,V,W,X,Y> {
        void accept(T t, U u, V v, W w, X x, Y y) throws Exception;

        default RunnableThrowable asRunnable(T t, U u, V v, W w, X x, Y y){
            return () -> accept(t, u, v, w, x, y);
        }

        default void acceptNoThrowable(T t, U u, V v, W w, X x, Y y){
            NoThrows.run(asRunnable(t, u, v, w, x, y));
        }

        default void acceptRuntimeThrowable(T t, U u, V v, W w, X x, Y y){
            RuntimeThrows.run(asRunnable(t, u, v, w, x, y));
        }
    }

    @FunctionalInterface
    interface HeptaConsumerThrowable<T,U,V,W,X,Y,Z> {
        void accept(T t, U u, V v, W w, X x, Y y, Z z) throws Exception;

        default RunnableThrowable asRunnable(T t, U u, V v, W w, X x, Y y, Z z){
            return () -> accept(t, u, v, w, x, y, z);
        }

        default void acceptNoThrowable(T t, U u, V v, W w, X x, Y y, Z z){
            NoThrows.run(asRunnable(t, u, v, w, x, y, z));
        }

        default void acceptRuntimeThrowable(T t, U u, V v, W w, X x, Y y, Z z){
            RuntimeThrows.run(asRunnable(t, u, v, w, x, y, z));
        }
    }
}