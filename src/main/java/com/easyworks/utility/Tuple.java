package com.easyworks.utility;

import com.easyworks.Loggable;
import com.easyworks.NoThrows;

import java.util.Arrays;
import java.util.function.Supplier;

public abstract class Tuple implements AutoCloseable {
    public static Loggable loggable;

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

    public static <T,U,V,W,X,Y,Z> Hepta<T,U,V,W,X,Y,Z> create(T t, U u, V v, W w, X x, Y y, Z z){
        return new Hepta<>(t, u, v, w, x, y, z);
    }

    public static <T> Array<T> ofArray(T... values) { return new Array<>(values); }

    //endregion Factories to create Strong-typed Tuple instances based on the number of given arguments

    //region Extended Strong-typed classes

    protected static class Array<T> extends Tuple{
        private final T[] array;
        protected Array(T... values){
            super(values);
            array = Arrays.copyOf(values, values.length);
        }

        public T[] getArray(){
            return array;
        }

        @Override
        public int getLength() {
            return array.length;
        }
    }

    protected static class Unit extends Tuple {
        protected Unit(){
            super();
        }

        @Override
        public int getLength() {
            return 0;
        }
    }

    protected static class Single<T> extends Tuple {
        private final Supplier<T> _1;

        protected Single(T t){
            super(t);
            _1 = () -> t;
        }

        public T getFirst() {
            return _1.get();
        }

        @Override
        public int getLength() {
            return 1;
        }
    }

    protected static class Dual<T,U> extends Tuple {
//        protected static class Array<T> extends Dual<T,T>{
//            protected Array(T t1, T t2){
//                super(t1, t2);
//            }
//        }

        private final Supplier<T> _1;
        private final Supplier<U> _2;

        protected Dual(T t, U u){
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

        @Override
        public int getLength() {
            return 2;
        }
    }

    protected static class Triple<T,U,V> extends Tuple {
//        protected static class Array<T> extends Triple<T,T,T>{
//            protected Array(T t1, T t2, T t3){
//                super(t1, t2, t3);
//            }
//        }

        private final Supplier<T> _1;
        private final Supplier<U> _2;
        private final Supplier<V> _3;

        protected Triple(T t, U u, V v){
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

        @Override
        public int getLength() {
            return 3;
        }
    }

    protected static class Quad<T,U,V,W> extends Tuple {
//        protected static class Array<T> extends Quad<T,T,T,T>{
//            protected Array(T t1, T t2, T t3, T t4){
//                super(t1, t2, t3, t4);
//            }
//        }

        private final Supplier<T> _1;
        private final Supplier<U> _2;
        private final Supplier<V> _3;
        private final Supplier<W> _4;

        protected Quad(T t, U u, V v, W w){
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

        @Override
        public int getLength() {
            return 4;
        }
    }

    protected static class Penta<T,U,V,W,X> extends Tuple {
//        protected static class Array<T> extends Penta<T,T,T,T,T>{
//            protected Array(T t1, T t2, T t3, T t4, T t5){
//                super(t1, t2, t3, t4, t5);
//            }
//        }

        private final Supplier<T> _1;
        private final Supplier<U> _2;
        private final Supplier<V> _3;
        private final Supplier<W> _4;
        private final Supplier<X> _5;

        protected Penta(T t, U u, V v, W w, X x){
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

        @Override
        public int getLength() {
            return 5;
        }
    }

    protected static class Hexa<T,U,V,W,X,Y> extends Tuple {
//        protected static class Array<T> extends Hexa<T,T,T,T,T,T>{
//            protected Array(T t1, T t2, T t3, T t4, T t5, T t6){
//                super(t1, t2, t3, t4, t5, t6);
//            }
//        }

        private final Supplier<T> _1;
        private final Supplier<U> _2;
        private final Supplier<V> _3;
        private final Supplier<W> _4;
        private final Supplier<X> _5;
        private final Supplier<Y> _6;

        protected Hexa(T t, U u, V v, W w, X x, Y y){
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

        @Override
        public int getLength() {
            return 6;
        }
    }

    protected static class Hepta<T,U,V,W,X,Y,Z> extends Tuple {
//        protected static class Array<T> extends Hepta<T,T,T,T,T,T,T>{
//            protected Array(T t1, T t2, T t3, T t4, T t5, T t6, T t7){
//                super(t1, t2, t3, t4, t5, t6, t7);
//            }
//        }

        private final Supplier<T> _1;
        private final Supplier<U> _2;
        private final Supplier<V> _3;
        private final Supplier<W> _4;
        private final Supplier<X> _5;
        private final Supplier<Y> _6;
        private final Supplier<Z> _7;

        protected Hepta(T t, U u, V v, W w, X x, Y y, Z z){
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

        @Override
        public int getLength() {
            return 7;
        }
    }
    //endregion

    private final Object[] values;
    private final String[] classNames;

    private Tuple(Object... arguments){
        int length = arguments.length;
        values = new Object[length];
        classNames = new String[length];
        for (int i=0; i<length; i++){
            Object argument = arguments[i];
            values[i] = argument;
            classNames[i] = getClassName(argument);
        }
    }

//    public <T> Array<T> getArray(Class<T> clazz){
//        String class
//    }

    public abstract int getLength();

    @Override
    public int hashCode() {
        return 17 * Arrays.deepHashCode(values) + getLength();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Tuple) || getLength() != ((Tuple) obj).getLength())
            return false;
        if (obj == this)
            return true;

        return Arrays.deepEquals(values, ((Tuple) obj).values);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    private boolean closed = false;

    @Override
    public void close() throws Exception {
        if(!closed) {
            //Close AutoCloseable object in reverse order
            for (int i = getLength() - 1; i >= 0; i--) {
                Object value = values[i];
                if (value != null && value instanceof AutoCloseable) {
                    NoThrows.run(() -> ((AutoCloseable) value).close());
                }
            }
            closed = true;
            System.out.println("close() run successfully!");
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if(!closed && loggable != null)
        {
            loggable.log( "FORGOT TO CLOSE THIS!" );
        }
        super.finalize();
    }
}
