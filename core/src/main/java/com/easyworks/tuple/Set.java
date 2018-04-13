package com.easyworks.tuple;

import java.util.Arrays;
import java.util.Objects;

/**
 * Special Tuple type with all elements of the same type <code>T</code>
 * @param <T>   Type of the persisted elements
 */
public class Set<T> extends Tuple{
    public final static com.easyworks.tuple.Set EMPTY = new com.easyworks.tuple.Set();
    public final Class<T> elementType;

    /**
     * Constructor to accept multiple elements of same type <code>T</code>
     * Notice: if only one 'null' is used, then it would be wrapped as Object[]{null}, thus elementType is Object.class
     * @param elements  Elements to be persisted
     */
    protected Set(T... elements){
        super(elements);
        elementType = (Class<T>) elements.getClass().getComponentType();
    }

    /**
     * Constructor to accept multiple elements of same type <code>T</code> that is specified explicitly
     * Notice: if only one 'null' is used, then it would be wrapped as Object[]{null}, thus elementType is Object.class
     * @param elementType  type of the elements being specified to cope with <tt>Type Erasure</tt>
     * @param elements  Elements to be persisted
     */
    protected Set(Class<T> elementType, T... elements){
        super(elements);
        Objects.requireNonNull(elementType);
        this.elementType = elementType;
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

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof com.easyworks.tuple.Set))
            return false;
        if (obj == this)
            return true;
        com.easyworks.tuple.Set other = (com.easyworks.tuple.Set)obj;
        if(!other.canEqual(this) || getLength() != ((Tuple) obj).getLength())
            return false;

        return super.equals(other);
    }

    @Override
    public boolean canEqual(Object other) {
        return (other instanceof com.easyworks.tuple.Set) && ((com.easyworks.tuple.Set) other).elementType.equals(elementType);
    }

    /**
     * Override Tuple.getSetOf() to return either EMPTY set or the itself if the class is matched
     * @param clazz Class to evaluate the saved values.
     * @param <R>   Type of the persistent elements
     * @return      The Set iselft if the class is matched, or Empty.
     */
//    @Override
    public <R> com.easyworks.tuple.Set<R> getSetOf(Class<R> clazz){
        Objects.requireNonNull(clazz);

        if(clazz.equals(elementType) || clazz.isAssignableFrom(elementType))
            return (com.easyworks.tuple.Set<R>) this;
        return EMPTY;
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", elementType.getSimpleName(),
                super.toString());
    }
}
