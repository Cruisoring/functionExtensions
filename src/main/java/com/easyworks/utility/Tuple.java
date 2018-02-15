package com.easyworks.utility;

import com.easyworks.Loggable;
import com.easyworks.NoThrows;
import com.easyworks.function.SupplierThrowable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class Tuple implements AutoCloseable {
    public static Loggable loggable;

    public static final Unit UNIT = new Unit();
    public static final Single TRUE = new Single(true);
    public static final Single FALSE = new Single(false);

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

    public static <T> Set<T> setOf(T... values) { return new Set<T>(values); }

    //endregion Factories to create Strong-typed Tuple instances based on the number of given arguments

    //region Extended Strong-typed classes

    public static class Set<T> extends Tuple{
        public final static Set EMPTY = new Set();
//        public final T[] group;
        public final Class<T> componentType;
        protected Set(T... values){
            super(values);
//            group = Arrays.copyOf(values, values.length);
            componentType = (Class<T>) values.getClass().getComponentType();
        }

        public T[] asArray(){
            return (T[])values;
        }

        public T get(int index){
            return (T) values[index];
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null || !(obj instanceof Set))
                return false;
            if (obj == this)
                return true;
            Set other = (Set)obj;
            if(!other.canEqual(this) || getLength() != ((Tuple) obj).getLength())
                return false;

            return super.equals(other);
        }

        @Override
        public boolean canEqual(Object other) {
            return (other instanceof Set) && ((Set) other).componentType == componentType;
        }

        @Override
        public <R> Set<R> getSetOf(Class<R> clazz){
            Objects.requireNonNull(clazz);
            if(clazz.equals(componentType) || clazz.isAssignableFrom(componentType))
                return (Set<R>) this;
            return EMPTY;
        }

        @Override
        public String toString() {
            return String.format("%s[%s]", componentType.getSimpleName(),
                    Arrays.stream(values)
                            .map(v -> v==null?"null":v.toString())
                            .collect(Collectors.joining(", ")));
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
        private final Supplier<T> _1;
        private final Supplier<U> _2;
        private final Supplier<V> _3;
        private final Supplier<W> _4;
        private final Supplier<X> _5;
        private final Supplier<Y> _6;

        protected Hexa(T t, U u, V v, W w, X x, Y y){
            super(t, u, v, w, x, y);
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
        private final Supplier<T> _1;
        private final Supplier<U> _2;
        private final Supplier<V> _3;
        private final Supplier<W> _4;
        private final Supplier<X> _5;
        private final Supplier<Y> _6;
        private final Supplier<Z> _7;

        protected Hepta(T t, U u, V v, W w, X x, Y y, Z z){
            super(t, u, v, w, x, y, z);
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

    protected final Object[] values;
    private final Class[] classes;

    private Tuple(Object... arguments){
        int length = arguments.length;
        values = new Object[length];
        classes = new Class[length];
        for (int i=0; i<length; i++){
            Object obj = arguments[i];
            values[i] = obj;
            classes[i] = obj == null ? null : obj.getClass();
        }
    }

    public <T> Set<T> getSetOf(Class<T> clazz){
        Objects.requireNonNull(clazz);
        List<T> matched = new ArrayList<>();

        SupplierThrowable.PredicateThrowable<Class> predicate = TypeHelper.getClassPredicate(clazz);

        int length = getLength();
        for (int i = 0; i < length; i++) {
            if(classes[i] == null) continue;
            if(predicate.testNoThrows(classes[i])){
                matched.add((T)values[i]);
            }
        };
        T[] array = (T[]) matched.stream().toArray();
        return setOf(array);
    }

    public int getLength(){
        return values.length;
    }

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
        Tuple other = (Tuple)obj;
        if(!other.canEqual(this))
            return false;

        return Arrays.deepEquals(values, other.values);
    }

    public boolean canEqual(Object obj) {
        return (obj instanceof Tuple);
    }

    @Override
    public String toString() {
        return String.format("[%s]", Arrays.stream(values)
                .map(v -> v==null?"null":v.toString()).collect(Collectors.joining(", ")));
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
                    Logger.L("%s closed()", value);
                }
            }
            closed = true;
            Logger.L("%s.close() run successfully!", this);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if(!closed)
        {
            Logger.L( "******FORGOT TO CLOSE THE TUPLE!" );
        }
        super.finalize();
    }
}
