package io.github.cruisoring.throwables;

import io.github.cruisoring.Functions;
import io.github.cruisoring.Revokable;
import org.junit.Test;

import java.util.function.Function;

import static io.github.cruisoring.Asserts.*;

public class BiFunctionThrowableTest {

    BiFunctionThrowable<Integer, Integer, Boolean> function = (arg1, arg2) -> arg1 / arg2 > 0;

    @Test
    public void apply() throws Exception {
        assertFalse(function.apply(1, 2));
        assertTrue(function.apply(2, 2));

        assertException(function.asSupplierThrowable(1, 0), ArithmeticException.class);
    }

    @Test
    public void tryApply() {
        assertFalse(function.tryApply(1, 2));
        assertTrue(function.tryApply(2, 2));

        try(Revokable<Function<Exception, Object>> revokable = Revokable.register(
            Functions::getDefaultExceptionHandler, f -> Functions.setDefaultExceptionHandler(f), Functions::returnsNull)) {
            assertNull(function.tryApply(1, 0));

            Functions.setDefaultExceptionHandler(e -> e);
            assertException(() -> function.tryApply(1, 0), ClassCastException.class);

            Functions.setDefaultExceptionHandler(e -> true);
            assertTrue(function.tryApply(1, 0));

            Functions.setDefaultExceptionHandler(Functions::logThenThrows);
            assertException(() -> function.tryApply(1, 0), IllegalStateException.class);
        }
    }

    @Test
    public void asSupplierThrowable() throws Exception {
        assertTrue(function.asSupplierThrowable(4, 2).get());
        assertFalse(function.asSupplierThrowable(4, -2).get());

        assertException(function.asSupplierThrowable(1, 0), ArithmeticException.class);
        assertNull(function.asSupplierThrowable(1, 0).tryGet());
    }

    @Test
    public void withHandler() {
        assertTrue(function.withHandler(Functions::logThenThrows).apply(4, 2));
        assertFalse(function.withHandler(Functions::logThenThrows).apply(-4, 2));

        assertNull(function.withHandler(Functions::logAndReturnsNull).apply(1, 0));
        assertTrue(function.withHandler(Functions::returnsTrue).apply(1, 0));
        assertFalse(function.withHandler(Functions::returnsFalse).apply(1, 0));
        assertException(() -> function.withHandler(Functions::logThenThrows).apply(1, 0), IllegalStateException.class);
    }

    @Test
    public void orElse() {
        assertTrue(function.orElse(false).apply(4, 2));
        assertFalse(function.orElse(false).apply(-4, 2));
        assertFalse(function.orElse(true).apply(-4, 2));

        assertNull(function.orElse(null).apply(1, 0));
        assertFalse(function.orElse(false).apply(1, 0));
    }
}