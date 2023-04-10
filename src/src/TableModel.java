package src;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {

    private Object [][] data;
    private String [] columnNames;

    public TableModel(Object[][] data, String[] columnNames) {

        this.data = data;
        this.columnNames = columnNames;

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
    public String getColumnName(int column){
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }
}
