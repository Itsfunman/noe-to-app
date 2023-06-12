package src;

import javax.swing.table.AbstractTableModel;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CustomTableModel extends AbstractTableModel {

    private String[][] data;
    private String[] columnNames;
    private String fileName;

    public CustomTableModel(String[][] data, String fileName, String [] columnNames) {
        this.data = data;
        this.columnNames = columnNames;
        this.fileName = fileName;
    }

    public CustomTableModel(String[][] data, String [] columnNames) {
        this.data = data;
        this.columnNames = columnNames;
    }

    public void saveData() {
        try (FileWriter fileWriter = new FileWriter(fileName);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {

            for (String[] row : data) {
                StringBuilder line = new StringBuilder();
                for (String value : row) {
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
        return true; // Set the cells as editable
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value.toString();
        fireTableCellUpdated(row, col);
    }

    public void setData(String[][] newData) {
        this.data = newData;
        fireTableDataChanged();
    }


    public String[][] getData() {
        return this.data;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void addRow(String[] rowData) {
        // Create a new data array with increased size
        String[][] newData = new String[data.length + 1][getColumnCount()];
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

    public void removeRow(int row) {
        // Create a new data array with reduced size
        String[][] newData = new String[data.length - 1][getColumnCount()];
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

