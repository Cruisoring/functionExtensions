package io.github.cruisoring.repository;

import io.github.cruisoring.TypeHelper;
import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple2;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static io.github.cruisoring.Asserts.*;

public class TupleRepositoryTest {

    TupleRepository2<String, Integer, Boolean> dualValuesRepository =
            TupleRepository2.fromKey(s -> Tuple.create(s.length(), s.contains("a")));

    public static void assertMatch(Tuple[] tuplesExpected, Tuple[] tuples) {
        if (tuplesExpected.length != tuples.length)
            fail("Tuple array of different length");

        Arrays.sort(tuplesExpected);
        Arrays.sort(tuples);
        if (!TypeHelper.valueEquals(tuplesExpected, tuples)) {
            Logger.D("Expected: %s    !=   %s", Tuple.of(tuplesExpected), Tuple.of(tuples));
            fail("Failed with Arrays.deepEquals after sorting");
        }
    }

    @Test
    public void retrieve() {
        Tuple2 value = dualValuesRepository.retrieve("abc");
        assertNotNull(value);
        assertEquals(Integer.valueOf(3), dualValuesRepository.getFirstValue("abc"));
        assertEquals(true, dualValuesRepository.getSecondValue("abc"));

        assertEquals(Integer.valueOf(0), dualValuesRepository.getFirstValue(""));
        assertEquals(2, dualValuesRepository.storage.size());

        assertEquals(Integer.valueOf(10), dualValuesRepository.getFirstValue("0123456789"));
        assertEquals(3, dualValuesRepository.storage.size());

        assertNull(dualValuesRepository.retrieve(null));
        assertEquals(3, dualValuesRepository.storage.size());
        assertEquals(null, dualValuesRepository.getFirstValue(null));
        assertEquals(null, dualValuesRepository.getSecondValue(null));
    }

    @Test
    public void toSingleValuesRepository1() {
        TupleRepository1.TupleKeys1<String, Integer> repository = TupleRepository1.fromKeys1(
                s -> Tuple.create(s.length())
        );
        Assert.assertEquals(Integer.valueOf(0), repository.getFirst(""));
        Assert.assertEquals(Integer.valueOf(1), repository.getFirst(" "));
        Assert.assertEquals(Integer.valueOf(10), repository.getFirst("0123456789"));
        Assert.assertEquals(null, repository.getFirst(null));
        assertTrue(repository.containsKeyOf(""),
                repository.containsKeyOf(" "),
                repository.containsKeyOf("0123456789"));
        assertFalse(repository.containsKeyOf(null));

        assertTrue(repository.containsKey(Tuple.create("")),
                repository.containsKey(Tuple.create(" ")),
                repository.containsKey(Tuple.create("0123456789")));
        assertFalse(repository.containsKey(Tuple.create(null)));
        repository.containsKey("");

        assertMatch(repository.storage.keySet().toArray(new Tuple[3]), new Tuple[]{Tuple.create("0123456789"), Tuple.create(""), Tuple.create(" ")});
        assertMatch(repository.storage.values().toArray(new Tuple[3]), new Tuple[]{Tuple.create(10), Tuple.create(0), Tuple.create(1)});
    }

    @Test
    public void toSingleValuesRepository2() {
        TupleRepository1.TupleKeys2<String, String, Integer> repository = TupleRepository1.fromKeys2(
                (s1, s2) -> Tuple.create(s1.length() + s2.length())
        );
        Assert.assertEquals(Integer.valueOf(0), repository.getFirst("", ""));
        Assert.assertEquals(Integer.valueOf(1), repository.getFirst(" ", ""));
        Assert.assertEquals(Integer.valueOf(12), repository.getFirst("0123456789", "aa"));
        Assert.assertEquals(null, repository.getFirst(null, "abc"));
        assertTrue(repository.containsKeyOf("", ""));
        assertTrue(repository.containsKeyOf(" ", ""));
        assertFalse(repository.containsKeyOf("", " "));
        assertTrue(repository.containsKeyOf("0123456789", "aa"));
        assertFalse(repository.containsKeyOf("aa", "0123456789"));
        assertFalse(repository.containsKeyOf(null, "abc"));
        assertTrue(repository.containsKey(Tuple.create("0123456789", "aa")));
        assertTrue(repository.containsKey(Tuple.create(" ", "")));
        assertFalse(repository.containsKey(Tuple.create(" ", "0123456789")));
        assertFalse(repository.containsKey(null));

        assertMatch(new Tuple[]{Tuple.create("", ""), Tuple.create(" ", ""), Tuple.create("0123456789", "aa")}
                , repository.storage.keySet().toArray(new Tuple[3]));
        assertMatch(new Tuple[]{Tuple.create(12), Tuple.create(0), Tuple.create(1)}
                , repository.storage.values().toArray(new Tuple[3]));
    }

