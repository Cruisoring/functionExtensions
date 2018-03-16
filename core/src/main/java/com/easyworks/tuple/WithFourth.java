package com.easyworks.tuple;

public interface WithFourth<T,U,V,W> extends WithThird<T,U,V> {

    /**
     * Get the fourth element of <code>W</code>
     * @return  value of the fourth element of type <code>W</code>
     */
    default W getFourth() {
        return (W)getValueAt(3);
    }

}
