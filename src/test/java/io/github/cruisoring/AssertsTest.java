package io.github.cruisoring;

import io.github.cruisoring.logger.LogLevel;
import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.utility.ArrayHelper;
import io.github.cruisoring.utility.SetHelper;
import io.github.cruisoring.utility.StringHelper;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.function.Predicate;

import static io.github.cruisoring.Asserts.*;

public class AssertsTest {

    static Revokable<LogLevel> revokable = Revokable.register(() -> defaultLogLevel, l -> defaultLogLevel=l, LogLevel.info);

    @AfterClass
    public static void afterAssertsTests(){
        if(revokable != null) {
            revokable.close();
        }
    }

    void methodNeedNotNullArguments(Integer num, Number... others){
        assertAllNotNull(num, others);
    }

    @Test
    public void testCheckNotNull_withoutNull() {
        methodNeedNotNullArguments(1, 22f, 0, 3.2);
    }

    @Test
    public void testCheckNotNull_singlueNull() {
        assertException(() -> methodNeedNotNullArguments(null), NullPointerException.class);
    }

    @Test
    public void testCheckNotNull() {
        assertException(() -> methodNeedNotNullArguments(123, 4, null, 5.6), NullPointerException.class);
    }

    @Test
    public void checStates_singleExpression(){
        Asserts.assertAllTrue(1>0);
    }

    @Test
    public void checStates_multipleTrues(){
        Asserts.assertAllTrue(1>0, 2>1, 3>2);
    }

    @Test
    public void checStates_firstFalse(){
        assertException(() -> Asserts.assertAllTrue(1 > 2), IllegalStateException.class);
    }

    @Test
    public void checStates_lastFalse(){
        assertException(() -> Asserts.assertAllTrue(0 == 0, 1 > 2), IllegalStateException.class);
    }

    @Test
    public void testAssertLogging() {
        assertLogging(() -> Logger.D("Test runnable output: %d test passed is %s", 1, true), "runnable", 1, true);

        assertLogging(() -> {
            String s = StringHelper.tryFormatString("still get something: %d / %d = %d", 10, 2, 10/2);
            Logger.V(s);
            return s;
        }, "10 / 2 = 5");
    }

    @Test
    public void testIsAllMatched() {
        assertAllTrue(
            isAllMatched(o -> o != null, ArrayHelper.asList(null, 3), 456),
            isAllMatched(o -> o != null, ArrayHelper.asList()),
            isAllMatched(o -> o != null, ArrayHelper.asList(1, 'a')),
            isAllMatched(o -> o != null, SetHelper.asSet(1, 'a')),
            isAllMatched(o -> o != null, new Object[]{}),
            isAllMatched(o -> o != null, new Number[]{1}),
            isAllMatched(o -> o != null, new Comparable[]{1, 3.3, "abc"}),

            isAllMatched(o -> o != null, 123),
            isAllMatched(o -> o != null, 123, 'a', "abc"),
            isAllMatched(o -> o != null, 123, "", "a", "abc"),
            isAllMatched(o -> o != null, 123, 'a', "abc")
        );

        assertAllFalse(
            isAllMatched(o -> o != null, ArrayHelper.asList(null, null)),
            isAllMatched(o -> o != null, ArrayHelper.asList(1, 'a', null)),
            isAllMatched(o -> o != null, SetHelper.asSet(1, 'a', null)),
            isAllMatched(o -> o != null, new Object[]{null}),
            isAllMatched(o -> o != null, new Number[]{1, null}),
            isAllMatched(o -> o != null, new Comparable[]{1, 3.3, "abc", null}),

            isAllMatched(o -> o != null, 123, null),
            isAllMatched(o -> o != null, 123, 'a', "abc", null),
            isAllMatched(o -> o != null, 123, "", "a", "abc", null),
            isAllMatched(o -> o != null, 123, 'a', null, "abc")
        );
    }

