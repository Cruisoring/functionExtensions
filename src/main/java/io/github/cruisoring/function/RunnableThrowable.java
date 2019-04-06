package io.github.cruisoring.function;

import java.util.function.Consumer;

/**
 * Functional Interface defined to identify methods returning nothing while their service logic could throw Exceptions.
 */
@FunctionalInterface
public interface RunnableThrowable {

    /**
     * The abstract method to be mapped to Lambda Expresion accepting no argument and returning nothing.
     * @throws Exception    Any Exception could be thrown by the concerned service logic.
     */
    void run() throws Exception;


    /**
     * Execute <code>run()</code> and ignore any Exceptions thrown.
     */
    default void tryRun(){
        try {
            run();
        }catch (Exception e){ }
    }

    /**
     * Convert the RunnableThrowable to Runnable
     * @param exceptionHandler  Exception Handler of the caught Exceptions that retuns default value of type R.
     * @return  The Runnerable version of the original RunnableThrowable
     */
    default Runnable withHandler(Consumer<Exception> exceptionHandler){
        Runnable runnable = () -> {
            try {
                run();
            } catch (Exception e) {
                if(exceptionHandler != null)
                    exceptionHandler.accept(e);
            }
        };
        return runnable;
    }
}