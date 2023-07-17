package tableClasses;

import javax.swing.table.AbstractTableModel;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A custom table model that extends AbstractTableModel and provides functionality for saving data to a file,
 * adding and removing rows, and managing table data and column names.
 */
public class CustomTableModel extends AbstractTableModel {

    private Object[][] data;
    private String[] columnNames;
    private String fileName;

    /**
     * Constructs a CustomTableModel with the provided data, file name, and column names.
     *
     * @param data        The initial data for the table.
     * @param fileName    The name of the file to save the data.
     * @param columnNames The column names for the table.
     */
    public CustomTableModel(Object[][] data, String fileName, String[] columnNames) {
        this.data = data;
        this.columnNames = columnNames;
        this.fileName = fileName;
    }

    /**
     * Constructs a CustomTableModel with the provided data and column names.
     *
     * @param data        The initial data for the table.
     * @param columnNames The column names for the table.
     */
    public CustomTableModel(String[][] data, String[] columnNames) {
        this.data = data;
        this.columnNames = columnNames;
    }

    /**
     * Saves the data in the table to a file.
     * The file name is specified during construction.
     */
    public void saveData() {
        try (FileWriter fileWriter = new FileWriter(fileName);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {

            Object[][] currentData = getData(); // Retrieve the current data from the table model

            for (Object[] row : currentData) {
                StringBuilder line = new StringBuilder();
                for (Object value : row) {
                    line.append(value).append(",");
                }
                line.deleteCharAt(line.length() - 1); // Remove the trailing comma
                writer.write(line.toString());
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col != 0; // Set the cells as editable
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value.toString();
        fireTableCellUpdated(row, col);
    }

    /**
     * Sets the data in the table model to the provided new data.
     *
     * @param newData The new data for the table.
     */
    public void setData(String[][] newData) {
        this.data = newData;
        fireTableDataChanged();
    }

    /**
     * Returns the current data in the table model.
     *
     * @return The current data.
     */
    public Object[][] getData() {
        return this.data;
    }

    /**
     * Returns the column names of the table model.
     *
     * @return The column names.
     */
    public String[] getColumnNames() {
        return columnNames;
    }

    /**
     * Sets the column names of the table model.
     *
     * @param columnNames The column names to set.
     */
    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    /**
     * Returns the file name for saving the table data.
     *
     * @return The file name.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the file name for saving the table data.
     *
     * @param fileName The file name to set.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Adds a new row with the provided rowData to the table.
     *
     * @param rowData The data for the new row.
     */
    public void addRow(String[] rowData) {
        // Create a new data array with increased size
        Object[][] newData = new Object[data.length + 1][getColumnCount()];
        // Copy existing data to the new array
        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < getColumnCount(); col++) {
                newData[row][col] = data[row][col];
            }
        }
        // Add the new row data to the new array
        for (int col = 0; col < getColumnCount(); col++) {
            newData[data.length][col] = rowData[col];
        }
        // Update the data and fire table data change event
        data = newData;
        fireTableDataChanged();
    }

    /**
     * Removes the row at the specified index from the table.
     *
     * @param row The index of the row to remove.
     */
    public void removeRow(int row) {
        // Create a new data array with reduced size
        Object[][] newData = new Object[data.length - 1][getColumnCount()];
        // Copy existing data except for the row to be removed
        int newRow = 0;
        for (int oldRow = 0; oldRow < data.length; oldRow++) {
            if (oldRow != row) {
                for (int col = 0; col < getColumnCount(); col++) {
                    newData[newRow][col] = data[oldRow][col];
                }
                newRow++;
            }
        }
        // Update the data and fire table data change event
        data = newData;
        fireTableDataChanged();
    }
}
