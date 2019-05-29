package io.github.cruisoring.utility;

import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static io.github.cruisoring.Asserts.*;

public class SetHelperTest {

    @Test
    public void asSet() {
        assertEquals(SetHelper.asLinkedHashSet(1, 2, 3), SetHelper.asSet(LinkedHashSet::new, 1, 2, 3));
        assertNotEquals(SetHelper.asHashSet(100, 2, 3, 5), SetHelper.asSet(LinkedHashSet::new, 100, 2, 3, 5), true);
    }

    @Test
    public void asTreeSet() {
        assertEquals(SetHelper.asLinkedHashSet(1, 2, 3), SetHelper.asTreeSet(3, 2, 1));
        assertException(() -> SetHelper.asTreeSet(1, 2, null, 3), NullPointerException.class);
    }

    @Test
    public void union() {
        Set<Integer> setA = SetHelper.asSet(1, 2, 3);
        Set<Integer> setB = SetHelper.asSet(3, 4, 5, null);
        Set<Integer> setC = SetHelper.asSet(4, 5, null);
        assertEquals(SetHelper.asLinkedHashSet(1, 2, 3, 4, 5, null), SetHelper.union(setA, setB, setC));
    }

    @Test
    public void intersection() {
        Set<Integer> setA = SetHelper.asSet(1, 2, 3, 4);
        Set<Integer> setB = SetHelper.asSet(3, 4, 5);
        Set<Integer> setC = SetHelper.asSet(3, 5, 7);
        assertEquals(SetHelper.asSet(3, 4), SetHelper.intersection(setA, setB));
        assertEquals(SetHelper.asSet(3), SetHelper.intersection(setA, setC));
        assertEquals(SetHelper.asSet(3), SetHelper.intersection(setA, setC, setB));
    }

    @Test
    public void difference() {
        Set<Integer> setA = SetHelper.asSet(1, 2, 3, 4);
        Set<Integer> setB = SetHelper.asSet(3, 4, 5);
        Set<Integer> setC = SetHelper.asSet();
        assertEquals(SetHelper.asSet(1, 2), SetHelper.difference(setA, setB));
        assertEquals(SetHelper.asSet(5), SetHelper.difference(setB, setA));
        assertEquals(SetHelper.asSet(1, 2, 3, 4), SetHelper.difference(setA, setC));
        assertEquals(SetHelper.asSet(), SetHelper.difference(setC, setA));
        assertEquals(SetHelper.asSet(), SetHelper.difference(setA, setA));
    }

    @Test
    public void symmetricDifference() {
        Set<Integer> setA = SetHelper.asSet(1, 2, 3, 4);
        Set<Integer> setB = SetHelper.asSet(3, 4, 5);
        Set<Integer> setC = SetHelper.asSet();
        assertEquals(SetHelper.asSet(1, 2, 5), SetHelper.symmetricDifference(setA, setB));
        assertEquals(SetHelper.asSet(1, 2, 5), SetHelper.symmetricDifference(setB, setA));
        assertEquals(SetHelper.asSet(1, 2, 3, 4), SetHelper.symmetricDifference(setA, setC));
        assertEquals(SetHelper.asSet(1, 2, 3, 4), SetHelper.symmetricDifference(setC, setA));
        assertEquals(SetHelper.asSet(), SetHelper.symmetricDifference(setA, setA));
    }
}