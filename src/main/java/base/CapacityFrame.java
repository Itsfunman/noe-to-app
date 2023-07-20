package base;

import sqlStuff.CapacityDAO;
import tableClasses.CustomTable;
import tableClasses.CustomTableModel;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class CapacityFrame extends JFrame {

    private CustomTable customTable;
    private CustomTableModel customTableModel;

    private String[] columnNames = new String[]{"CATEGORY", "BEDS", "ROOMS", "BEDS", "Ø ROOMS", "Ø BEDS"};

    private Toolbar toolbar;

    /**
     * Constructs a CapacityFrame with the specified title.
     *
     * @param title the title of the frame
     */
    public CapacityFrame(String title) {
        super(title);

        setSize(800, 500);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Set custom Icon
        ImageIcon icon = new ImageIcon("assets/NOETOLogo.jpg");
        setIconImage(icon.getImage());

        initToolbar();
        initCapacityTable();
    }

    private void initToolbar() {
        toolbar = new Toolbar(this);
        toolbar.setVisible(true);
        toolbar.setLocation(0, 0);

        add(toolbar);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                toolbar.setSize(getWidth(), 30);
            }
        });
    }

    private void initCapacityTable(){

        // Remove the previous table, if it exists
        if (customTable != null) {
            remove(customTable);
        }

        Object [][] dataList = CapacityDAO.getCapacityViewFromDB();


        customTableModel = new CustomTableModel(dataList, columnNames);
        customTable = new CustomTable(customTableModel);
        customTable.setBounds(10, 50, 770, 350);

        add(customTable);

    }

}
