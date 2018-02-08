package com.easyworks.utility;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ClassHelperTest {

    @Test
    public void getClassPredicate_withValueTypes_handleBothValueAndWrapperClass() {
        PredicateThrows<Class> intPredicate = ClassHelper.getClassPredicate(int.class);
        assertTrue(intPredicate.testNoThrows(int.class));
        assertTrue(intPredicate.testNoThrows(Integer.class));
        assertFalse(intPredicate.testNoThrows(Boolean.class));
    }

    @Test
    public void getClassPredicate_withValueArrayTypes_handleBothValueAndWrapperClass() {
        PredicateThrows<Class> intPredicate = ClassHelper.getClassPredicate(int[].class);
        assertTrue(intPredicate.testNoThrows(int[].class));
        assertTrue(intPredicate.testNoThrows(Integer[].class));
        assertFalse(intPredicate.testNoThrows(Boolean[].class));
        assertFalse(intPredicate.testNoThrows(Object[].class));
        assertFalse(intPredicate.testNoThrows(int.class));
    }
}