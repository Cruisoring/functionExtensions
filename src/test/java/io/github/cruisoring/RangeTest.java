package io.github.cruisoring;

import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.utility.ArrayHelper;
import io.github.cruisoring.utility.PlainList;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.cruisoring.Asserts.*;

public class RangeTest {

    @Test
    public void testInfiniteRanges() {
        assertEquals(Range.NEGATIVE_INFINITY.intValue(), Range.ALL_INT.getStartInclusive());
        assertEquals(Range.POSITIVE_INFINITY.intValue(), Range.ALL_INT.getEndInclusive());
        assertEquals("(−∞, +∞)", Range.ALL_INT.toString());

        assertEquals(Range.INFINITE_LENGTH, Range.ALL_INT.size());
    }

    @Test
    public void indexesOfLength() {
        Range range;
        range = Range.ofLength(0);
        assertEquals(Range.NONE, range);
        assertEquals("[0, 0)", range.toString());
        assertEquals(0L, range.size());
        assertEquals(0, range.getStartInclusive());
        assertEquals(-1, range.getStartExclusive());
        assertEquals(-1, range.getEndInclusive());
        assertEquals(0, range.getEndExclusive());
        assertEquals(Integer.valueOf(100), range.getStream().findFirst().orElse(100));

        range = Range.ofLength(1);
        assertEquals("[0, 1)", range.toString());
        assertEquals(1L, range.size());
        assertEquals(0, range.getStartInclusive());
        assertEquals(-1, range.getStartExclusive());
        assertEquals(0, range.getEndInclusive());
        assertEquals(1, range.getEndExclusive());
        assertEquals(ArrayHelper.asList(0), range.getStream().collect(Collectors.toList()));

        range = Range.ofLength(3);
        assertEquals("[0, 3)", range.toString());
        assertEquals(3L, range.size());
        assertEquals(0, range.getStartInclusive());
        assertEquals(-1, range.getStartExclusive());
        assertEquals(2, range.getEndInclusive());
        assertEquals(3, range.getEndExclusive());
        assertEquals(ArrayHelper.asList(0, 1, 2), range.getStream().collect(Collectors.toList()));

        //java.lang.IllegalStateException: Length shall not be negative value: -1
        //range = Range.ofLength(-1);
    }

    @Test
    public void open() {
        Range range;
        range = Range.open(3, 8);
        assertEquals("[4, 8)", range.toString());
        assertEquals(ArrayHelper.asList(4, 5, 6, 7), range.getStream().collect(Collectors.toList()));
        assertEquals(4L, range.size());
        assertEquals(4, range.getStartInclusive());
        assertEquals(3, range.getStartExclusive());
        assertEquals(7, range.getEndInclusive());
        assertEquals(8, range.getEndExclusive());

        range = Range.open(3, Range.POSITIVE_INFINITY - 2);
        assertEquals("[4, 2147483645)", range.toString());
        assertEquals(ArrayHelper.asList(4, 5, 6, 7), range.getStream().limit(4).collect(Collectors.toList()));
        assertEquals(Range.POSITIVE_INFINITY - 6, (int) range.size());
        assertEquals(4, range.getStartInclusive());
        assertEquals(3, range.getStartExclusive());
        assertEquals(Range.POSITIVE_INFINITY - 3, range.getEndInclusive());
        assertEquals(Range.POSITIVE_INFINITY - 2, range.getEndExclusive());
    }

    @Test
    public void closed() {
        Range range;
        range = Range.closed(3, 8);
        assertEquals("[3, 9)", range.toString());
        assertEquals(ArrayHelper.asList(3, 4, 5, 6, 7, 8), range.getStream().collect(Collectors.toList()));
        assertEquals(6L, range.size());
        assertEquals(3, range.getStartInclusive());
        assertEquals(2, range.getStartExclusive());
        assertEquals(8, range.getEndInclusive());
        assertEquals(9, range.getEndExclusive());

        range = Range.closed(3, Range.POSITIVE_INFINITY - 2);
        assertEquals("[3, 2147483646)", range.toString());
        assertEquals(Range.POSITIVE_INFINITY - 4, (int) range.size());
        assertEquals(3, range.getStartInclusive());
        assertEquals(2, range.getStartExclusive());
        assertEquals(Range.POSITIVE_INFINITY - 2, range.getEndInclusive());
        assertEquals(Range.POSITIVE_INFINITY - 1, range.getEndExclusive());
    }

