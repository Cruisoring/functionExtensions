package com.easyworks.repository;

import com.easyworks.function.*;
import com.easyworks.tuple.*;

import java.util.HashMap;
import java.util.Map;

public class QuadValuesRepository<TKey, T, U, V, W> extends Repository<TKey, Quad<T,U,V,W>>
        implements QuadValues<TKey, T,U,V,W> {

    protected QuadValuesRepository(SupplierThrowable<Map<TKey, Quad<T,U,V,W>>> storageSupplier,
                                   TriConsumerThrowable<TKey, Quad<T,U,V,W>, Quad<T,U,V,W>> changesConsumer,
                                   FunctionThrowable<TKey, Quad<T, U, V, W>> valueFunction){
        super(storageSupplier, changesConsumer, valueFunction);
    }

    protected QuadValuesRepository(FunctionThrowable<TKey, Quad<T, U, V, W>> valueFunction){
        this(HashMap::new, null, valueFunction);
    }

    @Override
    public Quad<T, U, V, W> retrieve(TKey tKey) {
        return get(tKey, null);
    }

    public static class QuadValuesRepository1<K1, T,U,V,W> extends QuadValuesRepository<Single<K1>, T,U,V,W>
            implements SingleKeys.QuadValues<K1, T,U,V,W> {

        protected QuadValuesRepository1(SupplierThrowable<Map<Single<K1>, Quad<T,U,V,W>>> storageSupplier,
                                        TriConsumerThrowable<Single<K1>, Quad<T,U,V,W>, Quad<T,U,V,W>> changesConsumer,
                                        FunctionThrowable<K1, Quad<T,U,V,W>> valueFunction) {
            super(storageSupplier, changesConsumer, single -> valueFunction.apply(single.getFirst()));
        }

        protected QuadValuesRepository1(FunctionThrowable<K1, Quad<T,U,V,W>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    public static class QuadValuesRepository2<K1,K2, T,U,V,W> extends QuadValuesRepository<Dual<K1,K2>, T,U,V,W>
            implements DualKeys.QuadValues<K1,K2, T,U,V,W>{

        protected QuadValuesRepository2(SupplierThrowable<Map<Dual<K1,K2>, Quad<T,U,V,W>>> storageSupplier,
                                        TriConsumerThrowable<Dual<K1,K2>, Quad<T,U,V,W>, Quad<T,U,V,W>> changesConsumer,
                                        BiFunctionThrowable<K1, K2, Quad<T,U,V,W>> valueFunction) {
            super(storageSupplier, changesConsumer, dual -> valueFunction.apply(dual.getFirst(), dual.getSecond()));
        }

        protected QuadValuesRepository2(BiFunctionThrowable<K1, K2, Quad<T,U,V,W>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    public static class QuadValuesRepository3<K1,K2,K3, T,U,V,W> extends QuadValuesRepository<Triple<K1,K2,K3>, T,U,V,W>
            implements TripleKeys.QuadValues<K1,K2,K3, T,U,V,W>{

        protected QuadValuesRepository3(SupplierThrowable<Map<Triple<K1,K2,K3>, Quad<T,U,V,W>>> storageSupplier,
                                        TriConsumerThrowable<Triple<K1,K2,K3>, Quad<T,U,V,W>, Quad<T,U,V,W>> changesConsumer,
                                        TriFunctionThrowable<K1, K2, K3, Quad<T,U,V,W>> valueFunction) {
            super(storageSupplier, changesConsumer, triple -> valueFunction.apply(triple.getFirst(), triple.getSecond(), triple.getThird()));
        }

        protected QuadValuesRepository3(TriFunctionThrowable<K1, K2, K3, Quad<T,U,V,W>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    public static class QuadValuesRepository4<K1,K2,K3,K4, T,U,V,W> extends QuadValuesRepository<Quad<K1,K2,K3,K4>, T,U,V,W>
            implements QuadKeys.QuadValues<K1,K2,K3,K4, T,U,V,W> {

        protected QuadValuesRepository4(SupplierThrowable<Map<Quad<K1,K2,K3,K4>, Quad<T,U,V,W>>> storageSupplier,
                                        TriConsumerThrowable<Quad<K1,K2,K3,K4>, Quad<T,U,V,W>, Quad<T,U,V,W>> changesConsumer,
                                        QuadFunctionThrowable<K1,K2,K3,K4, Quad<T,U,V,W>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(), tuple.getFourth()));
        }

        protected QuadValuesRepository4(QuadFunctionThrowable<K1,K2,K3,K4, Quad<T,U,V,W>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    public static class QuadValuesRepository5<K1,K2,K3,K4,K5, T,U,V,W> extends QuadValuesRepository<Penta<K1,K2,K3,K4,K5>, T,U,V,W>
            implements PentaKeys.QuadValues<K1,K2,K3,K4,K5, T,U,V,W> {

        protected QuadValuesRepository5(SupplierThrowable<Map<Penta<K1,K2,K3,K4,K5>, Quad<T,U,V,W>>> storageSupplier,
                                        TriConsumerThrowable<Penta<K1,K2,K3,K4,K5>, Quad<T,U,V,W>, Quad<T,U,V,W>> changesConsumer,
                                        PentaFunctionThrowable<K1,K2,K3,K4,K5, Quad<T,U,V,W>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth()));
        }

        protected QuadValuesRepository5(PentaFunctionThrowable<K1,K2,K3,K4,K5, Quad<T,U,V,W>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    public static class QuadValuesRepository6<K1,K2,K3,K4,K5,K6, T,U,V,W> extends QuadValuesRepository<Hexa<K1,K2,K3,K4,K5,K6>, T,U,V,W>
            implements HexaKeys.QuadValues<K1,K2,K3,K4,K5,K6, T,U,V,W> {

        protected QuadValuesRepository6(SupplierThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Quad<T,U,V,W>>> storageSupplier,
                                        TriConsumerThrowable<Hexa<K1,K2,K3,K4,K5,K6>, Quad<T,U,V,W>, Quad<T,U,V,W>> changesConsumer,
                                        HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Quad<T,U,V,W>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth()));
        }

        protected QuadValuesRepository6(HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Quad<T,U,V,W>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    public static class QuadValuesRepository7<K1,K2,K3,K4,K5,K6,K7, T,U,V,W> extends QuadValuesRepository<Hepta<K1,K2,K3,K4,K5,K6,K7>, T,U,V,W>
            implements HeptaKeys.QuadValues<K1,K2,K3,K4,K5,K6,K7, T,U,V,W> {

        protected QuadValuesRepository7(SupplierThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Quad<T,U,V,W>>> storageSupplier,
                                        TriConsumerThrowable<Hepta<K1,K2,K3,K4,K5,K6,K7>, Quad<T,U,V,W>, Quad<T,U,V,W>> changesConsumer,
                                        HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Quad<T,U,V,W>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth(), tuple.getSeventh()));
        }

        protected QuadValuesRepository7(HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Quad<T,U,V,W>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }
}
