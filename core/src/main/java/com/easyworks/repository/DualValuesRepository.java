package com.easyworks.repository;

import com.easyworks.function.*;
import com.easyworks.tuple.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic repository use Tuple.Dual as values to keep 2 elements mapped from a specific key
 *
 * @param <TKey>    type of the key, can be any Type
 * @param <T>       type of the first element of the Tuple.Dual value mapped from the key
 * @param <U>       type of the second element of the Tuple.Dual value mapped from the key
 */
public class DualValuesRepository<TKey, T, U>
        extends Repository<TKey, Dual<T,U>>
        implements DualValues<TKey, T, U> {


    //region Factories to create repositories of keeping two elements as the map value
    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map any key to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 1 key to Tuple of 2 elements
     * @param <TKey>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Dual value mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual value mapped from the key
     * @return      Constructed Repository to map 1 key to 2 values
     */
    public static <TKey,T,U> DualValuesRepository<TKey, T, U> fromKey(
            SupplierThrowable<Map<TKey, Dual<T,U>>> storageSupplier,
            TriConsumerThrowable<TKey, Dual<T,U>, Dual<T,U>> changesConsumer,
            FunctionThrowable<TKey, Dual<T,U>> valueFunction){
        return new DualValuesRepository(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with given map factory
     *
     * @param valueFunction     Function to map 1 key to Tuple of 2 elements
     * @param <TKey>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Dual value mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual value mapped from the key
     * @return      Constructed Repository to map 1 key to 2 values
     */
    public static <TKey,T,U> DualValuesRepository<TKey, T, U> fromKey(
            FunctionThrowable<TKey, Dual<T,U>> valueFunction){
        return new DualValuesRepository(valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 1 key to Tuple of 2 elements
     * @param <K1>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Dual value mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual value mapped from the key
     * @return      Constructed Repository to map 1 key to 2 values
     */
    public static <K1, T,U> SingleKeys<K1, T,U> fromOneKeys(
            SupplierThrowable<Map<Single<K1>, Dual<T,U>>> storageSupplier,
            TriConsumerThrowable<Single<K1>, Dual<T,U>, Dual<T,U>> changesConsumer,
            FunctionThrowable<K1, Dual<T, U>> valueFunction){
        return new SingleKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 2 keys to Tuple of 2 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <T>   type of the first element of the Tuple.Dual value mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual value mapped from the key
     * @return      Constructed Repository to map 2 keys to 2 values
     */
    public static <K1,K2, T,U> DualKeys<K1,K2, T,U> fromTwoKeys(
            SupplierThrowable<Map<Dual<K1,K2>, Dual<T,U>>> storageSupplier,
            TriConsumerThrowable<Dual<K1,K2>, Dual<T,U>, Dual<T,U>> changesConsumer,
            BiFunctionThrowable<K1,K2, Dual<T, U>> valueFunction){
        return new DualKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 3 keys to Tuple of 2 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <T>   type of the first element of the Tuple.Dual value mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual value mapped from the key
     * @return      Constructed Repository to map 3 keys to 2 values
     */
    public static <K1,K2,K3, T,U> TripleKeys<K1,K2,K3, T,U> fromThreeKeys(
            SupplierThrowable<Map<Triple<K1,K2,K3>, Dual<T,U>>> storageSupplier,
            TriConsumerThrowable<Triple<K1,K2,K3>, Dual<T,U>, Dual<T,U>> changesConsumer,
            TriFunctionThrowable<K1,K2,K3, Dual<T, U>> valueFunction){
        return new TripleKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 4 keys to Tuple of 2 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <T>   type of the first element of the Tuple.Dual value mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual value mapped from the key
     * @return      Constructed Repository to map 4 keys to 2 values
     */
    public static <K1,K2,K3,K4, T,U> QuadKeys<K1,K2,K3,K4, T,U> fromFourKeys(
            SupplierThrowable<Map<Quad<K1,K2,K3,K4>, Dual<T,U>>> storageSupplier,
            TriConsumerThrowable<Quad<K1,K2,K3,K4>, Dual<T,U>, Dual<T,U>> changesConsumer,
            QuadFunctionThrowable<K1,K2,K3,K4, Dual<T, U>> valueFunction){
        return new QuadKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 5 keys to Tuple of 2 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <T>   type of the first element of the Tuple.Dual value mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual value mapped from the key
     * @return      Constructed Repository to map 5 keys to 2 values
     */
    public static <K1,K2,K3,K4,K5, T,U> PentaKeys<K1,K2,K3,K4,K5, T,U> fromFiveKeys(
            SupplierThrowable<Map<Penta<K1,K2,K3,K4,K5>, Dual<T,U>>> storageSupplier,
            TriConsumerThrowable<Penta<K1,K2,K3,K4,K5>, Dual<T,U>, Dual<T,U>> changesConsumer,
            PentaFunctionThrowable<K1,K2,K3,K4,K5, Dual<T, U>> valueFunction){
        return new PentaKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 6 keys to Tuple of 2 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <K6>  type of the sixth element of the Key
     * @param <T>   type of the first element of the Tuple.Dual value mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual value mapped from the key
     * @return      Constructed Repository to map 6 keys to 2 values
     */
    public static <K1,K2,K3,K4,K5,K6, T,U> HexaKeys<K1,K2,K3,K4,K5,K6, T,U> fromSixKeys(
            SupplierThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Dual<T,U>>> storageSupplier,
            TriConsumerThrowable<Hexa<K1,K2,K3,K4,K5,K6>, Dual<T,U>, Dual<T,U>> changesConsumer,
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Dual<T, U>> valueFunction){
        return new HexaKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 7 keys to Tuple of 2 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <K6>  type of the sixth element of the Key
     * @param <K7>  type of the seventh element of the Key
     * @param <T>   type of the first element of the Tuple.Dual value mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual value mapped from the key
     * @return      Constructed Repository to map 7 keys to 2 values
     */
    public static <K1,K2,K3,K4,K5,K6,K7, T,U> HeptaKeys<K1,K2,K3,K4,K5,K6,K7, T,U> fromSevenKeys(
            SupplierThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Dual<T,U>>> storageSupplier,
            TriConsumerThrowable<Hepta<K1,K2,K3,K4,K5,K6,K7>, Dual<T,U>, Dual<T,U>> changesConsumer,
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Dual<T, U>> valueFunction){
        return new HeptaKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 1 key to Tuple of 2 elements
     * @param <K1>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Dual value mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual value mapped from the key
     * @return      Constructed Repository to map 1 key to 2 values
     */
    public static <K1, T,U> SingleKeys<K1, T,U> fromOneKeys(
            FunctionThrowable<K1, Dual<T, U>> valueFunction){
        return new SingleKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 2 keys to Tuple of 2 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <T>   type of the first element of the Tuple.Dual value mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual value mapped from the key
     * @return      Constructed Repository to map 2 keys to 2 values
     */
    public static <K1,K2, T,U> DualKeys<K1,K2, T,U> fromTwoKeys(
            BiFunctionThrowable<K1,K2, Dual<T, U>> valueFunction){
        return new DualKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 3 keys to Tuple of 2 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <T>   type of the first element of the Tuple.Dual value mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual value mapped from the key
     * @return      Constructed Repository to map 3 keys to 2 values
     */
    public static <K1,K2,K3, T,U> TripleKeys<K1,K2,K3, T,U> fromThreeKeys(
            TriFunctionThrowable<K1,K2,K3, Dual<T, U>> valueFunction){
        return new TripleKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 4 keys to Tuple of 2 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <T>   type of the first element of the Tuple.Dual value mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual value mapped from the key
     * @return      Constructed Repository to map 4 keys to 2 values
     */
    public static <K1,K2,K3,K4, T,U> QuadKeys<K1,K2,K3,K4, T,U> fromFourKeys(
            QuadFunctionThrowable<K1,K2,K3,K4, Dual<T, U>> valueFunction){
        return new QuadKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 5 keys to Tuple of 2 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <T>   type of the first element of the Tuple.Dual value mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual value mapped from the key
     * @return      Constructed Repository to map 5 keys to 2 values
     */
    public static <K1,K2,K3,K4,K5, T,U> PentaKeys<K1,K2,K3,K4,K5, T,U> fromFiveKeys(
            PentaFunctionThrowable<K1,K2,K3,K4,K5, Dual<T, U>> valueFunction){
        return new PentaKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 6 keys to Tuple of 2 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <K6>  type of the sixth element of the Key
     * @param <T>   type of the first element of the Tuple.Dual value mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual value mapped from the key
     * @return      Constructed Repository to map 6 keys to 2 values
     */
    public static <K1,K2,K3,K4,K5,K6, T,U> HexaKeys<K1,K2,K3,K4,K5,K6, T,U> fromSixKeys(
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Dual<T, U>> valueFunction){
        return new HexaKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 7 keys to Tuple of 2 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <K6>  type of the sixth element of the Key
     * @param <K7>  type of the seventh element of the Key
     * @param <T>   type of the first element of the Tuple.Dual value mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual value mapped from the key
     * @return      Constructed Repository to map 7 keys to 2 values
     */
    public static <K1,K2,K3,K4,K5,K6,K7, T,U> HeptaKeys<K1,K2,K3,K4,K5,K6,K7, T,U> fromSevenKeys(
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Dual<T, U>> valueFunction){
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
     * @param valueFunction     Function to map 1 key to Tuple of 2 elements
     */
    protected DualValuesRepository(SupplierThrowable<Map<TKey, Dual<T,U>>> storageSupplier,
                                   TriConsumerThrowable<TKey, Dual<T,U>, Dual<T,U>> changesConsumer,
                                   FunctionThrowable<TKey, Dual<T, U>> valueFunction){
        super(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 1 key to Tuple of 2 elements
     */
    protected DualValuesRepository(FunctionThrowable<TKey, Dual<T, U>> valueFunction){
        this(HashMap::new, null, valueFunction);
    }
    //endregion

    /**
     * Get the strong-typed Dual&lt;T,U&gt; value mapped from the given key
     * @param key   key to retrieve the strong-typed Dual&lt;T,U&gt; value
     * @return      the strong-typed Dual&lt;T,U&gt; value mapped from the specific key
     */
    @Override
    public Dual<T,U> retrieve(TKey key) {
        return get(key, null);
    }

    /**
     * Generic repository use Tuple. to keep value of 2 elements mapped from a key of the Tuple
     * Notice: the actual type of the Key is Tuple.Single wrapping the actual value of <tt>K1</tt>
     *
     * @param <K1>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Dual value mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual value mapped from the key
     */
    public static class SingleKeys<K1, T,U> extends DualValuesRepository<Single<K1>, T,U>
            implements com.easyworks.repository.SingleKeys.DualValues<K1, T, U> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 1 key to Tuple of 2 elements
         */
        protected SingleKeys(SupplierThrowable<Map<Single<K1>, Dual<T,U>>> storageSupplier,
                             TriConsumerThrowable<Single<K1>, Dual<T,U>, Dual<T,U>> changesConsumer,
                             FunctionThrowable<K1, Dual<T, U>> valueFunction) {
            super(storageSupplier, changesConsumer, single -> valueFunction.apply(single.getFirst()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 1 key to Tuple of 2 elements
         */
        protected SingleKeys(FunctionThrowable<K1, Dual<T, U>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Dual to keep value of 2 elements mapped from a key, and keep the key as Tuple.Dual
     * of 2 different elements
     *
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <T>   type of the first element of the Tuple.Dual mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual mapped from the key
     */
    public static class DualKeys<K1,K2, T,U> extends DualValuesRepository<Dual<K1,K2>, T,U>
            implements com.easyworks.repository.DualKeys.DualValues<K1,K2, T,U> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 2 keys to Tuple of 2 elements
         */
        protected DualKeys(SupplierThrowable<Map<Dual<K1,K2>, Dual<T,U>>> storageSupplier,
                           TriConsumerThrowable<Dual<K1,K2>, Dual<T,U>, Dual<T,U>> changesConsumer,
                           BiFunctionThrowable<K1, K2, Dual<T, U>> valueFunction) {
            super(storageSupplier, changesConsumer, dual -> valueFunction.apply(dual.getFirst(), dual.getSecond()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 2 keys to Tuple of 2 elements
         */
        protected DualKeys(BiFunctionThrowable<K1, K2, Dual<T, U>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Dual to keep 2 elements of the value mapped from a Tuple
     * of 3 different elements
     *
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <T>   type of the first element of the Tuple.Dual mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual mapped from the key
     */
    public static class TripleKeys<K1,K2,K3, T,U> extends DualValuesRepository<Triple<K1,K2,K3>, T,U>
            implements com.easyworks.repository.TripleKeys.DualValues<K1,K2,K3, T,U> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 3 keys to Tuple of 2 elements
         */
        protected TripleKeys(SupplierThrowable<Map<Triple<K1,K2,K3>, Dual<T,U>>> storageSupplier,
                             TriConsumerThrowable<Triple<K1,K2,K3>, Dual<T,U>, Dual<T,U>> changesConsumer,
                             TriFunctionThrowable<K1, K2, K3, Dual<T, U>> valueFunction) {
            super(storageSupplier, changesConsumer, triple -> valueFunction.apply(triple.getFirst(), triple.getSecond(), triple.getThird()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 3 keys to Tuple of 2 elements
         */
        protected TripleKeys(TriFunctionThrowable<K1, K2, K3, Dual<T, U>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Dual to keep value of 2 elements mapped from a key, and keep the key as Tuple.Hepta
     * of 4 different elements
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <K4>  type of fourth element of the Key
     * @param <T>   type of the first element of the Tuple.Dual mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual mapped from the key
     */
    public static class QuadKeys<K1,K2,K3,K4, T,U> extends DualValuesRepository<Quad<K1,K2,K3,K4>, T,U>
            implements com.easyworks.repository.QuadKeys.DualValues<K1,K2,K3,K4, T,U> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 4 keys to Tuple of 2 elements
         */
        protected QuadKeys(SupplierThrowable<Map<Quad<K1,K2,K3,K4>, Dual<T,U>>> storageSupplier,
                           TriConsumerThrowable<Quad<K1,K2,K3,K4>, Dual<T,U>, Dual<T,U>> changesConsumer,
                           QuadFunctionThrowable<K1,K2,K3,K4, Dual<T, U>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(), tuple.getFourth()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 4 keys to Tuple of 2 elements
         */
        protected QuadKeys(QuadFunctionThrowable<K1,K2,K3,K4, Dual<T, U>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Dual to keep value of 2 elements mapped from a key, and keep the key as Tuple.Hepta
     * of 5 different elements
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <K4>  type of fourth element of the Key
     * @param <K5>  type of fifth element of the Key
     * @param <T>   type of the first element of the Tuple.Dual mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual mapped from the key
     */
    public static class PentaKeys<K1,K2,K3,K4,K5, T,U> extends DualValuesRepository<Penta<K1,K2,K3,K4,K5>, T,U>
            implements com.easyworks.repository.PentaKeys.DualValues<K1,K2,K3,K4,K5, T,U> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 5 keys to Tuple of 2 elements
         */
        protected PentaKeys(SupplierThrowable<Map<Penta<K1,K2,K3,K4,K5>, Dual<T,U>>> storageSupplier,
                            TriConsumerThrowable<Penta<K1,K2,K3,K4,K5>, Dual<T,U>, Dual<T,U>> changesConsumer,
                            PentaFunctionThrowable<K1,K2,K3,K4,K5, Dual<T, U>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 5 keys to Tuple of 2 elements
         */
        protected PentaKeys(PentaFunctionThrowable<K1,K2,K3,K4,K5, Dual<T, U>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Dual to keep value of 2 elements mapped from a key, and keep the key as Tuple.Hepta
     * of 6 different elements
     *
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <K4>  type of fourth element of the Key
     * @param <K5>  type of fifth element of the Key
     * @param <K6>  type of sixth element of the Key
     *
     * @param <T>   type of the first element of the Tuple.Dual mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual mapped from the key
     */
    public static class HexaKeys<K1,K2,K3,K4,K5,K6, T,U> extends DualValuesRepository<Hexa<K1,K2,K3,K4,K5,K6>, T,U>
            implements com.easyworks.repository.HexaKeys.DualValues<K1,K2,K3,K4,K5,K6, T,U> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 6 keys to Tuple of 2 elements
         */
        protected HexaKeys(SupplierThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Dual<T,U>>> storageSupplier,
                           TriConsumerThrowable<Hexa<K1,K2,K3,K4,K5,K6>, Dual<T,U>, Dual<T,U>> changesConsumer,
                           HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Dual<T, U>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 6 keys to Tuple of 2 elements
         */
        protected HexaKeys(HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Dual<T, U>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Dual to keep value of 2 elements mapped from a key, and keep the key as Tuple.Hepta
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
     * @param <T>   type of the first element of the Tuple.Dual mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual mapped from the key
     */
    public static class HeptaKeys<K1,K2,K3,K4,K5,K6,K7, T,U> extends DualValuesRepository<Hepta<K1,K2,K3,K4,K5,K6,K7>, T,U>
            implements com.easyworks.repository.HeptaKeys.DualValues<K1,K2,K3,K4,K5,K6,K7, T,U> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 7 keys to Tuple of 2 elements
         */
        protected HeptaKeys(SupplierThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Dual<T,U>>> storageSupplier,
                            TriConsumerThrowable<Hepta<K1,K2,K3,K4,K5,K6,K7>, Dual<T,U>, Dual<T,U>> changesConsumer,
                            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Dual<T, U>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth(), tuple.getSeventh()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 7 keys to Tuple of 2 elements
         */
        protected HeptaKeys(HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Dual<T, U>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }
}
