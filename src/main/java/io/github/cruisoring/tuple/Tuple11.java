package io.github.cruisoring.tuple;

/**
 * Tuple type with 11 elements persisted.
 * @param <T> Type of the first persisted element
 * @param <U> Type of the second persisted element
 * @param <V> Type of the third persisted element
 * @param <W> Type of the fourth persisted element
 * @param <X> Type of the fifth persisted element
 * @param <Y> Type of the sixth persisted element
 * @param <Z> Type of the seventh persisted element
 * @param <A> Type of the 8th persisted element
 * @param <B> Type of the 9th persisted element
 * @param <C> Type of the 10th persisted element
 * @param <D> Type of the 11th persisted element
 */
public class Tuple11<T,U,V,W,X,Y,Z,A,B,C,D> extends Tuple
        implements WithValues11<T,U,V,W,X,Y,Z,A,B,C,D> {

    protected Tuple11(T t, U u, V v, W w, X x, Y y, Z z, A a, B b, C c, D d){
        super(t, u, v, w, x, y, z, a, b, c, d);
    }

}
