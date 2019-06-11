package io.github.cruisoring.throwables;

import io.github.cruisoring.Functions;
import io.github.cruisoring.Revokable;
import org.junit.Test;

import java.util.function.Function;

import static io.github.cruisoring.Asserts.*;

public class BiPredicateThrowableTest {

    BiPredicateThrowable<String, Double> function = (s, d) -> d.equals(Double.parseDouble(s));

    @Test
    public void test() throws Exception {
        assertFalse(function.test("1", 2.));
        assertTrue(function.test("2", 2.));

        assertException(() -> function.test("", 2.), NumberFormatException.class);
        assertException(() -> function.test("2.", null), NullPointerException.class);
    }

    @Test
    public void tryTest() {
        assertFalse(function.tryTest("1", 2.));
        assertTrue(function.tryTest("2.0000", 2.));

        try(Revokable<Function<Exception, Object>> revokable = Revokable.register(
            Functions::getDefaultExceptionHandler, f -> Functions.setDefaultExceptionHandler(f), Functions::returnsNull)) {
            assertException(() -> function.tryTest("string", 0.), NullPointerException.class);

            Functions.setDefaultExceptionHandler(e -> e);
            assertException(() -> function.tryTest("string", 0.), ClassCastException.class);

            Functions.setDefaultExceptionHandler(e -> true);
            assertTrue(function.tryTest("string", 0.));

            Functions.setDefaultExceptionHandler(Functions::logThenThrows);
            assertException(() -> function.tryTest("string", 0.), IllegalStateException.class);
        }
    }

    @Test
    public void asSupplierThrowable() throws Exception {
        assertTrue(function.asSupplierThrowable("2.0000", 2.).get());
        assertFalse(function.asSupplierThrowable("20", 2.).get());

        assertException(function.asSupplierThrowable("", 2.), NumberFormatException.class);
        assertNull(function.asSupplierThrowable("", 2.).tryGet());
    }

    @Test
    public void withHandler() {
        assertTrue(function.withHandler(Functions::logThenThrows).test("2", 2.));
        assertFalse(function.withHandler(Functions::logThenThrows).test("-2", 2.));

        assertException(() -> function.withHandler(Functions::logThenThrows).test("string", 2.), IllegalStateException.class);
        assertException(() -> function.withHandler(Functions::logAndReturnsNull).test("", 2.), NullPointerException.class);
        assertTrue(function.withHandler(Functions::returnsTrue).test("string", 2.));
        assertFalse(function.withHandler(Functions::returnsFalse).test("2", null));
    }

    @Test
    public void orElse() {
        assertTrue(function.orElse(false).test("2.0", 2.));
        assertFalse(function.orElse(true).test("1.0", 2.));

        assertFalse(function.orElse(false).test("1.0", null));
        assertTrue(function.orElse(true).test(null, null));
        assertFalse(function.orElse(false).test("1", null));
    }
}