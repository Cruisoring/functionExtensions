package com.easyworks;

import com.easyworks.function.*;
import com.easyworks.repository.Repository;
import com.easyworks.utility.Defaults;
import com.easyworks.utility.TypeHelper;
import sun.reflect.ConstantPool;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Functional Interface executioner to execute methods with expected 1-7 parameters as
 * either RunnableThrowable or SupplierThrowable depending on if it returns void or some result.
 */
public class Functions<R> {

    /**
     * Helper method to get the return type of a RunnableThrowable (as void.class) or SupplierThrowable.
     * Notice: only applicable on first-hand lambda expressions. Lambda Expressions created by Lambda would erase the return type in Java 1.8.161.
     * @param aThrowable solid Lambda expression
     * @return  The type of the return value defined by the Lambda Expression.
     */
    public static Class getReturnType(AbstractThrowable aThrowable){
        Objects.requireNonNull(aThrowable);
        Class lambdaClass = aThrowable.getClass();
        ConstantPool constantPool = TypeHelper.getConstantPoolOfClass(lambdaClass);
        Method functionInterfaceMethod = null;
        int index = constantPool.getSize();
        while(--index >=0) {
            try {
                functionInterfaceMethod = (Method)  constantPool.getMethodAt(index);
                break;
            } catch (Exception ex){
                continue;
            }
        }
        Class returnType = functionInterfaceMethod.getReturnType();
        return returnType;

    }

    /**
     * Repository to keep the return type of any Lambda expression.
     */
    public static Repository<AbstractThrowable, Class> lambdaReturnTypes = new Repository<>(Functions::getReturnType);

    /**
     * Factory to create Functions instance with given exception handler and default value factory.
     * @param exeptionConsumer  Exception Handler to allow differentiated processing of different kinds of Excpeitons.
     * @param defaultValueFactory   Default return value factory based on the returned type of the Lambda Expression.
     * @param <R> Type of the return value.
     * @return  Higher-order Function executioner with customised behaviours.
     */
    public static <R> Functions<R> buildFunctions(ConsumerThrowable<Exception> exeptionConsumer, FunctionThrowable<AbstractThrowable, Object> defaultValueFactory){
        Objects.requireNonNull(exeptionConsumer);
        Objects.requireNonNull(defaultValueFactory);
        ExceptionHandler <R> handler = (Exception ex, AbstractThrowable supplier) -> {
            try{
                exeptionConsumer.accept(ex);
                return (R) defaultValueFactory.apply(supplier);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        };
        return new Functions(handler);
    };

    // Static Functions instance to hidden any Exceptions by returning default values matching the given Lambda Expression
    public static final Functions ReturnsDefaultValue = new Functions((ex, supplier) ->
            Defaults.defaultOfType(lambdaReturnTypes.get((AbstractThrowable) supplier, null)));

    // Static Functions instance to simply throw RuntimeException whenever an Exception is caught.
    public static final Functions ThrowsRuntimeException = new Functions();

    // Default Functions executioer that could be changed dynamically. For instance, change it to ThrowsRuntimeException
    // when debugging, while ReturnsDefaultValue when releasing.
    public static Functions Default = ReturnsDefaultValue;

    /**
     * For each of the inputs, apply function parallelly to try to get output within timeout specified by timeoutMillis
     * @param function      Function to get result of type <code>R</code> with input of type <code>T</code>
     * @param inputs        Multiple input value of type <code>T</code> to be applied to <code>function</code>
     * @param timeoutMills  Timeout value in Milliseconds
     * @param <T>           Type of the input values <code>inputs</code>
     * @param <R>           Type of the result returned by applying input to <code>function</code>
     * @return              A list of converted results from <code>inputs</code> by <code>function</code> or null when Exception caught.
     */
    public static <T, R> List<R> applyParallel(FunctionThrowable<T, R> function, List<T> inputs, long timeoutMills){
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
        } finally{
            EXEC.shutdown();
        }
    }

