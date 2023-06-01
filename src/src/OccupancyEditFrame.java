package src;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.io.*;
import java.util.Arrays;



public class OccupancyEditFrame extends JFrame {

    private JToolBar toolbar;
    private CustomTable customTable;
    private JComboBox hotelChoice;
    private JComboBox occupancyTypeChoice;
    private HashMap<String, Hotel> hotelMap;
    private CustomTableModel tableModel;
    private String fileName;

    private String [] columnNames = new String[]{"JAHR", "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
            "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    public OccupancyEditFrame(String title){
        super(title);

        setSize(800,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(null);
        setLocationRelativeTo(null);

        InitToolbar();
        InitHotelChoice();
        InitOccupancyTypeChoice();
    }

    private void InitHotelChoice() {
        hotelChoice = new JComboBox<>();
        hotelChoice.setBounds(410, getHeight() - 70, 130, 20);

        hotelMap = new HashMap<>();

        for (Hotel hotel : Hotel.hotels) {
            hotelChoice.addItem(hotel.getHotelName());
            hotelMap.put(hotel.getHotelName(), hotel);
        }

        add(hotelChoice);

        hotelChoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (fileName != null && !fileName.equals(null)){
                    saveData(fileName, fetchData(fileName));
                }
                String selectedHotelName = (String) hotelChoice.getSelectedItem();
                Hotel selectedHotel = hotelMap.get(selectedHotelName);
                fileName = generateFileName(selectedHotel, occupancyTypeChoice.getSelectedItem());
                InitHotelOccupancyTable(fileName);
            }
        });
    }

    private void InitOccupancyTypeChoice(){
        occupancyTypeChoice = new JComboBox<>();
        occupancyTypeChoice.setBounds(260, getHeight() - 70, 130, 20);

        occupancyTypeChoice.addItem("Bed Occupancy");
        occupancyTypeChoice.addItem("Room Occupancy");

        add(occupancyTypeChoice);

        occupancyTypeChoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!fileName.equals(null)){
                    saveData(fileName, fetchData(fileName));
                }
                String selectedHotelName = (String) hotelChoice.getSelectedItem();
                Hotel selectedHotel = hotelMap.get(selectedHotelName);
                fileName = generateFileName(selectedHotel, occupancyTypeChoice.getSelectedItem());
                InitHotelOccupancyTable(fileName);

            }
        });
    }

    private String generateFileName(Hotel hotel, Object occupancyType){

        String occupancyTypeString;
        // Use a more reliable method to check occupancy type
        if (occupancyType.equals("Bed Occupancy")) {
            occupancyTypeString = "BedOccupancy";
        } else {
            occupancyTypeString = "RoomOccupancy";
        }

        String newFileName = hotel.getHotelName() + occupancyTypeString + ".txt";

        return newFileName;
    }

    private void InitHotelOccupancyTable(String fileName) {

        String[][] data = fetchData(fileName);
        // Check if there are fewer rows than years
        if (data.length - 1 < getNumberOfYears()) {

            addRowsToCoverYears(fileName, data.length - 1);
            data = fetchData(fileName);

        }

        if (customTable != null) {
            remove(customTable); // Remove the existing table
        }

        saveTableData();

        customTable = new CustomTable(data, columnNames);
        customTable.setBounds(10, 50, 770, 350);

        customTable.getTable().getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();

                String [][] tableData = customTable.getTableModel().getData();
                if (row >= 0 && row < tableData.length && column >= 0 && column < tableData[row].length) {
                    String newValue = tableData[row][column];
                    editRow(fileName, row, column, newValue);
                }
            }
        });

        add(customTable);

        tableModel = new CustomTableModel(data, "data/" + fileName, columnNames);

        revalidate();
        repaint();
    }

    private void editRow(String fileName, int rowIndex, int columnIndex, String newValue) {
        String[][] data = fetchData(fileName);

        if (rowIndex >= 0 && rowIndex < data.length && columnIndex >= 0 && columnIndex < data[rowIndex].length) {
            data[rowIndex][columnIndex] = newValue;
            saveData(fileName, data);
        } else {
            System.out.println("Invalid row index or column index.");
        }
    }

    private void saveTableData() {
        if (tableModel != null) {
            tableModel.saveData();
        }
    }

    private int getNumberOfYears() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int startYear = 2000; // Starting year
        return currentYear - startYear + 1;
    }

    private void InitToolbar() {

        toolbar = new Toolbar(this);
        toolbar.setVisible(true);
        toolbar.setLocation(0, 0);

        add(toolbar);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                toolbar.setSize(getWidth(), 30);
            }
        });
    }

    private String[][] fetchData(String fileName) {
        String[][] data;

        try (FileReader fileReader = new FileReader("data/" + fileName);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            ArrayList<String[]> dataList = new ArrayList<>();

            String line;
            int year = 2000; // Starting year

            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");

                // Skip invalid lines or lines with insufficient values
                if (values.length != 13) {
                    continue;
                }

                dataList.add(values);
                year++;
            }

            data = new String[dataList.size()][13];
            dataList.toArray(data);

        } catch (IOException e) {
            e.printStackTrace();
            data = new String[0][13]; // Empty array for new file
        }

        return data;
    }

    private void saveData(String fileName, String[][] data) {

        try (FileWriter fileWriter = new FileWriter("data/" + fileName);
             BufferedWriter writer = new BufferedWriter(fileWriter)){
            // user does not exist in file, so add to file
            for (String[] row : data)
            {
                String line = "";
                for (String value : row) {
                    line = line + value + ",";
                }
                //System.out.println(line);
                writer.write(line);
                writer.newLine();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addRowsToCoverYears(String fileName, int currentRowCount) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[][] data = fetchData(fileName);

        for (int year = currentRowCount + 2001; year <= currentYear; year++) {
            String[] newRow = new String[13];
            newRow[0] = String.valueOf(year);

            Arrays.fill(newRow, 1, newRow.length, "0"); // Fill elements from index 1 to end with "0"

            data = Arrays.copyOf(data, data.length + 1);
            data[data.length - 1] = newRow;
        }

        saveData(fileName, data);
    }

}
