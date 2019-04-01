package io.github.cruisoring.tuple;

import io.github.cruisoring.utility.ArrayHelper;

/**
 * Tuple type with 20 elements persisted.
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
 * @param <H> Type of the 15th persisted element
 * @param <I> Type of the 16th persisted element
 * @param <J> Type of the 17th persisted element
 * @param <K> Type of the 18th persisted element
 * @param <L> Type of the 19th persisted element
 * @param <M> Type of the 20th persisted element
 */
public class TuplePlus<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I,J,K,L,M> extends Tuple
        implements WithValues20<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I,J,K,L,M> {

    protected TuplePlus(final T t, final U u, final V v, final W w, final X x, final Y y, final Z z, final A a, final B b,
                        final C c, final D d, final E e, final F f, final G g, final H h, final I i, final J j, final K k,
                        final L l, final M m, final Object... more){
        super(ArrayHelper.mergeTypedArray(new Object[]{t,u,v,w,x,y,z,a,b,c,d,e,f,g,h,i,j,k,l,m}, more));
    }

}