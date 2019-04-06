package io.github.cruisoring.tuple;

public interface WithValues19<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I,J,K,L> extends WithValues18<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I,J,K> {

    /**
     * Get the 19th element of <code>D</code>
     * @return  value of the 19th element of type <code>D</code>
     */
    default L getNineteenth() {
        return (L) getValue(18);
    }
}
