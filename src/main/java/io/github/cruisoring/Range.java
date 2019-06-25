package io.github.cruisoring;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple2;
import io.github.cruisoring.utility.SimpleTypedList;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.github.cruisoring.Asserts.*;
import static java.util.Comparator.comparing;

/**
 * This data structure specify a continuous set of Integers by its startExclusive and endExclusive sides integers.
 * By stealing the 2 largest integers and 2 smallest integers to represent the Positive and Negative Infinity, this class can be used to represent real life ranges.
 */
public class Range implements Comparable<Range> {
    //region Constants
    public static final long INFINITE_LENGTH = Long.MAX_VALUE;

    //Integer number reserved to represent negative infinity: −∞, shall not be used explicitly as argument to specify the below or upper bound
    public static final Integer NEGATIVE_INFINITY = Integer.MIN_VALUE;
    //Integer number reserved to represent positive infinity: +∞, shall not be used explicitly as argument to specify the below or upper bound
    public static final Integer POSITIVE_INFINITY = Integer.MAX_VALUE;

    //All integer numbers except the resevered Integer.MIN_VALUE and Integer.MAX_VALUE that represent −∞ and +∞ respectively
    public static final Range ALL_INT = new Range(NEGATIVE_INFINITY, POSITIVE_INFINITY);
    //Empty integer range
    public static final Range NONE = new Range(0, 0);
    private static final int _RUN_PARALLEL = 100;
    static Random random = new Random();
    private static final Predicate<Tuple2<Range, Range>> overlapPredicate = tuple -> tuple.getFirst().overlaps(tuple.getSecond());
    //endregion

    //region Static methods

    //region Static factory methods with explicit or implicit ends of the Range
    /**
     * Returns a range contains all indexes for a enumerable object with specific bufferSize.
     *
     * @param length Length of the enumerable object, must be greater than or equal to 0.
     * @return Range of the indexes.
     */
    public static Range ofLength(int length) {
        Asserts.assertTrue(length >= 0, "Length shall not be negative value: " + length);
        return length == 0 ? NONE : new Range(0, length);
    }

    /**
     * Returns a range that contains all values strictly greater than {@code startExclusive} and less than {@code endExclusive}
     *
     * @param startExclusive Value below the first one of the range, shall be greater than NEGATIVE_INFINITY+1.
     * @param endExclusive   Value greater than the last one of the range, shall be less than POSITIVE_INFINITY-1.
     * @return An Range object between {@code startExclusive} and {@code endExclusive} exclusively.
     */
    public static Range open(int startExclusive, int endExclusive) {
        Asserts.assertTrue(startExclusive > NEGATIVE_INFINITY + 1, "To represent infinitive range below endEnclusive, use belowOpen(endExclusive)");
        Asserts.assertTrue(endExclusive < POSITIVE_INFINITY - 1, "To represent range above startExclusive, use aboveOpen(startExclusive)");
        return new Range(startExclusive + 1, endExclusive);
    }

    /**
     * Returns a range that contains all values greater than or equal to {@code startInclusive} and less than or equal to {@code endInclusive}
     *
     * @param startInclusive First value of the defined range, shall be greater than NEGATIVE_INFINITY+1.
     * @param endInclusive   Last value of the defined range, shall be less than POSITIVE_INFINITY-1.
     * @return An Range object between {@code startExclusive} and {@code endExclusive} inclusively.
     */
    public static Range closed(int startInclusive, int endInclusive) {
        Asserts.assertTrue(startInclusive > NEGATIVE_INFINITY + 1, "To represent infinitive range below endEnclusive, use belowClosed(endInclusive)");
        Asserts.assertTrue(endInclusive < POSITIVE_INFINITY - 1, "To represent range above startExclusive, use aboveClosed(startInclusive)");
        return new Range(startInclusive, endInclusive + 1);
    }

    /**
     * Returns a range that contains all values greater than {@code startExclusive} and less than or equal to {@code endInclusive}
     *
     * @param startExclusive Value below the first one of the range, shall be greater than NEGATIVE_INFINITY+1.
     * @param endInclusive   Last value of the defined range, shall be less than POSITIVE_INFINITY-1.
     * @return An Range object between {@code startExclusive} exclusive and {@code endInclusive} inclusive.
     */
    public static Range openClosed(int startExclusive, int endInclusive) {
        Asserts.assertTrue(startExclusive > NEGATIVE_INFINITY + 1, "To represent infinitive range below endEnclusive, use belowOpen(endExclusive)");
        Asserts.assertTrue(endInclusive < POSITIVE_INFINITY - 1, "To represent range above startExclusive, use aboveClosed(startInclusive)");
        return new Range(startExclusive + 1, endInclusive + 1);
    }

