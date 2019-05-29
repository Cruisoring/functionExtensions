package io.github.cruisoring.utility;

import io.github.cruisoring.function.SupplierThrowable;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;

import static io.github.cruisoring.Asserts.checkNotNull;
import static io.github.cruisoring.Asserts.checkWithoutNull;

/**
 * Helper class with Set related methods implemented.
 * @see <a href="https://en.wikipedia.org/wiki/Set_theory">Set theory</a>
 */
public class SetHelper {

    //Default Set factory to be used to create new Set.
    public static Supplier<Set> defaultSetSupplier = HashSet::new;

    /**
     * Convert a given array to a Set created by the given factory method.
     * @param setSupplier   the factory method to create the Set&lt;T&gt;
     * @param elements      the Array of elements to be converted, the null would be treated as an Array containing only one single element of null.
     * @param <T>           the type of the elements.
     * @return the Set containing same elements.
     */
    public static <T> Set<T> asSet(SupplierThrowable<Set<T>> setSupplier, T... elements) {
        Set<T> set = setSupplier == null ? defaultSetSupplier.get() : setSupplier.orElse(null).get();
        checkNotNull(set, "Failed to get the instance of Set<T>");

        return fillSet(set, elements);
    }

    public static <T> Set<T> asSet(T... elements) {
        return asSet(null, elements);
    }


    static <T> Set<T> fillSet(Set<T> set, T... elements){
        if (elements == null) {
            set.add(null);
        } else {
            for (int i = 0; i < elements.length; i++) {
                set.add(elements[i]);
            }
        }
        return set;
    }

    /**
     * Convert a given array to a {code HashSet}.
     *
     * @param elements the Array of elements to be converted, the null would be treated as an Array containing only one single element of null.
     * @param <T>      the type of the elements.
     * @return the HashSet containing same elements with no order guaranteed.
     */
    public static <T> Set<T> asHashSet(T... elements) {
        return fillSet(new HashSet<>(), elements);
    }

    /**
     * Convert a given array to a {code TreeSet} to get the elements sorted.
     *
     * @param elements the Array of elements to be converted, the null would be treated as an Array containing only one single element of null.
     * @param <T>      the type of the elements.
     * @return the TreeSet containing same elements sorted.
     */
    public static <T> Set<T> asTreeSet(T... elements) {
        checkWithoutNull(elements);
        return fillSet(new TreeSet<>(), elements);
    }

    /**
     * Convert a given array to a {@code LinkedHashSet} that keep the same order as they are in the array.
     *
     * @param elements the Array of elements to be converted, the null would be treated as an Array containing only one single element of null.
     * @param <T>      the type of the elements.
     * @return the LinkedHashSet containing same set of elements with orders.
     */
    public static <T> Set<T> asLinkedHashSet(T... elements) {
        return fillSet(new LinkedHashSet<>(), elements);
    }

    /**
     * Union of the sets A, B and others if provided, denoted A ∪ B ∪ ..., is the set of all objects that are a member of A,
     * or B, or any of others. The union of {1, 2, 3} and {2, 3, 4} is the set {1, 2, 3, 4} .
     * @param setA      the first Set to get the Union
     * @param setB      the second Set to get the Union
     * @param others    optional other Sets to get the Union
     * @param <T>       the type of the elements.
     * @return          the set of all objects that are a member of A, or B, or any of others.
     */
    public static <T> Set<T> union(Set<T> setA, Set<T> setB, Set<T>... others) {
        Set<T> set = defaultSetSupplier.get();
        checkWithoutNull(set, setA, setB, others);

        set.addAll(setA);
        set.addAll(setB);
        for (int i = 0; i < others.length; i++) {
            set.addAll(others[i]);
        }
        return set;
    }

    /**
     * Intersection of the sets A, B and others if provided, denoted A ∩ B ∩ ..., is the set of all objects that are members
     * of A, B and others. The intersection of {1, 2, 3} and {2, 3, 4} is the set {2, 3}
     * @param setA      the first Set to get the Intersection
     * @param setB      the second Set to get the Intersection
     * @param others    optional other Sets to get the Intersection
     * @param <T>       the type of the elements.
     * @return          the set of all objects that are members belong to every given Sets.
     */
    public static <T> Set<T> intersection(Set<T> setA, Set<T> setB, Set<T>... others) {
        Set<T> intersection = defaultSetSupplier.get();
        checkWithoutNull(intersection, setA, setB, others);

        intersection.addAll(setA);
        intersection.retainAll(setB);
        for (int i = 0; i < others.length; i++) {
            if(intersection.isEmpty())
                return intersection;
            intersection.retainAll(others[i]);
        }
        return intersection;
    }

    /**
     * Set difference of A and B, denoted A \ B, is the set of all members of A that are not members of B.
     * The set difference {1, 2, 3} \ {2, 3, 4} is {1} , while, conversely, the set difference {2, 3, 4} \ {1, 2, 3} is {4}
     * @param setA      the first Set to get the Difference
     * @param setB      the second Set to get the Difference
     * @param <T>       the type of the elements.
     * @return          the set of all members of A that are not members of B.
     */
    public static <T> Set<T> difference(Set<T> setA, Set<T> setB){
        Set<T> set = defaultSetSupplier.get();
        checkWithoutNull(set, setA, setB);

        set.addAll(setA);
        set.removeAll(setB);
        return set;
    }

    /**
     * Symmetric difference of sets A and B, denoted A △ B or A ⊖ B, is the set of all objects that are a member
     * of exactly one of A and B (elements which are in one of the sets, but not in both).
     * For instance, for the sets {1, 2, 3} and {2, 3, 4} , the symmetric difference set is {1, 4}.
     * It is the set difference of the union and the intersection, (A ∪ B) \ (A ∩ B) or (A \ B) ∪ (B \ A).
     * @param setA      the first Set to get the Symmetric Difference
     * @param setB      the second Set to get the Symmetric Difference
     * @param <T>       the type of the elements.
     * @return          the set of all objects that are a member of either A or B, but not both.
     */
    public static <T> Set<T> symmetricDifference(Set<T> setA, Set<T> setB) {
        Set<T> set = defaultSetSupplier.get();
        checkWithoutNull(set, setA, setB);

        set.addAll(setA);
        for (T element : setB) {
            // .add() returns false if element already exists
            if (!set.add(element)) {
                set.remove(element);
            }
        }
        return set;
    }
}
