package io.github.cruisoring.repository;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple5;
import io.github.cruisoring.tuple.Tuple6;
import org.junit.Before;
import org.junit.Test;

import static io.github.cruisoring.Asserts.*;

public class MultiValuesRepositoryTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void toSingleValuesRepository() {
        TupleRepository1<String, Integer> repository = TupleRepository1.fromKey(s -> Tuple.create(s.length()));
        assertEquals(Integer.valueOf(0), repository.getFirstValue(""));
        assertEquals(Integer.valueOf(1), repository.getFirstValue(" "));
        assertEquals(Integer.valueOf(10), repository.getFirstValue("0123456789"));
        assertEquals(null, repository.getFirstValue(null));
        assertTrue(repository.containsKey(""),
                repository.containsKey(" "),
                repository.containsKey("0123456789"));
        assertFalse(repository.containsKey(null));
        assertEquals(new String[]{"", " ", "0123456789"}, repository.storage.keySet().toArray(new String[3]));
        assertEquals(new Tuple[]{Tuple.create(0), Tuple.create(1), Tuple.create(10)}, repository.storage.values().toArray(new Tuple[3]));
    }

    @Test
    public void toDualValuesRepository() {
        TupleRepository2<String, Integer, Boolean> repository = TupleRepository2.fromKey(s ->
                Tuple.create(s.length(), s.contains("?")));
        assertEquals(Integer.valueOf(0), repository.getFirstValue(""));
        assertEquals(Integer.valueOf(1), repository.getFirstValue(" "));
        assertEquals(Integer.valueOf(11), repository.getFirstValue("0123456789?"));
        assertEquals(false, repository.getSecondValue(""));
        assertEquals(false, repository.getSecondValue(" "));
        assertEquals(true, repository.getSecondValue("0123456789?"));
        assertEquals(null, repository.getFirstValue(null));
        assertEquals(null, repository.getSecondValue(null));
        assertTrue(repository.containsKey(""),
                repository.containsKey(" "),
                repository.containsKey("0123456789?"));
        assertFalse(repository.containsKey(null),
                repository.containsKey(1233455));
        assertEquals(new String[]{"", " ", "0123456789?"},
                repository.storage.keySet().toArray(new String[3]));
        assertEquals(new Tuple[]{Tuple.create(0, false), Tuple.create(1, false), Tuple.create(11, true)},
                repository.storage.values().toArray(new Tuple[3]));
    }

    @Test
    public void toTripleValuesRepository() {
        TupleRepository3<String, Integer, Boolean, Character> repository = TupleRepository3.fromKey(s ->
                Tuple.create(s.length(), s.contains("?"), s.charAt(0)));
        assertEquals(null, repository.getFirstValue(null));
        //StringIndexOutOfBoundsException thrown with key of empty String
        assertEquals(null, repository.getFirstValue(""));
        assertEquals(Integer.valueOf(1), repository.getFirstValue(" "));
        assertEquals(false, repository.getSecondValue(" "));
        assertEquals(Character.valueOf(' '), repository.getThirdValue(" "));

        assertEquals(Integer.valueOf(11), repository.getFirstValue("0123456789?"));
        assertEquals(true, repository.getSecondValue("0123456789?"));
        assertEquals(Character.valueOf('0'), repository.getThirdValue("0123456789?"));

        assertFalse(repository.containsKey(null),
                repository.containsKey(""),
                repository.containsKey(1233455));
        assertTrue(repository.containsKey(" "),
                repository.containsKey("0123456789?"));
        assertEquals(new String[]{" ", "0123456789?"}, repository.storage.keySet().toArray(new String[2]));
        assertEquals(new Tuple[]{Tuple.create(1, false, ' '), Tuple.create(11, true, '0')},
                repository.storage.values().toArray(new Tuple[2]));
    }

    @Test
    public void toQuadValuesRepository() {
        TupleRepository4<String, Integer, Boolean, Character, char[]> repository = TupleRepository4.fromKey(s ->
                Tuple.create(s.length(), s.contains("?"), s.charAt(0), s.toCharArray()));
        assertEquals(null, repository.getFirstValue(null));
        //StringIndexOutOfBoundsException thrown with key of empty String
        assertEquals(null, repository.getFirstValue(""));

        assertEquals(Character.valueOf(' '), repository.getThirdValue(" "));

        char[] chars = repository.getFourthValue("0123456789?");
        assertEquals(11, chars.length);
        assertEquals('0', chars[0]);
        assertEquals('?', chars[10]);

        assertFalse(repository.containsKey(null),
                repository.containsKey(""));
        assertTrue(repository.containsKey(" "),
                repository.containsKey("0123456789?"));
        assertFalse(repository.containsKey(' '));
        assertEquals(new String[]{" ", "0123456789?"}, repository.storage.keySet().toArray(new String[2]));
        assertEquals(new Tuple[]{Tuple.create(1, false, ' ', new char[]{' '}), Tuple.create(11, true, '0', "0123456789?".toCharArray())},
                repository.storage.values().toArray(new Tuple[2]));
    }

    @Test
    public void toPentaValuesRepository() {
        TupleRepository5<String, Integer, Boolean, Character, char[], String> repository = TupleRepository5.fromKey(s ->
                Tuple.create(s.length(), s.contains("?"), s.charAt(0), s.toCharArray(), s.substring(5)));
        assertEquals(null, repository.getThirdValue(null));
        //StringIndexOutOfBoundsException thrown with strings whose length is less than 5
        assertEquals(null, repository.getFirstValue(""));
        assertEquals(null, repository.getFourthValue("   "));

        char[] chars = repository.getFourthValue("abcd?");
        assertEquals(5, chars.length);
        assertEquals("", repository.getFifthValue("abcd?"));

        assertEquals("56789?", repository.getFifthValue("0123456789?"));

        assertFalse(repository.containsKey(null),
                repository.containsKey("    "));
        assertTrue(repository.containsKey("abcd?"),
                repository.containsKey("0123456789?"));
        assertFalse(repository.containsKey(' '));
        assertEquals(new String[]{"abcd?", "0123456789?"}, repository.storage.keySet().toArray(new String[2]));
        assertEquals(new Tuple[]{Tuple.create(5, true, 'a', "abcd?".toCharArray(), ""),
                        Tuple.create(11, true, '0', "0123456789?".toCharArray(), "56789?")},
                repository.storage.values().toArray(new Tuple[2]));


        Tuple5 tuple = repository.retrieve("A test of retrieve");
        assertEquals(3, repository.getSize());
        assertEquals(Tuple.create(18, false, 'A', "A test of retrieve".toCharArray(), "t of retrieve"), tuple);
    }

    @Test
    public void toHaxaValuesRepository() {
        TupleRepository6<String, Integer, Boolean, Character, char[], String, Integer> repository = TupleRepository6.fromKey(s ->
                Tuple.create(s.length(), s.contains("?"), s.charAt(0), s.toCharArray(), s.substring(5),
                        Character.getNumericValue(s.charAt(s.length() - 1))));
        assertEquals(null, repository.getThirdValue(null));
        //StringIndexOutOfBoundsException thrown with strings whose length is less than 5
        assertEquals(null, repository.getFirstValue(""));
        assertEquals(null, repository.getSixthValue("   "));

        char[] chars = repository.getFourthValue("?abcd");
        assertEquals(5, chars.length);
        assertEquals("", repository.getFifthValue("?abcd"));

        assertEquals("456789", repository.getFifthValue("?0123456789"));

        assertFalse(repository.containsKey(null),
                repository.containsKey("    "));
        assertTrue(repository.containsKey("?abcd"),
                repository.containsKey("?0123456789"));
        assertFalse(repository.containsKey(' '));
        assertEquals(new String[]{"?abcd", "?0123456789"}, repository.storage.keySet().toArray(new String[2]));
        assertEquals(new Tuple[]{Tuple.create(5, true, '?', "?abcd".toCharArray(), "", 13),
                        Tuple.create(11, true, '?', "?0123456789".toCharArray(), "456789", 9)},
                repository.storage.values().toArray(new Tuple[2]));

        Tuple6 tuple = repository.retrieve("A test of retrieve");
        assertEquals(3, repository.getSize());
        assertEquals(Tuple.create(18, false, 'A', "A test of retrieve".toCharArray(), "t of retrieve", 14), tuple);
    }

    @Test
    public void toHeptaValuesRepository() {
        TupleRepository7<String, Integer, Boolean, Character, char[], String, Integer, Boolean> repository = TupleRepository7.fromKey(s ->
                Tuple.create(s.length(), s.contains("?"), s.charAt(0), s.toCharArray(), s.substring(5),
                        Character.getNumericValue(s.charAt(s.length() - 1)), s.length() > 7));
        assertEquals(null, repository.getThirdValue(null));
        //StringIndexOutOfBoundsException thrown with strings whose length is less than 5
        assertEquals(null, repository.getFirstValue(""));
        assertEquals(null, repository.getSixthValue("   "));

        char[] chars = repository.getFourthValue("?abcd");
        assertEquals(5, chars.length);
        assertEquals("", repository.getFifthValue("?abcd"));

        assertEquals("456789", repository.getFifthValue("?0123456789"));

        assertFalse(repository.containsKey(null), repository.containsKey("    "));
        assertTrue(repository.containsKey("?abcd"), repository.containsKey("?0123456789"));
        assertFalse(repository.containsKey(' '));
        assertEquals(new String[]{"?abcd", "?0123456789"}, repository.storage.keySet().toArray(new String[2]));
        assertEquals(new Tuple[]{Tuple.create(5, true, '?', "?abcd".toCharArray(), "", 13, false),
                        Tuple.create(11, true, '?', "?0123456789".toCharArray(), "456789", 9, true)},
                repository.storage.values().toArray(new Tuple[2]));

        Tuple tuple = repository.retrieve("A test of retrieve");
        assertEquals(3, repository.getSize());
        assertEquals(Tuple.create(18, false, 'A', "A test of retrieve".toCharArray(), "t of retrieve", 14, true), tuple);
    }
}