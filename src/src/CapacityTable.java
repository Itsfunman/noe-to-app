package src;

import javax.swing.*;
import java.awt.*;

public class CapacityTable extends JScrollPane {

    private JTable table;
    private TableModel tableModel;

    private String [] columnNames = {"Kategorie", "Betriebe", "Zimmer", "Betten", "Ø Zimmer", "Ø Betten"};
    //Enter hotel information here
    Object[][] data = {
            {"row1col1", "row1col2", "row1col3", "row1col4", "row1col5", "row1col6"},
            {"row2col1", "row2col2", "row2col3", "row2col4", "row2col5", "row2col6"},
            {"row3col1", "row3col2", "row3col3", "row3col4", "row3col5", "row3col6"},
            {"row4col1", "row4col2", "row4col3", "row4col4", "row4col5", "row4col6"},
            {"row5col1", "row5col2", "row5col3", "row5col4", "row5col5", "row5col6"},
            {"row6col1", "row6col2", "row6col3", "row6col4", "row6col5", "row6col6"}
    };


    public CapacityTable() {

        tableModel = new TableModel(data, columnNames);
        table = new JTable(tableModel);

        setLayout(new ScrollPaneLayout());

        setViewportView(table);

    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public TableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(TableModel tableModel) {
        this.tableModel = tableModel;
    }
}
