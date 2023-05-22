package src;

import javax.swing.*;

public class HotelOccupancyTable extends JScrollPane {

    private CustomTableModel tableModel;
    private JTable table;


    public HotelOccupancyTable(String[][] data) {
        tableModel = new CustomTableModel(data);
        table = new JTable(tableModel);

        setLayout(new ScrollPaneLayout());
        setViewportView(table);
    }

    public void updateData(String[][] newData) {
        tableModel.setData(newData);
    }
}

