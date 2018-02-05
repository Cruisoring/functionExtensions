package com.easyworks;

import com.easyworks.function.RunnableThrows;
import com.easyworks.function.SupplierThrows;
import com.easyworks.utilities.Defaults;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * This generic class encapsulate factory to enable lazy evaluation and keeps the duration to call the factory method.
 * It also support AutoCloseable to release resources, including other dependent Lazy instances, automatically when close() is called.
 * @param <T> Type of the instance to be initialized.
 */
public class LazyTimed<T> extends Lazy<T> {
    Duration timeToSupplier;
    /**
     * Construct a Lazy object with factory, instead of value set.
     * @param supplier The factory to create value instance when getValue() is called.
     */
    public LazyTimed(SupplierThrows<T> supplier){
        super(supplier);
    }

    /**
     * Construct a Lazy object with factory set, as well as customer closing logic.
     * @param supplier  The factory to create value instance when getValue() is called.
     * @param closing   The customer closing mechanism to release its own resources.
     */
    public LazyTimed(SupplierThrows<T> supplier, RunnableThrows closing){
        super(supplier, closing);
    }

    /**
     * Create a dependent Lazy instance with corresponding factory included.
     * @param dependentSupplier The factory to create another Lazy<U> instance.
     * @param <U>               Type of the object that depending on this.value of type T.
     * @return                  Another Lazy instance to delay initialization of type U.
     */
    public <U> LazyTimed<U> attach(SupplierThrows<U> dependentSupplier){
        Objects.nonNull(dependentSupplier);
        LazyTimed<U> child = new LazyTimed(dependentSupplier);
        this.closing = this.closing.tryStartWith(child::close);
        return child;
    }

    /**
     * Create a dependent Lazy instance with corresponding factory included.
     * @param dependentSupplier The factory to create another Lazy<U> instance.
     * @param closing           The customer closing mechanism to release resources of the newly created Lazy U instance.
     * @param <U>               Type of the object that depending on this.value of type T.
     * @return                  Another Lazy instance to delay initialization of type U.
     */
    public <U> LazyTimed<U> attach(SupplierThrows<U> dependentSupplier, RunnableThrows closing){
        Objects.nonNull(dependentSupplier);
        LazyTimed<U> child = new LazyTimed(dependentSupplier, closing);
        this.closing = this.closing.tryStartWith(child::close);
        return child;
    }

    /**
     * Create value with the given factory when it is not created yet.
     * If isCreated shows it is already created, then the existing instance would be returned.
     * Notice: RuntimeException would be thrown if calling supplier get any error.
     * @return
     */
    @Override
    public T getValue(){
        if(!isValueCreated()){
            long start = Instant.now().toEpochMilli();
            T v = getValue();
            long milliseconds = Instant.now().toEpochMilli() - start;
            timeToSupplier = Duration.ofMillis(milliseconds);
            return v;
        }
        else{
            return getValue();
        }
    }

    /**
     * When the value has not been created successfully, try to create the value with the factory then return it when
     * there is no Exception, or returns the defaultValue.
     * Notice: when Exception thrown, isCreated would not be set to True.
     * @param defaultValue  Default value of type T when calling the supplier method get any Exception.
     * @return  Either the created and cached value when factory is called successfully, or the default value if there
     * is any Exception thrown.
     */
    @Override
    public T getValue(T defaultValue) {
        if(!isValueCreated()){
            long start = Instant.now().toEpochMilli();
            try {
                return getValue(defaultValue);
            }catch (Exception ex){
                return defaultValue;
            }finally {
                timeToSupplier = Duration.ofMillis(Instant.now().toEpochMilli()-start);
            }
        }
        return getValue();
    }

    /**
     * When value created, reset it and release any resource bounded if the instance is AutoCloseable.
     */
    @Override
    public void reset(){
        if(isValueCreated()){
            super.reset();
            timeToSupplier = Defaults.defaultValue(timeToSupplier);
        }
    }
}