    @Test
    public void openClosed() {
        Range range;
        range = Range.openClosed(3, 8);
        assertEquals("[4, 9)", range.toString());
        assertEquals(ArrayHelper.asList(4, 5, 6, 7, 8), range.getStream().collect(Collectors.toList()));
        assertEquals(5L, range.size());
        assertEquals(4, range.getStartInclusive());
        assertEquals(3, range.getStartExclusive());
        assertEquals(8, range.getEndInclusive());
        assertEquals(9, range.getEndExclusive());

        range = Range.openClosed(3, Range.POSITIVE_INFINITY - 2);
        assertEquals("[4, 2147483646)", range.toString());
        assertEquals(Range.POSITIVE_INFINITY - 5, (int) range.size());
        assertEquals(4, range.getStartInclusive());
        assertEquals(3, range.getStartExclusive());
        assertEquals(Range.POSITIVE_INFINITY - 2, range.getEndInclusive());
        assertEquals(Range.POSITIVE_INFINITY - 1, range.getEndExclusive());
    }

    @Test
    public void closedOpen() {
        Range range;
        range = Range.closedOpen(3, 8);
        assertEquals("[3, 8)", range.toString());
        assertEquals(ArrayHelper.asList(3, 4, 5, 6, 7), range.getStream().collect(Collectors.toList()));
        assertEquals(5L, range.size());
        assertEquals(3, range.getStartInclusive());
        assertEquals(2, range.getStartExclusive());
        assertEquals(7, range.getEndInclusive());
        assertEquals(8, range.getEndExclusive());

        range = Range.closedOpen(3, Range.POSITIVE_INFINITY - 2);
        assertEquals("[3, 2147483645)", range.toString());
        assertEquals(Range.POSITIVE_INFINITY - 5, (int) range.size());
        assertEquals(3, range.getStartInclusive());
        assertEquals(2, range.getStartExclusive());
        assertEquals(Range.POSITIVE_INFINITY - 3, range.getEndInclusive());
        assertEquals(Range.POSITIVE_INFINITY - 2, range.getEndExclusive());
    }

    @Test
    public void aboveClosed() {
        Range range;
        range = Range.aboveClosed(-1);
        assertEquals("[-1, +∞)", range.toString());
        assertEquals(ArrayHelper.asList(-1, 0, 1, 2, 3), range.getStream().limit(5).collect(Collectors.toList()));
        assertEquals(Range.INFINITE_LENGTH, range.size());
        assertEquals(-1, range.getStartInclusive());
        assertEquals(-2, range.getStartExclusive());
        assertEquals(Range.POSITIVE_INFINITY.intValue(), range.getEndInclusive());
        assertEquals(Range.POSITIVE_INFINITY.intValue(), range.getEndExclusive());

        range = Range.aboveClosed(3);
        assertEquals("[3, +∞)", range.toString());
        assertEquals(ArrayHelper.asList(3, 4, 5, 6, 7), range.getStream().limit(5).collect(Collectors.toList()));
        assertEquals(Range.INFINITE_LENGTH, range.size());
        assertEquals(3, range.getStartInclusive());
        assertEquals(2, range.getStartExclusive());
        assertEquals(Range.POSITIVE_INFINITY.intValue(), range.getEndInclusive());
        assertEquals(Range.POSITIVE_INFINITY.intValue(), range.getEndExclusive());
    }

