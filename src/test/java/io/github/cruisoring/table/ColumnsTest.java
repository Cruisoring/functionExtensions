package io.github.cruisoring.table;

import io.github.cruisoring.TypeHelper;
import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static io.github.cruisoring.Asserts.*;

public class ColumnsTest {
    static final String[][] sharedColumnDefitions = new String[][]{
            new String[]{"ID", "UID", "Unique Id"},
            new String[]{"First Name", "Given Name", "User Name"},
            new String[]{"Birthday"},
            new String[]{"Mobile", "Phone"},
            new String[]{"Email"},
            new String[]{"Address"}
    };

    static final IColumns shared = new Columns(sharedColumnDefitions);

    @Test
    public void testConstructor_WithNullStrings(){
        Columns col = assertException(() -> new Columns("ID", null, "a"), NullPointerException.class);
    }

    @Test
    public void testConstructor_WithDuplicatedNames(){
        Columns col = assertException(() -> new Columns("ID", "Age", "ID"), UnsupportedOperationException.class);
    }

    @Test
    public void constructWithEmpty(){
        assertException(() -> new Columns(new String[0][]), UnsupportedOperationException.class);
    }

    @Test
    public void constructWithIndexMissing(){
        assertException(() -> new Columns(new String[][]{
                new String[]{"ID"},
                new String[0],
                new String[]{"Mobile", "Phone"}
        }), UnsupportedOperationException.class);
    }

    @Test
    public void testConstructor_WithMissingIndex(){

        Columns col = assertException(() -> new Columns(new String[][]{
                new String[0],
                new String[]{"Mobile", "Phone"}
        }), UnsupportedOperationException.class);
    }

    @Test
    public void getColumnIndexes() {
        Columns columns1 = new Columns("id", "name", "BOD");
        Map<String, Integer> indexes = columns1.getColumnIndexes();
        assertEquals(3, indexes.size());
        assertEquals(Integer.valueOf(2), indexes.get("BOD"));

        assertEquals(11, shared.getColumnIndexes().size());
    }

    @Test
    public void width() {
        assertEquals(3, new Columns("id", "name", "BOD").width());
        assertEquals(6, shared.width());
    }

    @Test
    public void size() {
        assertEquals(3, new Columns("id", "name", "BOD").size());
        assertEquals(11, shared.size());
    }

    @Test
    public void containsKey() {
        assertAllTrue(shared.containsKey("ID"),
                shared.containsKey("Given Name"),
                shared.containsKey("Address"));

        assertAllFalse(shared.containsKey(0),
                shared.containsKey("Id"),
                shared.containsKey(null));
    }

    @Test
    public void containsValue() {
        assertAllTrue(shared.containsValue(0),
                shared.containsValue(1),
                shared.containsValue(2),
                shared.containsValue(3),
                shared.containsValue(4),
                shared.containsValue(5));
    }

    @Test
    public void get() {
        assertEquals(Integer.valueOf(0), shared.get("ID"));
        assertEquals(Integer.valueOf(2), shared.get("Birthday"));
        assertEquals(Integer.valueOf(1), shared.get("First Name"));
        assertEquals(Integer.valueOf(1), shared.get("Given Name"));
        assertEquals(Integer.valueOf(5), shared.get("Address"));
        assertEquals(Integer.valueOf(0), shared.get("id"));    //By default, the Columns created with Map use String.CASE_INSENSITIVE_ORDER to compare names
        assertEquals(Integer.valueOf(-1), shared.get("Id "));    //By default, the Column name can contain space that make it different
    }

    @Test
    public void mapIndexes_withColumnName(){
        IColumns other = new Columns(new String[][]{
                new String[]{"identity", "id"},
                new String[]{"userName", "name", "GivenName"},
                new String[]{"contact", "Phone"},
                new String[]{"address"},
                new String[]{"DOB", "Birthday"},
                new String[]{"email"}
        }, Columns.ESCAPED_CASE_INSENSITIVE);

        assertEquals(Tuple.of(0, 0), shared.mapIndexes("ID", other));
        assertEquals(Tuple.of(1, 1), shared.mapIndexes("User Name", other));
        assertEquals(Tuple.of(3, 2), shared.mapIndexes("Phone", other));
        assertEquals(Tuple.of(5, 3), shared.mapIndexes("Address", other));
        assertEquals(Tuple.of(2, 4), shared.mapIndexes("Birthday", other));
        assertEquals(Tuple.of(4, 5), shared.mapIndexes("Email", other));

        assertAllNull(shared.mapIndexes("identity", other)); //"identity" is not defined in shared
        assertAllNull(shared.mapIndexes("first name", other)); //"first name" is unknown for other
        assertAllNull(shared.mapIndexes("title", other)); //"title" is unknown for both
        assertAllNull(shared.mapIndexes("userName", other)); //"userName" is not regarded as equal to "user name" by shared
    }

