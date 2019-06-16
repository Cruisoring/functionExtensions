package io.github.cruisoring.utility;

import org.junit.Test;

import static io.github.cruisoring.Asserts.*;

public class ResourceHelperTest {

    @Test
    public void testResourcePaths() {
        String[] resourcePaths = ResourceHelper.resourcePaths;
        assertAllTrue(resourcePaths.length == 2,
                resourcePaths[0].endsWith("src/test/resources/"),
                resourcePaths[1].endsWith("src/main/resources/"));
    }

    @Test
    public void isResourceAvailable() {
        assertAllTrue(ResourceHelper.isResourceAvailable("test.sql"));
        assertAllFalse(ResourceHelper.isResourceAvailable("test.txt"));
    }

    @Test
    public void getTextFromResourceFile() {
        assertEquals("select * from  TV_T_BASIS_USERROLE where user_id = 'abc'", ResourceHelper.getTextFromResourceFile("test.sql"));
    }
}