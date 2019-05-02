package io.github.cruisoring;

import io.github.cruisoring.function.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.cruisoring.Asserts.checkWithoutNull;

/**
 * Functional Interface executioner to execute methods with expected 1-7 parameters as
 * either RunnableThrowable or SupplierThrowable depending on if it returns void or some result.
 */
public class Functions<R> {

    /**
     * Constructor with a Non-nullable ExceptionHandler to define behaviours when execute an WithValueReturned.
     * Notice: thanks to the Java Type Erasing, the Functions instance created can be used with Lambda expressions of
     * different return types.
     *
     * @param exceptionHandler ExceptionHandler instance that could be defined as Lambda to either throw RuntimeException or
     *                         return default value of type R.
     * @param defaultReturner  generic Exception handler that handle the thrown Exception and return a default value
     *                         expected by the withValueReturned
     */
    public Functions(Consumer<Exception> exceptionHandler, BiFunction<Exception, WithValueReturned, Object> defaultReturner) {
        checkWithoutNull(exceptionHandler);
        this.handler = exceptionHandler;
        this.defaultReturner = defaultReturner;
    }

    // Static Functions instance to simply throw RuntimeException whenever an Exception is caught.
    public static final Functions ThrowsRuntimeException = new Functions();

    // Static Functions instance to hidden any Exceptions by returning default values matching the given Lambda Expression
    private static BiFunction<Exception, WithValueReturned, Object> returnDefaultValue =
            (Exception ex, WithValueReturned throwable) -> TypeHelper.getDefaultValue(TypeHelper.getReturnType(throwable));
    //    public static final Functions ReturnsDefaultValue = new Functions((ExceptionHandler) returnDefaultValue);
    @SuppressWarnings("unchecked")
    public static final Functions ReturnsDefaultValue = new Functions(
            ex -> {
            },
            returnDefaultValue
    );
    // Default Functions executioer that could be changed dynamically. For instance, change it to ThrowsRuntimeException
    // when debugging, while ReturnsDefaultValue when releasing.
    public static Functions Default = ReturnsDefaultValue;
    private static Integer processorNumber;
    //Exception Handler that shall throw either RuntimeException or return default value of Type R when an exception is caught.
    private final Consumer<Exception> handler;
    private final BiFunction<Exception, WithValueReturned, Object> defaultReturner;

    /**
     * Constructor without ExceptionHandler provided, would always throw RuntimeException accordingly.
     */
    private Functions() {
        this.handler = null;
        this.defaultReturner = null;
    }

    /**
     * Factory to create Functions instance with given exception handler and default value factory.
     *
     * @param exceptionHandler    Exception Handler to allow differentiated processing of different kinds of Excpeitons.
     * @param defaultValueFactory Default return value factory based on the returned type of the Lambda Expression.
     * @param <R>                 Type of the return value.
     * @return Higher-order Function executioner with customised behaviours.
     */
    public static <R> Functions<R> buildFunctions(Consumer<Exception> exceptionHandler,
                                                  BiFunction<Exception, WithValueReturned, R> defaultValueFactory) {
        checkWithoutNull(exceptionHandler);
        checkWithoutNull(defaultValueFactory);
        return new Functions(exceptionHandler, defaultValueFactory);
    }

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

        if (processorNumber == null) {
            processorNumber = Runtime.getRuntime().availableProcessors();
        }

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

    /**
     * Execute the given RunnableThrowable with ExceptionHandler to tackle the Exception thrown during the execution.
     * When <code>handler</code> is not null, it would handle the thrown Exception with the predefined logics; otherwise
     * RuntimeException would be thrown.
     *
     * @param runnableThrowable Service logic receiving and returning no arguments.
     */
    public void run(RunnableThrowable runnableThrowable) {
        try {
            runnableThrowable.run();
        } catch (Exception ex) {
            if (handler != null)
                handler.accept(ex);
            else
                throw new RuntimeException(ex);
        }
    }

