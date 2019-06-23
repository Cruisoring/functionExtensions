package io.github.cruisoring;

import io.github.cruisoring.logger.LogLevel;
import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.throwables.*;
import io.github.cruisoring.utility.PlainList;

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

    private final static Integer processorNumber = Runtime.getRuntime().availableProcessors();

    //The default interval used to perform repetitive operations
    public static long defaultDelayMills = 100;

    //Default Exception Handler that determines how the function handle Exception when calling withHandler(...)
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
     * Execute the given business logic by using the {@code Functions::defaultExceptionHandler} to handle Exception.
     * @param runnableThrowable the business logic without returning value to be executed.
     */
    public static void tryRun(RunnableThrowable runnableThrowable){
        runnableThrowable.withHandler(defaultExceptionHandler).run();
    }

    /**
     * Execute the given business logic to get returned value by using the {@code Functions::defaultExceptionHandler} to handle Exception.
     * @param supplierThrowable the business logic with returning value to be executed.
     * @param <R>   type of the returned value
     * @return      the value returned by the concerned business logic, or something handled by the
     * {@code Functions::defaultExceptionHandler} when Exception is caught.
     */
    public static <R> R tryGet(SupplierThrowable<R> supplierThrowable){
        return supplierThrowable.withHandler(defaultExceptionHandler).get();
    }

    /**
     * For each of the inputs, apply function in parallel to try to get output within timeout specified by timeoutMillis
     *
     * @param function     Function to get result of type <code>R</code> with input of type <code>T</code>
     * @param inputs       Multiple input value of type <code>T</code> to be applied to concerned <code>function</code>
     * @param timeoutMills Timeout value in Milliseconds
     * @param <T>          Type of the input values <code>inputs</code>
     * @param <R>          Type of the result returned by applying input to <code>function</code>
     * @return A list of converted results from <code>inputs</code> by <code>function</code> or null when Exception caught.
     */
    public static <T, R> List<R> applyParallel(FunctionThrowable<T, R> function, List<T> inputs, long timeoutMills) {
        List<Callable<R>> callables = new PlainList<>();
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
        } catch (InterruptedException ignored) {
            return null;
        } finally {
            EXEC.shutdown();
        }
    }

    /**
     * Generic method to consume a stream of values with the same business logic in parallel within the given time window.
     * @param consumerThrowable business logic to be executed upon a given value of type <tt>T</tt>
     * @param paramStream       a stream of values to feed the concerned business logic
     * @param timeoutMills      Timeout value in Milliseconds
     * @param <T>               Type of the value consumed by the concerned business logic
     */
    public static <T> void runParallel(ConsumerThrowable<T> consumerThrowable, Stream<T> paramStream, long timeoutMills) {
        List<Callable<Void>> callables = new PlainList<>();
        paramStream.forEach(param -> {
            callables.add(() -> {
                consumerThrowable.accept(param);
                return null;
            });
        });

        ExecutorService EXEC = Executors.newCachedThreadPool();
        try {
            EXEC.invokeAll(callables, timeoutMills, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ignored) {
        } finally {
            EXEC.shutdown();
        }
    }

    /**
     * Generic method that can be used upon a {@code List} or {@code Array} with action specified on each of its elements.
     * @param consumerThrowable the business logic to be run against every elements of a {@code List} or {@code Array}
     * @param length    the length of the concerned {@code List} or {@code Array}
     */
    public static void withElementsParallel(ConsumerThrowable<Integer> consumerThrowable, int length) {
        if (length < 1)
            throw new IllegalArgumentException("length must be greater than 0");

        int threadNumer = Integer.min(processorNumber, length / 10);
        ExecutorService EXEC = Executors.newFixedThreadPool(threadNumer);

        int step = length / threadNumer;
        List<Callable<Void>> callables = new PlainList<>();
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
        } catch (InterruptedException ignored) {
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
        List<Callable<Void>> callables = new PlainList<>();
        tasks.stream().forEach(runnable -> {
            callables.add(() -> {
                runnable.run();
                return null;
            });
        });

        ExecutorService EXEC = Executors.newCachedThreadPool();
        try {
            EXEC.invokeAll(callables, timeoutMills, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ignored) {
        } finally {
            EXEC.shutdown();
        }
    }

    /**
     * Halt the thread by sleeping given millSeconds if it is valid.
     * @param timeMills     time to sleep in MilliSeconds, take effect only when it is greater than 0.
     */
    public static void sleep(long timeMills) {
        try {
            if (timeMills > 0) {
                TimeUnit.MILLISECONDS.sleep(timeMills);
            }
        } catch (InterruptedException e) {
            Logger.getDefault().log(defaultExceptionLogLevel, e);
        }
    }

    /**
     * Generic method to test if expected condition is met before timeout, the optional action specified would be called each time if expected condition is not met.
     * @param predicate         Predicate to check if expected condition is met.
     * @param timeoutMillis     Timeout in mills, notice it might block the process too long with a big value.
     * @param action            Optional action, if not null, to take if expected condition is not met
     * @param delayMills        Delay in millSeconds between tests that must be greater than 0.
     * @param initialDelayMills Initial dealy in mills.
     * @return  <tt>true</tt> if the expected condition is met before timeout, otherwise <tt>false</tt>
     */
    public static boolean testUntil(SupplierThrowable<Boolean> predicate, long timeoutMillis, RunnableThrowable action, long delayMills, long initialDelayMills) {
        Asserts.assertAllTrue(predicate != null, timeoutMillis >= 0, delayMills > 0, initialDelayMills >= 0);

        long now = System.currentTimeMillis();
        final long start = now;
        final long until = now + timeoutMillis;
        if(initialDelayMills > 0) {
            sleep(initialDelayMills);
        }

        Exception lastException = null;
        while (now < until){
            try {
                if (predicate.get()) {
                    return true;
                } else if (action != null) {
                    action.run();
                }
            }catch (Exception e){
                if(lastException == null || !TypeHelper.valueEquals(lastException.getMessage(), e.getMessage())){
                    lastException = e;
                    Logger.getDefault().log(defaultExceptionLogLevel, e);
                }
            }

            now = System.currentTimeMillis();
            long sleepToNext = delayMills - ((now - start)%delayMills);
            sleep(sleepToNext);
        }
        return false;
    }

    /**
     * Test if expected condition is met before timeout with default intervals immediately, the optional action specified would be called each time if expected condition is not met.
     * @param predicate         Predicate to check if expected condition is met.
     * @param timeoutMillis     Timeout in mills, notice it might block the process too long with a big value.
     * @param action            Optional action, if not null, to take if expected condition is not met
     * @return  <tt>true</tt> if the expected condition is met before timeout, otherwise <tt>false</tt>
     */
    public static boolean testUntil(SupplierThrowable<Boolean> predicate, long timeoutMillis, RunnableThrowable action) {
        return testUntil(predicate, timeoutMillis, action, defaultDelayMills, 0);
    }

    /**
     * Test if expected condition is met before timeout with default interval immediately.
     * @param predicate         Predicate to check if expected condition is met.
     * @param timeoutMillis     Timeout in mills, notice it might block the process too long with a big value.
     * @return  <tt>true</tt> if the expected condition is met before timeout, otherwise <tt>false</tt>
     */
    public static boolean testUntil(SupplierThrowable<Boolean> predicate, long timeoutMillis) {
        return testUntil(predicate, timeoutMillis, null);
    }

    /**
     * Generic method to get the value with given business logic with specific interval,
     * return the value when it meet criteria defined by the predicate or timeout happened.
     *
     * @param valueGetter   Business logic to specify how to get the value.
     * @param timeoutMillis Timeout in mills, notice it might block the process too long with a big value.
     * @param predicate     Predicate to see if the retrieved value is qualified.
     * @param delayMills    Delay in millSeconds between tests that must be greater than 0.
     * @param <T>           Type of the value to be returned.
     * @return              Value returned by the supplier, otherwise <tt>null</tt>.
     */
    public static <T> T tryGet(SupplierThrowable<T> valueGetter, long timeoutMillis, PredicateThrowable<T> predicate, long delayMills) {
        Asserts.assertAllTrue(valueGetter != null, timeoutMillis >= 0, predicate != null, delayMills > 0);

        long now = System.currentTimeMillis();
        final long start = now;
        final long until = now + timeoutMillis;

        Exception lastException = null;
        while (now < until){
            try {
                T result = valueGetter.tryGet();
                if (predicate.test(result)) {
                    return result;
                }
            }catch (Exception e){
                if(lastException == null || !TypeHelper.valueEquals(lastException.getMessage(), e.getMessage())){
                    lastException = e;
                    Logger.getDefault().log(defaultExceptionLogLevel, e);
                }
            }

            now = System.currentTimeMillis();
            long sleepToNext = delayMills - ((now - start)%delayMills);
            sleep(sleepToNext);
        }
        return null;
    }

    /**
     * Generic method to get the value with given business logic with default interval,
     * return the value when it is not <tt>null</tt> or timeout happened.
     *
     * @param valueGetter   Business logic to specify how to get the value.
     * @param timeoutMillis Timeout in mills, notice it might block the process too long with a big value.
     * @param <T>           Type of the value to be returned.
     * @return              Value returned by the supplier, otherwise <tt>null</tt>.
     */
    public static <T> T tryGet(SupplierThrowable<T> valueGetter, long timeoutMillis) {
        return tryGet(valueGetter, timeoutMillis, t -> t != null, defaultDelayMills);
    }

    private Functions(){}

}
