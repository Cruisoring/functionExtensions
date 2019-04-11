package io.github.cruisoring.table;

import io.github.cruisoring.TypeHelper;
import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple2;
import io.github.cruisoring.tuple.WithValues;
import io.github.cruisoring.tuple.WithValues5;
import org.junit.Test;

import java.time.LocalDate;
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

        WithValuesByName r2 = table2.getRow(2);
        assertEquals("Test", r2.getValueByName("Id"));
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

        Iterator<WithValuesByName> iterator = table2.iterator();
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
        TableColumns columns = new TableColumns("ID", "First Name", "Last Name", "Gender", "IsActive", "Favorite", "Other");
        TupleTable5<Integer, String, String, Character, Boolean> table5 = columns.createTable5();
        table5.addValues(Tuple.create(0, "Alice", "Wilson", 'F', true));
        table5.addValues(1, "Bob", "Nilson", 'M', false, 99);
        table5.addValues(2, "Clare", "Neons", 'F', true, "Movie", 25);
        table5.addValues(3, "David", "Wilson", 'M', null, "", 20);
        table5.addValues(Tuple.create(4, "Eddy", "Claks", 'M', true, null, "Unknown", LocalDate.of(2019, 4, 11)));
        assertTrue(table5.add(columns.createRow(5, "Fred", "Nil", 'M', false)));    //Would add success with TupleRow of right signature
        assertFalse(table5.add(columns.createRow(5, "Grey", "Thompson", 6.33, false)));   //Would fail since it calls the add(WithValuesByName) when the TupleRow is of wrong signature
