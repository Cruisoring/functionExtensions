package io.github.cruisoring;

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
}