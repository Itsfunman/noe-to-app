package src;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.io.*;
import java.util.Objects;

/*
UP NEXT:
 - add a neat way to edit months
 - find a way to safe and load transactional data
 - add hotelID to hotelData
 */
public class OccupancyEditFrame extends JFrame {

    private String currentHotel = Hotel.hotels.get(0).getHotelName();
    private int currentYear = 0;
    private String currentMode = "BedOccupancy";

    private JToolBar toolbar;

    private final JLabel [] labels = new JLabel[3];
    private final String [] labelNames = {"Jahr:", "Hotel:", "Mode:"};

    private JComboBox<String> hotelChoice;
    private Hotel [] hotelOptions;

    private JComboBox<String> modeChoice;
    private final String [] modeOptions = {"Bed Occupancy", "Room Occupancy"};

    private JComboBox<Integer> yearChoice;

    private JTextField [] monthInput;

    private JButton goButton;
    private JButton updateButton;

    public OccupancyEditFrame(String title){
        super(title);

        setSize(800,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(null);
        setLocationRelativeTo(null);

        InitToolbar();
        InitSelector();
        InitButtons();
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

    private void InitSelector() {
        InitHotelChoice();
        InitYearChoice();
        InitModeChoice();
        InitLabels();
        InitMonthChoice();
    }

    private void InitButtons(){
        InitGoButton();
        InitUpdateButton();
    }

    private void InitLabels(){

        for (int i = 0; i < labels.length; i++ ){
            JLabel label = new JLabel(labelNames[i]);

            label.setBounds((getWidth()/2) - 300 + (i * 200), 35, 60, 20);
            label.setVisible(true);

            add(label);

            //Must be done this way because we can´t use int i in the addComponentAdapter
            int pos = i;

            label.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    label.setLocation((getWidth()/2) - 300 + (pos * 200), 35);
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
        hotelChoice.setBounds((getWidth()/2) - 240, 35, 130, 20);
        add(hotelChoice);

        hotelChoice.addActionListener(e -> {
            setCurrentHotel(Objects.requireNonNull(hotelChoice.getSelectedItem()).toString());
            InitSelector();
            InitButtons();
        });

        hotelChoice.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                hotelChoice.setLocation((getWidth()/2) - 240, 35);
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
        yearChoice.setBounds((getWidth()/2) - 40, 35, 130, 20);
        add(yearChoice);

        yearChoice.addActionListener(e -> {
            setCurrentYear(currentYear = yearChoice.getSelectedIndex());
            InitSelector();
            InitButtons();
        });

        yearChoice.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                yearChoice.setLocation((getWidth()/2) - 40, 35);
            }
        });
    }

    private void InitModeChoice(){

        modeChoice = new JComboBox<>(modeOptions);
        modeChoice.setBounds((getWidth()/2) + 160, 35, 130, 20);
        add(modeChoice);

        modeChoice.addActionListener(e -> {
            setCurrentMode(Objects.requireNonNull(modeChoice.getSelectedItem()).toString().replaceAll(" ", ""));
            System.out.println(currentMode);
            InitSelector();
            InitButtons();
        });

        modeChoice.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                modeChoice.setLocation((getWidth()/2) + 160, 35);
            }
        });

    }

    private void InitMonthChoice(){

        Month[] months = Month.values();
        int rowIncrease = 1;

        monthInput = new JTextField[12];

        for (int i = 0; i < months.length; i++) {
            if (i % 6 == 0) {
                rowIncrease++;
            }
            createLabel(i, rowIncrease, months);
            createInputField(i, rowIncrease);
        }


    }

    private void InitGoButton(){

        goButton = new JButton("GO");
        goButton.setBounds(getWidth()/2 - 110, 80, 100, 20);
        add(goButton);

        goButton.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                goButton.setLocation(getWidth()/2 - 110, 80);
            }
        });

        goButton.addActionListener(e -> {
            InitSelector();
            InitButtons();
        });

    }

    private void InitUpdateButton(){

        updateButton = new JButton("UPDATE");
        updateButton.setBounds(getWidth()/2 + 10, 80, 100, 20);
        add(updateButton);

        updateButton.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateButton.setLocation(getWidth()/2 + 10, 80);
            }
        });

    }

    private void fillHotelOptions(){

        for (int i = 0; i < hotelOptions.length; i++){
            hotelOptions[i] = Hotel.hotels.get(i);
        }
    }

    private void createLabel(int i, int rowIncrease, Month [] months){
        JLabel label = new JLabel(months[i].name());

        int deducter = 0;

        if (i >= 6){
            deducter = 600;
        }

        label.setBounds(100 * (i + 1) - deducter,60 * rowIncrease,80,20);
        add(label);

        int finalDeducter = deducter;

        label.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                label.setLocation(100 * (i + 1) - finalDeducter,60 * rowIncrease);
            }
        });

    }

    private void createInputField(int i, int rowIncrease){

        JTextField textField = new JTextField();

        int deducter = 0;

        if (i >= 6){
            deducter = 600;
        }

        textField.setBounds(100 * (i + 1) - deducter,60 * rowIncrease + 20,80,20);

        monthInput[i] = textField;
        fillField(monthInput, i);
        add(textField);

        int finalDeducter = deducter;

        textField.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                textField.setLocation(100 * (i + 1) - finalDeducter,60 * rowIncrease + 20);
            }
        });

    }

    private void fillField(JTextField[] monthInput, int i){

        int currentRow = 0;

        String fileName = "data/" + getCurrentHotel() + getCurrentMode() + ".txt";
        String [] lineContent = new String[12];

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))){
            String line;
            while((line = br.readLine()) != null){
                if(currentRow == getCurrentYear()){
                    line = line.replaceAll(",", "");
                    for (int k = 0; k < lineContent.length; k++){
                        lineContent[k] = line.substring(k,k + 1);
                    }
                    break;
                }
                currentRow++;
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        monthInput[i].setText(lineContent[i]);

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

    public String getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(String currentMode) {
        this.currentMode = currentMode;
    }
}
