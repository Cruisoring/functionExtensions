package com.easyworks.repository;

import com.easyworks.function.*;
import com.easyworks.tuple.*;

import java.util.HashMap;
import java.util.Map;

public class SingleValuesRepository<TKey, T> extends Repository<TKey, Single<T>>
        implements SingleValues<TKey, T> {

    protected SingleValuesRepository(SupplierThrowable<Map<TKey, Single<T>>> storageSupplier,
                                     TriConsumerThrowable<TKey, Single<T>, Single<T>> changesConsumer,
                                     FunctionThrowable<TKey, Single<T>> valueFunction){
        super(storageSupplier, changesConsumer, valueFunction);
    }

    protected SingleValuesRepository(FunctionThrowable<TKey, Single<T>> valueFunction){
        this(HashMap::new, null, valueFunction);
    }

    @Override
    public Single<T> retrieve(TKey tKey) {
        return get(tKey, null);
    }

    public static class SingleValuesRepository1<K1, T> extends SingleValuesRepository<Single<K1>, T>
            implements SingleKeys.SingleValues<K1, T> {

        protected SingleValuesRepository1(SupplierThrowable<Map<Single<K1>, Single<T>>> storageSupplier,
                                          TriConsumerThrowable<Single<K1>, Single<T>, Single<T>> changesConsumer,
                                          FunctionThrowable<K1, Single<T>> valueFunction) {
            super(storageSupplier, changesConsumer, single -> valueFunction.apply(single.getFirst()));
        }

        protected SingleValuesRepository1(FunctionThrowable<K1, Single<T>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    public static class SingleValuesRepository2<K1,K2, T> extends SingleValuesRepository<Dual<K1,K2>, T>
            implements DualKeys.SingleValues<K1,K2, T> {

        protected SingleValuesRepository2(SupplierThrowable<Map<Dual<K1,K2>, Single<T>>> storageSupplier,
                                          TriConsumerThrowable<Dual<K1,K2>, Single<T>, Single<T>> changesConsumer,
                                          BiFunctionThrowable<K1, K2, Single<T>> valueFunction) {
            super(storageSupplier, changesConsumer, dual -> valueFunction.apply(dual.getFirst(), dual.getSecond()));
        }

        protected SingleValuesRepository2(BiFunctionThrowable<K1, K2, Single<T>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    public static class SingleValuesRepository3<K1,K2,K3, T> extends SingleValuesRepository<Triple<K1,K2,K3>, T>
            implements TripleKeys.SingleValues<K1,K2,K3, T> {

        protected SingleValuesRepository3(SupplierThrowable<Map<Triple<K1,K2,K3>, Single<T>>> storageSupplier,
                                          TriConsumerThrowable<Triple<K1,K2,K3>, Single<T>, Single<T>> changesConsumer,
                                          TriFunctionThrowable<K1, K2, K3, Single<T>> valueFunction) {
            super(storageSupplier, changesConsumer, triple -> valueFunction.apply(triple.getFirst(), triple.getSecond(), triple.getThird()));
        }

        protected SingleValuesRepository3(TriFunctionThrowable<K1, K2, K3, Single<T>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    public static class SingleValuesRepository4<K1,K2,K3,K4, T> extends SingleValuesRepository<Quad<K1,K2,K3,K4>, T>
            implements QuadKeys.SingleValues<K1,K2,K3,K4, T> {

        protected SingleValuesRepository4(SupplierThrowable<Map<Quad<K1,K2,K3,K4>, Single<T>>> storageSupplier,
                                          TriConsumerThrowable<Quad<K1,K2,K3,K4>, Single<T>, Single<T>> changesConsumer,
                                          QuadFunctionThrowable<K1,K2,K3,K4, Single<T>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(), tuple.getFourth()));
        }

        protected SingleValuesRepository4(QuadFunctionThrowable<K1,K2,K3,K4, Single<T>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    public static class SingleValuesRepository5<K1,K2,K3,K4,K5, T> extends SingleValuesRepository<Penta<K1,K2,K3,K4,K5>, T>
            implements PentaKeys.SingleValues<K1,K2,K3,K4,K5, T> {

        protected SingleValuesRepository5(SupplierThrowable<Map<Penta<K1,K2,K3,K4,K5>, Single<T>>> storageSupplier,
                                          TriConsumerThrowable<Penta<K1,K2,K3,K4,K5>, Single<T>, Single<T>> changesConsumer,
                                          PentaFunctionThrowable<K1,K2,K3,K4,K5, Single<T>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth()));
        }

        protected SingleValuesRepository5(PentaFunctionThrowable<K1,K2,K3,K4,K5, Single<T>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    public static class SingleValuesRepository6<K1,K2,K3,K4,K5,K6, T> extends SingleValuesRepository<Hexa<K1,K2,K3,K4,K5,K6>, T>
            implements HexaKeys.SingleValues<K1,K2,K3,K4,K5,K6, T> {

        protected SingleValuesRepository6(SupplierThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Single<T>>> storageSupplier,
                                          TriConsumerThrowable<Hexa<K1,K2,K3,K4,K5,K6>, Single<T>, Single<T>> changesConsumer,
                                          HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Single<T>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth()));
        }

        protected SingleValuesRepository6(HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Single<T>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    public static class SingleValuesRepository7<K1,K2,K3,K4,K5,K6,K7, T> extends SingleValuesRepository<Hepta<K1,K2,K3,K4,K5,K6,K7>, T>
            implements HeptaKeys.SingleValues<K1,K2,K3,K4,K5,K6,K7, T> {

        protected SingleValuesRepository7(SupplierThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Single<T>>> storageSupplier,
                                          TriConsumerThrowable<Hepta<K1,K2,K3,K4,K5,K6,K7>, Single<T>, Single<T>> changesConsumer,
                                          HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Single<T>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth(), tuple.getSeventh()));
        }

        protected SingleValuesRepository7(HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Single<T>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }
}
