package src;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/*

As senior user of NOE-TO
I want to get a summary about the transactional data on the screen
I can analyze the number of room occupancy and bed occupancy per month
Selection shall be possible for one hotel and/or year and/or category (all hotels higher or equal) and/or month

 */

public class OccupancyFrame extends JFrame {

    String [] columnNames = new String[]{"JAHR/MONAT", "ANZAHL", "RÄUME", "BENUTZT", "BETTEN", "BENUTZT"};

    private Toolbar toolbar;


    private CustomTable occupancyTable;
    public static String[][] selectedOccupancyData;

    private int startYear;
    private int endYear;
    private int startMonth;
    private int endMonth;
    private String minCategory;
    private String maxCategory;
    private Hotel hotel;

    private JCheckBox multiHotel;
    private JLabel multiHotelLabel;

    private JComboBox <String> hotelComboBox;
    private JLabel hotelLabel;

    private JComboBox <Integer> startYearBox;
    private JComboBox <Integer> endYearBox;
    private JLabel yearLabel;

    private JComboBox <Integer> startMonthBox;
    private JComboBox <Integer> endMonthBox;
    private JLabel monthLabel;

    private JComboBox <String> minCategoryBox;
    private JComboBox <String> maxCategoryBox;
    private JLabel categoryLabel;

    private JButton applyButton;

    private HashMap<String, Hotel> hotelMap = new HashMap<>();

    private int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    private ArrayList <Integer> years = new ArrayList<>();

    private Integer [] months = new Integer[12];

    private String [] categories = new String[5];