    /**
     * Returns a range that contains all values greater than or equal to {@code startInclusive} and less than {@code endExclusive}
     *
     * @param startInclusive First value of the defined range, shall be greater than NEGATIVE_INFINITY+1.
     * @param endExclusive   Value greater than the last one of the range, shall be less than POSITIVE_INFINITY-1.
     * @return An Range object between {@code startInclusive} exclusive and {@code endExclusive} inclusive.
     */
    public static Range closedOpen(int startInclusive, int endExclusive) {
        Asserts.assertTrue(startInclusive > NEGATIVE_INFINITY + 1, "To represent infinitive range below endEnclusive, use belowClosed(endInclusive)");
        Asserts.assertTrue(endExclusive < POSITIVE_INFINITY - 1, "To represent range above startExclusive, use aboveOpen(startExclusive)");
        return new Range(startInclusive, endExclusive);
    }

    /**
     * Returns a range that contains all values greater than or equal to {@code startInclusive}
     *
     * @param startInclusive First value of the defined range, shall be greater than NEGATIVE_INFINITY+1.
     * @return An Range object above {@code startInclusive} inclusive.
     */
    public static Range aboveClosed(int startInclusive) {
        return startInclusive + 1 > NEGATIVE_INFINITY + 1 ?
                new Range(startInclusive, POSITIVE_INFINITY) : ALL_INT;
    }

    /**
     * Returns a range that contains all values greater than {@code startExclusive}
     *
     * @param startExclusive Value below the first one of the range, shall be greater than NEGATIVE_INFINITY+1.
     * @return An Range object above {@code startInclusive} exclusive.
     */
    public static Range aboveOpen(int startExclusive) {
        return startExclusive > NEGATIVE_INFINITY + 1 ?
                new Range(startExclusive + 1, POSITIVE_INFINITY) : ALL_INT;
    }

    /**
     * Returns a range that contains all values less than or equal to {@code endInclusive}
     *
     * @param endInclusive Last value of the defined range, shall be less than POSITIVE_INFINITY-1.
     * @return An Range object below {@code endInclusive} inclusive.
     */
    public static Range belowClosed(int endInclusive) {
        return endInclusive < POSITIVE_INFINITY - 1 ?
                new Range(NEGATIVE_INFINITY, endInclusive + 1) : ALL_INT;
    }

    /**
     * Returns a range that contains all values less than {@code endExclusive}
     *
     * @param endExclusive Value greater than the last one of the range, shall be less than POSITIVE_INFINITY-1.
     * @return An Range object below {@code endExclusive} exclusive.
     */
    public static Range belowOpen(int endExclusive) {
        return endExclusive < POSITIVE_INFINITY - 1 ?
                new Range(NEGATIVE_INFINITY, endExclusive) : ALL_INT;
    }
    //endregion

    /**
     * Check if the Range is contained by the indexes of bufferSize.
     *
     * @param range  Range to be checked.
     * @param length Length of the valid range [0, bufferSize)
     * @return True if the range is contained by [0, bufferSize), otherwise False.
     */
    public static boolean withinLength(Range range, Integer length) {
        Asserts.assertAllTrue(range != null, length >= 0);

        return range != null && range._start >= 0 && range._end <= length;
    }

    /**
     * Converting the indexes of wrapping characters in pairs as unmodifiable Range list.
     *
     * @param indexes List of the indexes that must be even.
     * @return List of the ranges with even indexes as startInclusive, and odd indexes as endInclusive.
     */
    public static List<Range> indexesToRanges(Collection<Integer> indexes) {
        assertAllNotNull(indexes);
        Asserts.assertAllTrue(indexes.size() % 2 == 0);

        return _indexesToRanges(indexes);
    }
    //region Instance variables
    //Represent the size of the range in long, -1 when size is infinite
    private final long size;

