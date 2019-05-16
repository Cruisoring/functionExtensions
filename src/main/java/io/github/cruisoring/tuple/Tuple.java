package io.github.cruisoring.tuple;

import io.github.cruisoring.Functions;
import io.github.cruisoring.TypeHelper;
import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.utility.ArrayHelper;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Predicate;

import static io.github.cruisoring.Asserts.checkStates;
import static io.github.cruisoring.Asserts.checkWithoutNull;

/**
 * This is a special data structure contains multiple immutable elements in fixed sequence. The AutoCloseable implementation
 * would close any elements if they are also AutoCloseable.
 * @param <T>   the generic type of the value held by the {@code Tuple}
 */
public class Tuple<T extends Object> implements ITuple<T> {

    //region Tuples that are used commonly
    public static final Tuple0 UNIT = new Tuple0();
    public static final Tuple1 TRUE = new Tuple1(true);
    public static final Tuple1 FALSE = new Tuple1(false);
    //endregion

    //region Static methods
    /**
     * Create a Tuple instance to keep any number of elements without caring about their Type info. Notice this method
     * would always try to unfold the <code>elements</code> as an Array while create() might treat elements as a single argument.
     *
     * @param elements All elements to be persisted by the Tuple
     * @return A <tt>Tuple</tt> instance with length of the elements
     */
    public static Tuple of(final Object... elements) {
        if (elements == null) {
            return create(null);
        }
        int length = elements.length;
        switch (length) {
            case 0:
                return UNIT;
            case 1:
                return new Tuple1(elements[0]);
            case 2:
                return new Tuple2(elements[0], elements[1]);
            case 3:
                return new Tuple3(elements[0], elements[1], elements[2]);
            case 4:
                return new Tuple4(elements[0], elements[1], elements[2], elements[3]);
            case 5:
                return new Tuple5(elements[0], elements[1], elements[2], elements[3], elements[4]);
            case 6:
                return new Tuple6(elements[0], elements[1], elements[2], elements[3], elements[4], elements[5]);
            case 7:
                return new Tuple7(elements[0], elements[1], elements[2], elements[3], elements[4],
                        elements[5], elements[6]);
            case 8:
                return new Tuple8(elements[0], elements[1], elements[2], elements[3], elements[4],
                        elements[5], elements[6], elements[7]);
            case 9:
                return new Tuple9(elements[0], elements[1], elements[2], elements[3], elements[4],
                        elements[5], elements[6], elements[7], elements[8]);
            case 10:
                return new Tuple10(elements[0], elements[1], elements[2], elements[3], elements[4],
                        elements[5], elements[6], elements[7], elements[8], elements[9]);

            default:
                return new TuplePlus(elements[0], elements[1], elements[2], elements[3], elements[4],
                        elements[5], elements[6], elements[7], elements[8], elements[9],
                        Arrays.copyOfRange(elements, 10, length));
        }
    }

    /**
     * Factory to create a <tt>Tuple</tt> instance of type <tt>T</tt> with elements of the same type, and their type to cope with Tpe Erasure
     *
     * @param elements Elements of the same type T
     * @param <V>      Actually Type of the elements
     * @return A strong-typed Tuple containing instances of the same type <tt>T</tt>
     */
    public static <V> Tuple<V> setOf(final V... elements) {
        checkStates(elements != null);
        Class<? extends V> elemntType = (Class<? extends V>) ArrayHelper.getComponentType(elements);
        return setOfType(elemntType, elements);
    }

    /**
     * Factory to create a <code>Tuple</code> of type <code>T</code>
     *
     * @param collection  Collection of Elements of type <code>T</code> to be persisted
     * @param elementType Class of the elements to cope with Java Type Erasure
     * @param <T>         Declared Type of the elements
     * @return A strong-typed Tuple containing instances of the same type <tt>T</tt>
     */
    public static <T> Tuple<T> setFromCollection(Class<? extends T> elementType, Collection<T> collection) {
        checkWithoutNull(collection);
        checkWithoutNull(elementType);
        T[] array = collection.toArray((T[]) Array.newInstance(elementType, 0));
        return setOfType(elementType, array);
    }

