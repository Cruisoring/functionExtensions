package io.github.cruisoring.tuple;

public interface WithValues6<T,U,V,W,X,Y> extends WithValues5<T,U,V,W,X> {

    /**
     * Get the sixth element of <code>Y</code>
     * @return  value of the sixth element of type <code>Y</code>
     */
    default Y getSixth() {
        return (Y) getValue(5);
    }

}
