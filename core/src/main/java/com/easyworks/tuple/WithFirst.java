package com.easyworks.tuple;

public interface WithFirst<T> extends WithValues {
    /**
     * Get the first element of <code>T</code>
     * @return  value of the persisted element of type <code>T</code>
     */
    default T getFirst() {
        return (T)getValueAt(0);
    }

}