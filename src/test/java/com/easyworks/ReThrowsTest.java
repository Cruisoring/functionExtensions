package com.easyworks;

import com.easyworks.function.BiPredicateThrows;
import com.easyworks.function.PredicateThrows;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ReThrowsTest extends TestCase {

    @Test(expected = NullPointerException.class)
    public void testTest1() {
        PredicateThrows<File> p = file -> file.getCanonicalPath().length() > 100;
        //Test would fail with NullPointException
        ReThrows.test(null, p);
    }

    @Test(expected = NullPointerException.class)
    public void testTest2() {
        BiPredicateThrows<String, Integer> p = (s, i) -> s.length() > i;
        //Test would fail with NullPointException
        ReThrows.test(null, 5, p);
    }

    @Test(expected = IOException.class)
    public void testRun() {
        //Test would fail with IOException
        ReThrows.run(() -> {throw new IOException();});
    }

    public void testGet() {
    }

    public void testAccept() {
    }

    public void testAccept1() {
    }

    public void testApply() {
    }

    public void testApply1() {
    }
}