package base;



import objectClasses.Occupancy;
import sqlStuff.DBConnection;
import sqlStuff.OccupancyDAO;
import tableClasses.CustomTable;
import tableClasses.CustomTableModel;
import objectClasses.Hotel;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.io.*;
import java.util.List;


/**
 * Class used for editing occupancy data
 */
public class OccupancyEditFrame extends JFrame {

    private JToolBar toolbar;
    private CustomTable customTable;
    private JComboBox hotelChoice;
    private HashMap<String, Hotel> hotelMap;
    private CustomTableModel customTableModel;
    //These values are initially set to -1 since no hotelID or partpos can have this value
    private int hotelID = -1;

    private String[] columnNames = new String[]{"YEAR", "MONTH", "USED ROOMS", "USED BEDS"};

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

                String selectedHotelName = (String) hotelChoice.getSelectedItem();
                Hotel selectedHotel = hotelMap.get(selectedHotelName);
                hotelID = selectedHotel.getHotelID();


                InitHotelOccupancyTable(hotelID);

            }
        });
    }


    /**
     * Initializes the occupancy data based on the user selection
     * @param hotelID
     */
    private void InitHotelOccupancyTable(int hotelID) {

        // Remove the previous table, if it exists
        if (customTable != null) {
            remove(customTable);
        }

        List <Occupancy> dataList = OccupancyDAO.fetchOccupancyBasedOnField(hotelID);
        int rowCount = dataList.size();
        int columnCount = columnNames.length;

        Object[][] data = new Object[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            Occupancy occupancy = dataList.get(i); // Use dataList instead of hotels
            data[i] = occupancy.toObjectArray();
        }

        customTableModel = new CustomTableModel(data, columnNames);
        customTable = new CustomTable(customTableModel);
        customTable.setBounds(10, 50, 770, 350);
        customTable.getTable().getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                //Gets cell
                int row = e.getFirstRow();
                int column = e.getColumn();
                //Checks whether selection is valid
                if (row >= 0 && (column == 2 || column == 3)) {
                    //Implements check box
                    JPanel inputPanel = new JPanel();
                    inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

                    JCheckBox gdprCheckBox = new JCheckBox("I have taken note of the GDPR");

                    inputPanel.add(gdprCheckBox);

                    int result = JOptionPane.showConfirmDialog(null, inputPanel, "Input", JOptionPane.OK_CANCEL_OPTION);

                    //If ok is selected data gets saved to file and database
                    if (result == JOptionPane.OK_OPTION && gdprCheckBox.isSelected()) {

                        Object [] changedData = customTableModel.getData()[row]; //Gets String with changed data
                        updateOccupancyInDB(changedData); //Saves data to database

                    }

                }
            }
        });

        add(customTable);

        customTableModel = new CustomTableModel(data, columnNames);
        customTable.updateData(data);

        revalidate();
        repaint();
    }

    /**
     * Updates hotel in DB
     * @param rowData
     */
    public void updateOccupancyInDB(Object[] rowData) {

        // Update the hotel in the database using a PreparedStatement
        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement pst = connection.prepareStatement("UPDATE dbo.occupancy SET usedRooms = ?, usedBeds = ? WHERE hotelid = ? AND year = ? AND month = ?");

            pst.setInt(1, Integer.parseInt(rowData[2].toString()));//RoomNumber
            pst.setInt(2, Integer.parseInt(rowData[3].toString()));//BedNumber

            pst.setInt(3, hotelID);//ID
            pst.setInt(4, Integer.parseInt(rowData[0].toString()));//Year
            pst.setInt(5, Integer.parseInt(rowData[1].toString()));//Month

            pst.executeUpdate();
            pst.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
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


}



