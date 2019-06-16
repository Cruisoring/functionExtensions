package io.github.cruisoring.repository;

import io.github.cruisoring.throwables.FunctionThrowable;
import io.github.cruisoring.throwables.TriConsumerThrowable;
import io.github.cruisoring.tuple.*;

import java.util.HashMap;
import java.util.Map;

import static io.github.cruisoring.Asserts.checkNoneNulls;

/**
 * Repository where values are of <tt>Tuple</tt> type, but the key is single value of type <tt>TKey</tt>.
 *
 * @param <TKey> Type of the key
 */
public class TupleRepository<TKey> extends Repository<TKey, Tuple> {

    /**
     * Construct a repository with given map factory, extra changesConsumer logic and Function to map key to value
     * Shall be considerer only when the Tuple value contains more than 7 elements
     *
     * @param map             Factory to get a map instance
     * @param changesConsumer Extra steps to run before closing() being called.
     * @param valueFunction   Function to map key of TKey type to value of TValue type
     */
    protected TupleRepository(Map<TKey, Tuple> map,
                              TriConsumerThrowable<TKey, Tuple, Tuple> changesConsumer,
                              FunctionThrowable<TKey, Tuple> valueFunction) {
        super(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with  Function to map key to value
     * Shall be considerer only when the Tuple value contains more than 7 elements
     *
     * @param valueFunction Function to map key of TKey type to value of TValue type
     */
    protected TupleRepository(FunctionThrowable<TKey, Tuple> valueFunction) {
        this(new HashMap(), null, valueFunction);
    }

    public static <TKey> TupleRepository<TKey> create(
            Map<TKey, Tuple> map,
            TriConsumerThrowable<TKey, Tuple, Tuple> changesConsumer,
            FunctionThrowable<TKey, Tuple> valueFunction) {
        return new TupleRepository<TKey>(map, changesConsumer, valueFunction);
    }

    public static <TKey> TupleRepository<TKey> create(
            FunctionThrowable<TKey, Tuple> valueFunction) {
        return new TupleRepository<TKey>(valueFunction);
    }

    //region Factory methods to create TupleRepository ith Function to map key to Tuple, and optional Map and changesConsumer action

    public static <TKey, T> TupleRepository1<TKey, T> create1(
            Map<TKey, Tuple1<T>> map,
            TriConsumerThrowable<TKey, Tuple1<T>, Tuple1<T>> changesConsumer,
            FunctionThrowable<TKey, Tuple1<T>> valueFunction) {
        return new TupleRepository1(map, changesConsumer, valueFunction);
    }

    public static <TKey, T, U> TupleRepository2<TKey, T, U> create2(
            Map<TKey, Tuple2<T, U>> map,
            TriConsumerThrowable<TKey, Tuple2<T, U>, Tuple2<T, U>> changesConsumer,
            FunctionThrowable<TKey, Tuple2<T, U>> valueFunction) {
        return new TupleRepository2(map, changesConsumer, valueFunction);
    }

    public static <TKey, T, U, V> TupleRepository3<TKey, T, U, V> create3(
            Map<TKey, Tuple3<T, U, V>> map,
            TriConsumerThrowable<TKey, Tuple3<T, U, V>, Tuple3<T, U, V>> changesConsumer,
            FunctionThrowable<TKey, Tuple3<T, U, V>> valueFunction) {
        return new TupleRepository3(map, changesConsumer, valueFunction);
    }

    public static <TKey, T, U, V, W> TupleRepository4<TKey, T, U, V, W> create4(
            Map<TKey, Tuple4<T, U, V, W>> map,
            TriConsumerThrowable<TKey, Tuple4<T, U, V, W>, Tuple4<T, U, V, W>> changesConsumer,
            FunctionThrowable<TKey, Tuple4<T, U, V, W>> valueFunction) {
        return new TupleRepository4(map, changesConsumer, valueFunction);
    }

    public static <TKey, T, U, V, W, X> TupleRepository5<TKey, T, U, V, W, X> create5(
            Map<TKey, Tuple5<T, U, V, W, X>> map,
            TriConsumerThrowable<TKey, Tuple5<T, U, V, W, X>, Tuple5<T, U, V, W, X>> changesConsumer,
            FunctionThrowable<TKey, Tuple5<T, U, V, W, X>> valueFunction) {
        return new TupleRepository5(map, changesConsumer, valueFunction);
    }

    public static <TKey, T, U, V, W, X, Y> TupleRepository6<TKey, T, U, V, W, X, Y> create6(
            Map<TKey, Tuple6<T, U, V, W, X, Y>> map,
            TriConsumerThrowable<TKey, Tuple6<T, U, V, W, X, Y>, Tuple6<T, U, V, W, X, Y>> changesConsumer,
            FunctionThrowable<TKey, Tuple6<T, U, V, W, X, Y>> valueFunction) {
        return new TupleRepository6(map, changesConsumer, valueFunction);
    }

    public static <TKey, T, U, V, W, X, Y, Z> TupleRepository7<TKey, T, U, V, W, X, Y, Z> create7(
            Map<TKey, Tuple7<T, U, V, W, X, Y, Z>> map,
            TriConsumerThrowable<TKey, Tuple7<T, U, V, W, X, Y, Z>, Tuple7<T, U, V, W, X, Y, Z>> changesConsumer,
            FunctionThrowable<TKey, Tuple7<T, U, V, W, X, Y, Z>> valueFunction) {
        return new TupleRepository7(map, changesConsumer, valueFunction);
    }

    //region Factory methods to create TupleRepository with Function to map key to Tuple
    public static <TKey, T> TupleRepository1<TKey, T> create1(
            FunctionThrowable<TKey, Tuple1<T>> valueFunction) {
        return new TupleRepository1(valueFunction);
    }

    public static <TKey, T, U> TupleRepository2<TKey, T, U> create2(
            FunctionThrowable<TKey, Tuple2<T, U>> valueFunction) {
        return new TupleRepository2(valueFunction);
    }
    //endregion

    public static <TKey, T, U, V> TupleRepository3<TKey, T, U, V> create3(
            FunctionThrowable<TKey, Tuple3<T, U, V>> valueFunction) {
        return new TupleRepository3(valueFunction);
    }

    public static <TKey, T, U, V, W> TupleRepository4<TKey, T, U, V, W> create4(
            FunctionThrowable<TKey, Tuple4<T, U, V, W>> valueFunction) {
        return new TupleRepository4(valueFunction);
    }

    public static <TKey, T, U, V, W, X> TupleRepository5<TKey, T, U, V, W, X> create5(
            FunctionThrowable<TKey, Tuple5<T, U, V, W, X>> valueFunction) {
        return new TupleRepository5(valueFunction);
    }

    public static <TKey, T, U, V, W, X, Y> TupleRepository6<TKey, T, U, V, W, X, Y> create6(
            FunctionThrowable<TKey, Tuple6<T, U, V, W, X, Y>> valueFunction) {
        return new TupleRepository6(valueFunction);
    }

    public static <TKey, T, U, V, W, X, Y, Z> TupleRepository7<TKey, T, U, V, W, X, Y, Z> create7(
            FunctionThrowable<TKey, Tuple7<T, U, V, W, X, Y, Z>> valueFunction) {
        return new TupleRepository7(valueFunction);
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the specified
     * key.  More formally, returns <tt>true</tt> if and only if
     * this map contains a mapping for a key <tt>k</tt> such that
     * <tt>(key==null ? k==null : key.equals(k))</tt>.  (There can be
     * at most one such mapping.)
     *
     * @param key key whose presence in this map is to be tested
     * @return <tt>true</tt> if this map contains a mapping for the specified
     * key, <tt>null</tt> if no such mapping
     */
    public Tuple retrieve(TKey key) {
        return get(key, null);
    }

    /**
     * Override update method to prevent keep null as the value
     *
     * @param tKey          The key used to get the concerned value
     * @param existingValue The existing value mapped from the given key, or null if the map does not contains the key
     * @param newValue      The value to be used to put or replace the existing value related with the given key
     * @return The latest value related with the given key
     * @throws Exception Any Exceptions that might be thrown
     */
    @Override
    public Tuple update(TKey tKey, Tuple existingValue, Tuple newValue) throws Exception {
        checkNoneNulls(newValue);
        return super.update(tKey, existingValue, newValue);
    }
    //endregion
}
