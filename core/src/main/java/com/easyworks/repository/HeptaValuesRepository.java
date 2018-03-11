package com.easyworks.repository;

import com.easyworks.function.*;
import com.easyworks.tuple.*;

import java.util.HashMap;
import java.util.Map;

public class HeptaValuesRepository<TKey, T, U, V, W, X, Y, Z> extends Repository<TKey, Hepta<T,U,V,W,X,Y,Z>>
    implements HeptaValues<TKey, T,U,V,W,X,Y,Z>{

    protected HeptaValuesRepository(SupplierThrowable<Map<TKey, Hepta<T,U,V,W,X,Y,Z>>> storageSupplier,
                                 ConsumerThrowable<Map<TKey, Hepta<T,U,V,W,X,Y,Z>>> closing,
                                 FunctionThrowable<TKey, Hepta<T,U,V,W,X,Y,Z>> valueFunction){
        super(storageSupplier, closing, t -> valueFunction.apply(t));
    }

    protected HeptaValuesRepository(FunctionThrowable<TKey, Hepta<T,U,V,W,X,Y,Z>> valueFunction){
        this(HashMap::new, null, valueFunction);
    }

    @Override
    public Hepta<T, U, V, W, X, Y, Z> retrieve(TKey tKey) {
        return (Hepta<T,U,V,W,X,Y,Z>)get(tKey, null);
    }

    public static class HeptaValuesRepository1<K1, T,U,V,W,X,Y,Z> extends HeptaValuesRepository<Single<K1>, T,U,V,W,X,Y,Z>
                implements SingleKeys.SingleKeys7<K1, T,U,V,W,X,Y,Z> {

        protected HeptaValuesRepository1(SupplierThrowable<Map<Single<K1>, Hepta<T,U,V,W,X,Y,Z>>> storageSupplier,
                                        ConsumerThrowable<Map<Single<K1>, Hepta<T,U,V,W,X,Y,Z>>> closing,
                                        FunctionThrowable<K1, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            super(storageSupplier, closing, single -> valueFunction.apply(single.getFirst()));
        }

        protected HeptaValuesRepository1(FunctionThrowable<K1, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public boolean containsKeyOf(K1 k1) {
            return containsKey(getKey(k1));
        }
    }

    public static class HeptaValuesRepository2<K1,K2, T,U,V,W,X,Y,Z> extends HeptaValuesRepository<Dual<K1,K2>, T,U,V,W,X,Y,Z>
            implements DualKeys.DualKeys7<K1,K2, T,U,V,W,X,Y,Z>{

        protected HeptaValuesRepository2(SupplierThrowable<Map<Dual<K1,K2>, Hepta<T,U,V,W,X,Y,Z>>> storageSupplier,
                                        ConsumerThrowable<Map<Dual<K1,K2>, Hepta<T,U,V,W,X,Y,Z>>> closing,
                                        BiFunctionThrowable<K1, K2, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            super(storageSupplier, closing, dual -> valueFunction.apply(dual.getFirst(), dual.getSecond()));
        }

        protected HeptaValuesRepository2(BiFunctionThrowable<K1, K2, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Hepta<T,U,V,W,X,Y,Z> retrieve(K1 k1, K2 k2) {
            return retrieve(getKey(k1, k2));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2) {
            return containsKey(getKey(k1, k2));
        }
    }

    public static class HeptaValuesRepository3<K1,K2,K3, T,U,V,W,X,Y,Z> extends HeptaValuesRepository<Triple<K1,K2,K3>, T,U,V,W,X,Y,Z>
            implements TripleKeys.TripleKeys7<K1,K2,K3, T,U,V,W,X,Y,Z>{

        protected HeptaValuesRepository3(SupplierThrowable<Map<Triple<K1,K2,K3>, Hepta<T,U,V,W,X,Y,Z>>> storageSupplier,
                                        ConsumerThrowable<Map<Triple<K1,K2,K3>, Hepta<T,U,V,W,X,Y,Z>>> closing,
                                        TriFunctionThrowable<K1, K2, K3, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            super(storageSupplier, closing, triple -> valueFunction.apply(triple.getFirst(), triple.getSecond(), triple.getThird()));
        }

        protected HeptaValuesRepository3(TriFunctionThrowable<K1, K2, K3, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Hepta<T,U,V,W,X,Y,Z> retrieve(K1 k1, K2 k2, K3 k3) {
            return retrieve(getKey(k1, k2, k3));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3) {
            return containsKey(getKey(k1, k2, k3));
        }
    }

    public static class HeptaValuesRepository4<K1,K2,K3,K4, T,U,V,W,X,Y,Z> extends HeptaValuesRepository<Quad<K1,K2,K3,K4>, T,U,V,W,X,Y,Z>
            implements QuadKeys.QuadKeys7<K1,K2,K3,K4, T,U,V,W,X,Y,Z> {

        protected HeptaValuesRepository4(SupplierThrowable<Map<Quad<K1,K2,K3,K4>, Hepta<T,U,V,W,X,Y,Z>>> storageSupplier,
                                        ConsumerThrowable<Map<Quad<K1,K2,K3,K4>, Hepta<T,U,V,W,X,Y,Z>>> closing,
                                        QuadFunctionThrowable<K1,K2,K3,K4, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            super(storageSupplier, closing, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(), tuple.getFourth()));
        }

        protected HeptaValuesRepository4(QuadFunctionThrowable<K1,K2,K3,K4, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Hepta<T,U,V,W,X,Y,Z> retrieve(K1 k1, K2 k2, K3 k3, K4 k4) {
            return retrieve(getKey(k1, k2, k3, k4));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4) {
            return containsKey(getKey(k1, k2, k3, k4));
        }
    }

    public static class HeptaValuesRepository5<K1,K2,K3,K4,K5, T,U,V,W,X,Y,Z> extends HeptaValuesRepository<Penta<K1,K2,K3,K4,K5>, T,U,V,W,X,Y,Z>
            implements PentaKeys.PentaKeys7<K1,K2,K3,K4,K5, T,U,V,W,X,Y,Z> {

        protected HeptaValuesRepository5(SupplierThrowable<Map<Penta<K1,K2,K3,K4,K5>, Hepta<T,U,V,W,X,Y,Z>>> storageSupplier,
                                        ConsumerThrowable<Map<Penta<K1,K2,K3,K4,K5>, Hepta<T,U,V,W,X,Y,Z>>> closing,
                                        PentaFunctionThrowable<K1,K2,K3,K4,K5, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            super(storageSupplier, closing, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth()));
        }

        protected HeptaValuesRepository5(PentaFunctionThrowable<K1,K2,K3,K4,K5, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Hepta<T,U,V,W,X,Y,Z> retrieve(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return retrieve(getKey(k1, k2, k3, k4, k5));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return containsKey(getKey(k1, k2, k3, k4, k5));
        }
    }

    public static class HeptaValuesRepository6<K1,K2,K3,K4,K5,K6, T,U,V,W,X,Y,Z> extends HeptaValuesRepository<Hexa<K1,K2,K3,K4,K5,K6>, T,U,V,W,X,Y,Z>
            implements HexaKeys.HexaKeys7<K1,K2,K3,K4,K5,K6, T,U,V,W,X,Y,Z> {

        protected HeptaValuesRepository6(SupplierThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Hepta<T,U,V,W,X,Y,Z>>> storageSupplier,
                                        ConsumerThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Hepta<T,U,V,W,X,Y,Z>>> closing,
                                        HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            super(storageSupplier, closing, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth()));
        }

        protected HeptaValuesRepository6(HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Hepta<T,U,V,W,X,Y,Z> retrieve(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return retrieve(getKey(k1, k2, k3, k4, k5, k6));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return containsKey(getKey(k1, k2, k3, k4, k5, k6));
        }
    }

    public static class HeptaValuesRepository7<K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X,Y,Z> extends HeptaValuesRepository<Hepta<K1,K2,K3,K4,K5,K6,K7>, T,U,V,W,X,Y,Z>
            implements HeptaKeys.HeptaKeys7<K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X,Y,Z> {

        protected HeptaValuesRepository7(SupplierThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Hepta<T,U,V,W,X,Y,Z>>> storageSupplier,
                                        ConsumerThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Hepta<T,U,V,W,X,Y,Z>>> closing,
                                        HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            super(storageSupplier, closing, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth(), tuple.getSeventh()));
        }

        protected HeptaValuesRepository7(HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Hepta<T,U,V,W,X,Y,Z>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Hepta<T,U,V,W,X,Y,Z> retrieve(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return retrieve(getKey(k1, k2, k3, k4, k5, k6, k7));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return containsKey(getKey(k1, k2, k3, k4, k5, k6, k7));
        }
    }
}
