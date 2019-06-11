package io.github.cruisoring.throwables;

import io.github.cruisoring.Functions;
import io.github.cruisoring.Revokable;
import org.junit.Test;

import java.util.function.Function;

import static io.github.cruisoring.Asserts.*;

public class PredicateThrowableTest {
    PredicateThrowable<String> function = s -> Integer.valueOf(s) > 0;

    @Test
    public void test() throws Exception {
        assertFalse(function.test("-1"));
        assertTrue(function.test("2"));

        assertException(() -> function.test(""), NumberFormatException.class);
        assertException(() -> function.test(null), NumberFormatException.class);
    }

    @Test
    public void tryTest() {
        assertFalse(function.tryTest("-1"));
        assertTrue(function.tryTest("2"));

        try(Revokable<Function<Exception, Object>> revokable = Revokable.register(
            Functions::getDefaultExceptionHandler, f -> Functions.setDefaultExceptionHandler(f), Functions::returnsNull)) {
            assertException(() -> function.tryTest("string"), NullPointerException.class);

            Functions.setDefaultExceptionHandler(e -> e);
            assertException(() -> function.tryTest("string"), ClassCastException.class);

            Functions.setDefaultExceptionHandler(e -> true);
            assertTrue(function.tryTest("string"));

            Functions.setDefaultExceptionHandler(Functions::logThenThrows);
            assertException(() -> function.tryTest("string"), IllegalStateException.class);
        }
    }

    @Test
    public void asSupplierThrowable() throws Exception {
        assertTrue(function.asSupplierThrowable("2").get());
        assertFalse(function.asSupplierThrowable("-3").get());

        assertException(function.asSupplierThrowable(""), NumberFormatException.class);
        assertNull(function.asSupplierThrowable("").tryGet());
    }

    @Test
    public void withHandler() {
        assertTrue(function.withHandler(Functions::logThenThrows).test("2"));
        assertFalse(function.withHandler(Functions::logThenThrows).test("-2"));

        assertException(() -> function.withHandler(Functions::logThenThrows).test("string"), IllegalStateException.class);
        assertException(() -> function.withHandler(Functions::logAndReturnsNull).test(""), NullPointerException.class);
        assertTrue(function.withHandler(Functions::returnsTrue).test("string"));
        assertFalse(function.withHandler(Functions::returnsFalse).test(""));
    }

    @Test
    public void orElse() {
        assertTrue(function.orElse(false).test("2"));
        assertFalse(function.orElse(true).test("-1"));

        assertFalse(function.orElse(false).test("sds"));
        assertTrue(function.orElse(true).test(null));
        assertFalse(function.orElse(false).test(""));
    }
}