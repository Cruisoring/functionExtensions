package io.github.cruisoring.tuple;

/**
 * Tuple type with two elements persisted.
 * @param <T> Type of the first persisted element
 * @param <U> Type of the second persisted element
 */
public class Tuple2<T,U> extends Tuple
        implements WithValues2<T,U> {

    protected Tuple2(T t, U u){
        super(t, u);
    }

}
