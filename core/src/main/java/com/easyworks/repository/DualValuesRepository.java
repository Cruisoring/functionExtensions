package com.easyworks.repository;

import com.easyworks.function.*;
import com.easyworks.tuple.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic repository use Tuple.Dual to keep 2 elements mapped from a key
 * Notice: the actual type of the Key is TKey itself
 * @param <TKey>    type of the Key
 * @param <T>       type of the first element of the Tuple.Dual that is mapped from the key
 * @param <U>       type of the second element of the Tuple.Dual that is mapped from the key
 */
public class DualValuesRepository<TKey, T, U> extends Repository<TKey, Dual<T,U>>
        implements DualValues<TKey, T, U> {
    /**
     * Construct a repository with given map factory, extra changesConsumer logic and Function to map key to value
     * Shall be considerer only when the Tuple value contains more than 7 elements
     * @param storageSupplier   Factory to get a map instance
     * @param changesConsumer           Extra steps to run before reset() being called.
     * @param valueFunction     Function to map key of <tt>TKey<tt> type to value of <tt>Tuple.Dual<tt> type
     */
    protected DualValuesRepository(SupplierThrowable<Map<TKey, Dual<T,U>>> storageSupplier,
                                   TriConsumerThrowable<TKey, Dual<T,U>, Dual<T,U>> changesConsumer,
                                   FunctionThrowable<TKey, Dual<T, U>> valueFunction){
        super(storageSupplier, changesConsumer, valueFunction);
    }

    /**
     * Construct a repository with given map factory
     * @param valueFunction     Function to map key of <tt>TKey<tt> type to value of <tt>Tuple.Dual<tt> type
     */
    protected DualValuesRepository(FunctionThrowable<TKey, Dual<T, U>> valueFunction){
        this(HashMap::new, null, valueFunction);
    }

    @Override
    public Dual<T,U> retrieve(TKey tKey) {
        return get(tKey, null);
    }

    /**
     * Generic repository use Tuple.Dual to keep value of 2 elements mapped from a key, and keep the key as Tuple.Single
     * Notice: the actual type of the Key is Tuple.Single wrapping the actual value of <tt>K1</tt>
     * @param <K1>  type of the Key
     * @param <T>   type of the first element of the Tuple.Dual that is mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual that is mapped from the key
     */
    public static class DualValuesRepository1<K1, T,U> extends DualValuesRepository<Single<K1>, T,U>
            implements SingleKeys.DualValues<K1, T, U> {

        /**
         * Construct a repository with given map factory, extra changesConsumer logic and Function to map key to value of 2 elements
         * @param storageSupplier   Factory to get a map instance
         * @param changesConsumer           Extra steps to run before reset() being called.
         * @param valueFunction     Function to map key of <tt>K1<tt> type to value of <tt>Tuple.Dual<tt> type
         */
        protected DualValuesRepository1(SupplierThrowable<Map<Single<K1>, Dual<T,U>>> storageSupplier,
                                        TriConsumerThrowable<Single<K1>, Dual<T,U>, Dual<T,U>> changesConsumer,
                                        FunctionThrowable<K1, Dual<T, U>> valueFunction) {
            super(storageSupplier, changesConsumer, single -> valueFunction.apply(single.getFirst()));
        }

        /**
         * Construct a repository with given map factory
         * @param valueFunction     Function to map key of <tt>K1<tt> type to value of <tt>Tuple.Dual<tt> type
         */
        protected DualValuesRepository1(FunctionThrowable<K1, Dual<T, U>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Dual to keep value of 2 elements mapped from a key, and keep the key as Tuple.Hepta
     * of 2 different elements
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <T>   type of the first element of the Tuple.Dual that is mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual that is mapped from the key
     */
    public static class DualValuesRepository2<K1,K2, T,U> extends DualValuesRepository<Dual<K1,K2>, T,U>
            implements DualKeys.DualValues<K1,K2, T,U> {

        /**
         * Construct a repository with given map factory, extra changesConsumer logic and Function to map key of 2 elements to value of 2 elements
         * @param storageSupplier   Factory to get a map instance
         * @param changesConsumer           Extra steps to run before reset() being called.
         * @param valueFunction     Function to map key of 2 elements to value of 2 elements
         */
        protected DualValuesRepository2(SupplierThrowable<Map<Dual<K1,K2>, Dual<T,U>>> storageSupplier,
                                        TriConsumerThrowable<Dual<K1,K2>, Dual<T,U>, Dual<T,U>> changesConsumer,
                                        BiFunctionThrowable<K1, K2, Dual<T, U>> valueFunction) {
            super(storageSupplier, changesConsumer, dual -> valueFunction.apply(dual.getFirst(), dual.getSecond()));
        }

        /**
         * Construct a repository with Function to map key of 2 elements to value of 2 elements
         * @param valueFunction     Function to map key of 2 elements to value of 2 elements
         */
        protected DualValuesRepository2(BiFunctionThrowable<K1, K2, Dual<T, U>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Dual to keep value of 2 elements mapped from a key, and keep the key as Tuple.Hepta
     * of 3 different elements
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <T>   type of the first element of the Tuple.Dual that is mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual that is mapped from the key
     */
    public static class DualValuesRepository3<K1,K2,K3, T,U> extends DualValuesRepository<Triple<K1,K2,K3>, T,U>
            implements TripleKeys.DualValues<K1,K2,K3, T,U>{

        /**
         * Construct a repository with given map factory, extra changesConsumer logic and Function to map key of 3 elements to value of 2 elements
         * @param storageSupplier   Factory to get a map instance
         * @param changesConsumer           Extra steps to run before reset() being called.
         * @param valueFunction     Function to map key of 3 elements to value of 2 elements
         */
        protected DualValuesRepository3(SupplierThrowable<Map<Triple<K1,K2,K3>, Dual<T,U>>> storageSupplier,
                                        TriConsumerThrowable<Triple<K1,K2,K3>, Dual<T,U>, Dual<T,U>> changesConsumer,
                                        TriFunctionThrowable<K1, K2, K3, Dual<T, U>> valueFunction) {
            super(storageSupplier, changesConsumer, triple -> valueFunction.apply(triple.getFirst(), triple.getSecond(), triple.getThird()));
        }

        /**
         * Construct a repository with Function to map key of 3 elements to value of 2 elements
         * @param valueFunction     Function to map key of 3 elements to value of 2 elements
         */
        protected DualValuesRepository3(TriFunctionThrowable<K1, K2, K3, Dual<T, U>> valueFunction) {
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
     * @param <T>   type of the first element of the Tuple.Dual that is mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual that is mapped from the key
     */
    public static class DualValuesRepository4<K1,K2,K3,K4, T,U> extends DualValuesRepository<Quad<K1,K2,K3,K4>, T,U>
            implements QuadKeys.DualValues<K1,K2,K3,K4, T,U> {

        /**
         * Construct a repository with given map factory, extra changesConsumer logic and Function to map key of 4 elements to value of 2 elements
         * @param storageSupplier   Factory to get a map instance
         * @param changesConsumer           Extra steps to run before reset() being called.
         * @param valueFunction     Function to map key of 4 elements to value of 2 elements
         */
        protected DualValuesRepository4(SupplierThrowable<Map<Quad<K1,K2,K3,K4>, Dual<T,U>>> storageSupplier,
                                        TriConsumerThrowable<Quad<K1,K2,K3,K4>, Dual<T,U>, Dual<T,U>> changesConsumer,
                                        QuadFunctionThrowable<K1,K2,K3,K4, Dual<T, U>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(), tuple.getFourth()));
        }

        /**
         * Construct a repository with Function to map key of 4 elements to value of 2 elements
         * @param valueFunction     Function to map key of 4 elements to value of 2 elements
         */
        protected DualValuesRepository4(QuadFunctionThrowable<K1,K2,K3,K4, Dual<T, U>> valueFunction) {
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
     * @param <T>   type of the first element of the Tuple.Dual that is mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual that is mapped from the key
     */
    public static class DualValuesRepository5<K1,K2,K3,K4,K5, T,U> extends DualValuesRepository<Penta<K1,K2,K3,K4,K5>, T,U>
            implements PentaKeys.DualValues<K1,K2,K3,K4,K5, T,U> {

        /**
         * Construct a repository with given map factory, extra changesConsumer logic and Function to map key of 5 elements to value of 2 elements
         * @param storageSupplier   Factory to get a map instance
         * @param changesConsumer           Extra steps to run before reset() being called.
         * @param valueFunction     Function to map key of 5 elements to value of 2 elements
         */
        protected DualValuesRepository5(SupplierThrowable<Map<Penta<K1,K2,K3,K4,K5>, Dual<T,U>>> storageSupplier,
                                        TriConsumerThrowable<Penta<K1,K2,K3,K4,K5>, Dual<T,U>, Dual<T,U>> changesConsumer,
                                        PentaFunctionThrowable<K1,K2,K3,K4,K5, Dual<T, U>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth()));
        }

        /**
         * Construct a repository with Function to map key of 5 elements to value of 2 elements
         * @param valueFunction     Function to map key of 5 elements to value of 2 elements
         */
        protected DualValuesRepository5(PentaFunctionThrowable<K1,K2,K3,K4,K5, Dual<T, U>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Dual to keep value of 2 elements mapped from a key, and keep the key as Tuple.Hepta
     * of 6 different elements
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <K4>  type of fourth element of the Key
     * @param <K5>  type of fifth element of the Key
     * @param <K6>  type of sixth element of the Key
     * @param <T>   type of the first element of the Tuple.Dual that is mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual that is mapped from the key
     */
    public static class DualValuesRepository6<K1,K2,K3,K4,K5,K6, T,U> extends DualValuesRepository<Hexa<K1,K2,K3,K4,K5,K6>, T,U>
            implements HexaKeys.DualValues<K1,K2,K3,K4,K5,K6, T,U> {

        /**
         * Construct a repository with given map factory, extra changesConsumer logic and Function to map key of 6 elements to value of 2 elements
         * @param storageSupplier   Factory to get a map instance
         * @param changesConsumer           Extra steps to run before reset() being called.
         * @param valueFunction     Function to map key of 6 elements to value of 2 elements
         */
        protected DualValuesRepository6(SupplierThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Dual<T,U>>> storageSupplier,
                                        TriConsumerThrowable<Hexa<K1,K2,K3,K4,K5,K6>, Dual<T,U>, Dual<T,U>> changesConsumer,
                                        HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Dual<T, U>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth()));
        }

        /**
         * Construct a repository with Function to map key of 6 elements to value of 2 elements
         * @param valueFunction     Function to map key of 6 elements to value of 2 elements
         */
        protected DualValuesRepository6(HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Dual<T, U>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    /**
     * Generic repository use Tuple.Dual to keep value of 2 elements mapped from a key, and keep the key as Tuple.Hepta
     * of 7 different elements
     * @param <K1>  type of first element of the Key
     * @param <K2>  type of second element of the Key
     * @param <K3>  type of third element of the Key
     * @param <K4>  type of fourth element of the Key
     * @param <K5>  type of fifth element of the Key
     * @param <K6>  type of sixth element of the Key
     * @param <K7>  type of seventh element of the Key
     * @param <T>   type of the first element of the Tuple.Dual that is mapped from the key
     * @param <U>   type of the second element of the Tuple.Dual that is mapped from the key
     */
    public static class DualValuesRepository7<K1,K2,K3,K4,K5,K6,K7, T,U> extends DualValuesRepository<Hepta<K1,K2,K3,K4,K5,K6,K7>, T,U>
            implements HeptaKeys.DualValues<K1,K2,K3,K4,K5,K6,K7, T,U> {

        /**
         * Construct a repository with given map factory, extra changesConsumer logic and Function to map key of 7 elements to value of 2 elements
         * @param storageSupplier   Factory to get a map instance
         * @param changesConsumer           Extra steps to run before reset() being called.
         * @param valueFunction     Function to map key of 7 elements to value of 2 elements
         */
        protected DualValuesRepository7(SupplierThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Dual<T,U>>> storageSupplier,
                                        TriConsumerThrowable<Hepta<K1,K2,K3,K4,K5,K6,K7>, Dual<T,U>, Dual<T,U>> changesConsumer,
                                        HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Dual<T, U>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth(), tuple.getSeventh()));
        }

        /**
         * Construct a repository with Function to map key of 7 elements to value of 2 elements
         * @param valueFunction     Function to map key of 7 elements to value of 2 elements
         */
        protected DualValuesRepository7(HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Dual<T, U>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

}
