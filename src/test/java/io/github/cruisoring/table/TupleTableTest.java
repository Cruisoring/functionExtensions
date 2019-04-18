package io.github.cruisoring.table;

import io.github.cruisoring.TypeHelper;
import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple2;
import io.github.cruisoring.tuple.WithValues;
import io.github.cruisoring.tuple.WithValues1;
import io.github.cruisoring.utility.ArrayHelper;
import org.junit.Test;

import java.time.LocalDate;
import java.util.*;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TupleTableTest {

    @Test
    public void getColumnIndex() {
        TupleTable2<String, Integer> table2 = new TupleTable2<String, Integer>(
            new Columns("Id", "age"), String.class, Integer.class);
        assertEquals(0, table2.getColumnIndex("Id"));
        assertEquals(1, table2.getColumnIndex("age"));
        assertEquals(-1, table2.getColumnIndex("agE"));
        assertEquals(-1, table2.getColumnIndex("ID"));
        assertEquals(-1, table2.getColumnIndex(""));
        assertEquals(-1, table2.getColumnIndex(null));
    }

    @Test
    public void getDisplayedNames() {
        TupleTable2<String, Integer> table2 =  new TupleTable2<String, Integer>(
            new Columns("Id", "age"), String.class, Integer.class);
        Collection<String> names = table2.getDisplayedNames();
        assertTrue(names.containsAll(Arrays.asList("Id", "age")));
    }

    @Test
    public void getRowCount() {
        TupleTable2<String, Integer> table2 = new TupleTable2<String, Integer>(
            new Columns("Id", "age"), String.class, Integer.class);
        assertEquals(0, table2.size());
        table2.addValues(Tuple.create("Test", 123));
        assertEquals(1, table2.size());
    }

    @Test
    public void getRow() {
        TupleTable2<String, Integer> table2 =  new TupleTable2<String, Integer>(
            new Columns("Id", "age"), String.class, Integer.class);
        assertNull(table2.getRow(0));
        table2.addValues(Tuple.create("Test", 123));
        table2.addValues(Tuple.create(null, null));
        table2.addValues(Tuple.create("Test", 123));
        table2.addValues(Tuple.create("", null));
        assertEquals(4, table2.size());

        IColumns indexes = (IColumns)table2.getColumns();
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
    public void getRow_withViewColumns(){
        Columns columns = new Columns("ID", "First Name", "Last Name", "Gender", "IsActive", "Favorite", "Other");
        TupleTable5<Integer, String, String, Character, Boolean> table5 = columns.createTable5(Integer.class, String.class, String.class, Character.class, Boolean.class);
        table5.addValues(Tuple.create(0, "Alice", "Wilson", 'F', true));
        table5.addValues(1, "Bob", "Nilson", 'M', false, 99);
        table5.addValues(2, "Clare", "Neons", 'F', true, "Movie", 25);
        table5.addValues(3, "David", "Wilson", 'M', null, "", 20);
        table5.addValues(Tuple.create(4, "Eddy", "Claks", 'M', true, null, "Unknown", LocalDate.of(2019, 4, 11)));
        assertTrue(table5.add(columns.createRow(5, "Fred", "Nil", 'M', false)));    //Would add success with TupleRow of right signature

        Columns viewColumns = new Columns(new HashMap<Integer, WithValues1<String[]>>(){{
            put(0, Tuple.create(new String[]{"id", "Identifier", "ID"}));
            put(1, Tuple.create(new String[]{"name", "First Name"}));
            put(2, Tuple.create(new String[]{"active", "IsActive"}));
        }}, Columns.ESCAPED_CASE_INSENSITIVE);

        WithValuesByName row2 = table5.getRow(2, viewColumns);
        Map<String, Object> map = row2.asMap();
        assertTrue(Arrays.deepEquals(new Object[]{2, "Clare", true}, map.values().toArray()));
    }

    @Test
    public void getAllRow() {
        TupleTable2<String, Integer> table2 =  new TupleTable2<String, Integer>(
            new Columns("Id", "age"), String.class, Integer.class);
        assertNull(table2.getRow(0));
        table2.addValues(Tuple.create("Test", 123));
        table2.addValues(Tuple.create(null, null));
        table2.addValues(Tuple.create("Third", 456));
        table2.addValues(Tuple.create("", null));
        assertEquals(4, table2.size());

        WithValuesByName[] rows = table2.getAllRows();
        Arrays.stream(rows).forEach(row -> Logger.D(row.toString()));
        assertEquals(Tuple.create("Third", 456), rows[2].getValues());
    }

    @Test
    public void getColumnIndexes() {
        TupleTable2<String, Integer> table2 = new TupleTable2<String, Integer>(
            new Columns("Id", "age"), String.class, Integer.class);
        Map<String, Integer> indexes = table2.getColumns();
        indexes.put("a", 3);
    }

    @Test
    public void contains() {
        TupleTable2<String, Integer> table2 = new TupleTable2<String, Integer>(
            new Columns("Id", "age"), String.class, Integer.class);
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
        TupleTable2<String, Integer> table2 = new TupleTable2<String, Integer>(
            new Columns("Id", "age"), String.class, Integer.class);
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
        TupleTable2<String, Integer> table2 =new TupleTable2<String, Integer>(
            new Columns("Id", "age"), String.class, Integer.class);
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
        TupleTable2<String, Integer> table2 = new TupleTable2<String, Integer>(
            new Columns("Id", "age"), String.class, Integer.class);
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
    public void getColumnValues(){
        Columns columns = new Columns("ID", "First Name", "Last Name", "Gender", "IsActive", "Favorite", "Other");
        TupleTable5<Integer, String, String, Character, Boolean> table5 = new TupleTable5<Integer, String, String, Character, Boolean>(columns,
            Integer.class, String.class, String.class, Character.class, Boolean.class);
        table5.addValues(Tuple.create(0, "Alice", "Wilson", 'F', true));
        table5.addValues(1, "Bob", "Nilson", 'M', false, 99);
        table5.addValues(2, "Clare", "Neons", 'F', true, "Movie", 25);
        table5.addValues(3, "David", "Wilson", 'M', null, "", 20);
        table5.addValues(Tuple.create(4, "Eddy", "Claks", 'M', true, null, "Unknown", LocalDate.of(2019, 4, 11)));

        assertTrue(Objects.deepEquals(new Integer[]{0,1,2,3,4}, table5.getColumnValues(0)));
        assertTrue(Objects.deepEquals(new String[]{"Alice", "Bob", "Clare", "David", "Eddy"}, table5.getColumnValues(1)));
        assertTrue(Objects.deepEquals(null, table5.getColumnValues(8)));
        assertTrue(Objects.deepEquals(new String[]{"Wilson", "Nilson", "Neons", "Wilson", "Claks"}, table5.getColumnValues("Last Name")));
        assertTrue(Objects.deepEquals(new Character[]{'F', 'M', 'F', 'M', 'M'}, table5.getColumnValues("Gender")));
        assertTrue(Objects.deepEquals(new Boolean[]{true, false, true, null, true}, table5.getColumnValues("IsActive")));
    }

    @Test
    public void getColumnValues2(){
        Map<Integer, WithValues1<String[]>> sharedColumnDefitions = new HashMap<Integer, WithValues1<String[]>>(){{
            put(0, Tuple.create(new String[]{"ID", "UID", "Unique Id"}));
            put(1, Tuple.create(new String[]{"First Name", "Given Name", "User Name"}));
            put(2, Tuple.create(new String[]{"Birthday"}));
            put(3, Tuple.create(new String[]{"Mobile", "Phone"}));
            put(4, Tuple.create(new String[]{"Email"}));
            put(5, Tuple.create(new String[]{"Address"}));
        }};

         final IColumns columns = new Columns(sharedColumnDefitions, Columns.ESCAPED_CASE_INSENSITIVE);
         TupleTable6<Integer, String, LocalDate, Integer, String, String> table6 = columns.createTable6(
             Integer.class, String.class, LocalDate.class, Integer.class, String.class, String.class);

         table6.addValues(0, "Tom", LocalDate.of(2000, 1, 1), 400123456, "tom@email.com", null);
         table6.addValues(1, "Bob", LocalDate.of(1977, 1, 1), 071234456, null, "Bob's home");
         table6.addValues(3, "Charlie", null, 400333444, "charlie@email.com", null);
         table6.addValues(9, "Denis", null, null, null, null);
         table6.addValues(2, "Eddy", LocalDate.of(1977, 1, 1), 023337747, "eddy@email.com", "Eddy's house");

        assertTrue(Objects.deepEquals(new Integer[]{0,1,3,9,2}, table6.getColumnValues("UID")));
        assertTrue(Objects.deepEquals(new String[]{"Tom", "Bob", "Charlie", "Denis", "Eddy"}, table6.getColumnValues("user_name")));
        assertTrue(Objects.deepEquals(new LocalDate[]{LocalDate.of(2000, 1, 1),
            LocalDate.of(1977, 1, 1), null, null, LocalDate.of(1977, 1, 1)}, table6.getColumnValues("birthday")));

    }

    @Test
    public void add() {
        Columns columns = new Columns("ID", "First Name", "Last Name", "Gender", "IsActive", "Favorite", "Other");
        TupleTable5<Integer, String, String, Character, Boolean> table5 = new TupleTable5<Integer, String, String, Character, Boolean>(columns,
            Integer.class, String.class, String.class, Character.class, Boolean.class);
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
        assertEquals(5, table5.width());
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
        Columns columns = new Columns("ID", "First Name", "Last Name", "Gender", "IsActive", "Favorite", "Other");
        TupleTable5<Integer, String, String, Character, Boolean> table5 = columns.createTable5(Integer.class, String.class, String.class, Character.class, Boolean.class);
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
        assertFalse(table5.removeValues(1, "Bob", "Nilson", null, false, 22));        //Not matched with the "Gender" value
        assertFalse(table5.removeValues(1, "Bob", "Nilson", 'M', false, 22, new int[0]));        //Not matched with two extra values
        assertFalse(table5.removeValues(1, "Bob", "Nilson", 'M'));                    //Not matched by shorting last 2 values
        assertTrue(table5.removeValues(1, "Bob", "Nilson", 'M', false, 99));          //shall be removed

        //Remove second to last row by its values
        assertTrue(table5.removeValues(4, "Eddy", "Claks", 'M', true, null, "Unknown", LocalDate.of(2019, 4, 11)));

        //Remove last row
        assertTrue(table5.removeValues(5, "Fred", "Nil", 'M', false));

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
        Columns columns = new Columns("ID", "First Name", "Last Name", "Gender", "IsActive", "Favorite", "Other");
        TupleTable5<Integer, String, String, Character, Boolean> table5 = columns.createTable5(
            Integer.class, String.class, String.class, Character.class, Boolean.class);
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
        Columns columns = new Columns("ID", "First Name", "Last Name", "Gender", "IsActive", "Favorite", "Other");
        TupleTable5<Integer, String, String, Character, Boolean> table5 = columns.createTable5(
            Integer.class, String.class, String.class, Character.class, Boolean.class
        );
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
        Columns columns = new Columns("ID", "First Name", "Last Name", "Gender", "IsActive", "Favorite", "Other");
        TupleTable5<Integer, String, String, Character, Boolean> table5 = columns.createTable5(
            Integer.class, String.class, String.class, Character.class, Boolean.class);
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
        Columns columns = new Columns("ID", "First Name", "Last Name", "Gender", "IsActive", "Favorite", "Other");
        TupleTable5<Integer, String, String, Character, Boolean> table5 = columns.createTable5(
            Integer.class, String.class, String.class, Character.class, Boolean.class);
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