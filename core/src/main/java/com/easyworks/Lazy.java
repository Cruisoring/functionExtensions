package com.easyworks;

import com.easyworks.function.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This generic class encapsulate factory to enable lazy evaluation of the concerned value.
 * It also support AutoCloseable to release resources, including other dependent Lazy instances, when close() is called.
 * @param <T> Type of the instance to be initialized.
 */
public class Lazy<T> implements AutoCloseable {
    final SupplierThrowable<T> supplier;
    final BiConsumerThrowable<T,T> actionOnChanges;
    private boolean isInitialized = false;
    private T value = null;
    private boolean isClosed = false;
    protected List<AutoCloseable> dependencies;

    /**
     * Construct a Lazy object with value factory, instead of value itself.
     * @param supplier The factory to create value instance when getValue() is called.
     */
    public Lazy(SupplierThrowable<T> supplier){
        Objects.requireNonNull(supplier);
        this.supplier = supplier;
        this.actionOnChanges = null;
    }

    /**
     * Construct a Lazy object with value factory, as well as customer closing logic.
     * @param supplier              The factory to create value instance when getValue() is called.
     * @param actionOnChanges    Extra steps to run before reset() being called.
     */
    public Lazy(SupplierThrowable<T> supplier, BiConsumerThrowable<T,T> actionOnChanges){
        Objects.requireNonNull(supplier);
        this.supplier = supplier;
        this.actionOnChanges = actionOnChanges;
//        this.closing = actionOnChanges == null ? this::reset : () -> this.resetAfterAction(actionOnChanges);
    }

    /**
     * Create a dependent Lazy instance whose value can be generated only with this.value.
     * @param function  Function to get the dependent value (Type U) from this value (Type T).
     * @param <U>       Type of the object that depending on this.value of type T.
     * @return          Another Lazy instance to delay initialization of type U.
     */
    public <U> Lazy<U> create(FunctionThrowable<T, U> function){
        Lazy<U> dependency = new Lazy(() -> function.apply(getValue()));
        if(dependencies == null){
            dependencies = new ArrayList<>();
        }
        dependencies.add(dependency);
        return dependency;
    }

    /**
     * Create a dependent Lazy instance whose value can be generated only with this.value.
     * @param function  Function to get the dependent value (of Type U) from this value (of Type T).
     * @param actionOnChanges   The customer closing mechanism to release its own resources (of Type U).
     * @param <U>       Type of the object that depending on this.value of type T.
     * @return          Another Lazy instance to delay initialization of type U.
     */
    public <U> Lazy<U> create(FunctionThrowable<T, U> function, BiConsumerThrowable<U,U> actionOnChanges){
        Lazy<U> dependency = new Lazy(() -> function.apply(getValue()), actionOnChanges);
        if(dependencies == null){
            dependencies = new ArrayList<>();
        }
        dependencies.add(dependency);
        return dependency;
    }

    /**
     * Indicate if the value has been created with the factory method.
     * @return {@code True} if value is created, otherwise {@code False}.
     */
    public boolean isValueInitialized(){
        return isInitialized;
    }

    /**
     * Create value with the given factory when it is not created yet.
     * If isInitialized shows it is already created, then the existing instance would be returned.
     * Notice: RuntimeException would be thrown if calling supplier get any error.
     * @return
     */
    public T getValue(){
        if(!isInitialized){
            T oldValue = value;
            value = (T) Functions.Default.apply(supplier);
            isInitialized = true;
            isClosed = false;
            Functions.Default.run(actionOnChanges, oldValue, value);
        }
        return value;
    }

//    /**
//     * When the value has not been created successfully, try to create the value with the factory then return it when
//     * there is no Exception, or returns the defaultOfType.
//     * Notice: when Exception thrown, isInitialized would not be set to True.
//     * @param defaultValue  Default value of type T when calling the supplier method get any Exception.
//     * @return  Either the created and cached value when factory is called successfully, or the default value if there
//     * is any Exception thrown.
//     */
//    public T getValue(T defaultValue) {
//        if(!isInitialized){
//            try {
//                value = supplier.get();
//                isInitialized = true;
//            }catch (Exception ex){
//                return defaultValue;
//            }
//        }
//        return value;
//    }

    /**
     * When value created, reset it and release any resource bounded if the instance is AutoCloseable.
     */
    public void reset(){
        if(!isClosed){
            isClosed = true;
            if(dependencies != null && dependencies.size() > 0){
                for (int i = dependencies.size()-1; i >= 0; i--) {
                    AutoCloseable child = dependencies.get(i);
                    if(child instanceof Lazy && ((Lazy)child).isClosed)
                        continue;

                    Functions.Default.run(child::close);
                }
                dependencies.clear();
            }
            if(isInitialized) {
                isInitialized = false;
                if (value != null && value instanceof AutoCloseable) {
                    Functions.Default.run(((AutoCloseable) value)::close);
                }
                T currentValue = value;
                value = null;
                if(actionOnChanges != null){
                    Functions.Default.run(actionOnChanges, currentValue, null);
                }
            }
        }
    }

    /**
     * Release any resources and refrain any Exceptions.
     * Notice: the <code>closing</code> could be updated to close any depending Lazy instances.
     * @throws Exception    Actually no Exception would be thrown.
     */
    @Override
    public void close() throws Exception {
        Functions.Default.run(this::reset);
    }
}
