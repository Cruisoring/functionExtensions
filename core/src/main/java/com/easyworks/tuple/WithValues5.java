package com.easyworks.tuple;

public interface WithValues5<T,U,V,W,X> extends WithValues4<T,U,V,W> {

    /**
     * Get the fifth element of <code>X</code>
     * @return  value of the fifth element of type <code>X</code>
     */
    default X getFifth() {
        return (X)getValueAt(4);
    }

}
