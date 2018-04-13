package com.easyworks.repository;

import com.easyworks.function.FunctionThrowable;
import com.easyworks.function.TriConsumerThrowable;
import com.easyworks.tuple.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Repository where values are of <tt>Tuple</tt> type, but the key is single value of type <tt>TKey</tt>.
 * @param <TKey>    Type of the key
 */
public class MultiValuesRepository<TKey> extends Repository<TKey, Tuple> {

    /**
     * Construct a repository with given map factory, extra changesConsumer logic and Function to map key to value
     * Shall be considerer only when the Tuple value contains more than 7 elements
     * @param map   Factory to get a map instance
     * @param changesConsumer           Extra steps to run before reset() being called.
     * @param valueFunction     Function to map key of <tt>TKey<tt> type to value of <tt>Tuple<tt> type
     */
    protected MultiValuesRepository(Map<TKey, Tuple> map,
                                    TriConsumerThrowable<TKey, Tuple, Tuple> changesConsumer,
                                    FunctionThrowable<TKey, Tuple> valueFunction){
        super(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with  Function to map key to value
     * Shall be considerer only when the Tuple value contains more than 7 elements
     * @param valueFunction     Function to map key of <tt>TKey<tt> type to value of <tt>Tuple<tt> type
     */
    protected MultiValuesRepository(FunctionThrowable<TKey, Tuple> valueFunction){
        this(new HashMap(), null, valueFunction);
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
     *         key, <tt>null</tt> if no such mapping
     */
    public Tuple retrieve(TKey key){
        return get(key, null);
    }

    //region Factory methods to create MultiValuesRepository ith Function to map key to Tuple, and optional Map and changesConsumer action
    public static <TKey,T> SingleValuesRepository<TKey, T> toSingleValuesRepository(
            Map<TKey, Tuple1<T>> map,
            TriConsumerThrowable<TKey, Tuple1<T>, Tuple1<T>> changesConsumer,
            FunctionThrowable<TKey, Tuple1<T>> valueFunction){
        return new SingleValuesRepository(map, changesConsumer, valueFunction);
    }

    public static <TKey,T,U,V> TripleValuesRepository<TKey,T,U,V> toTripleValuesRepository(
            Map<TKey, Tuple3<T,U,V>> map,
            TriConsumerThrowable<TKey, Tuple3<T,U,V>, Tuple3<T,U,V>> changesConsumer,
            FunctionThrowable<TKey, Tuple3<T,U,V>> valueFunction){
        return new TripleValuesRepository(map, changesConsumer, valueFunction);
    }

    public static <TKey,T,U,V,W> QuadValuesRepository<TKey,T,U,V,W> toQuadValuesRepository(
            Map<TKey, Tuple4<T,U,V,W>> map,
            TriConsumerThrowable<TKey, Tuple4<T,U,V,W>, Tuple4<T,U,V,W>> changesConsumer,
            FunctionThrowable<TKey, Tuple4<T,U,V,W>> valueFunction){
        return new QuadValuesRepository(map, changesConsumer, valueFunction);
    }

    public static <TKey,T,U,V,W,X> PentaValuesRepository<TKey,T,U,V,W,X> toPentaValuesRepository(
            Map<TKey, Tuple5<T,U,V,W,X>> map,
            TriConsumerThrowable<TKey, Tuple5<T,U,V,W,X>, Tuple5<T,U,V,W,X>> changesConsumer,
            FunctionThrowable<TKey, Tuple5<T,U,V,W,X>> valueFunction){
        return new PentaValuesRepository(map, changesConsumer, valueFunction);
    }

    public static <TKey,T,U,V,W,X,Y> HexaValuesRepository<TKey,T,U,V,W,X,Y> toHexaValuesRepository(
            Map<TKey, Tuple6<T,U,V,W,X,Y>> map,
            TriConsumerThrowable<TKey, Tuple6<T,U,V,W,X,Y>, Tuple6<T,U,V,W,X,Y>> changesConsumer,
            FunctionThrowable<TKey, Tuple6<T,U,V,W,X,Y>> valueFunction){
        return new HexaValuesRepository(map, changesConsumer, valueFunction);
    }

    public static <TKey,T,U,V,W,X,Y,Z> HeptaValuesRepository<TKey,T,U,V,W,X,Y,Z> toHeptaValuesRepository(
            Map<TKey, Tuple7<T,U,V,W,X,Y,Z>> map,
            TriConsumerThrowable<TKey, Tuple7<T,U,V,W,X,Y,Z>, Tuple7<T,U,V,W,X,Y,Z>> changesConsumer,
            FunctionThrowable<TKey, Tuple7<T,U,V,W,X,Y,Z>> valueFunction){
        return new HeptaValuesRepository(map, changesConsumer, valueFunction);
    }

    public static <TKey> MultiValuesRepository<TKey> toMultiValuesRepository(
            Map<TKey, Tuple> map,
            TriConsumerThrowable<TKey, Tuple, Tuple> changesConsumer,
            FunctionThrowable<TKey, Tuple> valueFunction){
        return new MultiValuesRepository<TKey>(map, changesConsumer, valueFunction);
    }
    //endregion


    //region Factory methods to create MultiValuesRepository with Function to map key to Tuple
    public static <TKey,T> SingleValuesRepository<TKey, T> toSingleValuesRepository(
            FunctionThrowable<TKey, Tuple1<T>> valueFunction){
        return new SingleValuesRepository(valueFunction);
    }

    public static <TKey,T,U,V> TripleValuesRepository<TKey,T,U,V> toTripleValuesRepository(
            FunctionThrowable<TKey, Tuple3<T,U,V>> valueFunction){
        return new TripleValuesRepository(valueFunction);
    }

    public static <TKey,T,U,V,W> QuadValuesRepository<TKey,T,U,V,W> toQuadValuesRepository(
            FunctionThrowable<TKey, Tuple4<T,U,V,W>> valueFunction){
        return new QuadValuesRepository(valueFunction);
    }

    public static <TKey,T,U,V,W,X> PentaValuesRepository<TKey,T,U,V,W,X> toPentaValuesRepository(
            FunctionThrowable<TKey, Tuple5<T,U,V,W,X>> valueFunction){
        return new PentaValuesRepository(valueFunction);
    }

    public static <TKey,T,U,V,W,X,Y> HexaValuesRepository<TKey,T,U,V,W,X,Y> toHexaValuesRepository(
            FunctionThrowable<TKey, Tuple6<T,U,V,W,X,Y>> valueFunction){
        return new HexaValuesRepository(valueFunction);
    }

    public static <TKey,T,U,V,W,X,Y,Z> HeptaValuesRepository<TKey,T,U,V,W,X,Y,Z> toHeptaValuesRepository(
            FunctionThrowable<TKey, Tuple7<T,U,V,W,X,Y,Z>> valueFunction){
        return new HeptaValuesRepository(valueFunction);
    }

    public static <TKey> MultiValuesRepository<TKey> toMultiValuesRepository(
            FunctionThrowable<TKey, Tuple> valueFunction){
        return new MultiValuesRepository<TKey>(valueFunction);
    }
    //endregion
}
