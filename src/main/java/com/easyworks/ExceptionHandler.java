package com.easyworks;

import java.util.function.Function;

@FunctionalInterface
public interface ExceptionHandler<T>
        extends Function<Exception, T> {
}
