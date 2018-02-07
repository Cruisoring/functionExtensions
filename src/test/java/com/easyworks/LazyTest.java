package com.easyworks;

import com.easyworks.utility.Lazy;
import org.junit.Test;

import static org.junit.Assert.*;

public class LazyTest {

    @Test
    public void attach() throws Exception {
        Lazy<String> string1 = new Lazy<String>(() -> "string1");
        Lazy<Integer> integerLazy = string1.attach(() -> Integer.valueOf(44));
        assertEquals(Integer.valueOf(44), integerLazy.getValue());
        string1.close();
        assertFalse(integerLazy.isValueCreated());
    }

    @Test
    public void attach1() throws Exception {
        Lazy<String> string1 = new Lazy<String>(() -> "string1");
        Lazy<Integer> integerLazy = string1.attach(() -> Integer.valueOf(44),
                () -> System.out.println("The optional RunnableThrows do nothing.")
        );
        assertEquals(Integer.valueOf(44), integerLazy.getValue());
        string1.close();
        assertTrue(integerLazy.isValueCreated());
    }

    @Test
    public void attach2() throws Exception {
        Lazy<String> string1 = new Lazy<String>(() -> "string1");
        Lazy<Integer> integerLazy = string1.attach(() -> Integer.valueOf(44));
        Lazy<Boolean> booleanLazy = integerLazy.attach(() -> Boolean.TRUE);

        assertEquals(Integer.valueOf(44), integerLazy.getValue());
        assertTrue(booleanLazy.getValue());
        string1.close();
        assertFalse(integerLazy.isValueCreated());
        assertFalse(booleanLazy.isValueCreated());
    }

    @Test
    public void isValueCreated() throws Exception {
        Lazy<Integer> integerLazy=null;
        Lazy<Boolean> booleanLazy = null;
        try(Lazy<String> stringLazy = new Lazy<String>(() -> "ok")){
            integerLazy = stringLazy.attach(() -> Integer.valueOf(44));
            assertEquals(Integer.valueOf(44), integerLazy.getValue());
            booleanLazy = integerLazy.attach(() -> Boolean.TRUE);
            assertTrue(booleanLazy.getValue());
        }finally {
            assertNotNull(integerLazy);
            assertFalse(integerLazy.isValueCreated());
            assertFalse(booleanLazy.isValueCreated());
        }
    }

    @Test
    public void getValue() throws Exception {
        Lazy<String> string1 = new Lazy<String>(() -> "string1");
        Lazy<Integer> integerLazy = string1.attach(() -> string1.getValue().length());
        Lazy<Boolean> booleanLazy = integerLazy.attach(() -> integerLazy.getValue() == 7);

        assertTrue(booleanLazy.getValue());
        assertTrue(integerLazy.isValueCreated() && (integerLazy.getValue() == 7));
        assertTrue(string1.isValueCreated() && string1.getValue().equals("string1"));
    }

    private String throwException(){
        throw new IllegalArgumentException("test");
    }

    @Test
    public void getValue2() throws Exception {
        Lazy<String> string1 = new Lazy<String>(this::throwException);
        Lazy<Integer> integerLazy = string1.attach(() -> string1.getValue("Default value").length());
        Lazy<Boolean> booleanLazy = integerLazy.attach(() -> integerLazy.getValue() == 13);

        assertTrue(booleanLazy.getValue());
        assertTrue(integerLazy.isValueCreated() && (integerLazy.getValue() == 13));
        assertTrue(!string1.isValueCreated() && string1.getValue("vvv").equals("vvv"));
        assertTrue(!string1.isValueCreated());
    }

    @Test
    public void reset() throws Exception {
        Lazy<String> string1 = new Lazy<String>(() -> "string1");
        //The value is not created initially, isValueCreated() returns true after calling getValue()
        assertTrue(!string1.isValueCreated()
                && string1.getValue().equals("string1")
                && string1.isValueCreated()
        );
        string1.reset();
        assertFalse(string1.isValueCreated());
        assertTrue(string1.getValue().equals("string1"));
    }


    @Test
    public void close() throws Exception {
        String message = null;
        Lazy<Integer> integerLazy = new Lazy(() -> Integer.valueOf(33),
                () -> System.out.print("You shall see me after the test :)"));
        integerLazy.close();
    }

}