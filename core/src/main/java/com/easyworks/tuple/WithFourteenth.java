package com.easyworks.tuple;

public interface WithFourteenth<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G> extends WithThirteenth<T,U,V,W,X,Y,Z,A,B,C,D,E,F> {

    /**
     * Get the 14th element of <code>G</code>
     * @return  value of the 14th element of type <code>G</code>
     */
    default G getFourteenth() {
        return (G)getValueAt(13);
    }
}
