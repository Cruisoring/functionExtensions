package io.github.cruisoring.utility;

import io.github.cruisoring.Asserts;
import io.github.cruisoring.TypeHelper;
import io.github.cruisoring.throwables.PredicateThrowable;

import java.util.*;
import java.util.stream.IntStream;

import static io.github.cruisoring.Asserts.*;

public class PlainList<E> implements List<E> {

    public static int DEFAULT_CAPACITY = 16;
    public static int MAX_CAPACITY = 0x2000000;

    /**
     * Get the least reasonable figure that is multiple {@code DEFAULT_CAPACITY} to cater the demanded capacity
     * @param capacity  the demanded capacity
     * @return          the least exponential number of {@code DEFAULT_CAPACITY}
     */
    public static int getDefaultCapacity(int capacity){
        if(capacity <= DEFAULT_CAPACITY) {
            return DEFAULT_CAPACITY;
        }
        assertTrue(capacity < MAX_CAPACITY, "The desirable capacity %d is greater than MAX_CAPCITY %d", capacity, MAX_CAPACITY);
        int result = DEFAULT_CAPACITY;
        do {
           result *= 2;
        }while (result < capacity);
        return result;
    }

    protected final Class<? extends E> elementType;
    int capacity;
    E[] elements;
    int upperIndex;

    /**
     * Create a {@code PlainList} by specifying its element type, and Collection of data to be copied.
     * @param elementType       the component type of the Array to keep the elements
     * @param initialCapacity   the desirable capacity to create the initial array, so as to differ the signature with constructor using array
     * @param collection        the Collection of elements to be copied to this {@code PlainList}
     */
    public PlainList(Class<? extends E> elementType, int initialCapacity, Collection<E> collection) {
        this.elementType = checkNotNull(elementType, "ElementType must be specified");
        initialCapacity = Math.max(initialCapacity, checkNotNull(collection, "No Collection spedified!").size());
        resize(initialCapacity);
        upperIndex = 0;
        Iterator<E> iterator = collection.iterator();
        iterator.forEachRemaining(e -> elements[upperIndex++] = e);
    }

    /**
     * Create a {@code PlainList} by specifying its element type, and the Array of data to be copied.
     * @param elementType       the component type of the Array to keep the elements
     * @param values            the Array of elements to be copied to this {@code PlainList}
     */
    public PlainList(Class<? extends E> elementType, E... values){
        this.elementType = Asserts.checkNotNull(elementType, "ElementType must be specified");
        values = values != null ? values : (E[])ArrayHelper.create(elementType, 1, i -> null);

        elements = Arrays.copyOf(values, values.length);
        upperIndex = elements.length;
    }

    /**
     * Create a {@code PlainList} by default element type, and the Array of data to be copied.
     * @param values            the Array of elements to be copied to this {@code PlainList}
     */
    public PlainList(E... values){
        this((Class<? extends E>)(values == null ? Object.class : values.getClass().getComponentType()), values);
    }

    /**
     * Allocate spaces to keep the elements and updating the capacity accordingly.
     * @param capacity  the expected capacity reserved for elements keeping.
     * @return  the old Array of elements that can be used to copy data to the newly allocated spaces.
     */
    public E[] resize(int capacity){
        assertTrue(capacity >= 0, "The capacity must be greater or equal to 0");
        E[] old = elements;
        this.capacity = capacity;
        elements = (E[]) ArrayHelper.getNewArray(elementType, capacity);
        return old;
    }

    protected void ensureCapacity(int extraSize, int index) {
        E[] old = elements;
        if(extraSize + upperIndex > capacity){
            capacity = getDefaultCapacity(extraSize+ upperIndex);
            resize(capacity);
        }

        if(index < upperIndex) {
            System.arraycopy(old, index, elements, index+extraSize, upperIndex -index);
        }
        if(index > 0 && old != elements){
            System.arraycopy(old, 0, elements, 0, index);
        }
    }

    @Override
    public int size() {
        return upperIndex;
    }

    @Override
    public boolean isEmpty() {
        return upperIndex == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new PlainListIterator(this);
    }

    @Override
    public Object[] toArray() {
        return (Object[]) ArrayHelper.create(Object.class, upperIndex, i -> elements[i]);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if(a == null) {
            return (T[]) TypeHelper.copyOfRange(elements, 0, upperIndex);
        } else if (a.length < upperIndex) {
            return (T[]) ArrayHelper.create(a.getClass().getComponentType(), upperIndex, i -> elements[i]);
         } else {
            ArrayHelper.setAll(a, i -> i < upperIndex ? elements[i] : null);
            return a;
        }
    }

    @Override
    public boolean add(E e) {
        if(upperIndex >= capacity) {
            ensureCapacity(1, upperIndex);
        }
        elements[upperIndex] = e;
        upperIndex++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if(index < 0) {
            return false;
        }

        for (int i = index+1; i < upperIndex; i++) {
            elements[i-1] = elements[i];
        }
        elements[--upperIndex] = null;
        return true;
    }

