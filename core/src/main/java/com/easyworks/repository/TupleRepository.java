package com.easyworks.repository;

import com.easyworks.function.*;
import com.easyworks.tuple.*;

import java.util.Map;

public class TupleRepository {

    //region Factory methods to create TupleRepository with Functions, storageSupplier and closing logic
    public static <K1, T> SingleValuesRepository.SingleValuesRepository1<K1, T> toSingleValuesRepository(
            SupplierThrowable<Map<Single<K1>, Single<T>>> storageSupplier,
            ConsumerThrowable<Map<Single<K1>, Single<T>>> closing,
            FunctionThrowable<K1, T> valueFunction){
        return new SingleValuesRepository.SingleValuesRepository1(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2, T> SingleValuesRepository.SingleValuesRepository2<K1,K2, T> toSingleValuesRepository(
            SupplierThrowable<Map<Dual<K1,K2>, Single<T>>> storageSupplier,
            ConsumerThrowable<Map<Dual<K1,K2>, Single<T>>> closing,
            BiFunctionThrowable<K1, K2, T> valueFunction){
        return new SingleValuesRepository.SingleValuesRepository2(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3, T> SingleValuesRepository.SingleValuesRepository3<K1,K2,K3, T> toSingleValuesRepository(
            SupplierThrowable<Map<Triple<K1,K2,K3>, Single<T>>> storageSupplier,
            ConsumerThrowable<Map<Triple<K1,K2,K3>, Single<T>>> closing,
            TriFunctionThrowable<K1,K2,K3, T> valueFunction){
        return new SingleValuesRepository.SingleValuesRepository3(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4, T> SingleValuesRepository.SingleValuesRepository4<K1,K2,K3,K4, T> toSingleValuesRepository(
            SupplierThrowable<Map<Quad<K1,K2,K3,K4>, Single<T>>> storageSupplier,
            ConsumerThrowable<Map<Quad<K1,K2,K3,K4>, Single<T>>> closing,
            QuadFunctionThrowable<K1,K2,K3,K4, T> valueFunction){
        return new SingleValuesRepository.SingleValuesRepository4(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4,K5, T> SingleValuesRepository.SingleValuesRepository5<K1,K2,K3,K4,K5, T> toSingleValuesRepository(
            SupplierThrowable<Map<Penta<K1,K2,K3,K4,K5>, Single<T>>> storageSupplier,
            ConsumerThrowable<Map<Penta<K1,K2,K3,K4,K5>, Single<T>>> closing,
            PentaFunctionThrowable<K1,K2,K3,K4,K5, T> valueFunction){
        return new SingleValuesRepository.SingleValuesRepository5(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6, T> SingleValuesRepository.SingleValuesRepository6<K1,K2,K3,K4,K5,K6, T> toSingleValuesRepository(
            SupplierThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Single<T>>> storageSupplier,
            ConsumerThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Single<T>>> closing,
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, T> valueFunction){
        return new SingleValuesRepository.SingleValuesRepository6(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6,K7, T> SingleValuesRepository.SingleValuesRepository7<K1,K2,K3,K4,K5,K6,K7, T> toSingleValuesRepository(
            SupplierThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Single<T>>> storageSupplier,
            ConsumerThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Single<T>>> closing,
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, T> valueFunction){
        return new SingleValuesRepository.SingleValuesRepository7(storageSupplier, closing, valueFunction);
    }



    public static <K1, T,U> DualValuesRepository.DualValuesRepository1<K1, T,U> toDualValuesRepository(
            SupplierThrowable<Map<Single<K1>, Dual<T,U>>> storageSupplier,
            ConsumerThrowable<Map<Single<K1>, Dual<T,U>>> closing,
            FunctionThrowable<K1, Dual<T, U>> valueFunction){
        return new DualValuesRepository.DualValuesRepository1(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2, T,U> DualValuesRepository.DualValuesRepository2<K1,K2, T,U> toDualValuesRepository(
            SupplierThrowable<Map<Dual<K1,K2>, Dual<T,U>>> storageSupplier,
            ConsumerThrowable<Map<Dual<K1,K2>, Dual<T,U>>> closing,
            BiFunctionThrowable<K1,K2, Dual<T, U>> valueFunction){
        return new DualValuesRepository.DualValuesRepository2(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3, T,U> DualValuesRepository.DualValuesRepository3<K1,K2,K3, T,U> toDualValuesRepository(
            SupplierThrowable<Map<Triple<K1,K2,K3>, Dual<T,U>>> storageSupplier,
            ConsumerThrowable<Map<Triple<K1,K2,K3>, Dual<T,U>>> closing,
            TriFunctionThrowable<K1,K2,K3, Dual<T, U>> valueFunction){
        return new DualValuesRepository.DualValuesRepository3(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4, T,U> DualValuesRepository.DualValuesRepository4<K1,K2,K3,K4, T,U> toDualValuesRepository(
            SupplierThrowable<Map<Quad<K1,K2,K3,K4>, Dual<T,U>>> storageSupplier,
            ConsumerThrowable<Map<Quad<K1,K2,K3,K4>, Dual<T,U>>> closing,
            QuadFunctionThrowable<K1,K2,K3,K4, Dual<T, U>> valueFunction){
        return new DualValuesRepository.DualValuesRepository4(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4,K5, T,U> DualValuesRepository.DualValuesRepository5<K1,K2,K3,K4,K5, T,U> toDualValuesRepository(
            SupplierThrowable<Map<Penta<K1,K2,K3,K4,K5>, Dual<T,U>>> storageSupplier,
            ConsumerThrowable<Map<Penta<K1,K2,K3,K4,K5>, Dual<T,U>>> closing,
            PentaFunctionThrowable<K1,K2,K3,K4,K5, Dual<T, U>> valueFunction){
        return new DualValuesRepository.DualValuesRepository5(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6, T,U> DualValuesRepository.DualValuesRepository6<K1,K2,K3,K4,K5,K6, T,U> toDualValuesRepository(
            SupplierThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Dual<T,U>>> storageSupplier,
            ConsumerThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Dual<T,U>>> closing,
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Dual<T, U>> valueFunction){
        return new DualValuesRepository.DualValuesRepository6(storageSupplier, closing, valueFunction);
    }
    
    public static <K1,K2,K3,K4,K5,K6,K7, T,U> DualValuesRepository.DualValuesRepository7<K1,K2,K3,K4,K5,K6,K7, T,U> toDualValuesRepository(
            SupplierThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Dual<T,U>>> storageSupplier,
            ConsumerThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Dual<T,U>>> closing,
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Dual<T, U>> valueFunction){
        return new DualValuesRepository.DualValuesRepository7(storageSupplier, closing, valueFunction);
    }

    public static <K1, T,U,V> TripleValuesRepository.TripleValuesRepository1<K1, T,U,V> toTripleValuesRepository(
            SupplierThrowable<Map<Single<K1>, Triple<T,U,V>>> storageSupplier,
            ConsumerThrowable<Map<Single<K1>, Triple<T,U,V>>> closing,
            FunctionThrowable<K1, Triple<T,U,V>>  valueFunction){
        return new TripleValuesRepository.TripleValuesRepository1(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2, T,U,V> TripleValuesRepository.TripleValuesRepository2<K1,K2, T,U,V> toTripleValuesRepository(
            SupplierThrowable<Map<Dual<K1,K2>, Triple<T,U,V>>> storageSupplier,
            ConsumerThrowable<Map<Dual<K1,K2>, Triple<T,U,V>>> closing,
            BiFunctionThrowable<K1,K2, Triple<T,U,V>>  valueFunction){
        return new TripleValuesRepository.TripleValuesRepository2(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3, T,U,V> TripleValuesRepository.TripleValuesRepository3<K1,K2,K3, T,U,V> toTripleValuesRepository(
            SupplierThrowable<Map<Triple<K1,K2,K3>, Triple<T,U,V>>> storageSupplier,
            ConsumerThrowable<Map<Triple<K1,K2,K3>, Triple<T,U,V>>> closing,
            TriFunctionThrowable<K1,K2,K3, Triple<T,U,V>> valueFunction){
        return new TripleValuesRepository.TripleValuesRepository3(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4, T,U,V> TripleValuesRepository.TripleValuesRepository4<K1,K2,K3,K4, T,U,V> toTripleValuesRepository(
            SupplierThrowable<Map<Quad<K1,K2,K3,K4>, Triple<T,U,V>>> storageSupplier,
            ConsumerThrowable<Map<Quad<K1,K2,K3,K4>, Triple<T,U,V>>> closing,
            QuadFunctionThrowable<K1,K2,K3,K4, Triple<T,U,V>> valueFunction){
        return new TripleValuesRepository.TripleValuesRepository4(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4,K5, T,U,V> TripleValuesRepository.TripleValuesRepository5<K1,K2,K3,K4,K5, T,U,V> toTripleValuesRepository(
            SupplierThrowable<Map<Penta<K1,K2,K3,K4,K5>, Triple<T,U,V>>> storageSupplier,
            ConsumerThrowable<Map<Penta<K1,K2,K3,K4,K5>, Triple<T,U,V>>> closing,
            PentaFunctionThrowable<K1,K2,K3,K4,K5, Triple<T,U,V>> valueFunction){
        return new TripleValuesRepository.TripleValuesRepository5(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6, T,U,V> TripleValuesRepository.TripleValuesRepository6<K1,K2,K3,K4,K5,K6, T,U,V> toTripleValuesRepository(
            SupplierThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Triple<T,U,V>>> storageSupplier,
            ConsumerThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Triple<T,U,V>>> closing,
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Triple<T,U,V>> valueFunction){
        return new TripleValuesRepository.TripleValuesRepository6(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6,K7, T,U,V> TripleValuesRepository.TripleValuesRepository7<K1,K2,K3,K4,K5,K6,K7, T,U,V> toTripleValuesRepository(
            SupplierThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Triple<T,U,V>>> storageSupplier,
            ConsumerThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Triple<T,U,V>>> closing,
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Triple<T,U,V>> valueFunction){
        return new TripleValuesRepository.TripleValuesRepository7(storageSupplier, closing, valueFunction);
    }

    public static <K1, T,U,V,W> QuadValuesRepository.QuadValuesRepository1<K1, T,U,V,W> toQuadValuesRepository(
            SupplierThrowable<Map<Single<K1>, Quad<T,U,V,W>>> storageSupplier,
            ConsumerThrowable<Map<Single<K1>, Quad<T,U,V,W>>> closing,
            FunctionThrowable<K1, Quad<T,U,V,W>> valueFunction){
        return new QuadValuesRepository.QuadValuesRepository1(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2, T,U,V,W> QuadValuesRepository.QuadValuesRepository2<K1,K2, T,U,V,W> toQuadValuesRepository(
            SupplierThrowable<Map<Dual<K1,K2>, Quad<T,U,V,W>>> storageSupplier,
            ConsumerThrowable<Map<Dual<K1,K2>, Quad<T,U,V,W>>> closing,
            BiFunctionThrowable<K1,K2, Quad<T,U,V,W>> valueFunction){
        return new QuadValuesRepository.QuadValuesRepository2(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3, T,U,V,W> QuadValuesRepository.QuadValuesRepository3<K1,K2,K3, T,U,V,W> toQuadValuesRepository(
            SupplierThrowable<Map<Triple<K1,K2,K3>, Quad<T,U,V,W>>> storageSupplier,
            ConsumerThrowable<Map<Triple<K1,K2,K3>, Quad<T,U,V,W>>> closing,
            TriFunctionThrowable<K1,K2,K3, Quad<T,U,V,W>> valueFunction){
        return new QuadValuesRepository.QuadValuesRepository3(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4, T,U,V,W> QuadValuesRepository.QuadValuesRepository4<K1,K2,K3,K4, T,U,V,W> toQuadValuesRepository(
            SupplierThrowable<Map<Quad<K1,K2,K3,K4>, Quad<T,U,V,W>>> storageSupplier,
            ConsumerThrowable<Map<Quad<K1,K2,K3,K4>, Quad<T,U,V,W>>> closing,
            QuadFunctionThrowable<K1,K2,K3,K4, Quad<T,U,V,W>> valueFunction){
        return new QuadValuesRepository.QuadValuesRepository4(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4,K5, T,U,V,W> QuadValuesRepository.QuadValuesRepository5<K1,K2,K3,K4,K5, T,U,V,W> toQuadValuesRepository(
            SupplierThrowable<Map<Penta<K1,K2,K3,K4,K5>, Quad<T,U,V,W>>> storageSupplier,
            ConsumerThrowable<Map<Penta<K1,K2,K3,K4,K5>, Quad<T,U,V,W>>> closing,
            PentaFunctionThrowable<K1,K2,K3,K4,K5, Quad<T,U,V,W>> valueFunction){
        return new QuadValuesRepository.QuadValuesRepository5(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6, T,U,V,W> QuadValuesRepository.QuadValuesRepository6<K1,K2,K3,K4,K5,K6, T,U,V,W> toQuadValuesRepository(
            SupplierThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Quad<T,U,V,W>>> storageSupplier,
            ConsumerThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Quad<T,U,V,W>>> closing,
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Quad<T,U,V,W>> valueFunction){
        return new QuadValuesRepository.QuadValuesRepository6(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6,K7, T,U,V,W> QuadValuesRepository.QuadValuesRepository7<K1,K2,K3,K4,K5,K6,K7, T,U,V,W> toQuadValuesRepository(
            SupplierThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Quad<T,U,V,W>>> storageSupplier,
            ConsumerThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Quad<T,U,V,W>>> closing,
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Quad<T,U,V,W>> valueFunction){
        return new QuadValuesRepository.QuadValuesRepository7(storageSupplier, closing, valueFunction);
    }

    public static <K1, T,U,V,W,X> PentaValuesRepository.PentaValuesRepository1<K1, T,U,V,W,X> toPentaValuesRepository(
            SupplierThrowable<Map<Single<K1>, Penta<T,U,V,W,X>>> storageSupplier,
            ConsumerThrowable<Map<Single<K1>, Penta<T,U,V,W,X>>> closing,
            FunctionThrowable<K1, Penta<T,U,V,W,X>> valueFunction){
        return new PentaValuesRepository.PentaValuesRepository1(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2, T,U,V,W,X> PentaValuesRepository.PentaValuesRepository2<K1,K2, T,U,V,W,X> toPentaValuesRepository(
            SupplierThrowable<Map<Dual<K1,K2>, Penta<T,U,V,W,X>>> storageSupplier,
            ConsumerThrowable<Map<Dual<K1,K2>, Penta<T,U,V,W,X>>> closing,
            BiFunctionThrowable<K1,K2, Penta<T,U,V,W,X>> valueFunction){
        return new PentaValuesRepository.PentaValuesRepository2(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3, T,U,V,W,X> PentaValuesRepository.PentaValuesRepository3<K1,K2,K3, T,U,V,W,X> toPentaValuesRepository(
            SupplierThrowable<Map<Triple<K1,K2,K3>, Penta<T,U,V,W,X>>> storageSupplier,
            ConsumerThrowable<Map<Triple<K1,K2,K3>, Penta<T,U,V,W,X>>> closing,
            TriFunctionThrowable<K1,K2,K3, Penta<T,U,V,W,X>> valueFunction){
        return new PentaValuesRepository.PentaValuesRepository3(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4, T,U,V,W,X> PentaValuesRepository.PentaValuesRepository4<K1,K2,K3,K4, T,U,V,W,X> toPentaValuesRepository(
            SupplierThrowable<Map<Quad<K1,K2,K3,K4>, Penta<T,U,V,W,X>>> storageSupplier,
            ConsumerThrowable<Map<Quad<K1,K2,K3,K4>, Penta<T,U,V,W,X>>> closing,
            QuadFunctionThrowable<K1,K2,K3,K4, Penta<T,U,V,W,X>> valueFunction){
        return new PentaValuesRepository.PentaValuesRepository4(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4,K5, T,U,V,W,X> PentaValuesRepository.PentaValuesRepository5<K1,K2,K3,K4,K5, T,U,V,W,X> toPentaValuesRepository(
            SupplierThrowable<Map<Penta<K1,K2,K3,K4,K5>, Penta<T,U,V,W,X>>> storageSupplier,
            ConsumerThrowable<Map<Penta<K1,K2,K3,K4,K5>, Penta<T,U,V,W,X>>> closing,
            PentaFunctionThrowable<K1,K2,K3,K4,K5, Penta<T,U,V,W,X>> valueFunction){
        return new PentaValuesRepository.PentaValuesRepository5(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6, T,U,V,W,X> PentaValuesRepository.PentaValuesRepository6<K1,K2,K3,K4,K5,K6, T,U,V,W,X> toPentaValuesRepository(
            SupplierThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Penta<T,U,V,W,X>>> storageSupplier,
            ConsumerThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Penta<T,U,V,W,X>>> closing,
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Penta<T,U,V,W,X>> valueFunction){
        return new PentaValuesRepository.PentaValuesRepository6(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X> PentaValuesRepository.PentaValuesRepository7<K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X> toPentaValuesRepository(
            SupplierThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Penta<T,U,V,W,X>>> storageSupplier,
            ConsumerThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Penta<T,U,V,W,X>>> closing,
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Penta<T,U,V,W,X>> valueFunction){
        return new PentaValuesRepository.PentaValuesRepository7(storageSupplier, closing, valueFunction);
    }


    public static <K1, T,U,V,W,X,Y> HexaValuesRepository.HexaValuesRepository1<K1, T,U,V,W,X,Y> toHexaValuesRepository(
            SupplierThrowable<Map<Single<K1>, Hexa<T,U,V,W,X,Y>>> storageSupplier,
            ConsumerThrowable<Map<Single<K1>, Hexa<T,U,V,W,X,Y>>> closing,
            FunctionThrowable<K1, Hexa<T,U,V,W,X,Y>> valueFunction){
        return new HexaValuesRepository.HexaValuesRepository1(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2, T,U,V,W,X,Y> HexaValuesRepository.HexaValuesRepository2<K1,K2, T,U,V,W,X,Y> toHexaValuesRepository(
            SupplierThrowable<Map<Dual<K1,K2>, Hexa<T,U,V,W,X,Y>>> storageSupplier,
            ConsumerThrowable<Map<Dual<K1,K2>, Hexa<T,U,V,W,X,Y>>> closing,
            BiFunctionThrowable<K1,K2, Hexa<T,U,V,W,X,Y>> valueFunction){
        return new HexaValuesRepository.HexaValuesRepository2(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3, T,U,V,W,X,Y> HexaValuesRepository.HexaValuesRepository3<K1,K2,K3, T,U,V,W,X,Y> toHexaValuesRepository(
            SupplierThrowable<Map<Triple<K1,K2,K3>, Hexa<T,U,V,W,X,Y>>> storageSupplier,
            ConsumerThrowable<Map<Triple<K1,K2,K3>, Hexa<T,U,V,W,X,Y>>> closing,
            TriFunctionThrowable<K1,K2,K3, Hexa<T,U,V,W,X,Y>> valueFunction){
        return new HexaValuesRepository.HexaValuesRepository3(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4, T,U,V,W,X,Y> HexaValuesRepository.HexaValuesRepository4<K1,K2,K3,K4, T,U,V,W,X,Y> toHexaValuesRepository(
            SupplierThrowable<Map<Quad<K1,K2,K3,K4>, Hexa<T,U,V,W,X,Y>>> storageSupplier,
            ConsumerThrowable<Map<Quad<K1,K2,K3,K4>, Hexa<T,U,V,W,X,Y>>> closing,
            QuadFunctionThrowable<K1,K2,K3,K4, Hexa<T,U,V,W,X,Y>> valueFunction){
        return new HexaValuesRepository.HexaValuesRepository4(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4,K5, T,U,V,W,X,Y> HexaValuesRepository.HexaValuesRepository5<K1,K2,K3,K4,K5, T,U,V,W,X,Y> toHexaValuesRepository(
            SupplierThrowable<Map<Penta<K1,K2,K3,K4,K5>, Hexa<T,U,V,W,X,Y>>> storageSupplier,
            ConsumerThrowable<Map<Penta<K1,K2,K3,K4,K5>, Hexa<T,U,V,W,X,Y>>> closing,
            PentaFunctionThrowable<K1,K2,K3,K4,K5, Hexa<T,U,V,W,X,Y>> valueFunction){
        return new HexaValuesRepository.HexaValuesRepository5(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6, T,U,V,W,X,Y> HexaValuesRepository.HexaValuesRepository6<K1,K2,K3,K4,K5,K6, T,U,V,W,X,Y> toHexaValuesRepository(
            SupplierThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Hexa<T,U,V,W,X,Y>>> storageSupplier,
            ConsumerThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Hexa<T,U,V,W,X,Y>>> closing,
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Hexa<T,U,V,W,X,Y>> valueFunction){
        return new HexaValuesRepository.HexaValuesRepository6(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X,Y> HexaValuesRepository.HexaValuesRepository7<K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X,Y> toHexaValuesRepository(
            SupplierThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Hexa<T,U,V,W,X,Y>>> storageSupplier,
            ConsumerThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Hexa<T,U,V,W,X,Y>>> closing,
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Hexa<T,U,V,W,X,Y>> valueFunction){
        return new HexaValuesRepository.HexaValuesRepository7(storageSupplier, closing, valueFunction);
    }

    public static <K1, T,U,V,W,X,Y,Z> HeptaValuesRepository.HeptaValuesRepository1<K1, T,U,V,W,X,Y,Z> toHeptaValuesRepository(
            SupplierThrowable<Map<Single<K1>, Hepta<T,U,V,W,X,Y,Z>>> storageSupplier,
            ConsumerThrowable<Map<Single<K1>, Hepta<T,U,V,W,X,Y,Z>>> closing,
            FunctionThrowable<K1, Hepta<T,U,V,W,X,Y,Z>> valueFunction){
        return new HeptaValuesRepository.HeptaValuesRepository1(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2, T,U,V,W,X,Y,Z> HeptaValuesRepository.HeptaValuesRepository2<K1,K2, T,U,V,W,X,Y,Z> toHeptaValuesRepository(
            SupplierThrowable<Map<Dual<K1,K2>, Hepta<T,U,V,W,X,Y,Z>>> storageSupplier,
            ConsumerThrowable<Map<Dual<K1,K2>, Hepta<T,U,V,W,X,Y,Z>>> closing,
            BiFunctionThrowable<K1,K2, Hepta<T,U,V,W,X,Y,Z>> valueFunction){
        return new HeptaValuesRepository.HeptaValuesRepository2(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3, T,U,V,W,X,Y,Z> HeptaValuesRepository.HeptaValuesRepository3<K1,K2,K3, T,U,V,W,X,Y,Z> toHeptaValuesRepository(
            SupplierThrowable<Map<Triple<K1,K2,K3>, Hepta<T,U,V,W,X,Y,Z>>> storageSupplier,
            ConsumerThrowable<Map<Triple<K1,K2,K3>, Hepta<T,U,V,W,X,Y,Z>>> closing,
            TriFunctionThrowable<K1,K2,K3, Hepta<T,U,V,W,X,Y,Z>> valueFunction){
        return new HeptaValuesRepository.HeptaValuesRepository3(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4, T,U,V,W,X,Y,Z> HeptaValuesRepository.HeptaValuesRepository4<K1,K2,K3,K4, T,U,V,W,X,Y,Z> toHeptaValuesRepository(
            SupplierThrowable<Map<Quad<K1,K2,K3,K4>, Hepta<T,U,V,W,X,Y,Z>>> storageSupplier,
            ConsumerThrowable<Map<Quad<K1,K2,K3,K4>, Hepta<T,U,V,W,X,Y,Z>>> closing,
            QuadFunctionThrowable<K1,K2,K3,K4, Hepta<T,U,V,W,X,Y,Z>> valueFunction){
        return new HeptaValuesRepository.HeptaValuesRepository4(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4,K5, T,U,V,W,X,Y,Z> HeptaValuesRepository.HeptaValuesRepository5<K1,K2,K3,K4,K5, T,U,V,W,X,Y,Z> toHeptaValuesRepository(
            SupplierThrowable<Map<Penta<K1,K2,K3,K4,K5>, Hepta<T,U,V,W,X,Y,Z>>> storageSupplier,
            ConsumerThrowable<Map<Penta<K1,K2,K3,K4,K5>, Hepta<T,U,V,W,X,Y,Z>>> closing,
            PentaFunctionThrowable<K1,K2,K3,K4,K5, Hepta<T,U,V,W,X,Y,Z>> valueFunction){
        return new HeptaValuesRepository.HeptaValuesRepository5(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6, T,U,V,W,X,Y,Z> HeptaValuesRepository.HeptaValuesRepository6<K1,K2,K3,K4,K5,K6, T,U,V,W,X,Y,Z> toHeptaValuesRepository(
            SupplierThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Hepta<T,U,V,W,X,Y,Z>>> storageSupplier,
            ConsumerThrowable<Map<Hexa<K1,K2,K3,K4,K5,K6>, Hepta<T,U,V,W,X,Y,Z>>> closing,
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Hepta<T,U,V,W,X,Y,Z>> valueFunction){
        return new HeptaValuesRepository.HeptaValuesRepository6(storageSupplier, closing, valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X,Y,Z> HeptaValuesRepository.HeptaValuesRepository7<K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X,Y,Z> toHeptaValuesRepository(
            SupplierThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Hepta<T,U,V,W,X,Y,Z>>> storageSupplier,
            ConsumerThrowable<Map<Hepta<K1,K2,K3,K4,K5,K6,K7>, Hepta<T,U,V,W,X,Y,Z>>> closing,
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Hepta<T,U,V,W,X,Y,Z>> valueFunction){
        return new HeptaValuesRepository.HeptaValuesRepository7(storageSupplier, closing, valueFunction);
    }
    //endregion


    //region Factory methods to create TupleRepositries with Functions
    public static <K1, T> SingleValuesRepository.SingleValuesRepository1<K1, T> toSingleValuesRepository(
            FunctionThrowable<K1, Single<T>> valueFunction){
        return new SingleValuesRepository.SingleValuesRepository1(valueFunction);
    }

    public static <K1,K2, T> SingleValuesRepository.SingleValuesRepository2<K1,K2, T> toSingleValuesRepository(
            BiFunctionThrowable<K1, K2, Single<T>> valueFunction){
        return new SingleValuesRepository.SingleValuesRepository2(valueFunction);
    }

    public static <K1,K2,K3, T> SingleValuesRepository.SingleValuesRepository3<K1,K2,K3, T> toSingleValuesRepository(
            TriFunctionThrowable<K1,K2,K3, Single<T>> valueFunction){
        return new SingleValuesRepository.SingleValuesRepository3(valueFunction);
    }

    public static <K1,K2,K3,K4, T> SingleValuesRepository.SingleValuesRepository4<K1,K2,K3,K4, T> toSingleValuesRepository(
            QuadFunctionThrowable<K1,K2,K3,K4, Single<T>> valueFunction){
        return new SingleValuesRepository.SingleValuesRepository4(valueFunction);
    }

    public static <K1,K2,K3,K4,K5, T> SingleValuesRepository.SingleValuesRepository5<K1,K2,K3,K4,K5, T> toSingleValuesRepository(
            PentaFunctionThrowable<K1,K2,K3,K4,K5, Single<T>> valueFunction){
        return new SingleValuesRepository.SingleValuesRepository5(valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6, T> SingleValuesRepository.SingleValuesRepository6<K1,K2,K3,K4,K5,K6, T> toSingleValuesRepository(
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Single<T>> valueFunction){
        return new SingleValuesRepository.SingleValuesRepository6(valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6,K7, T> SingleValuesRepository.SingleValuesRepository7<K1,K2,K3,K4,K5,K6,K7, T> toSingleValuesRepository(
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Single<T>> valueFunction){
        return new SingleValuesRepository.SingleValuesRepository7(valueFunction);
    }



    public static <K1, T,U> DualValuesRepository.DualValuesRepository1<K1, T,U> toDualValuesRepository(
            FunctionThrowable<K1, Dual<T, U>> valueFunction){
        return new DualValuesRepository.DualValuesRepository1(valueFunction);
    }

    public static <K1,K2, T,U> DualValuesRepository.DualValuesRepository2<K1,K2, T,U> toDualValuesRepository(
            BiFunctionThrowable<K1,K2, Dual<T, U>> valueFunction){
        return new DualValuesRepository.DualValuesRepository2(valueFunction);
    }

    public static <K1,K2,K3, T,U> DualValuesRepository.DualValuesRepository3<K1,K2,K3, T,U> toDualValuesRepository(
            TriFunctionThrowable<K1,K2,K3, Dual<T, U>> valueFunction){
        return new DualValuesRepository.DualValuesRepository3(valueFunction);
    }

    public static <K1,K2,K3,K4, T,U> DualValuesRepository.DualValuesRepository4<K1,K2,K3,K4, T,U> toDualValuesRepository(
            QuadFunctionThrowable<K1,K2,K3,K4, Dual<T, U>> valueFunction){
        return new DualValuesRepository.DualValuesRepository4(valueFunction);
    }

    public static <K1,K2,K3,K4,K5, T,U> DualValuesRepository.DualValuesRepository5<K1,K2,K3,K4,K5, T,U> toDualValuesRepository(
            PentaFunctionThrowable<K1,K2,K3,K4,K5, Dual<T, U>> valueFunction){
        return new DualValuesRepository.DualValuesRepository5(valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6, T,U> DualValuesRepository.DualValuesRepository6<K1,K2,K3,K4,K5,K6, T,U> toDualValuesRepository(
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Dual<T, U>> valueFunction){
        return new DualValuesRepository.DualValuesRepository6(valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6,K7, T,U> DualValuesRepository.DualValuesRepository7<K1,K2,K3,K4,K5,K6,K7, T,U> toDualValuesRepository(
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Dual<T, U>> valueFunction){
        return new DualValuesRepository.DualValuesRepository7(valueFunction);
    }

    public static <K1, T,U,V> TripleValuesRepository.TripleValuesRepository1<K1, T,U,V> toTripleValuesRepository(
            FunctionThrowable<K1, Triple<T,U,V>>  valueFunction){
        return new TripleValuesRepository.TripleValuesRepository1(valueFunction);
    }

    public static <K1,K2, T,U,V> TripleValuesRepository.TripleValuesRepository2<K1,K2, T,U,V> toTripleValuesRepository(
            BiFunctionThrowable<K1,K2, Triple<T,U,V>>  valueFunction){
        return new TripleValuesRepository.TripleValuesRepository2(valueFunction);
    }

    public static <K1,K2,K3, T,U,V> TripleValuesRepository.TripleValuesRepository3<K1,K2,K3, T,U,V> toTripleValuesRepository(
            TriFunctionThrowable<K1,K2,K3, Triple<T,U,V>> valueFunction){
        return new TripleValuesRepository.TripleValuesRepository3(valueFunction);
    }

    public static <K1,K2,K3,K4, T,U,V> TripleValuesRepository.TripleValuesRepository4<K1,K2,K3,K4, T,U,V> toTripleValuesRepository(
            QuadFunctionThrowable<K1,K2,K3,K4, Triple<T,U,V>> valueFunction){
        return new TripleValuesRepository.TripleValuesRepository4(valueFunction);
    }

    public static <K1,K2,K3,K4,K5, T,U,V> TripleValuesRepository.TripleValuesRepository5<K1,K2,K3,K4,K5, T,U,V> toTripleValuesRepository(
            PentaFunctionThrowable<K1,K2,K3,K4,K5, Triple<T,U,V>> valueFunction){
        return new TripleValuesRepository.TripleValuesRepository5(valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6, T,U,V> TripleValuesRepository.TripleValuesRepository6<K1,K2,K3,K4,K5,K6, T,U,V> toTripleValuesRepository(
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Triple<T,U,V>> valueFunction){
        return new TripleValuesRepository.TripleValuesRepository6(valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6,K7, T,U,V> TripleValuesRepository.TripleValuesRepository7<K1,K2,K3,K4,K5,K6,K7, T,U,V> toTripleValuesRepository(
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Triple<T,U,V>> valueFunction){
        return new TripleValuesRepository.TripleValuesRepository7(valueFunction);
    }

    public static <K1, T,U,V,W> QuadValuesRepository.QuadValuesRepository1<K1, T,U,V,W> toQuadValuesRepository(
            FunctionThrowable<K1, Quad<T,U,V,W>> valueFunction){
        return new QuadValuesRepository.QuadValuesRepository1(valueFunction);
    }

    public static <K1,K2, T,U,V,W> QuadValuesRepository.QuadValuesRepository2<K1,K2, T,U,V,W> toQuadValuesRepository(
            BiFunctionThrowable<K1,K2, Quad<T,U,V,W>> valueFunction){
        return new QuadValuesRepository.QuadValuesRepository2(valueFunction);
    }

    public static <K1,K2,K3, T,U,V,W> QuadValuesRepository.QuadValuesRepository3<K1,K2,K3, T,U,V,W> toQuadValuesRepository(
            TriFunctionThrowable<K1,K2,K3, Quad<T,U,V,W>> valueFunction){
        return new QuadValuesRepository.QuadValuesRepository3(valueFunction);
    }

    public static <K1,K2,K3,K4, T,U,V,W> QuadValuesRepository.QuadValuesRepository4<K1,K2,K3,K4, T,U,V,W> toQuadValuesRepository(
            QuadFunctionThrowable<K1,K2,K3,K4, Quad<T,U,V,W>> valueFunction){
        return new QuadValuesRepository.QuadValuesRepository4(valueFunction);
    }

    public static <K1,K2,K3,K4,K5, T,U,V,W> QuadValuesRepository.QuadValuesRepository5<K1,K2,K3,K4,K5, T,U,V,W> toQuadValuesRepository(
            PentaFunctionThrowable<K1,K2,K3,K4,K5, Quad<T,U,V,W>> valueFunction){
        return new QuadValuesRepository.QuadValuesRepository5(valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6, T,U,V,W> QuadValuesRepository.QuadValuesRepository6<K1,K2,K3,K4,K5,K6, T,U,V,W> toQuadValuesRepository(
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Quad<T,U,V,W>> valueFunction){
        return new QuadValuesRepository.QuadValuesRepository6(valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6,K7, T,U,V,W> QuadValuesRepository.QuadValuesRepository7<K1,K2,K3,K4,K5,K6,K7, T,U,V,W> toQuadValuesRepository(
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Quad<T,U,V,W>> valueFunction){
        return new QuadValuesRepository.QuadValuesRepository7(valueFunction);
    }

    public static <K1, T,U,V,W,X> PentaValuesRepository.PentaValuesRepository1<K1, T,U,V,W,X> toPentaValuesRepository(
            FunctionThrowable<K1, Penta<T,U,V,W,X>> valueFunction){
        return new PentaValuesRepository.PentaValuesRepository1<>(valueFunction);
    }

    public static <K1,K2, T,U,V,W,X> PentaValuesRepository.PentaValuesRepository2<K1,K2, T,U,V,W,X> toPentaValuesRepository(
            BiFunctionThrowable<K1,K2, Penta<T,U,V,W,X>> valueFunction){
        return new PentaValuesRepository.PentaValuesRepository2(valueFunction);
    }

    public static <K1,K2,K3, T,U,V,W,X> PentaValuesRepository.PentaValuesRepository3<K1,K2,K3, T,U,V,W,X> toPentaValuesRepository(
            TriFunctionThrowable<K1,K2,K3, Penta<T,U,V,W,X>> valueFunction){
        return new PentaValuesRepository.PentaValuesRepository3(valueFunction);
    }

    public static <K1,K2,K3,K4, T,U,V,W,X> PentaValuesRepository.PentaValuesRepository4<K1,K2,K3,K4, T,U,V,W,X> toPentaValuesRepository(
            QuadFunctionThrowable<K1,K2,K3,K4, Penta<T,U,V,W,X>> valueFunction){
        return new PentaValuesRepository.PentaValuesRepository4(valueFunction);
    }

    public static <K1,K2,K3,K4,K5, T,U,V,W,X> PentaValuesRepository.PentaValuesRepository5<K1,K2,K3,K4,K5, T,U,V,W,X> toPentaValuesRepository(
            PentaFunctionThrowable<K1,K2,K3,K4,K5, Penta<T,U,V,W,X>> valueFunction){
        return new PentaValuesRepository.PentaValuesRepository5(valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6, T,U,V,W,X> PentaValuesRepository.PentaValuesRepository6<K1,K2,K3,K4,K5,K6, T,U,V,W,X> toPentaValuesRepository(
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Penta<T,U,V,W,X>> valueFunction){
        return new PentaValuesRepository.PentaValuesRepository6(valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X> PentaValuesRepository.PentaValuesRepository7<K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X> toPentaValuesRepository(
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Penta<T,U,V,W,X>> valueFunction){
        return new PentaValuesRepository.PentaValuesRepository7(valueFunction);
    }


    public static <K1, T,U,V,W,X,Y> HexaValuesRepository.HexaValuesRepository1<K1, T,U,V,W,X,Y> toHexaValuesRepository(
            FunctionThrowable<K1, Hexa<T,U,V,W,X,Y>> valueFunction){
        return new HexaValuesRepository.HexaValuesRepository1<>(valueFunction);
    }

    public static <K1,K2, T,U,V,W,X,Y> HexaValuesRepository.HexaValuesRepository2<K1,K2, T,U,V,W,X,Y> toHexaValuesRepository(
            BiFunctionThrowable<K1,K2, Hexa<T,U,V,W,X,Y>> valueFunction){
        return new HexaValuesRepository.HexaValuesRepository2(valueFunction);
    }

    public static <K1,K2,K3, T,U,V,W,X,Y> HexaValuesRepository.HexaValuesRepository3<K1,K2,K3, T,U,V,W,X,Y> toHexaValuesRepository(
            TriFunctionThrowable<K1,K2,K3, Hexa<T,U,V,W,X,Y>> valueFunction){
        return new HexaValuesRepository.HexaValuesRepository3(valueFunction);
    }

    public static <K1,K2,K3,K4, T,U,V,W,X,Y> HexaValuesRepository.HexaValuesRepository4<K1,K2,K3,K4, T,U,V,W,X,Y> toHexaValuesRepository(
            QuadFunctionThrowable<K1,K2,K3,K4, Hexa<T,U,V,W,X,Y>> valueFunction){
        return new HexaValuesRepository.HexaValuesRepository4(valueFunction);
    }

    public static <K1,K2,K3,K4,K5, T,U,V,W,X,Y> HexaValuesRepository.HexaValuesRepository5<K1,K2,K3,K4,K5, T,U,V,W,X,Y> toHexaValuesRepository(
            PentaFunctionThrowable<K1,K2,K3,K4,K5, Hexa<T,U,V,W,X,Y>> valueFunction){
        return new HexaValuesRepository.HexaValuesRepository5(valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6, T,U,V,W,X,Y> HexaValuesRepository.HexaValuesRepository6<K1,K2,K3,K4,K5,K6, T,U,V,W,X,Y> toHexaValuesRepository(
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Hexa<T,U,V,W,X,Y>> valueFunction){
        return new HexaValuesRepository.HexaValuesRepository6(valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X,Y> HexaValuesRepository.HexaValuesRepository7<K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X,Y> toHexaValuesRepository(
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Hexa<T,U,V,W,X,Y>> valueFunction){
        return new HexaValuesRepository.HexaValuesRepository7(valueFunction);
    }

    public static <K1, T,U,V,W,X,Y,Z> HeptaValuesRepository.HeptaValuesRepository1<K1, T,U,V,W,X,Y,Z> toHeptaValuesRepository(
            FunctionThrowable<K1, Hepta<T,U,V,W,X,Y,Z>> valueFunction){
        return new HeptaValuesRepository.HeptaValuesRepository1(valueFunction);
    }

    public static <K1,K2, T,U,V,W,X,Y,Z> HeptaValuesRepository.HeptaValuesRepository2<K1,K2, T,U,V,W,X,Y,Z> toHeptaValuesRepository(
            BiFunctionThrowable<K1,K2, Hepta<T,U,V,W,X,Y,Z>> valueFunction){
        return new HeptaValuesRepository.HeptaValuesRepository2(valueFunction);
    }

    public static <K1,K2,K3, T,U,V,W,X,Y,Z> HeptaValuesRepository.HeptaValuesRepository3<K1,K2,K3, T,U,V,W,X,Y,Z> toHeptaValuesRepository(
            TriFunctionThrowable<K1,K2,K3, Hepta<T,U,V,W,X,Y,Z>> valueFunction){
        return new HeptaValuesRepository.HeptaValuesRepository3(valueFunction);
    }

    public static <K1,K2,K3,K4, T,U,V,W,X,Y,Z> HeptaValuesRepository.HeptaValuesRepository4<K1,K2,K3,K4, T,U,V,W,X,Y,Z> toHeptaValuesRepository(
            QuadFunctionThrowable<K1,K2,K3,K4, Hepta<T,U,V,W,X,Y,Z>> valueFunction){
        return new HeptaValuesRepository.HeptaValuesRepository4(valueFunction);
    }

    public static <K1,K2,K3,K4,K5, T,U,V,W,X,Y,Z> HeptaValuesRepository.HeptaValuesRepository5<K1,K2,K3,K4,K5, T,U,V,W,X,Y,Z> toHeptaValuesRepository(
            PentaFunctionThrowable<K1,K2,K3,K4,K5, Hepta<T,U,V,W,X,Y,Z>> valueFunction){
        return new HeptaValuesRepository.HeptaValuesRepository5(valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X,Y,Z> HeptaValuesRepository.HeptaValuesRepository6<K1,K2,K3,K4,K5,K6, T,U,V,W,X,Y,Z> toHeptaValuesRepository(
            HexaFunctionThrowable<K1,K2,K3,K4,K5,K6, Hepta<T,U,V,W,X,Y,Z>> valueFunction){
        return new HeptaValuesRepository.HeptaValuesRepository6(valueFunction);
    }

    public static <K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X,Y,Z> HeptaValuesRepository.HeptaValuesRepository7<K1,K2,K3,K4,K5,K6,K7, T,U,V,W,X,Y,Z> toHeptaValuesRepository(
            HeptaFunctionThrowable<K1,K2,K3,K4,K5,K6,K7, Hepta<T,U,V,W,X,Y,Z>> valueFunction){
        return new HeptaValuesRepository.HeptaValuesRepository7(valueFunction);
    }
    //endregion
}
