package com.easyworks.repository;

import com.easyworks.Functions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RepositoryTest {

    @Test
    public void apply() {
        Repository<String, Integer> repository = new Repository<>(s -> s.length());
        assertEquals(0, Functions.ReturnsDefaultValue.apply(() -> repository.apply("")));
        assertEquals(1, Functions.ReturnsDefaultValue.apply(() -> repository.apply("X")));
        assertEquals(3, Functions.ReturnsDefaultValue.apply(() -> repository.apply("abc")));

        //Because Type Erasure, the return type of the Lambda s -> s.length() is deemed as Object, thus null would be returned
        assertEquals(null, Functions.ReturnsDefaultValue.apply(() -> repository.apply(null)));
    }

    @Test
    public void get() {
        Repository<String, Integer> repository = new Repository<>(s -> s.length());
        assertEquals(Integer.valueOf(0), repository.get("", -1));
        assertEquals(Integer.valueOf(1), repository.get("a", -1));
        assertEquals(Integer.valueOf(3), repository.get("abc", -1));

        assertEquals(Integer.valueOf(-1), repository.get(null, -1));
        assertEquals(3, repository.storage.size());
    }

    @Test
    public void repeativeGet(){
        Repository<Key, Value> repository = new Repository<>(key -> new Value(key.id.length()));
        assertEquals(new Value(0), repository.get(new Key(""), null));
        assertEquals(new Value(1), repository.get(new Key("a"), null));
        assertEquals(new Value(1), repository.get(new Key("b"), null));
        assertEquals(new Value(1), repository.get(new Key("b"), null));
        assertEquals(new Value(0), repository.get(new Key(""), null));
        assertEquals(new Value(10), repository.get(new Key("0123456789"), null));
        assertEquals(null, repository.get(new Key(null), null));
        assertEquals(4, repository.storage.size());
    }

    List<String> logs = new ArrayList();
    class Key implements AutoCloseable {
        private String id;
        public Key(String s) { id = s;}
        @Override public void close() throws Exception {
            logs.add(String.format("Key '%s' closed", id));
        }
        @Override public int hashCode(){return id.hashCode();}
        @Override public boolean equals(Object other){ return other instanceof Key && ((Key) other).id.equals(this.id); }
    }
    class Value implements AutoCloseable {
        private Integer value;
        public Value(Integer n) {value = n;}
        @Override public void close() throws Exception {
            logs.add(String.format("Value %s closed", value));
        }
        @Override public int hashCode(){return value.hashCode();}
        @Override public boolean equals(Object other){ return other instanceof Value && ((Value) other).value.equals(this.value); }
    }
//
//    @Test
//    public void reset() {
//        Repository<Key, Value> repository = new Repository<>(key -> new Value(key.id.length()));
//        assertEquals(new Value(0), repository.get(new Key(""), null));
//        assertEquals(new Value(1), repository.get(new Key("a"), null));
//        assertEquals(new Value(1), repository.get(new Key("b"), null));
//        assertEquals(new Value(1), repository.get(new Key("b"), null));
//        assertEquals(new Value(0), repository.get(new Key(""), null));
//        assertEquals(new Value(10), repository.get(new Key("0123456789"), null));
//        assertEquals(null, repository.get(new Key(null), null));
//        assertEquals(4, repository.storage.size());
//        String[] logsArray = logs.toArray(new String[logs.size()]);
//        Arrays.sort(logsArray);
//        assertTrue(TypeHelper.valueEquals(new String[]{"Key '' closed", "Key '0123456789' closed", "Key 'a' closed", "Key 'b' closed",
//                "Value 0 closed", "Value 1 closed", "Value 1 closed", "Value 10 closed"}, logsArray));
//        assertEquals(0, repository.storage.size());
//    }
}