    /**
     * Factory to create a <tt>Tuple</tt> instance with fixed length of elements of the same type <tt>V</tt>
     *
     * @param elementType Class of the Type of the elements
     * @param elements    Elements of same type <tt>V</tt> to be persisted
     * @param <V>         Type of the given elements
     * @return A strong-typed <code>Tuple?.Set1&lt;V&gt;</code>Tuple instance
     */
    public static <V> Tuple<V> setOfType(final Class<? extends V> elementType, final V... elements) {
        checkWithoutNull(elementType);
        checkStates(elements != null);
        int length = elements.length;

        switch (length) {
            case 0:
                return UNIT;
            case 1:
                return new Tuple1.Set1<V>(elementType, elements[0]);
            case 2:
                return new Tuple2.Set2<V>(elementType, elements[0], elements[1]);
            case 3:
                return new Tuple3.Set3<V>(elementType, elements[0], elements[1], elements[2]);
            case 4:
                return new Tuple4.Set4<V>(elementType, elements[0], elements[1], elements[2], elements[3]);
            case 5:
                return new Tuple5.Set5<V>(elementType, elements[0], elements[1], elements[2], elements[3], elements[4]);
            case 6:
                return new Tuple6.Set6<V>(elementType, elements[0], elements[1], elements[2], elements[3], elements[4],
                        elements[5]);
            case 7:
                return new Tuple7.Set7<V>(elementType, elements[0], elements[1], elements[2], elements[3], elements[4],
                        elements[5], elements[6]);
            case 8:
                return new Tuple8.Set8<V>(elementType, elements[0], elements[1], elements[2], elements[3], elements[4],
                        elements[5], elements[6], elements[7]);
            case 9:
                return new Tuple9.Set9<V>(elementType, elements[0], elements[1], elements[2], elements[3], elements[4],
                        elements[5], elements[6], elements[7], elements[8]);
            case 10:
                return new Tuple10.Set10<V>(elementType, elements[0], elements[1], elements[2], elements[3], elements[4],
                        elements[5], elements[6], elements[7], elements[8], elements[9]);

            default:
                return new TuplePlus.SetPlus<V>(elementType, elements[0], elements[1], elements[2], elements[3], elements[4],
                        elements[5], elements[6], elements[7], elements[8], elements[9],
                        Arrays.copyOfRange(elements, 10, length));

        }
    }

    //region Factories to create Strong-typed Tuple instances based on the number of given arguments

    /**
     * Crate an Tuple0 that has no value saved, shall not be called directly.
     *
     * @return The Tuple0 object.
     */
    protected static Tuple0 create() {
        return UNIT;
    }

    /**
     * Create a Tuple1 instance that contains only one value of Type T
     *
     * @param t   Element to be persisted by the Tuple
     * @param <T> Type of the element <code>t</code>
     * @return Tuple containing 6 elements that could be accessed as their original types.
     */
    public static <T> Tuple1<T> create(final T t) {
        return new Tuple1(t);
    }

    /**
     * Create a Tuple2 instance that contains 2 elements of Types T/U respectively
     *
     * @param t   First element to be persisted by the Tuple
     * @param u   Second element to be persisted by the Tuple
     * @param <T> Type of the first element <code>t</code>
     * @param <U> Type of the second element <code>u</code>
     * @return Tuple containing 2 elements that could be accessed as their original types.
     */
    public static <T, U> Tuple2<T, U> create(final T t, final U u) {
        return new Tuple2(t, u);
    }

    /**
     * Create a Tuple3 instance that contains 3 elements of Types T/U/V respectively
     *
     * @param t   First element to be persisted by the Tuple
     * @param u   Second element to be persisted by the Tuple
     * @param v   Third element to be persisted by the Tuple
     * @param <T> Type of the first element <code>t</code>
     * @param <U> Type of the second element <code>u</code>
     * @param <V> Type of the third element <code>v</code>
     * @return Tuple containing 3 elements that could be accessed as their original types.
     */
    public static <T, U, V> Tuple3<T, U, V> create(final T t, final U u, final V v) {
        return new Tuple3(t, u, v);
    }