    @Test
    public void mapIndexes_ESCAPED(){
        IColumns other = new Columns(new String[][]{
                new String[]{"identity", "id"},
                new String[]{"userName", "name", "GivenName"},
                new String[]{"contact", "Phone"},
                new String[]{"address"},
                new String[]{"DOB", "Birthday"},
                new String[]{"email"}
        }, Columns.ESCAPED);

        WithValues<Integer> mappedIndexes = shared.mapIndexes(other);
        assertEquals(Tuple.create(null, 1, 4, 2, null, null), mappedIndexes);
        mappedIndexes = other.mapIndexes(shared);
        assertEquals(Tuple.create(0, null, 3, 5, 2, 4), mappedIndexes);
    }

    @Test
    public void mapIndexes_ESCAPED_CASE_INSENSITIVE(){
        IColumns other = new Columns(new String[][]{
                new String[]{"identity", "id"},
                new String[]{"userName", "name", "GivenName"},
                new String[]{"contact", "Phone"},
                new String[]{"address"},
                new String[]{"DOB", "Birthday"},
                new String[]{"email"}
        }, Columns.ESCAPED_CASE_INSENSITIVE);

        WithValues<Integer> mappedIndexes = shared.mapIndexes(other);
        assertEquals(Tuple.create(0, 1, 4, 2, 5, 3), mappedIndexes);
    }

    @Test
    public void put() {
        assertAllNull(shared.put("New", 6));
    }

    @Test
    public void remove() {
        assertAllNull(shared.remove("ID"));
    }

    @Test
    public void getColumnNames() {
        Logger.D(String.join(", ", shared.getColumnNames()));
        assertAllTrue(TypeHelper.valueEquals(
            new String[]{"ID", "First Name", "Birthday", "Mobile", "Email", "Address"},
            shared.getColumnNames().toArray()));
    }

    @Test
    public void keySet(){
        assertEquals(11, shared.keySet().size());
        Logger.D(String.join(", ", shared.keySet()));
        assertEquals("ID, UID, Unique Id, First Name, Given Name, User Name, Birthday, Mobile, Phone, Email, Address",
                String.join(", ", shared.keySet()));
    }

    @Test
    public void values() {
        assertEquals(11, shared.values().size());

        Set<Integer> valueSet = new HashSet<>(shared.values());
        assertEquals(6, valueSet.size());
    }

    @Test
    public void rowOf() {
        TupleRow row0 = shared.createRow();
        Logger.D(row0.toString());
        TupleRow row1 = shared.createRow(123);
        Logger.D(row1.toString());

        TupleRow row6 = shared.createRow(123, "Tom", LocalDate.of(2000, 1, 1), "0400111222", null, "somewhere");
        Logger.I(row6.toString());
        assertEquals("Tom", row6.getValueByName("Given Name"));
        assertEquals("0400111222", row6.getValueByName("Phone"));
        assertEquals(123, row6.getValueByName("id"));

        TupleRow8<Integer, String, LocalDate, String, String, String, Character, Boolean> row8 = (TupleRow8<Integer, String, LocalDate, String, String, String, Character, Boolean>) shared.createRow(123, "Tom", LocalDate.of(2000, 1, 1),
                "0400111222", null, "somewhere", 'M', true);
        Logger.I(row8.toString());
        assertEquals(Character.valueOf('M'), row8.getValue(6));
        assertEquals(true, row8.getValue(7));
    }

    @Test
    public void createTable() {
        TupleTable6<Integer, String, LocalDate, String, String, String> table6 = shared.createTable6(
            Integer.class, String.class, LocalDate.class, String.class, String.class, String.class);
        table6.addValues(22, "Alice", LocalDate.of(2001, 12, 1), "0400333222", "alice@d.com", "home", 'F', false);

        TupleRow row8 = shared.createRow(456, "Tom", LocalDate.of(2000, 1, 1),
                "0400111222", null, "library", 'M', true);
        table6.add(row8);

        TupleRow row9 = shared.createRow(789, "Tom", LocalDate.of(2000, 1, 1),
                "0400111222", null, "classroom", 'M', true, "Note");
        table6.add(row9);

        table6.rows.forEach(v -> Logger.D(v.toString()));
        table6.forEach(v -> Logger.I(v.toString()));
    }
}