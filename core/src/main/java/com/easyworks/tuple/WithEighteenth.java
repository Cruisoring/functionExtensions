package com.easyworks.tuple;

public interface WithEighteenth<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I,J,K> extends WithSeventeenth<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I,J> {

    /**
     * Get the 18th element of <code>K</code>
     * @return  value of the 18th element of type <code>K</code>
     */
    default K getEighteenth() {
        return (K)getValueAt(17);
    }
}
