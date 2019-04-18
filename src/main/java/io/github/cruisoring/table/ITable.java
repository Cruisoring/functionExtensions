package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues;
import io.github.cruisoring.utility.ArrayHelper;

import java.util.Collection;

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
     * Get all column columns defined in the RowDataSupplier.
     *
     * @return List of column columns.
     */
    Collection<String> getDisplayedNames();

    /**
     * Get total column number that identified as non-empty cells of the first row.
     *
     * @return Number of columns identified in the first row.
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
     * Placeholder of Collection&lt;WithValuesByName&gt;.add()
     * @param row   row to be added.
     * @return always return false and let the sub-classes to add <code>WithValuesByName</code> of matched type
     * as this <code>TupleTable</code>
     */
    boolean add(WithValuesByName row);

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
     * Get all rows of values as an array of {@WithValuesByName}
     *
     * @return the array of {@WithValuesByName}, each represent the corresponding elements with the {@code IColumns} of this table
     */
    WithValuesByName[] getAllRows();

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
     * Get the value of the cell with its <code>rowIndex</code> and <code>columnIndex</code>
     *
     * @param rowIndex    the Index of the row containting that cell (0 based)
     * @param columnIndex the Index of the column where the cell is in the row (0 based)
     * @return value of the concerned cell if the <code>rowIndex</code> and <code>columnIndex</code> can locate the cell
     */
    default Object getCellValue(int rowIndex, int columnIndex) {
        WithValues row = getRow(rowIndex);

        return row == null || columnIndex < 0 || columnIndex >= row.getLength() ? null : row.getValue(columnIndex);
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
        if(columnName == null){
            return null;
        }

        int columnIndex = getColumnIndex(columnName);
        return getColumnValues(columnIndex);
    }

    /**
     * Remove the <code>WithValues</code> by its values.
     * @param values    all values including nulls contained by the matched <code>WithValues</code>
     * @return  <code>true</code> if removal happened, otherwise <code>false</code>
     */
    default boolean removeValues(Object... values){
        return remove(Tuple.of(values));
    }
}
