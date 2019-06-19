package io.github.cruisoring;

import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.throwables.*;
import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple3;
import io.github.cruisoring.utility.ArrayHelper;
import io.github.cruisoring.utility.SetHelper;
import io.github.cruisoring.utility.StringHelper;
import org.junit.Test;

import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.github.cruisoring.Asserts.*;
import static io.github.cruisoring.TypeHelper.*;
import static io.github.cruisoring.utility.SetHelper.asSet;

public class TypeHelperTest {

    static Object object1 = new Object[]{
            new Object[]{1, 1.2d, 3},
            new int[0],
            new int[0],
            new Number[0],
            new Comparable[]{null, 2, 3},
            new Object[]{null, 4},
            new int[][]{null, new int[]{5, 6}},
            null,
            Arrays.asList(1, null, true),
            Arrays.asList(1, null, true),
            new HashSet<Number>(Arrays.asList(2.0f, 1, 3.0))
    };
    static Object object2 = new Object[]{
            new Number[]{1, 1.2f, 4},
            new double[0],
            new Integer[0],
            new Comparable[0],
            new Number[]{null, 2, 3},
            new Integer[]{null, 4},
            new Object[]{null, new int[]{5, 6}},
            null,
            Arrays.asList(1, null, true),
            new Comparable[]{1, null, true},
            new Number[]{2.0f, 1, 3.0}
    };
    static Number nullNumber = null;
    static Comparable nullComparable = null;


    @Test
    public void testNodeEquals() {
        Logger.D(deepToString(getDeepIndexes(object1)));

        //Normal nodes are always compared as Objects, 1 vs 1, 1.2d vs 1.2f, 3 vs 4 with Same type only
        assertAllTrue(
                nodeEquals(object1, object2, new int[]{0, 0, 0}, EqualityStategy.SameTypeOnly),
                nodeEquals(object1, object2, new int[]{4, 1, 0}, EqualityStategy.SameTypeOnly),
                nodeEquals(object1, object2, new int[]{4, 2, 0}, EqualityStategy.SameTypeOnly),
                nodeEquals(object1, object2, new int[]{5, 1, 0}, EqualityStategy.SameTypeOnly),
                nodeEquals(object1, object2, new int[]{5, 1, 0}, EqualityStategy.SameTypeOnly),
                nodeEquals(object1, object2, new int[]{6, 1, 0, 0}, EqualityStategy.SameTypeOnly),
                nodeEquals(object1, object2, new int[]{6, 1, 1, 0}, EqualityStategy.SameTypeOnly),
                nodeEquals(object1, object2, new int[]{7, -1}, EqualityStategy.SameTypeOnly),
                nodeEquals(object1, object2, new int[]{8, 0, 0}, EqualityStategy.SameTypeOnly),
                nodeEquals(object1, object2, new int[]{8, 1, -1}, EqualityStategy.SameTypeOnly),
                nodeEquals(object1, object2, new int[]{8, 2, 0}, EqualityStategy.SameTypeOnly),
                nodeEquals(object1, object2, new int[]{9, 0, 0}, EqualityStategy.SameTypeOnly),
                nodeEquals(object1, object2, new int[]{9, 2, 0}, EqualityStategy.SameTypeOnly),
                nodeEquals(object1, object2, new int[]{10, 0, 0}, EqualityStategy.SameTypeOnly),
                nodeEquals(object1, object2, new int[]{10, 1, 0}, EqualityStategy.SameTypeOnly),
                nodeEquals(object1, object2, new int[]{10, 2, 0}, EqualityStategy.SameTypeOnly)
        );
        assertAllFalse(
                nodeEquals(object1, object2, new int[]{0, 1, 0}, EqualityStategy.SameTypeOnly),
                nodeEquals(object1, object2, new int[]{0, 2, 0}, EqualityStategy.SameTypeOnly),
                nodeEquals(object1, object2, new int[]{1, -2}, EqualityStategy.SameTypeOnly),
                nodeEquals(object1, object2, new int[]{2, -2}, EqualityStategy.SameTypeOnly),
                nodeEquals(object1, object2, new int[]{3, -2}, EqualityStategy.SameTypeOnly),
                nodeEquals(object1, object2, new int[]{4, 0, -1}, EqualityStategy.SameTypeOnly),
                nodeEquals(object1, object2, new int[]{5, 0, -1}, EqualityStategy.SameTypeOnly),
                nodeEquals(object1, object2, new int[]{6, 0, -1}, EqualityStategy.SameTypeOnly),
                nodeEquals(object1, object2, new int[]{9, 1, -1}, EqualityStategy.SameTypeOnly)
        );

        //Both as BetweenAssignableTypes
        assertAllTrue(
                nodeEquals(object1, object2, new int[]{0, 0, 0}, EqualityStategy.BetweenAssignableTypes),
                nodeEquals(object1, object2, new int[]{4, 1, 0}, EqualityStategy.BetweenAssignableTypes),
                nodeEquals(object1, object2, new int[]{4, 2, 0}, EqualityStategy.BetweenAssignableTypes),
                nodeEquals(object1, object2, new int[]{5, 1, 0}, EqualityStategy.BetweenAssignableTypes),
                nodeEquals(object1, object2, new int[]{5, 1, 0}, EqualityStategy.BetweenAssignableTypes),
                nodeEquals(object1, object2, new int[]{6, 1, 0, 0}, EqualityStategy.BetweenAssignableTypes),
                nodeEquals(object1, object2, new int[]{6, 1, 1, 0}, EqualityStategy.BetweenAssignableTypes),
                nodeEquals(object1, object2, new int[]{7, -1}, EqualityStategy.BetweenAssignableTypes),
                nodeEquals(object1, object2, new int[]{8, 0, 0}, EqualityStategy.BetweenAssignableTypes),
                nodeEquals(object1, object2, new int[]{8, 1, -1}, EqualityStategy.BetweenAssignableTypes),
                nodeEquals(object1, object2, new int[]{8, 2, 0}, EqualityStategy.BetweenAssignableTypes),
                nodeEquals(object1, object2, new int[]{9, 0, 0}, EqualityStategy.BetweenAssignableTypes),
                nodeEquals(object1, object2, new int[]{9, 2, 0}, EqualityStategy.BetweenAssignableTypes),
                nodeEquals(object1, object2, new int[]{10, 0, 0}, EqualityStategy.BetweenAssignableTypes),
                nodeEquals(object1, object2, new int[]{10, 1, 0}, EqualityStategy.BetweenAssignableTypes),
                nodeEquals(object1, object2, new int[]{10, 2, 0}, EqualityStategy.BetweenAssignableTypes),
                nodeEquals(object1, object2, new int[]{2, -2}, EqualityStategy.BetweenAssignableTypes),
                nodeEquals(object1, object2, new int[]{5, 0, -1}, EqualityStategy.BetweenAssignableTypes),
                nodeEquals(object1, object2, new int[]{6, 0, -1}, EqualityStategy.BetweenAssignableTypes)
        );

        assertAllFalse(
                nodeEquals(object1, object2, new int[]{0, 1, 0}, EqualityStategy.BetweenAssignableTypes),
                nodeEquals(object1, object2, new int[]{0, 2, 0}, EqualityStategy.BetweenAssignableTypes),
                nodeEquals(object1, object2, new int[]{1, -2}, EqualityStategy.BetweenAssignableTypes),
                nodeEquals(object1, object2, new int[]{3, -2}, EqualityStategy.BetweenAssignableTypes),
                nodeEquals(object1, object2, new int[]{4, 0, -1}, EqualityStategy.BetweenAssignableTypes),
                nodeEquals(object1, object2, new int[]{9, 1, -1}, EqualityStategy.BetweenAssignableTypes)
        );

        //Both as TypeIgnored
        assertAllTrue(
                nodeEquals(object1, object2, new int[]{0, 0, 0}, EqualityStategy.TypeIgnored),
                nodeEquals(object1, object2, new int[]{4, 1, 0}, EqualityStategy.TypeIgnored),
                nodeEquals(object1, object2, new int[]{4, 2, 0}, EqualityStategy.TypeIgnored),
                nodeEquals(object1, object2, new int[]{5, 1, 0}, EqualityStategy.TypeIgnored),
                nodeEquals(object1, object2, new int[]{5, 1, 0}, EqualityStategy.TypeIgnored),
                nodeEquals(object1, object2, new int[]{6, 1, 0, 0}, EqualityStategy.TypeIgnored),
                nodeEquals(object1, object2, new int[]{6, 1, 1, 0}, EqualityStategy.TypeIgnored),
                nodeEquals(object1, object2, new int[]{7, -1}, EqualityStategy.TypeIgnored),
                nodeEquals(object1, object2, new int[]{8, 0, 0}, EqualityStategy.TypeIgnored),
                nodeEquals(object1, object2, new int[]{8, 1, -1}, EqualityStategy.TypeIgnored),
                nodeEquals(object1, object2, new int[]{8, 2, 0}, EqualityStategy.TypeIgnored),
                nodeEquals(object1, object2, new int[]{9, 0, 0}, EqualityStategy.TypeIgnored),
                nodeEquals(object1, object2, new int[]{9, 2, 0}, EqualityStategy.TypeIgnored),
                nodeEquals(object1, object2, new int[]{10, 0, 0}, EqualityStategy.TypeIgnored),
                nodeEquals(object1, object2, new int[]{10, 1, 0}, EqualityStategy.TypeIgnored),
                nodeEquals(object1, object2, new int[]{10, 2, 0}, EqualityStategy.TypeIgnored),
                nodeEquals(object1, object2, new int[]{1, -2}, EqualityStategy.TypeIgnored),
                nodeEquals(object1, object2, new int[]{2, -2}, EqualityStategy.TypeIgnored),
                nodeEquals(object1, object2, new int[]{3, -2}, EqualityStategy.TypeIgnored),
                nodeEquals(object1, object2, new int[]{4, 0, -1}, EqualityStategy.TypeIgnored),
                nodeEquals(object1, object2, new int[]{5, 0, -1}, EqualityStategy.TypeIgnored),
                nodeEquals(object1, object2, new int[]{6, 0, -1}, EqualityStategy.TypeIgnored),
                nodeEquals(object1, object2, new int[]{9, 1, -1}, EqualityStategy.TypeIgnored)
        );

        assertAllFalse(
                nodeEquals(object1, object2, new int[]{0, 1, 0}, EqualityStategy.TypeIgnored),
                nodeEquals(object1, object2, new int[]{0, 2, 0}, EqualityStategy.TypeIgnored)
        );

        Revokable.register(() -> TypeHelper.EMPTY_ARRAY_NODE, emptyArray -> TypeHelper.EMPTY_ARRAY_NODE = emptyArray, TypeHelper.NULL_NODE);
        Revokable.register(() -> TypeHelper.EMPTY_COLLECTION_NODE, empty -> TypeHelper.EMPTY_COLLECTION_NODE = empty, TypeHelper.NULL_NODE);
        assertEquals("[[0, 0, 0], [0, 1, 0], [0, 2, 0], [1, -1], [2, -1], [3, -1], [4, 0, -1], [4, 1, 0], [4, 2, 0], [5, 0, -1], [5, 1, 0], [6, 0, -1], [6, 1, 0, 0], [6, 1, 1, 0], [7, -1], [8, 0, 0], [8, 1, -1], [8, 2, 0], [9, 0, 0], [9, 1, -1], [9, 2, 0], [10, 0, 0], [10, 1, 0], [10, 2, 0]]",
                deepToString(getDeepIndexes(object1)));

        assertAllTrue(
                nodeEquals(object1, object2, new int[]{0, 0, 0}, EqualityStategy.EmptyAsNull),
                nodeEquals(object1, object2, new int[]{4, 1, 0}, EqualityStategy.EmptyAsNull),
                nodeEquals(object1, object2, new int[]{4, 2, 0}, EqualityStategy.EmptyAsNull),
                nodeEquals(object1, object2, new int[]{5, 1, 0}, EqualityStategy.EmptyAsNull),
                nodeEquals(object1, object2, new int[]{5, 1, 0}, EqualityStategy.EmptyAsNull),
                nodeEquals(object1, object2, new int[]{6, 1, 0, 0}, EqualityStategy.EmptyAsNull),
                nodeEquals(object1, object2, new int[]{6, 1, 1, 0}, EqualityStategy.EmptyAsNull),
                nodeEquals(object1, object2, new int[]{7, -1}, EqualityStategy.EmptyAsNull),
                nodeEquals(object1, object2, new int[]{8, 0, 0}, EqualityStategy.EmptyAsNull),
                nodeEquals(object1, object2, new int[]{8, 1, -1}, EqualityStategy.EmptyAsNull),
                nodeEquals(object1, object2, new int[]{8, 2, 0}, EqualityStategy.EmptyAsNull),
                nodeEquals(object1, object2, new int[]{9, 0, 0}, EqualityStategy.EmptyAsNull),
                nodeEquals(object1, object2, new int[]{9, 2, 0}, EqualityStategy.EmptyAsNull),
                nodeEquals(object1, object2, new int[]{10, 0, 0}, EqualityStategy.EmptyAsNull),
                nodeEquals(object1, object2, new int[]{10, 1, 0}, EqualityStategy.EmptyAsNull),
                nodeEquals(object1, object2, new int[]{10, 2, 0}, EqualityStategy.EmptyAsNull),
                nodeEquals(object1, object2, new int[]{1, -1}, EqualityStategy.EmptyAsNull),
                nodeEquals(object1, object2, new int[]{2, -1}, EqualityStategy.EmptyAsNull),
                nodeEquals(object1, object2, new int[]{3, -1}, EqualityStategy.EmptyAsNull),
                nodeEquals(object1, object2, new int[]{4, 0, -1}, EqualityStategy.EmptyAsNull),
                nodeEquals(object1, object2, new int[]{5, 0, -1}, EqualityStategy.EmptyAsNull),
                nodeEquals(object1, object2, new int[]{6, 0, -1}, EqualityStategy.EmptyAsNull),
                nodeEquals(object1, object2, new int[]{9, 1, -1}, EqualityStategy.EmptyAsNull)
        );

        assertAllFalse(
                nodeEquals(object1, object2, new int[]{0, 1, 0}, EqualityStategy.EmptyAsNull),
                nodeEquals(object1, object2, new int[]{0, 2, 0}, EqualityStategy.EmptyAsNull)
        );

        Revokable.revokeAll();
    }

