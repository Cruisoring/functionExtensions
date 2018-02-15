package com.easyworks;

import com.easyworks.utility.Defaults;
import junit.framework.TestCase;

public class DefaultsTest extends TestCase {

    public void testDefaultValue() {
        assertEquals(false, Defaults.defaultOfType(boolean.class).booleanValue());
        assertEquals('\0', Defaults.defaultOfType(char.class).charValue());
        assertEquals(0, Defaults.defaultOfType(byte.class).byteValue());
        assertEquals(0, Defaults.defaultOfType(short.class).shortValue());
        assertEquals(0, Defaults.defaultOfType(int.class).intValue());
        assertEquals(0, Defaults.defaultOfType(long.class).longValue());
        assertEquals(0.0f, Defaults.defaultOfType(float.class).floatValue());
        assertEquals(0.0d, Defaults.defaultOfType(double.class).doubleValue());
        assertNull(Defaults.defaultOfType(void.class));
        assertNull(Defaults.defaultOfType(String.class));
    }

    public void testGet() {
        assertTrue(false == Defaults.defaultOfInstance(true));
        assertTrue('\0' == Defaults.defaultOfInstance('a'));
        assertTrue(0 == Defaults.defaultOfInstance(0x7));
        assertTrue(0 == Defaults.defaultOfInstance(33));
        assertTrue(0L == Defaults.defaultOfInstance(50L));
        assertTrue(0.0f == Defaults.defaultOfInstance(38.3f));
        assertTrue(0.0d == Defaults.defaultOfInstance(77d));
    }
}