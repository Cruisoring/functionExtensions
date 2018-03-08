package com.easyworks.repository;

import com.easyworks.tuple.Set;
import com.easyworks.tuple.Tuple;
import com.easyworks.utility.ArrayHelper;
import com.easyworks.utility.Logger;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TupleRepositoryTest {

    public static void assertMatch(Tuple[] tuplesExpected, Tuple[] tuples){
        if(tuplesExpected.length != tuples.length)
            fail("Tuple array of different length");

        if(!ArrayHelper.matchWithoutOrder(tuplesExpected, tuples)){
            Logger.L("Expected: %s    !=   %s", Tuple.asTuple(tuplesExpected), Tuple.asTuple(tuples));
            fail("Failed with Arrays.deepEquals after sorting");
        }
    }

    @Test
    public void toSingleValuesRepository1() {
        SingleValuesRepository.SingleValuesRepository1<String, Integer> repository = TupleRepository.toSingleValuesRepository(
                s -> s.length()
        );
        assertEquals(Integer.valueOf(0), repository.getFirst(""));
        assertEquals(Integer.valueOf(1), repository.getFirst(" "));
        assertEquals(Integer.valueOf(10), repository.getFirst("0123456789"));
        assertEquals(null, repository.getFirst(null));
        assertTrue(repository.containsKeyOf(""));
        assertTrue(repository.containsKeyOf(" "));
        assertTrue(repository.containsKeyOf("0123456789"));
        assertFalse(repository.containsKeyOf(null));

        assertTrue(repository.containsKey(""));
        assertTrue(repository.containsKey(" "));
        assertTrue(repository.containsKey("0123456789"));
        assertFalse(repository.containsKey(null));
        repository.containsKey("");

        assertMatch(repository.getValue().keySet().toArray(new Tuple[3]), new Tuple[]{Tuple.create("0123456789"), Tuple.create(""), Tuple.create(" ")});
        assertMatch(repository.getValue().values().toArray(new Tuple[3]), new Tuple[]{Tuple.create(10), Tuple.create(0), Tuple.create(1)});
    }

    @Test
    public void toSingleValuesRepository2() {
        SingleValuesRepository.SingleValuesRepository2<String, String, Integer> repository = TupleRepository.toSingleValuesRepository(
                (s1,s2) -> s1.length() + s2.length()
        );
        assertEquals(Integer.valueOf(0), repository.getFirst("",""));
        assertEquals(Integer.valueOf(1), repository.getFirst(" ", ""));
        assertEquals(Integer.valueOf(12), repository.getFirst("0123456789", "aa"));
        assertEquals(null, repository.getFirst(null, "abc"));
        assertTrue(repository.containsKeyOf("",""));
        assertTrue(repository.containsKeyOf(" ", ""));
        assertFalse(repository.containsKeyOf("", " "));
        assertTrue(repository.containsKeyOf("0123456789", "aa"));
        assertFalse(repository.containsKeyOf("aa", "0123456789"));
        assertFalse(repository.containsKeyOf(null, "abc"));
        assertTrue(repository.containsKey(Tuple.create("0123456789", "aa")));
        assertTrue(repository.containsKey(Tuple.create(" ", "")));
        assertFalse(repository.containsKey(Tuple.create(" ", "0123456789")));
        assertFalse(repository.containsKey(null));

        assertMatch(new Tuple[]{Tuple.create("",""), Tuple.create(" ", ""), Tuple.create("0123456789", "aa")}
            , repository.getValue().keySet().toArray(new Tuple[3]));
        assertMatch(new Tuple[]{Tuple.create(12), Tuple.create(0), Tuple.create(1)}
            , repository.getValue().values().toArray(new Tuple[3]));
    }

    @Test
    public void toSingleValuesRepository3() {
        SingleValuesRepository.SingleValuesRepository3<String, String, Boolean, Integer> repository = TupleRepository.toSingleValuesRepository(
                (s1,s2,b) -> s1.length() + (b?s2.length():0)
        );
        assertEquals(Integer.valueOf(0), repository.getFirst("", "", true));
        assertEquals(Integer.valueOf(0), repository.getFirst("","  ", false));
        assertEquals(Integer.valueOf(1), repository.getFirst(" ", null, false));
        assertEquals(Integer.valueOf(12), repository.getFirst("0123456789", "ab", true));
        assertEquals(null, repository.getFirst(null, "abc", false));
        assertTrue(repository.containsKeyOf("", "", true));
        assertTrue(repository.containsKeyOf("","  ", false));
        assertFalse(repository.containsKeyOf("0123456789", "ab", false));
        assertTrue(repository.containsKeyOf("0123456789", "ab", true));
        assertFalse(repository.containsKeyOf(null, "abc", false));
        assertTrue(repository.containsKey(Tuple.create("0123456789", "ab", true)));
        assertFalse(repository.containsKey(null));

        assertMatch(new Tuple[]{
                Tuple.create("", "", true), Tuple.create("","  ", false), Tuple.create(" ", null, false)
                        , Tuple.create("0123456789", "ab", true)}
                , repository.getValue().keySet().toArray(new Tuple[4]));
        assertMatch(new Tuple[]{Tuple.create(12), Tuple.create(0), Tuple.create(1), Tuple.create(0)}
                , repository.getValue().values().toArray(new Tuple[4]));
    }

    @Test
    public void toSingleValuesRepository4() {
        SingleValuesRepository.SingleValuesRepository4<String, String, Boolean, Integer, Integer> repository = TupleRepository.toSingleValuesRepository(
                (s1,s2,b,n) -> s1.length() + (b?s2.length():n)
        );
        assertEquals(Integer.valueOf(0), repository.getFirst("", "", true, 10));
        assertEquals(Integer.valueOf(11), repository.getFirst("","  ", false, 11));
        assertEquals(Integer.valueOf(4), repository.getFirst(" ", null, false, 3));
        assertEquals(Integer.valueOf(8), repository.getFirst("0123456789", "ab", false, -2));
        assertEquals(null, repository.getFirst(null, "abc", false, 33));
        assertTrue(repository.containsKeyOf("", "", true, 10));
        assertTrue(repository.containsKeyOf("","  ", false, 11));
        assertFalse(repository.containsKeyOf("0123456789", "ab", true, -2));
        assertTrue(repository.containsKeyOf("0123456789", "ab", false, -2));
        assertFalse(repository.containsKeyOf(null, "abc", false, 33));
        assertTrue(repository.containsKey(Tuple.create("0123456789", "ab", false, -2)));
        assertFalse(repository.containsKey(null));

        assertMatch(new Tuple[]{
                        Tuple.create("", "", true, 10), Tuple.create("","  ", false, 11), Tuple.create(" ", null, false, 3)
                        , Tuple.create("0123456789", "ab", false, -2)}
                , repository.getValue().keySet().toArray(new Tuple[4]));
        assertMatch(new Tuple[]{Tuple.create(8), Tuple.create(4), Tuple.create(11), Tuple.create(0)}
                , repository.getValue().values().toArray(new Tuple[4]));
    }

    @Test
    public void toSingleValuesRepository5() {
        SingleValuesRepository.SingleValuesRepository5<String, String, Boolean, String, Integer, Integer> repository = TupleRepository.toSingleValuesRepository(
                (s1,s2,b,s3,n) -> s1.length() + s2.length() + (b?s3.length():n)
        );
        assertEquals(Integer.valueOf(3), repository.getFirst("", "", true, "abc", 10));
        assertEquals(Integer.valueOf(13), repository.getFirst("","  ", false, null, 11));
        assertEquals(null, repository.getFirst(" ", null, false, null, 3));
        assertEquals(null, repository.getFirst(null, "abc", false, "", 33));
        assertTrue(repository.containsKeyOf("", "", true, "abc", 10));
        assertTrue(repository.containsKey(Tuple.create("","  ", false, null, 11)));
        assertFalse(repository.containsKey(null));

        assertMatch(new Tuple[]{
                        Tuple.create("", "", true, "abc", 10), Tuple.create("","  ", false, null, 11)}
                , repository.getValue().keySet().toArray(new Tuple[2]));
        assertMatch(new Tuple[]{Tuple.create(3), Tuple.create(13)}
                , repository.getValue().values().toArray(new Tuple[2]));
    }

    @Test
    public void toSingleValuesRepository6() {
        SingleValuesRepository.SingleValuesRepository6<String, String, Boolean, String, Integer, Integer, Boolean> repository = TupleRepository.toSingleValuesRepository(
                (s1,s2,b,s3,n1,n2) -> s1.length() + s2.length() + (b?s3.length():n1) > n2
        );
        assertEquals(false, repository.getFirst("", "", true, "abc", 10, 8));
        assertEquals(true, repository.getFirst("","  ", false, null, 11, 12));
        assertEquals(null, repository.getFirst(" ", null, false, null, 3, 1));
        assertEquals(null, repository.getFirst(null, "abc", false, "", 33, null));

        assertMatch(new Tuple[]{
                        Tuple.create("", "", true, "abc", 10, 8), Tuple.create("","  ", false, null, 11, 12)}
                , repository.getValue().keySet().toArray(new Tuple[2]));
    }

    @Test
    public void toSingleValuesRepository7() {
        SingleValuesRepository.SingleValuesRepository7<Object,Object,Object,Object,Object,Object,Object,Tuple> repository = TupleRepository.toSingleValuesRepository(
                (o1,o2,o3,o4,o5,o6,o7)->{
                    Tuple all = Tuple.create(o1,o2,o3,o4,o5,o6,o7);
                    Set<Integer> integerSet = all.getSetOf(Integer.class);
                    Set<String> stringSet = all.getSetOf(String.class, s->s.length()>1);
                    return Tuple.create(integerSet, stringSet);
                }
        );

        assertEquals(Tuple.create(Tuple.setOf(22,-33), Tuple.setOf("abc", "1234567"))
                , repository.getFirst("", 22, null, 'a', "abc", -33, "1234567"));
    }

    @Test
    public void toDualValuesRepository() {
    }

    @Test
    public void toDualValuesRepository1() {
    }

    @Test
    public void toTriValuesRepository() {
    }

    @Test
    public void toTriValuesRepository1() {
    }

    @Test
    public void toQuadValuesRepository() {
    }

    @Test
    public void toQuadValuesRepository1() {
    }

    @Test
    public void toPentaValuesRepository() {
    }

    @Test
    public void toPentaValuesRepository1() {
    }

    @Test
    public void toHexaValuesRepository() {
    }

    @Test
    public void toHexaValuesRepository1() {
    }

    @Test
    public void toHeptaValuesRepository() {
    }

    @Test
    public void toHeptaValuesRepository1() {
    }
}