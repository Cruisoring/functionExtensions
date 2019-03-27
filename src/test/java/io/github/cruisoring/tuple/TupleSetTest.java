package io.github.cruisoring.tuple;

import org.junit.Test;

import static org.junit.Assert.*;

public class TupleSetTest {

    @Test
    public void asArray() {
        TupleSet<Number> numbers = TupleSet.setOf(1, 2.3f, 3.07, 44, null);
        Number[] array = numbers.asArray();
        assertEquals(Number[].class, array.getClass());
    }

    @Test
    public void getSetOf() {
    }
}