package io.github.cruisoring;

import io.github.cruisoring.throwables.PredicateThrowable;
import io.github.cruisoring.utility.ReadOnlyList;

import java.util.List;
import java.util.stream.IntStream;

import static io.github.cruisoring.Asserts.assertNotNull;

/**
 * Instead of using {@code Object[]} to keep the element values thus need to box/un-box for read/write operators,
 * the {@code TypedList<E>} would retain the type of the elements supplied either explicitly or implicitly by the compiler,
 * and use it to create the corresponding array fro CRUD operations of the elements, along with some new methods.
 *
 * @param <E> the type of the elements saved by this {@code TypeList<E>}
 */
public interface TypedList<E> extends List<E> {

    /**
     * Get the componentType of the underlying array to keep data.
     *
     * @return the componentType of the element array created to perform element CRUD.
     */
    Class<? extends E> getElementType();

    /**
     * Batch execution of remove(int) with zero or multiple indexes.
     *
     * @param indexes the indexes of all matched elements.
     * @return the number of elements removed.
     */
    int removeByIndexes(Integer... indexes);

    /**
     * Insert all elements of the given array to the specific position.
     *
     * @param index index at which to insert the first element from the specified collection
     * @param array the Array containing elements to be added to this list
     * @return true if this list changed as a result of the call
     */
    boolean insertAll(int index, E... array);

    /**
     * Append all elements of the given array to the end of the list.
     *
     * @param array the Array containing elements to be added to this list
     * @return true if this list changed as a result of the call
     */
    boolean appendAll(E... array);

    /**
     * Create a {@code ReadOnlyList<E>} if needed with all existing elements for reading only.
     *
     * @return a {@code ReadOnlyList<E>} with all existing elements.
     */
    ReadOnlyList<E> asReadOnly();

    /**
     * Loop through the existing elements to find and return indexes of those matching with the given {@code elementPredicate} as an Array.
     *
     * @param elementPredicate the Predicate to evaluate if the element matched expected condition or not.
     * @return the indexes of elements matching with the given {@code elementPredicate} as an Array.
     */
    default Integer[] matchedIndexes(PredicateThrowable<E> elementPredicate) {
        assertNotNull(elementPredicate, "The elementPredicate must be supplied.");

        Integer[] indexes = IntStream.range(0, size())
                .filter(i -> elementPredicate.orElse(false).test(get(i))).boxed()
                .toArray(size -> new Integer[size]);
        return indexes;
    }

    /**
     * Remove all elements matching with the given {@code elementPredicate}.
     *
     * @param predicate the Predicate to evaluate if the element matched expected condition or not.
     * @return <tt>true</tt> if this collection changed as a result of the call
     */
    default boolean removeAllMatched(PredicateThrowable<E> predicate) {
        Integer[] indexes = matchedIndexes(predicate);
        return removeByIndexes(indexes) > 0;
    }

    /**
     * Retain all elements matching with the given {@code elementPredicate}.
     *
     * @param predicate the Predicate to evaluate if the element matched expected condition or not.
     * @return <tt>true</tt> if this collection changed as a result of the call
     */
    default boolean retainAllMatched(PredicateThrowable<E> predicate) {
        Integer[] indexes = matchedIndexes(predicate.reversed());
        return removeByIndexes(indexes) > 0;
    }

    /**
     * Returns a view of the portion of this list within the spcified {@code Range}
     *
     * @param range the {@code Range} used to specify fromIndex, inclusive, and toIndex, exclusive
     * @return a view of the specified range within this list
     */
    default List<E> subList(Range range) {
        assertNotNull(range, "Range must be used to specify the fromIndex and toIndex to get subList");
        return subList(range.getStartInclusive(), range.getEndExclusive());
    }
}
