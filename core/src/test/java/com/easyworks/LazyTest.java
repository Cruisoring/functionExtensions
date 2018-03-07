package com.easyworks;

import com.easyworks.utility.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class LazyTest {
    public static List<String> logs = new ArrayList<>();

    @Before
    public void clear(){
        logs.clear();
    }

    @Test
    public void create() throws Exception {
        Lazy<String> refString = new Lazy<String>(() -> "abcdefg");
        Lazy<Integer> refInt = refString.create(str -> str.length());
        Lazy<Boolean> refBoolean = refInt.create(n -> n%2 == 0);

        assertEquals(false, refBoolean.getValue());
        assertTrue(refString.isValueInitialized());
        assertTrue(refInt.isValueInitialized());
    }

    @Test
    public void createWithExtraClosingAction() throws Exception {
        List<String> logs = new ArrayList<>();
        Lazy<Boolean> booleanLazy;
        try(Lazy<String> stringLazy = new Lazy<String>(() -> "1234567",
                s -> logs.add(String.format("stringLazy is closing")));
            Lazy<Integer> integerLazy = stringLazy.create(str -> 37 + str.length(),
                    i -> logs.add(String.format("integerLazy is closing")))) {
            assertEquals(Integer.valueOf(44), integerLazy.getValue());
            assertTrue(stringLazy.isValueInitialized());
            booleanLazy = integerLazy.create(n -> n%2==0,
                    b -> logs.add(String.format("booleanLazy is closing")));
            boolean booleanValue = booleanLazy.getValue();
        }

        assertFalse(booleanLazy.isValueInitialized());
        assertTrue(Arrays.deepEquals(new String[]{"integerLazy is closing", "booleanLazy is closing", "stringLazy is closing"}, logs.toArray()));
    }

    @Test
    public void closeDependency_parentsNotClosed() throws Exception {
        Lazy<String> string1 = new Lazy<String>(() -> "1234567");
        Lazy<Integer> integerLazy = string1.create(str -> 37 + str.length(),
                i -> Logger.L("%d would be reset.", i));
        Lazy<Boolean> booleanLazy = integerLazy.create(i -> i%2 == 0);

        assertTrue(booleanLazy.getValue());
        assertTrue(integerLazy.isValueInitialized());
        assertTrue(string1.isValueInitialized());

        booleanLazy.close();
        assertFalse(booleanLazy.isValueInitialized());
        assertTrue(integerLazy.isValueInitialized());

        string1.close();
        assertFalse(integerLazy.isValueInitialized());
        assertFalse(booleanLazy.isValueInitialized());
        assertFalse(string1.isValueInitialized());

        assertTrue(booleanLazy.getValue());
        assertTrue(integerLazy.isValueInitialized());
        assertTrue(booleanLazy.isValueInitialized());
        assertTrue(string1.isValueInitialized());

    }

    @Test
    public void getValue() throws Exception {
        Lazy<String> string1 = new Lazy<String>(() -> "string1");
        Lazy<Integer> integerLazy = string1.create(s -> s.length());
        Lazy<Boolean> booleanLazy = integerLazy.create(n -> n == 7);

        assertTrue(booleanLazy.getValue());
        assertTrue(integerLazy.isValueInitialized() && (integerLazy.getValue() == 7));
        assertTrue(string1.isValueInitialized() && string1.getValue().equals("string1"));
    }

    private String throwException(){
        throw new IllegalArgumentException("test");
    }

    @Test
    public void getValue2() throws Exception {
        Lazy<String> string1 = new Lazy<String>(this::throwException);
        Lazy<Integer> integerLazy = string1.create(s -> s.length());
        Lazy<Boolean> booleanLazy = integerLazy.create(i -> i == 13);

        //The following step would fail due to the Type Erasures happened with intergerLazy and booleanLazy
//        assertTrue(booleanLazy.getValue());
//        assertTrue(integerLazy.isValueInitialized() && (integerLazy.getValue() == 13));
//        assertTrue(!string1.isValueInitialized() && string1.getValue().equals("vvv"));
//        assertTrue(!string1.isValueInitialized());
        //The following results are not intended and shall be fixed in future
        assertNull(booleanLazy.getValue());
        assertTrue(booleanLazy.isValueInitialized());
        assertTrue(integerLazy.isValueInitialized());
        assertTrue(string1.isValueInitialized());
    }

    @Test
    public void reset() throws Exception {
        Lazy<String> string1 = new Lazy<String>(() -> "string1");
        //The value is not created initially, isValueInitialized() returns true after calling getValue()
        assertTrue(!string1.isValueInitialized()
                && string1.getValue().equals("string1")
                && string1.isValueInitialized()
        );
        string1.reset();
        assertFalse(string1.isValueInitialized());
        assertTrue(string1.getValue().equals("string1"));
    }


    @Test
    public void close() throws Exception {

        Lazy<Integer> integerLazy = new Lazy(() -> Integer.valueOf(33),
                t -> logs.add("You shall see me after the test :)"));
        integerLazy.close();
        assertEquals("You shall see me after the test :)", logs.get(0));
    }

    public class Account implements AutoCloseable {
        private String email;
        public Account(String email){this.email = email; logs.add("Account created: " + email);}
        @Override
        public void close() throws Exception {
            logs.add("Account closed: " + email);
        }
    }
    public class Inbox implements AutoCloseable {
        public Inbox(Account account){ logs.add("Inbox opened");}
        public void checkMail(){logs.add("Check inbox");}
        @Override
        public void close() throws Exception {
            logs.add("Inbox closed");
        }
    }
    public class Outbox implements AutoCloseable{
        public Outbox(Account account){ logs.add("Outbox opened");}
        public void sendMail(){logs.add("Send mail");}
        @Override
        public void close() throws Exception {
            logs.add("Outbox closed");
        }
    }

    @Test
    public void chain_creation_sample(){
        Lazy<String> emailLazy = new Lazy<>(() -> "email@test.com");
        //With try-with-resources pattern:
//        try(Lazy<Account> accountLazy = emailLazy.create(Account::new)){
//            Lazy<Inbox> inboxLazy = accountLazy.create(Inbox::new);
//            Lazy<Outbox> outboxLazy = accountLazy.create(Outbox::new);
//            outboxLazy.getValue().sendMail();
//            inboxLazy.getValue().checkMail();
//        }catch (Exception ex){
//        }

        Lazy<Account> accountLazy = emailLazy.create(Account::new);
        Lazy<Inbox> inboxLazy = accountLazy.create(Inbox::new);
        Lazy<Outbox>  outboxLazy = accountLazy.create(Outbox::new);
        outboxLazy.getValue().sendMail();
        inboxLazy.getValue().checkMail();
        emailLazy.reset();  //Instead of emailLazy.close() to avoid Exception try-catch

        assertTrue(Arrays.deepEquals(logs.toArray(), new String[]{
                "Account created: email@test.com", "Outbox opened", "Send mail", "Inbox opened", "Check inbox",
                "Outbox closed", "Inbox closed", "Account closed: email@test.com"
        }));
    }
}