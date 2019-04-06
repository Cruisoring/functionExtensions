package io.github.cruisoring.tuple;

public interface WithValues18<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I,J,K> extends WithValues17<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I,J> {

    /**
     * Get the 18th element of <code>K</code>
     * @return  value of the 18th element of type <code>K</code>
     */
    default K getEighteenth() {
        return (K) getValue(17);
    }
}
