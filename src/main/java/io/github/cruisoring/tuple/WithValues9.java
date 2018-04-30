package io.github.cruisoring.tuple;

public interface WithValues9<T,U,V,W,X,Y,Z,A,B> extends WithValues8<T,U,V,W,X,Y,Z,A> {

    /**
     * Get the nineth element of <code>B</code>
     * @return  value of the nineth element of type <code>B</code>
     */
    default B getNineth() {
        return (B)getValueAt(8);
    }
}
