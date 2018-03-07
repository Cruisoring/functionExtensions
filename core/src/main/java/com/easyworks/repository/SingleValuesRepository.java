package com.easyworks.repository;

import com.easyworks.function.*;
import com.easyworks.tuple.*;

import java.util.HashMap;
import java.util.Map;

public class SingleValuesRepository<TKey, T> extends MultiValuesRepository<TKey>
    implements SingleValues<TKey, T> {

    protected SingleValuesRepository(SupplierThrowable<Map<TKey, Tuple>> storageSupplier,
                                     ConsumerThrowable<Map<TKey, Tuple>> closing,
                                     FunctionThrowable<TKey, T> valueFunction){
        super(storageSupplier, closing, t -> Tuple.create(valueFunction.apply(t)));
    }

    protected SingleValuesRepository(FunctionThrowable<TKey, T> valueFunction){
        this(HashMap::new, null, valueFunction);
    }

    @Override
    public Single<T> retrieve(TKey tKey) {
        return (Single<T>)get(tKey, null);
    }


    public static class SingleValuesRepository2<K1,K2, T> extends SingleValuesRepository<Dual<K1,K2>, T>
        implements DualKeys<K1,K2, T>{

        protected SingleValuesRepository2(SupplierThrowable<Map<Dual<K1,K2>, Tuple>> storageSupplier,
                                        ConsumerThrowable<Map<Dual<K1,K2>, Tuple>> closing,
                                        BiFunctionThrowable<K1, K2, T> valueFunction) {
            super(storageSupplier, closing, dual -> valueFunction.apply(dual.getFirst(), dual.getSecond()));
        }

        protected SingleValuesRepository2(BiFunctionThrowable<K1, K2, T> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public T retrieve(K1 k1, K2 k2) {
            return retrieve(getKey(k1, k2)).getFirst();
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2) {
            return containsKey(getKey(k1, k2));
        }
    }

    public static class SingleValuesRepository1<K1, T> extends SingleValuesRepository<Single<K1>, T>
        implements SingleKeys<K1>{

        protected SingleValuesRepository1(SupplierThrowable<Map<Single<K1>, Tuple>> storageSupplier,
                                        ConsumerThrowable<Map<Single<K1>, Tuple>> closing,
                                        FunctionThrowable<K1, T> valueFunction) {
            super(storageSupplier, closing, dual -> valueFunction.apply(dual.getFirst()));
        }

        protected SingleValuesRepository1(FunctionThrowable<K1, T> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public boolean containsKeyOf(K1 k1) {
            return containsKey(getKey(k1));
        }
    }

    public static class SingleValuesRepository3<K1,K2,K3, T> extends SingleValuesRepository<Triple<K1,K2,K3>, T>
        implements TripleKeys<K1,K2,K3, T>{

        protected SingleValuesRepository3(SupplierThrowable<Map<Triple<K1,K2,K3>, Tuple>> storageSupplier,
                                        ConsumerThrowable<Map<Triple<K1,K2,K3>, Tuple>> closing,
                                        TriFunctionThrowable<K1, K2, K3, T> valueFunction) {
            super(storageSupplier, closing, triple -> valueFunction.apply(triple.getFirst(), triple.getSecond(), triple.getThird()));
        }

        protected SingleValuesRepository3(TriFunctionThrowable<K1, K2, K3, T> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public T retrieve(K1 k1, K2 k2, K3 k3) {
            return retrieve(getKey(k1, k2, k3)).getFirst();
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3) {
            return containsKey(getKey(k1, k2, k3));
        }
    }

    public static class SingleValuesRepository4<K1,K2,K3,K4, T> extends SingleValuesRepository<Quad<K1,K2,K3,K4>, T>
        implements QuadKeys<K1,K2,K3,K4, T> {

        protected SingleValuesRepository4(SupplierThrowable<Map<Quad<K1,K2,K3,K4>, Tuple>> storageSupplier,
                                        ConsumerThrowable<Map<Quad<K1,K2,K3,K4>, Tuple>> closing,
                                        QuadFunctionThrowable<K1,K2,K3,K4, T> valueFunction) {
            super(storageSupplier, closing, tuple -> 
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(), tuple.getFourth()));
        }

        protected SingleValuesRepository4(QuadFunctionThrowable<K1,K2,K3,K4, T> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public T retrieve(K1 k1, K2 k2, K3 k3, K4 k4) {
            return retrieve(getKey(k1, k2, k3, k4)).getFirst();
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4) {
            return containsKey(getKey(k1, k2, k3, k4));
        }
    }

    public static class SingleValuesRepository5<K1,K2,K3,K4,K5, T> extends SingleValuesRepository<Penta<K1,K2,K3,K4,K5>, T>
            implements PentaKeys<K1,K2,K3,K4,K5, T> {

        protected SingleValuesRepository5(SupplierThrowable<Map<Penta<K1,K2,K3,K4,K5>, Tuple>> storageSupplier,
                                        ConsumerThrowable<Map<Penta<K1,K2,K3,K4,K5>, Tuple>> closing,
                                        PentaFunctionThrowable<K1,K2,K3,K4,K5, T> valueFunction) {
            super(storageSupplier, closing, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth()));
        }

        protected SingleValuesRepository5(PentaFunctionThrowable<K1,K2,K3,K4,K5, T> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public T retrieve(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return retrieve(getKey(k1, k2, k3, k4, k5)).getFirst();
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5) {
            return containsKey(getKey(k1, k2, k3, k4, k5));
        }
    }

    public static class SingleValuesRepository6<K1,K2,K3,K4,K5,K6, T> extends SingleValuesRepository<Hexa<K1,K2,K3,K4,K5,K6>, T>
            implements HexaKeys<K1,K2,K3,K4,K5,K6, T> {

        protected SingleValuesRepository6(SupplierThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Tuple>> storageSupplier,
                                        ConsumerThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Tuple>> closing,
                                        HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, T> valueFunction) {
            super(storageSupplier, closing, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth()));
        }

        protected SingleValuesRepository6(HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, T> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public T retrieve(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return retrieve(getKey(k1, k2, k3, k4, k5, k6)).getFirst();
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6) {
            return containsKey(getKey(k1, k2, k3, k4, k5, k6));
        }
    }

    public static class SingleValuesRepository7<K1,K2,K3,K4,K5,K6,K7, T> extends SingleValuesRepository<Hepta<K1,K2,K3,K4,K5,K6,K7>, T>
            implements HeptaKeys<K1,K2,K3,K4,K5,K6,K7, T> {

        protected SingleValuesRepository7(SupplierThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Tuple>> storageSupplier,
                                        ConsumerThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Tuple>> closing,
                                        HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, T> valueFunction) {
            super(storageSupplier, closing, tuple ->
                    valueFunction.apply(tuple.getFirst(), tuple.getSecond(), tuple.getThird(),
                            tuple.getFourth(), tuple.getFifth(), tuple.getSixth(), tuple.getSeventh()));
        }

        protected SingleValuesRepository7(HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, T> valueFunction) {
            this(HashMap::new, null, valueFunction);
        }

        @Override
        public T retrieve(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return retrieve(getKey(k1, k2, k3, k4, k5, k6, k7)).getFirst();
        }

        @Override
        public boolean containsKeyOf(K1 k1, K2 k2, K3 k3, K4 k4, K5 k5, K6 k6, K7 k7) {
            return containsKey(getKey(k1, k2, k3, k4, k5, k6, k7));
        }
    }

}
