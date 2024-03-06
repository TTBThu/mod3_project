package com.myproject.dao;

import com.myproject.model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeDAO {
    private Connection connection;

    public EmployeeDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Employee> getEmployees(int offset) {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM Employee ORDER BY Emp_Name LIMIT 10 OFFSET ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, offset);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Employee employee = mapResultSetToEmployee(resultSet);
                    employees.add(employee);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public List<Employee> searchEmployees(String keyword) {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM Employee WHERE Emp_Id LIKE ? OR Emp_Name LIKE ? ORDER BY Emp_Name LIMIT 10";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + keyword + "%");
            preparedStatement.setString(2, "%" + keyword + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Employee employee = mapResultSetToEmployee(resultSet);
                    employees.add(employee);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public boolean updateEmployeeStatus(String empId, byte empStatus) {
        String updateQuery = "UPDATE Employee SET Emp_Status = ? WHERE Emp_Id = ?";
        String blockAccountQuery = "UPDATE Account SET Acc_Status = 'Block' WHERE Emp_Id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
             PreparedStatement blockAccountStatement = connection.prepareStatement(blockAccountQuery)) {
            preparedStatement.setByte(1, empStatus);
            preparedStatement.setString(2, empId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                if (empStatus != 0) { // If employee status is not Active, block the account
                    blockAccountStatement.setString(1, empId);
                    blockAccountStatement.executeUpdate();
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addEmployee(Employee employee) {
        String insertQuery = "INSERT INTO Employee (Emp_Id, Emp_Name, Birth_Of_Date, Email, Phone, Address, Emp_Status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, employee.getEmpId());
            preparedStatement.setString(2, employee.getEmpName());
            preparedStatement.setDate(3, new java.sql.Date(employee.getBirthDate().getTime()));
            preparedStatement.setString(4, employee.getEmail());
            preparedStatement.setString(5, employee.getPhone());
            preparedStatement.setString(6, employee.getAddress());
            preparedStatement.setByte(7, employee.getEmpStatus());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isEmployeeExists(String empId) {
        String query = "SELECT COUNT(*) AS count FROM Employee WHERE Emp_Id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, empId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateEmployee(Employee employee) {
        String updateQuery = "UPDATE Employee SET Emp_Name = ?, Birth_Of_Date = ?, Email = ?, Phone = ?, Address = ? WHERE Emp_Id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, employee.getEmpName());
            preparedStatement.setDate(2, new java.sql.Date(employee.getBirthDate().getTime()));
            preparedStatement.setString(3, employee.getEmail());
            preparedStatement.setString(4, employee.getPhone());
            preparedStatement.setString(5, employee.getAddress());
            preparedStatement.setString(6, employee.getEmpId());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Employee mapResultSetToEmployee(ResultSet resultSet) throws SQLException {
        String empId = resultSet.getString("Emp_Id");
        String empName = resultSet.getString("Emp_Name");
        Date birthDate = resultSet.getDate("Birth_Of_Date");
        String email = resultSet.getString("Email");
        String phone = resultSet.getString("Phone");
        String address = resultSet.getString("Address");
        byte empStatus = resultSet.getByte("Emp_Status");
        return new Employee(empId, empName, birthDate, email, phone, address, empStatus);
    }
}
