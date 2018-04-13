package com.easyworks.repository;

import com.easyworks.function.*;
import com.easyworks.tuple.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic repository use Tuple.Tuple2 as values to keep 2 elements mapped from a specific key
 *
 * @param <TKey>    type of the key, can be any Type
 * @param <T>       type of the first element of the Tuple.Tuple2 value mapped from the key
 * @param <U>       type of the second element of the Tuple.Tuple2 value mapped from the key
 */
public class DualValuesRepository<TKey, T, U>
        extends Repository<TKey, Tuple2<T,U>>
        implements DualValues<TKey, T, U> {


    //region Factories to create repositories of keeping two elements as the map value
    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map any key to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 1 key to Tuple of 2 elements
     * @param <TKey>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple2 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple2 value mapped from the key
     * @return      Constructed Repository to map 1 key to 2 values
     */
    public static <TKey,T,U> DualValuesRepository<TKey, T, U> fromKey(
            Map<TKey, Tuple2<T,U>> map,
            TriConsumerThrowable<TKey, Tuple2<T,U>, Tuple2<T,U>> changesConsumer,
            FunctionThrowable<TKey, Tuple2<T,U>> valueFunction){
        return new DualValuesRepository(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with given map factory
     *
     * @param valueFunction     Function to map 1 key to Tuple of 2 elements
     * @param <TKey>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple2 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple2 value mapped from the key
     * @return      Constructed Repository to map 1 key to 2 values
     */
    public static <TKey,T,U> DualValuesRepository<TKey, T, U> fromKey(
            FunctionThrowable<TKey, Tuple2<T,U>> valueFunction){
        return new DualValuesRepository(valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 1 key to Tuple of 2 elements
     * @param <K1>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple2 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple2 value mapped from the key
     * @return      Constructed Repository to map 1 key to 2 values
     */
    public static <K1, T,U> SingleKeys<K1, T,U> fromOneKeys(
            Map<Tuple1<K1>, Tuple2<T,U>> map,
            TriConsumerThrowable<Tuple1<K1>, Tuple2<T,U>, Tuple2<T,U>> changesConsumer,
            FunctionThrowable<K1, Tuple2<T, U>> valueFunction){
        return new SingleKeys(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 2 keys to Tuple of 2 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple2 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple2 value mapped from the key
     * @return      Constructed Repository to map 2 keys to 2 values
     */
    public static <K1,K2, T,U> DualKeys<K1,K2, T,U> fromTwoKeys(
            Map<Tuple2<K1,K2>, Tuple2<T,U>> map,
            TriConsumerThrowable<Tuple2<K1,K2>, Tuple2<T,U>, Tuple2<T,U>> changesConsumer,
            BiFunctionThrowable<K1,K2, Tuple2<T, U>> valueFunction){
        return new DualKeys(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 3 keys to Tuple of 2 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple2 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple2 value mapped from the key
     * @return      Constructed Repository to map 3 keys to 2 values
     */
    public static <K1,K2,K3, T,U> TripleKeys<K1,K2,K3, T,U> fromThreeKeys(
            Map<Tuple3<K1,K2,K3>, Tuple2<T,U>> map,
            TriConsumerThrowable<Tuple3<K1,K2,K3>, Tuple2<T,U>, Tuple2<T,U>> changesConsumer,
            TriFunctionThrowable<K1,K2,K3, Tuple2<T, U>> valueFunction){
        return new TripleKeys(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 4 keys to Tuple of 2 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple2 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple2 value mapped from the key
     * @return      Constructed Repository to map 4 keys to 2 values
     */
    public static <K1,K2,K3,K4, T,U> QuadKeys<K1,K2,K3,K4, T,U> fromFourKeys(
            Map<Tuple4<K1,K2,K3,K4>, Tuple2<T,U>> map,
            TriConsumerThrowable<Tuple4<K1,K2,K3,K4>, Tuple2<T,U>, Tuple2<T,U>> changesConsumer,
            QuadFunctionThrowable<K1,K2,K3,K4, Tuple2<T, U>> valueFunction){
        return new QuadKeys(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 5 keys to Tuple of 2 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple2 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple2 value mapped from the key
     * @return      Constructed Repository to map 5 keys to 2 values
     */
    public static <K1,K2,K3,K4,K5, T,U> PentaKeys<K1,K2,K3,K4,K5, T,U> fromFiveKeys(
            Map<Tuple5<K1,K2,K3,K4,K5>, Tuple2<T,U>> map,
            TriConsumerThrowable<Tuple5<K1,K2,K3,K4,K5>, Tuple2<T,U>, Tuple2<T,U>> changesConsumer,
            PentaFunctionThrowable<K1,K2,K3,K4,K5, Tuple2<T, U>> valueFunction){
        return new PentaKeys(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 6 keys to Tuple of 2 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <K6>  type of the sixth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple2 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple2 value mapped from the key
     * @return      Constructed Repository to map 6 keys to 2 values
     */
    public static <K1,K2,K3,K4,K5,K6, T,U> HexaKeys<K1,K2,K3,K4,K5,K6, T,U> fromSixKeys(
            Map<Tuple6<K1,K2,K3,K4,K5,K6>, Tuple2<T,U>> map,
            TriConsumerThrowable<Tuple6<K1,K2,K3,K4,K5,K6>, Tuple2<T,U>, Tuple2<T,U>> changesConsumer,
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Tuple2<T, U>> valueFunction){
        return new HexaKeys(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 7 keys to Tuple of 2 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <K6>  type of the sixth element of the Key
     * @param <K7>  type of the seventh element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple2 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple2 value mapped from the key
     * @return      Constructed Repository to map 7 keys to 2 values
     */
    public static <K1,K2,K3,K4,K5,K6,K7, T,U> HeptaKeys<K1,K2,K3,K4,K5,K6,K7, T,U> fromSevenKeys(
            Map<Tuple7<K1,K2,K3,K4,K5,K6,K7>, Tuple2<T,U>> map,
            TriConsumerThrowable<Tuple7<K1,K2,K3,K4,K5,K6,K7>, Tuple2<T,U>, Tuple2<T,U>> changesConsumer,
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Tuple2<T, U>> valueFunction){
        return new HeptaKeys(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 1 key to Tuple of 2 elements
     * @param <K1>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple2 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple2 value mapped from the key
     * @return      Constructed Repository to map 1 key to 2 values
     */
    public static <K1, T,U> SingleKeys<K1, T,U> fromOneKeys(
            FunctionThrowable<K1, Tuple2<T, U>> valueFunction){
        return new SingleKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 2 keys to Tuple of 2 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple2 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple2 value mapped from the key
     * @return      Constructed Repository to map 2 keys to 2 values
     */
    public static <K1,K2, T,U> DualKeys<K1,K2, T,U> fromTwoKeys(
            BiFunctionThrowable<K1,K2, Tuple2<T, U>> valueFunction){
        return new DualKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 3 keys to Tuple of 2 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple2 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple2 value mapped from the key
     * @return      Constructed Repository to map 3 keys to 2 values
     */
    public static <K1,K2,K3, T,U> TripleKeys<K1,K2,K3, T,U> fromThreeKeys(
            TriFunctionThrowable<K1,K2,K3, Tuple2<T, U>> valueFunction){
        return new TripleKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 4 keys to Tuple of 2 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple2 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple2 value mapped from the key
     * @return      Constructed Repository to map 4 keys to 2 values
     */
    public static <K1,K2,K3,K4, T,U> QuadKeys<K1,K2,K3,K4, T,U> fromFourKeys(
            QuadFunctionThrowable<K1,K2,K3,K4, Tuple2<T, U>> valueFunction){
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
     * @param <T>   type of the first element of the Tuple.Tuple2 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple2 value mapped from the key
     * @return      Constructed Repository to map 5 keys to 2 values
     */
    public static <K1,K2,K3,K4,K5, T,U> PentaKeys<K1,K2,K3,K4,K5, T,U> fromFiveKeys(
            PentaFunctionThrowable<K1,K2,K3,K4,K5, Tuple2<T, U>> valueFunction){
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
     * @param <T>   type of the first element of the Tuple.Tuple2 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple2 value mapped from the key
     * @return      Constructed Repository to map 6 keys to 2 values
     */
    public static <K1,K2,K3,K4,K5,K6, T,U> HexaKeys<K1,K2,K3,K4,K5,K6, T,U> fromSixKeys(
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Tuple2<T, U>> valueFunction){
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
     * @param <T>   type of the first element of the Tuple.Tuple2 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple2 value mapped from the key
     * @return      Constructed Repository to map 7 keys to 2 values
     */
    public static <K1,K2,K3,K4,K5,K6,K7, T,U> HeptaKeys<K1,K2,K3,K4,K5,K6,K7, T,U> fromSevenKeys(
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Tuple2<T, U>> valueFunction){
        return new HeptaKeys(valueFunction);
    }
    //endregion

    //region Constructors
    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 1 key to Tuple of 2 elements
     */
    protected DualValuesRepository(Map<TKey, Tuple2<T,U>> map,
                                   TriConsumerThrowable<TKey, Tuple2<T,U>, Tuple2<T,U>> changesConsumer,
                                   FunctionThrowable<TKey, Tuple2<T, U>> valueFunction){
        super(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 1 key to Tuple of 2 elements
     */
    protected DualValuesRepository(FunctionThrowable<TKey, Tuple2<T, U>> valueFunction){
        this(new HashMap(), null, valueFunction);
    }
    //endregion

    /**
     * Get the strong-typed Tuple2&lt;T,U&gt; value mapped from the given key
     * @param key   key to retrieve the strong-typed Tuple2&lt;T,U&gt; value
     * @return      the strong-typed Tuple2&lt;T,U&gt; value mapped from the specific key
     */
    @Override
    public Tuple2<T,U> retrieve(TKey key) {
        return get(key, null);
    }

    /**
     * Generic repository use Tuple. to keep value of 2 elements mapped from a key of the Tuple
     * Notice: the actual type of the Key is Tuple.Tuple1 wrapping the actual value of <tt>K1</tt>
     *
     * @param <K1>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple2 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple2 value mapped from the key
     */
    public static class SingleKeys<K1, T,U> extends DualValuesRepository<Tuple1<K1>, T,U>
            implements com.easyworks.repository.SingleKeys.DualValues<K1, T, U> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param map   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 1 key to Tuple of 2 elements
         */
        protected SingleKeys(Map<Tuple1<K1>, Tuple2<T,U>> map,
                             TriConsumerThrowable<Tuple1<K1>, Tuple2<T,U>, Tuple2<T,U>> changesConsumer,
                             FunctionThrowable<K1, Tuple2<T, U>> valueFunction) {
            super(map, changesConsumer, single -> valueFunction.apply(single.getFirst()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 1 key to Tuple of 2 elements
         */
        protected SingleKeys(FunctionThrowable<K1, Tuple2<T, U>> valueFunction) {
            this(new HashMap(), null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple2 to keep value of 2 elements mapped from a key, and keep the key as Tuple.Tuple2
     * of 2 different elements
     *
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple2 mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple2 mapped from the key
     */
    public static class DualKeys<K1,K2, T,U> extends DualValuesRepository<Tuple2<K1,K2>, T,U>
            implements com.easyworks.repository.DualKeys.DualValues<K1,K2, T,U> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param map   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 2 keys to Tuple of 2 elements
         */
        protected DualKeys(Map<Tuple2<K1,K2>, Tuple2<T,U>> map,
                           TriConsumerThrowable<Tuple2<K1,K2>, Tuple2<T,U>, Tuple2<T,U>> changesConsumer,
                           BiFunctionThrowable<K1, K2, Tuple2<T, U>> valueFunction) {
            super(map, changesConsumer, dual -> valueFunction.apply(dual.getFirst(), dual.getSecond()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 2 keys to Tuple of 2 elements
         */
        protected DualKeys(BiFunctionThrowable<K1, K2, Tuple2<T, U>> valueFunction) {
            this(new HashMap(), null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple2 to keep 2 elements of the value mapped from a Tuple
     * of 3 different elements
     *
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple2 mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple2 mapped from the key
     */
    public static class TripleKeys<K1,K2,K3, T,U> extends DualValuesRepository<Tuple3<K1,K2,K3>, T,U>
            implements com.easyworks.repository.TripleKeys.DualValues<K1,K2,K3, T,U> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param map   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 3 keys to Tuple of 2 elements
         */
        protected TripleKeys(Map<Tuple3<K1,K2,K3>, Tuple2<T,U>> map,
                             TriConsumerThrowable<Tuple3<K1,K2,K3>, Tuple2<T,U>, Tuple2<T,U>> changesConsumer,
                             TriFunctionThrowable<K1, K2, K3, Tuple2<T, U>> valueFunction) {
            super(map, changesConsumer, triple -> valueFunction.apply(triple.getFirst(), triple.getSecond(), triple.getThird()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 3 keys to Tuple of 2 elements
         */
        protected TripleKeys(TriFunctionThrowable<K1, K2, K3, Tuple2<T, U>> valueFunction) {
            this(new HashMap(), null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple2 to keep value of 2 elements mapped from a key, and keep the key as Tuple.Tuple7
     * of 4 different elements
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <K4>  type of fourth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple2 mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple2 mapped from the key
     */
    public static class QuadKeys<K1,K2,K3,K4, T,U> extends DualValuesRepository<Tuple4<K1,K2,K3,K4>, T,U>
            implements com.easyworks.repository.QuadKeys.DualValues<K1,K2,K3,K4, T,U> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param map   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 4 keys to Tuple of 2 elements
         */
        protected QuadKeys(Map<Tuple4<K1,K2,K3,K4>, Tuple2<T,U>> map,
                           TriConsumerThrowable<Tuple4<K1,K2,K3,K4>, Tuple2<T,U>, Tuple2<T,U>> changesConsumer,
                           QuadFunctionThrowable<K1,K2,K3,K4, Tuple2<T, U>> valueFunction) {
            super(map, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(), tuple.getFourth()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 4 keys to Tuple of 2 elements
         */
        protected QuadKeys(QuadFunctionThrowable<K1,K2,K3,K4, Tuple2<T, U>> valueFunction) {
            this(new HashMap(), null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple2 to keep value of 2 elements mapped from a key, and keep the key as Tuple.Tuple7
     * of 5 different elements
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <K4>  type of fourth element of the Key
     * @param <K5>  type of fifth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple2 mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple2 mapped from the key
     */
    public static class PentaKeys<K1,K2,K3,K4,K5, T,U> extends DualValuesRepository<Tuple5<K1,K2,K3,K4,K5>, T,U>
            implements com.easyworks.repository.PentaKeys.DualValues<K1,K2,K3,K4,K5, T,U> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param map   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 5 keys to Tuple of 2 elements
         */
        protected PentaKeys(Map<Tuple5<K1,K2,K3,K4,K5>, Tuple2<T,U>> map,
                            TriConsumerThrowable<Tuple5<K1,K2,K3,K4,K5>, Tuple2<T,U>, Tuple2<T,U>> changesConsumer,
                            PentaFunctionThrowable<K1,K2,K3,K4,K5, Tuple2<T, U>> valueFunction) {
            super(map, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 5 keys to Tuple of 2 elements
         */
        protected PentaKeys(PentaFunctionThrowable<K1,K2,K3,K4,K5, Tuple2<T, U>> valueFunction) {
            this(new HashMap(), null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple2 to keep value of 2 elements mapped from a key, and keep the key as Tuple.Tuple7
     * of 6 different elements
     *
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <K4>  type of fourth element of the Key
     * @param <K5>  type of fifth element of the Key
     * @param <K6>  type of sixth element of the Key
     *
     * @param <T>   type of the first element of the Tuple.Tuple2 mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple2 mapped from the key
     */
    public static class HexaKeys<K1,K2,K3,K4,K5,K6, T,U> extends DualValuesRepository<Tuple6<K1,K2,K3,K4,K5,K6>, T,U>
            implements com.easyworks.repository.HexaKeys.DualValues<K1,K2,K3,K4,K5,K6, T,U> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param map   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 6 keys to Tuple of 2 elements
         */
        protected HexaKeys(Map<Tuple6<K1,K2,K3,K4,K5,K6>, Tuple2<T,U>> map,
                           TriConsumerThrowable<Tuple6<K1,K2,K3,K4,K5,K6>, Tuple2<T,U>, Tuple2<T,U>> changesConsumer,
                           HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Tuple2<T, U>> valueFunction) {
            super(map, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 6 keys to Tuple of 2 elements
         */
        protected HexaKeys(HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Tuple2<T, U>> valueFunction) {
            this(new HashMap(), null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple2 to keep value of 2 elements mapped from a key, and keep the key as Tuple.Tuple7
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
     * @param <T>   type of the first element of the Tuple.Tuple2 mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple2 mapped from the key
     */
    public static class HeptaKeys<K1,K2,K3,K4,K5,K6,K7, T,U> extends DualValuesRepository<Tuple7<K1,K2,K3,K4,K5,K6,K7>, T,U>
            implements com.easyworks.repository.HeptaKeys.DualValues<K1,K2,K3,K4,K5,K6,K7, T,U> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param map   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 7 keys to Tuple of 2 elements
         */
        protected HeptaKeys(Map<Tuple7<K1,K2,K3,K4,K5,K6,K7>, Tuple2<T,U>> map,
                            TriConsumerThrowable<Tuple7<K1,K2,K3,K4,K5,K6,K7>, Tuple2<T,U>, Tuple2<T,U>> changesConsumer,
                            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Tuple2<T, U>> valueFunction) {
            super(map, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth(), tuple.getSeventh()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 7 keys to Tuple of 2 elements
         */
        protected HeptaKeys(HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Tuple2<T, U>> valueFunction) {
            this(new HashMap(), null, valueFunction);
        }
    }
}
