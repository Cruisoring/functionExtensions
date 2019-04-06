package io.github.cruisoring.tuple;

public interface WithValues17<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I,J> extends WithValues16<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H,I> {

    /**
     * Get the 17th element of <code>J</code>
     * @return  value of the 17th element of type <code>J</code>
     */
    default J getSeventeenth() {
        return (J) getValue(16);
    }
}
