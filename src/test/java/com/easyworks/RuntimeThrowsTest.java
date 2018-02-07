package com.easyworks;

import com.easyworks.function.BiPredicateThrows;
import com.easyworks.function.PredicateThrows;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class RuntimeThrowsTest extends TestCase {

    @Test
    public void testTest1() {
        PredicateThrows<File> p = file -> file.getCanonicalPath().length() > 100;
        //Test would fail with NullPointException
        RuntimeThrows.test(null, p);
    }

    @Test
    public void testTest2() {
        BiPredicateThrows<String, Integer> p = (s, i) -> s.length() > i;
        //Test would fail with NullPointException
        RuntimeThrows.test(null, 5, p);
    }

    @Test
    public void testRun() {
        //Test would fail with IOException
        RuntimeThrows.run(() -> {throw new IOException();});
    }
}