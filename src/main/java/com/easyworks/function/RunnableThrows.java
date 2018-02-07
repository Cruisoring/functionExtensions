package com.easyworks.function;

import com.easyworks.NoThrows;
import com.easyworks.RuntimeThrows;

@FunctionalInterface
public interface RunnableThrows {
    void run() throws Exception;

    default void runNoThrows(){
        NoThrows.run(this::run);
    }

    default void runRuntimeThrows(){
        RuntimeThrows.run(this::run);
    }

    default RunnableThrows tryStartWith(RunnableThrows other){
        return () -> {
            NoThrows.run(other);
            this.run();
        };
    }

    default RunnableThrows tryFollowWith(RunnableThrows other) {
        return () -> {
            try {
                this.run();
            } finally {
                NoThrows.run(other);
            }
        };
    }
}