package businessLogic;

import data.Database;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;

public class Employee {
    private int empId;
    private String name;
    private String empAdd;
    private String empPhNo;

    public  Employee(int id, String n, String address, String phNo) {
        empId = id;
        name = n;
        empAdd = address;
        empPhNo = phNo;
    }

    public int getEmpId() {
        return empId;
    }

    public String getName() {
        return name;
    }

    public String getEmpAdd() {
        return empAdd;
    }

    public String getEmpPhNo() {
        return empPhNo;
    }

    public static ArrayList<Employee> getAllEmployees() {
        return Database.getAllEmployeeDet();
    }
}
