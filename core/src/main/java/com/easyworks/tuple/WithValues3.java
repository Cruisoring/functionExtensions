package com.easyworks.tuple;

public interface WithValues3<T,U,V> extends WithValues2<T,U> {

    /**
     * Get the third element of <code>V</code>
     * @return  value of the third element of type <code>V</code>
     */
    default V getThird() {
        return (V)getValueAt(2);
    }

}