    @Test
    public void aboveOpen() {
        Range range;
        range = Range.aboveOpen(-2);
        assertEquals("[-1, +∞)", range.toString());
        assertEquals(Range.INFINITE_LENGTH, range.size());
        assertEquals(-1, range.getStartInclusive());
        assertEquals(-2, range.getStartExclusive());
        assertEquals(Range.POSITIVE_INFINITY.intValue(), range.getEndInclusive());
        assertEquals(Range.POSITIVE_INFINITY.intValue(), range.getEndExclusive());

        range = Range.aboveOpen(2);
        assertEquals("[3, +∞)", range.toString());
        assertEquals(Range.INFINITE_LENGTH, range.size());
        assertEquals(3, range.getStartInclusive());
        assertEquals(2, range.getStartExclusive());
        assertEquals(Range.POSITIVE_INFINITY.intValue(), range.getEndInclusive());
        assertEquals(Range.POSITIVE_INFINITY.intValue(), range.getEndExclusive());
    }

    @Test
    public void belowClosed() {
        Range range;
        range = Range.belowClosed(-1);
        assertEquals("(−∞, 0)", range.toString());
        assertEquals(ArrayHelper.asList(-1, -2, -3, -4, -5), range.getStream().limit(5).collect(Collectors.toList()));
        assertEquals(Range.INFINITE_LENGTH, range.size());
        assertEquals(Range.NEGATIVE_INFINITY.intValue(), range.getStartInclusive());
        assertEquals(Range.NEGATIVE_INFINITY.intValue(), range.getStartExclusive());
        assertEquals(-1, range.getEndInclusive());
        assertEquals(0, range.getEndExclusive());

        range = Range.belowClosed(3);
        assertEquals("(−∞, 4)", range.toString());
        assertEquals(ArrayHelper.asList(3, 2, 1, 0, -1, -2), range.getStream().limit(6).collect(Collectors.toList()));
        assertEquals(Range.INFINITE_LENGTH, range.size());
        assertEquals(Range.NEGATIVE_INFINITY.intValue(), range.getStartInclusive());
        assertEquals(Range.NEGATIVE_INFINITY.intValue(), range.getStartExclusive());
        assertEquals(3, range.getEndInclusive());
        assertEquals(4, range.getEndExclusive());
    }

    @Test
    public void belowOpen() {
        Range range;
        range = Range.belowOpen(-1);
        assertEquals("(−∞, -1)", range.toString());
        assertEquals(ArrayHelper.asList(-2, -3, -4, -5), range.getStream().limit(4).collect(Collectors.toList()));
        assertEquals(Range.INFINITE_LENGTH, range.size());
        assertEquals(Range.NEGATIVE_INFINITY.intValue(), range.getStartInclusive());
        assertEquals(Range.NEGATIVE_INFINITY.intValue(), range.getStartExclusive());
        assertEquals(-2, range.getEndInclusive());
        assertEquals(-1, range.getEndExclusive());

        range = Range.belowOpen(3);
        assertEquals("(−∞, 3)", range.toString());
        assertEquals(ArrayHelper.asList(2, 1, 0, -1, -2, -3, -4, -5), range.getStream().limit(8).collect(Collectors.toList()));
        assertEquals(Range.INFINITE_LENGTH, range.size());
        assertEquals(Range.NEGATIVE_INFINITY.intValue(), range.getStartInclusive());
        assertEquals(Range.NEGATIVE_INFINITY.intValue(), range.getStartExclusive());
        assertEquals(2, range.getEndInclusive());
        assertEquals(3, range.getEndExclusive());
    }

    @Test
    public void contains() {
        Range range;
        range = Range.ofLength(0);
        assertEquals(Range.NONE, range);
        assertEquals("[0, 0)", range.toString());
        assertAllFalse(range.contains(0),
                range.contains(1),
                range.contains(-1));

        range = Range.ofLength(1);
        assertEquals("[0, 1)", range.toString());
        assertAllTrue(range.contains(0));
        assertAllFalse(range.contains(1),
                range.contains(-1));

        range = Range.ofLength(3);
        assertEquals("[0, 3)", range.toString());
        assertAllTrue(range.contains(0),
                range.contains(1),
                range.contains(2));
        assertAllFalse(range.contains(3),
                range.contains(-1));
    }

