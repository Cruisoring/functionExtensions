package io.github.cruisoring;

import io.github.cruisoring.function.ConsumerThrowable;

import java.util.Objects;

/**
 * AutoCloseableObject wrap the given Object as an AutoCloseable instance with mandatory action to taken when it is closed.
 *
 * @param <T> Type of the value to be wrapped.
 */
public class AutoCloseableObject<T> implements AutoCloseable {
    public final static ConsumerThrowable DoNothing = t -> {
    };

    final T value;
    final ConsumerThrowable<T> closing;
    private boolean isClosed = false;

    public AutoCloseableObject(T value, ConsumerThrowable<T> closingAction) {
        Objects.requireNonNull(closingAction);

        this.value = value;
        this.closing = closingAction;
    }

    public AutoCloseableObject(ConsumerThrowable<T> closingAction) {
        this(null, closingAction);
    }

    public T getValue() {
        return value;
    }

    /**
     * When value created, closing it and release any resource bounded if the instance is AutoCloseable.
     */
    public void closing() {
        if (!isClosed) {
            isClosed = true;
            closing.tryAccept(value);
        }
    }

    @Override
    public void close() throws Exception {
        closing();
    }
}
