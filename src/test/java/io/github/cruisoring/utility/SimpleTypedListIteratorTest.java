package io.github.cruisoring.utility;

import io.github.cruisoring.TypeHelper;
import org.junit.Test;

import static io.github.cruisoring.Asserts.*;

public class SimpleTypedListIteratorTest {

    @Test
    public void testListIterator() {
        //*/
        SimpleTypedList<Integer> integers = new SimpleTypedList<Integer>(0, 1, 2, 3, 4);
        TypedListIterator<Integer> iterator = new TypedListIterator<>(integers);
        /*/
        ArrayList<Integer> integers = new ArrayList<>();
        integers.addAll(Arrays.asList(0, 1, 2, 3, 4));
        ListIterator iterator = integers.listIterator();
        //*/
        assertAllFalse(
                iterator.hasPrevious(),
                !iterator.hasNext()
        );
        assertException(() -> iterator.set(9), IllegalStateException.class);
        assertAllTrue(
                iterator.next().equals(0),
                iterator.nextIndex() == 1,
                iterator.previousIndex() == 0,
                iterator.hasPrevious(),
                iterator.previous().equals(0),
                !iterator.hasPrevious(),
                iterator.next().equals(0),
                iterator.next().equals(1),
                iterator.next().equals(2)
        );
        iterator.add(null);
        assertException(() -> iterator.set(-2), IllegalStateException.class);
        assertAllTrue(
                integers.get(3) == null,
                iterator.previous() == null,
                iterator.next() == null,
                iterator.next().equals(3)
        );
        iterator.set(-3);
        iterator.add(5);
        assertException(() -> iterator.remove(), IllegalStateException.class);
        iterator.add(6);
        assertAllTrue(
                TypeHelper.valueEquals(new Integer[]{0, 1, 2, null, -3, 5, 6, 4}, integers),
                iterator.next().equals(4),
                !iterator.hasNext()
        );
        iterator.add(9);
        assertAllTrue(
                iterator.previous().equals(9),
                iterator.previous().equals(4)
        );
        iterator.set(-4);

        assertEquals(new Integer[]{0, 1, 2, null, -3, 5, 6, -4, 9}, integers);

    }

}