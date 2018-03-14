package com.easyworks.repository;

import com.easyworks.function.*;
import com.easyworks.tuple.*;

import java.util.HashMap;
import java.util.Map;

public class HexaValuesRepository<TKey, T, U, V, W, X, Y> extends Repository<TKey, Hexa<T,U,V,W,X,Y>>
        implements HexaValues<TKey, T,U,V,W,X,Y>{

    protected HexaValuesRepository(SupplierThrowable<Map<TKey, Hexa<T,U,V,W,X,Y>>> storageSupplier,
                                   TriConsumerThrowable<TKey, Hexa<T,U,V,W,X,Y>, Hexa<T,U,V,W,X,Y>> changesConsumer,
                                   FunctionThrowable<TKey, Hexa<T,U,V,W,X,Y>> valueFunction){
        super(storageSupplier, changesConsumer, valueFunction);
    }

    protected HexaValuesRepository(FunctionThrowable<TKey, Hexa<T,U,V,W,X,Y>> valueFunction){
        this(HashMap::new, null, valueFunction);
    }

    @Override
    public Hexa<T, U, V, W, X, Y> retrieve(TKey tKey) {
        return (Hexa<T,U,V,W,X,Y>)get(tKey, null);
    }

    public static class HexaValuesRepository1<K1, T,U,V,W,X,Y> extends HexaValuesRepository<Single<K1>, T,U,V,W,X,Y>
            implements SingleKeys.SingleKeys6<K1, T,U,V,W,X,Y> {

        protected HexaValuesRepository1(SupplierThrowable<Map<Single<K1>, Hexa<T,U,V,W,X,Y>>> storageSupplier,
                                        TriConsumerThrowable<Single<K1>, Hexa<T,U,V,W,X,Y>, Hexa<T,U,V,W,X,Y>> changesConsumer,
                                        FunctionThrowable<K1, Hexa<T,U,V,W,X,Y>> valueFunction) {
            super(storageSupplier, changesConsumer, single -> valueFunction.apply(single.getFirst()));
        }

        protected HexaValuesRepository1(FunctionThrowable<K1, Hexa<T,U,V,W,X,Y>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public boolean containsKeyOf(K1 k1) {
            return containsKey(getKey(k1));
        }
    }

    public static class HexaValuesRepository2<K1,K2, T,U,V,W,X,Y> extends HexaValuesRepository<Dual<K1,K2>, T,U,V,W,X,Y>
            implements DualKeys.DualKeys6<K1,K2, T,U,V,W,X,Y>{

        protected HexaValuesRepository2(SupplierThrowable<Map<Dual<K1,K2>, Hexa<T,U,V,W,X,Y>>> storageSupplier,
                                        TriConsumerThrowable<Dual<K1,K2>, Hexa<T,U,V,W,X,Y>, Hexa<T,U,V,W,X,Y>> changesConsumer,
                                        BiFunctionThrowable<K1, K2, Hexa<T,U,V,W,X,Y>> valueFunction) {
            super(storageSupplier, changesConsumer, dual -> valueFunction.apply(dual.getFirst(), dual.getSecond()));
        }

        protected HexaValuesRepository2(BiFunctionThrowable<K1, K2, Hexa<T,U,V,W,X,Y>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Hexa<T,U,V,W,X,Y> retrieve(K1 k1, K2 k2) {
            return retrieve(getKey(k1, k2));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2) {
            return containsKey(getKey(k1, k2));
        }
    }

    public static class HexaValuesRepository3<K1,K2,K3, T,U,V,W,X,Y> extends HexaValuesRepository<Triple<K1,K2,K3>, T,U,V,W,X,Y>
            implements TripleKeys.TripleKeys6<K1,K2,K3, T,U,V,W,X,Y>{

        protected HexaValuesRepository3(SupplierThrowable<Map<Triple<K1,K2,K3>, Hexa<T,U,V,W,X,Y>>> storageSupplier,
                                        TriConsumerThrowable<Triple<K1,K2,K3>, Hexa<T,U,V,W,X,Y>, Hexa<T,U,V,W,X,Y>> changesConsumer,
                                        TriFunctionThrowable<K1, K2, K3, Hexa<T,U,V,W,X,Y>> valueFunction) {
            super(storageSupplier, changesConsumer, triple -> valueFunction.apply(triple.getFirst(), triple.getSecond(), triple.getThird()));
        }

        protected HexaValuesRepository3(TriFunctionThrowable<K1, K2, K3, Hexa<T,U,V,W,X,Y>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Hexa<T,U,V,W,X,Y> retrieve(K1 k1, K2 k2, K3 k3) {
            return retrieve(getKey(k1, k2, k3));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3) {
            return containsKey(getKey(k1, k2, k3));
        }
    }

    public static class HexaValuesRepository4<K1,K2,K3,K4, T,U,V,W,X,Y> extends HexaValuesRepository<Quad<K1,K2,K3,K4>, T,U,V,W,X,Y>
            implements QuadKeys.QuadKeys6<K1,K2,K3,K4, T,U,V,W,X,Y> {

        protected HexaValuesRepository4(SupplierThrowable<Map<Quad<K1,K2,K3,K4>, Hexa<T,U,V,W,X,Y>>> storageSupplier,
                                        TriConsumerThrowable<Quad<K1,K2,K3,K4>, Hexa<T,U,V,W,X,Y>, Hexa<T,U,V,W,X,Y>> changesConsumer,
                                        QuadFunctionThrowable<K1,K2,K3,K4, Hexa<T,U,V,W,X,Y>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(), tuple.getFourth()));
        }

        protected HexaValuesRepository4(QuadFunctionThrowable<K1,K2,K3,K4, Hexa<T,U,V,W,X,Y>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Hexa<T,U,V,W,X,Y> retrieve(K1 k1, K2 k2, K3 k3, K4 k4) {
            return retrieve(getKey(k1, k2, k3, k4));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4) {
            return containsKey(getKey(k1, k2, k3, k4));
        }
    }

    public static class HexaValuesRepository5<K1,K2,K3,K4,K5, T,U,V,W,X,Y> extends HexaValuesRepository<Penta<K1,K2,K3,K4,K5>, T,U,V,W,X,Y>
            implements PentaKeys.PentaKeys6<K1,K2,K3,K4,K5, T,U,V,W,X,Y> {

        protected HexaValuesRepository5(SupplierThrowable<Map<Penta<K1,K2,K3,K4,K5>, Hexa<T,U,V,W,X,Y>>> storageSupplier,
                                        TriConsumerThrowable<Penta<K1,K2,K3,K4,K5>, Hexa<T,U,V,W,X,Y>, Hexa<T,U,V,W,X,Y>> changesConsumer,
                                        PentaFunctionThrowable<K1,K2,K3,K4,K5, Hexa<T,U,V,W,X,Y>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth()));
        }

        protected HexaValuesRepository5(PentaFunctionThrowable<K1,K2,K3,K4,K5, Hexa<T,U,V,W,X,Y>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Hexa<T,U,V,W,X,Y> retrieve(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return retrieve(getKey(k1, k2, k3, k4, k5));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return containsKey(getKey(k1, k2, k3, k4, k5));
        }
    }

    public static class HexaValuesRepository6<K1,K2,K3,K4,K5,K6, T,U,V,W,X,Y> extends HexaValuesRepository<Hexa<K1,K2,K3,K4,K5,K6>, T,U,V,W,X,Y>
            implements HexaKeys.HexaKeys6<K1,K2,K3,K4,K5,K6, T,U,V,W,X,Y> {

        protected HexaValuesRepository6(SupplierThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Hexa<T,U,V,W,X,Y>>> storageSupplier,
                                        TriConsumerThrowable<Hexa<K1,K2,K3,K4,K5,K6>, Hexa<T,U,V,W,X,Y>, Hexa<T,U,V,W,X,Y>> changesConsumer,
                                        HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Hexa<T,U,V,W,X,Y>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth()));
        }

        protected HexaValuesRepository6(HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Hexa<T,U,V,W,X,Y>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Hexa<T,U,V,W,X,Y> retrieve(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return retrieve(getKey(k1, k2, k3, k4, k5, k6));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return containsKey(getKey(k1, k2, k3, k4, k5, k6));
        }
    }

    public static class HexaValuesRepository7<K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X,Y> extends HexaValuesRepository<Hepta<K1,K2,K3,K4,K5,K6,K7>, T,U,V,W,X,Y>
            implements HeptaKeys.HeptaKeys6<K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X,Y> {

        protected HexaValuesRepository7(SupplierThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Hexa<T,U,V,W,X,Y>>> storageSupplier,
                                        TriConsumerThrowable<Hepta<K1,K2,K3,K4,K5,K6,K7>, Hexa<T,U,V,W,X,Y>, Hexa<T,U,V,W,X,Y>> changesConsumer,
                                        HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Hexa<T,U,V,W,X,Y>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth(), tuple.getSeventh()));
        }

        protected HexaValuesRepository7(HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Hexa<T,U,V,W,X,Y>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Hexa<T,U,V,W,X,Y> retrieve(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return retrieve(getKey(k1, k2, k3, k4, k5, k6, k7));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return containsKey(getKey(k1, k2, k3, k4, k5, k6, k7));
        }
    }

}