//        table5.addValues(5, "Elisa", "Carter", 'F');           //Not allowed without filling the first 5 arguments
//        table5.addValues(5, "Elissa", "Carter", "Female", true)  //Not allow any argument with wrong type

        table5.forEach(row -> Logger.D(row.toString()));

        assertEquals(6, table5.size());
        assertEquals(7, table5.width());
        assertEquals(Integer.valueOf(1), table5.getCellValue(1, 0));        //Access cell value by rowIndex and colIndex
        assertEquals(true, table5.getCellValue(4, "IsActive"));    //Access cell value by rowIndex and columnName
        assertEquals(null, table5.getCellValue(0, "Favorite"));    //return null if value is missing
        assertEquals(Integer.valueOf(99), table5.getCellValue(1, "Favorite"));  //Favorite cell can contain Integer
        assertEquals("Movie", table5.getCellValue(2, "Favorite"));  //Or String since type of column6 is not specified by table5
        assertEquals(LocalDate.of(2019, 4, 11), table5.getCellValue(4, 7)); //The cell can still be accessible with colIndex

        WithValuesByName row1 = table5.getRow(1);
        assertTrue(table5.add(row1));

        table5.forEach(row -> Logger.D(row.toString()));
    }

    @Test
    public void remove() {
        TableColumns columns = new TableColumns("ID", "First Name", "Last Name", "Gender", "IsActive", "Favorite", "Other");
        TupleTable5<Integer, String, String, Character, Boolean> table5 = columns.createTable5();
        table5.addValues(Tuple.create(0, "Alice", "Wilson", 'F', true));
        table5.addValues(1, "Bob", "Nilson", 'M', false, 99);
        table5.addValues(2, "Clare", "Neons", 'F', true, "Movie", 25);
        table5.addValues(3, "David", "Wilson", 'M', null, "", 20);
        table5.addValues(Tuple.create(4, "Eddy", "Claks", 'M', true, null, "Unknown", LocalDate.of(2019, 4, 11)));
        assertTrue(table5.add(columns.createRow(5, "Fred", "Nil", 'M', false)));    //Would add success with TupleRow of right signature
        assertFalse(table5.add(columns.createRow(5, "Grey", "Thompson", 6.33, false)));   //Would fail since it calls the add(WithValuesByName) when the TupleRow is of wrong signature

        assertEquals(6, table5.size());
        //Test of Collection.remove(Object o):
        assertFalse(table5.remove(Tuple.of(0, "Alice", "Wilson", 'F', false, 1)));      //Not matched with an extra value
        assertFalse(table5.remove(Tuple.of(0, "Alice", "Wilson", 'F', false)));         //Not matched with the "IsActive" value
        assertFalse(table5.remove(Tuple.of(0, "Alice", "Wilson", 'F')));                //Not matched by shorting last value
        assertTrue(table5.remove(Tuple.of(0, "Alice", "Wilson", 'F', true)));           //The tuple created is held by the table, thus can be removed

        //Test of ITable.remove(Object... values)
        assertFalse(table5.remove(1, "Bob", "Nilson", null, false, 22));        //Not matched with the "Gender" value
        assertFalse(table5.remove(1, "Bob", "Nilson", 'M', false, 22, new int[0]));        //Not matched with two extra values
        assertFalse(table5.remove(1, "Bob", "Nilson", 'M'));                    //Not matched by shorting last 2 values
        assertTrue(table5.remove(1, "Bob", "Nilson", 'M', false, 99));          //shall be removed

        //Remove second to last row by its values
        assertTrue(table5.remove(4, "Eddy", "Claks", 'M', true, null, "Unknown", LocalDate.of(2019, 4, 11)));

        //Remove last row
        assertTrue(table5.remove(5, "Fred", "Nil", 'M', false));

        //Test of removing TupleRows
        WithValuesByName row0 = table5.getRow(0);
        assertEquals(Integer.valueOf(2), row0.getValueByName("ID"));
        assertTrue(table5.remove(row0));

        table5.forEach(row -> Logger.D(row.toString()));
        assertEquals(1, table5.size());
        assertEquals(Integer.valueOf(20), table5.getCellValue(0, "Other"));
    }

    @Test
    public void testContains() {
        TableColumns columns = new TableColumns("ID", "First Name", "Last Name", "Gender", "IsActive", "Favorite", "Other");
        TupleTable5<Integer, String, String, Character, Boolean> table5 = columns.createTable5();
        table5.addValues(Tuple.create(0, "Alice", "Wilson", 'F', true));
        table5.addValues(1, "Bob", "Nilson", 'M', false, 99);
        table5.addValues(2, "Clare", "Neons", 'F', true, "Movie", 25);
        table5.addValues(3, "David", "Wilson", 'M', null, "", 20);
        table5.addValues(Tuple.create(4, "Eddy", "Claks", 'M', true, null, "Unknown", LocalDate.of(2019, 4, 11)));
        assertTrue(table5.add(columns.createRow(5, "Fred", "Nil", 'M', false)));    //Would add success with TupleRow of right signature

        //Test of Collection.contains(Object o):
        assertFalse(table5.contains(Tuple.of(0, "Alice", "Wilson", 'F', false, 1)));      //Not matched with an extra value
        assertFalse(table5.contains(Tuple.of(0, "Alice", "Wilson", 'F', false)));         //Not matched with the "IsActive" value
        assertFalse(table5.contains(Tuple.of(0, "Alice", "Wilson", 'F')));                //Not matched by shorting last value
        assertTrue(table5.contains(Tuple.of(0, "Alice", "Wilson", 'F', true)));           //The tuple created is held by the table, thus can be removed

        //Test of ITable.contains(Object... values)
        assertFalse(table5.contains(1, "Bob", "Nilson", null, false, 22));        //Not matched with the "Gender" value
        assertFalse(table5.contains(1, "Bob", "Nilson", 'M', false, 22, new int[0]));        //Not matched with two extra values
        assertFalse(table5.contains(1, "Bob", "Nilson", 'M'));                    //Not matched by shorting last 2 values
        assertTrue(table5.contains(1, "Bob", "Nilson", 'M', false, 99));          //shall be removed

        //Contains second to last row by its values
        assertTrue(table5.contains(4, "Eddy", "Claks", 'M', true, null, "Unknown", LocalDate.of(2019, 4, 11)));

        //Contains last row
        assertTrue(table5.contains(5, "Fred", "Nil", 'M', false));

        //Test of removing TupleRows
        WithValuesByName row0 = table5.getRow(0);
        assertTrue(table5.contains(row0));

        table5.forEach(row -> Logger.D(row.toString()));
    }

    @Test
    public void addAll() {
        TableColumns columns = new TableColumns("ID", "First Name", "Last Name", "Gender", "IsActive", "Favorite", "Other");
        TupleTable5<Integer, String, String, Character, Boolean> table5 = columns.createTable5();
        table5.addValues(Tuple.create(0, "Alice", "Wilson", 'F', true));
        table5.addValues(1, "Bob", "Nilson", 'M', false, 99);
        table5.addValues(2, "Clare", "Neons", 'F', true, "Movie", 25);

        WithValuesByName row2 = table5.getRow(2);
        table5.remove(row2);
        assertEquals(2, table5.size());

        List<WithValuesByName> collection = Arrays.asList(
                table5.getRow(1),           //Row from this table
                columns.createRow(11, "Grey", "Thompson", 6.33, false),  //Row with not matched value types
                row2,                                //Row deleted
                columns.createRow(5, "Alice", "Wilson", 'F', false, 1)    //with extra column
                , columns.createRow(3, "David", "Wilson", 'M', null, "", 20)
                , columns.createRow(4, "Eddy", "Claks", 'M', true, null, "Unknown", LocalDate.of(2019, 4, 11))
        );
        table5.addAll(collection);
        table5.forEach(row -> Logger.D(row.toString()));
        assertTrue(Objects.deepEquals(new Object[]{0, 1, 1, 2, 5, 3, 4}, table5.getColumnValues("ID")));
    }

    @Test
    public void removeAll() {
        TableColumns columns = new TableColumns("ID", "First Name", "Last Name", "Gender", "IsActive", "Favorite", "Other");
        TupleTable5<Integer, String, String, Character, Boolean> table5 = columns.createTable5();
        table5.addValues(Tuple.create(0, "Alice", "Wilson", 'F', true));
        table5.addValues(1, "Bob", "Nilson", 'M', false, 99);
        table5.addValues(2, "Clare", "Neons", 'F', true, "Movie", 25);
        table5.addValues(3, "David", "Wilson", 'M', null, "", 20);
        table5.addValues(Tuple.create(4, "Eddy", "Claks", 'M', true, null, "Unknown", LocalDate.of(2019, 4, 11)));
        assertTrue(table5.add(columns.createRow(5, "Fred", "Nil", 'M', false)));    //Would add success with TupleRow of right signature

        WithValues row0 = table5.getRow(0);
        WithValues row3 = table5.getRow(3).getValues();
        Tuple tuple = Tuple.create(4, "Eddy", "Claks", 'M', true, null, "Unknown", LocalDate.of(2019, 4, 11));
        assertTrue(table5.removeAll(Arrays.asList(row0, row3, tuple)));

        assertTrue(Objects.deepEquals(new Object[]{1, 2, 5}, table5.getColumnValues("ID")));
    }

    @Test
    public void retainAll() {
        TableColumns columns = new TableColumns("ID", "First Name", "Last Name", "Gender", "IsActive", "Favorite", "Other");
        TupleTable5<Integer, String, String, Character, Boolean> table5 = columns.createTable5();
        table5.addValues(Tuple.create(0, "Alice", "Wilson", 'F', true));
        table5.addValues(1, "Bob", "Nilson", 'M', false, 99);
        table5.addValues(2, "Clare", "Neons", 'F', true, "Movie", 25);
        table5.addValues(3, "David", "Wilson", 'M', null, "", 20);
        table5.addValues(Tuple.create(4, "Eddy", "Claks", 'M', true, null, "Unknown", LocalDate.of(2019, 4, 11)));
        assertTrue(table5.add(columns.createRow(5, "Fred", "Nil", 'M', false)));    //Would add success with TupleRow of right signature

        WithValues row0 = table5.getRow(0);
        WithValues row3 = table5.getRow(3).getValues();
        Tuple tuple = Tuple.create(4, "Eddy", "Claks", 'M', true, null, "Unknown", LocalDate.of(2019, 4, 11));
        assertTrue(table5.retainAll(Arrays.asList(row0, row3, tuple)));

        table5.forEach(r -> Logger.D(r.toString()));
        assertTrue(Objects.deepEquals(new Object[]{0, 3, 4}, table5.getColumnValues("ID")));
    }

    @Test
    public void clear() {
    }
}