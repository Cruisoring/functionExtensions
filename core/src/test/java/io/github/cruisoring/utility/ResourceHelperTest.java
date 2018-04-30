package io.github.cruisoring.utility;

import org.junit.Assert;
import org.junit.Test;

public class ResourceHelperTest {

    @Test
    public void isResourceAvailable() {
        Assert.assertTrue(ResourceHelper.isResourceAvailable("test.sql"));
        Assert.assertFalse(ResourceHelper.isResourceAvailable("test.txt"));
    }

    @Test
    public void getTextFromResourceFile() {
        Assert.assertEquals("select * from  TV_T_BASIS_USERROLE where user_id = 'abc'", ResourceHelper.getTextFromResourceFile("test.sql"));
    }
}