    @Test
    public void toSingleValuesRepository3() {
        TupleRepository1.TupleKeys3<String, String, Boolean, Integer> repository = TupleRepository1.fromKeys3(
                (s1, s2, b) -> Tuple.create(s1.length() + (b ? s2.length() : 0))
        );
        Assert.assertEquals(Integer.valueOf(0), repository.getFirst("", "", true));
        Assert.assertEquals(Integer.valueOf(0), repository.getFirst("", "  ", false));
        Assert.assertEquals(Integer.valueOf(1), repository.getFirst(" ", null, false));
        Assert.assertEquals(Integer.valueOf(12), repository.getFirst("0123456789", "ab", true));
        Assert.assertEquals(null, repository.getFirst(null, "abc", false));
        assertTrue(repository.containsKeyOf("", "", true));
        assertTrue(repository.containsKeyOf("", "  ", false));
        assertFalse(repository.containsKeyOf("0123456789", "ab", false));
        assertTrue(repository.containsKeyOf("0123456789", "ab", true));
        assertFalse(repository.containsKeyOf(null, "abc", false));
        assertTrue(repository.containsKey(Tuple.create("0123456789", "ab", true)));
        assertFalse(repository.containsKey(null));

        assertMatch(new Tuple[]{
                        Tuple.create("", "", true), Tuple.create("", "  ", false), Tuple.create(" ", null, false)
                        , Tuple.create("0123456789", "ab", true)}
                , repository.storage.keySet().toArray(new Tuple[4]));
        assertMatch(new Tuple[]{Tuple.create(12), Tuple.create(0), Tuple.create(1), Tuple.create(0)}
                , repository.storage.values().toArray(new Tuple[4]));
    }

    @Test
    public void toSingleValuesRepository4() {
        TupleRepository1.TupleKeys4<String, String, Boolean, Integer, Integer> repository = TupleRepository1.fromKeys4(
                (s1, s2, b, n) -> Tuple.create(s1.length() + (b ? s2.length() : n))
        );
        Assert.assertEquals(Integer.valueOf(0), repository.getFirst("", "", true, 10));
        Assert.assertEquals(Integer.valueOf(11), repository.getFirst("", "  ", false, 11));
        Assert.assertEquals(Integer.valueOf(4), repository.getFirst(" ", null, false, 3));
        Assert.assertEquals(Integer.valueOf(8), repository.getFirst("0123456789", "ab", false, -2));
        Assert.assertEquals(null, repository.getFirst(null, "abc", false, 33));
        assertTrue(repository.containsKeyOf("", "", true, 10));
        assertTrue(repository.containsKeyOf("", "  ", false, 11));
        assertFalse(repository.containsKeyOf("0123456789", "ab", true, -2));
        assertTrue(repository.containsKeyOf("0123456789", "ab", false, -2));
        assertFalse(repository.containsKeyOf(null, "abc", false, 33));
        assertTrue(repository.containsKey(Tuple.create("0123456789", "ab", false, -2)));
        assertFalse(repository.containsKey(null));

        assertMatch(new Tuple[]{
                        Tuple.create("", "", true, 10), Tuple.create("", "  ", false, 11), Tuple.create(" ", null, false, 3)
                        , Tuple.create("0123456789", "ab", false, -2)}
                , repository.storage.keySet().toArray(new Tuple[4]));
        assertMatch(new Tuple[]{Tuple.create(8), Tuple.create(4), Tuple.create(11), Tuple.create(0)}
                , repository.storage.values().toArray(new Tuple[4]));
    }