    public OccupancyFrame(String title){
        super(title);

        setSize(800, 500);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        InitToolbar();
        InitMultiHotelCheckBox();

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

    private void InitMultiHotelCheckBox(){

        multiHotel = new JCheckBox();
        multiHotel.setBounds((getWidth()/2) - 80, 250, 20,20);

        add(multiHotel);

        multiHotelLabel = new JLabel("MULTI-HOTEL MODUS");
        multiHotelLabel.setBounds((getWidth()/2) - 60, 250, 150,20);
        add(multiHotelLabel);

        InitHotelChoice();

        multiHotel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (multiHotel.isSelected()){
                    InitMultiChoice();
                } else {
                    InitHotelChoice();
                }
            }
        });

    }

    private void InitMultiChoice(){

        if (hotelComboBox != null){
            hotelComboBox.setVisible(false);
            hotelLabel.setVisible(false);

        }

        repaint();

        InitCategoryChoice();
        InitYearChoice();
        InitMonthChoice();
        InitApplyButton();

    }

    private void InitHotelChoice(){

        if (startYearBox != null){
            remove(startYearBox);
            remove(endYearBox);
            remove(yearLabel);

            remove(startMonthBox);
            remove(endMonthBox);
            remove(monthLabel);

            remove(minCategoryBox);
            remove(maxCategoryBox);
            remove(categoryLabel);

            remove(applyButton);
        }

        repaint();

        hotelComboBox = new JComboBox<>();

        for (Hotel hotel : Hotel.hotels){
            hotelComboBox.addItem(hotel.getHotelName());
            hotelMap.put(hotel.getHotelName(), hotel);
        }

        hotelComboBox.setBounds((getWidth()/2) - 60, 325, 160, 20);
        add(hotelComboBox);

        hotelLabel = new JLabel("HOTEL : ");
        hotelLabel.setBounds(getWidth()/2 - 110, 325, 60, 20);
        add(hotelLabel);

        InitYearChoice();
        InitMonthChoice();
        InitApplyButton2();

    }
    private void InitApplyButton2(){

        applyButton = new JButton("APPLY");
        applyButton.setBounds((getWidth()/2) - 45, 350, 90, 20);
        add(applyButton);

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                startYear = (int) startYearBox.getSelectedItem();
                endYear = (int) endYearBox.getSelectedItem();

                startMonth = (int) startMonthBox.getSelectedItem();
                endMonth = (int) endMonthBox.getSelectedItem();

                int hotelID = hotelMap.get(hotelComboBox.getSelectedItem()).getHotelID();

                try {
                    if (startYear < endYear) {
                        InitSingleOccupancyTable(startYear, endYear, startMonth, endMonth, hotelID);
                    } else if (startYear == endYear && startMonth <= endMonth && minCategory.length() <= maxCategory.length()){
                        InitMultiOccupancyTable(startYear, endYear, startMonth, endMonth, minCategory, maxCategory);
                    } else{
                        throw new IllegalArgumentException("Invalid value size!");
                    }
                } catch (IllegalArgumentException iae){
                    iae.printStackTrace();
                }

            }
        });

    }

    private void InitYearChoice(){

        for (int i = 2014; i <= currentYear; i++){
            years.add(i);
        }

        startYearBox = new JComboBox<>(years.toArray(new Integer[0]));
        startYearBox.setBounds((getWidth()/2) - 115, 275, 100,20);
        add(startYearBox);

        endYearBox = new JComboBox<>(years.toArray(new Integer[0]));
        endYearBox.setBounds((getWidth()/2) + 15, 275, 100,20);
        add(endYearBox);

        yearLabel = new JLabel("BIS");
        yearLabel.setBounds((getWidth()/2) - 10, 275, 20,20);
        add(yearLabel);

    }

    private void InitMonthChoice(){

        for (int i = 1; i <= months.length; i++){
            months[i-1] = i;
        }

        startMonthBox = new JComboBox<>(months);
        startMonthBox.setBounds((getWidth()/2) - 115, 300, 100,20);
        add(startMonthBox);

        endMonthBox = new JComboBox<>(months);
        endMonthBox.setBounds((getWidth()/2) + 15, 300, 100,20);
        add(endMonthBox);

        monthLabel = new JLabel("BIS");
        monthLabel.setBounds((getWidth()/2) - 10, 300, 20,20);
        add(monthLabel);

    }

    private void InitCategoryChoice(){

        categories = new String[]{"*", "**", "***", "****", "*****"};

        minCategoryBox = new JComboBox<>(categories);
        minCategoryBox.setBounds((getWidth()/2) - 115, 325, 100,20);
        add(minCategoryBox);

        maxCategoryBox = new JComboBox<>(categories);
        maxCategoryBox.setBounds((getWidth()/2) + 15, 325, 100,20);
        add(maxCategoryBox);

        categoryLabel = new JLabel("BIS");
        categoryLabel.setBounds((getWidth()/2) - 10, 325, 20,20);
        add(categoryLabel);

    }

    private void InitApplyButton(){

        applyButton = new JButton("APPLY");
        applyButton.setBounds((getWidth()/2) - 45, 350, 90, 20);
        add(applyButton);

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                startYear = (int) startYearBox.getSelectedItem();
                endYear = (int) endYearBox.getSelectedItem();

                startMonth = (int) startMonthBox.getSelectedItem();
                endMonth = (int) endMonthBox.getSelectedItem();

                minCategory = (String) minCategoryBox.getSelectedItem();
                maxCategory = (String) maxCategoryBox.getSelectedItem();

                try {
                    if (startYear < endYear && minCategory.length() <= maxCategory.length()) {
                        InitMultiOccupancyTable(startYear, endYear, startMonth, endMonth, minCategory, maxCategory);
                    } else if (startYear == endYear && startMonth <= endMonth && minCategory.length() <= maxCategory.length()){
                        InitMultiOccupancyTable(startYear, endYear, startMonth, endMonth, minCategory, maxCategory);
                    } else{
                        throw new IllegalArgumentException("Invalid value size!");
                    }
                } catch (IllegalArgumentException iae){
                    iae.printStackTrace();
                }

            }
        });

    }

    private void InitSingleOccupancyTable(int startYear, int endYear, int startMonth, int endMonth, int hotelID){

        if (occupancyTable != null){
            remove(occupancyTable);
        }

        selectedOccupancyData = fetchSingleHotelData(startYear, endYear, startMonth, endMonth, hotelID);

        occupancyTable = new CustomTable(selectedOccupancyData, columnNames);
        occupancyTable.setLocation(100, 60);
        occupancyTable.setVisible(true);
        add(occupancyTable);

        // Set the width of the JScrollPane to 3/4 of the window width and the height to equal the combined height of
        //the rows times 1.5 (don´t ask why, I don´t know)
        double tableWidth = getWidth() * 0.75;
        double tableHeight = occupancyTable.getTable().getRowHeight() * (11.5);
        occupancyTable.setSize((int) tableWidth, (int) tableHeight);

    }

    private String [][] fetchSingleHotelData(int startYear, int endYear, int startMonth, int endMonth, int hotelID){

        int rowNumber = getRowCount(startYear, endYear, startMonth, endMonth);
        String [][] workData = new String[rowNumber][columnNames.length];

        //FORMAT:
        //"YEAR/MONTH", "AMOUNT SELECTED", "ROOMS", "USEDROOMS", "BEDS", "USEDBEDS"

        ArrayList<Integer> hotelIDs = new ArrayList<>();
        hotelIDs.add(hotelID);
        int[] otherValues = otherValues(hotelIDs);


        //Initialize rooms and beds used
        try (FileReader fileReader = new FileReader("data/occupancies.txt");
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");

                // Skip invalid lines or lines with insufficient values
                if (values.length != 7) {
                    continue;
                }


                // Check if the line matches the given hotelID
                if (hotelID == Integer.parseInt(values[0])) {

                    if (Integer.parseInt(values[5]) >= startYear && Integer.parseInt(values[5]) <= endYear) {
                        if (Integer.parseInt(values[5]) == startYear) {
                            if (Integer.parseInt(values[6]) >= startMonth) {
                                int rowIndex = ((Integer.parseInt(values[5]) - startYear) * 12) + (Integer.parseInt(values[6]) - startMonth);

                                // Check if the row index is within the valid range
                                if (rowIndex >= 0 && rowIndex < rowNumber) {
                                    if (workData[rowIndex][3] == null) {
                                        workData[rowIndex][3] = "0";
                                    }
                                    if (workData[rowIndex][5] == null) {
                                        workData[rowIndex][5] = "0";
                                    }

                                    // Update room and bed values
                                    workData[rowIndex][3] = String.valueOf(Integer.parseInt(workData[rowIndex][3]) + Integer.parseInt(values[2]));
                                    workData[rowIndex][5] = String.valueOf(Integer.parseInt(workData[rowIndex][5]) + Integer.parseInt(values[4]));
                                }
                            }
                        } else if (Integer.parseInt(values[5]) == endYear) {
                            if (Integer.parseInt(values[6]) <= endMonth) {
                                int rowIndex = ((Integer.parseInt(values[5]) - startYear) * 12) + (Integer.parseInt(values[6]) - startMonth);

                                // Check if the row index is within the valid range
                                if (rowIndex >= 0 && rowIndex < rowNumber) {
                                    if (workData[rowIndex][3] == null) {
                                        workData[rowIndex][3] = "0";
                                    }
                                    if (workData[rowIndex][5] == null) {
                                        workData[rowIndex][5] = "0";
                                    }

                                    // Update room and bed values
                                    workData[rowIndex][3] = String.valueOf(Integer.parseInt(workData[rowIndex][3]) + Integer.parseInt(values[2]));
                                    workData[rowIndex][5] = String.valueOf(Integer.parseInt(workData[rowIndex][5]) + Integer.parseInt(values[4]));
                                }
                            }
                        } else {
                            int rowIndex = ((Integer.parseInt(values[5]) - startYear) * 12) + (Integer.parseInt(values[6]) - startMonth);

                            // Check if the row index is within the valid range
                            if (rowIndex >= 0 && rowIndex < rowNumber) {
                                if (workData[rowIndex][3] == null) {
                                    workData[rowIndex][3] = "0";
                                }
                                if (workData[rowIndex][5] == null) {
                                    workData[rowIndex][5] = "0";
                                }

                                // Update room and bed values
                                workData[rowIndex][3] = String.valueOf(Integer.parseInt(workData[rowIndex][3]) + Integer.parseInt(values[2]));
                                workData[rowIndex][5] = String.valueOf(Integer.parseInt(workData[rowIndex][5]) + Integer.parseInt(values[4]));
                            }
                        }
                    }


                }


            }


        } catch (IOException e) {
            e.printStackTrace();
            selectedOccupancyData = new String[0][7]; // Empty array for new file
        }


        int monthCount = startMonth - 1;
        int yearCount = startYear;
        //Initialize the rest
        for (int i = 0; i < rowNumber; i++){

            //Initialize Date
            if (monthCount % 12 == 0){
                yearCount++;
                monthCount = 0;
            }

            monthCount++;

            workData[i][0] = yearCount + "/" + monthCount;

            //Initialize amount selected
            workData[i][1] = "1";

            //Initialize room number
            workData[i][2] = String.valueOf(otherValues[0]);

            //Initialize bed number
            workData[i][4] = String.valueOf(otherValues[1]);
        }

        return workData;
    }

    private void InitMultiOccupancyTable(int startYear, int endYear, int startMonth, int endMonth, String minCategory, String maxCategory){

        if (occupancyTable != null){
            remove(occupancyTable);
        }

        selectedOccupancyData = fetchMultiHotelData(startYear, endYear, startMonth, endMonth, minCategory, maxCategory);

        occupancyTable = new CustomTable(selectedOccupancyData, columnNames);
        occupancyTable.setLocation(100, 60);
        occupancyTable.setVisible(true);
        add(occupancyTable);

        // Set the width of the JScrollPane to 3/4 of the window width and the height to equal the combined height of
        //the rows times 1.5 (don´t ask why, I don´t know)
        double tableWidth = getWidth() * 0.75;
        double tableHeight = occupancyTable.getTable().getRowHeight() * (11.5);
        occupancyTable.setSize((int) tableWidth, (int) tableHeight);

    }

    private String [][] fetchMultiHotelData(int startYear, int endYear, int startMonth, int endMonth, String minCategory, String maxCategory){

        int rowNumber = getRowCount(startYear, endYear, startMonth, endMonth);
        String [][] workData = new String[rowNumber][columnNames.length];

        //FORMAT:
        //"YEAR/MONTH", "AMOUNT SELECTED", "ROOMS", "USEDROOMS", "BEDS", "USEDBEDS"

        ArrayList<Integer> hotelIDs = getHotelIDs(minCategory, maxCategory);

        int [] otherValues = otherValues(hotelIDs);

        //Initialize rooms and beds used
        try (FileReader fileReader = new FileReader("data/occupancies.txt");
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");

                // Skip invalid lines or lines with insufficient values
                if (values.length != 7) {
                    continue;
                }


                // Check if the line matches the given hotelID
                if (hotelIDs.contains(Integer.parseInt(values[0]))) {

                    if (Integer.parseInt(values[5]) >= startYear && Integer.parseInt(values[5]) <= endYear) {
                        if (Integer.parseInt(values[5]) == startYear) {
                            if (Integer.parseInt(values[6]) >= startMonth) {
                                int rowIndex = ((Integer.parseInt(values[5]) - startYear) * 12) + (Integer.parseInt(values[6]) - startMonth);

                                // Check if the row index is within the valid range
                                if (rowIndex >= 0 && rowIndex < rowNumber) {
                                    if (workData[rowIndex][3] == null) {
                                        workData[rowIndex][3] = "0";
                                    }
                                    if (workData[rowIndex][5] == null) {
                                        workData[rowIndex][5] = "0";
                                    }

                                    // Update room and bed values
                                    workData[rowIndex][3] = String.valueOf(Integer.parseInt(workData[rowIndex][3]) + Integer.parseInt(values[2]));
                                    workData[rowIndex][5] = String.valueOf(Integer.parseInt(workData[rowIndex][5]) + Integer.parseInt(values[4]));
                                }
                            }
                        } else if (Integer.parseInt(values[5]) == endYear) {
                            if (Integer.parseInt(values[6]) <= endMonth) {
                                int rowIndex = ((Integer.parseInt(values[5]) - startYear) * 12) + (Integer.parseInt(values[6]) - startMonth);

                                // Check if the row index is within the valid range
                                if (rowIndex >= 0 && rowIndex < rowNumber) {
                                    if (workData[rowIndex][3] == null) {
                                        workData[rowIndex][3] = "0";
                                    }
                                    if (workData[rowIndex][5] == null) {
                                        workData[rowIndex][5] = "0";
                                    }

                                    // Update room and bed values
                                    workData[rowIndex][3] = String.valueOf(Integer.parseInt(workData[rowIndex][3]) + Integer.parseInt(values[2]));
                                    workData[rowIndex][5] = String.valueOf(Integer.parseInt(workData[rowIndex][5]) + Integer.parseInt(values[4]));
                                }
                            }
                        } else {
                            int rowIndex = ((Integer.parseInt(values[5]) - startYear) * 12) + (Integer.parseInt(values[6]) - startMonth);

                            // Check if the row index is within the valid range
                            if (rowIndex >= 0 && rowIndex < rowNumber) {
                                if (workData[rowIndex][3] == null) {
                                    workData[rowIndex][3] = "0";
                                }
                                if (workData[rowIndex][5] == null) {
                                    workData[rowIndex][5] = "0";
                                }

                                // Update room and bed values
                                workData[rowIndex][3] = String.valueOf(Integer.parseInt(workData[rowIndex][3]) + Integer.parseInt(values[2]));
                                workData[rowIndex][5] = String.valueOf(Integer.parseInt(workData[rowIndex][5]) + Integer.parseInt(values[4]));
                            }
                        }
                    }


                }


            }


        } catch (IOException e) {
            e.printStackTrace();
            selectedOccupancyData = new String[0][7]; // Empty array for new file
        }


        int monthCount = startMonth - 1;
        int yearCount = startYear;
        //Initialize the rest
        for (int i = 0; i < rowNumber; i++){

            //Initialize Date
            if (monthCount % 12 == 0){
                yearCount++;
                monthCount = 0;
            }

            monthCount++;

            workData[i][0] = yearCount + "/" + monthCount;

            //Initialize amount selected
            workData[i][1] = String.valueOf(hotelIDs.size());

            //Initialize room number
            workData[i][2] = String.valueOf(otherValues[0]);

            //Initialize bed number
            workData[i][4] = String.valueOf(otherValues[1]);
        }

        return workData;

    }

    private int getRowCount(int startYear, int endYear, int startMonth, int endMonth){

        int rowCount = 0;

        int lowMonth = 12 - startMonth + 1;
        int highMonth = endMonth;

        int lowYear = startYear - 2014;
        int highYear = endYear - 2014;
        int yearDiff = highYear - lowYear;


        if (yearDiff == 0){

            rowCount = endMonth - startMonth + 1;

        } else if (yearDiff == 1){

            rowCount += 12 - startMonth + 1;
            rowCount += endMonth;

        } else if (yearDiff >= 2){

            rowCount += 12 - startMonth + 1;
            rowCount += (yearDiff - 1) * 12;
            rowCount += endMonth;

        }

        System.out.println(rowCount);

        return rowCount;

    }

    private ArrayList<Integer> getHotelIDs(String minCategory, String maxCategory){

        ArrayList<Integer> hotelIDs = new ArrayList<>();

        int minLength = minCategory.length();
        int maxLength = maxCategory.length();

        try (FileReader fileReader = new FileReader("data/hotelData.txt");
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");

                // Skip invalid lines or lines with insufficient values
                if (values.length != 15) {
                    continue;
                }

                //Test whether hotel is of category of interest
                int catInt = values[2].length();

                if (catInt <= maxLength && catInt >= minLength){
                    hotelIDs.add(Integer.valueOf(values[0]));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            selectedOccupancyData = new String[0][15]; // Empty array for new file
        }

        return hotelIDs;
    }

    private int [] otherValues(ArrayList<Integer> hotelIDs){

        int [] otherValues = new int[]{0,0};

        try (FileReader fileReader = new FileReader("data/hotelData.txt");
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");

                // Skip invalid lines or lines with insufficient values
                if (values.length != 15) {
                    continue;
                }


                // Check if the line matches the given hotelID
                if (hotelIDs.contains(Integer.parseInt(values[0]))) {

                    otherValues[0] += Integer.parseInt(values[3]);
                    otherValues[1] += Integer.parseInt(values[4]);

                }


            }
        } catch (IOException e) {
            e.printStackTrace();
            selectedOccupancyData = new String[0][7]; // Empty array for new file
        }

        return otherValues;

    }

}
