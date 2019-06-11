package io.github.cruisoring.throwables;

import io.github.cruisoring.Functions;
import io.github.cruisoring.Revokable;
import org.junit.Test;

import java.util.function.Function;

import static io.github.cruisoring.Asserts.*;

public class FunctionThrowableTest {

    FunctionThrowable<String, String> function = s -> s.toLowerCase();

    @Test
    public void apply() throws Exception {
        assertEquals("abc", function.apply("ABC"));

        assertException(() -> function.apply(null), NullPointerException.class);
    }

    @Test
    public void tryApply() {
        assertEquals("abc", function.tryApply("ABC"));

        try(Revokable<Function<Exception, Object>> revokable = Revokable.register(
            Functions::getDefaultExceptionHandler, f -> Functions.setDefaultExceptionHandler(f), Functions::returnsNull)) {
            assertNull(function.tryApply(null));

            Functions.setDefaultExceptionHandler(e -> e);
            assertException(() -> function.tryApply(null), ClassCastException.class);

            Functions.setDefaultExceptionHandler(e -> "");
            assertEquals("", function.tryApply(null));

            Functions.setDefaultExceptionHandler(Functions::logThenThrows);
            assertException(() -> function.tryApply(null), IllegalStateException.class);
        }
    }

    @Test
    public void asSupplierThrowable() throws Exception {
        assertEquals("abc", function.asSupplierThrowable("Abc").get());
        assertException(() -> function.asSupplierThrowable(null).get(), NullPointerException.class);

        assertException(function.asSupplierThrowable(null), NullPointerException.class);
        assertNull(function.asSupplierThrowable(null).tryGet());
    }

    @Test
    public void withHandler() {
        assertEquals("abc", function.withHandler(Functions::logThenThrows).apply("abc"));
        assertEquals("abc", function.withHandler(Functions::logThenThrows).apply("ABC"));

        assertNull(function.withHandler(Functions::logAndReturnsNull).apply(null));
        assertException(() -> function.withHandler(Functions::returnsTrue).apply(null), ClassCastException.class);
        assertException(() -> function.withHandler(Functions::logThenThrows).apply(null), IllegalStateException.class);
    }

    @Test
    public void orElse() {
        assertEquals("something wrong", function.orElse("something wrong").apply(null));
        assertNull(function.orElse(null).apply(null));
    }
}