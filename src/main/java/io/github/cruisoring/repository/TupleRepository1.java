package io.github.cruisoring.repository;

import io.github.cruisoring.throwables.*;
import io.github.cruisoring.tuple.*;

import java.util.HashMap;
import java.util.Map;

import static io.github.cruisoring.Asserts.checkNoneNulls;

/**
 * Generic repository use Tuple.Tuple2 as values to keep 1 element mapped from a specific key
 *
 * @param <TKey> type of the key, can be any Type
 * @param <T>    type of the first element of the Tuple.Tuple2 value mapped from the key
 */
public class TupleRepository1<TKey, T>
        extends Repository<TKey, Tuple1<T>>
        implements TupleValues1<TKey, T> {


    //region Factories to create repositories of keeping 1 element as the map value

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map             Factory to get the strong-typed map instance
     * @param changesConsumer Extra steps to be called when any entry updated
     * @param valueFunction   Function to map 1 key to Tuple of 1 element
     */
    protected TupleRepository1(Map<TKey, Tuple1<T>> map,
                               TriConsumerThrowable<TKey, Tuple1<T>, Tuple1<T>> changesConsumer,
                               FunctionThrowable<TKey, Tuple1<T>> valueFunction) {
        super(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     *
     * @param valueFunction Function to map 1 key to Tuple of 1 element
     */
    protected TupleRepository1(FunctionThrowable<TKey, Tuple1<T>> valueFunction) {
        this(new HashMap(), null, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map any key to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map             Factory to get the strong-typed map instance
     * @param changesConsumer Extra steps to be called when any entry updated
     * @param valueFunction   Function to map 1 key to Tuple of 1 element
     * @param <TKey>          type of the first element of the Key
     * @param <T>             type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return Constructed Repository to map 1 key to 1 value as a Tuple
     */
    public static <TKey, T> TupleRepository1<TKey, T> fromKey(
            Map<TKey, Tuple1<T>> map,
            TriConsumerThrowable<TKey, Tuple1<T>, Tuple1<T>> changesConsumer,
            FunctionThrowable<TKey, Tuple1<T>> valueFunction) {
        return new TupleRepository1(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with given map factory
     *
     * @param valueFunction Function to map 1 key to Tuple of 1 element
     * @param <TKey>        type of the first element of the Key
     * @param <T>           type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return Constructed Repository to map 1 key to 1 value as a Tuple
     */
    public static <TKey, T> TupleRepository1<TKey, T> fromKey(
            FunctionThrowable<TKey, Tuple1<T>> valueFunction) {
        return new TupleRepository1(valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map             Factory to get the strong-typed map instance
     * @param changesConsumer Extra steps to be called when any entry updated
     * @param valueFunction   Function to map 1 key to Tuple of 1 element
     * @param <K1>            type of the first element of the Key
     * @param <T>             type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return Constructed Repository to map 1 key to 1 value as a Tuple
     */
    public static <K1, T> TupleKeys1<K1, T> fromKeys1(
            Map<Tuple1<K1>, Tuple1<T>> map,
            TriConsumerThrowable<Tuple1<K1>, Tuple1<T>, Tuple1<T>> changesConsumer,
            FunctionThrowable<K1, Tuple1<T>> valueFunction) {
        return new TupleKeys1(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map             Factory to get the strong-typed map instance
     * @param changesConsumer Extra steps to be called when any entry updated
     * @param valueFunction   Function to map 2 keys to Tuple of 1 element
     * @param <K1>            type of the first element of the Key
     * @param <K2>            type of the second element of the Key
     * @param <T>             type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return Constructed Repository to map 2 keys to 1 value as a Tuple
     */
    public static <K1, K2, T> TupleKeys2<K1, K2, T> fromKeys2(
            Map<Tuple2<K1, K2>, Tuple1<T>> map,
            TriConsumerThrowable<Tuple2<K1, K2>, Tuple1<T>, Tuple1<T>> changesConsumer,
            BiFunctionThrowable<K1, K2, Tuple1<T>> valueFunction) {
        return new TupleKeys2(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map             Factory to get the strong-typed map instance
     * @param changesConsumer Extra steps to be called when any entry updated
     * @param valueFunction   Function to map 3 keys to Tuple of 1 element
     * @param <K1>            type of the first element of the Key
     * @param <K2>            type of the second element of the Key
     * @param <K3>            type of the third element of the Key
     * @param <T>             type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return Constructed Repository to map 3 keys to 1 value as a Tuple
     */
    public static <K1, K2, K3, T> TupleKeys3<K1, K2, K3, T> fromKeys3(
            Map<Tuple3<K1, K2, K3>, Tuple1<T>> map,
            TriConsumerThrowable<Tuple3<K1, K2, K3>, Tuple1<T>, Tuple1<T>> changesConsumer,
            TriFunctionThrowable<K1, K2, K3, Tuple1<T>> valueFunction) {
        return new TupleKeys3(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map             Factory to get the strong-typed map instance
     * @param changesConsumer Extra steps to be called when any entry updated
     * @param valueFunction   Function to map 4 keys to Tuple of 1 element
     * @param <K1>            type of the first element of the Key
     * @param <K2>            type of the second element of the Key
     * @param <K3>            type of the third element of the Key
     * @param <K4>            type of the fourth element of the Key
     * @param <T>             type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return Constructed Repository to map 4 keys to 1 value as a Tuple
     */
    public static <K1, K2, K3, K4, T> TupleKeys4<K1, K2, K3, K4, T> fromKeys4(
            Map<Tuple4<K1, K2, K3, K4>, Tuple1<T>> map,
            TriConsumerThrowable<Tuple4<K1, K2, K3, K4>, Tuple1<T>, Tuple1<T>> changesConsumer,
            QuadFunctionThrowable<K1, K2, K3, K4, Tuple1<T>> valueFunction) {
        return new TupleKeys4(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map             Factory to get the strong-typed map instance
     * @param changesConsumer Extra steps to be called when any entry updated
     * @param valueFunction   Function to map 5 keys to Tuple of 1 element
     * @param <K1>            type of the first element of the Key
     * @param <K2>            type of the second element of the Key
     * @param <K3>            type of the third element of the Key
     * @param <K4>            type of the fourth element of the Key
     * @param <K5>            type of the fifth element of the Key
     * @param <T>             type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return Constructed Repository to map 5 keys to 1 value as a Tuple
     */
    public static <K1, K2, K3, K4, K5, T> TupleKeys5<K1, K2, K3, K4, K5, T> fromKeys5(
            Map<Tuple5<K1, K2, K3, K4, K5>, Tuple1<T>> map,
            TriConsumerThrowable<Tuple5<K1, K2, K3, K4, K5>, Tuple1<T>, Tuple1<T>> changesConsumer,
            PentaFunctionThrowable<K1, K2, K3, K4, K5, Tuple1<T>> valueFunction) {
        return new TupleKeys5(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map             Factory to get the strong-typed map instance
     * @param changesConsumer Extra steps to be called when any entry updated
     * @param valueFunction   Function to map 6 keys to Tuple of 1 element
     * @param <K1>            type of the first element of the Key
     * @param <K2>            type of the second element of the Key
     * @param <K3>            type of the third element of the Key
     * @param <K4>            type of the fourth element of the Key
     * @param <K5>            type of the fifth element of the Key
     * @param <K6>            type of the sixth element of the Key
     * @param <T>             type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return Constructed Repository to map 6 keys to 1 value as a Tuple
     */
    public static <K1, K2, K3, K4, K5, K6, T> TupleKeys6<K1, K2, K3, K4, K5, K6, T> fromKeys6(
            Map<Tuple6<K1, K2, K3, K4, K5, K6>, Tuple1<T>> map,
            TriConsumerThrowable<Tuple6<K1, K2, K3, K4, K5, K6>, Tuple1<T>, Tuple1<T>> changesConsumer,
            HexaFunctionThrowable<K1, K2, K3, K4, K5, K6, Tuple1<T>> valueFunction) {
        return new TupleKeys6(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
     * Notice: Shall be used when there are special cases need to be put first
     *
     * @param map             Factory to get the strong-typed map instance
     * @param changesConsumer Extra steps to be called when any entry updated
     * @param valueFunction   Function to map 7 keys to Tuple of 1 element
     * @param <K1>            type of the first element of the Key
     * @param <K2>            type of the second element of the Key
     * @param <K3>            type of the third element of the Key
     * @param <K4>            type of the fourth element of the Key
     * @param <K5>            type of the fifth element of the Key
     * @param <K6>            type of the sixth element of the Key
     * @param <K7>            type of the seventh element of the Key
     * @param <T>             type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return Constructed Repository to map 7 keys to 1 value as a Tuple
     */
    public static <K1, K2, K3, K4, K5, K6, K7, T> TupleKeys7<K1, K2, K3, K4, K5, K6, K7, T> fromKeys7(
            Map<Tuple7<K1, K2, K3, K4, K5, K6, K7>, Tuple1<T>> map,
            TriConsumerThrowable<Tuple7<K1, K2, K3, K4, K5, K6, K7>, Tuple1<T>, Tuple1<T>> changesConsumer,
            HeptaFunctionThrowable<K1, K2, K3, K4, K5, K6, K7, Tuple1<T>> valueFunction) {
        return new TupleKeys7(map, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     *
     * @param valueFunction Function to map 1 key to Tuple of 1 element
     * @param <K1>          type of the first element of the Key
     * @param <T>           type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return Constructed Repository to map 1 key to 1 value as a Tuple
     */
    public static <K1, T> TupleKeys1<K1, T> fromKeys1(
            FunctionThrowable<K1, Tuple1<T>> valueFunction) {
        return new TupleKeys1(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     *
     * @param valueFunction Function to map 2 keys to Tuple of 1 element
     * @param <K1>          type of the first element of the Key
     * @param <K2>          type of the second element of the Key
     * @param <T>           type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return Constructed Repository to map 2 keys to 1 value as a Tuple
     */
    public static <K1, K2, T> TupleKeys2<K1, K2, T> fromKeys2(
            BiFunctionThrowable<K1, K2, Tuple1<T>> valueFunction) {
        return new TupleKeys2(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     *
     * @param valueFunction Function to map 3 keys to Tuple of 1 element
     * @param <K1>          type of the first element of the Key
     * @param <K2>          type of the second element of the Key
     * @param <K3>          type of the third element of the Key
     * @param <T>           type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return Constructed Repository to map 3 keys to 1 value as a Tuple
     */
    public static <K1, K2, K3, T> TupleKeys3<K1, K2, K3, T> fromKeys3(
            TriFunctionThrowable<K1, K2, K3, Tuple1<T>> valueFunction) {
        return new TupleKeys3(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     *
     * @param valueFunction Function to map 4 keys to Tuple of 1 element
     * @param <K1>          type of the first element of the Key
     * @param <K2>          type of the second element of the Key
     * @param <K3>          type of the third element of the Key
     * @param <K4>          type of the fourth element of the Key
     * @param <T>           type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return Constructed Repository to map 4 keys to 1 value as a Tuple
     */
    public static <K1, K2, K3, K4, T> TupleKeys4<K1, K2, K3, K4, T> fromKeys4(
            QuadFunctionThrowable<K1, K2, K3, K4, Tuple1<T>> valueFunction) {
        return new TupleKeys4(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     *
     * @param valueFunction Function to map 5 keys to Tuple of 1 element
     * @param <K1>          type of the first element of the Key
     * @param <K2>          type of the second element of the Key
     * @param <K3>          type of the third element of the Key
     * @param <K4>          type of the fourth element of the Key
     * @param <K5>          type of the fifth element of the Key
     * @param <T>           type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return Constructed Repository to map 5 keys to 1 value as a Tuple
     */
    public static <K1, K2, K3, K4, K5, T> TupleKeys5<K1, K2, K3, K4, K5, T> fromKeys5(
            PentaFunctionThrowable<K1, K2, K3, K4, K5, Tuple1<T>> valueFunction) {
        return new TupleKeys5(valueFunction);
    }
    //endregion

    //region Constructors

    /**
     * Construct a repository with evaluation logic only
     *
     * @param valueFunction Function to map 6 keys to Tuple of 1 element
     * @param <K1>          type of the first element of the Key
     * @param <K2>          type of the second element of the Key
     * @param <K3>          type of the third element of the Key
     * @param <K4>          type of the fourth element of the Key
     * @param <K5>          type of the fifth element of the Key
     * @param <K6>          type of the sixth element of the Key
     * @param <T>           type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return Constructed Repository to map 6 keys to 1 value as a Tuple
     */
    public static <K1, K2, K3, K4, K5, K6, T> TupleKeys6<K1, K2, K3, K4, K5, K6, T> fromKeys6(
            HexaFunctionThrowable<K1, K2, K3, K4, K5, K6, Tuple1<T>> valueFunction) {
        return new TupleKeys6(valueFunction);
    }

    /**
     * Construct a repository with evaluation logic only
     *
     * @param valueFunction Function to map 7 keys to Tuple of 1 element
     * @param <K1>          type of the first element of the Key
     * @param <K2>          type of the second element of the Key
     * @param <K3>          type of the third element of the Key
     * @param <K4>          type of the fourth element of the Key
     * @param <K5>          type of the fifth element of the Key
     * @param <K6>          type of the sixth element of the Key
     * @param <K7>          type of the seventh element of the Key
     * @param <T>           type of the first element of the Tuple.Tuple1 value mapped from the key
     * @return Constructed Repository to map 7 keys to 1 value as a Tuple
     */
    public static <K1, K2, K3, K4, K5, K6, K7, T> TupleKeys7<K1, K2, K3, K4, K5, K6, K7, T> fromKeys7(
            HeptaFunctionThrowable<K1, K2, K3, K4, K5, K6, K7, Tuple1<T>> valueFunction) {
        return new TupleKeys7(valueFunction);
    }
    //endregion

    /**
     * Get the strong-typed Tuple2&lt;T,U&gt; value mapped from the given key
     *
     * @param key key to retrieve the strong-typed Tuple2&lt;T,U&gt; value
     * @return the strong-typed Tuple2&lt;T,U&gt; value mapped from the specific key
     */
    @Override
    public Tuple1<T> retrieve(TKey key) {
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
    public Tuple1<T> update(TKey tKey, Tuple1<T> existingValue, Tuple1<T> newValue) throws Exception {
        checkNoneNulls(newValue);
        return super.update(tKey, existingValue, newValue);
    }


    /**
     * Generic repository use Tuple. to keep value of 2 elements mapped from a key of the Tuple
     * Notice: the actual type of the Key is Tuple.Tuple1 wrapping the actual value of <tt>K1</tt>
     *
     * @param <K1> type of the first element of the Key
     * @param <T>  type of the first element of the Tuple.Tuple1 value mapped from the key
     */
    public static class TupleKeys1<K1, T> extends TupleRepository1<Tuple1<K1>, T>
            implements io.github.cruisoring.repository.TupleKeys1.TupleValues1<K1, T> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param map             Factory to get the strong-typed map instance
         * @param changesConsumer Extra steps to be called when any entry updated
         * @param valueFunction   Function to map 1 key to Tuple of 1 element
         */
        protected TupleKeys1(Map<Tuple1<K1>, Tuple1<T>> map,
                             TriConsumerThrowable<Tuple1<K1>, Tuple1<T>, Tuple1<T>> changesConsumer,
                             FunctionThrowable<K1, Tuple1<T>> valueFunction) {
            super(map, changesConsumer, single -> valueFunction.apply(single.getFirst()));
        }

        /**
         * Construct a repository with evaluation logic only
         *
         * @param valueFunction Function to map 1 key to Tuple of 1 element
         */
        protected TupleKeys1(FunctionThrowable<K1, Tuple1<T>> valueFunction) {
            this(new HashMap(), null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple1 to keep value of 2 elements mapped from a key, and keep the key as Tuple.Tuple1
     * of 2 different elements
     *
     * @param <K1> type of first element of the Key
     * @param <K2> type of second element of the Key
     * @param <T>  type of the first element of the Tuple.Tuple1 mapped from the key
     */
    public static class TupleKeys2<K1, K2, T> extends TupleRepository1<Tuple2<K1, K2>, T>
            implements io.github.cruisoring.repository.TupleKeys2.TupleValues1<K1, K2, T> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param map             Factory to get the strong-typed map instance
         * @param changesConsumer Extra steps to be called when any entry updated
         * @param valueFunction   Function to map 2 keys to Tuple of 1 element
         */
        protected TupleKeys2(Map<Tuple2<K1, K2>, Tuple1<T>> map,
                             TriConsumerThrowable<Tuple2<K1, K2>, Tuple1<T>, Tuple1<T>> changesConsumer,
                             BiFunctionThrowable<K1, K2, Tuple1<T>> valueFunction) {
            super(map, changesConsumer, dual -> valueFunction.apply(dual.getFirst(), dual.getSecond()));
        }

        /**
         * Construct a repository with evaluation logic only
         *
         * @param valueFunction Function to map 2 keys to Tuple of 1 element
         */
        protected TupleKeys2(BiFunctionThrowable<K1, K2, Tuple1<T>> valueFunction) {
            this(new HashMap(), null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple1 to keep 2 elements of the value mapped from a Tuple
     * of 3 different elements
     *
     * @param <K1> type of first element of the Key
     * @param <K2> type of second element of the Key
     * @param <K3> type of third element of the Key
     * @param <T>  type of the first element of the Tuple.Tuple1 mapped from the key
     */
    public static class TupleKeys3<K1, K2, K3, T> extends TupleRepository1<Tuple3<K1, K2, K3>, T>
            implements io.github.cruisoring.repository.TupleKeys3.TupleValues1<K1, K2, K3, T> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param map             Factory to get the strong-typed map instance
         * @param changesConsumer Extra steps to be called when any entry updated
         * @param valueFunction   Function to map 3 keys to Tuple of 1 element
         */
        protected TupleKeys3(Map<Tuple3<K1, K2, K3>, Tuple1<T>> map,
                             TriConsumerThrowable<Tuple3<K1, K2, K3>, Tuple1<T>, Tuple1<T>> changesConsumer,
                             TriFunctionThrowable<K1, K2, K3, Tuple1<T>> valueFunction) {
            super(map, changesConsumer, triple -> valueFunction.apply(triple.getFirst(), triple.getSecond(), triple.getThird()));
        }

        /**
         * Construct a repository with evaluation logic only
         *
         * @param valueFunction Function to map 3 keys to Tuple of 1 element
         */
        protected TupleKeys3(TriFunctionThrowable<K1, K2, K3, Tuple1<T>> valueFunction) {
            this(new HashMap(), null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple1 to keep value of 2 elements mapped from a key, and keep the key as Tuple.Tuple7
     * of 4 different elements
     *
     * @param <K1> type of first element of the Key
     * @param <K2> type of second element of the Key
     * @param <K3> type of third element of the Key
     * @param <K4> type of fourth element of the Key
     * @param <T>  type of the first element of the Tuple.Tuple1 mapped from the key
     */
    public static class TupleKeys4<K1, K2, K3, K4, T> extends TupleRepository1<Tuple4<K1, K2, K3, K4>, T>
            implements io.github.cruisoring.repository.TupleKeys4.TupleValues1<K1, K2, K3, K4, T> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param map             Factory to get the strong-typed map instance
         * @param changesConsumer Extra steps to be called when any entry updated
         * @param valueFunction   Function to map 4 keys to Tuple of 1 element
         */
        protected TupleKeys4(Map<Tuple4<K1, K2, K3, K4>, Tuple1<T>> map,
                             TriConsumerThrowable<Tuple4<K1, K2, K3, K4>, Tuple1<T>, Tuple1<T>> changesConsumer,
                             QuadFunctionThrowable<K1, K2, K3, K4, Tuple1<T>> valueFunction) {
            super(map, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(), tuple.getFourth()));
        }

        /**
         * Construct a repository with evaluation logic only
         *
         * @param valueFunction Function to map 4 keys to Tuple of 1 element
         */
        protected TupleKeys4(QuadFunctionThrowable<K1, K2, K3, K4, Tuple1<T>> valueFunction) {
            this(new HashMap(), null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple1 to keep value of 2 elements mapped from a key, and keep the key as Tuple.Tuple7
     * of 5 different elements
     *
     * @param <K1> type of first element of the Key
     * @param <K2> type of second element of the Key
     * @param <K3> type of third element of the Key
     * @param <K4> type of fourth element of the Key
     * @param <K5> type of fifth element of the Key
     * @param <T>  type of the first element of the Tuple.Tuple1 mapped from the key
     */
    public static class TupleKeys5<K1, K2, K3, K4, K5, T> extends TupleRepository1<Tuple5<K1, K2, K3, K4, K5>, T>
            implements io.github.cruisoring.repository.TupleKeys5.TupleValues1<K1, K2, K3, K4, K5, T> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param map             Factory to get the strong-typed map instance
         * @param changesConsumer Extra steps to be called when any entry updated
         * @param valueFunction   Function to map 5 keys to Tuple of 1 element
         */
        protected TupleKeys5(Map<Tuple5<K1, K2, K3, K4, K5>, Tuple1<T>> map,
                             TriConsumerThrowable<Tuple5<K1, K2, K3, K4, K5>, Tuple1<T>, Tuple1<T>> changesConsumer,
                             PentaFunctionThrowable<K1, K2, K3, K4, K5, Tuple1<T>> valueFunction) {
            super(map, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth()));
        }

        /**
         * Construct a repository with evaluation logic only
         *
         * @param valueFunction Function to map 5 keys to Tuple of 1 element
         */
        protected TupleKeys5(PentaFunctionThrowable<K1, K2, K3, K4, K5, Tuple1<T>> valueFunction) {
            this(new HashMap(), null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple1 to keep value of 2 elements mapped from a key, and keep the key as Tuple.Tuple7
     * of 6 different elements
     *
     * @param <K1> type of first element of the Key
     * @param <K2> type of second element of the Key
     * @param <K3> type of third element of the Key
     * @param <K4> type of fourth element of the Key
     * @param <K5> type of fifth element of the Key
     * @param <K6> type of sixth element of the Key
     * @param <T>  type of the first element of the Tuple.Tuple1 mapped from the key
     */
    public static class TupleKeys6<K1, K2, K3, K4, K5, K6, T> extends TupleRepository1<Tuple6<K1, K2, K3, K4, K5, K6>, T>
            implements io.github.cruisoring.repository.TupleKeys6.TupleValues1<K1, K2, K3, K4, K5, K6, T> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param map             Factory to get the strong-typed map instance
         * @param changesConsumer Extra steps to be called when any entry updated
         * @param valueFunction   Function to map 6 keys to Tuple of 1 element
         */
        protected TupleKeys6(Map<Tuple6<K1, K2, K3, K4, K5, K6>, Tuple1<T>> map,
                             TriConsumerThrowable<Tuple6<K1, K2, K3, K4, K5, K6>, Tuple1<T>, Tuple1<T>> changesConsumer,
                             HexaFunctionThrowable<K1, K2, K3, K4, K5, K6, Tuple1<T>> valueFunction) {
            super(map, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth()));
        }

        /**
         * Construct a repository with evaluation logic only
         *
         * @param valueFunction Function to map 6 keys to Tuple of 1 element
         */
        protected TupleKeys6(HexaFunctionThrowable<K1, K2, K3, K4, K5, K6, Tuple1<T>> valueFunction) {
            this(new HashMap(), null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Tuple1 to keep value of 2 elements mapped from a key, and keep the key as Tuple.Tuple7
     * of 7 different elements
     *
     * @param <K1> type of first element of the Key
     * @param <K2> type of second element of the Key
     * @param <K3> type of third element of the Key
     * @param <K4> type of fourth element of the Key
     * @param <K5> type of fifth element of the Key
     * @param <K6> type of sixth element of the Key
     * @param <K7> type of seventh element of the Key
     * @param <T>  type of the first element of the Tuple.Tuple1 mapped from the key
     */
    public static class TupleKeys7<K1, K2, K3, K4, K5, K6, K7, T> extends TupleRepository1<Tuple7<K1, K2, K3, K4, K5, K6, K7>, T>
            implements io.github.cruisoring.repository.TupleKeys7.TupleValues1<K1, K2, K3, K4, K5, K6, K7, T> {

        /**
         * Construct a repository with map supplier, extra changesConsumer logic and evaluate logic to map keys to values
         * Notice: Shall be used when there are special cases need to be put first
         *
         * @param map             Factory to get the strong-typed map instance
         * @param changesConsumer Extra steps to be called when any entry updated
         * @param valueFunction   Function to map 7 keys to Tuple of 1 element
         */
        protected TupleKeys7(Map<Tuple7<K1, K2, K3, K4, K5, K6, K7>, Tuple1<T>> map,
                             TriConsumerThrowable<Tuple7<K1, K2, K3, K4, K5, K6, K7>, Tuple1<T>, Tuple1<T>> changesConsumer,
                             HeptaFunctionThrowable<K1, K2, K3, K4, K5, K6, K7, Tuple1<T>> valueFunction) {
            super(map, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth(), tuple.getSeventh()));
        }

        /**
         * Construct a repository with evaluation logic only
         *
         * @param valueFunction Function to map 7 keys to Tuple of 1 element
         */
        protected TupleKeys7(HeptaFunctionThrowable<K1, K2, K3, K4, K5, K6, K7, Tuple1<T>> valueFunction) {
            this(new HashMap(), null, valueFunction);
        }
    }
}
