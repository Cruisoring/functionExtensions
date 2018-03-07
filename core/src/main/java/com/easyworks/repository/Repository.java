package com.easyworks.repository;

import com.easyworks.Functions;
import com.easyworks.Lazy;
import com.easyworks.function.ConsumerThrowable;
import com.easyworks.function.FunctionThrowable;
import com.easyworks.function.RunnableThrowable;
import com.easyworks.function.SupplierThrowable;

import java.util.*;

/**
 * Wrapping of Map instance and business logic to get value from key
 * @param <TKey>    the type of keys maintained by this map
 * @param <TValue>  the type of mapped values
 */
public class Repository<TKey, TValue> extends Lazy<Map<TKey, TValue>>
        implements FunctionThrowable<TKey, TValue> {
    //Default time to close parallelly all AutoCloseable keys/values contained by the map
    public static final long DEFAULT_RESET_TIMEOUT = 2000;

    //Function to map key of <code>TKey<code> type to value of <code>TValue<code> type
    final FunctionThrowable<TKey, TValue> valueFunctionThrowable;

    /**
     * Construct a repository with given map factory, extra closing logic and Function to map key to value
     * @param storageSupplier   Factory to get a map instance
     * @param closing           Extra steps to run before reset() being called.
     * @param valueFunction     Function to map key of <code>TKey<code> type to value of <code>TValue<code> type
     */
    public Repository(SupplierThrowable<Map<TKey, TValue>> storageSupplier,
                      ConsumerThrowable<Map<TKey, TValue>> closing,
                      FunctionThrowable<TKey, TValue> valueFunction){
        super(storageSupplier, closing);
        Objects.requireNonNull(valueFunction);
        this.valueFunctionThrowable = valueFunction;
    }

    /**
     * Construct a repository as a HashMap without extra closing action, with function to map key to value
     * @param valueFunction     Function to map key of <code>TKey<code> type to value of <code>TValue<code> type
     */
    public Repository(FunctionThrowable<TKey, TValue> valueFunction){
        this(HashMap::new, null, valueFunction);
    }

    /**
     * Try to get cached value of the corresponding key first, mapping and caching key to value only when it is not cached before
     * @param tKey      The key of type <code>TKey</code> to be mapped to value of type <code>TValue</code>
     * @return          mapped value of type <code>TValue</code> from the given tKey
     * @throws Exception
     */
    @Override
    public TValue apply(TKey tKey) throws Exception {
        TValue result;
        Map<TKey, TValue> storage = getValue();
        if(!storage.containsKey(tKey)){
            result = valueFunctionThrowable.apply(tKey);
            storage.put(tKey, result);
        } else {
            result = storage.get(tKey);
        }
        return result;
    }

    /**
     * Wrap the business logic of apply() to refrain throwing Exception by returning defaultValue
     * @param tKey          The key of type <code>TKey</code> to be mapped to value of type <code>TValue</code>
     * @param defaultValue  The default value to be returned when there is any Exception thrown by apply(TKey key)
     * @return          mapped value of type <code>TValue</code> from the given tKey
     */
    public TValue get(TKey tKey, TValue defaultValue){
        try {
            return apply(tKey);
        }catch (Exception ex){
            return defaultValue;
        }
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the specified
     * key.  More formally, returns <tt>true</tt> if and only if
     * this map contains a mapping for a key <tt>k</tt> such that
     * <tt>(key==null ? k==null : key.equals(k))</tt>.  (There can be
     * at most one such mapping.)
     *
     * @param tKey key whose presence in this map is to be tested
     * @return <tt>true</tt> if this map contains a mapping for the specified
     *         key
     * @throws ClassCastException if the key is of an inappropriate type for
     *         this map
     * (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this map
     *         does not permit null keys
     * (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */

    public boolean containsKey(TKey tKey){
        return getValue().containsKey(tKey);
    }

    /**
     * Extra closing logic to close all AutoCloseable Keys/Values within a limited time specified by DEFAULT_RESET_TIMEOUT
     */
    @Override
    public void reset() {
        if(isValueInitialized()){
            List<RunnableThrowable> keyValueToClose = new ArrayList<>();
            Map<TKey, TValue> map = getValue();
            map.entrySet().forEach(entry -> {
                TKey key = entry.getKey();
                if(key instanceof AutoCloseable){
                    AutoCloseable kClose = (AutoCloseable)key;
                    keyValueToClose.add(kClose::close);
                }
                TValue value = entry.getValue();
                if(value instanceof AutoCloseable){
                    AutoCloseable vClose = (AutoCloseable)value;
                    keyValueToClose.add(vClose::close);
                }
            });
            map.clear();
            if(keyValueToClose.size() > 0){
                Functions.runParallel(keyValueToClose, DEFAULT_RESET_TIMEOUT);
            }
        }
        super.reset();
    }
}
