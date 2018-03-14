package com.easyworks.repository;

import com.easyworks.function.FunctionThrowable;
import com.easyworks.function.SupplierThrowable;
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
     * @param storageSupplier   Factory to get a map instance
     * @param changesConsumer           Extra steps to run before reset() being called.
     * @param valueFunction     Function to map key of <tt>TKey<tt> type to value of <tt>Tuple<tt> type
     */
    protected MultiValuesRepository(SupplierThrowable<Map<TKey, Tuple>> storageSupplier,
                                    TriConsumerThrowable<TKey, Tuple, Tuple> changesConsumer,
                                    FunctionThrowable<TKey, Tuple> valueFunction){
        super(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with  Function to map key to value
     * Shall be considerer only when the Tuple value contains more than 7 elements
     * @param valueFunction     Function to map key of <tt>TKey<tt> type to value of <tt>Tuple<tt> type
     */
    protected MultiValuesRepository(FunctionThrowable<TKey, Tuple> valueFunction){
        this(HashMap::new, null, valueFunction);
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
            SupplierThrowable<Map<TKey, Single<T>>> storageSupplier,
            TriConsumerThrowable<TKey, Single<T>, Single<T>> changesConsumer,
            FunctionThrowable<TKey, Single<T>> valueFunction){
        return new SingleValuesRepository(storageSupplier, changesConsumer, valueFunction);
    }

    public static <TKey,T,U> DualValuesRepository<TKey, T, U> toDualValuesRepository(
            SupplierThrowable<Map<TKey, Dual<T,U>>> storageSupplier,
            TriConsumerThrowable<TKey, Dual<T,U>, Dual<T,U>> changesConsumer,
            FunctionThrowable<TKey, Dual<T,U>> valueFunction){
        return new DualValuesRepository(storageSupplier, changesConsumer, valueFunction);
    }

    public static <TKey,T,U,V> TripleValuesRepository<TKey,T,U,V> toTripleValuesRepository(
            SupplierThrowable<Map<TKey, Triple<T,U,V>>> storageSupplier,
            TriConsumerThrowable<TKey, Triple<T,U,V>, Triple<T,U,V>> changesConsumer,
            FunctionThrowable<TKey, Triple<T,U,V>> valueFunction){
        return new TripleValuesRepository(storageSupplier, changesConsumer, valueFunction);
    }

    public static <TKey,T,U,V,W> QuadValuesRepository<TKey,T,U,V,W> toQuadValuesRepository(
            SupplierThrowable<Map<TKey, Quad<T,U,V,W>>> storageSupplier,
            TriConsumerThrowable<TKey, Quad<T,U,V,W>, Quad<T,U,V,W>> changesConsumer,
            FunctionThrowable<TKey, Quad<T,U,V,W>> valueFunction){
        return new QuadValuesRepository(storageSupplier, changesConsumer, valueFunction);
    }

    public static <TKey,T,U,V,W,X> PentaValuesRepository<TKey,T,U,V,W,X> toPentaValuesRepository(
            SupplierThrowable<Map<TKey, Penta<T,U,V,W,X>>> storageSupplier,
            TriConsumerThrowable<TKey, Penta<T,U,V,W,X>, Penta<T,U,V,W,X>> changesConsumer,
            FunctionThrowable<TKey, Penta<T,U,V,W,X>> valueFunction){
        return new PentaValuesRepository(storageSupplier, changesConsumer, valueFunction);
    }

    public static <TKey,T,U,V,W,X,Y> HexaValuesRepository<TKey,T,U,V,W,X,Y> toHexaValuesRepository(
            SupplierThrowable<Map<TKey, Hexa<T,U,V,W,X,Y>>> storageSupplier,
            TriConsumerThrowable<TKey, Hexa<T,U,V,W,X,Y>, Hexa<T,U,V,W,X,Y>> changesConsumer,
            FunctionThrowable<TKey, Hexa<T,U,V,W,X,Y>> valueFunction){
        return new HexaValuesRepository(storageSupplier, changesConsumer, valueFunction);
    }

    public static <TKey,T,U,V,W,X,Y,Z> HeptaValuesRepository<TKey,T,U,V,W,X,Y,Z> toHeptaValuesRepository(
            SupplierThrowable<Map<TKey, Hepta<T,U,V,W,X,Y,Z>>> storageSupplier,
            TriConsumerThrowable<TKey, Hepta<T,U,V,W,X,Y,Z>, Hepta<T,U,V,W,X,Y,Z>> changesConsumer,
            FunctionThrowable<TKey, Hepta<T,U,V,W,X,Y,Z>> valueFunction){
        return new HeptaValuesRepository(storageSupplier, changesConsumer, valueFunction);
    }

    public static <TKey> MultiValuesRepository<TKey> toMultiValuesRepository(
            SupplierThrowable<Map<TKey, Tuple>> storageSupplier,
            TriConsumerThrowable<TKey, Tuple, Tuple> changesConsumer,
            FunctionThrowable<TKey, Tuple> valueFunction){
        return new MultiValuesRepository<TKey>(storageSupplier, changesConsumer, valueFunction);
    }
    //endregion


    //region Factory methods to create MultiValuesRepository with Function to map key to Tuple
    public static <TKey,T> SingleValuesRepository<TKey, T> toSingleValuesRepository(
            FunctionThrowable<TKey, Single<T>> valueFunction){
        return new SingleValuesRepository(valueFunction);
    }

    public static <TKey,T,U> DualValuesRepository<TKey, T, U> toDualValuesRepository(
            FunctionThrowable<TKey, Dual<T,U>> valueFunction){
        return new DualValuesRepository(valueFunction);
    }

    public static <TKey,T,U,V> TripleValuesRepository<TKey,T,U,V> toTripleValuesRepository(
            FunctionThrowable<TKey, Triple<T,U,V>> valueFunction){
        return new TripleValuesRepository(valueFunction);
    }

    public static <TKey,T,U,V,W> QuadValuesRepository<TKey,T,U,V,W> toQuadValuesRepository(
            FunctionThrowable<TKey, Quad<T,U,V,W>> valueFunction){
        return new QuadValuesRepository(valueFunction);
    }

    public static <TKey,T,U,V,W,X> PentaValuesRepository<TKey,T,U,V,W,X> toPentaValuesRepository(
            FunctionThrowable<TKey, Penta<T,U,V,W,X>> valueFunction){
        return new PentaValuesRepository(valueFunction);
    }

    public static <TKey,T,U,V,W,X,Y> HexaValuesRepository<TKey,T,U,V,W,X,Y> toHexaValuesRepository(
            FunctionThrowable<TKey, Hexa<T,U,V,W,X,Y>> valueFunction){
        return new HexaValuesRepository(valueFunction);
    }

    public static <TKey,T,U,V,W,X,Y,Z> HeptaValuesRepository<TKey,T,U,V,W,X,Y,Z> toHeptaValuesRepository(
            FunctionThrowable<TKey, Hepta<T,U,V,W,X,Y,Z>> valueFunction){
        return new HeptaValuesRepository(valueFunction);
    }

    public static <TKey> MultiValuesRepository<TKey> toMultiValuesRepository(
            FunctionThrowable<TKey, Tuple> valueFunction){
        return new MultiValuesRepository<TKey>(valueFunction);
    }
    //endregion
}