    @Test
    public void toSingleValuesRepository5() {
        TupleRepository1.TupleKeys5<String, String, Boolean, String, Integer, Integer> repository = TupleRepository1.fromKeys5(
                (s1, s2, b, s3, n) -> Tuple.create(s1.length() + s2.length() + (b ? s3.length() : n))
        );
        Assert.assertEquals(Integer.valueOf(3), repository.getFirst("", "", true, "abc", 10));
        Assert.assertEquals(Integer.valueOf(13), repository.getFirst("", "  ", false, null, 11));
        Assert.assertEquals(null, repository.getFirst(" ", null, false, null, 3));
        Assert.assertEquals(null, repository.getFirst(null, "abc", false, "", 33));
        assertTrue(repository.containsKeyOf("", "", true, "abc", 10));
        assertTrue(repository.containsKey(Tuple.create("", "  ", false, null, 11)));
        assertFalse(repository.containsKey(null));

        assertMatch(new Tuple[]{
                        Tuple.create("", "", true, "abc", 10), Tuple.create("", "  ", false, null, 11)}
                , repository.storage.keySet().toArray(new Tuple[2]));
        assertMatch(new Tuple[]{Tuple.create(3), Tuple.create(13)}
                , repository.storage.values().toArray(new Tuple[2]));
    }

    @Test
    public void toSingleValuesRepository6() {
        TupleRepository1.TupleKeys6<String, String, Boolean, String, Integer, Integer, Boolean> repository = TupleRepository1.fromKeys6(
                (s1, s2, b, s3, n1, n2) -> Tuple.create(s1.length() + s2.length() + (b ? s3.length() : n1) > n2)
        );
        assertEquals(false, repository.getFirst("", "", true, "abc", 10, 8));
        assertEquals(true, repository.getFirst("", "  ", false, null, 11, 12));
        assertEquals(null, repository.getFirst(" ", null, false, null, 3, 1));
        assertEquals(null, repository.getFirst(null, "abc", false, "", 33, null));

        assertMatch(new Tuple[]{
                        Tuple.create("", "", true, "abc", 10, 8), Tuple.create("", "  ", false, null, 11, 12)}
                , repository.storage.keySet().toArray(new Tuple[2]));
    }

    @Test
    public void toSingleValuesRepository7() {
        TupleRepository1.TupleKeys7<Object, Object, Object, Object, Object, Object, Object, Tuple> repository = TupleRepository1.fromKeys7(
                (o1, o2, o3, o4, o5, o6, o7) -> {
                    Tuple<Object> all = Tuple.create(o1, o2, o3, o4, o5, o6, o7);
                    Tuple<Integer> integerSet = all.getSetOf(Integer.class);
                    Tuple<String> stringSet = all.getSetOf(String.class, s -> s.length() > 1);
                    return Tuple.create(Tuple.create(integerSet, stringSet));
                }
        );

        assertEquals(Tuple.create(Tuple.setOf(22, null, -33), Tuple.setOf("abc", "1234567"))
                , repository.getFirst("", 22, null, 'a', "abc", -33, "1234567"));
    }

    @Test
    public void toDualValuesRepository1() {
        TupleRepository2.TupleKeys1<String, Character, Integer> repository = TupleRepository2.fromKeys1(
                s -> Tuple.create(s.charAt(0), s.length())
        );

        assertEquals(Tuple.create(Character.valueOf('a'), 1), repository.retrieve(Tuple.create("a")));
        assertEquals(null, repository.getSecond(null));
        assertEquals(1, repository.getSize());

        assertEquals(Character.valueOf('x'), repository.getFirst("xyz"));
        assertEquals(false, repository.containsKeyOf(null));
        assertEquals(false, repository.containsKeyOf(""));
        assertMatch(new Tuple[]{Tuple.create("a"), Tuple.create("xyz")}, repository.storage.keySet().toArray(new Tuple[2]));
        assertMatch(new Tuple[]{Tuple.create('a', 1), Tuple.create('x', 3)}, repository.storage.values().toArray(new Tuple[2]));
    }