    /**
     * Create a Tuple4 instance that contains 4 elements of Types T/U/V/W respectively
     *
     * @param t   First element to be persisted by the Tuple
     * @param u   Second element to be persisted by the Tuple
     * @param v   Third element to be persisted by the Tuple
     * @param w   Fourth element to be persisted by the Tuple
     * @param <T> Type of the first element <code>t</code>
     * @param <U> Type of the second element <code>u</code>
     * @param <V> Type of the third element <code>v</code>
     * @param <W> Type of the fourth element <code>w</code>
     * @return Tuple containing 4 elements that could be accessed as their original types.
     */
    public static <T, U, V, W> Tuple4<T, U, V, W> create(final T t, final U u, final V v, final W w) {
        return new Tuple4(t, u, v, w);
    }

    /**
     * Create a Tuple5 instance that contains 5 elements of Types T/U/V/W/X respectively
     *
     * @param t   First element to be persisted by the Tuple
     * @param u   Second element to be persisted by the Tuple
     * @param v   Third element to be persisted by the Tuple
     * @param w   Fourth element to be persisted by the Tuple
     * @param x   Fifth element to be persisted by the Tuple
     * @param <T> Type of the first element <code>t</code>
     * @param <U> Type of the second element <code>u</code>
     * @param <V> Type of the third element <code>v</code>
     * @param <W> Type of the fourth element <code>w</code>
     * @param <X> Type of the fifth element <code>x</code>
     * @return Tuple containing 5 elements that could be accessed as their original types.
     */
    public static <T, U, V, W, X> Tuple5<T, U, V, W, X> create(final T t, final U u, final V v, final W w, final X x) {
        return new Tuple5(t, u, v, w, x);
    }

    /**
     * Create a Tuple6 instance that contains 6 elements of Types T/U/V/W/X/Y respectively
     *
     * @param t   First element to be persisted by the Tuple
     * @param u   Second element to be persisted by the Tuple
     * @param v   Third element to be persisted by the Tuple
     * @param w   Fourth element to be persisted by the Tuple
     * @param x   Fifth element to be persisted by the Tuple
     * @param y   Sixth element to be persisted by the Tuple
     * @param <T> Type of the first element <code>t</code>
     * @param <U> Type of the second element <code>u</code>
     * @param <V> Type of the third element <code>v</code>
     * @param <W> Type of the fourth element <code>w</code>
     * @param <X> Type of the fifth element <code>x</code>
     * @param <Y> Type of the sixth element <code>y</code>
     * @return Tuple containing 6 elements that could be accessed as their original types.
     */
    public static <T, U, V, W, X, Y> Tuple6<T, U, V, W, X, Y> create(
            final T t, final U u, final V v, final W w, final X x, final Y y) {
        return new Tuple6(t, u, v, w, x, y);
    }

    /**
     * Create a Tuple7 instance that contains 7 elements of Types T/U/V/W/X/Y/Z respectively
     *
     * @param t   First element to be persisted by the Tuple
     * @param u   Second element to be persisted by the Tuple
     * @param v   Third element to be persisted by the Tuple
     * @param w   Fourth element to be persisted by the Tuple
     * @param x   Fifth element to be persisted by the Tuple
     * @param y   Sixth element to be persisted by the Tuple
     * @param z   Seventh element to be persisted by the Tuple
     * @param <T> Type of the first element <code>t</code>
     * @param <U> Type of the second element <code>u</code>
     * @param <V> Type of the third element <code>v</code>
     * @param <W> Type of the fourth element <code>w</code>
     * @param <X> Type of the fifth element <code>x</code>
     * @param <Y> Type of the sixth element <code>y</code>
     * @param <Z> Type of the seventh element <code>z</code>
     * @return Tuple containing seven elements that could be accessed as their original types.
     */
    public static <T, U, V, W, X, Y, Z> Tuple7<T, U, V, W, X, Y, Z> create(
            final T t, final U u, final V v, final W w, final X x, final Y y, final Z z) {
        return new Tuple7(t, u, v, w, x, y, z);
    }