    /**
     * Constructor of Range support limited scope specified by the start and end index.
     *
     * @param startInclusive StartIndex of the concerned scope which might be included in the scope, shall be greater than NEGATIVE_INFINITY + 1.
     * @param endExclusive   EndIndex of the concerned scope that is above the last index of the scope, shall be lesser than POSITIVE_INFINITY.
     */
    protected Range(int startInclusive, int endExclusive) {
        assertTrue(startInclusive <= endExclusive,
                "Range startInclusive %d shall not be greater or equal to endExclusive %d.", startInclusive, endExclusive);

        //Notice: the largest 2 and smallest 2 integers are reserved to represent infinity and shall not be used
        _start = startInclusive <= NEGATIVE_INFINITY + 1 ? NEGATIVE_INFINITY : startInclusive;
        _end = endExclusive > POSITIVE_INFINITY - 1 ? POSITIVE_INFINITY : endExclusive;
        if (_start == NEGATIVE_INFINITY || _end == POSITIVE_INFINITY)
            size = INFINITE_LENGTH;
        else
            size = (long) _end - _start;
    }

    static List<Range> _indexesToRanges(Collection<Integer> indexes) {
        Iterator<Integer> iterator = indexes.iterator();
        List<Range> ranges = new SimpleTypedList<>();
        while (iterator.hasNext()) {
            Integer startIndex = iterator.next();
            Integer endIndex = iterator.next();
            Range range = Range.closed(startIndex, endIndex);
            ranges.add(range);
        }

        return Collections.unmodifiableList(ranges);
    }

    /**
     * Find the subList of the given index list within a specific range.
     *
     * @param allIndexes Indexes which would not contain the lower and upper end point of the range.
     * @param range      Range under concern.
     * @return Sublist of the given sorted index list within a specific range.
     */
    public static List<Integer> getIndexesInRange(List<Integer> allIndexes, Range range) {
        assertAllNotNull(allIndexes, range);

        if (allIndexes.isEmpty()) {
            return new SimpleTypedList<>();
        }

        //Sort the indexes with nature order
        Collections.sort(allIndexes, Comparator.naturalOrder());

        return _getIndexesInRange(allIndexes, range);
    }

    /**
     * Converting the indexes of starts and indexes of ends in pairs as unmodifiable Range list.
     *
     * @param startIndexes Index list of the starting characters.
     * @param endIndexes   Index list of the ending characters.
     * @return Unmodifiable list of the ranges with one of the index of starting character, and one of the index of the ending character.
     */
    public static List<Range> indexesToRanges(Collection<Integer> startIndexes, Collection<Integer> endIndexes) {
        assertAllNotNull(startIndexes, endIndexes);

        int size = startIndexes.size();
        Asserts.assertAllTrue(size == endIndexes.size());

        return _indexesToRanges(startIndexes, endIndexes);
    }

    private static List<Integer> _getIndexesInRange(List<Integer> allIndexes, Range range) {
        List<Integer> result = new SimpleTypedList<>();

        int count = allIndexes.size();
        Integer lower = range.getStartInclusive();
        Integer upper = range.getEndInclusive();
        if (count == 0 || lower > allIndexes.get(count - 1) || upper < allIndexes.get(0)) {
            return result;
        }

        boolean belowRange = true;
        for (int i = 0; i < count; i++) {
            Integer index = allIndexes.get(i);

            if (belowRange) {
                if(index >= lower) {
                    if (range.contains(index)) {
                        result.add(index);
                    } else if (index > upper) {
                        return result;
                    }
                    belowRange = false;
                }
            } else if (range.contains(index)) {
                result.add(index);
            } else if (index > upper) {
                return result;
            }
        }
        return result;
    }

    /**
     * Get all overlapped {@code Range} pairs from the two given collections.
     *
     * @param ranges1 the first Collection of {@code Range}
     * @param ranges2 the second Collection of {@code Range}
     * @return pairs of {@code Range} from two collections if they are overlapped.
     */
    public static List<Tuple2<Range, Range>> getOverlappedRangePairs(Collection<Range> ranges1, Collection<Range> ranges2) {
        return getRangePairs(ranges1, ranges2, overlapPredicate);
    }
    //endregion

