package com.easyworks.repository;

import com.easyworks.function.*;
import com.easyworks.tuple.*;

import java.util.HashMap;
import java.util.Map;

public class PentaValuesRepository<TKey, T, U, V, W, X> extends Repository<TKey, Penta<T,U,V,W,X>>
        implements PentaValues<TKey, T,U,V,W,X>{

    protected PentaValuesRepository(SupplierThrowable<Map<TKey, Penta<T,U,V,W,X>>> storageSupplier,
                                    TriConsumerThrowable<TKey, Penta<T,U,V,W,X>, Penta<T,U,V,W,X>> changesConsumer,
                                    FunctionThrowable<TKey, Penta<T,U,V,W,X>> valueFunction){
        super(storageSupplier, changesConsumer, valueFunction);
    }

    protected PentaValuesRepository(FunctionThrowable<TKey, Penta<T,U,V,W,X>> valueFunction){
        this(HashMap::new, null, valueFunction);
    }

    @Override
    public Penta<T, U, V, W, X> retrieve(TKey tKey) {
        return (Penta<T,U,V,W,X>)get(tKey, null);
    }

    public static class PentaValuesRepository1<K1, T,U,V,W,X> extends PentaValuesRepository<Single<K1>, T,U,V,W,X>
            implements SingleKeys.SingleKeys5<K1, T,U,V,W,X> {

        protected PentaValuesRepository1(SupplierThrowable<Map<Single<K1>, Penta<T,U,V,W,X>>> storageSupplier,
                                         TriConsumerThrowable<Single<K1>, Penta<T,U,V,W,X>, Penta<T,U,V,W,X>> changesConsumer,
                                         FunctionThrowable<K1, Penta<T,U,V,W,X>> valueFunction) {
            super(storageSupplier, changesConsumer, single -> valueFunction.apply(single.getFirst()));
        }

        protected PentaValuesRepository1(FunctionThrowable<K1, Penta<T,U,V,W,X>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public boolean containsKeyOf(K1 k1) {
            return containsKey(getKey(k1));
        }
    }

    public static class PentaValuesRepository2<K1,K2, T,U,V,W,X> extends PentaValuesRepository<Dual<K1,K2>, T,U,V,W,X>
            implements DualKeys.DualKeys5<K1,K2, T,U,V,W,X>{

        protected PentaValuesRepository2(SupplierThrowable<Map<Dual<K1,K2>, Penta<T,U,V,W,X>>> storageSupplier,
                                         TriConsumerThrowable<Dual<K1,K2>, Penta<T,U,V,W,X>, Penta<T,U,V,W,X>> changesConsumer,
                                         BiFunctionThrowable<K1, K2, Penta<T,U,V,W,X>> valueFunction) {
            super(storageSupplier, changesConsumer, dual -> valueFunction.apply(dual.getFirst(), dual.getSecond()));
        }

        protected PentaValuesRepository2(BiFunctionThrowable<K1, K2, Penta<T,U,V,W,X>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Penta<T,U,V,W,X> retrieve(K1 k1, K2 k2) {
            return retrieve(getKey(k1, k2));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2) {
            return containsKey(getKey(k1, k2));
        }
    }

    public static class PentaValuesRepository3<K1,K2,K3, T,U,V,W,X> extends PentaValuesRepository<Triple<K1,K2,K3>, T,U,V,W,X>
            implements TripleKeys.TripleKeys5<K1,K2,K3, T,U,V,W,X>{

        protected PentaValuesRepository3(SupplierThrowable<Map<Triple<K1,K2,K3>, Penta<T,U,V,W,X>>> storageSupplier,
                                         TriConsumerThrowable<Triple<K1,K2,K3>, Penta<T,U,V,W,X>, Penta<T,U,V,W,X>> changesConsumer,
                                         TriFunctionThrowable<K1, K2, K3, Penta<T,U,V,W,X>> valueFunction) {
            super(storageSupplier, changesConsumer, triple -> valueFunction.apply(triple.getFirst(), triple.getSecond(), triple.getThird()));
        }

        protected PentaValuesRepository3(TriFunctionThrowable<K1, K2, K3, Penta<T,U,V,W,X>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Penta<T,U,V,W,X> retrieve(K1 k1, K2 k2, K3 k3) {
            return retrieve(getKey(k1, k2, k3));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3) {
            return containsKey(getKey(k1, k2, k3));
        }
    }

    public static class PentaValuesRepository4<K1,K2,K3,K4, T,U,V,W,X> extends PentaValuesRepository<Quad<K1,K2,K3,K4>, T,U,V,W,X>
            implements QuadKeys.QuadKeys5<K1,K2,K3,K4, T,U,V,W,X> {

        protected PentaValuesRepository4(SupplierThrowable<Map<Quad<K1,K2,K3,K4>, Penta<T,U,V,W,X>>> storageSupplier,
                                         TriConsumerThrowable<Quad<K1,K2,K3,K4>, Penta<T,U,V,W,X>, Penta<T,U,V,W,X>> changesConsumer,
                                         QuadFunctionThrowable<K1,K2,K3,K4, Penta<T,U,V,W,X>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(), tuple.getFourth()));
        }

        protected PentaValuesRepository4(QuadFunctionThrowable<K1,K2,K3,K4, Penta<T,U,V,W,X>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Penta<T,U,V,W,X> retrieve(K1 k1, K2 k2, K3 k3, K4 k4) {
            return retrieve(getKey(k1, k2, k3, k4));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4) {
            return containsKey(getKey(k1, k2, k3, k4));
        }
    }

    public static class PentaValuesRepository5<K1,K2,K3,K4,K5, T,U,V,W,X> extends PentaValuesRepository<Penta<K1,K2,K3,K4,K5>, T,U,V,W,X>
            implements PentaKeys.PentaKeys5<K1,K2,K3,K4,K5, T,U,V,W,X> {

        protected PentaValuesRepository5(SupplierThrowable<Map<Penta<K1,K2,K3,K4,K5>, Penta<T,U,V,W,X>>> storageSupplier,
                                         TriConsumerThrowable<Penta<K1,K2,K3,K4,K5>, Penta<T,U,V,W,X>, Penta<T,U,V,W,X>> changesConsumer,
                                         PentaFunctionThrowable<K1,K2,K3,K4,K5, Penta<T,U,V,W,X>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth()));
        }

        protected PentaValuesRepository5(PentaFunctionThrowable<K1,K2,K3,K4,K5, Penta<T,U,V,W,X>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Penta<T,U,V,W,X> retrieve(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return retrieve(getKey(k1, k2, k3, k4, k5));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return containsKey(getKey(k1, k2, k3, k4, k5));
        }
    }

    public static class PentaValuesRepository6<K1,K2,K3,K4,K5,K6, T,U,V,W,X> extends PentaValuesRepository<Hexa<K1,K2,K3,K4,K5,K6>, T,U,V,W,X>
            implements HexaKeys.HexaKeys5<K1,K2,K3,K4,K5,K6, T,U,V,W,X> {

        protected PentaValuesRepository6(SupplierThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Penta<T,U,V,W,X>>> storageSupplier,
                                         TriConsumerThrowable<Hexa<K1,K2,K3,K4,K5,K6>, Penta<T,U,V,W,X>, Penta<T,U,V,W,X>> changesConsumer,
                                         HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Penta<T,U,V,W,X>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth()));
        }

        protected PentaValuesRepository6(HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Penta<T,U,V,W,X>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Penta<T,U,V,W,X> retrieve(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return retrieve(getKey(k1, k2, k3, k4, k5, k6));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return containsKey(getKey(k1, k2, k3, k4, k5, k6));
        }
    }

    public static class PentaValuesRepository7<K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X> extends PentaValuesRepository<Hepta<K1,K2,K3,K4,K5,K6,K7>, T,U,V,W,X>
            implements HeptaKeys.HeptaKeys5<K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X> {

        protected PentaValuesRepository7(SupplierThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Penta<T,U,V,W,X>>> storageSupplier,
                                         TriConsumerThrowable<Hepta<K1,K2,K3,K4,K5,K6,K7>, Penta<T,U,V,W,X>, Penta<T,U,V,W,X>> changesConsumer,
                                         HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Penta<T,U,V,W,X>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth(), tuple.getSeventh()));
        }

        protected PentaValuesRepository7(HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Penta<T,U,V,W,X>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Penta<T,U,V,W,X> retrieve(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return retrieve(getKey(k1, k2, k3, k4, k5, k6, k7));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return containsKey(getKey(k1, k2, k3, k4, k5, k6, k7));
        }
    }

}
