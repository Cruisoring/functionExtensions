package io.github.cruisoring.tuple;

public interface WithValues<T> {
    /**
     * Retrieve the element at specific index as an Object.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    T getValue(int index) throws IndexOutOfBoundsException;

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    int getLength();

    /**
     * Assist equals() to check if the other <code>WithValues</code> object can be equal with this ITuple.
     * @param obj   Object to be evaluated.
     * @return      <tt>true</tt> if <tt>obj</tt> can be equal with this.
     */
    boolean canEqual(Object obj);
}
