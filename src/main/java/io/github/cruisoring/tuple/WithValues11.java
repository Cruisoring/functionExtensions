package io.github.cruisoring.tuple;

public interface WithValues11<T,U,V,W,X,Y,Z,A,B,C,D> extends WithValues10<T,U,V,W,X,Y,Z,A,B,C> {

    /**
     * Get the 11th element of <code>D</code>
     * @return  value of the 11th element of type <code>D</code>
     */
    default D getEleventh() {
        return (D) getValue(10);
    }

}
