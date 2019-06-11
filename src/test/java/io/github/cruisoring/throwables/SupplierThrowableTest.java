package io.github.cruisoring.throwables;

import io.github.cruisoring.Functions;
import io.github.cruisoring.Revokable;
import org.junit.Test;

import java.util.function.Function;

import static io.github.cruisoring.Asserts.*;

public class SupplierThrowableTest {
    
    Object valueString = null;
    SupplierThrowable<String> supplierThrowable = () -> ((String)valueString).toLowerCase();

    @Test
    public void get() throws Exception {
        valueString = "ABC";
        assertEquals("abc", supplierThrowable.get());

        valueString = null;
        assertException(supplierThrowable, NullPointerException.class);
    }

    @Test
    public void tryGet() {
        valueString = "ABC";
        assertEquals("abc", supplierThrowable.tryGet());

        try(Revokable<Function<Exception, Object>> revokable = Revokable.register(
            Functions::getDefaultExceptionHandler, f -> Functions.setDefaultExceptionHandler(f), Functions::returnsNull)) {
            valueString = 1123;            
            assertNull(supplierThrowable.tryGet());

            Functions.setDefaultExceptionHandler(e -> "");
            assertEquals("", supplierThrowable.tryGet());

            Functions.setDefaultExceptionHandler(Functions::logThenThrows);
            assertException(supplierThrowable::tryGet, IllegalStateException.class);
        }
    }

    @Test
    public void withHandler() {
        valueString = "ABC";
        assertEquals("abc", supplierThrowable.withHandler(Functions::logThenThrows).get());

        valueString = null;
        assertNull(supplierThrowable.withHandler(Functions::logAndReturnsNull).get());
        assertException(() -> supplierThrowable.withHandler(Functions::returnsTrue).get(), ClassCastException.class);
        assertException(() -> supplierThrowable.withHandler(Functions::logThenThrows).get(), IllegalStateException.class);
    }

    @Test
    public void orElse() {
        valueString = 123;
        assertEquals("something wrong", supplierThrowable.orElse("something wrong").get());
        assertNull(supplierThrowable.orElse(null).get());
    }

}