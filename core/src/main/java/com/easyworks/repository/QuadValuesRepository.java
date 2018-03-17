package com.easyworks.repository;

import com.easyworks.function.*;
import com.easyworks.tuple.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic repository use Tuple.Quad as values to keep 4 elements mapped from a specific key
 *
 * @param <TKey>    type of the key, can be any Type
 * @param <T>       type of the first element of the Tuple.Quad value mapped from the key
 * @param <U>       type of the second element of the Tuple.Quad value mapped from the key
 * @param <V>       type of the third element of the Tuple.Quad value mapped from the key
 * @param <W>       type of the fourth element of the Tuple.Quad value mapped from the key
 */
public class QuadValuesRepository<TKey, T,U,V,W>
        extends Repository<TKey, Quad<T,U,V,W>>
        implements QuadValues<TKey, T,U,V,W> {


    //region Factories to create repositories of keeping 4 elements as the map value
    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map any key to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 1 key to Tuple of 4 elements
     * @param <TKey>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Quad value mapped from the key
     * @param <U>   type of the second element of the Tuple.Quad value mapped from the key
     * @param <V>   type of the third element of the Tuple.Quad value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Quad value mapped from the key
     * @return      Constructed Repository to map 1 key to 4 values as a Tuple
     */
    public static <TKey, T,U,V,W> QuadValuesRepository<TKey, T,U,V,W> fromKey(
            SupplierThrowable<Map<TKey, Quad<T,U,V,W>>> storageSupplier,
            TriConsumerThrowable<TKey, Quad<T,U,V,W>, Quad<T,U,V,W>> changesConsumer,
            FunctionThrowable<TKey, Quad<T,U,V,W>> valueFunction){
        return new QuadValuesRepository(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with given map factory
     *
     * @param valueFunction     Function to map 1 key to Tuple of 4 elements
     * @param <TKey>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Quad value mapped from the key
     * @param <U>   type of the second element of the Tuple.Quad value mapped from the key
     * @param <V>   type of the third element of the Tuple.Quad value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Quad value mapped from the key
     * @return      Constructed Repository to map 1 key to 4 values as a Tuple
     */
    public static <TKey, T,U,V,W> QuadValuesRepository<TKey, T,U,V,W> fromKey(
            FunctionThrowable<TKey, Quad<T,U,V,W>> valueFunction){
        return new QuadValuesRepository(valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 1 key to Tuple of 4 elements
     * @param <K1>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Quad value mapped from the key
     * @param <U>   type of the second element of the Tuple.Quad value mapped from the key
     * @param <V>   type of the third element of the Tuple.Quad value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Quad value mapped from the key
     * @return      Constructed Repository to map 1 key to 4 values as a Tuple
     */
    public static <K1, T,U,V,W> SingleKeys<K1, T,U,V,W> fromOneKeys(
            SupplierThrowable<Map<Single<K1>, Quad<T,U,V,W>>> storageSupplier,
            TriConsumerThrowable<Single<K1>, Quad<T,U,V,W>, Quad<T,U,V,W>> changesConsumer,
            FunctionThrowable<K1, Quad<T,U,V,W>> valueFunction){
        return new SingleKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 2 keys to Tuple of 4 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <T>   type of the first element of the Tuple.Quad value mapped from the key
     * @param <U>   type of the second element of the Tuple.Quad value mapped from the key
     * @param <V>   type of the third element of the Tuple.Quad value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Quad value mapped from the key
     * @return      Constructed Repository to map 2 keys to 4 values as a Tuple
     */
    public static <K1,K2, T,U,V,W> DualKeys<K1,K2, T,U,V,W> fromTwoKeys(
            SupplierThrowable<Map<Dual<K1,K2>, Quad<T,U,V,W>>> storageSupplier,
            TriConsumerThrowable<Dual<K1,K2>, Quad<T,U,V,W>, Quad<T,U,V,W>> changesConsumer,
            BiFunctionThrowable<K1,K2, Quad<T,U,V,W>> valueFunction){
        return new DualKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 3 keys to Tuple of 4 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <T>   type of the first element of the Tuple.Quad value mapped from the key
     * @param <U>   type of the second element of the Tuple.Quad value mapped from the key
     * @param <V>   type of the third element of the Tuple.Quad value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Quad value mapped from the key
     * @return      Constructed Repository to map 3 keys to 4 values as a Tuple
     */
    public static <K1,K2,K3, T,U,V,W> TripleKeys<K1,K2,K3, T,U,V,W> fromThreeKeys(
            SupplierThrowable<Map<Triple<K1,K2,K3>, Quad<T,U,V,W>>> storageSupplier,
            TriConsumerThrowable<Triple<K1,K2,K3>, Quad<T,U,V,W>, Quad<T,U,V,W>> changesConsumer,
            TriFunctionThrowable<K1,K2,K3, Quad<T,U,V,W>> valueFunction){
        return new TripleKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 4 keys to Tuple of 4 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <T>   type of the first element of the Tuple.Quad value mapped from the key
     * @param <U>   type of the second element of the Tuple.Quad value mapped from the key
     * @param <V>   type of the third element of the Tuple.Quad value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Quad value mapped from the key
     * @return      Constructed Repository to map 4 keys to 4 values as a Tuple
     */
    public static <K1,K2,K3,K4, T,U,V,W> QuadKeys<K1,K2,K3,K4, T,U,V,W> fromFourKeys(
            SupplierThrowable<Map<Quad<K1,K2,K3,K4>, Quad<T,U,V,W>>> storageSupplier,
            TriConsumerThrowable<Quad<K1,K2,K3,K4>, Quad<T,U,V,W>, Quad<T,U,V,W>> changesConsumer,
            QuadFunctionThrowable<K1,K2,K3,K4, Quad<T,U,V,W>> valueFunction){
        return new QuadKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 5 keys to Tuple of 4 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <T>   type of the first element of the Tuple.Quad value mapped from the key
     * @param <U>   type of the second element of the Tuple.Quad value mapped from the key
     * @param <V>   type of the third element of the Tuple.Quad value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Quad value mapped from the key
     * @return      Constructed Repository to map 5 keys to 4 values as a Tuple
     */
    public static <K1,K2,K3,K4,K5, T,U,V,W> PentaKeys<K1,K2,K3,K4,K5, T,U,V,W> fromFiveKeys(
            SupplierThrowable<Map<Penta<K1,K2,K3,K4,K5>, Quad<T,U,V,W>>> storageSupplier,
            TriConsumerThrowable<Penta<K1,K2,K3,K4,K5>, Quad<T,U,V,W>, Quad<T,U,V,W>> changesConsumer,
            PentaFunctionThrowable<K1,K2,K3,K4,K5, Quad<T,U,V,W>> valueFunction){
        return new PentaKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 6 keys to Tuple of 4 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <K6>  type of the sixth element of the Key
     * @param <T>   type of the first element of the Tuple.Quad value mapped from the key
     * @param <U>   type of the second element of the Tuple.Quad value mapped from the key
     * @param <V>   type of the third element of the Tuple.Quad value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Quad value mapped from the key
     * @return      Constructed Repository to map 6 keys to 4 values as a Tuple
     */
    public static <K1,K2,K3,K4,K5,K6, T,U,V,W> HexaKeys<K1,K2,K3,K4,K5,K6, T,U,V,W> fromSixKeys(
            SupplierThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Quad<T,U,V,W>>> storageSupplier,
            TriConsumerThrowable<Hexa<K1,K2,K3,K4,K5,K6>, Quad<T,U,V,W>, Quad<T,U,V,W>> changesConsumer,
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Quad<T,U,V,W>> valueFunction){
        return new HexaKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 7 keys to Tuple of 4 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <K6>  type of the sixth element of the Key
     * @param <K7>  type of the seventh element of the Key
     * @param <T>   type of the first element of the Tuple.Quad value mapped from the key
     * @param <U>   type of the second element of the Tuple.Quad value mapped from the key
     * @param <V>   type of the third element of the Tuple.Quad value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Quad value mapped from the key
     * @return      Constructed Repository to map 7 keys to 4 values as a Tuple
     */
    public static <K1,K2,K3,K4,K5,K6,K7, T,U,V,W> HeptaKeys<K1,K2,K3,K4,K5,K6,K7, T,U,V,W> fromSevenKeys(
            SupplierThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Quad<T,U,V,W>>> storageSupplier,
            TriConsumerThrowable<Hepta<K1,K2,K3,K4,K5,K6,K7>, Quad<T,U,V,W>, Quad<T,U,V,W>> changesConsumer,
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Quad<T,U,V,W>> valueFunction){
        return new HeptaKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 1 key to Tuple of 4 elements
     * @param <K1>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Quad value mapped from the key
     * @param <U>   type of the second element of the Tuple.Quad value mapped from the key
     * @param <V>   type of the third element of the Tuple.Quad value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Quad value mapped from the key
     * @return      Constructed Repository to map 1 key to 4 values as a Tuple
     */
    public static <K1, T,U,V,W> SingleKeys<K1, T,U,V,W> fromOneKeys(
            FunctionThrowable<K1, Quad<T,U,V,W>> valueFunction){
        return new SingleKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 2 keys to Tuple of 4 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <T>   type of the first element of the Tuple.Quad value mapped from the key
     * @param <U>   type of the second element of the Tuple.Quad value mapped from the key
     * @param <V>   type of the third element of the Tuple.Quad value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Quad value mapped from the key
     * @return      Constructed Repository to map 2 keys to 4 values as a Tuple
     */
    public static <K1,K2, T,U,V,W> DualKeys<K1,K2, T,U,V,W> fromTwoKeys(
            BiFunctionThrowable<K1,K2, Quad<T,U,V,W>> valueFunction){
        return new DualKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 3 keys to Tuple of 4 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <T>   type of the first element of the Tuple.Quad value mapped from the key
     * @param <U>   type of the second element of the Tuple.Quad value mapped from the key
     * @param <V>   type of the third element of the Tuple.Quad value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Quad value mapped from the key
     * @return      Constructed Repository to map 3 keys to 4 values as a Tuple
     */
    public static <K1,K2,K3, T,U,V,W> TripleKeys<K1,K2,K3, T,U,V,W> fromThreeKeys(
            TriFunctionThrowable<K1,K2,K3, Quad<T,U,V,W>> valueFunction){
        return new TripleKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 4 keys to Tuple of 4 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <T>   type of the first element of the Tuple.Quad value mapped from the key
     * @param <U>   type of the second element of the Tuple.Quad value mapped from the key
     * @param <V>   type of the third element of the Tuple.Quad value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Quad value mapped from the key
     * @return      Constructed Repository to map 4 keys to 4 values as a Tuple
     */
    public static <K1,K2,K3,K4, T,U,V,W> QuadKeys<K1,K2,K3,K4, T,U,V,W> fromFourKeys(
            QuadFunctionThrowable<K1,K2,K3,K4, Quad<T,U,V,W>> valueFunction){
        return new QuadKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 5 keys to Tuple of 4 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <T>   type of the first element of the Tuple.Quad value mapped from the key
     * @param <U>   type of the second element of the Tuple.Quad value mapped from the key
     * @param <V>   type of the third element of the Tuple.Quad value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Quad value mapped from the key
     * @return      Constructed Repository to map 5 keys to 4 values as a Tuple
     */
    public static <K1,K2,K3,K4,K5, T,U,V,W> PentaKeys<K1,K2,K3,K4,K5, T,U,V,W> fromFiveKeys(
            PentaFunctionThrowable<K1,K2,K3,K4,K5, Quad<T,U,V,W>> valueFunction){
        return new PentaKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 6 keys to Tuple of 4 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <K6>  type of the sixth element of the Key
     * @param <T>   type of the first element of the Tuple.Quad value mapped from the key
     * @param <U>   type of the second element of the Tuple.Quad value mapped from the key
     * @param <V>   type of the third element of the Tuple.Quad value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Quad value mapped from the key
     * @return      Constructed Repository to map 6 keys to 4 values as a Tuple
     */
    public static <K1,K2,K3,K4,K5,K6, T,U,V,W> HexaKeys<K1,K2,K3,K4,K5,K6, T,U,V,W> fromSixKeys(
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Quad<T,U,V,W>> valueFunction){
        return new HexaKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 7 keys to Tuple of 4 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <K6>  type of the sixth element of the Key
     * @param <K7>  type of the seventh element of the Key
     * @param <T>   type of the first element of the Tuple.Quad value mapped from the key
     * @param <U>   type of the second element of the Tuple.Quad value mapped from the key
     * @param <V>   type of the third element of the Tuple.Quad value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Quad value mapped from the key
     * @return      Constructed Repository to map 7 keys to 4 values as a Tuple
     */
    public static <K1,K2,K3,K4,K5,K6,K7, T,U,V,W> HeptaKeys<K1,K2,K3,K4,K5,K6,K7, T,U,V,W> fromSevenKeys(
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Quad<T,U,V,W>> valueFunction){
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
     * @param valueFunction     Function to map 1 key to Tuple of 4 elements
     */
    protected QuadValuesRepository(SupplierThrowable<Map<TKey, Quad<T,U,V,W>>> storageSupplier,
                                   TriConsumerThrowable<TKey, Quad<T,U,V,W>, Quad<T,U,V,W>> changesConsumer,
                                   FunctionThrowable<TKey, Quad<T,U,V,W>> valueFunction){
        super(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 1 key to Tuple of 4 elements
     */
    protected QuadValuesRepository(FunctionThrowable<TKey, Quad<T,U,V,W>> valueFunction){
        this(HashMap::new, null, valueFunction);
    }
    //endregion

    /**
     * Get the strong-typed Dual&lt;T,U&gt; value mapped from the given key
     * @param key   key to retrieve the strong-typed Dual&lt;T,U&gt; value
     * @return      the strong-typed Dual&lt;T,U&gt; value mapped from the specific key
     */
    @Override
    public Quad<T,U,V,W> retrieve(TKey key) {
        return get(key, null);
    }

    /**
     * Generic repository use Tuple. to keep value of 2 elements mapped from a key of the Tuple
     * Notice: the actual type of the Key is Tuple.Single wrapping the actual value of <tt>K1</tt>
     *
     * @param <K1>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Quad value mapped from the key
     * @param <U>   type of the second element of the Tuple.Quad value mapped from the key
     * @param <V>   type of the third element of the Tuple.Quad value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Quad value mapped from the key
     */
    public static class SingleKeys<K1, T,U,V,W> extends QuadValuesRepository<Single<K1>, T,U,V,W>
            implements com.easyworks.repository.SingleKeys.QuadValues<K1, T,U,V,W> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 1 key to Tuple of 4 elements
         */
        protected SingleKeys(SupplierThrowable<Map<Single<K1>, Quad<T,U,V,W>>> storageSupplier,
                             TriConsumerThrowable<Single<K1>, Quad<T,U,V,W>, Quad<T,U,V,W>> changesConsumer,
                             FunctionThrowable<K1, Quad<T,U,V,W>> valueFunction) {
            super(storageSupplier, changesConsumer, single -> valueFunction.apply(single.getFirst()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 1 key to Tuple of 4 elements
         */
        protected SingleKeys(FunctionThrowable<K1, Quad<T,U,V,W>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Quad to keep value of 2 elements mapped from a key, and keep the key as Tuple.Quad
     * of 2 different elements
     *
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <T>   type of the first element of the Tuple.Quad mapped from the key
     * @param <U>   type of the second element of the Tuple.Quad mapped from the key
     * @param <V>   type of the third element of the Tuple.Quad mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Quad mapped from the key
     */
    public static class DualKeys<K1,K2, T,U,V,W> extends QuadValuesRepository<Dual<K1,K2>, T,U,V,W>
            implements com.easyworks.repository.DualKeys.QuadValues<K1,K2, T,U,V,W> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 2 keys to Tuple of 4 elements
         */
        protected DualKeys(SupplierThrowable<Map<Dual<K1,K2>, Quad<T,U,V,W>>> storageSupplier,
                           TriConsumerThrowable<Dual<K1,K2>, Quad<T,U,V,W>, Quad<T,U,V,W>> changesConsumer,
                           BiFunctionThrowable<K1, K2, Quad<T,U,V,W>> valueFunction) {
            super(storageSupplier, changesConsumer, dual -> valueFunction.apply(dual.getFirst(), dual.getSecond()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 2 keys to Tuple of 4 elements
         */
        protected DualKeys(BiFunctionThrowable<K1, K2, Quad<T,U,V,W>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Quad to keep 2 elements of the value mapped from a Tuple
     * of 3 different elements
     *
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <T>   type of the first element of the Tuple.Quad mapped from the key
     * @param <U>   type of the second element of the Tuple.Quad mapped from the key
     * @param <V>   type of the third element of the Tuple.Quad mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Quad mapped from the key
     */
    public static class TripleKeys<K1,K2,K3, T,U,V,W> extends QuadValuesRepository<Triple<K1,K2,K3>, T,U,V,W>
            implements com.easyworks.repository.TripleKeys.QuadValues<K1,K2,K3, T,U,V,W> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 3 keys to Tuple of 4 elements
         */
        protected TripleKeys(SupplierThrowable<Map<Triple<K1,K2,K3>, Quad<T,U,V,W>>> storageSupplier,
                             TriConsumerThrowable<Triple<K1,K2,K3>, Quad<T,U,V,W>, Quad<T,U,V,W>> changesConsumer,
                             TriFunctionThrowable<K1, K2, K3, Quad<T,U,V,W>> valueFunction) {
            super(storageSupplier, changesConsumer, triple -> valueFunction.apply(triple.getFirst(), triple.getSecond(), triple.getThird()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 3 keys to Tuple of 4 elements
         */
        protected TripleKeys(TriFunctionThrowable<K1, K2, K3, Quad<T,U,V,W>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Quad to keep value of 2 elements mapped from a key, and keep the key as Tuple.Hepta
     * of 4 different elements
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <K4>  type of fourth element of the Key
     * @param <T>   type of the first element of the Tuple.Quad mapped from the key
     * @param <U>   type of the second element of the Tuple.Quad mapped from the key
     * @param <V>   type of the third element of the Tuple.Quad mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Quad mapped from the key
     */
    public static class QuadKeys<K1,K2,K3,K4, T,U,V,W> extends QuadValuesRepository<Quad<K1,K2,K3,K4>, T,U,V,W>
            implements com.easyworks.repository.QuadKeys.QuadValues<K1,K2,K3,K4, T,U,V,W> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 4 keys to Tuple of 4 elements
         */
        protected QuadKeys(SupplierThrowable<Map<Quad<K1,K2,K3,K4>, Quad<T,U,V,W>>> storageSupplier,
                           TriConsumerThrowable<Quad<K1,K2,K3,K4>, Quad<T,U,V,W>, Quad<T,U,V,W>> changesConsumer,
                           QuadFunctionThrowable<K1,K2,K3,K4, Quad<T,U,V,W>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(), tuple.getFourth()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 4 keys to Tuple of 4 elements
         */
        protected QuadKeys(QuadFunctionThrowable<K1,K2,K3,K4, Quad<T,U,V,W>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Quad to keep value of 2 elements mapped from a key, and keep the key as Tuple.Hepta
     * of 5 different elements
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <K4>  type of fourth element of the Key
     * @param <K5>  type of fifth element of the Key
     * @param <T>   type of the first element of the Tuple.Quad mapped from the key
     * @param <U>   type of the second element of the Tuple.Quad mapped from the key
     * @param <V>   type of the third element of the Tuple.Quad mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Quad mapped from the key
     */
    public static class PentaKeys<K1,K2,K3,K4,K5, T,U,V,W> extends QuadValuesRepository<Penta<K1,K2,K3,K4,K5>, T,U,V,W>
            implements com.easyworks.repository.PentaKeys.QuadValues<K1,K2,K3,K4,K5, T,U,V,W> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 5 keys to Tuple of 4 elements
         */
        protected PentaKeys(SupplierThrowable<Map<Penta<K1,K2,K3,K4,K5>, Quad<T,U,V,W>>> storageSupplier,
                            TriConsumerThrowable<Penta<K1,K2,K3,K4,K5>, Quad<T,U,V,W>, Quad<T,U,V,W>> changesConsumer,
                            PentaFunctionThrowable<K1,K2,K3,K4,K5, Quad<T,U,V,W>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 5 keys to Tuple of 4 elements
         */
        protected PentaKeys(PentaFunctionThrowable<K1,K2,K3,K4,K5, Quad<T,U,V,W>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Quad to keep value of 2 elements mapped from a key, and keep the key as Tuple.Hepta
     * of 6 different elements
     *
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <K4>  type of fourth element of the Key
     * @param <K5>  type of fifth element of the Key
     * @param <K6>  type of sixth element of the Key
     *
     * @param <T>   type of the first element of the Tuple.Quad mapped from the key
     * @param <U>   type of the second element of the Tuple.Quad mapped from the key
     * @param <V>   type of the third element of the Tuple.Quad mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Quad mapped from the key
     */
    public static class HexaKeys<K1,K2,K3,K4,K5,K6, T,U,V,W> extends QuadValuesRepository<Hexa<K1,K2,K3,K4,K5,K6>, T,U,V,W>
            implements com.easyworks.repository.HexaKeys.QuadValues<K1,K2,K3,K4,K5,K6, T,U,V,W> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 6 keys to Tuple of 4 elements
         */
        protected HexaKeys(SupplierThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Quad<T,U,V,W>>> storageSupplier,
                           TriConsumerThrowable<Hexa<K1,K2,K3,K4,K5,K6>, Quad<T,U,V,W>, Quad<T,U,V,W>> changesConsumer,
                           HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Quad<T,U,V,W>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 6 keys to Tuple of 4 elements
         */
        protected HexaKeys(HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Quad<T,U,V,W>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Quad to keep value of 2 elements mapped from a key, and keep the key as Tuple.Hepta
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
     * @param <T>   type of the first element of the Tuple.Quad mapped from the key
     * @param <U>   type of the second element of the Tuple.Quad mapped from the key
     * @param <V>   type of the third element of the Tuple.Quad mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Quad mapped from the key
     */
    public static class HeptaKeys<K1,K2,K3,K4,K5,K6,K7, T,U,V,W> extends QuadValuesRepository<Hepta<K1,K2,K3,K4,K5,K6,K7>, T,U,V,W>
            implements com.easyworks.repository.HeptaKeys.QuadValues<K1,K2,K3,K4,K5,K6,K7, T,U,V,W> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 7 keys to Tuple of 4 elements
         */
        protected HeptaKeys(SupplierThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Quad<T,U,V,W>>> storageSupplier,
                            TriConsumerThrowable<Hepta<K1,K2,K3,K4,K5,K6,K7>, Quad<T,U,V,W>, Quad<T,U,V,W>> changesConsumer,
                            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Quad<T,U,V,W>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth(), tuple.getSeventh()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 7 keys to Tuple of 4 elements
         */
        protected HeptaKeys(HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Quad<T,U,V,W>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }
}
