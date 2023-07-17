package tableClasses;

import javax.swing.table.AbstractTableModel;

/**
 * Abstract class used for the capacity table
 */
public class TableModel extends AbstractTableModel {

    private Object [][] data;
    private String [] columnNames;

    /**
     * Initializes the TableModel
     * @param data
     * @param columnNames
     */
    public TableModel(Object[][] data, String[] columnNames) {

        this.data = data;
        this.columnNames = columnNames;

    }

    /**
     * Returns the amount of rows
     * @return
     */
    @Override
    public int getRowCount() {
        return data.length;
    }

    /**
     * Returns the amount of coluns
     * @return
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Returns the column names
     * @param column
     * @return
     */
    @Override
    public String getColumnName(int column){
        return columnNames[column];
    }

    /**
     * Returns the value in a certain cell
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }


}
