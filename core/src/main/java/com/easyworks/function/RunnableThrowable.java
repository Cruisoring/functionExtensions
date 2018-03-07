package com.easyworks.function;

/**
 * Functional Interface defined to identify methods returning nothing while their service logic could throw Exceptions.
 */
@FunctionalInterface
public interface RunnableThrowable extends AbstractThrowable {

    /**
     * The abstract method to be mapped to Lambda Expresion accepting no argument and returning nothing.
     * @throws Exception    Any Exception could be thrown by the concerned service logic.
     */
    void run() throws Exception;

    /**
     * Defualt method to combine another method together, accepting no argument and returning nothing.
     * @param other Another method accepting no argument and returning nothing.
     * @return  A combined RunnableThrowable running <code>other</code> first, then <code>this</code> next.
     */
    default RunnableThrowable startWith(RunnableThrowable other){
        return () -> {
            other.run();
            this.run();
        };
    }

    /**
     * Default method to combine another method together, accepting no argument and returning nothing.
     * @param other Another method accepting no argument and returning nothing.
     * @return  A combined RunnableThrowable running <code>this</code> first, then <code>other</code> next.
     */
    default RunnableThrowable followWith(RunnableThrowable other) {
        return () -> {
            this.run();
            other.run();
        };
    }


}