    @Test
    public void encloses() {
        Range range0_0 = Range.NONE;
        Range range0_1 = new Range(0, 1);
        Range range1_1 = new Range(1, 1);
        Range range1_2 = new Range(1, 2);

        assertAllTrue(range0_0.contains(range0_0));
        assertAllFalse(range0_0.contains(range0_1),
                range0_0.contains(range1_1),
                range0_0.contains(range1_2));

        assertAllTrue(range1_2.contains(range1_1));
        assertAllFalse(range1_2.contains(range0_0),
                range1_2.contains(range0_1));

        Range range2_4 = new Range(2, 4);
        Range range3_4 = new Range(3, 4);
        assertAllFalse(range2_4.contains(range0_0),
                range2_4.contains(range1_1),
                range2_4.contains(range1_2));
        assertAllTrue(range2_4.contains(range3_4));

        Range range0_9 = Range.ofLength(9);
        assertAllTrue(range0_9.contains(range0_1),
                range0_9.contains(range1_1),
                range0_9.contains(range1_2),
                range0_9.contains(range2_4),
                range0_9.contains(range3_4));

        Range range0_9_2 = Range.ofLength(9);
        Range range0_15 = Range.ofLength(15);
        assertAllTrue(range0_9.contains(range0_9_2),
                range0_15.contains(range0_9));
    }

    @Test
    public void isConnected() {
        Range range0_0 = Range.NONE;
        Range range0_1 = new Range(0, 1);
        Range range1_1 = new Range(1, 1);
        Range range1_2 = new Range(1, 2);
        Range range0_3 = new Range(0, 3);
        Range range1_3 = new Range(1, 3);
        Range range2_4 = new Range(2, 4);
        Range range3_4 = new Range(3, 4);

        assertAllTrue(range0_0.isConnected(range0_1));
        assertAllTrue(range0_1.isConnected(range1_1));
        assertAllTrue(range0_1.isConnected(range1_2));
        assertAllTrue(range1_2.isConnected(range0_1));
        assertAllFalse(range0_1.isConnected(range2_4));

        assertAllFalse(range0_0.isConnected(range1_1));
        assertAllTrue(range0_0.isConnected(range0_3));
        assertAllFalse(range0_0.isConnected(range1_3));
        assertAllFalse(range1_2.isConnected(range3_4));
        assertAllTrue(range1_3.isConnected(range3_4));
        assertAllTrue(range3_4.isConnected(range1_3));
    }

    @Test
    public void testUnionWith() {
        Range range0_0 = Range.NONE;
        Range range0_1 = new Range(0, 1);
        Range range2_4 = new Range(2, 4);
        Range range3_4 = new Range(3, 4);

        assertEquals(Range.closedOpen(0, 4), range0_0.unionWith(range2_4));
        assertEquals(Range.closedOpen(0, 4), range0_1.unionWith(range3_4));

        Range range7_9 = new Range(7, 9);
        assertEquals(Range.closedOpen(0, 9), range0_1.unionWith(range3_4).unionWith(range7_9));

        assertEquals(Range.aboveClosed(2), range2_4.unionWith(Range.aboveOpen(10)));

        assertEquals(Range.ALL_INT, Range.aboveClosed(5).unionWith(Range.belowClosed(-100)));
    }

    @Test
    public void testIntersectionWith(){
        Range positives = Range.aboveOpen(0);
        Range nonNegatives = Range.aboveClosed(0);
        Range zero = Range.closed(0, 0);
        Range ten = Range.ofLength(10);
        Range range2_3 = Range.closed(2, 3);
        Range range1_9 = Range.closed(1, 9);
        Range range5 = Range.closed(5, 5);

        assertEquals(Range.NONE, Range.closed(-1, -1).intersectionWith(Range.ofLength(1)));
        assertEquals(Range.NONE, positives.intersectionWith(zero));
        assertEquals(Range.NONE, zero.intersectionWith(positives));
        assertEquals(Range.NONE, zero.intersectionWith(range1_9));
        assertEquals(Range.NONE, zero.intersectionWith(range2_3));
        assertEquals(Range.NONE, zero.intersectionWith(range5));
        assertEquals(Range.NONE, zero.intersectionWith(positives));
        assertEquals(zero, zero.intersectionWith(nonNegatives));
        assertEquals(zero, zero.intersectionWith(ten));
        assertEquals(range2_3, positives.intersectionWith(range2_3));
        assertEquals(range2_3, ten.intersectionWith(range2_3));
        assertEquals(range2_3, range2_3.intersectionWith(ten));
        assertEquals(range2_3, range2_3.intersectionWith(range2_3));
    }

