package com.easyworks.tuple;

public interface WithEighth<T,U,V,W,X,Y,Z,A> extends WithSeventh<T,U,V,W,X,Y,Z> {

    /**
     * Get the eighth element of <code>A</code>
     * @return  value of the eighth element of type <code>A</code>
     */
    default A getEighth() {
        return (A)getValueAt(7);
    }
}
