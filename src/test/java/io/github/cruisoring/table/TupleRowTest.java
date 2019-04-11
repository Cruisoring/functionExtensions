package io.github.cruisoring.table;

import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple1;
import io.github.cruisoring.tuple.WithValues;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TupleRowTest {
    TableColumns columns = new TableColumns("ID", "First Name", "Last Name", "Gender", "IsActive", "Favorite");

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

    @Test(expected = IndexOutOfBoundsException.class)
    public void getValueWithInvalidIndex() {
        TupleRow row = columns.createRow(1, "Alice", "Wilson", 'F', true, null, 77, "abc", null);
        assertEquals(null, row.getValue(-1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getValueWithInvalidIndex2() {
        TupleRow row = columns.createRow(1, "Alice", "Wilson", 'F', true, null, 77, "abc", null);
        assertEquals(null, row.getValue(10));
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
        assertTrue(columns==row.getColumnIndexes());
    }

    @Test
    public void asNameValuePairs() {
        TupleRow row = columns.createRow(1, "Alice", "Wilson", 'F', true, null, 77, "abc", null);
        NameValuePair[] pairs = row.asNameValuePairs();
        assertEquals(6, pairs.length);
        Logger.D(Arrays.stream(pairs).map(p -> p.toString()).reduce("", (s1, s2) -> s1 + ", " + s2));
    }

    @Test
    public void testToString() {
        TupleRow row = columns.createRow(1, "Alice", "Wilson", 'F', true, null, 77, "abc", null, "More to see");
//        Logger.D(row.toString());
        Logger.I(row.getValues().toString());
        assertEquals("[\"ID\"=1, \"First Name\"=Alice, \"Last Name\"=Wilson, \"Gender\"=F, \"IsActive\"=true, \"Favorite\"=null]",
                row.toString());

        TupleRow row2 = columns.createRow(1, "Alice", "Wilson", 'F');
        assertEquals("[\"ID\"=1, \"First Name\"=Alice, \"Last Name\"=Wilson, \"Gender\"=F]",
                row2.toString());
    }

    @Test
    public void testCompatibility(){
        WithValuesByName1<Integer> row1 = columns.createRow(1);
        WithValuesByName2<Integer, String> row2 = columns.createRow(2, "Bob");
        row1 = row2;
        Logger.D(row1.toString());
    }
}