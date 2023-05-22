package src;

import javax.swing.table.AbstractTableModel;
import java.io.*;

public class CustomTableModel extends AbstractTableModel {

    private String[][] data;
    private String[] columnNames;
    private String fileName;

    public CustomTableModel(String[][] data, String fileName) {
        this.data = data;
        this.columnNames = new String[]{"JAHR", "JAN", "FEB", "MAR", "APR", "MAI", "JUN",
                "JUL", "AUG", "SEP", "OKT", "NOV", "DEZ"};
        this.fileName = fileName;
    }

    public CustomTableModel(String[][] data) {
        this.data = data;
        this.columnNames = new String[]{"JAHR", "JAN", "FEB", "MAR", "APR", "MAI", "JUN",
                "JUL", "AUG", "SEP", "OKT", "NOV", "DEZ"};
    }

    public void saveData() {
        try (FileWriter fileWriter = new FileWriter("data/" + fileName);
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
        data = newData;
        fireTableDataChanged();
    }
}
