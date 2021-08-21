package ui;

import data.Database;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateForm {
    private JTextField empName;
    private JTextField empAdd;
    private JTextField empPhno;
    private JPanel updatePanel;
    private JButton updateBtn;
    private JButton clearButton;
    private JTextField txtEmpId;
    private int empId;
    private String emplName;
    private String empAddress;
    private String empphno;

    public UpdateForm() {
        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!emplName.isEmpty()) {
                    int id = Integer.parseInt(txtEmpId.getText());
                    String name = empName.getText();
                    String address = empAdd.getText();
                    String phNo = empPhno.getText();
                    Database.updateEmployee(id, name, address, phNo);
                } else {
                    String name = empName.getText();
                    String address = empAdd.getText();
                    String phNo = empPhno.getText();
                    Database.InsertEmployee(empId, name, address, phNo);
                }
            }
        });
    }

    public JPanel getupdatePane() {
        return updatePanel;
    }

    public void getUpdateDet(int id, String name, String address, String phNo) {
        empId = id;
        emplName = name;
        empAddress = address;
        empphno = phNo;
        txtEmpId.setText(String.valueOf(id));
        empName.setText(name);
        empAdd.setText(address);
        empPhno.setText(phNo);
    }

    public void createGUI() {
        JPanel panel = getupdatePane();
        JFrame frame = new JFrame();
        frame.setContentPane(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        ImageIcon logo = new ImageIcon("res/Developer.png");
        frame.setIconImage(logo.getImage());
    }
}
