package com.easyworks.tuple;

public interface WithEleventh<T,U,V,W,X,Y,Z,A,B,C> extends WithNineth<T,U,V,W,X,Y,Z,A,B> {

    /**
     * Get the 10th element of <code>C</code>
     * @return  value of the 10th element of type <code>C</code>
     */
    default C getTenth() {
        return (C)getValueAt(9);
    }
}