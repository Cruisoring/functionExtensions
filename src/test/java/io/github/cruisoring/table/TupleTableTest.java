package io.github.cruisoring.table;

import io.github.cruisoring.TypeHelper;
import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple2;
import io.github.cruisoring.tuple.Tuple5;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class TupleTableTest {

    @Test
    public void getColumnIndex() {
        TupleTable2<String, Integer> table2 = new TupleTable2<>("Id", "age");
        assertEquals(0, table2.getColumnIndex("Id"));
        assertEquals(1, table2.getColumnIndex("age"));
        assertEquals(-1, table2.getColumnIndex("agE"));
        assertEquals(-1, table2.getColumnIndex("ID"));
        assertEquals(-1, table2.getColumnIndex(""));
        assertEquals(-1, table2.getColumnIndex(null));
    }

    @Test
    public void getColumns() {
        TupleTable2<String, Integer> table2 = new TupleTable2<>("Id", "age");
        Collection<String> names = table2.getColumns();
        assertTrue(names.containsAll(Arrays.asList("Id", "age")));
    }

    @Test
    public void getRowCount() {
        TupleTable2<String, Integer> table2 = new TupleTable2<>("Id", "age");
        assertEquals(0, table2.size());
        table2.addValues(Tuple.create("Test", 123));
        assertEquals(1, table2.size());
    }

    @Test
    public void getRow() {
        TupleTable2<String, Integer> table2 = new TupleTable2<>("Id", "age");
        assertNull(table2.getRow(0));
        table2.addValues(Tuple.create("Test", 123));
        table2.addValues(Tuple.create(null, null));
        table2.addValues(Tuple.create("Test", 123));
        table2.addValues(Tuple.create("", null));
        assertEquals(4, table2.size());

        TableColumns indexes = new TableColumns(new HashMap<String, Integer>() {{
            put("Id", 0);
            put("age", 1);
        }});
        assertEquals(new TupleRow(indexes, Tuple.create("Test", 123)), table2.getRow(0));
        assertEquals(new TupleRow(indexes, Tuple.create(null, null)), table2.getRow(1));
        assertEquals(new TupleRow(indexes, Tuple.create("Test", 123)), table2.getRow(2));
        assertEquals(new TupleRow(indexes, Tuple.create("", null)), table2.getRow(3));

        TupleRow r2 = table2.getRow(2);
        assertEquals("Test", r2.getValue("Id"));
        assertEquals(123, r2.getValue(1));

        assertNull(table2.getRow(-1));
        assertNull(table2.getRow(8));
    }

    @Test
    public void getColumnIndexes() {
        TupleTable2<String, Integer> table2 = new TupleTable2<>("Id", "age");
        Map<String, Integer> indexes = table2.getColumnIndexes();
        indexes.put("a", 3);
    }

    @Test
    public void contains() {
        TupleTable2<String, Integer> table2 = new TupleTable2<>("Id", "age");
        table2.addValues(Tuple.create("Test", 123));
        table2.addValues(Tuple.create(null, null));
        table2.addValues(Tuple.create("Test", 123));
        table2.addValues(Tuple.create("", null));

        assertTrue(table2.contains(Tuple.create("Test", 123)));
        assertTrue(table2.contains(Tuple.create(null, null)));
        assertTrue(table2.contains(Tuple.create("", null)));
    }

    @Test
    public void iterator() {
        TupleTable2<String, Integer> table2 = new TupleTable2<>("Id", "age");
        table2.addValues(Tuple.create("Test", 123));
        table2.addValues(Tuple.create(null, null));
        table2.addValues(Tuple.create("Test", 123));
        table2.addValues(Tuple.create("", null));

        Iterator<TupleRow<Tuple2<String, Integer>>> iterator = table2.iterator();
        while (iterator.hasNext()) {
            Logger.D(iterator.next().toString());
        }
    }

    @Test
    public void toArray() {
        TupleTable2<String, Integer> table2 = new TupleTable2<>("Id", "age");
        table2.addValues(Tuple.create("Test", 123));
        table2.addValues(Tuple.create(null, null));
        table2.addValues(Tuple.create("Test", 123));
        table2.addValues(Tuple.create("", null));

        Object[] array = table2.toArray();
        Logger.I(TypeHelper.deepToString(array));
        assertTrue(array.length == 4
                && array[0].equals(new TupleRow(table2.columns, Tuple.create("Test", 123)))
                && array[3].equals(new TupleRow(table2.columns, Tuple.create("", null))));
    }

    @Test
    public void toTupleArray() {
        TupleTable2<String, Integer> table2 = new TupleTable2<>("Id", "age");
        table2.addValues(Tuple.create("Test", 123));
        table2.addValues(Tuple.create(null, null));
        table2.addValues(Tuple.create("Test", 123));
        table2.addValues(Tuple.create("", null));

        Tuple2<String, Integer>[] tuple2s = table2.toArray(new Tuple2[0]);
        Logger.I(TypeHelper.deepToString(tuple2s));

        Tuple[] tuples = table2.toArray(new Tuple[0]);
        Logger.I(TypeHelper.deepToString(tuples));
    }

    @Test
    public void add() {
        TupleTable5<Integer, String, String, Character, Boolean> table5 = new TupleTable5<>("ID", "First Name", "Last Name", "Gender", "IsActive");
        Tuple5<Integer, String, String, Character, Boolean> row = Tuple.create(1, "Alice", "Wilson", 'F', true);
        table5.addValues(row);
    }

    @Test
    public void remove() {
    }

    @Test
    public void containsAll() {
    }

    @Test
    public void addAll() {
    }

    @Test
    public void removeAll() {
    }

    @Test
    public void retainAll() {
    }

    @Test
    public void clear() {
    }
}