    /**
     * Execute the given SupplierThrowable with ExceptionHandler to tackle the Exception thrown during the execution.
     * When <code>handler</code> is not null, it would handle the thrown Exception with the predefined logics to return
     * the default value of given type; otherwise RuntimeException would be thrown.
     *
     * @param supplierThrowable Service logic receiving no arguments, but return something of type T.
     * @return The value returned by the service logic of <code>supplierThrowable</code>, or the default value returned
     * by the handler by parsing return type of the given <code>supplierThrowable</code>.
     */
    public R apply(SupplierThrowable<R> supplierThrowable) {
        try {
            return supplierThrowable.get();
        } catch (Exception ex) {
            return handler == null ? null : (R) defaultReturner.apply(ex, supplierThrowable);
        }
    }

    /**
     * Run a method accepting one argument with the actual one argument.
     *
     * @param consumer Lambda expression accepting one argument and returns nothing.
     * @param t        Argument to be consumed by the Lambda Expression.
     * @param <T>      Type of the argment <code>t</code>
     */
    public <T> void run(
            ConsumerThrowable<T> consumer,
            T t) {
        consumer.withHandler(handler).accept(t);
    }

    /**
     * Run a method accepting two argument with the actual two arguments.
     *
     * @param consumer Lambda expression accepting two argument and returns nothing.
     * @param t        First argument to be consumed by the Lambda Expression.
     * @param u        Second argument to be consumed by the Lambda Expression.
     * @param <T>      Type of the first argment <code>t</code>
     * @param <U>      Type of the second argument <code>u</code>
     */
    public <T, U> void run(
            BiConsumerThrowable<T, U> consumer,
            T t, U u) {
        consumer.withHandler(handler).accept(t, u);
    }

    /**
     * Run a method accepting 3 argument with the actual 3 arguments.
     *
     * @param consumer Lambda expression accepting three argument and returns nothing.
     * @param t        First argument to be consumed by the Lambda Expression.
     * @param u        Second argument to be consumed by the Lambda Expression.
     * @param v        The third argument to be consumed by the Lambda Expression.
     * @param <T>      Type of the first argment <code>t</code>
     * @param <U>      Type of the second argument <code>u</code>
     * @param <V>      Type of the third argument <code>v</code>
     */
    public <T, U, V> void run(
            TriConsumerThrowable<T, U, V> consumer,
            T t, U u, V v) {
        consumer.withHandler(handler).accept(t, u, v);
    }

    /**
     * Run a method accepting 4 argument with the actual 4 arguments.
     *
     * @param consumer Lambda expression accepting seven argument and returns nothing.
     * @param t        The first argument to be consumed by the Lambda Expression.
     * @param u        The second argument to be consumed by the Lambda Expression.
     * @param v        The third argument to be consumed by the Lambda Expression.
     * @param w        The fourth argument to be consumed by the Lambda Expression.
     * @param <T>      Type of the first argment <code>t</code>
     * @param <U>      Type of the second argument <code>u</code>
     * @param <V>      Type of the third argument <code>v</code>
     * @param <W>      Type of the fourth argument <code>w</code>
     */
    public <T, U, V, W> void run(
            QuadConsumerThrowable<T, U, V, W> consumer,
            T t, U u, V v, W w) {
        consumer.withHandler(handler).accept(t, u, v, w);
    }


    /**
     * Run a method accepting 5 argument with the actual 5 arguments.
     *
     * @param consumer Lambda expression accepting seven argument and returns nothing.
     * @param t        The first argument to be consumed by the Lambda Expression.
     * @param u        The second argument to be consumed by the Lambda Expression.
     * @param v        The third argument to be consumed by the Lambda Expression.
     * @param w        The fourth argument to be consumed by the Lambda Expression.
     * @param x        The fifth argument to be consumed by the Lambda Expression.
     * @param <T>      Type of the first argment <code>t</code>
     * @param <U>      Type of the second argument <code>u</code>
     * @param <V>      Type of the third argument <code>v</code>
     * @param <W>      Type of the fourth argument <code>w</code>
     * @param <X>      Type of the fifth argument <code>x</code>
     */
    public <T, U, V, W, X> void run(
            PentaConsumerThrowable<T, U, V, W, X> consumer,
            T t, U u, V v, W w, X x) {
        consumer.withHandler(handler).accept(t, u, v, w, x);
    }