    @Test
    public void testGetElement() {
        //null or simple Object shall throw IllegalStateException
        assertException(() -> getElement(null, 1), IllegalStateException.class);
        assertException(() -> getElement(123, 1), IllegalStateException.class);

        //Empty Array or Collection, or index out of range shall throw IllegalArgumentException
        assertException(() -> getElement(new int[0], 0), IllegalArgumentException.class);
        assertException(() -> getElement(Arrays.asList(), 0), IllegalArgumentException.class);
        assertException(() -> getElement(new Number[]{1, 2, 3}, 3), IllegalArgumentException.class);
        assertException(() -> getElement(new Number[]{1, 2, 3}, -1), IllegalStateException.class);
        assertException(() -> getElement(new HashSet<>(Arrays.asList(1, 2)), -1), IllegalStateException.class);
        assertException(() -> getElement(new HashSet<>(Arrays.asList(1, 2)), 2), IllegalArgumentException.class);

        //get element of an Array
        assertEquals(1, getElement(new Number[]{1, 2, 3}, 0));
        assertEquals(null, getElement(new Number[]{1, 2, null}, 2));
        assertEquals(null, getElement(new Number[][]{null, new Number[]{1}, new Number[]{2, 3, null}}, 0));
        assertEquals(new Number[]{1}, getElement(new Number[][]{null, new Number[]{1}, new Number[]{2, 3, null}}, 1));
        assertEquals(new Number[]{2, 3, null}, getElement(new Number[][]{null, new Number[]{1}, new Number[]{2, 3, null}}, 2));

        //get element of a list
        assertEquals(1, getElement(Arrays.asList(1, 2, 3), 0));
        assertEquals(null, getElement(Arrays.asList(1, 2, null), 2));

        //get element of a set
        assertEquals(1, getElement(asSet(1, 2, 3), 0));
        assertEquals(null, getElement(new LinkedHashSet(Arrays.asList(1, 2, null)), 2));

        //get element of a multi-dimensional array
        assertEquals(null, getElement(new Number[][]{null, new Number[]{1}, new Number[]{2, 3, null}}, 0));
        assertEquals(new Number[]{1}, getElement(new Number[][]{null, new Number[]{1}, new Number[]{2, 3, null}}, 1));
        assertEquals(new Number[]{2, 3, null}, getElement(new Number[][]{null, new Number[]{1}, new Number[]{2, 3, null}}, 2));

        //get element of a Collection of collection
        assertEquals(null, getElement(Arrays.asList(null, Arrays.asList(1, 2), asSet(3)), 0));
        assertEquals(Arrays.asList(1, 2), getElement(Arrays.asList(null, Arrays.asList(1, 2), asSet(3)), 1));
        assertEquals(asSet(3), getElement(Arrays.asList(null, Arrays.asList(1, 2), asSet(3)), 2));
    }

    @Test
    public void getDeepElement_withSimpleValue() {
        assertAllNull(getDeepElement(null, new int[0]));
        assertException(() -> getDeepElement(null, new int[]{0}), IllegalStateException.class);

        assertEquals(33, getDeepElement(33, new int[0]));
        assertException(() -> getDeepElement(33, new int[]{0}), IllegalStateException.class);
        assertException(() -> getDeepElement(33, new int[]{0, 0}), IllegalStateException.class);

        assertEquals("a string", getDeepElement("a string", new int[0]));
        assertException(() -> getDeepElement("a string", new int[]{1}), IllegalStateException.class);
        assertException(() -> getDeepElement("a string", new int[]{0, 0}), IllegalStateException.class);
    }

    @Test
    public void getDeepElement_withEmptyArray() {
        assertEquals(new int[0], getDeepElement(new int[0], new int[0]));
        assertException(() -> getDeepElement(new int[0], new int[]{0}), IllegalArgumentException.class);

        assertEquals(new String[]{}, getDeepElement(new String[]{}, new int[0]));
        assertException(() -> getDeepElement(new String[]{}, new int[]{0}), IllegalArgumentException.class);
        assertException(() -> getDeepElement(new String[]{}, new int[]{0, 0}), IllegalArgumentException.class);

        assertEquals(new Number[][]{}, getDeepElement(new Number[][]{}, new int[0]));
        assertException(() -> getDeepElement(new Number[][]{}, new int[]{0}), IllegalArgumentException.class);
        assertException(() -> getDeepElement(new String[][]{}, new int[]{0, 0}), IllegalArgumentException.class);
    }

    @Test
    public void getDeepElement_withEmptyCollection() {
        assertEquals(new ArrayList(), getDeepElement(new ArrayList(), new int[0]));
        assertException(() -> getDeepElement(new int[0], new int[]{0}), IllegalArgumentException.class);

        assertEquals(new String[]{}, getDeepElement(new String[]{}, new int[0]));
        assertException(() -> getDeepElement(new String[]{}, new int[]{0}), IllegalArgumentException.class);
        assertException(() -> getDeepElement(new String[]{}, new int[]{0, 0}), IllegalArgumentException.class);
    }

    @Test
    public void getDeepElement_withSimpleArray() {
        assertEquals(new int[]{1, 2, 3}, getDeepElement(new int[]{1, 2, 3}, new int[0]));
        assertEquals(1, getDeepElement(new int[]{1, 2, 3}, new int[]{0}));
        assertEquals(3, getDeepElement(new int[]{1, 2, 3}, new int[]{2}));
        assertException(() -> getDeepElement(new int[]{1, 2, 3}, new int[]{3}), IllegalArgumentException.class);

        assertEquals(new String[]{"a", null}, getDeepElement(new String[]{"a", null}, new int[0]));
        assertEquals("a", getDeepElement(new String[]{"a", null}, new int[]{0}));
        assertEquals(null, getDeepElement(new String[]{"a", null}, new int[]{1}));
        assertException(() -> getDeepElement(new String[]{"a", null}, new int[]{2}), IllegalArgumentException.class);
        assertException(() -> getDeepElement(new String[]{"a", null}, new int[]{0, 0}), IllegalStateException.class);
    }

    @Test
    public void getDeepElement_withSimpleCollection() {
        List list = Arrays.asList(1, 2.2, null, "abc", true);
        assertEquals(list, getDeepElement(list, new int[0]));
        assertEquals(1, getDeepElement(list, new int[]{0}));
        assertEquals(2.2, getDeepElement(list, new int[]{1}));
        assertEquals(null, getDeepElement(list, new int[]{2}));
        assertEquals(true, getDeepElement(list, new int[]{4}));
        assertException(() -> getDeepElement(new int[0], new int[]{5}), IllegalArgumentException.class);
    }

    @Test
    public void getDeepElement_withMultiDimensionArray() {
        Object array = new int[][]{new int[]{1}, null, new int[]{2, 3}};
        assertEquals(1, getDeepElement(array, new int[]{0, 0}));
        assertEquals(null, getDeepElement(array, new int[]{1}));
        assertEquals(new int[]{2, 3}, getDeepElement(array, new int[]{2}));
        assertEquals(2, getDeepElement(array, new int[]{2, 0}));
        assertEquals(3, getDeepElement(array, new int[]{2, 1}));
        assertException(() -> getDeepElement(array, new int[]{1, 0}), IllegalStateException.class);
        assertException(() -> getDeepElement(array, new int[]{2, 2}), IllegalArgumentException.class);
    }

    @Test
    public void getDeepElement_withCollectionOfCollection() {
        List list = Arrays.asList(new int[]{1}, null, new int[]{2, 3}, Arrays.asList(4, null, 6));
        assertEquals(1, getDeepElement(list, new int[]{0, 0}));
        assertEquals(null, getDeepElement(list, new int[]{1}));
        assertEquals(new int[]{2, 3}, getDeepElement(list, new int[]{2}));
        assertEquals(2, getDeepElement(list, new int[]{2, 0}));
        assertEquals(3, getDeepElement(list, new int[]{2, 1}));
        assertEquals(4, getDeepElement(list, new int[]{3, 0}));
        assertEquals(null, getDeepElement(list, new int[]{3, 1}));
        assertEquals(6, getDeepElement(list, new int[]{3, 2}));
        assertException(() -> getDeepElement(list, new int[]{1, 0}), IllegalStateException.class);
        assertException(() -> getDeepElement(list, new int[]{2, 2}), IllegalArgumentException.class);
        assertException(() -> getDeepElement(list, new int[]{4}), IllegalArgumentException.class);
    }

    @Test
    public void testGetDeepIndexes_ofSimpleOrArray() {
        Object target = null;
        int[][] deepIndexes = getDeepIndexes(target);
        assertEquals(new int[][]{new int[]{-1}}, deepIndexes);

        target = 33;
        deepIndexes = getDeepIndexes(target);
        assertEquals(new int[][]{new int[]{0}}, deepIndexes);

        target = new Object[0];
        deepIndexes = getDeepIndexes(target);
        assertEquals(new int[][]{new int[]{-2}}, deepIndexes);

        target = new Integer[]{null};
        deepIndexes = getDeepIndexes(target);
        assertEquals(new int[][]{new int[]{0, -1}}, deepIndexes);

        target = new Integer[]{null, null};
        deepIndexes = getDeepIndexes(target);
        assertEquals(new int[][]{new int[]{0, -1}, new int[]{1, -1}}, deepIndexes);

        target = new Object[]{1,
                new int[]{2, 3},
                new Object[]{null, '5', '6', new Character[]{'7', null}, null},
                null,
                11.0,
                new char[0],
                new ArrayList(),
                new char[0][],
                new int[][]{new int[0], null}};
        deepIndexes = getDeepIndexes(target);
        assertEquals(new int[][]{new int[]{0, 0}, new int[]{1, 0, 0}, new int[]{1, 1, 0}, new int[]{2, 0, -1}, new int[]{2, 1, 0},
                new int[]{2, 2, 0}, new int[]{2, 3, 0, 0}, new int[]{2, 3, 1, -1}, new int[]{2, 4, -1}, new int[]{3, -1}, new int[]{4, 0}
                , new int[]{5, -2}, new int[]{6, -3}, new int[]{7, -2}, new int[]{8, 0, -2}, new int[]{8, 1, -1}}, deepIndexes);
        assertEquals(16, deepIndexes.length);

        Object array = new Object[]{1,
                new int[]{2, 3},
                new Object[]{null, '5', '6', null},
                new char[0],
                11.0};
        deepIndexes = getDeepIndexes(array);
        assertEquals(new int[][]{new int[]{0, 0}, new int[]{1, 0, 0}, new int[]{1, 1, 0}, new int[]{2, 0, -1}, new int[]{2, 1, 0},
                new int[]{2, 2, 0}, new int[]{2, 3, -1}, new int[]{3, -2}, new int[]{4, 0}}, deepIndexes);
    }

