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
        assertAllFalse(function.apply(1, 2));
        assertAllTrue(function.apply(2, 2));

        assertException(function.asSupplierThrowable(1, 0), ArithmeticException.class);
    }

    @Test
    public void tryApply() {
        assertAllFalse(function.tryApply(1, 2));
        assertAllTrue(function.tryApply(2, 2));

        try(Revokable<Function<Exception, Object>> revokable = Revokable.register(
            Functions::getDefaultExceptionHandler, f -> Functions.setDefaultExceptionHandler(f), Functions::returnsNull)) {
            assertAllNull(function.tryApply(1, 0));

            Functions.setDefaultExceptionHandler(e -> e);
            assertException(() -> function.tryApply(1, 0), ClassCastException.class);

            Functions.setDefaultExceptionHandler(e -> true);
            assertAllTrue(function.tryApply(1, 0));

            Functions.setDefaultExceptionHandler(Functions::logThenThrows);
            assertException(() -> function.tryApply(1, 0), IllegalStateException.class);
        }
    }

    @Test
    public void asSupplierThrowable() throws Exception {
        assertAllTrue(function.asSupplierThrowable(4, 2).get());
        assertAllFalse(function.asSupplierThrowable(4, -2).get());

        assertException(function.asSupplierThrowable(1, 0), ArithmeticException.class);
        assertAllNull(function.asSupplierThrowable(1, 0).tryGet());
    }

    @Test
    public void withHandler() {
        assertAllTrue(function.withHandler(Functions::logThenThrows).apply(4, 2));
        assertAllFalse(function.withHandler(Functions::logThenThrows).apply(-4, 2));

        assertAllNull(function.withHandler(Functions::logAndReturnsNull).apply(1, 0));
        assertAllTrue(function.withHandler(Functions::returnsTrue).apply(1, 0));
        assertAllFalse(function.withHandler(Functions::returnsFalse).apply(1, 0));
        assertException(() -> function.withHandler(Functions::logThenThrows).apply(1, 0), IllegalStateException.class);
    }

    @Test
    public void orElse() {
        assertAllTrue(function.orElse(false).apply(4, 2));
        assertAllFalse(function.orElse(false).apply(-4, 2));
        assertAllFalse(function.orElse(true).apply(-4, 2));

        assertAllNull(function.orElse(null).apply(1, 0));
        assertAllFalse(function.orElse(false).apply(1, 0));
    }
}