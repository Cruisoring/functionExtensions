package io.github.cruisoring.utility;

import io.github.cruisoring.TypedList;
import io.github.cruisoring.throwables.PredicateThrowable;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.IntStream;

import static io.github.cruisoring.Asserts.*;

/**
 * {@code SimpleTypedList<E>} is a simple implementation of {@code TypedList<E>} to with real strong-typed element array behind.<br>
 * NOTICE: this class takes no measure to detect or thread contention!!! <b>So use it at your own risk!!!</b>
 *
 * @param <E> the generic type of the elements kept by this {@code SimpleTypedList<E>}
 */
public class SimpleTypedList<E> implements TypedList<E> {

    static final int DEFAULT_CAPACITY = 1024;
    static final int MAX_CAPACITY = 0x10000000;
    protected final Class<? extends E> elementType;
    int capacity;
    E[] elements;
    int upperIndex;
    /**
     * Create a {@code SimpleTypedList} by specifying its element type, and Collection of data to be copied.
     *
     * @param elementType         the component type of the Array to keep the elements
     * @param initialCapacity     the desirable capacity to create the initial array, so as to differ the signature with constructor using array
     * @param initValueCollection the Collection of elements to be copied to this {@code SimpleTypedList}
     */
    public SimpleTypedList(Class<? extends E> elementType, int initialCapacity, Collection<E> initValueCollection) {
        this.elementType = checkNotNull(elementType, "ElementType must be specified");
        initialCapacity = Math.max(initialCapacity, checkNotNull(initValueCollection, "No Collection spedified!").size());
        resize(initialCapacity);
        upperIndex = 0;
        Iterator<E> iterator = initValueCollection.iterator();
        iterator.forEachRemaining(e -> elements[upperIndex++] = e);
    }

    //region Constructors

    /**
     * Create a {@code SimpleTypedList} with a subset of the given values saved directly.
     *
     * @param initValues the Array of elements to be copied to this {@code SimpleTypedList}
     * @param from       the inclusive lower index boundary to be copied
     * @param to         the exclusive upper index boundary to be copied
     */
    public SimpleTypedList(E[] initValues, int from, int to) {
        assertAllFalse(initValues == null, from < 0, to > initValues.length, from > to);

        this.elementType = (Class<? extends E>) initValues.getClass().getComponentType();
        elements = Arrays.copyOfRange(initValues, from, to);
        upperIndex = elements.length;
    }

    /**
     * Create a {@code SimpleTypedList} by specifying its element type, and the Array of data to initialize.
     *
     * @param elementType the component type of the Array to keep the elements
     * @param values      the Array of elements to be copied to this {@code SimpleTypedList}
     */
    public SimpleTypedList(Class<? extends E> elementType, E... values) {
        this.elementType = checkNotNull(elementType, "ElementType must be specified");
        values = values != null ? values : (E[]) ArrayHelper.create(elementType, 1, i -> null);

        elements = Arrays.copyOf(values, values.length);
        upperIndex = elements.length;
    }

    /**
     * Create a {@code SimpleTypedList} by default element type, and the Array of data to be copied.
     *
     * @param values the Array of elements to be copied to this {@code SimpleTypedList}
     */
    public SimpleTypedList(E... values) {
        this((Class<? extends E>) (values == null ? Object.class : values.getClass().getComponentType()), values);
    }

    /**
     * Get the least reasonable figure that is multiple {@code DEFAULT_CAPACITY} to cater the demanded capacity
     *
     * @param capacity the demanded capacity
     * @return the least exponential number of {@code DEFAULT_CAPACITY}
     */
    static int getDefaultCapacity(int capacity) {
        if (capacity <= SimpleTypedList.DEFAULT_CAPACITY) {
            return SimpleTypedList.DEFAULT_CAPACITY;
        }
        assertTrue(capacity < SimpleTypedList.MAX_CAPACITY, "The desirable capacity %d is greater than MAX_CAPCITY %d", capacity, SimpleTypedList.MAX_CAPACITY);
        int result = SimpleTypedList.DEFAULT_CAPACITY;
        do {
            result *= 2;
        } while (result < capacity);
        return result;
    }
    //endregion

    protected void ensureCapacity(int extraSize, int index) {
        E[] old = elements;
        if (extraSize + upperIndex > capacity) {
            capacity = getDefaultCapacity(extraSize + upperIndex);
            resize(capacity);
        }

        if (index < upperIndex) {
            System.arraycopy(old, index, elements, index + extraSize, upperIndex - index);
        }
        if (index > 0 && old != elements) {
            System.arraycopy(old, 0, elements, 0, index);
        }
    }

