package com.easyworks.repository;

import com.easyworks.function.*;
import com.easyworks.tuple.*;

import java.util.HashMap;
import java.util.Map;

public class TripleValuesRepository<TKey, T, U, V> extends Repository<TKey, Triple<T,U,V>>
    implements TripleValues<TKey, T,U,V> {

    protected TripleValuesRepository(SupplierThrowable<Map<TKey, Triple<T,U,V>>> storageSupplier,
                                     TriConsumerThrowable<TKey, Triple<T,U,V>, Triple<T,U,V>> changesConsumer,
                                     FunctionThrowable<TKey, Triple<T,U,V>> valueFunction){
        super(storageSupplier, changesConsumer, valueFunction);
    }

    protected TripleValuesRepository(FunctionThrowable<TKey, Triple<T,U,V>> valueFunction){
        this(HashMap::new, null, valueFunction);
    }

    @Override
    public Triple<T, U, V> retrieve(TKey tKey) {
        return (Triple<T,U,V>)get(tKey, null);
    }

    public static class TripleValuesRepository1<K1, T,U,V> extends TripleValuesRepository<Single<K1>, T,U,V>
            implements SingleKeys.SingleKeys3<K1, T,U,V> {

        protected TripleValuesRepository1(SupplierThrowable<Map<Single<K1>, Triple<T,U,V>>> storageSupplier,
                                          TriConsumerThrowable<Single<K1>, Triple<T,U,V>, Triple<T,U,V>> changesConsumer,
                                          FunctionThrowable<K1, Triple<T,U,V>> valueFunction) {
            super(storageSupplier, changesConsumer, single -> valueFunction.apply(single.getFirst()));
        }

        protected TripleValuesRepository1(FunctionThrowable<K1, Triple<T,U,V>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public boolean containsKeyOf(K1 k1) {
            return containsKey(getKey(k1));
        }
    }

    public static class TripleValuesRepository2<K1,K2, T,U,V> extends TripleValuesRepository<Dual<K1,K2>, T,U,V>
            implements DualKeys.DualKeys3<K1,K2, T,U,V>{

        protected TripleValuesRepository2(SupplierThrowable<Map<Dual<K1,K2>, Triple<T,U,V>>> storageSupplier,
                                          TriConsumerThrowable<Dual<K1,K2>, Triple<T,U,V>, Triple<T,U,V>> changesConsumer,
                                          BiFunctionThrowable<K1, K2, Triple<T,U,V>> valueFunction) {
            super(storageSupplier, changesConsumer, dual -> valueFunction.apply(dual.getFirst(), dual.getSecond()));
        }

        protected TripleValuesRepository2(BiFunctionThrowable<K1, K2, Triple<T,U,V>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Triple<T,U,V> retrieve(K1 k1, K2 k2) {
            return retrieve(getKey(k1, k2));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2) {
            return containsKey(getKey(k1, k2));
        }
    }

    public static class TripleValuesRepository3<K1,K2,K3, T,U,V> extends TripleValuesRepository<Triple<K1,K2,K3>, T,U,V>
            implements TripleKeys.TripleKeys3<K1,K2,K3, T,U,V>{

        protected TripleValuesRepository3(SupplierThrowable<Map<Triple<K1,K2,K3>, Triple<T,U,V>>> storageSupplier,
                                          TriConsumerThrowable<Triple<K1,K2,K3>, Triple<T,U,V>, Triple<T,U,V>> changesConsumer,
                                          TriFunctionThrowable<K1, K2, K3, Triple<T,U,V>> valueFunction) {
            super(storageSupplier, changesConsumer, triple -> valueFunction.apply(triple.getFirst(), triple.getSecond(), triple.getThird()));
        }

        protected TripleValuesRepository3(TriFunctionThrowable<K1, K2, K3, Triple<T,U,V>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Triple<T,U,V> retrieve(K1 k1, K2 k2, K3 k3) {
            return retrieve(getKey(k1, k2, k3));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3) {
            return containsKey(getKey(k1, k2, k3));
        }
    }

    public static class TripleValuesRepository4<K1,K2,K3,K4, T,U,V> extends TripleValuesRepository<Quad<K1,K2,K3,K4>, T,U,V>
            implements QuadKeys.QuadKeys3<K1,K2,K3,K4, T,U,V> {

        protected TripleValuesRepository4(SupplierThrowable<Map<Quad<K1,K2,K3,K4>, Triple<T,U,V>>> storageSupplier,
                                          TriConsumerThrowable<Quad<K1,K2,K3,K4>, Triple<T,U,V>, Triple<T,U,V>> changesConsumer,
                                          QuadFunctionThrowable<K1,K2,K3,K4, Triple<T,U,V>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(), tuple.getFourth()));
        }

        protected TripleValuesRepository4(QuadFunctionThrowable<K1,K2,K3,K4, Triple<T,U,V>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Triple<T,U,V> retrieve(K1 k1, K2 k2, K3 k3, K4 k4) {
            return retrieve(getKey(k1, k2, k3, k4));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4) {
            return containsKey(getKey(k1, k2, k3, k4));
        }
    }

    public static class TripleValuesRepository5<K1,K2,K3,K4,K5, T,U,V> extends TripleValuesRepository<Penta<K1,K2,K3,K4,K5>, T,U,V>
            implements PentaKeys.PentaKeys3<K1,K2,K3,K4,K5, T,U,V> {

        protected TripleValuesRepository5(SupplierThrowable<Map<Penta<K1,K2,K3,K4,K5>, Triple<T,U,V>>> storageSupplier,
                                          TriConsumerThrowable<Penta<K1,K2,K3,K4,K5>, Triple<T,U,V>, Triple<T,U,V>> changesConsumer,
                                          PentaFunctionThrowable<K1,K2,K3,K4,K5, Triple<T,U,V>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth()));
        }

        protected TripleValuesRepository5(PentaFunctionThrowable<K1,K2,K3,K4,K5, Triple<T,U,V>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Triple<T,U,V> retrieve(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return retrieve(getKey(k1, k2, k3, k4, k5));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return containsKey(getKey(k1, k2, k3, k4, k5));
        }
    }

    public static class TripleValuesRepository6<K1,K2,K3,K4,K5,K6, T,U,V> extends TripleValuesRepository<Hexa<K1,K2,K3,K4,K5,K6>, T,U,V>
            implements HexaKeys.HexaKeys3<K1,K2,K3,K4,K5,K6, T,U,V> {

        protected TripleValuesRepository6(SupplierThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Triple<T,U,V>>> storageSupplier,
                                          TriConsumerThrowable<Hexa<K1,K2,K3,K4,K5,K6>, Triple<T,U,V>, Triple<T,U,V>> changesConsumer,
                                          HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Triple<T,U,V>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth()));
        }

        protected TripleValuesRepository6(HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Triple<T,U,V>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Triple<T,U,V> retrieve(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return retrieve(getKey(k1, k2, k3, k4, k5, k6));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return containsKey(getKey(k1, k2, k3, k4, k5, k6));
        }
    }

    public static class TripleValuesRepository7<K1,K2,K3,K4,K5,K6,K7, T,U,V> extends TripleValuesRepository<Hepta<K1,K2,K3,K4,K5,K6,K7>, T,U,V>
            implements HeptaKeys.HeptaKeys3<K1,K2,K3,K4,K5,K6,K7, T,U,V> {

        protected TripleValuesRepository7(SupplierThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Triple<T,U,V>>> storageSupplier,
                                          TriConsumerThrowable<Hepta<K1,K2,K3,K4,K5,K6,K7>, Triple<T,U,V>, Triple<T,U,V>> changesConsumer,
                                          HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Triple<T,U,V>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth(), tuple.getSeventh()));
        }

        protected TripleValuesRepository7(HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Triple<T,U,V>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Triple<T,U,V> retrieve(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return retrieve(getKey(k1, k2, k3, k4, k5, k6, k7));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return containsKey(getKey(k1, k2, k3, k4, k5, k6, k7));
        }
    }

}
