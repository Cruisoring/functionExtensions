package io.github.cruisoring;

import io.github.cruisoring.logger.LogLevel;
import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.throwables.ConsumerThrowable;
import io.github.cruisoring.throwables.FunctionThrowable;
import io.github.cruisoring.throwables.RunnableThrowable;
import io.github.cruisoring.throwables.SupplierThrowable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Functional Interface executioner to execute methods with expected 1-7 parameters as
 * either RunnableThrowable or SupplierThrowable depending on if it returns void or some result.
 */
public class Functions {
    //Default LogLevel to log the exception when needed
    public static LogLevel defaultExceptionLogLevel = LogLevel.debug;

    //Default Exception Handler that determines how voidThrowables or getThrowables handle Exception when calling withHandler(...)
    static Function<Exception, Object> defaultExceptionHandler = Functions::returnsNull;

    /**
     * Get the current defaultExceptionHandler that would change default behaviours of all {@code ofThrowable}s,
     * including tryApply()/tryTest()/tryAccept()/tryRun() of them.
     * @return the current {@code Function<Exception, Object>} instance used as default handler to handle Exception of all {@code ofThrowable}s.
     */
    public static Function<Exception, Object> getDefaultExceptionHandler() {
        return defaultExceptionHandler;
    }

    /**
     * Set the Functions.defaultExceptionHandler to a new handler that would change the default behaviours of all {@code ofThrowable}s,
     * including tryApply()/tryTest()/tryAccept()/tryRun() of them.
     * @param handler   the new {@code Function<Exception, Object>} instance used as default handler to handle Exception of all {@code ofThrowable}s.
     * @return  <tt>true</tt> if the Functions.defaultExceptionHandler is changed successfully, otherwise <tt>false</tt>
     */
    public static boolean setDefaultExceptionHandler(Function<Exception, Object> handler){
        if(handler == null || handler == defaultExceptionHandler) {
            return false;
        } else {
            defaultExceptionHandler = handler;
            return true;
        }
    }

    /**
     * Pre-defined handler to return null for any Exception caught.
     * @param exception the Exception to be handled that might keep the original Exception as its cause.
     * @return  null no matter what the Exception is.
     */
    public static Object returnsNull(Exception exception) {
        return null;
    }

    /**
     * Pre-defined handler to return <tt>false</tt> for any Exception caught.
     * @param exception the Exception to be handled that might keep the original Exception as its cause.
     * @return  <tt>false</tt> no matter what the Exception is.
     */
    public static Object returnsFalse(Exception exception) {
        return false;
    }

    /**
     * Pre-defined handler to return <tt>true</tt> for any Exception caught.
     * @param exception the Exception to be handled that might keep the original Exception as its cause.
     * @return  <tt>true</tt> no matter what the Exception is.
     */
    public static Object returnsTrue(Exception exception) {
        return true;
    }

    /**
     * Pre-defined handler to simply throw an {@code IllegalStateException} to wrap orginal Exception as RuntimeException.
     * @param cause the original Exception thrown by calling the functional interaface.
     * @return  nothing to be returned when the {@code IllegalStateException} is thrown
     */
    public static Object throwsIllegalStateException(Exception cause){
        throw new IllegalStateException(cause.getClass().getSimpleName(), cause);
    }

    /**
     * Pre-defined handler to simply log the details of the Exception before returnning null.
     * @param cause the original Exception thrown by calling the functional interaface.
     * @return  always return null.
     */
    public static Object logAndReturnsNull(Exception cause){
        Logger.getDefault().log(defaultExceptionLogLevel, cause);
        return null;
    }

    /**
     * Pre-defined handler to log the details of the Exception before throw an {@code IllegalStateException} to wrap orginal Exception as RuntimeException.
     * @param cause the original Exception thrown by calling the functional interaface.
     * @return  nothing to be returned when the {@code IllegalStateException} is thrown
     */
    public static Object logThenThrows(Exception cause){
        logAndReturnsNull(cause);
        return throwsIllegalStateException(cause);
    }