    /**
     * Get subString of the concerned JSON text with its Range.
     *
     * @param charSequence All JSON text to be parsed.
     * @param range    Range of the subString within the charSequence.
     * @return SubString specified by the given Range.
     */
    public static String subString(CharSequence charSequence, Range range) {
        assertAllNotNull(charSequence, range);
        Asserts.assertAllTrue(Range.withinLength(range, charSequence.length()));

        return charSequence.subSequence(range.getStartInclusive(), range.getEndExclusive()).toString();
    }

    static List<Range> _indexesToRanges(Collection<Integer> startIndexes, Collection<Integer> endIndexes) {
        TreeSet<Integer> sortedSet = new TreeSet<>(startIndexes);
        sortedSet.addAll(endIndexes);

        List<Range> ranges = new SimpleTypedList<>();

        for (int i = startIndexes.size() - 1; i >= 0; i--) {
            Integer start = null;
            Integer end = null;
            for (Integer index : sortedSet) {
                if (start == null || startIndexes.contains(index)) {
                    start = index;
                } else {
                    end = index;
                    break;
                }
            }
            ranges.add(Range.closed(start, end));
            sortedSet.remove(start);
            sortedSet.remove(end);
        }

        return ranges;
    }

    private final int _start, _end;
    //endregion

    //region Constructors

    /**
     * Get the pairs of {@code Range} from two collections meeting condition specified by {@code predicate}
     *
     * @param ranges1   the first Collection of {@code Range}
     * @param ranges2   the second Collection of {@code Range}
     * @param predicate specify condition of two {@code Range} shall meet.
     * @return pairs of {@code Range} from two collections meeting specific condition as a list
     */
    public static List<Tuple2<Range, Range>> getRangePairs(Collection<Range> ranges1, Collection<Range> ranges2, Predicate<Tuple2<Range, Range>> predicate) {
        assertAllNotNull(ranges1, ranges2, predicate);

        int size1 = ranges1.size();
        int size2 = ranges2.size();
        int combinations = size1 * size2;
        if (combinations == 0)
            return new SimpleTypedList<>();

        Stream<Tuple2<Range, Range>> options = ranges1.stream()
                .flatMap(x -> ranges2.stream().map(y -> Tuple.create(x, y)));

        List<Tuple2<Range, Range>> result;
        if (combinations < _RUN_PARALLEL) {
            result = options.filter(predicate).collect(Collectors.toList());
        } else {
            result = options.parallel().filter(predicate).collect(Collectors.toList());
        }
        return result;
    }
    //endregion

    //region Instance methods

    /**
     * Get the size of the range as a long value, -1 when it is infinitive.
     *
     * @return Count of values between _start (inclusive) and _end (exclusive), -1L when it is infinitive.
     */
    public long size() {
        return size;
    }

    /**
     * Indicates if the {@code Range} is empty with size of 0.
     * @return  <tt>true</tt> if its size is 0, otherwise <tt>false</tt>
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Get the valid indexes of this {@code Range} as a {@code Stream}
     * @return a {@code Stream} of integer indexes of this {@code Range}.
     */
    public Stream<Integer> getStream() {
        if (size == 0) {
            return Stream.empty();
        } else if (_start == NEGATIVE_INFINITY && _end == POSITIVE_INFINITY) {
            throw new IllegalStateException("Cannot get stream for all integers");
        } else if (size != INFINITE_LENGTH) {
            return IntStream.range(_start, _end).boxed();
        } else if (_start == NEGATIVE_INFINITY) {
            return IntStream.iterate(_end - 1, i -> i - 1).boxed();
        } else if (_end == POSITIVE_INFINITY) {
            return IntStream.iterate(_start, i -> i + 1).boxed();
        } else {
            throw new IllegalStateException(String.format("Failed to define the process when _start=%d and _end=%d.", _start, _end));
        }
    }

    /**
     * Check if the {@code Range} contains the value.
     *
     * @param index the value to be evaluated.
     * @return <tt>true</tt> if the {@code Range} does contain the value, otherwise <tt>false</tt>.
     */
    public boolean contains(int index) {
        return _start <= index && index < _end;
    }

    /**
     * Check if all values of the list contained by the {@code Range}
     *
     * @param indexes the values to be evaluated as a list
     * @return <tt>true</tt> if the {@code Range} does contain all the values, otherwise <tt>false</tt>.
     */
    public boolean containsAll(List<Integer> indexes) {
        assertAllNotNull(indexes);

        if (size == 0) {
            return false;
        } else if (indexes.isEmpty()) {
            return true;
        }

        Collections.sort(indexes);
        return contains(indexes.get(0)) && (indexes.isEmpty() || contains(indexes.get(indexes.size() - 1)));
    }

