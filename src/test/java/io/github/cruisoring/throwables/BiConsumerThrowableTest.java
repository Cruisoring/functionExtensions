package io.github.cruisoring.throwables;

import io.github.cruisoring.Functions;
import io.github.cruisoring.Revokable;
import org.junit.Test;

import java.util.function.Function;

import static io.github.cruisoring.Asserts.*;

public class BiConsumerThrowableTest {

    Boolean result = false;

    BiConsumerThrowable<Integer, Boolean> function = (i, b) -> {
        result = 10 / i > 3 && b;
    };

    @Test
    public void accept() throws Exception {
        function.accept(2, true);
        assertTrue(result);

        function.accept(4, true);
        assertFalse(result);

        assertException(() -> function.accept(0, false), ArithmeticException.class);
        assertException(() -> function.accept(2, null), NullPointerException.class);
    }

    @Test
    public void tryAccept() {
        function.tryAccept(2, true);
        assertTrue(result);

        function.tryAccept(4, true);
        assertFalse(result);

        try(Revokable<Function<Exception, Object>> revokable = Revokable.register(
            Functions::getDefaultExceptionHandler, f -> Functions.setDefaultExceptionHandler(f), Functions::returnsNull)
        ) {
            result = true;
            function.tryAccept(0, false);
            assertTrue(result);
            function.tryAccept(1, null);
            assertTrue(result);

            Functions.setDefaultExceptionHandler(e -> {
                throw new RuntimeException(e);});
            assertException(() -> function.tryAccept(0, false), RuntimeException.class);

            Functions.setDefaultExceptionHandler(Functions::logThenThrows);
            assertException(() -> function.tryAccept(0, false), IllegalStateException.class);
        }
    }

    @Test
    public void asRunnableThrowable() {
        RunnableThrowable runnableThrowable = function.asRunnableThrowable(1, true);
        runnableThrowable.tryRun();
        assertTrue(result);

        runnableThrowable = function.asRunnableThrowable(2, false);
        runnableThrowable.tryRun();
        assertFalse(result);

        runnableThrowable = function.asRunnableThrowable(2, null);
        assertException(runnableThrowable, NullPointerException.class);
    }

    @Test
    public void withHandler() {
        function.withHandler(Functions::logAndReturnsNull).accept(1, true);
        assertTrue(result);

        function.withHandler(Functions::logAndReturnsNull).accept(1, false);
        assertFalse(result);

        assertException(() -> function.withHandler(Functions::logThenThrows).accept(0, false), IllegalStateException.class);
    }
}