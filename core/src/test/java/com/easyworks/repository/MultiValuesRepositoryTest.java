package com.easyworks.repository;

import com.easyworks.tuple.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class MultiValuesRepositoryTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void toSingleValuesRepository() {
        SingleValuesRepository<String, Integer> repository = MultiValuesRepository.toSingleValuesRepository(s -> s.length());
        assertEquals(Integer.valueOf(0), repository.getFirstValue(""));
        assertEquals(Integer.valueOf(1), repository.getFirstValue(" "));
        assertEquals(Integer.valueOf(10), repository.getFirstValue("0123456789"));
        assertEquals(null, repository.getFirstValue(null));
        assertTrue(repository.containsKey(""));
        assertTrue(repository.containsKey(" "));
        assertTrue(repository.containsKey("0123456789"));
        assertFalse(repository.containsKey(null));
        assertTrue(Arrays.equals(repository.getValue().keySet().toArray(new String[3]), new String[]{"", " ", "0123456789"}));
        assertTrue(Arrays.equals(repository.getValue().values().toArray(new Tuple[3]), new Tuple[]{Tuple.create(0), Tuple.create(1), Tuple.create(10)}));
    }

    @Test
    public void toDualValuesRepository() {
        DualValuesRepository<String, Integer, Boolean> repository = MultiValuesRepository.toDualValuesRepository(s ->
                Tuple.create(s.length(), s.contains("?")));
        assertEquals(Integer.valueOf(0), repository.getFirstValue(""));
        assertEquals(Integer.valueOf(1), repository.getFirstValue(" "));
        assertEquals(Integer.valueOf(11), repository.getFirstValue("0123456789?"));
        assertEquals(false, repository.getSecondValue(""));
        assertEquals(false, repository.getSecondValue(" "));
        assertEquals(true, repository.getSecondValue("0123456789?"));
        assertEquals(null, repository.getFirstValue(null));
        assertEquals(null, repository.getSecondValue(null));
        assertTrue(repository.containsKey(""));
        assertTrue(repository.containsKey(" "));
        assertTrue(repository.containsKey("0123456789?"));
        assertFalse(repository.containsKey(null));
        assertFalse(repository.containsKey(1233455));
        assertTrue(Arrays.equals(repository.getValue().keySet().toArray(new String[3]), new String[]{"", " ", "0123456789?"}));
        assertTrue(Arrays.equals(repository.getValue().values().toArray(new Tuple[3]),
                new Tuple[]{Tuple.create(0,false), Tuple.create(1,false), Tuple.create(11, true)}));
    }

    @Test
    public void toTripleValuesRepository() {
        TripleValuesRepository<String, Integer, Boolean, Character> repository = MultiValuesRepository.toTripleValuesRepository(s ->
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

        assertFalse(repository.containsKey(null));
        assertFalse(repository.containsKey(""));
        assertTrue(repository.containsKey(" "));
        assertTrue(repository.containsKey("0123456789?"));
        assertFalse(repository.containsKey(1233455));
        assertTrue(Arrays.equals(repository.getValue().keySet().toArray(new String[2]), new String[]{" ", "0123456789?"}));
        assertTrue(Arrays.equals(repository.getValue().values().toArray(new Tuple[2]),
                new Tuple[]{Tuple.create(1,false, ' '), Tuple.create(11, true, '0')}));
    }

    @Test
    public void toQuadValuesRepository() {
        QuadValuesRepository<String, Integer, Boolean, Character, char[]> repository = MultiValuesRepository.toQuadValuesRepository(s ->
                Tuple.create(s.length(), s.contains("?"), s.charAt(0), s.toCharArray()));
        assertEquals(null, repository.getFirstValue(null));
        //StringIndexOutOfBoundsException thrown with key of empty String
        assertEquals(null, repository.getFirstValue(""));

        assertEquals(Character.valueOf(' '), repository.getThirdValue(" "));

        char[] chars = repository.getFourthValue("0123456789?");
        assertEquals(11, chars.length);
        assertEquals('0', chars[0]);
        assertEquals('?', chars[10]);

        assertFalse(repository.containsKey(null));
        assertFalse(repository.containsKey(""));
        assertTrue(repository.containsKey(" "));
        assertTrue(repository.containsKey("0123456789?"));
        assertFalse(repository.containsKey(' '));
        assertTrue(Arrays.equals(repository.getValue().keySet().toArray(new String[2]), new String[]{" ", "0123456789?"}));
        assertTrue(Arrays.equals(repository.getValue().values().toArray(new Tuple[2]),
                new Tuple[]{Tuple.create(1,false, ' ', new char[]{' '}), Tuple.create(11, true, '0', "0123456789?".toCharArray())}));
    }

    @Test
    public void toPentaValuesRepository() {
        PentaValuesRepository<String, Integer, Boolean, Character, char[], String> repository = MultiValuesRepository.toPentaValuesRepository(s ->
                Tuple.create(s.length(), s.contains("?"), s.charAt(0), s.toCharArray(), s.substring(5)));
        assertEquals(null, repository.getThirdValue(null));
        //StringIndexOutOfBoundsException thrown with strings whose length is less than 5
        assertEquals(null, repository.getFirstValue(""));
        assertEquals(null, repository.getFourthValue("   "));

        char[] chars = repository.getFourthValue("abcd?");
        assertEquals(5, chars.length);
        assertEquals("", repository.getFifthValue("abcd?"));

        assertEquals("56789?", repository.getFifthValue("0123456789?"));

        assertFalse(repository.containsKey(null));
        assertFalse(repository.containsKey("    "));
        assertTrue(repository.containsKey("abcd?"));
        assertTrue(repository.containsKey("0123456789?"));
        assertFalse(repository.containsKey(' '));
        assertTrue(Arrays.equals(repository.getValue().keySet().toArray(new String[2]), new String[]{"abcd?", "0123456789?"}));
        assertTrue(Arrays.equals(repository.getValue().values().toArray(new Tuple[2]),
                new Tuple[]{Tuple.create(5,true, 'a', "abcd?".toCharArray(), ""), Tuple.create(11, true, '0', "0123456789?".toCharArray(), "56789?")}));

        Penta tuple = repository.retrieve("A test of retrieve");
        assertEquals(3, repository.getSize());
        assertEquals(Tuple.create(18, false, 'A', "A test of retrieve".toCharArray(), "t of retrieve"), tuple);
    }

    @Test
    public void toHaxaValuesRepository() {
        HexaValuesRepository<String, Integer, Boolean, Character, char[], String, Integer> repository = MultiValuesRepository.toHexaValuesRepository(s ->
                Tuple.create(s.length(), s.contains("?"), s.charAt(0), s.toCharArray(), s.substring(5),
                        Character.getNumericValue(s.charAt(s.length()-1))));
        assertEquals(null, repository.getThirdValue(null));
        //StringIndexOutOfBoundsException thrown with strings whose length is less than 5
        assertEquals(null, repository.getFirstValue(""));
        assertEquals(null, repository.getSixthValue("   "));

        char[] chars = repository.getFourthValue("?abcd");
        assertEquals(5, chars.length);
        assertEquals("", repository.getFifthValue("?abcd"));

        assertEquals("456789", repository.getFifthValue("?0123456789"));

        assertFalse(repository.containsKey(null));
        assertFalse(repository.containsKey("    "));
        assertTrue(repository.containsKey("?abcd"));
        assertTrue(repository.containsKey("?0123456789"));
        assertFalse(repository.containsKey(' '));
        assertTrue(Arrays.equals(repository.getValue().keySet().toArray(new String[2]), new String[]{"?abcd", "?0123456789"}));
        assertTrue(Arrays.equals(repository.getValue().values().toArray(new Tuple[2]),
                new Tuple[]{Tuple.create(5,true, '?', "?abcd".toCharArray(), "", 13),
                        Tuple.create(11, true, '?', "?0123456789".toCharArray(), "456789", 9)}));

        Hexa tuple = repository.retrieve("A test of retrieve");
        assertEquals(3, repository.getSize());
        assertEquals(Tuple.create(18, false, 'A', "A test of retrieve".toCharArray(), "t of retrieve", 14), tuple);
    }

    @Test
    public void toHeptaValuesRepository() {
        HeptaValuesRepository<String, Integer, Boolean, Character, char[], String, Integer, Boolean> repository = MultiValuesRepository.toHeptaValuesRepository(s ->
                Tuple.create(s.length(), s.contains("?"), s.charAt(0), s.toCharArray(), s.substring(5),
                        Character.getNumericValue(s.charAt(s.length()-1)), s.length()>7));
        assertEquals(null, repository.getThirdValue(null));
        //StringIndexOutOfBoundsException thrown with strings whose length is less than 5
        assertEquals(null, repository.getFirstValue(""));
        assertEquals(null, repository.getSixthValue("   "));

        char[] chars = repository.getFourthValue("?abcd");
        assertEquals(5, chars.length);
        assertEquals("", repository.getFifthValue("?abcd"));

        assertEquals("456789", repository.getFifthValue("?0123456789"));

        assertFalse(repository.containsKey(null));
        assertFalse(repository.containsKey("    "));
        assertTrue(repository.containsKey("?abcd"));
        assertTrue(repository.containsKey("?0123456789"));
        assertFalse(repository.containsKey(' '));
        assertTrue(Arrays.equals(repository.getValue().keySet().toArray(new String[2]), new String[]{"?abcd", "?0123456789"}));
        assertTrue(Arrays.equals(repository.getValue().values().toArray(new Tuple[2]),
                new Tuple[]{Tuple.create(5,true, '?', "?abcd".toCharArray(), "", 13, false),
                        Tuple.create(11, true, '?', "?0123456789".toCharArray(), "456789", 9, true)}));

        Hepta tuple = repository.retrieve("A test of retrieve");
        assertEquals(3, repository.getSize());
        assertEquals(Tuple.create(18, false, 'A', "A test of retrieve".toCharArray(), "t of retrieve", 14, true), tuple);
    }

    @Test
    public void toMultiValuesRepository() {
        MultiValuesRepository<String> repository = MultiValuesRepository.toMultiValuesRepository(s ->
                Tuple.asTuple(s.length(), s.contains("?"), s.charAt(0), s.toCharArray(), s.substring(5),
                        Character.getNumericValue(s.charAt(s.length()-1)), s.length()>7, s.length()<12));

        Tuple tuple = repository.retrieve("A test of retrieve");
        assertEquals(8, tuple.getLength());
        assertEquals(1, repository.getSize());
        assertEquals(Tuple.asTuple(18, false, 'A', "A test of retrieve".toCharArray(), "t of retrieve", 14, true, false), tuple);


        MultiValuesRepository<String> repositoryOfTuples = MultiValuesRepository.toMultiValuesRepository(s ->
                Tuple.asTuple(
                        Tuple.create(s.length(), s.contains("?")),
                        Tuple.create(s.charAt(0), s.toCharArray(), s.substring(5)),
                        Tuple.create(Character.getNumericValue(s.charAt(s.length()-1)), s.length()>7, s.length()<12)));

        Tuple tuple2 = repositoryOfTuples.retrieve("A test of retrieve");
        assertEquals(3, tuple2.getLength());
        Set<Tuple> tupleSet = tuple2.getSetOf(Tuple.class);
        assertEquals(Tuple.asTuple(18, false), tupleSet.get(0));
        assertEquals(Tuple.asTuple('A', "A test of retrieve".toCharArray(), "t of retrieve"), tupleSet.get(1));
        assertEquals(Tuple.asTuple(14, true, false), tupleSet.get(2));
    }
}