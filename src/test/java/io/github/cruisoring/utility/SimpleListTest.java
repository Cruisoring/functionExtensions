package io.github.cruisoring.utility;

import io.github.cruisoring.logger.Logger;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.github.cruisoring.Asserts.*;


public class SimpleListTest {

    @Test
    public void testTypedList_withBaiscOperations() {
        SimpleList<Integer> list = new SimpleList<Integer>(Integer.class, 24, Arrays.asList(1, 2, 3, 4, 5).iterator());
        assertEquals(5, list.size());
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), list);

        SimpleList list2 = list;
        assertException(() -> list2.add(22.0), ArrayStoreException.class);
        assertEquals(5, list.size());

        list.add(6);
        assertEquals(6, list.get(5));

        list.add(null);
        assertAllTrue(list.contains(1), list.contains(5), list.contains(null), list.get(6)==null);

        list.clear();
        assertAllTrue(list.isEmpty());
    }

    @Test
    public void iterator() {
        SimpleList<String> strings = new SimpleList<>(String.class);
        strings.appendAll("First", "b", "c", "d", "e", "f");

        Iterator<String> iterator = strings.iterator();
        if(iterator.hasNext()){
            assertLogging(() -> Logger.D(iterator.next()), "First");
        }
        StringBuilder sb = new StringBuilder();
        iterator.forEachRemaining(s ->
                sb.append(s.toUpperCase()));
        assertEquals("BCDEF",sb.toString());
    }

    @Test
    public void testToArray() {
        SimpleList<Number> numbers = new SimpleList<>(Number.class);
        numbers.addAll(Arrays.asList(1, null, 3.2f, 4.3, Short.valueOf("5"), 6L));

        Object[] array = numbers.toArray();
        assertEquals(new Number[]{1, null, 3.2f, 4.3, Short.valueOf("5"), 6L}, array);

        Number[] numberArray = numbers.toArray(null);
        assertEquals(new Number[]{1, null, 3.2f, 4.3, Short.valueOf("5"), 6L}, numberArray);

        Number[] bigger = numbers.toArray(new Number[10]);
        assertEquals(new Number[]{1, null, 3.2f, 4.3, Short.valueOf("5"), 6L, null, null, null, null}, bigger);

        numbers.remove(null);
        Comparable[] comparables = numbers.toArray(new Comparable[0]);
        assertEquals(new Comparable[]{1, 3.2f, 4.3, Short.valueOf("5"), 6L}, comparables);
    }

    @Test
    public void testAddRemove() {
        SimpleList<Number> numbers = new SimpleList<>(Number.class);
        assertException(() -> numbers.add(1, 1), IllegalStateException.class);
        numbers.addAll(Arrays.asList(1, null, 3.2f, 4.3, Short.valueOf("5"), 6L));
        assertEquals(6, numbers.size());

        numbers.add(7);
        assertException(() -> ((SimpleList)numbers).addAll(Arrays.asList("abc")), ArrayStoreException.class);
        numbers.add(1, 1L);
        assertEquals(new Number[]{1, 1L, null, 3.2f, 4.3, Short.valueOf("5"), 6L, 7}, numbers);

        numbers.add(0, 0);
        assertAllTrue(numbers.remove(1L));
        assertAllTrue(numbers.remove(Integer.valueOf(1)));
        assertAllFalse(numbers.remove(4.2));
        assertAllTrue(numbers.remove(4.3));
        assertAllFalse(numbers.remove(4.3));
        numbers.add(3, 4);
        numbers.add(2, Byte.valueOf("2"));
        assertEquals(new Number[]{0, null, Byte.valueOf("2"), 3.2f, 4, Short.valueOf("5"), 6L, 7}, numbers);

        assertAllTrue(numbers.addAll(IntStream.rangeClosed(8, 16).boxed().collect(Collectors.toList())));
        assertEquals(17, numbers.size());
        assertEquals(new Number[]{0, null, Byte.valueOf("2"), 3.2f, 4, Short.valueOf("5"), 6L, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16}, numbers);

        assertAllTrue(numbers.removeAll(IntStream.rangeClosed(1, 16).boxed().collect(Collectors.toList())));
        assertEquals(new Number[]{0, null, Byte.valueOf("2"), 3.2f, Short.valueOf("5"), 6L}, numbers);
    }

    @Test
    public void testRemoveByIndexes() {
        SimpleList<Integer> numbers = new SimpleList<>(Integer.class);
        numbers.addAll(IntStream.rangeClosed(0, 10).boxed().collect(Collectors.toList()));
        numbers.removeByIndexes(-2, 4, 0, 3, 5, 9, -1, 100, 33);
        assertEquals(new Integer[]{1, 2, 6, 7, 8, 10}, numbers);
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