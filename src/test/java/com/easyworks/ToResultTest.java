package com.easyworks;

import com.easyworks.utilities.Result;
import org.junit.Test;

import static org.junit.Assert.*;

public class ToResultTest {
    @Test
    public void run() throws Exception {
        int number =3;
        assertEquals(Result.Success, ToResult.run(()->System.out.print("OK")));
    }

    @Test
    public void run1() throws Exception {
    }

    @Test
    public void accept() throws Exception {
    }

    @Test
    public void accept1() throws Exception {
    }

    @Test
    public void test1() throws Exception {
    }

    @Test
    public void test2() throws Exception {
    }

    @Test
    public void get() throws Exception {
    }

    @Test
    public void test3() throws Exception {
    }

    @Test
    public void test4() throws Exception {
    }

    @Test
    public void accept2() throws Exception {
    }

}