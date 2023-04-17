package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class HotelEdit extends JFrame{

    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;

    private  JToolBar toolbar;

    private JTable table1;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JScrollBar scrollBar1;

    DefaultTableModel model;
    private JFrame frame;

//    public static void main(String[] args) {
////        EventQueue.invokeLater(new Runnable() {
////            @Override
////            public void run() {
////                try {
////                    HotelEdit window = new HotelEdit();
////                    window.frame.setVisible(true);
////                }catch (Exception e)
////                {
////                    e.printStackTrace();
////                }
////            }
////        });
//    }


    public  HotelEdit(String windoName)
    {
        initialize(windoName);
    }

    private void initialize(String windowName)
    {
        frame = new JFrame(windowName);
        frame.setBounds(100,100,700,440);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setVisible(true);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(176,196,222));
        panel.setBounds(0,0,700,410);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        JLabel lblNmae = new JLabel("Kategorie:");
        lblNmae.setBounds(21,83,46,14);
        panel.add(lblNmae);

        JLabel lblNmae2 = new JLabel("Zimmer:");
        lblNmae2.setBounds(21,105,46,14);
        panel.add(lblNmae2);

        JLabel lblNmae3 = new JLabel("Betten:");
        lblNmae3.setBounds(21,127,46,14);
        panel.add(lblNmae3);

        JLabel lblNmae4 = new JLabel("Betriebe:");
        lblNmae4.setBounds(21,148,46,14);
        panel.add(lblNmae4);

        textField1  = new JTextField();
        textField1.setBounds(67,81,132,17);
        panel.add(textField1);
        textField1.setColumns(10);

        textField2 = new JTextField();
        textField2.setColumns(10);
        textField2.setBounds(67,100,132,17);
        panel.add(textField2);

        textField3 = new JTextField();
        textField3.setColumns(10);
        textField3.setBounds(67,124,132,17);
        panel.add(textField3);

        textField4 = new JTextField();
        textField4.setColumns(10);
        textField4.setBounds(67,145,132,17);
        panel.add(textField4);

        scrollBar1 = new JScrollBar();
        panel.add(scrollBar1);

        table1 = new JTable();
        table1.setBackground(new Color(176,196,222));
        table1.setBounds(240,50,300,300);
        model = new DefaultTableModel();
        Object[] column = {"Kategorie","Zimmer","Betten","Betriebe"};
        Object[] row = new Object[4];
        model.setColumnIdentifiers(column);
        table1.setModel(model);
        panel.add(table1);


        JButton kat = new JButton("Kategorie");
        kat.setBounds(240,30,77,20);
        panel.add(kat);

        JButton zimmer = new JButton("Zimmer");
        zimmer.setBounds(315,30,77,20);
        panel.add(zimmer);

        JButton betten = new JButton("Bettem");
        betten.setBounds(390,30,77,20);
        panel.add(betten);

        JButton betriebe = new JButton("Betriebe");
        betriebe.setBounds(465,30,77,20);
        panel.add(betriebe);

        addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                row[0] = textField1.getText();
                row[1] = textField2.getText();
                row[2] = textField3.getText();
                row[3] = textField4.getText();
                model.addRow(row);

                textField1.setText("");
                textField2.setText("");
                textField3.setText("");
                textField4.setText("");
            }
        });
        addButton.setBounds(21,180,70,23);
        panel.add(addButton);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table1.getSelectedRow();
                model.removeRow(selectedRow);
            }
        });
        deleteButton.setBounds(21,210,70,23);
        panel.add(deleteButton);

        updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table1.getSelectedRow();
                model.setValueAt(textField1.getText(),selectedRow,0);
                model.setValueAt(textField2.getText(),selectedRow,1);
                model.setValueAt(textField3.getText(),selectedRow,2);
                model.setValueAt(textField4.getText(),selectedRow,3);

            }
        });
        updateButton.setBounds(21,240,70,23);
        panel.add(updateButton);
        InitToolbar();

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

}
