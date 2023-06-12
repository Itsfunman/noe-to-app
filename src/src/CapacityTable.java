package src;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CapacityTable extends JScrollPane {

    private JTable table;
    private TableModel tableModel;
    private int totalCount;

    private String[] columnNames = {"Kategorie", "Betriebe", "Zimmer", "Betten", "Ø Zimmer", "Ø Betten"};
    //Enter hotel information here
    Object[][] data = new Object[6][6];


    public CapacityTable() {

        refillData();

        setLayout(new ScrollPaneLayout());

        setViewportView(table);

    }

    private void refillData() {
        // clear the data array
        for (int i = 1; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = 0;
            }
        }

        fillData();

        tableModel = new TableModel(data, columnNames);
        table = new JTable(tableModel);

        setViewportView(table);
    }

    private void fillData() {

        // fill the data array with new data
        for (int i = 0; i < data.length - 1; i++) {
            data[i][0] = 5 - i;

            int[] betriebInfo = getBetriebInfo(5 - i);
            data[i][1] = betriebInfo[0];
            data[i][2] = betriebInfo[1];
            data[i][3] = betriebInfo[2];
            data[i][4] = betriebInfo[3];
            data[i][5] = betriebInfo[4];
        }

        data[5][0] = "Total";

        for (int i = 1; i < 4; i++) {
            int count = 0;
            for (int k = 0; k < data.length - 1; k++) {
                count += (Integer) data[k][i];
                totalCount++;
            }
            data[5][i] = count;
        }

        if ((Integer) data[5][1] > 0) {
            data[5][4] = (Integer) data[5][2] / (Integer) data[5][1];
            data[5][5] = (Integer) data[5][3] / (Integer) data[5][1];
        }


    }

    private int[] getBetriebInfo(int kat) {
        int[] betriebInfo = new int[5];
        for (int i = 0; i < betriebInfo.length; i++) {
            betriebInfo[i] = 0;
        }

        Hotel.hotels.clear();
        loadHotels();

        for (int i = 0; i < Hotel.hotels.size(); i++) {
            if (kat == Hotel.hotels.get(i).getCategory()) {
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


    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public TableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(TableModel tableModel) {
        this.tableModel = tableModel;
    }

    private  void loadHotels() {
        try {
            File file = new File("data/oldhotels.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;
            while ((st = br.readLine()) != null) {
                String[] hotelData = st.split(",");

                if (hotelData.length == 14) {
                    Hotel hotel = new Hotel(hotelData[0], hotelData[1], hotelData[2], hotelData[3], hotelData[4],
                            hotelData[5], hotelData[6], hotelData[7], hotelData[8], hotelData[9], hotelData[10],
                            hotelData[11], hotelData[12], hotelData[13]);
                    Hotel.hotels.add(hotel);
                } else {
                    // Handle the case where the hotelData array doesn't have enough elements
                    System.out.println("Invalid hotel data: " + st);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
