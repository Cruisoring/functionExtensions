package com.easyworks;

import com.easyworks.utilities.Result;
import io.reactivex.Observable;

import java.util.Arrays;
import java.util.stream.Stream;


@FunctionalInterface
public interface ResultFunction<T> {
    /**
     *
     * @return
     */
    Result execute();

//    default ResultFunction combine(ResultFunction... others){
//        final Observable observable = Observable.concat(Observable.just(this), Observable.fromArray(others))
//                .map(f -> f.execute());
//
//        return () -> observable.map(rf -> rf.execute()).reduce()
//    }
}
