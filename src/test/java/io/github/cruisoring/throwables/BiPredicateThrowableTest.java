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
        assertAllFalse(function.test("1", 2.));
        assertAllTrue(function.test("2", 2.));

        assertException(() -> function.test("", 2.), NumberFormatException.class);
        assertException(() -> function.test("2.", null), NullPointerException.class);
    }

    @Test
    public void tryTest() {
        assertAllFalse(function.tryTest("1", 2.));
        assertAllTrue(function.tryTest("2.0000", 2.));

        try(Revokable<Function<Exception, Object>> revokable = Revokable.register(
            Functions::getDefaultExceptionHandler, f -> Functions.setDefaultExceptionHandler(f), Functions::returnsNull)) {
            assertException(() -> function.tryTest("string", 0.), NullPointerException.class);

            Functions.setDefaultExceptionHandler(e -> e);
            assertException(() -> function.tryTest("string", 0.), ClassCastException.class);

            Functions.setDefaultExceptionHandler(e -> true);
            assertAllTrue(function.tryTest("string", 0.));

            Functions.setDefaultExceptionHandler(Functions::logThenThrows);
            assertException(() -> function.tryTest("string", 0.), IllegalStateException.class);
        }
    }

    @Test
    public void asSupplierThrowable() throws Exception {
        assertAllTrue(function.asSupplierThrowable("2.0000", 2.).get());
        assertAllFalse(function.asSupplierThrowable("20", 2.).get());

        assertException(function.asSupplierThrowable("", 2.), NumberFormatException.class);
        assertAllNull(function.asSupplierThrowable("", 2.).tryGet());
    }

    @Test
    public void withHandler() {
        assertAllTrue(function.withHandler(Functions::logThenThrows).test("2", 2.));
        assertAllFalse(function.withHandler(Functions::logThenThrows).test("-2", 2.));

        assertException(() -> function.withHandler(Functions::logThenThrows).test("string", 2.), IllegalStateException.class);
        assertException(() -> function.withHandler(Functions::logAndReturnsNull).test("", 2.), NullPointerException.class);
        assertAllTrue(function.withHandler(Functions::returnsTrue).test("string", 2.));
        assertAllFalse(function.withHandler(Functions::returnsFalse).test("2", null));
    }

    @Test
    public void orElse() {
        assertAllTrue(function.orElse(false).test("2.0", 2.));
        assertAllFalse(function.orElse(true).test("1.0", 2.));

        assertAllFalse(function.orElse(false).test("1.0", null));
        assertAllTrue(function.orElse(true).test(null, null));
        assertAllFalse(function.orElse(false).test("1", null));
    }
}