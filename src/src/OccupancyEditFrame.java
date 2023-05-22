package src;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.io.*;
import java.util.Arrays;



public class OccupancyEditFrame extends JFrame {

    private JToolBar toolbar;
    private HotelOccupancyTable hotelOccupancyTable;
    private JComboBox hotelChoice;
    private JComboBox occupancyTypeChoice;
    private HashMap<String, Hotel> hotelMap;
    private CustomTableModel tableModel;
    private String fileName;

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
        hotelChoice.setBounds((getWidth() / 2) - 65, getHeight() - 70, 130, 20);

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
                    saveData(fileName, getData(fileName));
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
        occupancyTypeChoice.setBounds((getWidth() / 2) - 215, getHeight() - 70, 130, 20);

        occupancyTypeChoice.addItem("Bed Occupancy");
        occupancyTypeChoice.addItem("Room Occupancy");

        add(occupancyTypeChoice);

        occupancyTypeChoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!fileName.equals(null)){
                    saveData(fileName, getData(fileName));
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

        String[][] data = getData(fileName);
        // Check if there are fewer rows than years
        if (data.length - 1 < getNumberOfYears()) {
            addRowsToCoverYears(fileName, data.length - 1);
            data = getData(fileName);
        }

        if (hotelOccupancyTable != null) {
            remove(hotelOccupancyTable); // Remove the existing table
        }

        saveTableData();

        hotelOccupancyTable = new HotelOccupancyTable(data);
        hotelOccupancyTable.setBounds(10, 50, 770, 350);
        add(hotelOccupancyTable);

        tableModel = new CustomTableModel(data, fileName);

        revalidate();
        repaint();
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


    private String[][] getData(String fileName) {
        String[][] data;

        try (FileReader fileReader = new FileReader("data/" + fileName);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            ArrayList<String[]> dataList = new ArrayList<>();

            String line;
            int year = 2000; // Starting year

            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");

                // Skip invalid lines or lines with insufficient values
                if (values.length != 12) {
                    continue;
                }

                String[] row = new String[13];
                row[0] = String.valueOf(year);
                System.arraycopy(values, 0, row, 1, 12);

                dataList.add(row);
                year++;
            }

            System.out.println(dataList.size());
            data = new String[dataList.size()][13];
            dataList.toArray(data);

        } catch (IOException e) {
            e.printStackTrace();
            data = new String[0][0];
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
                System.out.println(line);
                writer.write(line);
                writer.newLine();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void addRowsToCoverYears(String fileName, int currentRowCount) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[][] data = getData(fileName);

        int lastYear = data.length - 1;

        for (int year = lastYear + 1; year <= currentYear - 2000; year++) {
            String[] newRow = new String[12];  // Update the row size to 12
            Arrays.fill(newRow, "0");

            data = Arrays.copyOf(data, data.length + 1);
            data[data.length - 1] = newRow;
        }

        // Exclude the year column when saving the data
        String[][] dataWithoutYear = new String[data.length][12];
        for (int i = 0; i < data.length; i++) {
            System.arraycopy(data[i], 1, dataWithoutYear[i], 0, 11);
            dataWithoutYear[i][11] = "0";  // Set the last element to "0"
        }

        saveData(fileName, dataWithoutYear);
    }








    // Example usage to edit a row
// Example usage to edit a row
    private void editRow(String fileName, int rowIndex, String[] updatedRow) {
        String[][] data = getData(fileName);

        if (rowIndex >= 0 && rowIndex < data.length && updatedRow.length == 12) {
            System.arraycopy(updatedRow, 0, data[rowIndex], 1, 12);
            saveData(fileName, data);

            // Update the table with the modified data
            hotelOccupancyTable.updateData(data);
        } else {
            System.out.println("Invalid row index or row data.");
        }
    }


}
