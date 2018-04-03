package com.easyworks;

import com.easyworks.function.RunnableThrowable;

public interface Loggable {
    void log(String message, Object... arguments);
}
