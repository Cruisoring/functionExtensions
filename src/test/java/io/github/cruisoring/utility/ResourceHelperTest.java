package io.github.cruisoring.utility;

import org.junit.Test;

import static io.github.cruisoring.Asserts.*;

public class ResourceHelperTest {

    @Test
    public void isResourceAvailable() {
        assertTrue(ResourceHelper.isResourceAvailable("test.sql"));
        assertFalse(ResourceHelper.isResourceAvailable("test.txt"));
    }

    @Test
    public void getTextFromResourceFile() {
        assertEquals("select * from  TV_T_BASIS_USERROLE where user_id = 'abc'", ResourceHelper.getTextFromResourceFile("test.sql"));
    }
}