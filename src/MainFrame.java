package src;

import javax.swing.*;
import java.awt.*;

//Gets originally initialized in the LoginFrame
public class MainFrame extends JFrame {

    public MainFrame(String title) {
        super(title);

        setSize(800,500);
        setResizable(true);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


    }
}
