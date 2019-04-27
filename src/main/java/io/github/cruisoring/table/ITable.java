package io.github.cruisoring.table;

import io.github.cruisoring.function.FunctionThrowable;
import io.github.cruisoring.function.PredicateThrowable;
import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues;
import io.github.cruisoring.utility.ArrayHelper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static io.github.cruisoring.Asserts.checkStates;
import static io.github.cruisoring.Asserts.checkWithoutNull;

/**
 * Interface of generic Table which is composed as collection of rows
 *
 * @param <R> Type of the table rows, shall be of {@code Tuple}
 */
public interface ITable<R extends WithValues> extends Collection<WithValuesByName> {
    /**
     * Retrieve ALL column columns of a RowDataSupplier, return them as a list with non-key columns before all key columns.
     * Thus both Update and Insert SQL statements could use data in this order.
     *
     * @return
     */
    IColumns getColumns();

    /**
     * Given a solid column tableName, find its ordinal index.
     *
     * @param columnName Name of the concerned column.
     * @return Index of the column concerned (0 based)
     */
    int getColumnIndex(String columnName);

    /**
     * Get the element type of the column specified by its name.
     *
     * @param columnName the name of the concerned column.
     * @return <code>null</code> if the column cannot be found with the given columnName, or {@code Object.class}
     * if its element type is not specified, otherwise its defined element type.
     */
    default Class getColumnElementType(String columnName) {
        int index = getColumnIndex(columnName);
        if (index == -1) {
            return null;
        }

        Class[] elementTypes = getElementTypes();
        return index < elementTypes.length ? elementTypes[index] : Object.class;
    }

    /**
     * Get all column columns defined in the RowDataSupplier.
     *
     * @return List of column columns.
     */
    Collection<String> getDisplayedNames();

    /**
     * Get total number of columns that have element type specified.
     *
     * @return Number of columns with element type specified.
     */
    int width();

    /**
     * Get the element types of each columns of this {@code ITable} instance.
     * @return  Array of types, each type specify the value type at the corresponding column.
     */
    Class[] getElementTypes();

    /**
     * Check if the <code>WithValues</code> specified by its values is contained by this <code>ITable</code>
     * @param values    all values including nulls contained by the matched <code>WithValues</code>
     * @return  <code>true</code> if it does exist such a <code>WithValues</code> with exactly values as specified
     * , otherwise <code>false</code>
     */
    default boolean contains(Object... values){
        return contains(Tuple.of(values));
    }

    /**
     * Get the index of the concerned row.
     *
     * @param row the row concerned with names identified.
     * @return  index of the row with identical values matched, otherwise <code>-1</code>
     */
    int indexOf(WithValuesByName row);

    /**
     * Get the index of the concerned row whose values have been specified by column names.
     *
     * @param valuesByName  Values of the row with only recognizable column names as the key.
     * @return      index of the first row whose values matched with the given {@code valuesByName}, or <code>-1</code>
     *              if there is no matching
     */
    int indexOf(Map<String, Object> valuesByName);

    /**
     * Placeholder of Collection&lt;WithValuesByName&gt;.add()
     * @param row   row to be added.
     * @return always return false and let the sub-classes to add <code>WithValuesByName</code> of matched type
     * as this <code>TupleTable</code>
     */
    boolean add(WithValuesByName row);

    /**
     * Add a row whose values are defined as a {@code Map<String, Object>} with missing ones as nulls.
     *
     * @param valuesByName  Values of the row with only recognizable column names as the key.
     * @return      <code>true</code> if the ITuple changes by adding the values, otherwise <code>false</code>
     */
    boolean addValues(Map<String, Object> valuesByName);

    /**
     * Add a qualified tuple to the <code>ITable</code> as a new row.
     *
     * @param tuple Tuple of the expected values of a new row.
     * @return <tt>true</tt> if the ITuple changes by adding the values.
     */
    boolean addValues(R tuple);

    /**
     * Get the row by its index (0 based)
     *
     * @param rowIndex the Index of the concnerned row (0 based)
     * @return The row with index of <code>rowIndex</code> if existing, otherwise null if rowIndex is out of range.
     */
    WithValuesByName getRow(int rowIndex);

    /**
     * Get first row whose values have been specified by column names.
     *
     * @param valuesByName  Values of the row with only recognizable column names as the key.
     * @return      the first row with specific values at concerned columns if it does exist, otherwise <code>null</code>
     */
    WithValuesByName getRow(Map<String, Object> valuesByName);


    /**
     * Get all rows of values as an array of {@WithValuesByName}
     *
     * @return the array of {@WithValuesByName}, each represent the corresponding elements with the {@code IColumns} of this table
     */
    WithValuesByName[] getAllRows();

    /**
     * Get all rows meeting expected conditions as an array of {@WithValuesByName}.
     *
     * @paramc expectedConditions the predicates for values identified by names
     * @return the array of {@WithValuesByName}, each represent the corresponding elements with the {@code IColumns} of this table
     */
    WithValuesByName[] getAllRows(Map<String, PredicateThrowable> expectedConditions);

    /**
     * Get all rows meeting expected conditions as a Stream of {@WithValuesByName}.
     *
     * @param expectedConditions the predicates for values identified by names
     * @return  the Stream of {@WithValuesByName} all meet conditions specified.
     */
    Stream<WithValuesByName> streamOfRows(Map<String, PredicateThrowable> expectedConditions);

