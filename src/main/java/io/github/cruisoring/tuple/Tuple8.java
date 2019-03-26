package io.github.cruisoring.tuple;

/**
 * Tuple type with 8 elements persisted.
 * @param <T> Type of the first persisted element
 * @param <U> Type of the second persisted element
 * @param <V> Type of the third persisted element
 * @param <W> Type of the fourth persisted element
 * @param <X> Type of the fifth persisted element
 * @param <Y> Type of the sixth persisted element
 * @param <Z> Type of the seventh persisted element
 * @param <A> Type of the 8th persisted element
 */
public class Tuple8<T,U,V,W,X,Y,Z,A> extends Tuple
        implements WithValues8<T,U,V,W,X,Y,Z,A> {

    protected Tuple8(final T t, final U u, final V v, final W w, final X x, final Y y, final Z z, final A a){
        super(t, u, v, w, x, y, z, a);
    }

}
