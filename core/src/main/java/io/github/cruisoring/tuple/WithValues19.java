package io.github.cruisoring.tuple;

public interface WithValues19<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I,J,K,L> extends WithValues18<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I,J,K> {

    /**
     * Get the 19th element of <code>L</code>
     * @return  value of the 19th element of type <code>L</code>
     */
    default L getNineteenth() {
        return (L)getValueAt(18);
    }
}
