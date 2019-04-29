package io.github.cruisoring.utility;

import org.junit.Test;

import static io.github.cruisoring.Asserts.assertEquals;

public class StackTraceHelperTest {

    @Test
    public void getStackTrace() {
        assertEquals(1, StackTraceHelper.getFilteredStacks(100, null).size());
        assertEquals(2, StackTraceHelper.getFilteredStacks(100, null,
                "sun.reflect.NativeMethodAccessorImpl", "java.lang.Thread").size());
        assertEquals(3, StackTraceHelper.getFilteredStacks(100, null,
                "sun.reflect.NativeMethodAccessorImpl").size());
    }

    @Test
    public void getCallerClassNames() {
        assertEquals(new String[]{StackTraceHelperTest.class.getName()},
                StackTraceHelper.getFilteredCallers().toArray());
        assertEquals(new String[]{StackTraceHelper.class.getName(), StackTraceHelper.class.getName(), StackTraceHelperTest.class.getName()},
                StackTraceHelper.getFilteredCallers("sun.reflect.NativeMethodAccessorImpl", "java.lang.Thread").toArray());
    }

    @Test
    public void getCallerClass() {
        assertEquals(StackTraceHelperTest.class, StackTraceHelper.getCallerClass());
    }

    @Test
    public void getCallerLabel() {
        assertEquals("getCallerLabel(StackTraceHelperTest.java:33)", StackTraceHelper.getCallerLabel(null));
        assertEquals("getCallerLabel(StackTraceHelperTest.java:34)", StackTraceHelper.getCallerLabel(null, "StackTraceHelper.java"));
    }
}