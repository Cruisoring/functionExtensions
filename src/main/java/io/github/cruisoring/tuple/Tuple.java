package io.github.cruisoring.tuple;

import io.github.cruisoring.Functions;
import io.github.cruisoring.TypeHelper;
import io.github.cruisoring.utility.ArrayHelper;
import io.github.cruisoring.utility.Logger;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Predicate;

/**
 * This is a special data structure contains multiple immutable elements in fixed sequence. The AutoCloseable implementation
 * would close any elements if they are also AutoCloseable.
 */
public class Tuple<T extends Object> implements AutoCloseable, Comparable<Tuple>, WithValues {

    public static final Tuple0 UNIT = new Tuple0();
    public static final Tuple1 TRUE = new Tuple1(true);
    public static final Tuple1 FALSE = new Tuple1(false);

    protected final Class<? extends T> _elementType;
    protected final T[] values;
    protected Integer _hashCode;
    protected int[][] deepLength;
    protected String _toString;

    /**
     * Protected constructor to keep the elements as a final array.
     * @param eType  type of the elements being specified to cope with <tt>Type Erasure</tt>
     * @param elements values to be persisted by the Tuple.
     */
    protected Tuple(Class<? extends T> eType, final T... elements){
        this._elementType = eType;
        int length = elements == null ? 1 : elements.length;
        values = (T[]) ArrayHelper.getNewArray(eType, length);

        if(elements == null) {
            values[0] = null;
        }else {
            for (int i = 0; i < length; i++) {
                values[i] = elements[i];
            }
        }
    }

    /**
     * Protected constructor to keep the elements as a final array.
     * @param elements values to be persisted by the Tuple.
     */
    protected Tuple(final T... elements){
        this._elementType = (Class<? extends T>) ArrayHelper.getComponentType(elements);
        int length = elements == null ? 1 : elements.length;
        values = (T[]) ArrayHelper.getNewArray(_elementType, length);

        if(elements == null) {
            values[0] = null;
        }else {
            for (int i = 0; i < length; i++) {
                values[i] = elements[i];
            }
        }
    }

    @Override
    public Object getValueAt(int index) {
        if(index < 0 || index >= values.length)
            return null;
        return values[index];
    }

    /**
     * Get new Array of values to prevent changes on the underlying array.
     * @return copied Array of the persistent values
     */
    public T[] asArray(){
        return Arrays.copyOf((T[]) values, values.length);
    }

    /**
     * Get the elements at specific position
     * @param index     Index of the expected element
     * @return          Element at that index
     * @throws IndexOutOfBoundsException    When invalid index is provided
     */
    public T get(int index) throws IndexOutOfBoundsException{
        if(index < 0 || index >= getLength())
            throw new IndexOutOfBoundsException();
        return (T) values[index];
    }


    /**
     * Get all Non-null elements matching the given class as an immutable <tt>Tuple</tt>
     * @param clazz Class to evaluate the saved values.
     * @param <U>   Type of the given Class.
     * @return      Immutable <code>Tuple</code> containing matched elements.
     */
    public <U> Tuple<U> getSetOf(Class<U> clazz){
        Objects.requireNonNull(clazz);

        try {
            Predicate<Class> predicate = TypeHelper.getClassEqualitor(clazz);
            Class equivalent = (TypeHelper.isPrimitive(clazz) && !clazz.isArray()) ? TypeHelper.getEquivalentClass(clazz) : clazz;
            Boolean isArray = clazz.isArray();
            Class<?> componentType = isArray ? clazz.getComponentType() : null;

            int length = getLength();
            List<U> list = new ArrayList<U>();
            for (int i = 0; i < length; i++) {
                Object v = values[i];
                if (v != null) {
                    Class vClass = v.getClass();
                    if(equivalent.equals(vClass)){
                        list.add((U)v);
                    } else if (predicate.test(vClass)) {
                        U t = (U) TypeHelper.getToEquivalentParallelConverter(vClass).apply(v);
                        list.add(t);
                    }
                } else if(!TypeHelper.isPrimitive(clazz)) {
                    list.add(null);
                }
            }
            Object[] array = list.toArray((U[])ArrayHelper.getNewArray(equivalent, list.size()));
            return setOf(equivalent, (U[])array);
        }catch (Exception ex){
            return null;
        }
    }

    /**
     * Get all Non-null elements of the given class and matched with predefined criteria as an immutable <tt>Tuple</tt>
     * @param clazz Class to evaluate the saved values.
     * @param valuePredicate predicate to filter elements by their values
     * @param <S>   Type of the given Class.
     * @return      Immutable <code>Tuple</code> containing matched elements.
     */
    public <S> Tuple<S> getSetOf(Class<S> clazz, Predicate<S> valuePredicate){
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(valuePredicate);
        List<S> matched = new ArrayList<>();

        Predicate<Class> classPredicate = TypeHelper.getClassEqualitor(clazz);

        int length = getLength();
        for (int i = 0; i < length; i++) {
            Object v = values[i];
            if(v != null){
                try {
                    if(classPredicate.test(v.getClass()) && valuePredicate.test((S)v))
                        matched.add((S)v);
                }catch (Exception ex){}
            }
        };
        S[] array = (S[]) matched.stream().toArray();
        return setOf(clazz, array);
    }

    /**
     * Get the length of the saved values.
     * @return length of the values.
     */
    public int getLength(){
        return values.length;
    }

    public int[][] getDeepLength() {
        if(deepLength == null){
            deepLength = TypeHelper.getDeepLength(values);
        }
        return deepLength;
    }

    @Override
    public int compareTo(Tuple o) {
        return o==null ? hashCode() : hashCode() - o.hashCode();
    }

    @Override
    public int hashCode() {
        if(_hashCode == null){
            _hashCode = TypeHelper.deepHashCode(values);
        }
        return _hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null || !(obj instanceof Tuple)){
            return false;
        } else if(obj == this){
            return true;
        }

        Tuple other = (Tuple)obj;
        if(!other.canEqual(this) || other.getLength() != values.length){
            return false;
        }

