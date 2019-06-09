package io.github.cruisoring.function;

import java.util.function.Consumer;

/**
 * Functional Interface defined to identify methods returning nothing while their service logic could throw Exceptions.
 */
@FunctionalInterface
public interface RunnableThrowable extends voidThrowable {

    /**
     * The abstract method to be mapped to Lambda Expresion accepting no argument and returning nothing.
     *
     * @throws Exception Any Exception could be thrown by the concerned service logic.
     */
    void run() throws Exception;

    /**
     * Execute <code>run()</code> and handle thrown Exception with the default handler of {@code throwsException}.
     */
    default void tryRun() {
        try {
            run();
        } catch (Exception e) {
            handle(e);
        }
    }

    /**
     * Convert the RunnableThrowable to Runnable
     *
     * @param exceptionHandlers Optional Exception Handlers to process the caught Exception with its first memeber if exists.
     * @return The Runnerable version that get Exceptions handled with the first of exceptionHandlers if given,
     *              otherwise {@code this::tryRun} if no exceptionHandler specified
     */
    default Runnable withHandler(Consumer<Exception>... exceptionHandlers) {
        if(exceptionHandlers == null || exceptionHandlers.length == 0) {
            return this::tryRun;
        } else {
            return () -> {
                try {
                    run();
                } catch (Exception e) {
                    exceptionHandlers[0].accept(e);
                }
            };
        }
    }
}