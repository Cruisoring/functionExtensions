package com.easyworks.tuple;

public interface WithSecond<T,U> extends WithFirst<T> {
    /**
     * Get the second element of <code>U</code>
     * @return  value of the second element of type <code>U</code>
     */
    default U getSecond() {
        return (U)getValues()[1];
    }

}
