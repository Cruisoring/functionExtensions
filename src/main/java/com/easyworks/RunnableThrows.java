package com.easyworks;

@FunctionalInterface
public interface RunnableThrows {
    void run() throws Exception;
}