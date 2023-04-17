package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class HotelEditFrame extends JFrame{

    private String [] labelNames = {"Kategorie:", "Zimmer:", "Betten:"};

    private JTextField[] textFields = new JTextField[3];

    private  JToolBar toolbar;

    private String[] row;

    private JTable table;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JScrollBar scrollBar;

    DefaultTableModel model;

    public HotelEditFrame(String windowName)
    {
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

        for (int i = 0; i < 3; i++){
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
        Object[] column = {"Kategorie","Zimmer","Betten"};
        model.setColumnIdentifiers(column);
        table.setModel(model);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(240,50,300,300);
        add(scrollPane);

        row = new String[3];

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
                model.addRow(row);

                textFields[0].setText("");
                textFields[1].setText("");
                textFields[2].setText("");
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
            }
        });
        updateButton.setBounds(60,240,100,20);
        add(updateButton);
    }
}
