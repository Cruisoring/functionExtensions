package io.github.cruisoring;

import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.throwables.SupplierThrowable;
import io.github.cruisoring.utility.DateTimeHelper;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.cruisoring.Asserts.*;

public class FunctionsTest {


    @Test
    public void runParallel() throws Exception {
        int[] array = new int[5];
        Range range = Range.ofLength(array.length);

        Functions.runParallel(i -> {
            Functions.sleep(2);
            array[-i] = i*2;
        }, Range.ofLength(20).getStream(), 1);

        Functions.runParallel(i -> array[i] = i*2, range.getStream(), 10);
        assertEquals(new int[]{0, 2, 4, 6, 8}, array);
    }

    @Test
    public void testUntil() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plus(Duration.ofMillis(500));
        Logger.D("start: %s, end: %s", DateTimeHelper.asString(start, "mm:ss.SSS"), DateTimeHelper.asString(end, "mm:ss.SSS"));

        //Timeout before predicate returns true
        AtomicInteger count = new AtomicInteger();
        assertFalse( Functions.testUntil(
                () -> LocalDateTime.now().isAfter(end),
                55,
                () -> Logger.D("count=%d", count.incrementAndGet()),
                10, 0));
        assertTrue(LocalDateTime.now().isBefore(end));

        LocalDateTime start2 = LocalDateTime.now();
        LocalDateTime end2 = start2.plus(Duration.ofMillis(50));
        Logger.D("start2: %s, end2: %s", DateTimeHelper.asString(start2, "mm:ss.SSS"), DateTimeHelper.asString(end2, "mm:ss.SSS"));

        //predicate returns true before timeout
        AtomicInteger count2 = new AtomicInteger();
        assertTrue( Functions.testUntil(
                () -> LocalDateTime.now().isAfter(end2),
                100,
                () -> Logger.D("count2=%d", count2.incrementAndGet()),
                5, 0));
        assertTrue(LocalDateTime.now().isAfter(end2));
    }

    @Test
    public void tryGet() {
        AtomicInteger count = new AtomicInteger();
        SupplierThrowable<Integer> integerSupplierThrowable = () -> count.incrementAndGet();

        //Timeout before value is greater than 20
        assertEquals(null, Functions.tryGet(integerSupplierThrowable, 100, v -> v > 20, 10));

        //value greater than 4 returned before timeout
        count.set(0);
        assertEquals(5, Functions.tryGet(integerSupplierThrowable, 100, v -> v > 4, 10));
    }
}