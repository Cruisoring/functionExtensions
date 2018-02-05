package com.easyworks.function;

import com.easyworks.NoThrows;

@FunctionalInterface
public interface RunnableThrows {
    void run() throws Exception;

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