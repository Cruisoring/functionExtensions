package com.easyworks.repository;

import com.easyworks.function.FunctionThrowable;
import com.easyworks.function.TriConsumerThrowable;
import com.easyworks.utility.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Wrapping of Map instance and business logic to get value from key
 * @param <TKey>    the type of keys maintained by this map
 * @param <TValue>  the type of mapped values
 */
public class Repository<TKey, TValue>
        implements FunctionThrowable<TKey, TValue> {
    public static boolean USE_DEFAULT_CHNAGES_LOG = false;
    //Default time to close parallelly all AutoCloseable keys/values contained by the map
    public static long DEFAULT_RESET_TIMEOUT = 2000;

    final Map<TKey, TValue> storage;

   //Function to map key of <code>TKey<code> type to value of <code>TValue<code> type
    final FunctionThrowable<TKey, TValue> valueFunctionThrowable;

    final TriConsumerThrowable<TKey, TValue, TValue> changesConsumer;

    /**
     * Construct a repository with given map factory, extra changesConsumer logic and Function to map key to value
     * @param map   Factory to get a map instance
     * @param changesConsumer           Extra steps to run before reset() being called.
     * @param valueFunction     Function to map key of <code>TKey<code> type to value of <code>TValue<code> type
     */
    public Repository(Map<TKey, TValue> map,
                      TriConsumerThrowable<TKey, TValue, TValue> changesConsumer,
                      FunctionThrowable<TKey, TValue> valueFunction){
        storage = map;
        Objects.requireNonNull(valueFunction);
        this.valueFunctionThrowable = valueFunction;
        this.changesConsumer = changesConsumer != null ? changesConsumer : (USE_DEFAULT_CHNAGES_LOG ? this::defualtChangesLog : null);
    }

    /**
     * Construct a repository as a HashMap without extra closing action, with function to map key to value
     * @param valueFunction     Function to map key of <code>TKey<code> type to value of <code>TValue<code> type
     */
    public Repository(FunctionThrowable<TKey, TValue> valueFunction){
        this(new HashMap(), null, valueFunction);
    }

    private void defualtChangesLog(TKey key, TValue oldValue, TValue newValue){
        Class keyClass = key.getClass();
        Object value = oldValue == null ? newValue : oldValue;
        Class valueClass = value == null ? null : value.getClass();
        Logger.L("%s<%s,%s>.put(%s: %s -> %s)",
                this.getClass().getSimpleName(), keyClass.getSimpleName(), valueClass.getSimpleName(),
                key, oldValue, newValue);
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
        if(!storage.containsKey(tKey)){
            result = valueFunctionThrowable.apply(tKey);
            storage.put(tKey, result);
            if(changesConsumer != null)
                changesConsumer.accept(tKey, null, result);
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

    public boolean containsKey(Object tKey){
        return storage.containsKey(tKey);
    }

    /**
     * Returns the number of key-value mappings in this map.  If the
     * map contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of key-value mappings in this map
     */
    public int getSize(){
        return storage.size();
    }

//    /**
//     * Extra closing logic to close all AutoCloseable Keys/Values within a limited time specified by DEFAULT_RESET_TIMEOUT
//     */
//    @Override
//    public void reset() {
//        if(isValueInitialized()){
//            List<RunnableThrowable> keyValueToClose = new ArrayList<>();
//            Map<TKey, TValue> map = storage;
//            map.entrySet().forEach(entry -> {
//                TKey key = entry.getKey();
//                if(key instanceof AutoCloseable){
//                    AutoCloseable kClose = (AutoCloseable)key;
//                    keyValueToClose.add(kClose::close);
//                }
//                TValue value = entry.getValue();
//                if(value instanceof AutoCloseable){
//                    AutoCloseable vClose = (AutoCloseable)value;
//                    keyValueToClose.add(vClose::close);
//                }
//
//                if(changesConsumer != null){
//                    Functions.Default.run(changesConsumer, key, value, null);
//                }
//            });
//            map.clear();
//            if(keyValueToClose.size() > 0){
//                Functions.runParallel(keyValueToClose, DEFAULT_RESET_TIMEOUT);
//            }
//        }
//        super.reset();
//    }
}
