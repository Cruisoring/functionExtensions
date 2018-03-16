package com.easyworks.tuple;

public interface WithThird<T,U,V> extends WithSecond<T,U> {

    /**
     * Get the third element of <code>V</code>
     * @return  value of the third element of type <code>V</code>
     */
    default V getThird() {
        return (V)getValues()[2];
    }

}