    /**
     * Get specific values of concerned row and represent with different columns
     * @param rowIndex      the Index of the concnerned row (0 based)
     * @param viewColumns   {@code IColumns} of another view with same or different number of columns with different names
     * @return  {@ WithValuesByName} with values of names specified by the {@code viewColumns}
     */
    WithValuesByName getRow(int rowIndex, IColumns viewColumns);

    /**
     * Get the view of this {@code ITable} with its columns definition as another {@code ITable}
     * @param viewColumns   the Column definition of new view.
     * @return A new {@code ITable} with the given columns.
     */
    ITable getView(IColumns viewColumns);

    /**
     * Validate columns exist for each of collection of String keys, and return the corresponding positions as the keys of the map.
     *
     * @param valueKeys Concerned keys of values that shall have one-to-one mappings of the columns.
     * @return      a {@code Map<Integer, String>} instance whose values are the concerned keys of the values, and their
     *              corresponding positions as the keys.
     */
    default Map<Integer, String> getIndexedNames(Collection<String> valueKeys) {
        checkWithoutNull(valueKeys);

        Map<Integer, String> map = new HashMap<>();
        IColumns columns = getColumns();
        for (String key : valueKeys) {
            Integer index = columns.get(key);
            if(index == -1){
                throw new UnsupportedOperationException("Value key of '" + key + "' is not recognizable!");
            } else if (map.containsKey(index)){
                throw new IllegalArgumentException(String.format("'%s' and '%s' are mapped to the same column %s @ %d",
                        map.get(index), key, columns.getColumnNames().get(index), index));
            }
            map.put(index, key);
        }
        return map;
    }

    /**
     * Get the value of the cell with its <code>rowIndex</code> and <code>columnIndex</code>
     *
     * @param rowIndex    the Index of the row containting that cell (0 based)
     * @param columnIndex the Index of the column where the cell is in the row (0 based)
     * @return value of the concerned cell if the <code>rowIndex</code> and <code>columnIndex</code> can locate the cell
     */
    default Object getCellValue(int rowIndex, int columnIndex) {
        checkStates(rowIndex >= 0, columnIndex >= 0);

        WithValues row = getRow(rowIndex);

        return row == null || columnIndex >= row.getLength() ?
                null : row.getValue(columnIndex);
    }

    /**
     * Get the value of the cell with its <code>rowIndex</code> and <code>columnName</code>
     *
     * @param rowIndex   the Index of the row containting that cell (0 based)
     * @param columnName the name of the column where the cell is in the row
     * @return value of the concerned cell if the <code>rowIndex</code> and <code>columnName</code> can locate the cell,
     * otherwise null
     */
    default Object getCellValue(int rowIndex, String columnName) {
        checkStates(rowIndex>=0, columnName != null);

        int columnIndex = getColumns().getOrDefault(columnName, -1);
        if (columnIndex == -1) {
            return null;
        }
        return getCellValue(rowIndex, columnIndex);
    }

    /**
     * Get cell values with index of <code>columnIndex</code> of all rows.
     * @param columnIndex   index of the concerned column.
     * @return  <code>null</code> if columnIndex is less than 0, otherwise the Object[] containing all values of these cell.
     */
    default Object getColumnValues(int columnIndex){
        if(columnIndex < 0 || columnIndex >= width()){
            return null;
        }

        int size = size();
        Object colValues = ArrayHelper.getNewArray(getElementTypes()[columnIndex], size);
        ArrayHelper.setAll(colValues, i -> getCellValue(i, columnIndex));

        return colValues;
    }

    /**
     * Get cell values with column name of <code>columnName</code> of all rows.
     * @param columnName   Name of the concerned column.
     * @return  <code>null</code> if columnName or not defined, otherwise the Object[] containing all values of these cell.
     */
    default Object getColumnValues(String columnName){
        checkWithoutNull(columnName);

        int columnIndex = getColumnIndex(columnName);
        return getColumnValues(columnIndex);
    }

    /**
     * Replace the concerned row with given values identified by names.
     *
     * @param row       the row to be updated
     * @param newValues the new values used to replace the corresponding values of the concerned row
     * @return          <tt>true</tt> if replace happens with the concerned row, otherwise <tt>false</tt>
     */
    boolean replace(WithValuesByName row, Map<String, Object> newValues);

    /**
     * Update the concerned row with all values generated by the {@code valueSuppliers}
     *
     * @param row       the row to be updated
     * @param valueSuppliers    one or multiple value suppliers to generate element values for the concerned row
     * @return          <tt>true</tt> if update happens with the concerned row, otherwise <tt>false</tt>
     */
    boolean update(WithValuesByName row, Map<String, FunctionThrowable<WithValuesByName, Object>> valueSuppliers);

    /**
     * Update all concerned rows with values generated by the suppliers
     * @param rowsToBeUpdate    the rows to be updated as a {@code Stream}
     * @param valueSuppliers    one or multiple value suppliers to generate element values for the concerned row
     * @return      number of rows changed
     */
    int updateAll(Stream<WithValuesByName> rowsToBeUpdate, Map<String, FunctionThrowable<WithValuesByName, Object>> valueSuppliers);

    /**
     * Remove the <code>WithValues</code> by its values.
     * @param values    all values including nulls contained by the matched <code>WithValues</code>
     * @return  <code>true</code> if removal happened, otherwise <code>false</code>
     */
    default boolean removeValues(Object... values){
        return remove(Tuple.of(values));
    }
}