    @Test
    public void overlaps() {
        Range range1_4 = new Range(1, 4);

        assertAllFalse(
                range1_4.overlaps(new Range(-1, 0)),
                range1_4.overlaps(new Range(0, 1)),
                range1_4.overlaps(new Range(2, 2)),
                range1_4.overlaps(new Range(4, 7)),
                range1_4.overlaps(Range.belowOpen(1)),
                range1_4.overlaps(Range.belowOpen(-1)),
                range1_4.overlaps(Range.aboveOpen(3)));

        assertAllTrue(
                range1_4.overlaps(new Range(-1, 2)),
                range1_4.overlaps(Range.aboveClosed(0)),
                range1_4.overlaps(Range.belowOpen(4)),
                range1_4.overlaps(Range.aboveOpen(0)),
                range1_4.overlaps(Range.aboveOpen(-3)),
                range1_4.overlaps(new Range(1, 2)),
                range1_4.overlaps(new Range(1, 4)),
                range1_4.overlaps(new Range(2, 4)),
                range1_4.overlaps(new Range(0, 3)),
                range1_4.overlaps(new Range(3, 7)),
                range1_4.overlaps(new Range(2, 9)),
                range1_4.overlaps(Range.aboveOpen(1)),
                range1_4.overlaps(Range.aboveOpen(2)),
                range1_4.overlaps(Range.aboveClosed(2)),
                range1_4.overlaps(Range.aboveClosed(3)),
                range1_4.overlaps(Range.belowOpen(3)),
                range1_4.overlaps(Range.belowOpen(2)),
                range1_4.overlaps(Range.belowClosed(1)));

        Range range2_2 = new Range(2, 2);
        assertAllFalse(range2_2.overlaps(new Range(-1, 0)),
                range2_2.overlaps(new Range(0, 1)),
                range2_2.overlaps(new Range(4, 7)),
                range2_2.overlaps(new Range(1, 2)),
                range2_2.overlaps(new Range(1, 4)),
                range2_2.overlaps(new Range(2, 4)),
                range2_2.overlaps(Range.aboveOpen(3)),
                range2_2.overlaps(Range.aboveOpen(-3)),
                range2_2.overlaps(Range.belowOpen(1)),
                range2_2.overlaps(Range.belowOpen(-1)));
    }

    @Test
    public void compareTo() {
        Range range3_4 = new Range(3, 4);
        Range range3_5 = new Range(3, 5);
        Range range7_8 = new Range(7, 8);
        Range range0_1 = new Range(0, 1);
        Range range0_10 = new Range(0, 10);
        Range range1_2 = new Range(1, 2);
        Range range0_3 = new Range(0, 3);
        Range range1_1 = new Range(1, 1);
        Range range2_4 = new Range(2, 4);
        Range range1_9 = new Range(1, 9);

        List<Range> rangeList = new PlainList<>(range3_4, range3_5, range7_8, range0_1, range0_10, range1_2, range0_3, range1_1, range2_4, range1_9);
        Collections.sort(rangeList);
        assertEquals(ArrayHelper.asList(range0_10, range0_3, range0_1, range1_9, range1_2, range1_1, range2_4, range3_5, range3_4, range7_8), rangeList);
    }

