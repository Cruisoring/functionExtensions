package io.github.cruisoring.utility;

import io.github.cruisoring.throwables.PredicateThrowable;

import java.util.*;

import static io.github.cruisoring.Asserts.*;

public class ReadOnlyList<E> extends PlainList<E> {

    public ReadOnlyList(Class<? extends E> elementType, Collection<E> collection) {
        super(elementType, collection.size(), collection);
    }

    public ReadOnlyList(Class<? extends E> elementType, E... values) {
        super(elementType, values);
    }

    public ReadOnlyList(E... elements) {
        super(elements);
    }

    @Override
    public E[] resize(int capacity) {
        return null;
    }

    @Override
    public boolean add(E e) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public int removeByIndexes(Integer... indexes) {
        return 0;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean insertAll(int index, E... array) {
        return false;
    }

    @Override
    public boolean appendAll(E... array) {
        return false;
    }

    @Override
    public boolean removeAllMatched(PredicateThrowable<E> predicate) {
        return false;
    }

    @Override
    public boolean retainAllMatched(PredicateThrowable<E> predicate) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
    }

    @Override
    public void add(int index, E element) {
    }

    @Override
    public E remove(int index) {
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        return new ReadOnlyListIterator();
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ReadOnlyListIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ReadOnlyListIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        assertAllFalse(fromIndex < 0, toIndex > upperIndex, fromIndex > toIndex);
        return new ReadOnlyList<E>(elementType, Arrays.copyOfRange(elements, fromIndex, toIndex));
    }

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
            return elements[lastReturned = cursor++];
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
            return elements[lastReturned = --cursor];
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
