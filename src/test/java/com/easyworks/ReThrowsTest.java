package com.easyworks;

import com.easyworks.function.BiPredicateThrows;
import com.easyworks.function.PredicateThrows;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ReThrowsTest extends TestCase {

    @Test(expected = RuntimeException.class)
    public void testTest1() {
        PredicateThrows<File> p = file -> file.getCanonicalPath().length() > 100;
        ReThrows.test(p, null);
    }

    @Test(expected = RuntimeException.class)
    public void testTest2() {
        BiPredicateThrows<String, Integer> p = (s, i) -> s.length() > i;
        ReThrows.test(p, null, 5);
    }

    @Test(expected = RuntimeException.class)
    public void testRun() {
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