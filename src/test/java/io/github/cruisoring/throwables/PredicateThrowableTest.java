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
        assertAllFalse(function.test("-1"));
        assertAllTrue(function.test("2"));

        assertException(() -> function.test(""), NumberFormatException.class);
        assertException(() -> function.test(null), NumberFormatException.class);
    }

    @Test
    public void tryTest() {
        assertAllFalse(function.tryTest("-1"));
        assertAllTrue(function.tryTest("2"));

        try(Revokable<Function<Exception, Object>> revokable = Revokable.register(
            Functions::getDefaultExceptionHandler, f -> Functions.setDefaultExceptionHandler(f), Functions::returnsNull)) {
            assertException(() -> function.tryTest("string"), NullPointerException.class);

            Functions.setDefaultExceptionHandler(e -> e);
            assertException(() -> function.tryTest("string"), ClassCastException.class);

            Functions.setDefaultExceptionHandler(e -> true);
            assertAllTrue(function.tryTest("string"));

            Functions.setDefaultExceptionHandler(Functions::logThenThrows);
            assertException(() -> function.tryTest("string"), IllegalStateException.class);
        }
    }

    @Test
    public void asSupplierThrowable() throws Exception {
        assertAllTrue(function.asSupplierThrowable("2").get());
        assertAllFalse(function.asSupplierThrowable("-3").get());

        assertException(function.asSupplierThrowable(""), NumberFormatException.class);
        assertAllNull(function.asSupplierThrowable("").tryGet());
    }

    @Test
    public void withHandler() {
        assertAllTrue(function.withHandler(Functions::logThenThrows).test("2"));
        assertAllFalse(function.withHandler(Functions::logThenThrows).test("-2"));

        assertException(() -> function.withHandler(Functions::logThenThrows).test("string"), IllegalStateException.class);
        assertException(() -> function.withHandler(Functions::logAndReturnsNull).test(""), NullPointerException.class);
        assertAllTrue(function.withHandler(Functions::returnsTrue).test("string"));
        assertAllFalse(function.withHandler(Functions::returnsFalse).test(""));
    }

    @Test
    public void orElse() {
        assertAllTrue(function.orElse(false).test("2"));
        assertAllFalse(function.orElse(true).test("-1"));

        assertAllFalse(function.orElse(false).test("sds"));
        assertAllTrue(function.orElse(true).test(null));
        assertAllFalse(function.orElse(false).test(""));
    }
}