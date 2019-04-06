package io.github.cruisoring.tuple;

public interface WithValues14<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G> extends WithValues13<T,U,V,W,X,Y,Z,A,B,C,D,E,F> {

    /**
     * Get the 14th element of <code>G</code>
     * @return  value of the 14th element of type <code>G</code>
     */
    default G getFourteenth() {
        return (G) getValue(13);
    }
}
