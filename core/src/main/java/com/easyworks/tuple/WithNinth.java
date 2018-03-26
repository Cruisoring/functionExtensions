package com.easyworks.tuple;

public interface WithNinth<T,U,V,W,X,Y,Z,A,B> extends WithEighth<T,U,V,W,X,Y,Z,A> {

    /**
     * Get the nineth element of <code>B</code>
     * @return  value of the nineth element of type <code>B</code>
     */
    default B getNineth() {
        return (B)getValueAt(8);
    }
}
