package io.github.cruisoring.utility;

import io.github.cruisoring.TypedList;

import java.util.*;
import java.util.function.Supplier;

import static io.github.cruisoring.Asserts.*;

public class ReadOnlyList<E> implements TypedList<E> {

    static final String UNSUPPORTED = "Writing operation is not supported by ReadOnlyList<E>";

    protected final Class<? extends E> elementType;
    protected final E[] elements;
    protected final int upperIndex;


    public ReadOnlyList(Class<? extends E> elementType, Collection<E> collection) {
        this.elementType = checkNotNull(elementType, "ElementType must be specified");
        upperIndex = checkNotNull(collection, "No Collection spedified!").size();
        final Iterator<E> iterator = collection.iterator();
        elements = (E[]) ArrayHelper.create(elementType, upperIndex, i -> iterator.next());
    }

    public ReadOnlyList(Class<? extends E> elementType, E... values) {
        this.elementType = checkNotNull(elementType, "ElementType must be specified");
        if (values == null) {
            upperIndex = 1;
            elements = (E[]) ArrayHelper.getNewArray(elementType, 1);
        } else {
            upperIndex = values.length;
            elements = Arrays.copyOf(values, values.length);
        }
    }

    public ReadOnlyList(E... values) {
        this((Class<? extends E>) (values == null ? Object.class : values.getClass().getComponentType()), values);
    }

    public ReadOnlyList(E[] values, int from, int to){
        assertAllFalse(values == null, from < 0, to > values.length, from > to);

        this.elementType = (Class<? extends E>) values.getClass().getComponentType();
        elements = Arrays.copyOfRange(values, from, to);
        upperIndex = elements.length;
    }

    public ReadOnlyList(Supplier<E[]> initValueSupplier){
        assertNotNull(initValueSupplier, "The supplier of init values must be specified");
        elements = initValueSupplier.get();
        this.elementType = (Class<? extends E>) elements.getClass().getComponentType();
        upperIndex = elements.length;
    }

    @Override
    public Class<? extends E> getElementType() {
        return elementType;
    }

    @Override
    public int removeByIndexes(Integer... indexes) {
        throw new UnsupportedOperationException(UNSUPPORTED);
    }

    @Override
    public boolean insertAll(int index, E... array) {
        throw new UnsupportedOperationException(UNSUPPORTED);
    }

    @Override
    public boolean appendAll(E... array) {
        throw new UnsupportedOperationException(UNSUPPORTED);
    }

    @Override
    public ReadOnlyList<E> asReadOnly() {
        return this;
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
        return new ReadOnlyListIterator();
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
        throw new UnsupportedOperationException(UNSUPPORTED);
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException(UNSUPPORTED);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Set elementSet = SetHelper.asSet(elements);
        return elementSet.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException(UNSUPPORTED);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException(UNSUPPORTED);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException(UNSUPPORTED);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException(UNSUPPORTED);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException(UNSUPPORTED);
    }

    @Override
    public E get(int index) {
        assertAllFalse(index < 0, index > upperIndex);
        return elements[index];
    }

    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException(UNSUPPORTED);
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException(UNSUPPORTED);
    }

    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException(UNSUPPORTED);
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
        return new ReadOnlyListIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        assertAllFalse(index < 0, index > upperIndex);
        return new ReadOnlyListIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        assertAllFalse(fromIndex < 0, toIndex > upperIndex, fromIndex > toIndex);
        return new ReadOnlyList<>(elementType, Arrays.copyOfRange(elements, fromIndex, toIndex));
    }
    //endregion


    public class ReadOnlyListIterator implements ListIterator<E> {
        //Specify the fromIndexInclusive and toIndexExclusive of this ReadOnlyListIterator
        int fromInclusive, toExclusive;
        int cursor;
        int lastReturned = -1;

        /**
         * Create a readonly ListIterator that have conventional behaviour upon this {@code ReadOnlyList}
         */
        public ReadOnlyListIterator() {
            this(0);
        }

        /**
         * Create a readonly ListIterator with starting position in this {@code ReadOnlyList}
         * @param position the starting position in this {@code ReadOnlyList}
         */
        public ReadOnlyListIterator(int position) {
            this(position, 0, upperIndex);
        }

        /**
         * Create a readonly ListIterator with starting position to iterate a part of this {@code ReadOnlyList}
         * @param position the starting position in this {@code ReadOnlyList}
         * @param from  the inclusive index can be iterated by this ListIterator.
         * @param to    the exclusive index or upper boundary of the list to be iterated by this ListIterator.
         */
        public ReadOnlyListIterator(int position, int from, int to) {
            assertAllFalse(from < 0, to > upperIndex, from > to, position < from, position > to);
            fromInclusive = from;
            toExclusive = to;
            cursor = fromInclusive;
        }

        @Override
        public boolean hasNext() {
            return cursor < toExclusive;
        }

        @Override
        public E next() {
            if(cursor >= toExclusive) {
                throw new NoSuchElementException(StringHelper.tryFormatString(
                        "No next element when cursor(%d) >= toExclusive(%d)", cursor, toExclusive));
            }
            lastReturned = cursor++;
            return elements[lastReturned];
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public boolean hasPrevious() {
            return cursor > fromInclusive;
        }

        @Override
        public E previous() {
            if(cursor <= fromInclusive) {
                throw new NoSuchElementException(StringHelper.tryFormatString(
                        "No previous element when cursor(%d) <= fromInclusive(%d)", cursor, fromInclusive));
            }
            lastReturned = --cursor;
            return elements[lastReturned];
        }

        @Override
        public int previousIndex() {
            return cursor-1;
        }

        @Override
        public void add(E e) {
            fail("No adding support of ReadOnlyListIterator");
        }

        @Override
        public void set(E e) {
            fail("No setting support of ReadOnlyListIterator");
        }

        @Override
        public void remove() {
            fail("No removing support of ReadOnlyListIterator");
        }
    }
}