    @Test
    public void gapWith() {
        Range range1_4 = new Range(1, 4);

        assertEquals(Range.NONE, range1_4.gapWith(new Range(1, 2)));
        assertEquals(Range.NONE, range1_4.gapWith(new Range(1, 1)));
        assertEquals(Range.NONE, range1_4.gapWith(new Range(1, 3)));
        assertEquals(Range.NONE, range1_4.gapWith(new Range(1, 4)));
        assertEquals(Range.NONE, range1_4.gapWith(new Range(1, 5)));
        assertEquals(Range.NONE, range1_4.gapWith(new Range(4, 5)));
        assertEquals(Range.NONE, range1_4.gapWith(Range.aboveClosed(0)));
        assertEquals(Range.NONE, range1_4.gapWith(Range.aboveClosed(1)));
        assertEquals(Range.NONE, range1_4.gapWith(Range.aboveClosed(3)));
        assertEquals(Range.NONE, range1_4.gapWith(Range.aboveClosed(4)));
        assertEquals(Range.NONE, range1_4.gapWith(new Range(-1, 1)));
        assertEquals(Range.NONE, range1_4.gapWith(Range.belowOpen(5)));
        assertEquals(Range.NONE, range1_4.gapWith(Range.belowOpen(4)));
        assertEquals(Range.NONE, range1_4.gapWith(Range.belowOpen(3)));
        assertEquals(Range.NONE, range1_4.gapWith(Range.belowOpen(1)));

        assertEquals(new Range(4, 5), range1_4.gapWith(new Range(5, 5)));
        assertEquals(new Range(4, 6), range1_4.gapWith(new Range(6, 6)));
        assertEquals(new Range(4, 6), range1_4.gapWith(new Range(6, 9)));
        assertEquals(new Range(4, 8), range1_4.gapWith(Range.aboveClosed(8)));
        assertEquals(new Range(0, 1), range1_4.gapWith(new Range(-9, 0)));
        assertEquals(new Range(-1, 1), range1_4.gapWith(new Range(-9, -1)));
        assertEquals(new Range(-7, 1), range1_4.gapWith(Range.belowClosed(-8)));
    }


    @Test
    public void testIndexesToRanges_WithPairedIndexes_getAllRanges() {
        List<Range> ranges;

        ranges = Range.indexesToRanges(new PlainList<>(), new PlainList<>());
        assertAllTrue(ranges.size() == 0);

        ranges = Range.indexesToRanges(ArrayHelper.asList(1), ArrayHelper.asList(5));
        assertEquals(Range.closed(1, 5), ranges.get(0));

        ranges = Range.indexesToRanges(ArrayHelper.asList(1, 3, 5), ArrayHelper.asList(7, 9, 11));
        assertEquals(ArrayHelper.asList(Range.closed(5, 7), Range.closed(3, 9), Range.closed(1, 11)), ranges);

        ranges = Range.indexesToRanges(ArrayHelper.asList(1, 3, 5, 10), ArrayHelper.asList(7, 9, 11, 13));
        assertEquals(ArrayHelper.asList(Range.closed(5, 7), Range.closed(3, 9), Range.closed(10, 11), Range.closed(1, 13)), ranges);
    }


    @Test
    public void testGetIndexesInRange() {
        List<Integer> subList;
        PlainList<Integer> list = new PlainList<>(1, 3, 4, 5, 9, 10, 12, 15, 21, 23, 25);

        //No overlapped:
        subList = Range.getIndexesInRange(list, Range.open(3, 4));
        assertAllTrue(subList.size() == 0);

        subList = Range.getIndexesInRange(list, Range.closed(17, 19));
        assertAllTrue(subList.size() == 0);

        subList = Range.getIndexesInRange(list, Range.openClosed(-1, 0));
        assertAllTrue(subList.size() == 0);

        subList = Range.getIndexesInRange(list, Range.open(25, 27));
        assertAllTrue(subList.size() == 0);

        //With 1 element in range:
        subList = Range.getIndexesInRange(list, Range.closed(4, 4));
        assertAllTrue(subList.size() == 1 && subList.get(0).equals(4));

        subList = Range.getIndexesInRange(list, Range.closed(5, 8));
        assertAllTrue(subList.size() == 1 && subList.get(0).equals(5));

        subList = Range.getIndexesInRange(list, Range.closed(0, 1));
        assertAllTrue(subList.size() == 1 && subList.get(0).equals(1));

        subList = Range.getIndexesInRange(list, Range.closed(24, 29));
        assertAllTrue(subList.size() == 1 && subList.get(0).equals(25));

        //With multiple element in range:
        subList = Range.getIndexesInRange(list, Range.closed(4, 5));
        assertEquals(Arrays.asList(4, 5), subList);

        subList = Range.getIndexesInRange(list, Range.open(4, 10));
        assertEquals(Arrays.asList(5, 9), subList);

        subList = Range.getIndexesInRange(list, Range.closed(0, 25));
        assertEquals(Arrays.asList(1, 3, 4, 5, 9, 10, 12, 15, 21, 23, 25), subList);

        subList = Range.getIndexesInRange(list, Range.closed(22, 30));
        assertEquals(Arrays.asList(23, 25), subList);


    }

