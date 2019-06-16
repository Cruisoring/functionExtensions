package io.github.cruisoring.table;

import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.throwables.PredicateThrowable;
import io.github.cruisoring.tuple.Tuple;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static io.github.cruisoring.Asserts.*;


public class TupleTableDemoTest {
    @Test
    public void createSimpleTable(){
        IColumns columns = new Columns("StudentId", "Name", "Gender", "Class", "Score", "Note");
        TupleTable5<Long, String, Character, String, Double> scoresTable = columns.createTable5(Long.class, String.class, Character.class, String.class, Double.class);

        scoresTable.addValues(1003L, "Apple Juicy", 'F', "6A", 89.5);
        scoresTable.addValues(1008L, "Bob Peterson", 'M', "6L", 99.0, "highest one");
        scoresTable.addValues(1008L, "Clark Cooker", 'M', "6B", 58.0, "failed");
        scoresTable.add(columns.createRow(1011L, "David Simpson", 'M', "6A", 77.5d));
        scoresTable.addValues(Tuple.create(1013L, "Emar K", 'F', "6C", 95.5, "outstanding"));
        scoresTable.addValues(new HashMap<String, Object>(){{
            put("StudentId", 1033L);
            put("Name", "Fred Thomson");
            put("Gender", 'M');
            put("Class", "6N");
            put("Note", "sick leave");
        }});
        scoresTable.forEach(row -> Logger.D(row.toString()));

        WithValuesByName row2 = scoresTable.getRow(2);

    }

    @Test
    public void accessRowOrCellByIndexes(){
        IColumns columns = new Columns("StudentId", "Name", "Gender", "Class", "Score", "Note");
        TupleTable5<Long, String, Character, String, Double> scoresTable = columns.createTable5(Long.class, String.class, Character.class, String.class, Double.class);

        scoresTable.addValues(1003L, "Apple Juicy", 'F', "6A", 89.5);
        scoresTable.addValues(1008L, "Bob Peterson", 'M', "6L", 99.0, "highest one");
        scoresTable.addValues(1008L, "Clark Cooker", 'M', "6B", 58.0, "failed");
        scoresTable.add(columns.createRow(1011L, "David Simpson", 'M', "6A", 77.5d));

        assertAllNull(scoresTable.getRow(8));
        WithValuesByName row2 = scoresTable.getRow(2);
        assertEquals(58.0, row2.getValueByName("Score"));
        assertEquals("failed", row2.getValue(5));
        Logger.D(row2.toString());
        assertEquals("Clark Cooker", scoresTable.getCellValue(2, 1));
        assertEquals(58.0, scoresTable.getCellValue(2, "Score"));

        Map<String, Object> map2 = row2.asMap();
    }

    @Test
    public void castingToStrongTyped(){
        IColumns columns = new Columns("StudentId", "Name", "Gender", "Class", "Score", "Note");
        TupleTable5<Long, String, Character, String, Double> scoresTable = columns.createTable5(Long.class, String.class, Character.class, String.class, Double.class);

        scoresTable.addValues(1003L, "Apple Juicy", 'F', "6A", 89.5);
        scoresTable.addValues(1008L, "Bob Peterson", 'M', "6L", 99.0, "highest one");
        scoresTable.addValues(1008L, "Clark Cooker", 'M', "6B", 58.0, "failed");
        scoresTable.add(columns.createRow(1011L, "David Simpson", 'M', "6A", 77.5d));

        assertAllNull(scoresTable.getRow(8));
        WithValuesByName5<Long, String, Character, String, Double> row3 = (WithValuesByName5<Long, String, Character, String, Double>) scoresTable.getRow(3);
        assertEquals(Long.valueOf(1011), row3.getFirst());
        assertEquals("David Simpson", row3.getSecond());
        assertEquals(Character.valueOf('M'), row3.getThird());
        assertEquals("6A", row3.getFourth());
        assertEquals(Double.valueOf(77.5), row3.getFifth());
    }

    @Test
    public void valueTypesEnforcement(){
        IColumns columns = new Columns("StudentId", "Name", "Gender", "Class", "Score", "Note");
        TupleTable5<Long, String, Character, String, Double> scoresTable = columns.createTable5(Long.class, String.class, Character.class, String.class, Double.class);

//        scoresTable.addValues(1003L, "Apple Juicy", 'F', "6A");
//        scoresTable.addValues(1008, "Bob Peterson", 'M', "6L", 99.0, "highest one");
//        scoresTable.addValues(1008L, "Clark Cooker", 'M', 58.0, "6B", "failed");

        assertAllFalse(scoresTable.add(columns.createRow(1011L, "David Simpson", 'M')));

        assertAllFalse(scoresTable.addValues(Tuple.create(1013L, "Emar K", 'F', "6C", 95f, "outstanding")));

        assertAllFalse(scoresTable.addValues(Tuple.create(1013L, 'F', "6C", "Emar K", 92d)));

        assertAllFalse(scoresTable.addValues(new HashMap<String, Object>(){{
            put("StudentId", 1033);
            put("Name", "Fred Thomson");
            put("Gender", "Male");
            put("Class", "6N");
            put("Note", "sick leave");
        }}));
        assertEquals(0, scoresTable.size());
    }

