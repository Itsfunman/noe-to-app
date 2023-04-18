package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Toolbar extends JToolBar {

    private JButton goCapacityButton;
    private JButton goOccupancyButton;
    private JButton goHotelEditButton;
    private JButton goOccupancyEditButton;
    private JFrame frame;

    public Toolbar(JFrame frame) {
        super();

        this.frame = frame;

        setLocation(0,0);
        setLayout(new FlowLayout());
        setVisible(true);
        setSize(frame.getWidth(), 30);

        InitGoCapacityButton();
        InitGoOccupancyButton();
        InitGoHotelEditButton();
        InitGoOccupancyEditButton();

    }

    private void InitGoOccupancyEditButton(){
        goOccupancyEditButton = new JButton("EDIT OCCUPANCY");
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
        goHotelEditButton = new JButton("EDIT HOTELS");
        goHotelEditButton.setVisible(true);

        add(goHotelEditButton);

        goHotelEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(frame instanceof JFrame){
                    frame.dispose();
                }
                try {
                    frame = new HotelEditFrame("NOE-TO");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
    }

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

}

