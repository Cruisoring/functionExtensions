package com.easyworks.tuple;

import com.easyworks.Functions;
import com.easyworks.function.PredicateThrowable;
import com.easyworks.function.SupplierThrowable;
import com.easyworks.utility.Logger;
import com.easyworks.utility.TypeHelper;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This is a special data structure contains multiple immutable elements in fixed sequence. The AutoCloseable implementation
 * would close any elements if they are also AutoCloseable.
 */
public class Tuple implements AutoCloseable {
    public static final Unit UNIT = new Unit();
    public static final Single TRUE = new Single(true);
    public static final Single FALSE = new Single(false);

    protected final Object[] values;

    /**
     * Protected constructor to keep the elements as a final array.
     * @param elements values to be persisted by the Tuple.
     */
    protected Tuple(Object... elements){
        int length = elements.length;
        values = new Object[length];
        for (int i=0; i<length; i++){
            values[i] = elements[i];
        }
    }

    /**
     * Get all Non-null elements matching the given class as an immutable <code>Set</code>
     * @param clazz Class to evaluate the saved values.
     * @param <T>   Type of the given Class.
     * @return      Immutable <code>Set</code> containing matched elements.
     */
    public <T> Set<T> getSetOf(Class<T> clazz){
        Objects.requireNonNull(clazz);
        List<T> matched = new ArrayList<>();

        PredicateThrowable<Class> predicate = TypeHelper.getClassPredicate(clazz);

        int length = getLength();
        for (int i = 0; i < length; i++) {
            Object v = values[i];
            if(v != null){
                try {
                    if(predicate.apply(v.getClass()))
                        matched.add((T)v);
                }catch (Exception ex){}
            }
        };
        T[] array = (T[]) matched.stream().toArray();
        return setOf(array);
    }

