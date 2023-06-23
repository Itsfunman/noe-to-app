package src;

import lombok.SneakyThrows;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HotelEditFrame extends JFrame {

    private JToolBar toolbar;
    private CustomTable customTable;
    private JButton addButton;
    private JButton deleteButton;

    // private String fileName = "data/hotelData.txt";

    private String fileName = "data/hotelData.txt";

    private String[] columnNames = {"ID", "Name", "Kategorie", "Anzahl Zimmer", "Anzahl Betten", "BesitzerIn", "Kontakt", "Adresse",
            "Stadt", "PLZ", "Telefonnummer", "Familien", "Hunde", "Spa", "Fitness"};

    public HotelEditFrame(String title) {
        super(title);

        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        initToolbar();
        initCustomTable();
        initAddButton();
        initDeleteButton();

        setVisible(true);
    }

    private void initDeleteButton() {
        deleteButton = new JButton("DELETE HOTEL");
        deleteButton.setBounds(335, 440, 130, 20);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = customTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Show a confirmation dialog with a checkbox
                    JCheckBox checkBox = new JCheckBox("Are you sure?");
                    Object[] message = {checkBox};
                    int option = JOptionPane.showConfirmDialog(null, message, "Confirmation", JOptionPane.OK_CANCEL_OPTION);

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
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No row selected.");
                }
            }
        });

        deleteButton.setVisible(true);
        add(deleteButton);
    }

    private void deleteHotelFromOccupancies(String hotelID){

        String occupancyFile = "data/occupancies.txt";
        File occupancy = new File(occupancyFile);

        try {
            List<String> lines = Files.readAllLines(occupancy.toPath());
            lines.removeIf(line -> line.startsWith(hotelID + ",")); // Remove lines that start with the given string

            Files.write(occupancy.toPath(), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

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

    private void initAddButton() {
        addButton = new JButton("ADD HOTEL");
        addButton.setBounds(175, 440, 130, 20);
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
                JCheckBox gdprCheckBox = new JCheckBox("I confirm that I have processed the data according to GDPR");

                // Add checkboxes to the input panel
                inputPanel.add(gdprCheckBox);

                // Show the input dialog
                int result = JOptionPane.showConfirmDialog(null, inputPanel, "Enter Hotel Details", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION && gdprCheckBox.isSelected()) {
                    // User confirmed GDPR processing, proceed with adding the hotel
                    for (String columnName : Arrays.copyOfRange(columnNames, 1, columnNames.length)) {

                        String input = JOptionPane.showInputDialog("Enter value for " + columnName);

                        rowData[Arrays.asList(columnNames).indexOf(columnName)] = input;

                    }
                    Hotel hotel = new Hotel(rowData[1], rowData[2], rowData[3], rowData[4], rowData[5], rowData[6], rowData[7],
                            rowData[8], rowData[9], rowData[10], rowData[11], rowData[12], rowData[13], rowData[14]);

                    rowData[0] = String.valueOf(hotel.getHotelID());
                    // Add the new row to the table
                    customTable.getTableModel().addRow(rowData);
                    // Save the updated data to the file
                    customTable.getTableModel().saveData();

                    addHotelToDB(rowData);
                }
            }
        });

        addButton.setVisible(true);
        add(addButton);
    }

    public void addHotelToDB(String[] rowData) throws SQLException {

        //add to DB
        PreparedStatement pst = null;
        Connection connection = null;
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        System.out.println(connection);
        try {
            pst = connection.prepareStatement("insert into dbo.hotel (hotelid,hotelname,kategorie,roomNumber,bedNumber,owner,contact,adress,city,plz,tel,family,animals,spa,fitness) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        pst.setInt(1, Integer.parseInt(rowData[0]));
        pst.setString(2, rowData[1]);
        pst.setString(3, rowData[2]);
        pst.setInt(4, Integer.parseInt(rowData[3]));
        pst.setInt(5, Integer.parseInt(rowData[4]));
        pst.setString(6, rowData[5]);
        pst.setString(7, rowData[6]);
        pst.setString(8, rowData[7]);
        pst.setString(9, rowData[8]);
        pst.setInt(10, Integer.parseInt(rowData[9]));
        pst.setLong(11, Long.parseLong(rowData[10]));
        pst.setString(12, rowData[11]);
        pst.setString(13, rowData[12]);
        pst.setString(14, rowData[13]);
        pst.setString(15, rowData[14]);

        pst.executeUpdate();

        {
            JOptionPane.showMessageDialog(null, "GDPR confirmation is required to add a hotel.");
        }
    }


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


        private void editRow (String fileName,int rowIndex, int columnIndex, String newValue){
            ArrayList<String[]> data = fetchData(fileName);

            if (rowIndex >= 0 && rowIndex < data.size() && columnIndex >= 0 && columnIndex < data.get(rowIndex).length) {
                data.get(rowIndex)[columnIndex] = newValue;
                saveData(fileName, data);
            } else {
                System.out.println("Invalid row index or column index.");
            }
        }

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

        private void initCustomTable () {
            ArrayList<String[]> dataList = fetchData(fileName);
            int rowCount = dataList.size();
            int columnCount = columnNames.length;

            String[][] data = new String[rowCount][columnCount];
            for (int i = 0; i < rowCount; i++) {
                data[i] = dataList.get(i);
            }

            CustomTableModel customTableModel = new CustomTableModel(data, fileName, columnNames);
            customTable = new CustomTable(customTableModel);
            customTable.setBounds(10, 50, 770, 350);

            customTable.getTable().getModel().addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    if (row >= 0 && column >= 0) {
                        Object updatedValue = customTableModel.getValueAt(row, column);
                        // Perform any necessary actions with the updated value

                        customTableModel.saveData(); // Save the updated data to the file
                    }
                }
            });

            add(customTable);
        }

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

    }