    /**
     * Create a Tuple8 instance that contains 8 elements of 8 types respectively
     *
     * @param t   First element to be persisted by the Tuple
     * @param u   Second element to be persisted by the Tuple
     * @param v   Third element to be persisted by the Tuple
     * @param w   Fourth element to be persisted by the Tuple
     * @param x   Fifth element to be persisted by the Tuple
     * @param y   Sixth element to be persisted by the Tuple
     * @param z   Seventh element to be persisted by the Tuple
     * @param a   Eighth element to be persisted by the Tuple
     * @param <T> Type of the first element <code>t</code>
     * @param <U> Type of the second element <code>u</code>
     * @param <V> Type of the third element <code>v</code>
     * @param <W> Type of the fourth element <code>w</code>
     * @param <X> Type of the fifth element <code>x</code>
     * @param <Y> Type of the sixth element <code>y</code>
     * @param <Z> Type of the seventh element <code>z</code>
     * @param <A> Type of the 8th element <code>a</code>
     * @return Tuple containing 8 elements that could be retrieved as their original types.
     */
    public static <T, U, V, W, X, Y, Z, A> Tuple8<T, U, V, W, X, Y, Z, A> create(
            final T t, final U u, final V v, final W w, final X x, final Y y, final Z z, final A a) {
        return new Tuple8(t, u, v, w, x, y, z, a);
    }

    /**
     * Create a Tuple9 instance that contains 9 elements of 9 types respectively
     *
     * @param t   First element to be persisted by the Tuple
     * @param u   Second element to be persisted by the Tuple
     * @param v   Third element to be persisted by the Tuple
     * @param w   Fourth element to be persisted by the Tuple
     * @param x   Fifth element to be persisted by the Tuple
     * @param y   Sixth element to be persisted by the Tuple
     * @param z   Seventh element to be persisted by the Tuple
     * @param a   Eighth element to be persisted by the Tuple
     * @param b   Ninth element to be persisted by the Tuple
     * @param <T> Type of the first element <code>t</code>
     * @param <U> Type of the second element <code>u</code>
     * @param <V> Type of the third element <code>v</code>
     * @param <W> Type of the fourth element <code>w</code>
     * @param <X> Type of the fifth element <code>x</code>
     * @param <Y> Type of the sixth element <code>y</code>
     * @param <Z> Type of the seventh element <code>z</code>
     * @param <A> Type of the 8th element <code>a</code>
     * @param <B> Type of the 9th element <code>b</code>
     * @return Tuple containing 9 elements that could be retrieved as their original types.
     */
    public static <T, U, V, W, X, Y, Z, A, B> Tuple9<T, U, V, W, X, Y, Z, A, B> create(
            final T t, final U u, final V v, final W w, final X x, final Y y, final Z z, final A a, final B b) {
        return new Tuple9(t, u, v, w, x, y, z, a, b);
    }

    /**
     * Create a Tuple10 instance that contains 10 elements of 10 types respectively
     *
     * @param t   First element to be persisted by the Tuple
     * @param u   Second element to be persisted by the Tuple
     * @param v   Third element to be persisted by the Tuple
     * @param w   Fourth element to be persisted by the Tuple
     * @param x   Fifth element to be persisted by the Tuple
     * @param y   Sixth element to be persisted by the Tuple
     * @param z   Seventh element to be persisted by the Tuple
     * @param a   Eighth element to be persisted by the Tuple
     * @param b   Ninth element to be persisted by the Tuple
     * @param c   Tenth element to be persisted by the Tuple
     * @param <T> Type of the first element <code>t</code>
     * @param <U> Type of the second element <code>u</code>
     * @param <V> Type of the third element <code>v</code>
     * @param <W> Type of the fourth element <code>w</code>
     * @param <X> Type of the fifth element <code>x</code>
     * @param <Y> Type of the sixth element <code>y</code>
     * @param <Z> Type of the seventh element <code>z</code>
     * @param <A> Type of the 8th element <code>a</code>
     * @param <B> Type of the 9th element <code>b</code>
     * @param <C> Type of the 10th element <code>c</code>
     * @return Tuple containing 10 elements that could be retrieved as their original types.
     */
    public static <T, U, V, W, X, Y, Z, A, B, C> Tuple10<T, U, V, W, X, Y, Z, A, B, C> create(
            final T t, final U u, final V v, final W w, final X x, final Y y, final Z z, final A a, final B b,
            final C c) {
        return new Tuple10(t, u, v, w, x, y, z, a, b, c);
    }

