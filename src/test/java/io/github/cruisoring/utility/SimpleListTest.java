package io.github.cruisoring.utility;

import org.junit.Test;

import java.util.Arrays;

import static io.github.cruisoring.Asserts.assertAllTrue;
import static io.github.cruisoring.Asserts.assertEquals;


public class SimpleListTest {

    @Test
    public void testConstructor() {
        SimpleList<Integer> list = new SimpleList<Integer>(Integer.class, 24, Arrays.asList(1, 2, 3, 4, 5).iterator());
        assertEquals(5, list.size());
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), list);
    }

    @Test
    public void size() {
        SimpleList<Integer> list = new SimpleList<>();
        assertEquals(0, list.size());
        assertAllTrue(list.isEmpty());

        list.add(1);
        assertEquals(1, list.get(0));

    }

    @Test
    public void isEmpty() {
    }

    @Test
    public void contains() {
    }

    @Test
    public void iterator() {
    }

    @Test
    public void toArray() {
    }

    @Test
    public void toArray1() {
    }

    @Test
    public void add() {
    }

    @Test
    public void remove() {
    }

    @Test
    public void remove1() {
    }

    @Test
    public void containOrNots() {
    }

    @Test
    public void containsAll() {
    }

    @Test
    public void ensureCapacity() {
    }

    @Test
    public void addAll() {
    }

    @Test
    public void addAll1() {
    }

    @Test
    public void removeAll() {
    }

    @Test
    public void retainAll() {
    }

    @Test
    public void clear() {
    }

    @Test
    public void get() {
    }

    @Test
    public void set() {
    }

    @Test
    public void add1() {
    }

    @Test
    public void remove2() {
    }

    @Test
    public void indexOf() {
    }

    @Test
    public void lastIndexOf() {
    }

    @Test
    public void listIterator() {
    }

    @Test
    public void listIterator1() {
    }

    @Test
    public void subList() {
    }
}