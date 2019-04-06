package io.github.cruisoring.tuple;

public interface WithValues8<T,U,V,W,X,Y,Z,A> extends WithValues7<T,U,V,W,X,Y,Z> {

    /**
     * Get the eighth element of <code>A</code>
     * @return  value of the eighth element of type <code>A</code>
     */
    default A getEighth() {
        return (A) getValue(7);
    }
}