    /**
     * Check if all values of the list contained by the {@code Range}
     *
     * @param indexes the values to be evaluated as a varargs
     * @return <tt>true</tt> if the {@code Range} does contain all the values, otherwise <tt>false</tt>.
     */
    public boolean containsAll(int... indexes) {
        if (size == 0) {
            return false;
        } else if (indexes.length == 0) {
            return true;
        }

        for (int value : indexes) {
            if (!contains(value))
                return false;
        }
        return true;
    }

    /**
     * Check if the {@code Range} contains all the values of other {@code Range}
     * @param other  another {@code Range} to be evaluated.
     * @return  <tt>true</tt> if this {@code Range} does contain all the values of other {@code Range}, otherwise <tt>false</tt>.
     */
    public boolean contains(Range other) {
        assertAllNotNull(other);
        return this._start <= other._start && this._end >= other._end;
    }

    /**
     * Check if there is no gap between this {@code Range} and other {@code Range}
     * @param other the other {@code Range} to be evaluated.
     * @return      <tt>true</tt> if the two {@code Range} are connected, otherwise <tt>false</tt>
     */
    public boolean isConnected(Range other) {
        assertAllNotNull(other);
        return this._start <= other._end && other._start <= this._end;
    }

    /**
     * Check if this {@code Range} and other {@code Range} both contain at least one common value
     * @param other the other {@code Range} to be evaluated.
     * @return      <tt>true</tt> if the two {@code Range} share common value, otherwise <tt>false</tt>
     */
    public boolean overlaps(Range other) {
        assertAllNotNull(other);

        if(isEmpty() || other.isEmpty()){
            return false;
        } else if(_start <= other._start){
            return _end > other._start;
        } else {
            return other._end > _start;
        }
    }

    /**
     * Get the shared values between two {@code Range} as a {@code Range}
     * @param other the other {@code Range} to be evaluated.
     * @return  {@code Range} containing all shared values
     */
    public Range unionWith(Range other) {
        assertAllNotNull(other);

        int minStart = Math.min(_start, other._start);
        int maxEnd = Math.max(_end, other._end);

        if (minStart == NEGATIVE_INFINITY && maxEnd == POSITIVE_INFINITY) {
            return ALL_INT;
        } else if (minStart == _start && maxEnd == _end) {
            return this;
        } else if (minStart == other._start && maxEnd == other._end) {
            return other;
        } else {
            return new Range(minStart, maxEnd);
        }
    }

    /**
     * Get the shared values of two {@code Range}s as a {@code Range}
     * @param other the other {@code Range} to be evaluated.
     * @return  {@code Range} containing all shared values
     */
    public Range intersectionWith(Range other) {
        assertAllNotNull(other);

        if(!overlaps(other)) {
            return NONE;
        }
        int start = Math.max(_start, other._start);
        int end = Math.min(_end, other._end);
        return new Range(start, end);
    }

    /**
     * Get the values between two {@code Range}s but not contained by either of them.
     * @param other the other {@code Range} to be evaluated.
     * @return  {@code Range} containing all values between two {@code Range}s but not contained by either of them.
     */
    public Range gapWith(Range other) {
        assertAllNotNull(other);

        if (_start == other._start || _end == other._end || _start == other._end || _end == other._start) {
            return NONE;
        } else if (_start < other._start) {
            return _end >= other._start ? NONE : closedOpen(_end, other._start);
        } else {
            return other._end >= _start ? NONE : closedOpen(other._end, _start);
        }
    }

    /**
     * Get the first value that may or may not be contained by this {@code Range}
     * @return the least value within this {@code Range} if it is not empty, otherwise the value right ahead of it
     */
    public int getStartInclusive() {
        return _start;
    }

    /**
     * Get the value before the first value that may or may not be contained by this {@code Range}
     * @return the biggest value before the first value denoting the first value of this {@code Range}
     */
    public int getStartExclusive() {
        return _start == NEGATIVE_INFINITY ? NEGATIVE_INFINITY : _start - 1;
    }

