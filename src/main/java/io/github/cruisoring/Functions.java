package io.github.cruisoring;

import io.github.cruisoring.function.ConsumerThrowable;
import io.github.cruisoring.function.FunctionThrowable;
import io.github.cruisoring.function.RunnableThrowable;
import io.github.cruisoring.function.SupplierThrowable;
import io.github.cruisoring.logger.LogLevel;
import io.github.cruisoring.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Functional Interface executioner to execute methods with expected 1-7 parameters as
 * either RunnableThrowable or SupplierThrowable depending on if it returns void or some result.
 */
public class Functions {
    public static LogLevel defaultExceptionLogLevel = LogLevel.debug;

    public static BiFunction<String, Exception, Object> defaultExceptionHandler = Functions::returnNull;


    private static Object returnNull(String message, Exception cause) {
        return null;
    }

    private static Object throwsIllegalStateException(String message, Exception cause){
        throw new IllegalStateException(message, cause);
    }

    private static Object logOnly(String message, Exception cause){
        if(message != null) {
            Logger.getDefault().log(defaultExceptionLogLevel, message);
        }
        Logger.getDefault().log(defaultExceptionLogLevel, cause);
        return null;
    }

    private static Object logThenThrows(String message, Exception cause){
        logOnly(message, cause);
        return throwsIllegalStateException(message, cause);
    }

    public static void tryRun(RunnableThrowable runnableThrowable){
        runnableThrowable.withHandler().run();
    }

    public static <R> R tryGet(SupplierThrowable<R> supplierThrowable){
        return supplierThrowable.withHandler().get();
    }


    private final static Integer processorNumber = Runtime.getRuntime().availableProcessors();

    /**
     * For each of the inputs, apply function parallelly to try to get output within timeout specified by timeoutMillis
     *
     * @param function     Function to get result of type <code>R</code> with input of type <code>T</code>
     * @param inputs       Multiple input value of type <code>T</code> to be applied to <code>function</code>
     * @param timeoutMills Timeout value in Milliseconds
     * @param <T>          Type of the input values <code>inputs</code>
     * @param <R>          Type of the result returned by applying input to <code>function</code>
     * @return A list of converted results from <code>inputs</code> by <code>function</code> or null when Exception caught.
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
