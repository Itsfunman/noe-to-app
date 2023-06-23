package src;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.io.*;
import java.util.List;
import java.util.Objects;

/*
UP NEXT:
 - add a neat way to edit months (ADD FUNCTIONING UPDATE BUTTON!!!)
 */

public class OccupancyEditFrame_OLD extends JFrame {

    private String currentHotel = Hotel.hotels.get(0).getHotelName();
    private int currentYear;
    private int currentMode;

    private JToolBar toolbar;

    private final JLabel [] labels = new JLabel[3];
    private final String [] labelNames = {"Jahr:", "Hotel:", "Mode:"};

    private JComboBox<String> hotelChoice;
    private Hotel [] hotelOptions;

    private JComboBox<String> modeChoice;
    private final String [] modeOptions = {"Bed Occupancy", "Room Occupancy"};

    private JComboBox<Integer> yearChoice;

    private JTextField [] monthInput;

    private JButton updateButton;

    private int editedRowIndex;

    public OccupancyEditFrame_OLD(String title){
        super(title);

        setSize(800,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(null);
        setLocationRelativeTo(null);

        InitToolbar();
        InitSelector();
        InitUpdateButton();
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
            InitMonthChoice();
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
            InitMonthChoice();
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
            //setCurrentMode(Objects.requireNonNull(modeChoice.getSelectedItem()).toString().replaceAll(" ", ""));
            setCurrentMode(modeChoice.getSelectedIndex());
            InitMonthChoice();
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

    private void InitUpdateButton(){

        updateButton = new JButton("UPDATE");
        updateButton.setSize(100,20);
        updateButton.setLocation((getWidth() - updateButton.getWidth())/2, 80);
        add(updateButton);

        updateButton.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateButton.setLocation((getWidth() - updateButton.getWidth())/2, 80);
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rewrite();
                //Currently data is only correctly updated when hitting the enter key, this needs to change
                //Maybe we can somehow call the correct fillFIeld methods from here?


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

        String [] lineContent  = findData();

        monthInput[i].setText(lineContent[i]);

        monthInput[i].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateData(monthInput);
            }
        });

    }

    private void updateData(JTextField[]monthInput){

        String newLineContent = "";

        for (int i = 0; i < monthInput.length; i++){
            String text = monthInput[i].getText();
            System.out.println("text[" + i + "] = " + text);
            newLineContent = newLineContent + text + ",";
        }


        String mode = "";

        if (getCurrentMode() == 0){
            mode = "BedOccupancy";
        } else {
            mode = "RoomOccupancy";
        }
        String fileName = "oldData/" + getCurrentHotel() + mode + ".txt";

        System.out.println(fileName + getEditedRowIndex());
        // Update the hotel information in the hotelData.txt file
        boolean caughtException = false;
        while (!caughtException){
            try {
                File file = new File(fileName);
                List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
                lines.set(getEditedRowIndex(), newLineContent);
                Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
                caughtException = true;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    private String[] findData() {
        setEditedRowIndex(0);

        String mode = getCurrentMode() == 0 ? "BedOccupancy" : "RoomOccupancy";
        String fileName = "oldData/" + getCurrentHotel() + mode + ".txt";
        String[] lineContent = new String[12];

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (getEditedRowIndex() == getCurrentYear()) {

                    String[] tokens = line.split(",");
                    for (int k = 0; k < lineContent.length; k++) {
                        if (tokens.length > k) {
                            lineContent[k] = tokens[k];
                        } else {
                            lineContent[k] = "";
                        }
                    }

                    break;
                }
                setEditedRowIndex(getEditedRowIndex() + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lineContent;
    }

    private void rewrite(){

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

    public int getEditedRowIndex() {
        return editedRowIndex;
    }

    public void setEditedRowIndex(int editedRowIndex) {
        this.editedRowIndex = editedRowIndex;
    }
}
