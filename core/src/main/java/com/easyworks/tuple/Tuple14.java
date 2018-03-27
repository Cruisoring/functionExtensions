package com.easyworks.tuple;

/**
 * Tuple type with 14 elements persisted.
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
 * @param <E> Type of the 12th persisted element
 * @param <F> Type of the 13th persisted element
 * @param <G> Type of the 14th persisted element
 */
public class Tuple14<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G> extends Tuple
        implements WithFourteenth<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G> {

    protected Tuple14(T t, U u, V v, W w, X x, Y y, Z z, A a, B b, C c, D d, E e, F f, G g){
        super(t, u, v, w, x, y, z, a, b, c, d, e, f, g);
    }

}
