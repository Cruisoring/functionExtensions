package io.github.cruisoring.tuple;

/**
 * Tuple type with five elements persisted.
 * @param <T> Type of the first persisted element
 * @param <U> Type of the second persisted element
 * @param <V> Type of the third persisted element
 * @param <W> Type of the fourth persisted element
 * @param <X> Type of the fifth persisted element
 */
public class Tuple5<T,U,V,W,X> extends Tuple
        implements WithValues5<T,U,V,W,X> {

    protected Tuple5(T t, U u, V v, W w, X x){
        super(t, u, v, w, x);
    }

}
