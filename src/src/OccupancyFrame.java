package src;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class OccupancyFrame extends JFrame {

    private OccupancyTable occupancyTable;
    private Toolbar toolbar;

    public OccupancyFrame(String title) {
        super(title);

        setSize(800, 500);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

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
        occupancyTable = new OccupancyTable();
        occupancyTable.setLocation(0, 30);
        occupancyTable.setVisible(true);
        add(occupancyTable);

        // Set the width of the JScrollPane to 3/4 of the window width and the height to equal the combined height of
        //the rows times 1.5 (don´t ask why, I don´t know
        double tableWidth = getWidth() * 0.75;
        double tableHeight = occupancyTable.getTable().getRowHeight() * (occupancyTable.getTable().getRowCount() + 1.5);
        occupancyTable.setSize((int) tableWidth, (int) tableHeight);

    }
}