    @Test
    public void testIsAnyMatched() {
        assertAllTrue(
            isAnyMatched(o -> o == null, ArrayHelper.asList(null, null)),
            isAnyMatched(o -> o == null, ArrayHelper.asList(1, 'a', null)),
            isAnyMatched(o -> o == null, SetHelper.asSet(1, 'a', null)),
            isAnyMatched(o -> o == null, new Object[]{null}),
            isAnyMatched(o -> o == null, new Number[]{1, null}),
            isAnyMatched(o -> o == null, new Comparable[]{1, 3.3, "abc", null}),

            isAnyMatched(o -> o == null, 123, null),
            isAnyMatched(o -> o == null, 123, 'a', "abc", null),
            isAnyMatched(o -> o == null, 123, "", "a", "abc", null),
            isAnyMatched(o -> o == null, 123, 'a', null, "abc")
        );

        assertAllFalse(
            isAnyMatched(o -> o == null, ArrayHelper.asList(null, 3), 456),
            isAnyMatched(o -> o == null, ArrayHelper.asList()),
            isAnyMatched(o -> o == null, ArrayHelper.asList(1, 'a')),
            isAnyMatched(o -> o == null, SetHelper.asSet(1, 'a')),
            isAnyMatched(o -> o == null, new Object[]{}),
            isAnyMatched(o -> o == null, new Number[]{1}),
            isAnyMatched(o -> o == null, new Comparable[]{1, 3.3, "abc"}),

            isAnyMatched(o -> o == null, 123),
            isAnyMatched(o -> o == null, 123, 'a', "abc"),
            isAnyMatched(o -> o == null, 123, "", "a", "abc"),
            isAnyMatched(o -> o == null, 123, 'a', "abc")
        );
    }

    @Test
    public void testMatchAll() {
        Predicate<Integer> predicate = i -> i > 0;
        assertAllTrue(
            isAllMatched(predicate, new int[]{}),
            isAllMatched(predicate, new int[]{1}),
            isAllMatched(predicate, new int[]{1, 2, 3}),
            isAllMatched(predicate, ArrayHelper.asList()),
            isAllMatched(predicate, SetHelper.asHashSet(1, 3, 5)),
            isAllMatched(predicate, ArrayHelper.asList(1, 5, 7))
        );

        assertAllFalse(
            isAllMatched(predicate, new int[]{0}),
            isAllMatched(predicate, new int[]{1, 0}),
            isAllMatched(predicate, new int[]{0, 1, 2, 3}),
            isAllMatched(predicate, ArrayHelper.asList(0)),
            isAllMatched(predicate, SetHelper.asHashSet(1, 3, 0)),
            isAllMatched(predicate, ArrayHelper.asList(1, 5, 7, -1))
        );

        assertException(() -> isAllMatched(predicate, ArrayHelper.asList("1", 2, 3)), ClassCastException.class);
    }

    @Test
    public void testMatchAny() {
        Predicate<Integer> predicate = i -> i > 0;
        assertAllTrue(
            isAnyMatched(predicate, new int[]{1}),
            isAnyMatched(predicate, new int[]{1, 2}),
            isAnyMatched(predicate, new int[]{1, 2, 3}),
            isAnyMatched(predicate, ArrayHelper.asList(777, 99)),
            isAnyMatched(predicate, SetHelper.asHashSet(1, 3, 5)),
            isAnyMatched(predicate, ArrayHelper.asList(1, 5, 7))
        );

        assertAllFalse(
            isAnyMatched(predicate, new int[]{0}),
            isAnyMatched(predicate, new int[]{-1, 0}),
            isAnyMatched(predicate, new int[]{0, -1}),
            isAnyMatched(predicate, ArrayHelper.asList(0)),
            isAnyMatched(predicate, SetHelper.asHashSet(-11, -3, 0)),
            isAnyMatched(predicate, ArrayHelper.asList(-1))
        );

        assertException(() -> isAnyMatched(predicate, ArrayHelper.asList(1.0, 3.3)), ClassCastException.class);
        assertException(() -> isAnyMatched(predicate, SetHelper.asHashSet(null, 3)), NullPointerException.class);
    }

}