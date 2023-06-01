package src;

import javax.swing.*;

public class HotelEditFrame_OLD extends JFrame{
/*
    private String [] labelNames = {"Name:", "Kategorie:", "Zimmer:", "Betten:",
                                    "BesiterIn:", "Kontakt:", "Strasse/HNr:",
                                    "Ort:", "PLZ:", "Telefonnummer:"};

    private JTextField[] textFields = new JTextField[labelNames.length];

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
        Object[] column = {"Name", "Kategorie", "Zimmer", "Betten",
                "BesiterIn", "Kontakt", "Strasse/HNr",
                "Ort", "PLZ", "Telefonnummer"};
        model.setColumnIdentifiers(column);
        table.setModel(model);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(240,50,520,300);
        add(scrollPane);

        row = new String[4];

    }

    private void InitButtons()

    {

        InitAddButton();
        InitDeleteButton();
        InitUpdateButton();

    }

    // NEEDS TO BE UPDATED --> ACCESS hotel.toString() and make a String array via .split(",") and add array to db
    // order of values would need to be changed/checked
    public void addHotelToDB() throws SQLException {
        PreparedStatement pst = null;
        Connection connection = DBConnection.getConnection();

        System.out.println(connection);
        pst = connection.prepareStatement("insert into dbo.hotel (hotelname,kategorie,roomNumber,bedNumber) values (?,?,?,?)");
        pst.setString(1, textFields[0].getText());
        pst.setString(2, textFields[1].getText());
        pst.setString(3, textFields[2].getText());
        pst.setString(4, textFields[3].getText());


//        pst.setInt(3, 3);
//        pst.setInt(4, 4);
        pst.executeUpdate();
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
                row[4] = textFields[4].getText();
                row[5] = textFields[5].getText();
                row[6] = textFields[6].getText();
                row[7] = textFields[7].getText();
                row[8] = textFields[8].getText();
                row[9] = textFields[9].getText();
                model.addRow(row);

                Hotel hotel = new Hotel(row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7], row[8], row[9]);
                Hotel.hotels.add(hotel);
                hotel.addToFile(hotel);

                try {
                    addHotelToDB();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                textFields[0].setText("");
                textFields[1].setText("");
                textFields[2].setText("");
                textFields[3].setText("");

            }
        });

        addButton.setBounds(350,360,100,20);
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

        deleteButton.setBounds(350,390,100,20);
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
                hotel.setCategory(Integer.parseInt((textFields[1].getText())));
                hotel.setRoomNumber(Integer.parseInt((textFields[2].getText())));
                hotel.setBedNumber(Integer.parseInt((textFields[3].getText())));

                // Update the hotel information in the hotelData.txt file
                boolean caughtException = false;
                while (!caughtException){
                    try {
                        File file = new File("data/hotelData.txt");
                        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
                        String lineToUpdate = hotel.toStringSimple();
                        String newLine = String.format("%d,%s,%d,%d,%d,%s,%s,%s,%s,%d,%d",
                                hotel.getHotelID(), hotel.getHotelName(), hotel.getCategory(), hotel.getRoomNumber(), hotel.getBedNumber(),
                                hotel.getHotelOwner(), hotel.getHotelContactInformation(), hotel.getAddress(), hotel.getCity(), hotel.getCityCode(),
                                hotel.getPhoneNumber());
                        lines.set(selectedRow, newLine);
                        Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
                        caughtException = true;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });

        updateButton.setBounds(350,420,100,20);
        add(updateButton);

    }

    private void loadHotels() {
        try {
            File file = new File("data/hotelData.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;
            while ((st = br.readLine()) != null) {

                String[] hotelData = st.split(",");
                Hotel hotel = new Hotel(hotelData[0], hotelData[1], hotelData[2], hotelData[3], hotelData[4], hotelData[5],
                        hotelData[6], hotelData[7], hotelData[8], hotelData[9]);

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
                row[4] = hotelData[4];
                row[5] = hotelData[5];
                row[6] = hotelData[6];
                row[7] = hotelData[7];
                row[8] = hotelData[8];
                row[9] = hotelData[9];
                model.addRow(row);

            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
*/
}
