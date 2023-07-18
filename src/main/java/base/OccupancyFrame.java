package base;



import objectClasses.Occupancy;
import sqlStuff.HotelDAO;
import sqlStuff.OccupancyDAO;
import tableClasses.CustomTable;
import objectClasses.Hotel;
import tableClasses.CustomTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/*

As senior user of NOE-TO
I want to get a summary about the transactional data on the screen
I can analyze the number of room occupancy and bed occupancy per month
Selection shall be possible for one hotel and/or year and/or category (all hotels higher or equal) and/or month

 */

/**
 * Class for the occupancy overview frame
 */
public class OccupancyFrame extends JFrame {

    String [] columnNamesMulti = {"YEAR", "MONTH", "AMOUNT", "ROOMS", "USED", "BEDS", "USED"};
    String [] columnNamesSingle = {"YEAR", "MONTH", "ROOMS", "USED", "BEDS", "USED"};
    private Toolbar toolbar;

    public static Object [][] selectedOccupancyData;

    private int startYear;
    private int endYear;
    private int startMonth;
    private int endMonth;
    private String minCategory;
    private String maxCategory;

    private JCheckBox multiHotel;
    private JLabel multiHotelLabel;

    private JComboBox <String> hotelComboBox;
    private JLabel hotelLabel;

    private JButton applyButton2;

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

    private CustomTable customTable;
    private CustomTableModel customTableModel;

    /**
     * Initializes the OccupancyFrame
     * @param title
     */
    public OccupancyFrame(String title){
        super(title);

        setSize(800, 500);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        //Set custom Icon
        ImageIcon icon = new ImageIcon("assets/NOETOLogo.jpg");
        setIconImage(icon.getImage());

        InitToolbar();
        InitMultiHotelCheckBox();

    }

    /**
     * Initializes the toolbar
     */
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

    /**
     * Allows mode selection
     */
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

    /**
     * initializes the boxes needed when working with multiple hotels
     */
    private void InitMultiChoice(){

        if (hotelComboBox != null){
            remove(startYearBox);
            remove(endYearBox);
            remove(yearLabel);

            remove(startMonthBox);
            remove(endMonthBox);
            remove(monthLabel);

            remove(hotelComboBox);
            remove(hotelLabel);

            remove(applyButton2);
        }

        repaint();

        InitCategoryChoice();
        InitYearChoice();
        InitMonthChoice();
        InitApplyButton();

    }

    /**
     * Initializes the single hotel choice mode
     */
    private void InitHotelChoice(){

        if (minCategoryBox != null){
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

    /**
     * Initializes applyButton2 for single hotel choice
     */
    private void InitApplyButton2(){

        applyButton2 = new JButton("CONFIRM");
        applyButton2.setBounds((getWidth()/2) - 60, 350, 120, 20);
        add(applyButton2);

        applyButton2.addActionListener(new ActionListener() {
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
                    } else if (startYear == endYear && startMonth <= endMonth){
                        InitSingleOccupancyTable(startYear, endYear, startMonth, endMonth, hotelID);
                    } else{
                        throw new IllegalArgumentException("Invalid value size!");
                    }
                } catch (IllegalArgumentException iae){
                    iae.printStackTrace();
                }

            }
        });

    }

    /**
     * Initializes the year choice
     */
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

        yearLabel = new JLabel("TO");
        yearLabel.setBounds((getWidth()/2) - 10, 275, 20,20);
        add(yearLabel);

    }

    /**
     * Initializes the month choice
     */
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

        monthLabel = new JLabel("TO");
        monthLabel.setBounds((getWidth()/2) - 10, 300, 20,20);
        add(monthLabel);

    }

    /**
     * Initializes the category choice for multi hotel mode
     */
    private void InitCategoryChoice(){

        categories = new String[]{"*", "**", "***", "****", "*****"};

        minCategoryBox = new JComboBox<>(categories);
        minCategoryBox.setBounds((getWidth()/2) - 115, 325, 100,20);
        add(minCategoryBox);

        maxCategoryBox = new JComboBox<>(categories);
        maxCategoryBox.setBounds((getWidth()/2) + 15, 325, 100,20);
        add(maxCategoryBox);

        categoryLabel = new JLabel("TO");
        categoryLabel.setBounds((getWidth()/2) - 10, 325, 20,20);
        add(categoryLabel);

    }

    /**
     * Initializes the apply button for multi hotel mode
     */
    private void InitApplyButton(){

        applyButton = new JButton("CONFIRM");
        applyButton.setBounds((getWidth()/2) - 60, 350, 120, 20);
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

    /**
     * Initializes the single hotel table based on user choice
     * @param startYear
     * @param endYear
     * @param startMonth
     * @param endMonth
     * @param hotelID
     */
    private void InitSingleOccupancyTable(int startYear, int endYear, int startMonth, int endMonth, int hotelID){

        // Remove the previous table, if it exists
        if (customTable != null) {
            remove(customTable);
        }

        List <Occupancy> dataList = OccupancyDAO.fetchSingleOccupancyOverview(startYear, endYear, startMonth, endMonth, hotelID);
        int rowCount = dataList.size();
        int columnCount = columnNamesSingle.length;

        Object[][] data = new Object[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            Occupancy occupancy = dataList.get(i); // Use dataList instead of hotels
            data[i] = occupancy.toSingleObjectOverviewArray();
        }

        customTableModel = new CustomTableModel(data, columnNamesSingle);
        customTable = new CustomTable(customTableModel);
        customTable.setLocation(100, 60);

        add(customTable);

        // Set the width of the JScrollPane to 3/4 of the window width and the height to equal the combined height of
        //the rows times 1.5 (don´t ask why, I don´t know)
        double tableWidth = getWidth() * 0.75;
        double tableHeight = customTable.getTable().getRowHeight() * (11.5);
        customTable.setSize((int) tableWidth, (int) tableHeight);

    }

    /**
     * Initializes the mult hotel table
     * @param startYear
     * @param endYear
     * @param startMonth
     * @param endMonth
     * @param minCategory
     * @param maxCategory
     */
    private void InitMultiOccupancyTable(int startYear, int endYear, int startMonth, int endMonth, String minCategory, String maxCategory){

        // Remove the previous table, if it exists
        if (customTable != null) {
            remove(customTable);
        }

        List <Occupancy> dataList = OccupancyDAO.fetchMultiOccupancyOverview(minCategory.length(), maxCategory.length(), startYear, endYear, startMonth, endMonth);
        int hotelCount = HotelDAO.getHotelIDs(minCategory, maxCategory).size();
        int rowCount = dataList.size();
        int columnCount = columnNamesMulti.length;

        Object[][] data = new Object[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            Occupancy occupancy = dataList.get(i); // Use dataList instead of hotels
            data[i] = occupancy.toMultiObjectOverviewArray(hotelCount);
        }

        customTableModel = new CustomTableModel(data, columnNamesMulti);
        customTable = new CustomTable(customTableModel);
        customTable.setLocation(100, 60);

        add(customTable);

        // Set the width of the JScrollPane to 3/4 of the window width and the height to equal the combined height of
        //the rows times 1.5 (don´t ask why, I don´t know)
        double tableWidth = getWidth() * 0.75;
        double tableHeight = customTable.getTable().getRowHeight() * (11.5);
        customTable.setSize((int) tableWidth, (int) tableHeight);

    }

    /**
     * returns the last selected occupancy data for export
     * @return
     */
    public static Object[][] getSelectedOccupancyData() {
        return selectedOccupancyData;
    }

}
