package com.easyworks.repository;

import com.easyworks.function.*;
import com.easyworks.tuple.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic repository use Tuple.Tuple2 as values to keep 1 element mapped from a specific key
 *
 * @param <TKey>    type of the key, can be any Type
 * @param <T>       type of the first element of the Tuple.Tuple2 value mapped from the key
 */
public class SingleValuesRepository<TKey, T>
        extends Repository<TKey, Tuple1<T>>
        implements SingleValues<TKey, T> {


    //region Factories to create repositories of keeping 1 element as the map value
    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map any key to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 1 key to Tuple of 1 element
     * @param <TKey>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return      Constructed Repository to map 1 key to 1 value as a Tuple
     */
    public static <TKey, T> SingleValuesRepository<TKey, T> fromKey(
            SupplierThrowable<Map<TKey, Tuple1<T>>> storageSupplier,
            TriConsumerThrowable<TKey, Tuple1<T>, Tuple1<T>> changesConsumer,
            FunctionThrowable<TKey, Tuple1<T>> valueFunction){
        return new SingleValuesRepository(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with given map factory
     *
     * @param valueFunction     Function to map 1 key to Tuple of 1 element
     * @param <TKey>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return      Constructed Repository to map 1 key to 1 value as a Tuple
     */
    public static <TKey, T> SingleValuesRepository<TKey, T> fromKey(
            FunctionThrowable<TKey, Tuple1<T>> valueFunction){
        return new SingleValuesRepository(valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 1 key to Tuple of 1 element
     * @param <K1>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return      Constructed Repository to map 1 key to 1 value as a Tuple
     */
    public static <K1, T> SingleKeys<K1, T> fromOneKeys(
            SupplierThrowable<Map<Tuple1<K1>, Tuple1<T>>> storageSupplier,
            TriConsumerThrowable<Tuple1<K1>, Tuple1<T>, Tuple1<T>> changesConsumer,
            FunctionThrowable<K1, Tuple1<T>> valueFunction){
        return new SingleKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 2 keys to Tuple of 1 element
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return      Constructed Repository to map 2 keys to 1 value as a Tuple
     */
    public static <K1,K2, T> DualKeys<K1,K2, T> fromTwoKeys(
            SupplierThrowable<Map<Tuple2<K1,K2>, Tuple1<T>>> storageSupplier,
            TriConsumerThrowable<Tuple2<K1,K2>, Tuple1<T>, Tuple1<T>> changesConsumer,
            BiFunctionThrowable<K1,K2, Tuple1<T>> valueFunction){
        return new DualKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 3 keys to Tuple of 1 element
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return      Constructed Repository to map 3 keys to 1 value as a Tuple
     */
    public static <K1,K2,K3, T> TripleKeys<K1,K2,K3, T> fromThreeKeys(
            SupplierThrowable<Map<Tuple3<K1,K2,K3>, Tuple1<T>>> storageSupplier,
            TriConsumerThrowable<Tuple3<K1,K2,K3>, Tuple1<T>, Tuple1<T>> changesConsumer,
            TriFunctionThrowable<K1,K2,K3, Tuple1<T>> valueFunction){
        return new TripleKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 4 keys to Tuple of 1 element
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return      Constructed Repository to map 4 keys to 1 value as a Tuple
     */
    public static <K1,K2,K3,K4, T> QuadKeys<K1,K2,K3,K4, T> fromFourKeys(
            SupplierThrowable<Map<Tuple4<K1,K2,K3,K4>, Tuple1<T>>> storageSupplier,
            TriConsumerThrowable<Tuple4<K1,K2,K3,K4>, Tuple1<T>, Tuple1<T>> changesConsumer,
            QuadFunctionThrowable<K1,K2,K3,K4, Tuple1<T>> valueFunction){
        return new QuadKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 5 keys to Tuple of 1 element
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return      Constructed Repository to map 5 keys to 1 value as a Tuple
     */
    public static <K1,K2,K3,K4,K5, T> PentaKeys<K1,K2,K3,K4,K5, T> fromFiveKeys(
            SupplierThrowable<Map<Tuple5<K1,K2,K3,K4,K5>, Tuple1<T>>> storageSupplier,
            TriConsumerThrowable<Tuple5<K1,K2,K3,K4,K5>, Tuple1<T>, Tuple1<T>> changesConsumer,
            PentaFunctionThrowable<K1,K2,K3,K4,K5, Tuple1<T>> valueFunction){
        return new PentaKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 6 keys to Tuple of 1 element
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <K6>  type of the sixth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return      Constructed Repository to map 6 keys to 1 value as a Tuple
     */
    public static <K1,K2,K3,K4,K5,K6, T> HexaKeys<K1,K2,K3,K4,K5,K6, T> fromSixKeys(
            SupplierThrowable<Map<Tuple6<K1,K2,K3,K4,K5,K6>, Tuple1<T>>> storageSupplier,
            TriConsumerThrowable<Tuple6<K1,K2,K3,K4,K5,K6>, Tuple1<T>, Tuple1<T>> changesConsumer,
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Tuple1<T>> valueFunction){
        return new HexaKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param storageSupplier   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 7 keys to Tuple of 1 element
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <K6>  type of the sixth element of the Key
     * @param <K7>  type of the seventh element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return      Constructed Repository to map 7 keys to 1 value as a Tuple
     */
    public static <K1,K2,K3,K4,K5,K6,K7, T> HeptaKeys<K1,K2,K3,K4,K5,K6,K7, T> fromSevenKeys(
            SupplierThrowable<Map<Tuple7<K1,K2,K3,K4,K5,K6,K7>, Tuple1<T>>> storageSupplier,
            TriConsumerThrowable<Tuple7<K1,K2,K3,K4,K5,K6,K7>, Tuple1<T>, Tuple1<T>> changesConsumer,
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Tuple1<T>> valueFunction){
        return new HeptaKeys(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 1 key to Tuple of 1 element
     * @param <K1>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return      Constructed Repository to map 1 key to 1 value as a Tuple
     */
    public static <K1, T> SingleKeys<K1, T> fromOneKeys(
            FunctionThrowable<K1, Tuple1<T>> valueFunction){
        return new SingleKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 2 keys to Tuple of 1 element
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return      Constructed Repository to map 2 keys to 1 value as a Tuple
     */
    public static <K1,K2, T> DualKeys<K1,K2, T> fromTwoKeys(
            BiFunctionThrowable<K1,K2, Tuple1<T>> valueFunction){
        return new DualKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 3 keys to Tuple of 1 element
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return      Constructed Repository to map 3 keys to 1 value as a Tuple
     */
    public static <K1,K2,K3, T> TripleKeys<K1,K2,K3, T> fromThreeKeys(
            TriFunctionThrowable<K1,K2,K3, Tuple1<T>> valueFunction){
        return new TripleKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 4 keys to Tuple of 1 element
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return      Constructed Repository to map 4 keys to 1 value as a Tuple
     */
    public static <K1,K2,K3,K4, T> QuadKeys<K1,K2,K3,K4, T> fromFourKeys(
            QuadFunctionThrowable<K1,K2,K3,K4, Tuple1<T>> valueFunction){
        return new QuadKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 5 keys to Tuple of 1 element
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return      Constructed Repository to map 5 keys to 1 value as a Tuple
     */
    public static <K1,K2,K3,K4,K5, T> PentaKeys<K1,K2,K3,K4,K5, T> fromFiveKeys(
            PentaFunctionThrowable<K1,K2,K3,K4,K5, Tuple1<T>> valueFunction){
        return new PentaKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 6 keys to Tuple of 1 element
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <K6>  type of the sixth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return      Constructed Repository to map 6 keys to 1 value as a Tuple
     */
    public static <K1,K2,K3,K4,K5,K6, T> HexaKeys<K1,K2,K3,K4,K5,K6, T> fromSixKeys(
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Tuple1<T>> valueFunction){
        return new HexaKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 7 keys to Tuple of 1 element
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <K6>  type of the sixth element of the Key
     * @param <K7>  type of the seventh element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return      Constructed Repository to map 7 keys to 1 value as a Tuple
     */
    public static <K1,K2,K3,K4,K5,K6,K7, T> HeptaKeys<K1,K2,K3,K4,K5,K6,K7, T> fromSevenKeys(
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Tuple1<T>> valueFunction){
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
     * @param valueFunction     Function to map 1 key to Tuple of 1 element
     */
    protected SingleValuesRepository(SupplierThrowable<Map<TKey, Tuple1<T>>> storageSupplier,
                                     TriConsumerThrowable<TKey, Tuple1<T>, Tuple1<T>> changesConsumer,
                                     FunctionThrowable<TKey, Tuple1<T>> valueFunction){
        super(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 1 key to Tuple of 1 element
     */
    protected SingleValuesRepository(FunctionThrowable<TKey, Tuple1<T>> valueFunction){
        this(HashMap::new, null, valueFunction);
    }
    //endregion

    /**
     * Get the strong-typed Tuple2&lt;T,U&gt; value mapped from the given key
     * @param key   key to retrieve the strong-typed Tuple2&lt;T,U&gt; value
     * @return      the strong-typed Tuple2&lt;T,U&gt; value mapped from the specific key
     */
    @Override
    public Tuple1<T> retrieve(TKey key) {
        return get(key, null);
    }

    /**
     * Generic repository use Tuple. to keep value of 2 elements mapped from a key of the Tuple
     * Notice: the actual type of the Key is Tuple.Tuple1 wrapping the actual value of <tt>K1</tt>
     *
     * @param <K1>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple1 value mapped from the key
     */
    public static class SingleKeys<K1, T> extends SingleValuesRepository<Tuple1<K1>, T>
            implements com.easyworks.repository.SingleKeys.SingleValues<K1, T> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 1 key to Tuple of 1 element
         */
        protected SingleKeys(SupplierThrowable<Map<Tuple1<K1>, Tuple1<T>>> storageSupplier,
                             TriConsumerThrowable<Tuple1<K1>, Tuple1<T>, Tuple1<T>> changesConsumer,
                             FunctionThrowable<K1, Tuple1<T>> valueFunction) {
            super(storageSupplier, changesConsumer, single -> valueFunction.apply(single.getFirst()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 1 key to Tuple of 1 element
         */
        protected SingleKeys(FunctionThrowable<K1, Tuple1<T>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple1 to keep value of 2 elements mapped from a key, and keep the key as Tuple.Tuple1
     * of 2 different elements
     *
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple1 mapped from the key
     */
    public static class DualKeys<K1,K2, T> extends SingleValuesRepository<Tuple2<K1,K2>, T>
            implements com.easyworks.repository.DualKeys.SingleValues<K1,K2, T> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 2 keys to Tuple of 1 element
         */
        protected DualKeys(SupplierThrowable<Map<Tuple2<K1,K2>, Tuple1<T>>> storageSupplier,
                           TriConsumerThrowable<Tuple2<K1,K2>, Tuple1<T>, Tuple1<T>> changesConsumer,
                           BiFunctionThrowable<K1, K2, Tuple1<T>> valueFunction) {
            super(storageSupplier, changesConsumer, dual -> valueFunction.apply(dual.getFirst(), dual.getSecond()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 2 keys to Tuple of 1 element
         */
        protected DualKeys(BiFunctionThrowable<K1, K2, Tuple1<T>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple1 to keep 2 elements of the value mapped from a Tuple
     * of 3 different elements
     *
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple1 mapped from the key
     */
    public static class TripleKeys<K1,K2,K3, T> extends SingleValuesRepository<Tuple3<K1,K2,K3>, T>
            implements com.easyworks.repository.TripleKeys.SingleValues<K1,K2,K3, T> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 3 keys to Tuple of 1 element
         */
        protected TripleKeys(SupplierThrowable<Map<Tuple3<K1,K2,K3>, Tuple1<T>>> storageSupplier,
                             TriConsumerThrowable<Tuple3<K1,K2,K3>, Tuple1<T>, Tuple1<T>> changesConsumer,
                             TriFunctionThrowable<K1, K2, K3, Tuple1<T>> valueFunction) {
            super(storageSupplier, changesConsumer, triple -> valueFunction.apply(triple.getFirst(), triple.getSecond(), triple.getThird()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 3 keys to Tuple of 1 element
         */
        protected TripleKeys(TriFunctionThrowable<K1, K2, K3, Tuple1<T>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple1 to keep value of 2 elements mapped from a key, and keep the key as Tuple.Tuple7
     * of 4 different elements
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <K4>  type of fourth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple1 mapped from the key
     */
    public static class QuadKeys<K1,K2,K3,K4, T> extends SingleValuesRepository<Tuple4<K1,K2,K3,K4>, T>
            implements com.easyworks.repository.QuadKeys.SingleValues<K1,K2,K3,K4, T> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 4 keys to Tuple of 1 element
         */
        protected QuadKeys(SupplierThrowable<Map<Tuple4<K1,K2,K3,K4>, Tuple1<T>>> storageSupplier,
                           TriConsumerThrowable<Tuple4<K1,K2,K3,K4>, Tuple1<T>, Tuple1<T>> changesConsumer,
                           QuadFunctionThrowable<K1,K2,K3,K4, Tuple1<T>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(), tuple.getFourth()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 4 keys to Tuple of 1 element
         */
        protected QuadKeys(QuadFunctionThrowable<K1,K2,K3,K4, Tuple1<T>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple1 to keep value of 2 elements mapped from a key, and keep the key as Tuple.Tuple7
     * of 5 different elements
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <K4>  type of fourth element of the Key
     * @param <K5>  type of fifth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple1 mapped from the key
     */
    public static class PentaKeys<K1,K2,K3,K4,K5, T> extends SingleValuesRepository<Tuple5<K1,K2,K3,K4,K5>, T>
            implements com.easyworks.repository.PentaKeys.SingleValues<K1,K2,K3,K4,K5, T> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 5 keys to Tuple of 1 element
         */
        protected PentaKeys(SupplierThrowable<Map<Tuple5<K1,K2,K3,K4,K5>, Tuple1<T>>> storageSupplier,
                            TriConsumerThrowable<Tuple5<K1,K2,K3,K4,K5>, Tuple1<T>, Tuple1<T>> changesConsumer,
                            PentaFunctionThrowable<K1,K2,K3,K4,K5, Tuple1<T>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 5 keys to Tuple of 1 element
         */
        protected PentaKeys(PentaFunctionThrowable<K1,K2,K3,K4,K5, Tuple1<T>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple1 to keep value of 2 elements mapped from a key, and keep the key as Tuple.Tuple7
     * of 6 different elements
     *
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <K4>  type of fourth element of the Key
     * @param <K5>  type of fifth element of the Key
     * @param <K6>  type of sixth element of the Key
     *
     * @param <T>   type of the first element of the Tuple.Tuple1 mapped from the key
     */
    public static class HexaKeys<K1,K2,K3,K4,K5,K6, T> extends SingleValuesRepository<Tuple6<K1,K2,K3,K4,K5,K6>, T>
            implements com.easyworks.repository.HexaKeys.SingleValues<K1,K2,K3,K4,K5,K6, T> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 6 keys to Tuple of 1 element
         */
        protected HexaKeys(SupplierThrowable<Map<Tuple6<K1,K2,K3,K4,K5,K6>, Tuple1<T>>> storageSupplier,
                           TriConsumerThrowable<Tuple6<K1,K2,K3,K4,K5,K6>, Tuple1<T>, Tuple1<T>> changesConsumer,
                           HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Tuple1<T>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 6 keys to Tuple of 1 element
         */
        protected HexaKeys(HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Tuple1<T>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple1 to keep value of 2 elements mapped from a key, and keep the key as Tuple.Tuple7
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
     * @param <T>   type of the first element of the Tuple.Tuple1 mapped from the key
     */
    public static class HeptaKeys<K1,K2,K3,K4,K5,K6,K7, T> extends SingleValuesRepository<Tuple7<K1,K2,K3,K4,K5,K6,K7>, T>
            implements com.easyworks.repository.HeptaKeys.SingleValues<K1,K2,K3,K4,K5,K6,K7, T> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param storageSupplier   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 7 keys to Tuple of 1 element
         */
        protected HeptaKeys(SupplierThrowable<Map<Tuple7<K1,K2,K3,K4,K5,K6,K7>, Tuple1<T>>> storageSupplier,
                            TriConsumerThrowable<Tuple7<K1,K2,K3,K4,K5,K6,K7>, Tuple1<T>, Tuple1<T>> changesConsumer,
                            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Tuple1<T>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth(), tuple.getSeventh()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 7 keys to Tuple of 1 element
         */
        protected HeptaKeys(HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Tuple1<T>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }
}