    /**
     * Create a TuplePlus instance that contains more than 20 elements of 20 types respectively
     *
     * @param t      First element to be persisted by the Tuple
     * @param u      Second element to be persisted by the Tuple
     * @param v      Third element to be persisted by the Tuple
     * @param w      Fourth element to be persisted by the Tuple
     * @param x      Fifth element to be persisted by the Tuple
     * @param y      Sixth element to be persisted by the Tuple
     * @param z      Seventh element to be persisted by the Tuple
     * @param a      Eighth element to be persisted by the Tuple
     * @param b      Ninth element to be persisted by the Tuple
     * @param c      Tenth element to be persisted by the Tuple
     * @param others All other elements to be persisted by the Tuple
     * @param <T>    Type of the first element <code>t</code>
     * @param <U>    Type of the second element <code>u</code>
     * @param <V>    Type of the third element <code>v</code>
     * @param <W>    Type of the fourth element <code>w</code>
     * @param <X>    Type of the fifth element <code>x</code>
     * @param <Y>    Type of the sixth element <code>y</code>
     * @param <Z>    Type of the seventh element <code>z</code>
     * @param <A>    Type of the 8th element <code>a</code>
     * @param <B>    Type of the 9th element <code>b</code>
     * @param <C>    Type of the 10th element <code>c</code>
     * @return Tuple containing more than 20 elements that could be retrieved as their original types.
     */
    public static <T, U, V, W, X, Y, Z, A, B, C> TuplePlus<T, U, V, W, X, Y, Z, A, B, C> create(
            T t, U u, V v, W w, X x, Y y, Z z, A a, B b, C c, Object... others) {
        return new TuplePlus(t, u, v, w, x, y, z, a, b, c, others);
    }
    //endregion Factories to create Strong-typed Tuple instances based on the number of given arguments
    //endregion

    //region Instance variables
    protected final Class<? extends T> _elementType;
    protected final T[] values;
    protected int[][] deepLength;
    protected String _toString;
    protected Integer _hashCode;
    protected Set<Integer> _signatures;
    private boolean closed = false;
    //endregion

    //region Constructors

    /**
     * Protected constructor to keep the elements as a final array.
     *
     * @param eType    type of the elements being specified to cope with <tt>Type Erasure</tt>
     * @param elements values to be persisted by the Tuple.
     */
    protected Tuple(Class<? extends T> eType, final T... elements) {
        this._elementType = eType;
        int length = elements == null ? 1 : elements.length;
        values = (T[]) ArrayHelper.getNewArray(eType, length);

        if (elements == null) {
            values[0] = null;
        } else {
            for (int i = 0; i < length; i++) {
                values[i] = elements[i];
            }
        }
    }

    /**
     * Protected constructor to keep the elements as a final array.
     *
     * @param elements values to be persisted by the Tuple.
     */
    protected Tuple(final T... elements) {
        this._elementType = (Class<? extends T>) ArrayHelper.getComponentType(elements);
        int length = elements == null ? 1 : elements.length;
        values = (T[]) ArrayHelper.getNewArray(_elementType, length);

        if (elements == null) {
            values[0] = null;
        } else {
            for (int i = 0; i < length; i++) {
                values[i] = elements[i];
            }
        }
    }
    //endregion

    //region Instance methods
    /**
     * Get new Array of values to prevent changes on the underlying array.
     *
     * @return copied Array of the persistent values
     */
    public T[] asArray() {
        return Arrays.copyOf(values, values.length);
    }