    /**
     * Get the indexes where the elements matched with the given predicate.
     * @param elementPredicate  the predicate to filter elements so as to get their indexes.
     * @return  the indexes of all matched elements.
     */
    public Integer[] matchedIndexes(PredicateThrowable<E> elementPredicate){
        Integer[] indexes = IntStream.range(0, upperIndex).boxed()
            .filter(i -> elementPredicate.orElse(false).test(elements[i]))
            .toArray(size ->new Integer[size]);
        return indexes;
    }

    /**
     * Batch execution of remove(int) with zero or multiple indexes.
     * @param indexes   the indexes of all matched elements.
     * @return          the number of elements removed.
     */
    public int removeByIndexes(Integer... indexes){
        Set set = SetHelper.asSet(indexes);
        int newSize = 0, last = -1, removed = 0;
        int length = upperIndex;
        for (int i = 0; i < length; i++) {
            if(!set.contains(i)) {
                if(last != -1){
                    elements[last++] = elements[i];
                }
                newSize++;
            } else {
                removed++;
                if (last == -1) {
                    last = i;
                }
            }
        }
        for (int i = newSize; i < length; i++) {
            elements[i] = null;
        }
        upperIndex = newSize;
        return removed;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Set elementSet = SetHelper.asSet(elements);
        Set cSet = SetHelper.asSet(c);
        cSet.removeAll(elementSet);
        return cSet.isEmpty();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return addAll(upperIndex, c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if(c == null){
            return false;
        }
        assertAllFalse(index<0, index> upperIndex);
        if(c.isEmpty()){
            return false;
        }
        int extraSize = c.size();
        ensureCapacity(extraSize, index);
        Iterator<? extends E> iterator = c.iterator();
        for (int i = 0; i < extraSize; i++) {
            elements[i + index] = iterator.next();
        }
        upperIndex += extraSize;
        return true;
    }

    /**
     * Insert all elements of the given array to the specific position.
     * @param index  index at which to insert the first element from the specified collection
     * @param array  the Array containing elements to be added to this list
     * @return      true if this list changed as a result of the call
     */
    public boolean insertAll(int index, E... array) {
        array = array == null ? (E[]) ArrayHelper.create(elementType, 1, i -> null) : array;
        assertAllFalse(index<0, index> upperIndex);
        int extraSize = array.length;
        if(extraSize == 0){
            return false;
        }
        ensureCapacity(extraSize, index);
        System.arraycopy(array, 0, elements, index, extraSize);
        upperIndex += extraSize;
        return true;
    }

    /**
     * Append all elements of the given array to the end of the list.
     * @param array  the Array containing elements to be added to this list
     * @return      true if this list changed as a result of the call
     */
    public boolean appendAll(E... array) {
        array = array == null ? (E[]) ArrayHelper.create(elementType, 1, i -> null) : array;
        return insertAll(upperIndex, array);
    }

    public boolean removeAllMatched(PredicateThrowable<E> predicate){
        Integer[] indexes = matchedIndexes(predicate);
        return removeByIndexes(indexes) > 0;
    }

    public boolean retainAllMatched(PredicateThrowable<E> predicate){
        Integer[] indexes = matchedIndexes(predicate.reversed());
        return removeByIndexes(indexes) > 0;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if(c == null) {
            return false;
        } else {
            Set set = SetHelper.asSet(c);
            return removeAllMatched(set::contains);
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if(c == null) {
            return false;
        }
        Set set = SetHelper.asSet(c);
        return removeAllMatched(e -> !set.contains(e));
    }

    @Override
    public void clear() {
        resize(DEFAULT_CAPACITY);
        upperIndex = 0;
    }

    @Override
    public E get(int index) {
        assertAllFalse(index<0, index> upperIndex);
        return elements[index];
    }

    @Override
    public E set(int index, E element) {
        E old = get(index);
        elements[index] = element;
        return old;
    }

    @Override
    public void add(int index, E element) {
        assertAllFalse(index<0, index> upperIndex);
        ensureCapacity(1, index);
        elements[index] = element;
        upperIndex += 1;
    }

    @Override
    public E remove(int index) {
        E old = get(index);
        if(index < upperIndex -1) {
            System.arraycopy(elements, index+1, elements, index, upperIndex -1-index);
        }
        elements[--upperIndex] = null;
        return old;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < upperIndex; i++)
                if (elements[i]==null)
                    return i;
        } else {
            for (int i = 0; i < upperIndex; i++)
                if (o.equals(elements[i]))
                    return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = upperIndex -1; i >= 0; i--)
                if (elements[i]==null)
                    return i;
        } else {
            for (int i = upperIndex -1; i >= 0; i--)
                if (o.equals(elements[i]))
                    return i;
        }
        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new PlainListIterator(this);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        assertAllFalse(index<0, index> upperIndex);
        return new PlainListIterator(this, index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        assertAllFalse(fromIndex < 0, toIndex > upperIndex, fromIndex > toIndex);
        return new PlainList<E>(elementType, Arrays.copyOfRange(elements, fromIndex, toIndex));
    }

}
