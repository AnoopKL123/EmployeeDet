package data;

import businessLogic.Employee;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.System.exit;

public class Database {
    public static Connection conn = null;
    private static String CONN_STRING = "jdbc:Oracle:thin:@//192.168.0.103:1521/XEPDB1";
    private static String USERNAME = "SYSTEM";
    private static String PASSWORD = "raoanoop";

    public static void getConnection() {
        if (conn != null) {
            return;
        } else {
            try {
                conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
                exit(-1);
            }
        }
    }

    public static ArrayList<Employee> getAllEmployeeDet() {
        getConnection();
        ArrayList<Employee> empList = new ArrayList<>();
        final String select = "select employeeId, employeename, employeeAdd, employeePhNo from employee order by employeeId";
        try {
            PreparedStatement stmt = conn.prepareStatement(select);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int empId = rs.getInt("employeeId");
                String name = rs.getString("employeename");
                String address = rs.getString("employeeAdd");
                String phoneNo = rs.getString("employeePhNo");
                empList.add(new Employee(empId, name, address, phoneNo));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "LOADING FAILED","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
        }
        conn = null;
        return empList;
    }

    public static void getEmpId(Map<Integer, Integer> empIdMap) {
        getConnection();
        final String select = "select employeeId from employee order by employeeId";
        try {
            PreparedStatement stmt = conn.prepareStatement(select);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int empId = rs.getInt("employeeId");
                empIdMap.put(empId, empId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn = null;
    }

    public static void updateEmployee(int id, String name, String address, String phNo) {
        final String update = "update employee set employeename = '" + name + "', employeeAdd = '" + address + "', " +
                "employeePhNo = '" + phNo + "' where employeeId = " + id;
        getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(update);
            stmt.executeQuery();
            JOptionPane.showMessageDialog(null, "SUCCESSFULL", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn = null;
    }

    public static void InsertEmployee(int id, String name, String address, String phNo) {
        final String insert = "insert into employee(employeeId, employeename, employeeAdd, employeePhNo) " +
                "values (" + id + ", '" + name + "', '" + address + "', '" + phNo + "')";
        getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(insert);
            stmt.executeQuery();
            JOptionPane.showMessageDialog(null, "SUCCESSFULL", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "FAILED","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
        }
        conn = null;
    }

    public static void deleteEmployee(int id) {
        final String delete = "delete from employee where employeeId = " + id;
        getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(delete);
            stmt.executeQuery();
            JOptionPane.showMessageDialog(null, "DELETION SUCCESSFULL", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "DELETION FAILED","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
        }
        conn = null;
    }

    public static void bulkInsert(List<Employee> empList) {
        getConnection();
        List<String> insertQryList = new ArrayList<>();
        for (Employee emp : empList) {
            String insertSQL = "insert into employee (employeeId, employeeName, employeeAdd, employeePhNo) values " +
                    "(" + emp.getEmpId() + ", '" + emp.getName() + "', '" + emp.getEmpAdd() + "', '" + emp.getEmpPhNo() + "')";
            insertQryList.add(insertSQL);
        }

        try {
            int count = 0;
            for (String sql : insertQryList) {
                count++;
                if (!(count > 200)) {
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.executeQuery();
                } else {
                    count = 1;
                    conn = null;
                    getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.executeQuery();
                }
            }
            JOptionPane.showMessageDialog(null, "UPLOAD SUCCESSFULL", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "UPLOAD FAILED","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
        }
        conn = null;
    }
}
