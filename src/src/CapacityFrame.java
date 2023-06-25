package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

//Gets originally initialized in the LoginFrame
public class CapacityFrame extends JFrame {

    private CapacityTable capacityTable;
    private Toolbar toolbar;

    public CapacityFrame(String title) {
        super(title);

        setSize(800, 500);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        //Set custom Icon
        ImageIcon icon = new ImageIcon("assets/NOETOLogo.jpg");
        setIconImage(icon.getImage());

        InitToolbar();
        InitCapacityTable();

    }

    private void InitToolbar() {
        toolbar = new Toolbar(this);
        toolbar.setVisible(true);
        toolbar.setLocation(0,0);

        add(toolbar);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                toolbar.setSize(getWidth(), 30);

            }
        });
    }

    private void InitCapacityTable() {
        capacityTable = new CapacityTable();
        capacityTable.setLocation(100, 60);
        capacityTable.setVisible(true);
        add(capacityTable);

        // Set the width of the JScrollPane to 3/4 of the window width and the height to equal the combined height of
        //the rows times 1.5 (don´t ask why, I don´t know
        double tableWidth = getWidth() * 0.75;
        double tableHeight = capacityTable.getTable().getRowHeight() * (capacityTable.getTable().getRowCount() + 1.5);
        capacityTable.setSize((int) tableWidth, (int) tableHeight);

    }


}