    @Test
    public void testEquals() {
        assertAllTrue(
                Range.NONE.equals(Range.NONE),
                Range.NONE.equals(Range.open(-1, 0)),
                Range.ofLength(1).equals(Range.closed(0, 0)),
                Range.openClosed(2, 3).equals(Range.closed(3, 3)),
                Range.aboveClosed(0).equals(Range.aboveOpen(-1))
        );
        assertAllFalse(
                Range.NONE.equals(Range.openClosed(-1, 0)),
                Range.NONE.equals(Range.openClosed(3, 3)),
                Range.ofLength(1).equals(Range.closed(0, 1)),
                Range.openClosed(2, 3).equals(Range.closed(4, 4)),
                Range.aboveClosed(0).equals(Range.aboveOpen(0))
        );
    }

    @Test
    public void testGetStream(){
        assertEquals(1L, Range.openClosed(1, 2).getStream().count());

        String[] days = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        assertLogging(() -> Range.ofLength(7).getStream().forEach(i -> Logger.D(days[i])), days);
        assertLogging(() -> Range.closed(5, 6).getStream().forEach(i -> Logger.V(days[i])), "Saturday", "Sunday");
        assertException(() -> Range.ofLength(8).getStream().forEach(i -> Logger.D(days[i])), ArrayIndexOutOfBoundsException.class);
        assertLogging(() -> Range.open(0, 2).getStream().forEach(i -> Logger.V(days[i])), "Tuesday");

        assertException(() -> Range.ALL_INT.getStream().findFirst(), IllegalStateException.class);
    }

    private void assertRandomIndexes(Range range) {
        Integer[] indexes = null;
        for (int i = 0; i < 10; i++) {
            indexes = range.getRandomIndexes();
            for (int j = 1; j < indexes.length; j++) {
                if (indexes[j] < indexes[j - 1]) {
                    Logger.D("Shuffled indexes for range of %d: %s", indexes.length,
                            Arrays.stream(indexes).map(index -> index.toString()).collect(Collectors.joining(", ")));
                    return;
                }
            }
        }
        fail("Failed to get randome indexes for range of %d: %s", indexes.length,
                Arrays.stream(indexes).map(index -> index.toString()).collect(Collectors.joining(", ")));
    }

    @Test
    public void getRandomIndexes() {
        Range range1 = Range.closed(1, 1);
        assertEquals(new Integer[]{1}, range1.getRandomIndexes());

        assertRandomIndexes(Range.closed(0, 1));
        assertRandomIndexes(Range.closed(0, 2));
        assertRandomIndexes(Range.closed(0, 100));
        assertRandomIndexes(Range.closed(0, 5));
        assertRandomIndexes(Range.closed(0, 10));
    }

    @Test
    public void subString() {
        assertEquals("", Range.NONE.subString("abc"));
        assertEquals("", new Range(1, 1).subString("abc"));
        assertEquals("a", new Range(0, 1).subString("abc"));
        assertEquals("b", new Range(1, 2).subString("abc"));
        assertEquals("ab", new Range(0, 2).subString("abc"));
        assertEquals("abc", Range.ofLength(3).subString("abc"));
        assertEquals("", new Range(3, 3).subString("abc"));
        assertEquals("c", new Range(2, 3).subString("abc"));
        assertEquals("bc", new Range(1, 3).subString("abc"));
        assertEquals("", new Range(1, 1).subString("abc"));

        assertException(() -> new Range(3, 4).subString("abc"), IllegalStateException.class);
        assertException(() -> new Range(1, 4).subString("abc"), IllegalStateException.class);
        assertException(() -> new Range(2, 1).subString("abc"), IllegalStateException.class);
        assertException(() -> new Range(-1, -1).subString("abc"), IllegalStateException.class);
    }
}