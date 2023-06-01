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

public class HotelEditFrame extends JFrame{

    private JToolBar toolbar;
    private CustomTable customTable;
    private JButton addButton;
    private JButton deleteButton;

    private String fileName = "data/hotelData.txt";

    private String[] columnNames = {"Name", "Kategorie", "Zimmer", "Betten",
            "BesiterIn", "Kontakt", "Strasse/HNr",
            "Ort", "PLZ", "Telefonnummer", "Familien", "Hunde", "Spa", "Fitness"};

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
                    // Remove the selected row from the table model
                    customTable.getTableModel().removeRow(selectedRow);
                    // Save the updated data to the file
                    customTable.getTableModel().saveData();
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
                for (int i = 0; i < columnNames.length; i++) {
                    String input = JOptionPane.showInputDialog("Enter value for " + columnNames[i]);
                    rowData[i] = input;
                }
                // Add the new row to the table
                customTable.getTableModel().addRow(rowData);
                // Save the updated data to the file
                customTable.getTableModel().saveData();
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

    private void initCustomTable() {
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

                String [][] tableData = customTable.getTableModel().getData();
                if (row >= 0 && row < tableData.length && column >= 0 && column < tableData[row].length) {
                    String newValue = tableData[row][column];
                    editRow(fileName, row, column, newValue);
                }
            }
        });

        add(customTable);
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

    private ArrayList<String[]> fetchData(String fileName) {
        ArrayList<String[]> dataList = new ArrayList<>();

        try (FileReader fileReader = new FileReader(fileName);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");

                // Skip invalid lines or lines with insufficient values
                if (values.length != 14) {
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