    /**
     * Get the elements at specific position
     *
     * @param index Index of the expected element
     * @return Element at that index
     * @throws IndexOutOfBoundsException When invalid index is provided
     */
    public T getValue(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= getLength())
            throw new IndexOutOfBoundsException();
        return values[index];
    }

    @Override
    public <U> Tuple<U> getSetOf(Class<U> clazz) {
        checkWithoutNull(clazz);

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
                    if (equivalent.equals(vClass)) {
                        list.add((U) v);
                    } else if (predicate.test(vClass)) {
                        U t = (U) TypeHelper.getToEquivalentParallelConverter(vClass).apply(v);
                        list.add(t);
                    }
                } else if (!TypeHelper.isPrimitive(clazz)) {
                    list.add(null);
                }
            }
            Object[] array = list.toArray((U[]) ArrayHelper.getNewArray(equivalent, list.size()));
            return setOfType(equivalent, (U[]) array);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public <S> Tuple<S> getSetOf(Class<S> clazz, Predicate<S> valuePredicate) {
        checkWithoutNull(clazz);
        checkWithoutNull(valuePredicate);
        List<S> matched = new ArrayList<>();

        Predicate<Class> classPredicate = TypeHelper.getClassEqualitor(clazz);

        int length = getLength();
        for (int i = 0; i < length; i++) {
            Object v = values[i];
            if (v != null) {
                try {
                    if (classPredicate.test(v.getClass()) && valuePredicate.test((S) v))
                        matched.add((S) v);
                } catch (Exception ex) {
                }
            }
        }
        S[] array = (S[]) matched.stream().toArray();
        return setOfType(clazz, array);
    }

    @Override
    public int getLength() {
        return values.length;
    }

    @Override
    public int[][] getDeepLength() {
        if (deepLength == null) {
            deepLength = TypeHelper.getDeepIndexes(values);
        }
        return deepLength;
    }

    @Override
    public int hashCode() {
        if (_hashCode == null) {
            _hashCode = TypeHelper.deepHashCode(values);
        }
        return _hashCode;
    }

    /**
     * Get the set of this.hashCode() and all its elements' hashCodes as signatures.
     *
     * @return the hashCodes of this and its elements as a Set1.
     */
    @Override
    public Set<Integer> getSignatures() {
        if (_signatures == null) {
            Set<Integer> hashCodes = new HashSet<>();
            hashCodes.add(hashCode());
            Arrays.stream(values).forEach(v -> hashCodes.add(v == null ? 0 : v.hashCode()));
            _signatures = Collections.unmodifiableSet(hashCodes);
        }
        return _signatures;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Tuple)) {
            return false;
        } else if (obj == this) {
            return true;
        }

        Tuple other = (Tuple) obj;
        //Notice: tuples with different signatures could still be equal
        if (!other.canEqual(this) || other.getLength() != values.length || hashCode() != other.hashCode()) {
            return false;
        }

        return TypeHelper.valueEquals(values, other.values, getDeepLength(), other.getDeepLength());
    }

    @Override
    public boolean canEqual(Object obj) {
        if (!(obj instanceof Tuple))
            return false;
        Tuple other = (Tuple) obj;
        return this._elementType.isAssignableFrom(other._elementType) || other._elementType.isAssignableFrom(this._elementType);
    }

    @Override
    public int compareTo(Object o) {
        if(o == null){
            return hashCode();
        } else if(!(o instanceof WithValues)) {
            return hashCode() - o.hashCode();
        }

        WithValues other = (WithValues)o;
        int thisLength = getLength();
        int otherLength = other.getLength();
        int len = thisLength < otherLength ? thisLength : otherLength;
        for (int i = 0; i < len; i++) {
            T t1 = getValue(i);
            int hash1 = t1 == null ? 0 : t1.hashCode();
            Object t2 = other.getValue(i);
            int hash2 = t2 == null ? 0 : t2.hashCode();
            if(hash1 != hash2){
                return hash1 - hash2;
            }
        }
        return thisLength - otherLength;
    }

    @Override
    public String toString() {
        if (_toString == null) {
            _toString = TypeHelper.deepToString(values);
        }
        return _toString;
    }

    /**
     * Check the flag <code>closed</code> first, then close the saved values if they are AutoCloseable in reverse order.
     *
     * @throws Exception any exception that could be thrown when closing AutoCloseable objects
     */
    @Override
    public void close() throws Exception {
        if (!closed) {
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
    //endregion

}