        return TypeHelper.valueEquals(values, other.values, getDeepLength(), other.getDeepLength());
    }

    public boolean canEqual(Object obj) {
        if(! (obj instanceof  Tuple))
            return false;
        Tuple other = (Tuple)obj;
        return this._elementType.isAssignableFrom(other._elementType) || other._elementType.isAssignableFrom(this._elementType);
    }

    @Override
    public String toString() {
        if(_toString==null){
            _toString = TypeHelper.deepToString(values);
        }
        return _toString;
    }

    private boolean closed = false;

    /**
     * Check the flag <code>closed</code> first, then close the saved values if they are AutoCloseable in reverse order.
     * @throws Exception any exception that could be thrown when closing AutoCloseable objects
     */
    @Override
    public void close() throws Exception {
        if(!closed) {
            //Close AutoCloseable object in reverse order
            for (int i = values.length - 1; i >= 0; i--) {
                Object value = values[i];
                if (value != null && value instanceof AutoCloseable) {
                    Functions.Default.run(() -> ((AutoCloseable) value).close());
                    Logger.V("%s closed()", value);
                }
            }
            closed = true;
            Logger.V("%s.close() run successfully!", this);
        }
    }

    //region Factories to create Strong-typed Tuple instances based on the number of given arguments

    /**
     * Create a Tuple instance to keep any number of elements without caring about their Type info.
     * @param elements  All elements to be persisted by the Tuple
     * @return          A <tt>Tuple</tt> instance with length of the elements
     */
    public static Tuple of(final Object... elements){
        if (elements == null){
            return create(null);
        }
        int length = elements.length;
        switch (length){
            case 0: return UNIT;
            case 1: return new Tuple1(elements[0]);
            case 2: return new Tuple2(elements[0], elements[1]);
            case 3: return new Tuple3(elements[0], elements[1], elements[2]);
            case 4: return new Tuple4(elements[0], elements[1], elements[2], elements[3]);
            case 5: return new Tuple5(elements[0], elements[1], elements[2], elements[3], elements[4]);
            case 6: return new Tuple6(elements[0], elements[1], elements[2], elements[3], elements[4], elements[5]);
            case 7: return new Tuple7(elements[0], elements[1], elements[2], elements[3], elements[4],
                    elements[5], elements[6]);
            case 8: return new Tuple8(elements[0], elements[1], elements[2], elements[3], elements[4],
                    elements[5], elements[6], elements[7]);
            case 9: return new Tuple9(elements[0], elements[1], elements[2], elements[3], elements[4],
                    elements[5], elements[6], elements[7], elements[8]);
            case 10: return new Tuple10(elements[0], elements[1], elements[2], elements[3], elements[4],
                    elements[5], elements[6], elements[7], elements[8], elements[9]);
            case 11: return new Tuple11(elements[0], elements[1], elements[2], elements[3], elements[4],
                    elements[5], elements[6], elements[7], elements[8], elements[9],
                    elements[10]);
            case 12: return new Tuple12(elements[0], elements[1], elements[2], elements[3], elements[4],
                    elements[5], elements[6], elements[7], elements[8], elements[9],
                    elements[10], elements[11]);
            case 13: return new Tuple13(elements[0], elements[1], elements[2], elements[3], elements[4],
                    elements[5], elements[6], elements[7], elements[8], elements[9],
                    elements[10], elements[11], elements[12]);
            case 14: return new Tuple14(elements[0], elements[1], elements[2], elements[3], elements[4],
                    elements[5], elements[6], elements[7], elements[8], elements[9],
                    elements[10], elements[11], elements[12], elements[13]);
            case 15: return new Tuple15(elements[0], elements[1], elements[2], elements[3], elements[4],
                    elements[5], elements[6], elements[7], elements[8], elements[9],
                    elements[10], elements[11], elements[12], elements[13], elements[14]);
            case 16: return new Tuple16(elements[0], elements[1], elements[2], elements[3], elements[4],
                    elements[5], elements[6], elements[7], elements[8], elements[9],
                    elements[10], elements[11], elements[12], elements[13], elements[14],
                    elements[15]);
            case 17: return new Tuple17(elements[0], elements[1], elements[2], elements[3], elements[4],
                    elements[5], elements[6], elements[7], elements[8], elements[9],
                    elements[10], elements[11], elements[12], elements[13], elements[14],
                    elements[15], elements[16]);
            case 18: return new Tuple18(elements[0], elements[1], elements[2], elements[3], elements[4],
                    elements[5], elements[6], elements[7], elements[8], elements[9],
                    elements[10], elements[11], elements[12], elements[13], elements[14],
                    elements[15], elements[16], elements[17]);
            case 19: return new Tuple19(elements[0], elements[1], elements[2], elements[3], elements[4],
                    elements[5], elements[6], elements[7], elements[8], elements[9],
                    elements[10], elements[11], elements[12], elements[13], elements[14],
                    elements[15], elements[16], elements[17], elements[18]);
            case 20: return new Tuple20(elements[0], elements[1], elements[2], elements[3], elements[4],
                    elements[5], elements[6], elements[7], elements[8], elements[9],
                    elements[10], elements[11], elements[12], elements[13], elements[14],
                    elements[15], elements[16], elements[17], elements[18], elements[19]);

            default: return new TuplePlus(elements[0], elements[1], elements[2], elements[3], elements[4],
                    elements[5], elements[6], elements[7], elements[8], elements[9],
                    elements[10], elements[11], elements[12], elements[13], elements[14],
                    elements[15], elements[16], elements[17], elements[18], elements[19], Arrays.copyOfRange(elements, 20, length));
        }
    }

    /**
     * Crate an Tuple0 that has no value saved, shall not be called directly.
     * @return  The Tuple0 object.
     */
    protected static Tuple0 create(){
        return UNIT;
    }

    /**
     * Create a Tuple1 instance that contains only one value of Type T
     * @param t     Element to be persisted by the Tuple
     * @param <T>   Type of the element <code>t</code>
     * @return      Tuple containing 6 elements that could be accessed as their original types.
     */
    public static <T> Tuple1<T> create(final T t){
        return new Tuple1(t);
    }

    /**
     * Create a Tuple2 instance that contains 2 elements of Types T/U respectively
     * @param t     First element to be persisted by the Tuple
     * @param u     Second element to be persisted by the Tuple
     * @param <T>   Type of the first element <code>t</code>
     * @param <U>   Type of the second element <code>u</code>
     * @return      Tuple containing 2 elements that could be accessed as their original types.
     */
    public static <T,U> Tuple2<T,U> create(final T t, final U u){
        return new Tuple2(t, u);
    }

    /**
     * Create a Tuple3 instance that contains 3 elements of Types T/U/V respectively
     * @param t     First element to be persisted by the Tuple
     * @param u     Second element to be persisted by the Tuple
     * @param v     Third element to be persisted by the Tuple
     * @param <T>   Type of the first element <code>t</code>
     * @param <U>   Type of the second element <code>u</code>
     * @param <V>   Type of the third element <code>v</code>
     * @return      Tuple containing 3 elements that could be accessed as their original types.
     */
    public static <T,U,V> Tuple3<T,U,V> create(final T t, final U u, final V v){
        return new Tuple3(t, u, v);
    }

    /**
     * Create a Tuple4 instance that contains 4 elements of Types T/U/V/W respectively
     * @param t     First element to be persisted by the Tuple
     * @param u     Second element to be persisted by the Tuple
     * @param v     Third element to be persisted by the Tuple
     * @param w     Fourth element to be persisted by the Tuple
     * @param <T>   Type of the first element <code>t</code>
     * @param <U>   Type of the second element <code>u</code>
     * @param <V>   Type of the third element <code>v</code>
     * @param <W>   Type of the fourth element <code>w</code>
     * @return      Tuple containing 4 elements that could be accessed as their original types.
     */
    public static <T,U,V,W> Tuple4<T,U,V,W> create(final T t, final U u, final V v, final W w){
        return new Tuple4(t, u, v, w);
    }

    /**
     * Create a Tuple5 instance that contains 5 elements of Types T/U/V/W/X respectively
     * @param t     First element to be persisted by the Tuple
     * @param u     Second element to be persisted by the Tuple
     * @param v     Third element to be persisted by the Tuple
     * @param w     Fourth element to be persisted by the Tuple
     * @param x     Fifth element to be persisted by the Tuple
     * @param <T>   Type of the first element <code>t</code>
     * @param <U>   Type of the second element <code>u</code>
     * @param <V>   Type of the third element <code>v</code>
     * @param <W>   Type of the fourth element <code>w</code>
     * @param <X>   Type of the fifth element <code>x</code>
     * @return      Tuple containing 5 elements that could be accessed as their original types.
     */
    public static <T,U,V,W,X> Tuple5<T,U,V,W,X> create(final T t, final U u, final V v, final W w, final X x){
        return new Tuple5(t, u, v, w, x);
    }

    /**
     * Create a Tuple6 instance that contains 6 elements of Types T/U/V/W/X/Y respectively
     * @param t     First element to be persisted by the Tuple
     * @param u     Second element to be persisted by the Tuple
     * @param v     Third element to be persisted by the Tuple
     * @param w     Fourth element to be persisted by the Tuple
     * @param x     Fifth element to be persisted by the Tuple
     * @param y     Sixth element to be persisted by the Tuple
     * @param <T>   Type of the first element <code>t</code>
     * @param <U>   Type of the second element <code>u</code>
     * @param <V>   Type of the third element <code>v</code>
     * @param <W>   Type of the fourth element <code>w</code>
     * @param <X>   Type of the fifth element <code>x</code>
     * @param <Y>   Type of the sixth element <code>y</code>
     * @return      Tuple containing 6 elements that could be accessed as their original types.
     */
    public static <T,U,V,W,X,Y> Tuple6<T,U,V,W,X,Y> create(
            final T t, final U u, final V v, final W w, final X x, final Y y){
        return new Tuple6(t, u, v, w, x, y);
    }

    /**
     * Create a Tuple7 instance that contains 7 elements of Types T/U/V/W/X/Y/Z respectively
     * @param t     First element to be persisted by the Tuple
     * @param u     Second element to be persisted by the Tuple
     * @param v     Third element to be persisted by the Tuple
     * @param w     Fourth element to be persisted by the Tuple
     * @param x     Fifth element to be persisted by the Tuple
     * @param y     Sixth element to be persisted by the Tuple
     * @param z     Seventh element to be persisted by the Tuple
     * @param <T>   Type of the first element <code>t</code>
     * @param <U>   Type of the second element <code>u</code>
     * @param <V>   Type of the third element <code>v</code>
     * @param <W>   Type of the fourth element <code>w</code>
     * @param <X>   Type of the fifth element <code>x</code>
     * @param <Y>   Type of the sixth element <code>y</code>
     * @param <Z>   Type of the seventh element <code>z</code>
     * @return      Tuple containing seven elements that could be accessed as their original types.
     */
    public static <T,U,V,W,X,Y,Z> Tuple7<T,U,V,W,X,Y,Z> create(
            final T t, final U u, final V v, final W w, final X x, final Y y, final Z z){
        return new Tuple7(t, u, v, w, x, y, z);
    }

    /**
     * Create a Tuple8 instance that contains 8 elements of 8 types respectively
     * @param t     First element to be persisted by the Tuple
     * @param u     Second element to be persisted by the Tuple
     * @param v     Third element to be persisted by the Tuple
     * @param w     Fourth element to be persisted by the Tuple
     * @param x     Fifth element to be persisted by the Tuple
     * @param y     Sixth element to be persisted by the Tuple
     * @param z     Seventh element to be persisted by the Tuple
     * @param a     Eighth element to be persisted by the Tuple
     * @param <T>   Type of the first element <code>t</code>
     * @param <U>   Type of the second element <code>u</code>
     * @param <V>   Type of the third element <code>v</code>
     * @param <W>   Type of the fourth element <code>w</code>
     * @param <X>   Type of the fifth element <code>x</code>
     * @param <Y>   Type of the sixth element <code>y</code>
     * @param <Z>   Type of the seventh element <code>z</code>
     * @param <A>   Type of the 8th element <code>a</code>
     * @return      Tuple containing 8 elements that could be retrieved as their original types.
     */
    public static <T,U,V,W,X,Y,Z,A> Tuple8<T,U,V,W,X,Y,Z,A> create(
            final T t, final U u, final V v, final W w, final X x, final Y y, final Z z, final A a){
        return new Tuple8(t, u, v, w, x, y, z, a);
    }

    /**
     * Create a Tuple9 instance that contains 9 elements of 9 types respectively
     * @param t     First element to be persisted by the Tuple
     * @param u     Second element to be persisted by the Tuple
     * @param v     Third element to be persisted by the Tuple
     * @param w     Fourth element to be persisted by the Tuple
     * @param x     Fifth element to be persisted by the Tuple
     * @param y     Sixth element to be persisted by the Tuple
     * @param z     Seventh element to be persisted by the Tuple
     * @param a     Eighth element to be persisted by the Tuple
     * @param b     Ninth element to be persisted by the Tuple
     * @param <T>   Type of the first element <code>t</code>
     * @param <U>   Type of the second element <code>u</code>
     * @param <V>   Type of the third element <code>v</code>
     * @param <W>   Type of the fourth element <code>w</code>
     * @param <X>   Type of the fifth element <code>x</code>
     * @param <Y>   Type of the sixth element <code>y</code>
     * @param <Z>   Type of the seventh element <code>z</code>
     * @param <A>   Type of the 8th element <code>a</code>
     * @param <B>   Type of the 9th element <code>b</code>
     * @return      Tuple containing 9 elements that could be retrieved as their original types.
     */
    public static <T,U,V,W,X,Y,Z,A,B> Tuple9<T,U,V,W,X,Y,Z,A,B> create(
            final T t, final U u, final V v, final W w, final X x, final Y y, final Z z, final A a, final B b){
        return new Tuple9(t, u, v, w, x, y, z, a, b);
    }
    
    /**
     * Create a Tuple10 instance that contains 10 elements of 10 types respectively
     * @param t     First element to be persisted by the Tuple
     * @param u     Second element to be persisted by the Tuple
     * @param v     Third element to be persisted by the Tuple
     * @param w     Fourth element to be persisted by the Tuple
     * @param x     Fifth element to be persisted by the Tuple
     * @param y     Sixth element to be persisted by the Tuple
     * @param z     Seventh element to be persisted by the Tuple
     * @param a     Eighth element to be persisted by the Tuple
     * @param b     Ninth element to be persisted by the Tuple
     * @param c     Tenth element to be persisted by the Tuple
     * @param <T>   Type of the first element <code>t</code>
     * @param <U>   Type of the second element <code>u</code>
     * @param <V>   Type of the third element <code>v</code>
     * @param <W>   Type of the fourth element <code>w</code>
     * @param <X>   Type of the fifth element <code>x</code>
     * @param <Y>   Type of the sixth element <code>y</code>
     * @param <Z>   Type of the seventh element <code>z</code>
     * @param <A>   Type of the 8th element <code>a</code>
     * @param <B>   Type of the 9th element <code>b</code>
     * @param <C>   Type of the 10th element <code>c</code>
     * @return      Tuple containing 10 elements that could be retrieved as their original types.
     */
    public static <T,U,V,W,X,Y,Z,A,B,C> Tuple10<T,U,V,W,X,Y,Z,A,B,C> create(
            final T t, final U u, final V v, final W w, final X x, final Y y, final Z z, final A a, final B b,
            final C c){
        return new Tuple10(t, u, v, w, x, y, z, a, b, c);
    }
    
    /**
     * Create a Tuple11 instance that contains 11 elements of 11 types respectively
     * @param t     First element to be persisted by the Tuple
     * @param u     Second element to be persisted by the Tuple
     * @param v     Third element to be persisted by the Tuple
     * @param w     Fourth element to be persisted by the Tuple
     * @param x     Fifth element to be persisted by the Tuple
     * @param y     Sixth element to be persisted by the Tuple
     * @param z     Seventh element to be persisted by the Tuple
     * @param a     Eighth element to be persisted by the Tuple
     * @param b     Ninth element to be persisted by the Tuple
     * @param c     Tenth element to be persisted by the Tuple
     * @param d     Eleventh element to be persisted by the Tuple
     * @param <T>   Type of the first element <code>t</code>
     * @param <U>   Type of the second element <code>u</code>
     * @param <V>   Type of the third element <code>v</code>
     * @param <W>   Type of the fourth element <code>w</code>
     * @param <X>   Type of the fifth element <code>x</code>
     * @param <Y>   Type of the sixth element <code>y</code>
     * @param <Z>   Type of the seventh element <code>z</code>
     * @param <A>   Type of the 8th element <code>a</code>
     * @param <B>   Type of the 9th element <code>b</code>
     * @param <C>   Type of the 10th element <code>c</code>
     * @param <D>   Type of the 11th element <code>d</code>
     * @return      Tuple containing 11 elements that could be retrieved as their original types.
     */
    public static <T,U,V,W,X,Y,Z,A,B,C,D> Tuple11<T,U,V,W,X,Y,Z,A,B,C,D> create(
            final T t, final U u, final V v, final W w, final X x, final Y y, final Z z, final A a, final B b,
            final C c, final D d){
        return new Tuple11(t, u, v, w, x, y, z, a, b, c, d);
    }

    /**
     * Create a Tuple12 instance that contains 12 elements of 12 types respectively
     * @param t     First element to be persisted by the Tuple
     * @param u     Second element to be persisted by the Tuple
     * @param v     Third element to be persisted by the Tuple
     * @param w     Fourth element to be persisted by the Tuple
     * @param x     Fifth element to be persisted by the Tuple
     * @param y     Sixth element to be persisted by the Tuple
     * @param z     Seventh element to be persisted by the Tuple
     * @param a     Eighth element to be persisted by the Tuple
     * @param b     Ninth element to be persisted by the Tuple
     * @param c     Tenth element to be persisted by the Tuple
     * @param d     Eleventh element to be persisted by the Tuple
     * @param e     Twelfth element to be persisted by the Tuple
     * @param <T>   Type of the first element <code>t</code>
     * @param <U>   Type of the second element <code>u</code>
     * @param <V>   Type of the third element <code>v</code>
     * @param <W>   Type of the fourth element <code>w</code>
     * @param <X>   Type of the fifth element <code>x</code>
     * @param <Y>   Type of the sixth element <code>y</code>
     * @param <Z>   Type of the seventh element <code>z</code>
     * @param <A>   Type of the 8th element <code>a</code>
     * @param <B>   Type of the 9th element <code>b</code>
     * @param <C>   Type of the 10th element <code>c</code>
     * @param <D>   Type of the 11th element <code>d</code>
     * @param <E>   Type of the 12th element <code>e</code>
     * @return      Tuple containing 12 elements that could be retrieved as their original types.
     */
    public static <T,U,V,W,X,Y,Z,A,B,C,D,E> Tuple12<T,U,V,W,X,Y,Z,A,B,C,D,E> create(
            final T t, final U u, final V v, final W w, final X x, final Y y, final Z z, final A a, final B b,
            final C c, final D d, final E e){
        return new Tuple12(t, u, v, w, x, y, z, a, b, c, d, e);
    }

    /**
     * Create a Tuple13 instance that contains 13 elements of 13 types respectively
     * @param t     First element to be persisted by the Tuple
     * @param u     Second element to be persisted by the Tuple
     * @param v     Third element to be persisted by the Tuple
     * @param w     Fourth element to be persisted by the Tuple
     * @param x     Fifth element to be persisted by the Tuple
     * @param y     Sixth element to be persisted by the Tuple
     * @param z     Seventh element to be persisted by the Tuple
     * @param a     Eighth element to be persisted by the Tuple
     * @param b     Ninth element to be persisted by the Tuple
     * @param c     Tenth element to be persisted by the Tuple
     * @param d     Eleventh element to be persisted by the Tuple
     * @param e     Twelfth element to be persisted by the Tuple
     * @param f     Thirteenth element to be persisted by the Tuple
     * @param <T>   Type of the first element <code>t</code>
     * @param <U>   Type of the second element <code>u</code>
     * @param <V>   Type of the third element <code>v</code>
     * @param <W>   Type of the fourth element <code>w</code>
     * @param <X>   Type of the fifth element <code>x</code>
     * @param <Y>   Type of the sixth element <code>y</code>
     * @param <Z>   Type of the seventh element <code>z</code>
     * @param <A>   Type of the 8th element <code>a</code>
     * @param <B>   Type of the 9th element <code>b</code>
     * @param <C>   Type of the 10th element <code>c</code>
     * @param <D>   Type of the 11th element <code>d</code>
     * @param <E>   Type of the 12th element <code>e</code>
     * @param <F>   Type of the 13th element <code>f</code>
     * @return      Tuple containing 13 elements that could be retrieved as their original types.
     */
    public static <T,U,V,W,X,Y,Z,A,B,C,D,E,F> Tuple13<T,U,V,W,X,Y,Z,A,B,C,D,E,F> create(
            final T t, final U u, final V v, final W w, final X x, final Y y, final Z z, final A a, final B b,
            final C c, final D d, final E e, final F f){
        return new Tuple13(t, u, v, w, x, y, z, a, b, c, d, e, f);
    }
    
    /**
     * Create a Tuple14 instance that contains 14 elements of 14 types respectively
     * @param t     First element to be persisted by the Tuple
     * @param u     Second element to be persisted by the Tuple
     * @param v     Third element to be persisted by the Tuple
     * @param w     Fourth element to be persisted by the Tuple
     * @param x     Fifth element to be persisted by the Tuple
     * @param y     Sixth element to be persisted by the Tuple
     * @param z     Seventh element to be persisted by the Tuple
     * @param a     Eighth element to be persisted by the Tuple
     * @param b     Ninth element to be persisted by the Tuple
     * @param c     Tenth element to be persisted by the Tuple
     * @param d     Eleventh element to be persisted by the Tuple
     * @param e     Twelfth element to be persisted by the Tuple
     * @param f     Thirteenth element to be persisted by the Tuple
     * @param g     Fourteenth element to be persisted by the Tuple
     * @param <T>   Type of the first element <code>t</code>
     * @param <U>   Type of the second element <code>u</code>
     * @param <V>   Type of the third element <code>v</code>
     * @param <W>   Type of the fourth element <code>w</code>
     * @param <X>   Type of the fifth element <code>x</code>
     * @param <Y>   Type of the sixth element <code>y</code>
     * @param <Z>   Type of the seventh element <code>z</code>
     * @param <A>   Type of the 8th element <code>a</code>
     * @param <B>   Type of the 9th element <code>b</code>
     * @param <C>   Type of the 10th element <code>c</code>
     * @param <D>   Type of the 11th element <code>d</code>
     * @param <E>   Type of the 12th element <code>e</code>
     * @param <F>   Type of the 13th element <code>f</code>
     * @param <G>   Type of the 14th element <code>g</code>
     * @return      Tuple containing 14 elements that could be retrieved as their original types.
     */
    public static <T,U,V,W,X,Y,Z,A,B,C,D,E,F,G> Tuple14<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G> create(
            final T t, final U u, final V v, final W w, final X x, final Y y, final Z z, final A a, final B b,
            final C c, final D d, final E e, final F f, final G g){
        return new Tuple14(t, u, v, w, x, y, z, a, b, c, d, e, f, g);
    }

    /**
     * Create a Tuple15 instance that contains 15 elements of 15 types respectively
     * @param t     First element to be persisted by the Tuple
     * @param u     Second element to be persisted by the Tuple
     * @param v     Third element to be persisted by the Tuple
     * @param w     Fourth element to be persisted by the Tuple
     * @param x     Fifth element to be persisted by the Tuple
     * @param y     Sixth element to be persisted by the Tuple
     * @param z     Seventh element to be persisted by the Tuple
     * @param a     Eighth element to be persisted by the Tuple
     * @param b     Ninth element to be persisted by the Tuple
     * @param c     Tenth element to be persisted by the Tuple
     * @param d     Eleventh element to be persisted by the Tuple
     * @param e     Twelfth element to be persisted by the Tuple
     * @param f     Thirteenth element to be persisted by the Tuple
     * @param g     Fourteenth element to be persisted by the Tuple
     * @param h     Fifteenth element to be persisted by the Tuple
     * @param <T>   Type of the first element <code>t</code>
     * @param <U>   Type of the second element <code>u</code>
     * @param <V>   Type of the third element <code>v</code>
     * @param <W>   Type of the fourth element <code>w</code>
     * @param <X>   Type of the fifth element <code>x</code>
     * @param <Y>   Type of the sixth element <code>y</code>
     * @param <Z>   Type of the seventh element <code>z</code>
     * @param <A>   Type of the 8th element <code>a</code>
     * @param <B>   Type of the 9th element <code>b</code>
     * @param <C>   Type of the 10th element <code>c</code>
     * @param <D>   Type of the 11th element <code>d</code>
     * @param <E>   Type of the 12th element <code>e</code>
     * @param <F>   Type of the 13th element <code>f</code>
     * @param <G>   Type of the 14th element <code>g</code>
     * @param <H>   Type of the 15th element <code>h</code>
     * @return      Tuple containing 15 elements that could be retrieved as their original types.
     */
    public static <T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H> Tuple15<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H> create(
            final T t, final U u, final V v, final W w, final X x, final Y y, final Z z, final A a, final B b,
            final C c, final D d, final E e, final F f, final G g, final H h){
        return new Tuple15(t, u, v, w, x, y, z, a, b, c, d, e, f, g, h);
    }


    /**
     * Create a Tuple16 instance that contains 16 elements of 16 types respectively
     * @param t     First element to be persisted by the Tuple
     * @param u     Second element to be persisted by the Tuple
     * @param v     Third element to be persisted by the Tuple
     * @param w     Fourth element to be persisted by the Tuple
     * @param x     Fifth element to be persisted by the Tuple
     * @param y     Sixth element to be persisted by the Tuple
     * @param z     Seventh element to be persisted by the Tuple
     * @param a     Eighth element to be persisted by the Tuple
     * @param b     Ninth element to be persisted by the Tuple
     * @param c     Tenth element to be persisted by the Tuple
     * @param d     Eleventh element to be persisted by the Tuple
     * @param e     Twelfth element to be persisted by the Tuple
     * @param f     Thirteenth element to be persisted by the Tuple
     * @param g     Fourteenth element to be persisted by the Tuple
     * @param h     Fifteenth element to be persisted by the Tuple
     * @param i     Sixteenth element to be persisted by the Tuple
     * @param <T>   Type of the first element <code>t</code>
     * @param <U>   Type of the second element <code>u</code>
     * @param <V>   Type of the third element <code>v</code>
     * @param <W>   Type of the fourth element <code>w</code>
     * @param <X>   Type of the fifth element <code>x</code>
     * @param <Y>   Type of the sixth element <code>y</code>
     * @param <Z>   Type of the seventh element <code>z</code>
     * @param <A>   Type of the 8th element <code>a</code>
     * @param <B>   Type of the 9th element <code>b</code>
     * @param <C>   Type of the 10th element <code>c</code>
     * @param <D>   Type of the 11th element <code>d</code>
     * @param <E>   Type of the 12th element <code>e</code>
     * @param <F>   Type of the 13th element <code>f</code>
     * @param <G>   Type of the 14th element <code>g</code>
     * @param <H>   Type of the 15th element <code>h</code>
     * @param <I>   Type of the 16th element <code>i</code>
     * @return      Tuple containing 16 elements that could be retrieved as their original types.
     */
    public static <T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I> Tuple16<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I> create(
            final T t, final U u, final V v, final W w, final X x, final Y y, final Z z, final A a, final B b,
            final C c, final D d, final E e, final F f, final G g, final H h, final I i){
        return new Tuple16(t, u, v, w, x, y, z, a, b, c, d, e, f, g, h, i);
    }

    /**
     * Create a Tuple17 instance that contains 17 elements of 17 types respectively
     * @param t     First element to be persisted by the Tuple
     * @param u     Second element to be persisted by the Tuple
     * @param v     Third element to be persisted by the Tuple
     * @param w     Fourth element to be persisted by the Tuple
     * @param x     Fifth element to be persisted by the Tuple
     * @param y     Sixth element to be persisted by the Tuple
     * @param z     Seventh element to be persisted by the Tuple
     * @param a     Eighth element to be persisted by the Tuple
     * @param b     Ninth element to be persisted by the Tuple
     * @param c     Tenth element to be persisted by the Tuple
     * @param d     Eleventh element to be persisted by the Tuple
     * @param e     Twelfth element to be persisted by the Tuple
     * @param f     Thirteenth element to be persisted by the Tuple
     * @param g     Fourteenth element to be persisted by the Tuple
     * @param h     Fifteenth element to be persisted by the Tuple
     * @param i     Sixteenth element to be persisted by the Tuple
     * @param j     Seventeenth element to be persisted by the Tuple
     * @param <T>   Type of the first element <code>t</code>
     * @param <U>   Type of the second element <code>u</code>
     * @param <V>   Type of the third element <code>v</code>
     * @param <W>   Type of the fourth element <code>w</code>
     * @param <X>   Type of the fifth element <code>x</code>
     * @param <Y>   Type of the sixth element <code>y</code>
     * @param <Z>   Type of the seventh element <code>z</code>
     * @param <A>   Type of the 8th element <code>a</code>
     * @param <B>   Type of the 9th element <code>b</code>
     * @param <C>   Type of the 10th element <code>c</code>
     * @param <D>   Type of the 11th element <code>d</code>
     * @param <E>   Type of the 12th element <code>e</code>
     * @param <F>   Type of the 13th element <code>f</code>
     * @param <G>   Type of the 14th element <code>g</code>
     * @param <H>   Type of the 15th element <code>h</code>
     * @param <I>   Type of the 16th element <code>i</code>
     * @param <J>   Type of the 17th element <code>j</code>
     * @return      Tuple containing 17 elements that could be retrieved as their original types.
     */
    public static <T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I,J> Tuple17<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I,J> create(
            final T t, final U u, final V v, final W w, final X x, final Y y, final Z z, final A a, final B b,
            final C c, final D d, final E e, final F f, final G g, final H h, final I i, final J j){
        return new Tuple17(t, u, v, w, x, y, z, a, b, c, d, e, f, g, h, i, j);
    }

    /**
     * Create a Tuple18 instance that contains 18 elements of 18 types respectively
     * @param t     First element to be persisted by the Tuple
     * @param u     Second element to be persisted by the Tuple
     * @param v     Third element to be persisted by the Tuple
     * @param w     Fourth element to be persisted by the Tuple
     * @param x     Fifth element to be persisted by the Tuple
     * @param y     Sixth element to be persisted by the Tuple
     * @param z     Seventh element to be persisted by the Tuple
     * @param a     Eighth element to be persisted by the Tuple
     * @param b     Ninth element to be persisted by the Tuple
     * @param c     Tenth element to be persisted by the Tuple
     * @param d     Eleventh element to be persisted by the Tuple
     * @param e     Twelfth element to be persisted by the Tuple
     * @param f     Thirteenth element to be persisted by the Tuple
     * @param g     Fourteenth element to be persisted by the Tuple
     * @param h     Fifteenth element to be persisted by the Tuple
     * @param i     Sixteenth element to be persisted by the Tuple
     * @param j     Seventeenth element to be persisted by the Tuple
     * @param k     Eighteenth element to be persisted by the Tuple
     * @param <T>   Type of the first element <code>t</code>
     * @param <U>   Type of the second element <code>u</code>
     * @param <V>   Type of the third element <code>v</code>
     * @param <W>   Type of the fourth element <code>w</code>
     * @param <X>   Type of the fifth element <code>x</code>
     * @param <Y>   Type of the sixth element <code>y</code>
     * @param <Z>   Type of the seventh element <code>z</code>
     * @param <A>   Type of the 8th element <code>a</code>
     * @param <B>   Type of the 9th element <code>b</code>
     * @param <C>   Type of the 10th element <code>c</code>
     * @param <D>   Type of the 11th element <code>d</code>
     * @param <E>   Type of the 12th element <code>e</code>
     * @param <F>   Type of the 13th element <code>f</code>
     * @param <G>   Type of the 14th element <code>g</code>
     * @param <H>   Type of the 15th element <code>h</code>
     * @param <I>   Type of the 16th element <code>i</code>
     * @param <J>   Type of the 17th element <code>j</code>
     * @param <K>   Type of the 18th element <code>k</code>
     * @return      Tuple containing 18 elements that could be retrieved as their original types.
     */
    public static <T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I,J,K> Tuple18<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I,J,K> create(
            final T t, final U u, final V v, final W w, final X x, final Y y, final Z z, final A a, final B b,
            final C c, final D d, final E e, final F f, final G g, final H h, final I i, final J j, final K k){
        return new Tuple18(t, u, v, w, x, y, z, a, b, c, d, e, f, g, h, i, j, k);
    }

    /**
     * Create a Tuple19 instance that contains 19 elements of 19 types respectively
     * @param t     First element to be persisted by the Tuple
     * @param u     Second element to be persisted by the Tuple
     * @param v     Third element to be persisted by the Tuple
     * @param w     Fourth element to be persisted by the Tuple
     * @param x     Fifth element to be persisted by the Tuple
     * @param y     Sixth element to be persisted by the Tuple
     * @param z     Seventh element to be persisted by the Tuple
     * @param a     Eighth element to be persisted by the Tuple
     * @param b     Ninth element to be persisted by the Tuple
     * @param c     Tenth element to be persisted by the Tuple
     * @param d     Eleventh element to be persisted by the Tuple
     * @param e     Twelfth element to be persisted by the Tuple
     * @param f     Thirteenth element to be persisted by the Tuple
     * @param g     Fourteenth element to be persisted by the Tuple
     * @param h     Fifteenth element to be persisted by the Tuple
     * @param i     Sixteenth element to be persisted by the Tuple
     * @param j     Seventeenth element to be persisted by the Tuple
     * @param k     Eighteenth element to be persisted by the Tuple
     * @param l     Nineteenth element to be persisted by the Tuple
     * @param <T>   Type of the first element <code>t</code>
     * @param <U>   Type of the second element <code>u</code>
     * @param <V>   Type of the third element <code>v</code>
     * @param <W>   Type of the fourth element <code>w</code>
     * @param <X>   Type of the fifth element <code>x</code>
     * @param <Y>   Type of the sixth element <code>y</code>
     * @param <Z>   Type of the seventh element <code>z</code>
     * @param <A>   Type of the 8th element <code>a</code>
     * @param <B>   Type of the 9th element <code>b</code>
     * @param <C>   Type of the 10th element <code>c</code>
     * @param <D>   Type of the 11th element <code>d</code>
     * @param <E>   Type of the 12th element <code>e</code>
     * @param <F>   Type of the 13th element <code>f</code>
     * @param <G>   Type of the 14th element <code>g</code>
     * @param <H>   Type of the 15th element <code>h</code>
     * @param <I>   Type of the 16th element <code>i</code>
     * @param <J>   Type of the 17th element <code>j</code>
     * @param <K>   Type of the 18th element <code>k</code>
     * @param <L>   Type of the 19th element <code>l</code>
     * @return      Tuple containing 19 elements that could be retrieved as their original types.
     */
    public static <T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I,J,K,L> Tuple19<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I,J,K,L> create(
            final T t, final U u, final V v, final W w, final X x, final Y y, final Z z, final A a, final B b,
            final C c, final D d, final E e, final F f, final G g, final H h, final I i, final J j, final K k,
            final L l){
        return new Tuple19(t, u, v, w, x, y, z, a, b, c, d, e, f, g, h, i, j, k, l);
    }

    /**
     * Create a Tuple20 instance that contains 20 elements of 20 types respectively
     * @param t     First element to be persisted by the Tuple
     * @param u     Second element to be persisted by the Tuple
     * @param v     Third element to be persisted by the Tuple
     * @param w     Fourth element to be persisted by the Tuple
     * @param x     Fifth element to be persisted by the Tuple
     * @param y     Sixth element to be persisted by the Tuple
     * @param z     Seventh element to be persisted by the Tuple
     * @param a     Eighth element to be persisted by the Tuple
     * @param b     Ninth element to be persisted by the Tuple
     * @param c     Tenth element to be persisted by the Tuple
     * @param d     Eleventh element to be persisted by the Tuple
     * @param e     Twelfth element to be persisted by the Tuple
     * @param f     Thirteenth element to be persisted by the Tuple
     * @param g     Fourteenth element to be persisted by the Tuple
     * @param h     Fifteenth element to be persisted by the Tuple
     * @param i     Sixteenth element to be persisted by the Tuple
     * @param j     Seventeenth element to be persisted by the Tuple
     * @param k     Eighteenth element to be persisted by the Tuple
     * @param l     Nineteenth element to be persisted by the Tuple
     * @param m     Twentieth element to be persisted by the Tuple
     * @param <T>   Type of the first element <code>t</code>
     * @param <U>   Type of the second element <code>u</code>
     * @param <V>   Type of the third element <code>v</code>
     * @param <W>   Type of the fourth element <code>w</code>
     * @param <X>   Type of the fifth element <code>x</code>
     * @param <Y>   Type of the sixth element <code>y</code>
     * @param <Z>   Type of the seventh element <code>z</code>
     * @param <A>   Type of the 8th element <code>a</code>
     * @param <B>   Type of the 9th element <code>b</code>
     * @param <C>   Type of the 10th element <code>c</code>
     * @param <D>   Type of the 11th element <code>d</code>
     * @param <E>   Type of the 12th element <code>e</code>
     * @param <F>   Type of the 13th element <code>f</code>
     * @param <G>   Type of the 14th element <code>g</code>
     * @param <H>   Type of the 15th element <code>h</code>
     * @param <I>   Type of the 16th element <code>i</code>
     * @param <J>   Type of the 17th element <code>j</code>
     * @param <K>   Type of the 18th element <code>k</code>
     * @param <L>   Type of the 19th element <code>l</code>
     * @param <M>   Type of the 20th element <code>m</code>
     * @return      Tuple containing 20 elements that could be retrieved as their original types.
     */
    public static <T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I,J,K,L,M> Tuple20<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I,J,K,L,M> create(
            final T t, final U u, final V v, final W w, final X x, final Y y, final Z z, final A a, final B b,
            final C c, final D d, final E e, final F f, final G g, final H h, final I i, final J j, final K k,
            final L l, final M m){
        return new Tuple20(t, u, v, w, x, y, z, a, b, c, d, e, f, g, h, i, j, k, l, m);
    }

    /**
     * Create a TuplePlus instance that contains more than 20 elements of 20 types respectively
     * @param t     First element to be persisted by the Tuple
     * @param u     Second element to be persisted by the Tuple
     * @param v     Third element to be persisted by the Tuple
     * @param w     Fourth element to be persisted by the Tuple
     * @param x     Fifth element to be persisted by the Tuple
     * @param y     Sixth element to be persisted by the Tuple
     * @param z     Seventh element to be persisted by the Tuple
     * @param a     Eighth element to be persisted by the Tuple
     * @param b     Ninth element to be persisted by the Tuple
     * @param c     Tenth element to be persisted by the Tuple
     * @param d     Eleventh element to be persisted by the Tuple
     * @param e     Twelfth element to be persisted by the Tuple
     * @param f     Thirteenth element to be persisted by the Tuple
     * @param g     Fourteenth element to be persisted by the Tuple
     * @param h     Fifteenth element to be persisted by the Tuple
     * @param i     Sixteenth element to be persisted by the Tuple
     * @param j     Seventeenth element to be persisted by the Tuple
     * @param k     Eighteenth element to be persisted by the Tuple
     * @param l     Nineteenth element to be persisted by the Tuple
     * @param m     Twentieth element to be persisted by the Tuple
     * @param others     All other elements to be persisted by the Tuple
     * @param <T>   Type of the first element <code>t</code>
     * @param <U>   Type of the second element <code>u</code>
     * @param <V>   Type of the third element <code>v</code>
     * @param <W>   Type of the fourth element <code>w</code>
     * @param <X>   Type of the fifth element <code>x</code>
     * @param <Y>   Type of the sixth element <code>y</code>
     * @param <Z>   Type of the seventh element <code>z</code>
     * @param <A>   Type of the 8th element <code>a</code>
     * @param <B>   Type of the 9th element <code>b</code>
     * @param <C>   Type of the 10th element <code>c</code>
     * @param <D>   Type of the 11th element <code>d</code>
     * @param <E>   Type of the 12th element <code>e</code>
     * @param <F>   Type of the 13th element <code>f</code>
     * @param <G>   Type of the 14th element <code>g</code>
     * @param <H>   Type of the 15th element <code>h</code>
     * @param <I>   Type of the 16th element <code>i</code>
     * @param <J>   Type of the 17th element <code>j</code>
     * @param <K>   Type of the 18th element <code>k</code>
     * @param <L>   Type of the 19th element <code>l</code>
     * @param <M>   Type of the 20th element <code>m</code>
     * @return      Tuple containing more than 20 elements that could be retrieved as their original types.
     */
    public static <T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I,J,K,L,M> TuplePlus<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I,J,K,L,M> create(
            T t, U u, V v, W w, X x, Y y, Z z, A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, Object... others){
        return new TuplePlus(t, u, v, w, x, y, z, a, b, c, d, e, f, g, h, i, j, k, l, m, others);
    }

    /**
     * Factory to create a <tt>Tuple</tt> instance of type <tt>T</tt> with elements of the same type
     * @param elements  Elements of same type <tt>T</tt> to be persisted
     * @param <T>       Type of the given elements
     * @return          A strong-typed Tuple instance
     */
    public static <T> Tuple<T> setOf(T... elements) {
        if(elements == null)
            return (Tuple<T>) Functions.ReturnsDefaultValue.apply(Tuple::new, elements);
        return new Tuple<T>(elements);
    }

    /**
     * Factory to create a <tt>Tuple</tt> instance of type <tt>T</tt> with elements of the same type, and their type to cope with Tpe Erasure
     * @param elementType   Class of the Type of the elements
     * @param elements      Elements of the same type T
     * @param <T>           Actually Type of the elements
     * @return              A strong-typed Tuple containing instances of the same type <tt>T</tt>
     */
    public static <T> Tuple<T> setOf(Class<? extends T> elementType, T[] elements){
        return new Tuple<T>(elementType, elements);
    }

    /**
     * Factory to create a <code>Tuple</code> of type <code>T</code>
     * @param collection    Collection of Elements of type <code>T</code> to be persisted
     * @param clazz         Class of the elements to cope with Java Type Erasure
     * @param <T>           Declared Type of the elements
     * @return              A strong-typed Tuple containing instances of the same type <tt>T</tt>
     */
    public static <T> Tuple<T> setOf(Collection<T> collection, Class<? extends T> clazz){
        Objects.requireNonNull(collection);
        T[] array = (T[])collection.toArray((T[]) Array.newInstance(clazz, 0));
        return setOf(clazz, array);
    }

    //endregion Factories to create Strong-typed Tuple instances based on the number of given arguments

}