    /**
     * Allocate spaces to keep the elements and updating the capacity accordingly.
     *
     * @param capacity the expected capacity reserved for elements keeping.
     * @return the old Array of elements that can be used to copy data to the newly allocated spaces.
     */
    public E[] resize(int capacity) {
        assertTrue(capacity >= 0, "The capacity must be greater or equal to 0");
        E[] old = elements;
        this.capacity = capacity;
        elements = (E[]) ArrayHelper.getNewArray(elementType, capacity);
        return old;
    }

    @Override
    public Class<? extends E> getElementType() {
        return elementType;
    }

    @Override
    public Integer[] matchedIndexes(PredicateThrowable<E> elementPredicate) {
        return IntStream.range(0, upperIndex)
                .filter(i -> elementPredicate.orElse(false).test(elements[i]))
                .boxed()
                .toArray(size -> new Integer[size]);
    }

    @Override
    public int removeByIndexes(Integer... indexes) {
        Set set = SetHelper.asSet(indexes);
        int newSize = 0;
        int last = -1;
        int removed = 0;
        int length = upperIndex;
        for (int i = 0; i < length; i++) {
            if (!set.contains(i)) {
                if (last != -1) {
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
    public boolean insertAll(int index, E... array) {
        array = array == null ? (E[]) ArrayHelper.create(elementType, 1, i -> null) : array;
        assertAllFalse(index < 0, index > upperIndex);
        int extraSize = array.length;
        if (extraSize == 0) {
            return false;
        }
        ensureCapacity(extraSize, index);
        System.arraycopy(array, 0, elements, index, extraSize);
        upperIndex += extraSize;
        return true;
    }

    @Override
    public boolean appendAll(E... array) {
        array = array == null ? (E[]) ArrayHelper.create(elementType, 1, i -> null) : array;
        return insertAll(upperIndex, array);
    }

    @Override
    public ReadOnlyList<E> asReadOnly() {
        return new ReadOnlyList<>(elements, 0, upperIndex);
    }

    //region Implementation of List interface
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
        return new TypedListIterator(this);
    }

    @Override
    public Object[] toArray() {
        return (Object[]) ArrayHelper.create(Object.class, upperIndex, i -> elements[i]);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a == null) {
            return (T[]) ArrayHelper.create(elementType, upperIndex, i -> elements[i]);
        } else if (a.length < upperIndex) {
            return (T[]) ArrayHelper.create(a.getClass().getComponentType(), upperIndex, i -> elements[i]);
        } else {
            ArrayHelper.setAll(a, i -> i < upperIndex ? elements[i] : null);
            return a;
        }
    }

    @Override
    public boolean add(E e) {
        if (upperIndex >= capacity) {
            ensureCapacity(1, upperIndex);
        }
        elements[upperIndex] = e;
        upperIndex++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index < 0) {
            return false;
        }

        int numMoved = upperIndex - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--upperIndex] = null;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Set elementSet = SetHelper.asSet(elements);
        return elementSet.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return addAll(upperIndex, c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (c == null) {
            return false;
        }
        assertAllFalse(index < 0, index > upperIndex);
        if (c.isEmpty()) {
            return false;
        }
        int extraSize = c.size();
        ensureCapacity(extraSize, index);
        Iterator<? extends E> iterator = c.iterator();
        for (int i = 0; i < extraSize; i++) {
            Array.set(elements, index + i, iterator.next());
        }
        upperIndex += extraSize;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            return false;
        } else {
            Set set = SetHelper.asSet(c);
            return removeAllMatched(set::contains);
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
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
        assertAllFalse(index < 0, index > upperIndex);
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
        assertAllFalse(index < 0, index > upperIndex);
        ensureCapacity(1, index);
        elements[index] = element;
        upperIndex += 1;
    }

    @Override
    public E remove(int index) {
        E old = get(index);
        if (index < upperIndex - 1) {
            System.arraycopy(elements, index + 1, elements, index, upperIndex - 1 - index);
        }
        elements[--upperIndex] = null;
        return old;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < upperIndex; i++)
                if (elements[i] == null)
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
            for (int i = upperIndex - 1; i >= 0; i--)
                if (elements[i] == null)
                    return i;
        } else {
            for (int i = upperIndex - 1; i >= 0; i--)
                if (o.equals(elements[i]))
                    return i;
        }
        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new TypedListIterator(this);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        assertAllFalse(index < 0, index > upperIndex);
        return new TypedListIterator(this, index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        assertAllFalse(fromIndex < 0, toIndex > upperIndex, fromIndex > toIndex);
        return new SimpleTypedList<>(elementType, Arrays.copyOfRange(elements, fromIndex, toIndex));
    }
    //endregion
}
