package io.github.cruisoring.tuple;

/**
 * Tuple type with six elements persisted.
 * @param <T> Type of the first persisted element
 * @param <U> Type of the second persisted element
 * @param <V> Type of the third persisted element
 * @param <W> Type of the fourth persisted element
 * @param <X> Type of the fifth persisted element
 * @param <Y> Type of the sixth persisted element
 */
public class Tuple6<T,U,V,W,X,Y> extends Tuple implements
        WithValues6<T,U,V,W,X,Y> {

    protected Tuple6(final T t, final U u, final V v, final W w, final X x, final Y y){
        super(t, u, v, w, x, y);
    }

}
