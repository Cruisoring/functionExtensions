package com.easyworks.tuple;

public interface WithFifth<T,U,V,W,X> extends WithFourth<T,U,V,W> {

    /**
     * Get the fifth element of <code>X</code>
     * @return  value of the fifth element of type <code>X</code>
     */
    default X getFifth() {
        return (X)getValueAt(4);
    }

}
