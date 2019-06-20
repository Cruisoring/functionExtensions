package io.github.cruisoring.utility;

import io.github.cruisoring.Asserts;
import io.github.cruisoring.TypeHelper;
import io.github.cruisoring.throwables.PredicateThrowable;

import java.util.*;
import java.util.stream.IntStream;

import static io.github.cruisoring.Asserts.*;

public class SimpleList<E> implements List<E> {

    public static int DEFAULT_CAPACITY = 16;
    public static int MAX_CAPACITY = 0x2000000;

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
    private int capacity;
    private E[] elements;
    private int current;

    public SimpleList(Class<? extends E> elementType, int initialCapacity, Iterator<E> iterator) {
        this.elementType = Asserts.checkNotNull(elementType, "ElementType must be specified");
        resize(initialCapacity);
        current = 0;
        if(iterator != null) {
            iterator.forEachRemaining(e -> elements[current++] = e);
        }
    }

    public SimpleList(Class<? extends E> elementType, int initialCapacity) {
        this(elementType, initialCapacity, null);
    }

    public SimpleList(Class<? extends E> elementType) {
        this(elementType==null? (Class<E>) Object.class : elementType, DEFAULT_CAPACITY);
    }

    public SimpleList() {
        this(null);
    }

    /**
     * Allocate spaces to keep the elements and updating the capacity accordingly.
     * @param capacity  the expected capacity reserved for elements keeping.
     * @return  the old Array of elements that can be used to copy data to the newly allocated spaces.
     */
    public E[] resize(int capacity){
        assertTrue(capacity > 0, "The capacity must be greater than 0");
        E[] old = elements;
        this.capacity = capacity;
        elements = (E[]) ArrayHelper.getNewArray(elementType, capacity);
        return old;
    }

    protected void ensureCapacity(int extraSize, int index) {
        E[] old = elements;
        if(extraSize + current > capacity){
            capacity = getDefaultCapacity(extraSize+ current);
            resize(capacity);
        }

        if(index < current) {
            System.arraycopy(old, index, elements, index+extraSize, current -index);
        }
        if(index > 0 && old != elements){
            System.arraycopy(old, 0, elements, 0, index);
        }
    }

    @Override
    public int size() {
        return current;
    }

    @Override
    public boolean isEmpty() {
        return current == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new SimpleIterator(0);
    }

    @Override
    public Object[] toArray() {
        return (Object[]) ArrayHelper.create(Object.class, current, i -> elements[i]);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if(a == null) {
            return (T[]) TypeHelper.copyOfRange(elements, 0, current);
        } else if (a.length < current) {
            return (T[]) ArrayHelper.create(a.getClass().getComponentType(), current, i -> elements[i]);
         } else {
            ArrayHelper.setAll(a, i -> i < current ? elements[i] : null);
            return a;
        }
    }

    @Override
    public boolean add(E e) {
        if(current >= capacity) {
            ensureCapacity(1, current);
        }
        elements[current] = e;
        current++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if(index < 0) {
            return false;
        }

        for (int i = index+1; i < current; i++) {
            elements[i-1] = elements[i];
        }
        elements[--current] = null;
        return true;
    }

    /**
     * Get the indexes where the elements matched with the given predicate.
     * @param elementPredicate  the predicate to filter elements so as to get their indexes.
     * @return  the indexes of all matched elements.
     */
    public Integer[] matchedIndexes(PredicateThrowable<E> elementPredicate){
        Integer[] indexes = IntStream.range(0, current).boxed()
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
        int length = current;
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
        current = newSize;
        return removed;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Set elementSet = SetHelper.asSet(c);
        Set cSet = SetHelper.asSet(c);
        cSet.removeAll(elementSet);
        return cSet.isEmpty();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return addAll(current, c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        assertAllFalse(index<0, index> current, c == null);
        if(c.isEmpty()){
            return false;
        }
        int extraSize = c.size();
        ensureCapacity(extraSize, index);
        Iterator<? extends E> iterator = c.iterator();
        for (int i = 0; i < extraSize; i++) {
            elements[i + index] = iterator.next();
        }
        current += extraSize;
        return true;
    }

    /**
     * Insert all elements of the given array to the specific position.
     * @param index  index at which to insert the first element from the specified collection
     * @param array  the Array containing elements to be added to this list
     * @return      true if this list changed as a result of the call
     */
    public boolean insertAll(int index, E... array) {
        assertAllFalse(index<0, index> current, array == null);
        int extraSize = array.length;
        if(extraSize == 0){
            return false;
        }
        ensureCapacity(extraSize, index);
        System.arraycopy(array, 0, elements, index, extraSize);
        current += extraSize;
        return true;
    }

    /**
     * Append all elements of the given array to the end of the list.
     * @param array  the Array containing elements to be added to this list
     * @return      true if this list changed as a result of the call
     */
    public boolean appendAll(E... array) {
        assertAllFalse(array == null);
        return insertAll(current, array);
    }

    public boolean removeAll(PredicateThrowable<E> predicate){
        Integer[] indexes = matchedIndexes(predicate);
        return removeByIndexes(indexes) > 0;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if(c == null) {
            return removeAll(e -> e == null);
        } else {
            Set set = SetHelper.asSet(c);
            return removeAll(set::contains);
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if(c == null) {
            return removeAll(e -> e != null);
        }
        Set set = SetHelper.asSet(c);
        return removeAll(e -> !set.contains(e));
    }

    @Override
    public void clear() {
        resize(DEFAULT_CAPACITY);
        current = 0;
    }

    @Override
    public E get(int index) {
        assertAllFalse(index<0, index> current);
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
        assertAllFalse(index<0, index> current);
        ensureCapacity(1, index);
        elements[index] = element;
        current += 1;
    }

    @Override
    public E remove(int index) {
        E old = get(index);
        if(index < current -1) {
            System.arraycopy(elements, index+1, elements, index, current -1-index);
        }
        elements[--current] = null;
        return old;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < current; i++)
                if (elements[i]==null)
                    return i;
        } else {
            for (int i = 0; i < current; i++)
                if (o.equals(elements[i]))
                    return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = current -1; i >= 0; i--)
                if (elements[i]==null)
                    return i;
        } else {
            for (int i = current -1; i >= 0; i--)
                if (o.equals(elements[i]))
                    return i;
        }
        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new SimpleIterator(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        assertAllFalse(index<0, index> current);
        return new SimpleIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    private class SimpleIterator implements ListIterator<E> {
        int cursor;
        int last=-1;

        public SimpleIterator(int index) {
            cursor = index;
        }

        @Override
        public boolean hasNext() {
            return cursor < current;
        }

        @Override
        public E next() {
            assertTrue(cursor < current, "There is no next element when cursor = %d and current = %d", cursor, current);
            return elements[last = cursor++];
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        public E previous() {
            assertTrue(cursor > 0, "There is no previous element when curor = %d", cursor);
            return elements[last = --cursor];
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor-1;
        }

        @Override
        public void remove() {
            assertFalse(last < 0, "The next() has not been called when last=%d and cursor=%d.", last, cursor);
            SimpleList.this.remove(last);
            cursor = last;
            last = -1;
        }

        @Override
        public void set(E e) {
            assertFalse(last < 0, "The next() has not been called when last=%d and cursor=%d.", last, cursor);
            SimpleList.this.set(last, e);
        }

        @Override
        public void add(E e) {
            SimpleList.this.add(cursor++, e);
            last = -1;
        }
    }

}
