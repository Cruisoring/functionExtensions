package com.easyworks.tuple;

public interface WithSixteenth<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I> extends WithFifteenth<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H> {

    /**
     * Get the 16th element of <code>I</code>
     * @return  value of the 16th element of type <code>I</code>
     */
    default I getSixteenth() {
        return (I)getValueAt(15);
    }
}
