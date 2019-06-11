package io.github.cruisoring.throwables;

import io.github.cruisoring.Functions;
import io.github.cruisoring.Revokable;
import org.junit.Test;

import java.util.function.Function;

import static io.github.cruisoring.Asserts.assertEquals;
import static io.github.cruisoring.Asserts.assertException;

public class RunnableThrowableTest {
    Integer index = 0;
    RunnableThrowable runnableThrowable = () -> {
        if(index < 0){
            throw new IndexOutOfBoundsException();
        }
        index++;
    };

    @Test
    public void run() throws Exception {
        index = 0;
        runnableThrowable.run();
        assertEquals(1, index);

        index = -2;
        assertException(runnableThrowable, IndexOutOfBoundsException.class);

        index = null;
        assertException(runnableThrowable, NullPointerException.class);
    }

    @Test
    public void tryRun() {
        index = 0;
        runnableThrowable.tryRun();
        assertEquals(1, index);

        try(Revokable<Function<Exception, Object>> revokable = Revokable.register(
            Functions::getDefaultExceptionHandler, f -> Functions.setDefaultExceptionHandler(f), Functions::returnsNull)
        ) {
            index = -2;
            runnableThrowable.tryRun();
            assertEquals(-2, index);

            index = null;
            runnableThrowable.tryRun();
            assertEquals(null, index);

            Functions.setDefaultExceptionHandler(e -> {throw new RuntimeException();});
            assertException(runnableThrowable::tryRun, RuntimeException.class);

            Functions.setDefaultExceptionHandler(Functions::logThenThrows);
            assertException(runnableThrowable::tryRun, IllegalStateException.class);
        }

    }

    @Test
    public void withHandler() {
        index = 0;
        runnableThrowable.withHandler(Functions::throwsIllegalStateException).run();
        assertEquals(1, index);

        index = -1;
        assertException(runnableThrowable.withHandler(Functions::throwsIllegalStateException)::run, IllegalStateException.class);
    }
}