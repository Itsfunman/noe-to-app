package base;



import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import sqlStuff.DBConnection;
import sqlStuff.HotelDAO;
import tableClasses.CustomTable;
import tableClasses.CustomTableModel;
import utilityClasses.Hotel;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static utilityClasses.Hotel.hotels;


/**
 * The HotelEditFrame class represents a JFrame for editing hotel data.
 */
public class HotelEditFrame extends JFrame {

    private JToolBar toolbar;
    private CustomTable customTable;
    private JButton addButton;
    private JButton deleteButton;
    private JButton excelButton;
    private JButton updateButton;

    private CustomTableModel customTableModel;

    // private String fileName = "data/hotelData.txt";

    private String fileName = "data/hotelData.txt";

    private String[] columnNames = {"ID", "Name", "Catagory", "Amount Rooms", "Amount Beds", "Owner", "Contact", "Adress",
            "City", "Zip Code", "Phonenumber", "Family", "Dog", "Spa", "Fitness"};

    /**
     * Constructs a HotelEditFrame object with the specified title.
     *
     * @param title the title of the frame
     */
    public HotelEditFrame(String title) {
        super(title);

        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        //Set custom Icon
        ImageIcon icon = new ImageIcon("assets/NOETOLogo.jpg");
        setIconImage(icon.getImage());

        initToolbar();
        initCustomTable();
        initAddButton();
        InitHotelFromExcelButton();
        initDeleteButton();

        setVisible(true);
    }

