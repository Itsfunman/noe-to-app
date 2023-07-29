package tableClasses;

import javax.swing.table.AbstractTableModel;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import lombok.*;

@Getter
@Setter
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
    public CustomTableModel(Object[][] data, String[] columnNames) {
        this.data = data;
        this.columnNames = columnNames;
    }


    @Override
    public Object getValueAt(int row, int col) {
        return data[row][col];
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
    public void setData(Object[][] newData) {
        this.data = newData;
        fireTableDataChanged();
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