    @Test
    public void testGetDeepIndexes_ofCollections() {
        assertEquals(new int[][]{new int[]{-3}}, getDeepIndexes(ArrayHelper.asList()));

        Object target = ArrayHelper.asList(null);
        int[][] deepIndexes = getDeepIndexes(target);
        assertEquals(new int[][]{new int[]{0, -1}}, deepIndexes);

        target = ArrayHelper.asList(null, null);
        deepIndexes = getDeepIndexes(target);
        assertEquals(new int[][]{new int[]{0, -1}, new int[]{1, -1}}, deepIndexes);

        target = ArrayHelper.asList(1,
                new int[]{2, 3},
                ArrayHelper.asList(null, '5', '6', ArrayHelper.asList('7', null), null),
                null,
                11.0,
                new char[0],
                new ArrayList(),
                new char[0][],
                new int[][]{new int[0], null});
        deepIndexes = getDeepIndexes(target);
        assertEquals(new int[][]{new int[]{0, 0}, new int[]{1, 0, 0}, new int[]{1, 1, 0}, new int[]{2, 0, -1}, new int[]{2, 1, 0},
                new int[]{2, 2, 0}, new int[]{2, 3, 0, 0}, new int[]{2, 3, 1, -1}, new int[]{2, 4, -1}, new int[]{3, -1}, new int[]{4, 0}
                , new int[]{5, -2}, new int[]{6, -3}, new int[]{7, -2}, new int[]{8, 0, -2}, new int[]{8, 1, -1}}, deepIndexes);
        assertEquals(16, deepIndexes.length);

        target = ArrayHelper.asList(1,
                SetHelper.asHashSet(2, 3),
                new Object[]{null, '5', '6', null},
                new char[0],
                11.0);
        deepIndexes = getDeepIndexes(target);
        assertEquals(new int[][]{new int[]{0, 0}, new int[]{1, 0, 0}, new int[]{1, 1, 0}, new int[]{2, 0, -1}, new int[]{2, 1, 0},
                new int[]{2, 2, 0}, new int[]{2, 3, -1}, new int[]{3, -2}, new int[]{4, 0}}, deepIndexes);

        List list = Arrays.asList(0, null, true, new int[]{1, 2}, new char[0], null, Arrays.asList(5, null), new boolean[][]{null, new boolean[]{true, false}});
        deepIndexes = getDeepIndexes(list);
        Logger.D(deepToString(deepIndexes));
        assertEquals(new int[][]{new int[]{0, 0}, new int[]{1, -1}, new int[]{2, 0}, new int[]{3, 0, 0}, new int[]{3, 1, 0},
                new int[]{4, -2}, new int[]{5, -1}, new int[]{6, 0, 0}, new int[]{6, 1, -1}, new int[]{7, 0, -1}, new int[]{7, 1, 0, 0}, new int[]{7, 1, 1, 0}
        }, deepIndexes);
    }

    @Test
    public void testDeepToString() {
        assertEquals("null", deepToString(null));
        assertEquals("1", deepToString(1));
        assertEquals("a String", deepToString("a String"));

        assertEquals("[null, 1, true, abc, x]", deepToString(new Object[]{null, 1, true, "abc", 'x'}));
        assertEquals("[null, 1, true, abc, x]", deepToString(Arrays.asList(null, 1, true, "abc", 'x')));

        assertEquals("[null, [1, null], [true, abc, x]]", deepToString(new Object[]{null, new Object[]{1, null}, new Object[]{true, "abc", 'x'}}));
        assertEquals("[null, [1, null], [true, abc, x]]", deepToString(Arrays.asList(null, Arrays.asList(1, null), Arrays.asList(true, "abc", 'x'))));
        assertEquals("[null, [1, null], [true, abc, x]]", deepToString(Arrays.asList(null, Arrays.asList(1, null), new Object[]{true, "abc", 'x'})));
    }

    @Test
    public void testValueEquals_withSimpleValues() {
        assertEquals(1, Integer.valueOf(1));
        assertEquals('a', Character.valueOf('a'));
        assertEquals(Boolean.FALSE, false);
        assertEquals(Double.valueOf(3.3), 3.3d);
        assertEquals(Float.MIN_VALUE, Float.valueOf(Float.MIN_VALUE));
        assertEquals(Long.MAX_VALUE, Long.valueOf(Long.MAX_VALUE));
        assertEquals(null, null);

        assertNotEquals((byte) 33, (short) 33);
        assertNotEquals(null, true);
        assertNotEquals(new int[0], 0);
        assertNotEquals(null, Boolean.TRUE);
    }

    @Test
    public void testValueEquals_withTwoArrays() {
        int[] array1 = new int[]{1, 2, 3};
        Integer[] array2 = new Integer[]{1, 2, 3};

        assertEquals(array1, array2);

        assertEquals(new Object[]{new int[]{3, 2, 1}, (short) 3, true, null, new String[]{"S1", "S2"}, new char[0], 1.1d},
                new Object[]{new Integer[]{3, 2, 1}, Short.valueOf("3"), Boolean.TRUE, null, new String[]{"S1", "S2"}, new Character[0], Double.valueOf(1.1)});

        assertEquals(new Object[]{3, (short) 3, true, null, "S1", "S2", 1.1d, DayOfWeek.WEDNESDAY},
                new Comparable[]{3, Short.valueOf("3"), true, null, "S1", "S2", Double.valueOf(1.1d), DayOfWeek.WEDNESDAY});
    }

    @Test
    public void testValueEquals_withDifferentStrategies() {
        assertEquals(new Object[]{new int[]{3, 2, 1}, new short[0], 1.1d, null, new String[]{"S1", "S2", null}, new Integer[]{null, 23}},
                new Object[]{new Integer[]{3, 2, 1}, new Short[0], Double.valueOf(1.1), null, new Comparable[]{"S1", "S2", null}, new Number[]{null, 23}});
        assertEquals(new Object[]{new int[]{3, 2, 1}, new short[0], 1.1d, null, new String[]{"S1", "S2", null}, new Integer[]{null, 23}},
                new Object[]{new Integer[]{3, 2, 1}, new Short[0], Double.valueOf(1.1), null, new Comparable[]{"S1", "S2", null}, new Number[]{null, 23}});
        assertAllTrue(
                valueEquals(new Object[]{new int[]{3, 2, 1}, new short[0], 1.1d, null, new String[]{"S1", "S2", null}, new Integer[]{null, 23}},
                new Object[]{new Integer[]{3, 2, 1}, new Short[0], Double.valueOf(1.1), null, new Comparable[]{"S1", "S2", null}, new Number[]{null, 23}},
                        EqualityStategy.TypeIgnored));
        assertAllFalse(
                valueEquals(new Object[]{new int[]{3, 2, 1}, new short[0], 1.1d, null, new String[]{"S1", "S2", null}, new Integer[]{null, 23}},
                new Object[]{new Integer[]{3, 2, 1}, new Short[0], Double.valueOf(1.1), null, new Comparable[]{"S1", "S2", null}, new Number[]{null, 23}},
                        EqualityStategy.SameTypeOnly));
    }

    @Test
    public void getGenericInfo() {
        FunctionThrowable<Integer, List<Integer>> listFactory = i -> new ArrayList<Integer>();
        Tuple3<Boolean, Class[], Class> genericInfo = lambdaGenericInfoRepository.retrieve(listFactory);
        assertEquals(Tuple.create(true, new Class[]{Integer.class}, List.class), genericInfo);

        BiFunctionThrowable<Integer, Integer, Boolean> func = (n1, n2) -> n1 + 12 > n2;
        genericInfo = lambdaGenericInfoRepository.retrieve(func);
        assertEquals(Tuple.create(true, new Class[]{Integer.class, Integer.class}, Boolean.class), genericInfo);
    }

    @Test
    public void getClassPredicate_withPrimitiveTypes_shallMatchCorrectly() {
        Predicate<Class> classPredicate = getClassEqualitor(int.class);
        assertAllTrue(classPredicate.test(int.class) && classPredicate.test(Integer.class));
        assertAllFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(int[].class) || classPredicate.test(Integer[].class));

        classPredicate = getClassEqualitor(char.class);
        assertAllTrue(classPredicate.test(char.class) && classPredicate.test(Character.class));
        assertAllFalse(classPredicate.test(null) || classPredicate.test(int.class) ||
                classPredicate.test(char[].class) || classPredicate.test(Character[].class));

