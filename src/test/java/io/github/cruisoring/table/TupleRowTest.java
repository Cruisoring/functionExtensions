package io.github.cruisoring.table;

import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static io.github.cruisoring.Asserts.*;
public class TupleRowTest {
    Columns columns = new Columns("ID", "First Name", "Last Name", "Gender", "IsActive", "Favorite");

    @Test
    public void getValues() {
        TupleRow row = columns.createRow(1, "Alice", "Wilson", 'F', true, null, 77, "abc", null);
        WithValues values = row.getValues();
        Logger.D(values.toString());
        assertEquals(Tuple.create(1, "Alice", "Wilson", 'F', true, null, 77, "abc", null), values);
    }

    @Test
    public void getValue() {
        TupleRow row = columns.createRow(1, "Alice", "Wilson", 'F', true, null, 77, "abc", null);
        assertEquals(Integer.valueOf(1), row.getValue(0));
        assertEquals("Alice", row.getValue(1));
        assertEquals(null, row.getValue(5));
        assertEquals(null, row.getValue(8));
    }

    @Test
    public void getValueWithInvalidIndex() {
        TupleRow row = columns.createRow(1, "Alice", "Wilson", 'F', true, null, 77, "abc", null);
        assertException(() -> row.getValue(-1), IndexOutOfBoundsException.class);
    }

    @Test
    public void getValueWithInvalidIndex2() {
        TupleRow row = columns.createRow(1, "Alice", "Wilson", 'F', true, null, 77, "abc", null);
        assertException(() -> row.getValue(10), IndexOutOfBoundsException.class);
    }

    @Test
    public void getValueByName() {
        TupleRow row = columns.createRow(1, "Alice", "Wilson", 'F', true, null, 77, "abc", null);
        assertEquals(Integer.valueOf(1), row.getValueByName("ID"));
        assertEquals(Character.valueOf('F'), row.getValueByName("Gender"));
        assertEquals(true, row.getValueByName("IsActive"));
        assertEquals(null, row.getValueByName("Favorite"));

        assertEquals(null, row.getValueByName("NOooo"));
        assertEquals(null, row.getValueByName("Id"));
    }

    @Test
    public void getLength() {
        TupleRow row = columns.createRow(1, "Alice", "Wilson", 'F', true, null, 77, "abc", null);
        assertEquals(9, row.getLength());
    }

    @Test
    public void getColumnIndexes() {
        TupleRow row = columns.createRow(1, "Alice", "Wilson", 'F', true, null, 77, "abc", null);
        assertAllTrue(columns==row.getColumnIndexes());
    }

    @Test
    public void asEntrySet() {
        TupleRow row = columns.createRow(1, "Alice", "Wilson", 'F', true, null, 77, "abc", null);
        Set<Map.Entry<String, Object>> pairs = row.asMap().entrySet();
        assertEquals(6, pairs.size());
        pairs.forEach(entry -> Logger.D("%s:%s", entry.getKey(), entry.getValue()));
    }

    @Test
    public void testToString() {
        TupleRow row = columns.createRow(1, "Alice", "Wilson", 'F', true, null, 77, "abc", null, "More to see");
//        Logger.D(row.toString());
        Logger.I(row.getValues().toString());
        assertEquals("{\"ID\"=1, \"First Name\"=Alice, \"Last Name\"=Wilson, \"Gender\"=F, \"IsActive\"=true, \"Favorite\"=null}",
                row.toString());

        TupleRow row2 = columns.createRow(1, "Alice", "Wilson", 'F');
        assertEquals("{\"ID\"=1, \"First Name\"=Alice, \"Last Name\"=Wilson, \"Gender\"=F}",
                row2.toString());
    }

    @Test
    public void testCompatibility(){
        TupleRow1<Integer> row1 = (TupleRow1<Integer>) columns.createRow(1);
        TupleRow2<Integer, String> row2 = (TupleRow2<Integer, String>) columns.createRow(2, "Bob");
        TupleRow3<Integer, String, Boolean> row3 = (TupleRow3<Integer, String, Boolean>) columns.createRow(2, "Bob", true);
        row1 = row3;
        row1 = row2;
        row2 = row3;
        Logger.D(row1.toString());
        Logger.D(row2.toString());
        //row3 = row2; //Need casting
    }
}