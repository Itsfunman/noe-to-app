package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

//Gets originally initialized in the LoginFrame
public class CapacityFrame extends JFrame {

    private JPanel capacityView = new JPanel();


    private CapacityTable capacityTable = new CapacityTable();

    //private JButton testButton = new JButton("Test");

    public CapacityFrame(String title) {
        super(title);

        setSize(800, 500);
        setResizable(true);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        InitCapacityTable();
    }

    private void InitCapacityTable(){

        capacityView.setSize((getWidth()/4) * 3, (getHeight()/4) * 3);
        capacityView.setVisible(true);

        add(capacityView);

        capacityTable.setSize((getWidth()/4) * 3, (getHeight()/4) * 3);
        capacityTable.setVisible(true);


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int x = 0;
                int y = 30;
                capacityView.setLocation(x,y);
                capacityView.setSize((getWidth()/4) * 3, (getHeight()/4) * 3);
                capacityTable.setLocation(x,y - 30);
                capacityTable.setSize((getWidth()/4) * 3, (getHeight()/4) * 3);

            }
        });
        capacityView.add(capacityTable);
    }

    /*
    private void InitTestButton(){

        //Defines component and adds it to the frame
        testButton.setSize(100,20);
        testButton.setVisible(true);
        add(testButton);

        //Adds a component listener with instructions that define the button location in a way, that makes it always
        //appear in the center, even when the user resizes the frame
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int x = getWidth()/2 - testButton.getWidth()/2;
                int y = getHeight()/2 - testButton.getHeight()/2;
                testButton.setLocation(x,y);
            }
        });
    }
    */

}