    @Test
    public void containsAndIndexOf(){
        IColumns columns = new Columns("StudentId", "Name", "Gender", "Class", "Score", "Note");
        TupleTable5<Long, String, Character, String, Double> scoresTable = columns.createTable5(Long.class, String.class, Character.class, String.class, Double.class);

        scoresTable.addValues(1003L, "Apple Juicy", 'F', "6A", 89.5);
        scoresTable.addValues(1008L, "Bob Peterson", 'M', "6L", 99.0, "highest one");
        scoresTable.addValues(1008L, "Clark Cooker", 'M', "6B", 58.0, "failed");
        scoresTable.add(columns.createRow(1011L, "David Simpson", 'M', "6A", 77.5d));

        //Contains(Object...) treat the elements as a whole to compare its hashCode first before valueEquals()
        assertAllTrue(scoresTable.contains(1003L, "Apple Juicy", 'F', "6A", 89.5));

        //Even a minor changes of the element value would result in a different hashCode, thus make contains fail fast
        assertAllFalse(scoresTable.contains(1003L, "Apple Juicy", 'F', "6B", 89.5));

        WithValuesByName row1 = scoresTable.getRow(1);
        //indexOf(WithValuesByName) run much faster if the Columns of row is identical with the TupleTable
        assertEquals(1, scoresTable.indexOf(row1));

        //indexOf(Map<String, Object>) means to locate row referred by conventional Map
        assertEquals(2, scoresTable.indexOf(new HashMap<String, Object>(){{
            put("Score", 58.0);
            put("StudentId", 1008L);
            put("Name", "Clark Cooker");
            put("Class", "6B");
            put("Gender", 'M');
            put("Note", "failed");
        }}));
    }

    @Test
    public void filterRowsWithValueOrPredicates(){
        IColumns columns = new Columns("StudentId", "Name", "Gender", "Class", "Score", "Note");
        TupleTable5<Long, String, Character, String, Double> scoresTable = columns.createTable5(Long.class, String.class, Character.class, String.class, Double.class);

        scoresTable.addValues(1003L, "Apple Juicy", 'F', "6A", 89.5);
        scoresTable.addValues(1008L, "Bob Peterson", 'M', "6L", 99.0, "highest one");
        scoresTable.addValues(1008L, "Clark Cooker", 'M', "6B", 58.0, "failed");
        scoresTable.add(columns.createRow(1011L, "David Simpson", 'M', "6A", 77.5d));


        //getRow(Map<String, Object> valuesByName) provides a quick means to find the first matched row by specifying concerned element values
        assertEquals("Clark Cooker", scoresTable.getRow(new HashMap<String, Object>(){{
            put("Class", "6B");
        }}).getValueByName("Name"));

        //streamOfRows(Map<String, PredicateThrowable> expectedConditions) allows filter and iterate through them fluently
        Stream<WithValuesByName> rowsOf6A = scoresTable.streamOfRows(new HashMap<String, PredicateThrowable>(){{
            put("Class", o -> o.toString().equals("6A"));
        }});
        assertEquals(2L, rowsOf6A.count());

        //getAllRows(Map<String, PredicateThrowable> expectedConditions) collect the stream as an Array, it can specify multiple conditions for filtering
        WithValuesByName[] maleOver75 = scoresTable.getAllRows(new HashMap<String, PredicateThrowable>(){{
            put("Gender", o -> o.equals('M'));
            put("Score", o -> (Double)o > 75.0);
        }});
        assertEquals(2, maleOver75.length);
        assertEquals("Bob Peterson", maleOver75[0].getValueByName("Name"));
        assertEquals("David Simpson", maleOver75[1].getValueByName("Name"));
    }

    @Test
    public void getColumnValues(){
        IColumns columns = new Columns("StudentId", "Name", "Gender", "Class", "Score", "Note");
        TupleTable5<Long, String, Character, String, Double> scoresTable = columns.createTable5(Long.class, String.class, Character.class, String.class, Double.class);

        scoresTable.addValues(1003L, "Apple Juicy", 'F', "6A", 89.5);
        scoresTable.addValues(1008L, "Bob Peterson", 'M', "6L", 99.0, "highest one");
        scoresTable.addValues(1008L, "Clark Cooker", 'M', "6B", 58.0, "failed");
        scoresTable.add(columns.createRow(1011L, "David Simpson", 'M', "6A", 77.5d));

        //The getColumnValues() can be casted to T[] if the type of the corresponding element has been declared as T
        String[] names = (String[]) scoresTable.getColumnValues(1);
        assertEquals(new Long[]{1003L, 1008L, 1008L, 1011L}, scoresTable.getColumnValues(0));
        assertEquals(new String[]{"Apple Juicy", "Bob Peterson", "Clark Cooker", "David Simpson"}, names);
        //When the index is out of the TupleTable.width(), it would return null
        assertAllNull(scoresTable.getColumnValues(5));

        //It is also possible to retrieve column values by name
        assertEquals(new Character[]{'F', 'M', 'M', 'M'}, scoresTable.getColumnValues("Gender"));
        assertEquals(new Double[]{89.5, 99.0, 58.0, 77.5}, scoresTable.getColumnValues("Score"));
        //return null if the index is out of the scope of the TupleTable
        assertAllNull(scoresTable.getColumnValues("Note"));
    }
}
