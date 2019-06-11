package io.github.cruisoring.throwables;

import io.github.cruisoring.Functions;
import io.github.cruisoring.Revokable;
import org.junit.Test;

import java.util.function.Function;

import static io.github.cruisoring.Asserts.assertEquals;
import static io.github.cruisoring.Asserts.assertException;

public class ConsumerThrowableTest {
    Integer result = null;

    ConsumerThrowable<String> function = s -> result = Integer.valueOf(s);

    @Test
    public void accept() throws Exception {
        function.accept("123");
        assertEquals(123, result);

        assertException(() -> function.accept("123.0"), NumberFormatException.class);
        assertException(() -> function.accept(null), NumberFormatException.class);
    }

    @Test
    public void tryAccept() {
        function.tryAccept("456");
        assertEquals(456, result);

        function.tryAccept("-33.02");
        assertEquals(456, result);

        try(Revokable<Function<Exception, Object>> revokable = Revokable.register(
            Functions::getDefaultExceptionHandler, f -> Functions.setDefaultExceptionHandler(f), Functions::returnsNull)
        ) {
            function.tryAccept("3");
            assertEquals(3, result);
            function.tryAccept(null);
            assertEquals(3, result);

            Functions.setDefaultExceptionHandler(e -> {
                throw new RuntimeException(e);});
            assertException(() -> function.tryAccept("false"), RuntimeException.class);

            Functions.setDefaultExceptionHandler(Functions::logThenThrows);
            assertException(() -> function.tryAccept(null), IllegalStateException.class);
        }
    }

    @Test
    public void asRunnableThrowable() {
        function.asRunnableThrowable("4").tryRun();
        assertEquals(4, result);

        function.asRunnableThrowable("3.").tryRun();
        assertEquals(4, result);

        assertException(function.asRunnableThrowable(null), NumberFormatException.class);
    }

    @Test
    public void withHandler() {
        result = 7;
        function.withHandler(Functions::logAndReturnsNull).accept("");
        assertEquals(7, result);

        function.withHandler(Functions::logAndReturnsNull).accept("1");
        assertEquals(1, result);

        assertException(() -> function.withHandler(Functions::logThenThrows).accept(null), IllegalStateException.class);
    }
}