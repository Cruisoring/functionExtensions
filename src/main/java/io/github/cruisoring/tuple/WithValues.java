package io.github.cruisoring.tuple;

import io.github.cruisoring.TypeHelper;
import io.github.cruisoring.function.PredicateThrowable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static io.github.cruisoring.Asserts.checkWithoutNull;

/**
 * Generic interface to define the expected behaviours of {@code Tuple}
 *
 * @param <T> the generic type of the concerned {@code WithValues}
 */
public interface WithValues<T> extends Comparable {
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

    /**
     * Returns this as an instance of {@code WithValues} that can be overriden by extended interface/class.
     * @return the {@code WithValues} instance represented by this.
     */
    default WithValues getValues(){
        return this;
    }

    /**
     * Get the set of this.hashCode() and all its elements' hashCodes as signatures.
     *
     * @return the hashCodes of this and its elements as a Set1.
     */
    default Set<Integer> getSignatures() {
        Set<Integer> hashCodes = new HashSet<>();
        hashCodes.add(hashCode());
        int len = getLength();
        for (int i = 0; i < len; i++) {
            T element = getValue(i);
            hashCodes.add(element == null ? 0 : element.hashCode());
        }
        return hashCodes;
    }

    /**
     * With concerned set of values at given positions, check if this {@code WithValues} have matched values.
     *
     * @param expectedValues the expected element values at specific positions.
     * @return      <code>true</code> if element values at the concerned positions are matched, otherwise <code>false</code>
     */
    default boolean isMatched(Map<Integer, Object> expectedValues){
        for (Integer index : expectedValues.keySet()) {
            if(!TypeHelper.valueEquals(expectedValues.get(index), getValue(index))){
                return false;
            }
        }
        return true;
    }

    /**
     * With predicates for values at concerned positions, check if this {@code WithValues} meet the expectations.
     *
     * @param expectedConditions the predicates for values at concerned positions
     * @return      <code>true</code> if element values at the concerned positions meet all conditions, otherwise <code>false</code>
     */
    default boolean meetConditions(Map<Integer, PredicateThrowable> expectedConditions){
        for (Integer index : expectedConditions.keySet()) {
            if(!expectedConditions.get(index).orElse().test(getValue(index))){
                return false;
            }
        }
        return true;
    }

    /**
     * Returns whether any elements of this {@code WithValues} match the provided
     * predicate.  May not evaluate the predicate on all elements if not
     * necessary for determining the result.  If the {@code WithValues} is empty then
     * {@code false} is returned and the predicate is not evaluated.
     * @param predicateThrowable    predicate to apply to elements of this stream
     * @return      {@code true} if any elements of the stream match the provided
     *          predicate, otherwise {@code false}
     */
    default boolean anyMatch(PredicateThrowable<T> predicateThrowable){
        checkWithoutNull(predicateThrowable);

        int len = getLength();
        try {
            for (int i = 0; i < len; i++) {
                if (predicateThrowable.test(getValue(i))){
                    return true;
                }
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * Returns whether all elements of this WithValues match the provided predicate.
     * May not evaluate the predicate on all elements if not necessary for determining the result.
     * @param predicateThrowable    {@code PredicateThrowable<T>} to apply to elements of this {@code WithValues}
     * @return  {@code true} if either all elements of the {@code WithValues} match the
     *  provided predicate or there is no element, otherwise {@code false}
     */
    default boolean allMatch(PredicateThrowable<T> predicateThrowable){
        checkWithoutNull(predicateThrowable);

        int len = getLength();
        try {
            for (int i = 0; i < len; i++) {
                if (!predicateThrowable.test(getValue(i))){
                    return false;
                }
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * Returns whether none elements of this WithValues match the provided predicate.
     * May not evaluate the predicate on all elements if not necessary for determining the result.
     * @param predicateThrowable    {@code PredicateThrowable<T>} to apply to elements of this {@code WithValues}
     * @return  {@code true} if none elements of the {@code WithValues} match the
     *  provided predicate or there is no element, otherwise {@code false}
     */
    default boolean noneMatch(PredicateThrowable<T> predicateThrowable){
        checkWithoutNull(predicateThrowable);

        int len = getLength();
        try {
            for (int i = 0; i < len; i++) {
                if (predicateThrowable.test(getValue(i))){
                    return false;
                }
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    default int compareTo(Object o){
        return o == null ? hashCode() : hashCode() - o.hashCode();
    }
}
