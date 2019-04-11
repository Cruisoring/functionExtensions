package io.github.cruisoring.table;

import io.github.cruisoring.tuple.WithValues;

import java.util.Collection;
import java.util.Map;

/**
 * Interface of generic Table which is composed as collection of rows
 *
 * @param <R> Type of the table rows that shall be of <code>Tuple</code>
 */
public interface ITable<R extends WithValues> extends Collection<WithValuesByName> {

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
    Collection<String> getColumns();

    /**
     * Get total column number that identified as non-empty cells of the first row.
     *
     * @return Number of columns identified in the first row.
     */
    int width();

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
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    WithValues getRow(int rowIndex) throws IndexOutOfBoundsException;

    /**
     * Retrieve ALL column columns of a RowDataSupplier, return them as a list with non-key columns before all key columns.
     * Thus both Update and Insert SQL statements could use data in this order.
     *
     * @return
     */
    Map<String, Integer> getColumnIndexes();

    /**
     * Get the value of the cell with its <code>rowIndex</code> and <code>columnIndex</code>
     *
     * @param rowIndex    the Index of the row containting that cell (0 based)
     * @param columnIndex the Index of the column where the cell is in the row (0 based)
     * @return value of the concerned cell if the <code>rowIndex</code> and <code>columnIndex</code> can locate the cell
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    default Object getCellValue(int rowIndex, int columnIndex) throws IndexOutOfBoundsException {
        WithValues row = getRow(rowIndex);

        return row.getValue(columnIndex);
    }

    /**
     * Get the value of the cell with its <code>rowIndex</code> and <code>columnName</code>
     *
     * @param rowIndex   the Index of the row containting that cell (0 based)
     * @param columnName the name of the column where the cell is in the row
     * @return value of the concerned cell if the <code>rowIndex</code> and <code>columnName</code> can locate the cell
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    default Object getCellValue(int rowIndex, String columnName) throws IndexOutOfBoundsException {
        int columnIndex = getColumnIndexes().getOrDefault(columnName, -1);
        if (columnIndex == -1) {
            throw new IndexOutOfBoundsException("No column of " + columnName);
        }
        return getCellValue(rowIndex, columnIndex);
    }
}
