package com.easyworks.repository;

import com.easyworks.function.*;
import com.easyworks.tuple.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic repository use Tuple.Tuple4 as values to keep 4 elements mapped from a specific key
 *
 * @param <TKey>    type of the key, can be any Type
 * @param <T>       type of the first element of the Tuple.Tuple4 value mapped from the key
 * @param <U>       type of the second element of the Tuple.Tuple4 value mapped from the key
 * @param <V>       type of the third element of the Tuple.Tuple4 value mapped from the key
 * @param <W>       type of the fourth element of the Tuple.Tuple4 value mapped from the key
 */
public class QuadValuesRepository<TKey, T,U,V,W>
        extends Repository<TKey, Tuple4<T,U,V,W>>
        implements QuadValues<TKey, T,U,V,W> {


    //region Factories to create repositories of keeping 4 elements as the map value
    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map any key to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 1 key to Tuple of 4 elements
     * @param <TKey>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple4 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple4 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple4 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple4 value mapped from the key
     * @return      Constructed Repository to map 1 key to 4 values as a Tuple
     */
    public static <TKey, T,U,V,W> QuadValuesRepository<TKey, T,U,V,W> fromKey(
            Map<TKey, Tuple4<T,U,V,W>> map,
            TriConsumerThrowable<TKey, Tuple4<T,U,V,W>, Tuple4<T,U,V,W>> changesConsumer,
            FunctionThrowable<TKey, Tuple4<T,U,V,W>> valueFunction){
        return new QuadValuesRepository(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with given map factory
     *
     * @param valueFunction     Function to map 1 key to Tuple of 4 elements
     * @param <TKey>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple4 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple4 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple4 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple4 value mapped from the key
     * @return      Constructed Repository to map 1 key to 4 values as a Tuple
     */
    public static <TKey, T,U,V,W> QuadValuesRepository<TKey, T,U,V,W> fromKey(
            FunctionThrowable<TKey, Tuple4<T,U,V,W>> valueFunction){
        return new QuadValuesRepository(valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 1 key to Tuple of 4 elements
     * @param <K1>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple4 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple4 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple4 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple4 value mapped from the key
     * @return      Constructed Repository to map 1 key to 4 values as a Tuple
     */
    public static <K1, T,U,V,W> SingleKeys<K1, T,U,V,W> fromOneKeys(
            Map<Tuple1<K1>, Tuple4<T,U,V,W>> map,
            TriConsumerThrowable<Tuple1<K1>, Tuple4<T,U,V,W>, Tuple4<T,U,V,W>> changesConsumer,
            FunctionThrowable<K1, Tuple4<T,U,V,W>> valueFunction){
        return new SingleKeys(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 2 keys to Tuple of 4 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple4 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple4 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple4 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple4 value mapped from the key
     * @return      Constructed Repository to map 2 keys to 4 values as a Tuple
     */
    public static <K1,K2, T,U,V,W> DualKeys<K1,K2, T,U,V,W> fromTwoKeys(
            Map<Tuple2<K1,K2>, Tuple4<T,U,V,W>> map,
            TriConsumerThrowable<Tuple2<K1,K2>, Tuple4<T,U,V,W>, Tuple4<T,U,V,W>> changesConsumer,
            BiFunctionThrowable<K1,K2, Tuple4<T,U,V,W>> valueFunction){
        return new DualKeys(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 3 keys to Tuple of 4 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple4 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple4 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple4 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple4 value mapped from the key
     * @return      Constructed Repository to map 3 keys to 4 values as a Tuple
     */
    public static <K1,K2,K3, T,U,V,W> TripleKeys<K1,K2,K3, T,U,V,W> fromThreeKeys(
            Map<Tuple3<K1,K2,K3>, Tuple4<T,U,V,W>> map,
            TriConsumerThrowable<Tuple3<K1,K2,K3>, Tuple4<T,U,V,W>, Tuple4<T,U,V,W>> changesConsumer,
            TriFunctionThrowable<K1,K2,K3, Tuple4<T,U,V,W>> valueFunction){
        return new TripleKeys(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 4 keys to Tuple of 4 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple4 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple4 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple4 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple4 value mapped from the key
     * @return      Constructed Repository to map 4 keys to 4 values as a Tuple
     */
    public static <K1,K2,K3,K4, T,U,V,W> QuadKeys<K1,K2,K3,K4, T,U,V,W> fromFourKeys(
            Map<Tuple4<K1,K2,K3,K4>, Tuple4<T,U,V,W>> map,
            TriConsumerThrowable<Tuple4<K1,K2,K3,K4>, Tuple4<T,U,V,W>, Tuple4<T,U,V,W>> changesConsumer,
            QuadFunctionThrowable<K1,K2,K3,K4, Tuple4<T,U,V,W>> valueFunction){
        return new QuadKeys(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 5 keys to Tuple of 4 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple4 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple4 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple4 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple4 value mapped from the key
     * @return      Constructed Repository to map 5 keys to 4 values as a Tuple
     */
    public static <K1,K2,K3,K4,K5, T,U,V,W> PentaKeys<K1,K2,K3,K4,K5, T,U,V,W> fromFiveKeys(
            Map<Tuple5<K1,K2,K3,K4,K5>, Tuple4<T,U,V,W>> map,
            TriConsumerThrowable<Tuple5<K1,K2,K3,K4,K5>, Tuple4<T,U,V,W>, Tuple4<T,U,V,W>> changesConsumer,
            PentaFunctionThrowable<K1,K2,K3,K4,K5, Tuple4<T,U,V,W>> valueFunction){
        return new PentaKeys(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 6 keys to Tuple of 4 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <K6>  type of the sixth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple4 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple4 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple4 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple4 value mapped from the key
     * @return      Constructed Repository to map 6 keys to 4 values as a Tuple
     */
    public static <K1,K2,K3,K4,K5,K6, T,U,V,W> HexaKeys<K1,K2,K3,K4,K5,K6, T,U,V,W> fromSixKeys(
            Map<Tuple6<K1,K2,K3,K4,K5,K6>, Tuple4<T,U,V,W>> map,
            TriConsumerThrowable<Tuple6<K1,K2,K3,K4,K5,K6>, Tuple4<T,U,V,W>, Tuple4<T,U,V,W>> changesConsumer,
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Tuple4<T,U,V,W>> valueFunction){
        return new HexaKeys(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map   Factory to get the strong-typed map instance
     * @param changesConsumer   Extra steps to be called when any entry updated
     * @param valueFunction     Function to map 7 keys to Tuple of 4 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <K5>  type of the fifth element of the Key
     * @param <K6>  type of the sixth element of the Key
     * @param <K7>  type of the seventh element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple4 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple4 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple4 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple4 value mapped from the key
     * @return      Constructed Repository to map 7 keys to 4 values as a Tuple
     */
    public static <K1,K2,K3,K4,K5,K6,K7, T,U,V,W> HeptaKeys<K1,K2,K3,K4,K5,K6,K7, T,U,V,W> fromSevenKeys(
            Map<Tuple7<K1,K2,K3,K4,K5,K6,K7>, Tuple4<T,U,V,W>> map,
            TriConsumerThrowable<Tuple7<K1,K2,K3,K4,K5,K6,K7>, Tuple4<T,U,V,W>, Tuple4<T,U,V,W>> changesConsumer,
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Tuple4<T,U,V,W>> valueFunction){
        return new HeptaKeys(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 1 key to Tuple of 4 elements
     * @param <K1>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple4 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple4 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple4 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple4 value mapped from the key
     * @return      Constructed Repository to map 1 key to 4 values as a Tuple
     */
    public static <K1, T,U,V,W> SingleKeys<K1, T,U,V,W> fromOneKeys(
            FunctionThrowable<K1, Tuple4<T,U,V,W>> valueFunction){
        return new SingleKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 2 keys to Tuple of 4 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple4 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple4 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple4 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple4 value mapped from the key
     * @return      Constructed Repository to map 2 keys to 4 values as a Tuple
     */
    public static <K1,K2, T,U,V,W> DualKeys<K1,K2, T,U,V,W> fromTwoKeys(
            BiFunctionThrowable<K1,K2, Tuple4<T,U,V,W>> valueFunction){
        return new DualKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 3 keys to Tuple of 4 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple4 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple4 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple4 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple4 value mapped from the key
     * @return      Constructed Repository to map 3 keys to 4 values as a Tuple
     */
    public static <K1,K2,K3, T,U,V,W> TripleKeys<K1,K2,K3, T,U,V,W> fromThreeKeys(
            TriFunctionThrowable<K1,K2,K3, Tuple4<T,U,V,W>> valueFunction){
        return new TripleKeys(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 4 keys to Tuple of 4 elements
     * @param <K1>  type of the first element of the Key
     * @param <K2>  type of the second element of the Key
     * @param <K3>  type of the third element of the Key
     * @param <K4>  type of the fourth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple4 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple4 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple4 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple4 value mapped from the key
     * @return      Constructed Repository to map 4 keys to 4 values as a Tuple
     */
    public static <K1,K2,K3,K4, T,U,V,W> QuadKeys<K1,K2,K3,K4, T,U,V,W> fromFourKeys(
            QuadFunctionThrowable<K1,K2,K3,K4, Tuple4<T,U,V,W>> valueFunction){
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
     * @param <T>   type of the first element of the Tuple.Tuple4 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple4 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple4 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple4 value mapped from the key
     * @return      Constructed Repository to map 5 keys to 4 values as a Tuple
     */
    public static <K1,K2,K3,K4,K5, T,U,V,W> PentaKeys<K1,K2,K3,K4,K5, T,U,V,W> fromFiveKeys(
            PentaFunctionThrowable<K1,K2,K3,K4,K5, Tuple4<T,U,V,W>> valueFunction){
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
     * @param <T>   type of the first element of the Tuple.Tuple4 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple4 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple4 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple4 value mapped from the key
     * @return      Constructed Repository to map 6 keys to 4 values as a Tuple
     */
    public static <K1,K2,K3,K4,K5,K6, T,U,V,W> HexaKeys<K1,K2,K3,K4,K5,K6, T,U,V,W> fromSixKeys(
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Tuple4<T,U,V,W>> valueFunction){
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
     * @param <T>   type of the first element of the Tuple.Tuple4 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple4 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple4 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple4 value mapped from the key
     * @return      Constructed Repository to map 7 keys to 4 values as a Tuple
     */
    public static <K1,K2,K3,K4,K5,K6,K7, T,U,V,W> HeptaKeys<K1,K2,K3,K4,K5,K6,K7, T,U,V,W> fromSevenKeys(
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Tuple4<T,U,V,W>> valueFunction){
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
     * @param valueFunction     Function to map 1 key to Tuple of 4 elements
     */
    protected QuadValuesRepository(Map<TKey, Tuple4<T,U,V,W>> map,
                                   TriConsumerThrowable<TKey, Tuple4<T,U,V,W>, Tuple4<T,U,V,W>> changesConsumer,
                                   FunctionThrowable<TKey, Tuple4<T,U,V,W>> valueFunction){
        super(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     * @param valueFunction     Function to map 1 key to Tuple of 4 elements
     */
    protected QuadValuesRepository(FunctionThrowable<TKey, Tuple4<T,U,V,W>> valueFunction){
        this(new HashMap(), null, valueFunction);
    }
    //endregion

    /**
     * Get the strong-typed Tuple2&lt;T,U&gt; value mapped from the given key
     * @param key   key to retrieve the strong-typed Tuple2&lt;T,U&gt; value
     * @return      the strong-typed Tuple2&lt;T,U&gt; value mapped from the specific key
     */
    @Override
    public Tuple4<T,U,V,W> retrieve(TKey key) {
        return get(key, null);
    }

    /**
     * Generic repository use Tuple. to keep value of 2 elements mapped from a key of the Tuple
     * Notice: the actual type of the Key is Tuple.Tuple1 wrapping the actual value of <tt>K1</tt>
     *
     * @param <K1>  type of the first element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple4 value mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple4 value mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple4 value mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple4 value mapped from the key
     */
    public static class SingleKeys<K1, T,U,V,W> extends QuadValuesRepository<Tuple1<K1>, T,U,V,W>
            implements com.easyworks.repository.SingleKeys.QuadValues<K1, T,U,V,W> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param map   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 1 key to Tuple of 4 elements
         */
        protected SingleKeys(Map<Tuple1<K1>, Tuple4<T,U,V,W>> map,
                             TriConsumerThrowable<Tuple1<K1>, Tuple4<T,U,V,W>, Tuple4<T,U,V,W>> changesConsumer,
                             FunctionThrowable<K1, Tuple4<T,U,V,W>> valueFunction) {
            super(map, changesConsumer, single -> valueFunction.apply(single.getFirst()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 1 key to Tuple of 4 elements
         */
        protected SingleKeys(FunctionThrowable<K1, Tuple4<T,U,V,W>> valueFunction) {
            this(new HashMap(), null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple4 to keep value of 2 elements mapped from a key, and keep the key as Tuple.Tuple4
     * of 2 different elements
     *
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple4 mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple4 mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple4 mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple4 mapped from the key
     */
    public static class DualKeys<K1,K2, T,U,V,W> extends QuadValuesRepository<Tuple2<K1,K2>, T,U,V,W>
            implements com.easyworks.repository.DualKeys.QuadValues<K1,K2, T,U,V,W> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param map   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 2 keys to Tuple of 4 elements
         */
        protected DualKeys(Map<Tuple2<K1,K2>, Tuple4<T,U,V,W>> map,
                           TriConsumerThrowable<Tuple2<K1,K2>, Tuple4<T,U,V,W>, Tuple4<T,U,V,W>> changesConsumer,
                           BiFunctionThrowable<K1, K2, Tuple4<T,U,V,W>> valueFunction) {
            super(map, changesConsumer, dual -> valueFunction.apply(dual.getFirst(), dual.getSecond()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 2 keys to Tuple of 4 elements
         */
        protected DualKeys(BiFunctionThrowable<K1, K2, Tuple4<T,U,V,W>> valueFunction) {
            this(new HashMap(), null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple4 to keep 2 elements of the value mapped from a Tuple
     * of 3 different elements
     *
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple4 mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple4 mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple4 mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple4 mapped from the key
     */
    public static class TripleKeys<K1,K2,K3, T,U,V,W> extends QuadValuesRepository<Tuple3<K1,K2,K3>, T,U,V,W>
            implements com.easyworks.repository.TripleKeys.QuadValues<K1,K2,K3, T,U,V,W> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param map   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 3 keys to Tuple of 4 elements
         */
        protected TripleKeys(Map<Tuple3<K1,K2,K3>, Tuple4<T,U,V,W>> map,
                             TriConsumerThrowable<Tuple3<K1,K2,K3>, Tuple4<T,U,V,W>, Tuple4<T,U,V,W>> changesConsumer,
                             TriFunctionThrowable<K1, K2, K3, Tuple4<T,U,V,W>> valueFunction) {
            super(map, changesConsumer, triple -> valueFunction.apply(triple.getFirst(), triple.getSecond(), triple.getThird()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 3 keys to Tuple of 4 elements
         */
        protected TripleKeys(TriFunctionThrowable<K1, K2, K3, Tuple4<T,U,V,W>> valueFunction) {
            this(new HashMap(), null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple4 to keep value of 2 elements mapped from a key, and keep the key as Tuple.Tuple7
     * of 4 different elements
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <K4>  type of fourth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple4 mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple4 mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple4 mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple4 mapped from the key
     */
    public static class QuadKeys<K1,K2,K3,K4, T,U,V,W> extends QuadValuesRepository<Tuple4<K1,K2,K3,K4>, T,U,V,W>
            implements com.easyworks.repository.QuadKeys.QuadValues<K1,K2,K3,K4, T,U,V,W> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param map   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 4 keys to Tuple of 4 elements
         */
        protected QuadKeys(Map<Tuple4<K1,K2,K3,K4>, Tuple4<T,U,V,W>> map,
                           TriConsumerThrowable<Tuple4<K1,K2,K3,K4>, Tuple4<T,U,V,W>, Tuple4<T,U,V,W>> changesConsumer,
                           QuadFunctionThrowable<K1,K2,K3,K4, Tuple4<T,U,V,W>> valueFunction) {
            super(map, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(), tuple.getFourth()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 4 keys to Tuple of 4 elements
         */
        protected QuadKeys(QuadFunctionThrowable<K1,K2,K3,K4, Tuple4<T,U,V,W>> valueFunction) {
            this(new HashMap(), null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple4 to keep value of 2 elements mapped from a key, and keep the key as Tuple.Tuple7
     * of 5 different elements
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <K4>  type of fourth element of the Key
     * @param <K5>  type of fifth element of the Key
     * @param <T>   type of the first element of the Tuple.Tuple4 mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple4 mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple4 mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple4 mapped from the key
     */
    public static class PentaKeys<K1,K2,K3,K4,K5, T,U,V,W> extends QuadValuesRepository<Tuple5<K1,K2,K3,K4,K5>, T,U,V,W>
            implements com.easyworks.repository.PentaKeys.QuadValues<K1,K2,K3,K4,K5, T,U,V,W> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param map   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 5 keys to Tuple of 4 elements
         */
        protected PentaKeys(Map<Tuple5<K1,K2,K3,K4,K5>, Tuple4<T,U,V,W>> map,
                            TriConsumerThrowable<Tuple5<K1,K2,K3,K4,K5>, Tuple4<T,U,V,W>, Tuple4<T,U,V,W>> changesConsumer,
                            PentaFunctionThrowable<K1,K2,K3,K4,K5, Tuple4<T,U,V,W>> valueFunction) {
            super(map, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 5 keys to Tuple of 4 elements
         */
        protected PentaKeys(PentaFunctionThrowable<K1,K2,K3,K4,K5, Tuple4<T,U,V,W>> valueFunction) {
            this(new HashMap(), null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple4 to keep value of 2 elements mapped from a key, and keep the key as Tuple.Tuple7
     * of 6 different elements
     *
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <K4>  type of fourth element of the Key
     * @param <K5>  type of fifth element of the Key
     * @param <K6>  type of sixth element of the Key
     *
     * @param <T>   type of the first element of the Tuple.Tuple4 mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple4 mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple4 mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple4 mapped from the key
     */
    public static class HexaKeys<K1,K2,K3,K4,K5,K6, T,U,V,W> extends QuadValuesRepository<Tuple6<K1,K2,K3,K4,K5,K6>, T,U,V,W>
            implements com.easyworks.repository.HexaKeys.QuadValues<K1,K2,K3,K4,K5,K6, T,U,V,W> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param map   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 6 keys to Tuple of 4 elements
         */
        protected HexaKeys(Map<Tuple6<K1,K2,K3,K4,K5,K6>, Tuple4<T,U,V,W>> map,
                           TriConsumerThrowable<Tuple6<K1,K2,K3,K4,K5,K6>, Tuple4<T,U,V,W>, Tuple4<T,U,V,W>> changesConsumer,
                           HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Tuple4<T,U,V,W>> valueFunction) {
            super(map, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 6 keys to Tuple of 4 elements
         */
        protected HexaKeys(HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Tuple4<T,U,V,W>> valueFunction) {
            this(new HashMap(), null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple4 to keep value of 2 elements mapped from a key, and keep the key as Tuple.Tuple7
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
     * @param <T>   type of the first element of the Tuple.Tuple4 mapped from the key
     * @param <U>   type of the second element of the Tuple.Tuple4 mapped from the key
     * @param <V>   type of the third element of the Tuple.Tuple4 mapped from the key
     * @param <W>   type of the fourth element of the Tuple.Tuple4 mapped from the key
     */
    public static class HeptaKeys<K1,K2,K3,K4,K5,K6,K7, T,U,V,W> extends QuadValuesRepository<Tuple7<K1,K2,K3,K4,K5,K6,K7>, T,U,V,W>
            implements com.easyworks.repository.HeptaKeys.QuadValues<K1,K2,K3,K4,K5,K6,K7, T,U,V,W> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param map   Factory to get the strong-typed map instance
         * @param changesConsumer   Extra steps to be called when any entry updated
         * @param valueFunction     Function to map 7 keys to Tuple of 4 elements
         */
        protected HeptaKeys(Map<Tuple7<K1,K2,K3,K4,K5,K6,K7>, Tuple4<T,U,V,W>> map,
                            TriConsumerThrowable<Tuple7<K1,K2,K3,K4,K5,K6,K7>, Tuple4<T,U,V,W>, Tuple4<T,U,V,W>> changesConsumer,
                            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Tuple4<T,U,V,W>> valueFunction) {
            super(map, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth(), tuple.getSeventh()));
        }

        /**
         * Construct a repository with evaluation logic only
         * @param valueFunction     Function to map 7 keys to Tuple of 4 elements
         */
        protected HeptaKeys(HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Tuple4<T,U,V,W>> valueFunction) {
            this(new HashMap(), null, valueFunction);
        }
    }
}
