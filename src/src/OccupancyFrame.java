package src;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Calendar;

public class OccupancyFrame extends JFrame {

    private OccupancyTable occupancyTable;
    private Toolbar toolbar;

    //Creates interface to choose a date
    private JLabel [] labels = new JLabel[3];
    private String [] labelNames = {"Hotel:", "Jahr:", "Monat:"};
    private JComboBox<String> hotelChoice;
    private String [] hotelOptions;
    private JComboBox<Integer> yearChoice;
    private JComboBox<Month> monthChoice;


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
        InitSelector();

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
        occupancyTable.setLocation(100, 60);
        occupancyTable.setVisible(true);
        add(occupancyTable);

        // Set the width of the JScrollPane to 3/4 of the window width and the height to equal the combined height of
        //the rows times 1.5 (don´t ask why, I don´t know
        double tableWidth = getWidth() * 0.75;
        double tableHeight = occupancyTable.getTable().getRowHeight() * (occupancyTable.getTable().getRowCount() + 1.5);
        occupancyTable.setSize((int) tableWidth, (int) tableHeight);

    }

    private void InitSelector() {
        InitLabels();
        InitHotelChoice();
        InitYearChoice();
        InitMonthChoice();
    }

    private void InitLabels(){

        for (int i = 0; i < labels.length; i++ ){
            JLabel label = new JLabel(labelNames[i]);

            label.setBounds(((getWidth()/3) * (i + 1)) - 210, 35, 60, 20);
            label.setVisible(true);

            add(label);

            //Must be done this way because we can´t use int i in the addComponentAdapter
            int pos = i;

            label.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    label.setLocation(((getWidth()/3) * (pos + 1)) - 210, 35);
                }
            });


        }
    }

    private void InitHotelChoice(){

        hotelOptions = new String[Hotel.hotels.size()];
        fillHotelOptions();
        hotelChoice = new JComboBox<>(hotelOptions);
        hotelChoice.setBounds((getWidth()/3) - 150, 35, 110, 20);
        add(hotelChoice);

        hotelChoice.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                hotelChoice.setLocation(((getWidth()/3) - 150), 35);
            }
        });

    }

    private void InitYearChoice(){

        ArrayList<Integer> years = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        for (int i = 2000; i <= currentYear; i++){
            years.add(i);
        }

        yearChoice = new JComboBox<>(years.toArray(new Integer[0]));
        yearChoice.setBounds((getWidth()/3) * 2 - 150, 35, 110, 20);
        add(yearChoice);

        yearChoice.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                yearChoice.setLocation((getWidth()/3) * 2 - 150, 35);
            }
        });
    }

    private void InitMonthChoice(){

        Month[] months = Month.values();

        monthChoice = new JComboBox<>(months);


        monthChoice.setBounds((getWidth()/3) * 3 - 150, 35, 110, 20);
        add(monthChoice);

        monthChoice.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                monthChoice.setLocation((getWidth()/3) * 3 - 150, 35);
            }
        });

    }

    private void fillHotelOptions(){

        for (int i = 0; i < hotelOptions.length; i++){
            hotelOptions[i] = Hotel.hotels.get(i).getHotelName();
        }
    }
}
