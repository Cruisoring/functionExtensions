package com.easyworks.function;

@FunctionalInterface
public interface RunnableThrows {
    void run() throws Exception;
}