    /**
     * Initializes the "IMPORTIEREN" button for importing hotel data from an Excel file.
     */
    private void InitHotelFromExcelButton(){

        excelButton = new JButton("IMPORT");
        excelButton.setBounds((getWidth()/2) - 65, 420, 130, 20);
        excelButton.setVisible(true);
        add(excelButton);

        excelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Open a dialog box to get input from the user for each column
                JPanel inputPanel = new JPanel();
                inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
                //Updated version
                // Create checkboxes for GDPR confirmation
                JCheckBox gdprCheckBox = new JCheckBox("I have taken note of the GDPR");

                // Add checkboxes to the input panel
                inputPanel.add(gdprCheckBox);

                // Show the input dialog
                int result = JOptionPane.showConfirmDialog(null, inputPanel, "Input", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION && gdprCheckBox.isSelected()) {
                    // User confirmed GDPR processing, proceed with adding the hotel
                    String inputFilePath = JOptionPane.showInputDialog("Route");
                    String targetFilePath = "data/importData.txt";

                    inputFilePath = inputFilePath.replaceAll("\"", "");

                    try {
                        // Load the input Excel file
                        FileInputStream inputStream = new FileInputStream(inputFilePath);
                        Workbook workbook = new XSSFWorkbook(inputStream);
                        Sheet sheet = workbook.getSheetAt(0);

                        // Read the contents of the Excel file
                        StringBuilder inputText = new StringBuilder();
                        for (Row row : sheet) {
                            for (Cell cell : row) {
                                inputText.append(cell).append("\t");
                            }
                            inputText.append("\n");
                        }

                        // Write the input text to the target file
                        FileWriter writer = new FileWriter(targetFilePath);
                        writer.write(inputText.toString());
                        writer.close();

                        System.out.println("Data from the Excel file has been copied to the target file successfully.");

                        workbook.close();
                        inputStream.close();
                    } catch (IOException iae) {
                        iae.printStackTrace();
                    }

                    try {
                        // Read the contents of the target file
                        BufferedReader reader = new BufferedReader(new FileReader(targetFilePath));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            // Split the row by tabs ('\t') into an array
                            String[] rowData = line.split("\t");

                            // Create a new array to store the parsed data
                            String[] parsedData = new String[14];

                            // Iterate over the rowData array and convert the appropriate values to integers
                            for (int i = 0; i < rowData.length; i++) {
                                if (rowData[i] != null) {
                                    if (i == 2 || i == 3 || i == 8 || i == 9) {
                                        // Convert the numeric values to integers
                                        int intValue = (int) Double.parseDouble(rowData[i]);
                                        parsedData[i] = String.valueOf(intValue);
                                    } else {
                                        // Copy other non-null values as strings
                                        parsedData[i] = rowData[i];
                                    }
                                }
                            }

                            String newString = "";
                            for (int i = 0; i < parsedData.length; i++){
                                if (parsedData[i] != null){
                                    if (i == 0){
                                        newString = newString + parsedData[i];
                                    } else {
                                        newString = newString + "," + parsedData[i];
                                    }
                                }
                            }

                            rowData = newString.split(",");

                            // Create a new array with length 15
                            String[] newRowData = new String[15];

                            // Leave the first element empty for later assignment
                            newRowData[0] = "";

                            // Copy the elements from newString to newRowData starting from index 1
                            System.arraycopy(rowData, 0, newRowData, 1, rowData.length);

                            Hotel hotel = new Hotel(newRowData[1], newRowData[2], newRowData[3], newRowData[4], newRowData[5], newRowData[6], newRowData[7],
                                         newRowData[8], newRowData[9], newRowData[10], newRowData[11], newRowData[12], newRowData[13], newRowData[14]);

                            newRowData[0] = String.valueOf(hotel.getHotelID());

                            // Add the new row to the table
                            customTable.getTableModel().addRow(newRowData);

                            // Save the updated data to the file
                            customTable.getTableModel().saveData();

                            HotelDAO.addHotelToDB(hotel);

                        }

                        reader.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }


                }
            }
        });

    }

    /**
     * Updates hotel in DB
     * @param rowIndex
     * @param rowData
     */
    public void updateHotelInDB(int rowIndex, Object[] rowData) {
        // Get the hotel ID from the rowData array, assuming it's in the first column
        int hotelID = (int) rowData[0];

        // Update the hotel in the database using a PreparedStatement
        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement pst = connection.prepareStatement("UPDATE dbo.hotel SET hotelname = ?, kategorie = ?, roomNumber = ?, bedNumber = ?, owner = ?, contact = ?, adress = ?, city = ?, plz = ?, tel = ?, family = ?, animals = ?, spa = ?, fitness = ? WHERE hotelid = ?");
            pst.setString(1, (String) rowData[1]);
            pst.setString(2, (String) rowData[2]);
            pst.setInt(3, (Integer) rowData[3]); // Cast to Integer instead of int
            pst.setInt(4, (Integer) rowData[4]); // Cast to Integer instead of int
            pst.setString(5, (String) rowData[5]);
            pst.setString(6, (String) rowData[6]);
            pst.setString(7, (String) rowData[7]);
            pst.setString(8, (String) rowData[8]);
            pst.setString(9, (String) rowData[9]);
            pst.setString(10, (String) rowData[10]);
            pst.setBoolean(11, (Boolean) rowData[11]); // Cast to Boolean instead of boolean
            pst.setBoolean(12, (Boolean) rowData[12]); // Cast to Boolean instead of boolean
            pst.setBoolean(13, (Boolean) rowData[13]); // Cast to Boolean instead of boolean
            pst.setBoolean(14, (Boolean) rowData[14]); // Cast to Boolean instead of boolean
            pst.setInt(15, hotelID);

            pst.executeUpdate();
            pst.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Initializes the "DELETE" button for importing hotel data from an Excel file.
     */
    private void initDeleteButton() {
        deleteButton = new JButton("DELETE");
        deleteButton.setBounds((getWidth()/2) + 75, 420, 130, 20);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = customTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Show a confirmation dialog with a checkbox
                    JCheckBox checkBox = new JCheckBox("Do you want to delete this hotel?");
                    Object[] message = {checkBox};
                    int option = JOptionPane.showConfirmDialog(null, message, "Confirm", JOptionPane.OK_CANCEL_OPTION);

                    if (option == JOptionPane.OK_OPTION && checkBox.isSelected()) {
                        // Retrieve the hotel ID from the selected row
                        String hotelID = customTable.getValueAt(selectedRow, 0).toString();

                        // Delete the hotel from the database
                        try {
                            deleteHotelFromDB(hotelID);
                            deleteHotelFromOccupancies(hotelID);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }

                        // Remove the selected row from the table model
                        customTable.getTableModel().removeRow(selectedRow);

                        // Save the updated data to the file
                        customTable.getTableModel().saveData();
                    } else {
                        JOptionPane.showMessageDialog(null, "No Row selected");
                    }
                }
            }
        });

        deleteButton.setVisible(true);
        add(deleteButton);
    }

    /**
     * Deletes hotels from occupanceis when they are removed
     */
    private void deleteHotelFromOccupancies(String hotelID) {
        String occupancyFile = "data/occupancies.txt";
        File occupancy = new File(occupancyFile);

        try (FileReader fileReader = new FileReader(occupancy);
             BufferedReader bufferedReader = new BufferedReader(fileReader);
             FileWriter fileWriter = new FileWriter("data/occupancies_temp2.txt");
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",", 2);

                String lineHotelID = parts[0];

                if (!lineHotelID.equals(hotelID)) {
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Replace the original file with the temporary file
        File originalFile = new File(occupancyFile);
        File tempFile = new File("data/occupancies_temp2.txt");
        try {
            Files.copy(tempFile.toPath(), originalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Hotel data deleted successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to delete hotel data.");
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

    /**
     * Deletes hotels from the database
     */
    private void deleteHotelFromDB(String hotelID) throws SQLException {
        // Delete from DB
        PreparedStatement pst = null;
        Connection connection = null;
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        try {
            pst = connection.prepareStatement("DELETE FROM dbo.hotel WHERE hotelid = ?");
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        pst.setInt(1, Integer.parseInt(hotelID));
        pst.executeUpdate();
    }

    /**
     * Initializes the add button to add new hotels
     */
    private void initAddButton() {
        addButton = new JButton("ADD");
        addButton.setBounds((getWidth()/2) - 205, 420, 130, 20);
        //updated version from Pia Zimmermann
        addButton.addActionListener(new ActionListener() {
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] rowData = new String[columnNames.length];
                // Open a dialog box to get input from the user for each column
                JPanel inputPanel = new JPanel();
                inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
                //Updated version
                // Create checkboxes for GDPR confirmation
                JCheckBox gdprCheckBox = new JCheckBox("I have taken note of the GDPR");

                // Add checkboxes to the input panel
                inputPanel.add(gdprCheckBox);

                // Show the input dialog
                int result = JOptionPane.showConfirmDialog(null, inputPanel, "Input", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION && gdprCheckBox.isSelected()) {
                    // User confirmed GDPR processing, proceed with adding the hotel
                    for (String columnName : Arrays.copyOfRange(columnNames, 1, columnNames.length)) {

                        String input = JOptionPane.showInputDialog("Give a value for " + columnName);

                        rowData[Arrays.asList(columnNames).indexOf(columnName)] = input;

                    }

                    Hotel hotel = new Hotel(rowData[1], rowData[2], rowData[3], rowData[4], rowData[5], rowData[6], rowData[7],
                            rowData[8], rowData[9], rowData[10], rowData[11], rowData[12], rowData[13], rowData[14]);

                    rowData[0] = String.valueOf(hotel.getHotelID());
                    // Add the new row to the table
                    customTable.getTableModel().addRow(rowData);
                    // Save the updated data to the file
                    customTable.getTableModel().saveData();

                    HotelDAO.addHotelToDB(hotel);
                }
            }
        });

        addButton.setVisible(true);
        add(addButton);
    }



    /**
     * Initializes toolbar to allow switching between hotels
     */
    private void initToolbar () {
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
     * Initializes the custom table for displaying hotel data.
     */
    private void editRow(String fileName, int rowIndex, int columnIndex, String newValue) {
        List<Hotel> data = HotelDAO.fetchDataFromDB();

        if (rowIndex >= 0 && rowIndex < data.size() && columnIndex >= 0 && columnIndex < getColumnCount()) {
            Hotel hotel = data.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    hotel.setCategory(newValue);
                    break;
                case 1:
                    hotel.setHotelName(newValue);
                    break;
                case 2:
                    hotel.setHotelOwner(newValue);
                    break;
                case 3:
                    hotel.setHotelContactInformation(newValue);
                    break;
                case 4:
                    hotel.setAddress(newValue);
                    break;
                case 5:
                    hotel.setCity(newValue);
                    break;
                case 6:
                    hotel.setCityCode(newValue);
                    break;
                case 7:
                    hotel.setPhoneNumber(newValue);
                    break;
                case 8:
                    hotel.setRoomNumber(Integer.parseInt(newValue));
                    break;
                case 9:
                    hotel.setBedNumber(Integer.parseInt(newValue));
                    break;
                case 10:
                    hotel.setFamily(Boolean.parseBoolean(newValue));
                    break;
                case 11:
                    hotel.setDog(Boolean.parseBoolean(newValue));
                    break;
                case 12:
                    hotel.setSpa(Boolean.parseBoolean(newValue));
                    break;
                case 13:
                    hotel.setFitness(Boolean.parseBoolean(newValue));
                    break;
                default:
                    System.out.println("Invalid column index.");
                    return;
            }

            HotelDAO.updateHotelInDB(hotel);
        } else {
            System.out.println("Invalid row index or column index.");
        }
    }

    private int getColumnCount() {
        return columnNames.length;
    }


    /**
     * saves Data when changes are made
     * @param fileName
     * @param data
     */
    private void saveData (String fileName, ArrayList < String[]>data){

        try (FileWriter fileWriter = new FileWriter(fileName);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {
            // user does not exist in file, so add to file
            for (String[] row : data) {
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

    /**
     * Initializes the CustomTable instance
     */
    private void initCustomTable () {
        List<Hotel> dataList = HotelDAO.fetchDataFromDB();
        int rowCount = dataList.size();
        int columnCount = columnNames.length;

        Object[][] data = new Object[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            Hotel hotel = hotels.get(i);
            data[i] = hotel.toObjectArray();
        }

        customTableModel = new CustomTableModel(data, fileName, columnNames);
        customTable = new CustomTable(customTableModel);
        customTable.setBounds(10, 50, 770, 350);

        //Like ButtonListener but with tables
        customTable.getTable().getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                //Gets cell
                int row = e.getFirstRow();
                int column = e.getColumn();
                //Checks whether selection is valid
                if (row >= 0 && column >= 0) {
                    //Implements check box
                    JPanel inputPanel = new JPanel();
                    inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

                    JCheckBox gdprCheckBox = new JCheckBox("I have taken note of the GDPR");

                    inputPanel.add(gdprCheckBox);

                    int result = JOptionPane.showConfirmDialog(null, inputPanel, "Input", JOptionPane.OK_CANCEL_OPTION);

                    //If ok is selected data gets saved to file and database
                    if (result == JOptionPane.OK_OPTION && gdprCheckBox.isSelected()) {

                        customTableModel.saveData(); // Save the updated data to the file
                        Object [] changedData = customTableModel.getData()[row]; //Gets String with changed data
                        updateHotelInDB(row, changedData); //Saves data to database

                    }

                }
            }
        });


        add(customTable);
    }

    /**
     * gets data from hotelData.txt
     * @param fileName
     * @return
     */
    /*
    private ArrayList<String[]> fetchData (String fileName){
        ArrayList<String[]> dataList = new ArrayList<>();

        try (FileReader fileReader = new FileReader(fileName);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");

                // Skip invalid lines or lines with insufficient values
                if (values.length != columnNames.length) {
                    continue;
                }

                dataList.add(values);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataList;
    }

     */



}
