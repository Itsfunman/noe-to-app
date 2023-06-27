package src;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.io.*;
import java.util.Arrays;


/**
 * Class used for editing occupancy data
 */
public class OccupancyEditFrame extends JFrame {

    private JToolBar toolbar;
    private CustomTable customTable;
    private JComboBox hotelChoice;
    private JComboBox occupancyTypeChoice;
    private HashMap<String, Hotel> hotelMap;
    private CustomTableModel tableModel;
    private String fileName;

    //These values are initially set to -1 since no hotelID or partpos can have this value
    private int hotelID = -1;
    private int partpos = -1;

    private String[] columnNames = new String[]{"YEAR", "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
            "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    /**
     * Initializes the OccupancyEditFrame
     * @param title
     */
    public OccupancyEditFrame(String title) {
        super(title);

        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(null);
        setLocationRelativeTo(null);

        //Set custom Icon
        ImageIcon icon = new ImageIcon("assets/NOETOLogo.jpg");
        setIconImage(icon.getImage());

        InitToolbar();
        InitHotelChoice();
        InitOccupancyTypeChoice();
    }

    /**
     * Initializes a dropbox used to select hotels
     */
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

                //Format: id,rooms,usedrooms,beds,usedbeds,year,month

                if (hotelID != -1 && partpos != -1) {
                    System.out.println("CALLED SAVE");
                    saveData(fetchData(hotelID, partpos), partpos);
                }

                String selectedHotelName = (String) hotelChoice.getSelectedItem();
                Hotel selectedHotel = hotelMap.get(selectedHotelName);
                int hotelID = selectedHotel.getHotelID();

                int partpos;

                if (occupancyTypeChoice.getSelectedIndex() == 0) {
                    partpos = 4;
                } else {
                    partpos = 2;
                }

                InitHotelOccupancyTable(hotelID, partpos);

            }
        });
    }

    /**
     * Initializes the occupancy type choice
     */
    private void InitOccupancyTypeChoice() {
        occupancyTypeChoice = new JComboBox<>();
        occupancyTypeChoice.setBounds(260, getHeight() - 70, 130, 20);

        occupancyTypeChoice.addItem("bed occupancy");
        occupancyTypeChoice.addItem("room occupancy");

        add(occupancyTypeChoice);

        occupancyTypeChoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Format: id,rooms,usedrooms,beds,usedbeds,year,month

                if (hotelID != -1 && partpos != -1) {
                    saveData(fetchData(hotelID, partpos), partpos);
                }


                String selectedHotelName = (String) hotelChoice.getSelectedItem();
                Hotel selectedHotel = hotelMap.get(selectedHotelName);
                hotelID = selectedHotel.getHotelID();

                if (occupancyTypeChoice.getSelectedIndex() == 0) {
                    partpos = 4;
                } else {
                    partpos = 2;
                }

                InitHotelOccupancyTable(hotelID, partpos);

            }
        });
    }

    /**
     * Initializes the occupancy data based on the user selection
     * @param hotelID
     * @param partpos
     */
    private void InitHotelOccupancyTable(int hotelID, int partpos) {

        String[][] data = fetchData(hotelID, partpos);
        // Check if there are fewer rows than years

        if (customTable != null) {
            remove(customTable); // Remove the existing table
        }

        //saveTableData();

        customTable = new CustomTable(data, columnNames);
        customTable.setBounds(10, 50, 770, 350);

        customTable.getTable().getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();

                String[][] tableData = customTable.getTableModel().getData();
                if (row >= 0 && row < tableData.length && column >= 0 && column < tableData[row].length) {
                    String newValue = tableData[row][column];
                    System.out.println(partpos);
                    editRow(row, column, newValue, partpos);
                }
            }
        });

        add(customTable);

        tableModel = new CustomTableModel(data, "data/occupancies.txt", columnNames);
        customTable.updateData(data);

        revalidate();
        repaint();
    }

    /**
     * Allows editing of rows in the table
     * @param rowIndex
     * @param columnIndex
     * @param newValue
     * @param partpos
     */
    private void editRow(int rowIndex, int columnIndex, String newValue, int partpos) {

        String[][] data = fetchData(hotelID, partpos);

        if (rowIndex >= 0 && rowIndex < data.length && columnIndex >= 0 && columnIndex < data[rowIndex].length) {
            data[rowIndex][columnIndex] = newValue;
            saveData(data, partpos);

        } else {
            System.out.println("Invalid row index or column index.");
        }
    }

    /**
     * Initializes the toolbar
     */
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

    /**
     * Gets data from file for the selected hotel
     * @param hotelID
     * @param partpos
     * @return
     */
    private String[][] fetchData(int hotelID, int partpos) {
        String[][] data;

        // Determine the current year
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        try (FileReader fileReader = new FileReader("data/occupancies.txt");
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            // Initialize the data array with the appropriate dimensions
            data = new String[currentYear - 2014 + 1][13];

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");

                // Skip invalid lines or lines with insufficient values
                if (values.length != 7) {
                    continue;
                }

                int lineHotelID = Integer.parseInt(values[0]);

                // Check if the line matches the given hotelID
                if (lineHotelID == hotelID) {

                    int lineYear = Integer.parseInt(values[5]);
                    int lineMonth = Integer.parseInt(values[6]);
                    int columnIndex = lineYear - 2014; // Calculate the column index based on the year
                    int rowIndex = lineMonth; // Use month as the row index (1-12)

                    // Update the appropriate value based on partpos
                    if (partpos == 2) { // usedRooms
                        data[columnIndex][rowIndex] = values[2];
                    } else if (partpos == 4) { // usedBeds
                        data[columnIndex][rowIndex] = values[4];
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            data = new String[0][13]; // Empty array for new file
        }

        for (int i = 0; i < data.length; i++){
            data[i][0] = String.valueOf(2014 + i);
        }
        return data;
    }

    /**
     * Saves data from the table to the file
     * @param data
     * @param partpos
     */
    private void saveData(String[][] data, int partpos) {
        try (FileReader fileReader = new FileReader("data/occupancies.txt");
             BufferedReader bufferedReader = new BufferedReader(fileReader);
             FileWriter fileWriter = new FileWriter("data/occupancies_temp.txt");
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");

                String lineHotelID = parts[0];
                String lineYear = parts[5];
                String lineMonth = parts[6];

                String selectedHotelName = (String) hotelChoice.getSelectedItem();
                Hotel selectedHotel = hotelMap.get(selectedHotelName);
                String valueHotelID = String.valueOf(selectedHotel.getHotelID());
                String valueYear = String.valueOf(customTable.getSelectedRow() + 2014);

                boolean lineUpdated = false;
                String newLine = line;

                for (String[] row : data) {
                    for (int i = 1; i < row.length; i++) {
                        if (row[i] != null) {

                            String valueMonth = String.valueOf(i);

                            if (lineHotelID.equals(valueHotelID)) {

                                if (lineYear.equals(valueYear)){

                                    if (lineMonth.equals(valueMonth)){
                                        System.out.println("ID AND YEAR AND MONTH FOUND");
                                        System.out.println(lineHotelID + " " + lineYear + " " + lineMonth);

                                        if (partpos == 2) {
                                            System.out.println("PARTPOS 2");
                                            newLine = parts[0] + "," + parts[1] + "," + row[i] + "," + parts[3] + "," + parts[4] + "," + parts[5] + "," + parts[6];
                                            lineUpdated = true;
                                            break;
                                        } else if (partpos == 4) {
                                            System.out.println("PARTPOS 4");
                                            newLine = parts[0] + "," + parts[1] + "," + parts[2] + "," + parts[3] + "," + row[i] + "," + parts[5] + "," + parts[6];
                                            lineUpdated = true;
                                            break;
                                        }

                                    }

                                }

                            }
                        }
                    }
                    if (lineUpdated) {
                        break;
                    }
                }

                bufferedWriter.write(newLine);
                bufferedWriter.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Replace the original file with the temporary file
        File originalFile = new File("data/occupancies.txt");
        File tempFile = new File("data/occupancies_temp.txt");
        try {
            Files.copy(tempFile.toPath(), originalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save data.");
        }

        // Delete the temporary file
        if (tempFile.exists()) {
            if (tempFile.delete()) {
                System.out.println("Temporary file deleted.");
            } else {
                System.out.println("Failed to delete temporary file.");
            }
        }
    }

}



