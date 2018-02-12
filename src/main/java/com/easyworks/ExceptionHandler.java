package com.easyworks;

import java.util.function.Function;

import static sun.management.snmp.jvminstr.JvmThreadInstanceEntryImpl.ThreadStateMap.Byte0.runnable;

@FunctionalInterface
public interface ExceptionHandler<T>
        extends Function<Exception, T> {
}
