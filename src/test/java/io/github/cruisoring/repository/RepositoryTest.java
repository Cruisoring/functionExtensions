package io.github.cruisoring.repository;

import io.github.cruisoring.utility.PlainList;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static io.github.cruisoring.Asserts.assertEquals;


public class RepositoryTest {

    List<String> logs = new PlainList();

    @Test
    public void apply() throws Exception {
        Repository<String, Integer> repository = new Repository<>(s -> s.length());
        assertEquals(Integer.valueOf(0), repository.apply(""));
        assertEquals(1, repository.tryApply("X"));
        assertEquals(3, repository.tryApply("abc"));

        //Because Type Erasure, the return type of the Lambda s -> s.length() is deemed as Object, thus null would be returned
        assertEquals(null, repository.tryApply(null));
    }

    @Test
    public void withPredefinedValues() throws Exception {
        Repository<String, Integer> repository = new Repository<>(
                new HashMap<String, Integer>() {{
                    put("Steel", 100);
                    put("Silver", 200);
                    put("Gold", 500);
                }}
                , null, s -> s.length());
        assertEquals(Integer.valueOf(4), repository.apply("Iron"));
        assertEquals(Integer.valueOf(500), repository.get("Gold", 40));
        assertEquals(Integer.valueOf(200), repository.apply("Silver"));
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
    public void repeativeGet() {
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

    @Test
    public void testUpdate() throws Exception {
        Repository<String, Integer> repository = new Repository<>(s -> s.length());
        assertEquals(Integer.valueOf(0), repository.apply(""));
        assertEquals(Integer.valueOf(1), repository.apply("a"));
        assertEquals(Integer.valueOf(3), repository.apply("abc"));

        assertEquals(Integer.valueOf(33), repository.update("abc", Integer.valueOf(3), Integer.valueOf(33)));
        assertEquals(Integer.valueOf(33), repository.apply("abc"));

        //Insert new value with oldValue as null
        assertEquals(Integer.valueOf(-1), repository.update("xyz", null, -1));
        assertEquals(Integer.valueOf(-1), repository.apply("xyz"));
    }

    @Test
    public void testClear() throws Exception {
        Repository<String, Integer> repository = new Repository<>(s -> s.length());
        assertEquals(Integer.valueOf(0), repository.apply(""));
        assertEquals(Integer.valueOf(1), repository.apply("a"));
        assertEquals(Integer.valueOf(3), repository.apply("abc"));
        assertEquals(Integer.valueOf(3), repository.apply("efg"));
        assertEquals(Integer.valueOf(4), repository.apply("1234"));

        //Predicate with no matched entries
        assertEquals(0, repository.clear((s, l) -> l > 10));

        //Predicate matched by key
        assertEquals(2, repository.clear((s, l) -> s.startsWith("a")));
        assertEquals(3, repository.getSize());

        //Predicate matched all entries
        assertEquals(3, repository.clear((s, l) -> true));
        assertEquals(0, repository.getSize());


    }

    class Key implements AutoCloseable {
        private String id;

        public Key(String s) {
            id = s;
        }

        @Override
        public void close() throws Exception {
            logs.add(String.format("Key '%s' closed", id));
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof Key && ((Key) other).id.equals(this.id);
        }
    }

    class Value implements AutoCloseable {
        private Integer value;

        public Value(Integer n) {
            value = n;
        }

        @Override
        public void close() throws Exception {
            logs.add(String.format("Value %s closed", value));
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof Value && ((Value) other).value.equals(this.value);
        }
    }
}