package io.github.cruisoring.utility;

import io.github.cruisoring.logger.Logger;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.github.cruisoring.Asserts.*;


public class PlainListTest {

    @Test
    public void testTypedList_withBaiscOperations() {
        PlainList<Integer> list = new PlainList<Integer>(Integer.class, 24, Arrays.asList(1, 2, 3, 4, 5));
        assertEquals(5, list.size());
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), list);

        PlainList list2 = list;
        assertException(() -> list2.add(22.0), ArrayStoreException.class);
        assertEquals(5, list.size());

        PlainList<Number> numbers = new PlainList(Number.class, 16, list);
        numbers.add(6.5);
        assertEquals(new Number[]{1, 2, 3, 4, 5, 6.5}, numbers);

        list.add(null);
        assertAllTrue(list.contains(1), list.contains(5), list.contains(null), list.get(6)==null);

        list.clear();
        assertAllTrue(list.isEmpty());
    }

    @Test
    public void testConstructors(){
        PlainList<Integer> integers = new PlainList<Integer>();
        integers.add(1);
        integers.add(0, 2);
        integers.add(0, null);
        assertEquals(new Integer[]{null, 2, 1}, integers);

        integers = new PlainList<Integer>(1, 2, 3);
        assertEquals(new int[]{1, 2, 3}, integers);

        PlainList<Comparable> comparables = new PlainList<Comparable>(Comparable.class);
        comparables.appendAll("abc", 'c', 123, null);
        assertEquals(new Object[]{"abc", 'c', 123, null}, comparables);

        PlainList<Comparable> comparables2 = new PlainList<Comparable>(Comparable.class, "abc", 'c', 123, null);
        assertEquals(new Object[]{"abc", 'c', 123, null}, comparables2);

        PlainList<String> strings = new PlainList<String>(String.class, 20, Arrays.asList("abc", null, "def"));
        assertEquals(new Object[]{"abc", null, "def"}, strings);
        assertException(() -> ((PlainList)strings).add(1, 123), ArrayStoreException.class);

        comparables = new PlainList("abc", null);
        assertAllTrue(comparables.appendAll('a', 1, 3f));
        assertEquals(new Comparable[]{"abc", null, 'a', 1, 3f}, comparables);
    }

    @Test
    public void iterator() {
        PlainList<String> strings = new PlainList<>(String.class);
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
        PlainList<Number> numbers = new PlainList<>(Number.class);
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
        PlainList<Number> numbers = new PlainList<>(Number.class);
        assertException(() -> numbers.add(1, 1), IllegalStateException.class);
        numbers.addAll(Arrays.asList(1, null, 3.2f, 4.3, Short.valueOf("5"), 6L));
        assertEquals(6, numbers.size());

        numbers.add(7);
        assertException(() -> ((PlainList)numbers).addAll(Arrays.asList("abc")), ArrayStoreException.class);
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
        PlainList<Integer> numbers = new PlainList<>(Integer.class);
        numbers.addAll(IntStream.rangeClosed(0, 10).boxed().collect(Collectors.toList()));
        numbers.removeByIndexes(-2, 4, 0, 3, 5, 9, -1, 100, 33);
        assertEquals(new Integer[]{1, 2, 6, 7, 8, 10}, numbers);
    }

    @Test
    public void containsAll() {
        PlainList<Number> numbers = new PlainList<>(Number.class);
        numbers.appendAll(1, null, 3.2f, 4.3, Short.valueOf("5"), 6L);

        assertEquals(new Object[]{1, null, 3.2f, 4.3, Short.valueOf("5"), 6L}, numbers);
        assertAllTrue(
                numbers.contains(null),
                numbers.contains(6L),
                numbers.contains(4.3),
                numbers.containsAll(SetHelper.asSet(null, 1)),
                numbers.containsAll(Arrays.asList(3.2f, 4.3, 1)),
                numbers.containsAll(Arrays.asList(1, null, 3.2f, 4.3, Short.valueOf("5"), 6L, null, 1))
        );
        assertAllFalse(
                numbers.contains(true),
                numbers.contains(5L),
                numbers.contains(4.3f),
                numbers.containsAll(SetHelper.asSet(2, null, 1)),
                numbers.containsAll(Arrays.asList(3.2f, 4.3, 1L)),
                numbers.containsAll(Arrays.asList(1, null, 3.2f, 4.3, Short.valueOf("4"), 6L, null, 1))
        );
    }

    @Test
    public void testOperatorsOfAll() {
        PlainList<Integer> integers = new PlainList<Integer>(Integer.class);
        assertAllTrue(
            integers.addAll(Arrays.asList(1, 2)),
            integers.addAll(0, Arrays.asList(-1, 0)),
            integers.appendAll(3, 4, 5),
            integers.insertAll(4, null),
            integers.appendAll(null)
        );
        assertEquals(new Integer[]{-1, 0, 1, 2, null, 3, 4, 5, null}, integers);

        assertAllFalse(
            integers.retainAll(null),
            integers.removeAll(null),
            integers.addAll(null),
            integers.addAll(0, null),
            integers.removeAll(null)
        );
        assertEquals(new Integer[]{-1, 0, 1, 2, null, 3, 4, 5, null}, integers);

        assertAllTrue(
            integers.removeAllMatched(i -> i == null),
            integers.removeAll(Arrays.asList(3, 7, 9, 0))
        );
        assertEquals(new Integer[]{-1, 1, 2, 4, 5}, integers);

        assertAllTrue(
            integers.retainAllMatched(i -> i % 2 != 0)
        );
        assertEquals(new Integer[]{-1, 1, 5}, integers);

        assertAllFalse(
            integers.retainAllMatched(i -> i < 10),
            integers.removeAll(Arrays.asList(-2, 4, 6))
        );

        assertAllTrue(
            integers.removeAll(Arrays.asList(1, 3, 9, 10, null)),
            integers.appendAll(7, 10, 15, 13, 5),
            integers.retainAll(Arrays.asList(-1, -2, 15, 5, null))
        );
        assertEquals(new Integer[]{-1, 5, 15, 5}, integers);
    }

    @Test
    public void testOperatorsOnSingleElement() {
        PlainList<Integer> integers = new PlainList<Integer>(0, 1, 2, 3, 4);
        assertAllTrue(
            integers.size() == 5,
            integers.contains(3),
            integers.containsAll(SetHelper.asSet(1, 2, 3, 4)),
            integers.get(1).equals(1),
            integers.set(3, -3).equals(3),
            integers.add(5),
            integers.addAll(SetHelper.asLinkedHashSet(null, 7)),
            integers.contains(null),
            integers.containsAll(SetHelper.asSet(-3, null, 7)),
            integers.indexOf(null)==6,
            integers.remove(7).equals(7),
            integers.add(null),
            !integers.contains(7),
            integers.lastIndexOf(null) == 7,
            integers.set(1, null).equals(1),
            integers.indexOf(null) == 1
        );
    }

    @Test
    public void testListIterator() {
        PlainList<Integer> integers = new PlainList<Integer>(0, 1, 2, 3, 4, 5);
        ListIterator<Integer> iterator = integers.listIterator();
        Set<Integer> set = new HashSet<>();
        iterator.forEachRemaining(i -> set.add(i));
        assertEquals(new Integer[]{0, 1, 2, 3, 4, 5}, set);

        set.clear();
        integers.listIterator(3).forEachRemaining(e -> set.add(e));
        assertEquals(new Integer[]{3, 4, 5}, set);
    }

    @Test
    public void testSubList() {
        PlainList<Integer> integers = new PlainList<Integer>(0, 1, 2, 3, 4, 5);
        assertEquals(new int[0], integers.subList(0, 0));
        assertEquals(new int[0], integers.subList(2, 2));
        assertEquals(new int[]{0}, integers.subList(0, 1));
        assertEquals(new int[]{0, 1}, integers.subList(0, 2));
        assertEquals(new int[]{0, 1, 2, 3, 4, 5}, integers.subList(0, 6));
        assertEquals(new int[]{}, integers.subList(4, 4));
        assertEquals(new int[]{4}, integers.subList(4, 5));
        assertEquals(new int[]{4, 5}, integers.subList(4, 6));

        assertException(() -> integers.subList(-1, 0), IllegalStateException.class);
        assertException(() -> integers.subList(7, 7), IllegalStateException.class);
        assertException(() -> integers.subList(0, 7), IllegalStateException.class);
        assertException(() -> integers.subList(4, 3), IllegalStateException.class);
    }
}