    /**
     * Run a method accepting 6 argument with the actual 6 arguments.
     *
     * @param consumer Lambda expression accepting seven argument and returns nothing.
     * @param t        The first argument to be consumed by the Lambda Expression.
     * @param u        The second argument to be consumed by the Lambda Expression.
     * @param v        The third argument to be consumed by the Lambda Expression.
     * @param w        The fourth argument to be consumed by the Lambda Expression.
     * @param x        The fifth argument to be consumed by the Lambda Expression.
     * @param y        The sixth argument to be consumed by the Lambda Expression.
     * @param <T>      Type of the first argment <code>t</code>
     * @param <U>      Type of the second argument <code>u</code>
     * @param <V>      Type of the third argument <code>v</code>
     * @param <W>      Type of the fourth argument <code>w</code>
     * @param <X>      Type of the fifth argument <code>x</code>
     * @param <Y>      Type of the sixth argument <code>y</code>
     */
    public <T, U, V, W, X, Y> void run(
            HexaConsumerThrowable<T, U, V, W, X, Y> consumer,
            T t, U u, V v, W w, X x, Y y) {
        consumer.withHandler(handler).accept(t, u, v, w, x, y);
    }

    /**
     * Run a method accepting 7 argument with the actual 7 arguments.
     *
     * @param consumer Lambda expression accepting seven argument and returns nothing.
     * @param t        The first argument to be consumed by the Lambda Expression.
     * @param u        The second argument to be consumed by the Lambda Expression.
     * @param v        The third argument to be consumed by the Lambda Expression.
     * @param w        The fourth argument to be consumed by the Lambda Expression.
     * @param x        The fifth argument to be consumed by the Lambda Expression.
     * @param y        The sixth argument to be consumed by the Lambda Expression.
     * @param z        The seventh argument to be consumed by the Lambda Expression.
     * @param <T>      Type of the first argment <code>t</code>
     * @param <U>      Type of the second argument <code>u</code>
     * @param <V>      Type of the third argument <code>v</code>
     * @param <W>      Type of the fourth argument <code>w</code>
     * @param <X>      Type of the fifth argument <code>x</code>
     * @param <Y>      Type of the sixth argument <code>y</code>
     * @param <Z>      Type of the seventh argument <code>z</code>
     */
    public <T, U, V, W, X, Y, Z> void run(
            HeptaConsumerThrowable<T, U, V, W, X, Y, Z> consumer,
            T t, U u, V v, W w, X x, Y y, Z z) {
        consumer.withHandler(handler).accept(t, u, v, w, x, y, z);
    }

    /**
     * Predicate to evaluate one argument <code>t</code> of type <code>T</code>
     *
     * @param predicate Method to accept one argument of type <code>T</code>
     * @param t         First argument of type <code>T</code>
     * @param <T>       Type of the given argument <code>t</code>
     * @return <code>True</code> if predicate is success, otherwise <code>False</code>
     */
    public <T> Boolean test(PredicateThrowable<T> predicate, T t) {
        Predicate<T> predicate1 = (t1) -> {
            try {
                return predicate.test(t1);
            } catch (Exception e) {
                return defaultReturner == null ? false : (Boolean) defaultReturner.apply(e, predicate);
            }
        };
        return predicate1.test(t);
    }

