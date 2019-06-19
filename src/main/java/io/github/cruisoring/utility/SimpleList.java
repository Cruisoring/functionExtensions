package io.github.cruisoring.utility;

import io.github.cruisoring.Asserts;
import io.github.cruisoring.TypeHelper;

import java.util.*;

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

    protected final Class<E> elementType;
    private int capacity;
    private E[] elements;
    private int size;

    public SimpleList(Class<E> elementType, int capacity, Iterator<E> iterator) {
        this.elementType = Asserts.checkNotNull(elementType, "ElementType must be specified");
        reset(capacity);
        size = 0;
        if(iterator != null) {
            iterator.forEachRemaining(e -> elements[size++] = e);
        }
    }

    public SimpleList(Class<E> elementType, int capacity) {
        this(elementType, capacity, null);
    }

    public SimpleList(Class<E> elementType) {
        this(elementType==null? (Class<E>) Object.class : elementType, DEFAULT_CAPACITY);
    }

    public SimpleList() {
        this(null);
    }

    public E[] reset(int capacity){
        assertTrue(capacity > 0, "The capacity must be greater than 0");
        E[] old = elements;
        this.capacity = capacity;
        elements = (E[]) ArrayHelper.getNewArray(elementType, capacity);
        return old;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) > 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new SimpleIterator(0);
    }

    @Override
    public Object[] toArray() {
        return (Object[]) ArrayHelper.create(Object.class, size, i -> elements[i]);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if(a == null) {
            return (T[]) TypeHelper.copyOfRange(elements, 0, size);
        } else if (a.length < size) {
            return (T[]) ArrayHelper.create(a.getClass().getComponentType(), size, i -> elements[i]);
         } else {
            ArrayHelper.setAll(a, i -> i < size ? elements[i] : null);
            return a;
        }
    }

    @Override
    public boolean add(E e) {
        if(size >= capacity) {
            ensureCapacity(1, size);
        }
        elements[size++] = e;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if(index < 0) {
            return false;
        }

        for (int i = index+1; i < size; i++) {
            elements[i-1] = elements[i];
        }
        elements[--size] = null;
        return true;
    }

    public int remove(boolean[] flags, boolean complement){
        int newSize = 0;
        int length = Math.min(flags.length, size);
        for (int i = 0; i < length; i++) {
            if(flags[i]==complement && newSize != i){
                elements[newSize++] = elements[i];
            }
        }
        for (int i = newSize; i < size; i++) {
            elements[i] = null;
        }
        return newSize;
    }

    public boolean[] containOrNots(Collection<?> c) {
        checkNotNull(c, "The Collection cannot be null");
        Set uniqueSet = (c instanceof Set) ? (Set) c : new HashSet(c);
        return  (boolean[]) ArrayHelper.create(boolean.class, size, i -> uniqueSet.contains(elements[i]));
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        int newSize = remove(containOrNots(c), true);
        if(newSize != size) {
            size = newSize;
            return true;
        } else {
            return false;
        }
    }

    protected void ensureCapacity(int extraSize, int index) {
        if(extraSize + size < capacity){
            return;
        }

        capacity = getDefaultCapacity(extraSize+size);
        E[] old = reset(capacity);
        if(index < size){
            System.arraycopy(old, 0, elements, 0, index);
        }
        if(index > 0) {
            System.arraycopy(old, index, elements, index+extraSize, size-index);
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return addAll(size, c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        assertAllFalse(index<0, index>=size, c == null);
        if(c.isEmpty()){
            return false;
        }
        int extraSize = c.size();
        ensureCapacity(extraSize, index);
        Iterator<? extends E> iterator = c.iterator();
        for (int i = 0; i < extraSize; i++) {
            elements[i + index] = iterator.next();
        }
        size += extraSize;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int newSize = remove(containOrNots(c), true);
        if(newSize != size) {
            size = newSize;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int newSize = remove(containOrNots(c), false);
        boolean anyChanges = newSize != size;
        size = newSize;
        return anyChanges;
    }

    @Override
    public void clear() {
        reset(DEFAULT_CAPACITY);
        size = 0;
    }

    @Override
    public E get(int index) {
        assertAllFalse(index<0, index>=size);
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
        ensureCapacity(1, index);
        elements[index] = element;
    }

    @Override
    public E remove(int index) {
        E old = get(index);
        if(index < size-1) {
            System.arraycopy(elements, index+1, elements, index, size-1-index);
        }
        elements[--size] = null;
        return old;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++)
                if (elements[i]==null)
                    return i;
        } else {
            for (int i = 0; i < size; i++)
                if (o.equals(elements[i]))
                    return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size-1; i >= 0; i--)
                if (elements[i]==null)
                    return i;
        } else {
            for (int i = size-1; i >= 0; i--)
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
        assertAllFalse(index<0, index>=size);
        return new SimpleIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    private class SimpleIterator implements ListIterator<E> {
        int cursor;
        int last=-1;

        public SimpleIterator(int index) {
            cursor = index;
        }

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            assertTrue(cursor < size, "There is no next element when cursor = %d and size = %d", cursor, size);
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
