package com.easyworks.repository;

import com.easyworks.function.*;
import com.easyworks.tuple.*;

import java.util.HashMap;
import java.util.Map;

public class HeptaValuesRepository<TKey, T, U, V, W, X, Y, Z> extends Repository<TKey, Hepta<T,U,V,W,X,Y,Z>>
        implements HeptaValues<TKey, T,U,V,W,X,Y,Z> {

    protected HeptaValuesRepository(SupplierThrowable<Map<TKey, Hepta<T,U,V,W,X,Y,Z>>> storageSupplier,
                                    TriConsumerThrowable<TKey, Hepta<T,U,V,W,X,Y,Z>, Hepta<T,U,V,W,X,Y,Z>> changesConsumer,
                                    FunctionThrowable<TKey, Hepta<T,U,V,W,X,Y,Z>> valueFunction){
        super(storageSupplier, changesConsumer, valueFunction);
    }

    protected HeptaValuesRepository(FunctionThrowable<TKey, Hepta<T,U,V,W,X,Y,Z>> valueFunction){
        this(HashMap::new, null, valueFunction);
    }

    @Override
    public Hepta<T,U,V,W,X,Y,Z> retrieve(TKey tKey) {
        return get(tKey, null);
    }

    public static class HeptaValuesRepository1<K1, T,U,V,W,X,Y,Z> extends HeptaValuesRepository<Single<K1>, T,U,V,W,X,Y,Z>
            implements SingleKeys.HeptaValues<K1, T,U,V,W,X,Y,Z> {

        protected HeptaValuesRepository1(SupplierThrowable<Map<Single<K1>, Hepta<T,U,V,W,X,Y,Z>>> storageSupplier,
                                         TriConsumerThrowable<Single<K1>, Hepta<T,U,V,W,X,Y,Z>, Hepta<T,U,V,W,X,Y,Z>> changesConsumer,
                                         FunctionThrowable<K1, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            super(storageSupplier, changesConsumer, single -> valueFunction.apply(single.getFirst()));
        }

        protected HeptaValuesRepository1(FunctionThrowable<K1, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    public static class HeptaValuesRepository2<K1,K2, T,U,V,W,X,Y,Z> extends HeptaValuesRepository<Dual<K1,K2>, T,U,V,W,X,Y,Z>
            implements DualKeys.HeptaValues<K1,K2, T,U,V,W,X,Y,Z> {

        protected HeptaValuesRepository2(SupplierThrowable<Map<Dual<K1,K2>, Hepta<T,U,V,W,X,Y,Z>>> storageSupplier,
                                         TriConsumerThrowable<Dual<K1,K2>, Hepta<T,U,V,W,X,Y,Z>, Hepta<T,U,V,W,X,Y,Z>> changesConsumer,
                                         BiFunctionThrowable<K1, K2, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            super(storageSupplier, changesConsumer, dual -> valueFunction.apply(dual.getFirst(), dual.getSecond()));
        }

        protected HeptaValuesRepository2(BiFunctionThrowable<K1, K2, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    public static class HeptaValuesRepository3<K1,K2,K3, T,U,V,W,X,Y,Z> extends HeptaValuesRepository<Triple<K1,K2,K3>, T,U,V,W,X,Y,Z>
            implements TripleKeys.HeptaValues<K1,K2,K3, T,U,V,W,X,Y,Z>{

        protected HeptaValuesRepository3(SupplierThrowable<Map<Triple<K1,K2,K3>, Hepta<T,U,V,W,X,Y,Z>>> storageSupplier,
                                         TriConsumerThrowable<Triple<K1,K2,K3>, Hepta<T,U,V,W,X,Y,Z>, Hepta<T,U,V,W,X,Y,Z>> changesConsumer,
                                         TriFunctionThrowable<K1, K2, K3, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            super(storageSupplier, changesConsumer, triple -> valueFunction.apply(triple.getFirst(), triple.getSecond(), triple.getThird()));
        }

        protected HeptaValuesRepository3(TriFunctionThrowable<K1, K2, K3, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    public static class HeptaValuesRepository4<K1,K2,K3,K4, T,U,V,W,X,Y,Z> extends HeptaValuesRepository<Quad<K1,K2,K3,K4>, T,U,V,W,X,Y,Z>
            implements QuadKeys.HeptaValues<K1,K2,K3,K4, T,U,V,W,X,Y,Z> {

        protected HeptaValuesRepository4(SupplierThrowable<Map<Quad<K1,K2,K3,K4>, Hepta<T,U,V,W,X,Y,Z>>> storageSupplier,
                                         TriConsumerThrowable<Quad<K1,K2,K3,K4>, Hepta<T,U,V,W,X,Y,Z>, Hepta<T,U,V,W,X,Y,Z>> changesConsumer,
                                         QuadFunctionThrowable<K1,K2,K3,K4, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(), tuple.getFourth()));
        }

        protected HeptaValuesRepository4(QuadFunctionThrowable<K1,K2,K3,K4, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    public static class HeptaValuesRepository5<K1,K2,K3,K4,K5, T,U,V,W,X,Y,Z> extends HeptaValuesRepository<Penta<K1,K2,K3,K4,K5>, T,U,V,W,X,Y,Z>
            implements PentaKeys.HeptaValues<K1,K2,K3,K4,K5, T,U,V,W,X,Y,Z> {

        protected HeptaValuesRepository5(SupplierThrowable<Map<Penta<K1,K2,K3,K4,K5>, Hepta<T,U,V,W,X,Y,Z>>> storageSupplier,
                                         TriConsumerThrowable<Penta<K1,K2,K3,K4,K5>, Hepta<T,U,V,W,X,Y,Z>, Hepta<T,U,V,W,X,Y,Z>> changesConsumer,
                                         PentaFunctionThrowable<K1,K2,K3,K4,K5, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth()));
        }

        protected HeptaValuesRepository5(PentaFunctionThrowable<K1,K2,K3,K4,K5, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    public static class HeptaValuesRepository6<K1,K2,K3,K4,K5,K6, T,U,V,W,X,Y,Z> extends HeptaValuesRepository<Hexa<K1,K2,K3,K4,K5,K6>, T,U,V,W,X,Y,Z>
            implements HexaKeys.HeptaValues<K1,K2,K3,K4,K5,K6, T,U,V,W,X,Y,Z> {

        protected HeptaValuesRepository6(SupplierThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Hepta<T,U,V,W,X,Y,Z>>> storageSupplier,
                                         TriConsumerThrowable<Hexa<K1,K2,K3,K4,K5,K6>, Hepta<T,U,V,W,X,Y,Z>, Hepta<T,U,V,W,X,Y,Z>> changesConsumer,
                                         HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth()));
        }

        protected HeptaValuesRepository6(HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }

    public static class HeptaValuesRepository7<K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X,Y,Z> extends HeptaValuesRepository<Hepta<K1,K2,K3,K4,K5,K6,K7>, T,U,V,W,X,Y,Z>
            implements HeptaKeys.HeptaValues<K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X,Y,Z> {

        protected HeptaValuesRepository7(SupplierThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Hepta<T,U,V,W,X,Y,Z>>> storageSupplier,
                                         TriConsumerThrowable<Hepta<K1,K2,K3,K4,K5,K6,K7>, Hepta<T,U,V,W,X,Y,Z>, Hepta<T,U,V,W,X,Y,Z>> changesConsumer,
                                         HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            super(storageSupplier, changesConsumer, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth(), tuple.getSeventh()));
        }

        protected HeptaValuesRepository7(HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }
    }
}
