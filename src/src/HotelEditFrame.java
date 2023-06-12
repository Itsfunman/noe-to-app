package src;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.util.ArrayList;

public class HotelEditFrame extends JFrame {

    private JToolBar toolbar;
    private CustomTable customTable;
    private JButton addButton;
    private JButton deleteButton;

   // private String fileName = "data/hotelData.txt";

    private String fileName = "data/oldhotels.txt";

    private String[] columnNames = {"ID", "Kategorie", "Name", "BesitzerIn", "Kontakt", "Adresse",
            "Stadt", "PLZ", "Telefonnummer", "Anzahl Zimmer", "Anzahl Betten"};

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



    private void initAddButton() {
        addButton = new JButton("ADD HOTEL");
        addButton.setBounds(335, 410, 130, 20);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] rowData = new String[columnNames.length];
                // Open a dialog box to get input from the user for each column
                JPanel inputPanel = new JPanel();
                inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

                // Create checkboxes for GDPR confirmation
                JCheckBox gdprCheckBox = new JCheckBox("I confirm that I have processed the data according to GDPR");

                // Add checkboxes to the input panel
                inputPanel.add(gdprCheckBox);

                // Show the input dialog
                int result = JOptionPane.showConfirmDialog(null, inputPanel, "Enter Hotel Details", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION && gdprCheckBox.isSelected()) {
                    // User confirmed GDPR processing, proceed with adding the hotel
                    for (int i = 0; i < columnNames.length; i++) {
                        String input = JOptionPane.showInputDialog("Enter value for " + columnNames[i]);
                        rowData[i] = input;
                    }
                    // Add the new row to the table
                    customTable.getTableModel().addRow(rowData);
                    // Save the updated data to the file
                    customTable.getTableModel().saveData();
                } else {
                    JOptionPane.showMessageDialog(null, "GDPR confirmation is required to add a hotel.");
                }
            }
        });

        addButton.setVisible(true);
        add(addButton);
    }



    private void initToolbar() {
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




    private void editRow(String fileName, int rowIndex, int columnIndex, String newValue) {
        ArrayList<String[]> data = fetchData(fileName);

        if (rowIndex >= 0 && rowIndex < data.size() && columnIndex >= 0 && columnIndex < data.get(rowIndex).length) {
            data.get(rowIndex)[columnIndex] = newValue;
            saveData(fileName, data);
        } else {
            System.out.println("Invalid row index or column index.");
        }
    }

    private void saveData(String fileName, ArrayList<String[]> data) {

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

    private void initCustomTable() {
        ArrayList<String[]> dataList = fetchData(fileName);
        int rowCount = dataList.size();
        int columnCount = columnNames.length;

        String[][] data = new String[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            data[i] = dataList.get(i);
        }

        CustomTableModel customTableModel = new CustomTableModel(data, columnNames);
        customTable = new CustomTable(customTableModel);
        customTable.setBounds(10, 50, 770, 350);

        customTable.getTable().getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // ...
            }
        });

        add(customTable);
    }

    private ArrayList<String[]> fetchData(String fileName) {
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
