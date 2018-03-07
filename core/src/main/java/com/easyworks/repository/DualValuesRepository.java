package com.easyworks.repository;

import com.easyworks.function.*;
import com.easyworks.tuple.*;

import java.util.HashMap;
import java.util.Map;

public class DualValuesRepository<TKey, T, U> extends MultiValuesRepository<TKey>
    implements DualValues<TKey, T,U> {
    protected DualValuesRepository(SupplierThrowable<Map<TKey, Tuple>> storageSupplier,
                                   ConsumerThrowable<Map<TKey, Tuple>> closing,
                                   FunctionThrowable<TKey, Dual<T, U>> valueFunction){
        super(storageSupplier, closing, t -> valueFunction.apply(t));
    }

    protected DualValuesRepository(FunctionThrowable<TKey, Dual<T, U>> valueFunction){
        this(HashMap::new, null, valueFunction);
    }

    @Override
    public Dual<T, U> retrieve(TKey tKey) {
        return (Dual<T, U>)get(tKey, null);
    }

    public static class DualValuesRepository1<K1, T,U> extends DualValuesRepository<Single<K1>, T,U>
        implements SingleKeys.SingleKeys2<K1, T, U> {

        protected DualValuesRepository1(SupplierThrowable<Map<Single<K1>, Tuple>> storageSupplier,
                                        ConsumerThrowable<Map<Single<K1>, Tuple>> closing,
                                        FunctionThrowable<K1, Dual<T, U>> valueFunction) {
            super(storageSupplier, closing, single -> valueFunction.apply(single.getFirst()));
        }

        protected DualValuesRepository1(FunctionThrowable<K1, Dual<T, U>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public boolean containsKeyOf(K1 k1) {
            return containsKey(getKey(k1));
        }
    }

    public static class DualValuesRepository2<K1,K2, T,U> extends DualValuesRepository<Dual<K1,K2>, T,U>
        implements DualKeys.DualKeys2<K1,K2, T,U>{

        protected DualValuesRepository2(SupplierThrowable<Map<Dual<K1,K2>, Tuple>> storageSupplier,
                                        ConsumerThrowable<Map<Dual<K1,K2>, Tuple>> closing,
                                        BiFunctionThrowable<K1, K2, Dual<T, U>> valueFunction) {
            super(storageSupplier, closing, dual -> valueFunction.apply(dual.getFirst(), dual.getSecond()));
        }

        protected DualValuesRepository2(BiFunctionThrowable<K1, K2, Dual<T, U>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Dual<T, U> retrieve(K1 k1, K2 k2) {
            return retrieve(getKey(k1, k2));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2) {
            return containsKey(getKey(k1, k2));
        }
    }

    public static class DualValuesRepository3<K1,K2,K3, T,U> extends DualValuesRepository<Triple<K1,K2,K3>, T,U>
        implements TripleKeys.TripleKeys2<K1,K2,K3, T,U>{

        protected DualValuesRepository3(SupplierThrowable<Map<Triple<K1,K2,K3>, Tuple>> storageSupplier,
                                        ConsumerThrowable<Map<Triple<K1,K2,K3>, Tuple>> closing,
                                        TriFunctionThrowable<K1, K2, K3, Dual<T, U>> valueFunction) {
            super(storageSupplier, closing, triple -> valueFunction.apply(triple.getFirst(), triple.getSecond(), triple.getThird()));
        }

        protected DualValuesRepository3(TriFunctionThrowable<K1, K2, K3, Dual<T, U>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Dual<T, U> retrieve(K1 k1, K2 k2, K3 k3) {
            return retrieve(getKey(k1, k2, k3));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3) {
            return containsKey(getKey(k1, k2, k3));
        }
    }

    public static class DualValuesRepository4<K1,K2,K3,K4, T,U> extends DualValuesRepository<Quad<K1,K2,K3,K4>, T,U>
        implements QuadKeys.QuadKeys2<K1,K2,K3,K4, T,U> {

        protected DualValuesRepository4(SupplierThrowable<Map<Quad<K1,K2,K3,K4>, Tuple>> storageSupplier,
                                        ConsumerThrowable<Map<Quad<K1,K2,K3,K4>, Tuple>> closing,
                                        QuadFunctionThrowable<K1,K2,K3,K4, Dual<T, U>> valueFunction) {
            super(storageSupplier, closing, tuple -> 
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(), tuple.getFourth()));
        }

        protected DualValuesRepository4(QuadFunctionThrowable<K1,K2,K3,K4, Dual<T, U>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Dual<T, U> retrieve(K1 k1, K2 k2, K3 k3, K4 k4) {
            return retrieve(getKey(k1, k2, k3, k4));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4) {
            return containsKey(getKey(k1, k2, k3, k4));
        }
    }

    public static class DualValuesRepository5<K1,K2,K3,K4,K5, T,U> extends DualValuesRepository<Penta<K1,K2,K3,K4,K5>, T,U>
            implements PentaKeys.PentaKeys2<K1,K2,K3,K4,K5, T,U> {

        protected DualValuesRepository5(SupplierThrowable<Map<Penta<K1,K2,K3,K4,K5>, Tuple>> storageSupplier,
                                        ConsumerThrowable<Map<Penta<K1,K2,K3,K4,K5>, Tuple>> closing,
                                        PentaFunctionThrowable<K1,K2,K3,K4,K5, Dual<T, U>> valueFunction) {
            super(storageSupplier, closing, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth()));
        }

        protected DualValuesRepository5(PentaFunctionThrowable<K1,K2,K3,K4,K5, Dual<T, U>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Dual<T, U> retrieve(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return retrieve(getKey(k1, k2, k3, k4, k5));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return containsKey(getKey(k1, k2, k3, k4, k5));
        }
    }

    public static class DualValuesRepository6<K1,K2,K3,K4,K5,K6, T,U> extends DualValuesRepository<Hexa<K1,K2,K3,K4,K5,K6>, T,U>
            implements HexaKeys.HexaKeys2<K1,K2,K3,K4,K5,K6, T,U> {

        protected DualValuesRepository6(SupplierThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Tuple>> storageSupplier,
                                        ConsumerThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Tuple>> closing,
                                        HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Dual<T, U>> valueFunction) {
            super(storageSupplier, closing, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth()));
        }

        protected DualValuesRepository6(HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Dual<T, U>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Dual<T, U> retrieve(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return retrieve(getKey(k1, k2, k3, k4, k5, k6));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return containsKey(getKey(k1, k2, k3, k4, k5, k6));
        }
    }

    public static class DualValuesRepository7<K1,K2,K3,K4,K5,K6,K7, T,U> extends DualValuesRepository<Hepta<K1,K2,K3,K4,K5,K6,K7>, T,U>
            implements HeptaKeys.HeptaKeys2<K1,K2,K3,K4,K5,K6,K7, T,U> {

        protected DualValuesRepository7(SupplierThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Tuple>> storageSupplier,
                                        ConsumerThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Tuple>> closing,
                                        HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Dual<T, U>> valueFunction) {
            super(storageSupplier, closing, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth(), tuple.getSeventh()));
        }

        protected DualValuesRepository7(HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Dual<T, U>> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public Dual<T, U> retrieve(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return retrieve(getKey(k1, k2, k3, k4, k5, k6, k7));
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return containsKey(getKey(k1, k2, k3, k4, k5, k6, k7));
        }
    }

}
