package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class used for the logo and the toolbar
 */
public class Toolbar extends JToolBar {

    private JLabel logo;
    private JButton goCapacityButton;
    private JButton goOccupancyButton;
    private JButton goHotelEditButton;
    private JButton goOccupancyEditButton;
    private JButton goExportButton;
    private JFrame frame;

    /**
     * Initializes the toolbar in the given frame
     * @param frame
     */
    public Toolbar(JFrame frame) {
        super();

        this.frame = frame;

        setLocation(66,0);
        setLayout(new FlowLayout());
        setVisible(true);
        setSize(frame.getWidth() - 66, 30);

        //Add elements from left to right
        InitLogo();
        InitGoCapacityButton();
        InitGoOccupancyButton();
        InitGoHotelEditButton();
        InitGoOccupancyEditButton();
        InitGoExportButton();


    }

    /**
     * Draws the logo
     */
    private void InitLogo() {
        ImageIcon icon = new ImageIcon("assets/NOETOLogo.jpg"); // Select image from files
        Image img = icon.getImage(); // Assign icon to image instance
        Image newImg = img.getScaledInstance(66, 30, Image.SCALE_SMOOTH); // Scale image
        ImageIcon newIcon = new ImageIcon(newImg); // Assign scaled image to icon
        logo = new JLabel(newIcon); // Create JLabel using ImageIcon
        logo.setBounds(0, 0, 66, 30); // Set position and size manually
        logo.setVisible(true); // Set visible
        this.frame.add(logo); // Add logo to frame
    }

    /**
     * Adds the Occupancy Edit chpice
     */
    private void InitGoOccupancyEditButton(){
        goOccupancyEditButton = new JButton("OCCUPANCYC EDIT");
        goOccupancyEditButton.setVisible(true);

        add(goOccupancyEditButton);

        goOccupancyEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(frame instanceof JFrame){
                    frame.dispose();
                }
                frame = new OccupancyEditFrame("NOE-TO");

            }
        });
    }

    /**
     * Adds the Hotel Edit choice
     */
    private void InitGoHotelEditButton() {
        goHotelEditButton = new JButton("HOTELS EDIT");
        goHotelEditButton.setVisible(true);

        add(goHotelEditButton);

        goHotelEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(frame instanceof JFrame){
                    frame.dispose();
                }
                frame = new HotelEditFrame("NOE-TO");

            }
        });
    }

    /**
     * Adds the capacity choice
     */
    private void InitGoCapacityButton(){

        goCapacityButton = new JButton("CAPACITY");
        goCapacityButton.setVisible(true);

        add(goCapacityButton);

        goCapacityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(frame instanceof JFrame){
                    frame.dispose();
                }
                frame = new CapacityFrame("NOE-TO");

            }
        });

    }

    /**
     * Adds the occupancy choice
     */
    private void InitGoOccupancyButton(){

        goOccupancyButton = new JButton("OCCUPANCY");
        goOccupancyButton.setVisible(true);

        add(goOccupancyButton);

        goOccupancyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(frame instanceof JFrame){
                    frame.dispose();
                }
                frame = new OccupancyFrame("NOE-TO");
            }
        });
    }

    /**
     * Adds the export choice
     */
    private void InitGoExportButton(){

        goExportButton = new JButton("EXPORT");
        goExportButton.setVisible(true);

        add(goExportButton);

        goExportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(frame instanceof JFrame){
                    frame.dispose();
                }
                frame = new ExportFrame("NOE-TO");
            }
        });

    }
}