    /**
     * Get the length of the saved values.
     * @return length of the values.
     */
    public int getLength(){
        return values.length;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(values);
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

    /**
     * Check the flag <code>closed</code> first, then close the saved values if they are AutoCloseable in reverse order.
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        if(!closed) {
            //Close AutoCloseable object in reverse order
            for (int i = values.length - 1; i >= 0; i--) {
                Object value = values[i];
                if (value != null && value instanceof AutoCloseable) {
                    Functions.Default.run(() -> ((AutoCloseable) value).close());
                    Logger.L("%s closed()", value);
                }
            }
            closed = true;
            Logger.L("%s.close() run successfully!", this);
        }
    }

//    @Override
//    protected void finalize() throws Throwable {
//        if(!closed)
//        {
//            Logger.L( "******FORGOT TO CLOSE THE TUPLE!" );
//        }
//        super.finalize();
//    }

    //region Factories to create Strong-typed Tuple instances based on the number of given arguments

    /**
     * Get the Tuple factory based on number of elements to be catpured.
     * @param elements  The values to be saved.
     * @return  Tuple factory to create corresponding Tuple with the given set of elements.
     */
    public static SupplierThrowable<Tuple> getTupleSupplier(Object... elements){
        if (elements == null){
            return () -> create(null);
        }
        int valueLength = elements.length;
        switch (valueLength){
            case 0: return () -> create();
            case 1: return () -> create(elements[0]);
            case 2: return () -> create(elements[0], elements[1]);
            case 3: return () -> create(elements[0], elements[1], elements[2]);
            case 4: return () -> create(elements[0], elements[1], elements[2], elements[3]);
            case 5: return () -> create(elements[0], elements[1], elements[2], elements[3], elements[4]);
            case 6: return () -> create(elements[0], elements[1], elements[2], elements[3], elements[4], elements[5]);
            case 7: return () -> create(elements[0], elements[1], elements[2], elements[3], elements[4], elements[5], elements[6]);
            default: return () -> new Tuple(elements);
        }
    }

    /**
     * Crate an Unit that has no value saved, shall not be called directly.
     * @return  The Unit object.
     */
    protected static Unit create(){
        return UNIT;
    }

    /**
     * Create a Tuple.Single instance that contains only one value of Type T
     * @param t     Element to be persisted by the Tuple
     * @param <T>   Type of the element <code>t</code>
     * @return      Tuple containing 6 elements that could be accessed as their original types.
     */
    public static <T> Single<T> create(T t){
        return new Single<>(t);
    }

    /**
     * Create a Tuple.Dual instance that contains 2 values of Type T/U respectively
     * @param t     First element to be persisted by the Tuple
     * @param u     Second element to be persisted by the Tuple
     * @param <T>   Type of the first element <code>t</code>
     * @param <U>   Type of the second element <code>u</code>
     * @return      Tuple containing 2 elements that could be accessed as their original types.
     */
    public static <T,U> Dual<T,U> create(T t, U u){
        return new Dual<>(t, u);
    }

    /**
     * Create a Tuple.Triple instance that contains 3 values of Type T/U/V respectively
     * @param t     First element to be persisted by the Tuple
     * @param u     Second element to be persisted by the Tuple
     * @param v     Third element to be persisted by the Tuple
     * @param <T>   Type of the first element <code>t</code>
     * @param <U>   Type of the second element <code>u</code>
     * @param <V>   Type of the third element <code>v</code>
     * @return      Tuple containing 3 elements that could be accessed as their original types.
     */
    public static <T,U,V> Triple<T,U,V> create(T t, U u, V v){
        return new Triple<>(t, u, v);
    }

    /**
     * Create a Tuple.Quad instance that contains 4 values of Type T/U/V/W respectively
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
    public static <T,U,V,W> Quad<T,U,V,W> create(T t, U u, V v, W w){
        return new Quad<>(t, u, v, w);
    }

    /**
     * Create a Tuple.Penta instance that contains 5 values of Type T/U/V/W/X respectively
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
    public static <T,U,V,W,X> Penta<T,U,V,W,X> create(T t, U u, V v, W w, X x){
        return new Penta<>(t, u, v, w, x);
    }

    /**
     * Create a Tuple.Hexa instance that contains 6 values of Type T/U/V/W/X/Y respectively
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
    public static <T,U,V,W,X,Y> Hexa<T,U,V,W,X,Y> create(T t, U u, V v, W w, X x, Y y){
        return new Hexa<>(t, u, v, w, x, y);
    }

    /**
     * Create a Tuple.Hepta instance that contains 7 values of Type T/U/V/W/X/Y/Z respectively
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
    public static <T,U,V,W,X,Y,Z> Hepta<T,U,V,W,X,Y,Z> create(T t, U u, V v, W w, X x, Y y, Z z){
        return new Hepta<>(t, u, v, w, x, y, z);
    }

    /**
     * Factory to create a <code>Set</code> instance of type <code>T</code> with elements of the same type
     * @param elements  Elements of same type <code>T</code> to be persisted
     * @param <T>       Type of the given elements
     * @return          A strong-typed Set object
     */
    public static <T> Set<T> setOf(T... elements) {
        if(elements == null)
            return (Set<T>) Functions.ReturnsDefaultValue.apply(Set::new, elements);
        return new Set<T>(elements);
    }

    /**
     * Factory to create a <code>Set</code> of type <code>T</code>
     * @param collection    Collection of Elements of type <code>T</code> to be persisted
     * @param clazz         Class of the elements to cope with Java Type Erasure
     * @param <T>           Declared Type of the elements
     * @return              Strong-typed Set, with component type of <code>T</code>
     */
    public static <T> Set<T> setOf(Collection<T> collection, Class<? extends T> clazz){
        Objects.requireNonNull(collection);
        T[] array = (T[])collection.toArray((T[]) Array.newInstance(clazz, 0));
        return setOf(array);
    }

    //endregion Factories to create Strong-typed Tuple instances based on the number of given arguments

    //region Extended Strong-typed classes

    //endregion
}
