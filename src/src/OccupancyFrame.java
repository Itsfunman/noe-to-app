package src;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

/*

As senior user of NOE-TO
I want to get a summary about the transactional data on the screen
I can analyze the number of room occupancy and bed occupancy per month
Selection shall be possible for one hotel and/or year and/or category (all hotels higher or equal) and/or month

 */

public class OccupancyFrame extends JFrame {

    private OccupancyTable occupancyTable;
    private Toolbar toolbar;

    //Creates interface to choose a date
    private JLabel [] labels = new JLabel[3];
    private String [] labelNames = {"Hotel:", "Jahr:", "Monat:"};
    private JComboBox<String> hotelChoice;
    private Hotel [] hotelOptions;
    private JComboBox<Integer> yearChoice;
    private JComboBox<String> modeChoice;
    private final String [] modeOptions = {"Bed Occupancy", "Room Occupancy"};

    private String currentHotel;
    private int currentYear;
    private int currentMode;


    public OccupancyFrame(String title) {
        super(title);

        setSize(800, 500);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        InitToolbar();
        InitOccupancyTable();
        InitDetailedMode();

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

    private void InitOccupancyTable() {
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

    private void InitDetailedMode() {
        InitDMLabels();
        InitHotelChoice();
        InitYearChoice();
        InitModeChoice();
    }

    private void InitDMLabels(){

        for (int i = 0; i < labels.length; i++ ){
            JLabel label = new JLabel(labelNames[i]);

            label.setBounds(((getWidth()/3) * (i + 1)) - 200, 250, 60, 20);
            label.setVisible(true);

            add(label);

            //Must be done this way because we can´t use int i in the addComponentAdapter
            int pos = i;

            label.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    label.setLocation(((getWidth()/3) * (pos + 1)) - 200, 250);
                }
            });


        }
    }

    private void InitHotelChoice(){

        hotelOptions = new Hotel[Hotel.hotels.size()];
        hotelChoice = new JComboBox<>();

        fillHotelOptions();

        for (Hotel hotelOption : hotelOptions) {
            String h = hotelOption.getHotelName();
            hotelChoice.addItem(h);
        }
        hotelChoice.setBounds((getWidth()/2) - 260, 250, 130, 20);
        add(hotelChoice);

        hotelChoice.addActionListener(e -> {
            setCurrentHotel(Objects.requireNonNull(hotelChoice.getSelectedItem()).toString());
        });

        hotelChoice.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                hotelChoice.setLocation((getWidth()/2) - 260, 250);
            }
        });

    }

    private void InitYearChoice(){

        ArrayList<Integer> years = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);

        for (int i = 2000; i <= thisYear; i++){
            years.add(i);
        }

        yearChoice = new JComboBox<>(years.toArray(new Integer[0]));
        yearChoice.setBounds((getWidth()/2), 250, 130, 20);
        add(yearChoice);

        yearChoice.addActionListener(e -> {
            setCurrentYear(currentYear = yearChoice.getSelectedIndex());
        });

        yearChoice.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                yearChoice.setLocation((getWidth()/2), 250);
            }
        });
    }

    private void InitModeChoice(){

        modeChoice = new JComboBox<>(modeOptions);
        modeChoice.setBounds((getWidth()/2) + 240, 250, 130, 20);
        add(modeChoice);

        modeChoice.addActionListener(e -> {
            //setCurrentMode(Objects.requireNonNull(modeChoice.getSelectedItem()).toString().replaceAll(" ", ""));
            setCurrentMode(modeChoice.getSelectedIndex());
        });

        modeChoice.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                modeChoice.setLocation((getWidth()/2) + 240, 250);
            }
        });

    }

    private void fillHotelOptions(){

        for (int i = 0; i < hotelOptions.length; i++){
            hotelOptions[i] = Hotel.hotels.get(i);
        }
    }

    public String getCurrentHotel() {
        return currentHotel;
    }

    public void setCurrentHotel(String currentHotel) {
        this.currentHotel = currentHotel;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }

    public int getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(int currentMode) {
        this.currentMode = currentMode;
    }
}
