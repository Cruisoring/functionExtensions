package io.github.cruisoring.table;

import io.github.cruisoring.logger.Logger;
import org.junit.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.*;

public class TableColumnsTest {
    Map<String, Integer> normalMap = new LinkedHashMap<String, Integer>(){{
        put("ID", 0);
        put("Birthday", 2);
        put("First Name", 1);
        put("Given Name", 1);
        put("Mobile", 3);
        put("Email", 4);
        put("Phone", 3);
        put("Address", 5);
    }};
    TableColumns shared = new TableColumns(normalMap);

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor_WithNullStrings(){
        TableColumns col = new TableColumns("ID", null, "a");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor_WithDuplicatedNames(){
        TableColumns col = new TableColumns("ID", "Age", "ID");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor_WithMissingIndex(){
        Map<String, Integer> incomplete= new HashMap<String, Integer>(){{
            put("ID", 1);
        }};
        TableColumns col = new TableColumns(incomplete);
    }

    @Test
    public void getColumnIndexes() {
        TableColumns columns1 = new TableColumns("id", "name", "BOD");
        Map<String, Integer> indexes = columns1.getColumnIndexes();
        assertEquals(3, indexes.size());
        assertEquals(Integer.valueOf(2), indexes.get("BOD"));

        assertEquals(8, shared.getColumnIndexes().size());
    }

    @Test
    public void width() {
        assertEquals(0, new TableColumns(new HashMap<>()).width());
        assertEquals(3, new TableColumns("id", "name", "BOD").width());
        assertEquals(6, shared.width());
    }

    @Test
    public void size() {
        assertEquals(0, new TableColumns(new HashMap<>()).size());
        assertEquals(3, new TableColumns("id", "name", "BOD").size());
        assertEquals(8, shared.size());
    }

    @Test
    public void containsKey() {
        assertTrue(shared.containsKey("ID"));
        assertTrue(shared.containsKey("Given Name"));
        assertTrue(shared.containsKey("Address"));

        assertFalse(shared.containsKey(0));
        assertFalse(shared.containsKey("Id"));
        assertFalse(shared.containsKey(null));
    }

    @Test
    public void containsValue() {
        assertTrue(shared.containsValue(0));
        assertTrue(shared.containsValue(1));
        assertTrue(shared.containsValue(2));
        assertTrue(shared.containsValue(3));
        assertTrue(shared.containsValue(4));
        assertTrue(shared.containsValue(5));
    }

    @Test
    public void get() {
        assertEquals(Integer.valueOf(0), shared.get("ID"));
        assertEquals(Integer.valueOf(2), shared.get("Birthday"));
        assertEquals(Integer.valueOf(1), shared.get("First Name"));
        assertEquals(Integer.valueOf(1), shared.get("Given Name"));
        assertEquals(Integer.valueOf(5), shared.get("Address"));
        assertEquals(Integer.valueOf(-1), shared.get("Id"));
    }

    @Test
    public void put() {
        assertNull(shared.put("New", 6));
    }

    @Test
    public void remove() {
        assertNull(shared.remove("ID"));
    }

    @Test
    public void getColumnNames() {
        Logger.V(String.join(", ", shared.getColumnNames()));
        assertTrue(Objects.deepEquals(new String[]{"ID", "First Name", "Birthday", "Mobile", "Email", "Address"}, shared.getColumnNames().toArray()));
    }

    @Test
    public void values() {
        assertEquals(8, shared.values().size());

        Set<Integer> valueSet = new HashSet<>(shared.values());
        assertEquals(6, valueSet.size());
    }

    @Test
    public void rowOf() {
        TupleRow row0 = shared.rowOf();
        Logger.D(row0.toString());
        TupleRow row1 = shared.rowOf(123);
        Logger.D(row1.toString());

        TupleRow row6 = shared.rowOf(123, "Tom", LocalDate.of(2000, 1, 1), "0400111222", null, "somewhere");
        Logger.I(row6.toString());
        assertEquals("Tom", row6.getValue("Given Name"));
        assertEquals("0400111222", row6.getValue("Phone"));
        assertNull(row6.getValue("id"));

        TupleRow8<Integer, String, LocalDate, String, String, String, Character, Boolean> row8 = shared.createRow(123, "Tom", LocalDate.of(2000, 1, 1),
                "0400111222", null, "somewhere", 'M', true);
        Logger.I(row8.toString());
        assertEquals(Character.valueOf('M'), row8.getValue(6));
        assertEquals(true, row8.getValue(7));
    }

    @Test
    public void createTable() {
        TupleTable8<Integer, String, LocalDate, String, String, String, Character, Boolean> table8 = shared.createTable8();
        table8.addValues(22, "Alice", LocalDate.of(2001, 12, 1), "0400333222", "alice@d.com", "somewhere", 'F', false);

        TupleRow8<Integer, String, LocalDate, String, String, String, Character, Boolean> row8 = shared.createRow(123, "Tom", LocalDate.of(2000, 1, 1),
                "0400111222", null, "somewhere", 'M', true);
        table8.add(row8);

        TupleRow9<Integer, String, LocalDate, String, String, String, Character, Boolean, String> row9 = shared.createRow(123, "Tom", LocalDate.of(2000, 1, 1),
                "0400111222", null, "somewhere", 'M', true, "Note");
        table8.add(row9);

        Logger.D("There are %d rows now", table8.size());
    }
}