    /**
     *
     * @param runnableThrowable
     */
    public static void tryRun(RunnableThrowable runnableThrowable){
        runnableThrowable.withHandler(defaultExceptionHandler).run();
    }

    public static <R> R tryGet(SupplierThrowable<R> supplierThrowable){
        return supplierThrowable.tryGet();
    }


    private final static Integer processorNumber = Runtime.getRuntime().availableProcessors();

    /**
     * For each of the inputs, apply throwables parallelly to try to get output within timeout specified by timeoutMillis
     *
     * @param function     Function to get result of type <code>R</code> with input of type <code>T</code>
     * @param inputs       Multiple input value of type <code>T</code> to be applied to <code>throwables</code>
     * @param timeoutMills Timeout value in Milliseconds
     * @param <T>          Type of the input values <code>inputs</code>
     * @param <R>          Type of the result returned by applying input to <code>throwables</code>
     * @return A list of converted results from <code>inputs</code> by <code>throwables</code> or null when Exception caught.
     */
    public static <T, R> List<R> applyParallel(FunctionThrowable<T, R> function, List<T> inputs, long timeoutMills) {
        List<Callable<R>> callables = new ArrayList<>();
        inputs.stream().forEach(input -> {
            callables.add(() -> function.apply(input));
        });

        ExecutorService EXEC = Executors.newCachedThreadPool();
        try {
            List<R> results;
            results = EXEC.invokeAll(callables)
                    .stream()
                    .map(f -> {
                        try {
                            return f.get(timeoutMills, TimeUnit.MINUTES);
                        } catch (Exception e) {
                            throw new IllegalStateException(e);
                        }
                    })
                    .collect(Collectors.toList());
            return results;
        } catch (InterruptedException e) {
            return null;
        } finally {
            EXEC.shutdown();
        }
    }

    public static <T> void runParallel(ConsumerThrowable<T> consumerThrowable, Stream<T> paramStream, long timeoutMills) {
        List<Callable<Void>> callables = new ArrayList<>();
        paramStream.forEach(param -> {
            callables.add(() -> {
                consumerThrowable.accept(param);
                return null;
            });
        });

        ExecutorService EXEC = Executors.newCachedThreadPool();
        try {
            EXEC.invokeAll(callables, timeoutMills, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        } finally {
            EXEC.shutdown();
        }
    }

    public static void runParallel(ConsumerThrowable<Integer> consumerThrowable, int length) {
        if (length < 1)
            throw new IllegalArgumentException("length must be greater than 0");

        int threadNumer = Integer.min(processorNumber, length / 10);
        ExecutorService EXEC = Executors.newFixedThreadPool(threadNumer);

        int step = length / threadNumer;
        List<Callable<Void>> callables = new ArrayList<>();
        try {
            for (int i = 0; i < threadNumer; i++) {
                final int start = i * step;
                final int end = Integer.min(length, (i + 1) * step);
                callables.add(() -> {
                    for (int j = start; j < end; j++) {
                        consumerThrowable.accept(j);
                    }
                    return null;
                });
            }

            EXEC.invokeAll(callables);
        } catch (InterruptedException e) {
        } finally {
            EXEC.shutdownNow();
        }
    }

    /**
     * Run the tasks parallely within the time of <code>timeoutMills</code>ms, disregard any Exceptions.
     *
     * @param tasks        The tasks to be executed parallely within the timeout
     * @param timeoutMills Timeout value in Milliseconds
     */
    public static void runParallel(List<RunnableThrowable> tasks, long timeoutMills) {
        List<Callable<Void>> callables = new ArrayList<>();
        tasks.stream().forEach(runnable -> {
            callables.add(() -> {
                runnable.run();
                return null;
            });
        });

        ExecutorService EXEC = Executors.newCachedThreadPool();
        try {
            EXEC.invokeAll(callables, timeoutMills, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        } finally {
            EXEC.shutdown();
        }
    }

}
