package io.github.cruisoring.tuple;

public interface WithValues20<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I,J,K,L,M> extends WithValues19<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I,J,K,L> {

    /**
     * Get the 20th element of <code>M</code>
     * @return  value of the 20th element of type <code>M</code>
     */
    default M getTwentieth() {
        return (M)getValueAt(19);
    }
}
