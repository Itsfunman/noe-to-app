package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class HotelEditFrame extends JFrame{

    private String [] labelNames = {"Name:", "Kategorie:", "Zimmer:", "Betten:"};

    private JTextField[] textFields = new JTextField[4];

    private  JToolBar toolbar;

    private String[] row;

    private JTable table;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JScrollBar scrollBar;

    DefaultTableModel model;

    public HotelEditFrame(String windowName) throws IOException {
        super(windowName);

        setSize(800,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        setVisible(true);
        setLayout(null);
        setLocationRelativeTo(null);

        InitToolbar();
        InitEntryFields();
        InitTable();

        InitButtons();
        loadHotels();


    }

    private void InitToolbar() {
        toolbar = new Toolbar(this);
        toolbar.setVisible(true);
        toolbar.setLocation(0,0);

        add(toolbar);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                toolbar.setSize(getWidth(), 30);

            }
        });
    }

    private void InitEntryFields(){

        for (int i = 0; i < labelNames.length; i++){
            JLabel lblName = new JLabel(labelNames[i]);
            lblName.setBounds(21, 83 + (22 * i) , 100, 20);
            add(lblName);

            textFields[i] = new JTextField();
            textFields[i].setBounds(125, 83 + (22 * i) , 100, 20);
            add(textFields[i]);
            textFields[i].setColumns(10);
        }

    }

    private void InitTable(){

        table = new JTable();
        model = new DefaultTableModel();
        Object[] column = {"Name", "Kategorie","Zimmer","Betten"};
        model.setColumnIdentifiers(column);
        table.setModel(model);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(240,50,300,300);
        add(scrollPane);

        row = new String[4];

    }

    private void InitButtons()

    {

        InitAddButton();
        InitDeleteButton();
        InitUpdateButton();

    }

    private void InitAddButton(){

        addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                row[0] = textFields[0].getText();
                row[1] = textFields[1].getText();
                row[2] = textFields[2].getText();
                row[3] = textFields[3].getText();
                model.addRow(row);

                Hotel hotel = new Hotel(row[0], row[1], row[2], row[3]);
                Hotel.hotels.add(hotel);
                hotel.addToFile(hotel);

                textFields[0].setText("");
                textFields[1].setText("");
                textFields[2].setText("");
                textFields[3].setText("");

            }
        });

        addButton.setBounds(60,180,100,20);
        add(addButton);
    }

    private void InitDeleteButton(){
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                model.removeRow(selectedRow);

                // Remove the hotel from the hotelData.txt file
                try {
                    File file = new File("data/hotelData.txt");
                    List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
                    Hotel hotel = (Hotel) Hotel.hotels.get(selectedRow);
                    String lineToRemove = hotel.toStringSimple();
                    lines.remove(lineToRemove);
                    Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                Hotel.hotels.remove(selectedRow);

            }
        });

        deleteButton.setBounds(60,210,100,20);
        add(deleteButton);
    }


    private void InitUpdateButton(){
        updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();

                model.setValueAt(textFields[0].getText(),selectedRow,0);
                model.setValueAt(textFields[1].getText(),selectedRow,1);
                model.setValueAt(textFields[2].getText(),selectedRow,2);
                model.setValueAt(textFields[3].getText(),selectedRow,3);

                Hotel hotel = (Hotel) Hotel.hotels.get(selectedRow);
                hotel.setHotelName((textFields[0].getText()));
                hotel.setStars(Integer.parseInt((textFields[1].getText())));
                hotel.setRoomNumber(Integer.parseInt((textFields[2].getText())));
                hotel.setBedNumber(Integer.parseInt((textFields[3].getText())));

                // Update the hotel information in the hotelData.txt file
                boolean caughtException = false;
                while (!caughtException){
                    try {
                        File file = new File("data/hotelData.txt");
                        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
                        String lineToUpdate = hotel.toStringSimple();
                        String newLine = String.format("%s,%d,%d,%d", hotel.getHotelName(), hotel.getStars(), hotel.getRoomNumber(), hotel.getBedNumber());
                        lines.set(selectedRow, newLine);
                        Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
                        caughtException = true;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });

        updateButton.setBounds(60,240,100,20);
        add(updateButton);

    }

    private void loadHotels() {
        try {
            File file = new File("data/hotelData.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;
            while ((st = br.readLine()) != null) {

                String[] hotelData = st.split(",");
                Hotel hotel = new Hotel(hotelData[0], hotelData[1], hotelData[2], hotelData[3]);

                //Must be changed in the future to check for more factors
                boolean hotelExists = false;
                for (Hotel h : Hotel.hotels){
                    if (h.getHotelName().equals(hotel.getHotelName())){
                        hotelExists = true;
                        break;
                    }
                }

                if (!hotelExists){
                    Hotel.hotels.add(hotel);
                }

                row[0] = hotelData[0];
                row[1] = hotelData[1];
                row[2] = hotelData[2];
                row[3] = hotelData[3];
                model.addRow(row);

            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
