package src;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomTable extends JScrollPane {

    private CustomTableModel tableModel;
    private JTable table;
    private int selectedRow;

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

    public void updateTable() {
        table.setModel(tableModel); // Set the updated table model
        tableModel.fireTableDataChanged(); // Notify the table model about the data change
    }

    public void updateData(String[][] newData) {
        tableModel.setData(newData);
    }

    public CustomTableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(CustomTableModel tableModel) {
        this.tableModel = tableModel;
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }

    public Object getValueAt(int row, int column) {
        return tableModel.getValueAt(row, column);
    }

}
