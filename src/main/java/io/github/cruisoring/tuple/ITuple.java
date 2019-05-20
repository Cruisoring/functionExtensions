package io.github.cruisoring.tuple;

import java.util.function.Predicate;

/**
 * Interface to specify how {@code Tuple} shall behave.
 * @param <T>   Generic type of the values held by the {@code ITuple}
 */
public interface ITuple<T extends Object> extends AutoCloseable, WithValues<T> {
    /**
     * Get all Non-null elements matching the given class as an immutable <tt>Tuple</tt>
     *
     * @param clazz Class to evaluate the saved values.
     * @param <U>   Type of the given Class.
     * @return Immutable <code>Tuple</code> containing matched elements.
     */
    <U> Tuple<U> getSetOf(Class<U> clazz);

    /**
     * Get all Non-null elements of the given class and matched with predefined criteria as an immutable <tt>Tuple</tt>
     *
     * @param clazz          Class to evaluate the saved values.
     * @param valuePredicate predicate to filter elements by their values
     * @param <S>            Type of the given Class.
     * @return Immutable <code>Tuple</code> containing matched elements.
     */
    <S> Tuple<S> getSetOf(Class<S> clazz, Predicate<S> valuePredicate);

    /**
     * Return an array of int[] to get the node type and indexes to access EVERY node elements of the concerned object.
     *
     * @return An array of int[]. Each node element is mapped to one int[] where the last int shows the type of the
     * value of the node:
     * NULL_NODE when it is null, EMPTY_ARRAY_NODE when it is an array of 0 length and otherwise NORMAL_VALUE_NODE.
     * The other int values of the int[] are indexes of the node element or its parent array in their container arrays.
     */
    int[][] getDeepIndexes();
}
