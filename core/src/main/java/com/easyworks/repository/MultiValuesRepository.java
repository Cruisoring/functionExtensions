package com.easyworks.repository;

import com.easyworks.function.ConsumerThrowable;
import com.easyworks.function.FunctionThrowable;
import com.easyworks.function.SupplierThrowable;
import com.easyworks.tuple.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Repository where values are of <tt>Tuple</tt> type.
 * @param <TKey>    Type of the key
 */
public class MultiValuesRepository<TKey> extends Repository<TKey, Tuple> {

    /**
     * Construct a repository with given map factory, extra closing logic and Function to map key to value
     * Shall be considerer only when the Tuple value contains more than 7 elements
     * @param storageSupplier   Factory to get a map instance
     * @param closing           Extra steps to run before reset() being called.
     * @param valueFunction     Function to map key of <tt>TKey<tt> type to value of <tt>Tuple<tt> type
     */
    protected MultiValuesRepository(SupplierThrowable<Map<TKey, Tuple>> storageSupplier,
                                    ConsumerThrowable<Map<TKey, Tuple>> closing,
                                    FunctionThrowable<TKey, Tuple> valueFunction){
        super(storageSupplier, closing, valueFunction);
    }

    /**
     * Construct a repository with  Function to map key to value
     * Shall be considerer only when the Tuple value contains more than 7 elements
     * @param valueFunction     Function to map key of <tt>TKey<tt> type to value of <tt>Tuple<tt> type
     */
    protected MultiValuesRepository(FunctionThrowable<TKey, Tuple> valueFunction){
        this(HashMap::new, null, valueFunction);
    }

    //region Factory methods to create MultiValuesRepository ith Function to map key to Tuple, and optional Map and closing action
    public static <TKey,T> SingleValuesRepository<TKey, T> toSingleValuesRepository(
            SupplierThrowable<Map<TKey, Tuple>> storageSupplier,
            ConsumerThrowable<Map<TKey, Tuple>> closing,
            FunctionThrowable<TKey, Single<T>> valueFunction){
        return new SingleValuesRepository(storageSupplier, closing, valueFunction);
    }

    public static <TKey,T,U> DualValuesRepository<TKey, T, U> toDualValuesRepository(
            SupplierThrowable<Map<TKey, Tuple>> storageSupplier,
            ConsumerThrowable<Map<TKey, Tuple>> closing,
            FunctionThrowable<TKey, Dual<T,U>> valueFunction){
        return new DualValuesRepository(storageSupplier, closing, valueFunction);
    }

    public static <TKey,T,U,V> TripleValuesRepository<TKey,T,U,V> toTripleValuesRepository(
            SupplierThrowable<Map<TKey, Tuple>> storageSupplier,
            ConsumerThrowable<Map<TKey, Tuple>> closing,
            FunctionThrowable<TKey, Triple<T,U,V>> valueFunction){
        return new TripleValuesRepository(storageSupplier, closing, valueFunction);
    }

    public static <TKey,T,U,V,W> QuadValuesRepository<TKey,T,U,V,W> toQuadValuesRepository(
            SupplierThrowable<Map<TKey, Tuple>> storageSupplier,
            ConsumerThrowable<Map<TKey, Tuple>> closing,
            FunctionThrowable<TKey, Quad<T,U,V,W>> valueFunction){
        return new QuadValuesRepository(storageSupplier, closing, valueFunction);
    }

    public static <TKey,T,U,V,W,X> PentaValuesRepository<TKey,T,U,V,W,X> toPentaValuesRepository(
            SupplierThrowable<Map<TKey, Tuple>> storageSupplier,
            ConsumerThrowable<Map<TKey, Tuple>> closing,
            FunctionThrowable<TKey, Penta<T,U,V,W,X>> valueFunction){
        return new PentaValuesRepository(storageSupplier, closing, valueFunction);
    }

    public static <TKey,T,U,V,W,X,Y> HexaValuesRepository<TKey,T,U,V,W,X,Y> toHexaValuesRepository(
            SupplierThrowable<Map<TKey, Tuple>> storageSupplier,
            ConsumerThrowable<Map<TKey, Tuple>> closing,
            FunctionThrowable<TKey, Hexa<T,U,V,W,X,Y>> valueFunction){
        return new HexaValuesRepository(storageSupplier, closing, valueFunction);
    }

    public static <TKey,T,U,V,W,X,Y,Z> HeptaValuesRepository<TKey,T,U,V,W,X,Y,Z> toHeptaValuesRepository(
            SupplierThrowable<Map<TKey, Tuple>> storageSupplier,
            ConsumerThrowable<Map<TKey, Tuple>> closing,
            FunctionThrowable<TKey, Hepta<T,U,V,W,X,Y,Z>> valueFunction){
        return new HeptaValuesRepository(storageSupplier, closing, valueFunction);
    }

    public static <TKey> MultiValuesRepository<TKey> toMultiValuesRepository(
            SupplierThrowable<Map<TKey, Tuple>> storageSupplier,
            ConsumerThrowable<Map<TKey, Tuple>> closing,
            FunctionThrowable<TKey, Tuple> valueFunction){
        return new MultiValuesRepository<TKey>(storageSupplier, closing, valueFunction);
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

    public static <TKey,T,U,V,W,X,Y> HexaValuesRepository<TKey,T,U,V,W,X,Y> toHaxaValuesRepository(
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
