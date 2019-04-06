package io.github.cruisoring.tuple;

public interface WithValues13<T,U,V,W,X,Y,Z,A,B,C,D,E,F> extends WithValues12<T,U,V,W,X,Y,Z,A,B,C,D,E> {

    /**
     * Get the 13th element of <code>F</code>
     * @return  value of the 13th element of type <code>F</code>
     */
    default F getThirteenth() {
        return (F) getValue(12);
    }
}
