package com.easyworks.tuple;

public interface WithSeventeenth<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I,J> extends WithSixteenth<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I> {

    /**
     * Get the 17th element of <code>J</code>
     * @return  value of the 17th element of type <code>J</code>
     */
    default J getSeventeenth() {
        return (J)getValueAt(16);
    }
}