        classPredicate = getClassEqualitor(boolean.class);
        assertAllTrue(classPredicate.test(boolean.class) && classPredicate.test(Boolean.class));
        assertAllFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(boolean[].class) || classPredicate.test(Boolean[].class));

        classPredicate = getClassEqualitor(byte.class);
        assertAllTrue(classPredicate.test(byte.class) && classPredicate.test(Byte.class));
        assertAllFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(byte[].class) || classPredicate.test(Byte[].class));

        classPredicate = getClassEqualitor(double.class);
        assertAllTrue(classPredicate.test(double.class) && classPredicate.test(Double.class));
        assertAllFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(double[].class) || classPredicate.test(Double[].class));

        classPredicate = getClassEqualitor(float.class);
        assertAllTrue(classPredicate.test(float.class) && classPredicate.test(Float.class));
        assertAllFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(float[].class) || classPredicate.test(Float[].class));

        classPredicate = getClassEqualitor(short.class);
        assertAllTrue(classPredicate.test(short.class) && classPredicate.test(Short.class));
        assertAllFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(short[].class) || classPredicate.test(Short[].class));

        classPredicate = getClassEqualitor(long.class);
        assertAllTrue(classPredicate.test(long.class) && classPredicate.test(Long.class));
        assertAllFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(long[].class) || classPredicate.test(Long[].class));
    }

    @Test
    public void getClassPredicate_withWrapperTypes_shallMatchCorrectly() {
        Predicate<Class> classPredicate = getClassEqualitor(Integer.class);
        assertAllTrue(classPredicate.test(int.class) && classPredicate.test(Integer.class));
        assertAllFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(int[].class) || classPredicate.test(Integer[].class));

        classPredicate = getClassEqualitor(Character.class);
        assertAllTrue(classPredicate.test(char.class) && classPredicate.test(Character.class));
        assertAllFalse(classPredicate.test(null) || classPredicate.test(int.class) ||
                classPredicate.test(char[].class) || classPredicate.test(Character[].class));

        classPredicate = getClassEqualitor(Boolean.class);
        assertAllTrue(classPredicate.test(boolean.class) && classPredicate.test(Boolean.class));
        assertAllFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(boolean[].class) || classPredicate.test(Boolean[].class));

        classPredicate = getClassEqualitor(Byte.class);
        assertAllTrue(classPredicate.test(byte.class) && classPredicate.test(Byte.class));
        assertAllFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(byte[].class) || classPredicate.test(Byte[].class));

        classPredicate = getClassEqualitor(Double.class);
        assertAllTrue(classPredicate.test(double.class) && classPredicate.test(Double.class));
        assertAllFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(double[].class) || classPredicate.test(Double[].class));

        classPredicate = getClassEqualitor(Float.class);
        assertAllTrue(classPredicate.test(float.class) && classPredicate.test(Float.class));
        assertAllFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(float[].class) || classPredicate.test(Float[].class));

        classPredicate = getClassEqualitor(Short.class);
        assertAllTrue(classPredicate.test(short.class) && classPredicate.test(Short.class));
        assertAllFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(short[].class) || classPredicate.test(Short[].class));

        classPredicate = getClassEqualitor(Long.class);
        assertAllTrue(classPredicate.test(long.class) && classPredicate.test(Long.class));
        assertAllFalse(classPredicate.test(null) || classPredicate.test(char.class) ||
                classPredicate.test(long[].class) || classPredicate.test(Long[].class));
    }

    @Test
    public void getClassPredicate_withPrimitiveArraysAndTheirWrappersType_shallMatchCorrectly() {
        Predicate<Class> classPredicate = getClassEqualitor(int[].class);
        assertAllTrue(classPredicate.test(int[].class) && classPredicate.test(Integer[].class));
        assertAllFalse(classPredicate.test(null) || classPredicate.test(Object[].class)
                || classPredicate.test(int.class) || classPredicate.test(Integer.class)
                || classPredicate.test(int[][].class) || classPredicate.test(Integer[][].class));

        classPredicate = getClassEqualitor(Integer[].class);
        assertAllTrue(classPredicate.test(int[].class) && classPredicate.test(Integer[].class));
        assertAllFalse(classPredicate.test(null) || classPredicate.test(Object[].class)
                || classPredicate.test(int.class) || classPredicate.test(Integer.class)
                || classPredicate.test(int[][].class) || classPredicate.test(Integer[][].class));

        classPredicate = getClassEqualitor(Integer[][].class);
        assertAllTrue(classPredicate.test(int[][].class) && classPredicate.test(Integer[][].class));
        assertAllFalse(classPredicate.test(null) || classPredicate.test(Object[][].class)
                || classPredicate.test(int[].class) || classPredicate.test(Integer.class)
                || classPredicate.test(int[][][].class) || classPredicate.test(Integer[][][].class));

    }

    @Test
    public void isPrimitive_withSimpleClass_getExpectedResults() {
        assertAllTrue(isPrimitive(int.class) && isPrimitive(char.class) && isPrimitive(byte.class) && isPrimitive(float.class)
                && isPrimitive(boolean.class) && isPrimitive(double.class) && isPrimitive(short.class) && isPrimitive(long.class));

        assertAllFalse(isPrimitive(Integer.class) || isPrimitive(Character.class) || isPrimitive(Byte.class) || isPrimitive(Float.class)
                || isPrimitive(Boolean.class) || isPrimitive(Double.class) || isPrimitive(Short.class) || isPrimitive(Long.class));

        assertAllFalse(isPrimitive(DayOfWeek.class) || isPrimitive(Object.class) || isPrimitive(Comparable.class) || isPrimitive(A.class)
                || isPrimitive(ITest1.class) || isPrimitive(new Integer(3).getClass()));
    }

    @Test
    public void isPrimitive_withArrayClass_getExpectedResults() {
        assertAllTrue(isPrimitive(int[].class),
                isPrimitive(char[].class),
                isPrimitive(byte[].class),
                isPrimitive(float[].class),
                isPrimitive(boolean[][].class),
                isPrimitive(double[][].class),
                isPrimitive(short[][].class),
                isPrimitive(long[][][].class));

        assertAllFalse(isPrimitive(Integer[][][].class),
                isPrimitive(Character[][].class),
                isPrimitive(Byte[].class),
                isPrimitive(Float[][][].class),
                isPrimitive(Boolean[].class),
                isPrimitive(Double[].class),
                isPrimitive(Short[].class),
                isPrimitive(Long[].class));

        assertAllFalse(isPrimitive(DayOfWeek[].class), isPrimitive(Object[][][].class), isPrimitive(Comparable[].class),
                isPrimitive(A[].class), isPrimitive(ITest1[][].class));

    }

    @Test
    public void getDefaultValue_withSimpleClass_getExpectedResults() {
        assertEquals(0, getDefaultValue(int.class));
        assertEquals((short) 0, getDefaultValue(short.class));
        assertEquals((byte) 0, getDefaultValue(byte.class));
        assertEquals((char) 0, getDefaultValue(char.class));
        assertEquals(0L, getDefaultValue(long.class));
        assertEquals(0d, getDefaultValue(double.class));
        assertEquals(0f, getDefaultValue(float.class));
        assertEquals(false, getDefaultValue(boolean.class));

        assertEquals(0, getDefaultValue(Integer.class));
        assertEquals((short) 0, getDefaultValue(Short.class));
        assertEquals((byte) 0, getDefaultValue(Byte.class));
        assertEquals((char) 0, getDefaultValue(Character.class));
        assertEquals(0L, getDefaultValue(Long.class));
        assertEquals(0d, getDefaultValue(Double.class));
        assertEquals(0f, getDefaultValue(Float.class));
        assertEquals(false, getDefaultValue(Boolean.class));

        assertEquals(null, getDefaultValue(Object.class));
        assertEquals(null, getDefaultValue(String.class));
        assertEquals(null, getDefaultValue(Comparable.class));
        assertEquals(null, getDefaultValue(DayOfWeek.class));
        assertEquals(null, getDefaultValue(A.class));
        assertEquals(null, getDefaultValue(ITest1.class));
    }

    @Test
    public void getDefaultValue_withArrayClass_getExpectedResults() {
        if (StringHelper.tryParseSystemProperties(false, "EMPTY_ARRAY_AS_DEFAULT")) {
            assertEquals(new int[0], getDefaultValue(int[].class));
            assertEquals(new short[0][], getDefaultValue(short[][].class));
            assertEquals(new long[0], getDefaultValue(long[].class));

            assertEquals(new Byte[0], getDefaultValue(Byte[].class));
            assertEquals(new Character[0][], getDefaultValue(Character[][].class));
            assertEquals(new Boolean[0], getDefaultValue(Boolean[].class));

            assertEquals(new Object[0], getDefaultValue(Object[].class));
            assertEquals(new String[0][][], getDefaultValue(String[][][].class));
            assertEquals(new Comparable[0], getDefaultValue(Comparable[].class));
            assertEquals(new DayOfWeek[0], getDefaultValue(DayOfWeek[].class));
            assertEquals(new A[0], getDefaultValue(A[].class));
            assertEquals(new ITest1[0], getDefaultValue(ITest1[].class));

            assertEquals(new Function[0], getDefaultValue(Function[].class));
            assertAllFalse(valueEquals(new Function[0], getDefaultValue(FunctionThrowable[].class)));
            assertEquals(new ofThrowable[0], getDefaultValue(ofThrowable[].class));
            assertEquals(new Predicate[0][], getDefaultValue(Predicate[][].class));
        } else {
            assertAllNull(getDefaultValue(int[].class), getDefaultValue(short[][].class), getDefaultValue(Byte[].class));
            assertAllNull(getDefaultValue(Character[][].class));
            assertAllNull(getDefaultValue(Boolean[].class));

            assertAllNull(getDefaultValue(Object[].class));
            assertAllNull(getDefaultValue(String[].class));
            assertAllNull(getDefaultValue(Comparable[].class));
            assertAllNull(getDefaultValue(DayOfWeek[].class));
            assertAllNull(getDefaultValue(A[].class));
            assertAllNull(getDefaultValue(ITest1[].class));

            assertAllNull(getDefaultValue(SupplierThrowable[].class));
            assertAllNull(getDefaultValue(Consumer[][].class));
            assertAllNull(getDefaultValue(List[].class));
        }
    }

    @Test
    public void getEquivalentClass_withSimpleClass_getExpectedResults() {
        assertEquals(Integer.class, getEquivalentClass(int.class));
        assertEquals(Character.class, getEquivalentClass(char.class));
        assertEquals(Short.class, getEquivalentClass(short.class));
        assertEquals(Long.class, getEquivalentClass(long.class));
        assertEquals(Byte.class, getEquivalentClass(byte.class));
        assertEquals(Boolean.class, getEquivalentClass(boolean.class));
        assertEquals(Double.class, getEquivalentClass(double.class));
        assertEquals(Float.class, getEquivalentClass(float.class));

        assertEquals(int.class, getEquivalentClass(Integer.class));
        assertEquals(char.class, getEquivalentClass(Character.class));
        assertEquals(short.class, getEquivalentClass(Short.class));
        assertEquals(long.class, getEquivalentClass(Long.class));
        assertEquals(byte.class, getEquivalentClass(Byte.class));
        assertEquals(boolean.class, getEquivalentClass(Boolean.class));
        assertEquals(double.class, getEquivalentClass(Double.class));
        assertEquals(float.class, getEquivalentClass(Float.class));

        assertEquals(Object.class, getEquivalentClass(Object.class));
        assertEquals(Object.class, getEquivalentClass(Comparable.class));
        assertEquals(Object.class, getEquivalentClass(String.class));
        assertEquals(Object.class, getEquivalentClass(Function.class));
        assertEquals(Object.class, getEquivalentClass(A.class));
        assertEquals(Object.class, getEquivalentClass(ITest1.class));
    }

    @Test
    public void getEquivalentClass_withArrayClass_getExpectedResults() {
        assertEquals(Integer[].class, getEquivalentClass(int[].class));
        assertEquals(Character[].class, getEquivalentClass(char[].class));
        assertEquals(Short[].class, getEquivalentClass(short[].class));
        assertEquals(Long[].class, getEquivalentClass(long[].class));
        assertEquals(Byte[][].class, getEquivalentClass(byte[][].class));
        assertEquals(Boolean[][].class, getEquivalentClass(boolean[][].class));
        assertEquals(Double[][].class, getEquivalentClass(double[][].class));
        assertEquals(Float[][].class, getEquivalentClass(float[][].class));

        assertEquals(int[][].class, getEquivalentClass(Integer[][].class));
        assertEquals(char[][].class, getEquivalentClass(Character[][].class));
        assertEquals(short[][].class, getEquivalentClass(Short[][].class));
        assertEquals(long[][].class, getEquivalentClass(Long[][].class));
        assertEquals(byte[].class, getEquivalentClass(Byte[].class));
        assertEquals(boolean[].class, getEquivalentClass(Boolean[].class));
        assertEquals(double[].class, getEquivalentClass(Double[].class));
        assertEquals(float[].class, getEquivalentClass(Float[].class));

        assertEquals(Object[].class, getEquivalentClass(Object[].class));
        assertEquals(Object[].class, getEquivalentClass(Comparable[].class));
        assertEquals(Object[][].class, getEquivalentClass(String[][].class));
        assertEquals(Object[].class, getEquivalentClass(Function[].class));
        assertEquals(Object[].class, getEquivalentClass(A[].class));
        assertEquals(Object[].class, getEquivalentClass(ITest1[].class));
    }

    @Test
    public void getToEquivalentConverter_withSimpleClass_getExpectedResults() {
        assertAllTrue(Integer.valueOf(100) == getToEquivalentParallelConverter(int.class).apply(100));
        assertAllTrue(Long.valueOf(100L) == getToEquivalentParallelConverter(long.class).apply(100L));
        assertAllTrue(Character.valueOf('a') == getToEquivalentParallelConverter(char.class).apply('a'));
        assertAllTrue(Byte.valueOf((byte) 33) == getToEquivalentParallelConverter(byte.class).apply((byte) 33));
        assertAllTrue(Boolean.valueOf(true) == getToEquivalentParallelConverter(boolean.class).apply(true));
        assertAllTrue(Short.valueOf((short) 45) == getToEquivalentParallelConverter(short.class).apply((short) 45));
        //Surprise, following two assertion of Float and Double would fail in Java 8
//        assertTrue(Float.valueOf(0f) == Float.valueOf(0f));
//        assertTrue(Double.valueOf(99.6d) == Double.valueOf(99.6d));
        assertAllTrue(1f == ((Float) getToEquivalentParallelConverter(float.class).apply(1f)));
        assertAllTrue(99.6d == ((Double) getToEquivalentParallelConverter(double.class).apply(99.6d)));

        assertAllTrue(-1 == (int) getToEquivalentParallelConverter(Integer.class).apply(Integer.valueOf(-1)));
        assertAllTrue((100L) == (long) getToEquivalentParallelConverter(Long.class).apply(Long.valueOf(100L)));
        assertAllTrue(('a') == (char) getToEquivalentParallelConverter(Character.class).apply(Character.valueOf('a')));
        assertAllTrue(((byte) 33) == (byte) getToEquivalentParallelConverter(Byte.class).apply(Byte.valueOf((byte) 33)));
        assertAllTrue((true) == (boolean) getToEquivalentParallelConverter(Boolean.class).apply(Boolean.valueOf(true)));
        assertAllTrue(((short) 45) == (short) getToEquivalentParallelConverter(Short.class).apply(Short.valueOf((short) 45)));
        assertAllTrue((-72.3d) == (double) getToEquivalentParallelConverter(Double.class).apply(Double.valueOf(-72.3d)));
        assertAllTrue((-0.03f) == (float) getToEquivalentParallelConverter(Float.class).apply(Float.valueOf(-0.03f)));

        Object obj = new ArrayList();
        assertAllTrue(obj == getToEquivalentParallelConverter(Object.class).apply(obj));
        assertAllTrue("abc" == getToEquivalentParallelConverter(Object.class).apply("abc"));
        Comparable comparable = 33;
        assertAllTrue(comparable == getToEquivalentParallelConverter(Object.class).apply(comparable));
        A newA = new A();
        assertAllTrue(newA == getToEquivalentParallelConverter(A.class).apply(newA));
        assertAllFalse(newA == getToEquivalentParallelConverter(A.class).apply(new A()));
    }

    @Test
    public void getToEquivalentConverter_withArrayClass_getExpectedResults() {
        assertEquals(new Long[]{1L, 2L, 3L}, getToEquivalentParallelConverter(long[].class).apply(new long[]{1, 2, 3}));
        assertEquals(new Short[]{1, 2, 3}, getToEquivalentParallelConverter(short[].class).apply(new short[]{1, 2, 3}));
        assertEquals(new Character[]{'a', 'b', 'c'}, getToEquivalentParallelConverter(char[].class).apply(new char[]{'a', 'b', 'c'}));
        assertEquals(new char[]{'a', 'b', 'c'}, getToEquivalentParallelConverter(char[].class).apply(new char[]{'a', 'b', 'c'}));
        assertEquals(new Byte[]{1, 2, 3}, getToEquivalentParallelConverter(byte[].class).apply(new byte[]{1, 2, 3}));
        assertEquals(new Boolean[]{true, false}, getToEquivalentParallelConverter(boolean[].class).apply(new boolean[]{true, false}));
        assertEquals(new Double[]{1.1, 2.2, 3.3}, getToEquivalentParallelConverter(double[].class).apply(new double[]{1.1, 2.2, 3.3}));
        assertEquals(new Float[]{-11f, -3.0f, 0f}, getToEquivalentParallelConverter(float[].class).apply(new float[]{-11f, -3.0f, 0f}));

        assertEquals(new int[]{1, 2, 3}, getToEquivalentParallelConverter(Integer[].class).apply(new Integer[]{1, 2, 3}));
        assertEquals(new long[]{1, 2, 3}, getToEquivalentParallelConverter(Long[].class).apply(new Long[]{1L, 2L, 3L}));
        assertEquals(new short[]{1, 2, 3}, getToEquivalentParallelConverter(Short[].class).apply(new Short[]{1, 2, 3}));
        assertEquals(new char[]{'a', 'b', 'c'}, getToEquivalentParallelConverter(Character[].class).apply(new Character[]{'a', 'b', 'c'}));
        assertEquals(new byte[]{1, 2, 3}, getToEquivalentParallelConverter(Byte[].class).apply(new Byte[]{1, 2, 3}));
        assertEquals(new boolean[]{true, false}, getToEquivalentParallelConverter(Boolean[].class).apply(new Boolean[]{true, false}));
        assertEquals(new double[]{1.1, 2.2, 3.3}, getToEquivalentParallelConverter(Double[].class).apply(new Double[]{1.1, 2.2, 3.3}));
        assertEquals(new float[]{-11f, -3.0f, 0f}, getToEquivalentParallelConverter(Float[].class).apply(new Float[]{-11f, -3.0f, 0f}));
        assertEquals(new Float[]{-11f, -3.0f, 0f}, getToEquivalentParallelConverter(Float[].class).apply(new Float[]{-11f, -3.0f, 0f}));

        assertException(() -> ArrayHelper.toPrimitive(new Boolean[]{true, false, null}), NullPointerException.class);
//        assertEquals(new boolean[]{true, false, false}, getToEquivalentParallelConverter(Boolean[].class).apply(new Boolean[]{true, false, null}));

        assertEquals(
                new Boolean[][]{new Boolean[]{true, false}, new Boolean[0], null},

                getToEquivalentParallelConverter(boolean[][].class)
                        .apply(new boolean[][]{new boolean[]{true, false}, new boolean[0], null}));
        assertEquals(new Byte[][]{new Byte[]{1, 2, 3}, null}, getToEquivalentParallelConverter(byte[][].class).apply(new byte[][]{new byte[]{1, 2, 3}, null}));
        assertEquals(new Double[][]{new Double[]{1.1}, new Double[]{2.2}, new Double[]{3.3}},
                getToEquivalentParallelConverter(double[][].class).apply((new double[][]{new double[]{1.1}, new double[]{2.2}, new double[]{3.3}})));
        assertEquals(new Float[][]{null}, getToEquivalentParallelConverter(float[][].class).apply(new float[][]{null}));

        assertEquals(new int[][]{new int[]{1, 2, 3}},
                getToEquivalentParallelConverter(Integer[][].class).apply(new Integer[][]{new Integer[]{1, 2, 3}}));
        assertEquals(new long[][]{}, getToEquivalentParallelConverter(Long[][].class).apply(new Long[][]{}));
        assertEquals(new short[][]{new short[]{1, 2, 3}}, getToEquivalentParallelConverter(Short[][].class).apply(new Short[][]{new Short[]{1, 2, 3}}));
        assertEquals(new char[][]{null, null}, getToEquivalentParallelConverter(Character[][].class).apply(new Character[][]{null, null}));


        Object[] objects = new Object[]{null, 0, void.class, 'a', "", null};
        assertEquals(objects, getToEquivalentParallelConverter(Object[].class).apply(objects));
        Comparable[] comparables = new Comparable[]{1, 33f, -2d, 'a', "abc"};
        assertEquals(comparables, getToEquivalentParallelConverter(Comparable[].class).apply(comparables));
        A[] newA = new A[]{};
        assertEquals(newA, getToEquivalentParallelConverter(A.class).apply(newA));
        String[] strings = new String[]{null, "", "   "};
        assertEquals(strings, getToEquivalentParallelConverter(A.class).apply(strings));
    }

    @Test
    public void getClassPredicate_withInterfaceOrClassTypes_shallMatchCorrectly() {
        Predicate<Class> classPredicate = getClassEqualitor(ITest1.class);
        assertAllTrue(classPredicate.test(A.class) && classPredicate.test(B.class)
                && classPredicate.test(C.class) && classPredicate.test(D.class)
                && classPredicate.test(ITest1.class));
        assertAllFalse(classPredicate.test(null) || classPredicate.test(Object.class));

        classPredicate = getClassEqualitor(ITest2.class);
        assertAllTrue(classPredicate.test(C.class) && classPredicate.test(D.class)
                && classPredicate.test(ITest2.class));
        assertAllFalse(classPredicate.test(null) || classPredicate.test(Object.class)
                || classPredicate.test(A.class) || classPredicate.test(B.class));

        classPredicate = getClassEqualitor(A.class);
        assertAllTrue(classPredicate.test(A.class) && classPredicate.test(B.class)
                && classPredicate.test(C.class));
        assertAllFalse(classPredicate.test(null) || classPredicate.test(Object.class)
                || classPredicate.test(D.class) || classPredicate.test(ITest1.class) || classPredicate.test(ITest2.class));

        classPredicate = getClassEqualitor(ITest1[].class);
        assertAllTrue(classPredicate.test(A[].class) && classPredicate.test(B[].class)
                && classPredicate.test(C[].class) && classPredicate.test(D[].class)
                && classPredicate.test(ITest1[].class));
        assertAllFalse(classPredicate.test(null) || classPredicate.test(Object.class)
                || classPredicate.test(A.class) || classPredicate.test(ITest1.class) || classPredicate.test(ITest2.class));

        classPredicate = getClassEqualitor(ITest2[].class);
        assertAllTrue(classPredicate.test(C[].class) && classPredicate.test(D[].class)
                && classPredicate.test(ITest2[].class));
        assertAllFalse(classPredicate.test(null) || classPredicate.test(Object.class)
                || classPredicate.test(Object[].class)
                || classPredicate.test(A[].class) || classPredicate.test(B[].class)
                || classPredicate.test(C.class) || classPredicate.test(D.class));

        classPredicate = getClassEqualitor(A[].class);
        assertAllTrue(classPredicate.test(A[].class) && classPredicate.test(B[].class)
                && classPredicate.test(C[].class));
        assertAllFalse(classPredicate.test(null) || classPredicate.test(Object.class)
                || classPredicate.test(Object[].class)
                || classPredicate.test(A.class) || classPredicate.test(B.class) || classPredicate.test(C.class)
                || classPredicate.test(D[].class) || classPredicate.test(ITest1[].class) || classPredicate.test(ITest2[].class));

        assertAllTrue(getClassEqualitor(Map.class).test(HashMap.class));
    }

    private void validateFactory(FunctionThrowable<Integer, Object> arrayFactory, int length, Class clazz) {
        Object array = arrayFactory.orElse(null).apply(length);
        assertEquals(clazz, array.getClass());
        assertEquals(length, Array.getLength(array));
    }

    @Test
    public void getArrayFactory_withPrimitiveTypes_getPrimitiveArrays() {
        FunctionThrowable<Integer, Object> arrayFactory = getArrayFactory(int.class);
        validateFactory(arrayFactory, 3, int[].class);

        arrayFactory = getArrayFactory(int[].class);
        validateFactory(arrayFactory, 2, int[][].class);

        arrayFactory = getArrayFactory(char.class);
        validateFactory(arrayFactory, 0, char[].class);

        arrayFactory = getArrayFactory(char[].class);
        validateFactory(arrayFactory, 1, char[][].class);

        arrayFactory = getArrayFactory(byte.class);
        validateFactory(arrayFactory, 2, byte[].class);

        arrayFactory = getArrayFactory(byte[].class);
        validateFactory(arrayFactory, 0, byte[][].class);

        arrayFactory = getArrayFactory(boolean.class);
        validateFactory(arrayFactory, 5, boolean[].class);

        arrayFactory = getArrayFactory(boolean[].class);
        validateFactory(arrayFactory, 1, boolean[][].class);

        arrayFactory = getArrayFactory(double.class);
        validateFactory(arrayFactory, 3, double[].class);

        arrayFactory = getArrayFactory(double[].class);
        validateFactory(arrayFactory, 0, double[][].class);

        arrayFactory = getArrayFactory(float.class);
        validateFactory(arrayFactory, 1, float[].class);

        arrayFactory = getArrayFactory(float[].class);
        validateFactory(arrayFactory, 0, float[][].class);

        arrayFactory = getArrayFactory(long.class);
        validateFactory(arrayFactory, 2, long[].class);

        arrayFactory = getArrayFactory(long[].class);
        validateFactory(arrayFactory, 3, long[][].class);

        arrayFactory = getArrayFactory(short.class);
        validateFactory(arrayFactory, 1, short[].class);

        arrayFactory = getArrayFactory(short[].class);
        validateFactory(arrayFactory, 0, short[][].class);
    }

    @Test
    public void getArrayFactory_withWrapperTypes_getWrapperArrays() {
        FunctionThrowable<Integer, Object> arrayFactory = getArrayFactory(Integer.class);
        validateFactory(arrayFactory, 3, Integer[].class);

        arrayFactory = getArrayFactory(Integer[].class);
        validateFactory(arrayFactory, 2, Integer[][].class);

        arrayFactory = getArrayFactory(Character.class);
        validateFactory(arrayFactory, 0, Character[].class);

        arrayFactory = getArrayFactory(Character[].class);
        validateFactory(arrayFactory, 1, Character[][].class);

        arrayFactory = getArrayFactory(Byte.class);
        validateFactory(arrayFactory, 2, Byte[].class);

        arrayFactory = getArrayFactory(Byte[].class);
        validateFactory(arrayFactory, 0, Byte[][].class);

        arrayFactory = getArrayFactory(Boolean.class);
        validateFactory(arrayFactory, 5, Boolean[].class);

        arrayFactory = getArrayFactory(Boolean[].class);
        validateFactory(arrayFactory, 1, Boolean[][].class);

        arrayFactory = getArrayFactory(Double.class);
        validateFactory(arrayFactory, 3, Double[].class);

        arrayFactory = getArrayFactory(Double[].class);
        validateFactory(arrayFactory, 0, Double[][].class);

        arrayFactory = getArrayFactory(Float.class);
        validateFactory(arrayFactory, 1, Float[].class);

        arrayFactory = getArrayFactory(Float[].class);
        validateFactory(arrayFactory, 0, Float[][].class);

        arrayFactory = getArrayFactory(Long.class);
        validateFactory(arrayFactory, 2, Long[].class);

        arrayFactory = getArrayFactory(Long[].class);
        validateFactory(arrayFactory, 3, Long[][].class);

        arrayFactory = getArrayFactory(Short.class);
        validateFactory(arrayFactory, 1, Short[].class);

        arrayFactory = getArrayFactory(Short[].class);
        validateFactory(arrayFactory, 0, Short[][].class);
    }

    @Test
    public void getArrayFactory_withClassOrInterfaceTypes_getRightArrays() {
        FunctionThrowable<Integer, Object> arrayFactory = getArrayFactory(ITest1.class);
        validateFactory(arrayFactory, 3, ITest1[].class);

        arrayFactory = getArrayFactory(ITest1[].class);
        validateFactory(arrayFactory, 2, ITest1[][].class);

        arrayFactory = getArrayFactory(A.class);
        validateFactory(arrayFactory, 0, A[].class);

        arrayFactory = getArrayFactory(C[].class);
        validateFactory(arrayFactory, 1, C[][].class);
    }

    public void getSetArrayElement_withPrimitiveArray_bothGetterSetterWorks() {
        int[] ints = new int[]{1, 2, 3};
        TriConsumerThrowable<Object, Integer, Object> setter = getArrayElementSetter(ints.getClass());
        assertEquals(1, Array.get(ints, 0));
        setter.withHandler(null).accept(ints, 2, 33);
        assertEquals(33, ints[2]);

        long[] longs = new long[]{1, 2L, 3};
        setter = getArrayElementSetter(longs.getClass());
        assertEquals(1L, Array.get(longs, 0));
        setter.withHandler(null).accept(longs, 2, 33L);
        assertEquals(33L, longs[2]);

        short[] shorts = new short[]{1, 2, 3};
        setter = getArrayElementSetter(shorts.getClass());
        assertEquals((short) 1, Array.get(shorts, 0));
        setter.withHandler(null).accept(shorts, 2, (short) 33);
        assertEquals((short) 33, shorts[2]);

        char[] chars = new char[]{'a', 'b', 'c'};
        setter = getArrayElementSetter(chars.getClass());
        assertEquals('a', Array.get(chars, 0));
        setter.withHandler(null).accept(chars, 2, 'x');
        assertEquals('x', chars[2]);

        byte[] bytes = new byte[]{44, 45, 46};
        setter = getArrayElementSetter(bytes.getClass());
        assertEquals((byte) 44, Array.get(bytes, 0));
        setter.withHandler(null).accept(bytes, 2, (byte) 55);
        assertEquals((byte) 55, bytes[2]);

        boolean[] booleans = new boolean[]{true, false};
        setter = getArrayElementSetter(booleans.getClass());
        assertEquals(true, Array.get(booleans, 0));
        setter.withHandler(null).accept(booleans, 1, true);
        assertEquals(true, booleans[1]);

        double[] doubles = new double[]{1.0, 2.0, 3.0};
        setter = getArrayElementSetter(doubles.getClass());
        assertEquals(1.0, Array.get(doubles, 0));
        setter.withHandler(null).accept(doubles, 1, 100);
        assertEquals(100.0, doubles[1]);

        float[] floats = new float[]{1.1f, 2.2f, 3.3f};
        setter = getArrayElementSetter(floats.getClass());
        assertEquals(1.1f, Array.get(floats, 0));
        setter.withHandler(null).accept(floats, 1, 101.3f);
        assertEquals(101.3f, floats[1]);
    }

    @Test
    public void getSetArrayElement_withWrapperArray_bothGetterSetterWorks() {
        Integer[] ints2 = new Integer[]{1, 2, 3};
        TriConsumerThrowable<Object, Integer, Object> setter = getArrayElementSetter(ints2.getClass());
        assertEquals(1, Array.get(ints2, 0));
        setter.withHandler(null).accept(ints2, 2, 33);
        assertEquals(Integer.valueOf(33), ints2[2]);

        Long[] longs2 = new Long[]{1L, 2L, 3L};
        setter = getArrayElementSetter(longs2.getClass());
        assertEquals(1L, Array.get(longs2, 0));
        setter.withHandler(null).accept(longs2, 2, 33L);
        assertEquals(Long.valueOf(33), longs2[2]);

        Short[] shorts2 = new Short[]{1, 2, 3};
        setter = getArrayElementSetter(shorts2.getClass());
        assertEquals(Short.valueOf((short) 1), Array.get(shorts2, 0));
        setter.withHandler(null).accept(shorts2, 2, Short.valueOf((short) 45));
        assertEquals(Short.valueOf((short) 45), shorts2[2]);

        Character[] chars2 = new Character[]{'a', 'b', 'c'};
        setter = getArrayElementSetter(chars2.getClass());
        assertEquals(Character.valueOf('a'), Array.get(chars2, 0));
        setter.withHandler(null).accept(chars2, 2, 'x');
        assertEquals(Character.valueOf('x'), chars2[2]);

        Byte[] bytes2 = new Byte[]{44, 45, 46};
        setter = getArrayElementSetter(bytes2.getClass());
        assertEquals(Byte.valueOf("44"), Array.get(bytes2, 0));
        setter.withHandler(null).accept(bytes2, 2, (byte) 55);
        assertEquals(Byte.valueOf("55"), bytes2[2]);

        Boolean[] booleans2 = new Boolean[]{true, false};
        setter = getArrayElementSetter(booleans2.getClass());
        assertEquals(true, Array.get(booleans2, 0));
        setter.withHandler(null).accept(booleans2, 1, true);
        assertEquals(true, booleans2[1]);

        Double[] doubles2 = new Double[]{1.0, 2.0, 3.0};
        setter = getArrayElementSetter(doubles2.getClass());
        assertEquals(1.0, Array.get(doubles2, 0));
        setter.withHandler(null).accept(doubles2, 1, 100.0);
        assertEquals(100.0, doubles2[1]);

        Float[] floats2 = new Float[]{1.1f, 2.2f, 3.3f};
        setter = getArrayElementSetter(floats2.getClass());
        assertEquals(1.1f, Array.get(floats2, 0));
        setter.withHandler(null).accept(floats2, 1, 101.3f);
        assertEquals(101.3f, floats2[1]);
    }

    @Test
    public void getSetArrayElement_withClassOrInterfaceType_bothGetterSetterWorks() {
        ITest1[] interfaces1 = new ITest1[]{null, new A(), new B(), new C(), new D()};
        TriConsumerThrowable<Object, Integer, Object> setter = getArrayElementSetter(interfaces1.getClass());
        assertEquals(null, Array.get(interfaces1, 0));
        assertEquals(A.class, Array.get(interfaces1, 1).getClass());
        assertEquals(D.class, Array.get(interfaces1, 4).getClass());
        setter.withHandler(null).accept(interfaces1, 0, new D());
        assertEquals(D.class, Array.get(interfaces1, 0).getClass());

        A[] aArray = new A[]{new A(), new B(), new C()};
        setter = getArrayElementSetter(aArray.getClass());
        assertEquals(A.class, Array.get(aArray, 0).getClass());
        assertEquals(B.class, Array.get(aArray, 1).getClass());

        //Invalid setting operation would not update the element
        setter.withHandler().accept(aArray, 1, new D());
        assertEquals(B.class, Array.get(aArray, 1).getClass());

        //set element@1 to new C()
        setter.withHandler(null).accept(aArray, 1, new C());
        assertEquals(C.class, Array.get(aArray, 1).getClass());
    }

    @Test
    public void getSetArrayElement_withMultiDimensionArrayTypes_bothGetterSetterWorks() {
        int[][] ints = new int[][]{new int[]{1, 2, 3}, null};
        TriConsumerThrowable<Object, Integer, Object> setter = getArrayElementSetter(ints.getClass());
        assertEquals(ints[0], Array.get(ints, 0));
        setter.withHandler(null).accept(ints, 1, new int[0]);
        assertEquals(0, ints[1].length);

        long[][] longs = new long[][]{new long[]{1, 2L, 3}, null};
        setter = getArrayElementSetter(longs.getClass());
        assertEquals(longs[0], Array.get(longs, 0));
        setter.withHandler(null).accept(longs, 0, null);
        assertAllNull(longs[0]);

        short[][] shorts = new short[][]{new short[]{1, 2, 3}, null};
        setter = getArrayElementSetter(shorts.getClass());
        assertEquals(shorts[0], Array.get(shorts, 0));
        setter.withHandler(null).accept(shorts, 0, null);
        assertAllNull(shorts[0]);

        char[][] chars = new char[][]{new char[]{'a', 'b', 'c'}, null};
        setter = getArrayElementSetter(chars.getClass());
        assertEquals(chars[0], Array.get(chars, 0));
        setter.withHandler(null).accept(chars, 0, null);
        assertAllNull(chars[0]);

        byte[][] bytes = new byte[][]{new byte[]{44, 45, 46}, null};
        setter = getArrayElementSetter(bytes.getClass());
        assertEquals(bytes[0], Array.get(bytes, 0));
        setter.withHandler(null).accept(bytes, 0, null);
        assertAllNull(bytes[0]);

        boolean[][] booleans = new boolean[][]{new boolean[]{true, false}, null};
        setter = getArrayElementSetter(booleans.getClass());
        assertEquals(booleans[0], Array.get(booleans, 0));
        setter.withHandler(null).accept(booleans, 0, null);
        assertAllNull(booleans[0]);

        double[][] doubles = new double[][]{new double[]{1.0, 2.0, 3.0}, null};
        setter = getArrayElementSetter(doubles.getClass());
        assertEquals(doubles[0], Array.get(doubles, 0));
        setter.withHandler(null).accept(doubles, 0, null);
        assertAllNull(doubles[0]);

        float[][] floats = new float[][]{new float[]{1.1f, 2.2f, 3.3f}, null};
        setter = getArrayElementSetter(floats.getClass());
        assertEquals(floats[0], Array.get(floats, 0));
        setter.withHandler(null).accept(floats, 0, null);
        assertAllNull(floats[0]);

        Integer[][] ints2 = new Integer[][]{new Integer[]{1, 2, 3}, null};
        setter = getArrayElementSetter(ints2.getClass());
        assertEquals(ints2[0], Array.get(ints2, 0));
        setter.withHandler(null).accept(ints2, 0, null);
        assertAllNull(ints2[0]);

        Long[][] longs2 = new Long[][]{new Long[]{1L, 2L, 3L}, null};
        setter = getArrayElementSetter(longs2.getClass());
        assertEquals(longs2[0], Array.get(longs2, 0));
        setter.withHandler(null).accept(longs2, 0, null);
        assertAllNull(longs2[0]);

        Short[][] shorts2 = new Short[][]{new Short[]{1, 2, 3}, null};
        setter = getArrayElementSetter(shorts2.getClass());
        assertEquals(shorts2[0], Array.get(shorts2, 0));
        setter.withHandler(null).accept(shorts2, 0, null);
        assertAllNull(shorts2[0]);

        Character[][] chars2 = new Character[][]{new Character[]{'a', 'b', 'c'}, null};
        setter = getArrayElementSetter(chars2.getClass());
        assertEquals(chars2[0], Array.get(chars2, 0));
        setter.withHandler(null).accept(chars2, 0, null);
        assertAllNull(chars2[0]);

        Byte[][] bytes2 = new Byte[][]{new Byte[]{44, 45, 46}, null};
        setter = getArrayElementSetter(bytes2.getClass());
        assertEquals(bytes2[0], Array.get(bytes2, 0));
        setter.withHandler(null).accept(bytes2, 0, null);
        assertAllNull(bytes2[0]);

        Boolean[][] booleans2 = new Boolean[][]{new Boolean[]{true, false}, null};
        setter = getArrayElementSetter(booleans2.getClass());
        assertEquals(booleans2[0], Array.get(booleans2, 0));
        setter.withHandler(null).accept(booleans2, 0, null);
        assertAllNull(booleans2[0]);

        Double[][] doubles2 = new Double[][]{new Double[]{1.0, 2.0, 3.0}, null};
        setter = getArrayElementSetter(doubles2.getClass());
        assertEquals(doubles2[0], Array.get(doubles2, 0));
        setter.withHandler(null).accept(doubles2, 0, null);
        assertAllNull(doubles2[0]);

        Float[][] floats2 = new Float[][]{new Float[]{1.1f, 2.2f, 3.3f}, null};
        setter = getArrayElementSetter(floats2.getClass());
        assertEquals(floats2[0], Array.get(floats2, 0));
        setter.withHandler(null).accept(floats2, 0, null);
        assertAllNull(floats2[0]);

        ITest1[][] interfaces1 = new ITest1[][]{new ITest1[]{null, new A(), new B(), new C(), new D()}, null};
        setter = getArrayElementSetter(interfaces1.getClass());
        assertEquals(interfaces1[0], Array.get(interfaces1, 0));
        assertEquals(null, Array.get(interfaces1, 1));
        setter.withHandler(null).accept(interfaces1, 0, null);
        assertAllNull(interfaces1[0]);

        A[][] aArray = new A[][]{new A[]{new A(), new B(), new C()}, null};
        setter = getArrayElementSetter(aArray.getClass());
        assertEquals(A[].class, Array.get(aArray, 0).getClass());

        //Invalid setting operation would not update the element
        setter.withHandler(null).accept(aArray, 0, null);
        assertAllNull(aArray[0]);
    }

    private void assertCopying(Object expected, TriFunctionThrowable<Object, Integer, Integer, Object> copier, Object original, int from, int to) {
        Object copy = copier.orElse(null).apply(original, from, to);
        if (expected == null) {
            assertAllNull(copy);
            return;
        }

        assertEquals(expected.getClass(), copy.getClass());
        assertEquals(expected, copy);
    }

    @Test
    public void getArrayRangeCopier_withPrimitiveTypes_getCorrectCopies() {
        TriFunctionThrowable<Object, Integer, Integer, Object> copier;

        int[] ints = new int[]{1, 2, 3, 4};
        copier = getArrayRangeCopier(ints.getClass().getComponentType());
        assertCopying(new int[]{2, 3}, copier, ints, 1, 3);

        long[] longs = new long[]{1, 2, 3, 4, 5};
        copier = getArrayRangeCopier(longs.getClass().getComponentType());
        assertCopying(new long[]{4, 5}, copier, longs, 3, 5);

        short[] shorts = new short[]{1, 2, 3, 4};
        copier = getArrayRangeCopier(shorts.getClass().getComponentType());
        assertCopying(null, copier, shorts, 5, 6);

        char[] chars = new char[]{1, 2, 3, 4};
        copier = getArrayRangeCopier(chars.getClass().getComponentType());
        assertCopying(new char[]{2, 3}, copier, chars, 1, 3);

        boolean[] booleans = new boolean[]{true, false, false, true, true};
        copier = getArrayRangeCopier(booleans.getClass().getComponentType());
        assertCopying(new boolean[]{true, false, false, true, true}, copier, booleans, 0, 5);

        byte[] bytes = new byte[]{1, 2, 3, 4};
        copier = getArrayRangeCopier(bytes.getClass().getComponentType());
        assertCopying(new byte[]{2}, copier, bytes, 1, 2);

        float[] floats = new float[]{1f, 2f, 3f, 4f};
        copier = getArrayRangeCopier(floats.getClass().getComponentType());
        assertCopying(new float[]{4f}, copier, floats, 3, 4);

        double[] doubles = new double[]{1d, 2d, 3d, 4d};
        copier = getArrayRangeCopier(doubles.getClass().getComponentType());
        //Be cautious when the <tt>to</tt> is more then the length of the original array, default values would be kep
        assertCopying(new double[]{0d}, copier, doubles, 4, 5);
    }

    @Test
    public void getArrayRangeCopier_withWrapperTypes_getCorrectCopies() {
        TriFunctionThrowable<Object, Integer, Integer, Object> copier;

        Integer[] ints = new Integer[]{1, 2, 3, 4};
        copier = getArrayRangeCopier(ints.getClass().getComponentType());
        assertCopying(new Integer[]{2, 3}, copier, ints, 1, 3);

        Long[] longs = new Long[]{1L, 2L, 3L, 4L, 5L};
        copier = getArrayRangeCopier(longs.getClass().getComponentType());
        //Be cautious when the <tt>to</tt> is more then the length of the original array, default values would be kep
        assertCopying(new Long[]{4L, 5L, null, null}, copier, longs, 3, 7);

        Short[] shorts = new Short[]{1, 2, 3, 4};
        copier = getArrayRangeCopier(shorts.getClass().getComponentType());
        assertCopying(null, copier, shorts, 3, 2);

        Character[] chars = new Character[]{'a', 'b', 'c', 'd'};
        copier = getArrayRangeCopier(chars.getClass().getComponentType());
        assertCopying(new Character[]{'b', 'c'}, copier, chars, 1, 3);

        Boolean[] booleans = new Boolean[]{true, false, false, true, true};
        copier = getArrayRangeCopier(booleans.getClass().getComponentType());
        assertCopying(new Boolean[]{true}, copier, booleans, 4, 5);

        Byte[] bytes = new Byte[]{1, 2, 3, 4};
        copier = getArrayRangeCopier(bytes.getClass().getComponentType());
        assertCopying(new Byte[]{2}, copier, bytes, 1, 2);

        Float[] floats = new Float[]{1f, 2f, 3f, 4f};
        copier = getArrayRangeCopier(floats.getClass().getComponentType());
        assertCopying(new Float[]{4f}, copier, floats, 3, 4);

        double[] doubles = new double[]{1d, 2d, 3d, 4d};
        copier = getArrayRangeCopier(doubles.getClass().getComponentType());
        assertCopying(new double[]{}, copier, doubles, 1, 1);
    }

    @Test
    public void getArrayRangeCopier_withClassOrInterfaceTypes_getCorrectCopies() {
        TriFunctionThrowable<Object, Integer, Integer, Object> copier;

        Comparable[] comparables = new Comparable[]{1, 'b', 3L, 4f, 5d, "6s"};
        copier = getArrayRangeCopier(comparables.getClass().getComponentType());
        assertCopying(new Comparable[]{1, 'b', 3L, 4f, 5d, "6s"}, copier, comparables, 0, 6);
        assertCopying(new Comparable[]{'b', 3L, 4f, 5d, "6s"}, copier, comparables, 1, 6);
        assertCopying(new Comparable[]{}, copier, comparables, 1, 1);
        assertCopying(new Comparable[]{1, 'b', 3L, 4f, 5d}, copier, comparables, 0, 5);
        assertCopying(null, copier, comparables, 3, 2);

        Object[] objects = new Object[]{1, 'b', 3L, 4f, 5d, "6s"};
        copier = getArrayRangeCopier(objects.getClass().getComponentType());
        assertCopying(new Object[]{1, 'b', 3L, 4f, 5d, "6s"}, copier, objects, 0, 6);
        assertCopying(new Object[]{'b', 3L, 4f, 5d, "6s"}, copier, objects, 1, 6);
        assertCopying(new Object[]{}, copier, objects, 5, 5);
        assertCopying(new Object[]{1, 'b', 3L, 4f, 5d}, copier, objects, 0, 5);

        ITest1[] tests = new ITest1[]{new A(), new B(), new C(), new D()};
        copier = getArrayRangeCopier(objects.getClass().getComponentType());
        assertCopying(new ITest1[]{tests[0], tests[1], tests[2], tests[3]}, copier, tests, 0, 4);
        assertCopying(new ITest1[]{tests[3]}, copier, tests, 3, 4);
        assertCopying(new ITest1[]{tests[1], tests[2], tests[3], null, null}, copier, tests, 1, 6);
        assertCopying(new ITest1[]{tests[2], tests[3], null}, copier, tests, 2, 5);
        assertCopying(null, copier, tests, 3, 2);
    }

    @Test
    public void rangeCopyOf_primitiveTypes_getPrimitiveArray() {
        assertEquals(new int[]{1, 2, 0, 0}, copyOfRange(new int[]{1, 2}, 0, 4));
        assertEquals(new char[]{'a', 'b'}, copyOfRange(new char[]{'a', 'b', 'c'}, 0, 2));
        assertEquals(new byte[]{}, copyOfRange(new byte[]{1, 2}, 0, 0));
        assertEquals(new float[]{1f, 2f}, copyOfRange(new float[]{0f, 1f, 2f, 3f}, 1, 3));
        assertEquals(new boolean[]{false, true, false, false}, copyOfRange(
                new boolean[]{true, true, false, false, true, false}, 3, 7));
        assertEquals(new byte[]{}, copyOfRange(new byte[]{1, 2}, 0, 0));
        assertEquals(new long[]{1L, 2L}, copyOfRange(new long[]{0L, 1L, 2L}, 1, 3));
        assertEquals(new short[]{3, 4, 0}, copyOfRange(new short[]{1, 2, 3, 4}, 2, 5));
    }

    private void assertToString(String expected, Function<Object, String> toString, Object array) {
        String arrayString = toString.apply(array);

        assertEquals(expected, arrayString);
    }

    @Test
    public void getArrayToString_withPrimitiveTypes_getExpectedString() {
        Function<Object, String> toString;

        int[] ints = new int[]{1, 2, 3};
        toString = getArrayToString(ints.getClass().getComponentType());
        assertToString("[1, 2, 3]", toString, ints);

        short[] shorts = new short[]{1, 2, 3};
        toString = getArrayToString(shorts.getClass().getComponentType());
        assertToString("[1, 2, 3]", toString, shorts);

        long[] longs = new long[]{1, 2, 3};
        toString = getArrayToString(longs.getClass().getComponentType());
        assertToString("[1, 2, 3]", toString, longs);

        double[] doubles = new double[]{1, 2, 3};
        toString = getArrayToString(doubles.getClass().getComponentType());
        assertToString("[1.0, 2.0, 3.0]", toString, doubles);

        float[] floats = new float[]{1, 2, 3};
        toString = getArrayToString(floats.getClass().getComponentType());
        assertToString("[1.0, 2.0, 3.0]", toString, floats);

        char[] chars = new char[]{'a', 'b', 'c'};
        toString = getArrayToString(chars.getClass().getComponentType());
        assertToString("[a, b, c]", toString, chars);

        byte[] bytes = new byte[]{'a', 'b', 'c'};
        toString = getArrayToString(bytes.getClass().getComponentType());
        assertToString("[97, 98, 99]", toString, bytes);

        boolean[] booleans = new boolean[]{true, false, true};
        toString = getArrayToString(booleans.getClass().getComponentType());
        assertToString("[true, false, true]", toString, booleans);
    }

    @Test
    public void getArrayToString_withWrapperTypes_getExpectedString() {
        Function<Object, String> toString;

        Integer[] ints = new Integer[]{1, 2, 3};
        toString = getArrayToString(ints.getClass().getComponentType());
        assertToString("[1, 2, 3]", toString, ints);

        Short[] shorts = new Short[]{1, 2, 3};
        toString = getArrayToString(shorts.getClass().getComponentType());
        assertToString("[1, 2, 3]", toString, shorts);

        Long[] longs = new Long[]{1L, 2L, 3L};
        toString = getArrayToString(longs.getClass().getComponentType());
        assertToString("[1, 2, 3]", toString, longs);

        Double[] doubles = new Double[]{1d, 2d, 3d, null};
        toString = getArrayToString(doubles.getClass().getComponentType());
        assertToString("[1.0, 2.0, 3.0, null]", toString, doubles);

        Float[] floats = new Float[]{null, 1f, 2f, 3f};
        toString = getArrayToString(floats.getClass().getComponentType());
        assertToString("[null, 1.0, 2.0, 3.0]", toString, floats);

        Character[] chars = new Character[]{'a', 'b', 'c'};
        toString = getArrayToString(chars.getClass().getComponentType());
        assertToString("[a, b, c]", toString, chars);

        Byte[] bytes = new Byte[]{'a', 'b', 'c'};
        toString = getArrayToString(bytes.getClass().getComponentType());
        assertToString("[97, 98, 99]", toString, bytes);

        Boolean[] booleans = new Boolean[]{true, false, true};
        toString = getArrayToString(booleans.getClass().getComponentType());
        assertToString("[true, false, true]", toString, booleans);
    }

    @Test
    public void getArrayToString_withClassOrInterfaceTypes_getExpectedString() {
        Function<Object, String> toString;

        Comparable[] comparables = new Comparable[]{1, 2L, null, "A string", 3, true};
        toString = getArrayToString(comparables.getClass().getComponentType());
        assertToString("[1, 2, null, A string, 3, true]", toString, comparables);

        Object[] objects = new Object[]{DayOfWeek.FRIDAY, 2.0001f, 3E2, new char[]{'a', 'b'}, new Integer[]{null, -1}};
        toString = getArrayToString(objects.getClass().getComponentType());
        assertToString("[FRIDAY, 2.0001, 300.0, [a, b], [null, -1]]", toString, objects);

        String[] strings = new String[]{"", " \t", null, "a"};
        toString = getArrayToString(strings.getClass().getComponentType());
        assertToString("[,  \t, null, a]", toString, strings);
    }

    @Test
    public void getArrayToString_withArrayOfArrayTypes_getExpectedString() {
        Function<Object, String> toString;

        Comparable[][] comparables = new Comparable[][]{new Integer[]{1, 2}, new String[]{null, "str"}, null, new Comparable[]{"A string", 3, true}};
        toString = getArrayToString(comparables.getClass().getComponentType());
        assertToString("[[1, 2], [null, str], null, [A string, 3, true]]", toString, comparables);

        Object[][] objects = new Object[][]{new Object[]{DayOfWeek.FRIDAY, 2.0001f, 3E2}, new Character[]{'a', 'b'},
                new Integer[]{null, -1}, new Boolean[]{true, false, null}};
        toString = getArrayToString(objects.getClass().getComponentType());
        assertToString("[[FRIDAY, 2.0001, 300.0], [a, b], [null, -1], [true, false, null]]", toString, objects);

        String[][] strings = new String[][]{null, new String[]{"", " \t", null, "a"}, new String[]{}};
        toString = getArrayToString(strings.getClass().getComponentType());
        assertToString("[null, [,  \t, null, a], []]", toString, strings);
    }

    @Test
    public void getArrayToString_withArrayOfArrayTypes_dependentKeysGenerated() {
        Function<Object, String> toString;

        assertAllFalse(classOperators.containsKey(Month.class) || classOperators.containsKey(Month[].class)
                || classOperators.containsKey(SupplierThrowable.class) || classOperators.containsKey(SupplierThrowable[].class));
        Object[] objects = new Object[][]{new Month[][]{null, new Month[]{Month.APRIL}},
                new SupplierThrowable[]{null, () -> 1}, new SupplierThrowable[][]{null}};
        toString = getArrayToString(objects.getClass().getComponentType());
        String result = toString.apply(objects);
        assertAllTrue(classOperators.containsKey(Month.class) && classOperators.containsKey(Month[].class)
                && classOperators.containsKey(SupplierThrowable.class) && classOperators.containsKey(SupplierThrowable[].class));
    }

    @Test
    public void testToEquivalent() {
        //For primitive types
        assertEquals(Integer.valueOf(45), toEquivalent(45));
        Object converted = toEquivalent(new int[]{1, 2, 3});
        assertAllTrue(converted.getClass().equals(Integer[].class) && valueEquals(new int[]{1, 2, 3}, converted));
        converted = toEquivalent(new int[][]{new int[]{1, 2}, null, new int[]{3}});
        assertAllTrue(converted.getClass().equals(Integer[][].class) &&
                valueEquals(new int[][]{new int[]{1, 2}, null, new int[]{3}}, converted));

        //For wrapper types
        assertEquals(45, toEquivalent(Integer.valueOf(45)));
        assertAllNull(toEquivalent(new Integer[]{1, 2, null}));
        converted = toEquivalent(new Integer[]{1, 2, 3});
        assertAllTrue(converted.getClass().equals(int[].class) && valueEquals(new int[]{1, 2, 3}, converted));
        converted = toEquivalent(new Integer[][]{new Integer[]{1, 2}, new Integer[]{3}, null, new Integer[]{4, 5}});
        assertAllTrue(converted.getClass().equals(int[][].class) &&
                valueEquals(new int[][]{new int[]{1, 2}, new int[]{3}, null, new int[]{4, 5}}, converted));

        //Other types
        String test = "test";
        converted = toEquivalent(test);
        assertEquals(test, converted);
        converted = toEquivalent(new String[]{"a", "b"});
        assertAllTrue(converted.getClass().equals(Object[].class) && valueEquals(new String[]{"a", "b"}, converted));

        Number[] numbers = new Number[]{1, 2d, 3f, 4L};
        converted = toEquivalent(numbers);
        assertAllTrue(converted.getClass().equals(Object[].class) && Array.getLength(converted) == 4);

        Comparable[][][] comparables = new Comparable[][][]{new Integer[][]{new Integer[0], new Integer[]{1}, new Integer[]{2, 3}},
                new String[][]{null, new String[]{"string1"}}, new Character[][]{new Character[]{'1'}}, new Boolean[][]{new Boolean[0], new Boolean[]{true, false}}};
        converted = toEquivalent(comparables);
        assertAllTrue(converted.getClass().equals(Object[][][].class) && valueEquals(comparables, converted));
    }

    @Test
    public void testConvert() {
        //For single object
        assertEquals(Integer.valueOf(3), convert(3, Integer.class));
        assertEquals(true, convert(Boolean.TRUE, boolean.class));

        Object anyObject = new Object[]{1.0, 2f, 3L, 4, 5};
        Object converted = convert(anyObject, Number[].class);
        assertAllTrue(converted.getClass().equals(Number[].class) && valueEquals(converted, anyObject));
        converted = convert(anyObject, Comparable[].class);
        assertAllTrue(converted.getClass().equals(Comparable[].class) && valueEquals(converted, anyObject));

        Integer[][][] integers = new Integer[][][]{new Integer[][]{new Integer[]{1}, null}, new Integer[0][]};
        converted = convert(integers, Object[].class);
        //The Integer[][][] is converted to Object[]{Integer[][], Integer[][]}
        assertAllTrue(converted.getClass().equals(Object[].class) && valueEquals(converted, integers));

        Number[] numbers = new Number[]{1, 2, 3, 4f, (byte) 5, 6f};
        Comparable[] comparables = convertParallel(numbers, Comparable[].class);
        assertAllNull(comparables);

        Object[] objects = convertSerial(numbers, Object[].class);
        assertEquals(numbers, objects);

    }

    @Test
    public void testConverToObjectArray() {
        int[] ints = new int[]{1, 2, 3};
        Object[] objects = convert(ints, Object[].class);
        assertEquals(new Object[]{1, 2, 3}, objects);
        int[] convertedInts = convert(objects, int[].class);
        assertEquals(ints, convertedInts);

        double[] doubles = new double[]{-1.2, 3};
        objects = convert(doubles, Object[].class);
        assertEquals(new Object[]{-1.2, 3d}, objects);
        double[] convertedDoubles = convert(objects, double[].class);
        assertEquals(convertedDoubles, doubles);

        char[] chars = new char[]{'a', 'b', 'c'};
        objects = convert(chars, Object[].class);
        assertEquals(new Object[]{'a', 'b', 'c'}, objects);
        char[] convertedChars = convert(objects, char[].class);
        assertEquals(convertedChars, chars);

        Integer[] integers = new Integer[]{1, 2, 3};
        objects = convert(ints, Object[].class);
        assertEquals(new Object[]{1, 2, 3}, objects);
        Integer[] convertedIntegers = convert(objects, Integer[].class);
        assertEquals(convertedIntegers, integers);

        Double[] dbls = new Double[]{-1.2, 3d};
        objects = convert(dbls, Object[].class);
        assertEquals(new Object[]{-1.2, 3d}, objects);
        Double[] convertedDbls = convert(objects, Double[].class);
        assertEquals(convertedDbls, dbls);

        Character[] characters = new Character[]{'a', 'b', 'c'};
        objects = convert(chars, Object[].class);
        assertEquals(new Object[]{'a', 'b', 'c'}, objects);
        Character[] convertedCharacters = convert(objects, Character[].class);
        assertEquals(convertedCharacters, characters);

    }

    @Test
    public void testDeepHashCode() {
        assertEquals(0, deepHashCode(null));
        assertEquals(37, deepHashCode(new int[0]));
        assertEquals(37, deepHashCode(new ArrayList()));

        List listOfNull = new ArrayList();
        listOfNull.add(null);
        assertEquals(deepHashCode(listOfNull), deepHashCode(new Integer[]{null}));
        assertEquals(deepHashCode(new Integer[]{1, 2, 3}), deepHashCode(new int[]{1, 2, 3}));
        assertEquals(deepHashCode(new int[]{1, 2, 3}), deepHashCode(Arrays.asList(1, 2, 3)));
        assertEquals(deepHashCode(new Object[]{null, new int[]{1, 5}, new int[][]{null, new int[]{3}, new int[]{7}}}),
                deepHashCode(Arrays.asList(null, Arrays.asList(1, 5), Arrays.asList(null, Arrays.asList(3), new int[]{7}))));
    }

    @Test
    public void testValueEquals() {
        assertAllTrue(valueEquals(null, null));
        assertAllTrue(valueEquals(true, true));
        assertAllTrue(valueEquals(new int[]{1, 2, 3}, new Number[]{1, 2, 3}));
        assertAllTrue(valueEquals(new Integer[]{null}, ArrayHelper.asList(null)));

    }

    @Test
    public void testValueEqualsParallel() {
    }

    @Test
    public void testValueEqualsSerial() {
    }

    @Test
    public void areEquivalent() {
        assertAllTrue(TypeHelper.areEquivalent(int.class, Integer.class),
                TypeHelper.areEquivalent(boolean[].class, Boolean[].class),
                TypeHelper.areEquivalent(boolean[][].class, Boolean[][].class),
                TypeHelper.areEquivalent(int.class, int.class),
                TypeHelper.areEquivalent(float[][][].class, Float[][][].class));

        assertAllFalse(TypeHelper.areEquivalent(int.class, int[].class),
                TypeHelper.areEquivalent(int.class, byte.class),
                TypeHelper.areEquivalent(Integer[][].class, Number[][].class));

        assertException(() -> TypeHelper.areEquivalent(int.class, null), NullPointerException.class);
    }

    @Test
    public void testCanBeAssigned() {
        assertAllTrue(canBeAssigned(Number.class, Integer.class),
                canBeAssigned(Number.class, int.class),
                canBeAssigned(int.class, Integer.class),
                canBeAssigned(Comparable.class, int.class),
                canBeAssigned(Comparable.class, Integer.class),
                canBeAssigned(Comparable[].class, int[].class),
                canBeAssigned(Comparable[].class, Integer[].class),
                canBeAssigned(Number[][].class, float[][].class),
                canBeAssigned(Object.class, int.class),
                canBeAssigned(Object.class, boolean[].class),
                canBeAssigned(Object.class, Character[][].class),
                canBeAssigned(Object[].class, char[][].class),
                canBeAssigned(Object[].class, Comparable[][].class),
                canBeAssigned(Object[].class, Enum[].class)
        );

        assertAllFalse(canBeAssigned(Integer.class, Number.class),
                canBeAssigned(int.class, Number.class),
                canBeAssigned(int.class, Integer[].class),
                canBeAssigned(int[].class, Integer.class),
                canBeAssigned(int.class, Comparable.class),
                canBeAssigned(Integer.class, Comparable.class),
                canBeAssigned(int[].class, Comparable[].class),
                canBeAssigned(Double[].class, Integer[].class),
                canBeAssigned(float[][].class, Number[][].class),
                canBeAssigned(int.class, Object.class),
                canBeAssigned(boolean[].class, Object.class),
                canBeAssigned(Character[].class, Object[].class),
                canBeAssigned(char[][].class, Object[].class)
        );

        assertException(() -> canBeAssigned(Object.class, null), NullPointerException.class);
        assertException(() -> canBeAssigned(null, null), NullPointerException.class);
    }

    @Test
    public void testCanValueEquals() {
        assertAllTrue(
                canValueEquals(null, null, null),
                canValueEquals(1, Integer.valueOf(1), null),
                canValueEquals("abc", "abc", null)
        );

        assertAllFalse(
                canValueEquals(null, "", null),
                canValueEquals(1, 2, null),
                canValueEquals("abc", null, null)
        );

        assertAllTrue(
                canValueEquals(null, null, EqualityStategy.EmptyAsNull),
                canValueEquals(null, new int[0], EqualityStategy.EmptyAsNull),
                canValueEquals(new HashSet(), null, EqualityStategy.EmptyAsNull),
                canValueEquals(new ArrayList(), new char[0], EqualityStategy.EmptyAsNull),
                canValueEquals(new char[0], new LinkedHashSet(), EqualityStategy.EmptyAsNull),
                canValueEquals(new int[0], Arrays.asList(), EqualityStategy.EmptyAsNull)
        );

        assertAllFalse(
                canValueEquals(null, new int[]{1}, EqualityStategy.EmptyAsNull),
                canValueEquals(new Integer[]{null}, null, EqualityStategy.EmptyAsNull),
                canValueEquals(Arrays.asList(1), new char[0], EqualityStategy.EmptyAsNull),
                canValueEquals(new char[0], new char[]{'a'}, EqualityStategy.EmptyAsNull),
                canValueEquals(new int[0], Arrays.asList(0), EqualityStategy.EmptyAsNull)
        );

        assertAllTrue(
                canValueEquals(null, null, EqualityStategy.SameTypeOnly),
                canValueEquals(1, Integer.valueOf(1), EqualityStategy.SameTypeOnly),
                canValueEquals(Tuple.TRUE, Tuple.create(true), EqualityStategy.SameTypeOnly)
        );

        assertAllFalse(
                canValueEquals(null, new int[0], EqualityStategy.SameTypeOnly),
                canValueEquals(new Integer[0], new int[0], EqualityStategy.SameTypeOnly),
                canValueEquals(Arrays.asList(1), new char[0], EqualityStategy.SameTypeOnly),
                canValueEquals(new char[0], new Character[]{'a'}, EqualityStategy.SameTypeOnly),
                canValueEquals(new int[0], Arrays.asList(0), EqualityStategy.SameTypeOnly)
        );

        assertAllNull(
                canValueEquals(new int[]{1, 2}, new int[]{1, 2}, EqualityStategy.SameTypeOnly),
                canValueEquals(new char[0], new char[]{'a'}, EqualityStategy.SameTypeOnly),
                canValueEquals(new Object[0], new Object[]{null}, EqualityStategy.SameTypeOnly)
        );

        assertAllFalse(
                canValueEquals(null, new int[0], EqualityStategy.BetweenAssignableTypes),
                canValueEquals(new Integer[0], null, EqualityStategy.BetweenAssignableTypes),
                canValueEquals(Arrays.asList(1), new char[0], EqualityStategy.BetweenAssignableTypes),
                canValueEquals(new char[0][], new char[0], EqualityStategy.BetweenAssignableTypes),
                canValueEquals(new byte[0], new char[0], EqualityStategy.BetweenAssignableTypes),
                canValueEquals(Arrays.asList(1), new HashSet(), EqualityStategy.BetweenAssignableTypes),
                canValueEquals(new Object[0], new ArrayList(), EqualityStategy.BetweenAssignableTypes),
                canValueEquals(new Number[0], new Comparable[0], EqualityStategy.BetweenAssignableTypes),
                canValueEquals(new int[0], Arrays.asList(0), EqualityStategy.BetweenAssignableTypes)
        );

        assertAllNull(
                canValueEquals(new Object[0], new char[0][], EqualityStategy.BetweenAssignableTypes),
                canValueEquals(new char[0], new Character[]{'a'}, EqualityStategy.BetweenAssignableTypes),
                canValueEquals(new Integer[0], new int[0], EqualityStategy.BetweenAssignableTypes),
                canValueEquals(new int[]{1, 2}, new int[]{}, EqualityStategy.BetweenAssignableTypes),
                canValueEquals(new Object[0], new char[]{'a'}, EqualityStategy.BetweenAssignableTypes),
                canValueEquals(new Object[0][], new Number[0][], EqualityStategy.BetweenAssignableTypes),
                canValueEquals(new Object[0][], new char[0][][], EqualityStategy.BetweenAssignableTypes),
                canValueEquals(new Object[0], new List[0], EqualityStategy.BetweenAssignableTypes),
                canValueEquals(new Object[0], new List[0][], EqualityStategy.BetweenAssignableTypes),
                canValueEquals(new Object[0], new Set[0], EqualityStategy.BetweenAssignableTypes),
                canValueEquals(new Object[0], new Set[0][], EqualityStategy.BetweenAssignableTypes)
        );

        assertAllTrue(
                canValueEquals(null, null, EqualityStategy.TypeIgnored),
                canValueEquals(1, Integer.valueOf(1), EqualityStategy.TypeIgnored),
                canValueEquals(Tuple.TRUE, Tuple.create(true), EqualityStategy.TypeIgnored),
                canValueEquals(new int[0], Arrays.asList(), EqualityStategy.TypeIgnored),
                canValueEquals(new int[0], new Boolean[0], EqualityStategy.TypeIgnored),
                canValueEquals(new HashSet(), new ArrayList(), EqualityStategy.TypeIgnored),
                canValueEquals(new Number[0], new Comparable[0], EqualityStategy.TypeIgnored),
                canValueEquals(new int[0], Arrays.asList(), EqualityStategy.TypeIgnored)
        );

        assertAllFalse(
                canValueEquals(null, new int[0], EqualityStategy.TypeIgnored),
                canValueEquals(new Integer[0], null, EqualityStategy.TypeIgnored),
                canValueEquals(new Integer[0], 1, EqualityStategy.TypeIgnored),
                canValueEquals(Arrays.asList(1), new char[0], EqualityStategy.TypeIgnored),
                canValueEquals(new char[0], new Character[]{'a'}, EqualityStategy.TypeIgnored),

                canValueEquals(new Object[0], 1, EqualityStategy.TypeIgnored),
                canValueEquals(new Object[0], new int[]{1, 2}, EqualityStategy.TypeIgnored),
                canValueEquals(new Number[]{2.2f}, new char[0], EqualityStategy.TypeIgnored),
                canValueEquals(new Number[]{2.2f}, new Comparable[0], EqualityStategy.TypeIgnored),
                canValueEquals(new char[0], Arrays.asList('a'), EqualityStategy.TypeIgnored)
        );

        assertAllNull(
                canValueEquals(new int[]{1, 2}, new Object[]{1}, EqualityStategy.TypeIgnored),
                canValueEquals(new int[]{1}, new char[]{'a'}, EqualityStategy.TypeIgnored),
                canValueEquals(new int[]{1}, Arrays.asList(2, true), EqualityStategy.TypeIgnored),
                canValueEquals(new Number[]{2.2f}, new Comparable[]{'a'}, EqualityStategy.TypeIgnored),
                canValueEquals(new Comparable[]{null}, new Object[]{null}, EqualityStategy.TypeIgnored)
        );
    }

    interface ITest1 {
    }

    interface ITest2 {
    }

    class A implements ITest1 {
    }

    class B extends A {
    }

    class C extends B implements ITest2 {
    }

    class D implements ITest1, ITest2 {
    }


}