    /**
     * Predicate to evaluate two arguments
     *
     * @param predicate Method to accept two arguments of type <code>T</code> and <code>U</code>
     * @param t         First argument of type <code>T</code>
     * @param u         Second argument of type <code>U</code>
     * @param <T>       Type of the first argument <code>t</code>
     * @param <U>       Type of the second argument <code>u</code>
     * @return <code>True</code> if predicate is success, otherwise <code>False</code>
     */
    public <T, U> Boolean test(
            BiPredicateThrowable<T, U> predicate,
            T t, U u) {
        BiPredicate<T, U> predicate1 = (t1, u1) -> {
            try {
                return predicate.test(t1, u1);
            } catch (Exception e) {
                return defaultReturner == null ? false : (Boolean) defaultReturner.apply(e, predicate);
            }
        };
        return predicate1.test(t, u);
    }

    /**
     * Execute a method accepting 1 argument with the actual 1 arguments, and return its result.
     *
     * @param function Lambda expression accepting seven argument and returns one result.
     * @param t        The first argument to be consumed by the Lambda Expression.
     * @param <T>      Type of the first argment <code>t</code>
     * @return The value returned by the service logic of <code>rop</code>, or the default value returned
     * by the handler by parsing return type of the given <code>rop</code>.
     */
    public <T> R apply(FunctionThrowable<T, R> function,
                       T t) {
        return function.withHandler(defaultReturner).apply(t);
    }

    /**
     * Execute a method accepting 2 argument with the actual 2 arguments, and return its result.
     *
     * @param function Lambda expression accepting seven argument and returns one result.
     * @param t        The first argument to be consumed by the Lambda Expression.
     * @param u        The second argument to be consumed by the Lambda Expression.
     * @param <T>      Type of the first argment <code>t</code>
     * @param <U>      Type of the second argument <code>u</code>
     * @return The value returned by the service logic of <code>rop</code>, or the default value returned
     * by the handler by parsing return type of the given <code>rop</code>.
     */
    public <T, U> R apply(
            BiFunctionThrowable<T, U, R> function,
            T t, U u) {
        return function.withHandler(defaultReturner).apply(t, u);
    }

    /**
     * Execute a method accepting 3 argument with the actual 3 arguments, and return its result.
     *
     * @param function Lambda expression accepting seven argument and returns one result.
     * @param t        The first argument to be consumed by the Lambda Expression.
     * @param u        The second argument to be consumed by the Lambda Expression.
     * @param v        The third argument to be consumed by the Lambda Expression.
     * @param <T>      Type of the first argment <code>t</code>
     * @param <U>      Type of the second argument <code>u</code>
     * @param <V>      Type of the third argument <code>v</code>
     * @return The value returned by the service logic of <code>rop</code>, or the default value returned
     * by the handler by parsing return type of the given <code>rop</code>.
     */
    public <T, U, V> R apply(
            TriFunctionThrowable<T, U, V, R> function,
            T t, U u, V v) {
        return function.withHandler(defaultReturner).apply(t, u, v);
    }

    /**
     * Execute a method accepting 4 argument with the actual 4 arguments, and return its result.
     *
     * @param function Lambda expression accepting seven argument and returns one result.
     * @param t        The first argument to be consumed by the Lambda Expression.
     * @param u        The second argument to be consumed by the Lambda Expression.
     * @param v        The third argument to be consumed by the Lambda Expression.
     * @param w        The fourth argument to be consumed by the Lambda Expression.
     * @param <T>      Type of the first argment <code>t</code>
     * @param <U>      Type of the second argument <code>u</code>
     * @param <V>      Type of the third argument <code>v</code>
     * @param <W>      Type of the fourth argument <code>w</code>
     * @return The value returned by the service logic of <code>rop</code>, or the default value returned
     * by the handler by parsing return type of the given <code>rop</code>.
     */
    public <T, U, V, W> R apply(
            QuadFunctionThrowable<T, U, V, W, R> function,
            T t, U u, V v, W w) {
        return function.withHandler(defaultReturner).apply(t, u, v, w);
    }

