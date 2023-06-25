package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Toolbar extends JToolBar {

    private JLabel logo;
    private JButton goCapacityButton;
    private JButton goOccupancyButton;
    private JButton goHotelEditButton;
    private JButton goOccupancyEditButton;
    private JButton goExportButton;
    private JFrame frame;

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


    private void InitGoOccupancyEditButton(){
        goOccupancyEditButton = new JButton("BELEGUNG BEARBEITEN");
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

    private void InitGoHotelEditButton() {
        goHotelEditButton = new JButton("HOTELS BEARBEITEN");
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

    private void InitGoCapacityButton(){

        goCapacityButton = new JButton("KAPAZITÄT");
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

    private void InitGoOccupancyButton(){

        goOccupancyButton = new JButton("BELEGUNG");
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

    private void InitGoExportButton(){

        goExportButton = new JButton("EXPORTIEREN");
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

