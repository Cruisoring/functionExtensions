package io.github.cruisoring;

import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.utility.PlainList;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static io.github.cruisoring.Asserts.*;


public class LazyTest {
    public static List<String> logs = new PlainList<>();

    @Before
    public void clear() {
        logs.clear();
    }

    @Test
    public void create() throws Exception {
        Lazy<String> refString = new Lazy<String>(() -> "abcdefg");
        Lazy<Integer> refInt = refString.create(str -> str.length());
        Lazy<Boolean> refBoolean = refInt.create(n -> n % 2 == 0);

        assertEquals(false, refBoolean.getValue());
        assertAllTrue(refString.isValueInitialized(),
                refInt.isValueInitialized());
    }

    @Test
    public void createWithExtraClosingAction() throws Exception {
        List<String> logs = new PlainList<>();
        Lazy<Boolean> booleanLazy;
        try (Lazy<String> stringLazy = new Lazy<String>(() -> "1234567",
                (s0, s1) -> logs.add(String.format("stringLazy changed: %s -> %s", s0, s1)));
             Lazy<Integer> integerLazy = stringLazy.create(str -> 37 + str.length(),
                     (i0, i1) -> logs.add(String.format("integerLazy changed: %s -> %s", i0, i1)))) {
            assertEquals(Integer.valueOf(44), integerLazy.getValue());
            assertAllTrue(stringLazy.isValueInitialized());
            booleanLazy = integerLazy.create(n -> n % 2 == 0,
                    (b0, b1) -> logs.add(String.format("booleanLazy changed: %s -> %s", b0, b1)));
            boolean booleanValue = booleanLazy.getValue();
        }

        assertAllFalse(booleanLazy.isValueInitialized());
        assertAllTrue(TypeHelper.valueEquals(new String[]{"stringLazy changed: null -> 1234567", "integerLazy changed: null -> 44",
                "booleanLazy changed: null -> true", "booleanLazy changed: true -> null",
                "integerLazy changed: 44 -> null", "stringLazy changed: 1234567 -> null"}, logs.toArray()));
    }

    @Test
    public void closeDependency_parentsNotClosed() throws Exception {
        Lazy<String> string1 = new Lazy<String>(() -> "1234567");
        Lazy<Integer> integerLazy = string1.create(str -> 37 + str.length(),
                (i0, i1) -> Logger.D("%d would be closing to %d", i0, i1));
        Lazy<Boolean> booleanLazy = integerLazy.create(i -> i % 2 == 0);

        assertAllTrue(booleanLazy.getValue(),
                integerLazy.isValueInitialized(),
                string1.isValueInitialized());

        booleanLazy.close();
        assertAllFalse(booleanLazy.isValueInitialized());
        assertAllTrue(integerLazy.isValueInitialized());

        string1.close();
        assertAllFalse(integerLazy.isValueInitialized(),
                booleanLazy.isValueInitialized(),
                string1.isValueInitialized());

        assertAllTrue(booleanLazy.getValue(),
                integerLazy.isValueInitialized(),
                booleanLazy.isValueInitialized());
        assertAllTrue(string1.isValueInitialized());

    }

    @Test
    public void getValue() throws Exception {
        Lazy<String> string1 = new Lazy<String>(() -> "string1");
        Lazy<Integer> integerLazy = string1.create(s -> s.length());
        Lazy<Boolean> booleanLazy = integerLazy.create(n -> n == 7);

        assertAllTrue(booleanLazy.getValue(),
                integerLazy.isValueInitialized(),
                integerLazy.getValue() == 7,
                string1.isValueInitialized(),
                string1.getValue().equals("string1"));
    }

    private String throwException() {
        throw new IllegalArgumentException("test");
    }

    @Test
    public void getValue2() throws Exception {
        Lazy<String> string1 = new Lazy<String>(this::throwException);
        Lazy<Integer> integerLazy = string1.create(s -> s.length());
        Lazy<Boolean> booleanLazy = integerLazy.create(i -> i == 13);

        //The following step would fail due to the Type Erasures happened with intergerLazy and booleanLazy
//        assertTrue(booleanLazy.getOriginalSetting());
//        assertTrue(integerLazy.isValueInitialized() && (integerLazy.getOriginalSetting() == 13));
//        assertTrue(!string1.isValueInitialized() && string1.getOriginalSetting().equals("vvv"));
//        assertTrue(!string1.isValueInitialized());
        //The following results are not intended and shall be fixed in future
        assertAllNull(booleanLazy.getValue());
        assertAllTrue(booleanLazy.isValueInitialized(),
                integerLazy.isValueInitialized(),
                string1.isValueInitialized());
    }

    @Test
    public void reset() throws Exception {
        Lazy<String> string1 = new Lazy<String>(() -> "string1");
        //The value is not created initially, isValueInitialized() returns true after calling getOriginalSetting()
        assertAllTrue(!string1.isValueInitialized()
                && string1.getValue().equals("string1")
                && string1.isValueInitialized()
        );
        string1.closing();
        assertAllFalse(string1.isValueInitialized());
        assertAllTrue(string1.getValue().equals("string1"));
    }


    @Test
    public void close() throws Exception {

        Lazy<Integer> integerLazy = new Lazy(() -> Integer.valueOf(33),
                (i0, i1) -> logs.add("You shall see me after the test :)"));
        Integer value = integerLazy.getValue();
        integerLazy.close();
        assertEquals("You shall see me after the test :)", logs.get(0));
    }

    @Test
    public void chain_creation_sample() {
        Lazy<String> emailLazy = new Lazy<>(() -> "email@test.com");
        //*/With try-with-resources pattern:
        try (Lazy<Account> accountLazy = emailLazy.create(Account::new)) {
            Lazy<Inbox> inboxLazy = accountLazy.create(Inbox::new);
            Lazy<Outbox> outboxLazy = accountLazy.create(Outbox::new);
            outboxLazy.getValue().sendMail();
            inboxLazy.getValue().checkMail();
        } catch (Exception ex) {
        }
        /*/
        Lazy<Account> accountLazy = emailLazy.create(Account::new);
        Lazy<Inbox> inboxLazy = accountLazy.create(Inbox::new);
        Lazy<Outbox>  outboxLazy = accountLazy.create(Outbox::new);
        outboxLazy.getOriginalSetting().sendMail();
        inboxLazy.getOriginalSetting().checkMail();
        emailLazy.closing();  //Instead of emailLazy.close() to avoid Exception try-catch
        //*/

        assertAllTrue(TypeHelper.valueEquals(logs.toArray(), new String[]{
                "Account created: email@test.com", "Outbox opened", "Send mail", "Inbox opened", "Check inbox",
                "Outbox closed", "Inbox closed", "Account closed: email@test.com"
        }));
    }

    public class Account implements AutoCloseable {
        private String email;

        public Account(String email) {
            this.email = email;
            logs.add("Account created: " + email);
        }

        @Override
        public void close() throws Exception {
            logs.add("Account closed: " + email);
        }
    }

    public class Inbox implements AutoCloseable {
        public Inbox(Account account) {
            logs.add("Inbox opened");
        }

        public void checkMail() {
            logs.add("Check inbox");
        }

        @Override
        public void close() throws Exception {
            logs.add("Inbox closed");
        }
    }

    public class Outbox implements AutoCloseable {
        public Outbox(Account account) {
            logs.add("Outbox opened");
        }

        public void sendMail() {
            logs.add("Send mail");
        }

        @Override
        public void close() throws Exception {
            logs.add("Outbox closed");
        }
    }
}