    @Test
    public void toDualValuesRepository4() {
        TupleRepository2.TupleKeys4<String, String, Integer, Boolean, String, Integer> repository = TupleRepository2.fromKeys4(
                (s1, s2, n, b) -> Tuple.create(s1 + s2, n + (b ? s1.length() : s2.length()))
        );

        assertEquals(Tuple.create("abc", 5), repository.retrieve(Tuple.create("a", "bc", 3, false)));
        Assert.assertEquals(true, repository.containsKeyOf("a", "bc", 3, false));
        Assert.assertEquals(null, repository.getSecond("a", null, 3, false));
        assertEquals(1, repository.getSize());

        Assert.assertEquals("12345", repository.getFirst("12", "345", 1, true));
        assertMatch(new Tuple[]{Tuple.create("a", "bc", 3, false), Tuple.create("12", "345", 1, true)},
                repository.storage.keySet().toArray(new Tuple[2]));
        assertMatch(new Tuple[]{Tuple.create("abc", 5), Tuple.create("12345", 3)},
                repository.storage.values().toArray(new Tuple[2]));
    }

    @Test
    public void toTripleValuesRepository1() {
        TupleRepository3.TupleKeys1<String, Boolean, String, Integer> repository = TupleRepository3.fromKeys1(
                s -> Tuple.create(s.length() > 2, s.substring(1), s.indexOf('?'))
        );

        assertEquals(Tuple.create(true, "bc", -1), repository.retrieve(Tuple.create("abc")));
        assertEquals(true, repository.getFirst("what?"));
        assertEquals("ood", repository.getSecond("good"));
        assertEquals(null, repository.getSecond(""));
        assertEquals(Integer.valueOf(0), repository.getThird("?"));

        assertEquals(false, repository.containsKeyOf(null));
        assertEquals(false, repository.containsKeyOf("GOOD"));
        assertMatch(new Tuple[]{Tuple.create("abc"), Tuple.create("good"), Tuple.create("?"), Tuple.create("what?")},
                repository.storage.keySet().toArray(new Tuple[4]));
        assertMatch(new Tuple[]{Tuple.create(true, "bc", -1), Tuple.create(true, "hat?", 4)
                        , Tuple.create(true, "ood", -1), Tuple.create(false, "", 0)},
                repository.storage.values().toArray(new Tuple[4]));
    }

    @Test
    public void toTripleValuesRepository5() {
        TupleRepository3.TupleKeys5<String, Boolean, String, Integer, Character, Boolean, String, Integer> repository = TupleRepository3.fromKeys5(
                (s1, b, s2, i, c) -> Tuple.create(s1.length() > 3 == b, s2.substring(i), s1.indexOf(c))
        );

        assertEquals(Tuple.create(false, "3", 0), repository.retrieve(Tuple.create("abc", true, "123", 2, 'a')));
        assertEquals(null, repository.retrieve(Tuple.create("abc", true, "123", 4, 'a')));
        Assert.assertEquals(true, repository.getFirst("what?", true, "abc", 0, '-'));
        Assert.assertEquals("ood", repository.getSecond("bad", false, "good", 1, 'a'));
        Assert.assertEquals(null, repository.getSecond(null, false, "good", 1, 'a'));
        Assert.assertEquals(Integer.valueOf(1), repository.getThird("bad", false, "good", 1, 'a'));

        Assert.assertEquals(false, repository.containsKeyOf("abc", true, "123", 4, 'a'));
        Assert.assertEquals(false, repository.containsKeyOf(null, false, "good", 1, 'a'));
        assertMatch(new Tuple[]{Tuple.create("abc", true, "123", 2, 'a'), Tuple.create("what?", true, "abc", 0, '-')
                        , Tuple.create("bad", false, "good", 1, 'a')},
                repository.storage.keySet().toArray(new Tuple[3]));
        assertMatch(new Tuple[]{Tuple.create(false, "3", 0), Tuple.create(true, "abc", -1)
                        , Tuple.create(true, "ood", 1)},
                repository.storage.values().toArray(new Tuple[3]));
    }

