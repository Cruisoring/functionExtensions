package com.easyworks.tuple;

public interface WithThirteenth<T,U,V,W,X,Y,Z,A,B,C,D,E,F> extends WithTwelfth<T,U,V,W,X,Y,Z,A,B,C,D,E> {

    /**
     * Get the 13th element of <code>F</code>
     * @return  value of the 13th element of type <code>F</code>
     */
    default F getThirteenth() {
        return (F)getValueAt(12);
    }
}