    /**
     * Get the biggest value may or may not contained by this {@code Range}
     * @return the biggest value within this {@code Range} if it is not empty, otherwise the value right ahead of it
     */
    public int getEndInclusive() {
        return _end == POSITIVE_INFINITY ? POSITIVE_INFINITY : _end - 1;
    }

    /**
     * Get the first value bigger than the largest value of the {@code Range}
     * @return the first value bigger than the largest value of the {@code Range}
     */
    public int getEndExclusive() {
        return _end;
    }

    /**
     * Get the direct children Ranges as a list.
     *
     * @param ranges Sorted Ranges instances with no one overlaps with another to be evaluated.
     * @return All Range instances contained by this Range only.
     */
    public List<Range> getChildRanges(SortedSet<Range> ranges) {
        List<Range> children = new SimpleTypedList<>();
        Range lastChild = null;
        for (Range range : ranges) {
            if (_end < range._start) {
                return children;
            } else if (equals(range)) {
            } else if (contains(range) && (lastChild == null || !lastChild.contains(range))) {
                children.add(range);
                lastChild = range;
            }
        }
        return children;
    }

    /**
     * Get the indexes within this Range as a list.
     *
     * @param indexes Sorted indexes to be evaluated.
     * @return All indexes fall into this Range but not on the boundries as a list.
     */
    public List<Integer> getWithinIndexes(SortedSet<Integer> indexes) {
        SortedSet<Integer> withinSet = indexes.subSet(_start + 1, _end - 1);
        List<Integer> children = new SimpleTypedList(Integer.class, withinSet);
        return children;
    }

    /**
     * Use the elements within this {@code Range} to construct a new {@code Range}.
     * @return a new {@code Range} that contains all insider elements of this {@code Range}.
     */
    public Range getInside() {
        if (size <= 2) {
            return NONE;
        } else {
            return new Range(_start + 1, _end - 1);
        }
    }

    /**
     * Save all values in this {@code Range} as an array with ruandom orders, which for example
     * can then be used to access elements of an array randomly.
     *
     * @return {@code Integer[]} of all values of this {@code Range} as an array with ruandom orders.
     * @throws IllegalStateException if the Range is unlimited
     */
    public Integer[] getRandomIndexes() {
        assertAllFalse(_start == NEGATIVE_INFINITY, _end == POSITIVE_INFINITY);

        //Let it throw ArithmeticException if overflow happens
        int len = Math.toIntExact(this.size);
        List<Integer> list = new SimpleTypedList();
        for (int count = 0, current = _start; current < _end; current++, count++) {
            list.add(count == 0 ? 0 : random.nextInt(count + 1), current);
        }
        return list.toArray(new Integer[len]);
    }

    /**
     * Returns a <code>CharSequence</code> that is the denoted part of given sequence specified by this {@code Range}.
     * @param charSequence  The <code>CharSequence</code> to be referred by this {@code Range}.
     * @return the specified subsequence
     */
    public CharSequence subSequence(CharSequence charSequence) {
        assertAllNotNull(charSequence);
        Asserts.assertAllTrue(Range.withinLength(this, charSequence.length()));

        return charSequence.subSequence(_start, _end);
    }

    /**
     * Returns a subString that is part of the given subsequence specified by this {@code Range}.
     * @param charSequence  The <code>CharSequence</code> to be referred by this {@code Range}.
     * @return the specified substring.
     */
    public String subString(CharSequence charSequence) {
        return subSequence(charSequence).toString();
    }

    @Override
    public int compareTo(Range o) {
        if (o == null) {
            //Range always greater than null
            return 1;
        }
        if (_start == o._start) {
            //Keep the largest one first
            return o._end - _end;
        } else {
            //Or keep the leftest one first
            return _start - o._start;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Range))
            return false;
        if (obj == this)
            return true;
        Range other = (Range) obj;
        return size == other.size && _start == other._start;
    }

    @Override
    public int hashCode(){
        return Long.hashCode(size) * 31 + _start;
    }

    @Override
    public String toString() {
        return String.format("%s, %s",
                _start <= NEGATIVE_INFINITY + 1 ? "(−∞" : String.format("[%d", _start),
                _end > POSITIVE_INFINITY - 1 ? "+∞)" : String.format("%d)", _end));
    }
    //endregion
}
