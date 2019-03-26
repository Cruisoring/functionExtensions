package io.github.cruisoring.tuple;

/**
 * Tuple type with 12 elements persisted.
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
 */
public class Tuple12<T,U,V,W,X,Y,Z,A,B,C,D,E> extends Tuple
        implements WithValues12<T,U,V,W,X,Y,Z,A,B,C,D,E> {

    protected Tuple12(final T t, final U u, final V v, final W w, final X x, final Y y, final Z z, final A a, final B b, final C c, final D d, final E e){
        super(t, u, v, w, x, y, z, a, b, c, d, e);
    }

}
