package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//Gets originally initialized in the LoginFrame
public class MainFrame extends JFrame {

    private JScrollPane hotelCapacity = new JScrollPane();
    private int frameWidth = 800;
    private int frameHeight = 500;

    private JButton testButton = new JButton("Test");

    public MainFrame(String title) {
        super(title);

        setSize(frameWidth,frameHeight);
        setResizable(true);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        //InitTestButton();

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
