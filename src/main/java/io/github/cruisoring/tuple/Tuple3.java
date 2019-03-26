package io.github.cruisoring.tuple;

/**
 * Tuple type with three elements persisted.
 * @param <T> Type of the first persisted element
 * @param <U> Type of the second persisted element
 * @param <V> Type of the third persisted element
 */
public class Tuple3<T,U,V> extends Tuple
    implements WithValues3<T,U,V> {

    protected Tuple3(final T t, final U u, final V v){
        super(t, u, v);
    }

}