    public static <T> void runParallel(ConsumerThrowable<T> consumerThrowable, Stream<T> paramStream, long timeoutMills){
        List<Callable<Void>> callables = new ArrayList<>();
        paramStream.forEach(param -> {
            callables.add(() -> {consumerThrowable.accept(param); return null;});
        });

        ExecutorService EXEC = Executors.newCachedThreadPool();
        try {
            EXEC.invokeAll(callables, timeoutMills, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        } finally{
            EXEC.shutdown();
        }
    }

    /**
     * Run the tasks parallely within the time of <code>timeoutMills</code>ms, disregard any Exceptions.
     * @param tasks         The tasks to be executed parallely within the timeout
     * @param timeoutMills  Timeout value in Milliseconds
     */
    public static void runParallel(List<RunnableThrowable> tasks, long timeoutMills){
        List<Callable<Void>> callables = new ArrayList<>();
        tasks.stream().forEach(runnable -> {
            callables.add(() -> {runnable.run(); return null;});
        });

        ExecutorService EXEC = Executors.newCachedThreadPool();
        try {
            EXEC.invokeAll(callables, timeoutMills, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        } finally{
            EXEC.shutdown();
        }
    }


    //Exception Handler that shall throw either RuntimeException or return default value of Type R when an exception is caught.
    private final ExceptionHandler<R> handler;

    /**
     * Constructor without ExceptionHandler provided, would always throw RuntimeException accordingly.
     */
    private Functions(){
        this.handler = null;
    }

    /**
     * Constructor with a Non-nullable ExceptionHandler to define behaviours when execute an AbstractThrowable.
     * Notice: thanks to the Java Type Erasing, the Functions instance created can be used with Lambda expressions of
     * different return types.
     * @param handler   ExceptionHandler instance that could be defined as Lambda to either throw RuntimeException or
     *                  return default value of type R.
     */
    public Functions(ExceptionHandler<R> handler){
        Objects.requireNonNull(handler);
        this.handler = handler;
    }

    /**
     * Execute the given RunnableThrowable with ExceptionHandler to tackle the Exception thrown during the execution.
     * When <code>handler</code> is not null, it would handle the thrown Exception with the predefined logics; otherwise
     * RuntimeException would be thrown.
     * @param runnableThrowable Service logic receiving and returning no arguments.
     */
    public void run(RunnableThrowable runnableThrowable){
        try {
            runnableThrowable.run();
        } catch (Exception ex){
            if(handler != null)
                handler.apply(ex, runnableThrowable);
            else
                throw new RuntimeException(ex);
        }
    }

    /**
     * Execute the given SupplierThrowable with ExceptionHandler to tackle the Exception thrown during the execution.
     * When <code>handler</code> is not null, it would handle the thrown Exception with the predefined logics to return
     * the default value of given type; otherwise RuntimeException would be thrown.
     * @param supplierThrowable Service logic receiving no arguments, but return something of type T.
     * @param <T>   Type of the returned value when everything goes smoothly.
     * @return  The value returned by the service logic of <code>supplierThrowable</code>, or the default value returned
     *      by the handler by parsing return type of the given <code>supplierThrowable</code>.
     */
    public <T> T apply(SupplierThrowable<T> supplierThrowable){
        try {
            return supplierThrowable.get();
        } catch (Exception ex){
            if(handler != null)
                return (T) handler.apply(ex, supplierThrowable);
            else
                throw new RuntimeException(ex);
        }
    }

    /**
     * To be called by other apply() methods with 1 to 7 arguments.
     * @param originalLambda    The original service logic to be executed, the return type is not erased by Java compiler
     * @param supplierThrowable Synthetic Lambda created with given parameters and the <code>originalLambda</code>.
     * @param <T>   Type of the returned value when everything goes smoothly.
     * @return  The value returned by the service logic of <code>originalLambda</code>, or the default value returned
     * by the hendler by parsing te given <code>originalLambdavvv</code>.
     */
    private <T> T apply(AbstractThrowable originalLambda, SupplierThrowable<T> supplierThrowable){
        try {
            return supplierThrowable.get();
        } catch (Exception ex){
            if(handler != null)
                return (T) handler.apply(ex, originalLambda);
            else
                throw new RuntimeException(ex);
        }
    }

    /**
     * Run a method accepting one argument with the actual one argument.
     * @param consumer  Lambda expression accepting one argument and returns nothing.
     * @param t     Argument to be consumed by the Lambda Expression.
     * @param <T>   Type of the argment <code>t</code>
     */
    public <T> void run(
            ConsumerThrowable<T> consumer,
            T t){
        run(() -> consumer.accept(t));
    }

    /**
     * Run a method accepting two argument with the actual two arguments.
     * @param consumer  Lambda expression accepting two argument and returns nothing.
     * @param t     First argument to be consumed by the Lambda Expression.
     * @param u     Second argument to be consumed by the Lambda Expression.
     * @param <T>   Type of the first argment <code>t</code>
     * @param <U>   Type of the second argument <code>u</code>
     */
    public <T,U> void run(
            BiConsumerThrowable<T,U> consumer,
            T t, U u){
        run(() -> consumer.accept(t,u));
    }

    /**
     * Run a method accepting 3 argument with the actual 3 arguments.
     * @param consumer  Lambda expression accepting three argument and returns nothing.
     * @param t     First argument to be consumed by the Lambda Expression.
     * @param u     Second argument to be consumed by the Lambda Expression.
     * @param v     The third argument to be consumed by the Lambda Expression.
     * @param <T>   Type of the first argment <code>t</code>
     * @param <U>   Type of the second argument <code>u</code>
     * @param <V>   Type of the third argument <code>v</code>
     */
    public <T,U,V> void run(
            TriConsumerThrowable<T,U,V> consumer,
            T t, U u, V v){
        run(() -> consumer.accept(t,u,v));
    }

    /**
     * Run a method accepting 4 argument with the actual 4 arguments.
     * @param consumer  Lambda expression accepting seven argument and returns nothing.
     * @param t     The first argument to be consumed by the Lambda Expression.
     * @param u     The second argument to be consumed by the Lambda Expression.
     * @param v     The third argument to be consumed by the Lambda Expression.
     * @param w     The fourth argument to be consumed by the Lambda Expression.
     * @param <T>   Type of the first argment <code>t</code>
     * @param <U>   Type of the second argument <code>u</code>
     * @param <V>   Type of the third argument <code>v</code>
     * @param <W>   Type of the fourth argument <code>w</code>
     */
    public <T,U,V,W> void run(
            QuadConsumerThrowable<T,U,V,W> consumer,
            T t, U u, V v, W w){
        run(() -> consumer.accept(t,u,v,w));
    }


    /**
     * Run a method accepting 5 argument with the actual 5 arguments.
     * @param consumer  Lambda expression accepting seven argument and returns nothing.
     * @param t     The first argument to be consumed by the Lambda Expression.
     * @param u     The second argument to be consumed by the Lambda Expression.
     * @param v     The third argument to be consumed by the Lambda Expression.
     * @param w     The fourth argument to be consumed by the Lambda Expression.
     * @param x     The fifth argument to be consumed by the Lambda Expression.
     * @param <T>   Type of the first argment <code>t</code>
     * @param <U>   Type of the second argument <code>u</code>
     * @param <V>   Type of the third argument <code>v</code>
     * @param <W>   Type of the fourth argument <code>w</code>
     * @param <X>   Type of the fifth argument <code>x</code>
     */
    public <T,U,V,W,X> void run(
            PentaConsumerThrowable<T,U,V,W,X> consumer,
            T t, U u, V v, W w, X x){
        run(() -> consumer.accept(t,u,v,w,x));
    }

    /**
     * Run a method accepting 6 argument with the actual 6 arguments.
     * @param consumer  Lambda expression accepting seven argument and returns nothing.
     * @param t     The first argument to be consumed by the Lambda Expression.
     * @param u     The second argument to be consumed by the Lambda Expression.
     * @param v     The third argument to be consumed by the Lambda Expression.
     * @param w     The fourth argument to be consumed by the Lambda Expression.
     * @param x     The fifth argument to be consumed by the Lambda Expression.
     * @param y     The sixth argument to be consumed by the Lambda Expression.
     * @param <T>   Type of the first argment <code>t</code>
     * @param <U>   Type of the second argument <code>u</code>
     * @param <V>   Type of the third argument <code>v</code>
     * @param <W>   Type of the fourth argument <code>w</code>
     * @param <X>   Type of the fifth argument <code>x</code>
     * @param <Y>   Type of the sixth argument <code>y</code>
     */
    public <T,U,V,W,X,Y> void run(
            HexaConsumerThrowable<T,U,V,W,X,Y> consumer,
            T t, U u, V v, W w, X x, Y y){
        run(() -> consumer.accept(t,u,v,w,x,y));
    }

    /**
     * Run a method accepting 7 argument with the actual 7 arguments.
     * @param consumer  Lambda expression accepting seven argument and returns nothing.
     * @param t     The first argument to be consumed by the Lambda Expression.
     * @param u     The second argument to be consumed by the Lambda Expression.
     * @param v     The third argument to be consumed by the Lambda Expression.
     * @param w     The fourth argument to be consumed by the Lambda Expression.
     * @param x     The fifth argument to be consumed by the Lambda Expression.
     * @param y     The sixth argument to be consumed by the Lambda Expression.
     * @param z     The seventh argument to be consumed by the Lambda Expression.
     * @param <T>   Type of the first argment <code>t</code>
     * @param <U>   Type of the second argument <code>u</code>
     * @param <V>   Type of the third argument <code>v</code>
     * @param <W>   Type of the fourth argument <code>w</code>
     * @param <X>   Type of the fifth argument <code>x</code>
     * @param <Y>   Type of the sixth argument <code>y</code>
     * @param <Z>   Type of the seventh argument <code>z</code>
     */
    public <T,U,V,W,X,Y,Z> void run(
            HeptaConsumerThrowable<T,U,V,W,X,Y,Z> consumer,
            T t, U u, V v, W w, X x, Y y, Z z){
        run(() -> consumer.accept(t,u,v,w,x,y,z));
    }

    /**
     * Predicate to evaluate one argument <code>t</code> of type <code>T</code>
     * @param predicate Method to accept one argument of type <code>T</code>
     * @param t         First argument of type <code>T</code>
     * @param <T>       Type of the given argument <code>t</code>
     * @return          <code>True</code> if predicate is success, otherwise <code>False</code>
     */
    public <T> boolean test(
            PredicateThrowable<T> predicate, T t){
        return apply(() -> predicate.test(t));
    }

    /**
     * Predicate to evaluate two arguments
     * @param predicate Method to accept two arguments of type <code>T</code> and <code>U</code>
     * @param t         First argument of type <code>T</code>
     * @param u         Second argument of type <code>U</code>
     * @param <T>       Type of the first argument <code>t</code>
     * @param <U>       Type of the second argument <code>u</code>
     * @return          <code>True</code> if predicate is success, otherwise <code>False</code>
     */
    public <T,U> boolean test(
            BiPredicateThrowable<T,U> predicate,
            T t, U u){
        return apply(() -> predicate.test(t, u));
    }

    /**
     * Execute a method accepting 1 argument with the actual 1 arguments, and return its result.
     * @param function  Lambda expression accepting seven argument and returns one result.
     * @param t     The first argument to be consumed by the Lambda Expression.
     * @param <T>   Type of the first argment <code>t</code>
     * @param <R>   Type of the return resul.
     * @return      The value returned by the service logic of <code>rop</code>, or the default value returned
     *      by the handler by parsing return type of the given <code>rop</code>.
     */
    public <T,R> R apply(
            FunctionThrowable<T,R> function,
            T t){
        return apply((AbstractThrowable)function, () -> function.apply(t));
    }

    /**
     * Execute a method accepting 2 argument with the actual 2 arguments, and return its result.
     * @param function  Lambda expression accepting seven argument and returns one result.
     * @param t     The first argument to be consumed by the Lambda Expression.
     * @param u     The second argument to be consumed by the Lambda Expression.
     * @param <T>   Type of the first argment <code>t</code>
     * @param <U>   Type of the second argument <code>u</code>
     * @param <R>   Type of the return resul.
     * @return      The value returned by the service logic of <code>rop</code>, or the default value returned
     *      by the handler by parsing return type of the given <code>rop</code>.
     */
    public <T,U,R> R apply(
            BiFunctionThrowable<T,U,R> function,
            T t, U u){
        return apply(function, () -> function.apply(t,u));
    }

    /**
     * Execute a method accepting 3 argument with the actual 3 arguments, and return its result.
     * @param function  Lambda expression accepting seven argument and returns one result.
     * @param t     The first argument to be consumed by the Lambda Expression.
     * @param u     The second argument to be consumed by the Lambda Expression.
     * @param v     The third argument to be consumed by the Lambda Expression.
     * @param <T>   Type of the first argment <code>t</code>
     * @param <U>   Type of the second argument <code>u</code>
     * @param <V>   Type of the third argument <code>v</code>
     * @param <R>   Type of the return resul.
     * @return      The value returned by the service logic of <code>rop</code>, or the default value returned
     *      by the handler by parsing return type of the given <code>rop</code>.
     */
    public <T,U,V,R> R apply(
            TriFunctionThrowable<T,U,V,R> function,
            T t, U u, V v){
        return apply(function, () -> function.apply(t,u,v));
    }

    /**
     * Execute a method accepting 4 argument with the actual 4 arguments, and return its result.
     * @param function  Lambda expression accepting seven argument and returns one result.
     * @param t     The first argument to be consumed by the Lambda Expression.
     * @param u     The second argument to be consumed by the Lambda Expression.
     * @param v     The third argument to be consumed by the Lambda Expression.
     * @param w     The fourth argument to be consumed by the Lambda Expression.
     * @param <T>   Type of the first argment <code>t</code>
     * @param <U>   Type of the second argument <code>u</code>
     * @param <V>   Type of the third argument <code>v</code>
     * @param <W>   Type of the fourth argument <code>w</code>
     * @param <R>   Type of the return resul.
     * @return      The value returned by the service logic of <code>rop</code>, or the default value returned
     *      by the handler by parsing return type of the given <code>rop</code>.
     */
    public <T,U,V,W,R> R apply(
            QuadFunctionThrowable<T,U,V,W,R> function,
            T t, U u, V v, W w){
        return apply(function, () -> function.apply(t,u,v,w));
    }

    /**
     * Execute a method accepting 5 argument with the actual 5 arguments, and return its result.
     * @param function  Lambda expression accepting seven argument and returns one result.
     * @param t     The first argument to be consumed by the Lambda Expression.
     * @param u     The second argument to be consumed by the Lambda Expression.
     * @param v     The third argument to be consumed by the Lambda Expression.
     * @param w     The fourth argument to be consumed by the Lambda Expression.
     * @param x     The fifth argument to be consumed by the Lambda Expression.
     * @param <T>   Type of the first argment <code>t</code>
     * @param <U>   Type of the second argument <code>u</code>
     * @param <V>   Type of the third argument <code>v</code>
     * @param <W>   Type of the fourth argument <code>w</code>
     * @param <X>   Type of the fifth argument <code>x</code>
     * @param <R>   Type of the return resul.
     * @return      The value returned by the service logic of <code>rop</code>, or the default value returned
     *      by the handler by parsing return type of the given <code>rop</code>.
     */
    public <T,U,V,W,X,R> R apply(
            PentaFunctionThrowable<T,U,V,W,X,R> function,
            T t, U u, V v, W w, X x){
        return apply(function, () -> function.apply(t,u,v,w,x));
    }

    /**
     * Execute a method accepting 6 argument with the actual 6 arguments, and return its result.
     * @param function  Lambda expression accepting seven argument and returns one result.
     * @param t     The first argument to be consumed by the Lambda Expression.
     * @param u     The second argument to be consumed by the Lambda Expression.
     * @param v     The third argument to be consumed by the Lambda Expression.
     * @param w     The fourth argument to be consumed by the Lambda Expression.
     * @param x     The fifth argument to be consumed by the Lambda Expression.
     * @param y     The sixth argument to be consumed by the Lambda Expression.
     * @param <T>   Type of the first argment <code>t</code>
     * @param <U>   Type of the second argument <code>u</code>
     * @param <V>   Type of the third argument <code>v</code>
     * @param <W>   Type of the fourth argument <code>w</code>
     * @param <X>   Type of the fifth argument <code>x</code>
     * @param <Y>   Type of the sixth argument <code>y</code>
     * @param <R>   Type of the return resul.
     * @return      The value returned by the service logic of <code>rop</code>, or the default value returned
     *      by the handler by parsing return type of the given <code>rop</code>.
     */
    public <T,U,V,W,X,Y,R> R apply(
            HexaFunctionThrowable<T,U,V,W,X,Y,R> function,
            T t, U u, V v, W w, X x, Y y){
        return apply(function, () -> function.apply(t,u,v,w,x,y));
    }

    /**
     * Execute a method accepting 7 argument with the actual 7 arguments, and return its result.
     * @param function  Lambda expression accepting seven argument and returns one result.
     * @param t     The first argument to be consumed by the Lambda Expression.
     * @param u     The second argument to be consumed by the Lambda Expression.
     * @param v     The third argument to be consumed by the Lambda Expression.
     * @param w     The fourth argument to be consumed by the Lambda Expression.
     * @param x     The fifth argument to be consumed by the Lambda Expression.
     * @param y     The sixth argument to be consumed by the Lambda Expression.
     * @param z     The seventh argument to be consumed by the Lambda Expression.
     * @param <T>   Type of the first argment <code>t</code>
     * @param <U>   Type of the second argument <code>u</code>
     * @param <V>   Type of the third argument <code>v</code>
     * @param <W>   Type of the fourth argument <code>w</code>
     * @param <X>   Type of the fifth argument <code>x</code>
     * @param <Y>   Type of the sixth argument <code>y</code>
     * @param <Z>   Type of the seventh argument <code>z</code>
     * @param <R>   Type of the return resul.
     * @return      The value returned by the service logic of <code>rop</code>, or the default value returned
     *      by the handler by parsing return type of the given <code>rop</code>.
     */
    public <T,U,V,W,X,Y,Z,R> R apply(
            HeptaFunctionThrowable<T,U,V,W,X,Y,Z,R> function,
            T t, U u, V v, W w, X x, Y y, Z z){
        return apply(function, () -> function.apply(t,u,v,w,x,y,z));
    }

//    //region Factories to convert various Consumer methods, plus expected arguments, to RunnableThrowable
//    // that accept no input and returns nothing.
//    public static<T> RunnableThrowable toRunnable(
//            T t,
//            RunnableThrowable.ConsumerThrowable<T> consumer) throws Exception{
//        return () -> consumer.accept(t);
//    }
//
//    public static<T,U> RunnableThrowable toRunnable(
//            T t, U u,
//            RunnableThrowable.BiConsumerThrowable<T,U> consumer) throws Exception{
//        return () -> consumer.accept(t, u);
//    }
//
//    public static<T,U,V> RunnableThrowable toRunnable(
//            T t, U u, V v,
//            RunnableThrowable.TriConsumerThrowable<T,U,V> consumer) throws Exception{
//        return () -> consumer.accept(t, u, v);
//    }
//
//    public static<T,U,V,W> RunnableThrowable toRunnable(
//            T t, U u, V v, W w,
//            RunnableThrowable.QuadConsumerThrowable<T,U,V,W> consumer) throws Exception{
//        return () -> consumer.accept(t, u, v, w);
//    }
//
//    public static<T,U,V,W,X> RunnableThrowable toRunnable(
//            T t, U u, V v, W w, X x,
//            RunnableThrowable.PentaConsumerThrowable<T,U,V,W,X> consumer) throws Exception{
//        return () -> consumer.accept(t, u, v, w, x);
//    }
//
//    public static<T,U,V,W,X,Y> RunnableThrowable toRunnable(
//            T t, U u, V v, W w, X x, Y y,
//            RunnableThrowable.HexaConsumerThrowable<T,U,V,W,X,Y> consumer) throws Exception{
//        return () -> consumer.accept(t, u, v, w, x, y);
//    }
//
//    public static<T,U,V,W,X,Y,Z> RunnableThrowable toRunnable(
//            T t, U u, V v, W w, X x, Y y, Z z,
//            RunnableThrowable.HeptaConsumerThrowable<T,U,V,W,X,Y,Z> consumer) throws Exception{
//        return () -> consumer.accept(t, u, v, w, x, y, z);
//    }
//    //endregion
//
//    //region Factories to convert various functions, plus expected 1-7 arguments, to RunnableThrowable
//    // that accept no input and returns nothing.
//    public static <T> SupplierThrowable<Boolean> asSupplier(
//            T t,
//            SupplierThrowable.PredicateThrowable<T> predicate)
//            throws Exception {
//        return () -> predicate.test(t);
//    }
//
//    public static <T,R> SupplierThrowable<R> asSupplier(
//            T t,
//            SupplierThrowable.FunctionThrowable<T,R> rop)
//            throws Exception {
//         return () -> rop.apply(t);
//    }
//
//    public static <T,U,R> SupplierThrowable<R> asSupplier(
//            T t, U u,
//            SupplierThrowable.BiFunctionThrowable<T,U,R> rop)
//            throws Exception {
//        return () -> rop.apply(t, u);
//    }
//
//    public static <T,U,V,R> SupplierThrowable<R> asSupplier(
//            T t, U u, V v,
//            SupplierThrowable.TriFunctionThrowable<T,U,V,R> rop)
//            throws Exception {
//        return () -> rop.apply(t, u, v);
//    }
//
//    public static <T,U,V,W,R> SupplierThrowable<R> asSupplier(
//            T t, U u, V v, W w,
//            SupplierThrowable.QuadFunctionThrowable<T,U,V,W,R> rop)
//            throws Exception {
//        return () -> rop.apply(t, u, v, w);
//    }
//
//    public static <T,U,V,W,X,R> SupplierThrowable<R> asSupplier(
//            T t, U u, V v, W w, X x,
//            SupplierThrowable.PentaFunctionThrowable<T,U,V,W,X,R> rop)
//            throws Exception {
//        return () -> rop.apply(t, u, v, w, x);
//    }
//
//    public static <T,U,V,W,X,Y,R> SupplierThrowable<R> asSupplier(
//            T t, U u, V v, W w, X x, Y y,
//            SupplierThrowable.HexaFunctionThrowable<T,U,V,W,X,Y,R> rop)
//            throws Exception {
//        return () -> rop.apply(t, u, v, w, x, y);
//    }
//
//    public static <T,U,V,W,X,Y,Z,R> SupplierThrowable<R> asSupplier(
//            T t, U u, V v, W w, X x, Y y, Z z,
//            SupplierThrowable.HeptaFunctionThrowable<T,U,V,W,X,Y,Z,R> rop)
//            throws Exception {
//        return () -> rop.apply(t, u, v, w, x, y, z);
//    }
//    //endregion
}
