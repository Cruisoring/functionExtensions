package com.easyworks.repository;

import com.easyworks.function.*;
import com.easyworks.tuple.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic repository use Tuple.Tuple6 as values to keep 6 elements mapped from a specific key
 *
 * @param <TKey>    type of the key, can be any Type
 * @param <T>       type of the first element of the Tuple.Tuple6 value mapped from the key
 * @param <U>       type of the second element of the Tuple.Tuple6 value mapped from the key
 * @param <V>       type of the third element of the Tuple.Tuple6 value mapped from the key
 * @param <W>       type of the fourth element of the Tuple.Tuple6 value mapped from the key
 * @param <X>       type of the fifth element of the Tuple.Tuple6 value mapped from the key
 * @param <Y>       type of the sixth element of the Tuple.Tuple6 value mapped from the key
 */
public class HexaValuesRepository<TKey, T,U,V,W,X,Y>
        extends Repository<TKey, Tuple6<T,U,V,W,X,Y>>
        implements HexaValues<TKey, T,U,V,W,X,Y> {


    //region Factories to create repositories of keeping 6 elements as the map value
    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map any key to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 1 key to Tuple of 6 elements
     * @param <TKey>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple6 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple6 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple6 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple6 value mapped from the key
     * @param <X>   type of the fifth element of the Tuple.Tuple6 value mapped from the key
     * @param <Y>   type of the sixth element of the Tuple.Tuple6 value mapped from the key
     * @return      Constructed Repository to map 1 key to 6 values as a Tuple
     */
    public static <TKey,T,U,V,W,X,Y> HexaValuesRepository<TKey, T,U,V,W,X,Y> fromKey(
            SupplierThrowable<Map<TKey, Tuple6<T,U,V,W,X,Y>>> storageSupplier,
            TriConsumerThrowable<TKey, Tuple6<T,U,V,W,X,Y>, Tuple6<T,U,V,W,X,Y>> changesConsumer,
            FunctionThrowable<TKey, Tuple6<T,U,V,W,X,Y>> valueFunction){
        return new HexaValuesRepository(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with given map factory
     *
     * @param valueFunction     Function to map 1 key to Tuple of 6 elements
     * @param <TKey>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple6 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple6 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple6 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple6 value mapped from the key
     * @param <X>   type of the fifth element of the Tuple.Tuple6 value mapped from the key
     * @param <Y>   type of the sixth element of the Tuple.Tuple6 value mapped from the key
     * @return      Constructed Repository to map 1 key to 6 values as a Tuple
     */
    public static <TKey, T,U,V,W,X,Y> HexaValuesRepository<TKey, T,U,V,W,X,Y> fromKey(
            FunctionThrowable<TKey, Tuple6<T,U,V,W,X,Y>> valueFunction){
        return new HexaValuesRepository(valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 1 key to Tuple of 6 elements
     * @param <K1>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple6 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple6 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple6 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple6 value mapped from the key
     * @param <X>   type of the fifth element of the Tuple.Tuple6 value mapped from the key
     * @param <Y>   type of the sixth element of the Tuple.Tuple6 value mapped from the key
     * @return      Constructed Repository to map 1 key to 6 values as a Tuple
     */
    public static <K1, T,U,V,W,X,Y> SingleKeys<K1, T,U,V,W,X,Y> fromOneKeys(
            SupplierThrowable<Map<Tuple1<K1>, Tuple6<T,U,V,W,X,Y>>> storageSupplier,
            TriConsumerThrowable<Tuple1<K1>, Tuple6<T,U,V,W,X,Y>, Tuple6<T,U,V,W,X,Y>> changesConsumer,
            FunctionThrowable<K1, Tuple6<T,U,V,W,X,Y>> valueFunction){
        return new SingleKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 2 keys to Tuple of 6 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple6 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple6 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple6 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple6 value mapped from the key
     * @param <X>   type of the fifth element of the Tuple.Tuple6 value mapped from the key
     * @param <Y>   type of the sixth element of the Tuple.Tuple6 value mapped from the key
     * @return      Constructed Repository to map 2 keys to 6 values as a Tuple
     */
    public static <K1,K2, T,U,V,W,X,Y> DualKeys<K1,K2, T,U,V,W,X,Y> fromTwoKeys(
            SupplierThrowable<Map<Tuple2<K1,K2>, Tuple6<T,U,V,W,X,Y>>> storageSupplier,
            TriConsumerThrowable<Tuple2<K1,K2>, Tuple6<T,U,V,W,X,Y>, Tuple6<T,U,V,W,X,Y>> changesConsumer,
            BiFunctionThrowable<K1,K2, Tuple6<T,U,V,W,X,Y>> valueFunction){
        return new DualKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 3 keys to Tuple of 6 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple6 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple6 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple6 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple6 value mapped from the key
     * @param <X>   type of the fifth element of the Tuple.Tuple6 value mapped from the key
     * @param <Y>   type of the sixth element of the Tuple.Tuple6 value mapped from the key
     * @return      Constructed Repository to map 3 keys to 6 values as a Tuple
     */
    public static <K1,K2,K3, T,U,V,W,X,Y> TripleKeys<K1,K2,K3, T,U,V,W,X,Y> fromThreeKeys(
            SupplierThrowable<Map<Tuple3<K1,K2,K3>, Tuple6<T,U,V,W,X,Y>>> storageSupplier,
            TriConsumerThrowable<Tuple3<K1,K2,K3>, Tuple6<T,U,V,W,X,Y>, Tuple6<T,U,V,W,X,Y>> changesConsumer,
            TriFunctionThrowable<K1,K2,K3, Tuple6<T,U,V,W,X,Y>> valueFunction){
        return new TripleKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 4 keys to Tuple of 6 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple6 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple6 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple6 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple6 value mapped from the key
     * @param <X>   type of the fifth element of the Tuple.Tuple6 value mapped from the key
     * @param <Y>   type of the sixth element of the Tuple.Tuple6 value mapped from the key
     * @return      Constructed Repository to map 4 keys to 6 values as a Tuple
     */
    public static <K1,K2,K3,K4, T,U,V,W,X,Y> QuadKeys<K1,K2,K3,K4, T,U,V,W,X,Y> fromFourKeys(
            SupplierThrowable<Map<Tuple4<K1,K2,K3,K4>, Tuple6<T,U,V,W,X,Y>>> storageSupplier,
            TriConsumerThrowable<Tuple4<K1,K2,K3,K4>, Tuple6<T,U,V,W,X,Y>, Tuple6<T,U,V,W,X,Y>> changesConsumer,
            QuadFunctionThrowable<K1,K2,K3,K4, Tuple6<T,U,V,W,X,Y>> valueFunction){
        return new QuadKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 5 keys to Tuple of 6 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple6 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple6 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple6 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple6 value mapped from the key
     * @param <X>   type of the fifth element of the Tuple.Tuple6 value mapped from the key
     * @param <Y>   type of the sixth element of the Tuple.Tuple6 value mapped from the key
     * @return      Constructed Repository to map 5 keys to 6 values as a Tuple
     */
    public static <K1,K2,K3,K4,K5, T,U,V,W,X,Y> PentaKeys<K1,K2,K3,K4,K5, T,U,V,W,X,Y> fromFiveKeys(
            SupplierThrowable<Map<Tuple5<K1,K2,K3,K4,K5>, Tuple6<T,U,V,W,X,Y>>> storageSupplier,
            TriConsumerThrowable<Tuple5<K1,K2,K3,K4,K5>, Tuple6<T,U,V,W,X,Y>, Tuple6<T,U,V,W,X,Y>> changesConsumer,
            PentaFunctionThrowable<K1,K2,K3,K4,K5, Tuple6<T,U,V,W,X,Y>> valueFunction){
        return new PentaKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 6 keys to Tuple of 6 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <K6>  type of the sixth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple6 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple6 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple6 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple6 value mapped from the key
     * @param <X>   type of the fifth element of the Tuple.Tuple6 value mapped from the key
     * @param <Y>   type of the sixth element of the Tuple.Tuple6 value mapped from the key
     * @return      Constructed Repository to map 6 keys to 6 values as a Tuple
     */
    public static <K1,K2,K3,K4,K5,K6, T,U,V,W,X,Y> HexaKeys<K1,K2,K3,K4,K5,K6, T,U,V,W,X,Y> fromSixKeys(
            SupplierThrowable<Map<Tuple6<K1,K2,K3,K4,K5,K6>, Tuple6<T,U,V,W,X,Y>>> storageSupplier,
            TriConsumerThrowable<Tuple6<K1,K2,K3,K4,K5,K6>, Tuple6<T,U,V,W,X,Y>, Tuple6<T,U,V,W,X,Y>> changesConsumer,
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Tuple6<T,U,V,W,X,Y>> valueFunction){
        return new HexaKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 7 keys to Tuple of 6 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <K6>  type of the sixth element of the Key
     * @param <K7>  type of the seventh element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple6 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple6 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple6 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple6 value mapped from the key
     * @param <X>   type of the fifth element of the Tuple.Tuple6 value mapped from the key
     * @param <Y>   type of the sixth element of the Tuple.Tuple6 value mapped from the key
     * @return      Constructed Repository to map 7 keys to 6 values as a Tuple
     */
    public static <K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X,Y> HeptaKeys<K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X,Y> fromSevenKeys(
            SupplierThrowable<Map<Tuple7<K1,K2,K3,K4,K5,K6,K7>, Tuple6<T,U,V,W,X,Y>>> storageSupplier,
            TriConsumerThrowable<Tuple7<K1,K2,K3,K4,K5,K6,K7>, Tuple6<T,U,V,W,X,Y>, Tuple6<T,U,V,W,X,Y>> changesConsumer,
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Tuple6<T,U,V,W,X,Y>> valueFunction){
        return new HeptaKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 1 key to Tuple of 6 elements
     * @param <K1>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple6 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple6 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple6 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple6 value mapped from the key
     * @param <X>   type of the fifth element of the Tuple.Tuple6 value mapped from the key
     * @param <Y>   type of the sixth element of the Tuple.Tuple6 value mapped from the key
     * @return      Constructed Repository to map 1 key to 6 values as a Tuple
     */
    public static <K1, T,U,V,W,X,Y> SingleKeys<K1, T,U,V,W,X,Y> fromOneKeys(
            FunctionThrowable<K1, Tuple6<T,U,V,W,X,Y>> valueFunction){
        return new SingleKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 2 keys to Tuple of 6 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple6 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple6 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple6 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple6 value mapped from the key
     * @param <X>   type of the fifth element of the Tuple.Tuple6 value mapped from the key
     * @param <Y>   type of the sixth element of the Tuple.Tuple6 value mapped from the key
     * @return      Constructed Repository to map 2 keys to 6 values as a Tuple
     */
    public static <K1,K2, T,U,V,W,X,Y> DualKeys<K1,K2, T,U,V,W,X,Y> fromTwoKeys(
            BiFunctionThrowable<K1,K2, Tuple6<T,U,V,W,X,Y>> valueFunction){
        return new DualKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 3 keys to Tuple of 6 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple6 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple6 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple6 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple6 value mapped from the key
     * @param <X>   type of the fifth element of the Tuple.Tuple6 value mapped from the key
     * @param <Y>   type of the sixth element of the Tuple.Tuple6 value mapped from the key
     * @return      Constructed Repository to map 3 keys to 6 values as a Tuple
     */
    public static <K1,K2,K3, T,U,V,W,X,Y> TripleKeys<K1,K2,K3, T,U,V,W,X,Y> fromThreeKeys(
            TriFunctionThrowable<K1,K2,K3, Tuple6<T,U,V,W,X,Y>> valueFunction){
        return new TripleKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 4 keys to Tuple of 6 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple6 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple6 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple6 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple6 value mapped from the key
     * @param <X>   type of the fifth element of the Tuple.Tuple6 value mapped from the key
     * @param <Y>   type of the sixth element of the Tuple.Tuple6 value mapped from the key
     * @return      Constructed Repository to map 4 keys to 6 values as a Tuple
     */
    public static <K1,K2,K3,K4, T,U,V,W,X,Y> QuadKeys<K1,K2,K3,K4, T,U,V,W,X,Y> fromFourKeys(
            QuadFunctionThrowable<K1,K2,K3,K4, Tuple6<T,U,V,W,X,Y>> valueFunction){
        return new QuadKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 5 keys to Tuple of 6 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple6 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple6 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple6 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple6 value mapped from the key
     * @param <X>   type of the fifth element of the Tuple.Tuple6 value mapped from the key
     * @param <Y>   type of the sixth element of the Tuple.Tuple6 value mapped from the key
     * @return      Constructed Repository to map 5 keys to 6 values as a Tuple
     */
    public static <K1,K2,K3,K4,K5, T,U,V,W,X,Y> PentaKeys<K1,K2,K3,K4,K5, T,U,V,W,X,Y> fromFiveKeys(
            PentaFunctionThrowable<K1,K2,K3,K4,K5, Tuple6<T,U,V,W,X,Y>> valueFunction){
        return new PentaKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 6 keys to Tuple of 6 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <K6>  type of the sixth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple6 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple6 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple6 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple6 value mapped from the key
     * @param <X>   type of the fifth element of the Tuple.Tuple6 value mapped from the key
     * @param <Y>   type of the sixth element of the Tuple.Tuple6 value mapped from the key
     * @return      Constructed Repository to map 6 keys to 6 values as a Tuple
     */
    public static <K1,K2,K3,K4,K5,K6, T,U,V,W,X,Y> HexaKeys<K1,K2,K3,K4,K5,K6, T,U,V,W,X,Y> fromSixKeys(
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Tuple6<T,U,V,W,X,Y>> valueFunction){
        return new HexaKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 7 keys to Tuple of 6 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <K6>  type of the sixth element of the Key
     * @param <K7>  type of the seventh element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple6 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple6 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple6 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple6 value mapped from the key
     * @param <X>   type of the fifth element of the Tuple.Tuple6 value mapped from the key
     * @param <Y>   type of the sixth element of the Tuple.Tuple6 value mapped from the key
     * @return      Constructed Repository to map 7 keys to 6 values as a Tuple
     */
    public static <K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X,Y> HeptaKeys<K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X,Y> fromSevenKeys(
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Tuple6<T,U,V,W,X,Y>> valueFunction){
        return new HeptaKeys(valueFunction);
    }
    //endregion

    //region Constructors
    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 1 key to Tuple of 6 elements
     */
    protected HexaValuesRepository(SupplierThrowable<Map<TKey, Tuple6<T,U,V,W,X,Y>>> storageSupplier,
                                   TriConsumerThrowable<TKey, Tuple6<T,U,V,W,X,Y>, Tuple6<T,U,V,W,X,Y>> changesConsumer,
                                   FunctionThrowable<TKey, Tuple6<T,U,V,W,X,Y>> valueFunction){
        super(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 1 key to Tuple of 6 elements
     */
    protected HexaValuesRepository(FunctionThrowable<TKey, Tuple6<T,U,V,W,X,Y>> valueFunction){
        this(HashMap::new, null, valueFunction);
    }
    //endregion

    /**
     * Get the strong-typed Tuple2&lt;T,U&gt; value mapped from the given key
     * @param key   key to retrieve the strong-typed Tuple2&lt;T,U&gt; value
     * @return      the strong-typed Tuple2&lt;T,U&gt; value mapped from the specific key
     */
    @Override
    public Tuple6<T,U,V,W,X,Y> retrieve(TKey key) {
        return get(key, null);
    }

    /**
     * Generic repository use Tuple. to keep value of 2 elements mapped from a key of the Tuple
     * Notice: the actual type of the Key is Tuple.Tuple1 wrapping the actual value of <tt>K1</tt>
     *
     * @param <K1>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple6 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple6 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple6 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple6 value mapped from the key
     * @param <X>   type of the fifth element of the Tuple.Tuple6 value mapped from the key
     * @param <Y>   type of the sixth element of the Tuple.Tuple6 value mapped from the key
     */
    public static class SingleKeys<K1, T,U,V,W,X,Y> extends HexaValuesRepository<Tuple1<K1>, T,U,V,W,X,Y>
            implements com.easyworks.repository.SingleKeys.HexaValues<K1, T,U,V,W,X,Y> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 1 key to Tuple of 6 elements
         */
        protected SingleKeys(SupplierThrowable<Map<Tuple1<K1>, Tuple6<T,U,V,W,X,Y>>> storageSupplier,
                             TriConsumerThrowable<Tuple1<K1>, Tuple6<T,U,V,W,X,Y>, Tuple6<T,U,V,W,X,Y>> changesConsumer,
                             FunctionThrowable<K1, Tuple6<T,U,V,W,X,Y>> valueFunction) {
            super(storageSupplier, changesConsumer, single -> valueFunction.apply(single.getFirst()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 1 key to Tuple of 6 elements
         */
        protected SingleKeys(FunctionThrowable<K1, Tuple6<T,U,V,W,X,Y>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple6 to keep value of 2 elements mapped from a key, and keep the key as Tuple.Tuple6
     * of 2 different elements
     *
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple6 mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple6 mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple6 mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple6 mapped from the key
     * @param <X>   type of the fifth element of the Tuple.Tuple6 mapped from the key
     * @param <Y>   type of the sixth element of the Tuple.Tuple6 mapped from the key
     */
    public static class DualKeys<K1,K2, T,U,V,W,X,Y> extends HexaValuesRepository<Tuple2<K1,K2>, T,U,V,W,X,Y>
            implements com.easyworks.repository.DualKeys.HexaValues<K1,K2, T,U,V,W,X,Y> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 2 keys to Tuple of 6 elements
         */
        protected DualKeys(SupplierThrowable<Map<Tuple2<K1,K2>, Tuple6<T,U,V,W,X,Y>>> storageSupplier,
                           TriConsumerThrowable<Tuple2<K1,K2>, Tuple6<T,U,V,W,X,Y>, Tuple6<T,U,V,W,X,Y>> changesConsumer,
                           BiFunctionThrowable<K1, K2, Tuple6<T,U,V,W,X,Y>> valueFunction) {
            super(storageSupplier, changesConsumer, dual -> valueFunction.apply(dual.getFirst(), dual.getSecond()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 2 keys to Tuple of 6 elements
         */
        protected DualKeys(BiFunctionThrowable<K1, K2, Tuple6<T,U,V,W,X,Y>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple6 to keep 2 elements of the value mapped from a Tuple
     * of 3 different elements
     *
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple6 mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple6 mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple6 mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple6 mapped from the key
     * @param <X>   type of the fifth element of the Tuple.Tuple6 mapped from the key
     * @param <Y>   type of the sixth element of the Tuple.Tuple6 mapped from the key
     */
    public static class TripleKeys<K1,K2,K3, T,U,V,W,X,Y> extends HexaValuesRepository<Tuple3<K1,K2,K3>, T,U,V,W,X,Y>
            implements com.easyworks.repository.TripleKeys.HexaValues<K1,K2,K3, T,U,V,W,X,Y> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 3 keys to Tuple of 6 elements
         */
        protected TripleKeys(SupplierThrowable<Map<Tuple3<K1,K2,K3>, Tuple6<T,U,V,W,X,Y>>> storageSupplier,
                             TriConsumerThrowable<Tuple3<K1,K2,K3>, Tuple6<T,U,V,W,X,Y>, Tuple6<T,U,V,W,X,Y>> changesConsumer,
                             TriFunctionThrowable<K1, K2, K3, Tuple6<T,U,V,W,X,Y>> valueFunction) {
            super(storageSupplier, changesConsumer, triple -> valueFunction.apply(triple.getFirst(), triple.getSecond(), triple.getThird()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 3 keys to Tuple of 6 elements
         */
        protected TripleKeys(TriFunctionThrowable<K1, K2, K3, Tuple6<T,U,V,W,X,Y>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple6 to keep value of 2 elements mapped from a key, and keep the key as Tuple.Tuple7
     * of 4 different elements
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <K4>  type of fourth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple6 mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple6 mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple6 mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple6 mapped from the key
     * @param <X>   type of the fifth element of the Tuple.Tuple6 mapped from the key
     * @param <Y>   type of the sixth element of the Tuple.Tuple6 mapped from the key
     */
    public static class QuadKeys<K1,K2,K3,K4, T,U,V,W,X,Y> extends HexaValuesRepository<Tuple4<K1,K2,K3,K4>, T,U,V,W,X,Y>
            implements com.easyworks.repository.QuadKeys.HexaValues<K1,K2,K3,K4, T,U,V,W,X,Y> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 4 keys to Tuple of 6 elements
         */
        protected QuadKeys(SupplierThrowable<Map<Tuple4<K1,K2,K3,K4>, Tuple6<T,U,V,W,X,Y>>> storageSupplier,
                           TriConsumerThrowable<Tuple4<K1,K2,K3,K4>, Tuple6<T,U,V,W,X,Y>, Tuple6<T,U,V,W,X,Y>> changesConsumer,
                           QuadFunctionThrowable<K1,K2,K3,K4, Tuple6<T,U,V,W,X,Y>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(), tuple.getFourth()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 4 keys to Tuple of 6 elements
         */
        protected QuadKeys(QuadFunctionThrowable<K1,K2,K3,K4, Tuple6<T,U,V,W,X,Y>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple6 to keep value of 2 elements mapped from a key, and keep the key as Tuple.Tuple7
     * of 5 different elements
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <K4>  type of fourth element of the Key
     * @param <K5>  type of fifth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple6 mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple6 mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple6 mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple6 mapped from the key
     * @param <X>   type of the fifth element of the Tuple.Tuple6 mapped from the key
     * @param <Y>   type of the sixth element of the Tuple.Tuple6 mapped from the key
     */
    public static class PentaKeys<K1,K2,K3,K4,K5, T,U,V,W,X,Y> extends HexaValuesRepository<Tuple5<K1,K2,K3,K4,K5>, T,U,V,W,X,Y>
            implements com.easyworks.repository.PentaKeys.HexaValues<K1,K2,K3,K4,K5, T,U,V,W,X,Y> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 5 keys to Tuple of 6 elements
         */
        protected PentaKeys(SupplierThrowable<Map<Tuple5<K1,K2,K3,K4,K5>, Tuple6<T,U,V,W,X,Y>>> storageSupplier,
                            TriConsumerThrowable<Tuple5<K1,K2,K3,K4,K5>, Tuple6<T,U,V,W,X,Y>, Tuple6<T,U,V,W,X,Y>> changesConsumer,
                            PentaFunctionThrowable<K1,K2,K3,K4,K5, Tuple6<T,U,V,W,X,Y>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 5 keys to Tuple of 6 elements
         */
        protected PentaKeys(PentaFunctionThrowable<K1,K2,K3,K4,K5, Tuple6<T,U,V,W,X,Y>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple6 to keep value of 2 elements mapped from a key, and keep the key as Tuple.Tuple7
     * of 6 different elements
     *
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <K4>  type of fourth element of the Key
     * @param <K5>  type of fifth element of the Key
     * @param <K6>  type of sixth element of the Key
     *
     * @param <T>   type of the first element of the Tuple.Tuple6 mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple6 mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple6 mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple6 mapped from the key
     * @param <X>   type of the fifth element of the Tuple.Tuple6 mapped from the key
     * @param <Y>   type of the sixth element of the Tuple.Tuple6 mapped from the key
     */
    public static class HexaKeys<K1,K2,K3,K4,K5,K6, T,U,V,W,X,Y> extends HexaValuesRepository<Tuple6<K1,K2,K3,K4,K5,K6>, T,U,V,W,X,Y>
            implements com.easyworks.repository.HexaKeys.HexaValues<K1,K2,K3,K4,K5,K6, T,U,V,W,X,Y> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 6 keys to Tuple of 6 elements
         */
        protected HexaKeys(SupplierThrowable<Map<Tuple6<K1,K2,K3,K4,K5,K6>, Tuple6<T,U,V,W,X,Y>>> storageSupplier,
                           TriConsumerThrowable<Tuple6<K1,K2,K3,K4,K5,K6>, Tuple6<T,U,V,W,X,Y>, Tuple6<T,U,V,W,X,Y>> changesConsumer,
                           HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Tuple6<T,U,V,W,X,Y>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 6 keys to Tuple of 6 elements
         */
        protected HexaKeys(HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Tuple6<T,U,V,W,X,Y>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple6 to keep value of 2 elements mapped from a key, and keep the key as Tuple.Tuple7
     * of 7 different elements
     *
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <K4>  type of fourth element of the Key
     * @param <K5>  type of fifth element of the Key
     * @param <K6>  type of sixth element of the Key
     * @param <K7>  type of seventh element of the Key
     *
     * @param <T>   type of the first element of the Tuple.Tuple6 mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple6 mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple6 mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple6 mapped from the key
     * @param <X>   type of the fifth element of the Tuple.Tuple6 mapped from the key
     * @param <Y>   type of the sixth element of the Tuple.Tuple6 mapped from the key
     */
    public static class HeptaKeys<K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X,Y> extends HexaValuesRepository<Tuple7<K1,K2,K3,K4,K5,K6,K7>, T,U,V,W,X,Y>
            implements com.easyworks.repository.HeptaKeys.HexaValues<K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X,Y> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 7 keys to Tuple of 6 elements
         */
        protected HeptaKeys(SupplierThrowable<Map<Tuple7<K1,K2,K3,K4,K5,K6,K7>, Tuple6<T,U,V,W,X,Y>>> storageSupplier,
                            TriConsumerThrowable<Tuple7<K1,K2,K3,K4,K5,K6,K7>, Tuple6<T,U,V,W,X,Y>, Tuple6<T,U,V,W,X,Y>> changesConsumer,
                            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Tuple6<T,U,V,W,X,Y>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth(), tuple.getSeventh()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 7 keys to Tuple of 6 elements
         */
        protected HeptaKeys(HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Tuple6<T,U,V,W,X,Y>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }
}
