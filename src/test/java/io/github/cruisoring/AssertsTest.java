package io.github.cruisoring;

import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.utility.StringHelper;
import org.junit.Test;

import static io.github.cruisoring.Asserts.*;

public class AssertsTest {


    void methodNeedNotNullArguments(Integer num, Number... others){
        checkWithoutNull(num, others);
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
        checkStates(1>0);
    }

    @Test
    public void checStates_multipleTrues(){
        checkStates(1>0, 2>1, 3>2);
    }

    @Test
    public void checStates_firstFalse(){
        assertException(() -> checkStates(1 > 2), IllegalStateException.class);
    }

    @Test
    public void checStates_lastFalse(){
        assertException(() -> checkStates(0 == 0, 1 > 2), IllegalStateException.class);
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
}