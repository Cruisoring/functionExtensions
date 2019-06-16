package io.github.cruisoring.utility;

import io.github.cruisoring.logger.Logger;
import org.junit.Test;

import static io.github.cruisoring.Asserts.assertEquals;
import static io.github.cruisoring.Asserts.assertLogging;

public class StackTraceHelperTest {

    @Test
    public void getStackTrace() {
        assertLogging(() -> Logger.D("%s", StackTraceHelper.getFilteredStacks(100, null)),
                "StackTraceHelperTest.getStackTrace(StackTraceHelperTest.java",
                "Asserts.assertLogging(Asserts.java:", "StackTraceHelperTest.java:14");
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
    public void testGetCallerLabel() {
        assertLogging(() -> Logger.D(StackTraceHelper.getCallerLabel(null)), "testGetCallerLabel");
        assertLogging(() -> Logger.D(StackTraceHelper.getCallerLabel(null, "StackTraceHelper.java")), "testGetCallerLabel");
    }
}