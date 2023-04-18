package src;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


/*
UP NEXT:
 - add a neat way to edit months
 - maybe change away from a JTextField for the year
 - make a single class that adds the choice fields
 - find a way to safe and load transactional data
 - add hotelID to hotelData
 */
public class OccupancyEditFrame extends JFrame {

    private JToolBar toolbar;

    private JLabel [] labels = new JLabel[3];
    private String [] labelNames = {"Hotel:", "Jahr:", "Monat:"};

    private JComboBox<String> hotelChoice;
    private String [] hotelOptions;

    private JTextField yearChoice;

    public OccupancyEditFrame(String title){
        super(title);

        setSize(800,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(null);
        setLocationRelativeTo(null);

        InitToolbar();
        InitLabels();
        InitHotelChoice();
        InitYearChoice();

    }

    private void InitToolbar(){

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

    private void InitLabels(){

        for (int i = 0; i < labels.length; i++ ){
            JLabel label = new JLabel(labelNames[i]);

            label.setBounds((getWidth()/2) - 60, ((getHeight()/2) - 35) + (i * 25), 100, 20);
            label.setVisible(true);

            add(label);

            //Must be done this way because we can´t use int i in the addComponentAdapter
            int pos = i;

            label.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    label.setLocation(getWidth() - 60, (getHeight() - 35) + (pos * 25));
                }
            });


        }
    }

    private void InitHotelChoice(){

        hotelOptions = new String[Hotel.hotels.size()];
        fillHotelOptions();
        hotelChoice = new JComboBox<>(hotelOptions);
        hotelChoice.setBounds((getWidth()/2), ((getHeight()/2) - 35), 150, 20);
        add(hotelChoice);

        hotelChoice.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                hotelChoice.setBounds((getWidth()/2), ((getHeight()/2) - 35), 150, 20);
            }
        });

    }

    private void InitYearChoice(){

        yearChoice = new JTextField();
        yearChoice.setBounds((getWidth()/2), ((getHeight()/2) - 10), 150, 20);
        add(yearChoice);

        yearChoice.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                yearChoice.setBounds((getWidth()/2), ((getHeight()/2)), 150, 20);
            }
        });
    }

    private void fillHotelOptions(){

        for (int i = 0; i < hotelOptions.length; i++){
            hotelOptions[i] = Hotel.hotels.get(i).getHotelName();
        }
    }
}
