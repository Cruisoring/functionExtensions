package io.github.cruisoring.tuple;

public interface WithValues7<T,U,V,W,X,Y,Z> extends WithValues6<T,U,V,W,X,Y> {

    /**
     * Get the seventh element of <code>Z</code>
     * @return  value of the seventh element of type <code>Z</code>
     */
    default Z getSeventh() {
        return (Z) getValue(6);
    }
}
