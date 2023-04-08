package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class CapacityTable extends JTable {

    private String [] headers;
    //needs to create TableModel
    private TableModel capacityModel = new TableModel();

    public CapacityTable() {
        super(capacityModel);
        this.headers = new String[]{"Kategorie", "Betriebe", "Zimmer", "Betten", "Ø Zimmer", "Ø Betten"};
    }
}
