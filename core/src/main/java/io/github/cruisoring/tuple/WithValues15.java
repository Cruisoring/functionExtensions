package io.github.cruisoring.tuple;

public interface WithValues15<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G,H> extends WithValues14<T,U,V,W,X,Y,Z,A,B,C,D,E,F,G> {

    /**
     * Get the 15th element of <code>H</code>
     * @return  value of the 15th element of type <code>H</code>
     */
    default H getFifteenth() {
        return (H)getValueAt(14);
    }
}