    /**
     * Execute a method accepting 5 argument with the actual 5 arguments, and return its result.
     *
     * @param function Lambda expression accepting seven argument and returns one result.
     * @param t        The first argument to be consumed by the Lambda Expression.
     * @param u        The second argument to be consumed by the Lambda Expression.
     * @param v        The third argument to be consumed by the Lambda Expression.
     * @param w        The fourth argument to be consumed by the Lambda Expression.
     * @param x        The fifth argument to be consumed by the Lambda Expression.
     * @param <T>      Type of the first argment <code>t</code>
     * @param <U>      Type of the second argument <code>u</code>
     * @param <V>      Type of the third argument <code>v</code>
     * @param <W>      Type of the fourth argument <code>w</code>
     * @param <X>      Type of the fifth argument <code>x</code>
     * @return The value returned by the service logic of <code>rop</code>, or the default value returned
     * by the handler by parsing return type of the given <code>rop</code>.
     */
    public <T, U, V, W, X> R apply(
            PentaFunctionThrowable<T, U, V, W, X, R> function,
            T t, U u, V v, W w, X x) {
        return function.withHandler(defaultReturner).apply(t, u, v, w, x);
    }

    /**
     * Execute a method accepting 6 argument with the actual 6 arguments, and return its result.
     *
     * @param function Lambda expression accepting seven argument and returns one result.
     * @param t        The first argument to be consumed by the Lambda Expression.
     * @param u        The second argument to be consumed by the Lambda Expression.
     * @param v        The third argument to be consumed by the Lambda Expression.
     * @param w        The fourth argument to be consumed by the Lambda Expression.
     * @param x        The fifth argument to be consumed by the Lambda Expression.
     * @param y        The sixth argument to be consumed by the Lambda Expression.
     * @param <T>      Type of the first argment <code>t</code>
     * @param <U>      Type of the second argument <code>u</code>
     * @param <V>      Type of the third argument <code>v</code>
     * @param <W>      Type of the fourth argument <code>w</code>
     * @param <X>      Type of the fifth argument <code>x</code>
     * @param <Y>      Type of the sixth argument <code>y</code>
     * @return The value returned by the service logic of <code>rop</code>, or the default value returned
     * by the handler by parsing return type of the given <code>rop</code>.
     */
    public <T, U, V, W, X, Y> R apply(
            HexaFunctionThrowable<T, U, V, W, X, Y, R> function,
            T t, U u, V v, W w, X x, Y y) {
        return function.withHandler(defaultReturner).apply(t, u, v, w, x, y);
    }

    /**
     * Execute a method accepting 7 argument with the actual 7 arguments, and return its result.
     *
     * @param function Lambda expression accepting seven argument and returns one result.
     * @param t        The first argument to be consumed by the Lambda Expression.
     * @param u        The second argument to be consumed by the Lambda Expression.
     * @param v        The third argument to be consumed by the Lambda Expression.
     * @param w        The fourth argument to be consumed by the Lambda Expression.
     * @param x        The fifth argument to be consumed by the Lambda Expression.
     * @param y        The sixth argument to be consumed by the Lambda Expression.
     * @param z        The seventh argument to be consumed by the Lambda Expression.
     * @param <T>      Type of the first argment <code>t</code>
     * @param <U>      Type of the second argument <code>u</code>
     * @param <V>      Type of the third argument <code>v</code>
     * @param <W>      Type of the fourth argument <code>w</code>
     * @param <X>      Type of the fifth argument <code>x</code>
     * @param <Y>      Type of the sixth argument <code>y</code>
     * @param <Z>      Type of the seventh argument <code>z</code>
     * @return The value returned by the service logic of <code>rop</code>, or the default value returned
     * by the handler by parsing return type of the given <code>rop</code>.
     */
    public <T, U, V, W, X, Y, Z> R apply(
            HeptaFunctionThrowable<T, U, V, W, X, Y, Z, R> function,
            T t, U u, V v, W w, X x, Y y, Z z) {
        return function.withHandler(defaultReturner).apply(t, u, v, w, x, y, z);
    }
}
