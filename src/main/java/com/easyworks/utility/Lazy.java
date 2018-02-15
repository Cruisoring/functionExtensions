package com.easyworks.utility;

import com.easyworks.NoThrows;
import com.easyworks.RuntimeThrows;
import com.easyworks.function.RunnableThrowable;
import com.easyworks.function.SupplierThrowable;

import java.util.Objects;

/**
 * This generic class encapsulate factory to enable lazy evaluation.
 * It also support AutoCloseable to release resources, including other dependent Lazy instances, automatically when close() is called.
 * @param <T> Type of the instance to be initialized.
 */
public class Lazy<T> implements AutoCloseable {
    private boolean isCreated = false;
    private T value;
    SupplierThrowable<T> supplier;
    RunnableThrowable closing;

    /**
     * Construct a Lazy object with factory, instead of value set.
     * @param supplier The factory to create value instance when getValue() is called.
     */
    public Lazy(SupplierThrowable<T> supplier){
        Objects.requireNonNull(supplier);
        this.supplier = supplier;
        this.closing = this::reset;
    }

    /**
     * Construct a Lazy object with factory set, as well as customer closing logic.
     * @param supplier  The factory to create value instance when getValue() is called.
     * @param closing   The customer closing mechanism to release its own resources.
     */
    public Lazy(SupplierThrowable<T> supplier, RunnableThrowable closing){
        this(supplier);
        Objects.requireNonNull(closing);
        this.closing = closing;
    }

    /**
     * Create a dependent Lazy instance with corresponding factory included.
     * @param dependentSupplier The factory to create another Lazy<U> instance.
     * @param <U>               Type of the object that depending on this.value of type T.
     * @return                  Another Lazy instance to delay initialization of type U.
     */
    public <U> Lazy<U> attach(SupplierThrowable<U> dependentSupplier){
        Lazy<U> dependent = new Lazy(dependentSupplier);
        this.closing = this.closing.tryStartWith(dependent::close);
        return dependent;
    }

    /**
     * Create a dependent Lazy instance with corresponding factory included.
     * @param dependentSupplier The factory to create another Lazy<U> instance.
     * @param closing           The customer closing mechanism to release resources of the newly created Lazy U instance.
     * @param <U>               Type of the object that depending on this.value of type T.
     * @return                  Another Lazy instance to delay initialization of type U.
     */
    public <U> Lazy<U> attach(SupplierThrowable<U> dependentSupplier, RunnableThrowable closing){
        Lazy<U> dependent = new Lazy(dependentSupplier, closing);
        this.closing = this.closing.tryStartWith(dependent::close);
        return dependent;
    }

    /**
     * Indicate if the value has been created with the factory method.
     * @return {@code True} if value is created, otherwise {@code False}.
     */
    public boolean isValueCreated(){
        return isCreated;
    }

    /**
     * Create value with the given factory when it is not created yet.
     * If isCreated shows it is already created, then the existing instance would be returned.
     * Notice: RuntimeException would be thrown if calling supplier get any error.
     * @return
     */
    public T getValue(){
        if(!isCreated){
            value = RuntimeThrows.get(supplier);
            isCreated = true;
        }
        return value;
    }

    /**
     * When the value has not been created successfully, try to create the value with the factory then return it when
     * there is no Exception, or returns the defaultOfType.
     * Notice: when Exception thrown, isCreated would not be set to True.
     * @param defaultValue  Default value of type T when calling the supplier method get any Exception.
     * @return  Either the created and cached value when factory is called successfully, or the default value if there
     * is any Exception thrown.
     */
    public T getValue(T defaultValue) {
        if(!isCreated){
            try {
                value = supplier.get();
                isCreated = true;
            }catch (Exception ex){
                return defaultValue;
            }
        }
        return value;
    }

    /**
     * When value created, reset it and release any resource bounded if the instance is AutoCloseable.
     */
    public void reset(){
        if(isCreated){
            isCreated = false;
            if (this.value instanceof AutoCloseable){
                NoThrows.run(((AutoCloseable) this.value)::close);
            }
            this.value = Defaults.defaultOfInstance(this.value);
        }
    }

    /**
     * Release any resources and refrain any Exceptions.
     * Notice: the <code>closing</code> could be updated to close any depending Lazy instances.
     * @throws Exception    Actually no Exception would be thrown.
     */
    @Override
    public void close() throws Exception {
        NoThrows.run(closing);
    }
}
