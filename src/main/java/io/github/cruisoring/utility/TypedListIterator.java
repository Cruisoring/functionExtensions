package io.github.cruisoring.utility;


import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import static io.github.cruisoring.Asserts.assertAllFalse;
import static io.github.cruisoring.Asserts.checkNotNull;

/**
 * A simplified ListIterator with lower and upper boundary specified upon a {@code List<E>} that is assumed not accessed by other thread.
 *
 * @param <E> the type of the elements
 */
public class TypedListIterator<E> implements ListIterator<E> {
    static final String LIST_CANNOT_BE_NULL = "The list instance must be specified.";

    final List<E> list;
    //Specify the fromIndexInclusive and toIndexExclusive of this TypedListIterator
    int fromInclusive;
    int toExclusive;
    int cursor;
    int lastReturned = -1;

    /**
     * Create a ListIterator that have conventional behaviour upon the given List.
     *
     * @param list the {@code List<E>} instance to be iterated.
     */
    public TypedListIterator(List<E> list) {
        this(checkNotNull(list, LIST_CANNOT_BE_NULL), 0);
    }

    /**
     * Create a ListIterator that have conventional behaviour upon the given List and with starting position in the list specified.
     *
     * @param list     the {@code List<E>} instance to be iterated.
     * @param position the starting position in the list.
     */
    public TypedListIterator(List<E> list, int position) {
        this(checkNotNull(list, LIST_CANNOT_BE_NULL), position, 0, list.size());
    }

    /**
     * Create a ListIterator that iterate the whole or part of the given List and with starting position within that scope specified.
     *
     * @param list     the {@code List<E>} instance to be iterated.
     * @param position the starting position in the list.
     * @param from     the inclusive index can be iterated by this ListIterator.
     * @param to       the exclusive index or upper boundary of the list to be iterated by this ListIterator.
     */
    public TypedListIterator(List<E> list, int position, int from, int to) {
        this.list = checkNotNull(list, LIST_CANNOT_BE_NULL);
        assertAllFalse(from < 0, to > list.size(), from > to, position < from, position > to);
        fromInclusive = from;
        toExclusive = to;
        cursor = position;
    }

    @Override
    public boolean hasNext() {
        return cursor < toExclusive;
    }

    @Override
    public E next() {
        if (cursor >= toExclusive) {
            throw new NoSuchElementException(StringHelper.tryFormatString(
                    "No next element when cursor(%d) >= toExclusive(%d)", cursor, toExclusive));
        }
        lastReturned = cursor++;
        return list.get(lastReturned);
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
        if (cursor <= fromInclusive) {
            throw new NoSuchElementException(StringHelper.tryFormatString(
                    "No previous element when cursor(%d) <= fromInclusive(%d)", cursor, fromInclusive));
        }
        lastReturned = --cursor;
        return list.get(lastReturned);
    }

    @Override
    public int previousIndex() {
        return cursor - 1;
    }

    @Override
    public void add(E e) {
        list.add(cursor++, e);
        lastReturned = -1;
        toExclusive++;
    }

    @Override
    public void set(E e) {
        if (lastReturned < 0) {
            throw new IllegalStateException("No element returned yet.");
        }

        list.set(lastReturned, e);
    }

    @Override
    public void remove() {
        if (lastReturned < 0) {
            throw new IllegalStateException("No element returned yet.");
        }
        cursor = lastReturned;
        list.remove(cursor);
        lastReturned = -1;
        toExclusive--;
    }
}

