package tableClasses;


import sqlStuff.HotelDAO;
import objectClasses.Hotel;

import javax.swing.*;
import java.util.List;

/**
 * Represents a scrollable table for displaying capacity information.
 * It contains a JTable and manages the data displayed in the table.
 */
public class CapacityTable extends JScrollPane {

    private JTable table;
    private TableModel tableModel;
    private int totalCount;

    private String[] columnNames = {"CATAGORY", "HOTELS", "ROOMS", "BEDS", "Ø ROOMS", "Ø BEDS"};

    // Enter hotel information here
    public static String[][] data = new String[6][6];


    /**
     * Constructs a CapacityTable.
     * Initializes the layout, sets up the viewport view, and fills the table with data.
     */
    public CapacityTable() {
        refillData();

        setLayout(new ScrollPaneLayout());
        setViewportView(table);
    }

    /**
     * Refills the data array and updates the table.
     */
    private void refillData() {
        // Clear the data array
        for (int i = 1; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = String.valueOf(0);
            }
        }

        fillData();

        tableModel = new TableModel(data, columnNames);
        table = new JTable(tableModel);

        setViewportView(table);
    }

    /**
     * Fills the data array with new data based on hotel information.
     */
    private void fillData() {
        // Fill the data array with new data
        for (int i = 0; i < data.length - 1; i++) {
            data[i][0] = String.valueOf(5 - i);

            int[] betriebInfo = getBetriebInfo(5 - i);
            data[i][1] = String.valueOf(betriebInfo[0]);
            data[i][2] = String.valueOf(betriebInfo[1]);
            data[i][3] = String.valueOf(betriebInfo[2]);
            data[i][4] = String.valueOf(betriebInfo[3]);
            data[i][5] = String.valueOf(betriebInfo[4]);
        }

        data[5][0] = "Total";

        for (int i = 1; i < 4; i++) {
            int count = 0;
            for (int k = 0; k < data.length - 1; k++) {
                count += Integer.parseInt(data[k][i]);
                totalCount++;
            }
            data[5][i] = String.valueOf(count);
        }

        if (Integer.parseInt(data[5][1]) > 0) {
            data[5][4] = String.valueOf(Integer.parseInt(data[5][2]) / Integer.parseInt(data[5][1]));
            data[5][5] = String.valueOf(Integer.parseInt(data[5][3]) / Integer.parseInt(data[5][1]));
        }
    }

    /**
     * Retrieves the information about a particular category of hotels.
     *
     * @param kat the category of hotels
     * @return an array containing the number of businesses, rooms, beds, average rooms per business, and average beds per business
     */
    private int[] getBetriebInfo(int kat) {
        int[] betriebInfo = new int[5];
        for (int i = 0; i < betriebInfo.length; i++) {
            betriebInfo[i] = 0;
        }

        Hotel.hotels.clear();
        loadHotels();

        for (int i = 0; i < Hotel.hotels.size(); i++) {
            if (kat == Hotel.hotels.get(i).getCategory().length()) {
                Hotel hotel = Hotel.hotels.get(i);
                betriebInfo[0]++;
                betriebInfo[1] += hotel.getRoomNumber();
                betriebInfo[2] += hotel.getBedNumber();
            }
        }

        if (betriebInfo[0] > 0) {
            betriebInfo[3] = (Integer) betriebInfo[1] / betriebInfo[0];
            betriebInfo[4] = (Integer) betriebInfo[2] / betriebInfo[0];
        }

        return betriebInfo;
    }

    /**
     * Loads hotel information from a file.
     */
    private void loadHotels() {
        List<Hotel> hotels = HotelDAO.fetchDataFromDB();

        // Update the hotels list in a synchronized block
        synchronized (Hotel.hotels) {
            Hotel.hotels.addAll(hotels);
        }
    }


    /**
     * Retrieves the JTable used in the CapacityTable.
     *
     * @return the JTable
     */
    public JTable getTable() {
        return table;
    }

    /**
     * Sets the JTable used in the CapacityTable.
     *
     * @param table the JTable
     */
    public void setTable(JTable table) {
        this.table = table;
    }

    /**
     * Retrieves the TableModel used in the CapacityTable.
     *
     * @return the TableModel
     */
    public TableModel getTableModel() {
        return tableModel;
    }

    /**
     * Sets the TableModel used in the CapacityTable.
     *
     * @param tableModel the TableModel
     */
    public void setTableModel(TableModel tableModel) {
        this.tableModel = tableModel;
    }

    /**
     * Retrieves the data array used in the CapacityTable.
     *
     * @return the data array
     */
    public static String[][] getData() {
        return data;
    }
}
