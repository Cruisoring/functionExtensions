package io.github.cruisoring.tuple;

import java.util.Arrays;
import java.util.Objects;

/**
 * Special Tuple type with all elements of the same type <code>T</code>
 * @param <T>   Type of the persisted elements
 */
public class TupleSet<T> extends Tuple{
    public final Class<T> elementType;

    /**
     * Constructor to accept multiple elements of same type <code>T</code>
     * Notice: if only one 'null' is used, then it would be wrapped as Object[]{null}, thus elementType is Object.class
     * @param elements  Elements to be persisted
     */
    protected TupleSet(final T... elements){
        super(elements);
        elementType = (Class<T>) elements.getClass().getComponentType();
    }

    /**
     * Constructor to accept multiple elements of same type <code>T</code> that is specified explicitly
     * Notice: if only one 'null' is used, then it would be wrapped as Object[]{null}, thus elementType is Object.class
     * @param elementType  type of the elements being specified to cope with <tt>Type Erasure</tt>
     * @param elements  Elements to be persisted
     */
    protected TupleSet(final Class<T> elementType, final T... elements){
        super(elementType, elements);
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
        if(obj == null || !(obj instanceof TupleSet))
            return false;
        if (obj == this)
            return true;
        TupleSet other = (TupleSet)obj;
        if(!other.canEqual(this) || getLength() != ((Tuple) obj).getLength())
            return false;

        return super.equals(other);
    }

    @Override
    public boolean canEqual(Object other) {
        return (other instanceof TupleSet) && ((TupleSet) other).elementType.equals(elementType);
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", elementType.getSimpleName(),
                super.toString());
    }
}
