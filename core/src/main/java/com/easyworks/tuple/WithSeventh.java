package com.easyworks.tuple;

public interface WithSeventh<T,U,V,W,X,Y,Z> extends WithSixth<T,U,V,W,X,Y> {

    /**
     * Get the seventh element of <code>Z</code>
     * @return  value of the seventh element of type <code>Z</code>
     */
    default Z getSeventh() {
        return (Z)getValueAt(6);
    }
}
