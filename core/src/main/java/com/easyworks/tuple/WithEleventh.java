package com.easyworks.tuple;

public interface WithEleventh<T,U,V,W,X,Y,Z,A,B,C,D> extends WithTenth<T,U,V,W,X,Y,Z,A,B,C> {

    /**
     * Get the 11th element of <code>D</code>
     * @return  value of the 11th element of type <code>D</code>
     */
    default D getEleventh() {
        return (D)getValueAt(10);
    }
}
