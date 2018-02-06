package com.easyworks.utilities;

import java.util.Arrays;
import java.util.function.Supplier;

public class Tuple {
    public static final Unit UNIT = new Unit();

    public static String getClassName(Object object){
        if(object == null) return null;
        return object.getClass().getName();
    }

    //region Factories to create Strong-typed Tuple instances based on the number of given arguments
    public static Unit create(){
        return UNIT;
    }

    public static <T> Single<T> create(T t){
        return new Single<>(t);
    }

    public static <T,U> Dual<T,U> create(T t, U u){
        return new Dual<>(t, u);
    }

    public static <T,U,V> Triple<T,U,V> create(T t, U u, V v){
        return new Triple<>(t, u, v);
    }

    public static <T,U,V,W> Quad<T,U,V,W> create(T t, U u, V v, W w){
        return new Quad<>(t, u, v, w);
    }

    public static <T,U,V,W,X> Penta<T,U,V,W,X> create(T t, U u, V v, W w, X x){
        return new Penta<>(t, u, v, w, x);
    }

    public static <T,U,V,W,X,Y> Hexa<T,U,V,W,X,Y> create(T t, U u, V v, W w, X x, Y y){
        return new Hexa<>(t, u, v, w, x, y);
    }
    //endregion Factories to create Strong-typed Tuple instances based on the number of given arguments

    //region Extended Strong-typed classes
    protected static class Unit extends Tuple {
        protected Unit(){
            super();
        }
    }

    protected static class Single<T> extends Tuple {
        private final Supplier<T> _1;

        public Single(T t){
            super(t);
            _1 = () -> t;
        }

        public T getFirst() {
            return _1.get();
        }
    }

    protected static class Dual<T,U> extends Tuple {
        private final Supplier<T> _1;
        private final Supplier<U> _2;

        public Dual(T t, U u){
            super(t, u);
            _1 = () -> t;
            _2 = () -> u;
        }

        public T getFirst() {
            return _1.get();
        }

        public U getSecond() {
            return _2.get();
        }
    }

    protected static class Triple<T,U,V> extends Tuple {
        private final Supplier<T> _1;
        private final Supplier<U> _2;
        private final Supplier<V> _3;

        public Triple(T t, U u, V v){
            super(t, u, v);
            _1 = () -> t;
            _2 = () -> u;
            _3 = () -> v;
        }

        public T getFirst() {
            return _1.get();
        }

        public U getSecond() {
            return _2.get();
        }

        public V getThird() {
            return _3.get();
        }
    }

    protected static class Quad<T,U,V,W> extends Tuple {
        private final Supplier<T> _1;
        private final Supplier<U> _2;
        private final Supplier<V> _3;
        private final Supplier<W> _4;

        public Quad(T t, U u, V v, W w){
            super(t, u, v, w);
            _1 = () -> t;
            _2 = () -> u;
            _3 = () -> v;
            _4 = () -> w;
        }

        public T getFirst() {
            return _1.get();
        }

        public U getSecond() {
            return _2.get();
        }

        public V getThird() {
            return _3.get();
        }

        public W getFourth() {
            return _4.get();
        }
    }

    protected static class Penta<T,U,V,W,X> extends Tuple {
        private final Supplier<T> _1;
        private final Supplier<U> _2;
        private final Supplier<V> _3;
        private final Supplier<W> _4;
        private final Supplier<X> _5;

        public Penta(T t, U u, V v, W w, X x){
            super(t, u, v, w);
            _1 = () -> t;
            _2 = () -> u;
            _3 = () -> v;
            _4 = () -> w;
            _5 = () -> x;
        }

        public T getFirst() {
            return _1.get();
        }

        public U getSecond() {
            return _2.get();
        }

        public V getThird() {
            return _3.get();
        }

        public W getFourth() {
            return _4.get();
        }

        public X getFifth() {
            return _5.get();
        }
    }

    protected static class Hexa<T,U,V,W,X,Y> extends Tuple {
        private final Supplier<T> _1;
        private final Supplier<U> _2;
        private final Supplier<V> _3;
        private final Supplier<W> _4;
        private final Supplier<X> _5;
        private final Supplier<Y> _6;

        public Hexa(T t, U u, V v, W w, X x, Y y){
            super(t, u, v, w);
            _1 = () -> t;
            _2 = () -> u;
            _3 = () -> v;
            _4 = () -> w;
            _5 = () -> x;
            _6 = () -> y;
        }

        public T getFirst() {
            return _1.get();
        }

        public U getSecond() {
            return _2.get();
        }

        public V getThird() {
            return _3.get();
        }

        public W getFourth() {
            return _4.get();
        }

        public X getFifth() {
            return _5.get();
        }

        public Y getSixth() {
            return _6.get();
        }
    }

    protected static class Hepta<T,U,V,W,X,Y,Z> extends Tuple {
        private final Supplier<T> _1;
        private final Supplier<U> _2;
        private final Supplier<V> _3;
        private final Supplier<W> _4;
        private final Supplier<X> _5;
        private final Supplier<Y> _6;
        private final Supplier<Z> _7;

        public Hepta(T t, U u, V v, W w, X x, Y y, Z z){
            super(t, u, v, w);
            _1 = () -> t;
            _2 = () -> u;
            _3 = () -> v;
            _4 = () -> w;
            _5 = () -> x;
            _6 = () -> y;
            _7 = () -> z;
        }

        public T getFirst() {
            return _1.get();
        }

        public U getSecond() {
            return _2.get();
        }

        public V getThird() {
            return _3.get();
        }

        public W getFourth() {
            return _4.get();
        }

        public X getFifth() {
            return _5.get();
        }

        public Y getSixth() {
            return _6.get();
        }

        public Z getSeventh() {
            return _7.get();
        }
    }
    //endregion

    private final Object[] values;
    private final String[] classNames;
    private final int length;

    private Tuple(Object... arguments){
        length = arguments.length;
        values = new Object[length];
        classNames = new String[length];
        for (int i=0; i<length; i++){
            Object argument = arguments[i];
            values[i] = argument;
            classNames[i] = getClassName(argument);
        }
    }

    public int getLength() {
        return length;
    }

    @Override
    public int hashCode() {
        return 17 * length + Arrays.deepHashCode(values);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Tuple) || length != ((Tuple) obj).length)
            return false;
        if (obj == this)
            return true;

        return Arrays.deepEquals(values, ((Tuple) obj).values);
    }
}
