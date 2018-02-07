package com.easyworks;

import com.easyworks.utility.Defaults;
import junit.framework.TestCase;

public class DefaultsTest extends TestCase {

    public void testDefaultValue() {
        assertEquals(false, Defaults.defaultValue(boolean.class).booleanValue());
        assertEquals('\0', Defaults.defaultValue(char.class).charValue());
        assertEquals(0, Defaults.defaultValue(byte.class).byteValue());
        assertEquals(0, Defaults.defaultValue(short.class).shortValue());
        assertEquals(0, Defaults.defaultValue(int.class).intValue());
        assertEquals(0, Defaults.defaultValue(long.class).longValue());
        assertEquals(0.0f, Defaults.defaultValue(float.class).floatValue());
        assertEquals(0.0d, Defaults.defaultValue(double.class).doubleValue());
        assertNull(Defaults.defaultValue(void.class));
        assertNull(Defaults.defaultValue(String.class));
    }

    public void testGet() {
        assertTrue(false == Defaults.defaultValue(true));
        assertTrue('\0' == Defaults.defaultValue('a'));
        assertTrue(0 == Defaults.defaultValue(0x7));
        assertTrue(0 == Defaults.defaultValue(33));
        assertTrue(0L == Defaults.defaultValue(50L));
        assertTrue(0.0f == Defaults.defaultValue(38.3f));
        assertTrue(0.0d == Defaults.defaultValue(77d));
    }
}