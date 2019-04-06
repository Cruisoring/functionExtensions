package io.github.cruisoring.tuple;

public interface WithValues16<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I> extends WithValues15<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H> {

    /**
     * Get the 16th element of <code>I</code>
     * @return  value of the 16th element of type <code>I</code>
     */
    default I getSixteenth() {
        return (I) getValue(15);
    }
}