    @Test
    public void toQuadValuesRepository1() {
        TupleRepository4.TupleKeys1<String, Boolean, String, Integer, Character> repository = TupleRepository4.fromKeys1(
                s -> Tuple.create(s.length() > 2, s.substring(1), s.indexOf('?'), s.charAt(0))
        );

        assertEquals(Tuple.create(true, "bc", -1, Character.valueOf('a')), repository.retrieve(Tuple.create("abc")));
        Assert.assertEquals(true, repository.getFirst("what?"));
        Assert.assertEquals("ood", repository.getSecond("good"));
        Assert.assertEquals(null, repository.getSecond(""));
        Assert.assertEquals(Integer.valueOf(0), repository.getThird("?"));
        Assert.assertEquals(Character.valueOf('w'), repository.getFourth("what?"));

        Assert.assertEquals(false, repository.containsKeyOf(null));
        Assert.assertEquals(false, repository.containsKeyOf("GOOD"));
        assertMatch(new Tuple[]{Tuple.create("abc"), Tuple.create("good"), Tuple.create("?"), Tuple.create("what?")},
                repository.storage.keySet().toArray(new Tuple[4]));
        assertMatch(new Tuple[]{Tuple.create(true, "bc", -1, 'a'), Tuple.create(true, "hat?", 4, 'w')
                        , Tuple.create(true, "ood", -1, 'g'), Tuple.create(false, "", 0, '?')},
                repository.storage.values().toArray(new Tuple[4]));
    }

    @Test
    public void toQuadValuesRepository6() {
        TupleRepository4.TupleKeys6<String, Boolean, String, Integer, Character, Boolean, Boolean, String, String, Integer> repository =
                TupleRepository4.fromKeys6(
                        (s1, b1, s2, i, c, b2) -> Tuple.create(b1 = (s1.length() > 3), s2.substring(i), s1.substring(i), (b2 ? 0 : 100))
                );

        assertEquals(Tuple.create(false, "3", "c", 0), repository.retrieve(Tuple.create("abc", true, "123", 2, 'a', true)));
        assertEquals(null, repository.retrieve(Tuple.create("abc", true, "123", 4, 'a', null)));
        Assert.assertEquals(true, repository.getFirst("what?", true, "abc", 0, '-', true));
        Assert.assertEquals("ood", repository.getSecond("bad", false, "good", 1, 'a', false));
        Assert.assertEquals(null, repository.getSecond(null, false, "good", 1, 'a', true));
        Assert.assertEquals("ad", repository.getThird("bad", false, "good", 1, 'a', false));
        Assert.assertEquals(Integer.valueOf(100), repository.getFourth("bad", false, "good", 1, 'a', false));

        Assert.assertEquals(false, repository.containsKeyOf("abc", true, "123", 4, 'a', null));
        Assert.assertEquals(false, repository.containsKeyOf(null, false, "good", 1, 'a', true));
        assertMatch(new Tuple[]{Tuple.create("abc", true, "123", 2, 'a', true),
                        Tuple.create("what?", true, "abc", 0, '-', true)
                        , Tuple.create("bad", false, "good", 1, 'a', false)},
                repository.storage.keySet().toArray(new Tuple[3]));
        assertMatch(new Tuple[]{Tuple.create(false, "3", "c", 0), Tuple.create(false, "ood", "ad", 100)
                        , Tuple.create(true, "abc", "what?", 0)},
                repository.storage.values().toArray(new Tuple[3]));
    }

    @Test
    public void toPentaValuesRepository1() {
        TupleRepository5.TupleKeys1<String, Boolean, String, Integer, Character, char[]> repository = TupleRepository5.fromKeys1(
                s -> Tuple.create(s.length() > 2, s.substring(1), s.indexOf('?'), s.charAt(0), s.toCharArray())
        );

        assertEquals(Tuple.create(true, "bc", -1, Character.valueOf('a'), "abc".toCharArray()), repository.retrieve(Tuple.create("abc")));
        Assert.assertEquals(true, repository.getFirst("what?"));
        Assert.assertEquals("ood", repository.getSecond("good"));
        Assert.assertEquals(null, repository.getSecond(""));
        Assert.assertEquals(Integer.valueOf(0), repository.getThird("?"));
        Assert.assertEquals(Character.valueOf('w'), repository.getFourth("what?"));
        Object v5 = repository.getFifth("what?");
        //assertTrue(ArrayHelper.matchArrays(TypeHelper.asArray("what?".toCharArray()), (Character[])v5));

        Assert.assertEquals(false, repository.containsKeyOf(null));
        Assert.assertEquals(false, repository.containsKeyOf("GOOD"));
        assertMatch(new Tuple[]{Tuple.create("abc"), Tuple.create("good"), Tuple.create("?"), Tuple.create("what?")},
                repository.storage.keySet().toArray(new Tuple[4]));
        assertMatch(new Tuple[]{
                        Tuple.create(true, "bc", -1, 'a', "abc".toCharArray()),
                        Tuple.create(true, "hat?", 4, 'w', "what?".toCharArray()),
                        Tuple.create(true, "ood", -1, 'g', "good".toCharArray()),
                        Tuple.create(false, "", 0, '?', "?".toCharArray())},
                repository.storage.values().toArray(new Tuple[4]));
    }

