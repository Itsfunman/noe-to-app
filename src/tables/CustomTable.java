package tables;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A custom table component that extends JScrollPane and encapsulates a JTable.
 * It provides functionality for updating the table data and tracking the selected row.
 */
public class CustomTable extends JScrollPane {

    private CustomTableModel tableModel;
    private JTable table;
    private int selectedRow;

    /**
     * Constructs a CustomTable with the provided data and column names.
     *
     * @param data        The initial data for the table.
     * @param columnNames The column names for the table.
     */
    public CustomTable(String[][] data, String[] columnNames) {
        this.tableModel = new CustomTableModel(data, columnNames);
        this.table = new JTable(tableModel);

        setLayout(new ScrollPaneLayout());
        setViewportView(table);

        // Add mouse listener to track selected row
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedRow = table.rowAtPoint(e.getPoint());
            }
        });
    }

    /**
     * Constructs a CustomTable with the provided table model.
     *
     * @param tableModel The custom table model for the table.
     */
    public CustomTable(CustomTableModel tableModel) {
        this.tableModel = tableModel;
        this.table = new JTable(tableModel);

        setLayout(new ScrollPaneLayout());
        setViewportView(table);

        // Add mouse listener to track selected row
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedRow = table.rowAtPoint(e.getPoint());
            }
        });
    }

    /**
     * Updates the table with the current table model.
     * This method should be called after modifying the table data in the table model.
     */
    public void updateTable() {
        table.setModel(tableModel); // Set the updated table model
        tableModel.fireTableDataChanged(); // Notify the table model about the data change
    }

    /**
     * Updates the data in the table model with the provided new data.
     *
     * @param newData The new data for the table.
     */
    public void updateData(String[][] newData) {
        tableModel.setData(newData);
        tableModel.fireTableDataChanged(); // Notify the table model about the data change
    }

    /**
     * Returns the custom table model associated with the CustomTable.
     *
     * @return The custom table model.
     */
    public CustomTableModel getTableModel() {
        return tableModel;
    }

    /**
     * Sets the custom table model for the CustomTable.
     *
     * @param tableModel The custom table model to set.
     */
    public void setTableModel(CustomTableModel tableModel) {
        this.tableModel = tableModel;
    }

    /**
     * Returns the JTable component contained within the CustomTable.
     *
     * @return The JTable component.
     */
    public JTable getTable() {
        return table;
    }

    /**
     * Sets the JTable component for the CustomTable.
     *
     * @param table The JTable component to set.
     */
    public void setTable(JTable table) {
        this.table = table;
    }

    /**
     * Returns the index of the selected row in the table.
     *
     * @return The index of the selected row.
     */
    public int getSelectedRow() {
        return selectedRow;
    }

    /**
     * Sets the index of the selected row in the table.
     *
     * @param selectedRow The index of the selected row to set.
     */
    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }

    /**
     * Returns the value at the specified row and column in the table.
     *
     * @param row    The row index.
     * @param column The column index.
     * @return The value at the specified row and column.
     */
    public Object getValueAt(int row, int column) {
        return tableModel.getValueAt(row, column);
    }

}
