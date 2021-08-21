package ui;

import businessLogic.Employee;
import data.Database;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainUI extends JFrame {
    private JPanel rootPanel;
    private JTable empDetTable;
    private JButton loadBtn;
    private JTabbedPane mainTabbedPane;
    private JTable empTable;
    private JButton readCsvBtn;
    private JButton clearBtn;
    private JButton deleteBtn;
    private JButton addBtn;
    private JTextField pathTxt;
    private JButton uploadButton;
    private JButton loadButton;
    private JButton exportBtn;
    private JTextField exportPath;
    private JButton browseButton;
    private JButton clearButton;
    private TreeMap<Integer, Integer> empIdMap = new TreeMap<>();
    private ArrayList<Employee> empDet = new ArrayList<>();
    private String fileName = "";
    private List<Employee> empList = new ArrayList<>();

    public MainUI() {
        empDetTable.setDefaultEditor(Object.class, null);
        empTable.setDefaultEditor(Object.class, null);

        Database.getEmpId(empIdMap);
        createTable();
        getEmpDet();
        createTableCsv();

        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearTable(empDetTable);
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearTable(empTable);
            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) empDetTable.getModel();
                int[] selectedRowIndex = empDetTable.getSelectedRows();
                if (selectedRowIndex.length > 0) {
                    for (int index : selectedRowIndex) {
                        int id = Integer.parseInt(model.getValueAt(index, 0).toString());
                        Database.deleteEmployee(id);
                    }
                    createTable();
                    getDBData();
                } else {
                    JOptionPane.showMessageDialog(null, "NO DATA SELECTED TO DELETE","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel)empDetTable.getModel();
                int selectedRowIndex = empDetTable.getSelectedRow();

                if (selectedRowIndex >= 0) {
                    int id = Integer.parseInt(model.getValueAt(selectedRowIndex, 0).toString());
                    String name = model.getValueAt(selectedRowIndex, 1).toString();
                    String address = model.getValueAt(selectedRowIndex, 2).toString();
                    String phNo = model.getValueAt(selectedRowIndex, 3).toString();

                    UpdateForm ui = new UpdateForm();
                    ui.createGUI();
                    ui.getUpdateDet(id, name, address, phNo);
                } else {
                    UpdateForm ui = new UpdateForm();
                    ui.createGUI();
                    int id = empIdMap.lastKey();
                    ui.getUpdateDet(id + 1, "", "", "");
                }
            }
        });

        readCsvBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Csv file", "csv");
                fileChooser.addChoosableFileFilter(filter);
                fileChooser.setFileFilter(filter);
                fileChooser.showOpenDialog(null);
                File f = fileChooser.getSelectedFile();
                fileName = f.getAbsolutePath();
                pathTxt.setText(fileName);
            }
        });

        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (empList.size() > 0) {
                    Database.bulkInsert(empList);
                } else {
                    JOptionPane.showMessageDialog(null, "NO DATA TO UPLOAD","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (pathTxt.getText().length() > 0 && !pathTxt.getText().contains("Select")) {
                        BufferedReader br = new BufferedReader(new FileReader(fileName));
                        String line;

                        while ((line = br.readLine()) != null) {
                            String[] value = line.split(",");
                            if (value.length > 4) {
                                JOptionPane.showMessageDialog(null, "CSV FILE DOESN'T MATCH THE FORMAT","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
                                break;
                            }
                            if (value[0].toLowerCase(Locale.ROOT).contains("employee")) {
                                continue;
                            }
                            Employee emp = new Employee(Integer.parseInt(value[0]), value[1].replace("'", " "), value[2], value[3]);
                            DefaultTableModel model = (DefaultTableModel) empTable.getModel();
                            model.addRow(new Object[]{
                                    Integer.parseInt(value[0]),
                                    value[1],
                                    value[2],
                                    value[3]
                            });
                            empList.add(emp);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "SELECT A CSV FILE","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int option = fileChooser.showOpenDialog(null);
                if(option == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();
                    exportPath.setText(file.getAbsolutePath());
                }
            }
        });

        exportBtn.addActionListener(new ActionListener() {
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (exportPath.getText().length() > 0 && !exportPath.getText().contains("Select")) {
                        if (empDetTable.getRowCount() > 0) {
                            FileWriter fw = new FileWriter(exportPath.getText() + "\\EmployeeDetails" + formatter.format(date) + ".csv");
                            BufferedWriter bw = new BufferedWriter(fw);
                            bw.write("EmployeeID, Employee Name, Address, PhoneNo");
                            bw.newLine();
                            for (int i = 0; i < empDetTable.getRowCount(); i++) {
                                for (int j = 0; j < empDetTable.getColumnCount(); j++) {
                                    //write
                                    bw.write(empDetTable.getValueAt(i, j).toString() + ",");
                                }
                                bw.newLine();//record per line
                            }
                            JOptionPane.showMessageDialog(null, "SUCCESSFULLY EXPORTED", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                            bw.close();
                            fw.close();
                        } else {
                            JOptionPane.showMessageDialog(null, "NO DATA TO EXPORT","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "SELECT THE PATH","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "ERROR","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public JTabbedPane getMainTabbedPane() {
        return mainTabbedPane;
    }

    public void clearTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        model.setRowCount(0);
    }

    private void getEmpDet() {
        loadBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == loadBtn) {
                    getDBData();
                    JOptionPane.showMessageDialog(null, "LOAD SUCCESSFULL", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    public void getDBData() {
        empDet = Employee.getAllEmployees();
        DefaultTableModel model = (DefaultTableModel)empDetTable.getModel();
        model.setRowCount(0);
        for (Employee emp : empDet) {
            model.addRow(new Object[] {
                    emp.getEmpId(),
                    emp.getName(),
                    emp.getEmpAdd(),
                    emp.getEmpPhNo()
            });
        }
    }

    public void createTable() {
        empDetTable.setModel(new DefaultTableModel(
                null,
                new String[] {"EmployeeID", "Employee Name", "Address", "PhoneNo"}
        ));
        TableColumnModel columns = empDetTable.getColumnModel();
        columns.getColumn(2).setMinWidth(200);

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        columns.getColumn(0).setCellRenderer(centerRender);
        columns.getColumn(1).setCellRenderer(centerRender);
        columns.getColumn(2).setCellRenderer(centerRender);
        columns.getColumn(3).setCellRenderer(centerRender);
    }

    public void createTableCsv() {
        empTable.setModel(new DefaultTableModel(
                null,
                new String[] {"EmployeeID", "Employee Name", "Address", "PhoneNo"}
        ));
        TableColumnModel columns = empTable.getColumnModel();
        columns.getColumn(2).setMinWidth(200);

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        columns.getColumn(0).setCellRenderer(centerRender);
        columns.getColumn(1).setCellRenderer(centerRender);
        columns.getColumn(2).setCellRenderer(centerRender);
        columns.getColumn(3).setCellRenderer(centerRender);
    }
}
