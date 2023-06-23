package src;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.*;
import java.sql.SQLOutput;
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

    //These values are initially set to -1 since no hotelID or partpos can have this value
    private int hotelID = -1;
    private int partpos = -1;

    private String[] columnNames = new String[]{"JAHR", "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
            "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    public OccupancyEditFrame(String title) {
        super(title);

        setSize(800, 500);
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

                //Format: id,rooms,usedrooms,beds,usedbeds,year,month

                if (hotelID != -1 && partpos != -1) {
                    //saveData(fetchData(hotelID, partpos), partpos);
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

    private void InitOccupancyTypeChoice() {
        occupancyTypeChoice = new JComboBox<>();
        occupancyTypeChoice.setBounds(260, getHeight() - 70, 130, 20);

        occupancyTypeChoice.addItem("Bed Occupancy");
        occupancyTypeChoice.addItem("Room Occupancy");

        add(occupancyTypeChoice);

        occupancyTypeChoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Format: id,rooms,usedrooms,beds,usedbeds,year,month

                if (hotelID != -1 && partpos != -1) {
                    //saveData(fetchData(hotelID, partpos), partpos);
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
                    editRow(row, column, newValue);
                }
            }
        });

        add(customTable);

        tableModel = new CustomTableModel(data, "data/occupancies.txt", columnNames);
        customTable.updateData(data);

        revalidate();
        repaint();
    }

    private void editRow(int rowIndex, int columnIndex, String newValue) {
        String[][] data = fetchData(hotelID, partpos);

        if (rowIndex >= 0 && rowIndex < data.length && columnIndex >= 0 && columnIndex < data[rowIndex].length) {
            data[rowIndex][columnIndex] = newValue;
            //saveData(data, partpos);
        } else {
            System.out.println("Invalid row index or column index.");
        }
    }

    private void saveTableData() {
        if (tableModel != null) {
            tableModel.saveData();
        }
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

                System.out.println("lineHotelID: "+ lineHotelID);
                System.out.println("hotelID: "+ hotelID);

                // Check if the line matches the given hotelID
                if (lineHotelID == hotelID) {
                    System.out.println("Fired");
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

        return data;
    }



    private void saveData(String[][] data, int partpos) {
        try (FileReader fileReader = new FileReader("data/occupancies.txt");
             BufferedReader bufferedReader = new BufferedReader(fileReader);
             FileWriter fileWriter = new FileWriter("data/occupancies.txt", false);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {

            ArrayList<String> lines = new ArrayList<>();
            String line;

            // Read the existing data and store it in the 'lines' list
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }

            // Update the specific lines for the given hotelID and partpos
            for (String[] row : data) {
                int rowIndex = Integer.parseInt(row[1]);
                int columnIndex = Integer.parseInt(row[2]);

                if (rowIndex >= 0 && rowIndex < lines.size()) {
                    String[] values = lines.get(rowIndex).split(",");
                    if (columnIndex >= 0 && columnIndex < values.length) {
                        if (partpos == 2) { // Update usedRooms
                            values[columnIndex] = row[0];
                        } else if (partpos == 4) { // Update usedBeds
                            values[columnIndex + 1] = row[0];
                        }
                        lines.set(rowIndex, String.join(",", values));
                    }
                }
            }

            // Write the updated data back to the file
            for (String updatedLine : lines) {
                writer.write(updatedLine);
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
