package com.easyworks.tuple;

public interface WithSixth<T,U,V,W,X,Y> extends WithFifth<T,U,V,W,X> {

    /**
     * Get the sixth element of <code>Y</code>
     * @return  value of the sixth element of type <code>Y</code>
     */
    default Y getSixth() {
        return (Y)getValues()[5];
    }

}