    @Test
    public void toPentaValuesRepository7() {
        TupleRepository5.TupleKeys7<String, Integer, Character, Byte, Boolean, String, Short,
                Boolean, String, Integer, Character, char[]> repository = TupleRepository5.fromKeys7(
                (s, n, ch, b, bool, subS, short1) ->
                        Tuple.create((s.charAt(n)) == ch, s.substring((int) b), s.indexOf(subS), s.charAt(short1), new char[]{s.charAt(bool ? n : short1)})
        );

        assertEquals(Tuple.create(true, "bcde", 2, Character.valueOf('d'), new char[]{'b'}),
                repository.retrieve(Tuple.create("abcde", 1, 'b', (byte) 1, true, "cd", (short) 3)));
        Assert.assertEquals(null, repository.getFirst("abcde", 1, 'b', (byte) 1, true, "cd", null));
        Assert.assertEquals(null, repository.getSecond("abcde", 11, 'b', (byte) 1, true, "cd", null));
        Assert.assertEquals(null, repository.getThird("abcde", 1, 'b', (byte) 9, false, "cd", null));

        Assert.assertEquals(false, repository.containsKeyOf("abcde", 1, 'b', (byte) 1, true, "cd", null));
        assertMatch(new Tuple[]{Tuple.create("abcde", 1, 'b', (byte) 1, true, "cd", (short) 3)},
                repository.storage.keySet().toArray(new Tuple[1]));
        assertMatch(new Tuple[]{
                        Tuple.create(true, "bcde", 2, Character.valueOf('d'), new char[]{'b'})},
                repository.storage.values().toArray(new Tuple[1]));
    }

    @Test
    public void toHexaValuesRepository1() {
        TupleRepository6.TupleKeys1<String, Character, Character, Integer, Boolean, String, Integer> repository =
                TupleRepository6.fromKeys1(s -> Tuple.create(
                        s.charAt(0), s.charAt(1), (s.charAt(2) - 'a'), (s.length() % 3) == 0, s.substring(2), s.length()));

        Assert.assertEquals(Character.valueOf('b'), repository.getSecond("abcdef"));
        assertEquals(Tuple.create('a', 'b', 2, true, "cdef", 6), repository.retrieve(Tuple.create("abcdef")));
    }

    @Test
    public void toHexaValuesRepository3() {
        TupleRepository6.TupleKeys3<String, Integer, Boolean,
                Character, Character, Integer, Boolean, String, Integer> repository =
                TupleRepository6.fromKeys3((s, i, b) -> Tuple.create(
                        s.charAt(0), s.charAt(1), (s.charAt(i) - 'a'), (s.length() % (b ? 3 : 5)) == 0, s.substring(i), s.length()));

        Assert.assertEquals(Character.valueOf('b'), repository.getSecond("abcde", 4, true));
        assertEquals(Tuple.create('a', 'b', 4, false, "e", 5), repository.retrieve(Tuple.create("abcde", 4, true)));
    }

    @Test
    public void toHeptaValuesRepository1() {
        TupleRepository7.TupleKeys1<String, Character, Character, Integer, Boolean, String, Integer, Byte> repository =
                TupleRepository7.fromKeys1(s -> Tuple.create(
                        s.charAt(0), s.charAt(1), (s.charAt(2) - 'a'), (s.length() % 3) == 0, s.substring(2), s.length(),
                        (byte) (s.charAt(s.length() - 1))));

        assertEquals(Character.valueOf('b'), repository.getSecond("abcdef"));
        assertEquals(Tuple.create('a', 'b', 2, true, "cdef", 6, (byte) 102), repository.retrieve(Tuple.create("abcdef")));
    }

    @Test
    public void toHeptaValuesRepository5() {
    }
}