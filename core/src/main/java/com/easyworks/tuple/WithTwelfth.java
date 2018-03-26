package com.easyworks.tuple;

public interface WithTwelfth<T,U,V,W,X,Y,Z,A,B,C,D,E> extends WithEleventh<T,U,V,W,X,Y,Z,A,B,C,D> {

    /**
     * Get the 12th element of <code>E</code>
     * @return  value of the 12th element of type <code>E</code>
     */
    default E getTwelfth() {
        return (E)getValueAt(11);
    }
}